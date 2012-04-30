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

package org.opennaas.extensions.idb.database.hibernate;

import java.util.Arrays;
import java.util.HashSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.EntityManager;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ConnectionConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.StatusType;
import org.opennaas.extensions.idb.database.TransactionManager;
import org.opennaas.extensions.idb.database.TransactionManagerLoad;
import org.opennaas.extensions.idb.exception.database.DatabaseException;

/**
 * Java representation of of the database entity {@link NrpsConnections}. This
 * object does not contain any logic.
 * 
 * @author Stephan Wagner (wagners@cs.uni-bonn.de)
 * @version 0.1
 */
@Entity
@Table(name = "NrpsConnection")
public class NrpsConnections implements java.io.Serializable {
	/** */
	private static final long serialVersionUID = 4589942413245237237L;

	/**
	 * @return the serialVersionUID
	 */
	public static long getSerialVersionUID() {
		return NrpsConnections.serialVersionUID;
	}

	/**
	 * Load connection from the DB.
	 * 
	 * @param dbKey
	 *            Primary key of connection in the database.
	 * @return Connection loaded.
	 * @throws DatabaseException
	 */
	public static NrpsConnections load(long dbKey) throws DatabaseException {
		return (NrpsConnections) (new TransactionManagerLoad(
				NrpsConnections.class, Long.valueOf(dbKey))).getResult();
	}

	private long pkNrpsConnection;

	private Connections connection;

	private Domain domain;

	private Endpoint sourceEndpoint;
	private Endpoint destinationEndpoint;
	private int bandwidth;
	/** directionality of the Connection. */
	private int directionality;

	/** latency of the the Connection. */
	private int latency;
	/** status of the the Connection. */
	private int status;
	/** */
	private static final int STATUS_ACTIVE = 1;
	/** */
	private static final int STATUS_CANCELLED_BY_SYSTEM = 2;
	/** */
	private static final int STATUS_CANCELLED_BY_USER = 3;
	/** */
	private static final int STATUS_COMPLETED = 4;
	/** */
	private static final int STATUS_PENDING = 5;
	/** */
	private static final int STATUS_SETUP_IN_PROGRESS = 6;

	// Constructors

	/** */
	private static final int STATUS_TEARDOWN_IN_PROGRESS = 7;

	/** */
	private static final int STATUS_UNKNOWN = 8;

	/**
	 * Create Connections object from JAXB input.
	 * 
	 * @param resConn
	 *            JAXB input
	 * @return Connections object created from JAXB input.
	 * @throws DatabaseException
	 */
	public static final NrpsConnections fromJaxb(
			final ConnectionConstraintType resConn) throws DatabaseException {
		final NrpsConnections result = new NrpsConnections();

		result.setBandwidth(resConn.getMinBW());
		if (resConn.isSetMaxBW()) {
			result.setBandwidth(resConn.getMaxBW());
		}
		if (resConn.isSetMaxDelay()) {
			result.setLatency(resConn.getMaxDelay().intValue());
		}
		result.setDirectionality(resConn.getDirectionality());

		final Endpoint ep = Endpoint.fromJaxb(resConn.getSource());
		result.setSourceEndpoint(ep);
		result.setDestinationEndpoint(Endpoint.fromJaxb(resConn.getTarget()
				.get(0)));
		return result;
	}

	/** default constructor. */
	public NrpsConnections() {
		// is Empty
	}

	public NrpsConnections(long pkNrpsConnection, Connections connection,
			Domain domain, Endpoint sourceEndpoint,
			Endpoint destinationEndpoint, int bandwidth, int directionality,
			int latency, int status) {
		super();
		this.setPkNrpsConnection(pkNrpsConnection);
		this.setConnection(connection);
		this.setDomain(domain);
		this.setSourceEndpoint(sourceEndpoint);
		this.setDestinationEndpoint(destinationEndpoint);
		this.setBandwidth(bandwidth);
		this.setDirectionality(directionality);
		this.setLatency(latency);
		this.setStatus(status);
	}

	/**
	 * @param connParam
	 *            connection to be compared to
	 * @return -1 0 or 1
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(NrpsConnections connParam) {
		if (this.getPkNrpsConnection() < connParam.getPkNrpsConnection()) {
			return -1;
		} else if (this.getPkNrpsConnection() == connParam
				.getPkNrpsConnection()) {
			return 0;
		} else {
			return 1;
		}
	}

	/**
	 * Deletes a {@link NrpsConnections} from the DB.
	 * 
	 * @param connection
	 *            instance of the {@link NrpsConnections} to be deleted
	 * @throws DatabaseException
	 */
	public void delete() throws DatabaseException {
		new TransactionManager(new HashSet<Object>(Arrays.asList(
				this.getConnection(), this.getDomain(),
				this.getSourceEndpoint(), this.getDestinationEndpoint()))) {
			@Override
			protected void dbOperation() {
				delete(this.session);
			}
		};
	}

