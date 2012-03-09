#!/bin/sh
PROPERTIES_FOLDER="properties/"
NAME="i2cat"
#pass needs least 4 characters
PASSKEY="j0anangellu1s"
PASSKEYSTORE="j0anangellu1s"
NAMEKEY="key$NAME"
NAMECERT="cert$NAME"

openssl genrsa -des3 -passout pass:$PASSKEY -out $NAMEKEY.pem 1024 
openssl req -new -x509 -key $NAMEKEY.pem -passin pass:$PASSKEY -out $NAMECERT.pem -days 1095 
openssl pkcs8 -topk8 -nocrypt -in $NAMEKEY.pem -inform PEM -passin pass:$PASSKEY -out $NAMEKEY.der -outform DER
openssl x509 -in $NAMECERT.pem -inform PEM -out $NAMECERT.der -outform DER
javac ImportKey.java
echo "Please, specify the keystore pass"
java ImportKey $NAMEKEY.der $NAMECERT.der $NAME $PASSKEYSTORE
mv ~/keystore.ImportKey aai.keystore
echo "Now, you will have to put the same keystore pass (see previous line and you will be able to see your alias and keystore pass)"
keytool -exportcert -alias $NAME -keystore aai.keystore -file $NAMECERT.cer --storepass $PASSKEYSTORE
cp "$NAMECERT.cer" "certs/"

#keytool -keystore aai.keystore -import -file certkifgui.cer -alias kifgui --storepass i2cat
#keytool -keystore aai.keystore -import -file certbenderidb2.cer -alias benderidb2 --storepass i2cat
#keytool -keystore aai.keystore -import -file certbenderidb1.cer -alias benderidb1 --storepass i2cat
#keytool -keystore aai.keystore -import -file certleelaadapter.cer -alias leelaadapter --storepass i2cat
#keytool -keystore aai.keystore -import -file certzoidbergadapter.cer -alias zoidbergadapter --storepass i2cat 
#cp aai.keystore "$PROPERTIES_FOLDER"




