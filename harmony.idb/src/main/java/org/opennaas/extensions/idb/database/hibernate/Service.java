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
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;

import com.mysema.query.jpa.impl.JPAQuery;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ConnectionConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DomainStatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.FixedReservationConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.MalleableReservationConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ServiceConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.StatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatusResponseType.ServiceStatus;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.EndpointNotFoundFaultException;
import org.opennaas.core.utils.Helpers;
import org.opennaas.core.utils.PhLogger;
import org.opennaas.core.utils.Tuple;
import org.opennaas.extensions.idb.database.TransactionManager;
import org.opennaas.extensions.idb.database.TransactionManagerLoad;
import org.opennaas.extensions.idb.exception.database.DatabaseException;

/**
 * Java representation of of the database entity {@link Service}. This object
 * does not contain any logic.
 * 
 * @author Stephan Wagner (wagners@cs.uni-bonn.de)
 * @version 0.1
 */
@Entity
@Table(name = "Service")
public class Service implements java.io.Serializable {
	private static Logger logger = PhLogger.getLogger();
	// Fields

	/**
     *
     */
	private static final long serialVersionUID = 3830003273304575938L;

	/** Primary key of this service in the database. */
	private long PK_service;

	/**
	 * User-provided ID of the service (unique only together with the
	 * corresponding reservation ID.
	 */
	private int serviceId;

	/**
	 * corresponding reservation.
	 */
	private Reservation reservation;

	/**
	 * Type of service.
	 */
	private int serviceType;

	/**
	 * Start time.
	 */
	private Date startTime;

	/**
	 * Deadline.
	 */
	private Date deadline;

	/**
	 * Duration.
	 */
	private int duration;

	/**
	 * Automatic activation.
	 */
	private boolean automaticActivation;

	/**
	 * Connections that belong to the service.
	 */
	private Map<Integer, Connections> connections = new HashMap<Integer, Connections>(
			0);

	// Constructors

	/** default constructor. */
	public Service() {
		// empty
	}

	/**
	 * minimal constructor.
	 * 
	 * @param serviceIdParam
	 *            id of the service
	 * @param reservationParam
	 *            corresponding reservation
	 * @param serviceTypeParam
	 *            type of service
	 * @param startTimeParam
	 *            start time
	 * @param automaticActivationParam
	 *            automatic activation
	 */
	public Service(int serviceIdParam, Reservation reservationParam,
			int serviceTypeParam, Date startTimeParam,
			boolean automaticActivationParam) {
		this.setServiceId(serviceIdParam);
		this.setReservation(reservationParam);
		this.serviceType = serviceTypeParam;
		this.setStartTime(startTimeParam);
		this.setDeadline(new Date());
		this.setDuration(0);
		this.setAutomaticActivation(automaticActivationParam);
	}

	/**
	 * full constructor without connections. *
	 * 
	 * @param serviceIdParam
	 *            id of the service
	 * @param reservationParam
	 *            corresponding reservation
	 * @param serviceTypeParam
	 *            type of service
	 * @param startTimeParam
	 *            start time
	 * @param deadlineParam
	 *            deadline
	 * @param durationParam
	 *            duration
	 * @param automaticActivationParam
	 *            automatic activation
	 */
	public Service(int serviceIdParam, Reservation reservationParam,
			int serviceTypeParam, Date startTimeParam, Date deadlineParam,
			int durationParam, boolean automaticActivationParam) {
		this.setServiceId(serviceIdParam);
		this.setReservation(reservationParam);
		this.setServiceId(serviceIdParam);
		this.setStartTime(startTimeParam);
		this.setDeadline(deadlineParam);
		this.setDuration(durationParam);
		this.setAutomaticActivation(automaticActivationParam);
	}

