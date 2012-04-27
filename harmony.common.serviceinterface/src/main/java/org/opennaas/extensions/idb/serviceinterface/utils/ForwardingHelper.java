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

package org.opennaas.extensions.idb.serviceinterface.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.muse.ws.addressing.soap.SoapFault;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ConnectionConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CreateReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.IsAvailableType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ServiceConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.InvalidRequestFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.OperationNotAllowedFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;
import org.opennaas.extensions.idb.serviceinterface.topology.registrator.AbstractTopologyRegistrator;
import org.opennaas.core.utils.Helpers;
import org.opennaas.core.utils.PhLogger;

public class ForwardingHelper {
    private final Logger logger = PhLogger.getLogger(ForwardingHelper.class);

    private static final String GET_METHOD = "getReservationID";
    private static final String SET_METHOD = "setReservationID";

    private final HashMap<Class<?>, Boolean> hasReservationId = new HashMap<Class<?>, Boolean>();

    private final HashMap<Class<?>, Method> getMethods = new HashMap<Class<?>, Method>();

    private final HashMap<Class<?>, Method> setMethods = new HashMap<Class<?>, Method>();

    private static ForwardingHelper instance = null;

    /**
     * Instance Getter.
     * 
     * @return
     */
    public static ForwardingHelper getInstance() {
        if (null == ForwardingHelper.instance) {
            ForwardingHelper.instance = new ForwardingHelper();
        }

        return ForwardingHelper.instance;
    }

    /**
     * Add suffix to object.
     * 
     * @param obj
     * @return
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InvalidRequestFaultException
     * @throws OperationNotAllowedFaultException
     */
    public final void addSuffix(final Object obj) throws SecurityException,
            NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException,
            InvalidRequestFaultException, UnexpectedFaultException {

        String id = this.getReservationId(obj);

        // If suffix allready exists, do nothing
        if (-1 < id.indexOf("@")) {
            return;
        }

        // Else: add Suffix
        id += "@" + this.getLocalDomain();

        this.setReservationId(obj, id);
    }

    /**
     * Get the getReservationId method.
     * 
     * @param obj
     * @return
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    private final Method getGetter(final Object obj) throws SecurityException,
            NoSuchMethodException {
        final Class<?> clazz = obj.getClass();
        Method method = this.getMethods.get(clazz);

        if (null == method) {
            method = clazz.getMethod(ForwardingHelper.GET_METHOD);
            this.getMethods.put(clazz, method);
        }

        return method;
    }

    /**
     * Get the local domain id.
     * 
     * @return
     * @throws UnexpectedFaultException
     */
    private final String getLocalDomain() throws UnexpectedFaultException {
        final AbstractTopologyRegistrator registrator = AbstractTopologyRegistrator
                .getLatestInstance();

        if (null != registrator) {
            return registrator.getDomainId();
        }

        throw new UnexpectedFaultException(
                "AbstractTopologyRegistrator has to be Instanciated before forwarding Requests!");
    }

    /**
     * Get ReservationId from Object.
     * 
     * @param obj
     * @return
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    private final String getReservationId(final Object obj)
            throws SecurityException, NoSuchMethodException,
            IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        final Method method = this.getGetter(obj);

        final String result = (String) method.invoke(obj);

        if (null == result) {
            throw new RuntimeException(obj.getClass().getSimpleName() + "."
                    + ForwardingHelper.GET_METHOD + "() returned null.");
        }

        return result;
    }

    /**
     * Get the setReservationId method.
     * 
     * @param obj
     * @return
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    private final Method getSetter(final Object obj) throws SecurityException,
            NoSuchMethodException {
        final Class<?> clazz = obj.getClass();
        Method method = this.setMethods.get(clazz);

        if (null == method) {
            method = clazz.getMethod(ForwardingHelper.SET_METHOD, String.class);
            this.setMethods.put(clazz, method);
        }

        return method;
    }

    /**
     * Get suffix from Object.
     * 
     * @param obj
     * @return
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InvalidRequestFaultException
     * @throws OperationNotAllowedFaultException
     */
    public final String getSuffix(final Object obj) throws SecurityException,
            NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException,
            InvalidRequestFaultException, UnexpectedFaultException {

        final String id = this.getReservationId(obj);

        final int suffix = id.indexOf("@");

        // Return own suffix if no suffix is found
        if (-1 == suffix) {
            return this.getLocalDomain();
        }

        final String domainId = id.substring(suffix + 1);

        return domainId;
    }

