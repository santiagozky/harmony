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

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;
import org.apache.muse.ws.addressing.soap.SoapFault;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AvailabilityCodeType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ConnectionAvailabilityType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ConnectionConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DomainRelationshipType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.IsAvailableResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.IsAvailableType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ServiceConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.StatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.EndpointNotFoundFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.PathNotFoundFaultException;
import org.opennaas.core.utils.PerformanceLogLevel;
import org.opennaas.core.utils.PhLogger;
import org.opennaas.core.utils.Tuple;
import org.opennaas.extensions.idb.database.hibernate.Connections;
import org.opennaas.extensions.idb.database.hibernate.Domain;
import org.opennaas.extensions.idb.database.hibernate.Endpoint;
import org.opennaas.extensions.idb.database.hibernate.NrpsConnections;
import org.opennaas.extensions.idb.database.hibernate.Reservation;
import org.opennaas.extensions.idb.database.hibernate.Service;
import org.opennaas.extensions.idb.exception.database.DatabaseException;
import org.opennaas.extensions.idb.reservation.exceptions.InvalidConnectionIdException;
import org.opennaas.extensions.idb.reservation.exceptions.InvalidServiceIdException;
import org.opennaas.extensions.idb.reservation.handler.MalleableReservationHelpers;
import org.opennaas.extensions.idb.reservation.pathcomputer.IPathComputer;
import org.opennaas.extensions.idb.reservation.pathcomputer.MockPathComputerScen1;
import org.opennaas.extensions.idb.reservation.pathcomputer.PathComputer;

/**
 * This class manages a path computer to instance to find an available path,
 * either for an availability request or for a reservation request.
 */

public class PathFinderNG {
	/** Default path computer class used. */
	private static Class<? extends IPathComputer> defaultPathComputerClass = PathComputer.class;

	/**
	 * Path computer class used for the TestScenario1 JUnit test. To be precise,
	 * this path computer is used if the domain ID of the source endpoint in a
	 * request starts with "scen1".
	 */
	private static Class<? extends IPathComputer> scenario1PathComputerClass = MockPathComputerScen1.class;

	/** Do we have at least one interdomain connection? */
	// protected boolean interdomain = false;

	/** All domains hosting at least one of the involved endpoints. */
	protected Set<Domain> endDomains = new HashSet<Domain>();

	/** Indicates whether this request has already been processed. */
	protected boolean processed = false;

	/** Only search for interdomain paths along subdomains. */
	protected final boolean subdomainRestriction;

	/** Input service list. */
	protected final Reservation reservation;

	/** Stores availability of requested connections. */
	protected List<ConnectionAvailabilityType> detailedAvailability;

	/**
	 * In case the requested services are unavailable, store the fault to be
	 * thrown for the createReservation operation.
	 */
	protected PathNotFoundFaultException pathNotFound = null;

	/** Output service list. */
	protected HashMap<Domain, Reservation> outputReservations = null;

	/** Alternative start time offset. */
	protected Long alternativeStartTimeOffset = null;

	/** Path computer instance. */
	protected IPathComputer pathComputer = null;

	/** NRPS Manager. */
	private IManager nrpsManager = null;

	/** Logger. */
	private final Logger log = PhLogger.getLogger(PathFinderNG.class);

	/** Performance-Logger. */
	private final Logger performanceLogger = PhLogger.getLogger("Performance");

	private String logMsg;

	private boolean malleable;

	/**
	 * Constructor.
	 * 
	 * @param services
	 *            Requested services.
	 * @param manager
	 *            NRPS manager used to handle requests to NRPSs.
	 * @throws EndpointNotFoundFaultException
	 *             Thrown, if one or more of the endpoints specified in the
	 *             services cannot be located.
	 * @throws DatabaseException
	 */
	public PathFinderNG(final Reservation reservation, final IManager manager,
			boolean subdomainRestriction, boolean malleableParam)
			throws EndpointNotFoundFaultException, DatabaseException {
		this.reservation = reservation;
		this.nrpsManager = manager;
		this.subdomainRestriction = subdomainRestriction;
		this.malleable = malleableParam;
		this.initPathComputer();
	}

	/**
	 * Returns whether there is at least one interdomain connection.
	 * 
	 * @return True, if there is at least one interdomain connection, false
	 *         otherwise.
	 */
	/*
	 * public boolean isInterdomain() { return this.interdomain; }
	 */

	/**
	 * Returns whether the involved endpoints belong to more than one domain.
	 * 
	 * @return True, if the involved endpoints belong to more than one domain,
	 *         false otherwise.
	 */
	public boolean isMultidomain() {
		return this.endDomains.size() > 1;
	}

