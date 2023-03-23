#!/bin/bash
set -ex

RUNNING_USER=$(id -u)
NONROOT_UID=$(id -u nonroot)
NONROOT_GID=$(id -g nonroot)

if [ "$RUNNING_USER" = "0" ]; then
  exec /usr/local/bin/gosu $NONROOT_UID:$NONROOT_GID "$@"
fi

exec "$@"
