if [ "$1" == "" ]; then
 echo "Usage: $0 <filename>";
 exit 1;
fi

: > $1

pause=5

for i in `seq 0 $pause 5000`; do
  loadAvg5=`cat /proc/loadavg|awk '{print $1}'`
  loadAvg10=`cat /proc/loadavg|awk '{print $2}'`
  loadAvg15=`cat /proc/loadavg|awk '{print $3}'`
  echo $i $loadAvg5 $loadAvg10 $loadAvg15
  echo $i $loadAvg5 $loadAvg10 $loadAvg15 >> $1
  sleep $pause
done

