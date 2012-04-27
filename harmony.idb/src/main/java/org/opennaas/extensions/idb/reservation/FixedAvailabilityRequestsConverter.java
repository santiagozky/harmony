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


package org.opennaas.extensions.idb.reservation;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;

import javax.xml.datatype.XMLGregorianCalendar;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ConnectionConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.FixedReservationConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.IsAvailableType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ServiceConstraintType;
import org.opennaas.core.utils.Helpers;
import org.opennaas.core.utils.Tuple;
import org.opennaas.extensions.idb.database.hibernate.Connections;
import org.opennaas.extensions.idb.database.hibernate.Domain;
import org.opennaas.extensions.idb.database.hibernate.Endpoint;
import org.opennaas.extensions.idb.database.hibernate.Reservation;
import org.opennaas.extensions.idb.database.hibernate.Service;
import org.opennaas.extensions.idb.exception.database.DatabaseException;
import org.opennaas.extensions.idb.reservation.handler.MalleableReservationHelpers;

public class FixedAvailabilityRequestsConverter {
	
	private PathHandler pathHandler;

	private int dataAmount;
	private int globalMinBw;
	private int globalMaxBw;
	private XMLGregorianCalendar globalStarttime;
	private Date globalDeadline;
	private Integer actualDuration;
	private Date actualStarttime;

	// table that contains availability-requests for each domain
	protected Hashtable<Domain, IsAvailableType> requests;

    // table that contains reservations for each domain
	private HashMap<Domain, Reservation> nrpsReservationTable;

    public FixedAvailabilityRequestsConverter(PathHandler ph) {
		super();

		this.pathHandler = ph;

		this.globalMinBw = 0;
		this.globalMaxBw = Integer.MAX_VALUE;
		this.globalStarttime = Helpers.generateXMLCalendar();
		this.globalDeadline = new Date();
		this.actualDuration = Integer.MAX_VALUE;
		this.actualStarttime = new Date();
		
		this.nrpsReservationTable = new HashMap<Domain, Reservation>();
		this.requests = new Hashtable<Domain, IsAvailableType>(0);
	}

	public void shiftRequests(XMLGregorianCalendar newStartTime) {
		// adapt Availability-Requests
		for(IsAvailableType request : this.requests.values()) {
			// there is only one ServiceConsctraintType here
			ServiceConstraintType sct = request.getService().get(0);
			sct.getFixedReservationConstraints().setStartTime(newStartTime);
		}
		// adapt Reservation-Requests
		for(Reservation res : this.nrpsReservationTable.values()) {
			// there is only one Service here
			Service service = res.getServices().values().iterator().next();
			service.setStartTime(Helpers.xmlCalendarToDate(newStartTime));
		}
		this.actualStarttime = Helpers.xmlCalendarToDate(newStartTime);
	}
	
	public void setDuration(int newDuration) {
		for(IsAvailableType request : this.requests.values()) {
			// there is only one ServiceConsctraintType here
			ServiceConstraintType sct = request.getService().get(0);
			sct.getFixedReservationConstraints().setDuration(newDuration);
		}
		// adapt Reservation-Requests
		for(Reservation res : this.nrpsReservationTable.values()) {
			// there is only one Service here
			Service service = res.getServices().values().iterator().next();
			service.setDuration(newDuration);
		}
		this.actualDuration = newDuration;
	}
	
	public XMLGregorianCalendar getGlobalStarttime() {
		return this.globalStarttime;
	}

	public Date getGlobalDeadline() {
		return this.globalDeadline;
	}

	public int getActualDuration() {
		return this.actualDuration;
	}

	public Date getActualStartTime() {
		return this.actualStarttime;
	}
	
	public HashMap<Domain, Reservation> getActualReservations() {
		return this.nrpsReservationTable;
	}

	public void addRequest(Domain dom, IsAvailableType request) {
		this.requests.put(dom, request);
	}
	
	public Hashtable<Domain, IsAvailableType> getRequests() {
		return this.requests;
	}

