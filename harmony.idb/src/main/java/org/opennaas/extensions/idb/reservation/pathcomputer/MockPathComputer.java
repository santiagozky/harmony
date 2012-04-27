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

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointInterfaceType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.EndpointNotFoundFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.PathNotFoundFaultException;
import org.opennaas.extensions.idb.database.hibernate.Domain;
import org.opennaas.extensions.idb.database.hibernate.Endpoint;
import org.opennaas.extensions.idb.database.hibernate.TNAPrefix;
import org.opennaas.extensions.idb.exception.database.DatabaseException;
import org.opennaas.extensions.idb.reservation.exceptions.InvalidConnectionIdException;
import org.opennaas.extensions.idb.reservation.exceptions.InvalidServiceIdException;
import org.opennaas.core.utils.Tuple;

/**
 * Basic path computer mock that implements the new interface. For additional
 * functionality, it is probably useful to implement child classes.
 */
public class MockPathComputer implements IPathComputer {
    /**
     * Class used to store data about connections.
     */
    static class ConnData {
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
    static class PruneData {
        public final Set<String> endpoints = new HashSet<String>();
        public final Map<String, Set<String>> edges = new HashMap<String, Set<String>>();
    }

    /**
     * Class used to store data about services added to this PC instance.
     */
    static class ServiceData {
        public final long startTime;
        public final long endTime;
        public final HashMap<Integer, ConnData> conn = new HashMap<Integer, ConnData>();

        public ServiceData(final long startTime, final long endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }

        public boolean overlapsWith(final ServiceData s) {
            if (this.startTime > s.startTime) {
                return this.startTime <= s.endTime;
            }
            // s.startTime >= this.startTime
            return s.startTime <= this.endTime;
        }
    }

    protected static Endpoint generateDummyEndpoint(final String domainName,
            final int bandwidth) throws DatabaseException {
        // get prefixes for domain
        final Set<TNAPrefix> prefList = Domain.load(domainName).getPrefixes();
        // generate new E-NNI with first prefix
        final String[] parts = prefList.iterator().next().getPrefix().split(
                "\\.");
        final String newEndPtID = parts[0] + "." + parts[1] + "." + parts[2]
                + "." + "255";
        final Endpoint end = new Endpoint();
        end.setTNA(newEndPtID);
        end.setName("CreateReservationTestENNI1");
        end.setDescription("Helper E-NNI for CreateReservationTest");
        end.setDomain(Domain.load(domainName));
        end.setBandwidth(bandwidth);
        end.setType(EndpointInterfaceType.BORDER.ordinal());
        end.save();
        return end;
    }

    protected static Endpoint getBorderEndpoint(final String domainName)
            throws DatabaseException {
        final Domain domain = Domain.load(domainName);
        for (final Endpoint e : domain.getEndpoints()) {
            if (e.getType() == EndpointInterfaceType.BORDER.ordinal()) {
                return e;
            }
        }
        return MockPathComputer.generateDummyEndpoint(domainName, 1000);
    }

    /**
     * All services added to this PC instance so far.
     */
    protected HashMap<Integer, ServiceData> services = new HashMap<Integer, ServiceData>();

