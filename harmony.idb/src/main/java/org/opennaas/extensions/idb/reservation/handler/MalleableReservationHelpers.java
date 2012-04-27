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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.IsAvailableResponseType;
import org.opennaas.core.utils.Helpers;
import org.opennaas.core.utils.Tuple;
import org.opennaas.extensions.idb.database.hibernate.Connections;
import org.opennaas.extensions.idb.database.hibernate.Domain;
import org.opennaas.extensions.idb.database.hibernate.Endpoint;
import org.opennaas.extensions.idb.database.hibernate.NrpsConnections;
import org.opennaas.extensions.idb.database.hibernate.Reservation;
import org.opennaas.extensions.idb.database.hibernate.Service;
import org.opennaas.extensions.idb.exception.database.DatabaseException;


/**
 * Handler for Reservation Setup.
 */
public final class MalleableReservationHelpers {
	
    /** Singleton instance. */
    private static MalleableReservationHelpers selfInstance = null;

    /**
     * Instance getter.
     * 
     * @return Singleton Instance
     * @throws URISyntaxException
     *             URISyntaxException
     * @throws DatabaseException
     *             DatabaseException
     */
    public static MalleableReservationHelpers getInstance() {
        if (MalleableReservationHelpers.selfInstance == null) {
        	MalleableReservationHelpers.selfInstance = new MalleableReservationHelpers();
        }
        return MalleableReservationHelpers.selfInstance;
    }

