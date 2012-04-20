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
 * Project: IST Phosphorus Harmony System.
 * Module: 
 * Description: 
 *
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 * @version $Id: TestTopologyWS.java 4906 2009-08-20 15:46:29Z carlos.baez@i2cat.net $
 *
 */
package org.opennaas.extension.idb.da.argon.webservice.test;

import java.util.List;

import org.apache.muse.ws.addressing.soap.SoapFault;
import org.junit.Assert;
import org.junit.Test;

import org.opennaas.extension.idb.da.argon.Constants;
import org.opennaas.extension.idb.da.argon.webservice.TopologyWS;
import org.opennaas.extension.idb.serviceinterface.databinding.jaxb.DomainInformationType;
import org.opennaas.extension.idb.serviceinterface.databinding.jaxb.exceptions.OperationNotSupportedFaultException;
import org.opennaas.extension.idb.serviceinterface.topology.SimpleTopologyClient;
import org.opennaas.core.utils.Config;

/**
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 * @version $Id: TestTopologyWS.java 4906 2009-08-20 15:46:29Z carlos.baez@i2cat.net $
 * 
 */
public class TestTopologyWS {

    /**
     * A simple topology client.
     */
    private final SimpleTopologyClient client;

    /**
     * The public constructor.
     */
    public TestTopologyWS() {
        super();
        if (Config.isTrue(Constants.testProperties, "test.callWebservice")) {
            final String epr = Config.getString(Constants.testProperties,
                    "test.topologyEPR");
            this.client = new SimpleTopologyClient(epr);
        } else {
            this.client = new SimpleTopologyClient(new TopologyWS());
        }
    }

    @Test
    public void testGetDomains() throws SoapFault {
        final List<DomainInformationType> domains = this.client.getDomains().getDomains();
        Assert.assertEquals("Shoudl not contain a sub domain.", 0, domains.size());        
    }

    @Test(expected=OperationNotSupportedFaultException.class)
    public void testUnsupportedOperation() throws SoapFault {
        this.client.addOrEditDomain("", "", "10.0.0.0/16");
        Assert.fail("Should not be supported!");
    }

}