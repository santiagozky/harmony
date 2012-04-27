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

import java.rmi.UnexpectedException;
import java.util.Random;

import org.apache.muse.ws.addressing.soap.SoapFault;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ActivateResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ActivateType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.BindResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.BindType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelJobResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelJobType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelReservationResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CompleteJobResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CompleteJobType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CreateReservationResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CreateReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationsResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationsType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatusResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.IsAvailableResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.IsAvailableType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.NotificationResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.NotificationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.InvalidReservationIDFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.utils.WebserviceUtils;
import org.opennaas.extensions.idb.serviceinterface.reservation.CommonReservationHandler;
import org.opennaas.extensions.idb.exception.database.DatabaseException;

/** Provision Request Handler. */
public final class ReservationRequestHandler extends CommonReservationHandler {

    /** Singleton Instance. */
    private static ReservationRequestHandler selfInstance = null;

    /** Performance logger instance. */
    // private static Logger performanceLogger =
    // PhLogger.getLogger("Performance");
    /**
     * Instance getter.
     * 
     * @return Singleton Instance
     */
    public static ReservationRequestHandler getInstance() {
        if (ReservationRequestHandler.selfInstance == null) {
            ReservationRequestHandler.selfInstance =
                    new ReservationRequestHandler();
        }
        return ReservationRequestHandler.selfInstance;
    }

    private void logReservationID(final long id) {
        try {
            getPerformanceLogger().debug("reservation_ID " + id);
        } catch (SoapFault e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void logReservationID(final String id)
            throws InvalidReservationIDFaultException {
        logReservationID(WebserviceUtils.convertReservationID(id));
    }

    private final String generateGRI() {
        byte[] randomBytes = new byte[20];
        new Random().nextBytes(randomBytes);

        String result = "";
        for (byte curr : randomBytes) {
            String hex =
                    Integer.toHexString(curr - Byte.MIN_VALUE).toUpperCase();

            if (1 == hex.length()) {
                hex = "0" + hex;
            }

            result += hex;
        }

        return result;
    }

    private final String generateToken() {
        return this.generateGRI();
    }

    /** Private constructor: Singleton. */
    private ReservationRequestHandler() {
        // Nothing to do here...
    }

    /**
     * Activation Handler.
     * 
     * @param element
     *            Activation Request
     * @return Activation Response @ * A DatabaseException
     * @throws SoapFault
     *             A SoapFault
     * @throws DatabaseException
     */
    @Override
    public ActivateResponseType activate(final ActivateType element)
            throws SoapFault, DatabaseException {
        logReservationID(element.getReservationID());
        final ActivateResponseType response =
                ReservationOperationsHandler.getInstance().activation(element);
        return response;
    }

    /*
     * Handler
     * =========================================================================
     */

    /**
     * Bind Handler.
     * 
     * @param element
     *            Bind Request
     * @return Bind Response
     */
    @Override
    public BindResponseType bind(final BindType element) {
        final BindResponseType response =
                MiscOperationsHandler.getInstance().bind(element);
        return response;
    }

    /**
     * CancelJob Handler.
     * 
     * @param element
     *            CancelJob Request
     * @return CancelJob Response
     * @throws DatabaseException
     *             @ * DatabaseExeption
     */
    @Override
    public CancelJobResponseType cancelJob(final CancelJobType element)
            throws DatabaseException {
        final CancelJobResponseType response =
                JobOperationsHandler.getInstance().cancelJob(element);
        return response;
    }

    /**
     * CancelReservation Handler.
     * 
     * @param element
     *            CancelReservation Request
     * @return CancelReservation Response @ * A DatabaseException
     * @throws SoapFault
     *             A SoapFault
     * @throws DatabaseException
     */
    @Override
    public CancelReservationResponseType cancelReservation(
            final CancelReservationType element) throws SoapFault,
            DatabaseException {
        logReservationID(element.getReservationID());
        final CancelReservationResponseType response =
                ReservationOperationsHandler.getInstance().cancelReservation(
                        element);
        return response;
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
     * CompleteJob Handler.
     * 
     * @param element
     *            CompleteJob Request
     * @return CompleteJob Response
     * @throws DatabaseException
     *             @ * DatabaseException
     */
    @Override
    public CompleteJobResponseType completeJob(final CompleteJobType element)
            throws DatabaseException {
        final CompleteJobResponseType response =
                JobOperationsHandler.getInstance().completeJob(element);
        return response;
    }

    /**
     * Reservation Handler.
     * 
     * @param element
     *            Reservation Request
     * @return Reservation Response @ * A DatabaseException
     * @throws SoapFault
     *             A SoapFault
     */
    @Override
    public CreateReservationResponseType createReservation(
            final CreateReservationType element) throws SoapFault,
            DatabaseException {
        
        String token = null;
        String gri = null;
        
        // Backup GRI
        if (null == element.getGRI()) {
            gri = this.generateGRI();
            
            element.setGRI(gri);
        } else {
            gri = element.getGRI();
        }

        // Backup Token
        if (null == element.getToken()) {
            token = this.generateToken();
            
            element.setToken(token);
        } else {
            token = element.getToken();
        }

        final CreateReservationResponseType response =
                ReservationSetupHandler.getInstance()
                        .createReservation(element);
        logReservationID(response.getReservationID());
      
        //TODO: This case should never happen
        if (null == response.getGRI()) {
            response.setGRI(gri);
        }
        if (null == response.getToken()) {
            response.setToken(token);
        }
        
        return response;
    }

    /**
     * GetReservation Handler.
     * 
     * @param element
     *            GetReservation Request
     * @return GetReservation Response @ * A DatabaseException
     * @throws InvalidReservationIDFaultException
     * @throws DatabaseException
     */
    public GetReservationResponseType getReservation(
            final GetReservationType element)
            throws InvalidReservationIDFaultException, DatabaseException {
        logReservationID(element.getReservationID());
        final GetReservationResponseType responseType =
                ReservationManagementHandler.getInstance().getReservation(
                        element);

        return responseType;
    }

    /**
     * GetReservations Handler.
     * 
     * @param element
     *            GetReservations Request
     * @return GetReservations Response
     * @throws DatabaseException
     *             @ * DatabaseException
     */
    @Override
    public GetReservationsResponseType getReservations(
            final GetReservationsType element) throws DatabaseException {
        final GetReservationsResponseType responseType =
                ReservationManagementHandler.getInstance().getReservations(
                        element);

        return responseType;
    }

    /**
     * Status Handler.
     * 
     * @param element
     *            Status Request
     * @return Status Response @ * A DatabaseException
     * @throws SoapFault
     *             A SoapFault
     * @throws UnexpectedException
     */
    @Override
    public GetStatusResponseType getStatus(final GetStatusType element)
            throws SoapFault, DatabaseException, UnexpectedException {
        logReservationID(element.getReservationID());
        final GetStatusResponseType response =
                ReservationOperationsHandler.getInstance().status(element);
        return response;
    }

    /**
     * . isAvailable Handler
     * 
     * @param element
     *            isAvailable Request
     * @return IsAvailable Response @ * DatabaseException
     * @throws SoapFault
     * @throws DatabaseException
     */
    @Override
    public IsAvailableResponseType isAvailable(final IsAvailableType element)
            throws SoapFault, DatabaseException {
        final IsAvailableResponseType response =
                ReservationSetupHandler.getInstance().isAvailable(element);
        return response;
    }

    /**
     * notify Handler.
     * 
     * @param element
     *            notify Request
     * @return notify Response
     */
    public NotificationResponseType notification(final NotificationType element) {
        return NotificationConsumerHandler.getInstance().notification(element);
    }

}
