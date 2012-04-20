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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.stream.StreamSource;

import org.apache.muse.util.xml.XmlUtils;
import org.apache.muse.ws.addressing.soap.SoapConstants;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import org.opennaas.core.security.utils.Request;
import org.opennaas.extension.idb.serviceinterface.databinding.jaxb.CancelReservation;
import org.opennaas.extension.idb.serviceinterface.databinding.jaxb.ConnectionConstraintType;
import org.opennaas.extension.idb.serviceinterface.databinding.jaxb.CreateReservation;
import org.opennaas.extension.idb.serviceinterface.databinding.jaxb.EndpointType;
import org.opennaas.extension.idb.serviceinterface.databinding.jaxb.IsAvailable;
import org.opennaas.extension.idb.serviceinterface.databinding.jaxb.ServiceConstraintType;
import org.opennaas.extension.idb.serviceinterface.databinding.jaxb.exceptions.InvalidRequestFaultException;
import org.opennaas.extension.idb.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;
import org.opennaas.extension.idb.serviceinterface.databinding.utils.JaxbSerializer;
import org.opennaas.core.utils.FileHelper;
import org.opennaas.core.utils.Helpers;

public class TestJAXBRequestParsing {
    private final String rawReservationRequest;
    private final String rawReservationRequestWithoutSoap;
    private final String simpleSoap;
    private CreateReservation createReservationRequest;

    public TestJAXBRequestParsing() throws Exception {
        this.rawReservationRequest =
                FileHelper.readFile("resources/data/RawReservationRequest.xml");
        this.rawReservationRequestWithoutSoap =
                FileHelper
                        .readFile("resources/data/RawReservationRequest-ohneSoap.xml");
        this.simpleSoap = FileHelper.readFile("resources/data/SimpleSoap.xml");
        this.testCreateJaxbObjectWithoutSoap();
    }

    /**
     * 
     * @param xml
     * @throws JAXBException
     */
    private void createRequestObject(final String xml) throws JAXBException {
        final JaxbSerializer serializer = JaxbSerializer.getInstance();
        this.createReservationRequest =
                (CreateReservation) serializer.xmlToObject(xml);
    }

