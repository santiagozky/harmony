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

package org.opennaas.core.security.g2mpls;

import java.util.HashMap;
import java.util.MissingResourceException;

import org.aaaarch.config.ConfigDomainsPhosphorus;
import org.aaaarch.config.ConstantsNS;
import org.aaaarch.gaaapi.ActionSet;
import org.aaaarch.gaaapi.PEP;
import org.aaaarch.gaaapi.ResourceHelper;
import org.aaaarch.gaaapi.SubjectSet;
import org.aaaarch.gaaapi.tvs.MalformedXMLTokenException;
import org.aaaarch.gaaapi.tvs.NotValidAuthzTokenException;
import org.aaaarch.gaaapi.tvs.TVS;
import org.aaaarch.gaaapi.tvs.TokenBuilder;
import org.aaaarch.gaaapi.tvs.XMLTokenType;
import org.w3c.dom.Element;

import org.opennaas.core.security.utils.Request;
import org.opennaas.core.security.utils.helper.SOAPHelper;
import org.opennaas.core.security.utils.helper.TokenHelper;
import org.opennaas.extension.idb.serviceinterface.databinding.jaxb.CreateReservation;
import org.opennaas.core.utils.Config;
/**
 * This package implements the necessary capabilities for a communication with the G2MPLS
 * @author carlos
 *
 */
public class SecurityG2MPLSHelper {
	
    private static Boolean TOKEN_MANAGEMENT = null;
	
	/**
	 * Authorize an action. Check if the operation in the resource can be done.
	 * 
	 * @param resources string representation of the sources
	 * @return check if it is valid or not
	 * @throws Exception
	 */
	public  static final boolean authorizate(String resources) throws Exception  {
		String domainViola = ConfigDomainsPhosphorus.DOMAIN_PHOSPHORUS_VIOLA;
//		String resourceInputURI = domainViola + "/harmony/" + "source=10.3.1.16/target=10.7.3.13";
		String resourceInputURI = domainViola+"/harmony/"+resources;
			// no policy for I2CAT
			//resourceInputURI = domainI2CAT + "/harmony/" + "source=10.3.1.16/target=10.7.3.13";
		//resourceId = "http://testbed.ist-phosphorus.eu/resource-context/phosphorus";
		//resourceId = "http://testbed.ist-phosphorus.eu/resource-context/phosphorus/experiment=demo010";
		
		HashMap resmap = ResourceHelper.parseResourceURI(resourceInputURI);
		HashMap<String, String> actmap = new HashMap<String, String>();
		HashMap<String, String> subjmap = new HashMap<String, String>();
		subjmap = SubjectSet.getSubjSetTest();
		
	    String action = ActionSet.NSP_CREATE_PATH;		

	    actmap.put(ConstantsNS.ACTION_ACTION_ID, action); 
    	// some modifications for experiments
	
    	subjmap.put(ConstantsNS.SUBJECT_CONTEXT, "demo041");	
		
		return PEP.authorizeAction(resmap, actmap, subjmap);
	}
	/**
	 * Create a token from a gri
	 * @param GRI string identifier
	 * @return
	 * @throws Exception
	 */
	public  static final String getToken(String gri) throws Exception  {
		
		String domainId = "http://testbed.ist-phosphorus.eu/viola";
		return  TokenBuilder.getXMLToken(domainId, gri, null, 0, false);
	}

	
	/** 
	 * Validate token
	 * @param tokenXML
	 * @return
	 * @throws MalformedXMLTokenException
	 * @throws NotValidAuthzTokenException
	 * @throws Exception
	 */
	public  static final boolean validate(String tokenXML) throws MalformedXMLTokenException, NotValidAuthzTokenException, Exception  {
		XMLTokenType token = new XMLTokenType (tokenXML);
		boolean timeValid = token.isTimeValid(token);
		boolean isValid = TVS.validateXMLToken(tokenXML, null);
		return (isValid && timeValid);
	}
	
	/**
	 * This method is used in the SecureSoapClient. When the client process a request to the G2MPLS. It checks if it is 
	 * a createReservation message (now it only works with createReservations)
	 * Process a request message
	 * @param soapRequest
	 * @return
	 * @throws Exception
	 */
	public static final Element processRequest (Element soapRequest) throws Exception {
		Request request = new Request(soapRequest.getOwnerDocument());
		//TODO Include other messages (topology messages)
		if (request.isCreateReservationRequest()) {
			String gri = SOAPHelper.getGRI(soapRequest);
			final Object rawRequest = request.getRawRequest();
			String strToken = getToken(gri);
            CreateReservation obj = (CreateReservation) rawRequest;
            obj.getCreateReservation().setToken(strToken);
            soapRequest = TokenHelper.addTokenCreate(soapRequest.getOwnerDocument(), strToken).getDocumentElement();
			
		}		
		return soapRequest;
	}
	
	
	/**
	 * Yet not implemented. It is supposed that the response will be correct
	 * @param soapRequest
	 * @return
	 */
	public static final Element processResponse (Element soapResponse) {

		return soapResponse;
	}

	/**
	 * Check if the token management with G2MPLS is working
	 * @return
	 */
	public static boolean isEnableG2MPLSManagement () {
        if (null == TOKEN_MANAGEMENT) {
            TOKEN_MANAGEMENT =
                    getVal("g2mpls.authz.request");
        }

        return TOKEN_MANAGEMENT.booleanValue();
        
	}
	
	
    /**
     * @param val
     * @param alt
     * @return
     */
    private static final Boolean getVal(final String val) {
        boolean res;

        try {
            res = Config.isTrue("aai", val);
        } catch (MissingResourceException e) {
           res = false;
        }

        return new Boolean(res);
    }
	



}
