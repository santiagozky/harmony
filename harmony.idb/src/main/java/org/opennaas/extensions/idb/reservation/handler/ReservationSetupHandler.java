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

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;
import org.apache.muse.ws.addressing.soap.SoapFault;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AvailabilityCodeType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ConnectionAvailabilityType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CreateReservationResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CreateReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DomainRelationshipType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.IsAvailableResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.IsAvailableType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ServiceConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.EndpointNotFoundFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.InvalidReservationIDFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.OperationNotSupportedFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.PathNotFoundFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.ReservationFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.utils.WebserviceUtils;
import org.opennaas.core.utils.Config;
import org.opennaas.core.utils.PhLogger;
import org.opennaas.extensions.idb.Constants;
import org.opennaas.extensions.idb.database.hibernate.Connections;
import org.opennaas.extensions.idb.database.hibernate.Domain;
import org.opennaas.extensions.idb.database.hibernate.Endpoint;
import org.opennaas.extensions.idb.database.hibernate.MAPNRPSResvID;
import org.opennaas.extensions.idb.database.hibernate.NrpsConnections;
import org.opennaas.extensions.idb.database.hibernate.Reservation;
import org.opennaas.extensions.idb.database.hibernate.Service;
import org.opennaas.extensions.idb.exception.database.DatabaseException;
import org.opennaas.extensions.idb.reservation.AdapterManager;
import org.opennaas.extensions.idb.reservation.IManager;
import org.opennaas.extensions.idb.reservation.PathFinderNG;
import org.opennaas.extensions.idb.utils.NotificationHelpers;

/**
 * Handler for Reservation Setup.
 */
public final class ReservationSetupHandler {
    /** Singleton instance. */
    private static ReservationSetupHandler selfInstance = null;
    /** NRPS Manager, for test-purposes using the JUnit-Mock. */
    private static IManager nrpsManager;
    /** standard Logger instance. */
    private final Logger logger = PhLogger.getLogger(this.getClass());

    private Logger performanceLogger = null;

    /** extra logger instance for the malleable reservations. */
    private final Logger malleableLogger = PhLogger.getSeparateLogger("malleable");

    /**
     * Instance getter.
     * 
     * @return Singleton Instance
     * @throws URISyntaxException
     *             URISyntaxException
     * @throws DatabaseException
     *             DatabaseException
     */
    public static ReservationSetupHandler getInstance() {
        if (ReservationSetupHandler.selfInstance == null) {
            ReservationSetupHandler.selfInstance = new ReservationSetupHandler();
        }
        return ReservationSetupHandler.selfInstance;
    }

    /** Used to implement exclusive createReservation requests. */
    private ReentrantLock createLock = null;

