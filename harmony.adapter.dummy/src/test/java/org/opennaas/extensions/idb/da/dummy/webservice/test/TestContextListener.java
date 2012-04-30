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
 * @version $Id$
 */
package org.opennaas.extensions.idb.da.dummy.webservice.test;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import org.opennaas.extensions.idb.da.dummy.webservice.ContextListener;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DomainInformationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointType;
import org.opennaas.core.utils.FileHelper;

/**
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 * @version $Id$
 */
public class TestContextListener extends ContextListener {

    /**
     * Test method for the interdomain property file.
     */
    @Test
    public final void testGetInterdomainPropertyFile() {
        final String filename =
                "resources/properties/" + this.getInterdomainPropertyFile()
                        + ".properties";
        Assert.assertTrue("Should return a valid property file", FileHelper
                .fileExists(filename));
    }

    /**
     * Test method for endpoint information.
     */
    @Test
    public final void testGetEndpoints() {
        final List<EndpointType> endpoints = this.getEndpoints();
        Assert.assertTrue("Should contain some endpoints", !endpoints
                .isEmpty());
    }

    /**
     * Test method for the logger.
     */
    @Test
    public final void testGetLogger() {
        final Logger logger = this.getLogger();
        Assert.assertTrue("Should contain a logger", logger != null);
    }

    /**
     * Test method for domain information.
     */
    @Test
    public final void testGetDomainInformation() {
        final DomainInformationType info = this.getDomainInformation();
        Assert.assertTrue("Should contain domain information", info
                .getDomainId().length() > 0);
    }

}
