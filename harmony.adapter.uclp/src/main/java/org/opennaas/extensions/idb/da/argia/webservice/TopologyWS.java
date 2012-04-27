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
package org.opennaas.extensions.idb.da.argia.webservice;

import org.w3c.dom.Element;
import org.apache.muse.core.AbstractCapability;

import org.opennaas.extensions.idb.da.argia.handler.TopologyHandler;
import org.opennaas.extensions.idb.serviceinterface.topology.ITopologyWS;

/**
 * @author jangassen
 *
 */
public class TopologyWS extends AbstractCapability implements ITopologyWS {

    /**
     * getDomains Handler.
     *
     * @param getDomains Request
     * @return getDomains Response
     * @throws Exception In case of errors
     */
    public Element getDomains (final Element getDomains)
             throws Exception {
        return TopologyHandler.getInstance().handle("getDomains", getDomains);
    }

    /**
     * addOrEditDomain Handler.
     *
     * @param addOrEditDomain Request
     * @return addOrEditDomain Response
     * @throws Exception In case of errors
     */
    public Element addOrEditDomain (final Element addOrEditDomain)
             throws Exception {
        return TopologyHandler.getInstance().handle("addOrEditDomain", addOrEditDomain);
    }

    /**
     * getLinks Handler.
     *
     * @param getLinks Request
     * @return getLinks Response
     * @throws Exception In case of errors
     */
    public Element getLinks (final Element getLinks)
             throws Exception {
        return TopologyHandler.getInstance().handle("getLinks", getLinks);
    }

    /**
     * getEndpoints Handler.
     *
     * @param getEndpoints Request
     * @return getEndpoints Response
     * @throws Exception In case of errors
     */
    public Element getEndpoints (final Element getEndpoints)
             throws Exception {
        return TopologyHandler.getInstance().handle("getEndpoints", getEndpoints);
    }

    /**
     * deleteLink Handler.
     *
     * @param deleteLink Request
     * @return deleteLink Response
     * @throws Exception In case of errors
     */
    public Element deleteLink (final Element deleteLink)
             throws Exception {
        return TopologyHandler.getInstance().handle("deleteLink", deleteLink);
    }

    /**
     * addEndpoint Handler.
     *
     * @param addEndpoint Request
     * @return addEndpoint Response
     * @throws Exception In case of errors
     */
    public Element addEndpoint (final Element addEndpoint)
             throws Exception {
        return TopologyHandler.getInstance().handle("addEndpoint", addEndpoint);
    }

    /**
     * addDomain Handler.
     *
     * @param addDomain Request
     * @return addDomain Response
     * @throws Exception In case of errors
     */
    public Element addDomain (final Element addDomain)
             throws Exception {
        return TopologyHandler.getInstance().handle("addDomain", addDomain);
    }

    /**
     * editDomain Handler.
     *
     * @param editDomain Request
     * @return editDomain Response
     * @throws Exception In case of errors
     */
    public Element editDomain (final Element editDomain)
             throws Exception {
        return TopologyHandler.getInstance().handle("editDomain", editDomain);
    }

    /**
     * deleteDomain Handler.
     *
     * @param deleteDomain Request
     * @return deleteDomain Response
     * @throws Exception In case of errors
     */
    public Element deleteDomain (final Element deleteDomain)
             throws Exception {
        return TopologyHandler.getInstance().handle("deleteDomain", deleteDomain);
    }

    /**
     * editEndpoint Handler.
     *
     * @param editEndpoint Request
     * @return editEndpoint Response
     * @throws Exception In case of errors
     */
    public Element editEndpoint (final Element editEndpoint)
             throws Exception {
        return TopologyHandler.getInstance().handle("editEndpoint", editEndpoint);
    }

    /**
     * editLink Handler.
     *
     * @param editLink Request
     * @return editLink Response
     * @throws Exception In case of errors
     */
    public Element editLink (final Element editLink)
             throws Exception {
        return TopologyHandler.getInstance().handle("editLink", editLink);
    }

    /**
     * addLink Handler.
     *
     * @param addLink Request
     * @return addLink Response
     * @throws Exception In case of errors
     */
    public Element addLink (final Element addLink)
             throws Exception {
        return TopologyHandler.getInstance().handle("addLink", addLink);
    }

    /**
     * deleteEndpoint Handler.
     *
     * @param deleteEndpoint Request
     * @return deleteEndpoint Response
     * @throws Exception In case of errors
     */
    public Element deleteEndpoint (final Element deleteEndpoint)
             throws Exception {
        return TopologyHandler.getInstance().handle("deleteEndpoint", deleteEndpoint);
    }

}
