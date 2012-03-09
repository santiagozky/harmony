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


package eu.ist_phosphorus.harmony.adapter.arrm.handler;

import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.apache.log4j.Logger;

import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.ActivateResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.ActivateType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.AvailabilityCodeType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CancelReservationResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CancelReservationType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.ConnectionAvailabilityType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.ConnectionConstraintType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.ConnectionStatusType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CreateReservationResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CreateReservationType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EndpointInterfaceType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EndpointType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.FixedReservationConstraintType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetReservationResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetReservationsComplexType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetReservationsResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetReservationsType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetStatusResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetStatusType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.IsAvailableResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.IsAvailableType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.ReservationType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.ServiceConstraintType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.StatusType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetStatusResponseType.ServiceStatus;
import eu.ist_phosphorus.harmony.common.serviceinterface.reservation.CommonReservationHandler;
import eu.ist_phosphorus.harmony.common.utils.Config;
import eu.ist_phosphorus.harmony.common.utils.PhLogger;

/**
 * Class to handle NSP reservation-requests in a predictable manner.
 */
public final class ReservationHandler extends CommonReservationHandler {

    /** Singleton instance. */
    private static ReservationHandler selfInstance;

    /** The logger. */
    private Logger logger = PhLogger.getLogger();
    
    /**
     * Instance getter.
     * 
     * @return Singleton Instance
     */
    public static ReservationHandler getInstance() {
        if (ReservationHandler.selfInstance == null) {
            ReservationHandler.selfInstance = new ReservationHandler();
        }
        return ReservationHandler.selfInstance;
    }

    private StatusType status;
    private int serviceID;
    private int reservationID;
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
     * @return
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

    /*
     * (non-Javadoc)
     * 
     * @seeeu.ist_phosphorus.harmony.adapter.dummy.handler.IReservationHandler#
     * runRequest
     * (eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb
     * .ActivateType)
     */
    @Override
    public ActivateResponseType activate(final ActivateType request) {
        final ActivateResponseType responseType = new ActivateResponseType();
        responseType.setSuccess(true);
        this.status = StatusType.ACTIVE;

        return responseType;
    }

    /**
     * 
     * @see ReservationHandler#runRequest(CancelReservationType)
     */
    @Override
    public CancelReservationResponseType cancelReservation(final CancelReservationType request) {
        this.logger.debug("cancelReservation called with following parameter: ");
        this.logger.debug(" GRI: " + request.getReservationID());

        //TODO: cancel the reservation in ARRM/IDC here
        
        /* dummy ------------------------------------------------------------ */
        final CancelReservationResponseType responseType =
                new CancelReservationResponseType();
        this.status = StatusType.CANCELLED_BY_USER;
        responseType.setSuccess(true);
        /* ------------------------------------------------------------------ */
        
        return responseType;
    }

