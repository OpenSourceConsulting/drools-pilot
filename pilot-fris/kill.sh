#!/bin/sh

. ./env.sh

PID=`ps -ef | grep java | grep "$PILOT_PROCESS_NAME" | awk '{print $2}'`

if [[ -z ${PID} ]];then
    logger -s "${PILOT_PROCESS_NAME} is not running."
    exit;
fi

ps -ef | grep java | grep "$PILOT_PROCESS_NAME" | awk {'print "kill -9 " $2'} | sh -x
