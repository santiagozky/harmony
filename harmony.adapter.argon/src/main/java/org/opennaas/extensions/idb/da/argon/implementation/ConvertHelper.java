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


package org.opennaas.extensions.idb.da.argon.implementation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;

import de.unibonn.viola.argon.requestProcessor.utils.StatusManager;
import de.unibonn.viola.argon.utils.jaxb.external.classes.IRes.AAAInformationType;
import de.unibonn.viola.argon.utils.jaxb.external.classes.IRes.ActivationReplyType;
import de.unibonn.viola.argon.utils.jaxb.external.classes.IRes.ActivationRequestType;
import de.unibonn.viola.argon.utils.jaxb.external.classes.IRes.AvailabilityReplyType;
import de.unibonn.viola.argon.utils.jaxb.external.classes.IRes.AvailabilityRequestType;
import de.unibonn.viola.argon.utils.jaxb.external.classes.IRes.CancelReplyType;
import de.unibonn.viola.argon.utils.jaxb.external.classes.IRes.CancelRequestType;
import de.unibonn.viola.argon.utils.jaxb.external.classes.IRes.ConnectionType;
import de.unibonn.viola.argon.utils.jaxb.external.classes.IRes.QueryReplyType;
import de.unibonn.viola.argon.utils.jaxb.external.classes.IRes.QueryRequestType;
import de.unibonn.viola.argon.utils.jaxb.external.classes.IRes.ReservationDataType;
import de.unibonn.viola.argon.utils.jaxb.external.classes.IRes.ReservationReplyType;
import de.unibonn.viola.argon.utils.jaxb.external.classes.IRes.ReservationRequestType;
import de.unibonn.viola.argon.utils.jaxb.external.classes.IRes.ServiceType;
import de.unibonn.viola.argon.utils.jaxb.external.classes.IRes.TimeConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ActivateResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ActivateType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AvailabilityCodeType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelReservationResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ConnectionAvailabilityType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ConnectionConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ConnectionStatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CreateReservationResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CreateReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.FixedReservationConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationsComplexType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationsResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatusResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.IsAvailableResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.IsAvailableType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ObjectFactory;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ServiceConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.StatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatusResponseType.ServiceStatus;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.EndpointNotFoundFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.utils.WebserviceUtils;
import org.opennaas.extensions.idb.serviceinterface.RequestHandler;
import org.opennaas.core.utils.Config;

/**
 * Implementation of the Reservation-Webservice for Muse.
 * 
 * @author Steffen Claus
 */
public final class ConvertHelper extends RequestHandler {

    /*
     * Instance Variables
     * =========================================================================
     */

    /** Singleton instance. */
    private static ConvertHelper selfInstance = null;

    /**
     * used for JMS.
     */
    private static long lastRequestID;

    /**
     * maps IP to internal interface.
     */
    private TNAMapper tnaMapper = TNAMapper.getInstance();

    /**
     * the log4j-Logger.
     */
    private static Logger log = Logger.getLogger(ConvertHelper.class);

    /*
     * Singleton Stuff
     * =========================================================================
     */

    /** Private constructor: Singleton. */
    private ConvertHelper() {
        // Nothing to do yet..
    }

    /**
     * Instance getter.
     * 
     * @return Singleton Instance
     */
    public static ConvertHelper getInstance() {
        if (ConvertHelper.selfInstance == null) {
            ConvertHelper.selfInstance = new ConvertHelper();
        }
        return ConvertHelper.selfInstance;
    }

    /**
     * Singleton - Cloning _not_ supported!
     * 
     * @return Should never return anything...
     * @throws CloneNotSupportedException
     *             Singleton hates to be cloned!
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    /** translation-factor between NSP and Argon. */
    private static final long NSP_ARGON_FACTOR = 1000000L;
    /** translation-factor between sec. and millisec. */
    // private static final long SEC_MILLISEC_FACTOR = 1000L;
    /**
     * the log4j-Logger.
     */
    // private static Logger log = Logger.getLogger(ConvertHelper.class);
    /** The Constant ACTIVATION. */
    public static final String ACTIVATION = "Activation";

