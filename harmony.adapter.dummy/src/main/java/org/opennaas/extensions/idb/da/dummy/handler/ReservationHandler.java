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


package org.opennaas.extensions.idb.da.dummy.handler;

import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ActivateResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ActivateType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AvailabilityCodeType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelReservationResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ConnectionAvailabilityType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ConnectionConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ConnectionStatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CreateReservationResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CreateReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointInterfaceType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.FixedReservationConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationsComplexType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationsResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationsType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatusResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.IsAvailableResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.IsAvailableType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ServiceConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.StatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatusResponseType.ServiceStatus;
import org.opennaas.extensions.idb.serviceinterface.reservation.CommonReservationHandler;
import org.opennaas.core.utils.Config;

/**
 * Class to handle NSP reservation-requests in a predictable manner.
 */
public final class ReservationHandler extends CommonReservationHandler {

    /*
     * Instance Variables
     * =========================================================================
     */

    /** Singleton instance. */
    private static ReservationHandler selfInstance;

    /*
     * Singleton Stuff
     * =========================================================================
     */

    /**
     * Instance getter.
     * 
     * @return Singleton Instance
     */
    public static ReservationHandler getInstance() {
        synchronized (ReservationHandler.class) {
            if (ReservationHandler.selfInstance == null) {
                ReservationHandler.selfInstance = new ReservationHandler();
            }
        }
        return ReservationHandler.selfInstance;
    }

    /** * */
    private StatusType status;
    /** * */
    private int serviceID;
    /** * */
    private int reservationID;
    /** * */
    private int connectionID;

    /** Private constructor: Singleton. */
    private ReservationHandler() {
        super();
        this.status = StatusType.UNKNOWN;
        this.reservationID = 0;
        this.connectionID = 0;
    }

    /**
     * Singleton - Cloning _not_ supported!
     * 
     * @return Should never return anything...
     * @throws CloneNotSupportedException
     *             Singleton hates to be cloned!
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    /*
     * Handler
     * =========================================================================
     */

    /**
     * @return The service contraint.
     * @throws DatatypeConfigurationException
     */
    private ServiceConstraintType createTestService() {
        final ServiceConstraintType service = new ServiceConstraintType();
        service.setTypeOfReservation(ReservationType.FIXED);
        final FixedReservationConstraintType fixedConstraints =
                new FixedReservationConstraintType();
        fixedConstraints.setDuration(100);
        try {
            fixedConstraints.setStartTime(DatatypeFactory.newInstance()
                    .newXMLGregorianCalendar(new GregorianCalendar()));
        } catch (final DatatypeConfigurationException e) {
            e.printStackTrace();
        }

        service.setFixedReservationConstraints(fixedConstraints);
        service.setAutomaticActivation(true);
        final ConnectionConstraintType connection =
                new ConnectionConstraintType();
        final EndpointType src = new EndpointType();
        src.setEndpointId(Config.getString("test", "test.endpoint0.tna"));
        final EndpointType dst = new EndpointType();
        dst.setEndpointId(Config.getString("test", "test.endpoint1.tna"));
        connection.setSource(src);
        connection.getTarget().add(dst);
        service.getConnections().add(connection);

        return service;
    }

    /**
     * Todo.
     */
    @Override
    public ActivateResponseType activate(final ActivateType request) {
        final ActivateResponseType responseType = new ActivateResponseType();
        responseType.setSuccess(true);
        this.status = StatusType.ACTIVE;

        return responseType;
    }

    /**
     * Todo.
     */
    @Override
    public CancelReservationResponseType cancelReservation(
            final CancelReservationType request) {
        final CancelReservationResponseType responseType =
                new CancelReservationResponseType();
        this.status = StatusType.CANCELLED_BY_USER;
        responseType.setSuccess(true);

        return responseType;
    }

    /**
     * @return The reservation ID.
     */
    public String createReservationId() {
        return String.valueOf(++this.reservationID);
    }

    /**
     * Todo.
     */
    @Override
    public CreateReservationResponseType createReservation(
            final CreateReservationType request) {
        final CreateReservationResponseType responseType =
                new CreateReservationResponseType();

        Long jobID = request.getJobID();
        if (jobID == null) {
            jobID = Long.valueOf(0);
        }
        responseType.setJobID(jobID);
        responseType.setReservationID(this.createReservationId());
        for (final ServiceConstraintType service : request.getService()) {
            if (service.isAutomaticActivation()) {
                this.status = StatusType.ACTIVE;
            } else {
                this.status = StatusType.PENDING;
            }
            this.serviceID = service.getServiceID();
            for (final ConnectionConstraintType c : service.getConnections()) {
                this.connectionID = c.getConnectionID();
            }
        }
        if (!this.status.equals(StatusType.ACTIVE)) {
            this.status = StatusType.PENDING;
        }
        return responseType;
    }

