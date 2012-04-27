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

package org.opennaas.extensions.idb.serviceinterface.databinding.validator;

import java.util.Date;

import javax.xml.datatype.XMLGregorianCalendar;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ConnectionConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CreateReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ServiceConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.InvalidRequestFaultException;
import org.opennaas.core.utils.Helpers;

public class SemanticValidator {

    /**
     * @param start
     * @param stop
     * @throws InvalidRequestFaultException
     */
    private static void checkReservationTime(final Date start, final Date stop)
            throws InvalidRequestFaultException {
        final Date now = new Date();

        if (!(start.before(stop) && stop.after(now))) {
            throw new InvalidRequestFaultException("Invalid reservation time! "
                    + start + " -> " + stop + ". Prozessing time: " + now);
        }
    }

    /**
     * @param start
     * @param duration
     * @throws InvalidRequestFaultException
     */
    private static void checkReservationTime(
            final XMLGregorianCalendar startXML, final long duration)
            throws InvalidRequestFaultException {
        final Date start = Helpers.xmlCalendarToDate(startXML);
        final Date stop = new Date(start.getTime() + duration * 1000);

        SemanticValidator.checkReservationTime(start, stop);
    }

    /**
     * @param start
     * @param stop
     * @throws InvalidRequestFaultException
     */
    private static void checkReservationTime(final XMLGregorianCalendar start,
            final XMLGregorianCalendar stop)
            throws InvalidRequestFaultException {
        SemanticValidator.checkReservationTime(
                Helpers.xmlCalendarToDate(start), Helpers
                        .xmlCalendarToDate(stop));
    }

    /**
     * @param input
     * @throws InvalidRequestFaultException
     */
    public static void validateContent(final Object input)
            throws InvalidRequestFaultException {
        if (input.getClass() == CreateReservationType.class) {
            SemanticValidator
                    .validateCreateReservationType((CreateReservationType) input);
        }
    }

    /**
     * @param crt
     * @throws InvalidRequestFaultException
     */
    private static void validateCreateReservationType(
            final CreateReservationType crt)
            throws InvalidRequestFaultException {
        for (final ServiceConstraintType service : crt.getService()) {

            // Fixed Reservation
            if (null != service.getFixedReservationConstraints()) {
                SemanticValidator.checkReservationTime(service
                        .getFixedReservationConstraints().getStartTime(),
                        service.getFixedReservationConstraints().getDuration());
            }

            // Malleable Reservation
            if (null != service.getMalleableReservationConstraints()) {
                SemanticValidator.checkReservationTime(service
                        .getMalleableReservationConstraints().getStartTime(),
                        service.getMalleableReservationConstraints()
                                .getDeadline());

                // Check for data amount
                for (final ConnectionConstraintType connection : service
                        .getConnections()) {
                    if (null == connection.getDataAmount()) {
                        throw new InvalidRequestFaultException(
                                "DataAmount must be set for Malleable Reservations!");
                    }
                }
            }

            for (final ConnectionConstraintType conn : service.getConnections()) {
                for (final EndpointType target : conn.getTarget()) {
                    if (conn.getSource().getEndpointId().equals(
                            target.getEndpointId())) {
                        throw new InvalidRequestFaultException(
                                "Source and destination may not be the same!");
                    }
                }

                final Integer maxBW = conn.getMaxBW();
                if ((maxBW != null) && (maxBW.intValue() < conn.getMinBW())) {
                    throw new InvalidRequestFaultException(
                            "MaxBW may not be less than MinBW!");
                }
            }
        }
    }
}
