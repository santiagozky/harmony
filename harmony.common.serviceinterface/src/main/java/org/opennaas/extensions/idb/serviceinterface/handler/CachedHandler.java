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
package org.opennaas.extensions.idb.serviceinterface.handler;

import java.lang.reflect.Method;
import java.util.HashMap;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.OperationNotSupportedFaultException;

/**
 * @author gassen
 * 
 */
public abstract class CachedHandler extends HarmonyHandler {
    /** Known methods map. */
    private final HashMap<Class<?>, Method> defaultHandlerMethods = new HashMap<Class<?>, Method>();

    /** Known methods map. */
    private final HashMap<String, Method> namedHandlerMethods = new HashMap<String, Method>();

    /** Self Class. */
    private final Class<? extends CachedHandler> selfCLASS = this.getClass();

    /**
     * @param param
     * @return
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    private final Method getDefaultMethod(final Class<?> param)
            throws SecurityException, NoSuchMethodException {
        // Lookup method in hashmap
        Method method = this.defaultHandlerMethods.get(param);

        // Method not yet hashed
        if (null == method) {
            // Lookup method
            method = this.selfCLASS.getMethod(HarmonyHandler.HANDLERMETHODNAME,
                    param);

            // Hash method
            this.defaultHandlerMethods.put(param, method);
        }

        return method;
    }

    /**
     * class.getMethod() is not very fast... so use a hashmap for known methods.
     * 
     * @param param
     *            Parameter Class
     * @param methodName
     *            Name of Handler method, null if equals default Name
     * @return Method
     * @throws OperationNotSupportedFaultException
     * @throws NoSuchMethodException
     *             No handler method found
     */
    protected final Method getMethod(final Class<?> param,
            final String methodName) throws SecurityException,
            OperationNotSupportedFaultException {

        Method method = null;

        try {
            // Should be most case
            if (null == methodName) {
                method = this.getDefaultMethod(param);
            } else {
                method = this.getNamedMethod(param, methodName);
            }
        } catch (final NoSuchMethodException e) {
            final String lookup = (null == methodName) ? HarmonyHandler.HANDLERMETHODNAME
                    : methodName;

            throw new OperationNotSupportedFaultException("Method " + lookup
                    + " ( " + param.getSimpleName() + " ) is not Supported by "
                    + this.getClass().getName(), e);
        }

        return method;
    }

    /**
     * @param param
     * @param methodName
     * @return
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    private final Method getNamedMethod(final Class<?> param,
            final String methodName) throws SecurityException,
            NoSuchMethodException {
        // Hash String
        final String lookup = param.getName() + ":" + methodName;

        // Lookup method in hashmap
        Method method = this.namedHandlerMethods.get(lookup);

        // Method not yet hashed
        if (null == method) {
            // Lookup method
            method = this.selfCLASS.getMethod(methodName, param);

            // Hash method
            this.namedHandlerMethods.put(lookup, method);
        }

        return method;
    }
}
