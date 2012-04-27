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


package org.opennaas.extensions.idb.reservation.pathcomputer;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DomainRelationshipType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.EndpointNotFoundFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.PathNotFoundFaultException;
import org.opennaas.extensions.idb.database.hibernate.Domain;
import org.opennaas.extensions.idb.database.hibernate.Endpoint;
import org.opennaas.extensions.idb.database.hibernate.VIEW_InterDomainLink;
import org.opennaas.extensions.idb.exception.database.DatabaseException;
import org.opennaas.extensions.idb.reservation.exceptions.InvalidConnectionIdException;
import org.opennaas.extensions.idb.reservation.exceptions.InvalidServiceIdException;
import org.opennaas.core.utils.Tuple;

/**
 * Basic path computer that implements interface IPathComputer. Additional
 * functionality, like compute multiple paths efficiently is yet to be
 * implemented.
 * 
 * @author Bert Andree (handree@science.uva.nl)
 */
public class PathComputer implements IPathComputer {
    /**
     * Class used to store data about connections.
     */
    class ConnData {
        public final Endpoint src;
        public final Endpoint dst;
        public final PruneData pruned = new PruneData();
        public List<Tuple<Endpoint, Endpoint>> path = null;
        /** Can be used by subclasses to store connection specific data. */
        public Object data;

        public ConnData(final Endpoint s, final Endpoint d) {
            this.src = s;
            this.dst = d;
            this.data = null;
        }

        public ConnData(final Endpoint s, final Endpoint d, final Object data) {
            this.src = s;
            this.dst = d;
            this.data = data;
        }
    }

    /**
     * Class used to store pruned resources.
     */
    class PruneData {
        public final Set<String> endpoints = new HashSet<String>();
        public final Map<String, Set<String>> edges = new HashMap<String, Set<String>>();
    }

    /**
     * Class used to store data about services added to this PC instance.
     */
    class ServiceData {
        public final long startTime;
        public final long endTime;
        public final HashMap<Integer, ConnData> conn = new HashMap<Integer, ConnData>();

        public ServiceData(final long startTime, final long endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }

        public boolean overlapsWith(final ServiceData s) {
            if (this.endTime < s.startTime) {
                /**
                 * this.startTime <= this.endTime this.endTime < s.startTime
                 * s.startTime <= s.endTime
                 */
                return false;
            } else if (this.startTime > s.endTime) {
                /**
                 * s.startTime <= s.endTime s.endTime < this.startTime
                 * this.startTime <= this.endTime
                 */
                return false;
            } else {
                /**
                 * this.startTime <= s.endTime s.startTime <= this.endTime
                 */
                return true;
            }
        }
    }

    Hashtable<String, Vertex> vertexSet = null;

    Hashtable<String, Edge> edgeSet = null;

    Hashtable<String, List<Edge>> adjSet = new Hashtable<String, List<Edge>>();

    BinaryHeap binaryHeap;

    /**
     * All services added to this PC instance so far.
     */
    protected HashMap<Integer, ServiceData> services = new HashMap<Integer, ServiceData>();

    private int getVIOLALocation(String tna) {
    	if (! tna.startsWith("10.7.")) {
    		return 0;
    	}
    	String lCode = tna.substring(5);
    	int dIdx = lCode.indexOf('.');
    	if (dIdx > 0) {
    		lCode = lCode.substring(0, dIdx);
    	}
    	int result = Integer.parseInt(lCode);
    	if ((result >= 11) && (result <= 13)) {
    		result -= 10;
    	}
    	if (result == 132) {
    		result = 3;
    	}
    	return result;
    }
    
