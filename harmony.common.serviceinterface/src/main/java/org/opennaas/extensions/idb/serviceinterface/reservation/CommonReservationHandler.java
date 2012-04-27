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
package org.opennaas.extensions.idb.serviceinterface.reservation;

import java.lang.reflect.InvocationTargetException;
import java.util.MissingResourceException;

import org.apache.muse.ws.addressing.soap.SoapFault;
import org.w3c.dom.Element;

import org.opennaas.extensions.idb.serviceinterface.RequestHandler;
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
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationsComplexType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationsResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationsType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatusResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.IsAvailableResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.IsAvailableType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.OperationNotAllowedFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.OperationNotSupportedFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.utils.JaxbSerializer;
import org.opennaas.extensions.idb.serviceinterface.topology.registrator.AbstractTopologyRegistrator;
import org.opennaas.extensions.idb.serviceinterface.utils.ForwardingHelper;
import org.opennaas.core.utils.Config;

/**
 * @author willner
 * 
 */
public class CommonReservationHandler extends RequestHandler {

    private static CommonReservationHandler selfInstance;

    /**
     * Instance getter.
     * 
     * @return Singleton Instance
     */
    public static CommonReservationHandler getInstance() {
        if (CommonReservationHandler.selfInstance == null) {
            CommonReservationHandler.selfInstance = new CommonReservationHandler();
        }
        return CommonReservationHandler.selfInstance;
    }

    private final ForwardingHelper forwardingHelper = ForwardingHelper
            .getInstance();

    /**
     * @param request
     * @return
     * @throws SoapFault
     */
    public ActivateResponseType activate(final ActivateType request)
            throws SoapFault, Throwable {
        throw new OperationNotSupportedFaultException("Not implemented yet.");
    }

    /**
     * @param request
     * @return
     * @throws SoapFault
     */
    public BindResponseType bind(final BindType request) throws SoapFault,
            Throwable {
        throw new OperationNotSupportedFaultException("Not implemented yet.");
    }

    /**
     * @param request
     * @return
     * @throws SoapFault
     */
    public CancelJobResponseType cancelJob(final CancelJobType request)
            throws SoapFault, Throwable {
        throw new OperationNotSupportedFaultException("Not implemented yet.");
    }

    /**
     * @param request
     * @return
     * @throws SoapFault
     */
    public CancelReservationResponseType cancelReservation(
            final CancelReservationType request) throws SoapFault, Throwable {
        throw new OperationNotSupportedFaultException("Not implemented yet.");
    }

    /**
     * @param request
     * @return
     * @throws SoapFault
     */
    public CompleteJobResponseType completeJob(final CompleteJobType request)
            throws SoapFault, Throwable {
        throw new OperationNotSupportedFaultException("Not implemented yet.");
    }

    /**
     * @param request
     * @return
     * @throws SoapFault
     */
    public CreateReservationResponseType createReservation(
            final CreateReservationType request) throws SoapFault, Throwable {
        throw new OperationNotSupportedFaultException("Not implemented yet.");
    }

    /**
     * @param request
     * @return
     * @throws SoapFault
     */
    public GetReservationsResponseType getReservations(
            final GetReservationsType request) throws SoapFault, Throwable {
        throw new OperationNotSupportedFaultException("Not implemented yet.");
    }

    /**
     * @param request
     * @return
     * @throws SoapFault
     */
    public GetStatusResponseType getStatus(final GetStatusType request)
            throws SoapFault, Throwable {
        throw new OperationNotSupportedFaultException("Not implemented yet.");
    }

    /**
     * 
     */
    @Override
    protected Object invokeMethod(final Object objRequestType,
            final String methodName) throws Throwable {
        Object result;

        // Check if forwarding is enabled
        final boolean doForward = Config.isTrue("hsiTemplate",
                "request.forwarding");

        if (doForward && this.forwardingHelper.isForeignRequest(objRequestType)) {
            this.getLogger().debug(
                    objRequestType.getClass().getSimpleName()
                            + " is foreign request");

            try {
                result = this.invokeMuseWS(objRequestType, methodName);
            } catch (final MissingResourceException exception) {
                throw new OperationNotAllowedFaultException(
                        "At least one endpoint of this request ("
                                + objRequestType.getClass().getSimpleName()
                                + ") is not part of this domain and since no parent broker is configured the request cannot be forwarded.");
            }
        } else {
            this.getLogger().debug(
                    objRequestType.getClass().getSimpleName()
                            + " is local request");

            // Remove Suffix
            if (this.forwardingHelper.hasReservationId(objRequestType)) {
                this.forwardingHelper.removeSuffix(objRequestType);
            }

            result = super.invokeMethod(objRequestType, methodName);
        }

        // Add Suffix
        if (this.forwardingHelper.hasReservationId(result)) {
            this.forwardingHelper.addSuffix(result);
            // Add Suffix to all reservations in ReservationsResponseType
        } else if (GetReservationsResponseType.class.isInstance(result)) {
            final GetReservationsResponseType type = (GetReservationsResponseType) result;

            for (final GetReservationsComplexType reservationComplexType : type
                    .getReservation()) {
                this.forwardingHelper.addSuffix(reservationComplexType);
            }
        }

        return result;
    }

    /**
     * @param objRequestType
     * @param methodName
     * @return
     * @throws SoapFault
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     */
    private Object invokeMuseWS(final Object objRequestType,
            final String methodName) throws SoapFault, InstantiationException,
            IllegalAccessException, InvocationTargetException,
            ClassNotFoundException, NoSuchMethodException {
        final String propertyFile = AbstractTopologyRegistrator
                .getLatestInstance().getPropertyFile();

        this.getLogger().debug("Using propertyfile: " + propertyFile);

        final SimpleReservationClient client = new SimpleReservationClient(
                Config.getString(propertyFile, "parent.reservationEPR"));

        final Object objRequest = this.setType(objRequestType);

        final Element elementRequest = JaxbSerializer.getInstance()
                .objectToElement(objRequest);

        this.getLogger().debug("Forwarding using method: " + methodName);

        final Element result = client.handleByName(elementRequest, methodName);

        final Object objResponse = JaxbSerializer.getInstance()
                .elementToObject(result);

        return this.getType(objResponse);
    }

    /**
     * @param request
     * @return
     * @throws SoapFault
     * @throws RuntimeException
     */
    public IsAvailableResponseType isAvailable(final IsAvailableType request)
            throws SoapFault, Throwable {
        throw new OperationNotSupportedFaultException("Not implemented yet.");
    }
}
