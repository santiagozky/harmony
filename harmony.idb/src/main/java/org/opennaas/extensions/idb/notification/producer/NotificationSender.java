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


package org.opennaas.extensions.idb.notification.producer;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.muse.ws.addressing.soap.SoapFault;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.NotificationMessageType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.NotificationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.PublishType;
import org.opennaas.extensions.idb.serviceinterface.notification.SimpleNotificationClient;
import org.opennaas.extensions.idb.serviceinterface.reservation.SimpleReservationClient;
import org.opennaas.core.utils.PhLogger;

public class NotificationSender extends Thread {

    /** Logger. */
    private final Logger logger = PhLogger.getLogger(this.getClass());

    /**
     * Proxy for the consumerClient.
     */
    private SimpleReservationClient consumerClient;

    /**
     * Proxy for the notificationClient.
     */
    private SimpleNotificationClient notificationClient;

    /**
     * flag if message should be a notificationType or a publishType if true ->
     * notificationType if false -> publishType
     */
    private final boolean messageTypeFlag;

    /**
     * notificationType to be sent.
     */
    private NotificationType notificationType;

    /**
     * publishType to be sent.
     */
    private PublishType publishType;

    /**
     * monitor for the thread
     */
    private final NotificationSenderMonitor monitor;

    /**
     * Constructor for the notification Sender thread.
     * 
     * @param epr
     *            consumer- or notificationClient-EPR, according to
     *            messageTypeFlag
     * @param topic
     *            topic to be sent
     * @param message
     *            message to be sent
     * @param messageTypeFlag
     *            flag if message should be a notificationType or a publishType
     */
    public NotificationSender(final String epr, final String topic,
            final List<NotificationMessageType> message,
            final boolean messageTypeFlag,
            final NotificationSenderMonitor monitor) {
        this.monitor = monitor;
        this.messageTypeFlag = messageTypeFlag;

        if (this.messageTypeFlag) {
            this.notificationType = new NotificationType();
            this.notificationType.setTopic(topic);
            this.notificationType.getNotificationList().addAll(message);

            this.consumerClient = new SimpleReservationClient(epr);
        } else {
            this.publishType = new PublishType();
            this.publishType.setTopic(topic);
            this.publishType.getNotificationList().addAll(message);

            this.notificationClient = new SimpleNotificationClient(epr);
        }
    }

    /**
     * getter for EPR
     * 
     * @return notification- or consumer-epr
     */
    public String getEpr() {
        if (this.messageTypeFlag) {
            return this.consumerClient.getEndpointReference().getAddress()
                    .toString();
        }
        return this.notificationClient.getEndpointReference().getAddress()
                .toString();
    }

    /**
     * getter for messages
     * 
     * @return notification-messages
     */
    public List<NotificationMessageType> getMessage() {
        if (this.messageTypeFlag) {
            return this.notificationType.getNotificationList();
        }
        return this.publishType.getNotificationList();
    }

    /**
     * getter for messageTypeFlag
     * 
     * @return messageTypeFlag
     */
    public boolean getMessageTypeFlag() {
        return this.messageTypeFlag;
    }

    /**
     * getter for monitor
     * 
     * @return monitor
     */
    public NotificationSenderMonitor getMonitor() {
        return this.monitor;
    }

    /**
     * getter for topic
     * 
     * @return topic
     */
    public String getTopic() {
        if (this.messageTypeFlag) {
            return this.notificationType.getTopic();
        }
        return this.publishType.getTopic();
    }

    @Override
    public void run() {
        if (this.messageTypeFlag) {
            try {
                this.consumerClient.notification(this.notificationType);
            } catch (final SoapFault e) {
                this.logger.warn("sending notification to "
                        + this.consumerClient.getDestination().getAddress()
                        + " failed! " + e.getMessage());
            }
        } else {
            try {
                this.notificationClient.publish(this.publishType);
            } catch (final SoapFault e) {
                this.logger.warn("publishing to "
                        + this.notificationClient.getDestination().getAddress()
                        + " failed! " + e.getMessage());
            }
        }

        // report finish status to monitor, if active
        if (this.monitor != null) {
            this.monitor.deregisterSender(this);
        }
    }
}
