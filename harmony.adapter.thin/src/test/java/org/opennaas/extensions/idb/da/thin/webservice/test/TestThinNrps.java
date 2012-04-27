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


package org.opennaas.extensions.idb.da.thin.webservice.test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.xml.bind.JAXBException;

import org.apache.muse.ws.addressing.soap.SoapFault;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

import org.opennaas.extensions.idb.da.thin.webservice.ReservationWS;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.Activate;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ActivateResponse;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ActivateType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelJob;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelJobResponse;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelJobType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelReservationResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ConnectionConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CreateReservationResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CreateReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.FixedReservationConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.IsAvailable;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.IsAvailableResponse;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.IsAvailableType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ServiceConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.InvalidRequestFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.OperationNotAllowedFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.utils.JaxbSerializer;
import org.opennaas.extensions.idb.serviceinterface.databinding.utils.WebserviceUtils;
import org.opennaas.extensions.idb.serviceinterface.reservation.SimpleReservationClient;
import org.opennaas.core.utils.Config;
import org.opennaas.core.utils.Helpers;

/**
 * JUnit test cases for the gmpls web service.
 * 
 * @author Alexander Zimmermann (zimmerm2@cs.uni-bonn.de), Daniel Beer
 *         (daniel.beer@iais.fraunhofer.de)
 * @version $Id$
 */
public class TestThinNrps {
    /**
     * General timeout for a test.
     */
    // private static final int TIMEOUT = 2 * 60 * 1000;
    private static final int TIMEOUT = 20 * 1000;

    /**
     * Web Service URI.
     */
    private static boolean serviceAvailable = true;

    /** */
    private static int createPathBandwidth = 1000;
    /** */
    private static int createPathDuration = 2 * 60;
    /** */
//    private static String[] createPathSourceTNA = { "10.7.1.6", "10.7.1.8",
//            "10.7.1.2" };
    private static String[] createPathSourceTNA = { "10.7.4.1", "10.7.4.2",
    			"10.7.4.3" };
    /** */
//    private static String[] createPathDestinationTNA = { "10.7.3.1",
//            "10.7.3.7", "10.7.3.10" };
    private static String[] createPathDestinationTNA = { "10.7.3.100",
    			"10.7.3.101", "10.7.3.102" };
    /** */
    private static long createdJobId;
    /** */
    private static long createdReservationId;

    /**
     * Web Service client.
     */
    private static SimpleReservationClient client;

    /**
     * Setup before creating test suite.
     * 
     * @throws java.lang.Exception
     *             An exception.
     */
    @BeforeClass
    public static final void setUpBeforeClass() throws Exception {
        if (Config.isTrue("test", "test.callWebservice")) {
            String eprUri = Config.getString("test", "test.reservationEPR");
            int portSeperatorPos = eprUri.indexOf(':', 7);
            String host = eprUri.substring(7, portSeperatorPos);
            int port = Integer.parseInt(eprUri.substring(portSeperatorPos + 1,
                    eprUri.indexOf('/', portSeperatorPos)));

            try {
                System.out.println("open Socket on host:" + host + "and port:"
                        + port);
                boolean status = InetAddress.getByName(host).isReachable(
                        TIMEOUT);
                System.out.println("Socket opened: " + status);
                if (!status) {
                    System.err.println("*** Service not running ***");
                    TestThinNrps.serviceAvailable = false;
                }
                TestThinNrps.client = new SimpleReservationClient(eprUri);

            } catch (UnknownHostException e) {
                System.err.println("*** Unknown Host ***");
                TestThinNrps.serviceAvailable = false;
            } catch (IOException e) {
                System.err.println("*** Service not running ***");
                TestThinNrps.serviceAvailable = false;
            }
        } else {
            client = new SimpleReservationClient(new ReservationWS());
        }
    }

    /**
     * Tear down after test suite ran.
     * 
     * @throws java.lang.Exception
     *             An exception.
     */
    @AfterClass
    public static final void tearDownAfterClass() throws Exception {
        // Insert methods for tear down if necessary
    }

    /**
     * Setup before a test.
     * 
     * @throws java.lang.Exception
     *             An exception
     */
    @Before
    public final void setUp() throws Exception {
        //TODO: setup database here?
    }

    /**
     * Tear down after a test.
     * 
     * @throws java.lang.Exception
     *             An exception.
     */
    @After
    public void tearDown() throws Exception {
        // Insert methods for tear down if necessary
    }

    @Test
    public final void testAvailability() throws InvalidRequestFaultException,
            SoapFault {
        if (TestThinNrps.serviceAvailable) {

            IsAvailable request = new IsAvailable();
            IsAvailableType availType = new IsAvailableType();

            availType.setJobID(Long.valueOf(System.currentTimeMillis()));

            final ServiceConstraintType service = new ServiceConstraintType();
            final ConnectionConstraintType connection = new ConnectionConstraintType();
            final FixedReservationConstraintType constraints = new FixedReservationConstraintType();

            constraints.setDuration(TestThinNrps.createPathDuration);
            constraints.setStartTime(Helpers.generateXMLCalendar(4, 500));

            final EndpointType source = new EndpointType();
            source.setEndpointId(createPathSourceTNA[0]);
            final EndpointType target = new EndpointType();
            target.setEndpointId(createPathDestinationTNA[0]);

            connection.setConnectionID(1);
            connection.setMinBW(TestThinNrps.createPathBandwidth);
            connection.setSource(source);
            connection.getTarget().add(target);

            service.setAutomaticActivation(true);
            service.setServiceID(1);
            service.setTypeOfReservation(ReservationType.FIXED);
            service.getConnections().add(connection);
            service.setFixedReservationConstraints(constraints);
            availType.getService().add(service);
            request.setIsAvailable(availType);

            IsAvailableResponse response = (IsAvailableResponse) JaxbSerializer
                    .getInstance().elementToObject(
                            client.isAvailable(JaxbSerializer.getInstance()
                                    .objectToElement(request))

                    );
            Assert.assertNotNull(response);
        } else {
            Assert.fail("Service unavailable");
        }

    }

