#!/usr/bin/env bash

set -e
echo "Build Job Service"

mvn clean package -U -Dmaven.test.skip=true
#mvn clean package -U -Dmaven.test.skip=true -Pnative native:compile