    /*
     * (non-Javadoc)
     * 
     * @see org.opennaas.extensions.idb.reservation.IPathComputerV2#addConnection(org.opennaas.extensions.idb.database.hibernate.Endpoint,
     *      org.opennaas.extensions.idb.database.hibernate.Endpoint, int, int)
     */
    public void addConnection(final Endpoint source,
            final Endpoint destination, final int serviceId,
            final int connectionId) throws EndpointNotFoundFaultException,
            DatabaseException, InvalidServiceIdException,
            InvalidConnectionIdException {

        // make sure that we know the endpoints' domains
        final String srcDomID = source.getDomain().getName();
        final String dstDomID = destination.getDomain().getName();
        if (srcDomID == null) {
            throw new EndpointNotFoundFaultException(source.getTNA());
        }
        if (dstDomID == null) {
            throw new EndpointNotFoundFaultException(destination.getTNA());
        }

        final ServiceData service = this.getServiceData(serviceId);
        final Integer id = Integer.valueOf(connectionId);
        if (service.conn.get(id) != null) {
            throw new InvalidConnectionIdException("ID " + connectionId
                    + " already exists in service " + serviceId);
        }
        service.conn.put(id, new ConnData(source, destination));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.opennaas.extensions.idb.reservation.IPathComputerV2#addService(long,
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
     * (non-Javadoc)
     * 
     * @see org.opennaas.extensions.idb.reservation.IPathComputerV2#computePaths(int)
     */
    public void computePaths(final int serviceId)
            throws PathNotFoundFaultException, InvalidServiceIdException {
        final ServiceData service = this.getServiceData(serviceId);
        for (final ConnData conn : service.conn.values()) {
            final String srcDomID = conn.src.getDomain().getName();
            final String dstDomID = conn.dst.getDomain().getName();

            if (conn.pruned.endpoints.contains(conn.src.getTNA())
                    || conn.pruned.endpoints.contains(conn.dst.getTNA())) {
                throw new PathNotFoundFaultException(
                        "src or dst endpoint is blocked");
            }

            conn.path = new LinkedList<Tuple<Endpoint, Endpoint>>();
            if (srcDomID.equals(dstDomID)) {
                // intra-domain ...

                final Set<String> prunedEdges = conn.pruned.edges.get(conn.src
                        .getTNA());
                if ((prunedEdges != null)
                        && prunedEdges.contains(conn.dst.getTNA())) {
                    throw new PathNotFoundFaultException(
                            "no intra-domain path from " + conn.src.getTNA()
                                    + " to " + conn.dst.getTNA());
                }

                conn.path
                        .add(new Tuple<Endpoint, Endpoint>(conn.src, conn.dst));
            } else {
                // inter-domain ...
                try {
                    conn.path.add(new Tuple<Endpoint, Endpoint>(conn.src,
                            MockPathComputer.getBorderEndpoint(srcDomID)));
                    conn.path.add(new Tuple<Endpoint, Endpoint>(
                            MockPathComputer.getBorderEndpoint(dstDomID),
                            conn.dst));
                } catch (final DatabaseException e) {
                    throw new RuntimeException(
                            "MockPathComputer.getBorderEndpoint threw: "
                                    + e.toString());
                }
            }
        }
    }

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
                        for (final Tuple<Endpoint, Endpoint> p : c.path) {
                            blocked.add(p.getFirstElement().getTNA());
                            blocked.add(p.getSecondElement().getTNA());
                        }
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
     * (non-Javadoc)
     * 
     * @see org.opennaas.extensions.idb.reservation.IPathComputerV2#getPath(int, int)
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
            throws EndpointNotFoundFaultException, InvalidServiceIdException,
            InvalidConnectionIdException {
        final ConnData conn = this.getConnectionData(serviceId, connectionId);

        final Map<String, Set<String>> prunedEdges = conn.pruned.edges;

        final String srcTNA = src.getTNA();
        final String dstTNA = dst.getTNA();
        Set<String> pdst = prunedEdges.get(srcTNA);
        if (pdst == null) {
            pdst = new HashSet<String>();
            prunedEdges.put(srcTNA, pdst);
        }
        pdst.add(dstTNA);
    }

    public void pruneEndpoint(final int serviceId, final int connectionId,
            final Endpoint endpoint) throws EndpointNotFoundFaultException,
            InvalidServiceIdException, InvalidConnectionIdException {
        final ConnData conn = this.getConnectionData(serviceId, connectionId);

        final Set<String> prunedEndpoints = conn.pruned.endpoints;

        prunedEndpoints.add(endpoint.getTNA());
    }
}
