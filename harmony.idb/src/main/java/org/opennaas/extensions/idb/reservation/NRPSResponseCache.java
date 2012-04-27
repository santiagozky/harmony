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

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ConnectionAvailabilityType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ConnectionConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.IsAvailableResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.IsAvailableType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ServiceConstraintType;
import org.opennaas.core.utils.Config;
import org.opennaas.core.utils.PhLogger;
import org.opennaas.extensions.idb.Constants;

/**
 * A cache to store responses from NRPSs for a short period of time. When the
 * entry time expires, the entry is deleted from the cache automatically. It is
 * based on the idea that consecutive requests over the same resources will have
 * the same IsAvailableType object, i.e. the same Java object will be used for
 * several consecutive requests.
 * 
 * @author Angel Sanchez (angel.sanchez@i2cat.net)
 * @author Jordi Ferrer (jordi.ferrer@i2cat.net)
 */
public class NRPSResponseCache {

	/**
	 * An entry for the cache. It contains information about availability of a
	 * path with a given constraints inside a Domain. It is in essence a
	 * ConnectionConstraintType object.
	 */
	class CacheEntry {
		// Time of creation of the entry
		Date timestamp = null;

		// Connection (path) info
		ConnectionConstraintType connection;

		// Availavility of the connection
		ConnectionAvailabilityType availability;

		// Alternative start time
		Long alternativeStartTime;

		// Start time of the resv
		XMLGregorianCalendar startTime;

		public CacheEntry(final ConnectionConstraintType con,
				final ConnectionAvailabilityType av, Long altStartTime,
				XMLGregorianCalendar st) {
			this.connection = con;
			this.availability = av;
			this.timestamp = new Date();
			this.alternativeStartTime = altStartTime;
			this.startTime = st;
		}

		public Long getAlternativeStartTime() {
			return this.alternativeStartTime;
		}

		public ConnectionAvailabilityType getAvailability() {
			return this.availability;
		}

		public ConnectionConstraintType getConnection() {
			return this.connection;
		}

		public String getSourceEP() {
			return this.connection.getSource().getEndpointId();
		}

		public String getTargetEP() {
			return this.connection.getTarget().get(0).getEndpointId();
		}

		public Date getTimestamp() {
			return this.timestamp;
		}

		public XMLGregorianCalendar getStartTime() {
			return this.startTime;
		}

	}

	private static NRPSResponseCache selfinstance;
	// static Vector < Timer > timers;

	/**
	 * Maps a Domain with a Vector containing the information for that domain
	 */
	// static HashMap < String, Vector < NRPSResponseCache.CacheEntry > > cache;
	static Hashtable<String, Hashtable<String, NRPSResponseCache.CacheEntry>> cache;

	static Logger logger;

	/** Static instance getter * */
	public static NRPSResponseCache getInstance() {
		if (NRPSResponseCache.selfinstance == null) {
			NRPSResponseCache.selfinstance = new NRPSResponseCache();

			NRPSResponseCache.logger.info("Created NRPSResponseCache");
		}

		return NRPSResponseCache.selfinstance;
	}

	public NRPSResponseCache() {
		// timers = new Vector < Timer > () ;
		// cache = new HashMap < String, Vector < NRPSResponseCache.CacheEntry >
		// > ();

		NRPSResponseCache.cache = new Hashtable<String, Hashtable<String, NRPSResponseCache.CacheEntry>>();

		NRPSResponseCache.logger = PhLogger.getLogger(this.getClass());
	}

