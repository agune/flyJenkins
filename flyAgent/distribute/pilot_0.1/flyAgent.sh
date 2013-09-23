#!/bin/sh

DAEMON_USER=pdc222
JAVA_HOME=/home/pdc222/jdk1.7.0_21
argc=$#
value=2


# resolve links - $0 may be a softlink
PRG="$0"

while [ -h "$PRG" ]; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`/"$link"
  fi
done


# Get standard environment variables
PRGDIR=`dirname "$PRG"`

DAEMON_HOME=$PRGDIR

export FLY_AGENT_HOME=$DAEMON_HOME


PID_FILE=$DAEMON_HOME/agent.pid

CLASSPATH=$DAEMON_HOME/flyAgent-jar-with-dependencies.jar:.

case "$1" in

	start)
	#
	#Start Daemon
	#
	if [ $value -ge $argc ]
	then
		echo "plase command : start [jenkins host] [rsa path] "
        	exit
	fi
	

	jsvc \
	-user $DAEMON_USER \
	-home $JAVA_HOME \
	-wait 10 \
	-pidfile $PID_FILE \
	-errfile "$DAEMON_HOME/log/error.log"\
	-cp $CLASSPATH \
	com.agun.agent.flyAgent $2 $3

	#
	# To get a verbose JVM
	#-verbose \
	#-debug	\
	exit $?
	;;

	stop)
	#
	#Stop
	#
	jsvc \
	-stop \
	-pidfile $PID_FILE \
	com.agun.agent.flyAgent
	exit $?
	;;	
#
	*)
	echo "Usage Agent start/stop"
	exit 1;;
esac
