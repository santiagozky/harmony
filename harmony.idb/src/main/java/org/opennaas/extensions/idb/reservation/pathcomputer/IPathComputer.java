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

import java.util.List;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.EndpointNotFoundFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.PathNotFoundFaultException;
import org.opennaas.extensions.idb.database.hibernate.Endpoint;
import org.opennaas.extensions.idb.exception.database.DatabaseException;
import org.opennaas.extensions.idb.reservation.exceptions.InvalidConnectionIdException;
import org.opennaas.extensions.idb.reservation.exceptions.InvalidServiceIdException;
import org.opennaas.core.utils.Tuple;

/**
 * Todo: Describe.
 * 
 * @author Christian de Waal (dewaal@cs.uni-bonn.de)
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 */
public interface IPathComputer {

    /**
     * Add a connection to the path computer's state.
     * 
     * @param source
     *            The connection's source endpoint.
     * @param destination
     *            The connection's destination endpoint.
     * @param serviceId
     *            ID of the service this connection belongs to.
     * @param connectionId
     *            Connection ID.
     * @throws EndpointNotFoundFaultException
     * @throws DatabaseException
     * @throws InvalidServiceIdException
     *             If the service ID was not previously registered.
     * @throws InvalidConnectionIdException
     *             If a connection with the same service ID / connection ID
     *             combination was previously registered.
     */
    void addConnection(Endpoint source, Endpoint destination, int serviceId,
            int connectionId) throws EndpointNotFoundFaultException,
            DatabaseException, InvalidServiceIdException,
            InvalidConnectionIdException;

    /**
     * Add a service to the path computer's state. The start and end times can
     * be given in arbitrary time units since they are only used to calculate
     * which services overlap in time and which do not.
     * 
     * @param startTime
     *            Service's start time.
     * @param endTime
     *            Service's end time.
     * @param serviceId
     *            Service ID.
     * @throws InvalidServiceIdException
     *             If a service with the same ID was previously registered.
     */
    void addService(long startTime, long endTime, int serviceId)
            throws InvalidServiceIdException;

    /**
     * Compute all paths for a specific service.
     * 
     * @param serviceId
     * @throws PathNotFoundFaultException
     * @throws InvalidServiceIdException
     *             If the service ID was not previously registered.
     */
    void computePaths(int serviceId) throws PathNotFoundFaultException,
            InvalidServiceIdException;

    /**
     * Get shortest path for a certain connection.
     * 
     * @param serviceId
     * @param connectionId
     * @throws InvalidServiceIdException
     *             If the service ID was not previously registered.
     * @throws InvalidConnectionIdException
     *             If no connection with this ID was previously registered for
     *             the given service ID.
     * @return ...
     */
    List<Tuple<Endpoint, Endpoint>> getPath(int serviceId, int connectionId)
            throws InvalidServiceIdException, InvalidConnectionIdException;

    /**
     * Remove an intra-domain edge from the internal topology graph of this path
     * computer.
     * 
     * @param serviceId
     * @param connectionId
     * @param src
     * @param dst
     * @throws InvalidServiceIdException
     *             If the service ID was not previously registered.
     * @throws InvalidConnectionIdException
     *             If no connection with this ID was previously registered for
     *             the given service ID.
     */
    void pruneEdge(int serviceId, int connectionId, Endpoint src, Endpoint dst)
            throws EndpointNotFoundFaultException, InvalidServiceIdException,
            InvalidConnectionIdException;

    /**
     * Prune endpoint from the internal topology graph of this path computer.
     * 
     * @param serviceId
     * @param connectionId
     * @param endpoint
     *            Endpoint to be removed.
     * @throws EndpointNotFoundFaultException
     *             Is thrown if a given endpoint does not exist.
     * @throws InvalidServiceIdException
     *             If the service ID was not previously registered.
     * @throws InvalidConnectionIdException
     *             If no connection with this ID was previously registered for
     *             the given service ID.
     */
    void pruneEndpoint(int serviceId, int connectionId, Endpoint endpoint)
            throws EndpointNotFoundFaultException, InvalidServiceIdException,
            InvalidConnectionIdException;
}
