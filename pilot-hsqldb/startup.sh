#!/bin/bash



PID=`ps -ef | grep java | grep "org.hsqldb.server.Server" | awk '{print $2}'`

if [ e$PID != "e" ] ; then
    echo "HSQLDB is already RUNNING..."
    exit;
fi



if [ $JAVA_HOME ] ; then
        PILOT_JAVA=$JAVA_HOME/bin/java
else
        PILOT_JAVA=java
fi

JAVA_OPTS="$JAVA_OPTS -server -Xms1024m -Xmx1024m"
JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote.port=3332"
JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote.ssl=false"
JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote.authenticate=false"


nohup $PILOT_JAVA -cp lib/hsqldb-2.3.3.jar $JAVA_OPTS org.hsqldb.server.Server --database.0 mem:nicedb --dbname.0 nicedb > ./logs/nohup.out 2>&1 &


sleep 1

if [ "$1" != "nolog" ]
then
tail -f logs/nohup.out
fi