    /*
     * (non-Javadoc)
     * 
     * @seeeu.ist_phosphorus.harmony.adapter.dummy.handler.IReservationHandler#
     * runRequest
     * (eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb
     * .CreateReservationType)
     */
    @Override
    public CreateReservationResponseType createReservation(
            final CreateReservationType harmonyRequest) throws Throwable {
        this.logger.debug("createReservation called with following parameter: ");
        for (ServiceConstraintType service : harmonyRequest.getService()) {
            this.logger.debug(" Type: " + service.getTypeOfReservation());
            this.logger.debug(" Start time: " + service.getFixedReservationConstraints().getStartTime());
            this.logger.debug(" Duration: " + service.getFixedReservationConstraints().getDuration());
            for (ConnectionConstraintType connection : service.getConnections()) {
                this.logger.debug(" From: " + connection.getSource().getEndpointId());
                this.logger.debug(" To: " + connection.getTarget().iterator().next().getEndpointId());
                this.logger.debug(" MinBW: " + connection.getMinBW());
                this.logger.debug(" MaxBW: " + connection.getMaxBW());
                this.logger.debug(" Delay: " + connection.getMaxDelay());
            }
        }
        
        /* send the request to the IDC -------------------------------------- */
        //CreateReservation idcResult = HarmonyIdcTranslator.convertCreateReservation(harmonyRequest);
        /* ------------------------------------------------------------------ */
        
        /* dummy result ----------------------------------------------------- */
        final CreateReservationResponseType responseType =
                new CreateReservationResponseType();

        Long jobID = harmonyRequest.getJobID();
        if (jobID == null) {
            jobID = Long.valueOf(0);
        }
        responseType.setJobID(jobID);
        responseType.setReservationID(String.valueOf(++this.reservationID));
        for (final ServiceConstraintType service : harmonyRequest.getService()) {
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
        /* ------------------------------------------------------------------ */

        return responseType;
    }

    /*
     * (non-Javadoc)
     * 
     * @seeeu.ist_phosphorus.harmony.adapter.dummy.handler.IReservationHandler#
     * runRequest
     * (eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb
     * .GetReservationsType)
     */
    @Override
    public GetReservationsResponseType getReservations(final GetReservationsType request) {
        final GetReservationsResponseType responseType =
                new GetReservationsResponseType();

        /* create a simple answer withou any meaning ------------------------ */
        final GetReservationResponseType rsvType =
                new GetReservationResponseType();
        rsvType.getService().add(this.createTestService());

        for (int i = 1; i <= this.reservationID; i++) {
            final GetReservationsComplexType complexType =
                    new GetReservationsComplexType();
            complexType.setReservationID(String.valueOf(i));
            complexType.setReservation(rsvType);
            responseType.getReservation().add(complexType);
        }
        /* ------------------------------------------------------------------ */

        return responseType;
    }

    /*
     * (non-Javadoc)
     * 
     * @seeeu.ist_phosphorus.harmony.adapter.dummy.handler.IReservationHandler#
     * runRequest
     * (eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb
     * .GetStatusType)
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
        endpoint1.setEndpointId(Config.getString("test", "test.endpoint0.tna"));
        endpoint1.setName("DCE1");
        endpoint1.setDescription("Dummy-Connection Endpoint 1");
        endpoint1.setDomainId("TestDomain1");
        endpoint1.setBandwidth(Integer.valueOf(1));
        endpoint1.setInterface(EndpointInterfaceType.USER);
        cst.setSource(endpoint1);
        final EndpointType endpoint2 = new EndpointType();
        endpoint2.setEndpointId(Config.getString("test", "test.endpoint1.tna"));
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

    /*
     * (non-Javadoc)
     * 
     * @seeeu.ist_phosphorus.harmony.common.serviceinterface.reservation.
     * IReservationHandler
     * #runRequest(eu.ist_phosphorus.harmony.common.serviceinterface
     * .databinding.jaxb.IsAvailableType)
     */
    @Override
    public IsAvailableResponseType isAvailable(final IsAvailableType request) {
        this.logger.debug("isAvailable called with following parameter: ");
        for (ServiceConstraintType service : request.getService()) {
            this.logger.debug(" Type: " + service.getTypeOfReservation());
            this.logger.debug(" Start time: " + service.getFixedReservationConstraints().getStartTime());
            this.logger.debug(" Duration: " + service.getFixedReservationConstraints().getDuration());
            for (ConnectionConstraintType connection : service.getConnections()) {
                this.logger.debug(" From: " + connection.getSource().getEndpointId());
                this.logger.debug(" To: " + connection.getTarget().iterator().next().getEndpointId());
                this.logger.debug(" MinBW: " + connection.getMinBW());
                this.logger.debug(" MaxBW: " + connection.getMaxBW());
                this.logger.debug(" Delay: " + connection.getMaxDelay());
            }
        }
        
        //TODO: ask here the ARRM/IDC for the availability
        
        /* dummy response --------------------------------------------------- */
        final IsAvailableResponseType responseType =
                new IsAvailableResponseType();
        final ConnectionAvailabilityType detailedResult =
                new ConnectionAvailabilityType();
        detailedResult.setAvailability(AvailabilityCodeType.AVAILABLE);
        detailedResult.setConnectionID(1);
        detailedResult.setServiceID(1);
        for (final ServiceConstraintType id : request.getService()) {
            detailedResult.setServiceID(id.getServiceID());
        }
        responseType.getDetailedResult().add(detailedResult);
        responseType.setAlternativeStartTimeOffset(Long
                .valueOf(835678436856783L));
        /* ------------------------------------------------------------------ */

        return responseType;
    }
}
