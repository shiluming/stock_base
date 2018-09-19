#!/bin/sh
# description: Auto-starts boot
# $API_HOME/bin/boot.sh
#启动命令： boot.sh start
#重启命令： boot.sh restart
#关闭命令： boot.sh stop
#是否运行： boot.sh status

CURRENT_DIR=`dirname $0`
API_HOME=`cd "$CURRENT_DIR/.." >/dev/null; pwd`
# 应用名
Tag="apidocs"
cd $API_HOME
# 要执行的jar包
Jar="$API_HOME/apidocs-1.0.jar"
RETVAL="0"

# See how we were called.
function start()
{
    echo $$ > $API_HOME/api.pid
    nohup $JAVA_HOME/jre/bin/java -Xms512m -Xmx512m \
    -XX:-UseGCOverheadLimit \
    -verbose:gc -Xloggc:jvm-gc.log \
    -Dappliction=$Tag \
    -jar $Jar --spring.config.location=$API_HOME/config > $API_HOME/logs/api_stdout.log 2>&1 &
    echo "$Tag started!"
     tail -f -n 0 $API_HOME/logs/api_stdout.log
}


function stop()
{
    pid=$(ps -ef|grep "$API_HOME/lib"|grep -v 'grep'|awk '{ print $2 }')
    if [ "$pid" != "" ]; then
        echo -n "$Tag ( pid: $pid) is running"
        echo
        echo -n "Shutting down $Tag..."
        echo 
        kill -9 "$pid" > /dev/null 2>&1
    fi
        status

}

function status()
{
    pid=$(ps -ef|grep "$API_HOME/lib"|grep -v 'grep'|awk '{ print $2 }')
    #echo "$pid"
    if [ "$pid" != "" ]; then
        echo "$Tag is running,pid is $pid"
    else
        echo "$Tag is stopped"
    fi
}



function usage()
{
   echo "Usage: $0 {start|stop|restart|status}"
   RETVAL="2"
}

# See how we were called.
RETVAL="0"
case "$1" in
    start)
        start
        ;;
    stop)
        stop
        ;;
    restart)
        stop
        start
        ;;
    reload)
        RETVAL="3"
        ;;
    status)
        status
        ;;
    *)
      usage
      ;;
esac

exit $RETVAL