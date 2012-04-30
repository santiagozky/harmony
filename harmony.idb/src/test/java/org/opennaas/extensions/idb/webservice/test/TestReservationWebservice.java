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


package org.opennaas.extensions.idb.test.webservice;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.muse.ws.addressing.soap.SoapFault;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ActivateResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.BindResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.BindType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelReservationResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ConnectionConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CreateReservationResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CreateReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.FixedReservationConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationsComplexType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationsResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatusResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ServiceConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.StatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.InvalidReservationIDFaultException;
import org.opennaas.extensions.idb.serviceinterface.topology.SimpleTopologyClient;
import org.opennaas.core.utils.Config;
import org.opennaas.core.utils.Helpers;
import org.opennaas.core.utils.PhLogger;
import org.opennaas.extensions.idb.Constants;
import org.opennaas.extensions.idb.database.hibernate.Reservation;
import org.opennaas.extensions.idb.exception.database.DatabaseException;
import org.opennaas.extensions.idb.reservation.handler.ReservationRequestHandler;
import org.opennaas.extensions.idb.webservice.TopologyWS;
import org.opennaas.extensions.idb.test.AbstractTest;

/**
 * JUnit test cases for the IDB Reservation webservice.
 * 
 * @author Alexander Zimmermann (zimmerm2@cs.uni-bonn.de)
 * @version $Id: TestReservationWebservice.java 2313 2008-02-24 01:28:15Z
 *          willner@cs.uni-bonn.de $
 */
public class TestReservationWebservice extends AbstractReservationTest {

    private static LinkedList<String> resIds = new LinkedList<String>();

    @AfterClass
    public static void cleanupTopology() {
        // why do we need this? and we really do need it...
        for (final String res : TestReservationWebservice.resIds) {
            try {
                System.out.println("deleting: " + res);
                Reservation.load(res).delete();
            } catch (final InvalidReservationIDFaultException e) {
                // reservation not found -> nothing to do
            } catch (final DatabaseException e) {
                e.printStackTrace();
            }
        }

        final SimpleTopologyClient topoClient;
        if (Config.isTrue(Constants.testProperties, "test.callWebservice")) {
            topoClient = new SimpleTopologyClient(Config.getString(
                    Constants.testProperties, "epr.topology"));
        } else {
            topoClient = new SimpleTopologyClient(new TopologyWS());
        }
        try {
            topoClient.deleteDomain(Config.getString(Constants.testProperties,
                    "default.domain.name"));
        } catch (final SoapFault e) {
            e.printStackTrace();
        }
    }

    @BeforeClass
    public static void initTopology() throws SoapFault {
        final SimpleTopologyClient topoClient;
        if (Config.isTrue(Constants.testProperties, "test.callWebservice")) {
            topoClient = new SimpleTopologyClient(Config.getString(
                    Constants.testProperties, "epr.topology"));
        } else {
            topoClient = new SimpleTopologyClient(new TopologyWS());
        }
        topoClient.addOrEditDomain(Config.getString(Constants.testProperties,
                "default.domain.name"), Config.getString(
                Constants.testProperties, "default.domain.epr"), Config
                .getString(Constants.testProperties, "default.domain.prefix"));
    }

    protected CreateReservationType resv1 = null;

    protected ServiceConstraintType service1 = null;

    protected ConnectionConstraintType connection1 = null;

    protected EndpointType endpoint1 = null;

    protected EndpointType endpoint2 = null;

    // The following fields resv2, service2, connection2, endpoint3, and
    // endpoint4 aren't used (yet). They can be used to test reservations
    // containing two connections.

    protected CreateReservationType resv2 = null;

    protected ServiceConstraintType service2 = null;

    protected ConnectionConstraintType connection2 = null;

    protected EndpointType endpoint3 = null;

    protected EndpointType endpoint4 = null;