    /**
     * Todo.
     */
    @Override
    public GetReservationsResponseType getReservations(
            final GetReservationsType request) {
        final GetReservationsResponseType responseType =
                new GetReservationsResponseType();

        /* create a simple answer without any meaning ----------------------- */
        final GetReservationResponseType rsvType =
                new GetReservationResponseType();
        rsvType.getService().add(this.createTestService());
        final GetReservationsComplexType complexType =
                new GetReservationsComplexType();
        for (int i = 1; i <= this.reservationID; i++) {
            complexType.setReservationID(String.valueOf(i));
            complexType.setReservation(rsvType);
            responseType.getReservation().add(complexType);
        }
        /* ------------------------------------------------------------------ */

        return responseType;
    }

    /**
     * Todo.
     */
    @Override
    public GetStatusResponseType getStatus(final GetStatusType request) {
        final GetStatusResponseType responseType = new GetStatusResponseType();
        final ServiceStatus serviceStatus = new ServiceStatus();

        /* generate DetailedStatus-parameters ------------------------------ */
        final ConnectionStatusType cst = new ConnectionStatusType();
        cst.setStatus(this.status);
        cst.setActualBW(1);
        cst.setConnectionID(this.connectionID);
        cst.setDirectionality(1);
        final EndpointType endpoint1 = new EndpointType();
        endpoint1
                .setEndpointId(Config.getString("test", "test.endpoint0.tna"));
        endpoint1.setName("DCE1");
        endpoint1.setDescription("Dummy-Connection Endpoint 1");
        endpoint1.setDomainId("TestDomain1");
        endpoint1.setBandwidth(Integer.valueOf(1));
        endpoint1.setInterface(EndpointInterfaceType.USER);
        cst.setSource(endpoint1);
        final EndpointType endpoint2 = new EndpointType();
        endpoint2
                .setEndpointId(Config.getString("test", "test.endpoint1.tna"));
        endpoint2.setName("DCE2");
        endpoint2.setDescription("Dummy-Connection Endpoint 2");
        endpoint2.setDomainId("TestDomain2");
        endpoint2.setBandwidth(Integer.valueOf(1));
        endpoint2.setInterface(EndpointInterfaceType.USER);
        cst.getTarget().add(endpoint2);
        /* ----------------------------------------------------------------- */

        serviceStatus.setServiceID(this.serviceID);
        for (final Integer id : request.getServiceID()) {
            serviceStatus.setServiceID(id.intValue());
        }
        serviceStatus.setStatus(this.status);
        serviceStatus.getConnections().add(cst);
        responseType.getServiceStatus().add(serviceStatus);

        return responseType;
    }

    /**
     * Todo.
     */
    @Override
    public IsAvailableResponseType isAvailable(final IsAvailableType request) {
        final IsAvailableResponseType responseType =
                new IsAvailableResponseType();
        final ConnectionAvailabilityType detailedResult =
                new ConnectionAvailabilityType();
        if (this.status == StatusType.ACTIVE) {
            detailedResult
                    .setAvailability(AvailabilityCodeType.ENDPOINT_NOT_AVAILABLE);
        } else {
            detailedResult.setAvailability(AvailabilityCodeType.AVAILABLE);
        }
        detailedResult.setConnectionID(0);
        detailedResult.setServiceID(0);
        for (final ServiceConstraintType id : request.getService()) {
            detailedResult.setServiceID(id.getServiceID());
            for (final ConnectionConstraintType connection : id
                    .getConnections()) {
            	detailedResult.setConnectionID(connection.getConnectionID());
                detailedResult.getBlockedEndpoints().add(
                        connection.getSource().getEndpointId());
                for (EndpointType target : connection.getTarget()) {
                    detailedResult.getBlockedEndpoints().add(
                            target.getEndpointId());
                }
            }

        }
        responseType.getDetailedResult().add(detailedResult);
        responseType.setAlternativeStartTimeOffset(Long.valueOf(70)
        /* Long.valueOf(request.getJobID().longValue() + 1) */);

        return responseType;
    }
}
