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

import client.classes.nsp.ReservationType;
import client.classes.nsp.StatusType;

/**
 * @author gassen
 */
public final class NspConverter extends Converter {

    public NspConverter() {
        super(
                "org.opennaas.extensions.idb.serviceinterface.databinding.jaxb",
                "client.classes.nsp");

        this.addExemption(Date.class, XMLGregorianCalendar.class);
        this.addExemption(XMLGregorianCalendar.class, Date.class);

        this
                .addExemption(
                        ReservationType.class,
                        org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ReservationType.class);
        this
                .addExemption(
                        org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.StatusType.class,
                        StatusType.class);
    }

    /**
     * Convert DomainStatusType Objects
     * 
     * @param src
     * @return
     */
    @Override
    public Object convertExemption(final Object src) {
        return ConverterUtils.convertExemption(src);
    }

    /**
     * Convert DomainStatusType Objects
     * 
     * @param src
     * @return
     */
    public org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ReservationType convertExemption(
            final ReservationType src) {
        return org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ReservationType
                .fromValue(src.value());
    }

    /**
     * Convert DomainStatusType Objects
     * 
     * @param src
     * @return
     */
    public StatusType convertExemption(
            final org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.StatusType src) {
        return new StatusType(src.value());
    }
}
