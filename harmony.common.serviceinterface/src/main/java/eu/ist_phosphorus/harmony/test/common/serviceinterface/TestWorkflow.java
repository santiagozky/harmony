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

package eu.ist_phosphorus.harmony.test.common.serviceinterface;

import javax.xml.datatype.DatatypeConfigurationException;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.apache.muse.ws.addressing.soap.SoapFault;
import org.junit.Test;

import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.AvailabilityCodeType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CancelReservationResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.ConnectionAvailabilityType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CreateReservationResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetReservationsComplexType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetReservationsResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetStatusResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.IsAvailableResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.IsAvailableType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.ServiceConstraintType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.StatusType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetStatusResponseType.ServiceStatus;
import eu.ist_phosphorus.harmony.common.serviceinterface.reservation.SimpleReservationClient;
import eu.ist_phosphorus.harmony.common.serviceinterface.topology.SimpleTopologyClient;
import eu.ist_phosphorus.harmony.common.utils.Config;
import eu.ist_phosphorus.harmony.common.utils.PhLogger;

public class TestWorkflow {

    /**
     * This workflow shows how the adapter is used by the IDB.
     * 
     * @throws SoapFault
     */
    public static void testSimpleWorkflow(final SimpleReservationClient client)
            throws SoapFault, DatatypeConfigurationException {
        TestWorkflow.testSimpleWorkflow(client, TestWorkflow.LOGGER);
    }

    /**
     * This workflow shows how the adapter is used by the IDB.
     * 
     * @throws SoapFault
     */
    public static void testSimpleWorkflow(final SimpleReservationClient client,
            final Logger logger) throws SoapFault,
            DatatypeConfigurationException {
        /* information about the requested path ----------------------------- */
        final int duration = 321; // seconds
        final int bandwidth = 100; // mbit/s
        final int delay = 800; // milliseconds
        final String source = Config.getString("test", "test.endpoint0.tna");
        final String target = Config.getString("test", "test.endpoint1.tna");
        /* ------------------------------------------------------------------ */

        TestWorkflow.testSimpleWorkflow(client, logger, source, target,
                duration, bandwidth, delay);
    }

