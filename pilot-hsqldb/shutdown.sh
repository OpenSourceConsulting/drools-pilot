#!/bin/bash

PID=`ps -ef | grep java | grep "org.hsqldb.server.Server" | awk '{print $2}'`

if [[ -z ${PID} ]];then
    logger -s "HSQLDB is not running."
    exit;
fi



if [ $JAVA_HOME ] ; then
        PILOT_JAVA=$JAVA_HOME/bin/java
else
        PILOT_JAVA=java
fi


$PILOT_JAVA -cp ./conf:lib/* com.nicecredit.pilot.hsqldb.ShutdownDB



