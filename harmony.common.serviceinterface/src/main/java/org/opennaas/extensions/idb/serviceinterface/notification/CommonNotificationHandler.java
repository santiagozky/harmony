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

/**
 * 
 */
package org.opennaas.extensions.idb.serviceinterface.notification;

import org.apache.muse.ws.addressing.soap.SoapFault;

import org.opennaas.extensions.idb.serviceinterface.RequestHandler;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AddTopicResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AddTopicType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationsResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationsType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatusResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetTopicsResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetTopicsType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.IsAvailableResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.IsAvailableType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.PublishResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.PublishType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.RemoveTopicResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.RemoveTopicType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.SubscribeResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.SubscribeType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.UnsubscribeResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.UnsubscribeType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.OperationNotSupportedFaultException;

/**
 * @author willner
 * 
 */
public class CommonNotificationHandler extends RequestHandler {

    private static CommonNotificationHandler selfInstance;

    /**
     * Instance getter.
     * 
     * @return Singleton Instance
     */
    public static CommonNotificationHandler getInstance() {
        if (CommonNotificationHandler.selfInstance == null) {
            CommonNotificationHandler.selfInstance = new CommonNotificationHandler();
        }
        return CommonNotificationHandler.selfInstance;
    }

    /**
     * @param request
     * @return
     * @throws SoapFault
     */
    public AddTopicResponseType addTopic(final AddTopicType request)
            throws SoapFault {
        throw new OperationNotSupportedFaultException("Not implemented yet.");
    }

    /**
     * @param request
     * @return
     * @throws SoapFault
     */
    public GetReservationsResponseType getReservations(
            final GetReservationsType request) throws SoapFault {
        throw new OperationNotSupportedFaultException("Not implemented yet.");
    }

    /**
     * @param request
     * @return
     * @throws SoapFault
     */
    public GetStatusResponseType getStatus(final GetStatusType request)
            throws SoapFault {
        throw new OperationNotSupportedFaultException("Not implemented yet.");
    }

    /**
     * @param request
     * @return
     * @throws SoapFault
     */
    public GetTopicsResponseType getTopics(final GetTopicsType request)
            throws SoapFault {
        throw new OperationNotSupportedFaultException("Not implemented yet.");
    }

    /**
     * @param request
     * @return
     * @throws SoapFault
     */
    public IsAvailableResponseType isAvailable(final IsAvailableType request)
            throws SoapFault {
        throw new OperationNotSupportedFaultException("Not implemented yet.");
    }

    /**
     * @param request
     * @return
     * @throws SoapFault
     */
    public PublishResponseType publish(final PublishType request)
            throws SoapFault {
        throw new OperationNotSupportedFaultException("Not implemented yet.");
    }

    /**
     * @param request
     * @return
     * @throws SoapFault
     */
    public RemoveTopicResponseType removeTopic(final RemoveTopicType request)
            throws SoapFault {
        throw new OperationNotSupportedFaultException("Not implemented yet.");
    }

    /**
     * @param request
     * @return
     * @throws SoapFault
     */
    public SubscribeResponseType subscribe(final SubscribeType request)
            throws SoapFault {
        throw new OperationNotSupportedFaultException("Not implemented yet.");
    }

    /**
     * @param request
     * @return
     * @throws SoapFault
     */
    public UnsubscribeResponseType unsubscribe(final UnsubscribeType request)
            throws SoapFault {
        throw new OperationNotSupportedFaultException("Not implemented yet.");
    }
}