    /**
     * Constructor of the class PathComputer
     */
    public PathComputer(boolean subdomainRestriction) {
        this.vertexSet = new Hashtable<String, Vertex>();
        this.edgeSet = new Hashtable<String, Edge>();

        /**
         * load the topology from the database
         */
        try {
            final Set<Domain> tmpdomains = Domain.loadAll();
            if (subdomainRestriction) {
            	Iterator<Domain> it = tmpdomains.iterator();
            	while (it.hasNext()) {
            		Domain d = it.next();
            		String rel = d.getRelationship();
            		if ((rel == null) || (! rel.equals(DomainRelationshipType.SUBDOMAIN.value()))) {
            			it.remove();
            		}
            	}
            }
        	final Set<VIEW_InterDomainLink> allLinks = VIEW_InterDomainLink.loadAll();
            for (final Domain myDomain : tmpdomains) {
                /**
                 * Do something with myDomain.getName(); as each domain only
                 * needs to be treversed once. This will make path computation
                 * more efficient. For first implementation this is not
                 * important as the network is small and (in general) the
                 * shortest path does not traverse a dommain twice.
                 */
                final String myDomainName = myDomain.getName();

                /**
                 * Now get the endpoints....
                 */
                for (final Endpoint e : myDomain.getEndpoints()) {
                    final String eTNA = e.getTNA();
                    /**
                     * Add endpoint e to the graph Note that we do not check if
                     * the endpoint is a border endpoint. This should be no
                     * problem as we can handle unconnected graphs.
                     */
                    final Vertex vertex = new Vertex();
                    vertex.name = eTNA;
                    vertex.endpoint = e;
                    vertex.source = myDomainName;
                    this.vertexSet.put(eTNA, vertex);
                    /**
                     * create adjacency list
                     */
                    final List<Edge> adjList = new ArrayList<Edge>();
                    this.adjSet.put(vertex.name, adjList);
                }
                /**
                 * Now we need to interconnect all endpoints of this domain Note
                 * that this can be done more efficiently
                 */
                for (final Endpoint e1 : myDomain.getEndpoints()) {
                    for (final Endpoint e2 : myDomain.getEndpoints()) {
                        final String e1TNA = e1.getTNA();
                        final String e2TNA = e2.getTNA();
                        if (!e1TNA.equals(e2TNA)) {
                            final Edge edge = new Edge();
                            edge.name = e1TNA + "_to_" + e2TNA;
                            edge.source = e1;
                            edge.destination = e2;
                            edge.weight = 1;
                            int hack1 = getVIOLALocation(e1TNA);
                            int hack2 = getVIOLALocation(e2TNA);
                            if ((hack1 > 0) && (hack2 > 0) && (hack1 != hack2)) {
                            	edge.weight += 1;
                            }
                            if (!this.edgeSet.contains(edge.name)) {
                                this.edgeSet.put(edge.name, edge);
                                // put in adjacency list
                                final List<Edge> adjList = this.adjSet
                                        .get(e1TNA);
                                adjList.add(edge);
                            }
                        }
                    }
                }
            }
            /**
             * Now load all interdomain links.
             */
            for (final Domain myDomain : tmpdomains) {
                final Set<VIEW_InterDomainLink> tmplinks = new HashSet<VIEW_InterDomainLink>(); //myDomain.loadAllLinks()
                final String myDomainName = myDomain.getName();
                for (VIEW_InterDomainLink link : allLinks) {
                	if (link.getSourceEndpoint().getDomain().getName().equals(myDomainName)
                			|| link.getDestEndpoint().getDomain().getName().equals(myDomainName)) {
                		tmplinks.add(link);
                	}
                }
                for (final VIEW_InterDomainLink myLink : tmplinks) {
                    /**
                     * Find source and destination endpoints of this link
                     */
                    final Endpoint e1 = myLink.getSourceEndpoint();
                    final Endpoint e2 = myLink.getDestEndpoint();
                    final String e1TNA = e1.getTNA();
                    final String e2TNA = e2.getTNA();
                	final List<Edge> adjList1 = this.adjSet.get(e1TNA);
                	final List<Edge> adjList2 = this.adjSet.get(e2TNA);
                    if ((! e1TNA.equals(e2TNA)) && (adjList1 != null) && (adjList2 != null)) {
                        /**
                         * Add edge (e1, e2) to the graph
                         */
                        final Edge edge = new Edge();
                        edge.name = e1TNA + "_to_" + e2TNA;
                        edge.source = e1;
                        edge.destination = e2;
                        edge.weight = 1;
                        if (!this.edgeSet.contains(edge.name)) {
                            this.edgeSet.put(edge.name, edge);
                            // put in adjacency list
                            adjList1.add(edge);
                        }
                        // Now put the reverse edge in as well
                        final Edge edge2 = new Edge();
                        edge2.name = e2TNA + "_to_" + e1TNA;
                        edge2.source = e2;
                        edge2.destination = e1;
                        edge2.weight = 1;
                        if (!this.edgeSet.contains(edge2.name)) {
                            this.edgeSet.put(edge2.name, edge2);
                            // put in adjacency list
                            adjList2.add(edge2);
                        }
                    }
                }
            }
        } catch (final DatabaseException e) {
            throw new RuntimeException(
                    "PathComputer caught DatabaseException: " + e.toString(), e);
        }
        /**
         * Prune resources that are knwon not to be available. This is not
         * implemented yet, and uncertain if it needs to be here. Resources may
         * be unavalaible a a specific time ant thus for a specific service.
         */
    } // PathComputer

