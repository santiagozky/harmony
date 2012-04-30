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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;

import com.mysema.query.jpa.impl.JPAQuery;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ConnectionConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ConnectionStatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DomainConnectionStatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.StatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.EndpointNotFoundFaultException;
import org.opennaas.core.utils.PhLogger;
import org.opennaas.extensions.idb.database.TransactionManager;
import org.opennaas.extensions.idb.database.TransactionManagerLoad;
import org.opennaas.extensions.idb.exception.database.DatabaseException;

/**
 * Java representation of of the database entity {@link Connections}. This
 * object does not contain any logic.
 * 
 * @author Stephan Wagner (wagners@cs.uni-bonn.de)
 * @version 0.1
 */
@Entity
@Table(name = "Connections")
public class Connections implements java.io.Serializable {
	/** */
	private static final long serialVersionUID = 4589942413245237237L;
	private static Logger logger = PhLogger.getLogger();
	/** primary key in the DB */
	private long PK_Connections;

	/** Id of the Connection (only unique within resvID/serviceID!). */
	private int connectionId;

	/** service the Connection belongs to. */
	private Service service;
	/** minimal bandwidth that the Connection will use. */
	private int minBandwidth;
	/** maximal bandwidth that the Connection will use. */
	private int maxBandwidth;
	/** minimal latency that the Connection allows. */
	private int maxLatency;
	/** directionality of the Connection. */
	private int directionality;
	/** dataAmount of the Connection. */
	private int dataAmount;
	/** Endpoint at which the Connection starts. */
	private Endpoint startpoint;
	/** Destination of the Connection. */
	private Set<Endpoint> endpoints = new HashSet<Endpoint>(0);

	private Map<Domain, NrpsConnections> nrpsConnections = new HashMap<Domain, NrpsConnections>(
			0);

	// Constructors

	/** default constructor. */
	public Connections() {
		// is Empty
	}

	/**
	 * minimal constructor.
	 * 
	 * @param connectionIdParam
	 *            initial value
	 * @param ServiceParam
	 *            initial value
	 * @param minBandwidthParam
	 *            initial value
	 * @param maxBandwidthParam
	 *            initial value
	 * @param directionalityParam
	 *            initial value
	 * @param startpointParam
	 *            initial value
	 */
	public Connections(int connectionIdParam, Service serviceParam,
			int minBandwidthParam, int maxBandwidthParam,
			int directionalityParam, Endpoint startpointParam) {
		this.PK_Connections = 0;
		this.connectionId = connectionIdParam;
		this.service = serviceParam;
		this.minBandwidth = minBandwidthParam;
		this.maxBandwidth = maxBandwidthParam;
		this.maxLatency = 0;
		this.directionality = directionalityParam;
		this.dataAmount = 0;
		this.startpoint = startpointParam;
	}

	/**
	 * full constructor.
	 * 
	 * @param connectionIdParam
	 *            initial value
	 * @param serviceParam
	 *            initial value
	 * @param minBandwidthParam
	 *            initial value
	 * @param maxBandwidthParam
	 *            initial value
	 * @param maxLatencyParam
	 *            initial value
	 * @param directionalityParam
	 *            initial value
	 * @param dataAmountParam
	 *            initial value
	 * @param startpointParam
	 *            initial value
	 */
	public Connections(int connectionIdParam, Service serviceParam,
			int minBandwidthParam, int maxBandwidthParam, int maxLatencyParam,
			int directionalityParam, int dataAmountParam,
			Endpoint startpointParam) {
		this.PK_Connections = 0;
		this.connectionId = connectionIdParam;
		this.service = serviceParam;
		this.minBandwidth = minBandwidthParam;
		this.maxBandwidth = maxBandwidthParam;
		this.maxLatency = maxLatencyParam;
		this.directionality = directionalityParam;
		this.dataAmount = dataAmountParam;
		this.startpoint = startpointParam;
	}

	/**
	 * Getter for field.
	 * 
	 * @return connectionId
	 */
	public int getConnectionId() {
		return this.connectionId;
	}

	/**
	 * @return the pkConnection
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getPK_Connections() {
		return this.PK_Connections;
	}

	/**
	 * @param pkConnection
	 *            the pkConnection to set
	 */
	public void setPK_Connections(long pkConnection) {
		this.PK_Connections = pkConnection;
	}

	/**
	 * Setter for field.
	 * 
	 * @param connectionIdParam
	 *            ID of the Connection
	 */
	public void setConnectionId(int connectionIdParam) {
		this.connectionId = connectionIdParam;
	}

