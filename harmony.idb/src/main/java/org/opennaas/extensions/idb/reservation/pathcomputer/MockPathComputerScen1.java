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


/**
 *
 */
package org.opennaas.extensions.idb.reservation.pathcomputer;

import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.PathNotFoundFaultException;
import org.opennaas.extensions.idb.database.hibernate.Endpoint;
import org.opennaas.extensions.idb.exception.database.DatabaseException;
import org.opennaas.extensions.idb.reservation.exceptions.InvalidServiceIdException;
import org.opennaas.core.utils.Tuple;

/**
 * @author dewaal
 * 
 */
public class MockPathComputerScen1 extends MockPathComputer {
    @Override
    public void computePaths(final int serviceId)
            throws PathNotFoundFaultException, InvalidServiceIdException {
        final ServiceData service = this.getServiceData(serviceId);
        boolean blockedT1l1 = false;
        boolean blockedT1l2 = false;
        boolean blockedT2l1 = false;
        boolean blockedT2l2 = false;
        boolean blockedT3l1 = false;
        boolean blockedT3l2 = false;
        for (final ConnData conn : service.conn.values()) {
            final Map<String, Set<String>> prunedEdges = conn.pruned.edges;
            final boolean prunedT1l1 = prunedEdges.containsKey("192.170.0.1")
                    && prunedEdges.get("192.170.0.1").contains("192.170.0.3");
            final boolean prunedT1l2 = prunedEdges.containsKey("192.170.0.2")
                    && prunedEdges.get("192.170.0.2").contains("192.170.0.4");
            final boolean prunedT2l1 = prunedEdges.containsKey("192.171.0.1")
                    && prunedEdges.get("192.171.0.1").contains("192.171.0.3");
            final boolean prunedT2l2 = prunedEdges.containsKey("192.171.0.2")
                    && prunedEdges.get("192.171.0.2").contains("192.171.0.4");
            final boolean prunedT3l1 = prunedEdges.containsKey("192.172.0.1")
                    && prunedEdges.get("192.172.0.1").contains("192.172.0.3");
            final boolean prunedT3l2 = prunedEdges.containsKey("192.172.0.2")
                    && prunedEdges.get("192.172.0.2").contains("192.172.0.4");
            Endpoint t1, t2, t3, t4;
            try {
                if (!(prunedT1l1 || blockedT1l1)) {
                    t1 = Endpoint.load("192.168.0.1");
                    t2 = Endpoint.load("192.170.0.1");
                    t3 = Endpoint.load("192.170.0.3");
                    t4 = Endpoint.load("192.169.0.1");
                    blockedT1l1 = true;
                } else if (!(prunedT1l2 || blockedT1l2)) {
                    t1 = Endpoint.load("192.168.0.2");
                    t2 = Endpoint.load("192.170.0.2");
                    t3 = Endpoint.load("192.170.0.4");
                    t4 = Endpoint.load("192.169.0.2");
                    blockedT1l2 = true;
                } else if (!(prunedT2l1 || blockedT2l1)) {
                    t1 = Endpoint.load("192.168.0.3");
                    t2 = Endpoint.load("192.171.0.1");
                    t3 = Endpoint.load("192.171.0.3");
                    t4 = Endpoint.load("192.169.0.3");
                    blockedT2l1 = true;
                } else if (!(prunedT2l2 || blockedT2l2)) {
                    t1 = Endpoint.load("192.168.0.4");
                    t2 = Endpoint.load("192.171.0.2");
                    t3 = Endpoint.load("192.171.0.4");
                    t4 = Endpoint.load("192.169.0.4");
                    blockedT2l2 = true;
                } else if (!(prunedT3l1 || blockedT3l1)) {
                    t1 = Endpoint.load("192.168.0.5");
                    t2 = Endpoint.load("192.172.0.1");
                    t3 = Endpoint.load("192.172.0.3");
                    t4 = Endpoint.load("192.169.0.5");
                    blockedT3l1 = true;
                } else if (!(prunedT3l2 || blockedT3l2)) {
                    t1 = Endpoint.load("192.168.0.6");
                    t2 = Endpoint.load("192.172.0.2");
                    t3 = Endpoint.load("192.172.0.4");
                    t4 = Endpoint.load("192.169.0.6");
                    blockedT3l2 = true;
                } else {
                    throw new PathNotFoundFaultException(); // TODO message
                }
            } catch (final DatabaseException e) {
                throw new InvalidServiceIdException("WILD HACK, actually a "
                        + e);
            }
            conn.path = new LinkedList<Tuple<Endpoint, Endpoint>>();
            conn.path.add(new Tuple<Endpoint, Endpoint>(conn.src, t1));
            conn.path.add(new Tuple<Endpoint, Endpoint>(t2, t3));
            conn.path.add(new Tuple<Endpoint, Endpoint>(t4, conn.dst));
        }
    }
}
