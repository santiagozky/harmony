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


package server.argon;

import java.util.Date;

import org.apache.log4j.Logger;

import client.classes.Endpoint;
import client.classes.InternalLink;
import client.classes.Reservation;
import client.classes.Router;
import client.classes.Service;
import de.unibonn.viola.argon.requestProcessor.utils.StatusManager;
import de.unibonn.viola.argon.utils.jaxb.external.classes.IRes.ConnectionType;
import de.unibonn.viola.argon.utils.jaxb.external.classes.IRes.ReservationRequestType;
import de.unibonn.viola.argon.utils.jaxb.external.classes.IRes.ServiceType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.StatusType;
import org.opennaas.core.utils.Helpers;

/**
 * maps external argon information to common Gui-information.
 * 
 * @author steffen
 */
public class ArgonGWTMapper {

    /**
     * the lopg4j-Logger.
     */
    private static Logger log = Logger.getLogger(ArgonGWTMapper.class);

    /**
     * maps argon-status to phosphorus StatusType.
     * 
     * @param internalStatus
     *            the argon-status
     * @return StatusType of the NSP
     */
    private static StatusType convertStatus(final int internalStatus) {
        if (StatusManager.isAborted(internalStatus)
                || StatusManager.isRejected(internalStatus)) {
            return StatusType.CANCELLED_BY_SYSTEM;
        }

        if (StatusManager.isCanceled(internalStatus)) {
            return StatusType.CANCELLED_BY_USER;
        }

        if (StatusManager.isSuccessful(internalStatus)) {
            return StatusType.COMPLETED;
        }

        if (StatusManager.isEstablished(internalStatus)) {
            return StatusType.ACTIVE;
        }

        if (StatusManager.isTeardown(internalStatus)) {
            return StatusType.TEARDOWN_IN_PROGRESS;
        }

        if (StatusManager.isInSetup(internalStatus)) {
            return StatusType.SETUP_IN_PROGRESS;
        }

        if (StatusManager.isAccepted(internalStatus)
                || StatusManager.isReserved(internalStatus)
                || StatusManager.isScheduled(internalStatus)) {
            return StatusType.PENDING;
        }
        return StatusType.UNKNOWN;
    }

    /**
     * maps internal reservation-requests to common reservation class.
     * 
     * @param r
     *            the type coming from argon
     * @return the type used in the Gui. the common type
     */
    public static final Reservation argon2gwt(final ReservationRequestType r) {

        final Reservation commonReservation = new Reservation();
        String popup = "";

        // convert Status
        commonReservation.setStatus(ArgonGWTMapper.convertStatus(
                r.getReservationData().getStatus()).value());
        commonReservation.setId(String.valueOf(r.getReservationHandle()));
        popup += "Res-ID: " + r.getReservationHandle();
        popup +=
                ", Status: "
                        + ArgonGWTMapper.convertStatus(r.getReservationData()
                                .getStatus());

        if (r.getReservationData().getService().size() > 0) {
            for (final ServiceType s : r.getReservationData().getService()) {
                // to be filled
                final Service commonService = new Service();
                commonService.setStatus(ArgonGWTMapper.convertStatus(
                        s.getStatus()).value());
                commonService.setId(s.getServiceHandle());
                popup += " - Service-ID: " + s.getServiceHandle();
                popup +=
                        ", Status: "
                                + ArgonGWTMapper.convertStatus(s.getStatus())
                                        .value();

                // if connections are filled
                if (s.getConnection().size() > 0) {
                    // read in information from first connection
                    final ConnectionType con = s.getConnection().get(0);
                    commonService.setSource(con.getSourceAddress());
                    commonService.setDestination(con.getDestinationAddress());
                    popup += " - Con-Src: " + con.getSourceAddress();
                    popup += ", Con-Dest: " + con.getDestinationAddress();
                }

                long duration = 0;
                duration =
                        s.getTimeConstraint().getEndDateTime()
                                .toGregorianCalendar().getTimeInMillis()
                                - s.getTimeConstraint().getStartDateTime()
                                        .toGregorianCalendar()
                                        .getTimeInMillis();
                if (duration > 0) {
                    duration = duration / 1000;
                } else {
                    duration = 0;
                }

                // compute duration
                commonService.setDuration((int) duration);
                commonService.setStartTime(Helpers.xmlCalendarToDate(s
                        .getTimeConstraint().getStartDateTime()));
                popup += ", Duration: " + duration;
                popup +=
                        ", Start: "
                                + Helpers
                                        .xmlCalendarToDate(s
                                                .getTimeConstraint()
                                                .getStartDateTime());

                commonReservation.getServices().add(commonService);

            }
        }

        commonReservation.setPopup(popup);
        // ArgonGWTMapper.log.info("popup: " + popup);
        return commonReservation;
    }

    /**
     * maps internal information (i.e. router) to common model.
     * 
     * @param router
     *            the internal class
     * @return the common router class
     */
    public static final Router argon2GWT(
            final de.unibonn.viola.argon.persist.hibernate.classes.Router router) {

        final Router result = new Router();
        result.setConfigurationAddress(router.getConfigurationAddress());
        result.setLoopBackAddress(router.getLoopBackAddress());
        result.setValidTo(new Date(router.getValidTo().getTime()));
        result.setType(router.getType());

        return result;
    }

