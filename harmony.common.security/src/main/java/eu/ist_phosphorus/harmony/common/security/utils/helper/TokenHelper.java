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


package eu.ist_phosphorus.harmony.common.security.utils.helper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.muse.util.xml.XmlUtils;
import org.apache.muse.ws.addressing.soap.SoapFault;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import eu.ist_phosphorus.harmony.common.security.utils.AAIConstants;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.exceptions.InvalidRequestFaultException;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.exceptions.OperationNotAllowedFaultException;
import eu.ist_phosphorus.harmony.common.utils.PhLogger;

public class TokenHelper {

    private static final HashMap<String, String> TOKEN =
            new HashMap<String, String>();

    private static final Logger log = PhLogger.getLogger();

    public static final String getTokenValue(final String token)
            throws SoapFault {
        try {
            Document docToken = XmlUtils.createDocument(token);

            NodeList values =
                    docToken.getElementsByTagName(AAIConstants.TOKEN_VALUE);

            Node node = values.item(0);

            if (null == node || null == node.getTextContent()) {
                throw new InvalidRequestFaultException(
                        "Token does not contain any value");
            }

            final String value = node.getTextContent();

            TOKEN.put(value, token);

            return value;
        } catch (SoapFault e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidRequestFaultException(
                    "Error while processing token: " + e.getMessage(), e);
        }
    }

    /**
     * Add Token to a specific jaxb object.
     * 
     * @param string
     *            the serialized JaxbObject
     * @param token
     *            the token
     * @return
     * @throws SoapFault
     */
    public static final Document addTokenCreate(final Document doc, final String token)
            throws SoapFault {
        NodeList nodes =
                doc.getElementsByTagNameNS("http://ist_phosphorus.eu/nsp/webservice/reservation","Token");
        
        if (nodes!=null && nodes.getLength()>0) {
        	nodes.item(0).setTextContent(token);
        }       
        return doc;
    }
    /**
     * Add Token to a specific jaxb object.
     * 
     * @param string
     *            the serialized JaxbObject
     * @param token
     *            the token
     * @return
     * @throws SoapFault
     */
    public static final String getToken(final Document doc)
            throws SoapFault {
        NodeList nodes =
            doc.getElementsByTagNameNS("http://ist_phosphorus.eu/nsp/webservice/reservation","Token");
	    if (nodes==null || nodes.getLength()==0) {
	        nodes = doc.getElementsByTagNameNS("http://ist_phosphorus.eu/nsp/webservice/reservation","Token");
	    }
	    
       	if (nodes!=null && nodes.getLength()>0) {
        	String tokenRet = nodes.item(0).getTextContent();
        	return tokenRet;
       	}
        return null;
    }

    /**
     * Extract Token from Object.
     * 
     * @param obj
     *            JaxbObject
     * @return Token, or null if Object has no Token field
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    public static final String getToken(final Object obj)
            throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        String token = null;

        try {
            final Method getToken = obj.getClass().getMethod("getToken");

            token = (String) getToken.invoke(obj);
        } catch (final NoSuchMethodException nsme) {
            TokenHelper.log.error(
                    "The request: " + obj.getClass().getCanonicalName()
                            + " contains no token!", nsme);
        }

        return token;
    }

    /**
     * @param val
     * @return
     * @throws OperationNotAllowedFaultException
     */
    public static final String getFullTokenByValue(final String val)
            throws OperationNotAllowedFaultException {
        final String result = TOKEN.get(val);

        if (null == result) {
            throw new OperationNotAllowedFaultException("Invalid Token!");
        }

        return result;
    }
    
	public static Document replaceTokenValue(Document dom, String tokenValue) {

        NodeList listNode =
        	dom.getElementsByTagNameNS("http://ist_phosphorus.eu/nsp/webservice/reservation","Token");
	    if (listNode==null || listNode.getLength()==0) {
	    	listNode = dom.getElementsByTagNameNS("http://ist_phosphorus.eu/nsp/webservice/reservation","Token");
	    }
		if (listNode!=null 
				&& listNode.getLength()>0) {
			TokenHelper.log.info("Getting Token...");
			TokenHelper.log.info(listNode.item(0).getTextContent());
			listNode.item(0).setTextContent(tokenValue);
			return dom;
		}
		return null;
	}
	

	

	
}
