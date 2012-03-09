 
Phosphorus WP1 Readme -
================================================================================



Index:
======

0) Prerequisites
	0.1) Required Software
	0.2) Required configuration

1) Getting started
	1.1) Creating certificates
	1.2) Upload your certificate
	1.3) Status of configuration

2) Adding Certificates
	2.1) Add Certificates

3) AAI Configuration
	3.1) Security configuration
	3.2) Build harmony-common-security.jar


0. Prerequisites
================

0.1: Required Software
	* OpenSSL (http://www.openssl.org/) or Windows (http://www.slproweb.com/products/Win32OpenSSL.html)
	* Java SDK and Keytool (http://java.sun.com/)
	* ImportKey tool (http://www.agentbob.info/agentbob/80/version/default/part/AttachmentData/data/ImportKey.java)

0.2: Required Configuration
	* the project should be updated. The package harmony-common-interfaces.jar have to support security
	* The path should be configured for Openssl and java. The command line have to support the keytool, javac, java and openssl command
	* For build and deploy your new HNA/IDB. You must follow the README for the folder to the IDB/Adapter project 


1. Getting started
===================

1.1: Creating certificates
	Go to the folder /resources/scripts. Specify your parameters for the configuration in the file createCerts.sh:

	NAME="myalias"    <-   alias for the certificate 
	PASSKEY="xxxxxx"     <-   key pass, needs least 4 characters
	NAMEKEY="key$NAME"   <-   key name
	NAMECERT="cert$NAME" <-   cerficate name

	Type this command and fill in the necessary information (PLEASE, specify the same PASSKEY in the questions)

	+----------------------------------------------------------------------+
	| > ./createCerts.sh                                                   |
	+----------------------------------------------------------------------+
	
1.2: Upload your certificate
	Now, your keys and certificates are created in the folder, please these files are very important, you must put its away in
	a secure place. If all worked correctly, you must have your certificate .cer in the ./certs folder, if you want to share 
	your certificate, please, upload the folder with subversion

	+----------------------------------------------------------------------+
	| > cd certs                                                           |
	| > svn update                                                         |
	+----------------------------------------------------------------------+

1.3: Status of configuration 
	In this point you should have:
	    * certificates of your idb/adapter with extension .der .pem
	    * private key of your idb/adapter with extension .der .pem
	    * a password of the key (PASSKEY="xxxxx")
	    * a keystore that it stores keys and certificates that you will manage (aai.keystore)
	    * a password of the keystore. You wrote down this string when the script executed the ImportKey
	    * an alias that you have specified with the Importkey and it was printed when you executed the Importkey 


2. Adding certificates
===================

2.1: Add Certificates
	You must include the HNA/IDB certificate for each HNA/IDB that it will connected with you. You must repeat this command for
	each certificate added:

	NOTE: These certificates should be located in XXXX. If you don't find some certificate, please reply us to the Phosphorus
	mailing list.	

	+--------------------------------------------------------------------------------------------------------------+
	| > keytool -keystore aai.keystore -import -file path/namecert.cer -alias namealias --storepass keystore_pass  |
	+--------------------------------------------------------------------------------------------------------------+



3. AAI configuration
===================

3.1: Security configuration
	With the first step completed. You must configure your AAI module. Inside the file folder xxxx, you replace the parameters for
	your configuration inside the files.

	Configure the aai.properties with 

	 authn.keystore.pass = passwordofkeystore -> keystore password
 	 authn.key.alias = alias                  -> certificate alias
 	 authn.key.pass = password                -> key password

	 WARNING: these parameters active the security for the HNA/IDB, only, the HNA/IDBs configured with your certificate will be able to
	 communicate with you

	 force.request.encryption  	= false
	 force.in.request.signing  	= false
	 force.out.request.signing 	= true <-
	 force.response.encryption 	= false
	 force.in.response.signing 	= false
	 force.out.response.signing	= true <-

	Configure the groups.properties. This file specifies the role that an alias have for the communication (f.e admin). For this, you must
	include all the HNA/IDBS that will have communication with you, for example:
	
	testuser = admin
	AlexanderWillner = admin
	i2cat = admin
	viola1 = admin
	viola2 = admin
	argonadapter1 = admin
	argonadapter2 = admin
	argonadapter3 = admin	

3.2: Build harmony-common-security.jar

	Finally, you can start building your new harmony-common-security. First, YOU MUST SPECIFY WHAT security.jar will be modified in the 
	variable path_jars inside the script, after, you will have to execute the buildSecurity.sh:

	NOTE: This path specifies the path of the final security module. You can specify the harmony-common-security.jar that is in the 
	idb/adapter project (lib/), and you will only need to build a new idb/adapter with security

	+----------------------------------------------------------------------+
	| > ./buildSecurity.sh                                                 |
	+----------------------------------------------------------------------+
	
	if it works fine, you will have a updated harmony-common-security.jar in your path_jars. Now you have to rebuild and deploy your HNA/IDB
	with this updated module in your path (lib/)

	+----------------------------------------------------------------------+
	| > ant build                                                          |
	| > ant deploy                                                         |
	+----------------------------------------------------------------------+


