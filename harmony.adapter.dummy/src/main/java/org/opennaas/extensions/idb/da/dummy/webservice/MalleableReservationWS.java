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


package org.opennaas.extensions.idb.da.dummy.webservice;

import org.apache.muse.ws.addressing.soap.SoapFault;
import org.w3c.dom.Element;

import org.opennaas.extensions.idb.da.dummy.handler.MalleableReservationHandler;
import org.opennaas.extensions.idb.serviceinterface.reservation.IReservationWS;

/**
 * Dummy Reservation Webservice for Malleable Reservations.
 * 
 * @author Alexander Zimmermann (zimmerm2@cs.uni-bonn.de)
 */
public class MalleableReservationWS implements IReservationWS {

    /**
     * Default constructor that initializes the ContextListener.
     */
    public MalleableReservationWS() {
        // no registration needed
        // if (AbstractTopologyRegistrator.getLatestInstance() == null) {
        // new ContextListener();
        // }
    }

    /**
     * activate Handler.
     * 
     * @param activate
     *            Request
     * @return activate Response
     * @throws SoapFault
     *             In case of errors
     */
    public final Element activate(final Element activate) throws SoapFault {
        return MalleableReservationHandler.getInstance().handle(activate,
                "activate");
    }

    /**
     * bind Handler.
     * 
     * @param bind
     *            Request
     * @return bind Response
     * @throws SoapFault
     *             In case of errors
     */
    public final Element bind(final Element bind) throws SoapFault {
        return MalleableReservationHandler.getInstance().handle(bind, "bind");
    }

    /**
     * cancelJob Handler.
     * 
     * @param cancelJob
     *            Request
     * @return cancelJob Response
     * @throws SoapFault
     *             In case of errors
     */
    public final Element cancelJob(final Element cancelJob) throws SoapFault {
        return MalleableReservationHandler.getInstance().handle(cancelJob,
                "cancelJob");
    }

    /**
     * cancelReservation Handler.
     * 
     * @param cancelReservation
     *            Request
     * @return cancelReservation Response
     * @throws SoapFault
     *             In case of errors
     */
    public final Element cancelReservation(final Element cancelReservation)
            throws SoapFault {
        return MalleableReservationHandler.getInstance().handle(
                cancelReservation, "cancelReservation");
    }

    /**
     * completeJob Handler.
     * 
     * @param completeJob
     *            Request
     * @return completeJob Response
     * @throws SoapFault
     *             In case of errors
     */
    public final Element completeJob(final Element completeJob)
            throws SoapFault {
        return MalleableReservationHandler.getInstance().handle(completeJob,
                "completeJob");
    }

    /**
     * createReservation Handler.
     * 
     * @param createReservation
     *            Request
     * @return createReservation Response
     * @throws SoapFault
     *             In case of errors
     */
    public final Element createReservation(final Element createReservation)
            throws SoapFault {
        return MalleableReservationHandler.getInstance().handle(
                createReservation, "createReservation");
    }

    /**
     * getFeatures Handler.
     * 
     * @param getFeatures
     *            Request
     * @return getFeatures Response
     * @throws SoapFault
     *             In case of errors
     */
    public final Element getFeatures(final Element getFeatures)
            throws SoapFault {
        return MalleableReservationHandler.getInstance().handle(getFeatures,
                "getFeatures");
    }

    /**
     * getReservation Handler.
     * 
     * @param getReservation
     *            Request
     * @return getReservation Response
     * @throws SoapFault
     *             In case of errors
     */
    public final Element getReservation(final Element getReservation)
            throws SoapFault {
        return MalleableReservationHandler.getInstance().handle(
                getReservation, "getReservation");
    }

    /**
     * getReservations Handler.
     * 
     * @param getReservations
     *            Request
     * @return getReservations Response
     * @throws SoapFault
     *             In case of errors
     */
    public final Element getReservations(final Element getReservations)
            throws SoapFault {
        return MalleableReservationHandler.getInstance().handle(
                getReservations, "getReservations");
    }

    /**
     * getStatus Handler.
     * 
     * @param getStatus
     *            Request
     * @return getStatus Response
     * @throws SoapFault
     *             In case of errors
     */
    public final Element getStatus(final Element getStatus) throws SoapFault {
        return MalleableReservationHandler.getInstance().handle(getStatus,
                "getStatus");
    }

    /**
     * isAvailable Handler.
     * 
     * @param isAvailable
     *            Request
     * @return isAvailable Response
     * @throws SoapFault
     *             In case of errors
     */
    public final Element isAvailable(final Element isAvailable)
            throws SoapFault {
        return MalleableReservationHandler.getInstance().handle(isAvailable,
                "isAvailable");
    }

    /**
     * notification Handler.
     * 
     * @param notification
     *            Request
     * @return notification Response
     * @throws SoapFault
     *             In case of errors
     */
    public final Element notification(final Element notification)
            throws SoapFault {
        return MalleableReservationHandler.getInstance().handle(notification,
                "notification");
    }

}
