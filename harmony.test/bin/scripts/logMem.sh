#!/bin/bash

if [ "$1" == "" ]; then
 echo "Usage: $0 <filename>";
 exit 1;
fi

: > $1

pause=30

i=0;
while true; do
  mem=`ps -eo pmem,pcpu,rss,vsize,user,command | sort -k 1 -r | head -n2|grep -v RSS`
  echo $i $mem
  echo $i $mem >> $1
  sleep $pause
  i=$[$i+$pause];
done

