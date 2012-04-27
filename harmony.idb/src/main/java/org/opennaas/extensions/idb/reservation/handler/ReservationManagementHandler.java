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


package org.opennaas.extensions.idb.reservation.handler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ConnectionConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.FixedReservationConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationResponse;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationsComplexType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationsResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationsType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ServiceConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.InvalidRequestFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.InvalidReservationIDFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.utils.JaxbSerializer;
import org.opennaas.extensions.idb.serviceinterface.databinding.utils.WebserviceUtils;
import org.opennaas.core.utils.Helpers;
import org.opennaas.core.utils.PhLogger;
import org.opennaas.extensions.idb.database.hibernate.Connections;
import org.opennaas.extensions.idb.database.hibernate.Endpoint;
import org.opennaas.extensions.idb.database.hibernate.Reservation;
import org.opennaas.extensions.idb.database.hibernate.Service;
import org.opennaas.extensions.idb.exception.database.DatabaseException;

/**
 * Handler for ReservationManagement operations.
 */
public final class ReservationManagementHandler {
    /** Singleton instance. */
    private static ReservationManagementHandler selfInstance = null;

    /**
     * Instance getter.
     * 
     * @return Singleton Instance
     */
    public static ReservationManagementHandler getInstance() {
        if (ReservationManagementHandler.selfInstance == null) {
            ReservationManagementHandler.selfInstance = new ReservationManagementHandler();
        }
        return ReservationManagementHandler.selfInstance;
    }

    /** Logger. */
    private final Logger logger = PhLogger.getLogger(this.getClass());

    /** Private constructor: Singleton. */
    private ReservationManagementHandler() {
        // nothing yet
    }

