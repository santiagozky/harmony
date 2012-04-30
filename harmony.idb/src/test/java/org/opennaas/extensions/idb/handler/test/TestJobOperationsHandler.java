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


package org.opennaas.extensions.idb.handler.test;

import java.util.List;

import junit.framework.Assert;

import org.apache.muse.ws.addressing.soap.SoapFault;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Element;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelJob;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelJobResponse;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelJobType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CompleteJob;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CompleteJobResponse;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CompleteJobType;
import org.opennaas.extensions.idb.serviceinterface.databinding.utils.JaxbSerializer;
import org.opennaas.extensions.idb.database.hibernate.Connections;
import org.opennaas.extensions.idb.database.hibernate.Domain;
import org.opennaas.extensions.idb.database.hibernate.Endpoint;
import org.opennaas.extensions.idb.database.hibernate.Reservation;
import org.opennaas.extensions.idb.database.hibernate.Service;
import org.opennaas.extensions.idb.utils.ReservationHelpers;
import org.opennaas.extensions.idb.utils.TopologyHelpers;
import org.opennaas.extensions.idb.test.AbstractTest;
import org.opennaas.extensions.idb.test.webservice.AbstractReservationTest;

/**
 * JUnit test cases for the ReservationSetupHandler.
 * 
 * @author Alexander Zimmermann (zimmerm2@cs.uni-bonn.de)
 * @version $Id$
 */
public class TestJobOperationsHandler extends AbstractReservationTest {
    public TestJobOperationsHandler() throws SoapFault {
        super();
    }

    /**
     * /** Test-Domain.
     */
    private static Domain testDomain = null;
    /** first Test-Reservation. */
    private static Reservation testRes1 = null;
    /** second Test-Reservation. */
    private static Reservation testRes2 = null;
    /** Test-JobId. */
    private static final long testJobId = 1205;
    /** first Test-Connection. */
    private static Connections testConn1 = null;
    /** second Test-Connection. */
    private static Connections testConn2 = null;
    /** first Test-Service. */
    private static Service testService1 = null;
    /** second Test-Service. */
    private static Service testService2 = null;

    /**
     * Setup before creating test suite.
     * 
     * @throws java.lang.Exception
     *             An exception.
     */
    @BeforeClass
    public static final void setUpBeforeClass() throws Exception {
        /* Create Test-Domain ---------------------------------------------- */
        TestJobOperationsHandler.testDomain = TopologyHelpers.getTestDomain();
        TestJobOperationsHandler.testDomain.save();

        /* Create Reservation 1 with endpoints, connection and service ----- */
        TestJobOperationsHandler.testRes1 = ReservationHelpers
                .getTestReservation();
        TestJobOperationsHandler.testRes1.setReservationId(0);
        TestJobOperationsHandler.testRes1.save();

        TestJobOperationsHandler.testService1 = ReservationHelpers
                .getTestService();
        TestJobOperationsHandler.testService1.setPK_service(0);
        TestJobOperationsHandler.testService1
                .setReservation(TestJobOperationsHandler.testRes1);
        TestJobOperationsHandler.testService1.save();

        TestJobOperationsHandler.testConn1 = ReservationHelpers
                .getTestConnection();
        TestJobOperationsHandler.testConn1
                .setService(TestJobOperationsHandler.testService1);
        final Endpoint end = TopologyHelpers.getTestEndpoint();
        end.setDomain(TestJobOperationsHandler.testDomain);
        end.save();
        TestJobOperationsHandler.testConn1.setStartpoint(end);

        final List<Endpoint> testEndpoints = TopologyHelpers
                .getTestEndpointsForDomain(TestJobOperationsHandler.testDomain);
        for (final Endpoint e : testEndpoints) {
            e.save();
            TestJobOperationsHandler.testConn1.getEndpoints().add(e);
        }
        TestJobOperationsHandler.testConn1.save();
        /* ----------------------------------------------------------------- */

        /* Create Reservation 2 with endpoints, connection and service ----- */
        TestJobOperationsHandler.testRes2 = ReservationHelpers
                .getTestReservation();
        TestJobOperationsHandler.testRes2.setReservationId(0);
        TestJobOperationsHandler.testRes2.save();

        TestJobOperationsHandler.testService2 = ReservationHelpers
                .getTestService();
        TestJobOperationsHandler.testService2.setPK_service(0);
        TestJobOperationsHandler.testService2
                .setReservation(TestJobOperationsHandler.testRes2);
        TestJobOperationsHandler.testService2.save();

        TestJobOperationsHandler.testConn2 = ReservationHelpers
                .getTestConnection();
        TestJobOperationsHandler.testConn2
                .setService(TestJobOperationsHandler.testService2);
        final Endpoint end2 = TopologyHelpers.getTestEndpoint();
        end2.setDomain(TestJobOperationsHandler.testDomain);
        end2.save();
        TestJobOperationsHandler.testConn2.setStartpoint(end);

        final List<Endpoint> testEndpoints2 = TopologyHelpers
                .getTestEndpointsForDomain(TestJobOperationsHandler.testDomain);
        for (final Endpoint e : testEndpoints2) {
            e.save();
            TestJobOperationsHandler.testConn2.getEndpoints().add(e);
        }
        TestJobOperationsHandler.testConn2.save();
        /* ----------------------------------------------------------------- */
    }

    /**
     * Tear down after test suite ran.
     * 
     * @throws java.lang.Exception
     *             An exception.
     */
    @AfterClass
    public static final void tearDownAfterClass() throws Exception {
        // delete testRes1
        TestJobOperationsHandler.testRes1.delete();

        // delete testRes2
        TestJobOperationsHandler.testRes2.delete();

        // delete testDomain
        TestJobOperationsHandler.testDomain.delete();
    }

    /**
     * Test CancelJob request.
     * 
     * @throws Exception
     *             An Exception
     */
    @Test(timeout = AbstractTest.TIMEOUT)
    public final void testCancelJob() throws Exception {
        CancelJobResponse response = new CancelJobResponse();
        final CancelJob request = new CancelJob();
        final CancelJobType requestType = new CancelJobType();

        requestType.setJobID(TestJobOperationsHandler.testJobId);
        request.setCancelJob(requestType);
        final Element requestElement = JaxbSerializer.getInstance()
                .objectToElement(request);

        /* testing over webservice ------------------------------------- */
        final Element responseElement = this.reservationClient
                .cancelJob(requestElement);
        response = (CancelJobResponse) JaxbSerializer.getInstance()
                .elementToObject(responseElement);

        Assert.assertTrue("Check Direct-CancelJob-Result", response
                .getCancelJobResponse().isSuccess());
        /* -------------------------------------------------------------- */
    }

    /**
     * Test CompleteJob request.
     * 
     * @throws Exception
     *             An Exception
     */
    @Test(timeout = AbstractTest.TIMEOUT)
    public final void testCompleteJob() throws Exception {
        CompleteJobResponse response = new CompleteJobResponse();
        final CompleteJob request = new CompleteJob();
        final CompleteJobType requestType = new CompleteJobType();

        requestType.setJobID(TestJobOperationsHandler.testJobId);
        request.setCompleteJob(requestType);
        final Element requestElement = JaxbSerializer.getInstance()
                .objectToElement(request);

        /* testing over webservice ------------------------------------- */
        final Element responseElement = this.reservationClient
                .completeJob(requestElement);
        response = (CompleteJobResponse) JaxbSerializer.getInstance()
                .elementToObject(responseElement);

        Assert.assertTrue("Check Direct-CompleteJob-Result", response
                .getCompleteJobResponse().isSuccess());
        /* -------------------------------------------------------------- */
    }
}
