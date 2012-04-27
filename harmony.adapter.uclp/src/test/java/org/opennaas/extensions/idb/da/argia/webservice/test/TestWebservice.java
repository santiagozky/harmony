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


package org.opennaas.extensions.idb.da.argia.webservice.test;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.apache.muse.ws.addressing.EndpointReference;
import org.apache.muse.ws.addressing.soap.SoapFault;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Element;

import org.opennaas.extensions.idb.da.argia.utils.ReservationHelpersUCLP;
import org.opennaas.extensions.idb.da.argia.webservice.ReservationWS;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.Activate;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ActivateResponse;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ActivateResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelJob;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelJobResponse;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelJobResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelReservation;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelReservationResponse;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelReservationResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CompleteJob;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CompleteJobResponse;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CompleteJobResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CreateReservation;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CreateReservationResponse;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CreateReservationResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatus;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatusResponse;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatusResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.IsAvailable;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.IsAvailableResponse;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.IsAvailableResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.OperationNotSupportedFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.utils.JaxbSerializer;
import org.opennaas.extensions.idb.serviceinterface.reservation.SimpleReservationClient;
import org.opennaas.core.utils.Config;
import org.opennaas.core.utils.PhLogger;

/**
 * JUnit test cases for the UCLPv2-Webservice.
 * 
 * @author Angel Sanchez (angel.sanchez@i2cat.net)
 * 
 */
public class TestWebservice {
    /**
     * General timeout for a test.
     */
    private static final int TIMEOUT = 10000;

    /**
     * Web Service URI.
     */
    private static EndpointReference wsEpr;

    /**
     * Web Service client.
     */
    private static SimpleReservationClient client;

    /**
     * The logger.
     */
    private static final Logger logger = PhLogger
            .getLogger(TestWebservice.class);

