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
 * Project: IST Phosphorus Harmony System.
 * Module: 
 * Description: 
 *
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 * @version $Id$
 *
 */
package org.opennaas.extensions.idb.da.argon.webservice.test;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.muse.ws.addressing.soap.SoapFault;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import org.opennaas.extensions.idb.da.argon.Constants;
import org.opennaas.extensions.idb.da.argon.webservice.ContextListener;
import org.opennaas.extensions.idb.da.argon.webservice.ReservationWS;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AvailabilityCodeType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelReservationResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ConnectionAvailabilityType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CreateReservationResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationsComplexType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationsResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatusResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.IsAvailableResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ServiceStatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.StatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.EndpointNotFoundFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.OperationNotSupportedFaultException;
import org.opennaas.extensions.idb.serviceinterface.reservation.SimpleReservationClient;
import org.opennaas.core.utils.Config;

/**
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 * @version $Id$
 * 
 */
public class TestReservationWS extends ContextListener {

    /**
     * A simple reservation client.
     */
    private final SimpleReservationClient client;
    private CreateReservationResponseType createResult;

    /**
     * The public constructor.
     */
    public TestReservationWS() {
        super();
        if (Config.isTrue(Constants.testProperties, "test.callWebservice")) {
            final String epr = Config.getString(Constants.testProperties,
                    "test.reservationEPR");
            this.client = new SimpleReservationClient(epr);
        } else {
            this.client = new SimpleReservationClient(new ReservationWS());
        }
        this.startup();
        this.getDomainInformation();
        this.getEndpoints();
    }

    @After
    public void cleanUp() throws SoapFault {
        if (null != this.createResult) {
            this.client.cancelReservation(this.createResult.getReservationID());
        }
    }

    /**
     * This method is not supported yet. 
     * 
     * @throws SoapFault
     */
    @Test(expected = OperationNotSupportedFaultException.class)
    public final void testUnsupportedOperation() throws SoapFault {
        this.client.bind("1", "10.0.0.1", "192.168.0.1");
    }

    /**
     * Test wrong endpoints.
     * 
     * @throws SoapFault
     * @throws DatatypeConfigurationException
     */
    @Test(expected = EndpointNotFoundFaultException.class)
    public final void testWrongEndpoint() throws SoapFault,
            DatatypeConfigurationException {
        /* Test the availability with wrong endpoints ----------------------- */
        this.client.isAvailable("10.7.8.999", "10.7.8.998", 100, 100, 100);
        /* ------------------------------------------------------------------ */
    }

    /**
     * Test method for the most important task: create, getStatus, and cancel.
     * 
     * @throws SoapFault
     *             If an error occurs within the Webservice.
     * @throws DatatypeConfigurationException
     *             Unknown.
     * @throws InterruptedException
     */
    @Test
    public final void testCreateReservationWorkflow() throws SoapFault,
            DatatypeConfigurationException, InterruptedException {

        /* Test the availability -------------------------------------------- */
        final IsAvailableResponseType availResult = this.client.isAvailable(
                Config.getString(Constants.testProperties, "test.endpoint0.tna"), Config
                        .getString(Constants.testProperties, "test.endpoint1.tna"), 100, 100,
                100);
        for (final ConnectionAvailabilityType a : availResult
                .getDetailedResult()) {
            Assert.assertTrue("Connection should be available", a
                    .getAvailability().equals(AvailabilityCodeType.AVAILABLE));
        }
        /* ------------------------------------------------------------------ */

        /* Create the reservation ------------------------------------------- */
        this.createResult = this.client.createReservation(Config.getString(
                Constants.testProperties, "test.endpoint0.tna"), Config.getString(Constants.testProperties,
                "test.endpoint1.tna"), 100, 100, 100);
        Assert.assertTrue("Should return a reservation ID", !this.createResult
                .getReservationID().isEmpty());
        /* ------------------------------------------------------------------ */

        /* Get the status --------------------------------------------------- */
        Thread
                .sleep(Config.getLong(Constants.testProperties, "test.tearupBuffer")
                        .longValue());
        final GetStatusResponseType statusResult = this.client
                .getStatus(this.createResult.getReservationID());
        for (final ServiceStatusType service : statusResult.getServiceStatus()) {
            Assert.assertTrue("Status should be active", service.getStatus()
                    .equals(StatusType.ACTIVE));
        }
        /* ------------------------------------------------------------------ */

        /* Cancel the reservation ------------------------------------------- */
        final CancelReservationResponseType cancelResult = this.client
                .cancelReservation(this.createResult.getReservationID());
        Assert.assertTrue("Cancel should succeed", cancelResult.isSuccess());
        /* ------------------------------------------------------------------ */

        /* Get the status --------------------------------------------------- */
        final GetStatusResponseType statusResult2 = this.client
                .getStatus(this.createResult.getReservationID());
        for (final ServiceStatusType service : statusResult2.getServiceStatus()) {
            Assert.assertTrue("Status should be active", service.getStatus()
                    .equals(StatusType.CANCELLED_BY_USER));
        }
        /* ------------------------------------------------------------------ */
    }

    /**
     * Test method for
     * {@link org.opennaas.extensions.idb.da.argon.webservice.ReservationWS#getReservations(org.w3c.dom.Element)}.
     * 
     * @throws SoapFault
     * @throws DatatypeConfigurationException
     * @throws InterruptedException
     */
    @Test
    public final void testGetReservations() throws SoapFault,
            DatatypeConfigurationException, InterruptedException {
        this.testCreateReservationWorkflow();
        final GetReservationsResponseType rsv = this.client
                .getReservations(1000000);
        boolean hasId = false;
        for (GetReservationsComplexType res : rsv.getReservation()) {
            if (res.getReservationID().equals(this.createResult.getReservationID())) {
                hasId = true;
            }
        }
        Assert.assertTrue("Should contain the least reservation", hasId);
    }
}
