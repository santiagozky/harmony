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


/**
 *
 */
package eu.ist_phosphorus.harmony.adapter.thin.handler;

import eu.ist_phosphorus.harmony.adapter.thin.implementation.AvailabilityHandler;
import eu.ist_phosphorus.harmony.adapter.thin.implementation.CancelHandler;
import eu.ist_phosphorus.harmony.adapter.thin.implementation.CreationHandler;
import eu.ist_phosphorus.harmony.adapter.thin.implementation.StatusHandler;
import eu.ist_phosphorus.harmony.common.serviceinterface.RequestHandler;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.ActivateResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.ActivateType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.BindResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.BindType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CancelJobResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CancelJobType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CancelReservationResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CancelReservationType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CompleteJobResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CompleteJobType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CreateReservationResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CreateReservationType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetReservationResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetReservationType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetReservationsResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetReservationsType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetStatusResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetStatusType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.IsAvailableResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.IsAvailableType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.NotificationResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.NotificationType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.exceptions.InvalidReservationIDFaultException;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.exceptions.OperationNotAllowedFaultException;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;

/**
 * @author Daniel Beer (daniel.beer@iais.fraunhofer.de)
 */
public final class ReservationHandler extends RequestHandler {
    /** Singleton instance. */
    private static ReservationHandler selfInstance = null;

    /**
     * Instance getter.
     *
     * @return Singleton Instance
     */
    public static ReservationHandler getInstance() {
        if (ReservationHandler.selfInstance == null) {
            ReservationHandler.selfInstance = new ReservationHandler();
        }
        return ReservationHandler.selfInstance;
    }

    /** Private constructor: Singleton. */
    private ReservationHandler() {
        // nothing
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
     * Activation Handler.
     * <p>
     * Handler will accept Activation-Requests and return a Activation-Response.
     * <p>
     *
     * @param element
     *                ActivationRequest
     * @return ActivationResponse
     * @throws UnexpectedFaultException
     *                 should not happen
     * @throws OperationNotAllowedFaultException
     */
    public ActivateResponseType activate(@SuppressWarnings("unused")
    final ActivateType element) throws OperationNotAllowedFaultException {
        throw new OperationNotAllowedFaultException("not supported!");

    }

    /**
     * Bind Handler. Not supported!
     *
     * @param element
     *                BindRequest
     * @return BindResponse
     * @throws UnexpectedFaultException
     *                 should not happen
     */
    @SuppressWarnings("unused")
    public BindResponseType bind(final BindType element)
            throws UnexpectedFaultException {
        throw new UnexpectedFaultException("not supported");
    }

    /**
     * CancelJob Handler.
     * <p>
     * Handler will accept CancelJob-Requests and return a CancelJob-Response.
     * <p>
     *
     * @param element
     *                CancelJobRequest
     * @return CancelJobResponse
     * @throws UnexpectedFaultException
     *                 should not happen
     */
    public CancelJobResponseType cancelJob(final CancelJobType element)
            throws UnexpectedFaultException {
        CancelJobResponseType temp =
                CancelHandler.getInstance().cancelJob(element);
        return temp;
    }

    /**
     * CancelReservation Handler.
     * <p>
     * Handler will accept CancelReservation-Requests and return a
     * CancelReservation-Response containing the success-parameter.
     * <p>
     *
     * @param request
     *                CancelReservationRequest
     * @return CancelReservationResponse
     * @throws UnexpectedFaultException
     *                 should not happen
     * @throws InvalidReservationIDFaultException
     */
    public CancelReservationResponseType cancelReservation(
            final CancelReservationType request)
            throws UnexpectedFaultException, InvalidReservationIDFaultException {
        return CancelHandler.getInstance().cancelReservation(request);
    }

    /**
     * CompleteJob Handler. not supported!
     *
     * @param element
     *                CompleteJobRequest
     * @return CompleteJobResponse
     * @throws UnexpectedFaultException
     *                 should not happen
     * @throws OperationNotAllowedFaultException
     */
    @SuppressWarnings("unused")
    public CompleteJobResponseType completeJob(final CompleteJobType element)
            throws OperationNotAllowedFaultException {
        throw new OperationNotAllowedFaultException("not supported!");
    }

    /**
     * Reservation Handler.
     * <p>
     * Handler will accept CreateReservation-Requests and return a
     * CreateReservation-Response
     * <p>
     *
     * @param element
     *                ReservationRequest
     * @return ReservationResponse
     * @throws UnexpectedFaultException
     *                 should not happen
     * @throws InvalidReservationIDFaultException
     */
    public CreateReservationResponseType createReservation(
            final CreateReservationType element)
            throws UnexpectedFaultException, InvalidReservationIDFaultException {
        return CreationHandler.getInstance().createReservation(element);
    }

    /**
     * Status Handler.
     * <p>
     * Handler will accept GetStatus-Requests and return a GetStatus-Response
     * containing a ServiceStatus-Object.
     * <p>
     *
     * @param element
     *                StatusRequest
     * @return StatusResponse
     * @throws UnexpectedFaultException
     *                 should not happen
     * @throws InvalidReservationIDFaultException
     */
    @SuppressWarnings("unused")
    public GetStatusResponseType getStatus(final GetStatusType element)
            throws UnexpectedFaultException, InvalidReservationIDFaultException {
        return StatusHandler.getInstance().getStatus(element);
    }

    /**
     * IsAvailable Handler.
     * <p>
     * Handler will accept Availability-Requests and return a Availability-
     * Response containing a ConnectionAvailability-Object.
     * <p>
     *
     * @param element
     *                IsAvailable Request
     * @return Response
     * @throws UnexpectedFaultException
     *                 should not happen
     */
    public IsAvailableResponseType isAvailable(final IsAvailableType element)
            throws UnexpectedFaultException {
        return AvailabilityHandler.getInstance().isAvailable(element);
    }

    /**
     * @param element
     *                of type NotificationType
     * @return NotificationResponseType
     * @throws OperationNotAllowedFaultException
     * @throws UnexpectedFaultException
     */
    public NotificationResponseType notification(@SuppressWarnings("unused")
    final NotificationType element) throws UnexpectedFaultException {
        return NotificationHandler.getInstance().notification(element);
    }

    /**
     * @param element
     * @return
     * @throws InvalidReservationIDFaultException
     */
    public GetReservationResponseType getReservation(
            final GetReservationType element) throws UnexpectedFaultException, InvalidReservationIDFaultException {
        return StatusHandler.getInstance().getReservation(element);
    }

    /**
     * @param element
     * @return
     */
    public GetReservationsResponseType getReservations(
            final GetReservationsType element) throws UnexpectedFaultException {
        return StatusHandler.getInstance().getReservations(element);
    }

}
