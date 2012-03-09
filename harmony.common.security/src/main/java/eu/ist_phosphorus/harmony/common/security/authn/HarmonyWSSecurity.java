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
package eu.ist_phosphorus.harmony.common.security.authn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.MissingResourceException;

import org.apache.log4j.Logger;
import org.apache.muse.util.xml.XmlUtils;
import org.apache.muse.ws.addressing.soap.SoapFault;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.exceptions.OperationNotAllowedFaultException;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;
import eu.ist_phosphorus.harmony.common.utils.Config;
import eu.ist_phosphorus.harmony.common.utils.PhLogger;

/**
 * Implementation of the Signature Factory for the Authentication.
 * 
 * @author Carlos Baez (carlos.baez@i2cat.net)
 * @version $Id$
 */

public class HarmonyWSSecurity {
    private final Logger log = PhLogger.getSeparateLogger("aai");
	static private Hashtable<String,List> hashIds = new Hashtable<String,List>();
	
    // private final static Logger logger = PhLogger.getSeparateLogger("aai");

	/**
	 * Constructor that it configures all the parameters
	 */
    public HarmonyWSSecurity(String keystoreType, String keyStoreFile,
            String keyStorePass, String privateKeyAlias, String privateKeyPass,
            String certificateAlias) throws SoapFault {
        try {
            SignatureFactory.getInstance(keystoreType,keyStoreFile,keyStorePass,privateKeyAlias,privateKeyPass,certificateAlias);
        } catch (final SoapFault e) {
        	log.error("WARNING: It couldn't be initialized the HarmonyWSSecurity: "+e.getMessage(),e);
//        	log.debug("WARNING: It couldn't be initialized the HarmonyWSSecurity: "+e.getMessage(),e);
            throw e;
        } catch (final Exception e) {
        	
            throw new UnexpectedFaultException(
                    "Unable to initialise Security: " + e.getMessage(), e);
        }
    }
    /**
     * Default constructor. Get the configuration from aai.properties file
     * @throws SoapFault
     */
    public HarmonyWSSecurity() throws SoapFault {
        try {
            SignatureFactory.getInstance();
        } catch (final SoapFault e) {
        	log.error("WARNING: It couldn't be initialized the HarmonyWSSecurity: "+e.getMessage(),e);
 //       	log.debug("WARNING: It couldn't be initialized the HarmonyWSSecurity: "+e.getMessage(),e);
            throw e;
        } catch (final Exception e) {
            throw new UnexpectedFaultException(
                    "Unable to initialise Security: " + e.getMessage(), e);
        }
    }
    
    /**
     * Constructor which replaces the hashtable
     * @param hash
     */
    public HarmonyWSSecurity(Hashtable<String,List> hash) {
    	hashIds = hash;
    	
    }

  
    /**
     * Decrypt of a String secure Message.
     * 
     * @param secureRequest
     *            String with a SOAP message encrypted
     * @return SOAP Message without encryption
     */
    @Deprecated
    public String decrypt(final String secureRequest) throws Exception {

        String request = "";

        if (secureRequest == null) {
            return secureRequest;
        }
        SignatureFactory signFactory = null;
        try {
        	signFactory = SignatureFactory.getInstance();
	    } catch (final Exception exception) {
//	    	log.debug("WARNING: It couldn't be initialized the module: "+exception.getMessage(),exception);
	    	log.error("WARNING: It couldn't be initialized the module: "+exception.getMessage(),exception);
	    }
        
        if (!signFactory.isEncrypted(secureRequest))
            return secureRequest;
        Document domRequest = SignatureFactory.String2DOM(secureRequest);

        request =
                SignatureFactory.Node2String(signFactory.decrypt(domRequest
                        .getDocumentElement()));
        return request;
    }

    /**
     * Decrypt of an element Document.
     * 
     * @param secureRequest
     *            String with a SOAP message encrypted
     * @return SOAP Message without encryption
     */
    public Document decrypt(final Element secureRequest) throws Exception {

        Document request = null;

        if (secureRequest == null) {
        	log.debug("Secure request it is null");
            return null;
        }

        SignatureFactory signFactory = null;
        try {
        	signFactory = SignatureFactory.getInstance();
	    } catch (final Exception exception) {
//	    	log.debug("WARNING: It couldn't be initialized the module: "+exception.getMessage(),exception);
	    	log.error("WARNING: It couldn't be initialized the module: "+exception.getMessage(),exception);
	    }

        if (!signFactory.isEncrypted(secureRequest))
            return secureRequest.getOwnerDocument();
        //get alias
  //      this.getUserRole(secureRequest)
        
        try  {
        	request = signFactory.decrypt(secureRequest);
        } catch (final Exception e2) {
//	    	log.debug("WARNING: It couldn't be decrypted: "+e2.getMessage(),e2);
	    	log.error("WARNING: It couldn't be decrypted: "+e2.getMessage(),e2);
	    	throw e2;
        }
        return request;
    }

