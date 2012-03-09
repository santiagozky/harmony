/**
*  This code is part of the Harmony System implemented in Work Package 1 
*  of the Phosphorus project. This work is supported by the European 
*  Comission under the Sixth Framework Programme with contract number 
*  IST-034115.
*
*  Copyright (C) 2006-2009 Phosphorus WP1 partners. Phosphorus Consortium.
*  http://ist-phosphorus.eu/
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */


/**
 * 
 */
package eu.ist_phosphorus.harmony.test.common.security;

import java.io.IOException;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.muse.util.xml.XmlUtils;
import org.apache.muse.ws.addressing.soap.SoapFault;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import eu.ist_phosphorus.harmony.common.security.authn.HarmonyWSSecurity;
import eu.ist_phosphorus.harmony.common.security.authn.SignatureFactory;
import eu.ist_phosphorus.harmony.common.security.utils.helper.SOAPHelper;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;
import eu.ist_phosphorus.harmony.common.utils.FileHelper;
import eu.ist_phosphorus.harmony.common.utils.PhLogger;

/**
 * @author Alexander Willner (willner@cs.uni-bonn.de), Carlos Baez
 *         (carlos.baez@i2cat.net)
 * 
 */
public class TestEncryption {
    private final Logger logger;
    private  HarmonyWSSecurity authn;
    private final String unencryptedRequest;
    private final String unencryptedResponse;

    /**
     * Constructor.
     * 
     * @throws IOException
     * @throws UnexpectedFaultException
     */

    public TestEncryption() throws IOException, SoapFault {
        this.unencryptedRequest =
                FileHelper.readFile("resources/data/RawReservationRequest.xml");
        this.unencryptedResponse =
            FileHelper.readFile("resources/data/RawReservationResponse.xml");
        this.authn = new HarmonyWSSecurity();

        this.logger = PhLogger.getSeparateLogger("aai");
    }
    
  /**
  * Test method for
  * {@link eu.ist_phosphorus.harmony.common.security.authn.HarmonyWSSecurity#decrypt(java.lang.String)}
  * .
  * 
  * @throws IOException
  */
 public static String  getIdentifier(String unencryptedRequest ) throws IOException {

   try {
		final Document domRequest =   SignatureFactory.String2DOM(unencryptedRequest);
		NodeList listNode = domRequest.getElementsByTagName("wsa:To");
		if (listNode == null || listNode.getLength()==0) return null;
			Node node = listNode.item(0);
			String textNodeContent = node.getTextContent();
			String[] strArray= textNodeContent.split("//");
			if (strArray == null || strArray.length<2) return null;
			String[] identArray = strArray[1].split("/");
			String identifier = null;
			if (identArray == null || identArray.length<2) return null;
			String hostname = identArray[0];
			String devicename = identArray[1];
			identifier = hostname +"_"+devicename;
			return identifier;
			
	
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null;
	}

 }

 /**
  * Test method for
  * {@link eu.ist_phosphorus.harmony.common.security.authn.HarmonyWSSecurity#decrypt(java.lang.String)}
  * .
  * 
  * @throws IOException
  */

