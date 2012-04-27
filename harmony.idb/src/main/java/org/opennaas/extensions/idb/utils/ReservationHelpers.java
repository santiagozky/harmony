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


package org.opennaas.extensions.idb.utils;

import java.net.URISyntaxException;
import java.util.List;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.Activate;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ActivateType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.Bind;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.BindType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelJob;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelJobType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelReservation;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CompleteJob;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CompleteJobType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ConnectionConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CreateReservation;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CreateReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.FixedReservationConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservation;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservations;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationsType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatus;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.IsAvailable;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.IsAvailableType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ServiceConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.utils.WebserviceUtils;
import org.opennaas.core.utils.Helpers;
import org.opennaas.extensions.idb.database.hibernate.Connections;
import org.opennaas.extensions.idb.database.hibernate.Domain;
import org.opennaas.extensions.idb.database.hibernate.Endpoint;
import org.opennaas.extensions.idb.database.hibernate.NrpsConnections;
import org.opennaas.extensions.idb.database.hibernate.Reservation;
import org.opennaas.extensions.idb.database.hibernate.Service;

/**
 * Collection Class for small helping methods for reservation-purposes.
 * 
 * @author zimmerm2
 */
public final class ReservationHelpers {
    /**
     * return a Test-ActivateReservation-Request with random values.
     * 
     * @return Activate with random values
     */
    public static Activate getTestActivate() {
        final Activate request = new Activate();
        final ActivateType requestType = new ActivateType();

        /* generating valid ActivateReservation-Request -------------------- */
        requestType.setReservationID(WebserviceUtils
                .convertReservationID(Helpers.getRandomLong()));
        requestType.setServiceID(Helpers.getRandomInt());

        request.setActivate(requestType);
        /* ----------------------------------------------------------------- */
        return request;
    }

    /**
     * return a Test-Bind-Request with random values.
     * 
     * @return Bind with random values
     */
    public static Bind getTestBind() {
        final Bind request = new Bind();
        final BindType requestType = new BindType();

        /* generating valid BindReservation-Request ------------------------ */
        requestType.setConnectionID(Helpers.getRandomInt());
        requestType.setEndpointID(TopologyHelpers.getRandomTNA());
        requestType.setReservationID(WebserviceUtils
                .convertReservationID(Helpers.getRandomLong()));
        requestType.setServiceID(Helpers.getRandomInt());
        requestType.getIPAdress().add(TopologyHelpers.getRandomTNA());

        request.setBind(requestType);
        /* ----------------------------------------------------------------- */
        return request;
    }

    /**
     * return a Test-CancelJob-Request with random values.
     * 
     * @return CancelJob with random values
     */
    public static CancelJob getTestCancelJob() {
        final CancelJob request = new CancelJob();
        final CancelJobType requestType = new CancelJobType();

        /* generating valid CancelJob-Request ------------------------------ */
        requestType.setJobID(Helpers.getRandomLong());

        request.setCancelJob(requestType);
        /* ----------------------------------------------------------------- */
        return request;
    }

    /**
     * return a Test-CancelReservation-Request with random values.
     * 
     * @return CancelReservation with random values
     */
    public static CancelReservation getTestCancelReservation() {
        final CancelReservation request = new CancelReservation();
        final CancelReservationType requestType = new CancelReservationType();

        /* generating valid CancelReservation-Request ---------------------- */
        requestType.setReservationID(WebserviceUtils
                .convertReservationID(Helpers.getRandomLong()));

        request.setCancelReservation(requestType);
        /* ----------------------------------------------------------------- */
        return request;
    }

    /**
     * return a Test-CompleteJob-Request with random values.
     * 
     * @return CompleteJob with random values
     */
    public static CompleteJob getTestCompleteJob() {
        final CompleteJob request = new CompleteJob();
        final CompleteJobType requestType = new CompleteJobType();

        /* generating valid CompleteJob-Request ---------------------------- */
        requestType.setJobID(Helpers.getRandomLong());

        request.setCompleteJob(requestType);
        /* ----------------------------------------------------------------- */
        return request;
    }

    /**
     * return a TestConnection with random values.
     * 
     * @return Connection with random values
     * @throws URISyntaxException 
     */
    public static Connections getTestConnection() throws URISyntaxException {
        final Connections testConnection = new Connections(Helpers
                .getPositiveRandomInt(), ReservationHelpers.getTestService(),
                Helpers.getPositiveRandomInt(), Helpers.getPositiveRandomInt(),
                Helpers.getPositiveRandomInt(), Helpers.getPositiveRandomInt(),
                Helpers.getPositiveRandomInt(), TopologyHelpers
                        .getTestEndpoint());

        return testConnection;
    }

