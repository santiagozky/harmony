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


package server.nsp;

import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Vector;

import org.apache.muse.util.xml.XmlUtils;
import org.w3c.dom.Element;

import server.common.GuiLogger;
import server.common.RemoteLoggableServiceServlet;
import client.classes.CancelResponse;
import client.classes.DomainStatus;
import client.classes.Reservation;
import client.classes.ReservationList;
import client.classes.ServiceStatus;
import client.classes.Status;
import client.classes.nsp.GetEndpointsResponseType;
import client.classes.nsp.GetEndpointsType;
import client.classes.nsp.GetLinksResponseType;
import client.classes.nsp.GetLinksType;
import client.helper.GuiException;
import client.interfaces.NspClientProxy;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelReservation;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelReservationResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DomainConnectionStatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservations;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationsComplexType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationsResponse;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationsResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationsType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatusResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.InvalidReservationIDFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.utils.JaxbSerializer;
import org.opennaas.core.utils.Config;
import org.opennaas.core.utils.Helpers;

public class NspClientProxyImpl extends RemoteLoggableServiceServlet implements
        NspClientProxy {

    /** * */
    private static final long serialVersionUID = 1L;
    /** * */
    private final GuiReservationClient reservationClient;
    /** * */
    private final GuiTopologyClient topologyClient;
    /** * */
    private static final HashMap<String, GuiReservationClient> RESERVATION_PROXIES =
            new HashMap<String, GuiReservationClient>();
    /** * */
    private static final HashMap<String, GuiTopologyClient> TOPOLOGY_PROXIES =
            new HashMap<String, GuiTopologyClient>();

    private final GuiLogger log;

    /**
     * Constructor.
     * 
     * @throws URISyntaxException
     */
    public NspClientProxyImpl() throws URISyntaxException {
        this.reservationClient =
                new GuiReservationClient(Config.getString("management",
                        "epr.reservation"));

        this.topologyClient =
                new GuiTopologyClient(Config.getString("management",
                        "epr.topology"));

        this.log = new GuiLogger(this);
    }

    /**
     * Get Reservation client for epr.
     * 
     * @param epr
     *            Endpoint Reference
     * @return Reservation Client
     * @throws GuiException
     *             in case of errors
     */
    private GuiReservationClient getReservationClient(final String epr)
            throws GuiException {

        if ((null == epr) || ("" == epr)) {
            return this.reservationClient;
        }

        GuiReservationClient result =
                NspClientProxyImpl.RESERVATION_PROXIES.get(epr);

        if (null == result) {
            try {
                result = new GuiReservationClient(epr);
            } catch (final URISyntaxException e) {
                throw new GuiException("Invalid EPR");
            }

            NspClientProxyImpl.RESERVATION_PROXIES.put(epr, result);
        }

        return result;
    }

    /**
     * Get Topology client for epr.
     * 
     * @param epr
     *            Endpoint Reference
     * @return Topology Client
     * @throws GuiException
     *             in case of errors
     */
    private GuiTopologyClient getTopologyClient(final String epr)
            throws GuiException {

        if ((null == epr) || ("" == epr)) {
            return this.topologyClient;
        }

        GuiTopologyClient result = NspClientProxyImpl.TOPOLOGY_PROXIES.get(epr);

        if (null == result) {
            try {
                result = new GuiTopologyClient(epr);
            } catch (final URISyntaxException e) {
                throw new GuiException("Invalid EPR");
            }

            NspClientProxyImpl.TOPOLOGY_PROXIES.put(epr, result);
        }

        return result;
    }

    /**
     * Forward a request to SimpleReservationClient.
     * 
     * @param epr
     *            Endpoint Reference
     * @param target
     *            Target method Name
     * @param request
     *            Request Object (Gui Object)
     * @return response Object (Gui Object)
     * @throws GuiException
     */
    public Object sendReservationRequest(final String epr, final String target,
            final Object request) throws GuiException {
        Object response = null;

        try {
            response =
                    this.getReservationClient(epr).sendRequest(target, request);
        } catch (final InvocationTargetException e) {
            this.log.error("Error sending reservation request: ", e);

            if (e.getCause() != null) {
                throw new GuiException(e.getCause());
            }

            throw new GuiException(e);
        } catch (final Exception e) {
            this.log.error("Error sending reservation request: ", e);

            throw new GuiException(e);
        }

        return response;
    }

    /**
     * Forward a request to SimpleReservationClient.
     * 
     * @param epr
     *            Endpoint Reference
     * @param target
     *            Target method Name
     * @param request
     *            Request Object (Gui Object)
     * @return response Object (Gui Object)
     * @throws GuiException
     */
    public Object sendTopologyRequest(final String epr, final String target,
            final Object request) throws GuiException {
        Object response = null;

        try {
            response = this.getTopologyClient(epr).sendRequest(target, request);
        } catch (final InvocationTargetException e) {
            this.log.error("Error sending topology request: ", e);

            if (e.getCause() != null) {
                throw new GuiException(e.getCause());
            }

            throw new GuiException(e);
        } catch (final Exception e) {
            this.log.error("Error sending topology request: ", e);

            throw new GuiException(e);
        }

        return response;
    }

    public ReservationList getReservations(final String epr, final int duration)
            throws GuiException {
        final GetReservations getReq = new GetReservations();
        final GetReservationsType getType = new GetReservationsType();
        // SET STARTTIME FOR PERIOD
        final GregorianCalendar endTime = new GregorianCalendar();
        endTime.setTime(new Date());
        endTime.add(Calendar.YEAR, 1); // Show advance reservations
        getType.setPeriodEndTime(Helpers.DateToXmlCalendar(endTime.getTime()));
        // SET ENDTIME FOR PERIOD
        final GregorianCalendar startGreg = new GregorianCalendar();
        startGreg.setTime(new Date());
        startGreg.add(Calendar.HOUR, duration * -1);
        getType.setPeriodStartTime(Helpers.DateToXmlCalendar(startGreg
                .getTime()));

        getReq.setGetReservations(getType);

        GetReservationsResponseType responseType = null;
        System.out.println(epr);
        this.log.debug("Getting reservations from " + epr);

        try {
            final Element requestElement =
                    JaxbSerializer.getInstance().objectToElement(getReq);

            this.log.debug(XmlUtils.toString(requestElement));

            final Element responseElement =
                    this.getReservationClient(epr).getReservations(
                            requestElement);

            responseType =
                    ((GetReservationsResponse) JaxbSerializer.getInstance()
                            .elementToObject(responseElement))
                            .getGetReservationsResponse();
        } catch (final Exception e) {
            throw new GuiException(e);
        }

        final ReservationList result = new ReservationList();

        for (final GetReservationsComplexType reservationType : responseType
                .getReservation()) {

            Reservation reservation = null;
            try {
                reservation = NspClassMapper.jaxb2gwt(reservationType);
            } catch (final InvalidReservationIDFaultException e) {
                e.printStackTrace();
            }

            result.getReservations().add(reservation);
        }

        return result;
    }

    public Vector<CancelResponse> cancelReservation(final String epr,
            final Vector<String> ids) {
        final Vector<CancelResponse> result = new Vector<CancelResponse>();

        for (int i = 0; i < ids.size(); i++) {

            final String id = (String) ids.get(i);

            final CancelReservationType requestType =
                    new CancelReservationType();
            requestType.setReservationID(id);

            final CancelReservation request = new CancelReservation();
            request.setCancelReservation(requestType);

            final CancelResponse response = new CancelResponse();
            response.setId(id);

            try {
                TokenCache.getCache().addTokenToObj(requestType);

                final CancelReservationResponseType responseType =
                        this.getReservationClient(epr).cancelReservation(
                                requestType);

                response.setStatus(responseType.isSuccess());
            } catch (final Exception e) {
                this.log.error("Error while cancelling reservation " + id
                        + ": " + e.getMessage(), e);

                response.setStatus(false);
            }

            result.add(response);
        }

        return result;
    }

    /**
     * Get Domains from Nsp.
     */
    public client.classes.nsp.GetDomainsResponseType getDomains()
            throws GuiException {

        final client.classes.nsp.GetDomainsType request =
                new client.classes.nsp.GetDomainsType();

        return (client.classes.nsp.GetDomainsResponseType) this
                .sendTopologyRequest(null, "getDomains", request);
    }

    /**
     * Get Domains from Nsp.
     */
    public client.classes.nsp.GetDomainsResponseType getDomains(final String epr)
            throws GuiException {

        final client.classes.nsp.GetDomainsType request =
                new client.classes.nsp.GetDomainsType();

        return (client.classes.nsp.GetDomainsResponseType) this
                .sendTopologyRequest(epr, "getDomains", request);
    }

    public Status getStatus(final String epr, final String id)
            throws GuiException {
        final GetStatusType statusType = new GetStatusType();

        statusType.setReservationID(id);

        GetStatusResponseType responseType = null;

        try {
            responseType = this.getReservationClient(epr).getStatus(statusType);
        } catch (final Exception e) {
            throw new GuiException(e);
        }

        final Status response = new Status();

        for (final GetStatusResponseType.ServiceStatus nspServiceStatus : responseType
                .getServiceStatus()) {
            final ServiceStatus guiServiceStatus = new ServiceStatus();
            guiServiceStatus.setServiceId(nspServiceStatus.getServiceID());

            for (final DomainConnectionStatusType nspDomainStatus : nspServiceStatus
                    .getConnections().get(0).getDomainStatus()) {
                final DomainStatus guiDomainStatus = new DomainStatus();

                guiDomainStatus.setDomain(nspDomainStatus.getDomain());
                guiDomainStatus.setStatus(nspDomainStatus.getStatus()
                        .getStatus().name());
                guiDomainStatus.setSource(nspDomainStatus.getStatus()
                        .getSource().getEndpointId());
                guiDomainStatus.setDestination(nspDomainStatus.getStatus()
                        .getTarget().get(0).getEndpointId());
                guiDomainStatus.setActualBW(nspDomainStatus.getStatus()
                        .getActualBW());

                guiServiceStatus.getDomainStates().add(guiDomainStatus);
            }

            response.getServiceStates().add(guiServiceStatus);
        }

        return response;
    }

    public GetLinksResponseType getLinks(final String epr,
            final GetLinksType request) throws GuiException {
        return (GetLinksResponseType) this.sendTopologyRequest(epr, "getLinks",
                request);
    }

    public GetEndpointsResponseType getEndpoints(final String epr,
            final GetEndpointsType request) throws GuiException {
        return (GetEndpointsResponseType) this.sendTopologyRequest(epr,
                "getEndpoints", request);
    }

    public client.classes.nsp.GetReservationsResponseType getReservations(
            final String epr,
            final client.classes.nsp.GetReservationsType request)
            throws GuiException {
        return (client.classes.nsp.GetReservationsResponseType) this
                .sendReservationRequest(epr, "getReservations", request);
    }

    public client.classes.nsp.GetStatusResponseType getStatus(final String epr,
            final client.classes.nsp.GetStatusType request) throws GuiException {
        return (client.classes.nsp.GetStatusResponseType) this
                .sendReservationRequest(epr, "getStatus", request);
    }

    public client.classes.nsp.CreateReservationResponseType createReservation(
            final String epr,
            final client.classes.nsp.CreateReservationType request)
            throws GuiException {
        return (client.classes.nsp.CreateReservationResponseType) this
                .sendReservationRequest(epr, "createReservation", request);
    }

    public client.classes.nsp.CancelReservationResponseType cancelReservation(
            final String epr,
            final client.classes.nsp.CancelReservationType request)
            throws GuiException {
        return (client.classes.nsp.CancelReservationResponseType) this
                .sendReservationRequest(epr, "cancelReservation", request);
    }

}
