#!/bin/sh
################################################################################
PROPERTIES_FOLDER="properties/"
NAME="ubo-nsp"
#pass needs least 4 characters
PASSKEY="XXXXXXXXXXXXX"
PASSKEYSTORE="XXXXXXXXXXXXXXXXX"
NAMEKEY="key$NAME"
NAMECERT="cert$NAME"
################################################################################


################################################################################
ORGA='University of Bonn';
DEP='Computer Science IV';
COUNTRY='DE';
STATE='Nordrhein-Westfalen';
CITY='Bonn';
MAIL='support4@cs.uni-bonn.de';
LOG="output"'.log';
STRENGTH="2048";

CONFIG=`mktemp -q /tmp/openssl-conf.XXXXXXXX`
DHOME=`pwd`"/$NAME"
################################################################################


################################################################################
mkdir -p "$DHOME";
cd "$DHOME";
cat <<EOF > $CONFIG
# -------------- BEGIN custom openssl.cnf -----
 HOME                    = $DHOME
 oid_section             = new_oids
 [ new_oids ]
 [ req ]
 default_days            = $DAYS
 default_keyfile         = $DHOME/${NAME}.key
 distinguished_name      = req_distinguished_name
 encrypt_key             = no
 string_mask             = nombstr
 [ req_distinguished_name ]
 commonName                     = Common Name (eg, YOUR name)
 commonName_default             = $DOMAIN
 commonName_max                 = 64
 countryName		            = Country Name (2 letter code)
 countryName_min                = 2
 countryName_max                = 2
 countryName_default            = $COUNTRY
 stateOrProvinceName            = State or Province Name (full name)
 stateOrProvinceName_default    = $STATE
 localityName                   = Locality Name (eg, city)
 localityName_default           = $CITY
 0.organizationName             = Organization Name (eg, company)
 0.organizationName_default     = $ORGA 
 organizationalUnitName         = Organizational Unit Name (eg, section)
 organizationalUnitName_default = $DEP
 emailAddress                   = Email Address
 emailAddress_max               = 40
 emailAddress_default           = $MAIL
# -------------- END custom openssl.cnf -----
EOF
################################################################################


################################################################################
openssl genrsa -des3 -passout pass:$PASSKEY -out $NAMEKEY.pem 1024 
openssl req -batch -config $CONFIG -new -x509 -key $NAMEKEY.pem -passin pass:$PASSKEY -out $NAMECERT.pem -days 1095 
openssl pkcs8 -topk8 -nocrypt -in $NAMEKEY.pem -inform PEM -passin pass:$PASSKEY -out $NAMEKEY.der -outform DER
openssl x509 -in $NAMECERT.pem -inform PEM -out $NAMECERT.der -outform DER
cp ../ImportKey.java .
javac ImportKey.java
echo "Please, specify the keystore pass"
java ImportKey $NAMEKEY.der $NAMECERT.der $NAME $PASSKEYSTORE
mv ~/keystore.ImportKey aai.keystore
echo "Now, you will have to put the same keystore pass (see previous line and you will be able to see your alias and keystore pass)"
keytool -exportcert -alias $NAME -keystore aai.keystore -file $NAMECERT.cer --storepass $PASSKEYSTORE
cp "$NAMECERT.cer" "../certs/"
################################################################################


################################################################################
#keytool -keystore aai.keystore -import -file certkifgui.cer -alias kifgui --storepass i2cat
#keytool -keystore aai.keystore -import -file certbenderidb2.cer -alias benderidb2 --storepass i2cat
#keytool -keystore aai.keystore -import -file certbenderidb1.cer -alias benderidb1 --storepass i2cat
#keytool -keystore aai.keystore -import -file certleelaadapter.cer -alias leelaadapter --storepass i2cat
#keytool -keystore aai.keystore -import -file certzoidbergadapter.cer -alias zoidbergadapter --storepass i2cat 
#cp aai.keystore "$PROPERTIES_FOLDER"
################################################################################
