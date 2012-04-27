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

package org.opennaas.extensions.idb.serviceinterface.reservation;

import java.net.URISyntaxException;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.muse.ws.addressing.EndpointReference;
import org.apache.muse.ws.addressing.soap.SoapFault;
import org.w3c.dom.Element;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.Activate;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ActivateResponse;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ActivateResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ActivateType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.Bind;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.BindResponse;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.BindResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.BindType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelReservation;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelReservationResponse;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelReservationResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ConnectionConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CreateReservation;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CreateReservationResponse;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CreateReservationResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CreateReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.FixedReservationConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservations;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationsResponse;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationsResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationsType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatus;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatusResponse;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatusResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.IsAvailable;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.IsAvailableResponse;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.IsAvailableResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.IsAvailableType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.MalleableReservationConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.Notification;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.NotificationResponse;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.NotificationResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.NotificationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ServiceConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.utils.JaxbSerializer;
import org.opennaas.core.utils.Helpers;
import org.opennaas.core.utils.Tuple;

public class SimpleReservationClient extends CommonReservationClient {

    public static final CreateReservationType getCreateReservationRequest(
            final int bandwidth, final int delay, final int duration,
            final XMLGregorianCalendar startTime,
            final Tuple<String, String>... ep) {
        final CreateReservationType resReq = new CreateReservationType();

        final ServiceConstraintType service = new ServiceConstraintType();
        final FixedReservationConstraintType constraints = new FixedReservationConstraintType();

        constraints.setDuration(duration);
        constraints.setStartTime(startTime);

        service.setTypeOfReservation(ReservationType.FIXED);
        service.setFixedReservationConstraints(constraints);
        service.setAutomaticActivation(true);
        service.setServiceID(1); // Fix for ARGIA system

        for (int i = 0; i < ep.length; i++) {
            final ConnectionConstraintType connection = new ConnectionConstraintType();

            final EndpointType src = new EndpointType();
            src.setEndpointId(ep[i].getFirstElement());
            final EndpointType dst = new EndpointType();
            dst.setEndpointId(ep[i].getSecondElement());

            connection.setConnectionID(i + 1);
            connection.setMinBW(bandwidth);
            connection.setMaxDelay(Integer.valueOf(delay));
            connection.setSource(src);
            connection.getTarget().add(dst);
            connection.setDirectionality(1);

            service.getConnections().add(connection);

        }

        resReq.getService().add(service);

        return resReq;
    }

    /**
     * @param source
     * @param target
     * @param bandwidth
     * @param delay
     * @param duration
     * @return
     * @throws DatatypeConfigurationException
     */
    public static IsAvailableType getIsAvailableRequest(final String source,
            final String target, final int bandwidth, final int delay,
            final int duration) throws DatatypeConfigurationException {
        final ServiceConstraintType service = new ServiceConstraintType();
        final FixedReservationConstraintType constraints = new FixedReservationConstraintType();
        final ConnectionConstraintType connection = new ConnectionConstraintType();

        constraints.setDuration(duration);
        constraints.setStartTime(DatatypeFactory.newInstance()
                .newXMLGregorianCalendar(new GregorianCalendar()));

        service.setTypeOfReservation(ReservationType.FIXED);
        service.setFixedReservationConstraints(constraints);
        service.setAutomaticActivation(true);
        service.setServiceID(Helpers.getPositiveRandomInt());

        final EndpointType src = new EndpointType();
        src.setEndpointId(target);
        final EndpointType dst = new EndpointType();
        dst.setEndpointId(source);

        connection.setMinBW(bandwidth);
        connection.setMaxDelay(Integer.valueOf(delay));
        connection.setSource(src);
        connection.getTarget().add(dst);
        connection.setDirectionality(1);
        connection.setConnectionID(Helpers.getPositiveRandomInt());

        service.getConnections().add(connection);

        final IsAvailableType request = new IsAvailableType();
        request.getService().add(service);
        return request;
    }

    /**
     * Constructor from superclass.
     * 
     * @param endpointReference
     */
    public SimpleReservationClient(final EndpointReference endpointReference) {
        super(endpointReference);
    }

    /**
     * Constructor from Superclass.
     * 
     * @param endpointReference
     * @throws URISyntaxException
     * @throws URISyntaxException
     */
    public SimpleReservationClient(final IReservationWS webservice) {
        super(webservice);
    }

