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


package eu.ist_phosphorus.harmony.common.security.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.muse.ws.addressing.soap.SoapFault;
import org.w3c.dom.Document;

import eu.ist_phosphorus.harmony.common.security.authn.HarmonyWSSecurity;
import eu.ist_phosphorus.harmony.common.security.authz.HarmonyPEP;
import eu.ist_phosphorus.harmony.common.security.g2mpls.SecurityG2MPLSHelper;
import eu.ist_phosphorus.harmony.common.security.utils.helper.ConfigHelper;
import eu.ist_phosphorus.harmony.common.security.utils.helper.SOAPHelper;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.exceptions.OperationNotAllowedFaultException;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;
import eu.ist_phosphorus.harmony.common.utils.Config;
import eu.ist_phosphorus.harmony.common.utils.PhLogger;

public class InputFilter {
    private final HarmonyWSSecurity authn;
    private final Logger log = PhLogger.getSeparateLogger("aai");
    private final HarmonyPEP pep;

    public InputFilter() throws SoapFault {
        this.authn = new HarmonyWSSecurity();
        this.pep = new HarmonyPEP();
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
    private final boolean managTrustedEntitiesInput(final Document request)
            {
			try {
	    		
    			//get WSAfrom    	
				//parse+generate alias
				String user = SOAPHelper.parseID(
						SOAPHelper.getFromAddress(request.getDocumentElement())
						);
    	
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
     * Check for Signature
     * 
     * @param request
     *            Request String
     * @throws OperationNotAllowedFaultException
     *             If Signature is invalid
     */
    private final String checkSignature(final Document request)
            throws OperationNotAllowedFaultException {
        // Server-Code -> Requests are inbound

        // If Not signed but have to
        if (ConfigHelper.inboundRequestSigning()
                && !this.authn.isSigned(request)) {
            final String error = "AuthN: Request is not signed - but have to.";
            this.log.error(error);
            throw new OperationNotAllowedFaultException(error);
        }
        HashMap<String, String> issuerData = null;
        
        try{
            issuerData = this.authn.isValid(request);       	
        } catch (Exception e) {
            final String error = "AuthN: Request is not valid - it couldn't check the signature.";
            this.log.debug("WARNING: AuthN: Request is not valid - it couldn't check the signature.");
            this.log.error(error,e);
            throw new OperationNotAllowedFaultException(error);
       
        }

        // If Signed and not valid
        if (this.authn.isSigned(request) && (null == issuerData)) {
            final String error = "AuthN: Request is not valid.";
            this.log.error(error);
            throw new OperationNotAllowedFaultException(error);
        }

        
        String user = null;
        if (null != issuerData) {
            user = issuerData.get("CN");
        }

        // If signed and user unknown
        if (this.authn.isSigned(request) && (null == user)) {
            throw new OperationNotAllowedFaultException("Unknown user");
        } 

        return user;
    }

    /**
     * Check if request is Encrypted.
     * 
     * @param request
     *            Request String
     * @throws OperationNotAllowedFaultException
     *             If request is not encrypted but have too
     */
    private final void checkEncryption(final Document request)
            throws OperationNotAllowedFaultException {
        // Server-Code -> Requests are inbound
        if (ConfigHelper.inboundRequestEncryption()) {
            if (!this.authn.isEncrypted(request)
            		&& !this.authn.isSigned(request)) {
                final String error =
                        "AuthN: Request is not encrypted or signed - but have to.";
                this.log.error(error);
                throw new OperationNotAllowedFaultException(error);
            }
        }
    }

    /**
     * Check if user is authorised.
     * 
     * @param request
     *            Request String
     * @throws UnexpectedFaultException
     *             In case of any parsing errors
     * @throws OperationNotAllowedFaultException
     *             If user is not Authorised
     */
    private final Document checkAuthorisation(final String user,
            final Document request) throws UnexpectedFaultException,
            OperationNotAllowedFaultException {
        final String userName;
        final String userRole;

        if (this.authn.isSigned(request) && (null != user)) {
            userName = user;
            userRole = this.authn.getGroupByUser(user);

            if (null == userRole) {
                throw new OperationNotAllowedFaultException("Unknown user role");
            }
        } else {
            userName = "notsigned";
            userRole = "admin";
        }
        Document newRequest = null;
        try {
        	newRequest = authn.removeSignature(request);
		} catch (Exception e) {
			log.debug("WARNING: AuthZ: It was impossible remove the signature");
			log.error(e.getMessage(),e);
			throw new OperationNotAllowedFaultException();

		}
        this.log.debug("INPUT: Username: " + userName + ", Userrole: " + userRole);
        this.log.debug("INPUT: Starting authorization...");

        if (Config.isTrue("aai", "force.request.authz")) {
            boolean isValid = false;

            try {
                isValid = this.pep.pre(newRequest, userName, userRole);
            } catch (OperationNotAllowedFaultException e) {
                throw e;
            } catch (Exception e) {
                throw new UnexpectedFaultException(e);
            }

            if (!isValid) {
                this.log.debug("WARNING: AuthZ: Request not allowed");
                this.log.error("AuthZ: Request not allowed");
                throw new OperationNotAllowedFaultException("AuthZ: Request not allowed");
            }
            this.log.debug("...done.");
            
        } else {
            this.log.debug("...not enabled.");
            
        }

        return newRequest;
    } 

    public final Document apply(final Document doc)
            throws OperationNotAllowedFaultException, UnexpectedFaultException{
        Document result = doc;
        
        //FIXME THIS WORKFLOW ONLY IS FOR DEMOSTRATION
        //TODO THIS IF MUST BE DELETED
        /* Check if the request is sent from a trusted entity */
        if (managTrustedEntitiesInput(doc))	{
        	this.log.debug("INPUT: It is a trusted entity");
        	String UUID = null;  //It is an UUID 
        	String user = null;  //It is an user 
			try {
				UUID  =  SOAPHelper.getUUID(doc.getDocumentElement());
				user = SOAPHelper.parseID(SOAPHelper.getFromAddress(doc.getDocumentElement()));

			} catch (IOException e) {
				log.debug("WARNING: IO Problems when it extracted the UUID, user of a trusted entity");
				log.error(e.getMessage(),e);
				throw new OperationNotAllowedFaultException();
			} 			
			//generate tuple
        	this.authn.setIdHash(UUID,user,false);
        	return result;
        }
        
        //TODO G2MPLS - Check if it is a G2MPLS trusted entity

		      
		this.log.debug("INPUT: Checking Signature...");
//		Check signature
        final String user = this.checkSignature(result);

        this.log.debug("INPUT: Checking encryption...");
//		Check encryption
        this.checkEncryption(result);

        //Store the identifier
        String UUID = null;
        try {
			UUID = SOAPHelper.getUUID(doc.getDocumentElement());
			
		} catch (IOException e) {
			log.debug("WARNING: UUID couldn't be extracted");
			log.error(e.getMessage(),e);
			throw new OperationNotAllowedFaultException();

		}	
		try {
			this.log.debug("INPUT: Decrypting...");
			result = this.authn.decrypt(result);
		} catch (Exception e) {
			log.debug("WARNING: Decrypt - the message couldn't be decrypted");
			log.error(e.getMessage(),e);
			throw new OperationNotAllowedFaultException();

		}
        this.log.debug("INPUT: user: "+user);
        this.log.debug("INPUT: Authorising...");
        result = this.checkAuthorisation(user, result);
        
        this.authn.setIdHash(UUID,user,true);
        return result;
    }
}
