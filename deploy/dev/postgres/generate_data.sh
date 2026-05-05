#!/bin/bash
set -e
cd ../../src/
./gradlew utils-bdfiller:bootJar --no-daemon
java -jar utils-bdfiller/build/libs/utils-bdfiller.jar
cd ../deploy/dev/