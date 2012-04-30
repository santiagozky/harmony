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


/**
 * Project: IST Phosphorus Harmony System. Module: Description:
 * 
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 * @version $Id$
 */
package org.opennaas.extensions.idb.da.dummy.webservice.test;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.muse.ws.addressing.soap.SoapFault;
import org.junit.Assert;
import org.junit.Test;

import org.opennaas.extensions.idb.da.dummy.webservice.ReservationWS;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AvailabilityCodeType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelReservationResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ConnectionAvailabilityType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CreateReservationResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationsResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatusResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.IsAvailableResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ServiceStatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.StatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.OperationNotSupportedFaultException;
import org.opennaas.extensions.idb.serviceinterface.reservation.SimpleReservationClient;
import org.opennaas.core.utils.Config;

/**
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 * @version $Id$
 */
public class TestReservationWS {

    /**
     * A simple reservation client.
     */
    private final SimpleReservationClient client;

    /**
     * The public constructor.
     */
    public TestReservationWS() {
        if (Config.isTrue("test", "test.callWebservice")) {
            final String epr = Config.getString("test", "test.reservationEPR");
            this.client = new SimpleReservationClient(epr);
        } else {
            this.client = new SimpleReservationClient(new ReservationWS());
        }
    }

    /**
     * Test method for the most important task: create, getStatus, and cancel.
     * 
     * @throws SoapFault
     *             If an error occurs within the Webservice.
     * @throws DatatypeConfigurationException
     *             Unknown.
     */
    @Test
    public final void testCreateReservationWorkflow() throws SoapFault,
            DatatypeConfigurationException {

        /* Test the availability -------------------------------------------- */
        final IsAvailableResponseType availResult =
                this.client.isAvailable(Config.getString("test",
                        "test.endpoint0.tna"), Config.getString("test",
                        "test.endpoint1.tna"), 100, 100, 100);
        for (final ConnectionAvailabilityType a : availResult
                .getDetailedResult()) {
            Assert.assertTrue("Connection should be available", a
                    .getAvailability().equals(AvailabilityCodeType.AVAILABLE));
        }
        /* ------------------------------------------------------------------ */

        /* Create the reservation ------------------------------------------- */
        final CreateReservationResponseType createResult =
                this.client.createReservation(Config.getString("test",
                        "test.endpoint0.tna"), Config.getString("test",
                        "test.endpoint1.tna"), 100, 100, 100);
        Assert.assertTrue("Should return a reservation ID", !createResult
                .getReservationID().isEmpty());
        /* ------------------------------------------------------------------ */

        /* Get the status --------------------------------------------------- */
        final GetStatusResponseType statusResult =
                this.client.getStatus(createResult.getReservationID());
        Assert.assertTrue("Should contain at least one reservation",
                statusResult.getServiceStatus().size() > 0);
        for (final ServiceStatusType service : statusResult.getServiceStatus()) {
            Assert.assertTrue("Status should be active", service.getStatus()
                    .equals(StatusType.ACTIVE));
        }
        /* ------------------------------------------------------------------ */

        /* Cancel the reservation ------------------------------------------- */
        final CancelReservationResponseType cancelResult =
                this.client.cancelReservation(createResult.getReservationID());
        Assert.assertTrue("Cancel should succeed", cancelResult.isSuccess());
        /* ------------------------------------------------------------------ */

        /* Get the status --------------------------------------------------- */
        final GetStatusResponseType statusResult2 =
                this.client.getStatus(createResult.getReservationID());
        for (final ServiceStatusType service : statusResult2
                .getServiceStatus()) {
            Assert.assertTrue("Status should be active", service.getStatus()
                    .equals(StatusType.CANCELLED_BY_USER));
        }
        /* ------------------------------------------------------------------ */
    }

    /**
     * Test method for
     * .
     * 
     * @throws SoapFault
     * @throws DatatypeConfigurationException
     */
    @Test
    public final void testGetReservations() throws SoapFault,
            DatatypeConfigurationException {
        this.testCreateReservationWorkflow();
        final GetReservationsResponseType rsv =
                this.client.getReservations(10000);
        Assert.assertTrue("Should contain at least one reservation", !rsv
                .getReservation().isEmpty());
    }

    /**
     * This method is not supported yet. So it should fail.
     * 
     * @throws SoapFault
     */
    @Test(expected = OperationNotSupportedFaultException.class)
    public final void testBind() throws SoapFault {
        this.client.bind("1@" + Config.getString("hsiDummy", "domain.name"),
                "10.0.0.1", "192.168.0.1");
    }
}