    public TestReservationWebservice() throws SoapFault {
        this.endpoint1 = new EndpointType();
        this.endpoint2 = new EndpointType();
        this.endpoint3 = new EndpointType();
        this.endpoint4 = new EndpointType();
        this.resv1 = new CreateReservationType();
        this.service1 = new ServiceConstraintType();

        final FixedReservationConstraintType reservationConstraintType = new FixedReservationConstraintType();
        reservationConstraintType.setDuration(1);
        reservationConstraintType.setStartTime(Helpers
                .generateXMLCalendar(5, 0));

        this.endpoint1.setEndpointId(Config.getString(Constants.testProperties,
                "default.domain.endpoint1"));
        this.endpoint2.setEndpointId(Config.getString(Constants.testProperties,
                "default.domain.endpoint2"));
        this.endpoint3.setEndpointId(Config.getString(Constants.testProperties,
                "default.domain.endpoint3"));
        this.endpoint4.setEndpointId(Config.getString(Constants.testProperties,
                "default.domain.endpoint4"));

        this.resv1 = new CreateReservationType();
        this.resv1.setJobID(Long.valueOf(0));

        this.service1 = new ServiceConstraintType();
        this.service1.setAutomaticActivation(false);
        this.service1.setServiceID((short) 1);
        this.service1.setFixedReservationConstraints(reservationConstraintType);
        this.service1.setTypeOfReservation(ReservationType.FIXED);

        this.connection1 = new ConnectionConstraintType();
        this.connection1.setConnectionID(1);
        this.connection1.setSource(this.endpoint1);
        this.connection1.unsetTarget();
        this.connection1.getTarget().add(this.endpoint2);
        this.service1.unsetConnections();
        this.service1.getConnections().add(this.connection1);
        this.resv1.unsetService();
        this.resv1.getService().add(this.service1);

        this.resv2 = new CreateReservationType();
        this.resv2.setJobID(Long.valueOf(0));

        this.service2 = new ServiceConstraintType();
        this.service2.setAutomaticActivation(false);
        this.service2.setServiceID((short) 1);
        this.service2.setFixedReservationConstraints(reservationConstraintType);
        this.service2.setTypeOfReservation(ReservationType.FIXED);

        this.connection2 = new ConnectionConstraintType();
        this.connection2.setConnectionID(2);
        this.connection2.setSource(this.endpoint3);
        this.connection2.unsetTarget();
        this.connection2.getTarget().add(this.endpoint4);
        this.service2.unsetConnections();
        this.service2.getConnections().add(this.connection1);
        this.service2.getConnections().add(this.connection2);
        this.resv2.unsetService();
        this.resv2.getService().add(this.service2);
    }

    /**
     * Test a bind request.
     * 
     * @throws Exception
     *             Any Exception that can occur during test-run
     */
    @Test(timeout = AbstractTest.TIMEOUT)
    public final void testBind() throws Exception {
        System.out.println("testBind() ...");
        final BindType requestType = new BindType();

        /* generating valid Bind-Request ----------------------------------- */
        requestType.setEndpointID("128.0.0.1");
        requestType.setConnectionID((short) 1);
        requestType.setReservationID("1");
        requestType.setServiceID((short) 1);
        requestType.getIPAdress().add("10.0.0.1");
        /* ----------------------------------------------------------------- */

        final BindResponseType response = this.reservationClient
                .bind(requestType);
        Assert.assertTrue("Bind result-check", response.isSuccess());
    }

    @Test(timeout = AbstractTest.TIMEOUT)
    public final void testCreateActivateReservation() throws Exception {
        System.out.println("testCreateActivateReservation() ...");
        final CreateReservationResponseType createResponse = this.reservationClient
                .createReservation(this.resv1);
        Assert.assertTrue(createResponse.isSetReservationID());
        TestReservationWebservice.resIds.add(createResponse.getReservationID());
        final ActivateResponseType activateResponse = this.reservationClient
                .activate(createResponse.getReservationID(), this.service1
                        .getServiceID());
        Assert.assertTrue(activateResponse.isSuccess());
        this.checkConnectionStatus(createResponse.getReservationID());
    }

    @Test
    // (timeout = TestReservationWebservice.TIMEOUT)
    public final void testCreateCancelReservation() throws Exception {
        System.out.println("testCreateCancelReservation() ...");
        final CreateReservationResponseType createResponse = this.reservationClient
                .createReservation(this.resv1);
        Assert.assertTrue(createResponse.isSetReservationID());
        TestReservationWebservice.resIds.add(createResponse.getReservationID());
        final CancelReservationResponseType cancelResponse = this.reservationClient
                .cancelReservation(createResponse.getReservationID());
        Assert.assertTrue(cancelResponse.isSuccess());
        // this.reservationId = 0;

    }

    @Test(timeout = AbstractTest.TIMEOUT)
    public final void testCreateReservationAndGetStatus() throws Exception {
        System.out.println("testCreateReservationAndGetStatus() ...");
        final CreateReservationResponseType createResponse = this.reservationClient
                .createReservation(this.resv1);
        Assert.assertTrue(createResponse.isSetReservationID());
        TestReservationWebservice.resIds.add(createResponse.getReservationID());

        final GetStatusResponseType statusResponse = this.reservationClient
                .getStatus(createResponse.getReservationID());
        Assert.assertTrue(statusResponse.isSetServiceStatus());
        Assert.assertTrue(statusResponse.getServiceStatus().size() == 1);
        final GetStatusResponseType.ServiceStatus r0 = statusResponse
                .getServiceStatus().get(0);
        Assert.assertEquals("Service should be pending (no auto activation)",
                StatusType.PENDING, r0.getStatus());

        Assert.assertEquals("Same service IDs", r0.getServiceID(),
                this.service1.getServiceID());
        final CancelReservationResponseType cancelResponse = this.reservationClient
        		.cancelReservation(createResponse.getReservationID());
        Assert.assertTrue(cancelResponse.isSuccess());
    }

