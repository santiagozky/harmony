@echo off

rem
rem 
rem

setlocal

set STORETYPE=JKS
if not "%1" == "" set STORETYPE=%1

echo Alias=cnl:user0#. Creating CNL users's RSA key pairs...
rem keytool -genkey -alias cnl01 -keyalg RSA -dname "CN=AAAuthreach Security, O=Collaboratory.nl, C=NL" -keypass xmlsecurity -keystore xmlsec/keystore1xmlsec.jks -storepass xmlsecurity -storetype %STORETYPE%

keytool -genkey -alias cnl_user01 -keyalg RSA -dname "CN=User01, O=Collaboratory.nl, C=NL" -keypass AuthN:user01 -keystore cnlsec/keystore5cnlsec.jks -storepass cnlsecurity -storetype %STORETYPE%
keytool -export -alias cnl_user01 -file cnlsec/cnl_user01.cer -keystore cnlsec/keystore5cnlsec.jks -storepass cnlsecurity -storetype %STORETYPE%
keytool -genkey -alias cnl_user02 -keyalg RSA -dname "CN=User02, O=Collaboratory.nl, C=NL" -keypass AuthN:user02 -keystore cnlsec/keystore5cnluser02.jks -storepass cnlsecurity -storetype %STORETYPE%
keytool -export -alias cnl_user02 -file cnlsec/cnl_user02.cer -keystore cnlsec/keystore5cnluser02.jks -storepass cnlsecurity -storetype %STORETYPE%
keytool -genkey -alias cnl_user03 -keyalg RSA -dname "CN=User03, O=Collaboratory.nl, C=NL" -keypass AuthN:user03 -keystore cnlsec/keystore5cnluser03.jks -storepass cnlsecurity -storetype %STORETYPE%
keytool -export -alias cnl_user03 -file cnlsec/cnl_user03.cer -keystore cnlsec/keystore5cnluser03.jks -storepass cnlsecurity -storetype %STORETYPE%
keytool -genkey -alias cnl_user04 -keyalg RSA -dname "CN=User04, O=Collaboratory.nl, C=NL" -keypass AuthN:user04 -keystore cnlsec/keystore5cnluser04.jks -storepass cnlsecurity -storetype %STORETYPE%
keytool -export -alias cnl_user04 -file cnlsec/cnl_user04.cer -keystore cnlsec/keystore5cnluser04.jks -storepass cnlsecurity -storetype %STORETYPE%

echo Alias=cnl:pep. Creating CNL PEP's RSA key pairs...
keytool -genkey -alias cnl_pep -keyalg RSA -dname "CN=PEP, O=Collaboratory.nl, C=NL" -keypass Trust:pep -keystore cnlsec/keystore5cnlsec.jks -storepass cnlsecurity -storetype %STORETYPE%
keytool -export -alias cnl_pep -file cnlsec/cnl_pep.cer -keystore cnlsec/keystore5cnlsec.jks -storepass cnlsecurity -storetype %STORETYPE%

echo Alias=cnl:aaapdp. Creating CNL PDP's RSA key pairs...
keytool -genkey -alias cnl_aaapdp -keyalg RSA -dname "CN=PDP, O=Collaboratory.nl, C=NL" -keypass Trust:pdp -keystore cnlsec/keystore5cnlaaapdp.jks -storepass cnlsecurity -storetype %STORETYPE%
keytool -export -alias cnl_aaapdp -file cnlsec/cnl_aaapdp.cer -keystore cnlsec/keystore5cnlaaapdp.jks -storepass cnlsecurity -storetype %STORETYPE%

echo Alias=cnl:CHEF. Creating CNL CHEF's RSA key pairs...
keytool -genkey -alias cnl_chef -keyalg RSA -dname "CN=CHEF, O=Collaboratory.nl, C=NL" -keypass CNL:chef -keystore cnlsec/keystore5cnlchef.jks -storepass cnlsecurity -storetype %STORETYPE%
keytool -export -alias cnl_chef -file cnlsec/cnl_chef.cer -keystore cnlsec/keystore5cnlchef.jks -storepass cnlsecurity -storetype %STORETYPE%

echo Alias=cnl:Surabaya. Creating CNL Surabaya's RSA key pairs...
keytool -genkey -alias cnl_surabaya -keyalg RSA -dname "CN=Surabaya, O=Collaboratory.nl, C=NL" -keypass CNL:surabaya -keystore cnlsec/keystore5cnlsurabaya.jks -storepass cnlsecurity -storetype %STORETYPE%
keytool -export -alias cnl_surabaya -file cnlsec/cnl_surabaya.cer -keystore cnlsec/keystore5cnlsurabaya.jks -storepass cnlsecurity -storetype %STORETYPE%

echo Copying trusted certificates into trusted/keystore5cnltrusted.jks
rem keytool -import -noprompt -alias cnl_user01 -file cnlsec/cnl_user01.cer -keystore cnlsec/keystore5cnltrusted.jks -storepass cnltrusted -storetype %STORETYPE%

rem Services' certs
keytool -import -noprompt -alias cnl_pep -file cnlsec/cnl_pep.cer -keystore trusted/keystore5cnltrusted.jks -storepass cnltrusted -storetype %STORETYPE%
keytool -import -noprompt -alias cnl_aaapdp -file cnlsec/cnl_aaapdp.cer -keystore trusted/keystore5cnltrusted.jks -storepass cnltrusted -storetype %STORETYPE%
keytool -import -noprompt -alias cnl_chef -file cnlsec/cnl_chef.cer -keystore trusted/keystore5cnltrusted.jks -storepass cnltrusted -storetype %STORETYPE%
keytool -import -noprompt -alias cnl_surabaya -file cnlsec/cnl_surabaya.cer -keystore trusted/keystore5cnltrusted.jks -storepass cnltrusted -storetype %STORETYPE%

rem User Certs
keytool -import -noprompt -alias cnl_user01 -file cnlsec/cnl_user01.cer -keystore trusted/keystore5cnltrusted.jks -storepass cnltrusted -storetype %STORETYPE%
keytool -import -noprompt -alias cnl_user02 -file cnlsec/cnl_user02.cer -keystore trusted/keystore5cnltrusted.jks -storepass cnltrusted -storetype %STORETYPE%
keytool -import -noprompt -alias cnl_user03 -file cnlsec/cnl_user03.cer -keystore trusted/keystore5cnltrusted.jks -storepass cnltrusted -storetype %STORETYPE%
keytool -import -noprompt -alias cnl_user04 -file cnlsec/cnl_user04.cer -keystore trusted/keystore5cnltrusted.jks -storepass cnltrusted -storetype %STORETYPE%


rem adel cnl01.cer

endlocal
