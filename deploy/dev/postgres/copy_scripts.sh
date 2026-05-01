#!/bin/bash
set -e
cp ../../src/persistence-postgres/src/main/resources/postgres-schema.sql ./postgres/scripts/schema.sql
cp ../../src/utils-bdfiller/generatedData/main.sql ./postgres/scripts/fill.sql