 public  static String  getUUID(String unencryptedRequest) throws IOException {

   try {	   
		final Document domRequest =   SignatureFactory.String2DOM(unencryptedRequest);
		NodeList listNode = domRequest.getElementsByTagName("wsa:RelatesTo");
		if (listNode != null && listNode.getLength()>0) {
			Node node = listNode.item(0);
			String textNodeContent = node.getTextContent();
			return textNodeContent;
		}		
		listNode = domRequest.getElementsByTagName("wsa:MessageID");
			if (listNode == null || listNode.getLength()==0) return null ;
				Node node = listNode.item(0);
				String textNodeContent = node.getTextContent();
				return textNodeContent;

	
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null;
	}

 }
 
 
 /**
 * Test method for
 * {@link eu.ist_phosphorus.harmony.common.security.authn.HarmonyWSSecurity#decrypt(java.lang.String)}
 * .
 * 
 * @throws IOException
 */
@Test
public final void testGetRequest() throws IOException {

  try {
	  		String identifier = getIdentifier(unencryptedRequest);
	  		String messageid = getUUID(unencryptedRequest);
			System.out.println(identifier);
			System.out.println(messageid);
	
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}	
}


/**
* Test method for
* {@link eu.ist_phosphorus.harmony.common.security.authn.HarmonyWSSecurity#decrypt(java.lang.String)}
* .
* 
* @throws IOException
*/
@Test
public final void testGetResponse() throws IOException {

	  try {
	  		String identifier = getIdentifier(unencryptedResponse);
	  		String messageid = getUUID(unencryptedResponse);
			System.out.println(messageid);
	
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}	
}
 
//    /**
//     * Test method for
//     * {@link eu.ist_phosphorus.harmony.common.security.authn.HarmonyWSSecurity#decrypt(java.lang.String)}
//     * .
//     * 
//     * @throws IOException
//     */
//    @Test
//    public final void testDecrypt() throws IOException {
//        Assert.assertEquals("Decryption of an unencrypted string failed!",
//                this.unencryptedRequest, this.authn
//                        .decrypt(this.unencryptedRequest));
//    }
//


//    /**
//     * Test all the security process.
//     * 
//     * @throws Exception
//     */
//    @Test
//    public final void testEncryptSignUnsignedDecrypt() throws Exception {
////		String UUID = this.authn.getUUID(SignatureFactory.String2DOM(unencryptedRequest).getDocumentElement());
////		String identifier = this.authn.getIdentifier(SignatureFactory.String2DOM(unencryptedRequest).getDocumentElement());
////		this.authn.setIdHash(UUID, identifier);
////		identifier = "----";
////		identifier = this.authn.getIdHash(UUID);
//		String identifier ="testuser";
//        final String encryptedRequest =
//                this.authn.encrypt(this.unencryptedRequest,identifier);
//        String signedencryptedRequest = this.authn.sign(encryptedRequest);
//        System.out.println("---Signed and encrypted---");
//        System.out.println(signedencryptedRequest);
//
//        boolean isValid = this.authn.isValid(signedencryptedRequest);
//
//        final String decryptedRequest = this.authn.decrypt(encryptedRequest);
//        System.out.println("---Decrypted---");
//        System.out.println(decryptedRequest);
//
//        Assert.assertEquals("The message has been modified, it isn't valid",
//                true, isValid);
//    }
    
    /**
    * Test method for
    * {@link eu.ist_phosphorus.harmony.common.security.authn.HarmonyWSSecurity#decrypt(java.lang.String)}
    * .
    * 
    * @throws IOException
    */
    @Test
    public final void testGetUserID() throws IOException {

    	  try {
    	  		String identifier = SOAPHelper.parseID(
    	  				SOAPHelper.getTo(SignatureFactory.String2DOM(unencryptedRequest).getDocumentElement())
    	  				);
    	  		String messageid = SOAPHelper.getUUID(SignatureFactory.String2DOM(unencryptedRequest).getDocumentElement());
    	  		System.out.println("identifier= "+identifier+" message="+messageid);
    	  		String group = authn.getGroupByUser(identifier);
    	  		System.out.println(group);
    	
    	} catch (Exception e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}	
    } 
    
    
    
