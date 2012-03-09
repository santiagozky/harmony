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

package eu.ist_phosphorus.harmony.test.testbed;

import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

import javax.xml.datatype.XMLGregorianCalendar;

import junit.framework.Assert;

import org.apache.muse.ws.addressing.soap.SoapFault;
import org.junit.After;

import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CancelReservationResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CancelReservationType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.ConnectionConstraintType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.ConnectionStatusType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CreateReservationResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CreateReservationType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.DomainStatusType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EndpointType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.FixedReservationConstraintType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetStatusResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetStatusType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.IsAvailableResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.IsAvailableType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.MalleableReservationConstraintType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.ReservationType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.ServiceConstraintType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.StatusType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetStatusResponseType.ServiceStatus;
import eu.ist_phosphorus.harmony.common.serviceinterface.reservation.SimpleReservationClient;
import eu.ist_phosphorus.harmony.common.utils.Config;

/**
 * Abstract class for all webservice based tests.
 * 
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 * @version $Id: AbstractTest.java 619 2007-08-29 19:04:26Z
 *          willner@cs.uni-bonn.de $
 */
/**
 * @author willner
 *
 */
/**
 * @author willner
 *
 */
/**
 * @author willner
 * 
 */
public abstract class AbstractReservationTest extends AbstractTest {
    /**
     * Reservation client.
     */
    protected final SimpleReservationClient reservationClient;
    protected final int duration = Config.getInt("test", "default.duration");
    protected int bandwidth = Config.getInt("test", "default.bandwidth");
    protected final int delay = Config.getInt("test", "default.delay");
    protected final int signalBuffer = Config.getInt("test",
            "default.signalBuffer");
    protected final int rsvSignalBuffer = Config.getInt("test",
            "default.rsvSignalBuffer");
    protected String reservationId;

    protected Set<String> reservationIds = new HashSet<String>();

    /**
     * Default constructor.
     * 
     * @throws SoapFault
     * @throws URISyntaxException
     * @throws URISyntaxException
     *             If the given EPR is not a valid URI.
     */
    protected AbstractReservationTest(final String epr) {
        super();
        this.reservationClient = new SimpleReservationClient(epr);
    }

    /**
     * @param src
     * @param dst
     * @param startTime
     * @param duration
     * @return The response.
     * @throws SoapFault
     */
    protected IsAvailableResponseType checkAvailability(final String src,
            final String dst, final XMLGregorianCalendar startTime,
            final int duration) throws SoapFault {
        final IsAvailableType req = new IsAvailableType();
        req.getService().add(
                this.createFixedTestService(src, dst, startTime, duration));
        return this.reservationClient.isAvailable(req);
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
     * @param reservationId
     * @return The status.
     * @throws Exception
     */
    protected GetStatusResponseType checkConnectionStatus(
            final String reservationId) throws Exception {
        return this.checkConnectionStatus(reservationId, StatusType.ACTIVE,
                StatusType.SETUP_IN_PROGRESS);
    }

    /**
     * @param reservationId
     * @param statusType
     * @return The status.
     * @throws Exception
     */
    protected GetStatusResponseType checkConnectionStatus(
            final String reservationId, final StatusType statusType)
            throws Exception {
        return this
                .checkConnectionStatus(reservationId, statusType, statusType);
    }

    /**
     * @param reservationId
     * @param status1
     * @param status2
     * @return The status.
     * @throws Exception
     */
    protected GetStatusResponseType checkConnectionStatus(
            final String reservationId, final StatusType status1,
            final StatusType status2) throws Exception {

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
            Assert.assertTrue("Service for reservation '" + reservationId
                    + "' should be active now, but it is: "
                    + servStatus.getStatus(), (servStatus.getStatus().equals(
                    status1) || servStatus.getStatus().equals(status2)));
            for (final ConnectionStatusType conStatus : servStatus
                    .getConnections()) {
                Assert.assertTrue("Connections for reservation '"
                        + reservationId + "' should be active now, but it is: "
                        + conStatus.getStatus(), (conStatus.getStatus().equals(
                        status1) || conStatus.getStatus().equals(status2)));
            }
        }

        return statusResponse;
    }

