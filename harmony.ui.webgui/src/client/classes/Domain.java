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


package client.classes;

import java.io.Serializable;

/**
 * Class to represent Domain informations.
 * 
 * @author gassen
 */
public class Domain implements Serializable {

    /** Version Id. */
    private static final long serialVersionUID = 1L;
    /** Domain Id. */
    private String id;
    /** Reservation EPR. */
    private String reservationEpr;
    /** Topology EPR. */
    private String topologyEpr;

    /**
     * Get Domain id.
     * 
     * @return the id
     */
    public final String getId() {
        return this.id;
    }

    /**
     * Get Reservation EPR.
     * 
     * @return the reservationEpr
     */
    public final String getReservationEpr() {
        return this.reservationEpr;
    }

    /**
     * Get Topology EPR.
     * 
     * @return the topologyEpr
     */
    public final String getTopologyEpr() {
        return this.topologyEpr;
    }

    /**
     * Set Domain Id.
     * 
     * @param id
     *            the id to set
     */
    public final void setId(final String id) {
        this.id = id;
    }

    /**
     * Set Reservation EPR.
     * 
     * @param reservationEpr
     *            the reservationEpr to set
     */
    public final void setReservationEpr(final String reservationEpr) {
        this.reservationEpr = reservationEpr;
    }

    /**
     * Set Topology EPR.
     * 
     * @param topologyEpr
     *            the topologyEpr to set
     */
    public final void setTopologyEpr(final String topologyEpr) {
        this.topologyEpr = topologyEpr;
    }

}