    /**
     * Test get reservations.
     * 
     * @throws Exception
     *             Any Exception that can occur during test-run
     */
    @Test(timeout = AbstractTest.TIMEOUT)
    public final void testGetReservations() throws Exception {
        final Set<String> createIDs = new LinkedHashSet<String>();
        final Set<String> getIDs = new LinkedHashSet<String>();

        final CreateReservationResponseType resResult1 = this.reservationClient
                .createReservation(Config.getString(Constants.testProperties,
                        "test.endpoint0.tna"), Config.getString(
                        Constants.testProperties, "test.endpoint1.tna"), 111,
                        111, 111);
        Assert.assertTrue("Should be successful", resResult1
                .isSetReservationID());
        createIDs.add(resResult1.getReservationID());
        TestReservationWebservice.resIds.add(resResult1.getReservationID());
        this.logger.debug("1. reservation: " + resResult1.getReservationID());

        final CreateReservationResponseType resResult2 = this.reservationClient
                .createReservation(Config.getString(Constants.testProperties,
                        "test.endpoint0.tna"), Config.getString(
                        Constants.testProperties, "test.endpoint1.tna"), 111,
                        111, 111);
        Assert.assertTrue("Should be successful", resResult2
                .isSetReservationID());
        createIDs.add(resResult2.getReservationID());
        TestReservationWebservice.resIds.add(resResult2.getReservationID());
        this.logger.debug("2. reservation: " + resResult2.getReservationID());

        final GetReservationsResponseType getResult = this.reservationClient
                .getReservations(1000000000);
        for (final GetReservationsComplexType rsv : getResult.getReservation()) {
            getIDs.add(rsv.getReservationID());
            this.logger.debug("Got reservation: " + rsv.getReservationID());
        }

        for (final String id : createIDs) {
            Assert.assertTrue("ID " + id +  " should be returned",
            		getIDs.contains(id));
        }
        final CancelReservationResponseType cancelResponse = this.reservationClient
				.cancelReservation(resResult1.getReservationID());
        Assert.assertTrue(cancelResponse.isSuccess());
        final CancelReservationResponseType cancelResponse2 = this.reservationClient
				.cancelReservation(resResult2.getReservationID());
        Assert.assertTrue(cancelResponse2.isSuccess());
    }

    /**
     * Test an unimplemented method.
     * 
     * @throws Exception
     */
    @Test(timeout = AbstractTest.TIMEOUT)
    public final void testNullParam() throws Exception {
        System.out.println("testNullParam() ...");
        try {
            this.reservationClient.activate(null, 0);
        } catch (final SoapFault e) {
            System.err.println(e.getMessage());
            Assert.assertEquals("Assert error message", null, e.getNodeURI());
        }
    }

    /**
     * Test singleton cloning method.
     * 
     * @throws CloneNotSupportedException
     */
    @Test(timeout = AbstractTest.TIMEOUT, expected = CloneNotSupportedException.class)
    public final void testSingletonCloning() throws CloneNotSupportedException {
        System.out.println("testSingletonCloning() ...");
        final ReservationRequestHandler resHandler = ReservationRequestHandler
                .getInstance();
        resHandler.clone();
    }

    /**
     * Stress test. Checks wheather the Create Reservation request is too slow.
     * @throws SoapFault If an exception occurs within the IDB.
     */
    //@Test(timeout = AbstractTest.TIMEOUT)
    public final void testStressDummy() throws SoapFault {
        final Logger logger = PhLogger.getSeparateLogger("stresstest");
        final String source = Config.getString("test.default.domain.endpoint1");
        final String target = Config.getString("test.default.domain.endpoint2");
        long beginning = 0;
        long later = 0;
        for (int i = 0; i < 2; i++) {
            long start = System.currentTimeMillis();
            CreateReservationResponseType result = this.reservationClient
                    .createReservation(source, target, 100, 100, 9999);
            long end = System.currentTimeMillis();
            beginning = (end - start);
            Assert.assertTrue(
                    "Create Reservation should take less than 7 seconds",
                    beginning < 7000);
            logger.info("Duration [ms]: " + beginning + " for "
                    + result.getReservationID());
        }
        for (int i = 0; i < 10; i++) {
            long start = System.currentTimeMillis();
            CreateReservationResponseType result = this.reservationClient
                    .createReservation(source, target, 100, 100, 9999);
            long end = System.currentTimeMillis();
            later = (end - start);
            logger.info("Duration [ms]: " + later + " for "
                    + result.getReservationID());
        }
        Assert.assertTrue(
                "Create Reservation should not slow down more than 25%",
                later < beginning + beginning / 100 * 25);
    }
}
