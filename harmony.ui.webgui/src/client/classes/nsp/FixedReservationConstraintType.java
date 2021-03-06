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
import java.util.Date;

/**
 * Constraints for fixed reservations
 * <p>
 * Java class for FixedReservationConstraintType complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;FixedReservationConstraintType&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;StartTime&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}dateTime&quot;/&gt;
 *         &lt;element name=&quot;Duration&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}int&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
public class FixedReservationConstraintType implements Serializable, Cloneable {

    /**
     *
     */
    private static final long serialVersionUID = -4072670240558685398L;
    protected Date startTime;
    protected int duration;

    /**
     * Gets the value of the startTime property.
     * 
     */
    public Date getStartTime() {
        return this.startTime;
    }

    /**
     * Sets the value of the startTime property.
     * 
     * @param value
     */
    public void setStartTime(final Date value) {
        this.startTime = value;
    }

    public boolean isSetStartTime() {
        return (this.startTime != null);
    }

    /**
     * Gets the value of the duration property.
     */
    public int getDuration() {
        return this.duration;
    }

    /**
     * Sets the value of the duration property.
     */
    public void setDuration(final int value) {
        this.duration = value;
    }

    public boolean isSetDuration() {
        return true;
    }

}
