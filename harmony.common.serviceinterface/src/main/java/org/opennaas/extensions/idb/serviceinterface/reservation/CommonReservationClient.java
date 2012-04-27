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

package org.opennaas.extensions.idb.serviceinterface.reservation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;

import org.apache.muse.core.proxy.ProxyHandler;
import org.apache.muse.ws.addressing.EndpointReference;
import org.apache.muse.ws.addressing.soap.SoapFault;
import org.w3c.dom.Element;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.utils.WebserviceUtils;
import org.opennaas.extensions.idb.serviceinterface.utils.EPRHelper;
import org.opennaas.extensions.idb.serviceinterface.utils.SecurityHelper;
import org.opennaas.core.utils.Helpers;

/**
 * Reservation client.
 * 
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 * @version $Id: ReservationClient.java 726 2007-09-04 12:23:08Z
 *          willner@cs.uni-bonn.de $
 */
public class CommonReservationClient extends ReservationClient {
    private final boolean testDirectly;
    private IReservationWS reservationServer;

    private long callTime = 0;
    private long callReturnTime = 0;

    /**
     * Instantiate the reservation client with the given EPR.
     * 
     * @param endpointReference
     *            The URL/EPR to the reservation adapter.
     */
    public CommonReservationClient(final EndpointReference endpointReference) {
        super(endpointReference, EPRHelper.getSource(EPRHelper.RESERVATION),
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
    public CommonReservationClient(final IReservationWS webservice) {
        super(new EndpointReference(WebserviceUtils.getEmptyURI()), EPRHelper
                .getSource(EPRHelper.RESERVATION), SecurityHelper
                .createSoapClient());
        this.testDirectly = true;
        this.reservationServer = webservice;
    }

    /**
     * Instantiate the reservation client with the given EPR.
     * 
     * @param endpointReference
     *            The URI/EPR to the reservation adapter.
     * @throws URISyntaxException
     *             If the given EPR is not a valid URI.
     */
    public CommonReservationClient(final String endpointReference) {
        this(Helpers.convertStringtoEPR(endpointReference));
    }

    @Override
    public Element activate(final Element activate) throws SoapFault {
        if (!this.testDirectly) {
            return super.activate(activate);
        }
        final String methodName = new Exception().getStackTrace()[0]
                .getMethodName();
        return this.callServer(activate, methodName);
    }

    @Override
    public Element bind(final Element bind) throws SoapFault {
        if (!this.testDirectly) {
            this.callTime = System.currentTimeMillis();
            final Element element = super.bind(bind);
            this.callReturnTime = System.currentTimeMillis();
            return element;
        }

        final String methodName = new Exception().getStackTrace()[0]
                .getMethodName();
        return this.callServer(bind, methodName);
    }

    /**
     * @param activate
     * @param methodName
     * @return
     */
    private Element callServer(final Element activate, final String methodName)
            throws SoapFault {
        final Class<?>[] argsClass = { Element.class };
        Element result = null;
        try {
            final Method method = this.reservationServer.getClass().getMethod(
                    methodName, argsClass);
            this.callTime = System.currentTimeMillis();
            result = (Element) method.invoke(this.reservationServer, activate);
            this.callReturnTime = System.currentTimeMillis();
        } catch (final InvocationTargetException e) {
            if (SoapFault.class.isInstance(e.getCause())) {
                throw (SoapFault) (e.getCause());
            }
            throw new UnexpectedFaultException("No SoapFault? (ID:3e8722f): "
                    + e.getCause(), e);
        } catch (final Exception e) {
            throw new UnexpectedFaultException("No SoapFault? (ID:3e8723f): "
                    + e.getCause(), e);
        }
        return result;
    }

    @Override
    public Element cancelJob(final Element cancelJob) throws SoapFault {
        if (!this.testDirectly) {
            this.callTime = System.currentTimeMillis();
            final Element element = super.cancelJob(cancelJob);
            this.callReturnTime = System.currentTimeMillis();
            return element;
        }
        final String methodName = new Exception().getStackTrace()[0]
                .getMethodName();
        return this.callServer(cancelJob, methodName);
    }

    @Override
    public Element cancelReservation(final Element cancelReservation)
            throws SoapFault {
        if (!this.testDirectly) {
            this.callTime = System.currentTimeMillis();
            final Element element = super.cancelReservation(cancelReservation);
            this.callReturnTime = System.currentTimeMillis();
            return element;
        }
        final String methodName = new Exception().getStackTrace()[0]
                .getMethodName();
        return this.callServer(cancelReservation, methodName);
    }

    @Override
    public Element completeJob(final Element completeJob) throws SoapFault {
        if (!this.testDirectly) {
            this.callTime = System.currentTimeMillis();
            final Element element = super.completeJob(completeJob);
            this.callReturnTime = System.currentTimeMillis();
            return element;
        }
        final String methodName = new Exception().getStackTrace()[0]
                .getMethodName();
        return this.callServer(completeJob, methodName);
    }

    @Override
    public Element createReservation(final Element createReservation)
            throws SoapFault {
        if (!this.testDirectly) {
            this.callTime = System.currentTimeMillis();
            final Element element = super.createReservation(createReservation);
            this.callReturnTime = System.currentTimeMillis();
            return element;
        }
        final String methodName = new Exception().getStackTrace()[0]
                .getMethodName();
        return this.callServer(createReservation, methodName);
    }

    public long getLastCallDuration() {
        return this.callReturnTime - this.callTime;
    }

    @Override
    public Element getReservation(final Element getReservation)
            throws SoapFault {
        if (!this.testDirectly) {
            this.callTime = System.currentTimeMillis();
            final Element element = super.getReservation(getReservation);
            this.callReturnTime = System.currentTimeMillis();
            return element;
        }
        final String methodName = new Exception().getStackTrace()[0]
                .getMethodName();
        return this.callServer(getReservation, methodName);
    }

    @Override
    public Element getReservations(final Element getReservations)
            throws SoapFault {
        if (!this.testDirectly) {
            this.callTime = System.currentTimeMillis();
            final Element element = super.getReservations(getReservations);
            this.callReturnTime = System.currentTimeMillis();
            return element;
        }
        final String methodName = new Exception().getStackTrace()[0]
                .getMethodName();
        return this.callServer(getReservations, methodName);
    }

    @Override
    public Element getStatus(final Element getStatus) throws SoapFault {
        if (!this.testDirectly) {
            this.callTime = System.currentTimeMillis();
            final Element element = super.getStatus(getStatus);
            this.callReturnTime = System.currentTimeMillis();
            return element;
        }
        final String methodName = new Exception().getStackTrace()[0]
                .getMethodName();
        return this.callServer(getStatus, methodName);
    }

    /**
     * @param request
     * @param methodName
     * @return
     * @throws SoapFault
     */
    public final Element handleByName(final Element request,
            final String methodName) throws SoapFault {
        final Object[] params = new Object[1];

        params[0] = request;

        final ProxyHandler handler = this.getHandler(methodName);

        try {
            return (Element) this.invoke(handler, params);
        } catch (final SoapFault e) {
            throw org.opennaas.extensions.idb.serviceinterface.databinding.utils.FaultConverter
                    .getInstance().getOriginalFault(e, this.getDestination());
        }
    }

    @Override
    public Element isAvailable(final Element isAvailable) throws SoapFault {
        if (!this.testDirectly) {
            this.callTime = System.currentTimeMillis();
            final Element element = super.isAvailable(isAvailable);
            this.callReturnTime = System.currentTimeMillis();
            return element;
        }
        final String methodName = new Exception().getStackTrace()[0]
                .getMethodName();
        return this.callServer(isAvailable, methodName);
    }

    @Override
    public Element notification(final Element notification) throws SoapFault {
        if (!this.testDirectly) {
            return super.notification(notification);
        }

        final String methodName = new Exception().getStackTrace()[0]
                .getMethodName();
        return this.callServer(notification, methodName);
    }

    public void setServer(final IReservationWS server) {
        this.reservationServer = server;
    }
}
