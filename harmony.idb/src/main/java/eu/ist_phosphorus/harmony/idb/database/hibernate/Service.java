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
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Basic;
import javax.persistence.Entity;
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
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.annotations.Proxy;
import org.hibernate.criterion.Restrictions;

import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.ConnectionConstraintType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.DomainStatusType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.FixedReservationConstraintType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.MalleableReservationConstraintType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.ReservationType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.ServiceConstraintType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.StatusType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetStatusResponseType.ServiceStatus;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.exceptions.EndpointNotFoundFaultException;
import eu.ist_phosphorus.harmony.common.utils.Helpers;
import eu.ist_phosphorus.harmony.common.utils.PhLogger;
import eu.ist_phosphorus.harmony.common.utils.Tuple;
import eu.ist_phosphorus.harmony.idb.database.TransactionManager;
import eu.ist_phosphorus.harmony.idb.database.TransactionManagerLoad;
import eu.ist_phosphorus.harmony.idb.exceptions.database.DatabaseException;

/**
 * Java representation of of the database entity {@link Service}. This object
 * does not contain any logic.
 *
 * @author Stephan Wagner (wagners@cs.uni-bonn.de)
 * @version 0.1
 */
@Entity
@Table(name = "Service")
@Proxy(lazy = false)
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
    private Map<Integer, Connections> connections =
            new HashMap<Integer, Connections>(0);

    // Constructors

    /** default constructor. */
    public Service() {
        // empty
    }

    /**
     * minimal constructor.
     *
     * @param serviceIdParam
     *                id of the service
     * @param reservationParam
     *                corresponding reservation
     * @param serviceTypeParam
     *                type of service
     * @param startTimeParam
     *                start time
     * @param automaticActivationParam
     *                automatic activation
     */
    public Service(final int serviceIdParam, final Reservation reservationParam,
            final int serviceTypeParam, final Date startTimeParam,
            final boolean automaticActivationParam) {
        this.serviceId = serviceIdParam;
        this.reservation = reservationParam;
        this.serviceType = serviceTypeParam;
        this.startTime = startTimeParam;
        this.deadline = new Date();
        this.duration = 0;
        this.automaticActivation = automaticActivationParam;
    }

    /**
     * full constructor without connections. *
     *
     * @param serviceIdParam
     *                id of the service
     * @param reservationParam
     *                corresponding reservation
     * @param serviceTypeParam
     *                type of service
     * @param startTimeParam
     *                start time
     * @param deadlineParam
     *                deadline
     * @param durationParam
     *                duration
     * @param automaticActivationParam
     *                automatic activation
     */
    public Service(final int serviceIdParam, final Reservation reservationParam,
            final int serviceTypeParam, final Date startTimeParam,
            final Date deadlineParam, final int durationParam,
            final boolean automaticActivationParam) {
        this.serviceId = serviceIdParam;
        this.reservation = reservationParam;
        this.serviceType = serviceTypeParam;
        this.startTime = startTimeParam;
        this.deadline = deadlineParam;
        this.duration = durationParam;
        this.automaticActivation = automaticActivationParam;
    }

    /**
     * full constructor.
     *
     * @param serviceIdParam
     *                id of the service
     * @param reservationParam
     *                corresponding reservation
     * @param serviceTypeParam
     *                type of service
     * @param startTimeParam
     *                start time
     * @param deadlineParam
     *                deadline
     * @param durationParam
     *                duration
     * @param automaticActivationParam
     *                automatic activation
     * @param connectionsParam
     *                connections of the Service
     */
    public Service(final int serviceIdParam, final Reservation reservationParam,
            final int serviceTypeParam, final Date startTimeParam,
            final Date deadlineParam, final int durationParam,
            final boolean automaticActivationParam,
            final HashMap<Integer, Connections> connectionsParam) {
        this.serviceId = serviceIdParam;
        this.reservation = reservationParam;
        this.serviceType = serviceTypeParam;
        this.startTime = startTimeParam;
        this.deadline = deadlineParam;
        this.duration = durationParam;
        this.automaticActivation = automaticActivationParam;
        this.connections = connectionsParam;
    }

    // Property accessors

    /**
     * @return the corresponding reservation
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_ReservationID")
    public final Reservation getReservation() {
        return this.reservation;
    }

    /**
     * @param reservationParam
     *                corresponding reservation
     */
    public final void setReservation(final Reservation reservationParam) {
        this.reservation = reservationParam;
    }

    /**
     * @return start time
     */
    public final Date getStartTime() {
        return this.startTime;
    }

    /**
     * @param startTimeParam
     *                start time
     */
    public final void setStartTime(final Date startTimeParam) {
        this.startTime = startTimeParam;
    }

    /**
     * @return deadline
     */
    @Basic(optional = true)
    public final Date getDeadline() {
        return this.deadline;
    }

    /**
     * @param deadlineParam
     *                deadline
     */
    public final void setDeadline(final Date deadlineParam) {
        /*
         * Date newDeadline = deadlineParam; if (newDeadline == null) {
         * XMLGregorianCalendar cal = Helpers.generateXMLCalendar();
         * cal.setYear(9999); newDeadline = cal.toGregorianCalendar().getTime(); }
         * this.deadline = newDeadline;
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
    public final int getDuration() {
        return this.duration;
    }

    /**
     * @param durationParam
     *                duration
     */
    public final void setDuration(final int durationParam) {
        this.duration = durationParam;
    }

    /**
     * @return automatic activation
     */
    public final boolean isAutomaticActivation() {
        return this.automaticActivation;
    }

    /**
     * @param automaticActivationParam
     *                automatic activation
     */
    public final void setAutomaticActivation(
            final boolean automaticActivationParam) {
        this.automaticActivation = automaticActivationParam;
    }

    /**
     * @return connections
     */
    @OneToMany(mappedBy="service",
    		   fetch=FetchType.LAZY,
    		   cascade = {javax.persistence.CascadeType.ALL})
    @MapKey(name = "connectionId")
    public final Map<Integer, Connections> getConnections() {
        return this.connections;
    }

    /**
     * Retrieve connection with a specific ID.
     *
     * @param connectionId
     *                Connection ID of requested connection.
     * @return Connection with the given Connection ID, or null if not found.
     */
    @Transient
    public final Connections getConnection(final int connectionId) {
        return getConnections().get(new Integer(connectionId));
    }

    /**
     * @param connections
     *                the prefixes to set
     */
    public final void setConnections(Map<Integer, Connections> connections) {
        this.connections = connections;
    }

    /**
     * @return the pkService
     */
    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    public long getPK_service() {
        return this.PK_service;
    }

    /**
     * @param pkService
     *                the pkService to set
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
     *                the serviceId to set
     */
    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    /**
     * @param serviceParam
     *                service to be checked
     * @return true if equals
     */
    public final boolean isEqual(final Service serviceParam) {
    	if(this.hashCode() == serviceParam.hashCode()) {
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
        if (o.getClass() == Service.class) {
            return isEqual((Service) o);
        }
        return false;
    }

    @Override
    public final int hashCode() {
        int result = Long.valueOf(this.getPK_service()).hashCode()
                     ^ new Integer(this.getServiceId()).hashCode()
                     ^ new Long(Helpers.trimDateToSeconds(this.getStartTime())).hashCode()
                     ^ new Boolean(this.isAutomaticActivation()).hashCode()
                     ^ new Integer(this.getDuration()).hashCode()
                     ^ ((this.getDeadline() == null) ? 0 : 
                    	 new Long(Helpers.trimDateToSeconds(this.getDeadline())).hashCode());

        // in the underlying objects, don't use the hashCode()-method, because this can end in 
        // dependency-circles. Instead only use the DB-primary key for the hash.
//        for (Connections c : getConnections().values()) {
//            result ^= new Long(c.getPK_Connections()).hashCode();
//        }
//        
//        result ^= new Long(this.getReservation().getReservationId()).hashCode();

        return result;
    }

    /**
     * @return copy of service
     */
    @Transient
    public final Service getCopy() {
        Service result =
            new Service(this.serviceId, this.reservation,
                    this.serviceType, this.startTime, this.deadline,
                    this.duration, this.automaticActivation);
        for (Connections c : getConnections().values()) {
            result.getConnections().put(new Integer(c.getConnectionId()), c);
        }
        return result;
    }

    @Override
    public final String toString() {
        return "<pk>" + getPK_service() + "</pk>" 
                + "<serviceId>" + getServiceId()
                + "</serviceId><reservationId>" + getReservation().getReservationId()
                + "</reservationId><startTime>" + getStartTime()
                + "</startTime><deadline>" + getDeadline() 
                + "</deadline><duration>" + getDuration()
                + "</duration><activation>" + isAutomaticActivation()
                + "</activation>";
    }

    /**
     * @param serviceParam
     *                service to be compared to
     * @return -1 0 or 1
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public final int compareTo(final Service serviceParam) {
        if (this.PK_service < serviceParam.getPK_service()) {
            return -1;
        } else if (this.PK_service == serviceParam.getPK_service()) {
            return 0;
        } else {
            return 1;
        }
    }

    protected final void fromJaxbStartTime(XMLGregorianCalendar startTime) {
        this.setStartTime(new Date(startTime.toGregorianCalendar()
                .getTimeInMillis()));
    }

    // Requires that startTime is already set!!
    protected final void fromJaxbDeadline(XMLGregorianCalendar deadline) {
        this.setDeadline(deadline.toGregorianCalendar().getTime());
    }

    public static final Service fromJaxb(final ServiceConstraintType sJaxb)
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

    public final ServiceConstraintType toJaxb() {
        ServiceConstraintType result = new ServiceConstraintType();
        result.setServiceID(this.getServiceId());
        result.setAutomaticActivation(this.isAutomaticActivation());

        if (this.duration > 0) {
            if (null != this.deadline) {// DeferrableReservation
                eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.DeferrableReservationConstraintType df =
                        new eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.DeferrableReservationConstraintType();
                df.setDuration(this.duration);
                df.setDeadline(Helpers.DateToXmlCalendar(this.deadline));
                df.setStartTime(Helpers.DateToXmlCalendar(this.startTime));

                result.setDeferrableReservationConstraints(df);
                result.setTypeOfReservation(ReservationType.DEFERRABLE);
            } else {// FixedReservation
                FixedReservationConstraintType fr =
                        new FixedReservationConstraintType();
                fr.setDuration(this.duration);
                fr.setStartTime(Helpers.DateToXmlCalendar(this.startTime));
                result.setFixedReservationConstraints(fr);
                result.setTypeOfReservation(ReservationType.FIXED);
            }
        } else if (null != this.deadline) {
            // MalleableReservation
            MalleableReservationConstraintType mr =
                    new MalleableReservationConstraintType();
            mr.setDeadline(Helpers.DateToXmlCalendar(this.deadline));
            mr.setStartTime(Helpers.DateToXmlCalendar(this.startTime));
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
        return (Service) (new TransactionManagerLoad(Service.class, Long
                .valueOf(dbKey))).getResult();
    }

    /**
     * load services from DB which contain the user service-ID NOT the
     * primary-key. Therefore we also need the corresponding reservationID to
     * make this unique.
     *
     * @param session
     */
    @SuppressWarnings("unchecked")
    public static final List<Service> loadWithUserID(int serviceUserId,
            Reservation res) throws DatabaseException {
        return (List<Service>) (new TransactionManager(
                new Tuple<Integer, Reservation>(new Integer(serviceUserId), res)) {
            @Override
            protected void dbOperation() {
                Tuple<Integer, Reservation> args = (Tuple<Integer, Reservation>) this.arg;
                this.result =
                        this.session.createCriteria(Service.class)
                                .setFetchMode("", FetchMode.SELECT).add(
                                        Restrictions.like("serviceId", args
                                                .getFirstElement())).add(
                                        Restrictions.like("reservation",
                                                args.getSecondElement()))
                                .list();
            }
        }).getResult();
    }

    public final void save(Session session) {
        session.saveOrUpdate(this);
    }

    public final void save() throws DatabaseException {
        new TransactionManager(new HashSet<Object>(Arrays.asList(reservation))) {
            @Override
            protected void dbOperation() throws Exception {
                save(this.session);
            }
        };
    }

    public final void delete(Session session) {
    	session.delete(this);
    }
    public final void delete() throws DatabaseException {
		new TransactionManager(new HashSet<Object>(Arrays.asList(reservation))) {
		    @Override
		    protected void dbOperation() {
		    	delete(this.session);
		    }
		};
    }

    public final void loadOrCreateUserEndpoints() throws DatabaseException,
            EndpointNotFoundFaultException {
        for (Connections c : getConnections().values()) {
            c.loadOrCreateUserEndpoints();
        }
    }

    /**
     * Load Connections from the DB. This will override all stored connections!!
     */
    @SuppressWarnings("unchecked")
    public final void loadConnections() throws DatabaseException {
        List<Connections> tmpConns =
                (List<Connections>) (new TransactionManager(new Long(
                        this.PK_service)) {
                    @Override
                    protected void dbOperation() {
                        this.result =
                                this.session.createCriteria(Connections.class)
                                        .setFetchMode("", FetchMode.SELECT)
                                        .add(
                                                Restrictions.like("fkService",
                                                        this.arg)).list();
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
    public final String getDebugInfo() {
        String connDebug = "";

        for (Connections conn : this.connections.values()) {
            connDebug += conn.getDebugInfo();
        }
        return "\n\tServiceId: " + this.serviceId + "\n\tpkService"
                + this.PK_service + "\n\tserviceType" + this.serviceType
                + "\n\tstarttime" + this.startTime.toString() + "\n\tduration"
                + this.duration + "\n\tDeadline" + this.deadline
                + "\n\tConnections: " + connDebug;
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
        HashMap<String, StatusType> domainStatus =
                new HashMap<String, StatusType>();
        result.setServiceID(getServiceId());
        result.setStatus(getStatus());
        for (Connections connection : getConnections().values()) {
            result.getConnections().add(connection.getConnectionStatusType());
            for (Entry<Domain, NrpsConnections> entry : connection
                    .getNrpsConnections().entrySet()) {
                if (domainStatus.containsKey(entry.getKey())) {
                    domainStatus.put(entry.getKey().getName(), aggregateStatus(
                            domainStatus.get(entry.getKey()), entry.getValue()
                                    .getStatusType()));
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
