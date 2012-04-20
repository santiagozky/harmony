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


package org.opennaas.core.security;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import org.aaaarch.config.ConfigDomainsPhosphorus;
import org.aaaarch.config.ConstantsNS;
import org.aaaarch.gaaapi.ActionSet;
import org.aaaarch.gaaapi.PEP;
import org.aaaarch.gaaapi.ResourceHelper;
import org.aaaarch.gaaapi.SubjectSet;
import org.aaaarch.gaaapi.authn.AuthenticateSubject;
import org.aaaarch.gaaapi.tvs.TVS;
import org.aaaarch.gaaapi.tvs.TVSTable;
import org.aaaarch.gaaapi.tvs.TokenBuilder;
import org.aaaarch.gaaapi.tvs.XMLTokenType;
import org.apache.muse.util.xml.XmlUtils;
import org.apache.muse.ws.addressing.soap.SoapFault;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import org.opennaas.core.security.authn.HarmonyWSSecurity;
import org.opennaas.core.security.authn.SignatureFactory;
import org.opennaas.core.security.authz.HarmonyPEP;
import org.opennaas.core.security.authz.HarmonyTVS;
import org.opennaas.core.security.utils.Request;
import org.opennaas.core.security.utils.helper.SOAPHelper;
import org.opennaas.core.security.utils.helper.TokenHelper;
import org.opennaas.extension.idb.serviceinterface.databinding.jaxb.CreateReservation;
import org.opennaas.extension.idb.serviceinterface.databinding.jaxb.CreateReservationResponse;
import org.opennaas.extension.idb.serviceinterface.databinding.jaxb.exceptions.OperationNotAllowedFaultException;
import org.opennaas.core.utils.FileHelper;

import org.opennaas.core.utils.Config;


public class TestWorkflows {



    @Test
    public final void testmanagTrustedEntities() throws IOException, SoapFault {
    	int numEntities = Config.getInt("aai","numEntities");
    	for (int i = 0; i<numEntities; i++) {
    		String entity = Config.getString("aai", "entity"+String.valueOf(i)+".name");
    		System.out.println("Entity : "+entity);
    	}
    	
    	
    	
    }

	
    @Test
    public final void testFormatGRI() throws IOException, SoapFault {
    	 String gri = SOAPHelper.getFormatGRI("532@idb-adapter-hna1");
        System.out.println(gri);
        String[]griInfo = "532@idb-adapter-hna1".split("@");
        System.out.println("Length: "+griInfo.length);
    }
    
    
    @Test
    public final void testExtractTokenValue() throws IOException, SoapFault {
        final String token =
                FileHelper.readFile("resources/data/TokenSample.xml");
        
        System.out.println(TokenHelper.getTokenValue(token));
    }
    
    
    
