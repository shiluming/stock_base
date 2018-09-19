#!/bin/sh
CURRENT_DIR=`dirname $0`
API_HOME=`cd "$CURRENT_DIR/.." >/dev/null; pwd`
sh $API_HOME/bin/boot.sh restart