    /*
     * @see org.opennaas.extensions.idb.reservation.IPathComputer2#addConnection(org.opennaas.extensions.idb.database.hibernate.Endpoint,
     *      org.opennaas.extensions.idb.database.hibernate.Endpoint, int, int)
     */
    public void addConnection(final Endpoint source,
            final Endpoint destination, final int serviceId,
            final int connectionId) throws EndpointNotFoundFaultException,
            InvalidServiceIdException, InvalidConnectionIdException {

        // make sure that we know the endpoints' domains
        final String srcDomID = source.getDomain().getName();
        final String dstDomID = destination.getDomain().getName();
        if (srcDomID == null) {
            throw new EndpointNotFoundFaultException(source.getTNA());
        }
        if (dstDomID == null) {
            throw new EndpointNotFoundFaultException(destination.getTNA());
        }

        /*
         * We first check if source and destination are blocked. In this case,
         * no path can be calculated anyway. TODO: change Exception type.
         */
        if (this.getBlockedEndpoints(serviceId).contains(source.getTNA())) {
            /*
             * Endpoint source was blocked, so no path available.
             */
            throw new EndpointNotFoundFaultException(
                    "addConnection: requested endpoint ( " + source.getTNA()
                            + " ) is Blocked by another connection.");
        }

        if (this.getBlockedEndpoints(serviceId).contains(destination.getTNA())) {
            /*
             * Endpoint destination was blocked, so no path available.
             */
            throw new EndpointNotFoundFaultException(
                    "addConnection: requested endpoint ( "
                            + destination.getTNA()
                            + " ) is Blocked by another connection.");
        }

        // add user endpoints and pseudo intra-domain links:
        if (!this.vertexSet.containsKey(source.getTNA())) {
            /**
             * First add source to vertexSet
             */
            final Vertex vertex = new Vertex();
            vertex.name = (source.getTNA());
            vertex.endpoint = source;
            vertex.source = srcDomID;
            this.vertexSet.put(source.getTNA(), vertex);
            /**
             * create adjacency list
             */
            List<Edge> adjList = new ArrayList<Edge>();
            this.adjSet.put(vertex.name, adjList);
            final Enumeration<Vertex> enumer = this.vertexSet.elements();
            while (enumer.hasMoreElements()) {
                final Vertex v = enumer.nextElement();
                if (v.source.equals(srcDomID)) {
                    /**
                     * v and source are of the same domain
                     */
                    if (!v.endpoint.getTNA().equals(source.getTNA())) {
                        /**
                         * v and source are different endpoints
                         */
                        final Edge edge = new Edge();
                        edge.name = v.endpoint.getTNA() + "_to_"
                                + source.getTNA();
                        edge.source = v.endpoint;
                        edge.destination = source;
                        edge.weight = 1;
                        if (!this.edgeSet.contains(edge.name)) {
                            this.edgeSet.put(edge.name, edge);
                            // put in adjacency list
                            adjList = this.adjSet.get(v.endpoint.getTNA());
                            adjList.add(edge);
                        }
                        // Now put the reverse edge in as well
                        final Edge edge2 = new Edge();
                        edge2.name = source.getTNA() + "_to_"
                                + v.endpoint.getTNA();
                        edge2.source = source;
                        edge2.destination = v.endpoint;
                        edge2.weight = 1;
                        if (!this.edgeSet.contains(edge2.name)) {
                            this.edgeSet.put(edge2.name, edge2);
                            // put in adjacency list
                            adjList = this.adjSet.get(source.getTNA());
                            adjList.add(edge2);
                        }
                    } // if
                } // if
            } // while
        }
        if (!this.vertexSet.containsKey(destination.getTNA())) {
            /**
             * First add destination to vertexSet
             */
            final Vertex vertex = new Vertex();
            vertex.name = (destination.getTNA());
            vertex.endpoint = destination;
            vertex.source = dstDomID;
            this.vertexSet.put(destination.getTNA(), vertex);
            /**
             * create adjacency list
             */
            List<Edge> adjList = new ArrayList<Edge>();
            this.adjSet.put(vertex.name, adjList);
            final Enumeration<Vertex> enumer = this.vertexSet.elements();
            while (enumer.hasMoreElements()) {
                final Vertex v = enumer.nextElement();
                if (v.source.equals(dstDomID)) {
                    /**
                     * v and destination are of the same domain
                     */
                    if (!v.endpoint.getTNA().equals(destination.getTNA())) {
                        /**
                         * v and destination are different endpoints
                         */
                        final Edge edge = new Edge();
                        edge.name = v.endpoint.getTNA() + "_to_"
                                + destination.getTNA();
                        edge.source = v.endpoint;
                        edge.destination = destination;
                        edge.weight = 1;
                        if (!this.edgeSet.contains(edge.name)) {
                            this.edgeSet.put(edge.name, edge);
                            // put in adjacency list
                            adjList = this.adjSet.get(v.endpoint.getTNA());
                            adjList.add(edge);
                        }
                        // Now put the reverse edge in as well
                        final Edge edge2 = new Edge();
                        edge2.name = destination.getTNA() + "_to_"
                                + v.endpoint.getTNA();
                        edge2.source = destination;
                        edge2.destination = v.endpoint;
                        edge2.weight = 1;
                        if (!this.edgeSet.contains(edge2.name)) {
                            this.edgeSet.put(edge2.name, edge2);
                            // put in adjacency list
                            adjList = this.adjSet.get(destination.getTNA());
                            adjList.add(edge2);
                        }
                    } // if
                } // if
            } // while
        }

        final ServiceData service = this.getServiceData(serviceId);
        // check if service exists
        if (service == null) {
            throw new InvalidServiceIdException("ID " + serviceId
                    + " not found");
        }

        final Integer id = Integer.valueOf(connectionId);
        // check if connectionId already exists for service ServiceId
        if (service.conn.get(id) != null) {
            throw new InvalidConnectionIdException("ID " + connectionId
                    + " already exists in service " + serviceId);
        }

        // add connection connectionId for service ServiceId
        service.conn.put(id, new ConnData(source, destination));
    }

