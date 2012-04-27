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

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AddTopicResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AddTopicType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetTopicsResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetTopicsType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.NotificationMessageType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.PublishResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.PublishType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.RemoveTopicResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.RemoveTopicType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.TopicNotFoundFaultException;
import org.opennaas.core.utils.Config;
import org.opennaas.core.utils.PhLogger;
import org.opennaas.extensions.idb.database.hibernate.Subscription;
import org.opennaas.extensions.idb.exception.database.DatabaseException;

public class NotificationProducerHandler {

    public abstract class DBHandler extends Thread {
        private final String topic;
        private final String epr;

        public DBHandler(final String topicParam, final String eprParam) {
            this.topic = topicParam;
            this.epr = eprParam;
        }

        @Override
        public void run() {
            this.toDo(this.topic, this.epr);
        }

        abstract protected void toDo(String topic, String epr);
    }

    /** Singleton Instance. */
    private static NotificationProducerHandler selfInstance = null;

    /**
     * Instance getter.
     * 
     * @return Singleton Instance
     */
    public static NotificationProducerHandler getInstance() {
        if (NotificationProducerHandler.selfInstance == null) {
            NotificationProducerHandler.selfInstance = new NotificationProducerHandler();
        }
        return NotificationProducerHandler.selfInstance;
    }

    /** subscriptions grouped by topics. */
    private final HashMap<String, Vector<String>> subscriptions;

    /** Logger. */
    private final Logger logger = PhLogger.getLogger(this.getClass());

    /** flag if subscriptions should be persisted. */
    private final boolean persistSubscription;

    /** Private constructor: Singleton. */
    private NotificationProducerHandler() {
        this.subscriptions = new HashMap<String, Vector<String>>();
        // Create a default TestTopic, for which everyone can subscribe
        this.subscriptions.put("TestTopic", new Vector<String>());
        // check persisting-flag
        this.persistSubscription = Config.isTrue("idb",
                "persistingSubscriptions");
    }

    /**
     * addSubscription-method for internal queries
     * 
     * @param addSubscription
     *            Request
     * @return addSubscription response
     * @throws TopicNotFoundFaultException
     */
    public boolean addSubscription(final String epr, final String topic)
            throws TopicNotFoundFaultException {
        // check if topic is existent
        if (!this.subscriptions.containsKey(topic)) {
            throw new TopicNotFoundFaultException("Topic " + topic
                    + " is not existent!");
        }

        // save topic local
        boolean resultLocal = this.subscriptions.get(topic).add(epr);
        this.logger.debug("subscribed " + epr + " on topic " + topic);

        // if local adding not worked, return false
        if (!resultLocal) {
            this.logger.error("subscribing " + epr + " on topic " + topic
                    + " failed!");
            return resultLocal;
        }

        if (this.persistSubscription) {
            // try to save the new subscription in DB
            final DBHandler handler = new DBHandler(topic, epr) {
                @Override
                protected void toDo(String topic, String epr) {
                    boolean resultDB = true;
                    Subscription subscription = null;
                    try {
                        // check if this subscription is already persistent
                        subscription = Subscription
                                .getSubscriptionForTopicAndEPR(topic, epr);
                    } catch (DatabaseException e) {
                        // nothing here, try to save a new Subscription
                    }

                    if (subscription == null) {
                        // Subscription till now not persistent -> save it
                        subscription = new Subscription(topic, epr);
                        try {
                            subscription.save();
                        } catch (DatabaseException e) {
                            resultDB = false;
                        }
                    }

                    if (!resultDB) {
                        Logger logger = PhLogger.getLogger(this.getClass());
                        logger.debug("subscribed " + epr + " on topic " + topic
                                + " , but not persistent in DB!");
                    }
                }
            };
            handler.start();
        }

        return resultLocal;
    }

    /**
     * addTopic-method for WS-queries
     * 
     * @param addTopic
     *            Request
     * @return addTopic response
     */
    public AddTopicResponseType addTopic(final AddTopicType addTopic) {
        final AddTopicResponseType response = new AddTopicResponseType();

        final boolean result = this.addTopic(addTopic.getTopic());

        response.setResult(result);

        return response;
    }