    /** The Constant MODIFICATION. */
    public static final String MODIFICATION = "Modification";

    /** The Constant QUERY. */
    public static final String QUERY = "Query";

    /** The Constant CANCEL. */
    public static final String CANCEL = "Cancel";

    /** The Constant BIND. */
    public static final String BIND = "Bind";

    /** The Constant AVAILABILITY. */
    public static final String AVAILABILITY = "Availability";

    /** The Constant RESERVATION. */
    public static final String RESERVATION = "Reservation";

    /**
     * The Object Factory for the muse-classes.
     */
    private static ObjectFactory of = new ObjectFactory();

    private AAAInformationType aaaInfo = null;

    private AAAInformationType getAAAInfo() {
        if (this.aaaInfo == null) {
            this.aaaInfo = new AAAInformationType();
            this.aaaInfo.setAuthenticationMethod("username/password");
            this.aaaInfo.setUsername("argon");
            this.aaaInfo.setPassword("geheim");
        }
        return this.aaaInfo;
    }

    /**
     * @return whether mock-mode is set
     */
    protected boolean mockMode() {
        return Config.getString("argonNRPS", "argonNRPS.mockMode").equals(
                "true");
    }

    /**
     * maps argon-availability-status to AvailabilityCodeType
     * 
     * @param internalAvailStatus
     *            the argon-availability-status
     * @return AvailabilityCodeType of the NSP
     */
    public static AvailabilityCodeType convertAvailStatus(
            final int internalAvailStatus) {

        if (StatusManager.isLinkNotAvailable(internalAvailStatus)) {
            return AvailabilityCodeType.PATH_NOT_AVAILABLE;
        }

        if (StatusManager.isDestNotAvailable(internalAvailStatus)
                || StatusManager.isSrcNotAvailable(internalAvailStatus)) {
            return AvailabilityCodeType.ENDPOINT_NOT_AVAILABLE;
        }

        return AvailabilityCodeType.AVAILABLE;
    }

    /**
     * maps ServiceType and ConnectionType from Argon to
     * ConnectionAvailabilityType from NSP.
     * 
     * @param s
     *            the ServiceType
     * @param c
     *            the ConnectionType
     * @return the ConnectionAvailabilityType
     */
    public ConnectionAvailabilityType createConnectionAvail(ServiceType s,
            ConnectionType c) {
        ConnectionAvailabilityType cAvail =
                of.createConnectionAvailabilityType();

        cAvail.setServiceID(s.getServiceHandle().intValue());
        cAvail.setConnectionID(c.getConnectionHandle().intValue());

        if (c.getStatus() != null) {
            cAvail.setAvailability(ConvertHelper.convertAvailStatus(c
                    .getStatus().intValue()));
            // TODO: dirty
            if (cAvail.getAvailability().compareTo(
                    AvailabilityCodeType.ENDPOINT_NOT_AVAILABLE) == 0) {
                if (StatusManager.isDestNotAvailable(c.getStatus().intValue())) {
                    cAvail.getBlockedEndpoints().add(
                            this.tnaMapper.getTNAFromInterface(c
                                    .getDestinationAddress()));
                }
                if (StatusManager.isSrcNotAvailable(c.getStatus().intValue())) {
                    cAvail.getBlockedEndpoints().add(
                            this.tnaMapper.getTNAFromInterface(c
                                    .getSourceAddress()));
                }
            }
        } else {
            cAvail
                    .setAvailability(AvailabilityCodeType.AVAILABILITY_NOT_CHECKED);
        }

        return cAvail;
    }

