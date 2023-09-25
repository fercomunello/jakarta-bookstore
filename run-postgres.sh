#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"

readonly dkr_network=jakartanet
readonly pg_container=jakarta-bookstore-db
readonly pg_database=jakartadb

docker network create --driver bridge ${dkr_network} &> /dev/null || true

echo "==================== PostgreSQL ===================="

echo "Restarting ${pg_container}...";
docker stop ${pg_container} &> /dev/null || true

docker run -d --rm \
  --name ${pg_container} \
  --network ${dkr_network} \
  -p 5432:5432 \
  -e POSTGRES_DB=${pg_database} \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -v "$(pwd)"/bookstore/src/main/resources/sql/scripts:/scripts:ro \
  postgres:bullseye

until docker exec ${pg_container} pg_isready --host localhost > /dev/null; do
  sleep 0.5
done; echo;

docker exec ${pg_container} pg_isready --version

echo "Run schema creation script:"; echo;
docker exec ${pg_container} psql -U postgres -d ${pg_database} --echo-all --quiet \
  -f /scripts/1-create-schema.sql
echo;

echo "Load initial data:"; echo;
docker exec ${pg_container} psql -e -U postgres -d ${pg_database} --echo-all --quiet \
  -f /scripts/2-insert-data.sql

echo "===================================================="; echo