    @Test(expected = OperationNotAllowedFaultException.class)
    public final void testActivation() throws SoapFault, InterruptedException {
        if (TestThinNrps.serviceAvailable) {

            createdJobId = System.currentTimeMillis();

            Long res1 = createReservation(createdJobId, createPathSourceTNA[0],
                    createPathDestinationTNA[0], 60000, false);

            Thread.sleep(5000);

            Activate request = new Activate();
            ActivateType actType = new ActivateType();

            actType.setReservationID(WebserviceUtils.convertReservationID(res1
                    .longValue()));
            actType.setServiceID(1);
            request.setActivate(actType);

            ActivateResponse response = (ActivateResponse) JaxbSerializer
                    .getInstance().elementToObject(
                            client.activate(JaxbSerializer.getInstance()
                                    .objectToElement(request)));
            Assert.assertTrue(response.getActivateResponse().isSuccess());
        } else {
            Assert.fail("Service unavailable");
        }

    }

    @Test(timeout = TestThinNrps.TIMEOUT)
    public final void testCreateReservation() throws SoapFault {
        if (TestThinNrps.serviceAvailable) {
            Long ret = createReservation(TestThinNrps.createPathSourceTNA[1],
                    TestThinNrps.createPathDestinationTNA[1]);

            Assert.assertNotNull(ret);
            TestThinNrps.createdReservationId = ret.longValue();
        } else {
            Assert.fail("Service unavailable");
        }
    }

    @Test(timeout = TestThinNrps.TIMEOUT)
    public final void testCancelReservation() throws SoapFault {
        if (TestThinNrps.serviceAvailable) {
            Assert
                    .assertTrue(cancelReservation(TestThinNrps.createdReservationId));

        } else {
            Assert.fail("Service unavailable");
        }
    }

    @Test
    public final void testCancelJob() {
        if (TestThinNrps.serviceAvailable) {

            CancelJob request = new CancelJob();
            CancelJobType cancelJob = new CancelJobType();

            createdJobId = System.currentTimeMillis();

            try {
                createReservation(createdJobId, createPathSourceTNA[2],
                        createPathDestinationTNA[2], 50000, true);
                createReservation(createdJobId, createPathSourceTNA[1],
                        createPathDestinationTNA[1], 600000, true);

                cancelJob.setJobID(createdJobId);

                request.setCancelJob(cancelJob);

                CancelJobResponse response = (CancelJobResponse) JaxbSerializer
                        .getInstance().elementToObject(
                                TestThinNrps.client
                                        .cancelJob(JaxbSerializer.getInstance()
                                                .objectToElement(request)));
                Assert.assertTrue(response.getCancelJobResponse().isSuccess());
            } catch (Exception e) {
                Assert.fail(e.getMessage());
            }
        } else {
            Assert.fail("Service unavailable");
        }

    }

    /**
     * @param srcTNA
     * @param destTNA
     * @return
     * @throws IOException
     * @throws JAXBException
     * @throws SAXException
     * @throws SoapFault
     */
    private Long createReservation(final String srcTNA, final String destTNA)
            throws SoapFault {
        return createReservation(System.currentTimeMillis(), srcTNA, destTNA,
                90, true);
    }

    /**
     * @param jobId
     * @param srcTNA
     * @param destTNA
     * @param offset
     * @param activation
     * @return
     * @throws IOException
     * @throws JAXBException
     * @throws SAXException
     * @throws SoapFault
     */
    private Long createReservation(final long jobId, final String srcTNA,
            final String destTNA, final int offset, final boolean activation)
            throws SoapFault {
        final CreateReservationType reservationType = new CreateReservationType();
        final ServiceConstraintType service = new ServiceConstraintType();
        final ConnectionConstraintType connection = new ConnectionConstraintType();
        final FixedReservationConstraintType constraints = new FixedReservationConstraintType();

        constraints.setDuration(TestThinNrps.createPathDuration);
        constraints.setStartTime(Helpers.generateXMLCalendar(4, offset));

        final EndpointType source = new EndpointType();
        source.setEndpointId(srcTNA);
        final EndpointType target = new EndpointType();
        target.setEndpointId(destTNA);

        connection.setConnectionID(1);
        connection.setMinBW(TestThinNrps.createPathBandwidth);
        connection.setSource(source);
        connection.getTarget().add(target);

        service.setAutomaticActivation(activation);
        service.setServiceID(1);
        service.setTypeOfReservation(ReservationType.FIXED);
        service.getConnections().add(connection);
        service.setFixedReservationConstraints(constraints);
        reservationType.getService().add(service);

        TestThinNrps.createdJobId = jobId;
        reservationType.setJobID(Long.valueOf(TestThinNrps.createdJobId));

        final CreateReservationResponseType response = client
                .createReservation(reservationType);

        return Long.valueOf(response.getReservationID());

    }

    /**
     * @param reservationId
     * @return
     * @throws IOException
     * @throws JAXBException
     * @throws SAXException
     * @throws SoapFault
     */
    private boolean cancelReservation(final long reservationId)
            throws SoapFault {
        CancelReservationType cancelType = new CancelReservationType();

        cancelType.setReservationID(WebserviceUtils
                .convertReservationID(reservationId));
        final CancelReservationResponseType response = TestThinNrps.client
                .cancelReservation(cancelType);

        return response.isSuccess();
    }
}
