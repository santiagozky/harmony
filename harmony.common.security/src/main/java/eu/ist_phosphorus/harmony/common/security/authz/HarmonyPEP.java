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


package eu.ist_phosphorus.harmony.common.security.authz;
import org.aaaarch.gaaapi.authn.AuthenticateSubject;
import org.aaaarch.gaaapi.tvs.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.aaaarch.config.ConfigDomainsPhosphorus;
import org.aaaarch.config.ConstantsNS;
import org.aaaarch.gaaapi.NotAuthorizedException;
import org.aaaarch.gaaapi.NotAvailablePDPException;
import org.aaaarch.gaaapi.PEP;
import org.aaaarch.gaaapi.ResourceHelper;
import org.apache.log4j.Logger;
import org.apache.muse.util.xml.XmlUtils;
import org.apache.muse.ws.addressing.soap.SoapFault;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.aaaarch.gaaapi.tvs.TVSTable;

import eu.ist_phosphorus.harmony.common.security.utils.Request;
import eu.ist_phosphorus.harmony.common.security.utils.ServletFilter;
import eu.ist_phosphorus.harmony.common.security.utils.helper.SOAPHelper;
import eu.ist_phosphorus.harmony.common.security.utils.helper.TokenHelper;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.exceptions.OperationNotAllowedFaultException;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;
import eu.ist_phosphorus.harmony.common.utils.PhLogger;
import eu.ist_phosphorus.harmony.common.utils.Config;


/**
 * Implementation of the Harmony PEP/PDP.
 * 
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 * @version $Id$
 */
public final class HarmonyPEP {
	static private HashMap<String,String> hashTokens = new HashMap<String,String>();
	static public Request requestStored = null; 
    private final Logger logger = PhLogger.getSeparateLogger("aai");
    @SuppressWarnings("unused")
    private String token;

    /**
     * 
     * @param config
     */
    public HarmonyPEP() {

    }

    /**
     * Maybe a method that returns a token...
     * 
     * @param authzTicketToken
     *            The authZ token.
     * @param sessionId
     *            The session ID.
     * @param resource
     *            The resource.
     * @param subjectContext
     *            The subject context.
     * @return A Token?
     * @throws NotAuthorizedException
     *             If the request is not been authorized.
     * @throws NotAvailablePDPException
     *             If PDP is not available.
     * @throws Exception
     *             If an exception is thrown within the GAAA-tk.
     */
    public String getTicket(final String authzTicketToken,
            final String sessionId, final String resource,
            final String subjectContext) throws NotAuthorizedException,
            NotAvailablePDPException, Exception {
        return PEP.authorizeAction(authzTicketToken, sessionId, resource,
                subjectContext);
    }

    @Deprecated
    public String post(final String request, final String gri) throws Exception {
        return this.post(XmlUtils.createDocument(request), gri);
    }

    public String post(final Document request, final String gri)
	    throws Exception {
		final HarmonyTVS tvs = new HarmonyTVS();
		final Request requestParser = new Request(request);
		final long notBefore = requestParser.getNotBefore();
		final long notAfter = requestParser.getNotAfter();
		
		final Date notBeforeDate = new Date(notBefore);
		final Date notAfterDate = new Date(notAfter - 1);
		
		this.logger.info("Not valid before: " + notBeforeDate);
		this.logger.info("Not valid after: " + notAfterDate);
		this.logger.info("Endpoints: " + requestParser.getEndoints());
		for (final Map.Entry<String, ArrayList<String>> entry : requestParser
		        .getEndoints().entrySet()) {
		    final String resourceSource = entry.getKey();
		
		    for (final String targetEP : entry.getValue()) {
		        tvs.addEntryTVSTable(gri, notBefore, notAfter, resourceSource,
		                targetEP);
		    }
  }

final int duration = (int) (notAfter - notBefore) / 60000;

return HarmonyTVS.getXMLToken(gri, duration, false, notBeforeDate,
        notAfterDate);
}
    
