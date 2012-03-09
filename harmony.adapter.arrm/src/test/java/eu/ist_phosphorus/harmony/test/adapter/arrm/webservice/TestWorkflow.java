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


package eu.ist_phosphorus.harmony.test.adapter.arrm.webservice;

import javax.xml.datatype.DatatypeConfigurationException;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.apache.muse.ws.addressing.soap.SoapFault;
import org.junit.Test;

import eu.ist_phosphorus.harmony.adapter.arrm.webservice.ReservationWS;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.AvailabilityCodeType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CancelReservationResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.ConnectionAvailabilityType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CreateReservationResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetReservationsComplexType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetReservationsResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetStatusResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.IsAvailableResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.StatusType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetStatusResponseType.ServiceStatus;
import eu.ist_phosphorus.harmony.common.serviceinterface.reservation.SimpleReservationClient;
import eu.ist_phosphorus.harmony.common.utils.Config;
import eu.ist_phosphorus.harmony.common.utils.PhLogger;

/**
 * 
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 * 
 */
public class TestWorkflow {

    /** The reservation client. */
    private SimpleReservationClient client;

    /** The logger. */
    private final Logger logger;

    /**
     * Default constructor.
     */
    public TestWorkflow() {
        if (Config.isTrue("test", "test.callWebservice")) {
            final String epr = Config.getString("test", "test.reservationEPR");
            this.client = new SimpleReservationClient(epr);
        } else {
            this.client = new SimpleReservationClient(new ReservationWS());
        }
        this.logger = PhLogger.getLogger();
    }

    /**
     * This workflow shows how the adapter is used by the IDB.
     * 
     * @throws SoapFault
     * 
     */
    @Test
    public void testSimpleWorkflow() throws SoapFault,
            DatatypeConfigurationException {
        /* information about the requested path ----------------------------- */
        final int duration = 100; // seconds
        final int bandwidth = 100; // mbit/s
        final int delay = 800; // milliseconds
        final String source = Config.getString("test", "test.endpoint0.tna");
        final String target = Config.getString("test", "test.endpoint1.tna");
        /* ------------------------------------------------------------------ */

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
        Assert.assertTrue("Should return a reservation ID", !"".equals(gri));
        /* ------------------------------------------------------------------ */

        /* optional: ask for all reservations (used in the GUI) ------------- */
        boolean containsReservation = false;
        final GetReservationsResponseType reservationsResult = this.client
                .getReservations(1000);
        for (final GetReservationsComplexType reservation : reservationsResult
                .getReservation()) {
            this.logger.debug("Found reservation: " + reservation.getReservationID());
            if (reservation.getReservationID().equals(gri)) {
                containsReservation = true;
            }
        }
        Assert.assertTrue("Should provide information about the reservations",
                containsReservation);
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
