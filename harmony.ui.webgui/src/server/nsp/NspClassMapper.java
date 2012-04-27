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

import client.classes.Reservation;
import client.classes.Service;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CreateReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationsComplexType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ServiceConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.InvalidReservationIDFaultException;
import org.opennaas.core.utils.Helpers;

public class NspClassMapper {
    public static final Reservation jaxb2gwt(
            final GetReservationsComplexType jaxbClass)
            throws InvalidReservationIDFaultException {
        final Reservation reservation = new Reservation();

        reservation.setId(jaxbClass.getReservationID());

        for (final ServiceConstraintType service : jaxbClass.getReservation()
                .getService()) {
            reservation.getServices().add(NspClassMapper.jaxb2gwt(service));
        }

        return reservation;
    }

    public static final Service jaxb2gwt(final ServiceConstraintType jaxbClass) {
        final Service service = new Service();

        service.setId(jaxbClass.getServiceID());
        service.setStartTime(Helpers.xmlCalendarToDate(jaxbClass
                .getFixedReservationConstraints().getStartTime()));
        service.setDuration(jaxbClass.getFixedReservationConstraints()
                .getDuration());

        return service;
    }

    public static final CreateReservationType gwt2jaxb(
            final Reservation gwtClass) {
        final CreateReservationType reservation = new CreateReservationType();

        // TODO

        return reservation;
    }

    public static final ServiceConstraintType gwt2jaxb(final Service gwtClass) {
        final ServiceConstraintType service = new ServiceConstraintType();

        // TODO

        return service;
    }

}