    /**
     * Constructor from Superclass.
     * 
     * @param endpointReference
     * @throws URISyntaxException
     */
    public SimpleReservationClient(final String endpointReference) {
        super(endpointReference);
    }

    /**
     * @param request
     * @return
     * @throws SoapFault
     */
    public ActivateResponseType activate(final ActivateType request)
            throws SoapFault {
        final Activate envelope = new Activate();

        envelope.setActivate(request);

        final Element reqElement = JaxbSerializer.getInstance()
                .objectToElement(envelope);

        final Element resElement = super.activate(reqElement);

        final ActivateResponse response = (ActivateResponse) JaxbSerializer
                .getInstance().elementToObject(resElement);

        return response.getActivateResponse();
    }

    /**
     * 
     * @param reservationID
     * @param serviceID
     * @return
     * @throws SoapFault
     */
    public ActivateResponseType activate(final String reservationID,
            final int serviceID) throws SoapFault {
        final ActivateType request = new ActivateType();
        request.setReservationID(reservationID);
        request.setServiceID(serviceID);
        return this.activate(request);
    }

    /**
     * @param request
     * @return
     * @throws SoapFault
     */
    public BindResponseType bind(final BindType request) throws SoapFault {
        final Bind envelope = new Bind();

        envelope.setBind(request);

        final Element reqElement = JaxbSerializer.getInstance()
                .objectToElement(envelope);

        final Element resElement = super.bind(reqElement);

        final BindResponse response = (BindResponse) JaxbSerializer
                .getInstance().elementToObject(resElement);

        return response.getBindResponse();
    }

    /**
     * 
     * @param reservationID
     * @param TNA
     * @param IP
     * @return
     * @throws SoapFault
     */
    public BindResponseType bind(final String reservationID, final String TNA,
            final String IP) throws SoapFault {
        final BindType request = new BindType();
        request.setReservationID(reservationID);
        request.setEndpointID(TNA);
        request.getIPAdress().add(IP);

        final Bind envelope = new Bind();

        envelope.setBind(request);

        final Element reqElement = JaxbSerializer.getInstance()
                .objectToElement(envelope);

        final Element resElement = super.bind(reqElement);

        final BindResponse response = (BindResponse) JaxbSerializer
                .getInstance().elementToObject(resElement);

        return response.getBindResponse();
    }

    /**
     * @param request
     * @return
     * @throws SoapFault
     */
    public CancelReservationResponseType cancelReservation(
            final CancelReservationType request) throws SoapFault {
        final CancelReservation envelope = new CancelReservation();

        envelope.setCancelReservation(request);

        final Element reqElement = JaxbSerializer.getInstance()
                .objectToElement(envelope);

        final Element resElement = super.cancelReservation(reqElement);

        final CancelReservationResponse response = (CancelReservationResponse) JaxbSerializer
                .getInstance().elementToObject(resElement);

        return response.getCancelReservationResponse();
    }

    public CancelReservationResponseType cancelReservation(
            final String reservationID) throws SoapFault {
        final CancelReservationType request = new CancelReservationType();
        request.setReservationID(reservationID);
        return this.cancelReservation(request);
    }

    /**
     * @param request
     * @return
     * @throws SoapFault
     */
    public CreateReservationResponseType createMalleableReservation(
            final CreateReservationType request) throws SoapFault {
        final CreateReservation envelope = new CreateReservation();

        envelope.setCreateReservation(request);

        final Element reqElement = JaxbSerializer.getInstance()
                .objectToElement(envelope);

        final Element resElement = super.createReservation(reqElement);

        final CreateReservationResponse response = (CreateReservationResponse) JaxbSerializer
                .getInstance().elementToObject(resElement);

        return response.getCreateReservationResponse();
    }

