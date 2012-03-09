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

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.annotations.Proxy;
import org.hibernate.criterion.Restrictions;

import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.exceptions.InvalidReservationIDFaultException;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.utils.WebserviceUtils;
import eu.ist_phosphorus.harmony.idb.database.DbConnectionManager;
import eu.ist_phosphorus.harmony.idb.database.TransactionManager;
import eu.ist_phosphorus.harmony.idb.exceptions.database.DatabaseException;


/**
 * Java representation of of the database entity {@link MAPNRPSResvID}. This
 * object does not contain any logic.
 *
 * @author Stephan Wagner (wagners@cs.uni-bonn.de)
 * @version 0.1
 */
@Entity
@Table(name = "MAP_NRPSResvID")
@Proxy(lazy = false)
public class MAPNRPSResvID implements java.io.Serializable {
    /** */
    private static final long serialVersionUID = 4589942413245237237L;
    /** autogenerated ID of the mapping. */
    private long PK_NRPSResvID;

    /** Database key of the reservation. */
    private Reservation reservation;

    /**
     * ID of the reservation in the Nrps.
     */
    private long nrpsReservationId;
    /**
     * ID of the domain the reservation belongs to.
     */
    private Domain domain;

    // Constructors

    /** default constructor. */
    public MAPNRPSResvID() {
        // is Empty
    }

    /**
     * full constructor.
     *
     * @param fkReservationIdParam
     *                initial value
     * @param nrpsReservationIdParam
     *                initial value
     * @param fkDomainNameParam
     *                initial value
     */
    public MAPNRPSResvID(final Reservation reservationParam,
            final long nrpsReservationIdParam, final Domain domainParam) {
        this.PK_NRPSResvID = 0;
        this.reservation = reservationParam;
        this.nrpsReservationId = nrpsReservationIdParam;
        this.domain = domainParam;
    }

    /**
     * full constructor.
     *
     * @param fkReservationIdParam
     *                initial value
     * @param nrpsReservationIdParam
     *                initial value
     * @param fkDomainNameParam
     *                initial value
     * @throws InvalidReservationIDFaultException
     */
    public MAPNRPSResvID(final Reservation reservationParam,
            final String nrpsReservationIdParam, final Domain domainParam) throws InvalidReservationIDFaultException {
        this.PK_NRPSResvID = 0;
        this.reservation = reservationParam;
        this.nrpsReservationId = WebserviceUtils.convertReservationID(nrpsReservationIdParam);
        this.domain = domainParam;
    }

    /**
     * Getter for field.
     *
     * @return pkNRPSResvID
     */
    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    public final long getPK_NRPSResvID() {
        return this.PK_NRPSResvID;
    }

    /**
     * Setter for field.
     *
     * @param pkResConnDomParam
     *                autogenerated ID of the mapping
     */
    public final void setPK_NRPSResvID(final long pkResConnDomParam) {
        this.PK_NRPSResvID = pkResConnDomParam;
    }

    /**
     * Getter for field.
     *
     * @return fkReservationId
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_reservationID")
    public final Reservation getReservation() {
        return this.reservation;
    }

    /**
     * Setter for field.
     *
     * @param fkReservationIdParam
     *                ID of the reservation
     */
    public final void setReservation(final Reservation reservationParam) {
        this.reservation = reservationParam;
    }

    /**
     * Getter for field.
     *
     * @return nrpsReservationId
     */
    public final long getnrpsReservationId() {
        return this.nrpsReservationId;
    }

    /**
     * Setter for field.
     *
     * @param nrpsReservationIdParam
     *                Id of the Reservation in the Nrps
     */
    public final void setnrpsReservationId(final long nrpsReservationIdParam) {
        this.nrpsReservationId = nrpsReservationIdParam;
    }

    /**
     * Getter for field.
     *
     * @return fkDomainName
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_domainName")
    public final Domain getDomain() {
        return this.domain;
    }

    /**
     * Setter for field.
     *
     * @param fkDomainNameParam
     *                Id of the domain
     */
    public final void setDomain(final Domain domainParam) {
        this.domain = domainParam;
    }

    /**
     * @param resParam
     *                mapping to be checked
     * @return true if equals
     */
    public final boolean isEqual(final MAPNRPSResvID mapParam) {
    	if(this.hashCode() == mapParam.hashCode()) {
    		return true;
    	}
    	return false;
    }

    /**
     * @param o
     * @return
     */
    @Override
    public final boolean equals(final Object o) {
        if (o.getClass() == MAPNRPSResvID.class) {
            return isEqual((MAPNRPSResvID) o);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = new Long(this.getPK_NRPSResvID()).hashCode()
                 ^ new Long(this.getnrpsReservationId()).hashCode();
//                 ^ new Long(this.getReservation().getReservationId()).hashCode()
//                 ^ this.getDomain().getName().hashCode();

        return result;
    }

    /**
     * @return copy of MAPNRPSResvID
     */
    @Transient
    public final MAPNRPSResvID getCopy() {
    	MAPNRPSResvID copy =
                new MAPNRPSResvID(this.getReservation(), this.getnrpsReservationId(),
                        this.getDomain());
        return copy;
    }

    @Override
    public final String toString() {
        return "<pk>" + this.getPK_NRPSResvID() + "</pk>" 
                + "<reservation>" + this.getReservation().getReservationId()
                + "</reservation><nrpsReservationId>" + this.getnrpsReservationId()
                + "</nrpsReservationId><domain>" + this.getDomain().getName()
                + "</domain>";
    }

    /**
     * @param mapParam
     *                MAPNRPSResvID to be compared to
     * @return -1 0 or 1
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public final int compareTo(final MAPNRPSResvID mapParam) {
        if (this.getPK_NRPSResvID() < mapParam.getPK_NRPSResvID()) {
            return -1;
        } else if (this.getPK_NRPSResvID() == mapParam.getPK_NRPSResvID()) {
            return 0;
        } else {
            return 1;
        }
    }


    /**
     * Method to save a {@link MAPNRPSResvID} to the DB.
     *
     * @param MAPNRPSResvID
     *                {@link MAPNRPSResvID} to be saved
     * @throws DatabaseException
     *                 if entity could not be saved
     */
    public final void save() throws DatabaseException {
        new TransactionManager() {
            @Override
            protected void dbOperation() throws Exception {
                save(this.session);
            }
        };
    }

    public final void save(Session session) {
    	session.saveOrUpdate(this);
    }

    @Transient
    @SuppressWarnings("unchecked")
    public static final List<MAPNRPSResvID> getMappingForReservation(final Reservation res) throws DatabaseException {
        final Session session = DbConnectionManager.openSession();
        final List<MAPNRPSResvID> tmpMAPResConnDom = session
                              .createCriteria(MAPNRPSResvID.class)
                              .setFetchMode("", FetchMode.SELECT)
                              .add(Restrictions.like("reservation", res))
                              .list();
        session.close();
        return tmpMAPResConnDom;
    }
}
