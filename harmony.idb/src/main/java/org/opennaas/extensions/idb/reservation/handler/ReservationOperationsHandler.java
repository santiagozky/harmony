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


package org.opennaas.extensions.idb.reservation.handler;

import java.rmi.UnexpectedException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.apache.muse.ws.addressing.soap.SoapFault;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ActivateResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ActivateType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelReservationResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ConnectionStatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DomainConnectionStatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DomainStatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointInterfaceType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatusResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ServiceStatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.StatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatusResponseType.ServiceStatus;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.InvalidServiceIDFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.ReservationIDNotFoundFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.utils.WebserviceUtils;
import org.opennaas.core.utils.PhLogger;
import org.opennaas.extensions.idb.database.hibernate.Connections;
import org.opennaas.extensions.idb.database.hibernate.Domain;
import org.opennaas.extensions.idb.database.hibernate.MAPNRPSResvID;
import org.opennaas.extensions.idb.database.hibernate.NrpsConnections;
import org.opennaas.extensions.idb.database.hibernate.Reservation;
import org.opennaas.extensions.idb.database.hibernate.Service;
import org.opennaas.extensions.idb.exception.database.DatabaseException;
import org.opennaas.extensions.idb.reservation.AdapterManager;
import org.opennaas.extensions.idb.reservation.IManager;
import org.opennaas.extensions.idb.utils.NotificationHelpers;

/**
 * Handler for Reservation Operations.
 */
public final class ReservationOperationsHandler {
    /** Singleton instance. */
    private static ReservationOperationsHandler selfInstance = null;

    /**
     * ReservationOperationsHandler instance getter.
     * 
     * @return Singleton Instance
     */
    public static ReservationOperationsHandler getInstance() {
        if (ReservationOperationsHandler.selfInstance == null) {
            ReservationOperationsHandler.selfInstance = new ReservationOperationsHandler();
        }
        return ReservationOperationsHandler.selfInstance;
    }

    /** NRPS Manager instance. */
    private final IManager nrpsManager;

    /** Logger instance. */
    private final Logger logger = PhLogger.getLogger(this.getClass());

    private Logger performanceLogger = null;

    /**
     * Private constructor: Singleton.
     */
    private ReservationOperationsHandler() {
        this.nrpsManager = AdapterManager.getInstance();
    }

