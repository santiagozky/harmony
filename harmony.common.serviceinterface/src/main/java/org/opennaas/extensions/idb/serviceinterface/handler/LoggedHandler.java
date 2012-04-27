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

import org.apache.log4j.Logger;
import org.apache.muse.ws.addressing.soap.SoapFault;

import org.opennaas.extensions.idb.serviceinterface.RequestHandler;
import org.opennaas.extensions.idb.serviceinterface.topology.registrator.AbstractTopologyRegistrator;
import org.opennaas.core.utils.PhLogger;

/**
 * @author gassen
 * 
 */
public class LoggedHandler extends CachedHandler {
    private static String logIDPrefix = null;

    /** Unique ID for logging. */
    private static int logID = 0;

    /** * */
    private static Logger performanceLogger = null;

    /**
     * @return
     */
    protected final static synchronized String getName() {
        if (LoggedHandler.logID > 0xFFFF) {
            LoggedHandler.logID = 0;
        }
        if (LoggedHandler.logID == 0) {
            final String p = Long.toHexString(System.currentTimeMillis());
            LoggedHandler.logIDPrefix = LoggedHandler.prefix4(p.substring(p
                    .length() - 4));
        }
        return "RequestHandler_"
                + LoggedHandler.logIDPrefix
                + LoggedHandler.prefix4(Integer
                        .toHexString(LoggedHandler.logID++));
    }

    private static final synchronized Logger getPerformanceLogger(
            final String domainName) {
        if (null == LoggedHandler.performanceLogger) {
            LoggedHandler.performanceLogger = PhLogger
                    .getSeparateLogger("performance." + domainName);
        }

        return LoggedHandler.performanceLogger;
    }

    /**
     * @param s
     * @return
     */
    private final static String prefix4(final String s) {
        String r = s;
        while (r.length() < 4) {
            r = "0" + r;
        }
        return r;
    }

    /**
     * 
     */
    private Logger logger = null;

    private String domainId = null;

    protected String getDomainId() {
        if (this.domainId == null) {
            final AbstractTopologyRegistrator registrator = AbstractTopologyRegistrator
                    .getLatestInstance();
            if (null == registrator) {
                this.domainId = "unknown";
            } else {
                this.domainId = registrator.getDomainId();
            }
        }
        return this.domainId;
    }

    /**
     * Secure way to get instance (Catches runtime exceptions)
     * 
     * @return Instance
     * @throws SoapFault
     *             In case of any runtime exceptions
     */
    protected final Logger getLogger() throws SoapFault {
        if (null == this.logger) {
            try {
                this.logger = PhLogger.getLogger(RequestHandler.class);
            } catch (final Exception e) {
                throw this.exceptionHandler.handleException(e);
            }
        }

        return this.logger;
    }

    /**
     * Secure way to get instance (Catches runtime exceptions)
     * 
     * @return Instance
     * @throws SoapFault
     *             In case of any runtime exceptions
     */
    public final Logger getPerformanceLogger() throws SoapFault {
        Logger result = null;
        try {
            result = LoggedHandler.getPerformanceLogger(this.getDomainId());
        } catch (final Exception e) {
            this.exceptionHandler.handleException(e);
        }
        return result;
    }
}
