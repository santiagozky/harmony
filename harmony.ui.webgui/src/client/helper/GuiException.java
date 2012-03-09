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


package client.helper;

import java.io.Serializable;

/**
 * Serializable Exception which can be thrown by Server. Has no Stacktrace!
 * 
 * @author gassen
 */
public class GuiException extends Exception implements Serializable {

    /**
     * Version Id
     */
    private static final long serialVersionUID = 1L;

    private String stackTrace;

    public static String exception2string(final Throwable ex) {
        return GuiException.exception2string(ex, true);
    }

    public static String exception2string(final Throwable ex,
            final boolean useHTML) {
        final StackTraceElement[] items = ex.getStackTrace();

        String result =
                (useHTML) ? "\n<i>Stacktrace</i>: <br>\n" : "\nStacktrace\n";

        for (int x = 0; x < items.length; x++) {
            String trace = items[x].toString();

            if (useHTML) {
                trace = StringUtils.escape(trace);
            }

            result += trace + "\n";
        }

        if (null != ex.getCause()) {
            result += (useHTML) ? "\n<br><u>Caused by:</u> " : "\nCaused by:\n";
            result += GuiException.exception2string(ex.getCause(), useHTML);
        }

        return (useHTML) ? "<b>" + StringUtils.escape(ex.toString())
                + "</b><br>\n" + result : ex.toString() + "\n" + result;
    }

    /**
     * Default Constructor.
     */
    public GuiException() {
        super();
    }

    /**
     * Constructor.
     * 
     * @param msg
     *            Fault Message.
     */
    public GuiException(final String msg) {
        super(msg);
    }

    /**
     * Constructor.
     * 
     * @param msg
     *            Fault Message.
     */
    public GuiException(final Throwable ex) {
        super(ex.getMessage());

        this.stackTrace = GuiException.exception2string(ex);
    }

    /**
     * @return the stackTrace
     */
    public String getStackTraceString() {
        return this.stackTrace;
    }

}