    /** Logger. */
    // private Logger log = PhLogger.getLogger(ReservationSetupHandler.class);
    /**
     * Private constructor: Singleton.
     * 
     * @throws URISyntaxException
     *             URISyntaxException
     * @throws DatabaseException
     *             DatabaseException
     */
    private ReservationSetupHandler() {
        if (Config.getString(Constants.idbProperties, "exclusiveCreateReservation")
                .equals("true")) {
            this.createLock = new ReentrantLock();
        }
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
     * CreateReservation Handler.
     * 
     * @param element
     *            CreateReservationType
     * @return CreateReservationResponseType
     * @throws SoapFault
     *             A SoapFault
     * @throws DatabaseException
     *             A DatabaseException
     */
    public CreateReservationResponseType createReservation(
            final CreateReservationType element) throws SoapFault,
            DatabaseException {

    	// check if a malleable reservation is requested
    	for(ServiceConstraintType sct : element.getService()) {
    		if (sct.getTypeOfReservation().equals(ReservationType.MALLEABLE)) {
    			return createMalleableReservation(element);
    		} else if (sct.getTypeOfReservation().equals(ReservationType.DEFERRABLE)) {
    			throw new OperationNotSupportedFaultException("deferrable reservations not yet supported");
    		}
    	}
    	// if this is no malleable reservation, handle it as normal fixed one
    	return createFixedReservation(element);
    }

 // ___________________________________________________________________________________________
 // MALLEABLE RESERVATION
 // ___________________________________________________________________________________________

    /**
     * handler for malleable reservation
     * 
     * @param element CreateReservationType
     * @return CreateReservationResponseType
     * @throws SoapFault
     * @throws DatabaseException
     */
    private CreateReservationResponseType createMalleableReservation(
    					CreateReservationType element) throws SoapFault, DatabaseException {
    	final long timeStart = System.currentTimeMillis();
    	this.logger.debug("-> malleable Reservation call");

        final Reservation resv = Reservation.fromJaxb(element);

        boolean dummyUsing = false;
    	// get nrpsManager for malleable reservations
    	for(Service service : resv.getServices().values()) {
    		for(Connections conn : service.getConnections().values()) {
    	    	if(conn.getStartpoint().getTNA().equals("128.1.0.10") &&
    	    		conn.getEndpoints().iterator().next().getTNA().equals("128.3.0.10")) {
    	    	    		dummyUsing = true;
    	    	}
    		}
    	}
    	
    	if(dummyUsing) {
    		this.malleableLogger.debug("using malleableDummyAdapter!");
    		ReservationSetupHandler.nrpsManager = AdapterManager.getMalleableInstance();
    	} else {
    		this.malleableLogger.debug("using real Adapter!");
    		ReservationSetupHandler.nrpsManager = AdapterManager.getInstance();
    	}

    	final long timePrePathFinder = System.currentTimeMillis();
    	getPerformanceLogger().info("duration_prePathFinder " + (timePrePathFinder - timeStart) + "ms");
        final PathFinderNG pathFinder = new PathFinderNG(resv,
                ReservationSetupHandler.nrpsManager,
                element.isSetSubdomainRestriction() && element.isSubdomainRestriction(), true);
    	final long timePathFinderInit = System.currentTimeMillis();
    	getPerformanceLogger().info("duration_initPathFinder " + (timePathFinderInit - timePrePathFinder) + "ms");
        if ((!pathFinder.isAvailable()) && pathFinder.isMultidomain()) {
            throw pathFinder.getPathNotFound();
        }

    	final long timeAvailability = System.currentTimeMillis();
    	getPerformanceLogger().info("duration_availability " + (timeAvailability - timePathFinderInit) + "ms");

        final HashMap<Domain, Reservation> nrpsReservations = pathFinder.getReservations();
        final long timePostPathFinder = System.currentTimeMillis();
        getPerformanceLogger().info("duration_PathFinderGetResv " + (timePostPathFinder - timeAvailability) + "ms");

        if(null == nrpsReservations) {
        	throw new ReservationFaultException("Failure in reservation-processing! No feasible reservations found in PathFinder!");
        }

        // compose createReservation requests
        final Hashtable<Domain, CreateReservationType> nrpsRequests = new Hashtable<Domain, CreateReservationType>();
        
        for (final Domain domain : nrpsReservations.keySet()) {
        	CreateReservationType r = nrpsReservations.get(domain).toJaxb();
        	if (domain.getRelationship().equals(DomainRelationshipType.PEER.value())) {
        		r.setSubdomainRestriction(true);
        	}
        	nrpsRequests.put(domain, r);
        }

        final long timePreNRPSController = System.currentTimeMillis();
        getPerformanceLogger().info("duration_postPathFinder_preNRPSController " + (timePreNRPSController - timePostPathFinder) + "ms");
        final Hashtable<Domain, CreateReservationResponseType> nrpsResponses =
        														createResInNRPS(nrpsRequests);
        final long timePostNRPSController = System.currentTimeMillis();
        getPerformanceLogger().info("duration_NRPSController " + (timePostNRPSController - timePreNRPSController) + "ms");

        // save reservation data to DB and create response
        for(Entry<Domain,Reservation> entry : nrpsReservations.entrySet()) {
            for(Service s : entry.getValue().getServices().values()) {
            	Service globalService = resv.getService(s.getServiceId());
            	globalService.setStartTime(s.getStartTime());
            	globalService.setDeadline(s.getDeadline());
            	globalService.setDuration(s.getDuration());
           		for(Connections c : s.getConnections().values()) {
           			Connections globalConn = globalService.getConnection(c.getConnectionId());
           			globalConn.setMinBandwidth(c.getMinBandwidth());
           			globalConn.setMaxBandwidth(c.getMaxBandwidth());
           			
           			NrpsConnections globalNC = globalConn.getNrpsConnection(entry.getKey());
           			globalNC.setBandwidth(c.getNrpsConnection(entry.getKey()).getBandwidth());
            	}
            }
        }
        final CreateReservationResponseType response = saveResInDbAndCreateResponse(nrpsResponses, resv);

        final long timeStop = System.currentTimeMillis();
        getPerformanceLogger().info("duration_postNRPSController " + (timeStop - timePostNRPSController) + "ms");
        return response;    	
    }

// ___________________________________________________________________________________________
// FIXED RESERVATION
// ___________________________________________________________________________________________

	/**
     * handler for fixed reservation
     * 
     * @param element CreateReservationType
     * @return CreateReservationResponseType
     * @throws SoapFault
     * @throws DatabaseException
     */
    private CreateReservationResponseType createFixedReservation(
    						CreateReservationType element) throws SoapFault, DatabaseException {
    	final long timeStart = System.currentTimeMillis();
    	this.logger.debug("-> fixed Reservation call");
    	// get nrpsManager for fixed reservations
        ReservationSetupHandler.nrpsManager = AdapterManager.getInstance();

        // get available multi-domain path
        final Reservation resv = Reservation.fromJaxb(element);

    	final long timePrePathFinder = System.currentTimeMillis();
    	getPerformanceLogger().info("duration_prePathFinder " + (timePrePathFinder - timeStart) + "ms");
        final PathFinderNG pathFinder = new PathFinderNG(resv,
                ReservationSetupHandler.nrpsManager,
                element.isSetSubdomainRestriction() && element.isSubdomainRestriction(), false);
    	final long timePathFinderInit = System.currentTimeMillis();
    	getPerformanceLogger().info("duration_initPathFinder " + (timePathFinderInit - timePrePathFinder) + "ms");
        if (pathFinder.isMultidomain() && (!pathFinder.isAvailable())) {
            throw pathFinder.getPathNotFound();
        }

    	final long timeAvailability = System.currentTimeMillis();
    	getPerformanceLogger().info("duration_availability " + (timeAvailability - timePathFinderInit) + "ms");

        final HashMap<Domain, Reservation> nrpsReservations = pathFinder
                .getReservations();
    	final long timePostPathFinder = System.currentTimeMillis();
    	getPerformanceLogger().info("duration_PathFinderGetResv " + (timePostPathFinder - timeAvailability) + "ms");

        // compose createReservation requests
        final Hashtable<Domain, CreateReservationType> nrpsRequests = new Hashtable<Domain, CreateReservationType>();
        for (final Domain domain : nrpsReservations.keySet()) {
        	CreateReservationType r = nrpsReservations.get(domain).toJaxb();
        	
        	
        	if (domain.getRelationship().equals(DomainRelationshipType.PEER.value())) {
        		r.setSubdomainRestriction(true);
        	}
            nrpsRequests.put(domain, r);
        }

    	final long timePreNRPSController = System.currentTimeMillis();
    	getPerformanceLogger().info("duration_postPathFinder_preNRPSController " + (timePreNRPSController - timePostPathFinder) + "ms");
        final Hashtable<Domain, CreateReservationResponseType> nrpsResponses =
        													createResInNRPS(nrpsRequests);
    	final long timePostNRPSController = System.currentTimeMillis();
    	getPerformanceLogger().info("duration_NRPSController " + (timePostNRPSController - timePreNRPSController) + "ms");

        // save reservation data to DB and create response
        final CreateReservationResponseType response = 
        							saveResInDbAndCreateResponse(nrpsResponses, resv);

    	final long timeStop = System.currentTimeMillis();
    	getPerformanceLogger().info("duration_postNRPSController " + (timeStop - timePostNRPSController) + "ms");
        return response;    	
    }

    /**
     * try to create reservation in NRPS and resturn responses
     * @param nrpsRequests
     * @return
     * @throws SoapFault
     * @throws DatabaseException
     */
    private Hashtable<Domain, CreateReservationResponseType> createResInNRPS(
    								Hashtable<Domain, CreateReservationType> nrpsRequests)
    													throws SoapFault, DatabaseException {
        final Hashtable<Domain, CreateReservationResponseType> nrpsResponses;
        // try to create reservation
    	if (this.createLock != null) {
    		this.createLock.lock();
    	}
    	try {
    	     nrpsResponses =
    					ReservationSetupHandler.nrpsManager.createReservation(nrpsRequests);
    	} finally {
    	    if (this.createLock != null) {
    		this.createLock.unlock();
    	    }
    	}    	
        // check if all requests were successful
        // NRPS manager does rollback, but returns replies as they came from the
        // domains
        for (final CreateReservationResponseType r : nrpsResponses.values()) {
            if ((r == null) || (!r.isSetReservationID())) {
                throw new PathNotFoundFaultException();
            }
        }

    	return nrpsResponses;
    }

    /**
     * try to save the given reservation in the DB and create a CreateReservationResponseType
     * @param nrpsResponses
     * @param res
     * @return
     * @throws EndpointNotFoundFaultException
     * @throws DatabaseException
     * @throws InvalidReservationIDFaultException
     */
    private CreateReservationResponseType saveResInDbAndCreateResponse(
    					Hashtable<Domain, CreateReservationResponseType> nrpsResponses,
						Reservation res) throws EndpointNotFoundFaultException, DatabaseException, InvalidReservationIDFaultException {
        // save reservation data to DB and create response
        final CreateReservationResponseType response = new CreateReservationResponseType();
        res.loadOrCreateUserEndpoints();
        res.save();

        for (final Domain domain : nrpsResponses.keySet()) {
            final MAPNRPSResvID map = new MAPNRPSResvID(
                    res, (nrpsResponses.get(domain)
                            .getReservationID()), domain);
            map.save();

            // subscribe for notifications in according notification-WS
            NotificationHelpers.subscribeToWSN(map.getDomain().getName(), map
                    .getnrpsReservationId());
        }
        // create topic for higher level IDB's if existent
        NotificationHelpers.createTopicForWSN(res.getReservationId());
        response.setReservationID(WebserviceUtils.convertReservationID(res.getReservationId()));
        
        response.setGRI(res.getGri());
        response.setToken(res.getToken());
        
        return response;    	
    }

    /**
     * IsAvailable Handler.
     * 
     * @param element
     *            IsAvailableType
     * @return IsAvailableResponseType
     * @throws SoapFault
     * @throws DatabaseException
     */
    public IsAvailableResponseType isAvailable(final IsAvailableType element)
            throws DatabaseException, SoapFault {
    	// get nrpsManager for fixed reservations
        ReservationSetupHandler.nrpsManager = AdapterManager.getInstance();

        final IsAvailableResponseType response = new IsAvailableResponseType();
        final Reservation res = Reservation.fromJaxb(element);

        final PathFinderNG sh = new PathFinderNG(res,
                ReservationSetupHandler.nrpsManager,
                element.isSetSubdomainRestriction() && element.isSubdomainRestriction(), false);
        HashMap<Integer, HashMap<Integer, ConnectionAvailabilityType>> detailedResults = new HashMap<Integer, HashMap<Integer, ConnectionAvailabilityType>>();
        for (final ConnectionAvailabilityType ca : sh.getDetailedResult()) {
        	int serviceID = ca.getServiceID();
        	int connectionID = ca.getConnectionID();
        	HashMap<Integer, ConnectionAvailabilityType> sMap = detailedResults.get(serviceID);
        	if (sMap == null) {
        		sMap = new HashMap<Integer, ConnectionAvailabilityType>();
        		detailedResults.put(ca.getServiceID(), sMap);
        	}
        	ConnectionAvailabilityType caResult = sMap.get(connectionID);

        	Set<String> blockedEndpoints = new HashSet<String>();
    		if (ca.isSetBlockedEndpoints()) {
    			for (String ep : ca.getBlockedEndpoints()) {
    				Connections iConn = res.getService(serviceID).getConnection(connectionID);
    				boolean isInput = ep.equals(iConn.getStartpoint().getTNA());
    				if (! isInput) {
    					for (Endpoint e : iConn.getEndpoints()) {
    						isInput = ep.equals(e.getTNA());
    						if (isInput) {
    							break;
    						}
    					}
    				}
    				if (isInput) {
    					blockedEndpoints.add(ep);
    				}
    			}
			}

        	if (caResult == null) {
        		sMap.put(ca.getConnectionID(), ca);
        		response.getDetailedResult().add(ca);
        		if (ca.isSetBlockedEndpoints()) {
        			Iterator<String> it = ca.getBlockedEndpoints().iterator();
        			while (it.hasNext()) {
        				String ep = it.next();
        				if (! blockedEndpoints.contains(ep)) {
        					it.remove();
        				}
        			}
        		}
        	} else {
        		if (ca.getAvailability() == AvailabilityCodeType.ENDPOINT_NOT_AVAILABLE) {
        			caResult.setAvailability(AvailabilityCodeType.ENDPOINT_NOT_AVAILABLE);
        			for (String ep : blockedEndpoints) {
        				caResult.getBlockedEndpoints().add(ep);
        			}
        		} else if (caResult.getAvailability() == AvailabilityCodeType.AVAILABLE) {
        			caResult.setAvailability(ca.getAvailability());
        		}
        		if (ca.isSetMaxBW()) {
        			int maxBw = ca.getMaxBW();
        			if (caResult.isSetMaxBW()) {
        				if (maxBw < caResult.getMaxBW()) {
            				caResult.setMaxBW(maxBw);
        				}
        			} else {
        				caResult.setMaxBW(maxBw);
        			}
        		}
        	}
        }
        response.setAlternativeStartTimeOffset(sh
                .getAlternativeStartTimeOffset());
        return response;
    }
}