    /**
     * Decrypt of an element Document.
     * 
     * @param secureRequest
     *            String with a SOAP message encrypted
     * @return SOAP Message without encryption
     */
    public Document decrypt(final Document secureRequest) throws Exception {
        return decrypt(secureRequest.getDocumentElement());
    }

    /**
     * Encrypt the SOAP message.
     * 
     * @param request
     *            A String SOAP message
     * @return SOAP message encrypted
     */
    @Deprecated
    public String encrypt(final String request,String identifier) throws Exception {
        String secureRequest = "";

        if (request == null) {
            return secureRequest;
        }
        Document domRequest = SignatureFactory.String2DOM(request);

        secureRequest =
                SignatureFactory.Node2String(this.encrypt(domRequest
                        .getDocumentElement(),identifier));
        return secureRequest;
    }

    /**
     * Encrypt the SOAP message.
     * 
     * @param request
     *            A String SOAP message
     * @return SOAP message encrypted
     */
    public Document encrypt(Element request,String alias) throws Exception  {
        Document secureRequest = null;
        // request = (Element)
        // request.getElementsByTagName("soap:Body").item(0);
        if (request == null) {
            return secureRequest;
        }
        SignatureFactory signFactory = null;
        try {
            signFactory = SignatureFactory.getInstance();
	    } catch (final Exception exception) {
//	    	log.debug("WARNING: It couldn't be initialized the module: "+exception.getMessage(),exception);
	    	log.error("WARNING: It couldn't be initialized the module: "+exception.getMessage(),exception);
	    }
            //get Information of the message
            
            
		    try {
	            secureRequest = signFactory.encrypt(request,alias);
		    } catch (final Exception e2) {
//		    	log.debug("WARNING: It couldn't be encrypted: "+e2.getMessage(),e2);
		    	log.error("WARNING: It couldn't be encrypted: "+e2.getMessage(),e2);
		    	throw e2;
		    }
		    
            return secureRequest;
    }
    

//    /**
//     * Get the user role information
//     * 
//     * @param secureRequest
//     * @return
//     * @throws Exception 
//     */
//    public String getUserRole(final Document secureRequest) throws Exception {
//        if (secureRequest == null) {
//            return "";
//        }
//
//        final SignatureFactory signFactory = SignatureFactory.getInstance();
//        
//        if (!signFactory.isSigned(secureRequest.getDocumentElement())){
//            return "";
//        }
//        
//        // TODO: Use aaatk
//        
//        return "admin";
//    }

    /**
     * Check if a message is encrypted
     * 
     * @param request
     *            String with the SOAP Message
     * @return result of the operation
     */
    @Deprecated
    public boolean isEncrypted(final String msgRequest)  {
        boolean isEncrypted = false;
        SignatureFactory signFactory = null;
        try {
            signFactory = SignatureFactory.getInstance();
        } catch (final Exception exception) {
//        	log.debug("WARNING: It couldn't be initialized the module: "+exception.getMessage(),exception);
        	log.error("WARNING: It couldn't be initialized the module: "+exception.getMessage(),exception);
        	return false;
        }
        try {
            isEncrypted = signFactory.isEncrypted(msgRequest);
	    } catch (final Exception exception) {
//	    	log.debug("WARNING: It couldn't be checked the encryption "+exception.getMessage(),exception);
	    	log.error("WARNING: It couldn't be checked the encryption "+exception.getMessage(),exception);
	    	//throw exception;
	    	return false;
	    }
        return isEncrypted;
    }

    /**
     * Check if a message is encrypted
     * 
     * @param request
     *            String with the SOAP Message
     * @return result of the operation
     * @throws Exception 
     */
    public boolean isEncrypted(final Document msgRequest)  {
        boolean isEncrypted = false;
        SignatureFactory signFactory = null;
     try {   
        
            signFactory = SignatureFactory.getInstance();
	    } catch (final Exception exception) {
//        	log.debug("WARNING: It couldn't be initialized the module: "+exception.getMessage(),exception);
        	log.error("WARNING: It couldn't be initialized the module: "+exception.getMessage(),exception);
	    	return false;
	    }
        try {    
            isEncrypted =
                    signFactory.isEncrypted(msgRequest.getDocumentElement());
        } catch (final Exception exception) {
  //       	log.debug("WARNING: It couldn't be checked the encryption "+exception.getMessage(),exception);
        	log.error("WARNING: It couldn't be checked the encryption "+exception.getMessage(),exception);
        	//throw exception;
        	return false;
        }

        return isEncrypted;
    }

