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

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.SubscribeResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.SubscribeType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.UnsubscribeResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.UnsubscribeType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.TopicNotFoundFaultException;

public class NotificationSubscriptionHandler {

    /** Singleton Instance. */
    private static NotificationSubscriptionHandler selfInstance = null;

    /**
     * Instance getter.
     * 
     * @return Singleton Instance
     */
    public static NotificationSubscriptionHandler getInstance() {
        if (NotificationSubscriptionHandler.selfInstance == null) {
            NotificationSubscriptionHandler.selfInstance = new NotificationSubscriptionHandler();
        }
        return NotificationSubscriptionHandler.selfInstance;
    }

    /** Private constructor: Singleton. */
    private NotificationSubscriptionHandler() {
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
     * subscribe-method for internal queries
     * 
     * @param subscribe
     *            Request
     * @return subscribe response
     * @throws TopicNotFoundFaultException
     */
    public boolean subscribe(final String epr, final String topic)
            throws TopicNotFoundFaultException {
        return NotificationProducerHandler.getInstance().addSubscription(epr,
                topic);
    }

    /**
     * subscribe-method for WS-queries
     * 
     * @param subscribe
     *            Request
     * @return subscribe response
     * @throws TopicNotFoundFaultException
     */
    public SubscribeResponseType subscribe(final SubscribeType subscribe)
            throws TopicNotFoundFaultException {
        final boolean result = this.subscribe(subscribe.getConsumerReference(),
                subscribe.getTopic());

        final SubscribeResponseType response = new SubscribeResponseType();
        response.setResult(result);
        return response;
    }

    /**
     * unsubscribe-method for internal queries
     * 
     * @param unsubscribe
     *            Request
     * @return unsubscribe response
     * @throws TopicNotFoundFaultException
     */
    public boolean unsubscribe(final String epr, final String topic)
            throws TopicNotFoundFaultException {
        return NotificationProducerHandler.getInstance().removeSubscription(
                epr, topic);
    }

    /**
     * unsubscribe-method for WS-queries
     * 
     * @param unsubscribe
     *            Request
     * @return unsubscribe response
     * @throws TopicNotFoundFaultException
     */
    public UnsubscribeResponseType unsubscribe(final UnsubscribeType unsubscribe)
            throws TopicNotFoundFaultException {
        final boolean result = this.unsubscribe(unsubscribe
                .getConsumerReference(), unsubscribe.getTopic());

        final UnsubscribeResponseType response = new UnsubscribeResponseType();
        response.setResult(result);
        return response;
    }
}