    /**
     * This workflow shows how the adapter is used by the IDB.
     * 
     * @throws SoapFault
     */
    public static void testSimpleWorkflow(final SimpleReservationClient client,
            final Logger logger, final String source, final String target,
            final int duration, final int bandwidth, final int delay)
            throws SoapFault, DatatypeConfigurationException {

        /* is the resource available ---------------------------------------- */
        logger.info("Is the resource available...");
        final IsAvailableResponseType availableResult = client.isAvailable(
                source, target, bandwidth, delay, duration);
        for (final ConnectionAvailabilityType connection : availableResult
                .getDetailedResult()) {
            Assert.assertTrue("Endpoints should be available", connection
                    .getAvailability().equals(AvailabilityCodeType.AVAILABLE));
        }
        /* ------------------------------------------------------------------ */

        /* create the actual reservation ------------------------------------ */
        logger.info("Create the actual reservation...");
        final CreateReservationResponseType createResult = client
                .createReservation(source, target, bandwidth, delay, duration);
        final String gri = createResult.getReservationID();
        PhLogger.getLogger().info("Got ID: " + gri);
        Assert.assertTrue("Should return a reservation ID", !"".equals(gri));
        /* ------------------------------------------------------------------ */

        /* optional: ask for all reservations (used in the GUI) ------------- */
        logger.info("Ask for all reservations (" + gri + ")...");
        boolean containsReservation = false;
        final GetReservationsResponseType reservationsResult = client
                .getReservations(3000);
        for (final GetReservationsComplexType reservation : reservationsResult
                .getReservation()) {
            logger
                    .debug("Found reservation: "
                            + reservation.getReservationID());
            if (reservation.getReservationID().equals(gri)) {
                containsReservation = true;
            }
        }
        logger.info("Found reservations: "
                + reservationsResult.getReservation().size());
        Assert.assertTrue("Should provide information about the reservations",
                containsReservation);
        /* ------------------------------------------------------------------ */

        /* ask for the status of the reservation ---------------------------- */
        logger.info("Ask for the status of the reservation...");
        GetStatusResponseType statusResult = client.getStatus(gri);
        while (StatusType.SETUP_IN_PROGRESS.equals(statusResult
                .getServiceStatus().iterator().next().getStatus())) {
            PhLogger.getLogger().info("Still in setup...");
            try {
                Thread.sleep(4000);
                statusResult = client.getStatus(gri);
            } catch (final InterruptedException e) {
                // Nothing
            }
        }

        for (final ServiceStatus service : statusResult.getServiceStatus()) {
            Assert
                    .assertEquals(
                            "Service should be active since auto-activation is enabled by default",
                            StatusType.ACTIVE, service.getStatus());
        }
        /* ------------------------------------------------------------------ */

        /* endpoints should be blocked now ---------------------------------- */
        logger.info("Endpoints should be blocked now...");
        boolean hasEqualServiceId = false;
        final IsAvailableType iat = SimpleReservationClient
                .getIsAvailableRequest(source, target, bandwidth, delay,
                        duration);

        final IsAvailableResponseType availability = client.isAvailable(iat);
        for (final ConnectionAvailabilityType ca : availability
                .getDetailedResult()) {
            final String availabilityCode = ca.getAvailability().name();
            Assert.assertEquals("Should be not available",
                    AvailabilityCodeType.ENDPOINT_NOT_AVAILABLE.toString(),
                    availabilityCode);
            for (final ServiceConstraintType sct : iat.getService()) {
                if (ca.getServiceID() == sct.getServiceID()) {
                    hasEqualServiceId = true;
                }
            }
            Assert.assertTrue("Must set service ID", ca.isSetServiceID());
            Assert.assertTrue("Must set connection ID", ca.isSetConnectionID());
            Assert.assertTrue("Must set blocked endpoints", ca
                    .isSetBlockedEndpoints());
        }
        Assert.assertTrue("Must have a service ID != 0", hasEqualServiceId);
        /* ------------------------------------------------------------------ */

        /* cancel the reservation again ------------------------------------- */
        logger.info("Cancel the reservation again...");
        final CancelReservationResponseType cancelResult = client
                .cancelReservation(gri);
        Assert.assertTrue("Should succeed", cancelResult.isSuccess());
        /* ------------------------------------------------------------------ */

        /* ask for the status of the reservation ---------------------------- */
        logger.info("Ask for the status of the reservation...");
        GetStatusResponseType statusResult2 = client.getStatus(gri);
        while (StatusType.TEARDOWN_IN_PROGRESS.equals(statusResult2
                .getServiceStatus().iterator().next().getStatus())
                || StatusType.ACTIVE.equals(statusResult2.getServiceStatus()
                        .iterator().next().getStatus())) {
            PhLogger.getLogger().info(
                    "Still cancelling...("
                            + statusResult2.getServiceStatus().iterator()
                                    .next().getStatus() + ")");
            try {
                Thread.sleep(4000);
                statusResult2 = client.getStatus(gri);
            } catch (final InterruptedException e) {
                // Nothing
            }
        }

        for (final ServiceStatus service : statusResult2.getServiceStatus()) {
            Assert.assertTrue("Service should be cancelled now", service
                    .getStatus().equals(StatusType.CANCELLED_BY_USER));
        }
        /* ------------------------------------------------------------------ */

        /* endpoints should not be blocked now ------------------------------ */
        logger.info("Endpoints should not be blocked now...");
        final IsAvailableResponseType availability2 = client.isAvailable(
                source, target, bandwidth, delay, duration);
        for (final ConnectionAvailabilityType ca : availability2
                .getDetailedResult()) {
            final String availabilityCode = ca.getAvailability().name();
            Assert
                    .assertEquals("Should be not available",
                            AvailabilityCodeType.AVAILABLE.toString(),
                            availabilityCode);
        }
        /* ------------------------------------------------------------------ */

    }

