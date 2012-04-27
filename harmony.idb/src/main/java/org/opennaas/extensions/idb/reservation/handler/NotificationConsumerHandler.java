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


/*******************************************************************************
 * =============================================================================
 * Copyright 2006 The Apache Software Foundation Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * =============================================================================
 */

package org.opennaas.extensions.idb.reservation.handler;

import java.util.MissingResourceException;

import org.apache.log4j.Logger;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ConnectionStatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.NotificationMessageType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.NotificationResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.NotificationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.StatusType;
import org.opennaas.core.utils.Config;
import org.opennaas.core.utils.NotificationTopic;
import org.opennaas.core.utils.PhLogger;
import org.opennaas.extensions.idb.Constants;
import org.opennaas.extensions.idb.database.hibernate.NrpsConnections;
import org.opennaas.extensions.idb.database.hibernate.VIEW_ReservationMapping;
import org.opennaas.extensions.idb.exception.database.DatabaseException;
import org.opennaas.extensions.idb.notification.producer.NotificationSender;

public class NotificationConsumerHandler implements INotificationConsumer {

    /** Singleton Instance. */
    private static NotificationConsumerHandler selfInstance = null;

    /**
     * Instance getter.
     * 
     * @return Singleton Instance
     */
    public static NotificationConsumerHandler getInstance() {
        if (NotificationConsumerHandler.selfInstance == null) {
            NotificationConsumerHandler.selfInstance = new NotificationConsumerHandler();
        }
        return NotificationConsumerHandler.selfInstance;
    }

    /** Logger instance. */
    Logger logger = PhLogger.getLogger(this.getClass());

    /** Private constructor: Singleton. */
    private NotificationConsumerHandler() {
        // Nothing to do here...
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
     * method to hand-off the notification to superior IDB
     * 
     * @param message
     *            the notification
     * @param resID
     *            the reservation-ID of this IDB-instance
     */
    private void handOffNotification(final NotificationType message,
            final long resID) {
        // create the new topic according to this IDB-instance and actual
        // reservationID
        final NotificationTopic topic = new NotificationTopic(Config.getString(
                Constants.hsiProperties, "domain.name"), resID);

        String epr;
        // get notification-WS EPR
        try {
            epr = Config.getString(Constants.hsiProperties, "domain.notificationEPR");
        } catch (final MissingResourceException e) {
            // no notification-WS declared in config therefore return
            this.logger.info("no notification-WS for hand-off declared");
            return;
        }

        // start thread to hand-off notification
        final NotificationSender senderThread = new NotificationSender(epr,
                topic.toString(), message.getNotificationList(), false, null);
        senderThread.start();
    }

    /**
     * implemented notification method
     */
    public NotificationResponseType notification(final NotificationType message) {
        this.logger.debug("Got Message from Topic: " + message.getTopic());
        final NotificationTopic topic = new NotificationTopic(message
                .getTopic());
        if (message.getNotificationList().size() == 0) {
            this.logger.debug("Message empty!");
        } else {
            // the notification has to be processed for every connection in it,
            // because the status is stored in the according NRPS-Connections
            for (final NotificationMessageType nmt : message
                    .getNotificationList()) {
                for (final ConnectionStatusType cst : nmt.getServiceStatus()
                        .getConnections()) {
                    this.logger.debug("Message for Domain: "
                            + topic.getDomainName() + "\n Reservation-ID: "
                            + topic.getReservationId() + " | Service-ID: "
                            + nmt.getServiceStatus().getServiceID()
                            + " | Connection-ID: " + cst.getConnectionID());
                    try {
                        // updateStatus in DB and hand-off notification
                        this.updateStatus(topic.getReservationId(), nmt
                                .getServiceStatus().getServiceID(), cst
                                .getConnectionID(), topic.getDomainName(), cst
                                .getStatus(), message);
                        this.logger.debug("updated status-info!");
                    } catch (final DatabaseException e) {
                        this.logger
                                .error("DatabaseException while updating status from notification!");
                        e.printStackTrace();
                    }
                }
            }
        }

        final NotificationResponseType response = new NotificationResponseType();
        return response;
    }

    /**
     * method to update the DB and hand off the notification if possible
     * 
     * @param reservationId
     *            NRPS-reservationID got through notification
     * @param serviceID
     *            overall service-ID
     * @param connectionID
     *            overall connection-ID
     * @param domain
     *            domain-name of the NRPS
     * @param status
     *            new status to be set
     * @param message
     *            the whole notification to hand-off
     * @throws DatabaseException
     */
    private void updateStatus(final long reservationId, final int serviceID,
            final int connectionID, final String domain,
            final StatusType status, final NotificationType message)
            throws DatabaseException {
        long idbReservationID = 0;
        // get all mappings for the NRPS-ReservationID
        for (final VIEW_ReservationMapping mapping : VIEW_ReservationMapping
                .getMappingsForNRPSResID(reservationId)) {
            // check if mapping is the one expected more exactly
            if ((mapping.getServiceId() == serviceID)
                    && (mapping.getConnectionId() == connectionID)
                    && mapping.getDomain().equals(domain)) {
                // save the IDB-reservationID for later hand-off
                idbReservationID = mapping.getReservationId();
                // load according nrpsConnection from DB
                final NrpsConnections nrpsConnection = NrpsConnections
                        .load(mapping.getNrpsConnection().getPkNrpsConnection());
                // update status
                nrpsConnection.setStatus(status);
                // save changes to DB
                nrpsConnection.save();
            }
        }
        // if there was a reservation to update hand-off notification to other
        // IDB's
        if (idbReservationID > 0) {
            this.handOffNotification(message, idbReservationID);
        }
    }
}
