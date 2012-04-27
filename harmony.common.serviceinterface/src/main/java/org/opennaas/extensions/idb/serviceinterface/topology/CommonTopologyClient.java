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

package org.opennaas.extensions.idb.serviceinterface.topology;

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
 * Topology client.
 * 
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 * @version $Id: TopologyClient.java 726 2007-09-04 12:23:08Z
 *          willner@cs.uni-bonn.de $
 */
public class CommonTopologyClient extends TopologyClient {
    private final boolean testDirectly;
    private Object topologyServer;

    /**
     * Instantiate the topology client with the given EPR.
     * 
     * @param endpointReference
     *            The URL/EPR to the topology adapter.
     */
    public CommonTopologyClient(final EndpointReference endpointReference) {
        super(endpointReference, EPRHelper.getSource(EPRHelper.TOPOLOGY),
                SecurityHelper.createSoapClient());
        this.testDirectly = false;
    }

    public CommonTopologyClient(final ITopologyWS topologyWS) {
        super(new EndpointReference(WebserviceUtils.getEmptyURI()), EPRHelper
                .getSource(EPRHelper.TOPOLOGY), SecurityHelper
                .createSoapClient());
        this.testDirectly = true;
        this.topologyServer = topologyWS;
    }

    /**
     * Instantiate the topology client with the given EPR.
     * 
     * @param endpointReference
     *            The URI/EPR to the toplogy adapter.
     * @throws URISyntaxException
     *             If the given EPR is not a valid URI.
     */
    public CommonTopologyClient(final String endpointReference) {
        this(Helpers.convertStringtoEPR(endpointReference));
    }

    @Override
    public Element addDomain(final Element addDomain) throws SoapFault {
        if (!this.testDirectly) {
            return super.addDomain(addDomain);
        }

        final String methodName = new Exception().getStackTrace()[0]
                .getMethodName();
        return this.callServer(addDomain, methodName);
    }

    @Override
    public Element addEndpoint(final Element addEndpoint) throws SoapFault {
        if (!this.testDirectly) {
            return super.addEndpoint(addEndpoint);
        }
        final String methodName = new Exception().getStackTrace()[0]
                .getMethodName();
        return this.callServer(addEndpoint, methodName);
    }

    @Override
    public Element addLink(final Element addLink) throws SoapFault {
        if (!this.testDirectly) {
            return super.addLink(addLink);
        }
        final String methodName = new Exception().getStackTrace()[0]
                .getMethodName();
        return this.callServer(addLink, methodName);
    }

    @Override
    public Element addOrEditDomain(final Element addOrEditDomain)
            throws SoapFault {
        if (!this.testDirectly) {
            return super.addOrEditDomain(addOrEditDomain);
        }

        final String methodName = new Exception().getStackTrace()[0]
                .getMethodName();
        return this.callServer(addOrEditDomain, methodName);
    }

    /**
     * @param activate
     * @param methodName
     * @return
     * @throws SoapFault
     */
    private Element callServer(final Element activate, final String methodName)
            throws SoapFault {
        final Class<?>[] argsClass = { Element.class };
        Element result = null;
        try {
            final Method method = this.topologyServer.getClass().getMethod(
                    methodName, argsClass);
            result = (Element) method.invoke(this.topologyServer, activate);
        } catch (final InvocationTargetException e) {
            if (SoapFault.class.isInstance(e.getCause())) {
                throw (SoapFault) (e.getCause());
            }
            throw new UnexpectedFaultException("No SoapFault? (ID:2e8722f)", e
                    .getCause());
        } catch (final Exception e) {
            throw new UnexpectedFaultException("No SoapFault? (ID:2e8723f)", e
                    .getCause());
        }

        return result;
    }

    @Override
    public Element deleteDomain(final Element deleteDomain) throws SoapFault {
        if (!this.testDirectly) {
            return super.deleteDomain(deleteDomain);
        }
        final String methodName = new Exception().getStackTrace()[0]
                .getMethodName();
        return this.callServer(deleteDomain, methodName);
    }

    @Override
    public Element deleteEndpoint(final Element deleteEndpoint)
            throws SoapFault {
        if (!this.testDirectly) {
            return super.deleteEndpoint(deleteEndpoint);
        }
        final String methodName = new Exception().getStackTrace()[0]
                .getMethodName();
        return this.callServer(deleteEndpoint, methodName);
    }

    @Override
    public Element deleteLink(final Element deleteLink) throws SoapFault {
        if (!this.testDirectly) {
            return super.deleteLink(deleteLink);
        }
        final String methodName = new Exception().getStackTrace()[0]
                .getMethodName();
        return this.callServer(deleteLink, methodName);
    }

    @Override
    public Element editDomain(final Element editDomain) throws SoapFault {
        if (!this.testDirectly) {
            return super.editDomain(editDomain);
        }
        final String methodName = new Exception().getStackTrace()[0]
                .getMethodName();
        return this.callServer(editDomain, methodName);
    }

    @Override
    public Element editEndpoint(final Element editEndpoint) throws SoapFault {
        if (!this.testDirectly) {
            return super.editEndpoint(editEndpoint);
        }
        final String methodName = new Exception().getStackTrace()[0]
                .getMethodName();
        return this.callServer(editEndpoint, methodName);
    }

    @Override
    public Element editLink(final Element editLink) throws SoapFault {
        if (!this.testDirectly) {
            return super.editLink(editLink);
        }
        final String methodName = new Exception().getStackTrace()[0]
                .getMethodName();
        return this.callServer(editLink, methodName);
    }

    @Override
    public Element getDomains(final Element getDomains) throws SoapFault {
        if (!this.testDirectly) {
            return super.getDomains(getDomains);
        }
        final String methodName = new Exception().getStackTrace()[0]
                .getMethodName();
        return this.callServer(getDomains, methodName);
    }

    @Override
    public Element getEndpoints(final Element getEndpoints) throws SoapFault {
        if (!this.testDirectly) {
            return super.getEndpoints(getEndpoints);
        }
        final String methodName = new Exception().getStackTrace()[0]
                .getMethodName();
        return this.callServer(getEndpoints, methodName);
    }

    @Override
    public Element getLinks(final Element getLinks) throws SoapFault {
        if (!this.testDirectly) {
            return super.getLinks(getLinks);
        }
        final String methodName = new Exception().getStackTrace()[0]
                .getMethodName();
        return this.callServer(getLinks, methodName);
    }

    public void setServer(final Object server) {
        this.topologyServer = server;
    }

}