	/**
	 * full constructor.
	 * 
	 * @param serviceIdParam
	 *            id of the service
	 * @param reservationParam
	 *            corresponding reservation
	 * @param serviceTypeParam
	 *            type of service
	 * @param startTimeParam
	 *            start time
	 * @param deadlineParam
	 *            deadline
	 * @param durationParam
	 *            duration
	 * @param automaticActivationParam
	 *            automatic activation
	 * @param connectionsParam
	 *            connections of the Service
	 */
	public Service(int serviceIdParam, Reservation reservationParam,
			int serviceTypeParam, Date startTimeParam, Date deadlineParam,
			int durationParam, boolean automaticActivationParam,
			HashMap<Integer, Connections> connectionsParam) {

		this.reservation = reservationParam;
		this.serviceType = serviceTypeParam;
		this.setConnections(connectionsParam);
		this.setReservation(reservationParam);
		this.setServiceId(serviceIdParam);
		this.setStartTime(startTimeParam);
		this.setDeadline(deadlineParam);
		this.setDuration(durationParam);
		this.setAutomaticActivation(automaticActivationParam);
	}

	// Property accessors

	/**
	 * @return the corresponding reservation
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_ReservationID")
	public Reservation getReservation() {
		return this.reservation;
	}

	/**
	 * @param reservationParam
	 *            corresponding reservation
	 */
	public void setReservation(Reservation reservationParam) {
		this.reservation = reservationParam;
	}

	/**
	 * @return start time
	 */
	public Date getStartTime() {
		return this.startTime;
	}

	/**
	 * @param startTimeParam
	 *            start time
	 */
	public void setStartTime(Date startTimeParam) {
		this.startTime = startTimeParam;
	}

	/**
	 * @return deadline
	 */
	@Basic(optional = true)
	public Date getDeadline() {
		return this.deadline;
	}

	/**
	 * @param deadlineParam
	 *            deadline
	 */
	public void setDeadline(Date deadlineParam) {
		/*
		 * Date newDeadline = deadlineParam; if (newDeadline == null) {
		 * XMLGregorianCalendar cal = Helpers.generateXMLCalendar();
		 * cal.setYear(9999); newDeadline = cal.toGregorianCalendar().getTime();
		 * } this.deadline = newDeadline;
		 */

		/*
		 * Christian: I don't think it is a good idea to simply fill in bogus
		 * values, rather we should run into NullPointerExceptions.
		 */

		this.deadline = deadlineParam;
	}

	/**
	 * @return duration
	 */
	@Basic(optional = true)
	public int getDuration() {
		return this.duration;
	}

	/**
	 * @param durationParam
	 *            duration
	 */
	public void setDuration(int durationParam) {
		this.duration = durationParam;
	}

	/**
	 * @return automatic activation
	 */
	public boolean isAutomaticActivation() {
		return this.automaticActivation;
	}

	/**
	 * @param automaticActivationParam
	 *            automatic activation
	 */
	public void setAutomaticActivation(boolean automaticActivationParam) {
		this.automaticActivation = automaticActivationParam;
	}

	/**
	 * @return connections
	 */
	@OneToMany(mappedBy = "service", fetch = FetchType.LAZY, cascade = { javax.persistence.CascadeType.ALL })
	@MapKey(name = "connectionId")
	public Map<Integer, Connections> getConnections() {
		return this.connections;
	}

	/**
	 * Retrieve connection with a specific ID.
	 * 
	 * @param connectionId
	 *            Connection ID of requested connection.
	 * @return Connection with the given Connection ID, or null if not found.
	 */
	@Transient
	public Connections getConnection(int connectionId) {
		return getConnections().get(new Integer(connectionId));
	}

	/**
	 * @param connections
	 *            the prefixes to set
	 */
	public void setConnections(Map<Integer, Connections> connections) {
		this.connections = connections;
	}

	/**
	 * @return the pkService
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getPK_service() {
		return this.PK_service;
	}

	/**
	 * @param pkService
	 *            the pkService to set
	 */
	public void setPK_service(long pkService) {
		this.PK_service = pkService;
	}

	/**
	 * @return the serviceId
	 */
	public int getServiceId() {
		return this.serviceId;
	}