	/**
	 * Getter for field.
	 * 
	 * @return service
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_Service")
	public Service getService() {
		return this.service;
	}

	/**
	 * Setter for field.
	 * 
	 * @param serviceParam
	 *            service
	 */
	public void setService(Service serviceParam) {
		this.service = serviceParam;
	}

	/**
	 * Getter for field.
	 * 
	 * @return minBandwidth
	 */
	public int getMinBandwidth() {
		return this.minBandwidth;
	}

	/**
	 * Setter for field.
	 * 
	 * @param minBandwidthParam
	 *            min bandwidth used by Connection
	 */
	public void setMinBandwidth(int minBandwidthParam) {
		this.minBandwidth = minBandwidthParam;
	}

	/**
	 * Getter for field.
	 * 
	 * @return maxBandwidth
	 */
	public int getMaxBandwidth() {
		return this.maxBandwidth;
	}

	/**
	 * Setter for field.
	 * 
	 * @param maxBandwidthParam
	 *            max. bandwidth used by Connection
	 */
	public void setMaxBandwidth(int maxBandwidthParam) {
		this.maxBandwidth = maxBandwidthParam;
	}

	/**
	 * Getter for field.
	 * 
	 * @return maxLatency
	 */
	@Basic(optional = true)
	public int getMaxLatency() {
		return this.maxLatency;
	}

