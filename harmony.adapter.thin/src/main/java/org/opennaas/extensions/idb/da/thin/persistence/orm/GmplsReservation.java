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
package org.opennaas.extensions.idb.da.thin.persistence.orm;

/**
 * @author Daniel Beer (daniel.beer@iais.fraunhofer.de)
 */
public class GmplsReservation {

    /** */
    private long reservationId;
    /** */
    private long jobId;
    /** */
    private String notificationConsumerURL = "";

    /**
     *
     * @param reservationId
     * @param jobId
     * @param consumerURL
     */
    public GmplsReservation(final long reservationId, final long jobId, final String consumerURL) {
        this.reservationId = reservationId;
        this.jobId = jobId;
        this.notificationConsumerURL = consumerURL;
    }

    /**
     * @return the reservationId
     */
    public final long getReservationId() {
        return this.reservationId;
    }

    /**
     * @param reservationId
     *                the reservationId to set
     */
    public final void setReservationId(final long reservationId) {
        this.reservationId = reservationId;
    }

    /**
     * @return the jobId
     */
    public final long getJobId() {
        return this.jobId;
    }

    /**
     * @param jobId
     *                the jobId to set
     */
    public final void setJobId(final long jobId) {
        this.jobId = jobId;
    }

    /**
     * @return the notificationConsumerURL
     */
    public final String getNotificationConsumerURL() {
        return this.notificationConsumerURL;
    }

    /**
     * @param consumerURL
     *                the notificationConsumerURL to set
     */
    public final void setNotificationConsumerURL(final String consumerURL) {
        this.notificationConsumerURL = consumerURL;
    }

}
