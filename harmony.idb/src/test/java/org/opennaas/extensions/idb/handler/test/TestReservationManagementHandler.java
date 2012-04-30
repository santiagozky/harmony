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
 *
 */
package org.opennaas.extensions.idb.handler.test;

import java.net.URISyntaxException;
import java.util.Date;

import junit.framework.Assert;

import org.apache.muse.ws.addressing.soap.SoapFault;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ConnectionConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationsComplexType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationsResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ServiceConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.InvalidReservationIDFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.utils.WebserviceUtils;
import org.opennaas.extensions.idb.database.hibernate.Connections;
import org.opennaas.extensions.idb.database.hibernate.Domain;
import org.opennaas.extensions.idb.database.hibernate.Endpoint;
import org.opennaas.extensions.idb.database.hibernate.Reservation;
import org.opennaas.extensions.idb.database.hibernate.Service;
import org.opennaas.extensions.idb.exception.database.DatabaseException;
import org.opennaas.extensions.idb.reservation.handler.ReservationRequestHandler;
import org.opennaas.extensions.idb.utils.ReservationHelpers;
import org.opennaas.extensions.idb.utils.TopologyHelpers;
import org.opennaas.extensions.idb.test.webservice.AbstractReservationTest;

/**
 * @author zimmerm2
 */
public class TestReservationManagementHandler extends AbstractReservationTest {
    public TestReservationManagementHandler() throws SoapFault {
        super();
    }

    private static ReservationRequestHandler handler;
    private static Domain sourceDomain;
    private static Domain destinationDomain;
    private static Reservation testReservation;
    private static Reservation testReservation2;
    private static long testReservationID;
    private static Service testService;
    private static int testServiceUserID = 12051205;
    private static Connections testConnection;
    private static int testConnectionUserID = 2111115;

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        TestReservationManagementHandler.handler = ReservationRequestHandler
                .getInstance();
        TestReservationManagementHandler.sourceDomain = TopologyHelpers
                .getTestDomain("TRMH-SourceDomain");
        Endpoint end1 = (Endpoint)TestReservationManagementHandler.sourceDomain
        												.getEndpoints().toArray()[0];
        Endpoint end2 = (Endpoint)TestReservationManagementHandler.sourceDomain
														.getEndpoints().toArray()[1];
        TestReservationManagementHandler.sourceDomain.save();
        end1.save();
        end2.save();

        TestReservationManagementHandler.destinationDomain = TopologyHelpers
                .getTestDomain("TRMH-DestinationDomain");
        end1 = (Endpoint)TestReservationManagementHandler.destinationDomain
														.getEndpoints().toArray()[0];
        end2 = (Endpoint)TestReservationManagementHandler.destinationDomain
														.getEndpoints().toArray()[1];
        TestReservationManagementHandler.destinationDomain.save();
        end1.save();
        end2.save();

        TestReservationManagementHandler.testReservation = ReservationHelpers
                .getTestReservation();
        TestReservationManagementHandler.testReservation.setReservationId(0);

        TestReservationManagementHandler.testService = ReservationHelpers
                .getTestService();
        TestReservationManagementHandler.testService
                .setServiceId(TestReservationManagementHandler.testServiceUserID);
        TestReservationManagementHandler.testService.setStartTime(new Date());
        TestReservationManagementHandler.testService.setDuration(360);
        TestReservationManagementHandler.testService.setReservation(
        							TestReservationManagementHandler.testReservation);

        TestReservationManagementHandler.testConnection = ReservationHelpers
                .getTestConnection();
        TestReservationManagementHandler.testConnection
                .setConnectionId(TestReservationManagementHandler.testConnectionUserID);
        Object[] ends = TestReservationManagementHandler.sourceDomain.getEndpoints().toArray();
        TestReservationManagementHandler.testConnection.setStartpoint((Endpoint)ends[0]);
        TestReservationManagementHandler.testConnection.addEndpoint((Endpoint)ends[1]);
        TestReservationManagementHandler.testConnection.setService(
        							TestReservationManagementHandler.testService);
        
        TestReservationManagementHandler.testService.addConnection(
        								TestReservationManagementHandler.testConnection);
        TestReservationManagementHandler.testReservation.addService(
        								TestReservationManagementHandler.testService);
        TestReservationManagementHandler.testReservation.save();