    @Test
    public final void testManagementTokenWorkflow() {
    	
		System.out.println("====================== Test for security intradomain ======================");
    	
		try {
		HarmonyPEP pep = new HarmonyPEP();
        final String createRequest =
            FileHelper.readFile("resources/data/CreateReservation.xml");        
    	
    	pep.pre(SignatureFactory.String2DOM(createRequest),"admin","admin");
    	/* -- OUTPUT ADD TOKEN -- */
        
        String createResponse = FileHelper.readFile("resources/data/CreateReservationResponse.xml");

        
        Document response =   SignatureFactory.String2DOM(createResponse);
    	if(Request.isCreateReservationResponse(response)) {

            Request requestParser = new Request(response);
            String gri = SOAPHelper.getFormatGRI(requestParser.getGRI());
        	final Object rawRequest = requestParser.getRawRequest();
            System.out.println("AuthZ: GRI is: " + gri);
            
			final String token = pep.post("539");
	        final HarmonyTVS tvs = new HarmonyTVS();		
//	        
//	        final int validTime = 1440; // 24 hours
//	        final long notBefore = System.currentTimeMillis();
//	        final long notAfter = notBefore + validTime * 60 * 1000;
//	        
//	        tvs.addEntryTVSTable("539", notBefore,
//	                notAfter, "10.7.12.2", "10.3.17.4");
//
//	        final String token =
//	            HarmonyTVS.getXMLToken("539",
//	                        validTime - 1, false, new Date(notBefore), new Date(
//	                                notAfter - 1));
//	        
	        
//			boolean isValid = tvs.validate(TokenHelper.getTokenValue(token), ConfigDomainsPhosphorus.DOMAIN_PHOSPHORUS_VIOLA, "539");

            //String token = HarmonyPEP.hashGRIs.get(gri);
            System.out.println("Token: " + token);
            CreateReservationResponse obj =
                    (CreateReservationResponse) rawRequest;

            System.out.println("Adding Token");
            obj.getCreateReservationResponse().setToken(token);
            response = TokenHelper.addTokenCreate(response, token);
            System.out.println("=== RESPONSE MESSAGE WITH NEW TOKEN ===");
            System.out.println(XmlUtils.toString(response));
            System.out.println("========================================");
            /* ----- CLIENT ----- */          
            
            
            /* create a reservation response */
    		String cancelResponse = XmlUtils.toString(response);
            Element elemCancelResponse =  SignatureFactory.String2DOM(cancelResponse).getDocumentElement();
            Document doc = elemCancelResponse.getOwnerDocument();
        	if(Request.isCreateReservationResponse(doc)) {
        		System.out.println("It is a create reservation response");
                requestParser = new Request(doc);
                gri =SOAPHelper.getFormatGRI(requestParser.getGRI());
                System.out.println("AuthZ: GRI is: " + gri);
                String tokenValue = TokenHelper.getToken(doc);
                System.out.println("AuthZ: get token value: " + tokenValue);
                pep.setHashToken(gri,tokenValue);                
            }
            
            /* Cancel a reservation request */
            final String cancelRequest =
                FileHelper.readFile("resources/data/CancelReservation.xml");   
            
            Element cancelReq =   SignatureFactory.String2DOM(cancelRequest).getDocumentElement();
        	/* Replace token */
        	gri =  SOAPHelper.getReservationID(cancelReq);
    		if (gri!=null &&
    				Request.isCancelReservation(cancelReq.getOwnerDocument())) {
    				System.out.println("Captured Cancel Reservation...");
    				String tokenValue = TokenHelper.getTokenValue(pep.getHashToken(gri));
    				System.out.println("Captured token value.."+tokenValue);
    				Document domWithToken = TokenHelper.replaceTokenValue(cancelReq.getOwnerDocument(),tokenValue);
   				
    				System.out.println("The token is replaced in the message");
    	            System.out.println("=== CANCEL RESERVATION REQUEST===");
    	            System.out.println(XmlUtils.toString(cancelReq));
    	            System.out.println("========================================");
    				
    		}

            /* ----------------- */  
        	/* -- INPUT TOKEN -- */
    		boolean isValid = pep.pre(cancelReq.getOwnerDocument(), "", "");
			System.out.println("\nSimple Token validation by TVS: Token validity: " + (isValid? "=VALID=" : "=INVALID="));


            assertTrue(isValid);
                    	             
        }
        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
    @Test
    public final void testInterDomainManagTokenWorkflow() {

    	// Create token type 1. 
//  	   	String tokenxml = TokenBuilder.getXMLPilotToken(domainId, gri, 0, tokenKey, tt, null);
	   	//   		writeToFile(tokenxml, (tokenfile));
    	try {
    		System.out.println("====================== Test for security interdomain ======================");
    		
//    	// Validation token type 1.
    		String domainViola = ConfigDomainsPhosphorus.DOMAIN_PHOSPHORUS_VIOLA;
			String resourceInputURI = domainViola + "/harmony/" + "source=10.3.1.16/target=10.7.3.13";
   			// no policy for I2CAT
   			//resourceInputURI = domainI2CAT + "/harmony/" + "source=10.3.1.16/target=10.7.3.13";
			//resourceId = "http://testbed.ist-phosphorus.eu/resource-context/phosphorus";
			//resourceId = "http://testbed.ist-phosphorus.eu/resource-context/phosphorus/experiment=demo010";
			
			HashMap resmap = ResourceHelper.parseResourceURI(resourceInputURI);
			HashMap<String, String> actmap = new HashMap<String, String>();
			HashMap<String, String> subjmap = new HashMap<String, String>();
			//subjmap = SubjectSet.getSubjSetTest();
			
		    String action = ActionSet.NSP_CREATE_PATH;		
		   //action = "cancel";
		    actmap.put(ConstantsNS.ACTION_ACTION_ID, action); 
	    	// some modifications for experiments
			
	    	//subjmap.put(ConstantsNS.SUBJECT_CONTEXT, "demo001");		
  	
	        subjmap.put(ConstantsNS.SUBJECT_SUBJECT_ID, "junit@viola.testbed.ist-phosphorus.eu");
	        subjmap.put(ConstantsNS.SUBJECT_CONFDATA, AuthenticateSubject.AUTHN_SUBJECT_CONFIRMED);
	        subjmap.put(ConstantsNS.SUBJECT_ROLE, "admin");
	        subjmap.put(ConstantsNS.SUBJECT_CONTEXT,"demo041");    	
		    

        final String createRequest =
            FileHelper.readFile("resources/data/CreateReservation.xml"); 
        
        Document domRequest =  SignatureFactory.String2DOM(createRequest);
        NodeList nodes =
        	domRequest.getElementsByTagNameNS("http://ist_phosphorus.eu/nsp/webservice/reservation","createReservation");
        
        if (nodes!=null && nodes.getLength()>0) {

            Request requestParser = new Request(domRequest);
            String gri = domRequest.getElementsByTagNameNS("http://ist_phosphorus.eu/nsp/webservice/reservation", "GRI").item(0).getTextContent();
            String formatGRI = SOAPHelper.getFormatGRI(gri);
        
        	final Object rawRequest = requestParser.getRawRequest();
            System.out.println("AuthZ: OLD GRI is: " + formatGRI);
            
            String domainId = "http://testbed.ist-phosphorus.eu/viola";
            
            PEP.authorizeAction(resmap, actmap, subjmap);
            String tokenxml = TokenBuilder.getXMLToken(domainId, formatGRI, null, 0, false);

   
            
            System.out.println("...Token pilot created...");
            System.out.println(tokenxml);
            System.out.println("........................");
			boolean isValid = TVS.validateXMLToken(tokenxml, null);
			
			
			System.out.println("\nSimple Token validation by TVS: Token validity: " + (isValid? "=VALID=" : "=INVALID="));

			XMLTokenType token = new XMLTokenType (tokenxml);
	 		boolean timevalid = token.isTimeValid(token);
	 		System.out.println("\nTest PEP-TVS: Token validity time: " + (timevalid ? "=VALID=" : "=INVALID="));	
	 		
	 		
			System.out.println("Token elements: TokenId = " + token.getTokenid() + 
					"; SessionId = " + token.getSessionid() + "; Issuer = " + token.getIssuer() +
					"\nValid from " + token.getNotBefore() + " to " + token.getNotOnOrAfter() +
					"\nTokenValue = " + token.getTokenValue() +
					"\nTokenDomain = " + token.getTokenDomain() + "\nTokenType = " + token.getTokenType());
			
            CreateReservation obj = (CreateReservation) rawRequest;
            obj.getCreateReservation().setToken(tokenxml);
            domRequest = TokenHelper.addTokenCreate(domRequest, tokenxml);
            

            
          System.out.println("=== REQUEST MESSAGE WITH NEW TOKEN ===");
          System.out.println(XmlUtils.toString(domRequest));
          System.out.println("========================================");
          assertTrue(isValid && timevalid);


        }
        
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    
    /**
     * The method for the entities management. It checks if the UUID (identifier inside the response) includes some reference.
     */
    private final boolean managTrustedEntitiesOutput(final Document request, HarmonyWSSecurity authn)
    {
				String UUID;
				try {
					UUID = SOAPHelper.getUUID(request.getDocumentElement());
					//get Identifier
					return ((Boolean) authn.getIdHashFlagSecurity(UUID)).booleanValue();
				
				} catch (IOException e) {
					return true;
				} catch (OperationNotAllowedFaultException e) {
					return true;
				}   		
	}   
    
    
    /** Method to get entities from a file
     * 
     * @param request
     * @return
     */
    private  final List<String> getEntities (final Document request){
		List<String> listEntities = new ArrayList<String>();
		try  {
			int numEntities = Config.getInt("aai","numEntities");
			for (int i = 0; i<numEntities; i++) {
				String entity = Config.getString("aai", "entity"+String.valueOf(i)+".name");
				listEntities.add(entity);
			}
		} catch (Exception e) {
			System.out.println("File with trusted entities couldn't be loaded");
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
	    		List<String> listEntities = getEntities(request);	    		
	    		return (listEntities.contains(user));
			} catch (IOException e) {
				System.out.println("WARNING: I was impossible to parse the From address");
				return false;
			} catch (OperationNotAllowedFaultException e) {
				System.out.println("WARNING: It is null the parameter in the parseID operation");
				return false;
			}
    		
    } 
    
    
    @Test
    public final void testTrustedEntityWorkflow() throws Exception {
			
			//Create Reservation request
	    	HarmonyWSSecurity authn = new HarmonyWSSecurity(new Hashtable<String,List>());
	    	
			String createRequest = FileHelper.readFile("resources/data/CreateReservation.xml");
			Document domRequest =  SignatureFactory.String2DOM(createRequest);
			
	        if (managTrustedEntitiesInput(domRequest))	{
	        	String UUID = null;  //It is an UUID 
	        	String user = null;  //It is an user 
				try {
					UUID  =  SOAPHelper.getUUID(domRequest.getDocumentElement());
					user = SOAPHelper.parseID(SOAPHelper.getFromAddress(domRequest.getDocumentElement()));
				} catch (IOException e) {
					throw new OperationNotAllowedFaultException();
				} 
	        	
				//generate tuple
	        	System.out.println("\nThe user: "+user+" UUID: "+UUID+" is an trusted entity");	

	        	authn.setIdHash(UUID,user,false);
	        	
	        	
	        	String createResponse = FileHelper.readFile("resources/data/CreateReservationResponse.xml");
				Document domResponse =  SignatureFactory.String2DOM(createResponse);
	        	System.out.println("\nThe message will go to a trusted entity: " + (!managTrustedEntitiesOutput(domResponse,authn) ? "=VALID=" : "=INVALID="));	

	           
	            
	        	
			
	        }
       
    }

}