    /**
     * Check if a message is signed
     * 
     * @param request
     *            String with the SOAP Message
     * @return result of the operation
     * @throws Exception 
     */
    @Deprecated
    public boolean isSigned(final String msgRequest) throws Exception {
        boolean isSigned = false;

        SignatureFactory signFactory = null;
        try {
            signFactory = SignatureFactory.getInstance();
        } catch (final Exception exception) {
//        	log.debug("WARNING: It couldn't be initialized the module "+exception.getMessage(),exception);
        	log.error("WARNING: It couldn't be initialized the module "+exception.getMessage(),exception);
        	//throw exception;
        	return false;
        }
            isSigned = signFactory.isSigned(msgRequest);

        return isSigned;
    }

    /**
     * Check if a message is signed
     * 
     * @param request
     *            Document with the SOAP Message
     * @return result of the operation
     */
    public boolean isSigned(final Document msgRequest) 
    //throws Exception
    {
        boolean isSigned = false;
        
        SignatureFactory signFactory = null;
        	this.log.debug("Checking if it is signed...");
        	this.log.debug(msgRequest);
//        	this.log.debug(XmlUtils.toString(msgRequest));
            this.log.debug("............................");
            try {
            	signFactory = SignatureFactory.getInstance();
	        } catch (final Exception exception) {
	        	log.debug("WARNING: It couldn't be initialized the module: "+exception.getMessage(),exception);
//	        	log.error("WARNING: It couldn't be initialized the module: "+exception.getMessage(),exception);
//	        	throw exception;
	        	return false;
	        }
	        try {
	        	isSigned = signFactory.isSigned(msgRequest.getDocumentElement());
	        } catch (final Exception exception) {
	        	this.log.error("WARNING: It couldn't be checked the signature: "+exception.getMessage(),exception);
//	        	this.log.debug("WARNING: It couldn't be checked the signature: "+exception.getMessage(),exception);
	        	return false;
	        }

        return isSigned;
    }

    /**
     * Check a SOAP message signed, and validate it
     * 
     * @param secureRequest
     * @return It is true if the request is valid
     */
    @Deprecated
    public boolean isValid(String secureRequest) throws Exception {
        // TODO: implement isValid
        if (secureRequest == null) {
            return false;
        }

        	Document domRequest = null;
            SignatureFactory signFactory = null;
            try {
            	domRequest = SignatureFactory.String2DOM(secureRequest);
            	signFactory = SignatureFactory.getInstance();
            } catch (final Exception e) {
            	log.error("WARNING: It was impossible get a instance: "+e.getMessage(),e);
//            	log.debug("WARNING: It was impossible get a instance: "+e.getMessage(),e);
            	throw e;
//                return null;
            }
            if (!signFactory.isSigned(domRequest.getDocumentElement()))
                return false;
            return (null != this.isValid(domRequest.getDocumentElement()));
    }

    /**
     * Check a SOAP Elemen body signed, and validate it
     * 
     * @param secureRequest
     * @return It is true if the request is valid
     */
    public HashMap<String, String> isValid(Element secureRequest) throws Exception{
        if (secureRequest == null) {
        	log.debug("WARNING: Secure request is null");
            return null;
        }
        SignatureFactory signFactory = null;
        try {

        	signFactory = SignatureFactory.getInstance();
        } catch (final Exception e) {
        	log.error("WARNING: It was impossible get a instance: "+e.getMessage(),e);
 //       	log.debug("WARNING: It was impossible get a instance: "+e.getMessage(),e);
        	//throw e;
            return null;
        }

        return signFactory.isValid(secureRequest);

    }

    /**
     * Check a SOAP Elemen body signed, and validate it
     * 
     * @param secureRequest
     * @return It is true if the request is valid
     */
    public HashMap<String, String> isValid(final Document secureRequest)  throws Exception {
        return isValid(secureRequest.getDocumentElement());
    }

    /**
     * Sign a String message. it calls the factory for the signing
     * 
     * @param msgRequest
     *            String with the SOAP Message
     * @return A SOAP message with the signature
     * @throws Exception
     */
    @Deprecated
    public String sign(final String msgRequest) throws Exception {
    	Document domRequest =null;
    	try {
    		  domRequest = SignatureFactory.String2DOM(msgRequest);
	    } catch (final Exception e) {
	    	log.error("WARNING: It was impossible parse string to DOM "+e.getMessage(),e);
//	    	log.debug("WARNING: It was impossible parse string to DOM: "+e.getMessage(),e);
	    	//throw e;
	    }
      

        final String encryptedRequest =
                SignatureFactory.Node2String(this.sign(domRequest
                        .getDocumentElement()));
        return encryptedRequest;
    }