        TestReservationManagementHandler.testReservationID = TestReservationManagementHandler.testReservation
                .getReservationId();
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        if (null != TestReservationManagementHandler.testReservation)
            TestReservationManagementHandler.testReservation.delete();
        if (null != TestReservationManagementHandler.testReservation2)
            TestReservationManagementHandler.testReservation2.delete();
        if (null != TestReservationManagementHandler.sourceDomain)
            TestReservationManagementHandler.sourceDomain.delete();
        if (null != TestReservationManagementHandler.destinationDomain)
            TestReservationManagementHandler.destinationDomain.delete();
    }

    /**
     * Test method for
     * {@link org.opennaas.extensions.idb.reservation.handler.ReservationManagementHandler#getReservation(org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationType)}.
     * 
     * @throws URISyntaxException
     * @throws DatabaseException
     * @throws InvalidReservationIDFaultException
     */
    @Test
    public final void testGetReservation() throws DatabaseException,
            InvalidReservationIDFaultException {
        final GetReservationType getType = new GetReservationType();
        getType
                .setReservationID(String.valueOf(TestReservationManagementHandler.testReservationID));

        final GetReservationResponseType resultWithoutServiceID = TestReservationManagementHandler.handler
                .getReservation(getType);
        Assert.assertFalse("Should contain data", resultWithoutServiceID
                .getService().isEmpty());

        getType
                .getServiceID()
                .add(
                        new Integer(
                                TestReservationManagementHandler.testServiceUserID));
        final GetReservationResponseType resultWithServiceID = TestReservationManagementHandler.handler
                .getReservation(getType);
        Assert.assertFalse("Should contain data", resultWithServiceID
                .getService().isEmpty());
    }

    /**
     * test webservice
     * 
     * @throws Exception
     */
    @Test
    public final void testGetReservations() throws Exception {
        final GetReservationsResponseType responseType = 
            this.reservationClient.getReservations(10000);

        // CHECK RESULT ========================================================
        GetReservationsComplexType checkReservation = null;
        ServiceConstraintType checkService = null;
        ConnectionConstraintType checkConnection = null;

        // Reservation check
        Assert.assertFalse("Should contain data", responseType.getReservation()
                .isEmpty());
        boolean reservationExists = false;
        for (final GetReservationsComplexType reservation : responseType
                .getReservation()) {
            if (WebserviceUtils.convertReservationID(reservation.getReservationID()) == 
                    TestReservationManagementHandler.testReservationID) {
                reservationExists = true;
                checkReservation = reservation;
            }
        }
        Assert.assertTrue("Should contain original reservation",
                reservationExists);

        // Service check
        // Endpoint check
        if (checkReservation == null) {
            throw new DatabaseException("Nobody knows (ID: 23rpgsdf32");
        }

        Assert.assertFalse("reservation should contain at least one service",
                checkReservation.getReservation().getService().isEmpty());
        boolean serviceExists = false;
        for (final ServiceConstraintType service : checkReservation
                .getReservation().getService()) {
            if (service.getServiceID() == TestReservationManagementHandler.testServiceUserID) {
                serviceExists = true;
                checkService = service;
            }
        }
        Assert.assertTrue("Should contain original service", serviceExists);

        // Connection check
        if (checkService == null) {
            throw new DatabaseException("Nobody knows (ID: 23rfadspgsdf32");
        }
        Assert.assertFalse("service should contain least one connection",
                checkService.getConnections().isEmpty());
        boolean connectionExists = false;
        for (final ConnectionConstraintType connection : checkService
                .getConnections()) {
            if (connection.getConnectionID() == TestReservationManagementHandler.testConnectionUserID) {
                connectionExists = true;
                checkConnection = connection;
            }
        }
        Assert.assertTrue("Should contain original connection",
                connectionExists);

        // Endpoint check
        if (checkConnection == null) {
            throw new DatabaseException("Nobody knows (ID: 34223rpasdfgsdf32");
        }

        Assert.assertTrue(checkConnection.getSource().getEndpointId().equals(
                TestReservationManagementHandler.testConnection.getStartpoint()
                        .getTNA()));
        Assert.assertTrue(checkConnection.getTarget().iterator().next()
                .getEndpointId().equals(
                        TestReservationManagementHandler.testConnection
                                .getEndpoints().iterator().next().getTNA()));
    }
    
    @Test
    public final void testGetReservations2() throws Exception {
    	TestReservationManagementHandler.testReservation2 = ReservationHelpers.getTestReservation();
    	Service newTestService = ReservationHelpers.getTestService();
    	newTestService.setStartTime(new Date());
    	newTestService.setDuration(360);
    	Connections newTestConn = ReservationHelpers.getTestConnection();
        Object[] ends = TestReservationManagementHandler.destinationDomain.getEndpoints().toArray();
        newTestConn.setStartpoint((Endpoint)ends[0]);
        newTestConn.addEndpoint((Endpoint)ends[1]);

        newTestService.addConnection(newTestConn);
        TestReservationManagementHandler.testReservation2.addService(newTestService);
        TestReservationManagementHandler.testReservation2.setReservationId(0);
        TestReservationManagementHandler.testReservation2.save();
//        newTestService.save();
//        newTestConn.save();
    	
        // try to get the reservation in a one hour window, back and in front of now
    	final GetReservationsResponseType responseType = 
    									this.reservationClient.getReservations(3600);

    	boolean firstResReturned = false;
    	boolean secondResReturned = false;
    	// check if both reservations are in the response
    	for(GetReservationsComplexType crct : responseType.getReservation()) {
    		if(WebserviceUtils.convertReservationID(crct.getReservationID()) ==
    				TestReservationManagementHandler.testReservation.getReservationId()) {
    			firstResReturned = true;
    		}
    		if(WebserviceUtils.convertReservationID(crct.getReservationID()) ==
    				TestReservationManagementHandler.testReservation2.getReservationId()) {
    			secondResReturned = true;
    		}
    	}
    	Assert.assertTrue("the two reservations should be returned", 
    								(firstResReturned && secondResReturned));
    }
}