	public void createRequestsFromMalleable(HashMap<Domain, Reservation> resMapping, int serviceId, int connId) {
		// create new Availability-Request
		for(Entry<Domain, Reservation> mapping : resMapping.entrySet()) {
			Domain domain = mapping.getKey();
			Reservation res = mapping.getValue();

			Service service = res.getService(serviceId);
			Connections conn = service.getConnection(connId);
			// new availability request
			IsAvailableType newRequest = new IsAvailableType();

			ConnectionConstraintType cct = new ConnectionConstraintType();
			cct.setConnectionID(conn.getConnectionId());
			this.dataAmount = conn.getDataAmount();
			cct.setDirectionality(conn.getDirectionality());
			cct.setMinBW(conn.getMinBandwidth());
			this.globalMinBw = cct.getMinBW();
			cct.setMaxBW(conn.getMaxBandwidth());
			// maxBandWidth is optional, if not set, use minBw
			if(cct.getMaxBW() == 0) this.globalMaxBw = cct.getMinBW();
			else this.globalMaxBw = cct.getMaxBW(); 
			cct.setMaxDelay(conn.getMaxLatency());

			Endpoint startPoint = conn.getStartpoint();
			cct.setSource(startPoint.toJaxb());
			for(Endpoint ep : conn.getEndpoints()) {
				cct.getTarget().add(ep.toJaxb());
			}
					
			FixedReservationConstraintType frct = new FixedReservationConstraintType();
			// as initial value the user-given startTime is used
			frct.setStartTime(Helpers.DateToXmlCalendar(service.getStartTime()));
			this.globalStarttime = frct.getStartTime();
			this.actualStarttime = Helpers.xmlCalendarToDate(this.globalStarttime);
			// save deadline of malleable reservation for later checks
			this.globalDeadline = service.getDeadline();
			// compute initial duration later -> here set MaxValue
			frct.setDuration(Integer.MAX_VALUE);

			ServiceConstraintType sct = new ServiceConstraintType();
			sct.setServiceID(service.getServiceId());
			sct.setAutomaticActivation(service.isAutomaticActivation());
			sct.setTypeOfReservation(ReservationType.FIXED);
			sct.setFixedReservationConstraints(frct);
			sct.getConnections().add(cct);

			newRequest.getService().add(sct);
			newRequest.setJobID(res.getJobId());

			this.requests.put(domain, newRequest);
		}

		// all reservations for all domains have been converted, now
		// the initial duration can be computed
		this.computeInitialBWandDuration();
		
		// save the reservation, service and connection
		// adapted to an fixed reservation-request for later using
		this.convertReservationRequests(resMapping, serviceId, connId);
	}
	
	private void computeInitialBWandDuration() {
		// list of bandwidths for the single endpoints, here as initial values
		// the maxima. 
		HashSet<Integer> maxBWs = new HashSet<Integer>();

		Hashtable<String, List<Integer>> endpointBws = this.pathHandler.getBwOfEndpoints();
		for(Entry<String, List<Integer>> bandwidths : endpointBws.entrySet()) {
			maxBWs.add(Collections.max(bandwidths.getValue()));
		}

		// take the minimum of all maxima as initial value for the requests
		Integer usedGlobalMinBw = Collections.min(maxBWs);
		
		for(IsAvailableType request : this.requests.values()) {
			// there is only one ServiceConstraintType and one ConnectionConstraintType
			ServiceConstraintType sct = request.getService().get(0);
			ConnectionConstraintType cct = sct.getConnections().get(0);

			// BW computing -------------------------------------------------
			Tuple<Integer, Integer> feasibleBWs = MalleableReservationHelpers
													.getFeasibleBws(
													endpointBws.get(cct.getSource().getEndpointId()),
													endpointBws.get(cct.getTarget().get(0).getEndpointId()),
													usedGlobalMinBw);

			// use the minimum of both feasible BWs as initial value, this is now
			// a fixed reservation, so both min- and max-BW have to be equal
			int usedMinBw = new Long(Math.min(feasibleBWs.getFirstElement(), 
						  					  feasibleBWs.getSecondElement())).intValue();

			cct.setMinBW(usedMinBw);
			cct.setMaxBW(usedMinBw);
					
			// save actual bandwidths of involved endpoints
			this.pathHandler.setActualBwOfEndpoint(cct.getSource().getEndpointId(), feasibleBWs.getFirstElement());
			this.pathHandler.setActualBwOfEndpoint(cct.getTarget().get(0).getEndpointId(), feasibleBWs.getSecondElement());

			// Duration computing -------------------------------------------
			long dataAmount = this.dataAmount * 8; // dataAmount in Mbit

			// duration = dataAmount / globalMinBw
			int resultingDuration = new Double(Math.ceil(dataAmount/usedGlobalMinBw)).intValue();
			sct.getFixedReservationConstraints().setDuration(resultingDuration);
			this.actualDuration = resultingDuration;
		}
	}
	
