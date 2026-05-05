#!/bin/sh
set -e
mkdir -p /usr/local/etc/redis
cat > /usr/local/etc/redis/redis.conf << EOF
bind 0.0.0.0
port ${REDIS_PORT}
requirepass ${REDIS_PASSWORD}
user default off
user ${REDIS_USERNAME} on >${REDIS_PASSWORD} ~* +@all
EOF
