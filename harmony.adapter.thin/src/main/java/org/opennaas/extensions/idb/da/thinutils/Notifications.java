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


package org.opennaas.extensions.idb.da.thinutils;

import org.apache.log4j.Logger;
import org.apache.muse.ws.addressing.soap.SoapFault;

import org.opennaas.extensions.idb.da.thin.webservice.ContextListener;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AddTopicType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.NotificationMessageType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.PublishType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.RemoveTopicType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ServiceStatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.SubscribeType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.InvalidReservationIDFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.utils.WebserviceUtils;
import org.opennaas.extensions.idb.serviceinterface.notification.SimpleNotificationClient;
import org.opennaas.core.utils.Config;
import org.opennaas.core.utils.NotificationTopic;
import org.opennaas.core.utils.PhLogger;

public class Notifications {

    private static Logger logger = PhLogger.getLogger(Notifications.class);

    private static SimpleNotificationClient notificationProducerThin = new SimpleNotificationClient(
	    Config.getString("hsi", "domain.notificationEPR"));
    private static SimpleNotificationClient notificationClient = new SimpleNotificationClient(
	    Config.getString("adapter", "epr.gmplsNWS"));
    private static String domainName = Config.getString("hsi", "domain.name");
    private static String gmplsDomainName = Config.getString("adapter",
	    "domain.name");

    public static void addTopic(final long reservationId) {

	AddTopicType topicType = new AddTopicType();
	topicType.setTopic(makeTopic(reservationId));
	try {
	    logger.debug("adding Topic: " + topicType.getTopic());
	    notificationProducerThin.addTopic(topicType);
	} catch (SoapFault e) {
	    logger.debug(e.getMessage(), e);

	}
    }

    public static void addTopic(final String reservationId)
	    throws InvalidReservationIDFaultException {
	addTopic(WebserviceUtils.convertReservationID(reservationId));
    }

    public static void removeTopic(final long reservationId) {
	RemoveTopicType req = new RemoveTopicType();
	req.setTopic(makeTopic(reservationId));
	try {
	    logger.debug("removing Topic: " + req.getTopic());
	    notificationProducerThin.removeTopic(req);
	} catch (SoapFault e) {
	    logger.debug(e.getMessage(), e);
	}

    }

    public static void subscribe(final long reservationId) {
	SubscribeType subscribe = new SubscribeType();
	subscribe
		.setTopic(new NotificationTopic(gmplsDomainName, reservationId)
			.toString());
	subscribe.setConsumerReference(Config.getString(
		ContextListener.interdomainPropertyFile,
		"domain.reservationEPR"));
	try {
	    logger.debug("subscribing to Topic: " + subscribe.getTopic());
	    logger.debug("subscribing with consumer ref: "
		    + subscribe.getConsumerReference());
	    notificationClient.subscribe(subscribe);
	} catch (SoapFault e) {
	    logger.debug(e.getMessage(), e);
	}
    }

    public static void publish(final long reservationId,
	    ServiceStatusType statusType) {
	PublishType pt = new PublishType();
	NotificationMessageType notification = new NotificationMessageType();
	pt.setTopic(makeTopic(reservationId));
	ServiceStatusType sst = new ServiceStatusType();
	sst.setServiceID(statusType.getServiceID());
	sst.setStatus(statusType.getStatus());
	sst.getConnections().addAll(statusType.getConnections());
	sst.getDomainStatus().addAll(statusType.getDomainStatus());
	notification.setServiceStatus(sst);
	pt.getNotificationList().add(notification);
	try {
	    logger.debug("publish status: " + statusType.getStatus()
		    + " on topic: " + pt.getTopic());
	    notificationProducerThin.publish(pt);
	} catch (SoapFault e) {
	    logger.debug(e.getMessage(), e);
	}
    }

    private static String makeTopic(final long reservationId) {
	return new NotificationTopic(domainName, reservationId).toString();
    }

}