	/**
	 * Add connection entries to the cache. This function inserts in the cache
	 * all the connections of all the services provided in the IsAvailableType
	 * object with their corresponding responses. It programs the deletion of
	 * the connections after a given interval as well.
	 * 
	 * @param req
	 *            Request for connections
	 * @param resp
	 *            Responses for the given request
	 */
	private void addEntries(final IsAvailableType req,
			final IsAvailableResponseType resp) {
		final String dom = req.getService().get(0).getConnections().get(0)
				.getSource().getDomainId();

		final List<ServiceConstraintType> services = req.getService();
		final List<ConnectionConstraintType> conns = new ArrayList<ConnectionConstraintType>();
		final List<ConnectionAvailabilityType> avails = resp
				.getDetailedResult();

		final Long alternativeTime = resp.getAlternativeStartTimeOffset();

		for (int i = 0; i < services.size(); i++) {

			final ServiceConstraintType auxService = services.get(i);
			conns.addAll(auxService.getConnections());

			XMLGregorianCalendar sTime = null;

			if (auxService.getTypeOfReservation().equals(
					ReservationType.DEFERRABLE)) {
				sTime = auxService.getDeferrableReservationConstraints()
						.getStartTime();
			} else if (auxService.getTypeOfReservation().equals(
					ReservationType.MALLEABLE)) {
				sTime = auxService.getMalleableReservationConstraints()
						.getStartTime();
			} else if (auxService.getTypeOfReservation().equals(
					ReservationType.FIXED)) {
				sTime = auxService.getFixedReservationConstraints()
						.getStartTime();
			}

			for (int z = 0; z < conns.size(); z++) {

				final ConnectionConstraintType auxConnection = conns.get(z);
				ConnectionAvailabilityType auxAvailable = avails.get(0);

				boolean found = false;

				for (int j = 0; j < avails.size() && !found; j++) {

					if (auxConnection.getConnectionID() == auxAvailable
							.getConnectionID()) {
						found = true;
					}

					auxAvailable = avails.get(j);

				}

				if (!NRPSResponseCache.cache.containsKey(dom)) {

					// Vector < NRPSResponseCache.CacheEntry > v =
					// new Vector < NRPSResponseCache.CacheEntry >();

					// v.add(new CacheEntry(auxcon, auxavail));

					final Hashtable<String, NRPSResponseCache.CacheEntry> hm = new Hashtable<String, NRPSResponseCache.CacheEntry>();

					hm.put("" + auxConnection.getSource()
							+ auxConnection.getTarget().get(0),
							new CacheEntry(auxConnection, auxAvailable,
									alternativeTime, sTime));

					NRPSResponseCache.cache.put(dom, hm);
				} else {
					NRPSResponseCache.cache.get(dom).put(
							"" + auxConnection.getSource()
									+ auxConnection.getTarget().get(0),
							new CacheEntry(auxConnection, auxAvailable,
									alternativeTime, sTime));
				}
			}

		}

		// TODO
		// Fix the cache
		// TODO Jordi Ferrer Riera (Fundació i2CAT)
		/*
		 * for (int i = 0; i < services.size(); i++) { final
		 * ServiceConstraintType auxsrv = services.get(i);
		 * conns.addAll(auxsrv.getConnections());
		 * 
		 * }
		 * 
		 * for (int j = 0; j < conns.size(); j++) { final
		 * ConnectionConstraintType auxcon = conns.get(j);
		 * ConnectionAvailabilityType auxavail = avails.get(0);
		 * 
		 * boolean found = false;
		 * 
		 * for (int i = 0; i < avails.size() && !found; i++) {
		 * 
		 * if (auxcon.getConnectionID() == auxavail.getConnectionID()) { found =
		 * true; } auxavail = avails.get(i); } // int k = 1;
		 * 
		 * // while (auxcon.getConnectionID() != auxavail.getConnectionID()) {
		 * // auxavail = avails.get(k); // k++; // }
		 * 
		 * if (!NRPSResponseCache.cache.containsKey(dom)) {
		 * 
		 * // Vector < NRPSResponseCache.CacheEntry > v = // new Vector <
		 * NRPSResponseCache.CacheEntry >();
		 * 
		 * // v.add(new CacheEntry(auxcon, auxavail));
		 * 
		 * final Hashtable<String, NRPSResponseCache.CacheEntry> hm = new
		 * Hashtable<String, NRPSResponseCache.CacheEntry>();
		 * 
		 * hm.put("" + auxcon.getSource() + auxcon.getTarget().get(0), new
		 * CacheEntry(auxcon, auxavail,alternativeTime));
		 * 
		 * NRPSResponseCache.cache.put(dom, hm); } else {
		 * NRPSResponseCache.cache.get(dom).put( "" + auxcon.getSource() +
		 * auxcon.getTarget().get(0), new CacheEntry(auxcon, auxavail,
		 * alternativeTime)); }
		 */
		// }
	}

	/**
	 * Adds a new entry to the cache (<request, response>). It activates a Timer
	 * to delete the entry after 30? sec.
	 * 
	 * @param req
	 *            Request to NRPS
	 * @param resp
	 *            Response from NRPS
	 */
	public void insert(final IsAvailableType req,
			final IsAvailableResponseType resp) {
		final long cacheTimeout = Long.parseLong(Config.getString(
				Constants.idbProperties, "cacheEntryTimeout"));

		final Timer t = new Timer();
		// timers.add(t);

		final TimerTask tt = new TimerTask() {
			@Override
			public void run() {
				// timers.remove(t);
				NRPSResponseCache.this.removeEntries(req);
				this.cancel();
				t.cancel();

				NRPSResponseCache.logger
						.info("Deleted entry(es) from NRPSResponseCache (time expired)");
			}
		};

		t.schedule(tt, cacheTimeout);

		if ((req != null) && (resp != null)) {
			this.addEntries(req, resp);
		}

		NRPSResponseCache.logger
				.info("Added new entry(es) in the NRPSResponseCache");
	}