	public void delete(EntityManager session) {
		session.remove(this);
	}

	/**
	 * @param o
	 * @return
	 */
	@Override
	public boolean equals(Object o) {
		if (o.getClass() == NrpsConnections.class) {
			return this.isEqual((NrpsConnections) o);
		}
		return false;
	}

	/**
	 * @return the bandwidth
	 */
	public int getBandwidth() {
		return this.bandwidth;
	}

	/**
	 * @return the connection
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_Connection")
	public Connections getConnection() {
		return this.connection;
	}

	/**
	 * @return copy of connection
	 */
	@Transient
	public NrpsConnections getCopy() {
		NrpsConnections newConn = new NrpsConnections(
				this.getPkNrpsConnection(), this.getConnection(),
				this.getDomain(), this.getSourceEndpoint(),
				this.getDestinationEndpoint(), this.getBandwidth(),
				this.getDirectionality(), this.getLatency(), this.getStatus());
		return newConn;
	}

	/**
	 * @return the destinationEndpoint
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_Destination")
	public Endpoint getDestinationEndpoint() {
		return this.destinationEndpoint;
	}

	/**
	 * Getter for field.
	 * 
	 * @return directionality
	 */
	public int getDirectionality() {
		return this.directionality;
	}

	/**
	 * @return the domain
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_Domain")
	public Domain getDomain() {
		return this.domain;
	}

	/**
	 * @return the latency
	 */
	public int getLatency() {
		return this.latency;
	}

	/**
	 * @return the pkNrpsConnection
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "PK_NrpsConnection")
	public long getPkNrpsConnection() {
		return this.pkNrpsConnection;
	}

	/**
	 * @return the sourceEndpoint
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_Source")
	public Endpoint getSourceEndpoint() {
		return this.sourceEndpoint;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public int getStatus() {
		return this.status;
	}

	/**
	 * @return the status
	 */
	@Transient
	public StatusType getStatusType() {

		switch (this.getStatus()) {
		case STATUS_ACTIVE:
			// logger.debug("\t\tNrpsConnection: " + getPkNrpsConnection()
			// + " Status" + StatusType.ACTIVE);
			return StatusType.ACTIVE;
		case STATUS_CANCELLED_BY_SYSTEM:
			// logger.debug("\t\tNrpsConnection: " + getPkNrpsConnection()
			// + " Status" + StatusType.CANCELLED_BY_SYSTEM);
			return StatusType.CANCELLED_BY_SYSTEM;
		case STATUS_CANCELLED_BY_USER:
			// logger.debug("\t\tNrpsConnection: " + getPkNrpsConnection()
			// + " Status" + StatusType.CANCELLED_BY_USER);
			return StatusType.CANCELLED_BY_USER;
		case STATUS_COMPLETED:
			// logger.debug("\t\tNrpsConnection: " + getPkNrpsConnection()
			// + " Status" + StatusType.COMPLETED);
			return StatusType.COMPLETED;
		case STATUS_PENDING:
			// logger.debug("\t\tNrpsConnection: " + getPkNrpsConnection()
			// + " Status" + StatusType.PENDING);
			return StatusType.PENDING;
		case STATUS_SETUP_IN_PROGRESS:
			// logger.debug("\t\tNrpsConnection: " + getPkNrpsConnection()
			// + " Status" + StatusType.SETUP_IN_PROGRESS);
			return StatusType.SETUP_IN_PROGRESS;
		case STATUS_TEARDOWN_IN_PROGRESS:
			// logger.debug("\t\tNrpsConnection: " + getPkNrpsConnection()
			// + " Status" + StatusType.TEARDOWN_IN_PROGRESS);
			return StatusType.TEARDOWN_IN_PROGRESS;
		case STATUS_UNKNOWN:
			// logger.debug("\t\tNrpsConnection: " + getPkNrpsConnection()
			// + " Status" + StatusType.UNKNOWN);
			return StatusType.UNKNOWN;
		default:
			throw new RuntimeException("No enum for int");
		}
	}