    /**
     * maps argon-status to StatusType.
     * 
     * @param internalStatus
     *            the argon-status
     * @return StatusType of the NSP
     */
    public static StatusType convertStatus(final int internalStatus) {

        if (StatusManager.isAborted(internalStatus)
                || StatusManager.isRejected(internalStatus)) {
            return StatusType.CANCELLED_BY_SYSTEM;
        }

        if (StatusManager.isCanceled(internalStatus)) {
            return StatusType.CANCELLED_BY_USER;
        }

        if (StatusManager.isSuccessful(internalStatus)) {
            return StatusType.COMPLETED;
        }

        if (StatusManager.isEstablished(internalStatus)) {
            return StatusType.ACTIVE;
        }

        if (StatusManager.isTeardown(internalStatus)) {
            return StatusType.TEARDOWN_IN_PROGRESS;
        }

        if (StatusManager.isInSetup(internalStatus)) {
            return StatusType.SETUP_IN_PROGRESS;
        }

        if (StatusManager.isAccepted(internalStatus)
                || StatusManager.isReserved(internalStatus)
                || StatusManager.isScheduled(internalStatus)) {
            return StatusType.PENDING;
        }
        return StatusType.UNKNOWN;
    }

    /**
     * converts the bandwidths.
     * 
     * @param argonBw
     *            the bandwidth used by argon
     * @return the bandwidth used by the NSP
     */
    public static Integer convertArgonToNspBw(final long argonBw) {
        long ret = argonBw / NSP_ARGON_FACTOR;
        Long returnValue = Long.valueOf(ret);
        return new Integer(returnValue.intValue());
    }

    /**
     * converts the bandwidths.
     * 
     * @param nspBw
     *            the bandwidths used by the NSP
     * @return the bandwidth used by argon
     */
    public static long convertNspToArgonBw(final int nspBw) {
        return nspBw * NSP_ARGON_FACTOR;
    }

    /**
     * converts the dataAmount.
     * 
     * @param nspData
     *            the dataAmount used by the NSP
     * @return the dataAmount used by argon
     */
    public static long convertNspToArgonDataAmount(final long nspData) {
        return nspData * NSP_ARGON_FACTOR;
    }

    /**
     * maps a NSP-Connection to an internal Argon-Connection.
     * 
     * @param c
     *            ConnectionConstraintType
     * @return ConnectionType
     * @throws EndpointNotFoundFaultException
     */
    public ConnectionType convertNsptoArgonConnection(
            final ConnectionConstraintType c)
            throws EndpointNotFoundFaultException {
        // internal Connection-Object

        ConnectionType con = new ConnectionType();
        con.setConnectionHandle(new Long(c.getConnectionID()));

        de.unibonn.viola.argon.utils.jaxb.external.classes.IRes.ConnectionConstraintType internalConstraint =
                new de.unibonn.viola.argon.utils.jaxb.external.classes.IRes.ConnectionConstraintType();

        // dataAmount from nSP is Mbyte
        // dataAmount from argon is Mbit
        if (c.isSetDataAmount()) {
            long nspDataAmount = c.getDataAmount().longValue();
            Long argonDataAmount =
                    Long.valueOf(convertNspToArgonDataAmount(nspDataAmount));
            internalConstraint.setDataAmount(argonDataAmount);
        }

        // for future use in argon
        // metric in nsp is milliseconds
        if (c.isSetMaxDelay()) {
            internalConstraint.setDelay(c.getMaxDelay());
        }

        if (c.isSetMinBW()) {
            if (c.getMinBW() != 0) {
                int nspMinBW = c.getMinBW();
                Long argonMinBW = Long.valueOf(convertNspToArgonBw(nspMinBW));
                internalConstraint.setMinBandwidth(argonMinBW);
            }
        }

        if (c.isSetMaxBW()) {
            int nspMaxBW = c.getMaxBW().intValue();
            Long argonMaxBW = Long.valueOf(convertNspToArgonBw(nspMaxBW));
            internalConstraint.setMaxBandwidth(argonMaxBW);
        }
        // for future use in argon
        // internalConstraint.setBandwidth(...);

        if (c.isSetDirectionality()) {
            if (c.getDirectionality() == 1) {
                con.setBidirectional(Boolean.valueOf(true));
            } else {
                con.setBidirectional(Boolean.valueOf(false));
            }
        }

        // get list of targets
        if (c.isSetTarget()) {
            List<EndpointType> targets = c.getTarget();

            // if there are more than on adresses throw Exception
            if (targets.size() > 1) {
                throw new RuntimeException(
                        "more than one target is not allowed");
            }
            // set destinationadress
            if (targets.get(0).isSetEndpointId()) {

                String intf =
                        this.tnaMapper.getInterfaceFromTNA(targets.get(0)
                                .getEndpointId().toString());
                con.setDestinationAddress(intf);
            }
        }

        if (c.isSetSource()) {
            if (c.getSource().isSetEndpointId()) {
                String intf =
                        this.tnaMapper.getInterfaceFromTNA(c.getSource()
                                .getEndpointId());
                con.setSourceAddress(intf);
            }
        }

        if (c.isSetConnectionID()) {
            con.setConnectionHandle(Long.valueOf(c.getConnectionID()));
        }

        con.setConnectionConstraint(internalConstraint);
        return con;
    }