    /**
     * check if all of the reservations are valid. This means they all have to
     * consist of services, connections and endpoints. Reservations not
     * fulfilling the constraints are deleted from the response-type. No
     * semantic check here!
     * 
     * @param checkType
     *            GetReservationResponseType
     * @return true, if valid, false else
     */
    private boolean checkIfValid(final GetReservationResponseType checkType) {
        boolean result = false;

        try {
            // check through Serializer and SyntxValidator if type is
            // correct
            final GetReservationResponse checkElement = new GetReservationResponse();
            checkElement.setGetReservationResponse(checkType);
            JaxbSerializer.getInstance().objectToElement(checkElement, false);
            result = true;
        } catch (final InvalidRequestFaultException e) {
            // validating the type went wrong -> remove reservation from
            // response and send fatal error mail
            this.logger.fatal("Inconsistent DB!! Not expected NULL-values!!");
        } catch (final Exception e) {
            // any other exception is not because of a invalid type
            e.printStackTrace();
        }
        return result;
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

    /**
     * create a ConnectionConstraintType out of a Connection.
     * 
     * @param connection
     *            Connections
     * @return ConnectionConstraintType
     */
    private ConnectionConstraintType createConnectionConstraintType(
            final Connections connection) {
        final ConnectionConstraintType cct = new ConnectionConstraintType();
        this.logger.debug("Connection ID: " + connection.getConnectionId());
        cct.setConnectionID(connection.getConnectionId());
        cct.setMinBW(connection.getMinBandwidth());
        cct.setMaxBW(new Integer(connection.getMaxBandwidth()));
        cct.setMaxDelay(new Integer(connection.getMaxLatency()));
        cct.setDataAmount(new Long(connection.getDataAmount()));
        cct.setDirectionality(connection.getDirectionality());
        cct.setSource(connection.getStartpoint().toJaxb());
        // load actual endpoints from DB
        for (final Endpoint e : connection.getEndpoints()) {
            cct.getTarget().add(e.toJaxb());
        }
        return cct;
    }

    /**
     * create a GetReservationResponseType out of a Reservation.
     * 
     * @param reservation
     *            Reservation
     * @return GetReservationResponseType
     */
    private GetReservationResponseType createGetReservationResponseType(
            final Reservation reservation) {
        final GetReservationResponseType responseType = new GetReservationResponseType();
        //this.logger.debug(reservation.getDebugInfo());
        responseType.setJobID(new Long(reservation.getJobId()));
        responseType.setNotificationConsumerURL(reservation.getConsumerUrl());

        for (final Entry<Integer, Service> service : reservation.getServices()
                .entrySet()) {

            responseType.getService().add(
                    this.createServiceConstraintType(service.getValue(),
                            service.getKey()));
        }

        return responseType;
    }

    /**
     * create a ServiceConstraintType out of a Service.
     * 
     * @param service
     *            Service
     * @return ServiceConstraintType
     */
    private ServiceConstraintType createServiceConstraintType(
            final Service service, final int serviceId) {
        final ServiceConstraintType sct = new ServiceConstraintType();
        sct.setServiceID(serviceId);
        this.logger.debug("Service ID: " + serviceId);
        // TODO: where to get ReservationType???
        // till now we only use fixed Reservations
        sct.setTypeOfReservation(ReservationType.FIXED);
        final FixedReservationConstraintType frc = new FixedReservationConstraintType();
        frc.setDuration(service.getDuration());
        frc.setStartTime(Helpers.DateToXmlCalendar(service.getStartTime()));
        sct.setFixedReservationConstraints(frc);
        sct.setAutomaticActivation(service.isAutomaticActivation());
        // set all connections corresponding to the service
        for (final Entry<Integer, Connections> connection : service
                .getConnections().entrySet()) {

            connection.getValue().setConnectionId(connection.getKey());
            sct.getConnections().add(
                    this.createConnectionConstraintType(connection.getValue()));
        }
        return sct;
    }

    /**
     * GetReservation Handler.
     * 
     * @param element
     *            GetReservationType
     * @return GetReservationResponseType
     * @throws DatabaseException
     *             A DatabaseException
     * @throws InvalidReservationIDFaultException
     */
    public GetReservationResponseType getReservation(
            final GetReservationType element) throws DatabaseException,
            InvalidReservationIDFaultException {
        final GetReservationResponseType responseType = new GetReservationResponseType();

        final Reservation reservation = Reservation.load(element
                .getReservationID());
        if (reservation == null) {
        	throw new InvalidReservationIDFaultException("reservation " + element.getReservationID() + " not found");
        }
        responseType.setJobID(new Long(reservation.getJobId()));
        responseType.setNotificationConsumerURL(reservation.getConsumerUrl());

        final List<ServiceConstraintType> serviceConstraintTypes = this
                .getServices(element.getReservationID(), element.getServiceID());

        responseType.getService().addAll(serviceConstraintTypes);

        return responseType;
    }

    /**
     * GetReservations Handler.
     * 
     * @param element
     *            GetReservationsType
     * @return GetReservationsResponseType
     * @throws DatabaseException
     *             A DatabaseException
     */
    public GetReservationsResponseType getReservations(
            final GetReservationsType element) throws DatabaseException {
        final GetReservationsResponseType tempResponse = new GetReservationsResponseType();

        final GetReservationsResponseType actualResponse = new GetReservationsResponseType();
        this.logger.debug("Get RSV from: " + element.getPeriodStartTime());
        this.logger.debug("Get RSC to: " + element.getPeriodEndTime());
        
        // get all reservations from DB, which lie in the given time period
        final Set<Reservation> reservations = Reservation.loadAllInPeriod(
                element.getPeriodStartTime().toGregorianCalendar().getTime(),
                element.getPeriodEndTime().toGregorianCalendar().getTime());
        this.logger.debug("reservation count " + reservations.size());

        // HACK-BEGIN only changed for EU-DEMO ---->
        // Calendar now = Calendar.getInstance();
        // for (Reservation reservation : reservations) {
        // Calendar end = null;
        // for (Service s : reservation.getServices()) {
        // Calendar cal = Calendar.getInstance();
        // cal.setTime(s.getStartTime());
        // cal.add(Calendar.SECOND, s.getDuration());
        // if ((end == null) || (cal.after(end))) {
        // end = cal;
        // }
        // }
        // if ((end != null) && end.after(now)) {
        // ----> HACK-END

        for (final Reservation reservation : reservations) {
            this.logger
                    .debug("reservationID:" + reservation.getReservationId());

            // get Status-Information for the actual reservation and put them
            // in the response
            final GetReservationsComplexType complexType = new GetReservationsComplexType();
            complexType.setReservationID(WebserviceUtils.convertReservationID(reservation
                    .getReservationId()));

            complexType.setReservation(this
                    .createGetReservationResponseType(reservation));

            tempResponse.getReservation().add(complexType);
        }

        // check if all reservations, that will be sent back are valid
        for (final GetReservationsComplexType compType : tempResponse
                .getReservation()) {
            if (this.checkIfValid(compType.getReservation())) {
                actualResponse.getReservation().add(compType);
            }
        }

        return actualResponse;
    }

    /**
     * get all serviceConstraintTypes for a reservation with known service-IDs.
     * 
     * @param resID
     *            long
     * @param idList
     *            List < Integer >
     * @return List < ServiceConstraintType >
     * @throws DatabaseException
     *             A DatabaseException
     */
    private List<ServiceConstraintType> getServices(final long resID,
            final List<Integer> idList) throws DatabaseException {
        final List<ServiceConstraintType> responseList = new ArrayList<ServiceConstraintType>();

        final Set<Service> services = new HashSet<Service>();
        if (idList.isEmpty()) {
            // get all services through reservation
            final Reservation reservation = Reservation.load(resID);
            services.addAll(reservation.getServices().values());
        } else {
            // get the services over corresponding service-user-Id and
            // reservation-Id.
            // This is not the primary-key in the DB, therefore we have to
            // use another method
            final Reservation reservation = Reservation.load(resID);
            for (final Integer i : idList) {
                services.addAll(Service.loadWithUserID(i.intValue(),
                        reservation));
            }
        }

        for (final Service service : services) {
            responseList.add(this.createServiceConstraintType(service, service
                    .getServiceId()));
        }

        return responseList;
    }

    /**
     * get all serviceConstraintTypes for a reservation with known service-IDs.
     * 
     * @param resID
     *            String
     * @param idList
     *            List < Integer >
     * @return List < ServiceConstraintType >
     * @throws DatabaseException
     *             A DatabaseException
     * @throws InvalidReservationIDFaultException
     */
    private List<ServiceConstraintType> getServices(final String resID,
            final List<Integer> idList) throws DatabaseException,
            InvalidReservationIDFaultException {
        return this.getServices(WebserviceUtils.convertReservationID(resID),
                idList);
    }
}
