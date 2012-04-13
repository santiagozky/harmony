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

package eu.ist_phosphorus.harmony.test.common.security;

import java.io.IOException;

import org.aaaarch.gaaapi.tvs.TVS;
import org.aaaarch.gaaapi.tvs.XMLTokenType;
import org.apache.muse.util.xml.XmlUtils;
import org.junit.Test;
import org.w3c.dom.Document;

import eu.ist_phosphorus.harmony.common.security.authn.SignatureFactory;
import eu.ist_phosphorus.harmony.common.utils.FileHelper;
import eu.ist_phosphorus.harmony.common.security.g2mpls.*;
import eu.ist_phosphorus.harmony.common.security.utils.helper.TokenHelper;

public class TestTokenG2MPLS {

		
	    @Test
	    public final void testSimpleCaseCreateReservation()  {
	        try {
	        	/* Sender */
				final String createRequest = FileHelper.readFile("resources/data/CreateReservation.xml");
				System.out.println("SOAP Message before");
				System.out.println("--------------- Request message  before ---------------");
				System.out.println(createRequest);
				System.out.println("-------------------------------------------------------");

				Document domRequest =  SignatureFactory.String2DOM(createRequest);
				if (SecurityG2MPLSHelper.isEnableG2MPLSManagement())  {
					SecurityG2MPLSHelper.processRequest(domRequest.getDocumentElement());
					
				}
				System.out.println("--------------- Request message modified ---------------");
				System.out.println(XmlUtils.toString(domRequest));
				System.out.println("--------------------------------------------------------");
				
				/* Receiver */
				String tokenxml = TokenHelper.getToken(domRequest);
				
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
		
				
				
	        
	        
	        } catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} 
	    	

	    }
	    
}