    /**
     * maps NSP-Service to internal argon service.
     * 
     * @param nspService
     *            the NSP ServiceType
     * @return the argon ServiceType
     * @throws EndpointNotFoundFaultException
     */
    public ServiceType convertNspToArgonService(
            final ServiceConstraintType nspService)
            throws EndpointNotFoundFaultException {

        // catch unsupported Features
        if (nspService.isSetTypeOfReservation()) {
            if (nspService.getTypeOfReservation() == ReservationType.DEFERRABLE) {
                throw new RuntimeException(
                        "DeferreableReservations are not supported");
            } else if (nspService.getTypeOfReservation() == ReservationType.MALLEABLE) {
                throw new RuntimeException(
                        "MalleableReservations are not supported");
            }
        }

        // internal Service-Object
        ServiceType argonService = new ServiceType();
        argonService.setServiceHandle(new Long(nspService.getServiceID()));

        // check all connections from current Service
        for (ConnectionConstraintType c : nspService.getConnections()) {
            if (c != null) {
                ConnectionType con = convertNsptoArgonConnection(c);
                // add connection to current service
                argonService.getConnection().add(con);
            }
        }

        if (nspService.isSetFixedReservationConstraints()) {

            // should have startTime und duration
            FixedReservationConstraintType fct =
                    nspService.getFixedReservationConstraints();

            // internal TimeConstraint
            TimeConstraintType ts = new TimeConstraintType();

            // create DatatypeFactory
            DatatypeFactory dataf;
            try {
                dataf = DatatypeFactory.newInstance();
            } catch (DatatypeConfigurationException ex) {
                throw new RuntimeException(ex);
            }

            // start-time
            if (fct.isSetStartTime()) {
                XMLGregorianCalendar start = fct.getStartTime();
                GregorianCalendar endGreg = start.toGregorianCalendar();

                // add given amount of seconds (i.e. duration) to start-time
                if (fct.isSetDuration()) {
                    endGreg.add(Calendar.SECOND, fct.getDuration());
                }

                // convert to XMLGregorianCalendar
                XMLGregorianCalendar end =
                        dataf.newXMLGregorianCalendar(endGreg);

                // set information in the internal TimeConstraint
                ts.setStartDateTime(start);
                ts.setEndDateTime(end);
                argonService.setTimeConstraint(ts);
            }
            argonService.setMalleable(Boolean.valueOf(false));
        }

        // TODO: for now
        argonService.setServiceType("L2-P2P");

        if (nspService.isSetAutomaticActivation()) {
            argonService.setAutoActivation(Boolean.valueOf(nspService
                    .isAutomaticActivation()));
        }

        if (nspService.isSetServiceID()) {
            argonService.setServiceHandle(Long.valueOf(nspService
                    .getServiceID()));
        }
        return argonService;
    }