    /**
     * addTopic-method for internal queries
     * 
     * @param addTopic
     *            Request
     * @return addTopic response
     */
    public boolean addTopic(final String topic) {
        // check if topic is already existent
        // if yes -> don't overwrite existing topic and return false
        if (this.subscriptions.containsKey(topic)) {
            return false;
        }

        this.subscriptions.put(topic, new Vector<String>());
        this.logger.debug("added topic: " + topic);
        return true;
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
     * getTopics-method for internal queries
     * 
     * @param getTopics
     *            Request
     * @return getTopics response
     */
    public Set<String> getTopics() {
        return this.subscriptions.keySet();
    }

    /**
     * getTopics-method for WS-queries
     * 
     * @param getTopics
     *            Request
     * @return getTopics response
     */
    public GetTopicsResponseType getTopics(final GetTopicsType getTopics) {
        final GetTopicsResponseType response = new GetTopicsResponseType();

        final Set<String> topics = this.getTopics();

        response.getTopics().addAll(topics);

        return response;
    }

    /**
     * initialize method for the subscriptions-HashMap
     * 
     * @param initialSubscriptions
     *            Subscriptions to be added to the NotificationProducer
     */
    public void initializeSubscriptions(
            final HashMap<String, Vector<String>> initialSubscriptions) {
        this.subscriptions.putAll(initialSubscriptions);
    }

    /**
     * publish-method for WS-queries
     * 
     * @param publish
     *            Request
     * @return publish response
     * @throws TopicNotFoundFaultException
     */
    public PublishResponseType publish(final PublishType publish)
            throws TopicNotFoundFaultException {
        final PublishResponseType response = new PublishResponseType();

        final boolean result = this.publish(publish.getTopic(), publish
                .getNotificationList());

        response.setResult(result);

        return response;
    }

    /**
     * publish-method for internal queries
     * 
     * @param publish
     *            Request
     * @return publish response
     * @throws TopicNotFoundFaultException
     */
    public boolean publish(final String topic,
            final List<NotificationMessageType> message)
            throws TopicNotFoundFaultException {
        // check if topic is existent
        if (!this.subscriptions.containsKey(topic)) {
            throw new TopicNotFoundFaultException("Topic " + topic
                    + " is not existent!");
        }

        // monitor, which is supervising the notificationSenders
        // here 2 minutes (120 seconds) timeout
        final NotificationSenderMonitor monitor = new NotificationSenderMonitor(
                120);

        this.logger.debug("Starting notification-distribution...");
        this.logger.debug("Sending status: "
                + message.get(0).getServiceStatus().getStatus() + " on topic: "
                + topic);

        // create new sender-thread and register it to the monitor
        for (final String epr : this.subscriptions.get(topic)) {
            final NotificationSender sender = new NotificationSender(epr,
                    topic, message, true, monitor);
            monitor.registerSender(sender);
        }

        // start notification publishing
        monitor.start();

        return true;
    }

    /**
     * removeSubscription-method for internal queries
     * 
     * @param removeSubscription
     *            Request
     * @return removeSubscription response
     * @throws TopicNotFoundFaultException
     */
    public boolean removeSubscription(final String epr, final String topic)
            throws TopicNotFoundFaultException {
        // check if topic is existent
        if (!this.subscriptions.containsKey(topic)) {
            throw new TopicNotFoundFaultException("Topic " + topic
                    + " is not existent!");
        }

        // remove topic local
        boolean resultLocal = this.subscriptions.get(topic).remove(epr);
        this.logger.debug("unsubscribed " + epr + " from topic " + topic);

        // if local removing not worked, return false
        if (!resultLocal) {
            this.logger.error("unsubscribing " + epr + " from topic " + topic
                    + " failed!");
            return resultLocal;
        }

        if (this.persistSubscription) {
            final DBHandler handler = new DBHandler(topic, epr) {
                @Override
                protected void toDo(String topic, String epr) {
                    // check if subscription is in DB
                    Subscription sub = null;
                    try {
                        sub = Subscription.getSubscriptionForTopicAndEPR(topic,
                                epr);
                    } catch (DatabaseException e1) {
                        e1.printStackTrace();
                    }

                    if (sub == null) {
                        // nothing here, subscription not found -> nothing to
                        // delete
                        return;
                    }

                    // try to remove subscription in DB
                    try {
                        sub.delete();
                    } catch (DatabaseException e) {
                        Logger logger = PhLogger.getLogger(this.getClass());
                        logger.debug("unsubscribed " + epr + " from topic "
                                + topic + " , but not persistent in DB!");
                        e.printStackTrace();
                    }
                }
            };
            handler.start();
        }

        return resultLocal;
    }

    /**
     * removeTopic-method for WS-queries
     * 
     * @param removeTopic
     *            Request
     * @return removeTopic response
     */
    public RemoveTopicResponseType removeTopic(final RemoveTopicType removeTopic) {
        final RemoveTopicResponseType response = new RemoveTopicResponseType();

        final boolean result = this.removeTopic(removeTopic.getTopic());

        response.setResult(result);

        return response;
    }

    /**
     * removeTopic-method for internal queries
     * 
     * @param removeTopic
     *            Request
     * @return removeTopic response
     */
    public boolean removeTopic(final String topic) {
        // check if topic is not existent
        // if yes -> return false
        if (!this.subscriptions.containsKey(topic)) {
            return false;
        }

        // remove Topic local
        this.subscriptions.remove(topic);

        if (this.persistSubscription) {
            final DBHandler handler = new DBHandler(topic, null) {
                @Override
                protected void toDo(String topic, String epr) {
                    // get all subscriptions for the topic
                    try {
                        for (Subscription sub : Subscription
                                .getSubscriptionsForTopic(topic)) {
                            sub.delete();
                        }
                    } catch (DatabaseException e) {
                        Logger logger = PhLogger.getLogger(this.getClass());
                        logger.debug("removed topic " + topic
                                + " , but not persistent in DB!");
                        e.printStackTrace();
                    }
                }
            };
            handler.start();
        }

        this.logger.debug("removed topic: " + topic);
        return true;
    }
}
