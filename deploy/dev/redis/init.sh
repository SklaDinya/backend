#!/bin/sh
set -e
export CONFIG_HOLDER=/usr/local/etc/redis
export CONFIG_FILE=${CONFIG_HOLDER}/redis.conf
mkdir -p ${CONFIG_HOLDER}
cat > ${CONFIG_FILE} << EOF
bind 0.0.0.0
port ${REDIS_PORT}
requirepass ${REDIS_PASSWORD}
user default off
user ${REDIS_USERNAME} on >${REDIS_PASSWORD} ~* +@all
EOF
redis-server ${CONFIG_FILE}
