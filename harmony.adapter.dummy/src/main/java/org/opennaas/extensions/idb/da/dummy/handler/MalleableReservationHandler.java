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

import java.util.Hashtable;
import java.util.Map;

import org.apache.log4j.Logger;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ActivateResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ActivateType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AvailabilityCodeType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelReservationResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ConnectionAvailabilityType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ConnectionConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CreateReservationResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CreateReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationsResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationsType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatusResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.IsAvailableResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.IsAvailableType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ServiceConstraintType;
import org.opennaas.extensions.idb.serviceinterface.reservation.CommonReservationHandler;
import org.opennaas.core.utils.Helpers;
import org.opennaas.core.utils.PhLogger;

/**
 * Class to handle NSP reservation-requests in a predictable manner for
 * malleable reservations.
 */
public final class MalleableReservationHandler extends
        CommonReservationHandler {

    /**
     * Todo.
     * 
     * @author willner
     */
    public static final class HelperStatus {
        /** * */
        private AvailabilityCodeType availability;
        /** * */
        private long startTimeOffset;

        /**
         * Todo.
         * 
         * @param a
         */
        public HelperStatus(final AvailabilityCodeType a) {
            this.availability = a;
        }

        /**
         * Todo.
         * 
         * @param o
         */
        public HelperStatus(final long o) {
            this.startTimeOffset = o;
        }

        /**
         * Todo.
         * 
         * @param a
         * @param o
         */
        public HelperStatus(final AvailabilityCodeType a, final long o) {
            this.availability = a;
            this.startTimeOffset = o;
        }

        /**
         * Todo.
         * 
         * @param a
         */
        public void setAvailability(final AvailabilityCodeType a) {
            this.availability = a;
        }

        /**
         * Todo.
         * 
         * @return The availability code.
         */
        public AvailabilityCodeType getAvailability() {
            return this.availability;
        }

        /**
         * Todo.
         * 
         * @param o
         */
        public void setStartTimeOffset(final long o) {
            this.startTimeOffset = o;
        }

        /**
         * Todo.
         * 
         * @return The start offset.
         */
        public long getStartTimeOffset() {
            return this.startTimeOffset;
        }
    }

    /*
     * Instance Variables
     * =========================================================================
     */

    /** Singleton instance. */
    private static MalleableReservationHandler selfInstance;

    /** logger instance. */
    private final Logger logger =
            PhLogger.getSeparateLogger("malleableDummyHandler");

    /*
     * Status properties
     * =========================================================================
     */
    /** status and alternative starttime offsets of endpoints. */
    private Map<String, HelperStatus> endpointStatus;

    /** status and alternative starttime offsets of links. */
    private Map<String, HelperStatus> linkStatus;

    /*
     * Singleton Stuff
     * =========================================================================
     */

    /**
     * Instance getter.
     * 
     * @return Singleton Instance
     */
    public static MalleableReservationHandler getInstance() {
        synchronized (MalleableReservationHandler.class) {
            if (MalleableReservationHandler.selfInstance == null) {
                MalleableReservationHandler.selfInstance =
                        new MalleableReservationHandler();
            }
        }
        return MalleableReservationHandler.selfInstance;
    }

    /** Private constructor: Singleton. */
    private MalleableReservationHandler() {
        super();
        this.initStatus();
    }

    /**
     * Todo.
     */
    private void initStatus() {
        // init the status of endpoints of domain1
        this.endpointStatus = new Hashtable<String, HelperStatus>();
        this.endpointStatus.put("128.1.0.1", new HelperStatus(
                AvailabilityCodeType.ENDPOINT_NOT_AVAILABLE, 160));
        this.endpointStatus.put("128.1.0.2", new HelperStatus(
                AvailabilityCodeType.AVAILABLE, 120));
        this.endpointStatus.put("128.1.0.3", new HelperStatus(
                AvailabilityCodeType.AVAILABLE, 120));
        this.endpointStatus.put("128.1.0.10", new HelperStatus(
                AvailabilityCodeType.AVAILABLE, 60));
        this.endpointStatus.put("128.1.0.11", new HelperStatus(
                AvailabilityCodeType.AVAILABLE, 60));
        // init the status of links of domain1
        this.linkStatus = new Hashtable<String, HelperStatus>();
        this.linkStatus.put("128.1.0.10:128.1.0.1", new HelperStatus(
                AvailabilityCodeType.AVAILABLE, 160));
        this.linkStatus.put("128.1.0.10:128.1.0.2", new HelperStatus(
                AvailabilityCodeType.PATH_NOT_AVAILABLE, 120));
        this.linkStatus.put("128.1.0.10:128.1.0.3", new HelperStatus(
                AvailabilityCodeType.AVAILABLE, 180));
        this.linkStatus.put("128.1.0.11:128.1.0.1", new HelperStatus(
                AvailabilityCodeType.AVAILABLE, 160));
        this.linkStatus.put("128.1.0.11:128.1.0.2", new HelperStatus(
                AvailabilityCodeType.PATH_NOT_AVAILABLE, 120));
        this.linkStatus.put("128.1.0.11:128.1.0.3", new HelperStatus(
                AvailabilityCodeType.AVAILABLE, 180));
        // init the status of endpoints of domain2
        this.endpointStatus.put("128.2.0.1", new HelperStatus(
                AvailabilityCodeType.ENDPOINT_NOT_AVAILABLE, 180));
        this.endpointStatus.put("128.2.0.2", new HelperStatus(
                AvailabilityCodeType.ENDPOINT_NOT_AVAILABLE, 100));
        this.endpointStatus.put("128.2.0.3", new HelperStatus(
                AvailabilityCodeType.AVAILABLE, 120));
        this.endpointStatus.put("128.2.0.4", new HelperStatus(
                AvailabilityCodeType.ENDPOINT_NOT_AVAILABLE, 180));
        this.endpointStatus.put("128.2.0.5", new HelperStatus(
                AvailabilityCodeType.ENDPOINT_NOT_AVAILABLE, 100));
        this.endpointStatus.put("128.2.0.6", new HelperStatus(
                AvailabilityCodeType.AVAILABLE, 120));
        this.endpointStatus.put("128.2.0.10", new HelperStatus(
                AvailabilityCodeType.AVAILABLE, 60));
        this.endpointStatus.put("128.2.0.11", new HelperStatus(
                AvailabilityCodeType.AVAILABLE, 60));
        // init the status of links of domain2
        this.linkStatus.put("128.2.0.1:128.2.0.4", new HelperStatus(
                AvailabilityCodeType.AVAILABLE, 180));
        this.linkStatus.put("128.2.0.1:128.2.0.5", new HelperStatus(
                AvailabilityCodeType.AVAILABLE, 180));
        this.linkStatus.put("128.2.0.1:128.2.0.6", new HelperStatus(
                AvailabilityCodeType.AVAILABLE, 180));
        this.linkStatus.put("128.2.0.2:128.2.0.4", new HelperStatus(
                AvailabilityCodeType.PATH_NOT_AVAILABLE, 180));
        this.linkStatus.put("128.2.0.2:128.2.0.5", new HelperStatus(
                AvailabilityCodeType.PATH_NOT_AVAILABLE, 180));
        this.linkStatus.put("128.2.0.2:128.2.0.6", new HelperStatus(
                AvailabilityCodeType.PATH_NOT_AVAILABLE, 180));
        this.linkStatus.put("128.2.0.3:128.2.0.4", new HelperStatus(
                AvailabilityCodeType.AVAILABLE, 180));
        this.linkStatus.put("128.2.0.3:128.2.0.5", new HelperStatus(
                AvailabilityCodeType.AVAILABLE, 180));
        this.linkStatus.put("128.2.0.3:128.2.0.6", new HelperStatus(
                AvailabilityCodeType.AVAILABLE, 180));
        // init the status of endpoints of domain3
        this.endpointStatus.put("128.3.0.1", new HelperStatus(
                AvailabilityCodeType.ENDPOINT_NOT_AVAILABLE, 300));
        this.endpointStatus.put("128.3.0.2", new HelperStatus(
                AvailabilityCodeType.ENDPOINT_NOT_AVAILABLE, 300));
        this.endpointStatus.put("128.3.0.3", new HelperStatus(
                AvailabilityCodeType.AVAILABLE, 120));
        this.endpointStatus.put("128.3.0.10", new HelperStatus(
                AvailabilityCodeType.AVAILABLE, 60));
        this.endpointStatus.put("128.3.0.11", new HelperStatus(
                AvailabilityCodeType.AVAILABLE, 60));
        // init the status of links of domain3
        this.linkStatus.put("128.3.0.1:128.3.0.10", new HelperStatus(
                AvailabilityCodeType.AVAILABLE, 180));
        this.linkStatus.put("128.3.0.2:128.3.0.10", new HelperStatus(
                AvailabilityCodeType.PATH_NOT_AVAILABLE, 300));
        this.linkStatus.put("128.3.0.3:128.3.0.10", new HelperStatus(
                AvailabilityCodeType.AVAILABLE, 180));
        this.linkStatus.put("128.3.0.1:128.3.0.11", new HelperStatus(
                AvailabilityCodeType.AVAILABLE, 180));
        this.linkStatus.put("128.3.0.2:128.3.0.11", new HelperStatus(
                AvailabilityCodeType.PATH_NOT_AVAILABLE, 300));
        this.linkStatus.put("128.3.0.3:128.3.0.11", new HelperStatus(
                AvailabilityCodeType.AVAILABLE, 180));
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
     * Todo..
     */
    @Override
    public ActivateResponseType activate(final ActivateType request) {
        final ActivateResponseType responseType = new ActivateResponseType();
        responseType.setSuccess(true);

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
        responseType.setSuccess(true);

        return responseType;
    }

    /**
     * Todo.
     */
    @Override
    public CreateReservationResponseType createReservation(
            final CreateReservationType request) {
        this.logger.debug("createReservation call");

        // no intelligence here. Only create random reservation-ID and return
        // it.
        final CreateReservationResponseType responseType =
                new CreateReservationResponseType();
        responseType.setJobID(request.getJobID());
        responseType.setReservationID(String.valueOf(Helpers
                .getPositiveRandomInt()));

        return responseType;
    }

    /**
     * Todo.
     */
    @Override
    public GetReservationsResponseType getReservations(
            final GetReservationsType request) {
        return new GetReservationsResponseType();
    }

    /**
     * Todo.
     */
    @Override
    public GetStatusResponseType getStatus(final GetStatusType request) {
        return new GetStatusResponseType();
    }

    /**
     * Todo.
     */
    @Override
    public IsAvailableResponseType isAvailable(final IsAvailableType request) {
        this.logger.debug("isAvailable call");

        final IsAvailableResponseType responseType =
                new IsAvailableResponseType();
        final ConnectionAvailabilityType cat =
                new ConnectionAvailabilityType();

        // get service- and connection-constraints
        // we assume here, that only one service and one connection are existent
        // !!! this may change in the future !!!
        final ServiceConstraintType sct = request.getService().get(0);
        final ConnectionConstraintType cct = sct.getConnections().get(0);
        final String sourceTNA = cct.getSource().getEndpointId();
        final String destinationTNA = cct.getTarget().get(0).getEndpointId();

        // check for a fixed reservation
        if (!sct.getTypeOfReservation().equals(ReservationType.FIXED)) {
            this.logger.debug("only fixed reservations supported!");
            return null;
        }

        // get the availability of the link from source to target
        final AvailabilityCodeType availabilityLink =
                this.checkAvailability(sourceTNA, destinationTNA);
        this.logger.debug("availability of link " + sourceTNA + ":"
                + destinationTNA + " : " + availabilityLink);

        // get the availabilities of the endpoints
        final AvailabilityCodeType availabilitySource =
                this.checkAvailability(sourceTNA);
        this.logger.debug("availability of source " + sourceTNA + ": "
                + availabilitySource);
        final AvailabilityCodeType availabilityDest =
                this.checkAvailability(destinationTNA);
        this.logger.debug("availability of destination " + destinationTNA
                + ": " + availabilityDest);

        final int availabilitySum =
                Math.max(availabilityLink.ordinal(), Math.max(
                        availabilitySource.ordinal(), availabilityDest
                                .ordinal()));

        cat.setConnectionID(cct.getConnectionID());
        cat.setServiceID(sct.getServiceID());

        cat.setAvailability(AvailabilityCodeType.values()[availabilitySum]);
        if (availabilitySum > 0) {
            // check offset for link
            long offsetLink = 0;
            if (availabilityLink
                    .equals(AvailabilityCodeType.PATH_NOT_AVAILABLE)) {
                offsetLink =
                        this.linkStatus.get(sourceTNA + ":" + destinationTNA)
                                .getStartTimeOffset();
            }
            // check offset for source
            long offsetSource = 0;
            if (availabilitySource
                    .equals(AvailabilityCodeType.ENDPOINT_NOT_AVAILABLE)) {
                cat.getBlockedEndpoints().add(sourceTNA);
                offsetSource =
                        this.endpointStatus.get(sourceTNA)
                                .getStartTimeOffset();
            }
            // check offset for destination
            long offsetDestination = 0;
            if (availabilityDest
                    .equals(AvailabilityCodeType.ENDPOINT_NOT_AVAILABLE)) {
                cat.getBlockedEndpoints().add(destinationTNA);
                offsetDestination =
                        this.endpointStatus.get(destinationTNA)
                                .getStartTimeOffset();
            }
            // set the alternative starttime offset in seconds
            responseType.setAlternativeStartTimeOffset(Math.max(offsetLink,
                    Math.max(offsetSource, offsetDestination)));
        }

        cat.setMaxBW(request.getService().get(0).getConnections().get(0)
                .getMaxBW());

        responseType.getDetailedResult().add(cat);

        return responseType;
    }

    /**
     * Todo.
     * 
     * @param endpointId
     * @return
     */
    private AvailabilityCodeType checkAvailability(final String endpointId) {
        if (this.endpointStatus.containsKey(endpointId)) {
            return this.endpointStatus.get(endpointId).getAvailability();
        }
        return AvailabilityCodeType.ENDPOINT_NOT_AVAILABLE;
    }

    /**
     * Todo.
     * 
     * @param source
     * @param dest
     * @return
     */
    private AvailabilityCodeType checkAvailability(final String source,
            final String dest) {
        final String linkId = source + ":" + dest;
        if (this.linkStatus.containsKey(linkId)) {
            return this.linkStatus.get(linkId).getAvailability();
        }
        return AvailabilityCodeType.PATH_NOT_AVAILABLE;
    }
}
