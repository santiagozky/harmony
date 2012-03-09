#!/bin/bash

if [ ! -e "$JETTY_HOME" ] ; then
  echo "Please set JETTY_HOME first.";
  exit;
fi

ant deploy && \
cp build/harmony-adapter-argon.war $JETTY_HOME/webapps/ && \
cd $JETTY_HOME && \
java -jar start.jar; \
cd -

