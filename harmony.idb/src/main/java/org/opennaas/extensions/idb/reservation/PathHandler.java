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

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.opennaas.core.utils.Tuple;
import org.opennaas.extensions.idb.database.hibernate.Endpoint;
import org.opennaas.extensions.idb.exception.database.DatabaseException;
import org.opennaas.extensions.idb.reservation.handler.MalleableReservationHelpers;

public class PathHandler {

	private List<Tuple<Endpoint, Endpoint>> path;
	private Hashtable<String, List<Integer>> bandwidthsOfEndpoints;
	private Hashtable<String, Integer> actualUsedBandwidthOfEndpoint;

	private int globalMinBw;
	private int globalMaxBw;

	public PathHandler() {
		this.path = new LinkedList<Tuple<Endpoint, Endpoint>>();
		this.bandwidthsOfEndpoints = new Hashtable<String, List<Integer>>(0);
		this.actualUsedBandwidthOfEndpoint = new Hashtable<String, Integer>(0);
		this.globalMinBw = 0;
		this.globalMaxBw = Integer.MAX_VALUE;
	}

	public PathHandler(List<Tuple<Endpoint, Endpoint>> pathParam, int minBw,
			int maxBw) {
		this.path = pathParam;
		this.bandwidthsOfEndpoints = new Hashtable<String, List<Integer>>(0);
		this.actualUsedBandwidthOfEndpoint = new Hashtable<String, Integer>(0);
		this.globalMinBw = minBw;
		this.globalMaxBw = maxBw;

		this.initEndpointWithBandwidths();
	}

	private void initEndpointWithBandwidths() {
		for (Tuple<Endpoint, Endpoint> pathPart : this.path) {
			Endpoint end = pathPart.getFirstElement();
			this.bandwidthsOfEndpoints.put(end.getTNA(),
					MalleableReservationHelpers.getBwsForEndpoint(end.getTNA(),
							this.globalMinBw, this.globalMaxBw));
			end = pathPart.getSecondElement();
			this.bandwidthsOfEndpoints.put(end.getTNA(),
					MalleableReservationHelpers.getBwsForEndpoint(end.getTNA(),
							this.globalMinBw, this.globalMaxBw));
		}
	}

	public List<Tuple<Endpoint, Endpoint>> getPath() {
		return this.path;
	}

	public Hashtable<String, List<Integer>> getBwOfEndpoints() {
		return this.bandwidthsOfEndpoints;
	}

	public List<Integer> getBwOfEndpoint(String tna) {
		return this.bandwidthsOfEndpoints.get(tna);
	}

	public Integer getActualBwOfEndpoint(String tna) {
		return this.actualUsedBandwidthOfEndpoint.get(tna);
	}

	public void setActualBwOfEndpoints(Hashtable<String, Integer> newBws) {
		this.actualUsedBandwidthOfEndpoint.putAll(newBws);
	}

	public void setActualBwOfEndpoint(String tna, Integer newBw) {
		this.actualUsedBandwidthOfEndpoint.put(tna, newBw);
	}

	public int getMinBw() {
		return this.globalMinBw;
	}

	public int getMaxBw() {
		return this.globalMaxBw;
	}

