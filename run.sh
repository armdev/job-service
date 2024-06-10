#!/usr/bin/env bash

set -e

mvn clean package -U -Dmaven.test.skip=true

docker compose --compatibility up -d --build
docker logs --follow job-service