    public String post(final String gri)
            throws Exception {
        final HarmonyTVS tvs = new HarmonyTVS();
        final Request requestParser = requestStored;
        final int validTime = 1440; // 24 hours
        final long notBefore = System.currentTimeMillis();
        final long notAfter = notBefore + validTime * 60 * 1000;
        
        
//        final Request requestParser = new Request(request);
//        final long notBefore = requestParser.getNotBefore();
//        final long notAfter = requestParser.getNotAfter();
       // long notAfter = notBefore + 1440 * 60 * 1000;
       final Date notBeforeDate = new Date(notBefore);
        final Date notAfterDate = new Date(notAfter - 1);

        this.logger.info("Not valid before: " + notBeforeDate);
        this.logger.info("Not valid after: " + notAfterDate);
        this.logger.info("Endpoints: " + requestParser.getEndoints());
        for (final Map.Entry<String, ArrayList<String>> entry : requestParser
                .getEndoints().entrySet()) {
            final String resourceSource = entry.getKey();

            for (final String targetEP : entry.getValue()) {
                tvs.addEntryTVSTable(gri, notBefore, notAfter, resourceSource,
                        targetEP);
            }
        }

        final int duration = (int) (notAfter - notBefore) / 60000;

        return HarmonyTVS.getXMLToken(gri, duration, false, notBeforeDate,
                notAfterDate);
    }

   

    /**
     * Generates Subject Map.
     * 
     * @param user
     *            Username
     * @param group
     *            Usergroup
     * @param action
     *            Action
     * @return Subject Map
     */
    private final HashMap<String, String> getSubjectMap(final String user,
            final String group, final String action) {
        final HashMap<String, String> subjmap = new HashMap<String, String>();

        final String subjectId = user;
        final String subjectConfdata = "";
        // how to get this information? some AAA-module...
        final String subjectRole = group;    	
    	String subjectContext = "demo041";  // use TNA based policy
    	
    	//Check if you add 
//      	String resourceId = Config.getString("aai","authz.TNAPolicy");
//    	if (resourceId!=null && resourceId.length()>0) {
//    		 subjectContext = resourceId; // use TNA based policy
//    	}
        subjmap.put(ConstantsNS.SUBJECT_SUBJECT_ID, subjectId);
        subjmap.put(ConstantsNS.SUBJECT_CONFDATA, subjectConfdata);
        subjmap.put(ConstantsNS.SUBJECT_ROLE, subjectRole);
        subjmap.put(ConstantsNS.SUBJECT_CONTEXT, subjectContext);
        return subjmap;
    }

    /**
     * Generates Action Map.
     * 
     * @param user
     *            Username
     * @param group
     *            Usergroup
     * @param action
     *            Action
     * @return Action Map
     */
    private final HashMap<String, String> getActionMap(final String user,
            final String group, final String action) {
        final HashMap<String, String> actmap = new HashMap<String, String>();

        actmap.put(ConstantsNS.ACTION_ACTION_ID, action);

        return actmap;
    }

    /**
     * Generate ResourceIdURIPrefix.
     * 
     * @return ResourceIdURIPrefix
     */
    private final String getResourceIdURIPrefix() {
        String resourceNamespace = "http://testbed.ist-phosphorus.eu/";
        String resourceDomain = "viola/";
        String resourceService = "harmony/";
//    	if (	Config.isTrue("aai","resource.force") //check if it gets the resource information
//    			&& Config.getString("aai","resource.namespace")!=null 
//    			&& Config.getString("aai","resource.namespace").length()>0
//    			&& Config.getString("aai","resource.domain")!=null 
//    			&& Config.getString("aai","resource.domain").length()>0
//    			&& Config.getString("aai","resource.service")!=null 
//    			&& Config.getString("aai","resource.service").length()>0
//    		) {
//            resourceNamespace =Config.getString("aai","resource.namespace");
//            resourceDomain = Config.getString("aai","resource.domain");
//            resourceService = Config.getString("aai","resource.service");
//    	} 
    	 return (resourceNamespace + resourceDomain + resourceService);
    	
    }

    /**
     * HandleCreateReservationRequest.
     * 
     * @param userName
     * @param userGroup
     * @param requestParser
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private final boolean handleCreateReservation(final String userName,
            final String userGroup, final Request requestParser)
            throws Exception {
        /** The action as a String */
        final String action = requestParser.getAction();

        this.logger.info("Initializing GAAAtk...");
        final HashMap<String, String> subjmap =
                this.getSubjectMap(userName, userGroup, action);
        final HashMap<String, String> actmap =
                this.getActionMap(userName, userGroup, action);
        