    /**
     * @return
     * @throws DatatypeConfigurationException
     * @throws SoapFault
     * @throws DatatypeConfigurationException
     */
    public CreateReservationResponseType createMalleableReservation(
            final String source, final String target, final long dataAmount,
            final XMLGregorianCalendar startTime,
            final XMLGregorianCalendar deadline, final int minBW,
            final int maxBW, final int delay) throws SoapFault {
        final CreateReservationType resReq = new CreateReservationType();

        final ServiceConstraintType service = new ServiceConstraintType();
        final MalleableReservationConstraintType constraints = new MalleableReservationConstraintType();
        final ConnectionConstraintType connection = new ConnectionConstraintType();

        constraints.setStartTime(startTime);
        constraints.setDeadline(deadline);

        service.setTypeOfReservation(ReservationType.MALLEABLE);
        service.setMalleableReservationConstraints(constraints);
        service.setAutomaticActivation(true);
        service.setServiceID(1); // Fix for ARGIA system

        final EndpointType src = new EndpointType();
        src.setEndpointId(source);
        final EndpointType dst = new EndpointType();
        dst.setEndpointId(target);

        connection.setDataAmount(dataAmount);
        connection.setMinBW(minBW);
        connection.setMaxBW(maxBW);
        connection.setMaxDelay(delay);
        connection.setSource(src);
        connection.getTarget().add(dst);
        connection.setConnectionID(1); // Fix for ARGIA system

        service.getConnections().add(connection);

        resReq.setJobID(Helpers.getPositiveRandomLong());
        resReq.getService().add(service);

        return this.createReservation(resReq);
    }

    /**
     * @param request
     * @return
     * @throws SoapFault
     */
    public CreateReservationResponseType createReservation(
            final CreateReservationType request) throws SoapFault {
        final CreateReservation envelope = new CreateReservation();

        envelope.setCreateReservation(request);

        final Element reqElement = JaxbSerializer.getInstance()
                .objectToElement(envelope);

        final Element resElement = super.createReservation(reqElement);

        final CreateReservationResponse response = (CreateReservationResponse) JaxbSerializer
                .getInstance().elementToObject(resElement);

        return response.getCreateReservationResponse();
    }

    public CreateReservationResponseType createReservation(final int bandwidth,
            final int delay, final int duration,
            final XMLGregorianCalendar startTime,
            final Tuple<String, String>... ep) throws SoapFault {

        final CreateReservationType resReq = SimpleReservationClient
                .getCreateReservationRequest(bandwidth, delay, duration,
                        startTime, ep);

        return this.createReservation(resReq);
    }

    public CreateReservationResponseType createReservation(final int bandwidth,
            final int delay, final int duration,
            final XMLGregorianCalendar startTime,
            final Tuple<String, String> tuple, final String samlAssertion)
            throws SoapFault {

        final CreateReservationType resReq = this.getCreateReservationRequest(
                bandwidth, delay, duration, startTime, tuple, samlAssertion);

        return this.createReservation(resReq);
    }

    /**
     * @return
     * @throws DatatypeConfigurationException
     * @throws SoapFault
     * @throws DatatypeConfigurationException
     */
    @SuppressWarnings("unchecked")
    public CreateReservationResponseType createReservation(final String source,
            final String target, final int bandwidth, final int delay,
            final int duration) throws SoapFault {
        XMLGregorianCalendar startTime;
        try {
            startTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(
                    new GregorianCalendar());
        } catch (final DatatypeConfigurationException e) {
            throw new RuntimeException(
                    "A serious configuration error was detected ...", e);
        }

        return this.createReservation(bandwidth, delay, duration, startTime,
                new Tuple<String, String>(source, target));
    }

    public CreateReservationResponseType createReservation(final String source,
            final String target, final int bandwidth, final int delay,
            final int duration, final String samlAssertion) throws SoapFault {
        XMLGregorianCalendar startTime;
        try {
            startTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(
                    new GregorianCalendar());
        } catch (final DatatypeConfigurationException e) {
            throw new RuntimeException(
                    "A serious configuration error was detected ...", e);
        }

        return this.createReservation(bandwidth, delay, duration, startTime,
                new Tuple<String, String>(source, target), samlAssertion);
    }

    /**
     * A simple API to create a reservation in advance.
     * 
     * @param source
     *            The source TNA.
     * @param target
     *            The target TNA.
     * @param startTime
     *            The start time.
     * @param bandwidth
     *            The requested path bandwidth.
     * @param delay
     *            The requested path delay.
     * @param duration
     *            The duration of the reservation.
     * @return The according reservation IDs.
     * @throws SoapFault
     *             If an error occurs.
     */
    @SuppressWarnings("unchecked")
    public CreateReservationResponseType createReservation(final String source,
            final String target, final XMLGregorianCalendar startTime,
            final int bandwidth, final int delay, final int duration)
            throws SoapFault {
        return this.createReservation(bandwidth, delay, duration, startTime,
                new Tuple<String, String>(source, target));
    }

