@echo off

rem
rem 
rem

setlocal

set STORETYPE=JKS
if not "%1" == "" set STORETYPE=%1

echo Alias=demch: Creating RSA key pair...
keytool -genkey -alias demch -keyalg RSA -dname "CN=Yuri Demchenko, OU=AIRG, O=UvA, C=NL" -keypass xmlsecurity -keystore xmlsec/keystore1xmlsec.jks -storepass xmlsecurity -storetype %STORETYPE%

echo Copying certificate of RSA public key...
keytool -export -alias demch -file xmlsec/demch.cer -keystore xmlsec/keystore1xmlsec.jks -storepass xmlsecurity -storetype %STORETYPE%
keytool -import -noprompt -alias demch -file xmlsec/demch.cer -keystore xmlsec/keystore2xmlsec.jks -storepass xmlsecurity -storetype %STORETYPE%

rem del xmlsec/demch.cer

endlocal