	public void calculateNrpsConnections() throws PathNotFoundFaultException {
		for (final Service service : this.reservation.getServices().values()) {
			try {
				computePaths(service.getServiceId());
				for (final Connections connection : service.getConnections()
						.values()) {
					// get path
					connection.getNrpsConnections().clear();
					final List<Tuple<Endpoint, Endpoint>> path = getActualPath(
							service.getServiceId(),
							connection.getConnectionId());

					this.logMsg = "PathComputer returned path (serviceID="
							+ service.getServiceId() + " connectionID="
							+ connection.getConnectionId() + "): ";
					for (final Tuple<Endpoint, Endpoint> hop : path) {
						Endpoint ep1;
						Endpoint ep2;
						try {
							ep1 = Endpoint.loadOrCreateUserEndpoint(hop
									.getFirstElement().getTNA());
							ep2 = Endpoint.loadOrCreateUserEndpoint(hop
									.getSecondElement().getTNA());

						} catch (final EndpointNotFoundFaultException e) {
							e.printStackTrace();
							throw new PathNotFoundFaultException(e);
						}
						this.logMsg += " [" + ep1.getTNA() + " - "
								+ ep2.getTNA() + "]";
						final NrpsConnections nrpsConnection = new NrpsConnections();
						nrpsConnection.setDomain(ep1.getDomain());
						nrpsConnection.setSourceEndpoint(ep1);
						nrpsConnection.setDestinationEndpoint(ep2);
						nrpsConnection.setBandwidth(connection
								.getMinBandwidth());
						nrpsConnection.setDirectionality(connection
								.getDirectionality());
						nrpsConnection.setStatus(StatusType.SETUP_IN_PROGRESS);
						nrpsConnection.setLatency(connection.getMaxLatency());
						connection.addNrpsConnection(nrpsConnection);
					}
					this.log.info(this.logMsg);
				}
			} catch (final InvalidServiceIdException e) {
				throw new RuntimeException("this should not happen: "
						+ e.toString());
			} catch (final InvalidConnectionIdException e) {
				throw new RuntimeException("this should not happen: "
						+ e.toString());
			} catch (final DatabaseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void calculateNrpsConnections(int serviceId, int connId)
			throws PathNotFoundFaultException {
		Service service = this.reservation.getService(serviceId);
		try {
			computePaths(service.getServiceId());
			Connections connection = service.getConnection(connId);
			// get path
			connection.getNrpsConnections().clear();
			final List<Tuple<Endpoint, Endpoint>> path = getActualPath(
					service.getServiceId(), connection.getConnectionId());

			this.logMsg = "PathComputer returned path (serviceID="
					+ service.getServiceId() + " connectionID="
					+ connection.getConnectionId() + "): ";
			for (final Tuple<Endpoint, Endpoint> hop : path) {
				Endpoint ep1;
				Endpoint ep2;
				try {
					ep1 = Endpoint.loadOrCreateUserEndpoint(hop
							.getFirstElement().getTNA());
					ep2 = Endpoint.loadOrCreateUserEndpoint(hop
							.getSecondElement().getTNA());
				} catch (final EndpointNotFoundFaultException e) {
					e.printStackTrace();
					throw new PathNotFoundFaultException(e);
				}
				this.logMsg += " [" + ep1.getTNA() + " - " + ep2.getTNA() + "]";
				final NrpsConnections nrpsConnection = new NrpsConnections();
				nrpsConnection.setDomain(ep1.getDomain());
				nrpsConnection.setSourceEndpoint(ep1);
				nrpsConnection.setDestinationEndpoint(ep2);
				nrpsConnection.setBandwidth(connection.getMinBandwidth());
				nrpsConnection
						.setDirectionality(connection.getDirectionality());
				nrpsConnection.setStatus(StatusType.SETUP_IN_PROGRESS);
				nrpsConnection.setLatency(connection.getMaxLatency());
				connection.addNrpsConnection(nrpsConnection);
			}

			this.log.info(this.logMsg);
		} catch (final InvalidServiceIdException e) {
			throw new RuntimeException("this should not happen: "
					+ e.toString());
		} catch (final InvalidConnectionIdException e) {
			throw new RuntimeException("this should not happen: "
					+ e.toString());
		} catch (final DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Returns an alternative start time offset.
	 * 
	 * @return Alternative start time offset.
	 * @throws DatabaseException
	 * @throws SoapFault
	 */
	public Long getAlternativeStartTimeOffset() throws DatabaseException,
			SoapFault {
		if (!this.processed) {
			this.process();
		}
		return this.alternativeStartTimeOffset;
	}

	/**
	 * Returns the detailed availability result that can be used in an
	 * availability reply.
	 * 
	 * @return Detailed availability result.
	 * @throws DatabaseException
	 * @throws SoapFault
	 */
	public List<ConnectionAvailabilityType> getDetailedResult()
			throws DatabaseException, SoapFault {
		if (!this.processed) {
			this.process();
		}
		return this.detailedAvailability;
	}

	/**
	 * In case the requested services are unavailable, returns the fault to be
	 * thrown for the createReservation operation.
	 * 
	 * @return Fault to be thrown by createReservation in case services are not
	 *         available.
	 */
	public PathNotFoundFaultException getPathNotFound() {
		return this.pathNotFound;
	}

	/**
	 * Returns intradomain services in a hashtable (with domain names as keys).
	 * 
	 * @return Intradomain services.
	 * @throws DatabaseException
	 * @throws IOException
	 * @throws JAXBException
	 * @throws SoapFault
	 */
	public HashMap<Domain, Reservation> getReservations()
			throws DatabaseException, SoapFault {
		if (!this.processed) {
			if (isMultidomain()) {
				this.process();
			} else { // shortcut for pure intradomain requests
				this.outputReservations = getAvailabilityRequests()
						.getSecondElement();
			}
		}
		return this.outputReservations;
	}

	/**
	 * This methods creates a new path computer instance and initializes it.
	 * 
	 * @throws EndpointNotFoundFaultException
	 *             In case one or more of the endpoints contained in the inputer
	 *             service list cannot be found
	 * @throws DatabaseException
	 *             DatabaseException
	 */
	protected void initPathComputer() throws EndpointNotFoundFaultException,
			DatabaseException {

		Class<? extends IPathComputer> pcClass = PathFinderNG.defaultPathComputerClass;

		// check if we are using TestScenario1
		// PathFinderNG.logger.debug(this.reservation.getDebugInfo());
		final Endpoint ep = this.reservation.getServices().values().iterator()
				.next().getConnections().values().iterator().next()
				.getStartpoint();
		final String domainName = ep.getDomain().getName();
		if ((domainName != null) && domainName.equals("scen1destination")) {
			pcClass = PathFinderNG.scenario1PathComputerClass;
		}

		// create path computer

		try {
			Constructor<? extends IPathComputer> c = pcClass
					.getConstructor(boolean.class);
			this.pathComputer = c.newInstance(this.subdomainRestriction);
		} catch (NoSuchMethodException e) {
			try {
				this.pathComputer = pcClass.newInstance();
			} catch (final InstantiationException e1) {
				throw new RuntimeException(
						"error initializing the path computer", e1);
			} catch (final IllegalAccessException e1) {
				throw new RuntimeException(
						"error initializing the path computer", e1);
			}
		} catch (SecurityException e) {
			throw new RuntimeException("error initializing the path computer",
					e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("error initializing the path computer",
					e);
		} catch (InstantiationException e) {
			throw new RuntimeException("error initializing the path computer",
					e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("error initializing the path computer",
					e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("error initializing the path computer",
					e);
		}

		// initialize path computer

		for (final Service service : this.reservation.getServices().values()) {

			// get service data

			// store service to path computer

			try {
				this.pathComputer
						.addService(
								service.getStartTime().getTime(),
								service.getStartTime().getTime()
										+ service.getDuration(),
								service.getServiceId());
			} catch (final InvalidServiceIdException e) {
				throw new RuntimeException("Duplicate service ID "
						+ service.getServiceId());
			}

			for (final Connections connection : service.getConnections()
					.values()) {

				try {
					final Endpoint source = connection.getStartpoint();
					final Endpoint target = connection.getEndpoints()
							.iterator().next();
					final Domain sourceDomain = source.getDomain();
					final Domain targetDomain = target.getDomain();
					/*
					 * if (!
					 * sourceDomain.getName().equals(targetDomain.getName())) {
					 * this.interdomain = true; }
					 */
					this.endDomains.add(sourceDomain);
					this.endDomains.add(targetDomain);
					this.pathComputer.addConnection(source, target,
							service.getServiceId(),
							connection.getConnectionId());
				} catch (final InvalidServiceIdException e) {
					throw new RuntimeException(
							"This really should not happen -- service ID "
									+ service.getServiceId()
									+ " not known to the path computer: " + e);
				} catch (final InvalidConnectionIdException e) {
					throw new RuntimeException(
							"Duplicate service ID / connection ID pair "
									+ service.getServiceId() + " / "
									+ connection.getConnectionId());
				}
			}
		}
	}

	/**
	 * Returns availability of the requested services.
	 * 
	 * @return Availability of requested services.
	 * @throws DatabaseException
	 * @throws SoapFault
	 */
	public boolean isAvailable() throws DatabaseException, SoapFault {
		if (!this.processed) {
			if (this.malleable) {
				this.processMalleable();
			} else {
				this.process();
			}
		}
		return (this.outputReservations != null);
	}

	/**
	 * This method takes a list of services possibly spanning multiple domains
	 * as input and splits them to single-domain services verified to be
	 * available.
	 * 
	 * @param inputServices
	 *            List < ServiceConstraintType >
	 * @return Hashtable < String, List < ServiceConstraintType > >
	 * @throws DatabaseException
	 *             DatabaseException
	 * @throws SoapFault
	 *             SoapFault
	 */
	protected void process() throws DatabaseException, SoapFault {

		this.processed = true;

		// start path-computing time-measuring
		this.performanceLogger.log(PerformanceLogLevel.PERFORMANCE_LOG,
				"started path-computing: " + new Date());

		int maxAvailableConnections = -1;
		// table that contains reservations for each domain
		HashMap<Domain, Reservation> nrpsReservationTable = null;
		boolean availablePathFound = false;

		String debug = null;
		while (!availablePathFound) {
			// table that contains detailed availability result for each
			// connection

			final Hashtable<Tuple<Integer, Integer>, ConnectionAvailabilityType> connectionAvailabilityTable = new Hashtable<Tuple<Integer, Integer>, ConnectionAvailabilityType>();

			// number of available connections in that table

			int availableConnectionsCount = 0;

			// split interdomain request to intradomain requests and
			// construct isAvailable requests for the NRPS manager
			Hashtable<Domain, IsAvailableType> nrpsRequests = new Hashtable<Domain, IsAvailableType>();
			try {
				final Tuple<Hashtable<Domain, IsAvailableType>, HashMap<Domain, Reservation>> result = getAvailabilityRequests();
				nrpsRequests = result.getFirstElement();
				nrpsReservationTable = result.getSecondElement();
			} catch (PathNotFoundFaultException e) {
				PathNotFoundFaultException newEx = new PathNotFoundFaultException(
						"No Path could be found! " + debug
								+ " PathComputerException: " + e.getMessage());
				this.pathNotFound = newEx;
				return;
			}

			// send requests

			final Hashtable<Domain, IsAvailableResponseType> nrpsResponses = this.nrpsManager
					.isAvailable(nrpsRequests);

			// evaluate results

			availablePathFound = true;
			Long offset = null;
			debug = "getAvailableServiceList: ";
			for (final Domain domain : nrpsResponses.keySet()) {
				debug += "<domain name=" + domain.getName() + ">";
				boolean pruned = false;
				boolean available = true;
				final IsAvailableResponseType nrpsResponse = nrpsResponses
						.get(domain);
				if (nrpsResponse.isSetAlternativeStartTimeOffset()) {
					final Long offsetNew = nrpsResponse
							.getAlternativeStartTimeOffset();
					debug += "<alternative_start_time_offset="
							+ offsetNew.longValue() + "/>";
					if ((offset == null) || (offset.compareTo(offsetNew) < 0)) {
						offset = offsetNew;
					}
				}
				// go through responses and prune from path computer graph if
				// necessary
				for (final ConnectionAvailabilityType ca : nrpsResponse
						.getDetailedResult()) {
					final Tuple<Integer, Integer> id = new Tuple<Integer, Integer>(
							new Integer(ca.getServiceID()), new Integer(
									ca.getConnectionID()));
					ConnectionAvailabilityType outputAvailability = connectionAvailabilityTable
							.get(id);
					if (outputAvailability == null) {
						outputAvailability = new ConnectionAvailabilityType();
						outputAvailability.setServiceID(id.getFirstElement()
								.intValue());
						outputAvailability.setConnectionID(id
								.getSecondElement().intValue());
						outputAvailability
								.setAvailability(AvailabilityCodeType.AVAILABLE);
						connectionAvailabilityTable.put(id, outputAvailability);
						availableConnectionsCount++;
					}

					final AvailabilityCodeType availability = ca
							.getAvailability();
					debug += "<connection service_id=" + ca.getServiceID()
							+ " connection_id=" + ca.getConnectionID()
							+ " result=" + availability;
					if ((availability == AvailabilityCodeType.ENDPOINT_NOT_AVAILABLE)
							&& (ca.isSetBlockedEndpoints())) {
						debug += " blocked_endpoints=";
						int i = 0;
						for (final String s : ca.getBlockedEndpoints()) {
							if (i++ > 0) {
								debug += ",";
							}
							debug += s;
						}
					}
					debug += "/>";
					if (availability != AvailabilityCodeType.AVAILABLE) {
						availablePathFound = false;
						available = false;
						Service s = null;
						if (availability != AvailabilityCodeType.AVAILABILITY_NOT_CHECKED) {
							// FIXME: nrpsReservationTable could be null!
							s = nrpsReservationTable.get(domain).getServices()
									.get(Integer.valueOf(ca.getServiceID()));
							if (s != null) {
								this.pruneStuff(s, ca.getConnectionID(),
										availability, ca.getBlockedEndpoints());
								pruned = true;
							} else {
								final String msg = "Service ID "
										+ ca.getServiceID()
										+ " not found in response from domain "
										+ domain.getName() + " .. bailing out!";
								this.log.fatal(msg);
								this.pathNotFound = new PathNotFoundFaultException(
										msg);
								return;
							}
						}
						if (outputAvailability.getAvailability() == AvailabilityCodeType.AVAILABLE) {
							availableConnectionsCount--;
							outputAvailability.setAvailability(availability);
						}
						if (availability == AvailabilityCodeType.ENDPOINT_NOT_AVAILABLE) {
							if (s == null) {
								s =

								nrpsReservationTable
										.get(domain)
										.getServices()
										.get(Integer.valueOf(ca.getServiceID()));
							}
							if (s != null) {
								final Connections c = s.getConnections().get(
										Integer.valueOf(ca.getConnectionID()));
								final List<String> blocked = new LinkedList<String>();
								for (final String tna : ca
										.getBlockedEndpoints()) {
									boolean reqSpec = tna.equals(c
											.getStartpoint().getTNA());
									for (final Endpoint endpoint : c
											.getEndpoints()) {
										if (endpoint.getTNA().equals(tna)) {
											reqSpec = true;
											break;
										}
									}
									if (reqSpec) {
										blocked.add(tna);
									}
								}
								if (blocked.size() > 0) {
									outputAvailability
											.setAvailability(AvailabilityCodeType.ENDPOINT_NOT_AVAILABLE);
									for (final String tna : blocked) {
										outputAvailability
												.getBlockedEndpoints().add(tna);
									}
								}
							}
						}
					}
				}
				if (!(available || pruned)) {
					// This can actually happen if a domain only answers
					// "not checked". The result is an infinite loop!
					this.log.fatal("Domain \""
							+ domain
							+ "\" returned only AVAILABILITY_NOT_CHECKED! Changing this to PATH_NOT_AVAILABLE for now ...");
					for (final ConnectionAvailabilityType ca : nrpsResponse
							.getDetailedResult()) {
						if (ca.getAvailability() == AvailabilityCodeType.AVAILABILITY_NOT_CHECKED) {
							final Service s = nrpsReservationTable.get(domain)
									.getServices()
									.get(Integer.valueOf(ca.getServiceID()));
							this.pruneStuff(s, ca.getConnectionID(),
									AvailabilityCodeType.PATH_NOT_AVAILABLE,
									null);
						}
					}
				}
				debug += "</domain>";
			}
			if (maxAvailableConnections < availableConnectionsCount) {
				maxAvailableConnections = availableConnectionsCount;
				this.detailedAvailability = new LinkedList<ConnectionAvailabilityType>();
				for (final ConnectionAvailabilityType ca : connectionAvailabilityTable
						.values()) {
					this.detailedAvailability.add(ca);
				}
			}
			this.log.info(debug);
			if ((this.alternativeStartTimeOffset == null)
					|| ((offset != null) && (this.alternativeStartTimeOffset
							.compareTo(offset) > 0))) {
				this.alternativeStartTimeOffset = offset;
			}
		}
		this.outputReservations = nrpsReservationTable;
	}

	private void processMalleable() throws SoapFault, DatabaseException {

		this.processed = true;

		MalleableReservationHelpers malleableHelpers = MalleableReservationHelpers
				.getInstance();
		/** extra logger instance for the malleable reservations. */
		Logger malleableLogger = PhLogger.getSeparateLogger("malleable");

		boolean reservationFound = false;

		HashMap<Domain, Reservation> finalDomResMapping = new HashMap<Domain, Reservation>();

		// initial AvailabilityRequest-creation and converting from malleable to
		// fixed
		for (Service service : this.reservation.getServices().values()) {
			for (Connections conn : service.getConnections().values()) {

				// log the reservation
				String log = "reservation: " + this.reservation.toString()
						+ "\n";
				log += "  -> service: " + service.toString() + "\n";
				log += "    -> connection: " + conn.toString() + "\n";
				malleableLogger.debug(log);

				reservationFound = false;
				boolean computeNewPath = true;
				PathHandler actualPh = null;
				FixedAvailabilityRequestsConverter actualRc = null;

				while (!reservationFound) {

					while (computeNewPath) {
						try {
							calculateNrpsConnections(service.getServiceId(),
									conn.getConnectionId());
						} catch (final PathNotFoundFaultException e) {
							String exMessage = "no feasible path available "
									+ "to create a malleable reservation from "
									+ "["
									+ conn.getStartpoint().getTNA()
									+ "] to "
									+ "["
									+ ((Endpoint) conn.getEndpoints().toArray()[0])
											.getTNA() + "] !";
							malleableLogger.error(exMessage);
							this.pathNotFound = new PathNotFoundFaultException(
									"No path could be found: " + e.getMessage()
											+ "(info:" + this.logMsg + ")", e);
							return;
						}

						// save path
						try {
							actualPh = new PathHandler(pathComputer.getPath(
									service.getServiceId(),
									conn.getConnectionId()),
									conn.getMinBandwidth(),
									conn.getMaxBandwidth());
							String pathLog = "computed path: ";
							for (Tuple<Endpoint, Endpoint> pathPart : actualPh
									.getPath()) {
								pathLog += "["
										+ pathPart.getFirstElement().getTNA()
										+ " -> "
										+ pathPart.getSecondElement().getTNA()
										+ "] ";
							}
							malleableLogger.debug(pathLog);
						} catch (InvalidServiceIdException e1) {
							malleableLogger
									.error("wrong service-ID for pathfinder!");
						} catch (InvalidConnectionIdException e1) {
							malleableLogger
									.error("wrong connection-ID for pathfinder!");
						}

						// create advanced availability requests
						actualRc = new FixedAvailabilityRequestsConverter(
								actualPh);

						actualRc.createRequestsFromMalleable(
								this.reservation.getReservationsForDomains(),
								service.getServiceId(), conn.getConnectionId());

						// validate availability requests
						if (!actualRc.validateRequests()) {
							malleableLogger
									.debug("Duration too long! Trying next path!");
							// the duration is too long, remove minBw-edges and
							// compute new path
							List<String> minBwEnds = actualPh
									.getMinBwEndpoints();

							Endpoint precedentEndpoint = null;
							for (Tuple<Endpoint, Endpoint> pathPart : actualPh
									.getPath()) {
								// intraDomain-edge
								if (minBwEnds.contains(pathPart
										.getFirstElement().getTNA())
										|| minBwEnds.contains(pathPart
												.getSecondElement().getTNA())) {
									try {
										malleableLogger.debug("pruning edge ["
												+ pathPart.getFirstElement()
														.getTNA()
												+ " -> "
												+ pathPart.getSecondElement()
														.getTNA() + "]");
										this.pruneExactOneEdge(service,
												conn.getConnectionId(),
												pathPart.getFirstElement(),
												pathPart.getSecondElement());
									} catch (EndpointNotFoundFaultException e) {
										malleableLogger
												.error("wrong endpoint for pathfinder!");
									} catch (InvalidServiceIdException e1) {
										malleableLogger
												.error("wrong service-ID for pathfinder!");
									} catch (InvalidConnectionIdException e1) {
										malleableLogger
												.error("wrong connection-ID for pathfinder!");
									}
								}
								// interDomain edge
								if (precedentEndpoint != null) {
									if (minBwEnds.contains(precedentEndpoint
											.getTNA())
											|| minBwEnds
													.contains(pathPart
															.getFirstElement()
															.getTNA())) {
										try {
											malleableLogger
													.debug("pruning edge ["
															+ precedentEndpoint
																	.getTNA()
															+ " -> "
															+ pathPart
																	.getFirstElement()
																	.getTNA()
															+ "]");
											this.pruneExactOneEdge(service,
													conn.getConnectionId(),
													precedentEndpoint,
													pathPart.getFirstElement());
										} catch (EndpointNotFoundFaultException e) {
											malleableLogger
													.error("wrong endpoint for pathfinder!");
										} catch (InvalidServiceIdException e1) {
											malleableLogger
													.error("wrong service-ID for pathfinder!");
										} catch (InvalidConnectionIdException e1) {
											malleableLogger
													.error("wrong connection-ID for pathfinder!");
										}
									}
								}
								precedentEndpoint = pathPart.getSecondElement();
							}
						} else {
							// requests are valid -> valid path Found
							computeNewPath = false;
						}
					}

					// log the availability-requests
					for (Domain d : actualRc.getRequests().keySet()) {
						for (ServiceConstraintType s : actualRc.getRequests()
								.get(d).getService()) {
							for (ConnectionConstraintType c : s
									.getConnections()) {
								for (EndpointType e : c.getTarget()) {
									malleableLogger
											.debug("request for domain: "
													+ d.getName()
													+ "\n"
													+ "for connection from ["
													+ c.getSource()
															.getEndpointId()
													+ "] to ["
													+ e.getEndpointId()
													+ "]\n"
													+ "start: "
													+ s.getFixedReservationConstraints()
															.getStartTime()
													+ " duration: "
													+ s.getFixedReservationConstraints()
															.getDuration()
													+ " bandwidth: "
													+ actualPh
															.getActualBwOfEndpoint(c
																	.getSource()
																	.getEndpointId())
													+ " | "
													+ actualPh
															.getActualBwOfEndpoint(e
																	.getEndpointId()));
								}
							}
						}
					}

					// send availability-requests
					Hashtable<Domain, IsAvailableResponseType> nrpsResponses = this.nrpsManager
							.isAvailable(actualRc.getRequests());

					int availabilitySum = 0;

					long altOffset = 0l;
					long actualEndTimeMillis = actualRc.getActualStartTime()
							.getTime() + (actualRc.getActualDuration() * 1000l);

					// list of blocked endpoints found in one timeslot
					// evaluation for a specific service
					List<String> blockedEndpoints = new ArrayList<String>();

					// list of blocked domains found in one timeslot evaluation
					// for a specific service
					List<String> blockedDomains = new ArrayList<String>();

					// log the availability response and get blocked endpoints
					for (Domain d : nrpsResponses.keySet()) {
						IsAvailableResponseType nrpsResponse = nrpsResponses
								.get(d);
						for (ConnectionAvailabilityType cat : nrpsResponse
								.getDetailedResult()) {
							AvailabilityCodeType av = cat.getAvailability();
							long altOffsetD = 0l;
							if ((av == AvailabilityCodeType.ENDPOINT_NOT_AVAILABLE)
									|| (av == AvailabilityCodeType.PATH_NOT_AVAILABLE)) {
								if (nrpsResponse
										.isSetAlternativeStartTimeOffset()) {
									altOffsetD = nrpsResponse
											.getAlternativeStartTimeOffset();
								}
								if (altOffsetD <= 0l) { // erroneous replies may
														// contain negative
														// values
									altOffsetD = 3600;
								}
							}
							final boolean prune;
							if (altOffsetD > 0l) {
								long offsetCheck = actualEndTimeMillis
										+ (altOffsetD * 1000l);
								prune = (offsetCheck > actualRc
										.getGlobalDeadline().getTime());
								malleableLogger.debug("offsetCheck: "
										+ actualEndTimeMillis
										+ " + "
										+ (altOffsetD * 1000l)
										+ " <?> "
										+ actualRc.getGlobalDeadline()
												.getTime());
								malleableLogger
										.debug("offsetCheck human-readable: "
												+ actualRc.getActualStartTime()
												+ " + "
												+ actualRc.getActualDuration()
												+ "s + " + altOffsetD
												+ "s <?> "
												+ actualRc.getGlobalDeadline());
							} else {
								prune = false;
							}
							// Service domainService = null;
							// if (prune) {
							// domainService =
							// result.getSecondElement().get(d).getService(cat.getServiceID());
							// if (domainService == null) {
							// String ss = "";
							// for (Integer i :
							// result.getSecondElement().get(d).getServices().keySet())
							// {
							// ss += " " + i;
							// }
							// malleableLogger.error("cannot retrieve service with ID "
							// + cat.getServiceID() + " for domain " +
							// d.getName() + ", available IDs: " + ss);
							// }
							// }
							computeNewPath |= prune;
							if (altOffsetD > altOffset) {
								altOffset = altOffsetD;
							}

							if (av == AvailabilityCodeType.ENDPOINT_NOT_AVAILABLE) {
								// port unavailable
								if (blockedEndpoints.size() == 0) { // UCLP
																	// adapter
																	// hack
									av = AvailabilityCodeType.PATH_NOT_AVAILABLE;
								} else if (prune) {
									this.pruneStuff(service,
											cat.getConnectionID(), av,
											cat.getBlockedEndpoints());
								}
							}
							if (av == AvailabilityCodeType.PATH_NOT_AVAILABLE) {
								// intradomain path is not available
								blockedDomains.add(d.getName());
								if (prune) {
									this.pruneStuff(service,
											cat.getConnectionID(), av, null);
								}
							}

							availabilitySum = Math.max(availabilitySum,
									av.ordinal());

							String logMsg = "availability "
									+ d.getName()
									+ " for service: "
									+ cat.getServiceID()
									+ " and connection: "
									+ cat.getConnectionID()
									+ ":\n"
									+ cat.getAvailability()
									+ "\n"
									+ "offset: "
									+ nrpsResponses.get(d)
											.getAlternativeStartTimeOffset()
									+ "\n blocked: ";
							for (String blocked : cat.getBlockedEndpoints()) {
								logMsg += blocked + " ";
								if (blocked != null) {
									blockedEndpoints.add(blocked);
								}
							}
							malleableLogger.debug(logMsg);
						}
					}

					if (availabilitySum > 0) {
						// first try to compute new starttime if feasible
						XMLGregorianCalendar newStartTime = malleableHelpers
								.computeNewStartTime(nrpsResponses,
										actualRc.getActualStartTime(),
										actualRc.getGlobalDeadline(),
										actualRc.getActualDuration());

						if (newStartTime != null) {
							// found a new startTime, that is feasible
							actualRc.shiftRequests(newStartTime);
							// no new path computing
							computeNewPath = false;
						} else {
							// check if bandwidths can be adapted
							Tuple<Boolean, Hashtable<String, Integer>> bwPossible = actualPh
									.isBwAdaptingPossible(blockedEndpoints);
							if (bwPossible.getFirstElement()) {
								// bandwidth adapting is feasible
								// -> use the same path, but adapt the requests
								// to new bandwidth
								// -> new durations have to be computed and the
								// availability-requests
								// on the same path should start from the
								// initial startTime again
								actualRc.adaptToNewBws(bwPossible
										.getSecondElement());

								// set the startTime back
								actualRc.shiftRequests(actualRc
										.getGlobalStarttime());
								// no new path computing
								computeNewPath = false;
							} else {
								// found no feasible startTime on that path and
								// the BW could not be
								// adapted -> prune edges of blocked endpoints,
								// edges of not-available
								// domains and try to find a new path

								List<Tuple<Endpoint, Endpoint>> prunableEdges = actualPh
										.getPrunableEdges(blockedEndpoints,
												blockedDomains);

								for (Tuple<Endpoint, Endpoint> edge : prunableEdges) {
									malleableLogger.debug("pruning edge: ["
											+ edge.getFirstElement().getTNA()
											+ " - "
											+ edge.getSecondElement().getTNA()
											+ "]");
									try {
										this.pruneExactOneEdge(service,
												conn.getConnectionId(),
												edge.getFirstElement(),
												edge.getSecondElement());
									} catch (InvalidServiceIdException e) {
										malleableLogger
												.error("This should not happen!");
										e.printStackTrace();
									} catch (InvalidConnectionIdException e) {
										malleableLogger
												.error("This should not happen!");
										e.printStackTrace();
									}
								}

								computeNewPath = true;
							}
						}
					} else {
						// all path-parts available -> set fixed reservations
						reservationFound = true;
						for (Entry<Domain, Reservation> entry : actualRc
								.getActualReservations().entrySet()) {
							malleableHelpers.mergeReservation(this.reservation,
									finalDomResMapping, entry.getValue(),
									entry.getKey());
						}
					}
				}
			}
		}

		this.outputReservations = finalDomResMapping;
	}

	/**
	 * This method prunes vertices or edges from a path computer instance
	 * depending on an availability reply from a domain.
	 * 
	 * @param pathComputer
	 *            IPathComputer Path computer instance.
	 * @param service
	 *            ServiceConstraintType Affected service.
	 * @param connectionId
	 *            int ID of the affected connection inside the service.
	 * @param availability
	 *            AvailabilityCodeType Availability value returned by the
	 *            domain.
	 * @param blockedEndpoints
	 *            List < String >
	 * @throws EndpointNotFoundFaultException
	 *             EndpointNotFoundFaultException
	 */
	public void pruneStuff(final Service service, final int connectionId,
			final AvailabilityCodeType availability,
			final List<String> blockedEndpoints)
			throws EndpointNotFoundFaultException {
		final int serviceId = service.getServiceId();

		try {
			AvailabilityCodeType av = availability;
			if (av == AvailabilityCodeType.ENDPOINT_NOT_AVAILABLE) {
				// port unavailable
				if (blockedEndpoints.size() == 0) { // UCLP adapter hack
					av = AvailabilityCodeType.PATH_NOT_AVAILABLE;
				}
				for (final String ep : blockedEndpoints) {
					this.log.info("PathComputer pruning endpoint " + ep
							+ " (serviceID=" + serviceId + " connectionID="
							+ connectionId + ")");
					final Endpoint e = new Endpoint();
					e.setTNA(ep);
					this.pathComputer.pruneEndpoint(serviceId, connectionId, e);
				}
			}
			if (av == AvailabilityCodeType.PATH_NOT_AVAILABLE) {
				// path unavailable
				// final Connections c = service.getConnections().get(
				// String.valueOf(connectionId));
				final Connections c = service.getConnections()
						.get(connectionId);

				if (c == null) {
					throw new RuntimeException("connection id " + connectionId
							+ " does not exist!");
				}
				Endpoint start = c.getStartpoint();
				String startTNA = start.getTNA();
				Endpoint end = (Endpoint) c.getEndpoints().toArray()[0];
				String endTNA = end.getTNA();

				this.log.info("PathComputer pruning edge " + startTNA + " - "
						+ endTNA + " (serviceID=" + serviceId
						+ " connectionID=" + connectionId + ")");

				this.pathComputer
						.pruneEdge(serviceId, connectionId, start, end);
			}

		} catch (final InvalidServiceIdException e) {
			throw new RuntimeException("this should not happen: "
					+ e.toString());
		} catch (final InvalidConnectionIdException e) {
			throw new RuntimeException("this should not happen: "
					+ e.toString());
		}
	}

	/**
	 * method to prune exact and only the edge between two given endpoints
	 * 
	 * @param service
	 *            affected service for pathComputer
	 * @param connectionId
	 *            affected connectionId for pathComputer
	 * @param start
	 *            the start endpoint of the edge
	 * @param target
	 *            the target endpoint of the edge
	 * @throws EndpointNotFoundFaultException
	 * @throws InvalidServiceIdException
	 * @throws InvalidConnectionIdException
	 */
	public void pruneExactOneEdge(final Service service,
			final int connectionId, final Endpoint start, final Endpoint target)
			throws EndpointNotFoundFaultException, InvalidServiceIdException,
			InvalidConnectionIdException {
		this.pathComputer.pruneEdge(service.getServiceId(), connectionId,
				start, target);

	}

	/**
	 * compute nrpsConnections for the actual computed path and create according
	 * isAvailableRequests
	 * 
	 * @return
	 * @throws DatabaseException
	 * @throws PathNotFoundFaultException
	 */
	public Tuple<Hashtable<Domain, IsAvailableType>, HashMap<Domain, Reservation>> getAvailabilityRequests()
			throws DatabaseException, PathNotFoundFaultException {
		Tuple<Hashtable<Domain, IsAvailableType>, HashMap<Domain, Reservation>> result;
		Hashtable<Domain, IsAvailableType> nrpsRequests = new Hashtable<Domain, IsAvailableType>();
		HashMap<Domain, Reservation> nrpsReservationTable = new HashMap<Domain, Reservation>();
		// split interdomain request to intradomain requests
		try {
			this.calculateNrpsConnections();
		} catch (final PathNotFoundFaultException e) {
			this.log.info("No path could be found");
			throw new PathNotFoundFaultException("No path could be found: "
					+ e.getMessage() + "(info:" + this.logMsg + ")", e);
		}
		// table that contains reservations for each domain
		nrpsReservationTable = this.reservation.getReservationsForDomains();

		// construct isAvailable requests for the NRPS manager
		for (final Domain domain : nrpsReservationTable.keySet()) {
			final IsAvailableType req = new IsAvailableType();
			if (domain.getRelationship().equals(
					DomainRelationshipType.PEER.value())) {
				req.setSubdomainRestriction(true);
			}
			for (final Service s : nrpsReservationTable.get(domain)
					.getServices().values()) {
				req.getService().add(s.toJaxb());
			}
			nrpsRequests.put(domain, req);
		}

		result = new Tuple<Hashtable<Domain, IsAvailableType>, HashMap<Domain, Reservation>>(
				nrpsRequests, nrpsReservationTable);
		return result;
	}

	/**
	 * get the actual computed path from the pathComputer-object of this
	 * pathFinder
	 * 
	 * @param serviceId
	 * @param connectionId
	 * @throws PathNotFoundFaultException
	 * @throws InvalidServiceIdException
	 */
	public List<Tuple<Endpoint, Endpoint>> getActualPath(int serviceId,
			int connectionId) throws InvalidServiceIdException,
			InvalidConnectionIdException {
		return this.pathComputer.getPath(serviceId, connectionId);
	}

	/**
	 * compute an available path in the pathComputer-object of this pathFinder
	 * 
	 * @param serviceId
	 * @throws PathNotFoundFaultException
	 * @throws InvalidServiceIdException
	 */
	public void computePaths(int serviceId) throws PathNotFoundFaultException,
			InvalidServiceIdException {
		this.pathComputer.computePaths(serviceId);
	}
}
