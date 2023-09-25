#!/usr/bin/env bash

readonly JDBC_DRIVER_VERSION=42.6.0

export JDBC_DRIVER=postgresql
export JDBC_DRIVER_MODULE=org.postgresql
export JDBC_DRIVER_CLASS=org.postgresql.Driver
export JDBC_DRIVER_JAR=${JDBC_DRIVER}-${JDBC_DRIVER_VERSION}.jar
export JDBC_DRIVER_URL=https://jdbc.postgresql.org/download/${JDBC_DRIVER_JAR}
export JDBC_DRIVER_FALLBACK_URL=https://repo1.maven.org/maven2/org/postgresql/\
${JDBC_DRIVER}/${JDBC_DRIVER_VERSION}/${JDBC_DRIVER_JAR}

export DATASOURCE_NAME=PostgresDS
export DATASOURCE_USERNAME=postgres
export DATASOURCE_PASSWORD=postgres
export JDBC_URL=jdbc:postgresql://127.0.0.1:5432/jakartadb

export JDBC_TX_ISOLATION_LEVEL=TRANSACTION_READ_COMMITTED
export JDBC_USE_FAST_FAIL=false
export JDBC_USE_CCM=true

export JDBC_MIN_POOL_SIZE=8
export JDBC_MAX_POOL_SIZE=20
export JDBC_POOL_FAIR=true
export JDBC_POOL_USE_STRICT=false

export JDBC_TRACK_STATEMENTS=NOWARN
export JDBC_STATISTICS_ENABLED=false
export JDBC_SPY=false