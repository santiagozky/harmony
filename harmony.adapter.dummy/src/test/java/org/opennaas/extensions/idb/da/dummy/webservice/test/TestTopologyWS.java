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
 * Project: IST Phosphorus Harmony System. Module: Description:
 * 
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 * @version $Id: TestTopologyWS.java 4681 2009-01-21 19:42:02Z
 *          carlos.baez@i2cat.net $
 */
package org.opennaas.extensions.idb.da.dummy.webservice.test;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.muse.ws.addressing.soap.SoapFault;
import org.junit.Assert;
import org.junit.Test;

import org.opennaas.extensions.idb.da.dummy.webservice.TopologyWS;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetDomainsResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetEndpointsResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetLinksResponseType;
import org.opennaas.extensions.idb.serviceinterface.topology.SimpleTopologyClient;
import org.opennaas.core.utils.Config;

/**
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 * @version $Id: TestTopologyWS.java 4681 2009-01-21 19:42:02Z
 *          carlos.baez@i2cat.net $
 */
public class TestTopologyWS {

    /**
     * A simple Topology client.
     */
    private final SimpleTopologyClient client;

    /**
     * The public constructor.
     */
    public TestTopologyWS() {
        if (Config.isTrue("test", "test.callWebservice")) {
            final String epr = Config.getString("test", "test.topologyEPR");
            this.client = new SimpleTopologyClient(epr);
        } else {
            this.client = new SimpleTopologyClient(new TopologyWS());
        }
    }

    /**
     * Test method for the most important task: create, getStatus, and cancel.
     * 
     * @throws SoapFault
     *             If an error occurs within the Webservice.
     * @throws DatatypeConfigurationException
     *             Unknown.
     */
    @Test
    public final void testGetDomainInformation() throws SoapFault,
            DatatypeConfigurationException {

        final GetDomainsResponseType domains = this.client.getDomains();
        Assert.assertFalse("Shouldn't return domains", domains.isSetDomains());

        final GetEndpointsResponseType endpoints =
                this.client.getEndpoints(Config.getString("hsiDummy",
                        "domain.name"));
        Assert.assertTrue("Should contain endpoints", endpoints
                .isSetEndpoints());

        final GetLinksResponseType links =
                this.client.getLinks(Config.getString("hsiDummy",
                        "domain.name"));
        Assert.assertTrue("Should contain links", links.isSetLink());

    }
}
