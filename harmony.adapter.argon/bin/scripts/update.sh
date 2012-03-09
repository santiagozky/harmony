#!/bin/bash
#
# Die Logs sind zu finden unter:
#
#  ./phosphorus.wp1/trunk/log/nohup.out
#  ./argon2/app/dist/log/Argon.UI.INFO.log
#  ./argon2/src/Argonv2/Argonv2/installer/nohup.out
#  ./argon2/app/openjms-0.7.7-beta-1/bin/nohup.out
#

# Start ########################################################################
cd /opt
. ./updateEnvironment.sh

argon="false";
adapter="false";

while [ "$#" -gt "0" ]; do
  case $1 in
     -argon) argon="true" ; shift ;;
     -adapter) adapter="true" ; shift ;;
     -h|--help) help="true"; shift ;;
     *) echo "Unknown operation '$1'"; exit;
  esac
done

if [ "$argon" == "false" ] && [ "$adapter" == "false" ]; then
  help="true";
fi

if [ "$help" == "true" ]; then
  echo "Usage: $0 <options>";
  echo "";
  echo "-argon        Restart the argon system (incl. JMS).";
  echo "-adapter      Restart the adapter.";
  echo "-h,  --help   This help.";
  echo "";
  echo "Mail bug reports and suggestions to <willner@cs.uni-bonn.de>.";
  exit;
fi;
################################################################################

echo "Updating permissions... (please enter the SUDOER password)";
sudo chown -R `whoami` $DIR_ARGON
sudo chown -R `whoami` $ADAPTER_HOME
sudo chown `whoami` $0
sudo chmod -R +w $DIR_ARGON
sudo chmod +x $DIR_INSTALLER/*.sh
sudo chmod +x $OPENJMS_HOME/bin/*.sh

#echo "Backup logs";
#./argonLogsBackup.sh

if [ "$argon" != "false" ]; then
  echo "Shutting down Argonv2 and OpenJMS...";
  cd $OPENJMS_HOME/bin/
  ./shutdown.sh;
  cd $DIR_INSTALLER/
  ./shutdown.sh;
fi;

if [ "$adapter" != "false" ]; then
  echo "Shutting down adapter and updating ...";
  pid=`ps wwaux|grep -i jetty|grep -v grep|awk '{print $2}'`
  kill $pid
  cd $ADAPTER_HOME
  svn up
  ant clean init deploy
  cp build/*.war $JETTY_HOME/webapps
fi;

#if [ "$argon" != "false" ] && [ "$adapter" != "false" ]; then
  # to be sure that all are killed
#  sudo killall java
#  sleep 3
#  sudo killall -9 java
#  sleep 3
#  sudo killall -9 java
#  sleep 1
#fi;

if [ "$argon" != "false" ]; then
  echo "Starting Argonv2 and OpenJMS...";

  cd $DIR_INSTALLER
  rm -rf /opt/argon2/app/
  svn up -r $ARGON_REVISION $DIR_ARGON/src/Argonv2
  echo /opt/argon2/app/ | ./install.sh && \
  . ./config && ./make.sh

  chmod -R +x $OPENJMS_HOME/bin/*
  cd $OPENJMS_HOME/bin/
  nohup ./startup.sh &
  sleep 5
  chmod -R +x $DIR_INSTALLER/*
  cd $DIR_INSTALLER
  nohup ./startup.sh &
  sleep 5
fi;


if [ "$adapter" != "false" ]; then
  echo "Starting adapter ...";
  cd $JETTY_HOME
  nohup java -Did=jetty -jar start.jar &
fi

cd /opt