	@Override
	public int hashCode() {
		int result = new Long(this.getPkNrpsConnection()).hashCode()
				^ new Integer(this.getBandwidth()).hashCode()
				^ new Integer(this.getDirectionality()).hashCode()
				^ new Integer(this.getStatus()).hashCode()
				^ new Integer(this.getLatency()).hashCode();

		// in the underlying objects, don't use the hashCode()-method, because
		// this can end in
		// dependency-circles. Instead only use the DB-primary key for the hash.
		// result ^= ((this.getConnection() == null) ? 0 : new Long(this
		// .getConnection().getPK_Connections()).hashCode());
		// result ^= ((this.getDomain() == null) ? 0 :
		// this.getDomain().getName()
		// .hashCode());
		// result ^= ((this.getSourceEndpoint() == null) ? 0 : this
		// .getSourceEndpoint().getTNA().hashCode());
		// result ^= ((this.getDestinationEndpoint() == null) ? 0 : this
		// .getDestinationEndpoint().getTNA().hashCode());

		return result;
	}

	/**
	 * @param connParam
	 *            connection to be checked
	 * @return true if equals
	 */
	public boolean isEqual(NrpsConnections connParam) {
		if (this.hashCode() == connParam.hashCode()) {
			return true;
		}
		return false;
	}

	/**
	 * Saves a Connections object to the DB non-recursively, i.e. without the
	 * endpoints.
	 * 
	 * @throws DatabaseException
	 *             if entity could not be saved
	 */
	public void save() throws DatabaseException {
		new TransactionManager(new HashSet<Object>(Arrays.asList(
				getConnection(), getDomain(), this.getSourceEndpoint(),
				this.getDestinationEndpoint()))) {
			@Override
			protected void dbOperation() {
				save(this.session);
			}
		};
	}

	/**
	 * Saves a Connections object to the DB.
	 * 
	 * @param session
	 *            Database session to be used for saving
	 * @throws DatabaseException
	 *             if entity could not be saved
	 */
	public void save(EntityManager session) {
		session.persist(this);
	}

	/**
	 * @param bandwidth
	 *            the bandwidth to set
	 */
	public void setBandwidth(int bandwidth) {
		this.bandwidth = bandwidth;
	}

	/**
	 * @param connection
	 *            the connection to set
	 */
	public void setConnection(Connections connection) {
		this.connection = connection;
	}

	/**
	 * @param destinationEndpoint
	 *            the destinationEndpoint to set
	 */
	public void setDestinationEndpoint(Endpoint destinationEndpoint) {
		this.destinationEndpoint = destinationEndpoint;
	}

	/**
	 * Setter for field.
	 * 
	 * @param directionalityParam
	 *            directionality
	 */
	public void setDirectionality(int directionalityParam) {
		this.directionality = directionalityParam;
	}

	/**
	 * @param domain
	 *            the domain to set
	 */
	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	/**
	 * @param latency
	 *            the latency to set
	 */
	public void setLatency(int latency) {
		this.latency = latency;
	}

	/**
	 * @param pkNrpsConnection
	 *            the pkNrpsConnection to set
	 */
	public void setPkNrpsConnection(long pkNrpsConnection) {
		this.pkNrpsConnection = pkNrpsConnection;
	}

	/**
	 * @param sourceEndpoint
	 *            the sourceEndpoint to set
	 */
	public void setSourceEndpoint(Endpoint sourceEndpoint) {
		this.sourceEndpoint = sourceEndpoint;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(StatusType status) {
		switch (status) {
		case ACTIVE:
			this.setStatus(NrpsConnections.STATUS_ACTIVE);
			break;
		case CANCELLED_BY_SYSTEM:
			this.setStatus(NrpsConnections.STATUS_CANCELLED_BY_SYSTEM);
			break;
		case CANCELLED_BY_USER:
			this.setStatus(NrpsConnections.STATUS_CANCELLED_BY_USER);
			break;
		case COMPLETED:
			this.setStatus(NrpsConnections.STATUS_COMPLETED);
			break;
		case PENDING:
			this.setStatus(NrpsConnections.STATUS_PENDING);
			break;
		case SETUP_IN_PROGRESS:
			this.setStatus(NrpsConnections.STATUS_SETUP_IN_PROGRESS);
			break;
		case TEARDOWN_IN_PROGRESS:
			this.setStatus(NrpsConnections.STATUS_TEARDOWN_IN_PROGRESS);
			break;
		case UNKNOWN:
			this.setStatus(NrpsConnections.STATUS_UNKNOWN);
			break;
		default:
			break;
		}
	}

	public Connections toConnnections(Connections parentConnection) {

		Connections result = new Connections(
				parentConnection.getConnectionId(),
				parentConnection.getService(), this.getBandwidth(),
				this.getBandwidth(), this.getLatency(), this.directionality,
				parentConnection.getDataAmount(), this.getSourceEndpoint());
		result.getEndpoints().add(this.getDestinationEndpoint());
		return result;
	}

}
