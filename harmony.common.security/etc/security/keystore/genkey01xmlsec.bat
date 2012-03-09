@echo off

rem
rem 
rem

setlocal

set STORETYPE=JKS
if not "%1" == "" set STORETYPE=%1

rem echo Alias=smith: Creating DSA key pair...
rem keytool -genkey -alias test -keyalg DSA -keypass xmlsecurity -keystore xmlsec/keystore1xmlsec.jks -storepass xmlsecurity -validity 2005 -dname "CN=Christian Geuer-Pollmann, OU=FB12NUE, O=University of Siegen C=DE"

rem echo Alias=smith: Creating DSA key pair...
rem keytool -genkey -alias smith -keyalg DSA -dname "CN=Agent Smith, O=Matrix, C=NL" -keypass xmlsecurity -keystore xmlsec/keystore1xmlsec.jks -storepass xmlsecurity -storetype %STORETYPE%

echo Alias=test1: Creating DSA key pair...
keytool -genkey -alias test01 -keyalg DSA -dname "CN=Agent Smith, O=Matrix, C=NL" -keypass xmlsecurity -keystore xmlsec/keystore1xmlsec.jks -storepass xmlsecurity -storetype %STORETYPE%

endlocal