    private Logger getPerformanceLogger() {
    	if (this.performanceLogger == null) {
    		try {
				this.performanceLogger = ReservationRequestHandler.getInstance().getPerformanceLogger();
			} catch (SoapFault e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	return this.performanceLogger;
    }

    /**
     * ActivateReservation Handler.
     * 
     * @param element
     *            CancelReservationType
     * @return CancelReservationResponseType
     * @throws SoapFault
     *             A SoapFault
     * @throws DatabaseException
     *             DatabaseException
     */
    public ActivateResponseType activation(final ActivateType element)
            throws SoapFault, DatabaseException {
        final Reservation resv = Reservation.load(element.getReservationID());
        if (resv == null) {
        	throw new ReservationIDNotFoundFaultException("Reservation ID " + element.getReservationID() + " not found");
        }
        // resv.loadServices();
        final Service service = resv.getService(element.getServiceID());
        if (service == null) {
        	throw new InvalidServiceIDFaultException("Service ID " + element.getServiceID() + " not found (within reservation ID " + element.getReservationID() + ")");
        }
        final Hashtable<Domain, ActivateType> nrpsRequests = new Hashtable<Domain, ActivateType>();
        for (final MAPNRPSResvID map : MAPNRPSResvID
                .getMappingForReservation(resv)) {
            final Domain domain = map.getDomain();
            final ActivateType nrpsRequest = new ActivateType();
            nrpsRequest.setReservationID(WebserviceUtils
                    .convertReservationID(map.getnrpsReservationId()));
            // TODO:
            // it may very well be the case that this service is not located in
            // the domain ...
            // do we need to make the mapping more precise??
            nrpsRequest.setServiceID(element.getServiceID());
            nrpsRequest.setGRI(element.getGRI());
            nrpsRequest.setToken(element.getToken());
            nrpsRequests.put(domain, nrpsRequest);
        }
        // Hashtable<Domain, ActivateResponseType> nrpsResponses =
        this.nrpsManager.activateReservation(nrpsRequests);
        // TODO check success fields?
        final ActivateResponseType response = new ActivateResponseType();
        response.setSuccess(true);
        return response;
    }

    /**
     * CancelReservation Handler.
     * 
     * @param element
     *            CancelReservationType
     * @return CancelReservationResponseType
     * @throws SoapFault
     *             A SoapFault
     * @throws DatabaseException
     *             DatabaseException
     */
    public CancelReservationResponseType cancelReservation(
            final CancelReservationType element) throws SoapFault,
            DatabaseException {

        final Reservation resv = Reservation.load(element.getReservationID());

        if (resv == null) {
        	throw new ReservationIDNotFoundFaultException("Reservation ID " + element.getReservationID() + " not found");
        }

        final Hashtable<Domain, CancelReservationType> nrpsRequests = new Hashtable<Domain, CancelReservationType>();

        for (final MAPNRPSResvID map : MAPNRPSResvID.getMappingForReservation(resv)) {
            final Domain domain = map.getDomain();
            final CancelReservationType nrpsRequest = new CancelReservationType();
            nrpsRequest.setGRI(element.getGRI());
            nrpsRequest.setToken(element.getToken());
            nrpsRequest.setReservationID(WebserviceUtils
                    .convertReservationID(map.getnrpsReservationId()));
            nrpsRequests.put(domain, nrpsRequest);
        }
        // Hashtable<Domain, CancelReservationResponseType> nrpsResponses =
        this.nrpsManager.cancelReservation(nrpsRequests);
        // TODO check success fields?

        // remove topic from notification-WS
        NotificationHelpers.removeTopicFromWSN(WebserviceUtils
                .convertReservationID(element.getReservationID()));

        final CancelReservationResponseType response = new CancelReservationResponseType();
        response.setSuccess(true);
        return response;
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

    /**
     * Add a new ConnectionStatusType (from a different domain) to a target
     * ConnectionStatusType object that is to be returned on the northbound
     * interface.
     * 
     * @param target
     *            Target ConnectionStatusType object. May be null, in this case
     *            a new target is created.
     * @param status
     *            New ConnectionStatusType object.
     * @param domainName
     *            Name of the domain the new ConnectionStatusType object
     *            originates from.
     * @return Target ConnectionStatusType object (or the newly created one, if
     *         target was null).
     */
    protected ConnectionStatusType combine(final ConnectionStatusType target,
            final ConnectionStatusType status, final String domainName) {
        ConnectionStatusType result;
        if (target == null) {
            result = new ConnectionStatusType();
        } else {
            result = target;
        }

        // DomainStatus

        final DomainConnectionStatusType ds = new DomainConnectionStatusType();
        ds.setDomain(domainName);
        ds.setStatus(status);

        result.getDomainStatus().add(ds);

        if (target == null) {
            // copy fields
            result.setConnectionID(status.getConnectionID());
            result.setSource(status.getSource());
            result.getTarget().clear();
            for (final EndpointType e : status.getTarget()) {
                result.getTarget().add(e);
            }
            result.setDirectionality(status.getDirectionality());
            result.setStatus(status.getStatus());
            result.setActualBW(status.getActualBW());
        } else {
            // TODO check for equality

            // Status value

            if ((result.getStatus().ordinal() < status.getStatus().ordinal())) {
                result.setStatus(status.getStatus());
            }

            // DomainStatus

            // for (DomainConnectionStatusType s : status.getDomainStatus()) {
            // result.getDomainStatus().add(s);
            // }
        }

        return result;
    }

    /**
     * Add a new ServiceStatus (from a different domain) to a target
     * ServiceStatus object that is to be returned on the northbound interface.
     * 
     * @param target
     *            Target ServiceStatus object. May be null, in this case a new
     *            target is created.
     * @param status
     *            New ServiceStatus object.
     * @param domainName
     *            Name of the domain the new ServiceStatus object originates
     *            from.
     * @return Target ServiceStatus object (or the newly created one, if target
     *         was null).
     */
    protected ServiceStatus combine(final ServiceStatus target,
            final ServiceStatus status, final String domainName) {

        ServiceStatus result;
        if (target == null) {
            result = new ServiceStatus();
        } else {
            result = target;
        }

        // DomainStatus

        final DomainStatusType ds = new DomainStatusType();
        ds.setDomain(domainName);
        ds.setStatus(status.getStatus());

        result.getDomainStatus().add(ds);

        if (target == null) {

            // Set service ID & status value

            result.setServiceID(status.getServiceID());
            result.setStatus(status.getStatus());

        } else {

            // Assert that the service IDs are equal

            if (result.getServiceID() != status.getServiceID()) {
                throw new RuntimeException("inconsistent service parameters");
            }

            // Status value

            if ((result.getStatus().ordinal() < status.getStatus().ordinal())) {
                result.setStatus(status.getStatus());
            }

            // DomainStatus
            // Here subdomains are added in the same hierarchy-level like their
            // parent-Domains. Therefore
            // commented this out. Have to observe, if additional info-loss
            // happens through this.
            // for (DomainStatusType s : status.getDomainStatus()) {
            // result.getDomainStatus().add(s);
            // }
        }

        for (final ConnectionStatusType cstatus : status.getConnections()) {
            final Iterator<ConnectionStatusType> i = result.getConnections()
                    .iterator();
            ConnectionStatusType rcs = null;
            while ((rcs == null) && i.hasNext()) {
                final ConnectionStatusType c = i.next();
                if (cstatus.getConnectionID() == c.getConnectionID()) {
                    rcs = c;
                }
            }
            if (rcs == null) {
                rcs = this.combine(rcs, cstatus, domainName);
                result.getConnections().add(rcs);
            } else {
                rcs = this.combine(rcs, cstatus, domainName);
            }
        }

        return result;
    }

    /**
     * GetStatus Handler.
     * 
     * @param element
     *            Status query.
     * @return Status response.
     * @throws DatabaseException
     *             DatabaseException
     * @throws SoapFault
     *             Fault
     * @throws UnexpectedException
     */
    public GetStatusResponseType status(final GetStatusType element)
            throws DatabaseException, SoapFault, UnexpectedException {
    	final long timeStart = System.currentTimeMillis();
    	
//        final GetStatusResponseType response = new GetStatusResponseType();

        // List of services queried
        final Set<Integer> todo = new HashSet<Integer>();

        // status for each of the queried services
        // Hashtable<Integer, ServiceStatus> statusTable =
        // new Hashtable<Integer, ServiceStatus>();

        // load reservation
        final Reservation resv = Reservation.load(element.getReservationID());
        final long resvLoadTime = System.currentTimeMillis(); 
    	getPerformanceLogger().info("duration_resvLoad " + (resvLoadTime - timeStart) + "ms");
        if (resv == null) {
            // Reservation not found -> throw Exception
        	throw new ReservationIDNotFoundFaultException("ReservationID " 
        												  + element.getReservationID()
        												  + " not found!");
        }

        // fill todo set
        if (element.isSetServiceID()) {
            for (final Integer id : element.getServiceID()) {
                todo.add(id);
            }
        } else {
            // load services from DB
            // Alex: gibt es nicht mehr ... resv.loadServices();
            for (final Service s : resv.getServices().values()) {
                todo.add(Integer.valueOf(s.getServiceId()));
            }
        }
        //
        // // get reservation ID mapping
        // List<MAPNRPSResvID> mapping =
        // MAPNRPSResvID.getMappingForReservation(resv.getReservationId());
        //
        // // requests for all involved NRPSs
        // Hashtable<Domain, GetStatusType> nrpsRequests =
        // new Hashtable<Domain, GetStatusType>();
        //
        // // fill nrpsRequests table
        // for (MAPNRPSResvID m : mapping) {
        // Domain dom = Domain.load(m.getfkDomainName());
        // GetStatusType req = new GetStatusType();
        // req.setReservationID(m.getnrpsReservationId());
        // for (Integer i : todo) {
        // req.getServiceID().add(i);
        // }
        // nrpsRequests.put(dom, req);
        // }
        //
        // // get responses
        // Hashtable<Domain, GetStatusResponseType> nrpsResponses =
        // this.nrpsManager.getStatus(nrpsRequests);

    	final Hashtable<Domain, GetStatusType> nrpsRequests = resv.getGetStatusType();
    	final long timePreNRPSManager = System.currentTimeMillis();
    	getPerformanceLogger().info("duration_createNRPSRequests " + (timePreNRPSManager - resvLoadTime) + "ms");
    	final Hashtable<Domain, GetStatusResponseType> nrpsResponses = this.nrpsManager
                .getStatus(nrpsRequests);
    	final long timePostNRPSManager = System.currentTimeMillis();
    	getPerformanceLogger().info("duration_NRPSController " + (timePostNRPSManager - timePreNRPSManager) + "ms");
        // EVIL HACK! fix the surfnet answer
        if (todo.size() == 1) {
            ServiceStatus surfnetStatus = null;
            for (final Domain dom : nrpsResponses.keySet()) {
                if (dom.getName().equals("SURFnet")) {
                    surfnetStatus = nrpsResponses.get(dom).getServiceStatus()
                            .get(0);
                    break;
                }
            }
            if (surfnetStatus != null) {
                final int serviceId = todo.iterator().next().intValue();
                final Service service = resv.getService(serviceId);
                // service.loadConnections();
                if (service.getConnections().size() == 1) {
                    final int oldServiceId = surfnetStatus.getServiceID();
                    if (oldServiceId != serviceId) {
                        this.logger.debug("fixing SURFnet reply service ID: "
                                + oldServiceId + " -> " + serviceId);
                        surfnetStatus.setServiceID(serviceId);
                    }
                    final ConnectionStatusType conn = surfnetStatus
                            .getConnections().get(0);
                    final int connectionId = service.getConnections().values()
                            .iterator().next().getConnectionId();
                    final int oldConnectionId = conn.getConnectionID();
                    if (oldConnectionId != connectionId) {
                        this.logger
                                .debug("fixing SURFnet reply connection ID: "
                                        + oldConnectionId + " -> "
                                        + connectionId);
                        conn.setConnectionID(connectionId);
                    }
                }
            }
        }

        // set all transient status values to "unknown"
        // in case a domain does not reply with a status value
    	for (Service s : resv.getServices().values()) {
    		for (Connections c : s.getConnections().values()) {
    			for (NrpsConnections nc : c.getNrpsConnections().values()) {
    				if (! WebserviceUtils.isFinalStatus(nc.getStatusType())) {
    					nc.setStatus(StatusType.UNKNOWN);
    				}
    			}
    		}
    	}

        for (final Entry<Domain, GetStatusResponseType> entry : nrpsResponses
                .entrySet()) {
            if (entry.getValue().isSetServiceStatus()) {
                for (final ServiceStatusType serviceStatusType : entry
                        .getValue().getServiceStatus()) {
                    if (serviceStatusType.isSetConnections()) {
                        for (final ConnectionStatusType connection : serviceStatusType
                                .getConnections()) {

                            final int serviceId = serviceStatusType
                                    .getServiceID();
                            final int connectionId = connection
                                    .getConnectionID();
                            final String domainName = entry.getKey().getName();
                            final StatusType status = connection.getStatus();

                            this.logger.debug("service ID: " + serviceId);
                            this.logger.debug("connection ID: " + connectionId);
                            this.logger.debug("domain: " + domainName);
                            this.logger.debug("status: " + status);
                            this.logger.debug("Setting new Status");

                                Service thisService = resv
                                        .getService(serviceId);
                                Connections thisConnection = thisService
                                        .getConnection(connectionId);
                                if (null == thisConnection) {
                                    throw new UnexpectedException(
                                            "returned connection ID '" + connectionId + "' doesn't match to current reservation");
                                }
                                Domain thisDomain = entry.getKey();
                                NrpsConnections thisNrpsConnection = thisConnection
                                        .getNrpsConnection(thisDomain);
                                if (null == thisNrpsConnection) {
                                    throw new UnexpectedException(
                                            "returned ID's don't match to current reservation");
                                }
                                thisNrpsConnection.setStatus(status);
                        }
                    } else {
                        // the response does not contain any connections so we
                        // send a mail for debugging.
                        String messageForGetStatus = "";
                        messageForGetStatus += "Getting Status for Reservation: \n"
                                + resv.getDebugInfo();
                        messageForGetStatus += "\n nrpsresponse from Domain "
                                + entry.getKey()
                                + " did not contain any connections!";
                        this.logger.fatal(messageForGetStatus);
                    }
                }
            } else {
                // the response does not contain any service so we send a
                // mail for debugging.
                String messageForGetStatus = "";
                messageForGetStatus += "Getting Status for Reservation: \n"
                        + resv.getDebugInfo();
                messageForGetStatus += "\n nrpsresponse from Domain "
                        + entry.getKey() + " did not contain any services!";
                this.logger.fatal(messageForGetStatus);
            }
        }

        resv.save();

        // for (Domain dom : nrpsResponses.keySet()) {
        // boolean cancelledBySystem = false;
        //
        // for (ServiceStatus status : nrpsResponses.get(dom)
        // .getServiceStatus()) {
        // int serviceId = status.getServiceID();
        // ServiceStatus combined =
        // statusTable.get(Integer.valueOf(serviceId));
        // if (combined == null) {
        // combined = combine(combined, status, dom.getName());
        // statusTable.put(Integer.valueOf(serviceId), combined);
        // response.getServiceStatus().add(combined);
        // } else {
        // combined = combine(combined, status, dom.getName());
        // }
        //
        // if (combined.getStatus().equals(StatusType.CANCELLED_BY_SYSTEM)) {
        // cancelledBySystem = true;
        // }
        // }
        //
        // // if DomainStatus is "CANCELLED_BY_SYSTEM" send error-mail
        // if (cancelledBySystem) {
        // this.logger.fatal("Status of domain " + dom.getName()
        // + " was CANCELLED_BY_SYSTEM!");
        // }
        // }
        //
        // // put endpoints into the response
        // for (ServiceStatus s : response.getServiceStatus()) {
        // int serviceId = s.getServiceID();
        // try {
        // Service idbService = resv.getService(serviceId);
        // for (ConnectionStatusType c : s.getConnections()) {
        // int connectionId = c.getConnectionID();
        // Connections idbConnection =
        // idbService.getConnection(connectionId);
        // EndpointType src = new EndpointType();
        // src.setEndpointId(idbConnection.getStartpoint().getTNA());
        // c.setSource(src);
        // c.unsetTarget();
        // for (Endpoint ep : idbConnection.getEndpoints()) {
        // EndpointType t = new EndpointType();
        // t.setEndpointId(ep.getTNA());
        // c.getTarget().add(t);
        // }
        // }
        // } catch (NullPointerException e) {
        // // !!! this try-catch is ONLY for test-purposes !!!
        // // if there are mock-answers, a NPE occurs because of not
        // // existing endpoints
        // }
        // }

    	final long timeStop = System.currentTimeMillis();
    	getPerformanceLogger().info("duration_postNRPSController " + (timeStop - timePostNRPSManager) + "ms");
        return resv.getGetStatusResponseType();
    }

    /**
     * getStatus Dummy-Handler.
     * 
     * @param element
     *            getStatusType
     * @return GetStatusResponseType
     */
    public GetStatusResponseType statusDummy(final GetStatusType element) {
        final GetStatusResponseType responseType = new GetStatusResponseType();
        final ServiceStatus serviceStatus = new ServiceStatus();

        /* generate DetailedStatus-parameters ------------------------------ */

        final ConnectionStatusType cst = new ConnectionStatusType();
        cst.setStatus(StatusType.ACTIVE);
        cst.setActualBW(1);
        cst.setConnectionID(1);
        cst.setDirectionality(1);
        final EndpointType c1 = new EndpointType();
        c1.setEndpointId("127.0.0.1");
        c1.setName("DCE1");
        c1.setDescription("Dummy-Connection Endpoint 1");
        c1.setDomainId("TestDomain1");
        c1.setBandwidth(Integer.valueOf(1));
        c1.setInterface(EndpointInterfaceType.USER);
        cst.setSource(c1);
        final EndpointType c2 = new EndpointType();
        c2.setEndpointId("10.0.0.1");
        c2.setName("DCE2");
        c2.setDescription("Dummy-Connection Endpoint 2");
        c2.setDomainId("TestDomain2");
        c2.setBandwidth(Integer.valueOf(1));
        c2.setInterface(EndpointInterfaceType.USER);
        cst.getTarget().add(c2);
        /* ----------------------------------------------------------------- */

        serviceStatus.setServiceID(1);
        serviceStatus.getConnections().add(cst);

        serviceStatus
                .setServiceID(element.getServiceID().get(0).intValue() + 1);
        serviceStatus.setStatus(StatusType.ACTIVE);

        responseType.getServiceStatus().add(serviceStatus);

        return responseType;
    }
}