    /**
     * Private constructor: Singleton.
     */
    private MalleableReservationHelpers() {
        // Singleton.
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
     * get all feasible BWs for a endpoint
     * @param domainId
     * @return
     */
    public static List<Integer> getBwsForEndpoint(String TNA, int minBw, int maxBw) {
    	
    	LinkedList<Integer> bwFromDB = new LinkedList<Integer>();
    	try {
    		// try to load endpoint from DB
			Endpoint end = Endpoint.load(TNA);
			if(end != null) {
		    	// TODO: get a list of bandwidths instead of only a single value
				int bw = end.getBandwidth();
				if( (bw >= minBw) && (bw <= maxBw) ) {
					bwFromDB.add(bw);
				}
			}
		} catch (DatabaseException e) {
			// nothing to do, bandwidths will be created automatically
		}
    	
    	// return list from DB if available
    	if(!bwFromDB.isEmpty()) {
    		return bwFromDB;
    	}
    	
    	// list of automatically created bandwidths for an endpoint
    	LinkedList<Integer> bws = new LinkedList<Integer>();

    	// test values -----------------------------------------------------------------
//    	if(TNA.equals("128.1.0.1")) {
//        	bws.add(new Integer(100)); // in MBit
//        	bws.add(new Integer(200)); // in MBit
//        	bws.add(new Integer(300)); // in MBit
//        	return bws;
//    	}
//    	if(TNA.equals("128.1.0.2")) {
//        	bws.add(new Integer(100)); // in MBit
//        	bws.add(new Integer(200)); // in MBit
//        	bws.add(new Integer(300)); // in MBit
//        	return bws;
//    	}
//    	if(TNA.equals("128.1.0.3")) {
//        	bws.add(new Integer(100)); // in MBit
//        	bws.add(new Integer(200)); // in MBit
//        	bws.add(new Integer(300)); // in MBit
//        	return bws;
//    	}
//    	if(TNA.equals("128.1.0.10")) {
//        	bws.add(new Integer(100)); // in MBit
//        	bws.add(new Integer(150)); // in MBit
//        	bws.add(new Integer(200)); // in MBit
//        	return bws;
//    	}
//    	if(TNA.equals("128.1.0.11")) {
//        	bws.add(new Integer(100)); // in MBit
//        	bws.add(new Integer(150)); // in MBit
//        	bws.add(new Integer(200)); // in MBit
//        	return bws;
//    	}
//    	if(TNA.equals("128.2.0.1")) {
//        	bws.add(new Integer(100)); // in MBit
//        	bws.add(new Integer(150)); // in MBit
//        	bws.add(new Integer(200)); // in MBit
//        	return bws;
//    	}
//    	if(TNA.equals("128.2.0.2")) {
//        	bws.add(new Integer(100)); // in MBit
//        	bws.add(new Integer(150)); // in MBit
//        	bws.add(new Integer(200)); // in MBit
//        	return bws;
//    	}
//    	if(TNA.equals("128.2.0.3")) {
//        	bws.add(new Integer(100)); // in MBit
//        	bws.add(new Integer(150)); // in MBit
//        	bws.add(new Integer(200)); // in MBit
//        	return bws;
//    	}
//    	if(TNA.equals("128.2.0.10")) {
//        	bws.add(new Integer(100)); // in MBit
//        	bws.add(new Integer(200)); // in MBit
//        	bws.add(new Integer(300)); // in MBit
//        	return bws;
//    	}
    	// ----------------------------------------------------------------- test values

    	// add bandwidths to the list beginning at maxBw, each as the half of the 
    	// bandwidth before till minBw is reached
   		while(maxBw > minBw) {
   			bws.add(maxBw);
   			maxBw = new Double(Math.floor(maxBw/2)).intValue();
   		}
   		if(!bws.contains(new Long(minBw))) bws.add(minBw);
    	// sort list ascending
   		Collections.sort(bws);

    	return bws;
    }
    
	/**
	 * find the greatest common value between the three parameters
	 * @param sourceBws
	 * @param targetBws
	 * @param usedBw
	 * @return
	 */
	public static Tuple<Integer, Integer> getFeasibleBws(List<Integer> sourceBws, 
											 List<Integer> targetBws, long usedBw) {
		// set pointer on min value
		int pointerSource = 0;
		int pointerTarget = 0;

		// find bandwidth for source endpoint greater equal than the used bandwidth 
		// of whole reservation
		boolean finished = false;
		int foundBwSource = 0;
		while( (pointerSource < sourceBws.size()) && !finished ) {
			if(sourceBws.get(pointerSource) >= usedBw) {
				foundBwSource = sourceBws.get(pointerSource);
				finished = true;
			}
			pointerSource++;
		}
		
		// find bandwidth for target endpoint greater equal than the used bandwidth
		finished = false;
		int foundBwTarget = 0;
		while( (pointerTarget < targetBws.size()) && !finished ) {
			if(targetBws.get(pointerTarget) >= usedBw) {
				foundBwTarget = targetBws.get(pointerTarget);
				finished = true;
			}
			pointerTarget++;
		}

		return new Tuple<Integer, Integer>(foundBwSource, foundBwTarget);
	}

	/**
	 * sort the bandwidths in a list according to a given strategy
	 */
	public static List<Integer> sortBwsByStrategy(List<Integer> list) {
		// TODO: implementation of other strategies possible
		Collections.sort(list);
		return list;
	}

	/**
	 * compute a new feasible startTime, between two timestamps 
	 * @param nrpsResponses
	 * @param startTime
	 * @param deadline
	 * @param actualDuration
	 * @return
	 */
	public XMLGregorianCalendar computeNewStartTime(
			Hashtable<Domain, IsAvailableResponseType> nrpsResponses,
			Date startTime, Date deadline, int actualDuration) {
		// compute the maxOffset of all isAvailableResponses
		long maxOffset = 0;
		for(IsAvailableResponseType response : nrpsResponses.values()) {
			if(response.isSetAlternativeStartTimeOffset()) {
				maxOffset = Math.max(maxOffset, response.getAlternativeStartTimeOffset());
			}
		}
		
		// if no offsets are sent back by the NRPS, choose a default offset
		if(maxOffset == 0) {
			// TODO: make this dependent from nrpsResponses?
			maxOffset = 3600; // one hour
		}
		
		// try to compute a new feasible startTime
		if(maxOffset > 0) {
			XMLGregorianCalendar start = Helpers.DateToXmlCalendar(startTime);
			// add maxOffset and duration
			XMLGregorianCalendar auxStart = Helpers.rollXMLCalendar(
											start, 0, 
											new Long(maxOffset).intValue() + actualDuration);
			// check if this startTime is feasible
			if((deadline.getTime() - auxStart.toGregorianCalendar().getTimeInMillis()) > 0) {
				return Helpers.rollXMLCalendar(start, 0, new Long(maxOffset).intValue());
			}
		}
		// no feasible new startTime or maxOffset not set correct
		return null;
	}
	
	public void mergeReservation(Reservation globalRes, HashMap<Domain, Reservation> domResMapping, Reservation nrpsReservation, Domain mappingDom) {
		Service s = nrpsReservation.getServices().values().iterator().next();
		int serviceId = s.getServiceId();
		int duration = s.getDuration();
		Date startTime = s.getStartTime();
		
		Connections conn = s.getConnections().values().iterator().next();
		int connId = conn.getConnectionId();
		int bw = conn.getMaxBandwidth();

		Reservation newRes = new Reservation();
		newRes.setReservationId(0);
		newRes.setJobId(globalRes.getJobId());
		newRes.setConsumerUrl(globalRes.getConsumerUrl());
		newRes.setTimeout(globalRes.getTimeout());
        // transfer GRI and token
        newRes.setGri(globalRes.getGri());
        newRes.setToken(globalRes.getToken());

		Service newService = new Service();
		newService.setPK_service(0);
		newService.setServiceId(serviceId);
		newService.setStartTime(startTime);
		newService.setDuration(duration);
		newService.setDeadline(null);
		newService.setAutomaticActivation(s.isAutomaticActivation());

		Connections newConn = new Connections();
		newConn.setPK_Connections(0);
		newConn.setConnectionId(connId);
		newConn.setDirectionality(conn.getDirectionality());
		newConn.setMaxBandwidth(bw);
		newConn.setMinBandwidth(bw);
		newConn.setMaxLatency(conn.getMaxLatency());
		newConn.setStartpoint(conn.getStartpoint());
		for(Endpoint e : conn.getEndpoints()) {
			newConn.addEndpoint(e);
		}

		NrpsConnections newNrpsConn = globalRes.getService(serviceId).getConnection(connId).getNrpsConnection(mappingDom).getCopy();
		newNrpsConn.setBandwidth(bw);
		
		newConn.addNrpsConnection(newNrpsConn);
		newService.addConnection(newConn);
		
		if(domResMapping.containsKey(mappingDom)) {
			if(domResMapping.get(mappingDom).getService(serviceId) == null) {
				domResMapping.get(mappingDom).addService(newService);
			} else {
				domResMapping.get(mappingDom).getService(serviceId).addConnection(newConn);
			}
		} else {
			newRes.addService(newService);
			domResMapping.put(mappingDom, newRes);
		}
	}
}
