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
package server.common;

import org.apache.log4j.Logger;

import org.opennaas.core.utils.PhLogger;

/**
 * @author gassen
 */
public class GuiLogger {
    private static Logger logger = null;
    private final RemoteLoggableServiceServlet servlet;

    /**
     * @return
     */
    public static Logger getInternalLogger() {
        if (null == GuiLogger.logger) {
            GuiLogger.logger = PhLogger.getSeparateLogger("gui", "server");
        }

        return GuiLogger.logger;
    }

    /**
     * @return
     */
    private final String getIpPrefix() {
        String result = "[Client ";
        
        if (this.servlet.getRequest() != null) {
            result += this.servlet.getRequest().getRemoteAddr() ;
        } else {
            result += "local";
        }

        
        return result + "] - ";
    }

    /**
     * @param servlet
     */
    public GuiLogger(final RemoteLoggableServiceServlet servlet) {
        this.servlet = servlet;
    }

    /**
     * @param message
     * @param t
     */
    public void debug(final Object message, final Throwable t) {
        GuiLogger.getInternalLogger().debug(this.getIpPrefix() + message, t);
    }

    /**
     * @param message
     */
    public void debug(final Object message) {
        GuiLogger.getInternalLogger().debug(this.getIpPrefix() + message);
    }

    /**
     * @param message
     * @param t
     */
    public void warn(final Object message, final Throwable t) {
        GuiLogger.getInternalLogger().warn(this.getIpPrefix() + message, t);
    }

    /**
     * @param message
     */
    public void warn(final Object message) {
        GuiLogger.getInternalLogger().warn(this.getIpPrefix() + message);
    }

    /**
     * @param message
     * @param t
     */
    public void error(final Object message, final Throwable t) {
        GuiLogger.getInternalLogger().error(this.getIpPrefix() + message, t);
    }

    /**
     * @param message
     */
    public void error(final Object message) {
        GuiLogger.getInternalLogger().error(this.getIpPrefix() + message);
    }

    /**
     * @param message
     * @param t
     */
    public void fatal(final Object message, final Throwable t) {
        GuiLogger.getInternalLogger().fatal(this.getIpPrefix() + message, t);
    }

    /**
     * @param message
     */
    public void fatal(final Object message) {
        GuiLogger.getInternalLogger().fatal(this.getIpPrefix() + message);
    }
}
