@echo off

rem
rem 
rem

setlocal

set STORETYPE=JKS
if not "%1" == "" set STORETYPE=%1

echo Alias=smith: Creating RSA key pair...
keytool -genkey -alias smith -keyalg RSA -dname "CN=Agent Smith, O=Matrix, C=NL" -keypass xmlsecurity -keystore xmlsec/keystore1xmlsec.jks -storepass xmlsecurity -storetype %STORETYPE%

echo Copying certificate of RSA public key...
keytool -export -alias smith -file xmlsec/smith.cer -keystore xmlsec/keystore1xmlsec.jks -storepass xmlsecurity -storetype %STORETYPE%
keytool -import -noprompt -alias smith -file xmlsec/smith.cer -keystore xmlsec/keystore2xmlsec.jks -storepass xmlsecurity -storetype %STORETYPE%

del smith.cer

endlocal