    /*
     * @see org.opennaas.extensions.idb.reservation.IPathComputer#addService(long,
     *      long, int)
     */
    public void addService(final long startTime, final long endTime,
            final int serviceId) throws InvalidServiceIdException {
        final Integer id = Integer.valueOf(serviceId);
        if (this.services.get(id) != null) {
            throw new InvalidServiceIdException("ID " + serviceId
                    + " already exists");
        }
        this.services.put(id, new ServiceData(startTime, endTime));
    }

    /*
     * @see org.opennaas.extensions.idb.reservation.IPathComputer#computePaths(int)
     */
    public void computePaths(final int serviceId)
            throws PathNotFoundFaultException, InvalidServiceIdException {
        final ServiceData service = this.getServiceData(serviceId);

        /*
         * In case this is not the first call to computePaths, we clear all
         * previously caltulated paths.
         */
        for (final ConnData conn : service.conn.values()) {
            conn.path = null;
        }
        for (final ConnData conn : service.conn.values()) {
            // WE LOOP OVER ALL CONNECTIONS. SO, WE SHOULD PRUNE ALL RESULTS ?
            // MAYBE NOT PRINE BECAUSE OF ROLL BACK!
            final String srcDomID = conn.src.getDomain().getName();
            final String dstDomID = conn.dst.getDomain().getName();

            /*
             * Get the list of pruned endpoints for this connection.
             */
            final Set<String> prunedEndpoints = conn.pruned.endpoints;

            /*
             * Get the list of pruned edges for this connection.
             */
            final Map<String, Set<String>> prunedEdges = conn.pruned.edges;

            conn.path = new LinkedList<Tuple<Endpoint, Endpoint>>();
            if (srcDomID.equals(dstDomID)) {
                // intra-domain ...

                /*
                 * First check if conn.src and conn.dst are pruned.
                 */
                if (prunedEndpoints.contains(conn.src.getTNA())) {
                    /*
                     * Endpoint conn.src was pruned, so no path available
                     */
                    throw new PathNotFoundFaultException(
                            "PathComputer: no path found, source "
                                    + conn.src.getTNA() + " NOT available.");
                }
                if (prunedEndpoints.contains(conn.dst.getTNA())) {
                    /*
                     * Endpoint conn.dst was pruned, so no path available
                     */
                    throw new PathNotFoundFaultException(
                            "PathComputer: no path found, target "
                                    + conn.dst.getTNA() + " NOT available.");
                }
                final Set<String> prunedDst = prunedEdges
                        .get(conn.src.getTNA());
                if ((prunedDst != null)
                        && prunedDst.contains(conn.dst.getTNA())) {
                    throw new PathNotFoundFaultException(
                            "PathComputer: no path found, intradomain path between "
                                    + conn.src.getTNA() + " and "
                                    + conn.dst.getTNA() + " NOT available.");
                }

                /*
                 * We don't check if conn.src and conn.dst are blocked. This is
                 * done more efficiently in addConnection
                 */

                conn.path
                        .add(new Tuple<Endpoint, Endpoint>(conn.src, conn.dst));
            } else {
                /*
                 * inter-domain ...
                 */

                /*
                 * First check if conn.src and conn.dst are pruned.
                 */
                if (prunedEndpoints.contains(conn.src.getTNA())) {
                    /*
                     * Endpoint conn.src was pruned, so no path available
                     */
                    throw new PathNotFoundFaultException(
                            "PathComputer: no path found, source "
                                    + conn.src.getTNA() + " NOT available.");
                }
                if (prunedEndpoints.contains(conn.dst.getTNA())) {
                    /*
                     * Endpoint conn.dst was pruned, so no path available
                     */
                    throw new PathNotFoundFaultException(
                            "PathComputer: no path found, destination "
                                    + conn.dst.getTNA() + " NOT available.");
                }

                /*
                 * We don't check if conn.src and conn.dst are blocked. This is
                 * done more efficiently in addConnection
                 */

                /*
                 * THIS PART IS BASED ON INITIALIZE SINGLE SOURCE FROM
                 * Dijkstra_interfaces_RDF MODULE We initialize with the
                 * destination and calculate the path to the source. This way we
                 * get the path in the right order.
                 */
                if (!this.vertexSet.containsKey(conn.dst.getTNA())) {
                    throw new PathNotFoundFaultException(
                            "PathComputer: source " + conn.dst.getTNA()
                                    + " NOT a proper vertex.");
                } // if
                Enumeration<Vertex> enumer = this.vertexSet.elements();
                while (enumer.hasMoreElements()) {
                    final Vertex v = enumer.nextElement();
                    v.distance = Integer.MAX_VALUE;
                    v.predecessor = "";
                } // while
                final Vertex v = this.vertexSet.get(conn.dst.getTNA());
                v.distance = 0;
                /*
                 * END: THIS PART IS BASED ON INITIALIZE SINGLE SOURCE FROM
                 * Dijkstra_interfaces_RDF MODULE
                 */

                /*
                 * THIS PART IS BASED ON SPATH FROM Dijkstra_interfaces_RDF
                 * MODULE
                 */
                this.binaryHeap = new BinaryHeap();

                enumer = this.vertexSet.elements();
                while (enumer.hasMoreElements()) {
                    final Vertex u = enumer.nextElement();
                    this.binaryHeap.insert(u);
                } // while

                while (!this.binaryHeap.isEmpty()) {
                    /**
                     * Remove vertex u from queue
                     */
                    final Vertex u = (Vertex) this.binaryHeap.extractMin();
                    /**
                     * Check for unconnectedness: if u.distance = infinity then
                     * unconnected graph
                     */
                    if (u.distance == Integer.MAX_VALUE) {
                        throw new PathNotFoundFaultException(
                                "destination vertex part of disconnected graph. Endpoint "
                                        + u.endpoint.getTNA()
                                        + " not connected.");
                    } // if

                    if (u.endpoint.equals(conn.src)) {
                        /**
                         * Vertex u is source.
                         */
                        break;
                    } // if

                    boolean blocked = this.getBlockedEndpoints(serviceId)
                            .contains(u.endpoint.getTNA());
                    boolean threenodesindomain = false;
                    String predecessor = u.predecessor;
                    if (predecessor != "") {
                        Vertex pvertex = this.vertexSet.get(predecessor);
                        predecessor = pvertex.predecessor;
                        if (predecessor != "") {
                            /*
                             * Now check if u and both predecessors are in the
                             * same domain. TODO: later we need to check to
                             * cross each domain only once.
                             */
                            if (u.source.equals(pvertex.source)) {
                                pvertex = this.vertexSet.get(predecessor);
                                if (u.source.equals(pvertex.source)) {
                                    threenodesindomain = true;
                                }
                            }
                        }
                    }

                    boolean isSrcDomain = u.source.equals(srcDomID);
                    if (!prunedEndpoints.contains(u.endpoint.getTNA())
                            && isSrcDomain) {
                        /**
                         * we already are in the source domain and u is not
                         * pruned or the src itself, so we should not select any
                         * Border endpoints anymore
                         */
                        isSrcDomain = true;
                        final List<Edge> adjList = this.adjSet.get(u.endpoint
                                .getTNA());
                        if (adjList != null) {
                            // loop over outgoing edges
                            for (int i = 0; i < adjList.size(); i++) {
                                final Edge edge = adjList.get(i);
                                final String s = edge.source.getTNA();
                                final String d = edge.destination.getTNA();
                                if (!s.equals(u.endpoint.getTNA())) {
                                    throw new PathNotFoundFaultException(
                                            "PathComputer: "
                                                    + u.endpoint.getTNA()
                                                    + " has improper adjacency list ("
                                                    + s + "," + d + ")");
                                } // if
                                int w = edge.weight;
                                /**
                                 * If this edge is pruned, we sould use w =
                                 * Integer.MAX_VALUE here
                                 */

                                /**
                                 * This is quick&dirty only proceed if d equals
                                 * conn.src.getTNA
                                 */
                                if (d.equals(conn.src.getTNA())) {

                                    final Set<String> psrc = prunedEdges.get(s);
                                    if (psrc != null) {
                                        /**
                                         * There there are pruned Edges outgoing
                                         * of s
                                         */
                                        if (psrc.contains(d)) {
                                            /**
                                             * Edge (s,d) is pruned
                                             */
                                            w = Integer.MAX_VALUE;
                                        }
                                    }

                                    try {
                                        if (!this.relax(u.endpoint.getTNA(), d,
                                                w)) {
                                            throw new PathNotFoundFaultException(
                                                    "PathComputer: relax("
                                                            + u.endpoint
                                                                    .getTNA()
                                                            + "," + srcDomID
                                                            + "," + "," + w
                                                            + ") failed");
                                        } // if
                                    } // try
                                    catch (final PathNotFoundFaultException e) {
                                        throw new PathNotFoundFaultException(
                                                "Relaxation step failed.", e);
                                    } // catch
                                }
                            }
                        }
                    }

                    /**
                     * Vertex u is connected, and it is not the source Now check
                     * if u.endpoint is pruned or blocked and if we stay in the
                     * same domain to long.
                     */
                    if (!prunedEndpoints.contains(u.endpoint.getTNA())
                            && !threenodesindomain && !blocked && !isSrcDomain) {

                        /**
                         * Endpoint u.endpoint is not pruned
                         */

                        final List<Edge> adjList = this.adjSet.get(u.endpoint
                                .getTNA());

                        if (adjList != null) {
                            // loop over outgoing edges
                            for (int i = 0; i < adjList.size(); i++) {
                                final Edge edge = adjList.get(i);
                                final String s = edge.source.getTNA();
                                final String d = edge.destination.getTNA();
                                if (!s.equals(u.endpoint.getTNA())) {
                                    throw new PathNotFoundFaultException(
                                            "PathComputer: "
                                                    + u.endpoint.getTNA()
                                                    + " has improper adjacency list ("
                                                    + s + "," + d + ")");
                                } // if
                                int w = edge.weight;
                                /**
                                 * If this edge is pruned, we sould use w =
                                 * Integer.MAX_VALUE here
                                 */
                                final Set<String> psrc = prunedEdges.get(s);
                                if (psrc != null) {
                                    /**
                                     * There there are pruned Edges outgoing of
                                     * s
                                     */
                                    if (psrc.contains(d)) {
                                        /**
                                         * Edge (s,d) is pruned
                                         */
                                        w = Integer.MAX_VALUE;
                                    }
                                }
                                /**
                                 * TODO If this endpoint d is pruned, or blocked
                                 * we sould use w = Integer.MAX_VALUE here MAY
                                 * BY NOT NECESAIRY
                                 */

                                try {
                                    if (!this.relax(u.endpoint.getTNA(), d, w)) {
                                        throw new PathNotFoundFaultException(
                                                "PathComputer: relax("
                                                        + u.endpoint.getTNA()
                                                        + "," + d + "," + ","
                                                        + w + ") failed");
                                    } // if
                                } // try
                                catch (final PathNotFoundFaultException e) {
                                    throw new PathNotFoundFaultException(
                                            "Relaxation step failed.", e);
                                } // catch
                            } // for
                        } // if
                    } // if
                } // while

                /*
                 * END: THIS PART IS BASED ON SPATH FROM Dijkstra_interfaces_RDF
                 * MODULE
                 */

                /*
                 * Note that we calculated the path from destination to source.
                 * So, the last endpoint is the source.
                 */

                // add shortest path
                // Christian: Fixed this to work when input also contains border
                // endpoints
                Vertex pvertex = this.vertexSet.get(conn.src.getTNA());
                if (pvertex == null) {
                    throw new PathNotFoundFaultException(
                            "PathComputer: error while reading back the calculated path, source not found.");
                }
                String currentDomain = pvertex.endpoint.getDomain().getName();
                while (pvertex.predecessor != "") {
                    final Vertex v1 = pvertex;
                    pvertex = this.vertexSet.get(v1.predecessor);
                    final Vertex v2 = pvertex;
                    final String domain = v2.endpoint.getDomain().getName();
                    if (domain.equals(currentDomain)) {
                        conn.path.add(new Tuple<Endpoint, Endpoint>(
                                v1.endpoint, v2.endpoint));
                        if (v2.predecessor != "") {
                            pvertex = this.vertexSet.get(v2.predecessor);
                            currentDomain = pvertex.endpoint.getDomain()
                                    .getName();
                        }
                    } else {
                        pvertex = v2;
                        currentDomain = domain;
                    }
                }

            }
        }
    }

