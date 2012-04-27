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


package org.opennaas.extensions.idb.da.argon.handler;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetDomainsResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetDomainsType;
import org.opennaas.extensions.idb.serviceinterface.topology.CommonTopologyHandler;

/**
 * Class to handle NSP topology-requests in a predictable manner.
 */
public final class TopologyHandler extends CommonTopologyHandler {
    /** Singleton Instance. */
    private static TopologyHandler selfInstance = null;

    /**
     * Instance getter.
     * 
     * @return Singleton Instance
     */
    public static TopologyHandler getInstance() {
        if (TopologyHandler.selfInstance == null) {
            TopologyHandler.selfInstance = new TopologyHandler();
        }
        return TopologyHandler.selfInstance;
    }

    /**
     * Private constructor: Singleton.
     */
    private TopologyHandler() {
        super();
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

    /*
     * Handler
     * =========================================================================
     */

    /**
     * GetDomains Handler.
     * <p>
     * Handler will accept GetDomains-Requests and return a GetDomain-Response
     * containing a list of DomainInformationTypes with only one element built
     * as follows: <br>
     * DomainID -> ArgonDomain <br>
     * Description -> ArgonDomain <br>
     * ReservationEPR -> Argon.reservation.epr <br>
     * TopologyEPR -> Argon.topology.epr <br>
     * TNAPrefixList = List with one TNAPrefix: <br> -> 0.0.0.0/32 <br>
     * <p>
     * 
     * @param element
     *            GetDomains Request
     * @return GetDomains Response
     */
    @Override
    public GetDomainsResponseType getDomains(final GetDomainsType element) {
        return new GetDomainsResponseType();
    }
}
