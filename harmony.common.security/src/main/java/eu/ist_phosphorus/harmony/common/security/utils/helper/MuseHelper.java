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

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.muse.util.xml.XmlUtils;
import org.apache.muse.ws.addressing.MessageHeaders;
import org.apache.muse.ws.addressing.soap.SoapConstants;
import org.apache.muse.ws.addressing.soap.SoapFault;
import org.apache.muse.ws.addressing.soap.SoapUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import eu.ist_phosphorus.harmony.common.security.utils.ServletFilter;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;

public class MuseHelper {

    /**
     * Method taken from MiniIsolationLayer (muse-plattform-mini). Method
     * generates appropriate headers for any SoapMessages including SoapFaults
     * which are needed for any responses.
     * 
     * @param request
     * @param result
     * @return
     */
    public static final Document handleRequest(Document request, Element result) {
        Element soap = XmlUtils.getFirstElement(request);
        Element header = XmlUtils.getElement(soap, SoapConstants.HEADER_QNAME);
        Element body = XmlUtils.getElement(soap, SoapConstants.BODY_QNAME);

        if (header == null)
            throw new RuntimeException(
                    "Invalid SOAP envelope: no header element.");

        if (body == null)
            throw new RuntimeException(
                    "Invalid SOAP envelope: no body element.");

        MessageHeaders addressing = null;

        try {
            addressing = new MessageHeaders(header);
        } catch (Throwable error) {
            System.out.println("ERROR");
        }

        MessageHeaders replyAddressing = null;

        if (SoapUtils.isFault(result))
            replyAddressing = addressing.createFaultHeaders();

        else
            replyAddressing = addressing.createReplyHeaders();

        //
        // import all of the headers into the response envelope...
        //        
        Element replyXML = replyAddressing.toXML();
        Element[] children = XmlUtils.getAllElements(replyXML);

        Document response = XmlUtils.createDocument();

        soap = XmlUtils.createElement(response, SoapConstants.ENVELOPE_QNAME);
        response.appendChild(soap);

        header = XmlUtils.createElement(response, SoapConstants.HEADER_QNAME);
        soap.appendChild(header);

        for (int n = 0; n < children.length; ++n) {
            Node next = response.importNode(children[n], true);
            header.appendChild(next);
        }

        //
        // add the result (valid or fault) to the SOAP body...
        //
        body = XmlUtils.createElement(response, SoapConstants.BODY_QNAME);
        soap.appendChild(body);

        result = (Element) response.importNode(result, true);
        body.appendChild(result);

        return response;
    }

    /**
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public static final String handleError(final Exception e)
            throws IOException, ServletException {
        SoapFault fault;

        // Check if exception already is subclass of SoapFault
        if (e instanceof SoapFault) {
            fault = (SoapFault) e;
        } else {
            fault = new UnexpectedFaultException(e);
        }
        // Convert exception to Element
        final Element xml = fault.toXML();

        // Try to generate a SoapFault message for response
        String responseString = "";

        // Dummy response for unsupported requests eg. HTTP GET
        if (null == ServletFilter.originalRequest) {
            UnexpectedFaultException dummy =
                    new UnexpectedFaultException("Unsupported Request: "
                            + e.getMessage(), e);

            ServletFilter.originalRequest = dummy.toXML().getOwnerDocument();
        }

        final Document responseDoc =
                MuseHelper.handleRequest(ServletFilter.originalRequest, xml);

        responseString = XmlUtils.toString(responseDoc);

        return responseString;
    }

    /**
     * Method to extract request data from Soap-Message taken from
     * MiniIsolationLayer
     * 
     * @param request
     * @return
     */
    public static final Element getData(final Node request) {
        final Element soap = XmlUtils.getFirstElement(request);
        final Element header =
                XmlUtils.getElement(soap, SoapConstants.HEADER_QNAME);
        final Element body =
                XmlUtils.getElement(soap, SoapConstants.BODY_QNAME);

        if (header == null) {
            throw new RuntimeException(
                    "Invalid SOAP envelope: no header element.");
        }

        if (body == null) {
            throw new RuntimeException(
                    "Invalid SOAP envelope: no body element.");
        }

        Element requestData = XmlUtils.getFirstElement(body);

        // And the magic fix to get JaxbSerializer working
        requestData = XmlUtils.getFirstElement(requestData);

        return requestData;
    }

    /**
     * VERY ugly hack!!! but this is needed to keep certs valid during
     * muse serialition process...
     * 
     * TODO: Find something better!
     * 
     * @param req
     * @return
     * @throws IOException
     * @throws SAXException
     */
    public static final Element prepareElementForMuse(final Element req)
            throws IOException, SAXException {
        final String temp = XmlUtils.toString(req);

        return XmlUtils.createDocument(temp).getDocumentElement();
    }
}
