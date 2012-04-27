package org.opennaas.extensions.gmpls.utils;

import org.apache.log4j.Logger;
import org.apache.muse.ws.addressing.soap.SoapFault;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AddTopicType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ConnectionStatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DomainStatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointInterfaceType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.NotificationMessageType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.PublishType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.RemoveTopicType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ServiceStatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.StatusType;
import org.opennaas.extensions.idb.serviceinterface.notification.SimpleNotificationClient;
import org.opennaas.core.utils.Config;
import org.opennaas.core.utils.NotificationTopic;
import org.opennaas.core.utils.PhLogger;

public class Notifications {
    private static Logger logger = PhLogger.getLogger(Notifications.class);
    private static String domainName = Config.getString("gmpls", "domain.name");

    public static void publish(final long reservationId, final StatusType st) {
        PublishType pt = new PublishType();
        NotificationMessageType notification = new NotificationMessageType();
        ServiceStatusType statusType = new ServiceStatusType();
        DomainStatusType dt = new DomainStatusType();
        ConnectionStatusType cst = new ConnectionStatusType();
        dt.setDomain(domainName);
        dt.setStatus(st);
        EndpointType ept = new EndpointType();
        ept.setEndpointId("123.123.123.123");
        ept.setDomainId("dummy");
        ept.setName("dummy");
        ept.setDescription("dummy");
        ept.setBandwidth(Integer.valueOf(0));
        ept.setInterface(EndpointInterfaceType.USER);
        cst.setActualBW(0);
        cst.setConnectionID(0);
        cst.setDirectionality(0);
        cst.setStatus(st);
        cst.setSource(ept);
        cst.getTarget().add(ept);
        statusType.getDomainStatus().add(dt);
        statusType.getConnections().add(cst);
        statusType.setStatus(st);
        pt
                .setTopic(new NotificationTopic(domainName, reservationId)
                        .toString());
        notification.setServiceStatus(statusType);
        pt.getNotificationList().add(notification);

        SimpleNotificationClient notificationClient =
                new SimpleNotificationClient(Config.getString("gmpls",
                        "epr.gmplsNWS"));
        try {
            logger.debug("publish status: " + statusType.getStatus()
                    + " on topic: " + pt.getTopic());
            notificationClient.publish(pt);
        } catch (SoapFault e) {
            logger.debug(e.getMessage(), e);
        }
    }

    public static void removeTopic(final long reservationId) {
        RemoveTopicType req = new RemoveTopicType();
        req.setTopic(new NotificationTopic(domainName, reservationId)
                .toString());
        SimpleNotificationClient notificationClient =
                new SimpleNotificationClient(Config.getString("gmpls",
                        "epr.gmplsNWS"));
        try {
            logger.debug("removing Topic: " + req.getTopic());
            notificationClient.removeTopic(req);
        } catch (SoapFault e) {
            logger.debug(e.getMessage(), e);
        }

    }

    public static void addTopic(final long reservationId) {
        SimpleNotificationClient notificationClient =
                new SimpleNotificationClient(Config.getString("gmpls",
                        "epr.gmplsNWS"));
        AddTopicType topicType = new AddTopicType();
        topicType.setTopic(new NotificationTopic(domainName, reservationId)
                .toString());

        try {
            logger.debug("adding Topic: " + topicType.getTopic());
            notificationClient.addTopic(topicType);
        } catch (SoapFault e) {
            logger.debug(e.getMessage(), e);
        }
    }
}
