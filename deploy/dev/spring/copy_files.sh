#!/bin/bash
set -e
cp ../../src/application/build/libs/skladinya-application.jar spring/app.jar
rm -rf spring/api/
cp -r ../../docs/ spring/api/