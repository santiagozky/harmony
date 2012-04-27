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

import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

import javax.xml.datatype.XMLGregorianCalendar;

import junit.framework.Assert;

import org.apache.muse.ws.addressing.soap.SoapFault;
import org.junit.After;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelReservationResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ConnectionConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ConnectionStatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CreateReservationResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CreateReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DomainStatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.FixedReservationConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatusResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.MalleableReservationConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ServiceConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatusResponseType.ServiceStatus;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.IsAvailableResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.IsAvailableType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.StatusType;
import org.opennaas.extensions.idb.serviceinterface.reservation.SimpleReservationClient;
import org.opennaas.core.utils.Config;
import org.opennaas.extensions.idb.Constants;
import org.opennaas.extensions.idb.webservice.ReservationWS;
import org.opennaas.extensions.idb.test.AbstractTest;

/**
 * Abstract class for all webservice based tests.
 * 
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 * @version $Id: AbstractTest.java 619 2007-08-29 19:04:26Z
 *          willner@cs.uni-bonn.de $
 */
public abstract class AbstractReservationTest extends AbstractTest {
    /**
     * Reservation client.
     */
    protected SimpleReservationClient reservationClient;
    protected Integer duration;
    protected Integer signalBuffer;
    protected Integer rsvSignalBuffer;
    
    protected Set<String> reservationIds = new HashSet<String>();

    @After
    public final void cleanUp() throws Exception {
    	for (String reservationId : this.reservationIds) {
    		System.out.println("cancelling resv " + reservationId + " ...");
            final CancelReservationType cancelRequest = new CancelReservationType();
            cancelRequest.setReservationID(reservationId);
            try {
	            CancelReservationResponseType r = this.reservationClient.cancelReservation(cancelRequest);
	            System.out.println("   success=" + r.isSuccess());
            } catch (Exception e) {
            	System.out.println("   an exception occured: " + e.getMessage());
            	throw e;
            }
    	}
    }

    /**
     * Default constructor.
     * 
     * @throws SoapFault
     * 
     * @throws URISyntaxException
     * @throws URISyntaxException
     *             If the given EPR is not a valid URI.
     */
    protected AbstractReservationTest() {
        super();
        this.duration = Config.getInt(Constants.testProperties, "default.duration");
        this.signalBuffer = Config.getInt(Constants.testProperties, "default.signalBuffer");
        this.rsvSignalBuffer = Config.getInt(Constants.testProperties, "default.rsvSignalBuffer");

        if (Config.isTrue(Constants.testProperties, "test.callWebservice")) {
            this.reservationClient = new SimpleReservationClient(Config
                    .getString(Constants.testProperties, "epr.reservation"));
        } else {
            this.reservationClient = new SimpleReservationClient(
                    new ReservationWS());
        }
    }

    /*
     * protected CancelReservationResponseType cancelReservation(Long
     * reservationID) throws SoapFault { final CancelReservationType requestType
     * = new CancelReservationType();
     * requestType.setReservationID(reservationID.longValue()); final
     * CancelReservationResponseType response =
     * this.reservationClient.cancelReservation(requestType); return response; }
     */
    /**
     * @param resResponse
     * @throws Exception
     */
    protected GetStatusResponseType checkConnectionStatus(
            final String reservationId) throws Exception {
        return this.checkConnectionStatus(reservationId, StatusType.ACTIVE,
                StatusType.SETUP_IN_PROGRESS);
    }

    protected GetStatusResponseType checkConnectionStatus(
            final String reservationId,
            final StatusType statusType) throws Exception {
        return this.checkConnectionStatus(reservationId, statusType, statusType);
    }

    /**
     * @param resResponse
     * @throws Exception
     */
    protected GetStatusResponseType checkConnectionStatus(
            final String reservationId,
            final StatusType status1, final StatusType status2)
            throws Exception {

        final GetStatusType statusReq;
        final GetStatusResponseType statusResponse;

        statusReq = new GetStatusType();
        statusReq.setReservationID(reservationId);
        statusResponse = this.reservationClient.getStatus(statusReq);
        Assert.assertTrue("The reservation should contain at least 1 service",
                statusResponse.getServiceStatus().size() > 0);

        for (final ServiceStatus servStatus : statusResponse.getServiceStatus()) {
            Assert.assertTrue("The service should contain at least 1 domain",
                    servStatus.getDomainStatus().size() > 0);
            for (final DomainStatusType domStatus : servStatus
                    .getDomainStatus()) {
                Assert.assertTrue("Domain '" + domStatus.getDomain()
                        + "' should be " + status1 + " now, but it is: "
                        + domStatus.getStatus(), (domStatus.getStatus().equals(
                        status1) || domStatus.getStatus().equals(status2)));
            }
            Assert.assertTrue(
                    "The service should contain at least 1 connection",
                    servStatus.getConnections().size() > 0);
            Assert.assertTrue("Service for reservation '"
                    + reservationId
                    + "' should be active now, but it is: "
                    + servStatus.getStatus(), (servStatus.getStatus().equals(
                    status1) || servStatus.getStatus().equals(status2)));
            for (final ConnectionStatusType conStatus : servStatus
                    .getConnections()) {
                Assert.assertTrue("Connections for reservation '"
                        + reservationId
                        + "' should be active now, but it is: "
                        + conStatus.getStatus(), (conStatus.getStatus().equals(
                        status1) || conStatus.getStatus().equals(status2)));
            }
        }
        
        return statusResponse;
    }