    /**
     * Create link set....
     */
    void createLinkSet(final BinaryHeap L, final List<String> p) {
        for (int i = 0; i < p.size(); i++) {
            final String ename = p.get(i);
            final Edge edge = this.edgeSet.get(ename);
            if (edge != null) {
                L.insert(edge);
            }
        }
    } // createLinkSet

    /**
     * Retrieve list of endpoints blocked by other connections.
     * 
     * @param serviceId
     *            Consider endpoints blocked in those services that are
     *            overlapping in time with the service identified by this ID.
     * @return List of blocked endpoints.
     * @throws InvalidServiceIdException
     *             Thrown if no service with the given ID has been previously
     *             added.
     */
    protected Set<String> getBlockedEndpoints(final int serviceId)
            throws InvalidServiceIdException {
        final Set<String> blocked = new HashSet<String>();
        final ServiceData o = this.getServiceData(serviceId);
        final Integer boxedId = new Integer(serviceId);
        for (final Integer id : this.services.keySet()) {
            final ServiceData s = this.services.get(id);
            if (id.equals(boxedId) || o.overlapsWith(s)) {
                for (final ConnData c : s.conn.values()) {
                    if (c.path != null) {
                        /*
                         * The path is already calculated and should be blocked.
                         */
                        for (final Tuple<Endpoint, Endpoint> p : c.path) {
                            blocked.add(p.getFirstElement().getTNA());
                            blocked.add(p.getSecondElement().getTNA());
                        }
                    } else {
                        /*
                         * No path is calculated yet, but src and dst should be
                         * blocked
                         */
                        blocked.add(c.src.getTNA());
                        blocked.add(c.dst.getTNA());
                    }

                }
            }
        }
        return blocked;
    }

