@echo off

rem
rem 
rem

setlocal

set STORETYPE=JKS
if not "%1" == "" set STORETYPE=%1

echo Alias=cnl01: Creating RSA key pair...
keytool -genkey -alias cnl01 -keyalg RSA -dname "CN=AAAuthreach Security, O=Collaboratory.nl, C=NL" -keypass xmlsecurity -keystore xmlsec/keystore1xmlsec.jks -storepass xmlsecurity -storetype %STORETYPE%

echo Copying certificate of RSA public key...
keytool -export -alias cnl01 -file xmlsec/cnl01.cer -keystore xmlsec/keystore1xmlsec.jks -storepass xmlsecurity -storetype %STORETYPE%
keytool -import -noprompt -alias cnl01 -file xmlsec/cnl01.cer -keystore xmlsec/keystore2xmlsec.jks -storepass xmlsecurity -storetype %STORETYPE%

rem adel cnl01.cer

endlocal
