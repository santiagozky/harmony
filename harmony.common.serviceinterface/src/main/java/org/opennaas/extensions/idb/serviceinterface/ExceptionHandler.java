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

package org.opennaas.extensions.idb.serviceinterface;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.apache.muse.ws.addressing.soap.SoapFault;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;
import org.opennaas.extensions.idb.serviceinterface.handler.HarmonyHandler;
import org.opennaas.core.utils.PhLogger;

/**
 * Class to handle Exception caused by Request Handler.
 * 
 * @author gassen
 * 
 */
public class ExceptionHandler {

    /** Owner Object. */
    private final HarmonyHandler owner;
    /** Owner Class. */
    private final Class<?> ownerClass;
    /** Logger for fatal-mail-logging. */
    private final Logger logger = PhLogger.getLogger(this.getClass());

    /**
     * Default Constructor.
     * 
     * @param paramOwner
     *            Owner Class for Exception Handler
     */
    public ExceptionHandler(final HarmonyHandler paramOwner) {
        this.owner = paramOwner;
        this.ownerClass = paramOwner.getClass();
    }

    /**
     * Handle exceptions which are not handled by sub RequestHandler.
     * 
     * @param cause
     *            Thrown exception
     * @return Detail Element
     */
    private UnexpectedFaultException handleDefaultException(
            final Throwable cause) {
        return new UnexpectedFaultException(cause);
    }

    /**
     * Send out default exception message. Overwrite this in derived classes.
     * 
     * To implement HandlerSpecific exception handle methods, you need to create
     * a function like this in the according RequestHandler:
     * 
     * public final SoapFault runException(Throwable cause) { // Retrun own
     * Fault }
     * 
     * @param cause
     *            Exception
     * @return Exception Message Object
     */
    public final SoapFault handleException(final Throwable cause) {
        this.logger.fatal("Exception <" + cause.getClass().getSimpleName()
                + "> handled by ExceptionHandler", cause);

        // Try to find exception handler
        try {
            final Method method = this.ownerClass.getMethod("runException",
                    Throwable.class);

            return (SoapFault) method.invoke(this.owner, cause);
            // SubHandler has no own exception handler
        } catch (final NoSuchMethodException ex) {
            return this.handleDefaultException(cause);
            // Exception can't be handled / Error while Serializing
        } catch (final Exception ex) {
            return this.handleInternalException(ex);
        }
    }

    /**
     * Handle exceptions which are thrown during Exception processing. E.g.
     * Serializer faults etc.
     * 
     * @param cause
     *            Thrown exception
     * @return Detail Element
     */
    private UnexpectedFaultException handleInternalException(
            final Throwable cause) {
        return this.handleDefaultException(cause);
    }
}