    /**
     * Test all the security process.
     * 
     * @throws Exception
     */
    @Test
    public final void testEncryptSignUnsignedDecrypt2() throws Exception {
        /* ************ HARMONY - IDB1 ***************** */
        /* Create a request message */

    	String privateKeyAlias="10.1.3.200_8080_harmony-idb-1";
		String privateKeyPass="idb1pass";
		String keystoreType="JKS";
		String keyStoreFile="/home/carlos/Escritorio/seguridad-testbed-virtual/moduleSec-bender1/properties/aai.keystore";
		String keystorePass="idb1pass";
		String certificateAlias="10.1.3.200_8080_harmony-idb-1";
		String UUID = SOAPHelper.getUUID(SignatureFactory.String2DOM(unencryptedRequest).getDocumentElement());
		String identifier = SOAPHelper.parseID(
				SOAPHelper.getTo(SignatureFactory.String2DOM(unencryptedRequest).getDocumentElement())
				);



		SignatureFactory.resetInstance();
    	this.authn = new HarmonyWSSecurity(keystoreType,keyStoreFile,keystorePass,privateKeyAlias,privateKeyPass,certificateAlias);
//		String identifier ="testuser";
        final String encryptedRequest =
                this.authn.encrypt(this.unencryptedRequest,identifier);
        String signedencryptedRequest = this.authn.sign(encryptedRequest);
        System.out.println("---Signed and encrypted---");
        System.out.println(signedencryptedRequest);
        /* ************ HARMONY - IDB2 ***************** */
        /* Get a request message */

		/* *** */
        UUID = SOAPHelper.getUUID(SignatureFactory.String2DOM(signedencryptedRequest).getDocumentElement());
		final HashMap<String, String> issuerData = this.authn.isValid(SignatureFactory.String2DOM(signedencryptedRequest));
		/* *** */
        String alias = null;
        if (null != issuerData) {
        	alias = issuerData.get("CN");
        }
		System.out.println(alias);
		this.authn.setIdHash(UUID,alias,true);
        SignatureFactory.resetInstance();
    	privateKeyAlias="10.1.3.200_8080_harmony-idb-2";
		privateKeyPass="idb2pass";
		keystoreType="JKS";
		keyStoreFile="/home/carlos/Escritorio/seguridad-testbed-virtual/moduleSec-bender2/properties/aai.keystore";
		keystorePass="idb2pass";
		certificateAlias="10.1.3.200_8080_harmony-idb-2";
    	this.authn = new HarmonyWSSecurity(keystoreType,keyStoreFile,keystorePass,privateKeyAlias,privateKeyPass,certificateAlias);
        boolean isValid = this.authn.isValid(signedencryptedRequest);
        System.out.println("---------------------------->ISVALID: "+isValid);
        final String decryptedRequest = this.authn.decrypt(encryptedRequest);
        System.out.println("---Decrypted---");
        System.out.println(decryptedRequest);
        /* Create a response message */
		identifier = "----";
        System.out.println("---Decrypted Response---");
        System.out.println(unencryptedResponse);
        identifier = SOAPHelper.getUUID(SignatureFactory.String2DOM(unencryptedResponse).getDocumentElement());
        System.out.println(unencryptedResponse);
        
		identifier = this.authn.getIdHashIdentifier(UUID);
//        final String encryptedResponse =
//            this.authn.encrypt(this.unencryptedResponse,identifier);
//        String signedencryptedResponse = this.authn.sign(encryptedResponse);
		
		final String encryptedResponse =this.authn.sign(unencryptedResponse);
		String signedencryptedResponse = this.authn.encrypt(encryptedResponse,identifier);


		
		
        System.out.println("---Signed and encrypted---");
        System.out.println(signedencryptedResponse);
        /* ************ HARMONY - IDB1 ***************** */
        /* Get a response message */
        SignatureFactory.resetInstance();
    	privateKeyAlias="10.1.3.200_8080_harmony-idb-1";
    	privateKeyPass="idb1pass";
		keystoreType="JKS";
		keyStoreFile="/home/carlos/Escritorio/seguridad-testbed-virtual/moduleSec-bender1/properties/aai.keystore";
		keystorePass="idb1pass";
		certificateAlias="10.1.3.200_8080_harmony-idb-1";
    	this.authn = new HarmonyWSSecurity(keystoreType,keyStoreFile,keystorePass,privateKeyAlias,privateKeyPass,certificateAlias);
        isValid = this.authn.isValid(signedencryptedResponse);
        System.out.println("---------------------------->ISVALID: "+isValid);
//        signedencryptedResponse = this.authn.removeSignature(signedencryptedResponse);

        final String decryptedResponse = this.authn.decrypt(signedencryptedResponse);
        System.out.println("---Decrypted---");
        System.out.println(decryptedResponse);
        
        isValid = this.authn.isValid(decryptedResponse);
        System.out.println("---------------------------->ISVALID: "+isValid);
        
        
        
        

        Assert.assertEquals("The message has been modified, it isn't valid",
                true, isValid);
    }
    
//
//    /**
//     * Test the encryption to the SOAP body element
//     * 
//     * @throws Exception
//     */
//    @Test
//    public final void testEncryptDecryptDOM() throws Exception {
//        /* Encrypt the body message */
//
//        final Document domRequest =
//                SignatureFactory.String2DOM(unencryptedRequest);
//        logger.info("------unencryptedRequest------");
//        logger.info(XmlUtils.toString(domRequest));
//        Element nodeBody = domRequest.getDocumentElement();
//        // Element nodeBody = (Element)
//        // domRequest.getElementsByTagName("soap:Body").item(0);
//        logger.info(XmlUtils.toString(nodeBody));
//        final Document encryptedRequest = this.authn.encrypt(nodeBody);
//
//        logger.info("------Encrypt/Decrypt with Dom class------");
//        logger.info(XmlUtils.toString(encryptedRequest));
//
//        Document domBody =
//                this.authn.decrypt(encryptedRequest.getDocumentElement());
//        // String strBody =
//        // this.authn.decrypt(XmlUtils.toString(encryptedRequest));
//        logger.info("------Decrypt element------");
//        logger.info(XmlUtils.toString(domBody));
//        // logger.info(SignatureFactory.DOM2String(domBody));
//        //        
//        Assert.assertEquals("The message has been modified, it isn't valid",
//                true, true);
//    }
//
//    /**
//     * Test the encryption to the SOAP body element
//     * 
//     * @throws Exception
//     */
//    @Test
//    public final void testSignUnsignedDOM() throws Exception {
//        /* Encrypt the body message */
//        final String request =
//                FileHelper.readFile("resources/data/RawReservationRequest.xml");
//
//        final Document domRequest = XmlUtils.createDocument(request);
//
//        final Element signedRequest =
//                this.authn.sign(domRequest.getDocumentElement());
//
//        final String temp = XmlUtils.toString(signedRequest, false, false);
//
//        final Element signedRequest2 =
//                XmlUtils.createDocument(temp).getDocumentElement();
//
//        boolean isValid = (null != this.authn.isValid(signedRequest));
//
//        Assert.assertEquals("The message has been modified, it isn't valid",
//                true, isValid);
//
//        Assert.assertEquals("The message has been modified, it isn't valid",
//                true, (null != this.authn.isValid(signedRequest2)));
//    }
//
//    /**
//     * Test the encryption to the SOAP body element
//     * 
//     * @throws Exception
//     */
//    @Test
//    public final void testDOMSignStringUnsigned() throws Exception {
//        /* Encrypt the body message */
//
//        final Document domRequest =
//                SignatureFactory.String2DOM(unencryptedRequest);
//        ;
//        final Element signedRequest =
//                this.authn.sign(domRequest.getDocumentElement());
//
//        String strRequest = SignatureFactory.Node2String(signedRequest);
//
//        boolean isValid = this.authn.isValid(strRequest);
//        Assert.assertEquals("The message has been modified, it isn't valid",
//                true, isValid);
//    }
//
//    /**
//     * Test the encryption to the SOAP body element
//     * 
//     * @throws Exception
//     */
//    @Test
//    public final void testStringSignStringDOM() throws Exception {
//        /* Encrypt the body message */
//
//        final Document domRequest =
//                SignatureFactory.String2DOM(unencryptedRequest);
//
//        // Element nodeBody = (Element)
//        // domRequest.getElementsByTagName("soap:Body").item(0);
//        // System.out.println(XmlUtils.toString(nodeBody));
//        final String signedRequest =
//                this.authn.sign(SignatureFactory.Node2String(domRequest));
//
//        Document docRequest = SignatureFactory.String2DOM(signedRequest);
//        boolean isValid =
//                (null != this.authn.isValid(docRequest.getDocumentElement()));
//        Assert.assertEquals("The message has been modified, it isn't valid",
//                true, isValid);
//    }
//
//    /**
//     * Test the encryption to the SOAP body element
//     * 
//     * @throws Exception
//     */
//    @Test
//    public final void testStringEncryptDecryptDOM() throws Exception {
//        /* Encrypt the body message */
//
//        final Document domRequest =
//                SignatureFactory.String2DOM(unencryptedRequest);
//
//        logger.debug("------UnencryptedRequest------");
//        logger.debug(unencryptedRequest);
//        // StringToFile("Unsigned.xml",SignatureFactory.Node2String(domRequest));
//
//        final String encryptRequest =
//                this.authn.encrypt(SignatureFactory.Node2String(domRequest));
//
//        logger.debug("------EncryptedRequest------");
//        logger.debug(encryptRequest);
//
//        Document docRequest = SignatureFactory.String2DOM(encryptRequest);
//        Document decryptRequest =
//                this.authn.decrypt(docRequest.getDocumentElement());
//
//        logger.debug("------DecryptedRequest------");
//        logger.debug(SignatureFactory.Node2String(decryptRequest));
//
//        Assert.assertEquals("The message has been modified, it isn't valid",
//                true, true);
//    }
//
//    /**
//     * Test the encryption to the SOAP body element
//     * 
//     * @throws Exception
//     */
//    @Test
//    public final void testDOMEncryptDecryptString() throws Exception {
//        /* Encrypt the body message */
//
//        final Document domRequest =
//                SignatureFactory.String2DOM(unencryptedRequest);
//
//        logger.debug("------UnencryptedRequest------");
//        logger.debug(unencryptedRequest);
//        // StringToFile("Unsigned.xml",SignatureFactory.Node2String(domRequest));
//
//        final Document encryptRequest =
//                this.authn.encrypt(domRequest.getDocumentElement());
//
//        logger.debug("------EncryptedRequest------");
//        logger.debug(SignatureFactory.Node2String(encryptRequest));
//
//        String decryptRequest =
//                this.authn
//                        .decrypt(SignatureFactory.Node2String(encryptRequest));
//
//        logger.debug("------DecryptedRequest------");
//        logger.debug(decryptRequest);
//
//        Assert.assertEquals("The message has been modified, it isn't valid",
//                true, true);
//    }
//
//    /**
//     * Test method for
//     * {@link eu.ist_phosphorus.harmony.common.security.authn.HarmonyWSSecurity#getUser(java.lang.String)}
//     * .
//     * 
//     * @throws Exception
//     */
//    @Test
//    public final void testGetUser() throws Exception {
//        final Document doc = XmlUtils.createDocument(this.unencryptedRequest);
//        
//        final Element reqWithSign = this.authn.sign(doc.getDocumentElement());
//        
//        final HashMap<String, String> certId = this.authn.isValid(reqWithSign);
//        
//        final String strCredentials = certId.get("CN");
//        // String reqWithSign = signFactory.sign(msgRequest);
//        // this.logger.debug("Credentials:\n" + strCredentials);
//        /* ------------------------------------------------------------------ */
//        /* Get the user role */
//        final String strRole = "admin";
//        this.logger.debug("User role:\n" + strRole);
//
//        Assert.assertEquals("Request has not the expected user ID",
//                "Alexander Willner", strCredentials);
//    }
//
//    /**
//     * Test method for
//     * {@link eu.ist_phosphorus.harmony.common.security.authn.HarmonyWSSecurity#isValid(java.lang.String)}
//     * .
//     * 
//     * @throws Exception
//     */
//    @Test
//    public final void testIsValid() throws Exception {
//        /* load an example Harmony reservation request ---------------------- */
//        final String reqWithSign = this.authn.sign(this.unencryptedRequest);
//        boolean isValid = false;
//        /* ------------------------------------------------------------------ */
//
//        /* Validate signature ----------------------------------------------- */
//        isValid = this.authn.isValid(this.unencryptedRequest);
//        Assert.assertFalse("Request should not be valid", isValid);
//
//        isValid = this.authn.isValid(reqWithSign);
//        // this.logger.debug("Validate:\n" + boolFactory);
//        Assert.assertTrue("Request is not valid", isValid);
//        /* ------------------------------------------------------------------ */
//
//    }
//
//    /**
//     * Test method for
//     * {@link eu.ist_phosphorus.harmony.common.security.authn.HarmonyWSSecurity#sign(java.lang.String)}
//     * .
//     * 
//     * @throws Exception
//     */
//    @Test
//    public final void testSign() throws Exception {
//        final String copyRequest = new String(this.unencryptedRequest);
//        /* ------------------------------------------------------------------ */
//
//        /* load an example Harmony reservation request ---------------------- */
//        final String reqWithSign = this.authn.sign(this.unencryptedRequest);
//        this.logger.debug("Signature:\n" + reqWithSign);
//
//        /* ------------------------------------------------------------------ */
//
//        Assert.assertTrue("Correct the signature", !reqWithSign
//                .equalsIgnoreCase(copyRequest));
//    }
//
//    /**
//     * Test method for
//     * {@link eu.ist_phosphorus.harmony.common.security.authn.HarmonyWSSecurity#sign(java.lang.String)}
//     * .
//     * 
//     * @throws Exception
//     */
//    @Test
//    public final void testIsSigned() throws Exception {
//        final String copyRequest = new String(this.unencryptedRequest);
//        /* ------------------------------------------------------------------ */
//        boolean isNotSigned = this.authn.isSigned(unencryptedRequest);
//
//        /* load an example Harmony reservation request ---------------------- */
//        final String reqWithSign = this.authn.sign(this.unencryptedRequest);
//        boolean isSigned = this.authn.isSigned(reqWithSign);
//        // this.logger.debug("Signature:\n" + reqWithSign);
//        /* ------------------------------------------------------------------ */
//
//        Assert
//                .assertTrue("Correct the signature", (!isNotSigned)
//                        && (isSigned));
//
//    }
//
//    /**
//     * 
//     * @throws Exception
//     */
//    @Test
//    public final void testIsEncrypted() throws Exception {
//        final String copyRequest = new String(this.unencryptedRequest);
//        /* ------------------------------------------------------------------ */
//        boolean isNotEncrypted = this.authn.isEncrypted(copyRequest);
//
//        /* load an example Harmony reservation request ---------------------- */
//        final String reqWithEncrypt =
//                this.authn.encrypt(this.unencryptedRequest);
//        boolean isEncrypted = this.authn.isEncrypted(reqWithEncrypt);
//        // this.logger.debug("Signature:\n" + reqWithSign);
//        /* ------------------------------------------------------------------ */
//
//        Assert.assertTrue("Correct the signature", (!isNotEncrypted)
//                && (isEncrypted));
//
//    }
//
//    /**
//     * 
//     * @throws Exception
//     */
//    @Test
//    public final void testRemoveSignature() throws Exception {
//        final String copyRequest = new String(this.unencryptedRequest);
//        /* ------------------------------------------------------------------ */
//
//        /* load an example Harmony reservation request ---------------------- */
//        final String reqWithSign = this.authn.sign(this.unencryptedRequest);
//        logger.info("Signature:\n" + reqWithSign);
//        // System.out.println("Signature:\n" + reqWithSign);
//        String reqWithoutSign = this.authn.removeSignature(reqWithSign);
//        logger.info("Request with signature removed:\n" + reqWithoutSign);
//        // System.out.println("Request with signature removed:\n" +
//        // reqWithoutSign);
//
//        /* ------------------------------------------------------------------ */
//
//        Assert.assertTrue("Correct the signature", !reqWithSign
//                .equalsIgnoreCase(copyRequest));
//
//    }

}