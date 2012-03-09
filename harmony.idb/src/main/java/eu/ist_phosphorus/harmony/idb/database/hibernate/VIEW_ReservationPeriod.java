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


package eu.ist_phosphorus.harmony.idb.database.hibernate;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Proxy;

/**
 * Java representation of of the database entity {@link VIEW_ReservationPeriod}.
 * This object does not contain any logic.
 *
 * @author Alexander Zimmermann (zimmerm2@cs.uni-bonn.de)
 * @version 0.1
 */
@Entity
@Table(name = "VIEW_ReservationPeriod")
@Proxy(lazy = false)
public class VIEW_ReservationPeriod implements java.io.Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 2920240505647918320L;

    /**
     *
     */

    // Fields

    /**
     *
     */

    /** Primary key of the reservation for this period in the database. */
    private long reservationId;

    /**
     * Start time.
     */
    private Date startTime;

    /**
     * Duration.
     */
    private int duration;

    /**
     * End time.
     */
    private Date endTime;

    // Constructors

    /** default constructor. this is a view in the DB, therefore no constructor.
     *
     */
    private VIEW_ReservationPeriod() {
        // empty
    }

    /**
     * minimal constructor. this is a view in the DB, therefore no constructor.
     *
     * @param reservationIdParam
     *                id of the reservation
     * @param startTimeParam
     *                start time
     */
    private VIEW_ReservationPeriod(final int reservationIdParam,
                                                    final Date startTimeParam) {
        this.reservationId = reservationIdParam;
        this.startTime = startTimeParam;
        this.duration = 0;
        this.endTime = new Date();
    }

    /**
     * full constructor. this is a view in the DB, therefore no constructor.
     *
     * @param reservationIdParam
     *                id of the service
     * @param startTimeParam
     *                start time
     * @param durationParam
     *                duration
     * @param endTimeParam
     *                end time
     */
    private VIEW_ReservationPeriod(final long reservationIdParam,
                              final Date startTimeParam,
                             final int durationParam, final Date endTimeParam) {
        this.setReservationId(reservationIdParam);
        this.setStartTime(startTimeParam);
        this.setDuration(durationParam);
        this.setEndTime(endTimeParam);
    }

    // Property accessors

    /**
     * @return id of the corresponding reservation
     */
    @Id
    public final long getReservationId() {
        return this.reservationId;
    }

    /**
     * this is a view in the DB, therefore no setters.
     * @param reservationIdParam
     *                id of the corresponding reservation
     */
    private final void setReservationId(final long reservationIdParam) {
        this.reservationId = reservationIdParam;
    }

    /**
     * @return start time
     */
    public final Date getStartTime() {
        return this.startTime;
    }

    /**
     * this is a view in the DB, therefore no setters.
     * @param startTimeParam
     *                start time
     */
    private final void setStartTime(final Date startTimeParam) {
        this.startTime = startTimeParam;
    }

    /**
     * @return duration
     */
    @Basic(optional = true)
    public final int getDuration() {
        return this.duration;
    }

    /**
     * this is a view in the DB, therefore no setters.
     * @param durationParam
     *                duration
     */
    private final void setDuration(final int durationParam) {
        this.duration = durationParam;
    }

    /**
     * @return end time
     */
    @Basic(optional = true)
    public final Date getEndTime() {
        return this.endTime;
    }

    /**
     * this is a view in the DB, therefore no setters.
     * @param endTimeParam
     *                end time
     */
    private final void setEndTime(final Date endTimeParam) {
        this.endTime = endTimeParam;
    }

    /**
     * @param resPerParam
     *                ReservationPeriod to be checked
     * @return true if equals
     */
    public final boolean isEqual(final VIEW_ReservationPeriod resPerParam) {
    	if(this.hashCode() == resPerParam.hashCode()) {
    		return true;
    	}
    	return false;
    }

    /**
     * @param o another ReservationPeriod
     * @return true if equal, false else
     */
    @Override
    public final boolean equals(final Object o) {
        if (o.getClass() == VIEW_ReservationPeriod.class) {
            return isEqual((VIEW_ReservationPeriod) o);
        }
        return false;
    }

    /**
     * @return hashCode of the ReservationPeriod
     */
    @Override
    public final int hashCode() {
        int result = (Long.valueOf(getReservationId())).hashCode()
            ^ (Integer.valueOf(getDuration())).hashCode()
            ^ getStartTime().hashCode()
            ^ getEndTime().hashCode();
        return result;
    }

    /**
     * @return copy of ReservationPeriod
     */
    @Transient
    public final VIEW_ReservationPeriod getCopy() {
        VIEW_ReservationPeriod result = new VIEW_ReservationPeriod(this.reservationId,
                this.startTime, this.duration, this.endTime);
        return result;
    }

    /**
     * @param resPerParam
     *                ReservationPeriod to be compared to
     * @return -1 0 or 1
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public final int compareTo(final VIEW_ReservationPeriod resPerParam) {
        if (this.reservationId < resPerParam.getReservationId()) {
            return -1;
        } else if (this.reservationId == resPerParam.getReservationId()) {
            return 0;
        } else {
            return 1;
        }
    }
}
