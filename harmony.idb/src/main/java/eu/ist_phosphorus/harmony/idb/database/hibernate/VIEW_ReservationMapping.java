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

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.FetchMode;
import org.hibernate.annotations.Proxy;
import org.hibernate.criterion.Restrictions;

import eu.ist_phosphorus.harmony.idb.database.TransactionManager;
import eu.ist_phosphorus.harmony.idb.exceptions.database.DatabaseException;

/**
 * Java representation of of the database entity {@link VIEW_ReservationMapping}
 * . This object does not contain any logic.
 * 
 * @author Alexander Zimmermann (zimmerm2@cs.uni-bonn.de)
 * @version 0.1
 */
@Entity
@Table(name = "VIEW_ReservationMapping")
@Proxy(lazy = false)
public class VIEW_ReservationMapping implements java.io.Serializable {

    private static final long serialVersionUID = 2920240505647918320L;

    // Fields

    /** Primary key of the NSP-reservation in the database. */
    private long reservationId;

    /**
     * jobId of the NSP-reservation.
     */
    private long jobId;

    /**
     * ReservationId of the NRPS-reservation.
     */
    private long nrpsReservationId;

    /**
     * domain.
     */
    private Domain domain;

    /**
     * pkService
     */
    private Service service;

    /**
     * user serviceId
     */
    private int serviceId;

    /**
     * pkConnection
     */
    private Connections connection;

    // Constructors

    /**
     * user connectionId
     */
    private int connectionId;

    // Property accessors

    /**
     * pkNrpsConnection
     */
    private NrpsConnections nrpsConnection;

    /**
     * default constructor. this is a view in the DB, therefore no constructor.
     * 
     */
    private VIEW_ReservationMapping() {
        // empty
    }

    public int getConnectionId() {
        return this.connectionId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_domainName")
    public Domain getDomain() {
        return this.domain;
    }

    @Basic(optional = true)
    public long getJobId() {
        return this.jobId;
    }

    public long getNrpsReservationId() {
        return this.nrpsReservationId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PK_Connections")
    public Connections getConnection() {
        return this.connection;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PK_NrpsConnection")
    public NrpsConnections getNrpsConnection() {
        return this.nrpsConnection;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PK_service")
    public Service getService() {
        return this.service;
    }

    @Id
    public long getReservationId() {
        return this.reservationId;
    }

    public int getServiceId() {
        return this.serviceId;
    }

    /**
     * @return hashCode of the ReservationMapping
     */
    @Override
    public final int hashCode() {
        final int result = (Long.valueOf(this.getReservationId())).hashCode()
                ^ (Long.valueOf(this.getJobId())).hashCode()
                ^ (Long.valueOf(this.getNrpsReservationId())).hashCode()
//                ^ this.getDomain().hashCode()
//                ^ (Long.valueOf(this.getService().getPK_service()).hashCode()
                ^ (Integer.valueOf(this.getServiceId())).hashCode()
//                        ^ (Long.valueOf(this.getConnection().getPK_Connections())).hashCode()
                ^ (Integer.valueOf(this.getConnectionId())).hashCode() 
//                        ^ (Long
//                        .valueOf(this.getNrpsConnection().getPkNrpsConnection())).hashCode());
                        ;
        return result;
    }

    /**
     * @param resMapParam
     *            ReservationMapping to be checked
     * @return true if equals
     */
    public final boolean isEqual(final VIEW_ReservationMapping resMapParam) {
    	if(this.hashCode() == resMapParam.hashCode()) {
    		return true;
    	}
    	return false;
    }

    /**
     * @param resMapParam
     *            ReservationMapping to be compared to
     * @return -1 0 or 1
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public final int compareTo(final VIEW_ReservationMapping resMapParam) {
        if (this.getReservationId() < resMapParam.getReservationId()) {
            return -1;
        } else if (this.getReservationId() == resMapParam.getReservationId()) {
            return 0;
        } else {
            return 1;
        }
    }

    /**
     * @param o
     *            another ReservationMapping
     * @return true if equal, false else
     */
    @Override
    public final boolean equals(final Object o) {
        if (o.getClass() == VIEW_ReservationMapping.class) {
            return this.isEqual((VIEW_ReservationMapping) o);
        }
        return false;
    }

    /**
     * @return copy of ReservationMapping
     */
    @Transient
    public final VIEW_ReservationMapping getCopy() {
        final VIEW_ReservationMapping result = new VIEW_ReservationMapping();
        result.setReservationId(this.getReservationId());
        result.setJobId(this.getJobId());
        result.setNrpsReservationId(this.getNrpsReservationId());
        result.setDomain(this.getDomain());
        result.setService(this.getService());
        result.setServiceId(this.getServiceId());
        result.setConnection(this.getConnection());
        result.setConnectionId(this.getConnectionId());
        result.setNrpsConnection(this.getNrpsConnection());
        return result;
    }

    public void setConnectionId(final int connectionId) {
        this.connectionId = connectionId;
    }

    public void setDomain(final Domain domain) {
        this.domain = domain;
    }

    public void setJobId(final long jobId) {
        this.jobId = jobId;
    }

    public void setNrpsReservationId(final long nrpsReservationId) {
        this.nrpsReservationId = nrpsReservationId;
    }

    public void setConnection(final Connections connectionParam) {
        this.connection = connectionParam;
    }

    public void setNrpsConnection(final NrpsConnections nrpsConnectionParam) {
        this.nrpsConnection = nrpsConnectionParam;
    }

    public void setService(final Service serviceParam) {
        this.service = serviceParam;
    }

    public void setReservationId(final long reservationIdParam) {
        this.reservationId = reservationIdParam;
    }

    public void setServiceId(final int serviceId) {
        this.serviceId = serviceId;
    }

    @Transient
    @SuppressWarnings("unchecked")
    public static final List<VIEW_ReservationMapping> getMappingsForNRPSResID(
            final long resId) throws DatabaseException {
        final List<VIEW_ReservationMapping> mappings = (List<VIEW_ReservationMapping>) (new TransactionManager(
                new Long(resId)) {
            @Override
            protected void dbOperation() {
                this.result = this.session.createCriteria(
                        VIEW_ReservationMapping.class).setFetchMode("",
                        FetchMode.SELECT).add(
                        Restrictions.like("nrpsReservationId", this.arg))
                        .list();
            }
        }).getResult();
        return mappings;
    }

    @Transient
    @SuppressWarnings("unchecked")
    public static final List<VIEW_ReservationMapping> getMappingsForNSPResID(
            final long resId) throws DatabaseException {
        final List<VIEW_ReservationMapping> mappings = (List<VIEW_ReservationMapping>) (new TransactionManager(
                new Long(resId)) {
            @Override
            protected void dbOperation() {
                this.result = this.session.createCriteria(
                        VIEW_ReservationMapping.class).setFetchMode("",
                        FetchMode.SELECT).add(
                        Restrictions.like("reservationId", this.arg)).list();
            }
        }).getResult();
        return mappings;
    }
}