    public static final de.unibonn.viola.argon.persist.hibernate.classes.Router gwt2Argon(
            final Router router) {
        final de.unibonn.viola.argon.persist.hibernate.classes.Router result =
                new de.unibonn.viola.argon.persist.hibernate.classes.Router();
        result.setLoopBackAddress(router.getLoopBackAddress());
        result.setConfigurationAddress(router.getConfigurationAddress());
        result.setType(router.getType());
        result.setValidTo(router.getValidTo());
        // TODO

        return result;
    }

    /**
     * TODO maps internal information (i.e. link) to common model.
     * 
     * @param link
     *            the internal class
     * @return the common Link class
     */
    public static final InternalLink argon2GWT(
            final de.unibonn.viola.argon.persist.hibernate.classes.InternalLink link) {

        String popup = "";
        final InternalLink result = new InternalLink();

        result.setBandwidth(link.getBandwidth().longValue());
        result.setDelay(link.getDelay());
        result.setDestinationNode(link.getDestinationNode());
        result.setIngoingInterface(link.getIngoingInterface());
        result.setUp(link.getUp());
        result.setOutgoingInterface(link.getOutgoingInterface());
        result.setSourceNode(link.getSourceNode());
        // result.setUniqueLabel(link.getUniqueLabel());
        result.setValidFrom(new Date(link.getValidFrom().getTime()));
        result.setValidTo(new Date(link.getValidTo().getTime()));

        popup += "Label: " + link.getUniqueLabel();
        popup += ", Source: " + link.getSourceNode();
        popup += ", OutgoingIntf: " + link.getOutgoingInterface();
        popup += ", Destination: " + link.getDestinationNode();
        popup += ", IngoingIntf: " + link.getIngoingInterface();
        popup += ", Bandwidth: " + link.getBandwidth();
        popup += ", Delay: " + link.getDelay();
        // popup += ", IsEndpoint: " + link.getIsEndpoint();
        popup += ", IsUp: " + link.getUp();
        popup += ", validFrom: " + link.getValidFrom().toString();
        popup += ", validTo: " + link.getValidTo().toString();
        result.setPopup(popup);

        return result;
    }

    public static final de.unibonn.viola.argon.persist.hibernate.classes.InternalLink gwt2Argon(
            final InternalLink link) {
        final de.unibonn.viola.argon.persist.hibernate.classes.InternalLink result =
                new de.unibonn.viola.argon.persist.hibernate.classes.InternalLink();

        result.setBandwidth(link.getBandwidth());
        result.setDelay(link.getDelay());
        result.setDestinationNode(link.getDestinationNode());
        result.setIngoingInterface(link.getIngoingInterface());
        // result.setIsEndpoint(link.getIsEndpoint());
        result.setUp(link.isUp());
        // result.setLinkUp(link.getLinkUp());
        result.setOutgoingInterface(link.getOutgoingInterface());
        result.setSourceNode(link.getSourceNode());
        result.setValidFrom(link.getValidFrom());
        result.setValidTo(link.getValidTo());

        return result;
    }

    /**
     * maps internal information (i.e. link) to common model.
     * 
     * @param link
     *            the internal class
     * @return the common Link class
     */
    public static final Endpoint argon2GWT(
            final de.unibonn.viola.argon.persist.hibernate.classes.Endlink link) {

        String popup = "";
        final Endpoint result = new Endpoint();

        result.setBandwidth(link.getBandwidth());
        result.setDelay(link.getDelay());
        result.setDestinationNode(link.getDestinationNode());
        result.setIngoingInterface(link.getIngoingInterface());
        result.setUp(link.getUp());
        result.setOutgoingInterface(link.getOutgoingInterface());
        result.setSourceNode(link.getSourceNode());
        // result.setUniqueLabel(link.getUniqueLabel());
        result.setValidFrom(new Date(link.getValidFrom().getTime()));
        result.setValidTo(new Date(link.getValidTo().getTime()));

        popup += "Label: " + link.getUniqueLabel();
        popup += ", Source: " + link.getSourceNode();
        popup += ", OutgoingIntf: " + link.getOutgoingInterface();
        popup += ", Destination: " + link.getDestinationNode();
        popup += ", IngoingIntf: " + link.getIngoingInterface();
        popup += ", Bandwidth: " + link.getBandwidth();
        popup += ", Delay: " + link.getDelay();
        // popup += ", IsEndpoint: " + link.getIsEndpoint();
        popup += ", IsUp: " + link.getUp();
        popup += ", validFrom: " + link.getValidFrom().toString();
        popup += ", validTo: " + link.getValidTo().toString();
        result.setPopup(popup);

        return result;
    }

    public static final de.unibonn.viola.argon.persist.hibernate.classes.Endlink gwt2Argon(
            final Endpoint link) {
        final de.unibonn.viola.argon.persist.hibernate.classes.Endlink result =
                new de.unibonn.viola.argon.persist.hibernate.classes.Endlink();

        result.setBandwidth(link.getBandwidth());
        result.setDelay(link.getDelay());
        result.setDestinationNode(link.getDestinationNode());
        result.setIngoingInterface(link.getIngoingInterface());
        // result.setIsEndpoint(link.getIsEndpoint());
        result.setUp(link.isUp());
        // result.setLinkUp(link.getLinkUp());
        result.setOutgoingInterface(link.getOutgoingInterface());
        result.setSourceNode(link.getSourceNode());
        result.setValidFrom(link.getValidFrom());
        result.setValidTo(link.getValidTo());

        return result;
    }

}
