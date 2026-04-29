#!/bin/bash

set -e

cd "$(dirname "$(readlink -f "$0")")"

# End previous session

docker compose -f docker-compose.yaml down

# Redis - no actions to prepare

# Postgres - build and copy

./postgres/generate_data.sh
./postgres/copy_scripts.sh

# Spring - build and copy

./spring/build_app.sh
./spring/copy_files.sh

# Start

docker compose -f docker-compose.yaml up -d
