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


package org.opennaas.extensions.idb.da.thin.handler;

import org.apache.log4j.Logger;

import org.opennaas.extensions.idb.da.thin.persistence.DbManager;
import org.opennaas.extensions.idb.da.thin.persistence.orm.GmplsConnection;
import org.opennaas.extensions.idb.da.thin.impl.StatusHandler;
import org.opennaas.extensions.idb.da.thinutils.Notifications;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.NotificationMessageType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.NotificationResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.NotificationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;
import org.opennaas.extensions.idb.serviceinterface.notification.CommonNotificationHandler;
import org.opennaas.core.utils.NotificationTopic;
import org.opennaas.core.utils.PhLogger;

/**
 * @author Stephan Wagner (stephan.wagner@iais.fraunhofer.de), Daniel Beer
 *         (daniel.beer@iais.fraunhofer.de)
 */
public final class NotificationHandler extends CommonNotificationHandler {
    /** Singleton instance. */
    private static NotificationHandler selfInstance = null;
    private static Logger logger = null;

    /**
     * Instance getter.
     *
     * @return Singleton Instance
     */
    public static NotificationHandler getInstance() {
        if (selfInstance == null) {
            selfInstance = new NotificationHandler();
        }
        return selfInstance;
    }

    /**
     * Private constructor: Singleton.
     */
    private NotificationHandler() {
        logger = PhLogger.getLogger(this.getClass());
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
     * Handles all kinds of notifications
     *
     * @param notification
     *                the incoming notification
     * @return response as GetStatusResponseType
     * @throws UnexpectedFaultException
     *                 should not happen
     */
    public NotificationResponseType notification(
            final NotificationType notification)
            throws UnexpectedFaultException {
        logger.debug("notification received");
        NotificationTopic notificationTopic =
                new NotificationTopic(notification.getTopic());
        for (NotificationMessageType notificationMessage : notification
                .getNotificationList()) {
            logger.debug("lspId is: " + notificationTopic.getReservationId());
            if (notificationMessage.isSetServiceStatus()) {

                logger.debug("status is: "
                        + notificationMessage.getServiceStatus().getStatus());

                GmplsConnection gc =
                        DbManager.reloadConnectionByPathId(new Long(
                                notificationTopic.getReservationId())
                                .intValue());
                logger.debug("gmplsConnection reloaded");
                gc
                        .setStatus(notificationMessage.getServiceStatus()
                                .getStatus());
                logger.debug("status set for gmpls Connection");
                logger.debug("details for gmpls Connection \n reservationId: "
                        + gc.getReservationId() + " \n serviceId: "
                        + gc.getServiceId() + "\n connectionId: "
                        + gc.getConnectionId() + " \n Status: "
                        + gc.convertStatus());
                DbManager
                        .updateStatus(gc, "Status was changed by notification");
                Notifications.publish(gc.getReservationId(), StatusHandler
                        .getStatusOfService(gc.getReservationId(), gc
                                .getServiceId(), false));
            }
        }
        return new NotificationResponseType();
    }
}