	/**
	 * Setter for field.
	 * 
	 * @param maxLatencyParam
	 *            max. latency allowed for this Connection
	 */
	public void setMaxLatency(int maxLatencyParam) {
		this.maxLatency = maxLatencyParam;
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
	 * Setter for field.
	 * 
	 * @param directionalityParam
	 *            directionality
	 */
	public void setDirectionality(int directionalityParam) {
		this.directionality = directionalityParam;
	}

	/**
	 * Getter for field.
	 * 
	 * @return dataAmount
	 */
	@Basic(optional = true)
	public int getDataAmount() {
		return this.dataAmount;
	}

	/**
	 * Setter for field.
	 * 
	 * @param dataAmountParam
	 *            dataAmount
	 */
	public void setDataAmount(int dataAmountParam) {
		this.dataAmount = dataAmountParam;
	}

	/**
	 * Getter for field.
	 * 
	 * @return startpoint
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_StartpointTNA", nullable = false)
	public Endpoint getStartpoint() {
		return this.startpoint;
	}

	/**
	 * Setter for field.
	 * 
	 * @param startpointParam
	 *            Start of Connection
	 */
	public void setStartpoint(Endpoint startpointParam) {
		this.startpoint = startpointParam;
	}

	/**
	 * Getter for field.
	 * 
	 * @return startpoint
	 */
	// @ManyToMany(fetch = FetchType.LAZY)
	// @JoinTable(name = "MAP_ConnEndpoint", joinColumns = @JoinColumn(name =
	// "FK_Connection"), inverseJoinColumns = @JoinColumn(name =
	// "FK_DestEndpointTNA"))
	@ManyToMany(mappedBy = "endpointInConnections")
	public Set<Endpoint> getEndpoints() {
		return this.endpoints;
	}

	/**
	 * Setter for field.
	 * 
	 * @param endpointsParam
	 *            destination of the Connection
	 */
	public void setEndpoints(Set<Endpoint> endpointsParam) {
		this.endpoints = endpointsParam;
	}

	public void addEndpoint(Endpoint endpointParam) {
		getEndpoints().add(endpointParam);
	}

	/**
	 * @return the nrpsConnections
	 */
	@OneToMany(mappedBy = "connection", fetch = FetchType.LAZY, cascade = { javax.persistence.CascadeType.ALL })
	@MapKey(name = "domain")
	public Map<Domain, NrpsConnections> getNrpsConnections() {
		return this.nrpsConnections;
	}

	/**
	 * @param nrpsConnections
	 *            the nrpsConnections to set
	 */
	public void setNrpsConnections(Map<Domain, NrpsConnections> nrpsConnections) {
		this.nrpsConnections = nrpsConnections;
	}

	/**
	 * @param connParam
	 *            connection to be checked
	 * @return true if equals
	 */
	public boolean isEqual(Connections connParam) {
		if (this.hashCode() == connParam.hashCode()) {
			return true;
		}
		return false;
	}

	/**
	 * @param o
	 * @return
	 */
	@Override
	public boolean equals(Object o) {
		if (o.getClass() == Connections.class) {
			return isEqual((Connections) o);
		}
		return false;
	}

	@Override
	public int hashCode() {
		int result = Long.valueOf(getPK_Connections()).hashCode()
				^ new Integer(this.getConnectionId()).hashCode()
				^ new Integer(this.getMinBandwidth()).hashCode()
				^ new Integer(this.getMaxBandwidth()).hashCode()
				^ new Integer(this.getMaxLatency()).hashCode()
				^ new Integer(this.getDirectionality()).hashCode()
				^ new Integer(this.getDataAmount()).hashCode();

		// in the underlying objects, don't use the hashCode()-method, because
		// this can end in
		// dependency-circles. Instead only use the DB-primary key for the hash.
		// result ^= ((this.getStartpoint() == null) ? 0 : this.getStartpoint()
		// .getTNA().hashCode());

		// for ( Endpoint e : getEndpoints()) {
		// result ^= e.getTNA().hashCode();
		// }

		// for (NrpsConnections nrpsConn : getNrpsConnections().values()) {
		// result ^= new Long(nrpsConn.getPkNrpsConnection()).hashCode();
		// }

		// result ^= new Long(this.getService().getPK_service()).hashCode();

		return result;
	}

	/**
	 * @param connParam
	 *            connection to be compared to
	 * @return -1 0 or 1
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Connections connParam) {
		if (this.connectionId < connParam.connectionId) {
			return -1;
		} else if (this.connectionId == connParam.connectionId) {
			return 0;
		} else {
			return 1;
		}
	}

	/**
	 * @return copy of connection
	 */
	@Transient
	public Connections getCopy() {
		Connections newConn = new Connections(this.connectionId, this.service,
				this.minBandwidth, this.maxBandwidth, this.maxLatency,
				this.directionality, this.dataAmount, this.startpoint);
		newConn.setPK_Connections(this.getPK_Connections());
		for (Endpoint endpoint : this.endpoints) {
			newConn.getEndpoints().add(endpoint);
		}

		for (Entry<Domain, NrpsConnections> entry : getNrpsConnections()
				.entrySet()) {
			newConn.getNrpsConnections().put(entry.getKey(), entry.getValue());
		}
		return newConn;
	}

	/**
	 * Create Connections object from JAXB input.
	 * 
	 * @param resConn
	 *            JAXB input
	 * @return Connections object created from JAXB input.
	 * @throws DatabaseException
	 * @throws EndpointNotFoundFaultException
	 */
	public static Connections fromJaxb(ConnectionConstraintType resConn)
			throws EndpointNotFoundFaultException, DatabaseException {
		Connections result = new Connections();
		result.setConnectionId(resConn.getConnectionID());
		result.setMinBandwidth(resConn.getMinBW());
		if (resConn.isSetMaxBW()) {
			result.setMaxBandwidth(resConn.getMaxBW());
		}
		if (resConn.isSetMaxDelay()) {
			result.setMaxLatency(resConn.getMaxDelay().intValue());
		}
		result.setDirectionality(resConn.getDirectionality());
		if (resConn.isSetDataAmount()) {
			result.setDataAmount(resConn.getDataAmount().intValue());
		}
		Endpoint ep = Endpoint.fromJaxb(resConn.getSource());
		result.setStartpoint(ep);
		result.getEndpoints().clear();
		for (EndpointType t : resConn.getTarget()) {
			result.getEndpoints().add(Endpoint.fromJaxb(t));
		}
		result.loadOrCreateUserEndpoints();
		return result;
	}

	/**
	 * Create Connections object from JAXB input.
	 * 
	 * @return ConnectionConstraintType object representing this connection.
	 */
	public ConnectionConstraintType toJaxb() {

		ConnectionConstraintType result = new ConnectionConstraintType();

		result.setConnectionID(this.getConnectionId());

		result.setMinBW(this.getMinBandwidth());
		if (this.getMaxBandwidth() > 0) {
			result.setMaxBW(new Integer(this.getMaxBandwidth()));
		}
		if (this.getMaxLatency() > 0) {
			result.setMaxDelay(new Integer(this.getMaxLatency()));
		}
		result.setDirectionality(this.getDirectionality());
		if (this.getDataAmount() > 0) {
			result.setDataAmount(new Long(this.getDataAmount()));
		}
		result.setSource(this.getStartpoint().toJaxb());
		result.getTarget().clear();
		for (Endpoint t : this.getEndpoints()) {
			result.getTarget().add(t.toJaxb());
		}
		return result;
	}

	/**
	 * load Endpoints from DB. This will override all stored endpoints!! TODO:
	 * check this query, fkConnection seems not to exists
	 */
	@SuppressWarnings("unchecked")
	public void loadNrpsConnections() throws DatabaseException {
		throw new RuntimeException("operation not available");
		// // get nrpsConnections from DB
		// List<NrpsConnections> ncList = (List<NrpsConnections>) (new
		// TransactionManager(
		// new Long(this.PK_Connections)) {
		// @Override
		// protected void dbOperation() {
		// QNrpsConnections nrpsCon = QNrpsConnections.nrpsConnections;
		// JPAQuery query = new JPAQuery(this.session);
		//
		// this.result = this.session
		// .createCriteria(NrpsConnections.class)
		// .setFetchMode("", FetchMode.SELECT)
		// .add(Restrictions.like("fkConnection", this.arg))
		// .list();
		// }
		// }).getResult();
		//
		// // clear old nrpsConnections
		// this.nrpsConnections.clear();
		// // put new nrpsConnections from DB into the set
		// for (NrpsConnections nc : ncList) {
		// this.nrpsConnections.put(nc.getDomain(), nc);
		// }
	}

	/**
	 * Load connection from the DB.
	 * 
	 * @param dbKey
	 *            Primary key of connection in the database.
	 * @return Connection loaded.
	 * @throws DatabaseException
	 */
	public static Connections load(long dbKey) throws DatabaseException {
		return (Connections) (new TransactionManagerLoad(Connections.class,
				Long.valueOf(dbKey))).getResult();
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
	 * Saves a Connections object to the DB.
	 * 
	 * @throws DatabaseException
	 *             if entity could not be saved
	 */
	public void save() throws DatabaseException {
		HashSet<Object> refresh = new HashSet<Object>(Arrays.asList(service,
				startpoint));
		refresh.addAll(getEndpoints());
		refresh.addAll(getNrpsConnections().values());
		new TransactionManager(refresh) {
			@Override
			protected void dbOperation() {
				save(this.session);
			}
		};
	}

	public void delete(EntityManager session) {
		session.remove(this);
	}

	/**
	 * Deletes a {@link Connections} from the DB.
	 * 
	 * @param connection
	 *            instance of the {@link Connections} to be deleted
	 * @throws DatabaseException
	 */
	public void delete() throws DatabaseException {
		HashSet<Object> refresh = new HashSet<Object>(Arrays.asList(service,
				startpoint));
		refresh.addAll(endpoints);
		refresh.addAll(nrpsConnections.values());
		new TransactionManager(refresh) {
			@Override
			protected void dbOperation() {
				delete(this.session);
			}
		};
	}

	/**
	 * Get connections in which a ConnectionEndpoint is participant.
	 * 
	 * @param epId
	 *            Id of the ConnectionEndpoint
	 * @return List of connections in which the ConnectionEndpoint participates
	 * @throws DatabaseException
	 */
	/*
	 * @SuppressWarnings("unchecked") protected Set<Connections>
	 * getConnectionsByEndpt( String cepId) throws DatabaseException { String
	 * epId = Endpoint.normalizeIPv4(cepId); EntityManager session =
	 * DbConnectionManager.openSession(); Set<Connections> result = new
	 * HashSet<Connections>(0); List<Connections> tmpCon1 =
	 * session.createCriteria(Connections.class).setFetchMode("",
	 * FetchMode.SELECT).add( Expression.sql("FK_StartpointID=" + epId)).list();
	 * List<MAPConnEndpoint> tmpCon2 =
	 * session.createCriteria(MAPConnEndpoint.class).setFetchMode("",
	 * FetchMode.SELECT).add( Expression.sql("FK_DestEndPointID=" +
	 * epId)).list(); session.close(); for (Connections c : tmpCon1) {
	 * result.add(c); } for (MAPConnEndpoint c : tmpCon2) { long cKey =
	 * c.getFkConnection(); result.add(Connections.load(cKey)); } return result;
	 * }
	 */

	public void loadOrCreateUserEndpoints() throws DatabaseException,
			EndpointNotFoundFaultException {
		setStartpoint(Endpoint.loadOrCreateUserEndpoint(getStartpoint()
				.getTNA()));
		Set<Endpoint> dstEndpoints = new HashSet<Endpoint>();
		for (Endpoint dst : getEndpoints()) {
			dstEndpoints.add(Endpoint.loadOrCreateUserEndpoint(dst.getTNA()));
		}
		setEndpoints(dstEndpoints);
	}

	@Transient
	public String getDebugInfo() {
		// String connDebug;
		// for (NrpsConnections nrpsConn : nrpsConnections.values()) {
		// connDebug += nrpsConn.getDebugInfo();
		// }
		String endpointInfo = "";
		for (Endpoint ep : this.endpoints) {
			endpointInfo += "\n\t\t\t" + ep.toString();
		}
		String nrpsConnectionInfo = "\n\t\tNrpsConnections:";
		for (NrpsConnections nc : getNrpsConnections().values()) {
			nrpsConnectionInfo += "\n\t\t\tPKNrpsConn:"
					+ nc.getPkNrpsConnection() + "\n\t\t\tStatus:"
					+ nc.getStatusType() + "\n\t\t\tSource:"
					+ nc.getSourceEndpoint().getTNA() + "\n\t\t\tDestination:"
					+ nc.getDestinationEndpoint().getTNA();
		}
		String linksInfo = "\n\t\tUserdLinks:";

		return "\n\t\tconnectionID: " + this.connectionId
				+ "\n\t\tPkConnection: " + this.PK_Connections
				+ "\n\t\tLatency" + this.maxLatency
				+ "\n\t\tStartPoint:\n\t\t\t" + this.startpoint.toString()
				+ "\n\t\tEndpoints:" + endpointInfo + nrpsConnectionInfo
				+ linksInfo;

	}

	@Override
	public String toString() {
		String result = "<pk>" + this.getPK_Connections() + "</pk>"
				+ "<connectionId>" + this.getConnectionId()
				+ "</connectionId><serviceId>"
				+ this.getService().getServiceId() + "</serviceId><minBW>"
				+ this.getMinBandwidth() + "</minBW><maxBW>"
				+ this.getMaxBandwidth() + "</maxBW><maxLatency>"
				+ this.getMaxLatency() + "</maxLatency><directionality>"
				+ this.getDirectionality() + "</directionality><dataAmount>"
				+ this.getDataAmount() + "</dataAmount><startpointTNA>"
				+ this.getStartpoint().getTNA() + "</startpointTNA>";

		for (Endpoint e : this.getEndpoints()) {
			result += "<destinationTNA>" + e.getTNA() + "</destinationTNA>";
		}

		for (NrpsConnections n : this.getNrpsConnections().values()) {
			result += "<nrpsConn>" + n.getPkNrpsConnection() + "</nrpsConn>";
		}
		return result;
	}

	@Transient
	public StatusType getStatus() {
		StatusType result = StatusType.values()[0];
		for (NrpsConnections nc : getNrpsConnections().values()) {
			result = Service.aggregateStatus(result, nc.getStatusType());
		}
		logger.debug("\tConnection: " + getConnectionId() + " Status" + result);
		return result;
	}

	@Transient
	public ConnectionStatusType getConnectionStatusType() {

		ConnectionStatusType result = new ConnectionStatusType();
		result.setActualBW(this.maxBandwidth);
		result.setConnectionID(this.connectionId);
		result.setDirectionality(this.directionality);
		result.setSource(this.startpoint.toJaxb());
		result.setStatus(getStatus());
		for (Endpoint ep : getEndpoints()) {
			result.getTarget().add(ep.toJaxb());
		}
		result.getDomainStatus().addAll(getDomainStatusType());
		return result;
	}

	@Transient
	public Collection<DomainConnectionStatusType> getDomainStatusType() {
		List<DomainConnectionStatusType> result = new ArrayList<DomainConnectionStatusType>();
		for (Entry<Domain, NrpsConnections> entry : getNrpsConnections()
				.entrySet()) {
			DomainConnectionStatusType dst = new DomainConnectionStatusType();
			dst.setDomain(entry.getKey().getName());
			NrpsConnections nc = entry.getValue();
			ConnectionStatusType cst = new ConnectionStatusType();
			cst.setActualBW(nc.getBandwidth());
			cst.setConnectionID(this.connectionId);
			cst.setDirectionality(nc.getDirectionality());
			cst.setSource(nc.getSourceEndpoint().toJaxb());
			cst.setStatus(nc.getStatusType());
			cst.getTarget().add(nc.getDestinationEndpoint().toJaxb());
			dst.setStatus(cst);
			result.add(dst);
		}

		return result;
	}

	@Transient
	public NrpsConnections getNrpsConnection(Domain domainName) {
		// TODO: this is a HACK, since connections.get(domainName) doesn't work
		// I've no clue why it doesn't return the domain :/
		Map<Domain, NrpsConnections> connections = getNrpsConnections();
		NrpsConnections result = null;
		for (Entry<Domain, NrpsConnections> s : connections.entrySet()) {
			if (s.getKey().equals(domainName)) {
				result = s.getValue();
			}
		}
		return result;
		// return connections.get(domainName);
	}

	public void addNrpsConnection(NrpsConnections nc) {
		nc.setConnection(this);
		this.nrpsConnections.put(nc.getDomain(), nc);
	}
}
