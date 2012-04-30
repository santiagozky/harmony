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


package org.opennaas.extensions.idb.notification;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import org.opennaas.core.utils.Config;
import org.opennaas.core.utils.PerformanceLogLevel;
import org.opennaas.core.utils.PhLogger;
import org.opennaas.extensions.idb.database.hibernate.Subscription;
import org.opennaas.extensions.idb.exception.database.DatabaseException;
import org.opennaas.extensions.idb.notification.producer.NotificationProducerHandler;

public class NotificationInitiator implements ServletContextListener {

    public void contextDestroyed(final ServletContextEvent arg0) {
        // TODO Auto-generated method stub
    }

    public void contextInitialized(final ServletContextEvent arg0) {
        final Logger performanceLogger = PhLogger.getLogger("Performance");
        // only check DB if the persistingSubscriptions-flag is set
        if (Config.isTrue("idb", "persistingSubscriptions")) {
            System.out.println("Dating up from DB ...");
            try {
                // create new NotificationProducer
                final NotificationProducerHandler producer = NotificationProducerHandler
                        .getInstance();

                final HashMap<String, Vector<String>> subscriptions = new HashMap<String, Vector<String>>();
                // get all available subscriptions
                final List<Subscription> subscriptionsFromDB = Subscription
                        .getAllSubscriptions();
                for (final Subscription sub : subscriptionsFromDB) {
                    final String topic = sub.getSubscriptionTopic();
                    final String epr = sub.getSubscriptionEPR();

                    // create new topic-entry
                    if (!subscriptions.containsKey(topic)) {
                        subscriptions.put(topic, new Vector<String>());
                    }
                    // add the subscription-entry
                    subscriptions.get(topic).add(epr);
                    }

                producer.initializeSubscriptions(subscriptions);
            } catch (final DatabaseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("... finished! Notification-WS up!");
        }
        performanceLogger
                .log(PerformanceLogLevel.PERFORMANCE_LOG, "bliblablub");
        System.out.println("Notification-WS up!");
    }
}