    /**
     * Setup before creating test suite.
     * 
     * @throws java.lang.Exception
     *             An exception.
     */
    @BeforeClass
    public static final void setUpBeforeClass() throws Exception {
        if (Config.isTrue("test", "test.callWebservice")) {
            TestWebservice.client = new SimpleReservationClient(Config
                    .getString("test", "test.reservationEPR"));
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
        // Insert methods for tear down if neccessary
    }

    /**
     * Setup before a test.
     * 
     * @throws java.lang.Exception
     *             An exception
     */
    @Before
    public final void setUp() throws Exception {
        // nothing yet
    }

    /**
     * Tear down before a test.
     * 
     * @throws java.lang.Exception
     *             An exception.
     */
    @After
    public void tearDown() throws Exception {
        // Insert methods for tear down if necessary
    }

    /**
     * create GetStatus-request, send it to the webservice and return the
     * response.
     * 
     * @return GetStatusResponseType
     */
    private final GetStatusResponseType getStatus() throws Exception {
        GetStatus request = ReservationHelpersUCLP.getTestGetStatus();
        request.setGetStatus(new GetStatusType());
        GetStatusResponse response = new GetStatusResponse();

        /* serializing the request ----------------------------------------- */
        Element requestElement = JaxbSerializer.getInstance().objectToElement(
                request);
        /* ----------------------------------------------------------------- */

        /* calling webservice ---------------------------------------------- */
        Element responseElement = TestWebservice.client
                .getStatus(requestElement);

        response = (GetStatusResponse) JaxbSerializer.getInstance()
                .elementToObject(responseElement);
        /* ----------------------------------------------------------------- */
        return response.getGetStatusResponse();
    }

    /**
     * create Availability-request, send it to the webservice and return the
     * response.
     * 
     * @return IsAvailableResponseType
     */
    private final IsAvailableResponseType createAvailability() throws Exception {
        IsAvailable request = ReservationHelpersUCLP.getTestIsAvailable();
        IsAvailableResponse response = new IsAvailableResponse();

        /* serializing request ----------------------------------------- */
        Element requestElement = JaxbSerializer.getInstance().objectToElement(
                request);
        /* ------------------------------------------------------------- */

        /* calling webservice ------------------------------------------ */
        Element responseElement = TestWebservice.client
                .isAvailable(requestElement);
        response = (IsAvailableResponse) JaxbSerializer.getInstance()
                .elementToObject(responseElement);
        /* ------------------------------------------------------------- */
        return response.getIsAvailableResponse();
    }

    /**
     * create Reservation-request, send it to the webservice and return the
     * response.
     * 
     * @return CreateReservationResponseType
     */
    private final CreateReservationResponseType createReservation()
            throws Exception {
        CreateReservation request = ReservationHelpersUCLP
                .getTestCreateReservation();
        CreateReservationResponse response = new CreateReservationResponse();

        /* serializing request ----------------------------------------- */
        Element requestElement = JaxbSerializer.getInstance().objectToElement(
                request);
        /* ------------------------------------------------------------- */

        /* calling webservice ------------------------------------------ */
        Element responseElement = TestWebservice.client
                .createReservation(requestElement);

        response = (CreateReservationResponse) JaxbSerializer.getInstance()
                .elementToObject(responseElement);
        /* ------------------------------------------------------------- */

        return response.getCreateReservationResponse();
    }

    /**
     * create Activate-request, send it to the webservice and return the
     * response.
     * 
     * @return ActivateResponseType
     */
    private final ActivateResponseType createActivate() throws Exception {
        Activate request = ReservationHelpersUCLP.getTestActivate();
        ActivateResponse response = new ActivateResponse();

        /* serializing request ----------------------------------------- */
        Element requestElement = JaxbSerializer.getInstance().objectToElement(
                request);
        /* ------------------------------------------------------------- */

        /* calling webservice ------------------------------------------ */
        Element responseElement = TestWebservice.client
                .activate(requestElement);
        response = (ActivateResponse) JaxbSerializer.getInstance()
                .elementToObject(responseElement);
        /* ------------------------------------------------------------- */
        return response.getActivateResponse();
    }

    /**
     * create CancelReservation-request, send it to the webservice and return
     * the response.
     * 
     * @return CancelReservationResponseType
     */
    private final CancelReservationResponseType cancelReservation()
            throws Exception {
        CancelReservation request = ReservationHelpersUCLP
                .getTestCancelReservation();
        CancelReservationResponse response = new CancelReservationResponse();

        /* serializing request ----------------------------------------- */
        Element requestElement = JaxbSerializer.getInstance().objectToElement(
                request);
        /* ------------------------------------------------------------- */

        /* calling webservice ------------------------------------------ */
        Element responseElement = TestWebservice.client
                .cancelReservation(requestElement);
        response = (CancelReservationResponse) JaxbSerializer.getInstance()
                .elementToObject(responseElement);
        /* ------------------------------------------------------------- */
        return response.getCancelReservationResponse();
    }

    /**
     * create CompleteJob-request, send it to the webservice and return the
     * response.
     * 
     * @return CompleteJobResponseType
     */
    private final CompleteJobResponseType completeJob() throws Exception {
        CompleteJob request = ReservationHelpersUCLP.getTestCompleteJob();
        CompleteJobResponse response = new CompleteJobResponse();

        /* serializing request ----------------------------------------- */
        Element requestElement = JaxbSerializer.getInstance().objectToElement(
                request);
        /* ------------------------------------------------------------- */

        /* calling webservice ------------------------------------------ */
        Element responseElement = TestWebservice.client
                .completeJob(requestElement);
        response = (CompleteJobResponse) JaxbSerializer.getInstance()
                .elementToObject(responseElement);
        /* ------------------------------------------------------------- */
        return response.getCompleteJobResponse();
    }

    /**
     * create CancelJob-request, send it to the webservice and return the
     * response.
     * 
     * @return CancelJobResponseType
     */
    private final CancelJobResponseType cancelJob() throws Exception {
        CancelJob request = ReservationHelpersUCLP.getTestCancelJob();
        CancelJobResponse response = new CancelJobResponse();

        /* serializing request ----------------------------------------- */
        Element requestElement = JaxbSerializer.getInstance().objectToElement(
                request);
        /* ------------------------------------------------------------- */

        /* calling webservice ------------------------------------------ */
        Element responseElement = TestWebservice.client
                .cancelJob(requestElement);
        response = (CancelJobResponse) JaxbSerializer.getInstance()
                .elementToObject(responseElement);
        /* ------------------------------------------------------------- */
        return response.getCancelJobResponse();
    }

    /**
     * Test getting status.
     * 
     * @throws Exception
     *             Any Exception that can occur during test-run
     */
    @Test(timeout = TestWebservice.TIMEOUT)
    public final void testGetStatus() throws Exception {
        GetStatusResponseType responseType = this.client.getStatus("1");
        Assert.assertTrue("Check ServiceStatus", responseType
                .isSetServiceStatus());
    }

    /**
     * Test availability.
     * 
     * @throws Exception
     *             Any Exception that can occur during test-run
     */
    @Test(timeout = TestWebservice.TIMEOUT)
    public final void testAvailability() throws Exception {
        IsAvailableResponseType responseType = createAvailability();
        Assert.assertTrue("Alternative Starttime set", responseType
                .isSetDetailedResult());
    }

    /**
     * Test creating a reservation.
     * 
     * @throws Exception
     *             Any Exception that can occur during test-run
     */
    @Test(timeout = TestWebservice.TIMEOUT)
    public final void testCreateReservation() throws Exception {
        CreateReservationResponseType responseType = this.client
                .createReservation(Config.getString("test",
                        "test.endpoint0.tna"), Config.getString("test",
                        "test.endpoint1.tna"), 100, 100, 100);

        Assert.assertTrue("ReservationID is empty", responseType
                .getReservationID().length() > 0);
    }

    /**
     * Test activating a reservation.
     * 
     * @throws Exception
     *             Any Exception that can occur during test-run
     */
    @Test(timeout = TestWebservice.TIMEOUT)
    public final void testActivateReservation() throws Exception {
        ActivateResponseType responseType = createActivate();
        Assert.assertFalse("Check Activate success", responseType.isSuccess());
    }

    /**
     * Test cancelling a reservation.
     * 
     * @throws Exception
     *             Any Exception that can occur during test-run
     */
    @Test(timeout = TestWebservice.TIMEOUT)
    public final void testCancelReservation() throws Exception {
        CancelReservationResponseType responseType = cancelReservation();
        Assert.assertFalse("Check CancelReservation success", responseType
                .isSuccess());
    }

    /**
     * Test unimplemented method.
     * 
     * @throws Exception
     *             Any Exception that can occur during test-run
     */
    @Test(timeout = TestWebservice.TIMEOUT, expected = OperationNotSupportedFaultException.class)
    public final void testUnimplementedMethod() throws Exception {
        this.client.bind("1.2.3.4", "5.6.7.8", "9.8.7.6");
    }
}
