#!/usr/bin/env bash
set -e
set -m
cd $(dirname $0)

set -a
. wildfly/config/wildfly.properties
set +a

RESET='false'
DEPLOY='false'
DEBUG_PORT=8787
MGMT_PORT=9990
HARD_RESET='false'

deployed_wars=0

while [ "$#" -gt 0 ]
do
  case "$1" in
    -r|--reset)
      RESET='true' && readonly RESET
      if [[ $2 = "hard" ]]; then
        HARD_RESET='true' && readonly HARD_RESET
        shift
      fi
      ;;
    -d|--deploy)
      DEPLOY='true' && readonly DEPLOY
      ;;
    --debug)
      if [[ $2 =~ [1-9] ]]; then
        DEBUG_PORT=$2 && readonly DEBUG_PORT
        shift
      fi
      ;;
    *|-h|--help)
      echo "Usage: ./run.sh [args...]"
      echo "where args include:"
      echo "  -d|--deploy: Build and deploy the .war artifacts on WildFly."
      echo "  --debug: Set the remote debug port. Default port is 8787."
      echo "  --reset|-r: Reset the sandbox environment."
      echo "  --reset|-r hard: Delete all sandbox files and provision a new WildFly instance."
      exit 0
  esac
  shift
done

reset_environment() {
  rm -rf server
  if test ${HARD_RESET} = true; then
    rm -rf downloads/*
  fi
}

rm_temporary_files() {
  if [ -d "server" ]; then
    echo "=> Clear temporary files and previous data"
    rm -rf server/standalone/tmp/
    rm -rf server/standalone/data/
    rm -rf server/bin/tmp/*

    echo "=> Clear standalone_xml_history/* folder"
    rm -rf server/standalone/configuration/standalone_xml_history/*
  fi
}

rm_non_unix_scripts() {
  echo ""
  echo "=> Remove non-unix scripts from bin/"
  cd server/bin
  rm installation-manager.bat installation-manager.ps1
  rm standalone.bat standalone.ps1
  rm standalone.conf.bat standalone.conf.ps1
  rm jboss-cli.bat jboss-cli.ps1
  rm common.bat common.ps1
  rm elytron-tool.bat elytron-tool.ps1
  rm add-user.bat add-user.ps1
  rm jdr.bat jdr.ps1
  cd - > /dev/null
}

provision_wildfly() {
  if [ ! -d "downloads" ]; then
    mkdir downloads
  fi
  cd downloads

  galleon_cached='false'
  if [[ -n "$(find . -name 'galleon-*' | head -1)" ]]; then
    if [[ -d "galleon-${GALLEON_VERSION}" ]]; then
      galleon_cached='true'
    fi
  fi

  if test ${galleon_cached} = false; then
    echo ""
    echo "=> Download and unzip WildFly Galleon packages"
    echo ""
    curl -L -O \
    https://github.com/wildfly/galleon/releases/download/$GALLEON_VERSION/galleon-$GALLEON_VERSION.zip

    unzip galleon-$GALLEON_VERSION.zip
    rm -f galleon-$GALLEON_VERSION.zip
  fi

  wildfly_cached='false'
  if [[ -n "$(find . -name 'wildfly-*' | head -1)" ]]; then
    if [[ -d "wildfly-${WILDFLY_VERSION}" ]]; then
      wildfly_cached=true
    fi
  fi

  if test ${wildfly_cached} = false; then
    rm -rf wildfly-$WILDFLY_VERSION
    echo ""
    echo "=> Provision a custom WildFly instance"
    echo ""
    ./galleon-$GALLEON_VERSION/bin/galleon.sh install wildfly:current#$WILDFLY_VERSION \
     --dir=wildfly-$WILDFLY_VERSION --config=standalone/$WILDFLY_CONFIG --default-configs=standalone/$WILDFLY_CONFIG \
     --verbose --layers=$WILDFLY_LAYERS,$MIDDLEWARE_LAYERS,$JAKARTA_EE_LAYERS,$MICROPROFILE_LAYERS
  fi

  cd ..
  mkdir server && cd server
  cp -v -r ../downloads/wildfly-$WILDFLY_VERSION/* .
  mkdir tmp
  cd ..

  rm_non_unix_scripts

  cd server/bin
  mkdir tmp

  echo ""
  echo "=> Create a management user for WildFly: admin|redhat"
  echo ""
  ./add-user.sh admin redhat --silent

  cd - > /dev/null
}

run_jboss_cli() {
  echo ""
  echo "=> Run ./jboss-cli.sh commands"
  echo ""
  rm -rf server/bin/config/
  mkdir server/bin/config/
  cp -v ../config/*.cli server/bin/config/

  cd server/bin
  sh jboss-cli.sh --file="config/setup-sandbox.cli"
  rm -rf config/

  cd - > /dev/null
}

rm_old_deployments() {
  rm -rf server/standalone/deployments/*.war.failed
  rm -rf server/standalone/deployments/*.war.deployed
  rm -rf server/standalone/deployments/*.war
}

deploy_apps_silent() {
  echo '=> Build .war artifacts...'
  cd ../../bookstore
  mvn -T 1C clean package -DskipTests
  cd - > /dev/null

  echo '=> Copy .war files to deployment folder'
  cp -v ../../bookstore/target/*.war server/standalone/deployments/
  deployed_wars=$(find server/standalone/deployments -maxdepth 1 -type f -name '*.war' | wc -l | tr -d ' ')
}

start_wildfly_async() {
  echo ""
  echo "=> Start WildFly..."
  echo ""

  cd server/bin

  export JAVA_OPTS="-Xms512M -Xmx4G -XX:MetaspaceSize=96M -XX:MaxMetaspaceSize=256m -Djava.net.preferIPv4Stack=true" && \
  export JAVA_OPTS="$JAVA_OPTS -Djboss.modules.system.pkgs=org.jboss.byteman -Djava.awt.headless=true" && \
  export JAVA_OPTS="$JAVA_OPTS -agentlib:jdwp=transport=dt_socket,address=${DEBUG_PORT},server=y,suspend=n" && \
  ./standalone.sh -c $WILDFLY_CONFIG > tmp/jboss_stdout &

  WILDFLY_BACK_PID=$!
  readonly WILDFLY_BACK_PID
  cd - > /dev/null
}

capture_logs_async() {
  cd server/bin
  [ ! -d "tmp" ] && mkdir tmp/

  rm -f tmp/jboss_stdout
  mkfifo tmp/jboss_stdout

  cat tmp/jboss_stdout &

  LOGS_BACK_PID=$!
  readonly LOGS_BACK_PID

  cd - > /dev/null
}

dots() {
  [ "$1" -eq 1 ] && echo .
  [ "$1" -eq 2 ] && echo ..
  [ "$1" -eq 3 ] && echo ...
}

do_sleep() {
  local -r timeout=$1
  local -r text=$2
  local -r wait_for_file=$3
  local dots_count=1
  local slept_ticks=0
  show_progress() {
    if [ "${dots_count}" -eq 4 ]
      then dots_count=1
    fi
    tput el
    printf "\r%s%s" "${text}" "$(dots "${dots_count}")"
    tput cup $(($(tput lines)-2)) 0
    dots_count=$((dots_count + 1))
    sleep .2
  }
  while true; do
    pwd > /dev/null
    if [[ ! -f $wait_for_file ]]; then
      show_progress
      slept_ticks=$(awk "BEGIN {print $slept_ticks+.2; exit}")
      if [[ $slept_ticks > $timeout ]]; then
        break
      fi
    fi
    pwd > /dev/null
    if [[ -f $wait_for_file ]]; then
      slept_ticks=0
      while true; do
        show_progress
        tput el
        slept_ticks=$(awk "BEGIN {print $slept_ticks+.1; exit}")
        if [[ $slept_ticks > $timeout ]]; then
          break 2
        fi
        pwd > /dev/null
        if [[ ! -f $wait_for_file ]]; then
          break 2
        fi
      done
    fi
  done
}

undeploy_apps() {
  echo ''
  cd server/standalone/deployments
  pwd > /dev/null
  if [[ $(find . -maxdepth 1 -type f -name '*.war.deployed' | wc -l | tr -d ' ') -gt 0 ]]; then
    cd - > /dev/null
    cd server/bin

    sh jboss-cli.sh --connect --controller=localhost:${MGMT_PORT} command='undeploy *.war' \
       > tmp/undeploy.out && rm -f tmp/undeploy.out &

    do_sleep 5 'Undeploying' 'tmp/undeploy.out'
  else
    local -r mode=$1
    if [ "${state}" == 'silent' ]
      then echo "No applications to undeploy"
    fi
  fi

  cd - > /dev/null
  rm_old_deployments
  deployed_wars=0
}

redeploy_apps() {
  undeploy_apps 'silent'

  touch mvn.out
  mvn -T 1C package -DskipTests -f ../../bookstore/pom.xml && rm -f mvn.out &
  do_sleep 4 'Building' 'mvn.out'

  cp -v ../../bookstore/target/*.war server/standalone/deployments
  cd server/standalone/deployments

  touch deploy.out
  while true; do
    if [[ $(find . -maxdepth 1 -type f -name '*.war.deployed' | wc -l | tr -d ' ') -gt 0 ]]; then
      rm -f deploy.out
      break
    else
      if [[ $(find . -maxdepth 1 -type f -name '*.war.failed' | wc -l | tr -d ' ') -gt 0 ]]; then
        rm -f deploy.out
        break
      fi
    fi
  done &

  do_sleep 3 'Deploying' 'deploy.out'

  deployed_wars=$(find . -maxdepth 1 -type f -name '*.war.deployed' | wc -l | tr -d ' ')
  cd - > /dev/null
}

stop_wildfly() {
  undeploy_apps 'silent'

  cd server/bin

  sh jboss-cli.sh --connect --controller=localhost:${MGMT_PORT} command=':shutdown' \
    > tmp/shutdown.out && rm -f tmp/shutdown.out &

  do_sleep 5 'Stopping the server' 'tmp/shutdown.out'

  cd - > /dev/null

  kill $LOGS_BACK_PID
  kill $WILDFLY_BACK_PID
}

restart_wildfly() {
  undeploy_apps 'silent'

  cd server/bin

  sh ./jboss-cli.sh --connect --controller=localhost:${MGMT_PORT} command=':shutdown(restart=true)' \
    > tmp/restart.out && rm -f tmp/restart.out &

  do_sleep 5 'Restarting the server' 'tmp/restart.out'

  cd - > /dev/null
}

reload_wildfly() {
  cd server/bin

  sh ./jboss-cli.sh --connect --controller=localhost:${MGMT_PORT} command=':reload' \
    > tmp/reload.out && rm -f tmp/reload.out &

  do_sleep 5 'Reloading the server' 'tmp/reload.out'

  cd - > /dev/null
}

wait_server_startup() {
  sleep 2
  cd server/bin
  sh jboss-cli.sh --connect --controller=localhost:${MGMT_PORT} command=pwn > /dev/null
  cd - > /dev/null
}

print_wildfly_version() {
  cd server/bin

  sh jboss-cli.sh --connect --controller=localhost:${MGMT_PORT} command=version \
      > tmp/version.out && echo $(cat tmp/version.out) && echo '' && rm -f tmp/version.out &

  do_sleep 3 'Connecting' 'tmp/version.out'

  cd - > /dev/null
}

print_help() {
  echo -e "The following commands are available:"
  echo
  echo -e "[\033[1;34mh\033[0m] - Show this help."
  if test ${DEPLOY} = true; then
    echo -e "[\033[1;34md\033[0m] - Deploy all applications."
    echo -e "[\033[1;34mu\033[0m] - Undeploy all applications."
  fi
  echo -e "[\033[1;34ms\033[0m] - Restart application server."
  echo -e "[\033[1;34ml\033[0m] - Reload application server configurations."
  echo -e "[\033[1;34mv\033[0m] - Show application server version and other info."
  echo -e "[\033[1;34mq\033[0m] - Quit the application server and this session."
  echo
}

launch_prompt() {
  echo ''

  local sandbox_dir="$(pwd)"
  while true; do
    local press_to=""
    if test ${DEPLOY} = true; then
      pwd > /dev/null
      if [[ "${deployed_wars}" -gt 0 ]]; then
        press_to="\r\033[KPress \033[1;34m[d]\033[0m to re-deploy"
      else
        press_to="\r\033[KPress \033[1;34m[d]\033[0m to deploy"
      fi
      press_to+=", \033[1;34m[u]\033[0m to undeploy, \033[1;34m[q]\033[0m to quit, \033[1;34m[h]\033[0m for more options."
    else
      press_to+="\r\033[KPress \033[1;34m[q]\033[0m to quit"
      press_to+=", \033[1;34m[s]\033[0m to restart, \033[1;34m[l]\033[0m to reload"
      press_to+=", \033[1;34m[h]\033[0m for more options."
    fi

    tput cup $(tput lines) 0
    echo -e -n $press_to
    tput cup $(($LINES-2)) 0

    read -n 1 -r -s -t 1 reply || true

    if [[ "$reply" != "" ]]; then
      case "$reply" in
         d)
           if test ${DEPLOY} = true; then
             redeploy_apps
           fi
           ;;
         u)
           if test ${DEPLOY} = true; then
             undeploy_apps
           fi
           ;;
         v)
           print_wildfly_version
           ;;
         l)
           reload_wildfly
           ;;
         s)
           clear
           tput cup $(($(tput lines)-2)) 0
           restart_wildfly
           ;;
         q)
           stop_wildfly
           break ;;
         h)
           print_help
           ;;
         *)
           echo -n ""
           ;;
      esac
      reply=""
    fi
  done
}

LINES=0
adjust_window () {
  clear

  LINES=$(tput lines)
  readonly LINES

  tput csr 0 $(($LINES-2))
  tput cup 0 0
}

main() {
  trap '' SIGINT

  adjust_window

  cd wildfly
  if [ ! -d "sandbox" ]; then
    mkdir sandbox
  fi
  cd sandbox

  if test ${RESET} = true; then
    reset_environment
  fi

  rm_temporary_files

  if [ ! -d "server" ]; then
    provision_wildfly
    run_jboss_cli
  fi

  rm_old_deployments
  if test ${DEPLOY} = true; then
    deploy_apps_silent
  fi

  capture_logs_async
  start_wildfly_async
  wait_server_startup
  launch_prompt

  trap SIGINT

  exit 0
}

main