    /**
     * @param obj
     * @return
     */
    public final boolean hasReservationId(final Object obj)
            throws SecurityException {
        final Class<?> key = obj.getClass();

        Boolean val = this.hasReservationId.get(key);

        if (null == val) {
            try {
                this.getGetter(obj);
                val = Boolean.TRUE;
                this.hasReservationId.put(key, val);
            } catch (final NoSuchMethodException ex) {
                val = Boolean.FALSE;
                this.hasReservationId.put(key, val);
            }
        }

        return val.booleanValue();
    }

    /**
     * Check if endpoint id matches own TNA prefixes.
     * 
     * @param domainId
     * @return
     * @throws SoapFault
     */
    protected final boolean isForeignDomain(final EndpointType ep)
            throws SoapFault {
        final AbstractTopologyRegistrator registrator = AbstractTopologyRegistrator
                .getLatestInstance();
        if (null == registrator) {
            throw new UnexpectedFaultException(
                    "Topology registrator must be initialized first!");
        }
        // if we are already at the top level, we cannot forward anyway
        if (!registrator.hasSuperdomain()) {
            return false;
        }
        final List<String> prefixList = registrator.allTNAPrefixes();
        if ((null != ep.getEndpointId()) && (null != prefixList)) {
            /* -------------------------------------------------------------- */
            final StringBuilder loggerList = new StringBuilder();
            for (final String prefix : prefixList) {
                loggerList.append(prefix + " ");
            }
            this.logger.debug("My prefixes: " + loggerList);
            /* -------------------------------------------------------------- */
            for (final String tnaPrefix : prefixList) {

                if (null != tnaPrefix) {
                    final boolean isLocal = Helpers.prefixMatch(ep
                            .getEndpointId(), tnaPrefix);

                    if (isLocal) {
                        this.logger.debug("Prefix match: " + ep.getEndpointId()
                                + " - " + tnaPrefix);

                        return false;
                    }
                }
            }
        }

        this.logger.debug("No prefix found to match " + ep.getEndpointId());

        return true;
    }

    /**
     * Check if domain id equals own domain id
     * 
     * @param domainId
     * @return
     * @throws SoapFault
     */
    protected final boolean isForeignDomain(final String domainId)
            throws SoapFault {
        if (null != domainId) {
            this.logger.debug("Own: " + this.getLocalDomain() + ", Req: "
                    + domainId);

            return !domainId.equals(this.getLocalDomain());
        }

        this.logger.warn("Either registrator or domainId are null");
        return false;
    }

    /**
     * Check if any of the Services is not for local domain.
     * 
     * @param obj
     * @return
     * @throws SoapFault
     */
    protected final boolean isForeignRequest(
            final List<ServiceConstraintType> obj) throws SoapFault {
        boolean isForeign = false;

        for (final ServiceConstraintType service : obj) {

            for (final ConnectionConstraintType connection : service
                    .getConnections()) {

                isForeign = this.isForeignDomain(connection.getSource());

                if (isForeign) {
                    return true;
                }

                for (final EndpointType dstEp : connection.getTarget()) {
                    isForeign = this.isForeignDomain(dstEp);

                    if (isForeign) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Check if object is for local domain.
     * 
     * @param obj
     * @return
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws SoapFault
     */
    public final boolean isForeignRequest(final Object obj)
            throws SecurityException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, SoapFault {
        boolean isForeign = false;

        this.logger.debug("Checking responsibility for "
                + obj.getClass().getSimpleName());

        // Check if Request is CreateReservation
        if (CreateReservationType.class.isInstance(obj)) {
            final CreateReservationType type = (CreateReservationType) obj;

            isForeign = this.isForeignRequest(type.getService());
            // Check if Request is IsAvailableType
        } else if (IsAvailableType.class.isInstance(obj)) {
            final IsAvailableType type = (IsAvailableType) obj;

            isForeign = this.isForeignRequest(type.getService());
            // Other Types should have a getReservationId method implemented.
        } else {
            try {
                final String id = this.getSuffix(obj);

                isForeign = this.isForeignDomain(id);
            } catch (final NoSuchMethodException e) {
                this.logger.warn("No such method: " + e.getMessage());
            }
        }

        return isForeign;
    }

    /**
     * Remove Suffix from Object.
     * 
     * @param obj
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public final void removeSuffix(final Object obj) throws SecurityException,
            IllegalArgumentException, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException {
        String id = this.getReservationId(obj);

        final int pos = id.indexOf("@");

        if (pos > 0) {
            id = id.substring(0, pos);
        }

        this.setReservationId(obj, id);
    }

    /**
     * Set ReservationId in object.
     * 
     * @param obj
     * @param id
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private final void setReservationId(final Object obj, final String id)
            throws SecurityException, NoSuchMethodException,
            IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        final Method method = this.getSetter(obj);

        method.invoke(obj, id);
    }

}