    /**
     * return a Test-CreateReservation-Request with random values.
     * 
     * @return CreateReservation with random values
     */
    public static CreateReservation getTestCreateReservation() {
        final CreateReservation request = new CreateReservation();
        final CreateReservationType requestType = new CreateReservationType();

        /* generating valid CreateReservation-Request ------------------ */
        requestType.setJobID(0L);
        final FixedReservationConstraintType reservationConstraintType = new FixedReservationConstraintType();
        reservationConstraintType.setDuration(Helpers.getRandomInt());

        reservationConstraintType.setStartTime(Helpers.generateXMLCalendar());

        final ServiceConstraintType serviceConstraintType = new ServiceConstraintType();
        serviceConstraintType.setAutomaticActivation(false);
        serviceConstraintType.setServiceID(Helpers.getRandomInt());
        serviceConstraintType
                .setFixedReservationConstraints(reservationConstraintType);
        serviceConstraintType.setTypeOfReservation(ReservationType.FIXED);

        final ConnectionConstraintType connection = new ConnectionConstraintType();

        final EndpointType c1 = new EndpointType();
        c1.setEndpointId(TopologyHelpers.getRandomTNA());
        final EndpointType c2 = new EndpointType();
        c2.setEndpointId(TopologyHelpers.getRandomTNA());
        connection.setSource(c1);
        connection.getTarget().add(c2);
        serviceConstraintType.getConnections().add(connection);

        requestType.getService().add(serviceConstraintType);

        request.setCreateReservation(requestType);
        /* ------------------------------------------------------------- */
        return request;
    }

    public static GetReservation getTestGetReservation(final String id) {
        final GetReservation request = new GetReservation();
        final GetReservationType value = new GetReservationType();
        value.setReservationID(id);
        request.setGetReservation(value);
        return request;
    }

    public static GetReservations getTestGetReservations() {
        final GetReservations request = new GetReservations();
        final GetReservationsType value = new GetReservationsType();
        value.setPeriodStartTime(Helpers.generateXMLCalendar(0, 0));
        value.setPeriodEndTime(Helpers.generateXMLCalendar(100, 0));
        request.setGetReservations(value);
        return request;
    }

    /**
     * return a Test-GetStatus-Request with random values.
     * 
     * @return GetStatus with random values
     */
    public static GetStatus getTestGetStatus() {
        final GetStatus request = new GetStatus();
        final GetStatusType requestType = new GetStatusType();

        /* generating valid GetStatus-Request ------------------------------ */
        requestType.setReservationID(WebserviceUtils
                .convertReservationID(Helpers.getRandomLong()));
        requestType.getServiceID().add(Integer.valueOf(Helpers.getRandomInt()));

        request.setGetStatus(requestType);
        /* ----------------------------------------------------------------- */
        return request;
    }

    /**
     * return a Test-IsAvailable-Request with random values.
     * 
     * @return IsAvailable with random values
     */
    public static IsAvailable getTestIsAvailable() {
        final IsAvailable request = new IsAvailable();
        final IsAvailableType requestType = new IsAvailableType();

        /* generating valid Availability-Request --------------------------- */
        final ServiceConstraintType serviceType = new ServiceConstraintType();
        final FixedReservationConstraintType frct = new FixedReservationConstraintType();
        final ConnectionConstraintType connection = new ConnectionConstraintType();

        final EndpointType c1 = new EndpointType();
        c1.setEndpointId(TopologyHelpers.getRandomTNA());
        final EndpointType c2 = new EndpointType();
        c2.setEndpointId(TopologyHelpers.getRandomTNA());
        connection.setSource(c1);
        connection.getTarget().add(c2);

        frct.setDuration(Helpers.getRandomInt());
        frct.setStartTime(Helpers.generateXMLCalendar());

        serviceType.setFixedReservationConstraints(frct);
        serviceType.getConnections().add(connection);
        serviceType.setTypeOfReservation(ReservationType.FIXED);

        requestType.setJobID(Long.valueOf(Helpers.getRandomLong()));
        requestType.getService().add(serviceType);

        request.setIsAvailable(requestType);
        /* ----------------------------------------------------------------- */
        return request;
    }

    /**
     * return an TestNrpsConnection with random values.
     * 
     * @return NrpsConnection with random values
     * @throws URISyntaxException 
     */
    public static NrpsConnections getTestNrpsConnection() throws URISyntaxException {
        final Domain randomDomain = TopologyHelpers.getTestDomain();
        final List<Endpoint> randomEndpoints = TopologyHelpers
                .getTestEndpointsForDomain(randomDomain);
        final NrpsConnections testNrpsConnection = new NrpsConnections(Helpers
                .getPositiveRandomLong(), ReservationHelpers
                .getTestConnection(), randomDomain, randomEndpoints.get(0),
                randomEndpoints.get(1), Helpers.getPositiveRandomInt(), Helpers
                        .getPositiveRandomInt(),
                Helpers.getPositiveRandomInt(),
                Helpers.getPositiveRandomInt() % 7 + 1);
        return testNrpsConnection;
    }

    /**
     * return a TestReservation with random values.
     * 
     * @return Reservation with random values
     */
    public static Reservation getTestReservation() {
        final Reservation testReservation = new Reservation(Helpers
                .getPositiveRandomLong(), Helpers.getRandomString(), Helpers
                .generateXMLCalendar(10, 0).toGregorianCalendar().getTime(),
                Helpers.getPositiveRandomLong());

        return testReservation;
    }

    /**
     * return a TestService with random values.
     * 
     * @return Service with random values
     */
    public static Service getTestService() {
        final Service testService = new Service(Helpers.getPositiveRandomInt(),
                ReservationHelpers.getTestReservation(), Helpers
                        .getPositiveRandomInt() % 3, Helpers
                        .generateXMLCalendar(10, 0).toGregorianCalendar()
                        .getTime(), Helpers.generateXMLCalendar(20, 0)
                        .toGregorianCalendar().getTime(), Helpers
                        .getPositiveRandomInt(), false);

        return testService;
    }
}