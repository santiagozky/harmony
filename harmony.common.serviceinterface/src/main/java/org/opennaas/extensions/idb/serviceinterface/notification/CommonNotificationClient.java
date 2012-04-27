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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;

import org.apache.muse.ws.addressing.EndpointReference;
import org.apache.muse.ws.addressing.soap.SoapFault;
import org.w3c.dom.Element;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.utils.WebserviceUtils;
import org.opennaas.extensions.idb.serviceinterface.utils.EPRHelper;
import org.opennaas.extensions.idb.serviceinterface.utils.SecurityHelper;
import org.opennaas.core.utils.Helpers;

/**
 * Notification client.
 * 
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 * @version $Id: NotificationClient.java 726 2007-09-04 12:23:08Z
 *          willner@cs.uni-bonn.de $
 * 
 */
public class CommonNotificationClient extends NotificationClient {
    private final boolean testDirectly;
    private Object notificationServer;

    /**
     * Instantiate the notification client with the given EPR.
     * 
     * @param endpointReference
     *            The URL/EPR to the notification adapter.
     */
    public CommonNotificationClient(final EndpointReference endpointReference) {
        super(endpointReference, EPRHelper.getSource(EPRHelper.NOTIFICATION),
                SecurityHelper.createSoapClient());
        this.testDirectly = false;
    }

    /**
     * To use the Webservice Java class instead of the servlet TCP/IP
     * communication.
     * 
     * @param webservice
     *            The Webservice class
     * @throws URISyntaxException
     */
    public CommonNotificationClient(final INotificationWS webservice) {
        super(new EndpointReference(WebserviceUtils.getEmptyURI()), EPRHelper
                .getSource(EPRHelper.NOTIFICATION), SecurityHelper
                .createSoapClient());
        this.testDirectly = true;
        this.notificationServer = webservice;
    }

    /**
     * Instantiate the reservation client with the given EPR.
     * 
     * @param endpointReference
     *            The URI/EPR to the reservation adapter.
     * @throws URISyntaxException
     *             If the given EPR is not a valid URI.
     */
    public CommonNotificationClient(final String endpointReference) {
        this(Helpers.convertStringtoEPR(endpointReference));
    }

    @Override
    public Element addTopic(final Element addTopic) throws SoapFault {
        if (!this.testDirectly) {
            return super.addTopic(addTopic);
        }

        final String methodName = new Exception().getStackTrace()[0]
                .getMethodName();
        return this.callServer(addTopic, methodName);
    }

    /**
     * @param activate
     * @param methodName
     * @return
     * @throws SoapFault
     */
    @SuppressWarnings("unchecked")
    private Element callServer(final Element activate, final String methodName)
            throws SoapFault {
        final Class[] argsClass = { Element.class };
        Element result = null;
        try {
            final Method method = this.notificationServer.getClass().getMethod(
                    methodName, argsClass);
            result = (Element) method.invoke(this.notificationServer, activate);
        } catch (final InvocationTargetException e) {
            if (SoapFault.class.isInstance(e.getCause())) {
                throw (SoapFault) (e.getCause());
            }
            throw new UnexpectedFaultException("No SoapFault? (ID:4e8722f)", e
                    .getCause());
        } catch (final Exception e) {
            throw new UnexpectedFaultException("No SoapFault? (ID:4e8723f)", e
                    .getCause());
        }
        return result;
    }

    @Override
    public Element getTopics(final Element getTopics) throws SoapFault {
        if (!this.testDirectly) {
            return super.getTopics(getTopics);
        }

        final String methodName = new Exception().getStackTrace()[0]
                .getMethodName();
        return this.callServer(getTopics, methodName);
    }

    @Override
    public Element publish(final Element publish) throws SoapFault {
        if (!this.testDirectly) {
            return super.publish(publish);
        }

        final String methodName = new Exception().getStackTrace()[0]
                .getMethodName();
        return this.callServer(publish, methodName);
    }

    @Override
    public Element removeTopic(final Element removeTopic) throws SoapFault {
        if (!this.testDirectly) {
            return super.removeTopic(removeTopic);
        }

        final String methodName = new Exception().getStackTrace()[0]
                .getMethodName();
        return this.callServer(removeTopic, methodName);
    }

    public void setServer(final Object server) {
        this.notificationServer = server;
    }

    @Override
    public Element subscribe(final Element subscribe) throws SoapFault {
        if (!this.testDirectly) {
            return super.subscribe(subscribe);
        }

        final String methodName = new Exception().getStackTrace()[0]
                .getMethodName();
        return this.callServer(subscribe, methodName);
    }

    @Override
    public Element unsubscribe(final Element unsubscribe) throws SoapFault {
        if (!this.testDirectly) {
            return super.unsubscribe(unsubscribe);
        }

        final String methodName = new Exception().getStackTrace()[0]
                .getMethodName();
        return this.callServer(unsubscribe, methodName);
    }
}