        //FIXME Modification for v0.8. It is necessary a specification in the subject_confdata
        // information
        subjmap.put(ConstantsNS.SUBJECT_CONFDATA, AuthenticateSubject.AUTHN_SUBJECT_CONFIRMED);
        

        //TODO It must be get the URI from a SOAP message
        final String resourceIdURIPrefix = this.getResourceIdURIPrefix();

        final HashMap<String, ArrayList<String>> endpoints =
                requestParser.getEndoints();
        boolean isValid = false;

        // if the requestType is unknown return false!!
        if (endpoints == null) {
            this.logger
                    .warn("The type of the request to authorize is not known!");
            return false;
        }

        if (!endpoints.isEmpty()) {
            isValid = true;
        }
        for (final Map.Entry<String, ArrayList<String>> sourceTarget : endpoints
                .entrySet()) {
            final String resourceSource =
                    "source=" + sourceTarget.getKey() + "/";
            for (final String targetEP : sourceTarget.getValue()) {
                final String resourceTarget = "target=" + targetEP + "/";
                /** The resource as an URI */
                final String resourceIdURI =
                        resourceIdURIPrefix + resourceSource + resourceTarget;
                /** The resource as a HashMap */
                final HashMap<String, String> resmap =
                        ResourceHelper.parseResourceURI(resourceIdURI);
                this.logger.debug("Resource ID: " + resourceIdURI);
                
                isValid &= PEP.authorizeAction(resmap, actmap, subjmap);
            }
        }
        return isValid;
    }

    /**
     * Handle CancelReservationRequest.
     * 
     * @param requestParser
     * @return
     * @throws Exception
     */
    private final boolean handleCancelReservation(final Request requestParser)
            throws Exception {
        boolean isValid;
        
        String domainId = "http://testbed.ist-phosphorus.eu/viola";
        
        final String gri = SOAPHelper.getFormatGRI(requestParser.getGRI());
        final HarmonyTVS tvs = new HarmonyTVS();

        final String token = requestParser.getToken();

        
        if (null == token) {
            throw new OperationNotAllowedFaultException(
                    "CancelRequest must contain token");
        }

        this.logger.info("TVS: domain(" + domainId + ") gri(" + gri
                + ") token(" + token + ")");

        System.out.println("TVS: domain(" + domainId + ") gri(" + gri
                + ") token(" + token + ")");
        
        isValid = tvs.validate(token, domainId, gri);

        return isValid;
    }

    /**
     * Handle GetReservationsRequest.
     * 
     * @param reuestParser
     * @return
     */
    private final boolean handleGetReservations(final Request reuestParser) {
        this.logger.info("GetReservationsResponse is enabled");

        return true;
    }
    


    public boolean pre(final String request, final String userName)
            throws Exception {
        return this.pre(request, userName, "admin");
    }

    @Deprecated
    public boolean pre(final String request, final String userName,
            final String userGroup) throws SoapFault, IOException, SAXException {
        return this.pre(XmlUtils.createDocument(request), userName, userGroup);
    }

    public boolean pre(final Document request, final String userName,
            final String userGroup) throws SoapFault {
        this.logger.info("Parsing the request...");
        Request requestParser = null;

        try {
            requestParser = new Request(request);
            
        } catch (SoapFault e) {
            throw e;
        } catch (Exception e) {
            throw new UnexpectedFaultException(e);
        }

        boolean isValid = false;

        this.logger.info("Using GAAAtk...");
        try {
            if (requestParser.isCreateReservationRequest()) {
            	requestStored = new Request(request);
                isValid =
                        this.handleCreateReservation(userName, userGroup,
                                requestParser);
            } else if (requestParser.isCancelRequest()) {
                isValid = this.handleCancelReservation(requestParser);
                
            } else if (requestParser.isGetReservationsRequest()) { 
                isValid = this.handleGetReservations(requestParser);
            } else {
                isValid = true;            	
            }
        } catch (SoapFault sf) {
            throw sf;
        } catch (Exception ex) {
            throw new UnexpectedFaultException(ex);
        }

        return isValid;
    }

	public static String getHashToken(String gri) {
		return hashTokens.get(gri);
	}

	public static void  setHashToken(String gri, String token) {
		if (hashTokens.size()>50) hashTokens = new HashMap<String,String>();
		hashTokens.put(gri,token);
		
	}


	
}
