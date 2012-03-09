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
package eu.ist_phosphorus.harmony.common.security.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.muse.util.messages.Messages;
import org.apache.muse.util.messages.MessagesFactory;
import org.apache.muse.util.xml.XmlUtils;
import org.apache.muse.ws.addressing.EndpointReference;
import org.apache.muse.ws.addressing.soap.SimpleSoapClient;
import org.apache.muse.ws.addressing.soap.SoapClient;
import org.apache.muse.ws.addressing.soap.SoapConstants;
import org.apache.muse.ws.addressing.soap.SoapFault;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import eu.ist_phosphorus.harmony.common.security.authn.HarmonyWSSecurity;
import eu.ist_phosphorus.harmony.common.security.authz.HarmonyPEP;
import eu.ist_phosphorus.harmony.common.security.g2mpls.*;
import eu.ist_phosphorus.harmony.common.security.utils.Request;
import eu.ist_phosphorus.harmony.common.security.utils.helper.ConfigHelper;
import eu.ist_phosphorus.harmony.common.security.utils.helper.MuseHelper;
import eu.ist_phosphorus.harmony.common.security.utils.helper.SOAPHelper;
import eu.ist_phosphorus.harmony.common.security.utils.helper.TokenHelper;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.exceptions.OperationNotAllowedFaultException;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;
import eu.ist_phosphorus.harmony.common.utils.Config;
import eu.ist_phosphorus.harmony.common.utils.PhLogger;

/**
 * @author gassen
 * 
 */
public class SecureSoapClient extends SimpleSoapClient implements SoapClient {

    // Used to lookup all exception messages
    private static Messages _MESSAGES =
            MessagesFactory.get(SimpleSoapClient.class);

    private final Logger log = PhLogger.getSeparateLogger("aai");

    private static final Element[] _EMPTY_ARRAY = new Element[0];

    private final HarmonyWSSecurity authn;
    private HashMap<String,String> hashTokens;

    /**
     * 
     */
    public SecureSoapClient() {
        try {
            this.authn = new HarmonyWSSecurity();
            hashTokens = new HashMap<String,String>();
        } catch (SoapFault e) {
            throw new RuntimeException("Unable to create SecureSoapClient: "
                    + e.getMessage(), e);
        }
    }

    /** Method to get entities from a file
     * 
     * @param request
     * @return
     */
    private  final List<String> getEntities (){
		List<String> listEntities = new ArrayList<String>();
		try  {
			int numEntities = Config.getInt("aai","numEntities");
			for (int i = 0; i<numEntities; i++) {
				String entity = Config.getString("aai", "entity"+String.valueOf(i)+".name");
				listEntities.add(entity);
			}
		} catch (Exception e) {
			this.log.debug("WARNING: File with trusted entities couldn't be loaded");
			this.log.error(e.getMessage(),e);
		}
		return listEntities;
    	
    }
    
    /**
	 * The method for the entities management
	 */
    private final boolean managTrustedEntities(final Document request, String URI)
            {
			try {
				//get WSAfrom    	
				//parse+generate alias
				String user = SOAPHelper.parseID(URI);
    	
	    		//Read entities
	    		List<String> listEntities = getEntities();	    		
	    		return listEntities.contains(user);
	    		
			} catch (IOException e) {
				this.log.debug("WARNING: I was impossible to parse the From address");
				this.log.error(e.getMessage(),e);
				return false;
			} catch (OperationNotAllowedFaultException e) {
				this.log.debug("WARNING: It is null the parameter in the parseID operation");
				this.log.error(e.getMessage(),e);
				return false;
			}
    		
    }
    
    /**
     * Check if the request message is for a trusted entity
     * @param request Request message
     * @return it can be checked or not
     */
    public final boolean isEntityRequest(final Document request) {
    	try {
			return managTrustedEntities(request,SOAPHelper.getTo(request.getDocumentElement()));
		} catch (Exception e) {
			this.log.debug("WARNING: It couldn't get \"TO\" URI");
			this.log.error(e.getMessage(),e);
			return false;
		}
    	
    }

