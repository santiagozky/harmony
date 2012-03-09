perl -p -e 's/passViaWebservice\s*=.*/passViaWebservice = false/' resources/properties/idb.properties > tmp; mv tmp resources/properties/idb.properties
perl -p -e 's/test.callWebservice\s*=.*/test.callWebservice = false/' resources/properties/test.properties > tmp; mv tmp resources/properties/test.properties

echo dir.tools=../../tools > build.local.properties
echo dir.jaxb=\${dir.tools}/jaxb >> build.local.properties
echo dir.muse=\${dir.tools}/muse >> build.local.properties

exit 0