	/**
	 * @param serviceId
	 *            the serviceId to set
	 */
	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}

	/**
	 * @param serviceParam
	 *            service to be checked
	 * @return true if equals
	 */
	public boolean isEqual(Service serviceParam) {
		if (this.hashCode() == serviceParam.hashCode()) {
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
		if (o.getClass() == Service.class) {
			return isEqual((Service) o);
		}
		return false;
	}

	@Override
	public int hashCode() {
		int result = Long.valueOf(this.getPK_service()).hashCode()
				^ new Integer(this.getServiceId()).hashCode()
				^ new Long(Helpers.trimDateToSeconds(this.getStartTime()))
						.hashCode()
				^ new Boolean(this.isAutomaticActivation()).hashCode()
				^ new Integer(this.getDuration()).hashCode()
				^ ((this.getDeadline() == null) ? 0 : new Long(
						Helpers.trimDateToSeconds(this.getDeadline()))
						.hashCode());

		// in the underlying objects, don't use the hashCode()-method, because
		// this can end in
		// dependency-circles. Instead only use the DB-primary key for the hash.
		// for (Connections c : getConnections().values()) {
		// result ^= new Long(c.getPK_Connections()).hashCode();
		// }
		//
		// result ^= new
		// Long(this.getReservation().getReservationId()).hashCode();

		return result;
	}

	/**
	 * @return copy of service
	 */
	@Transient
	public Service getCopy() {
		Service result = new Service(this.getServiceId(), this.reservation,
				this.serviceType, this.getStartTime(), this.getDeadline(),
				this.getDuration(), this.automaticActivation);
		for (Connections c : getConnections().values()) {
			result.getConnections().put(new Integer(c.getConnectionId()), c);
		}
		return result;
	}

	@Override
	public String toString() {
		return "<pk>" + getPK_service() + "</pk>" + "<serviceId>"
				+ getServiceId() + "</serviceId><reservationId>"
				+ getReservation().getReservationId()
				+ "</reservationId><startTime>" + getStartTime()
				+ "</startTime><deadline>" + getDeadline()
				+ "</deadline><duration>" + getDuration()
				+ "</duration><activation>" + isAutomaticActivation()
				+ "</activation>";
	}

	/**
	 * @param serviceParam
	 *            service to be compared to
	 * @return -1 0 or 1
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Service serviceParam) {
		if (this.getPK_service() < serviceParam.getPK_service()) {
			return -1;
		} else if (this.getPK_service() == serviceParam.getPK_service()) {
			return 0;
		} else {
			return 1;
		}
	}

	protected void fromJaxbStartTime(XMLGregorianCalendar startTime) {
		this.setStartTime(new Date(startTime.toGregorianCalendar()
				.getTimeInMillis()));
	}

	// Requires that startTime is already set!!
	protected void fromJaxbDeadline(XMLGregorianCalendar deadline) {
		this.setDeadline(deadline.toGregorianCalendar().getTime());
	}

	public static Service fromJaxb(ServiceConstraintType sJaxb)
			throws EndpointNotFoundFaultException, DatabaseException {
		Service result = new Service();
		result.setServiceId(sJaxb.getServiceID());
		if (sJaxb.isSetFixedReservationConstraints()) {
			result.fromJaxbStartTime(sJaxb.getFixedReservationConstraints()
					.getStartTime());
			result.setDuration(sJaxb.getFixedReservationConstraints()
					.getDuration());
			/*
			 * Christian: What is this for?! Fixed reservations don't have
			 * deadlines, period.
			 */
			/*
			 * XMLGregorianCalendar cal = Helpers.generateXMLCalendar();
			 * cal.setYear(9999);
			 * result.setDeadline(cal.toGregorianCalendar().getTime());
			 */
		} else if (sJaxb.isSetDeferrableReservationConstraints()) {
			result.fromJaxbStartTime(sJaxb
					.getDeferrableReservationConstraints().getStartTime());
			result.setDuration(sJaxb.getDeferrableReservationConstraints()
					.getDuration());
			result.fromJaxbDeadline(sJaxb.getDeferrableReservationConstraints()
					.getDeadline());
		} else if (sJaxb.isSetMalleableReservationConstraints()) {
			result.fromJaxbStartTime(sJaxb.getMalleableReservationConstraints()
					.getStartTime());
			/*
			 * Christian: Also, malleable reservations don't have a duration
			 * value.
			 */
			// result.setDuration(0);
			result.fromJaxbDeadline(sJaxb.getMalleableReservationConstraints()
					.getDeadline());
		}
		result.setAutomaticActivation(sJaxb.isAutomaticActivation());
		result.getConnections().clear();
		for (ConnectionConstraintType c : sJaxb.getConnections()) {
			Connections conn = Connections.fromJaxb(c);
			conn.setService(result);
			result.getConnections().put(new Integer(conn.getConnectionId()),
					conn);
		}
		return result;
	}

	public ServiceConstraintType toJaxb() {
		ServiceConstraintType result = new ServiceConstraintType();
		result.setServiceID(this.getServiceId());
		result.setAutomaticActivation(this.isAutomaticActivation());

		if (this.getDuration() > 0) {
			if (null != this.getDeadline()) {// DeferrableReservation
				org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DeferrableReservationConstraintType df = new org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DeferrableReservationConstraintType();
				df.setDuration(this.getDuration());
				df.setDeadline(Helpers.DateToXmlCalendar(this.getDeadline()));
				df.setStartTime(Helpers.DateToXmlCalendar(this.getStartTime()));

				result.setDeferrableReservationConstraints(df);
				result.setTypeOfReservation(ReservationType.DEFERRABLE);
			} else {// FixedReservation
				FixedReservationConstraintType fr = new FixedReservationConstraintType();
				fr.setDuration(this.getDuration());
				fr.setStartTime(Helpers.DateToXmlCalendar(this.getStartTime()));
				result.setFixedReservationConstraints(fr);
				result.setTypeOfReservation(ReservationType.FIXED);
			}
		} else if (null != this.getDeadline()) {
			// MalleableReservation
			MalleableReservationConstraintType mr = new MalleableReservationConstraintType();
			mr.setDeadline(Helpers.DateToXmlCalendar(this.getDeadline()));
			mr.setStartTime(Helpers.DateToXmlCalendar(this.getStartTime()));
			result.setMalleableReservationConstraints(mr);
			result.setTypeOfReservation(ReservationType.MALLEABLE);
		} else {
			throw new RuntimeException(
					"Error in Service.toJaxb()! Blame the validator!! Type, duration and deadline do not fit together");
		}
		result.getConnections().clear();
		for (Connections c : this.getConnections().values()) {
			result.getConnections().add(c.toJaxb());
		}
		return result;
	}

	/**
	 * Load service from the DB. This function does not load the connections
	 * that belong to the service - there is an additional method for doing
	 * this!
	 * 
	 * @param dbKey
	 * @return {@link Service} for given ID, or null if service was not found.
	 * @throws DatabaseException
	 */
	public static final Service load(long dbKey) throws DatabaseException {
		return (Service) (new TransactionManagerLoad(Service.class,
				Long.valueOf(dbKey))).getResult();
	}

	/**
	 * load services from DB which contain the user service-ID NOT the
	 * primary-key. Therefore we also need the corresponding reservationID to
	 * make this unique.
	 * 
	 * @param session
	 */
	@SuppressWarnings("unchecked")
	public static List<Service> loadWithUserID(int serviceUserId,
			Reservation res) throws DatabaseException {
		return (List<Service>) (new TransactionManager(
				new Tuple<Integer, Reservation>(new Integer(serviceUserId), res)) {
			@Override
			protected void dbOperation() {
				Tuple<Integer, Reservation> args = (Tuple<Integer, Reservation>) this.arg;
				QService service = QService.service;
				JPAQuery query = new JPAQuery(this.session);

				this.result = query
						.from(service)
						.where(service.serviceId.eq(args.getFirstElement())
								.and(service.reservation.eq(args
										.getSecondElement()))).list(service);
			}
		}).getResult();
	}

	public void save(EntityManager session) {
		session.persist(this);
	}

	public void save() throws DatabaseException {
		new TransactionManager(new HashSet<Object>(
				Arrays.asList(getReservation()))) {
			@Override
			protected void dbOperation() throws Exception {
				save(this.session);
			}
		};
	}

	public void delete(EntityManager session) {
		session.remove(this);
	}

	public void delete() throws DatabaseException {
		new TransactionManager(new HashSet<Object>(
				Arrays.asList(getReservation()))) {
			@Override
			protected void dbOperation() {
				delete(this.session);
			}
		};
	}

	public void loadOrCreateUserEndpoints() throws DatabaseException,
			EndpointNotFoundFaultException {
		for (Connections c : getConnections().values()) {
			c.loadOrCreateUserEndpoints();
		}
	}

	/**
	 * Load Connections from the DB. This will override all stored connections!!
	 */
	@SuppressWarnings("unchecked")
	public void loadConnections() throws DatabaseException {
		List<Connections> tmpConns = (List<Connections>) (new TransactionManager(
				new Long(this.getPK_service())) {
			@Override
			protected void dbOperation() {
				QConnections connections = QConnections.connections;
				JPAQuery query = new JPAQuery(this.session);
				this.result = query.from(connections)
						.where(connections.service.eq((Service) this.arg))
						.list(connections);
			}
		}).getResult();

		// clear all old connections
		this.connections.clear();
		// put new connections from DB into the set
		for (Connections connections : tmpConns) {
			this.connections.put(new Integer(connections.getConnectionId()),
					connections);
		}

	}

	public void addConnection(Connections connection) {
		connection.setService(this);
		this.connections.put(new Integer(connection.getConnectionId()),
				connection);
	}

	@Transient
	public String getDebugInfo() {
		String connDebug = "";

		for (Connections conn : this.connections.values()) {
			connDebug += conn.getDebugInfo();
		}
		return "\n\tServiceId: " + this.getServiceId() + "\n\tpkService"
				+ this.getPK_service() + "\n\tserviceType" + this.serviceType
				+ "\n\tstarttime" + this.getStartTime().toString()
				+ "\n\tduration" + this.getDuration() + "\n\tDeadline"
				+ this.getDeadline() + "\n\tConnections: " + connDebug;
	}

	public static StatusType aggregateStatus(StatusType s1, StatusType s2) {
		if ((s1.ordinal() < s2.ordinal())) {
			return s2;
		}
		return s1;
	}

	@Transient
	public StatusType getStatus() {
		StatusType result = StatusType.values()[0];
		for (Connections connection : getConnections().values()) {
			result = aggregateStatus(result, connection.getStatus());
		}
		logger.debug("Service: " + getServiceId() + " Status" + result);
		return result;
	}

	@Transient
	public ServiceStatus getServiceStatus() {
		ServiceStatus result = new ServiceStatus();
		HashMap<String, StatusType> domainStatus = new HashMap<String, StatusType>();
		result.setServiceID(getServiceId());
		result.setStatus(getStatus());
		for (Connections connection : getConnections().values()) {
			result.getConnections().add(connection.getConnectionStatusType());
			for (Entry<Domain, NrpsConnections> entry : connection
					.getNrpsConnections().entrySet()) {
				if (domainStatus.containsKey(entry.getKey())) {
					domainStatus.put(
							entry.getKey().getName(),
							aggregateStatus(domainStatus.get(entry.getKey()),
									entry.getValue().getStatusType()));
				} else {
					domainStatus.put(entry.getKey().getName(), entry.getValue()
							.getStatusType());
				}
			}
		}
		for (Entry<String, StatusType> entry : domainStatus.entrySet()) {
			DomainStatusType dst = new DomainStatusType();
			dst.setDomain(entry.getKey());
			dst.setStatus(entry.getValue());
			result.getDomainStatus().add(dst);
		}
		return result;
	}
}