    /**
     * Method to extract createReservationRequest data from Soap-Message taken
     * from MiniIsolationLayer
     * 
     * @param createReservationRequest
     * @return
     */
    private Element getData(final Node request) {
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

    private String getSoapBody(final String xml) throws SOAPException,
            IOException {
        String result = "";
        // Create SoapMessage
        final MessageFactory msgFactory = MessageFactory.newInstance();
        final SOAPMessage message = msgFactory.createMessage();

        final SOAPPart soapPart = message.getSOAPPart();

        // Load the SOAP text into a stream source
        final byte[] buffer = xml.getBytes();
        final ByteArrayInputStream stream = new ByteArrayInputStream(buffer);
        final StreamSource source = new StreamSource(stream);

        // Set contents of message
        soapPart.setContent(source);

        // not essential
        soapPart.normalizeDocument();

        // -- DONE

        message.writeTo(System.out);

        // Try accessing the SOAPBody
        System.out.println("\n");

        final SOAPBody soapBody = message.getSOAPBody();

        final Iterator<?> i = soapBody.getChildElements();
        final Node node = (Node) i.next(); // "mh:reservation"
        result = node.getChildNodes().item(0) // "isbn"
                .getChildNodes().item(0) // text node inside "isbn"
                .getNodeValue();

        return result;
    }

    /**
     * 
     * @return
     */
    private List<String> getSourceEndpoint() {
        final List<String> result = new ArrayList<String>();
        final List<ServiceConstraintType> service =
                this.createReservationRequest.getCreateReservation()
                        .getService();
        for (final ServiceConstraintType temp : service) {
            for (final ConnectionConstraintType temp2 : temp.getConnections()) {
                result.add(temp2.getSource().getEndpointId().trim());
            }
        }
        return result;
    }

    /**
     * 
     * @return
     */
    private List<String> getTargetEndpoint() {
        final List<String> result = new ArrayList<String>();
        final List<ServiceConstraintType> service =
                this.createReservationRequest.getCreateReservation()
                        .getService();
        for (final ServiceConstraintType temp : service) {
            for (final ConnectionConstraintType temp2 : temp.getConnections()) {
                for (final EndpointType temp3 : temp2.getTarget()) {
                    result.add(temp3.getEndpointId().trim());
                }
            }
        }
        return result;
    }

    /**
     * 
     * @throws JAXBException
     */
    @Test
    public void testCreateJaxbObjectWithoutSoap() throws JAXBException {
        this.createRequestObject(this.rawReservationRequestWithoutSoap);
        Assert.assertTrue("Object should not be null",
                null != this.createReservationRequest);
        final int expected = 1;
        final int actual =
                this.createReservationRequest.getCreateReservation()
                        .getService().size();
        Assert.assertEquals("Request should contain one service", expected,
                actual);
    }

    /**
     * 
     * @throws JAXBException
     */
    // @Test
    // FIXME unexpected element...
    public void testCreateJaxbObjectWithSoap() throws JAXBException {
        this.createRequestObject(this.rawReservationRequest);
        Assert.assertTrue("Object should not be null",
                null != this.createReservationRequest);
        final int expected = 1;
        final int actual =
                this.createReservationRequest.getCreateReservation()
                        .getService().size();
        Assert.assertEquals("Request should contain one service", expected,
                actual);
    }

    @Test
    public void testCreateReservationNoToken() throws Exception {
        final Request r = new Request(this.rawReservationRequest);
        Assert.assertNull(r.getToken());
    }

    /**
     * @throws JAXBException
     * 
     */
    @Test
    public void testExtractEndpoints() throws JAXBException {
        final List<String> target = this.getTargetEndpoint();
        final List<String> source = this.getSourceEndpoint();

        final int expectedSourceSize = 1;
        final int actualSourceSize = source.size();
        Assert.assertEquals("Source size should equal", expectedSourceSize,
                actualSourceSize);

        final int expectedTargetSize = 1;
        final int actualTargetSize = target.size();
        Assert.assertEquals("Target size should equal", expectedTargetSize,
                actualTargetSize);

        final String expectedSourceTNA = "128.0.0.1";
        final String actualSourceTNA = source.get(0);
        Assert.assertEquals("Source TNA should equal", expectedSourceTNA,
                actualSourceTNA);

        final String expectedTargetTNA = "128.0.0.2";
        final String actualTargetTNA = target.get(0);
        Assert.assertEquals("Target TNA should equal", expectedTargetTNA,
                actualTargetTNA);

    }

    @Test
    public void testGetGRI() throws Exception {
        final Request r =
                new Request(FileHelper
                        .readFile("resources/data/CancelReservation.xml"));
        Assert.assertNotNull(r.getGRI());
    }

    @Test
    public void testGetToken() throws IOException, Exception {
        // TODO: assertion, if request has a token
        final Request r =
                new Request(FileHelper
                        .readFile("resources/data/CancelReservation.xml"));
        r.getToken();
    }

    @Test
    public void testParseCancelReservation()
            throws InvalidRequestFaultException, UnexpectedFaultException,
            JAXBException, IOException, SAXException {

        /* setup ------------------------------------------------------------ */
        final File cancelReservation =
                new File("resources/data/CancelReservation.xml");
        final Document doc = XmlUtils.createDocument(cancelReservation);
        /* ------------------------------------------------------------------ */

        /* Create the object from created element --------------------------- */
        final CancelReservation object =
                (CancelReservation) JaxbSerializer.getInstance()
                        .elementToObject(this.getData(doc));
        /* ------------------------------------------------------------------ */

        Assert.assertEquals("83", object.getCancelReservation()
                .getReservationID());
        /* ------------------------------------------------------------------ */

        /* Check the String representation ---------------------------------- */
    }

    /**
     * 
     * @throws InvalidRequestFaultException
     * @throws UnexpectedFaultException
     * @throws JAXBException
     * @throws SAXException
     * @throws IOException
     */
    @Test
    public void testParseIsAvailable() throws InvalidRequestFaultException,
            UnexpectedFaultException, JAXBException, IOException, SAXException {

        /* setup ------------------------------------------------------------ */
        final File isAvailable =
                new File("resources/data/ValidIsAvailable.xml");
        final Document doc = XmlUtils.createDocument(isAvailable);
        /* ------------------------------------------------------------------ */

        /* Create the object from created element --------------------------- */
        final IsAvailable object =
                (IsAvailable) JaxbSerializer.getInstance().elementToObject(
                        this.getData(doc));
        /* ------------------------------------------------------------------ */

        /* Check the service id --------------------------------------------- */
        final long expectedServiceID = 42;
        final long actualServiceID =
                object.getIsAvailable().getService().get(0).getServiceID();
        Assert.assertEquals("Service id should equal", expectedServiceID,
                actualServiceID);
        /* ------------------------------------------------------------------ */

        /* Check the String representation ---------------------------------- */
        final String expectedString =
                "<ns3:ServiceID>" + expectedServiceID + "</ns3:ServiceID>";
        final String actualString =
                JaxbSerializer.getInstance().objectToXml(object);

        Assert.assertTrue(
                "String representation should contain the service ID",
                actualString.contains(expectedString));
        /* ------------------------------------------------------------------ */
    }

    /**
     * 
     * @throws SOAPException
     * @throws IOException
     */
    @Test
    public void testParseSimpleSoap() throws SOAPException, IOException {
        final String expected = "1234567";
        final String actual = this.getSoapBody(this.simpleSoap);
        Assert.assertEquals("Parsed message body should equals", expected,
                actual);
    }

    @Test
    public void testTime() throws JAXBException {
        createRequestObject(this.rawReservationRequestWithoutSoap);

        XMLGregorianCalendar cal =
                this.createReservationRequest.getCreateReservation()
                        .getService().get(0).getFixedReservationConstraints()
                        .getStartTime();

        Date date = Helpers.xmlCalendarToDate(cal);

        Assert.assertEquals("Dates should be equal",
                "Thu Dec 11 12:14:14 CET 2008", date.toString());
    }
}
