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


package org.opennaas.extensions.idb.notification.handler;

import org.opennaas.extensions.idb.serviceinterface.RequestHandler;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AddTopicResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AddTopicType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetTopicsResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetTopicsType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.PublishResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.PublishType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.RemoveTopicResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.RemoveTopicType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.SubscribeResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.SubscribeType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.UnsubscribeResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.UnsubscribeType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.TopicNotFoundFaultException;
import org.opennaas.extensions.idb.notification.producer.NotificationProducerHandler;
import org.opennaas.extensions.idb.notification.producer.NotificationSubscriptionHandler;

/** Provision Request Handler. */
public final class NotificationRequestHandler extends RequestHandler {

    /** Singleton Instance. */
    private static NotificationRequestHandler selfInstance = null;

    /**
     * Instance getter.
     * 
     * @return Singleton Instance
     */
    public static NotificationRequestHandler getInstance() {
        if (NotificationRequestHandler.selfInstance == null) {
            NotificationRequestHandler.selfInstance = new NotificationRequestHandler();
        }
        return NotificationRequestHandler.selfInstance;
    }

    /** Private constructor: Singleton. */
    private NotificationRequestHandler() {
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

    /*
     * Handler
     * =========================================================================
     */

    /**
     * addTopic Handler.
     * 
     * @param element
     *            addTopic Request
     * @return addTopic Response
     */
    public AddTopicResponseType addTopic(final AddTopicType element) {
        return NotificationProducerHandler.getInstance().addTopic(element);
    }

    /**
     * getTopics Handler.
     * 
     * @param element
     *            getTopics Request
     * @return getTopics Response
     */
    public GetTopicsResponseType getTopics(final GetTopicsType element) {
        return NotificationProducerHandler.getInstance().getTopics(element);
    }

    /**
     * publish Handler.
     * 
     * @param element
     *            publish Request
     * @return publish Response
     * @throws TopicNotFoundFaultException
     */
    public PublishResponseType publish(final PublishType element)
            throws TopicNotFoundFaultException {
        return NotificationProducerHandler.getInstance().publish(element);
    }

    /**
     * removeTopic Handler.
     * 
     * @param element
     *            removeTopic Request
     * @return removeTopic Response
     */
    public RemoveTopicResponseType removeTopic(final RemoveTopicType element) {
        return NotificationProducerHandler.getInstance().removeTopic(element);
    }

    /**
     * subscribe Handler.
     * 
     * @param element
     *            subscribe Request
     * @return subscribe Response
     * @throws TopicNotFoundFaultException
     */
    public SubscribeResponseType subscribe(final SubscribeType element)
            throws TopicNotFoundFaultException {
        return NotificationSubscriptionHandler.getInstance().subscribe(element);
    }

    /**
     * unsubscribe Handler.
     * 
     * @param element
     *            unsubscribe Request
     * @return unsubscribe Response
     * @throws TopicNotFoundFaultException
     */
    public UnsubscribeResponseType unsubscribe(final UnsubscribeType element)
            throws TopicNotFoundFaultException {
        return NotificationSubscriptionHandler.getInstance().unsubscribe(
                element);
    }
}
