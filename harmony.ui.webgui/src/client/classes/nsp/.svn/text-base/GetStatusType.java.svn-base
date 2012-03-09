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


package client.classes.nsp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GetStatusType implements Serializable, Cloneable {
    /**
     *
     */
    private static final long serialVersionUID = 2201250027105485381L;
    protected String reservationID;
    protected List<java.lang.Integer> serviceID;

    /**
     * Gets the value of the reservationID property.
     */
    public String getReservationID() {
        return this.reservationID;
    }

    /**
     * Sets the value of the reservationID property.
     */
    public void setReservationID(final String value) {
        this.reservationID = value;
    }

    public boolean isSetReservationID() {
        return true;
    }

    /**
     * Gets the value of the serviceID property.
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the serviceID property.
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getServiceID().add(newItem);
     * </pre>
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list {@link Integer }
     */
    public List<java.lang.Integer> getServiceID() {
        if (this.serviceID == null) {
            this.serviceID = new ArrayList<java.lang.Integer>();
        }
        return this.serviceID;
    }

    public boolean isSetServiceID() {
        return ((this.serviceID != null) && (!this.serviceID.isEmpty()));
    }

    public void unsetServiceID() {
        this.serviceID = null;
    }

}
