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

/**
 * <p>
 * Java class for GetReservationsComplexType complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;GetReservationsComplexType&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;ID&quot; type=&quot;{http://ist_phosphorus.eu/nsp/webservice/reservation}ReservationIdentifierType&quot;/&gt;
 *         &lt;element name=&quot;Reservation&quot; type=&quot;{http://ist_phosphorus.eu/nsp/webservice/reservation}GetReservationResponseType&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
public class GetReservationsComplexType implements Serializable, Cloneable {

    /**
     *
     */
    private static final long serialVersionUID = -6362085284695090415L;
    protected String id;
    protected GetReservationResponseType reservation;

    /**
     * Gets the value of the id property.
     */
    public String getReservationID() {
        return this.id;
    }

    /**
     * Sets the value of the id property.
     */
    public void setReservationID(final String value) {
        this.id = value;
    }

    public boolean isSetID() {
        return true;
    }

    /**
     * Gets the value of the reservation property.
     * 
     * @return possible object is {@link GetReservationResponseType }
     */
    public GetReservationResponseType getReservation() {
        return this.reservation;
    }

    /**
     * Sets the value of the reservation property.
     * 
     * @param value
     *            allowed object is {@link GetReservationResponseType }
     */
    public void setReservation(final GetReservationResponseType value) {
        this.reservation = value;
    }

    public boolean isSetReservation() {
        return (this.reservation != null);
    }

}