    /**
     * @param reservationId
     * @param serviceId
     * @return
     * @throws UnexpectedFaultException
     */
    private ServiceConstraintType convertArgonServiceToServiceConstraintType(
            ServiceType service) {
        ServiceConstraintType constraint = new ServiceConstraintType();
        FixedReservationConstraintType frct =
                new FixedReservationConstraintType();

        int serviceID = service.getServiceHandle().intValue();
        constraint.setServiceID(serviceID);
        if (service.isMalleable().booleanValue()) {
            constraint.setTypeOfReservation(ReservationType.MALLEABLE);
        } else {
            constraint.setTypeOfReservation(ReservationType.FIXED);
        }

        if (service.getConnection().isEmpty()) {
            log.debug("!!! No connections for Service !!!");
            constraint.setServiceID(serviceID);
        } else {
            constraint.setAutomaticActivation(service.isAutoActivation()
                    .booleanValue());

            XMLGregorianCalendar start =
                    service.getTimeConstraint().getStartDateTime();
            XMLGregorianCalendar end =
                    service.getTimeConstraint().getEndDateTime();
            frct.setStartTime(start);
            Long duration =
                    Long
                            .valueOf((end.toGregorianCalendar()
                                    .getTimeInMillis() - start
                                    .toGregorianCalendar().getTimeInMillis()) / 1000);
            frct.setDuration(duration.intValue());

            constraint.setFixedReservationConstraints(frct);

            for (ConnectionType connection : service.getConnection()) {
                ConnectionConstraintType conConstraint =
                        convertArgonConnectionToConnectionConstraintType(connection);
                constraint.getConnections().add(conConstraint);
            }
        }
        return constraint;
    }

    private ConnectionConstraintType convertArgonConnectionToConnectionConstraintType(
            ConnectionType connection) {

        ConnectionConstraintType conConstraint = new ConnectionConstraintType();

        if (connection.getConnectionConstraint().getMinBandwidth() != null) {
            conConstraint.setMinBW(convertArgonToNspBw(
                    connection.getConnectionConstraint().getMinBandwidth()
                            .longValue()).intValue());
        }

        if (connection.getSourceAddress() != null) {
            EndpointType endpointSource = new EndpointType();
            String tna =
                    this.tnaMapper.getTNAFromInterface(connection
                            .getSourceAddress());
            endpointSource.setEndpointId(tna);
            conConstraint.setSource(endpointSource);
        }

        if (connection.getDestinationAddress() != null) {
            EndpointType endpointTarget = new EndpointType();
            String tna =
                    this.tnaMapper.getTNAFromInterface(connection
                            .getDestinationAddress());
            endpointTarget.setEndpointId(tna);
            conConstraint.getTarget().add(endpointTarget);
        }

        if (connection.getConnectionHandle() != null) {
            conConstraint.setConnectionID(connection.getConnectionHandle()
                    .intValue());
        }

        if (connection.isBidirectional() != null) {
            if (connection.isBidirectional().booleanValue()) {
                conConstraint.setDirectionality(1);
            } else {
                conConstraint.setDirectionality(0);
            }
        }

        return conConstraint;
    }

    /**
     * maps an internal Argon-Service to a NSP-Service.
     * 
     * @param s
     *            ServiceType
     * @return ServiceStatus
     */
    public ServiceStatus convertArgonToNspService(final ServiceType s) {
        ServiceStatus stat = new ServiceStatus();

        if (s.getServiceHandle() != null) {
            stat.setServiceID(s.getServiceHandle().intValue());
        }
        if (s.getStatus() != null) {
            stat.setStatus(convertStatus(s.getStatus().intValue()));
        }

        // get start and end
        // TODO commented out because its not needed here .. but when we insert
        // a new getReservationData operation
        /*
         * if (s.getTimeConstraint() != null) { GregorianCalendar start =
         * s.getTimeConstraint().getStartDateTime() .toGregorianCalendar();
         * GregorianCalendar end = s.getTimeConstraint().getEndDateTime()
         * .toGregorianCalendar(); //create DatatypeFactory DatatypeFactory
         * dataf; try { dataf = DatatypeFactory.newInstance(); } catch
         * (DatatypeConfigurationException ex) { throw new RuntimeException(ex);
         * } //compute duration Long duration =
         * Long.valueOf((end.getTimeInMillis() - start.getTimeInMillis()) /
         * SEC_MILLISEC_FACTOR); XMLGregorianCalendar startXML =
         * dataf.newXMLGregorianCalendar(start); FixedReservationConstraintType
         * fixedResCons = of.createFixedReservationConstraintType();
         * fixedResCons.setStartTime(startXML);
         * fixedResCons.setDuration(duration.intValue());
         * stat.setTypeOfReservation(ReservationType.FIXED);
         * stat.setFixedReservationConstraints(fixedResCons); }
         */

        for (ConnectionType c : s.getConnection()) {
            stat.getConnections().add(convertArgonToNspConnection(c));
        }
        return stat;
    }

