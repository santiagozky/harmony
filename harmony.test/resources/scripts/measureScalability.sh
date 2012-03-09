#!/bin/sh
export QUICK=1
export ANT_OPTS="$ANT_OPTS -Djavax.net.ssl.trustStore=resources/utils/keystore -XX:MaxPermSize=512M -Xms512M -Xmx512M"
LOGFILE=$0.log
:> $LOGFILE
echo "See $LOGFILE for details..."
for prop in `ls resources/measure.properties/*.properties`; do
  export measureProperties=$prop
  echo "Starting measurement: $prop..."
  ant -Dclass=MeasureScalability testJunit >> $LOGFILE 2>&1
done
