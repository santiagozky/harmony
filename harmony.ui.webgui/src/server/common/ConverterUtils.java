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

import java.util.Date;

import javax.xml.datatype.XMLGregorianCalendar;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

import org.opennaas.core.utils.Helpers;

/**
 * @author gassen
 */
public class ConverterUtils {
    /**
     * @param val
     * @return
     */
    public static final Date convertExemption(final XMLGregorianCalendar val) {
        return Helpers.xmlCalendarToDate(val);
    }

    /**
     * @param val
     * @return
     */
    public static final Date convertExemption(final XMLGregorianCalendarImpl val) {
        return Helpers.xmlCalendarToDate(val);
    }

    /**
     * @param val
     * @return
     */
    public static final XMLGregorianCalendar convertExemption(final Date val) {
        return Helpers.DateToXmlCalendar(val);
    }

    /**
     * @param val
     * @return
     */
    public static final Object convertExemption(final Object val) {
        GuiLogger.getInternalLogger().error(
                "No method defined to convert " + val);

        return null;
    }
}