	/**
	 * check if adapting of bandwidth is possible for the given requests
	 * 
	 * @return
	 * @throws DatabaseException
	 * @throws HibernateException
	 */
	public Tuple<Boolean, Hashtable<String, Integer>> isBwAdaptingPossible(
			List<String> blockedEndpoints) {

		// bandwidths for every endpoint from DB
		Hashtable<String, List<Integer>> bws = new Hashtable<String, List<Integer>>();
		// pointer on actual bandwidth in the above list
		Hashtable<String, Integer> pointer = new Hashtable<String, Integer>();

		// for every involved endpoint, the resulting new bandwidth
		Hashtable<String, Integer> responseBWs = new Hashtable<String, Integer>();

		// get bandwidths for all actual used endpoints
		for (Entry<String, List<Integer>> entry : this.bandwidthsOfEndpoints
				.entrySet()) {
			// sort the bandwidths
			List<Integer> l = MalleableReservationHelpers
					.sortBwsByStrategy(entry.getValue());
			// add list
			bws.put(entry.getKey(), l);
			// get pointer and save it
			pointer.put(entry.getKey(), l
					.indexOf(this.actualUsedBandwidthOfEndpoint.get(entry
							.getKey())));
		}

		long newMinBw = Long.MAX_VALUE;
		for (String tna : blockedEndpoints) {
			// decrement all pointers of blocked endpoints, because
			// we are searching for lower bandwidths, than the actual one
			int newPointer = pointer.get(tna) - 1;
			pointer.put(tna, newPointer);
			// if a pointer is out of range -> no feasible bandwidth
			if (newPointer == -1) {
				return new Tuple<Boolean, Hashtable<String, Integer>>(false,
						null);
			}
			int newBw = bws.get(tna).get(newPointer);
			// check for the lowest new bandwidth
			newMinBw = Math.min(newMinBw, newBw);
			// put new feasible bandwidth to response
			responseBWs.put(tna, newBw);
		}

		// check all endpoints if they can be adapted to new feasible bandwidths
		for (String tna : this.bandwidthsOfEndpoints.keySet()) {
			int auxPointer = pointer.get(tna) - 1;
			// search the smallest value greater than the new minimal bandwidth
			while ((auxPointer >= 0)
					&& (bws.get(tna).get(auxPointer) >= newMinBw)) {
				auxPointer--;
			}
			// take the value of auxPointer + 1
			pointer.put(tna, auxPointer + 1);

			// put bandwidth in the response
			responseBWs.put(tna, bws.get(tna).get(pointer.get(tna)));
		}

		return new Tuple<Boolean, Hashtable<String, Integer>>(true, responseBWs);
	}

	public List<String> getMinBwEndpoints() {
		int minBw = Integer.MAX_VALUE;
		LinkedList<String> minBwEnds = new LinkedList<String>();

		for (Tuple<Endpoint, Endpoint> pathPart : this.path) {
			// source endpoint check
			String endId = pathPart.getFirstElement().getTNA();
			int endBw = this.actualUsedBandwidthOfEndpoint.get(endId);
			if (endBw == minBw)
				minBwEnds.add(endId);
			if (endBw < minBw) {
				minBwEnds.clear();
				minBwEnds.add(endId);
			}

			// target endpoint check
			endId = pathPart.getSecondElement().getTNA();
			endBw = this.actualUsedBandwidthOfEndpoint.get(endId);
			if (endBw == minBw)
				minBwEnds.add(endId);
			if (endBw < minBw) {
				minBwEnds.clear();
				minBwEnds.add(endId);
			}
		}

		return minBwEnds;
	}

	public List<Tuple<Endpoint, Endpoint>> getPrunableEdges(
			List<String> blockedEndpoints, List<String> blockedDomains) {
		LinkedList<Tuple<Endpoint, Endpoint>> result = new LinkedList<Tuple<Endpoint, Endpoint>>();

		// try to find path-parts which are prunable
		if (this.path != null) {
			int pointer = 0;
			while (pointer < this.path.size()) {
				Tuple<Endpoint, Endpoint> firstPart = this.path.get(pointer);
				// check blocked domains and blocked endpoints
				if (blockedDomains.contains(firstPart.getFirstElement()
						.getDomain().getName())
						|| blockedEndpoints.contains(firstPart
								.getFirstElement().getTNA())
						|| blockedEndpoints.contains(firstPart
								.getSecondElement().getTNA())) {
					result.add(firstPart);
				}

				// check for interdomain path-part
				if (pointer + 1 < path.size()) {
					Tuple<Endpoint, Endpoint> secondPart = path
							.get(pointer + 1);
					if (blockedEndpoints.contains(firstPart.getSecondElement()
							.getTNA())
							|| blockedEndpoints.contains(secondPart
									.getFirstElement().getTNA())) {
						result.add(new Tuple<Endpoint, Endpoint>(firstPart
								.getSecondElement(), secondPart
								.getFirstElement()));
					}
				}
				pointer++;
			}
		}

		return result;

	}

}
