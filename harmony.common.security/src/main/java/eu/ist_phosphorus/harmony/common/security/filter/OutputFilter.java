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

import org.apache.log4j.Logger;
import org.apache.muse.ws.addressing.soap.SoapFault;
import org.w3c.dom.Document;

import eu.ist_phosphorus.harmony.common.security.authn.HarmonyWSSecurity;
import eu.ist_phosphorus.harmony.common.security.authz.HarmonyPEP;
import eu.ist_phosphorus.harmony.common.security.utils.Request;
import eu.ist_phosphorus.harmony.common.security.utils.helper.ConfigHelper;
import eu.ist_phosphorus.harmony.common.security.utils.helper.SOAPHelper;
import eu.ist_phosphorus.harmony.common.security.utils.helper.TokenHelper;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CreateReservationResponse;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.exceptions.OperationNotAllowedFaultException;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.exceptions.OperationNotSupportedFaultException;
import eu.ist_phosphorus.harmony.common.utils.Config;
import eu.ist_phosphorus.harmony.common.utils.PhLogger;

public class OutputFilter {
    private final HarmonyWSSecurity authn;
    private final Logger log = PhLogger.getSeparateLogger("aai");

    public OutputFilter() throws SoapFault {
        this.authn = new HarmonyWSSecurity();
    }

    /**
     * @param doc
     * @throws Exception
     */
    private final Document managTokenServerOutput(final Document doc) throws Exception {
        Document response = doc;

        final HarmonyPEP pep = new HarmonyPEP();

        try {
                	
        	if(Request.isCreateReservationResponse(response)) {        		
        	/* Token management */	        		
                final Request requestParser = new Request(response);
                final String gri = SOAPHelper.getFormatGRI(requestParser.getGRI());
            	final Object rawRequest = requestParser.getRawRequest();
                this.log.debug("OUTPUT: AuthZ: GRI is: " + gri);
               	final String token = pep.post(gri);
                this.log.debug("OUTPUT: Token: " + token);
                CreateReservationResponse obj =
                        (CreateReservationResponse) rawRequest;

                this.log.debug("OUTPUT: Adding Token");
                obj.getCreateReservationResponse().setToken(token);
                response = TokenHelper.addTokenCreate(response, TokenHelper.getTokenValue(token));           
            }
        	
        } catch (final OperationNotSupportedFaultException exception) {
            // If Object contains no GRI -> Skip
        	this.log.debug("WARNING: The message doesn't contain GRI");
            this.log.error(exception.getMessage(),exception);
        } catch (final Exception e2) {
            // If Object contains no GRI -> Skip
        	
        	this.log.debug("WARNING: Problem in the Outputfilter");   	
            this.log.error(e2.getMessage(),e2);
        }

        return response;
    }
    
    
    /**
     * The method for the entities management. It checks if the UUID (identifier inside the response) includes some reference.
     */
    private final boolean managTrustedEntitiesOutput(final Document request)
    {
				try {
					String UUID = SOAPHelper.getUUID(request.getDocumentElement());
					//get Identifier
					return ((Boolean)this.authn.getIdHashFlagSecurity(UUID)).booleanValue();
				
				} catch (IOException e) {
					return true;
				} catch (OperationNotAllowedFaultException e) {
					return true;
				}   		
	}      
    

    /**
     * @param doc
     * @return
     */
    private final Document sign(final Document doc) {
        Document response = doc;
        try {
            response =
                    this.authn.sign(response.getDocumentElement())
                            .getOwnerDocument();
        } catch (Exception e) {
        	this.log.debug("WARNING: Unable to sign response");
            this.log.error(e.getMessage(), e);
        }

        return response;
    }

    /**
     * @param doc
     * @return
     */
    private final Document encrypt(final Document doc, String UUID) throws OperationNotAllowedFaultException {
        Document response = doc;        
        String identifier = this.authn.getIdHashIdentifier(UUID);
		try {
        	response = this.authn.encrypt(response.getDocumentElement(),identifier);
		} catch (Exception e) {
        	this.log.debug("WARNING: Unable to encrypt response");
		    this.log.error(e.getMessage(), e);
		}

        return response;
    }  
    
    /**
     * @param request
     * @return
     * @throws Exception
     */
    public final Document apply(final Document request) throws Exception {
        Document response = request;

        
        //FIXME THIS WORKFLOW ONLY IS FOR DEMOSTRATION
        //TODO THIS IF MUST BE DELETED
        /* Check if the response is sent to a trusted entity */
        String UUID="";
        //GET THE UUID
        try {
        	//Get UUID 
        	UUID = SOAPHelper.getUUID(request.getDocumentElement());
        	
        } catch (Exception e) {
        	this.log.debug("WARNING: It was impossible to get the UUID from the message");
		    this.log.error(e.getMessage(), e);
			throw new OperationNotAllowedFaultException();	
		}
        
        //TODO G2MPLS - Check if it is a G2MPLS if it is, you should change the managToken
        
        if (!managTrustedEntitiesOutput(request)) {
        	this.log.debug("OUTPUT: It is a trusted entity");
            this.authn.removeHash(UUID);
        	return response;
        }
        
        
        if (Config.isTrue("aai", "force.request.authz")) {
            this.log.debug("OUTPUT: Adding Token to response...");
            response = this.managTokenServerOutput(response);
        }
        
        if (ConfigHelper.outboundResponseEncryption()) {
            this.log.debug("OUTPUT: Encrypting...");
            response = this.encrypt(response,UUID);
        }

        if (ConfigHelper.outboundResponseSigning()) {
           this.log.debug("OUTPUT: Adding Signature...");
            response = this.sign(response);

        }
        
        //Remove the UUID
        this.authn.removeHash(UUID);        
        return response;
    }
}
