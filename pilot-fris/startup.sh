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

JAVA_OPTS="$JAVA_OPTS -server -Xms1024m -Xmx1024m"
JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote.port=3333"
JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote.ssl=false"
JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote.authenticate=false"
JAVA_OPTS="$JAVA_OPTS -Dkie.mbeans=enabled"


nohup $PILOT_JAVA -cp ./conf:lib/* $JAVA_OPTS -D$PILOT_PROCESS_NAME $MAIN_CLASS > ./logs/nohup.out 2>&1 &


sleep 1

if [ "$1" != "nolog" ]
then
tail -f logs/nohup.out
fi

