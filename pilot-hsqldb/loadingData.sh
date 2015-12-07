#!/bin/bash



MAIN_CLASS=com.nicecredit.pilot.hsqldb.LoadingData

PID=`ps -ef | grep java | grep "org.hsqldb.server.Server" | awk '{print $2}'`

if [[ -z ${PID} ]];then
    logger -s "HSQLDB is not running."
    exit;
fi

PID=`ps -ef | grep java | grep "$MAIN_CLASS" | awk '{print $2}'`

if [ e$PID != "e" ] ; then
    echo "hsqldb.LoadingData is already RUNNING..."
    exit;
fi



if [ $JAVA_HOME ] ; then
        PILOT_JAVA=$JAVA_HOME/bin/java
else
        PILOT_JAVA=java
fi

JAVA_OPTS="$JAVA_OPTS -server -Xms1536m -Xmx2548m"
JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote.port=3331"
JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote.ssl=false"
JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote.authenticate=false"


nohup $PILOT_JAVA -cp ./conf:lib/* $JAVA_OPTS $MAIN_CLASS > ./logs/nohup.out 2>&1 &


sleep 1

if [ "$1" != "nolog" ]
then
tail -f logs/nohup.out
fi

