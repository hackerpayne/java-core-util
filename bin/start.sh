#!/bin/sh

# 应用程序名
if [ -n "$2" ]; then
    JAR_NAME=$2
else
    JAR_NAME=admin.jar
fi

# 程序目录
APP_HOME=$(cd $(dirname $0); pwd)

# spring启动环境
ENV="prod"

# JVM参数
JVM_OPTS="-Dname=$JAR_NAME  -Duser.timezone=Asia/Shanghai -Xms512m -Xmx1024m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m -XX:+HeapDumpOnOutOfMemoryError -XX:+UseZGC"
#JAVA_OPTS="-ms512m -mx512m -Xmn256m -Djava.awt.headless=true -XX:MaxPermSize=128m"

cd "$APP_HOME"

get_pid() {
    # Find PID by jar name, excluding grep itself
    ps -ef | grep java | grep "$JAR_NAME" | grep -v grep | awk '{print $2}'
}
usage() {
    echo -e "\033[0;31m Usage: \033[0m  \033[0;34m sh  $0  {start|stop|restart|status}  {SpringBootJarName} \033[0m
    \033[0;31m Example: \033[0m
    	  \033[0;33m sh  $0  start test.jar \033[0m"
    exit 1
}

start()
{
    PID=$(get_pid)

    if [ x"$PID" != x"" ]; then
        echo "$JAR_NAME is running...., pid=${PID}"
    else
        nohup java $JVM_OPTS -jar $JAR_NAME > /dev/null 2>&1 &
        # echo $! > $SERVER/run.pid
        echo "Start $JAR_NAME success..."
    fi
}

stop()
{
    echo "Stop $JAR_NAME"

    PID=$(get_pid)

    if [ x"$PID" != x"" ]; then
        # kill `cat $SERVER/run.pid`
        # rm -rf $SERVER/run.pid
        kill -TERM $PID
        echo "$JAR_NAME (pid:$PID) exiting..."
        while [ x"$PID" != x"" ]
        do
            sleep 1
            PID=$(get_pid)
        done
        echo "$JAR_NAME exited."
    else
        echo "$JAR_NAME already stopped."
    fi
}

restart()
{
    stop
    sleep 2
    start
}

status()
{
    PID=$(get_pid)
    if [ x"$PID" != x"" ]; then
        echo "$JAR_NAME is running..., pid is ${PID}"
    else
        echo "$JAR_NAME is not running..."
    fi
}



case $1 in
    start)
    start;;
    stop)
    stop;;
    restart)
    restart;;
    status)
    status;;
    *)
    usage;;


esac