    /**
     * Check if the response message is for a trusted entity
     * @param re message
     * @return it can be checked or not
     */    
    public final boolean isEntityResponse(final Document response) {
    	try {
			return managTrustedEntities(response,SOAPHelper.getFromAddress(response.getDocumentElement()));
		} catch (Exception e) {
			this.log.debug("WARNING: It couldn't get \"FROM\" URI",e);
			this.log.error(e.getMessage(),e);
			return false;
		}
    }

    
    /**
     * Process the harmony Request: Encrypt and add Signature.
     * 
     * @param req
     * @return
     * @throws Exception
     */
    public final Element processRequest(final Element req) throws Exception {
        Element reqElement = req;

        try {
        	
        	/* Logging */
            this.log.debug("########################CLIENT REQUEST MESSAGE##########################");
            this.log.debug(XmlUtils.toString(req));
            this.log.debug("########################################################################");
//            
    		/* Replace token */
        	String gri =  SOAPHelper.getReservationID(req);
    		if (gri!=null &&
    				Request.isCancelReservation(req.getOwnerDocument())) {
    				String formatGRI=SOAPHelper.getFormatGRI(gri);
    				String tokenValue = HarmonyPEP.getHashToken(formatGRI);
    				this.log.debug("CLIENT: Captured Cancel Reservation..."+formatGRI);
    				this.log.debug("CLIENT: Captured token value.."+tokenValue);
    				if (tokenValue != null) {
    					TokenHelper.replaceTokenValue(req.getOwnerDocument(),tokenValue);   				
    					this.log.debug("CLIENT: The token is replaced in the message");
    				} else {
    					this.log.debug("CLIENT: Authz - WARNING - The request doesn't include the token");
    				}    				
    		}
    		
            //FIXME THIS WORKFLOW ONLY IS FOR DEMOSTRATION
            //TODO THIS IF MUST BE DELETED
    		/* Check trusted entities */
    		if (this.isEntityRequest(req.getOwnerDocument())) {
    			this.log.debug("CLIENT: It is a trusted entity");
    			return reqElement;
    		}

    		
    		
	        /* AAI Workflow */
	        if (ConfigHelper.outboundRequestEncryption() 
	        		&& ConfigHelper.outboundRequestSigning()) {
	            this.log.debug("CLIENT: Encrypting Message");
	            
	            String identifier = SOAPHelper.parseID(SOAPHelper.getTo(req));
//	            this.logger.debug("Identifier is: "+identifier);            
	            Document docElement = this.authn.encrypt(reqElement,identifier);
	            
	            //If the message couldn't be encrypted
	            if (docElement==null) {
	            	throw new OperationNotAllowedFaultException("The message could not be encryped");
	            }
	        }
	        
	        if (ConfigHelper.outboundRequestSigning()) {
	            this.log.debug("CLIENT: Signing Message");
	            reqElement = this.authn.sign(reqElement);
	        }
	        
	    } catch (OperationNotAllowedFaultException e) {
	        throw e;
	    } catch (final Exception e) {
	    	this.log.debug("WARNING: Error processing the request");
	        this.log.error(e.getMessage(), e);
	        //TODO add to the hash table...
	        
	    }

        return reqElement;
    }

    /**
     * Process the Incoming Response: Decrypt and validate.
     * 
     * @param response
     * @return
     */
    public final Document processResponse(final Document response)
            throws OperationNotAllowedFaultException {

        Document resDocument = response;

        // Client Code -> Responses are inbound!
        try {


//        		//This operator specifies if it is working with a G2MPLS configuration
//        		if (!SecurityG2MPLSHelper.isEnableG2MPLSManagement()) {
        	
            		//FIXME THIS WORKFLOW ONLY IS FOR DEMOSTRATION
            		//TODO THIS IF MUST BE DELETED
					/* Check trusted entities */
					if (this.isEntityResponse(response)) {
						this.log.debug("CLIENT: It is a trusted entity");
						return response;
					}
					
        			/* AAI Workflow **/
	                if (ConfigHelper.inboundResponseSigning()) {
	                    if (!this.authn.isSigned(response)) {
	                        throw new OperationNotAllowedFaultException(
	                                "Response is not signed");
	                    } else if (null == this.authn.isValid(resDocument)) {
	                        throw new OperationNotAllowedFaultException(
	                                "Response signature is not valid");
	                    } else {
	                        resDocument = this.authn.removeSignature(resDocument);
	                    }
	                }
	                
	                if (ConfigHelper.inboundResponseEncryption()) {
	                    if (this.authn.isEncrypted(resDocument)) {
	                        resDocument = authn.decrypt(resDocument);
	                    } else {
	                        throw new OperationNotAllowedFaultException(
	                                "Response is not encrypted");
	                    }
	                }
	                
//        		}
                //Authenticate
                this.log.debug("########################CLIENT RESPONSE MESSAGE########################");
               this.log.debug(XmlUtils.toString(response));
                this.log.debug("########################################################################");
                managTokenClient(resDocument);
                
               
                
                
                

        } catch (OperationNotAllowedFaultException e) {
            throw e;
        } catch (final Exception e) {
            this.log.debug("WARNING: Error processing Response");
	        this.log.error(e.getMessage(), e);

            resDocument = response;
        }

        return resDocument;
    }
    
    
    /**
     * @param doc
     * @throws Exception
     */
    private final void managTokenClient(final Document doc) throws Exception {       
           //Capture the token for the GRI selected
        	if(Request.isCreateReservationResponse(doc)) {
        		
        		final Request requestParser = new Request(doc);
                final String gri =SOAPHelper.getFormatGRI(requestParser.getGRI());
                this.log.debug("CLIENT: AuthZ: GRI is: " + gri);
                String tokenValue = TokenHelper.getToken(doc);
                if (tokenValue != null) {
                   this.log.debug("CLIENT: AuthZ: get token value: " + tokenValue);
                   this.log.debug("CLIENT: Storing GRI to the hash...");
            		if (hashTokens.size()>50) {
            			hashTokens = new HashMap<String,String>();
            		}
                    HarmonyPEP.setHashToken(gri,tokenValue);                  	
                } else {
                	this.log.debug("Authz: WARNING - It is not received token");
                }
        		            
        	}
    }


