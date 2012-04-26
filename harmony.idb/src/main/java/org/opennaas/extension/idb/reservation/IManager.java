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


package org.opennaas.extension.idb.reservation;

import java.util.Hashtable;

import org.apache.muse.ws.addressing.soap.SoapFault;

import org.opennaas.extension.idb.serviceinterface.databinding.jaxb.ActivateResponseType;
import org.opennaas.extension.idb.serviceinterface.databinding.jaxb.ActivateType;
import org.opennaas.extension.idb.serviceinterface.databinding.jaxb.CancelReservationResponseType;
import org.opennaas.extension.idb.serviceinterface.databinding.jaxb.CancelReservationType;
import org.opennaas.extension.idb.serviceinterface.databinding.jaxb.CreateReservationResponseType;
import org.opennaas.extension.idb.serviceinterface.databinding.jaxb.CreateReservationType;
import org.opennaas.extension.idb.serviceinterface.databinding.jaxb.GetStatusResponseType;
import org.opennaas.extension.idb.serviceinterface.databinding.jaxb.GetStatusType;
import org.opennaas.extension.idb.serviceinterface.databinding.jaxb.IsAvailableResponseType;
import org.opennaas.extension.idb.serviceinterface.databinding.jaxb.IsAvailableType;
import org.opennaas.extension.idb.database.hibernate.Domain;
import org.opennaas.extension.idb.exceptions.database.DatabaseException;

public interface IManager {

    /**
     * @param element
     *            Element
     * @return ActivateResponseType
     * @throws SoapFault
     *             A SoapFault
     * @throws DatabaseException
     */
    public abstract Hashtable<Domain, ActivateResponseType> activateReservation(
            final Hashtable<Domain, ActivateType> requests) throws SoapFault,
            DatabaseException;

    /**
     * @param element
     *            Element
     * @return CancelReservationResponseType
     * @throws SoapFault
     *             A SoapFault
     * @throws DatabaseException
     */
    public abstract Hashtable<Domain, CancelReservationResponseType> cancelReservation(
            final Hashtable<Domain, CancelReservationType> requests)
            throws SoapFault, DatabaseException;

    /**
     * @param element
     *            Element
     * @return CreateReservationResponseType
     * @throws SoapFault
     *             A SoapFault
     * @throws DatabaseException
     */
    public abstract Hashtable<Domain, CreateReservationResponseType> createReservation(
            final Hashtable<Domain, CreateReservationType> requests)
            throws SoapFault, DatabaseException;

    /**
     * @param requests
     * @return
     * @throws SoapFault
     * @throws DatabaseException
     */
    public abstract Hashtable<Domain, GetStatusResponseType> getStatus(
            final Hashtable<Domain, GetStatusType> requests) throws SoapFault,
            DatabaseException;

    /**
     * @param element
     *            Element
     * @return CreateReservationResponseType
     * @throws SoapFault
     *             A SoapFault
     * @throws DatabaseException
     */
    public abstract Hashtable<Domain, IsAvailableResponseType> isAvailable(
            final Hashtable<Domain, IsAvailableType> requests)
            throws SoapFault, DatabaseException;
}