    /**
     * Look up the data for a specific connection.
     * 
     * @param serviceId
     *            Service ID.
     * @param connectionId
     *            Connection ID.
     * @return ConnData object for the given service ID / connection ID pair.
     * @throws InvalidServiceIdException
     *             Thrown if no service with this ID has been previously added.
     * @throws InvalidConnectionIdException
     *             Thrown if no connection with this ID pair has been previously
     *             added.
     */
    protected ConnData getConnectionData(final int serviceId,
            final int connectionId) throws InvalidServiceIdException,
            InvalidConnectionIdException {
        final ServiceData service = this.getServiceData(serviceId);
        final ConnData conn = service.conn.get(Integer.valueOf(connectionId));
        if (conn == null) {
            throw new InvalidConnectionIdException("ID " + connectionId
                    + " not found");
        }
        return conn;
    }

    /*
     * @see org.opennaas.extensions.idb.reservation.IPathComputer#getPath(int, int)
     */
    public List<Tuple<Endpoint, Endpoint>> getPath(final int serviceId,
            final int connectionId) throws InvalidServiceIdException,
            InvalidConnectionIdException {
        final ConnData conn = this.getConnectionData(serviceId, connectionId);

        return conn.path;
    }

    /**
     * Look up the data for a specific service.
     * 
     * @param serviceId
     *            Service ID.
     * @return ServiceData object with the given service ID.
     * @throws InvalidServiceIdException
     *             Thrown if no service with the given ID has been previously
     *             added.
     */
    protected ServiceData getServiceData(final int serviceId)
            throws InvalidServiceIdException {
        final ServiceData service = this.services.get(Integer
                .valueOf(serviceId));
        if (service == null) {
            throw new InvalidServiceIdException("ID " + serviceId
                    + " not found");
        }
        return service;
    }

