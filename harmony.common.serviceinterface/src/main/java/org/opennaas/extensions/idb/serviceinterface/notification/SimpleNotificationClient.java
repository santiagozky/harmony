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

package org.opennaas.extensions.idb.serviceinterface.notification;

import java.net.URISyntaxException;

import org.apache.muse.ws.addressing.EndpointReference;
import org.apache.muse.ws.addressing.soap.SoapFault;
import org.w3c.dom.Element;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AddTopic;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AddTopicResponse;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AddTopicResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AddTopicType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetTopics;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetTopicsResponse;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetTopicsResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetTopicsType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.Publish;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.PublishResponse;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.PublishResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.PublishType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.RemoveTopic;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.RemoveTopicResponse;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.RemoveTopicResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.RemoveTopicType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.Subscribe;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.SubscribeResponse;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.SubscribeResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.SubscribeType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.Unsubscribe;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.UnsubscribeResponse;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.UnsubscribeResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.UnsubscribeType;
import org.opennaas.extensions.idb.serviceinterface.databinding.utils.JaxbSerializer;

public class SimpleNotificationClient extends CommonNotificationClient {

    /**
     * Constructor from superclass.
     * 
     * @param endpointReference
     */
    public SimpleNotificationClient(final EndpointReference endpointReference) {
        super(endpointReference);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param webservice
     */
    public SimpleNotificationClient(final INotificationWS webservice) {
        super(webservice);
    }

    /**
     * Constructor from Superclass.
     * 
     * @param endpointReference
     * @throws URISyntaxException
     */
    public SimpleNotificationClient(final String endpointReference) {
        super(endpointReference);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param request
     * @return
     * @throws SoapFault
     */
    public AddTopicResponseType addTopic(final AddTopicType request)
            throws SoapFault {
        final AddTopic envelope = new AddTopic();

        envelope.setAddTopic(request);

        final Element reqElement = JaxbSerializer.getInstance()
                .objectToElement(envelope);

        final Element resElement = super.addTopic(reqElement);

        final AddTopicResponse response = (AddTopicResponse) JaxbSerializer
                .getInstance().elementToObject(resElement);

        return response.getAddTopicResponse();
    }

    /**
     * @param request
     * @return
     * @throws SoapFault
     */
    public GetTopicsResponseType getTopics(final GetTopicsType request)
            throws SoapFault {
        final GetTopics envelope = new GetTopics();

        envelope.setGetTopics(request);

        final Element reqElement = JaxbSerializer.getInstance()
                .objectToElement(envelope);

        final Element resElement = super.getTopics(reqElement);

        final GetTopicsResponse response = (GetTopicsResponse) JaxbSerializer
                .getInstance().elementToObject(resElement);

        return response.getGetTopicsResponse();
    }

    /**
     * @param request
     * @return
     * @throws SoapFault
     */
    public PublishResponseType publish(final PublishType request)
            throws SoapFault {
        final Publish envelope = new Publish();

        envelope.setPublish(request);

        final Element reqElement = JaxbSerializer.getInstance()
                .objectToElement(envelope);

        final Element resElement = super.publish(reqElement);

        final PublishResponse response = (PublishResponse) JaxbSerializer
                .getInstance().elementToObject(resElement);

        return response.getPublishResponse();
    }

    /**
     * @param request
     * @return
     * @throws SoapFault
     */
    public RemoveTopicResponseType removeTopic(final RemoveTopicType request)
            throws SoapFault {
        final RemoveTopic envelope = new RemoveTopic();

        envelope.setRemoveTopic(request);

        final Element reqElement = JaxbSerializer.getInstance()
                .objectToElement(envelope);

        final Element resElement = super.removeTopic(reqElement);

        final RemoveTopicResponse response = (RemoveTopicResponse) JaxbSerializer
                .getInstance().elementToObject(resElement);

        return response.getRemoveTopicResponse();
    }

    /**
     * @param request
     * @return
     * @throws SoapFault
     */
    public SubscribeResponseType subscribe(final SubscribeType request)
            throws SoapFault {
        final Subscribe envelope = new Subscribe();

        envelope.setSubscribe(request);

        final Element reqElement = JaxbSerializer.getInstance()
                .objectToElement(envelope);

        final Element resElement = super.subscribe(reqElement);

        final SubscribeResponse response = (SubscribeResponse) JaxbSerializer
                .getInstance().elementToObject(resElement);

        return response.getSubscribeResponse();
    }

    /**
     * @param request
     * @return
     * @throws SoapFault
     */
    public UnsubscribeResponseType unsubscribe(final UnsubscribeType request)
            throws SoapFault {
        final Unsubscribe envelope = new Unsubscribe();

        envelope.setUnsubscribe(request);

        final Element reqElement = JaxbSerializer.getInstance()
                .objectToElement(envelope);

        final Element resElement = super.unsubscribe(reqElement);

        final UnsubscribeResponse response = (UnsubscribeResponse) JaxbSerializer
                .getInstance().elementToObject(resElement);

        return response.getUnsubscribeResponse();
    }
}