    /**
     * maps an internal Argon-Connection to a NSP-Connection.
     * 
     * @param c
     *            ConnectionType
     * @return ConnectionStatusType
     */
    public ConnectionStatusType convertArgonToNspConnection(
            final ConnectionType c) {
        ConnectionStatusType conStatus = of.createConnectionStatusType();
        EndpointType endpointSource = of.createEndpointType();
        EndpointType endpointTarget = of.createEndpointType();

        if (c.getStatus() != null) {
            conStatus.setStatus(convertStatus(c.getStatus().intValue()));
        }
        // what BW should we use? min max or Bandwidth, which is for future use
        if (c.getConnectionConstraint() != null) {
            de.unibonn.viola.argon.utils.jaxb.external.classes.IRes.ConnectionConstraintType constraint =
                    c.getConnectionConstraint();
            Long bw = constraint.getBandwidth();
            Long bwMin = constraint.getMinBandwidth();
            Long bwMax = constraint.getMaxBandwidth();
            long bwl = 0l;
            if (bw != null) {
                bwl = bw.longValue();
            } else if (bwMin != null) {
                bwl = bwMin.longValue();
            } else if (bwMax != null) {
                bwl = bwMax.longValue();
            }
            conStatus.setActualBW(convertArgonToNspBw(bwl).intValue());
        }

        if (c.getSourceAddress() != null) {
            String tna =
                    this.tnaMapper.getTNAFromInterface(c.getSourceAddress());
            endpointSource.setEndpointId(tna);
            conStatus.setSource(endpointSource);
        }

        if (c.getDestinationAddress() != null) {
            String tna =
                    this.tnaMapper.getTNAFromInterface(c
                            .getDestinationAddress());
            endpointTarget.setEndpointId(tna);
            conStatus.getTarget().add(endpointTarget);
        }

        if (c.getConnectionHandle() != null) {
            conStatus.setConnectionID(c.getConnectionHandle().intValue());
        }

        if (c.isBidirectional() != null) {
            if (c.isBidirectional().booleanValue()) {
                conStatus.setDirectionality(1);
            } else {
                conStatus.setDirectionality(0);
            }
        }
        return conStatus;
    }

    /**
     * create a unique ID.
     * 
     * @return long
     */
    public synchronized long getUniqueRequestId() {
        long requestID = System.currentTimeMillis();
        if (lastRequestID >= requestID) {
            requestID = ++lastRequestID;
        } else {
            lastRequestID = requestID;
        }
        return requestID;
    }

    /**
     * create a Argon-request out of a Nsp-GetStatus-request.
     * 
     * @param request
     *            GetStatusType
     * @return QueryRequestType
     */
    public QueryRequestType createArgonRequest(final GetStatusType request) {
        // internal QueryRequest
        QueryRequestType internalRequest = new QueryRequestType();

        // for future use when malleable-reservation are supported
        // internalRequest.setFixReservationTime(value);

        if (request.isSetReservationID()) {
            internalRequest.setReservationHandle(Long.valueOf(request
                    .getReservationID()));
        }
        return internalRequest;
    }

    /**
     * create a Argon-request out of a Nsp-GetStatus-request.
     * 
     * @param request
     *            CreateReservationType
     * @return ReservationRequestType
     * @throws EndpointNotFoundFaultException
     */
    public ReservationRequestType createArgonRequest(
            final CreateReservationType request)
            throws EndpointNotFoundFaultException {
        // internal Object which will be sent to JMS-Topic
        ReservationRequestType internalRequest = new ReservationRequestType();

        // the data inside of internalRequest
        ReservationDataType reqData = new ReservationDataType();

        // check all services from createReservation-Message
        for (ServiceConstraintType s : request.getService()) {
            reqData.getService().add(convertNspToArgonService(s));
        }

        if (request.isSetNotificationConsumerURL()) {
            reqData
                    .setNotificationAddress(request
                            .getNotificationConsumerURL());
        }

        internalRequest.setReservationData(reqData);

        internalRequest.setAAAInformation(getAAAInfo());

        return internalRequest;
    }