	public boolean validateRequests() {
		// validate if requests are complient to malleable restricitions
		for(IsAvailableType request : this.requests.values()) {
			// there is only one ServiceConstraintType and one ConnectionConstraintType
			ServiceConstraintType sct = request.getService().get(0);

			long durationInMillis = sct.getFixedReservationConstraints().getDuration() * 1000l;
			long startTimeInMillis = sct.getFixedReservationConstraints().getStartTime()
													.toGregorianCalendar().getTimeInMillis();
			long deadlineInMillis = this.globalDeadline.getTime();

			if(startTimeInMillis + durationInMillis > deadlineInMillis) {
				// request is not valid, the duration is too long -> return false
				return false;
			}
		}
		
		// all request are valid -> return true
		return true;
	}
	
	public void adaptToNewBws(Hashtable<String, Integer> newBws) {
		this.pathHandler.setActualBwOfEndpoints(newBws);
		
		// the single endpoints may use different bandwidths, but the duration for
		// the fixed reservation is dependent from the smallest bandwidth
		// -> get smallest bandwidth from list for later duration calculation
		long smallestBW = Collections.min(newBws.values());

		long dataAmount = this.dataAmount * 8; // in Mbit
		int newDuration = new Double(Math.ceil(dataAmount/smallestBW)).intValue();

		this.setDuration(newDuration);
	}

	/**
	 * convert a malleable reservation to a fixed CreateReservationType under 
	 * given constraints
	 * @throws DatabaseException 
	 */
	private void convertReservationRequests(
										HashMap<Domain, Reservation> malleableReservations,
										int serviceId, int connId) {

		for(Domain domain : malleableReservations.keySet()) {
			Reservation malleableRes = malleableReservations.get(domain);
			Service malleableService = malleableRes.getService(serviceId);
			Connections malleableConn = malleableService.getConnection(connId);
			
			// new fixed reservation
			Reservation fixedRes = new Reservation();
			fixedRes.setReservationId(0);
			fixedRes.setJobId(malleableRes.getJobId());
			fixedRes.setConsumerUrl(malleableRes.getConsumerUrl());
			fixedRes.setTimeout(malleableRes.getTimeout());

			// new fixed service
			Service fixedService = new Service();
			fixedService.setPK_service(0);
			fixedService.setServiceId(malleableService.getServiceId());
			fixedService.setAutomaticActivation(malleableService.isAutomaticActivation());
			fixedService.setStartTime(this.actualStarttime);
			fixedService.setDuration(this.actualDuration);

			// new fixed connection
			Connections fixedConn = new Connections();
			fixedConn.setPK_Connections(0);
			fixedConn.setConnectionId(malleableConn.getConnectionId());
			fixedConn.setDirectionality(malleableConn.getDirectionality());
			fixedConn.setMaxLatency(malleableConn.getMaxLatency());
			fixedConn.setStartpoint(malleableConn.getStartpoint());
			// get according bandwidths from given list
			HashSet<Integer> usedBws = new HashSet<Integer>();
			usedBws.add(this.pathHandler.getActualBwOfEndpoint(fixedConn.getStartpoint().getTNA()));
			for(Endpoint e : malleableConn.getEndpoints()) {
				fixedConn.getEndpoints().add(e);
				usedBws.add(this.pathHandler.getActualBwOfEndpoint(e.getTNA()));
			}
			fixedConn.setMaxBandwidth(Collections.min(usedBws).intValue());
			fixedConn.setMinBandwidth(Collections.min(usedBws).intValue());

			fixedService.addConnection(fixedConn);
			fixedRes.addService(fixedService);
			
			this.nrpsReservationTable.put(domain, fixedRes);
		}
	}
}