    public void pruneEdge(final int serviceId, final int connectionId,
            final Endpoint src, final Endpoint dst)
            throws InvalidServiceIdException, InvalidConnectionIdException {
        final ConnData conn = this.getConnectionData(serviceId, connectionId);

        final Map<String, Set<String>> prunedEdges = conn.pruned.edges;

        final String srcTNA = src.getTNA();
        final String dstTNA = dst.getTNA();

        Set<String> pdst = prunedEdges.get(srcTNA);
        if (pdst == null) {
            pdst = new HashSet<String>();
            prunedEdges.put(srcTNA, pdst);
        }
        if (!pdst.contains(dstTNA)) {
            pdst.add(dstTNA);
        }

        pdst = prunedEdges.get(dstTNA);
        if (pdst == null) {
            pdst = new HashSet<String>();
            prunedEdges.put(dstTNA, pdst);
        }
        if (!pdst.contains(srcTNA)) {
            pdst.add(srcTNA);
        }
    }

    public void pruneEndpoint(final int serviceId, final int connectionId,
            final Endpoint endpoint) throws InvalidServiceIdException,
            InvalidConnectionIdException {
        final ConnData conn = this.getConnectionData(serviceId, connectionId);

        final Set<String> prunedEndpoints = conn.pruned.endpoints;

        if (!prunedEndpoints.contains(endpoint.getTNA())) {
            prunedEndpoints.add(endpoint.getTNA());
        }
    }

    /**
     * Relaxation step of Dijkstra algorithm
     */
    public boolean relax(final String u, final String v, final int w)
            throws PathNotFoundFaultException {
        final Vertex uu = this.vertexSet.get(u);
        if (uu == null) {
            throw new PathNotFoundFaultException("PathComputer: vertex " + u
                    + " NOT in vertex set");
        }// if
        final Vertex vv = this.vertexSet.get(v);
        if (vv == null) {
            throw new PathNotFoundFaultException("PathComputer: vertex " + v
                    + " NOT in vertex set");
        }// if
        final int du = uu.distance;
        final int dv = vv.distance;

        if ((w < Integer.MAX_VALUE) && (dv > du + w)) {
            /**
             * We check for Integer.MAX_VALUE to prevent an overflow
             */

            final int index = this.binaryHeap.getIndex(vv);
            if (index >= 0) {
                vv.predecessor = u;
                this.binaryHeap.decreaseKey(index, du + w);
            } // if
            else {
                throw new PathNotFoundFaultException("PathComputer: " + u
                        + "(d=" + du + ")," + v + "(d=" + dv + ")," + w
                        + " failed, " + v + " not in queue (edge weight = " + w
                        + ")");
            } // else
        } // if

        return true;
    } // relax
}