    @SuppressWarnings("unchecked")
    private CreateReservationType getCreateReservationRequest(
            final int bandwidth, final int delay, final int duration,
            final XMLGregorianCalendar startTime,
            final Tuple<String, String> tuple, final String samlAssertion) {
        final CreateReservationType result = SimpleReservationClient
                .getCreateReservationRequest(bandwidth, delay, duration,
                        startTime, tuple);
        result.setSamlAssertion(samlAssertion);
        return result;
    }

    /**
     * @param request
     * @return
     * @throws SoapFault
     */
    public GetReservationsResponseType getReservations(
            final GetReservationsType request) throws SoapFault {
        final GetReservations envelope = new GetReservations();

        envelope.setGetReservations(request);

        final Element reqElement = JaxbSerializer.getInstance()
                .objectToElement(envelope);

        final Element resElement = super.getReservations(reqElement);

        final GetReservationsResponse response = (GetReservationsResponse) JaxbSerializer
                .getInstance().elementToObject(resElement);

        return response.getGetReservationsResponse();
    }

    /**
     * 
     * @param timeframeInSeconds
     *            Time in seconds to ask for around the current timestamp
     * @return
     * @throws SoapFault
     * @throws DatatypeConfigurationException
     */
    public GetReservationsResponseType getReservations(
            final long timeframeInSeconds) throws SoapFault,
            DatatypeConfigurationException {
        final XMLGregorianCalendar startTime = DatatypeFactory.newInstance()
                .newXMLGregorianCalendar(new GregorianCalendar());
        final XMLGregorianCalendar endTime = DatatypeFactory.newInstance()
                .newXMLGregorianCalendar(new GregorianCalendar());
        final Duration duration = DatatypeFactory.newInstance().newDuration(
                timeframeInSeconds * 1000);

        startTime.add(duration.negate());
        endTime.add(duration);

        final GetReservationsType request = new GetReservationsType();
        request.setPeriodStartTime(startTime);
        request.setPeriodEndTime(endTime);

        return this.getReservations(request);
    }

    /**
     * @param request
     * @return
     * @throws SoapFault
     */
    public GetStatusResponseType getStatus(final GetStatusType request)
            throws SoapFault {
        final GetStatus envelope = new GetStatus();

        envelope.setGetStatus(request);

        final Element reqElement = JaxbSerializer.getInstance()
                .objectToElement(envelope);

        final Element resElement = super.getStatus(reqElement);

        final GetStatusResponse response = (GetStatusResponse) JaxbSerializer
                .getInstance().elementToObject(resElement);

        return response.getGetStatusResponse();
    }

    public GetStatusResponseType getStatus(final String reservationID)
            throws SoapFault {
        final GetStatusType request = new GetStatusType();
        request.setReservationID(reservationID);
        return this.getStatus(request);
    }

    /**
     * @param request
     * @return
     * @throws SoapFault
     */
    public IsAvailableResponseType isAvailable(final IsAvailableType request)
            throws SoapFault {
        final IsAvailable envelope = new IsAvailable();

        envelope.setIsAvailable(request);

        final Element reqElement = JaxbSerializer.getInstance()
                .objectToElement(envelope);

        final Element resElement = super.isAvailable(reqElement);

        final IsAvailableResponse response = (IsAvailableResponse) JaxbSerializer
                .getInstance().elementToObject(resElement);

        return response.getIsAvailableResponse();
    }

    /**
     * 
     * @param source
     * @param target
     * @return
     * @throws SoapFault
     * @throws DatatypeConfigurationException
     */
    public IsAvailableResponseType isAvailable(final String source,
            final String target, final int bandwidth, final int delay,
            final int duration) throws SoapFault,
            DatatypeConfigurationException {

        final IsAvailableType request = SimpleReservationClient
                .getIsAvailableRequest(source, target, bandwidth, delay,
                        duration);

        return this.isAvailable(request);
    }

    /**
     * @param request
     * @return
     * @throws SoapFault
     */
    public NotificationResponseType notification(final NotificationType request)
            throws SoapFault {
        final Notification envelope = new Notification();

        envelope.setNotification(request);

        final Element reqElement = JaxbSerializer.getInstance()
                .objectToElement(envelope);

        final Element resElement = super.notification(reqElement);

        final NotificationResponse response = (NotificationResponse) JaxbSerializer
                .getInstance().elementToObject(resElement);

        return response.getNotificationResponse();
    }
}