    /**
     * Wrap Exceptions to Element responses.
     * 
     * @param e
     * @return
     */
    private final Element[] throwWraper(final Exception e) {
        SoapFault soapFault = null;

        if (SoapFault.class.isInstance(e)) {
            // Terrible Hack to change Message...
            try {
                Constructor<?> construct =
                        e.getClass().getConstructor(String.class);
                soapFault =
                        (SoapFault) construct.newInstance("Client Error: "
                                + e.getMessage());
            } catch (Exception e1) {
                soapFault = new SoapFault("Client Error: " + e.getMessage());
            }

            soapFault.initCause(soapFault.getCause());
            soapFault.setStackTrace(soapFault.getStackTrace());
        } else {
            soapFault =
                    new UnexpectedFaultException("Client Error: "
                            + e.getMessage(), e);
        }

        return new Element[] { soapFault.toXML() };
    }

    @Override
    public Element[] send(final EndpointReference src,
            final EndpointReference dest, final String wsaAction,
            Element[] body, Element[] extraHeaders) {
        if (dest == null) {
            throw new NullPointerException(SecureSoapClient._MESSAGES
                    .get("NullDestinationEPR"));
        }

        if (wsaAction == null) {
            throw new NullPointerException(SecureSoapClient._MESSAGES
                    .get("NullActionURI"));
        }

        if (body == null) {
            body = SecureSoapClient._EMPTY_ARRAY;
        }

        if (extraHeaders == null) {
            extraHeaders = SecureSoapClient._EMPTY_ARRAY;
        }
     
        Element soapRequest =
                this.createMessage(src, dest, wsaAction, body, extraHeaders);      

        try {
            soapRequest = MuseHelper.prepareElementForMuse(soapRequest);
            
            /* TODO Create token for a createReservation, it is added in the SOAP message */ 
            if (SecurityG2MPLSHelper.isEnableG2MPLSManagement()
            		&& new Request(soapRequest.getOwnerDocument()).isCreateReservationRequest() ) {      
                soapRequest = SecurityG2MPLSHelper.processRequest(soapRequest);            	            	

            } else {    
                soapRequest = processRequest(soapRequest);            	
            }
            
        } catch (Exception e) {
            return throwWraper(e);
        }

        String strSoap = XmlUtils.toString(soapRequest, false, false);

        final byte[] soapBytes = strSoap.getBytes();

        if (this.isUsingTrace()) {
            this.trace(soapRequest, false);
        }

        Element soapResponse = null;

        try {

            // set up the HTTP request - POST of SOAP 1.2 data
            final URL url = this.getDestinationURL(dest);
            final HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type",
                    SoapConstants.CONTENT_TYPE_HEADER);
            connection.setDoOutput(true);
            connection.connect();
        
            // send the SOAP request...
            final OutputStream output = connection.getOutputStream();
            output.write(soapBytes);
            output.flush();
            output.close();

            final int responseCode = connection.getResponseCode();
            InputStream response = null;
            Document responseDoc = null;

            // only use getInputStream() if we got HTTP 200 OK
            // ---------------MESSAGE RESPONSE-----------------
            if (responseCode == HttpURLConnection.HTTP_OK) {
                response = connection.getInputStream();
                try {
                	responseDoc = XmlUtils.createDocument(response);
                } catch (Exception e) {
                	this.log.debug("WARNING: It couldn't create the response message");
                	this.log.error(e.getMessage(),e);
                }
              
                /* TODO Validate response message if it is a G2MPLS trusted entity */
                responseDoc = this.processResponse(responseDoc);
            } else {
                response = connection.getErrorStream();
                responseDoc = XmlUtils.createDocument(response);
            }
            
            // read in the response and build an XML document from it
            soapResponse = XmlUtils.getFirstElement(responseDoc);

            response.close();
            connection.disconnect();

        } catch (final SoapFault e) {
            return this.throwWraper(e);
        } catch (final Throwable error) {
            final SoapFault soapFault =
                    new SoapFault(error.getMessage(), error);
            return new Element[] { soapFault.toXML() };
        }

        if (this.isUsingTrace()) {
            this.trace(soapResponse, true);
        }

        // return the elements inside the SOAP body
        final Element responseBody =
                XmlUtils.getElement(soapResponse, SoapConstants.BODY_QNAME);
        return XmlUtils.getAllElements(responseBody);
    }
    

}