	/**
	 * Gets the cached response for the given request.
	 * 
	 * @param req
	 *            Request for the NRPS
	 * @return Cached response for the given request or null if it is not cached
	 */
	public IsAvailableResponseType lookup(final IsAvailableType req) {
		NRPSResponseCache.logger.info("Lookup in the NRPSResponseCache...");

		final String dom = req.getService().get(0).getConnections().get(0)
				.getSource().getDomainId();

		final List<ServiceConstraintType> services = req.getService();

		final IsAvailableResponseType resp = new IsAvailableResponseType();

		XMLGregorianCalendar sTime = null;

		// Get requested connections
		for (int i = 0; i < services.size(); i++) {
			final ServiceConstraintType auxsrv = services.get(i);

			for (int j = 0; j < auxsrv.getConnections().size(); j++) {
				final ConnectionConstraintType conn = auxsrv.getConnections()
						.get(j);

				if (null != NRPSResponseCache.cache.get(dom)) {

					if (null != NRPSResponseCache.cache.get(dom).get(
							"" + conn.getSource() + conn.getTarget().get(0))) {

						// Patched for malleable reservations
						// Check also start time if the reservation type is
						// malleable
						if (auxsrv.getTypeOfReservation().equals(
								ReservationType.MALLEABLE)) {

							sTime = auxsrv.getMalleableReservationConstraints()
									.getStartTime();

							if (NRPSResponseCache.cache.get(dom).get(
									"" + conn.getSource()
											+ conn.getTarget().get(0))
									.getStartTime().equals(sTime)) {

								final CacheEntry cachedInfo = NRPSResponseCache.cache
										.get(dom).get(
												""
														+ conn.getSource()
														+ conn.getTarget().get(
																0));

								final ConnectionAvailabilityType connAvail = cachedInfo
										.getAvailability();
								connAvail.setConnectionID(conn
										.getConnectionID());
								connAvail.setServiceID(auxsrv.getServiceID());

								// Set the alternative start time (offset)
								Long alternativeStartTime = NRPSResponseCache.cache
										.get(dom).get(
												""
														+ conn.getSource()
														+ conn.getTarget().get(
																0))
										.getAlternativeStartTime();

								resp
										.setAlternativeStartTimeOffset(alternativeStartTime);

								// Add the connAvail
								resp.getDetailedResult().add(connAvail);
							} else {
								NRPSResponseCache.logger
										.info("NRPSResponseCache lookup failed... Going down to NRPS");
								return null;
							}
							// Fixed or deferrable reservations
						} else if (auxsrv.getTypeOfReservation().equals(
								ReservationType.FIXED)
								|| auxsrv.getTypeOfReservation().equals(
										ReservationType.DEFERRABLE)) {

							final CacheEntry cachedInfo = NRPSResponseCache.cache
									.get(dom).get(
											"" + conn.getSource()
													+ conn.getTarget().get(0));

							final ConnectionAvailabilityType connAvail = cachedInfo
									.getAvailability();
							connAvail.setConnectionID(conn.getConnectionID());
							connAvail.setServiceID(auxsrv.getServiceID());

							// Set the alternative start time
							Long alternativeStartTime = NRPSResponseCache.cache
									.get(dom).get(
											"" + conn.getSource()
													+ conn.getTarget().get(0))
									.getAlternativeStartTime();
							resp
									.setAlternativeStartTimeOffset(alternativeStartTime);

							// Add the connAvail
							resp.getDetailedResult().add(connAvail);

						}
					} else {

						NRPSResponseCache.logger
								.info("NRPSResponseCache lookup failed... Going for request to NRPSs");
						// If at least one of the paths is not available,
						// return null to make the requests to the NRPSs
						return null;
					}

				} else {

					// If there's no entry for the domain in the caché, return
					// null
					// tom make the requests to the NRPSs
					NRPSResponseCache.logger
							.info("NRPSResponseCache lookup failed... Going for request to NRPSs");
					return null;
				}
			}
		}

		NRPSResponseCache.logger
				.info("Found all the information in the NRPSResponseCache! Returning results...");

		return resp;
	}

	/**
	 * Removes automatically the connection entries whose timeout has expired.
	 * 
	 * @param req
	 *            Object with the connections to remove from the cache.
	 */
	void removeEntries(final IsAvailableType req) {
		final String dom = req.getService().get(0).getConnections().get(0)
				.getSource().getDomainId();

		final List<ServiceConstraintType> services = req.getService();

		// Remove connections
		for (int i = 0; i < services.size(); i++) {
			final ServiceConstraintType auxsrv = services.get(i);

			String connID = null;

			for (int j = 0; j < auxsrv.getConnections().size(); j++) {

				final ConnectionConstraintType conn = auxsrv.getConnections()
						.get(j);

				connID = "" + conn.getSource() + conn.getTarget().get(0);

				NRPSResponseCache.cache.get(dom).remove(connID);
			}
		}

		// Delete hashtable entry if there are not more connections for the
		// domain
		if (NRPSResponseCache.cache.get(dom).isEmpty()) {
			NRPSResponseCache.cache.remove(dom);
		}
	}

}