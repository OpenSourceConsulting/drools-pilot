#!/bin/bash

. ./env.sh

MAIN_CLASS=com.nicecredit.pilot.PilotMain

PID=`ps -ef | grep java | grep "$PILOT_PROCESS_NAME" | awk '{print $2}'`

if [ e$PID != "e" ] ; then
    echo "$PILOT_PROCESS_NAME is already RUNNING..."
    exit;
fi



if [ $JAVA_HOME ] ; then
        PILOT_JAVA=$JAVA_HOME/bin/java
else
        PILOT_JAVA=java
fi

nohup $PILOT_JAVA -cp ./conf:lib/* -server -Xms1024m -Xmx1024m -D$PILOT_PROCESS_NAME $MAIN_CLASS > ./logs/nohup.out 2>&1 &


sleep 1

if [ "$1" != "nolog" ]
then
tail -f logs/nohup.out
fi