    /**
     * @throws Exception
     */
    @After
    public final void cleanUp() throws Exception {
        final CancelReservationType cancelRequest = new CancelReservationType();
        CancelReservationResponseType r;
        for (final String reservationId : this.reservationIds) {
            System.out.println("cancelling resv " + reservationId + " ...");
            cancelRequest.setReservationID(reservationId);
            try {
                r = this.reservationClient.cancelReservation(cancelRequest);
                System.out.println("   success=" + r.isSuccess());
            } catch (final Exception e) {
                System.out
                        .println("   an exception occured: " + e.getMessage());
                throw e;
            }
        }
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
            final String src, final String dst,
            final XMLGregorianCalendar startTime, final int duration)
            throws Exception {
        /*
         * return this.reservationClient.createReservation(src, dst, Config
         * .getInt("test", "default.bandwidth").intValue(), Config.getInt(
         * "test", "default.delay").intValue(), duration);
         */
        final CreateReservationType request = new CreateReservationType();
        request.getService().add(
                this.createFixedTestService(src, dst, startTime, duration));
        return this.reservationClient.createReservation(request);
    }

    /**
     * Helper function to create a test service.
     * 
     * @param src
     *            Source Endpoint.
     * @param dst
     *            Destination Endpoint.
     * @param duration
     *            Duration in seconds.
     * @return The ServiceConstraintType.
     */
    protected ServiceConstraintType createFixedTestService(final String src,
            final String dst, final XMLGregorianCalendar startTime,
            final int duration) {
        final ServiceConstraintType service = new ServiceConstraintType();
        final FixedReservationConstraintType constraints = new FixedReservationConstraintType();
        final EndpointType source = new EndpointType();
        final EndpointType target = new EndpointType();
        final ConnectionConstraintType connection = this.createTestConnection(
                source, target);

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

    protected CreateReservationResponseType createMalleableTestReservation(
            final String src, final String dst,
            final XMLGregorianCalendar startTime,
            final XMLGregorianCalendar deadline, final int dataAmount)
            throws SoapFault {
        final CreateReservationType request = new CreateReservationType();
        request.getService().add(
                this.createMalleableTestService(src, dst, startTime, deadline,
                        dataAmount));
        return this.reservationClient.createReservation(request);
    }

    /**
     * Todo.
     * 
     * @param src
     * @param dst
     * @param startTime
     * @param deadline
     * @param dataAmount
     * @return The service contraints.
     */
    protected ServiceConstraintType createMalleableTestService(
            final String src, final String dst,
            final XMLGregorianCalendar startTime,
            final XMLGregorianCalendar deadline, final int dataAmount) {
        final ServiceConstraintType service = new ServiceConstraintType();
        final MalleableReservationConstraintType constraints = new MalleableReservationConstraintType();
        System.out.println("    startTime " + startTime.toString());
        System.out.println("    deadline " + deadline.toString());
        final EndpointType source = new EndpointType();
        final EndpointType target = new EndpointType();
        final ConnectionConstraintType connection = this.createTestConnection(
                source, target);
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
     *            Source Endpoint.
     * @param dst
     *            Destination Endpoint.
     * @return A ConnectionContraintType.
     */
    private ConnectionConstraintType createTestConnection(
            final EndpointType src, final EndpointType dst) {
        final ConnectionConstraintType connection = new ConnectionConstraintType();
        connection.setConnectionID(1);
        connection.setDirectionality(1);
        connection.setMinBW(1000);
        // connection.setMaxBW(1000);
        connection.setMaxDelay(1000);
        connection.setSource(src);
        connection.getTarget().add(dst);
        return connection;
    }
}
