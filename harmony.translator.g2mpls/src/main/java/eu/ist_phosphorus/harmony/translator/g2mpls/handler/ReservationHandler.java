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


package eu.ist_phosphorus.harmony.translator.g2mpls.handler;

import org.apache.muse.ws.addressing.soap.SoapFault;

import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.ActivateResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.ActivateType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CancelReservationResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CancelReservationType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CreateReservationResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CreateReservationType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetReservationsResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetReservationsType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetStatusResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetStatusType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.IsAvailableResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.IsAvailableType;
import eu.ist_phosphorus.harmony.common.serviceinterface.reservation.CommonReservationHandler;
import eu.ist_phosphorus.harmony.common.serviceinterface.reservation.SimpleReservationClient;
import eu.ist_phosphorus.harmony.common.utils.Config;

/**
 * Class to handle NSP reservation-requests in a predictable manner.
 */
public final class ReservationHandler extends CommonReservationHandler {

    /*
     * Instance Variables
     * =========================================================================
     */

    /** Singleton instance. */
    private static ReservationHandler selfInstance;

    /*
     * Singleton Stuff
     * =========================================================================
     */

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

    final private SimpleReservationClient client;
    final private eu.ist_phosphorus.harmony.adapter.dummy.handler.ReservationHandler dummyHandler;

    /** Private constructor: Singleton. */
    private ReservationHandler() {
        super();
        final String epr = Config.getString("g2mpls.epr");
        this.client = new SimpleReservationClient(epr);
        this.dummyHandler = eu.ist_phosphorus.harmony.adapter.dummy.handler.ReservationHandler.getInstance();
    }

    /*
     * (non-Javadoc)
     * 
     * @seeeu.ist_phosphorus.harmony.adapter.dummy.handler.IReservationHandler#
     * runRequest
     * (eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb
     * .ActivateType)
     */
    @Override
    public ActivateResponseType activate(
            final ActivateType request) {
        return this.dummyHandler.activate(request);
    }

    /*
     * Handler
     * =========================================================================
     */

    /**
     * 
     * @see ReservationHandler#runRequest(CancelReservationType)
     */
    @Override
    public CancelReservationResponseType cancelReservation(
            final CancelReservationType request) throws SoapFault {
        
        /* Use the G2MPLS GW if enabled ------------------------------------- */
        if (Config.isTrue("g2mpls.forward.reservation")) {
            return this.client.cancelReservation(request);
        }
        /* ------------------------------------------------------------------ */

        return this.dummyHandler.cancelReservation(request);
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
     * (non-Javadoc)
     * 
     * @seeeu.ist_phosphorus.harmony.adapter.dummy.handler.IReservationHandler#
     * runRequest
     * (eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb
     * .CreateReservationType)
     */
    @Override
    public CreateReservationResponseType createReservation(
            final CreateReservationType request) throws SoapFault {

        /* Use the G2MPLS GW if enabled ------------------------------------- */
        if (Config.isTrue("g2mpls.forward.reservation")) {
            return this.client.createReservation(request);
        }
        /* ------------------------------------------------------------------ */

        return this.dummyHandler.createReservation(request);
    }

    /**
     * (non-Javadoc)
     * 
     * @seeeu.ist_phosphorus.harmony.adapter.dummy.handler.IReservationHandler#
     * runRequest
     * (eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb
     * .GetReservationsType)
     */
    @Override
    public GetReservationsResponseType getReservations(
            final GetReservationsType request) throws SoapFault {
        /* Use the G2MPLS GW if enabled ------------------------------------- */
        if (Config.isTrue("g2mpls.forward.reservation")) {
            return this.client.getReservations(request);
        }
        /* ------------------------------------------------------------------ */

        return this.dummyHandler.getReservations(request);
    }

    /**
     * (non-Javadoc)
     * 
     * @seeeu.ist_phosphorus.harmony.adapter.dummy.handler.IReservationHandler#
     * runRequest
     * (eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb
     * .GetStatusType)
     */
    @Override
    public GetStatusResponseType getStatus(final GetStatusType request)
        throws SoapFault {
        /* Use the G2MPLS GW if enabled ------------------------------------- */
        if (Config.isTrue("g2mpls.forward.reservation")) {
            return this.client.getStatus(request);
        }
        /* ------------------------------------------------------------------ */

        return this.dummyHandler.getStatus(request);
    }

    /**
     * (non-Javadoc)
     * 
     * @seeeu.ist_phosphorus.harmony.common.serviceinterface.reservation.
     * IReservationHandler
     * #runRequest(eu.ist_phosphorus.harmony.common.serviceinterface
     * .databinding.jaxb.IsAvailableType)
     */
    @Override
    public IsAvailableResponseType isAvailable(final IsAvailableType request) 
        throws SoapFault {
        /* Use the G2MPLS GW if enabled ------------------------------------- */
        if (Config.isTrue("g2mpls.forward.reservation")) {
            return this.dummyHandler.isAvailable(request);
            // Alex: disabled for a moment return this.client.isAvailable(request);
        }
        /* ------------------------------------------------------------------ */

        return this.dummyHandler.isAvailable(request);
    }
}