    protected IsAvailableResponseType checkAvailability(
            final String src, final String dst, final XMLGregorianCalendar startTime, final int duration) throws SoapFault {
        final IsAvailableType req = new IsAvailableType();
        req.getService()
                .add(this.createFixedTestService(src, dst, startTime, duration));
        return this.reservationClient.isAvailable(req);
    }
    /**
     * Helper function to create a test service.
     *
     * @param src
     *                Source Endpoint.
     * @param dst
     *                Destination Endpoint.
     * @param duration
     *                Duration in seconds.
     * @return The ServiceConstraintType.
     */
    protected ServiceConstraintType createFixedTestService(final String src,
            final String dst, final XMLGregorianCalendar startTime, final int duration) {
        final ServiceConstraintType service = new ServiceConstraintType();
        final FixedReservationConstraintType constraints =
                new FixedReservationConstraintType();
        final EndpointType source = new EndpointType();
        final EndpointType target = new EndpointType();
        final ConnectionConstraintType connection =
                this.createTestConnection(source, target);

        constraints.setDuration(duration);
        constraints.setStartTime(startTime);

        source.setEndpointId(src);
        target.setEndpointId(dst);

        service.setAutomaticActivation(true);
        service.setServiceID(1);
        service.setTypeOfReservation(ReservationType.FIXED);
        service.getConnections().add(connection);
        service.setFixedReservationConstraints(constraints);

        return service;
    }
    
    protected ServiceConstraintType createMalleableTestService(final String src,
    		final String dst, final XMLGregorianCalendar startTime, final XMLGregorianCalendar deadline, final int dataAmount) {
        final ServiceConstraintType service = new ServiceConstraintType();
        final MalleableReservationConstraintType constraints =
                new MalleableReservationConstraintType();
        System.out.println("    startTime "+startTime.toString());
        System.out.println("    deadline "+deadline.toString());
        final EndpointType source = new EndpointType();
        final EndpointType target = new EndpointType();
        final ConnectionConstraintType connection =
                this.createTestConnection(source, target);
        connection.setDataAmount(100000l);
        connection.setMinBW(100);
        connection.setMaxBW(1000);

        constraints.setStartTime(startTime);
        constraints.setDeadline(deadline);

        source.setEndpointId(src);
        target.setEndpointId(dst);

        service.setAutomaticActivation(true);
        service.setServiceID(1);
        service.setTypeOfReservation(ReservationType.MALLEABLE);
        service.getConnections().add(connection);
        service.setMalleableReservationConstraints(constraints);

        return service;
    }

    /**
     * Helper function to create a test connection.
     *
     * @param src
     *                Source Endpoint.
     * @param dst
     *                Destination Endpoint.
     * @return A ConnectionContraintType.
     */
    private ConnectionConstraintType createTestConnection(
            final EndpointType src,
            final EndpointType dst) {
        final ConnectionConstraintType connection =
                new ConnectionConstraintType();
        connection.setConnectionID(1);
        connection.setDirectionality(1);
        connection.setMinBW(1000);
        //connection.setMaxBW(1000);
        connection.setMaxDelay(1000);
        connection.setSource(src);
        connection.getTarget().add(dst);
        return connection;
    }

    /**
     * Helper function to create a test reservation.
     * 
     * @param src
     *            Source Endpoint.
     * @param dst
     *            Destination Endpoint.
     * @param duration
     *            Duration in milliseconds.
     * @return The CreateReservationResponseType.
     * @throws Exception
     *             In case an error occurs.
     */
    protected CreateReservationResponseType createFixedTestReservation(
            final String src, final String dst, final XMLGregorianCalendar startTime, final int duration)
            throws Exception {
/*        return this.reservationClient.createReservation(src, dst, Config
                .getInt(Constants.testProperties, "default.bandwidth").intValue(), Config.getInt(
                Constants.testProperties, "default.delay").intValue(), duration); */
    	CreateReservationType request = new CreateReservationType();
    	request.getService().add(createFixedTestService(src, dst, startTime, duration));
    	return this.reservationClient.createReservation(request);
    }

    protected CreateReservationResponseType createMalleableTestReservation(
    		final String src, final String dst, final XMLGregorianCalendar startTime, final XMLGregorianCalendar deadline, final int dataAmount) throws SoapFault {
    	CreateReservationType request = new CreateReservationType();
    	request.getService().add(createMalleableTestService(src, dst, startTime, deadline, dataAmount));
    	return this.reservationClient.createReservation(request);
    }
}