    /**
     * Sign a String message. it calls the factory for the signing
     * 
     * @param msgRequest
     *            String with the SOAP Message
     * @return A SOAP message with the signature
     * @throws Exception
     */
    public Element sign(Element nodeRequest) throws Exception {
    	SignatureFactory signFactory = null;
    	try {
            signFactory = SignatureFactory.getInstance();
	    } catch (final Exception e) {
	    	log.error("WARNING: It was impossible get a instance: "+e.getMessage(),e);
//	    	log.debug("WARNING: It was impossible get a instance: "+e.getMessage(),e);
	    	//throw e;
	    }

        final Element resultElement = signFactory.sign(nodeRequest);

        return resultElement;
    }

    /**
     * Sign a String message. it calls the factory for the signing
     * 
     * @param msgRequest
     *            String with the SOAP Message
     * @return A SOAP message with the signature
     */
    @Deprecated
    public String removeSignature(final String msgRequest) throws Exception {

    	SignatureFactory signFactory = null;
    	try {
            signFactory = SignatureFactory.getInstance();
	    } catch (final Exception e) {
	    	log.error("WARNING: It was impossible get a instance: "+e.getMessage(),e);
	//    	log.debug("WARNING: It was impossible get a instance: "+e.getMessage(),e);
	    	//throw e;
	    }
       try {
            final String strRequest = signFactory.removeSignature(msgRequest);
            return strRequest;

        } catch (final Exception e) {
           log.error("WARNING: it couldn't be removed the signature: "+e.getMessage(),e);
 //          log.debug("WARNING: it couldn't be removed the signature: "+e.getMessage(),e);
           throw e;
        }
    }

    /**
     * Sign a String message. it calls the factory for the signing
     * 
     * @param msgRequest
     *            String with the SOAP Message
     * @return A SOAP message with the signature
     */
    public Document removeSignature(final Document msgRequest) throws Exception{

	    	SignatureFactory signFactory = null;
	    	try {
	            signFactory = SignatureFactory.getInstance();
		    } catch (final Exception e) {
		    	log.error("WARNING: It was impossible get a instance: "+e.getMessage(),e);
//		    	log.debug("WARNING: It was impossible get a instance: "+e.getMessage(),e);
		    	//throw e;
		    }            
            
            try {
            	return signFactory.removeSignature(msgRequest);
	        } catch (final Exception e) {            
	        	log.error("WARNING: it couldn't be removed the signature: "+e.getMessage(),e);
//	        	log.debug("WARNING: it couldn't be removed the signature: "+e.getMessage(),e);
	            throw e;
	        }

    }
    
    public final String getGroupByUser(final String user) {
        try {
            return Config.getString("groups", user.replace(" ", ""));
        } catch (MissingResourceException e) {
            return null;
        }
    }
    

    
    public boolean getIdHashFlagSecurity(String key) throws OperationNotAllowedFaultException {
    	List elems = getIdHash(key);
    	if (elems == null) throw new OperationNotAllowedFaultException();
    	return ((Boolean) elems.get(1)).booleanValue();
    	
    }
    
    public String getIdHashIdentifier(String key) throws OperationNotAllowedFaultException {
    	List elems = getIdHash(key);
    	if (elems == null) throw new OperationNotAllowedFaultException();
    	return ((String)elems.get(0));
    	
    }

  public List getIdHash(String key) {
	log.debug("Getting an element to the hashtable");
	Enumeration<String> enumKeys = hashIds.keys();
	List listElements = null;
	
	//print all the values
    	while(enumKeys.hasMoreElements())  {
    		String strKey = enumKeys.nextElement();
    		listElements= hashIds.get(strKey);
    		String ident = (String)listElements.get(0);
    		Boolean flag= (Boolean)listElements.get(1);
    		
    		log.debug("key: "+strKey+ " identifier: "+ident
    				+ " flagsecurity: "+flag.toString());
    	}
	//get the correct value
	listElements = hashIds.get(key);
//	hashIds.remove(key);
	return listElements;
	
}
    
    public void setIdHash(String key, String identifier, boolean flagSecurity) {
    	log.debug("put an element to the hashtable");
    	if (hashIds.size() > 30) hashIds.clear(); //clean the hashtable 
    	
    	List listElements = new ArrayList();
    	listElements.add(identifier);
    	listElements.add(new Boolean(flagSecurity));
    	
    	//set the correct value
    	hashIds.put(key,listElements);
    	Enumeration<String> enumKeys = hashIds.keys();
    	
    	//print all the values
	    	while(enumKeys.hasMoreElements())  {
	    		String strKey = enumKeys.nextElement();
	    		listElements= hashIds.get(strKey);
	    		String ident = (String)listElements.get(0);
	    		Boolean flag = (Boolean)listElements.get(1);
	    		
	    		log.debug("key: "+strKey+ " identifier: "+ident
	    				+ " flagsecurity: "+flag.toString());
	    	}
    }
    
    public boolean removeHash(String key) {
    	return (null!=hashIds.remove(key));
    }
    
    
}
