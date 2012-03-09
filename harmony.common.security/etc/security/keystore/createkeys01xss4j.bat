@echo off

rem
rem 
rem

setlocal

set STORETYPE=JKS
if not "%1" == "" set STORETYPE=%1

echo Creating RSA key pair...
keytool -genkey -alias Alice -keyalg RSA -dname "CN=Alice, O=IBM, C=US" -keypass keypass -keystore keystore2 -storepass storepass -storetype %STORETYPE%

echo Copying certificate of RSA public key...
keytool -export -alias Alice -file foo.cer -keystore keystore2 -storepass storepass -storetype %STORETYPE%
keytool -import -noprompt -alias Alice -file foo.cer -keystore keystore1 -storepass storepass -storetype %STORETYPE%
del foo.cer

echo Creating triple DES key...
java enc.KeyGenerator -g -keyalg DESede -alias Bob -keypass keypass -keystore keystore2 -storetype %STORETYPE% -storepass storepass

echo Copying triple DES key...
java enc.KeyGenerator -c -alias Bob -keypass keypass -keystore keystore2 -storetype %STORETYPE% -storepass storepass -dalias Bob -dkeypass keypass -dkeystore keystore1 -dstoretype %STORETYPE% -dstorepass storepass

endlocal
