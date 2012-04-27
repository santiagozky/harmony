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


package org.opennaas.extensions.idb.da.argia.utils;


import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.Activate;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ActivateType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelJob;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelJobType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelReservation;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CompleteJob;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CompleteJobType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ConnectionConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CreateReservation;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CreateReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointInterfaceType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.FixedReservationConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatus;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.IsAvailable;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.IsAvailableType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ServiceConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.utils.WebserviceUtils;
import org.opennaas.core.utils.Helpers;

/**
 * Collection Class for small helping methods for reservation-purposes.
 *
 * @author zimmerm2, Angel Sanchez
 */
public final class ReservationHelpersUCLP {

    /**
     * return a Test-CreateReservation-Request with random values.
     *
     * @return CreateReservation with random values
     */
    public static CreateReservation getTestCreateReservation() {
        CreateReservation request = new CreateReservation();
        CreateReservationType requestType = new CreateReservationType();

        /* generating valid CreateReservation-Request ------------------ */
        requestType.setJobID(0L);
        FixedReservationConstraintType reservationConstraintType =
                new FixedReservationConstraintType();


        reservationConstraintType.setDuration(30);

        reservationConstraintType.setStartTime(Helpers.generateXMLCalendar());

        ServiceConstraintType serviceConstraintType =
                new ServiceConstraintType();

        serviceConstraintType = new ServiceConstraintType();
        serviceConstraintType.setAutomaticActivation(true);
        serviceConstraintType.setServiceID(1);
        serviceConstraintType
                .setFixedReservationConstraints(reservationConstraintType);
        serviceConstraintType.setTypeOfReservation(ReservationType.FIXED);

        final ConnectionConstraintType connection =
                new ConnectionConstraintType();

        EndpointType c1 = new EndpointType();
        c1.setEndpointId("10.3.1.15");
        c1.setInterface(EndpointInterfaceType.BORDER);
        EndpointType c2 = new EndpointType();
        c2 = new EndpointType();
        c2.setEndpointId("10.3.1.16");
        c2.setInterface(EndpointInterfaceType.BORDER);

        connection.setSource(c1);
        connection.getTarget().add(c2);
        connection.setConnectionID(1);
        connection.setMaxBW(1000);
        connection.setMaxDelay(200);
        serviceConstraintType.getConnections().add(connection);

        requestType.getService().add(serviceConstraintType);
        requestType.setJobID(0L);

        request.setCreateReservation(requestType);
        /* ------------------------------------------------------------- */
        return request;
    }

    /**
     * return a Test-IsAvailable-Request with random values.
     *
     * @return IsAvailable with random values
     */
    public static IsAvailable getTestIsAvailable() {
        IsAvailable request = new IsAvailable();
        IsAvailableType requestType = new IsAvailableType();

        /* generating valid Availability-Request --------------------------- */
        final ServiceConstraintType serviceType = new ServiceConstraintType();
        final FixedReservationConstraintType frct =
                new FixedReservationConstraintType();
        final ConnectionConstraintType connection =
                new ConnectionConstraintType();

        EndpointType c1 = new EndpointType();
        c1.setEndpointId("10.3.1.15");
        c1.setInterface(EndpointInterfaceType.BORDER);

        EndpointType c2 = new EndpointType();
        c2 = new EndpointType();
        c2.setEndpointId("10.3.1.16");
        c2.setInterface(EndpointInterfaceType.BORDER);

        connection.setSource(c1);
        connection.getTarget().add(c2);
        connection.setConnectionID(1);
        connection.setMaxBW(1000);
        connection.setMaxDelay(200);

        frct.setDuration(30);
        frct.setStartTime(Helpers.generateXMLCalendar());

        serviceType.setFixedReservationConstraints(frct);
        serviceType.getConnections().add(connection);
        serviceType.setTypeOfReservation(ReservationType.FIXED);
        serviceType.setAutomaticActivation(true);

        requestType.setJobID(0L);
        requestType.getService().add(serviceType);

        request.setIsAvailable(requestType);
        /* ----------------------------------------------------------------- */
        return request;
    }

    /**
     * return a Test-ActivateReservation-Request with random values.
     *
     * @return Activate with random values
     */
    public static Activate getTestActivate() {
        Activate request = new Activate();
        ActivateType requestType = new ActivateType();

        /* generating valid ActivateReservation-Request -------------------- */
        requestType.setReservationID(WebserviceUtils
                .convertReservationID(Helpers.getRandomLong()));
        requestType.setServiceID(Helpers.getRandomInt());

        request.setActivate(requestType);
        /* ----------------------------------------------------------------- */
        return request;
    }

    /**
     * return a Test-CancelJob-Request with random values.
     *
     * @return CancelJob with random values
     */
    public static CancelJob getTestCancelJob() {
        CancelJob request = new CancelJob();
        CancelJobType requestType = new CancelJobType();

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
        CancelReservation request = new CancelReservation();
        CancelReservationType requestType = new CancelReservationType();

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
        CompleteJob request = new CompleteJob();
        CompleteJobType requestType = new CompleteJobType();

        /* generating valid CompleteJob-Request ---------------------------- */
        requestType.setJobID(Helpers.getRandomLong());

        request.setCompleteJob(requestType);
        /* ----------------------------------------------------------------- */
        return request;
    }

    /**
     * return a Test-GetStatus-Request with random values.
     *
     * @return GetStatus with random values
     */
    public static GetStatus getTestGetStatus() {
        GetStatus request = new GetStatus();
        GetStatusType requestType = new GetStatusType();

        /* generating valid GetStatus-Request ------------------------------ */
        requestType.setReservationID(WebserviceUtils
                .convertReservationID(Helpers.getRandomLong()));

        request.setGetStatus(requestType);
        /* ----------------------------------------------------------------- */
        return request;
    }

}
