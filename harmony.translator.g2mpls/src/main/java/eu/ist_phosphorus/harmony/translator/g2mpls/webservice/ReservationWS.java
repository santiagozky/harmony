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


package eu.ist_phosphorus.harmony.translator.g2mpls.webservice;

import org.apache.muse.core.AbstractCapability;
import org.apache.muse.ws.addressing.soap.SoapFault;
import org.w3c.dom.Element;

import eu.ist_phosphorus.harmony.common.serviceinterface.reservation.IReservationWS;
import eu.ist_phosphorus.harmony.common.serviceinterface.topology.registrator.AbstractTopologyRegistrator;
import eu.ist_phosphorus.harmony.translator.g2mpls.handler.ReservationHandler;

/**
 * Dummy Reservation Webservice.
 * 
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 */
public class ReservationWS extends AbstractCapability implements IReservationWS {

    /**
     * Default constructor that initializes the ContextListener.
     */
    public ReservationWS() {
        if (AbstractTopologyRegistrator.getLatestInstance() == null) {
            new ContextListener();
        }
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
    public Element activate(final Element activate) throws SoapFault {
        return ReservationHandler.getInstance().handle(activate, "activate");
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
    public Element bind(final Element bind) throws SoapFault {
        return ReservationHandler.getInstance().handle(bind, "bind");
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
    public Element cancelJob(final Element cancelJob) throws SoapFault {
        return ReservationHandler.getInstance().handle(cancelJob, "cancelJob");
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
    public Element cancelReservation(final Element cancelReservation)
            throws SoapFault {
        return ReservationHandler.getInstance().handle(cancelReservation,
                "cancelReservation");
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
    public Element completeJob(final Element completeJob) throws SoapFault {
        return ReservationHandler.getInstance().handle(completeJob,
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
    public Element createReservation(final Element createReservation)
            throws SoapFault {
        return ReservationHandler.getInstance().handle(createReservation,
                "createReservation");
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
    public Element getFeatures(final Element getFeatures) throws SoapFault {
        return ReservationHandler.getInstance().handle(getFeatures,
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
    public Element getReservation(final Element getReservation)
            throws SoapFault {
        return ReservationHandler.getInstance().handle(getReservation,
                "getReservation");
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
    public Element getReservations(final Element getReservations)
            throws SoapFault {
        return ReservationHandler.getInstance().handle(getReservations,
                "getReservations");
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
    public Element getStatus(final Element getStatus) throws SoapFault {
        return ReservationHandler.getInstance().handle(getStatus, "getStatus");
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
    public Element isAvailable(final Element isAvailable) throws SoapFault {
        return ReservationHandler.getInstance().handle(isAvailable,
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
    public Element notification(final Element notification) throws SoapFault {
        return ReservationHandler.getInstance().handle(notification,
                "notification");
    }

}