    /**
     * create a Argon-request out of a Nsp-CancelReservation-request.
     * 
     * @param request
     *            CancelReservationType
     * @return CancelRequestType
     */
    public CancelRequestType createArgonRequest(
            final CancelReservationType request) {
        // internal Object which will be sent to JMS-Topic
        CancelRequestType internalRequest = new CancelRequestType();

        // copy reservationID
        // check whether this information won't be overwritten inside of Argon
        if (request.isSetReservationID()) {
            internalRequest.setReservationHandle(Long.valueOf(request
                    .getReservationID()));
        }
        internalRequest.setAAAInformation(getAAAInfo());

        return internalRequest;
    }

    /**
     * create a Argon-request out of a Nsp-IsAvailable-request.
     * 
     * @param request
     *            IsAvailableType
     * @return AvailabilityRequestType
     * @throws EndpointNotFoundFaultException
     */
    public AvailabilityRequestType createArgonRequest(
            final IsAvailableType request)
            throws EndpointNotFoundFaultException {
        // internal Object which will be sent to JMS-Topic
        AvailabilityRequestType internalRequest = new AvailabilityRequestType();

        // convert services
        for (ServiceConstraintType s : request.getService()) {
            internalRequest.getService().add(convertNspToArgonService(s));
        }

        internalRequest.setAAAInformation(getAAAInfo());

        return internalRequest;
    }

    /**
     * create a Argon-request out of a Nsp-Activate-request.
     * 
     * @param request
     *            ActivateType
     * @return ActivationRequestType
     */
    public ActivationRequestType createArgonRequest(final ActivateType request) {
        // internal Object which will be sent to JMS-Topic
        ActivationRequestType internalRequest = new ActivationRequestType();

        if (request.isSetReservationID()) {
            internalRequest.setReservationHandle(Long.valueOf(request
                    .getReservationID()));
        }

        // actType contains only one ServiceID to be activated
        if (request.isSetServiceID()) {
            internalRequest.getServiceHandle().add(
                    Long.valueOf(request.getServiceID()));
        }

        return internalRequest;
    }

    /**
     * create a Nsp-GetStatus-response out of a Argon-GetStatus-reply.
     * 
     * @param reply
     *            QueryReplyType
     * @return GetStatusResponseType
     */
    public GetStatusResponseType createNspResponse(final QueryReplyType reply) {
        // fill responseType
        GetStatusResponseType responseType = of.createGetStatusResponseType();

        // this is optional in argon, therefore we need to check
        // if this information is set
        if (reply.getReservationData() != null) {
            for (ServiceType s : reply.getReservationData().getService()) {
                responseType.getServiceStatus()
                        .add(convertArgonToNspService(s));
            }
        }
        return responseType;
    }

    /**
     * create a Nsp-CreateReservation-response out of a
     * Argon-CreateReservation-reply.
     * 
     * @param reply
     *            ReservationReplyType
     * @return CreateReservationResponseType
     */
    public CreateReservationResponseType createNspResponse(
            final ReservationReplyType reply) {
        // populate this response with information from reply
        CreateReservationResponseType responseType =
                of.createCreateReservationResponseType();
        responseType
                .setReservationID(WebserviceUtils.convertReservationID(reply
                        .getReservationHandle().longValue()));

        // return response-Element
        return responseType;
    }

    /**
     * create a Nsp-CancelReservation-response out of a Argon-Cancel-reply.
     * 
     * @param reply
     *            CancelReplyType
     * @return CancelReservationResponseType
     */
    public CancelReservationResponseType createNspResponse(
            final CancelReplyType reply) {
        // populate this response with information from reply
        CancelReservationResponseType responseType =
                of.createCancelReservationResponseType();

        // there's only one interesting field, the result
        responseType.setSuccess(reply.isResult());

        return responseType;
    }

