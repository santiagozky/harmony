for i in $(ls); do
#echo $i
if [[ (-d "$i/src" ) && ! ( -d "$i/src/main") ]]; then
   cd "$i/src"
   echo $( pwd)
   
   
 mkdir -p main/java
 mkdir -p test/java/eu/ist_phosphorus/harmony
 mv eu main/java/
 mv main/java/eu/ist_phosphorus/harmony/test test/java/eu/ist_phosphorus/harmony/
   cd ../..
   
  fi
done


