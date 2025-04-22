#!/bin/sh
set -e

until pg_isready -h postgres -U admin -d hql_fabric_db; do
  echo "Waiting for postgres to be ready..."
  sleep 2
done

exec java -jar app.jar