    /**
     * create a Nsp-IsAvailable-response out of a Argon-Availability-reply.
     * 
     * @param reply
     *            AvailabilityReplyType
     * @return IsAvailableResponseType
     */
    public IsAvailableResponseType createNspResponse(
            final AvailabilityReplyType reply) {

        // boolean availability = reply.isResult();

        // inverse Mapping from internal Object to external Object
        // populate this response with information from reply
        IsAvailableResponseType responseType =
                of.createIsAvailableResponseType();

        // TODO: is it possible that every service got its own start-time?
        // solution for now: take the earliest start-time of all services
        // in Argon theres only one computed offset for the whole reservation

        XMLGregorianCalendar couldRunAt = reply.getCouldRunAt();
        if (couldRunAt != null) {
            long earlierMillis = Long.MAX_VALUE;

            if (null != reply.getReservationData()) {
                for (ServiceType service : reply.getReservationData()
                        .getService()) {
                    long s =
                            service.getTimeConstraint().getStartDateTime()
                                    .toGregorianCalendar().getTimeInMillis();
                    if (s < earlierMillis) {
                        earlierMillis = s;
                    }
                }
            }

            // XMLGregorianCalander
            Calendar later = reply.getCouldRunAt().toGregorianCalendar();
            long laterMillis = later.getTimeInMillis();

            long delta = laterMillis - earlierMillis; // ms
            long offset = delta / 1000;
            if ((delta % 1000l) != 0) {
                offset++;
            }

            // needs to be computed
            responseType.setAlternativeStartTimeOffset(Long.valueOf(offset));
        }

        // TODO: what about maxBW or blockedEndpoints in IsAvailableResponse?

        // set availabilityType
        if (reply.getReservationData() != null) {
            for (ServiceType s : reply.getReservationData().getService()) {

                if (s.getConnection() != null) {
                    for (ConnectionType c : s.getConnection()) {

                        ConnectionAvailabilityType cAvail =
                                createConnectionAvail(s, c);

                        responseType.getDetailedResult().add(cAvail);
                    }
                }
            }
        } else {
            throw new RuntimeException("No reservation data in ARGON reply!");
        }
        return responseType;
    }

    /**
     * create a Nsp-Activate-response out of a Argon-Activation-reply.
     * 
     * @param reply
     *            ActivationReplyType
     * @return ActivateResponseType
     */
    public ActivateResponseType createNspResponse(
            final ActivationReplyType reply) {
        ActivateResponseType responseType = of.createActivateResponseType();

        // there's only one interesting field, the result
        responseType.setSuccess(reply.isResult());

        // return response-Element
        return responseType;
    }

    public GetReservationResponseType convertArgonRequestToGetReservationResponseType(
            ReservationRequestType res) {
        GetReservationResponseType response = new GetReservationResponseType();
        response.setJobID(res.getReservationHandle());
        // TODO:
        // response.setNotificationConsumerURL(value);
        for (ServiceType service : res.getReservationData().getService()) {
            response.getService().add(
                    convertArgonServiceToServiceConstraintType(service));
        }
        return response;
    }

    public GetReservationsComplexType convertArgonRequestToGetReservationsComplexType(
            ReservationRequestType res) {
        GetReservationsComplexType grct = new GetReservationsComplexType();
        long reservationId = res.getReservationHandle().longValue();
        grct.setReservationID(WebserviceUtils
                .convertReservationID(reservationId));
        grct
                .setReservation(convertArgonRequestToGetReservationResponseType(res));
        return grct;
    }

    public GetReservationsResponseType createNspResponse(
            ArrayList<ReservationRequestType> argonRequests) {
        final GetReservationsResponseType response =
                new GetReservationsResponseType();
        for (ReservationRequestType res : argonRequests) {
            response.getReservation().add(
                    convertArgonRequestToGetReservationsComplexType(res));
        }
        return response;
    }

    public GetReservationResponseType createNspResponse(
            ReservationRequestType argonRequest) {
        return convertArgonRequestToGetReservationResponseType(argonRequest);
    }
}
