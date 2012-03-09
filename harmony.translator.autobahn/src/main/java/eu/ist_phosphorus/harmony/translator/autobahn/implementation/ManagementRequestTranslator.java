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

package eu.ist_phosphorus.harmony.translator.autobahn.implementation;

import java.util.Date;
import java.util.Locale;

import net.es.oscars.wsdlTypes.GlobalReservationId;
import net.es.oscars.wsdlTypes.Layer2Info;
import net.es.oscars.wsdlTypes.Layer3Info;
import net.es.oscars.wsdlTypes.ListReply;
import net.es.oscars.wsdlTypes.ListRequest;
import net.es.oscars.wsdlTypes.ListRequestSequence_type0;
import net.es.oscars.wsdlTypes.ListReservations;
import net.es.oscars.wsdlTypes.PathInfo;
import net.es.oscars.wsdlTypes.QueryReservation;
import net.es.oscars.wsdlTypes.QueryReservationResponse;
import net.es.oscars.wsdlTypes.ResDetails;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.AvailabilityCodeType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.ConnectionAvailabilityType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.ConnectionConstraintType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.ConnectionStatusType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.DomainStatusType;
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
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;
import eu.ist_phosphorus.harmony.common.utils.Config;
import eu.ist_phosphorus.harmony.common.utils.Helpers;

public final class ManagementRequestTranslator extends HarmonyAutobahnTranslator {
    /**
     * HSI GetReservationsResponseType -> AutoBAHN ListReservationsResponse
     * 
     * @param arg
     * @return A list of reservations.
     */
    public ListReply convert(final GetReservationsResponseType arg) {

        final ListReply reply = new ListReply();

        final int elements = arg.getReservation().size();
        reply.setTotalResults(elements);
        final ResDetails[] details = new ResDetails[elements];
        final ResDetails detail = new ResDetails();

        for (final GetReservationsComplexType res : arg.getReservation()) {

            final GetReservationResponseType type = res.getReservation();

            if (type.getService().size() <= 1) {
                for (final ServiceConstraintType service : type.getService()) {
                    service.getFixedReservationConstraints();

                    // TODO...
                }
            } else {
                this.logger.info("Skipping " + res.getReservationID());

                continue;
            }

            details[details.length] = detail;
        }

        reply.setResDetails(details);
        // hsi.setListReservationsResponse(reply);

        throw new RuntimeException("Not yet implemented");
    }

    /**
     * HSI GetReservationsType -> AutoBAHN ListReservations
     * 
     * @param arg
     * @return A list of reservations.
     */
    public ListRequest convert(final GetReservationsType arg) {
        final ListRequest req = new ListRequest();

        final Date start = Helpers.xmlCalendarToDate(arg.getPeriodStartTime());
        final Date end = Helpers.xmlCalendarToDate(arg.getPeriodEndTime());

        final ListRequestSequence_type0 sequence =
                new ListRequestSequence_type0();

        this.logger.info("Looking up " + start + " -> " + end);

        sequence.setStartTime((long) Math.floor(start.getTime() / 1000.0));
        sequence.setEndTime((long) Math.ceil(end.getTime() / 1000.0));

        final String[] resStatuses = {};

        req.setResStatus(resStatuses);
        req.setDescription("");
        req.setResRequested(1000);

        req.setListRequestSequence_type0(sequence);

        return req;
    }

    /**
     * HSI GetStatusResponseType -> AutoBAHN queryReservationResponse
     * 
     * @param arg
     * @return The reservation query response.
     */
    public QueryReservationResponse convert(final GetStatusResponseType arg) {
        throw new RuntimeException("Not yet implemented");
    }

    /**
     * HSI GetStatusType -> AutoBAHN queryReservation
     * 
     * @param arg
     * @return The query reservation response.
     */
    public GlobalReservationId convert(final GetStatusType arg) {
        final QueryReservation queryReservation = new QueryReservation();

        final GlobalReservationId id = new GlobalReservationId();
        id.setGri(this.reservationIdMapper.harmonyToAutobahn(arg.getReservationID(),
                false));

        queryReservation.setQueryReservation(id);

        return id;
    }

    /**
     * AutoBAHN ListReservations -> HSI GetReservationsType
     * 
     * @param arg
     * @return The reservations.
     */
    public GetReservationsType convert(final ListReservations arg) {
        final GetReservationsType hsi = new GetReservationsType();

        final ListRequestSequence_type0 sequence =
                arg.getListReservations().getListRequestSequence_type0();

        final Date start = new Date(sequence.getStartTime());
        final Date end = new Date(sequence.getEndTime());

        hsi.setPeriodStartTime(Helpers.DateToXmlCalendar(start));
        hsi.setPeriodEndTime(Helpers.DateToXmlCalendar(end));

        return hsi;
    }

