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

package org.opennaas.extensions.idb.serviceinterface.topology.registrator;

import org.apache.log4j.Logger;
import org.apache.muse.ws.addressing.soap.SoapFault;

/**
 * The GenericRegistrator class implements the generic functionality used for
 * registering domains and endpoints. This includes retrying registration in
 * case of a failure and calling a different registration in case a specified
 * Exception is caught. E.g., if a DomainAlreadyExistsFaultException is caught
 * in an addDomain call, then the following retries will use the editDomain
 * call.
 */
public abstract class GenericRegistrator {
    /** Registration request. */
    public final Object req;

    /** Sleep interval (in ms) between unsuccessful registration attempts. */
    public final long sleep;

    /**
     * Constructor.
     * 
     * @param req
     *            Registration request.
     * @param sleep
     *            Sleep interval (in ms) between unsuccessful registration
     *            attempts.
     */
    public GenericRegistrator(final Object req, final long sleep) {
        this.req = req;
        this.sleep = sleep;
    }

    /**
     * Start the registration proccess.
     * 
     * @param log
     *            Logger object to use for logging. This may be null to disable
     *            logging.
     * @param tries
     *            Number of registration attempts before giving up.
     * @param bailException
     *            If registration result in this exception, then a new
     *            GenericRegistrator specified as next parameter is immediately
     *            called.
     * @param bail
     *            Use this GenericRegistrator in case the exception specified in
     *            the bailException parameter is caught.
     * @return Success of registration (true if successful, false otherwise).
     */
    public final boolean genericRegistrator(final Logger log, final int tries,
            final Class<? extends Exception> bailException,
            final GenericRegistrator bail) {
        int triesLeft = tries;
        while (triesLeft-- > 0) {
            try {
                if (this.register()) {
                    return true;
                }
            } catch (final Throwable t) {
                if (t.getClass().equals(bailException)) {
                    return bail.genericRegistrator(log, tries, null, null);
                }
                if (log != null) {
                    log.error("Error occured in GenericRegistrator: " + t);
                }
            }
            try {
                Thread.sleep(this.sleep);
            } catch (final InterruptedException e) {
                if (log != null) {
                    log.error("GenericRegistrator interrupted -- aborting");
                }
                return false;
            }
        }
        return false;
    }

    /**
     * The specific registration attempt, to be implemented by a child class.
     * 
     * @return Success of registration (true if successful, false otherwise).
     * @throws SoapFault
     */
    public abstract boolean register() throws SoapFault;
}
