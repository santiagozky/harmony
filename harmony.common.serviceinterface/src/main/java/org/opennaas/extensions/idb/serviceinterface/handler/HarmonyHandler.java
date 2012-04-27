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

import org.apache.muse.ws.addressing.soap.SoapFault;

import org.opennaas.extensions.idb.serviceinterface.ExceptionHandler;
import org.opennaas.extensions.idb.serviceinterface.databinding.utils.AJaxbSerializer;
import org.opennaas.extensions.idb.serviceinterface.databinding.utils.JaxbSerializer;

/**
 * @author gassen
 * 
 */
public abstract class HarmonyHandler {

    /** Name of methods which should be invoked. */
    protected static final String HANDLERMETHODNAME = "runRequest";

    /** Serializer Instance. */
    private AJaxbSerializer serializer = null;

    /** Exception Handler. */
    protected final ExceptionHandler exceptionHandler = new ExceptionHandler(
            this);

    /**
     * Secure way to get instance (Catches runtime exceptions)
     * 
     * @return Instance
     * @throws SoapFault
     *             In case of any runtime exceptions
     */
    protected final AJaxbSerializer getSerializer() throws SoapFault {
        if (null == this.serializer) {
            try {
                this.serializer = JaxbSerializer.getInstance();
            } catch (final Exception e) {
                throw this.exceptionHandler.handleException(e);
            }
        }

        return this.serializer;
    }

    /**
     * Secure way to set the Serializer (Catches runtime exceptions)
     */
    protected final void setSerializer(final AJaxbSerializer newSerializer) {
        this.serializer = newSerializer;
    }
}