    /**
     * AutoBAHN ListReservationsResponse -> HSI GetReservationsResponseType
     * 
     * @param arg
     * @return The reservations.
     */
    public GetReservationsResponseType convert(final ListReply arg) {
        final GetReservationsResponseType hsi =
                new GetReservationsResponseType();

        final ListReply reply = arg;

        if (reply.getResDetails() == null) {
            this.logger.info("No reservatios found :-(");
        } else {
            for (final ResDetails detail : reply.getResDetails()) {
                // TODO Move these out of the loop
                final ServiceConstraintType serviceConstraintType =
                        new ServiceConstraintType();
                final GetReservationResponseType getReservationResponseType =
                        new GetReservationResponseType();
                final FixedReservationConstraintType fixedReservationConstraints =
                        new FixedReservationConstraintType();
                final ConnectionConstraintType connectionConstraintType =
                        new ConnectionConstraintType();
                final GetReservationsComplexType reservationComplexType =
                        new GetReservationsComplexType();
                final Date date = new Date();
                final EndpointType destEp = new EndpointType();
                final EndpointType sourceEp = new EndpointType();

                reservationComplexType
                        .setReservationID(this.reservationIdMapper
                                .autobahnToHarmony(detail.getGlobalReservationId()));
                this.logger.info("Found reservation ID: "
                        + reservationComplexType.getReservationID() + " (HSI) "
                        + detail.getGlobalReservationId() + " (AutoBAHN)");

                serviceConstraintType
                        .setTypeOfReservation(ReservationType.FIXED);

                fixedReservationConstraints.setDuration((int) ((detail
                        .getEndTime() - detail.getStartTime())));

                date.setTime(detail.getStartTime() * 1000);
                fixedReservationConstraints.setStartTime(Helpers
                        .DateToXmlCalendar(date));

                serviceConstraintType
                        .setFixedReservationConstraints(fixedReservationConstraints);

                getReservationResponseType.getService().add(
                        serviceConstraintType);

                final String autobahnSrc =
                        detail.getPathInfo().getLayer2Info().getSrcEndpoint();

                try {
                    sourceEp.setEndpointId(this.endpointMapper.autobahnToHarmony(
                            autobahnSrc, true));
                } catch (final RuntimeException e) {
                    this.logger.info("Skipping " + autobahnSrc + "("
                            + e.getMessage() + ")");
                    continue;
                }

                final String autobahnDst =
                        detail.getPathInfo().getLayer2Info().getDestEndpoint();

                try {
                    destEp.setEndpointId(this.endpointMapper.autobahnToHarmony(
                            autobahnDst, true));
                } catch (final RuntimeException e) {
                    this.logger.info("Skipping " + autobahnDst + "("
                            + e.getMessage() + ")");
                    continue;
                }

                connectionConstraintType.setSource(sourceEp);
                connectionConstraintType.getTarget().add(destEp);

                serviceConstraintType.getConnections().add(
                        connectionConstraintType);
                reservationComplexType
                        .setReservation(getReservationResponseType);

                hsi.getReservation().add(reservationComplexType);
            }
        }
        return hsi;
    }

    /**
     * AutoBAHN queryReservation -> HSI GetStatusType
     * 
     * @param arg
     * @return The status type.
     */
    public GetStatusType convert(final QueryReservation arg) {
        throw new RuntimeException("Not yet implemented");
    }

