#!/bin/bash
set -e
cd ../../src/
./gradlew application:bootJar --no-daemon
cd ../deploy/dev/