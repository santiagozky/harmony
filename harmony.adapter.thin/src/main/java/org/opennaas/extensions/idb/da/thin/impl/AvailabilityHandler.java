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


package org.opennaas.extensions.idb.da.thin.impl;

import java.sql.Timestamp;
import java.util.Calendar;

import org.apache.log4j.Logger;

import org.opennaas.extensions.idb.da.thin.persistence.DbManager;
import org.opennaas.extensions.idb.da.thin.persistence.exceptions.DestinationPortUnavailableException;
import org.opennaas.extensions.idb.da.thin.persistence.exceptions.PathNotFoundException;
import org.opennaas.extensions.idb.da.thin.persistence.exceptions.SourceAndDestinationPortUnavailableException;
import org.opennaas.extensions.idb.da.thin.persistence.exceptions.SourcePortUnavailableException;
import org.opennaas.extensions.idb.da.thin.persistence.orm.GmplsConnection;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AvailabilityCodeType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ConnectionAvailabilityType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ConnectionConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.IsAvailableResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.IsAvailableType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ServiceConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;
import org.opennaas.core.utils.Helpers;
import org.opennaas.core.utils.PhLogger;

/**
 * @author Stephan Wagner (stephan.wagner@iais.fraunhofer.de), Daniel Beer
 *         (daniel.beer@iais.fraunhofer.de)
 */
public final class AvailabilityHandler {
    /** Singleton instance. */
    private static AvailabilityHandler selfInstance = null;
    private static Logger logger = null;

    /**
     * Instance getter.
     *
     * @return Singleton Instance
     */
    public static AvailabilityHandler getInstance() {
        if (AvailabilityHandler.selfInstance == null) {
            AvailabilityHandler.selfInstance = new AvailabilityHandler();
        }
        return AvailabilityHandler.selfInstance;
    }

    /**
     * Private constructor: Singleton.
     */
    private AvailabilityHandler() {
        logger = PhLogger.getLogger(this.getClass());
    }

    /**
     * Singleton - Cloning _not_ supported!
     *
     * @return Should never return anything...
     * @throws CloneNotSupportedException
     *                 Singleton hates to be cloned!
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    /**
     * Checks availability for the request.
     *
     * @param isAvailableRequest
     *                request
     * @return the availability for the request
     * @throws UnexpectedFaultException
     *                 should not happen
     */
    public IsAvailableResponseType isAvailable(
            final IsAvailableType isAvailableRequest)
            throws UnexpectedFaultException {
        final IsAvailableResponseType response = new IsAvailableResponseType();

        long jobId = System.currentTimeMillis();
        if (isAvailableRequest.isSetJobID()
                && (isAvailableRequest.getJobID().longValue() > 0)) {
            jobId = isAvailableRequest.getJobID().longValue();
        }

        final long reservationId = DbManager.insertReservation(jobId, null);

        for (final ServiceConstraintType sct : isAvailableRequest.getService()) {
            final Calendar calStart =
                    Helpers.xmlCalendarToCalendar(sct
                            .getFixedReservationConstraints().getStartTime());
            final Timestamp startTime =
                    new Timestamp(calStart.getTime().getTime());

            calStart.add(Calendar.SECOND, sct.getFixedReservationConstraints()
                    .getDuration());
            final Timestamp endTime = new Timestamp(calStart.getTimeInMillis());
            for (final ConnectionConstraintType cct : sct.getConnections()) {
                final GmplsConnection con = new GmplsConnection();
                con.setJobId(jobId);
                con.setReservationId(reservationId);
                con.setServiceId(sct.getServiceID());
                con.setConnectionId(cct.getConnectionID());
                con.setSrcTNA(cct.getSource().getEndpointId());
                con.setDestTNA(cct.getTarget().get(0).getEndpointId());
                con.setStartTime(startTime);
                con.setEndTime(endTime);
                con.setBandwidth(cct.getMinBW());
                final ConnectionAvailabilityType avail =
                        new ConnectionAvailabilityType();
                avail.setServiceID(sct.getServiceID());
                avail.setConnectionID(cct.getConnectionID());

                try {
                    if (DbManager.insertConnection(con)) {
                        avail.setAvailability(AvailabilityCodeType.AVAILABLE);
                    }
                } catch (final SourcePortUnavailableException e) {
                    avail
                            .setAvailability(AvailabilityCodeType.ENDPOINT_NOT_AVAILABLE);
                    avail.getBlockedEndpoints().add(
                            cct.getSource().getEndpointId());
                    e.printStackTrace();
                } catch (final DestinationPortUnavailableException e) {
                    avail
                            .setAvailability(AvailabilityCodeType.ENDPOINT_NOT_AVAILABLE);
                    avail.getBlockedEndpoints().add(
                            cct.getTarget().get(0).getEndpointId());
                    e.printStackTrace();
                } catch (final SourceAndDestinationPortUnavailableException e) {
                    avail
                            .setAvailability(AvailabilityCodeType.ENDPOINT_NOT_AVAILABLE);
                    avail.getBlockedEndpoints().add(
                            cct.getSource().getEndpointId());
                    avail.getBlockedEndpoints().add(
                            cct.getTarget().get(0).getEndpointId());
                    e.printStackTrace();
                } catch (final PathNotFoundException e) {
                    avail
                            .setAvailability(AvailabilityCodeType.PATH_NOT_AVAILABLE);
                    e.printStackTrace();
                }
                response.getDetailedResult().add(avail);
            }

        }
        DbManager.deleteWholeReservation(reservationId);
        return response;
    }
}
