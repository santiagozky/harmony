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
package client.helper;

import java.util.Date;

import client.interfaces.CommonServiceAsync;
import client.template.panel.LoggingPanel;

import com.google.gwt.user.client.Cookies;

/**
 * Wrapper class to call logger via rpc.
 * 
 * @author gassen
 */
public class GuiLogger {

    /**
     * 
     */
    private static final CommonServiceAsync SERVICE = RpcRequest.common();
    /**
     * 
     */
    private static final GuiLoggerCallback CALLBACK = new GuiLoggerCallback();

    /**
     * 
     */
    public static final int TRACE = 0;
    /**
     * 
     */
    public static final int DEBUG = 1;
    /**
     * 
     */
    public static final int WARN = 2;
    /**
     * 
     */
    public static final int ERROR = 3;
    /**
     * 
     */
    public static final int FATAL = 4;

    /**
     * 
     */
    private static int logLevel = GuiLogger.getLogLevel();

    /**
     * Do not instanciate.
     */
    private GuiLogger() {

    }

    /**
     * @return
     */
    private static final int getLogLevel() {
        final String value = Cookies.getCookie("LogLevel");

        if (null != value) {
            return Integer.valueOf(value).intValue();
        } else {
            return GuiLogger.TRACE;
        }
    }

    /**
     * @param level
     */
    public static final void setLogLevel(final int level) {
        GuiLogger.logLevel = level;
    }

    /**
     * @return
     */
    private static final String getTimeStamp() {
        return (new Date()).toString();
    }

    /**
     * Default Log.
     * 
     * @param message
     */
    public static final void log(final String message) {
        GuiLogger.traceLog(message);
    }

    /**
     * Debug Log.
     * 
     * @param message
     */
    public static final void traceLog(final String message) {
        if (GuiLogger.TRACE >= GuiLogger.logLevel) {
            LoggingPanel.log("[TRACE " + GuiLogger.getTimeStamp() + "] "
                    + message);
        }
    }

    /**
     * Debug Log.
     * 
     * @param message
     */
    public static final void debugLog(final String message) {
        if (GuiLogger.DEBUG >= GuiLogger.logLevel) {
            LoggingPanel.log("[DEBUG " + GuiLogger.getTimeStamp() + "] "
                    + message);
        }
    }

    /**
     * Error Log.
     * 
     * @param message
     */
    public static final void warnLog(final String message) {
        if (GuiLogger.WARN >= GuiLogger.logLevel) {
            LoggingPanel.log("[WARN " + GuiLogger.getTimeStamp() + "] "
                    + message);
        }
        GuiLogger.SERVICE.warnLog(message, GuiLogger.CALLBACK);
    }

    /**
     * Error Log.
     * 
     * @param message
     */
    public static final void errorLog(final String message) {
        if (GuiLogger.ERROR >= GuiLogger.logLevel) {
            LoggingPanel.log("[ERROR " + GuiLogger.getTimeStamp() + "] "
                    + message);
        }
        GuiLogger.SERVICE.errorLog(message, GuiLogger.CALLBACK);
    }

    /**
     * Fatal Log.
     * 
     * @param message
     */
    public static final void fatalLog(final String message) {
        if (GuiLogger.FATAL >= GuiLogger.logLevel) {
            LoggingPanel.log("[FATAL " + GuiLogger.getTimeStamp() + "] "
                    + message);
        }
        GuiLogger.SERVICE.fatalLog(message, GuiLogger.CALLBACK);
    }

    /**
     * Error Log.
     * 
     * @param message
     */
    public static final void traceLog(final String message,
            final Throwable caught) {
        final String newMessage =
                message + "\n" + GuiException.exception2string(caught, false);
        GuiLogger.traceLog(newMessage);
    }

    /**
     * Debug Log.
     * 
     * @param message
     */
    public static final void debugLog(final String message,
            final Throwable caught) {
        final String newMessage =
                message + "\n" + GuiException.exception2string(caught, false);
        GuiLogger.debugLog(newMessage);
    }

    /**
     * Error Log.
     * 
     * @param message
     */
    public static final void warnLog(final String message,
            final Throwable caught) {
        final String newMessage =
                message + "\n" + GuiException.exception2string(caught, false);
        GuiLogger.warnLog(newMessage);
    }

    /**
     * Error Log.
     * 
     * @param message
     */
    public static final void errorLog(final String message,
            final Throwable caught) {
        final String newMessage =
                message + "\n" + GuiException.exception2string(caught, false);
        GuiLogger.errorLog(newMessage);
    }

    /**
     * Fatal Log.
     * 
     * @param message
     */
    public static final void fatalLog(final String message,
            final Throwable caught) {
        final String newMessage =
                message + "\n" + GuiException.exception2string(caught, false);
        GuiLogger.fatalLog(newMessage);
    }
}