    /**
     * AutoBAHN queryReservationResponse -> HSI GetStatusResponseType
     * 
     * @param arg
     * @return The status type.
     * @throws UnexpectedFaultException
     */
    public GetStatusResponseType convert(final ResDetails arg)
            throws UnexpectedFaultException {
        final GetStatusResponseType responseType = new GetStatusResponseType();

        final ResDetails detail = arg;

        final ServiceStatus serviceStatus = new ServiceStatus();

        this.logger.info("Got status: " + detail.getStatus());
        String status;
        if (AutoBAHNClient.STATUS_CANCELLED.equals(detail.getStatus())) {
            status = StatusType.CANCELLED_BY_USER.toString();
        } else if (AutoBAHNClient.STATUS_TEARDOWN.equals(detail.getStatus())) {
            status = StatusType.TEARDOWN_IN_PROGRESS.toString();
        } else if (AutoBAHNClient.STATUS_FAILED.equals(detail.getStatus())) {
            status = StatusType.CANCELLED_BY_SYSTEM.toString();
        } else if (AutoBAHNClient.STATUS_FINISHED.equals(detail.getStatus())) {
            status = StatusType.COMPLETED.toString();
        } else if (AutoBAHNClient.STATUS_INCREATE.equals(detail.getStatus())
                || AutoBAHNClient.STATUS_INSETUP.equals(detail.getStatus())
                || AutoBAHNClient.STATUS_ACCEPTED.equals(detail.getStatus())) {
            status = StatusType.SETUP_IN_PROGRESS.toString();
        } else {
            status = detail.getStatus();
        }
        status = status.toLowerCase(Locale.getDefault());

        final DomainStatusType domainStatus = new DomainStatusType();
        domainStatus.setDomain(Config.getString("hsiAutobahn", "domain.name"));

        try {
            this.logger.info("Set status to: " + status);
            domainStatus.setStatus(StatusType.fromValue(status));
            serviceStatus.setStatus(StatusType.fromValue(status));
        } catch (final IllegalArgumentException e) {
            throw new UnexpectedFaultException("The Status '" + status
                    + "' returned from AutoBAHN is not Supported by Harmony", e);
        }

        final PathInfo pathInfo = detail.getPathInfo();

        final EndpointType src = new EndpointType();
        final EndpointType dst = new EndpointType();

        if (null != pathInfo.getLayer3Info()) {
            final Layer3Info info = pathInfo.getLayer3Info();

            src.setEndpointId(this.endpointMapper.autobahnToHarmony(info
                    .getSrcHost()));
            dst.setEndpointId(this.endpointMapper.autobahnToHarmony(info
                    .getDestHost()));
        } else if (null != pathInfo.getLayer2Info()) {
            final Layer2Info info = pathInfo.getLayer2Info();

            // src.setEndpointId(this.endpointMapper.autobahnToHarmony(EndpointMapper
            // .getFullSrcAddress(info)));
            // dst.setEndpointId(this.endpointMapper.autobahnToHarmony(EndpointMapper
            // .getFullDstAddress(info)));
            src.setEndpointId(this.endpointMapper.autobahnToHarmony(info
                    .getSrcEndpoint()));
            dst.setEndpointId(this.endpointMapper.autobahnToHarmony(info
                    .getDestEndpoint()));
        } else {
            src.setEndpointId("127.0.0.1");
            dst.setEndpointId("127.0.0.2");
        }

        this.logger.info("Src set to: " + src.getEndpointId());
        this.logger.info("Dest set to: " + dst.getEndpointId());

        final ConnectionStatusType connectionStatus =
                new ConnectionStatusType();
        connectionStatus.setStatus(StatusType.fromValue(status));
        connectionStatus.setActualBW(detail.getBandwidth());
        connectionStatus.setSource(src);
        connectionStatus.getTarget().add(dst);
        connectionStatus.setDirectionality(1);
        connectionStatus.setConnectionID(1);

        serviceStatus.setServiceID(1);
        serviceStatus.setStatus(StatusType.fromValue(status));
        serviceStatus.getDomainStatus().add(domainStatus);
        serviceStatus.getConnections().add(connectionStatus);

        responseType.getServiceStatus().add(serviceStatus);

        return responseType;
    }

    public ListRequest convert(final IsAvailableType request) {
        final ListRequest listReq = new ListRequest();
        final String[] status =
                {AutoBAHNClient.STATUS_ACTIVE, AutoBAHNClient.STATUS_INCREATE,
                        AutoBAHNClient.STATUS_INSETUP, AutoBAHNClient.STATUS_TEARDOWN};
        listReq.setResStatus(status);
        return listReq;
    }

    /**
     * Simulate an isAvailable response from an AutoBAHN.
     * 
     * @param listReservations
     *            The active AutoBAHN reservations.
     * @param request
     *            The Harmony is available request.
     * @return A valid is available response.
     */
    public IsAvailableResponseType simulateIsAvailable(
            final ListReply listReservations, final IsAvailableType request) {

        final IsAvailableResponseType response = new IsAvailableResponseType();
        final ConnectionAvailabilityType availability =
                new ConnectionAvailabilityType();
        availability.setAvailability(AvailabilityCodeType.AVAILABLE);
        availability.setServiceID(0);
        availability.setConnectionID(0);
        availability.setMaxBW(Config.getInt("translatorAutobahn", "autobahn.maxBW"));

        /* ------------------------------------------------------------------ */
        for (final ServiceConstraintType service : request.getService()) {
            availability.setServiceID(service.getServiceID());
            for (final ConnectionConstraintType connection : service
                    .getConnections()) {
                availability.setConnectionID(connection.getConnectionID());
                if (this.containsEndpoint(listReservations, connection
                        .getSource().getEndpointId())) {
                    availability
                            .setAvailability(AvailabilityCodeType.
                                    ENDPOINT_NOT_AVAILABLE);
                    availability.getBlockedEndpoints().add(
                            connection.getSource().getEndpointId());
                }
                for (final EndpointType target : connection.getTarget()) {
                    if (this.containsEndpoint(listReservations, target
                            .getEndpointId())) {
                        availability
                                .setAvailability(AvailabilityCodeType.
                                        ENDPOINT_NOT_AVAILABLE);
                        availability.getBlockedEndpoints().add(
                                target.getEndpointId());
                    }
                }
            }
        }
        /* ------------------------------------------------------------------ */

        response.getDetailedResult().add(availability);

        return response;
    }

    /**
     * @param listReservations
     * @param endpointId
     * @return
     */
    private boolean containsEndpoint(final ListReply listReservations,
            final String endpointId) {
        if (listReservations.getTotalResults() == 0) {
            return false;
        }
        for (final ResDetails reservation : listReservations.getResDetails()) {
            final Layer2Info info = reservation.getPathInfo().getLayer2Info();
            if (null != info) {
                final String target =
                        this.endpointMapper
                                .autobahnToHarmony(info.getDestEndpoint());
                final String source =
                        this.endpointMapper.autobahnToHarmony(info.getSrcEndpoint());
                if (endpointId.equals(target) || endpointId.equals(source)) {
                    return true;
                }
            }
        }
        return false;
    }

}
