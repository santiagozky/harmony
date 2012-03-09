#!/bin/sh
#path with the security.jar
PATH_JAR="path_jars/harmony-common-security.jar" 
#folder with the file configuration
CONFIG_FOLDER="$PWD/properties"
TEMP_FOLDER="temp/"
echo 'Getting file security'
#mkdir "$TEMP_FOLDER"
cp "$PATH_JAR" "$TEMP_FOLDER"
cd "$TEMP_FOLDER" 
jar xvf harmony-common-security.jar
echo 'Replacing file configurations'
cp "$CONFIG_FOLDER/aai.keystore" "properties/"
cp "$CONFIG_FOLDER/groups.properties" "properties/"
cp "$CONFIG_FOLDER/aai.properties" "properties/"
#mv harmony-common-security.jar harmony-common-security.jar.old
rm harmony-common-security.jar 
jar cvf harmony-common-security.jar *
cd "$OLDPWD"
cp "$TEMP_FOLDER/harmony-common-security.jar" "$PATH_JAR"
#permissions