    /** The reservation client. */
    private final SimpleReservationClient client;

    /** The reservation client. */
    private final SimpleTopologyClient idb;

    /** The logger. */
    private static final Logger LOGGER = PhLogger.getLogger();

    /**
     * Default constructor.
     */
    public TestWorkflow() {
        final String clientEpr = Config.getString("test",
                "test.client1ReservationEPR");
        this.client = new SimpleReservationClient(clientEpr);

        final String idbEpr = Config.getString("test", "test.idbTopologyEPR");
        this.idb = new SimpleTopologyClient(idbEpr);
    }

    /**
     * Create a reservation between two foreign tnas to check the message
     * forwarding.
     * 
     * @throws SoapFault
     * @throws DatatypeConfigurationException
     */
    @Test
    public void testMessageForwarding() throws SoapFault,
            DatatypeConfigurationException {

        // Test can only be run over webservice
        // =====================================================================
        if (!Config.isTrue("test", "test.callWebservice")) {
            TestWorkflow.LOGGER
                    .info("Message forwarding is only supported by webservice calls");
            return;
        }

        // Add 2 dummy domains to idb
        // =====================================================================
        final String id1 = Config.getString("test", "test.client1Id");
        final String id2 = Config.getString("test", "test.client2Id");

        final String epr1 = Config.getString("test",
                "test.client1ReservationEPR");
        final String epr2 = Config.getString("test",
                "test.client2ReservationEPR");

        final String tna1 = Config.getString("test", "test.client1TNAPrefix");
        final String tna2 = Config.getString("test", "test.client2TNAPrefix");

        this.idb.addOrEditDomain(id1, epr1, tna1);
        this.idb.addOrEditDomain(id2, epr2, tna2);

        // Workflow test
        // =====================================================================
        final int duration = 100; // seconds
        final int bandwidth = 100; // mbit/s
        final int delay = 800; // milliseconds

        final String source = Config.getString("test",
                "test.foreignEndpoint0.tna");
        final String target = Config.getString("test",
                "test.foreignEndpoint1.tna");

        /* is the resource available ---------------------------------------- */
        final IsAvailableResponseType availableResult = this.client
                .isAvailable(source, target, bandwidth, delay, duration);
        for (final ConnectionAvailabilityType connection : availableResult
                .getDetailedResult()) {
            Assert.assertTrue("Endpoints should be available", connection
                    .getAvailability().equals(AvailabilityCodeType.AVAILABLE));
        }
        /* ------------------------------------------------------------------ */

        /* create the actual reservation ------------------------------------ */
        final CreateReservationResponseType createResult = this.client
                .createReservation(source, target, bandwidth, delay, duration);
        final String gri = createResult.getReservationID();
        TestWorkflow.LOGGER.debug("Reservation created: " + gri);
        Assert.assertTrue("Should return a reservation ID", !"".equals(gri));
        /* ------------------------------------------------------------------ */

        /* ask for the status of the reservation ---------------------------- */
        final GetStatusResponseType statusResult = this.client.getStatus(gri);
        for (final ServiceStatus service : statusResult.getServiceStatus()) {
            Assert
                    .assertTrue(
                            "Service should be active since auto-activation is enabled by default",
                            service.getStatus().equals(StatusType.ACTIVE));
        }
        /* ------------------------------------------------------------------ */

        /* cancel the reservation again ------------------------------------- */
        final CancelReservationResponseType cancelResult = this.client
                .cancelReservation(gri);
        Assert.assertTrue("Should succeed", cancelResult.isSuccess());
        /* ------------------------------------------------------------------ */

        /* ask for the status of the reservation ---------------------------- */
        final GetStatusResponseType statusResult2 = this.client.getStatus(gri);
        for (final ServiceStatus service : statusResult2.getServiceStatus()) {
            Assert.assertTrue("Service should be cancelled now", service
                    .getStatus().equals(StatusType.CANCELLED_BY_USER));
        }
        /* ------------------------------------------------------------------ */
    }
}
