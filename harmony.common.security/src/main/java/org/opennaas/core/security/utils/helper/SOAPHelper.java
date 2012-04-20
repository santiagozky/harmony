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

package org.opennaas.core.security.utils.helper;

import java.io.IOException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.opennaas.extension.idb.serviceinterface.databinding.jaxb.exceptions.OperationNotAllowedFaultException;

public class SOAPHelper {
	
	/**
	 * Function to get UUID from a SOAP Request
	 * @param elemRequest
	 * @return
	 * @throws IOException
	 */
    public  static String  getUUID(Element elemRequest) throws IOException {
        NodeList listNode =
        	elemRequest.getElementsByTagNameNS("http://www.w3.org/2005/08/addressing","RelatesTo");
        
    	if (listNode != null && listNode.getLength()>0) {
    		Node node = listNode.item(0);
    		String textNodeContent = node.getTextContent();
    		return textNodeContent;
    	}
    	
    	listNode =
    		elemRequest.getElementsByTagNameNS("http://www.w3.org/2005/08/addressing","MessageID");
    	
    		if (listNode == null || listNode.getLength()==0) return null ;
    			Node node = listNode.item(0);
    			String textNodeContent = node.getTextContent();
    			return textNodeContent;

    }
    

    public  static String getFromAddress(Element elemRequest) throws IOException {
    	String textNodeContent = "";
        NodeList listNode =
        	elemRequest.getElementsByTagNameNS("http://www.w3.org/2005/08/addressing","Address");
        
    	if (listNode != null && listNode.getLength()>0) {
    		Node node = listNode.item(0);
    		textNodeContent = node.getTextContent();
    	}
		return textNodeContent;  	

     }
    
    public  static String getTo(Element elemRequest) throws IOException {
    	String textNodeContent = "";
        NodeList listNode =
        	elemRequest.getElementsByTagNameNS("http://www.w3.org/2005/08/addressing","To");
        
    	if (listNode != null && listNode.getLength()>0) {
    		Node node = listNode.item(0);
    		textNodeContent = node.getTextContent();
    	}
		return textNodeContent;  	

     }
    
	public static String getGRI(Element dom) {
		String gri = null;
       NodeList listNode =
        	dom.getElementsByTagNameNS("http://ist_phosphorus.eu/nsp/webservice/reservation","GRI");
	    if (listNode==null || listNode.getLength()==0) {
	    	listNode = dom.getElementsByTagNameNS("http://ist_phosphorus.eu/nsp/webservice/reservation","GRI");
	    }
		if (listNode!=null 
				&& listNode.getLength()>0) {
			gri = listNode.item(0).getTextContent();
		}
		return gri;
	}
	
	public static String getReservationID(Element dom) {
		String gri = null;
		

        NodeList listNode =
        	dom.getElementsByTagNameNS("http://ist_phosphorus.eu/nsp/webservice/reservation","ReservationID");
	    if (listNode==null || listNode.getLength()==0) {
	    	listNode = dom.getElementsByTagNameNS("http://ist_phosphorus.eu/nsp/webservice/reservation","ReservationID");
	    }
		if (listNode!=null 
				&& listNode.getLength()>0) {			
			gri = listNode.item(0).getTextContent();
		}
		return gri;
	}
	
	public static String getFormatGRI(String gri) {
		String[] splitGRI = gri.split("@");
		if (splitGRI.length==2) {
				return splitGRI[0];
		}
		return gri;
	}
	
    public static String parseID(String strRequest) throws IOException, OperationNotAllowedFaultException {
    		if (strRequest==null 
    				|| strRequest.equalsIgnoreCase("")) {
    			throw new OperationNotAllowedFaultException();
    		}
   			String[] strArray= strRequest.split("//");
   			if (strArray == null || strArray.length<2) return null;
   			String[] identArray = strArray[1].split("/");
   			String identifier = null;
   			if (identArray == null || identArray.length<2) return null;
   			String hostname = identArray[0];
   			String devicename = identArray[1];
   			identifier = hostname +"_"+devicename;
   			identifier = identifier.replaceAll(":", "_");
   			return identifier;
    }
    
}
