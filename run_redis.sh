#!/bin/bash

docker compose --file redis-compose.yml --compatibility up -d --build
