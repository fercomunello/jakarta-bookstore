#!/usr/bin/env bash
set -e
cd $(dirname $0)

set -a
. config/wildfly.properties
set +a

reset=false
hard_reset=false
deploy=false
debug_port=8787

while [ "$#" -gt 0 ]
do
  case "$1" in
    -r|--reset)
      reset=true
      if [[ $2 = "hard" ]]; then
        hard_reset=true
        shift
      fi
      ;;
    -d|--deploy)
      deploy=true
      ;;
    --debug)
      if [[ $2 =~ [1-9] ]]; then
        debug_port=$2
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
  if test ${hard_reset} = true; then
    rm -rf downloads/*
  fi
}

rm_temporary_files() {
  if [ -d "server" ]; then
    echo "=> Clear standalone/tmp folder"
    rm -rf server/standalone/tmp/*

    echo "=> Clear standalone_xml_history/* folder"
    rm -rf server/standalone/configuration/standalone_xml_history/*
  fi
}

rm_non_unix_scripts() {
  echo ""
  echo "=> Remove non-unix scripts from bin/"
  cd server/bin || exit 1
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

  galleon_cached=false
  if [[ -n "$(find . -name 'galleon-*' | head -1)" ]]; then
    if [[ -d "galleon-${GALLEON_VERSION}" ]]; then
      galleon_cached=true
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

  wildfly_cached=false
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
  cd ..

  rm_non_unix_scripts

  echo ""
  echo "=> Create a management user for WildFly: admin|redhat"
  echo ""
  cd server/bin || exit 1
  ./add-user.sh admin redhat --silent

  cd - > /dev/null
}

run_jboss_cli() {
  echo ""
  echo "=> Run ./jboss-cli.sh commands"
  echo ""
  rm -rf server/bin/config/
  mkdir server/bin/config/
  cp -v ../config/*.cli server/bin/config/ || exit 1

  cd server/bin || exit 1
  ./jboss-cli.sh --file="config/setup-sandbox.cli"
  rm -rf config/

  cd - > /dev/null
}

deploy_apps_silent() {
  echo ""
  echo "=> Remove old deployments"
  rm -rf server/standalone/deployments/*.war.failed
  rm -rf server/standalone/deployments/*.war.deployed
  rm -rf server/standalone/deployments/*.war

  echo "=> Build .war artifacts..."
  cd ../../bookstore || exit 1
  mvn -T 1C clean package -DskipTests
  cd - > /dev/null

  echo "=> Copy .war files to deployment folder"
  cp -v ../../bookstore/target/*.war server/standalone/deployments/ || exit 1
}

start_wildfly() {
  echo ""
  echo "=> Start WildFly..."
  echo ""

  export JAVA_OPTS="-Xms512M -Xmx4G -XX:MetaspaceSize=96M -XX:MaxMetaspaceSize=256m -Djava.net.preferIPv4Stack=true"
  export JAVA_OPTS="$JAVA_OPTS -Djboss.modules.system.pkgs=org.jboss.byteman -Djava.awt.headless=true"
  export JAVA_OPTS="$JAVA_OPTS -agentlib:jdwp=transport=dt_socket,address=${debug_port},server=y,suspend=n"

  ./server/bin/standalone.sh -c $WILDFLY_CONFIG
}

main() {
  if [ ! -d "sandbox" ]; then
    mkdir sandbox
  fi
  cd sandbox

  if test ${reset} = true; then
    reset_environment
  fi

  rm_temporary_files

  if [ ! -d "server" ]; then
    provision_wildfly
    run_jboss_cli
  fi

  if test ${deploy} = true; then
    deploy_apps_silent
  fi

  start_wildfly
  exit 0
}

main