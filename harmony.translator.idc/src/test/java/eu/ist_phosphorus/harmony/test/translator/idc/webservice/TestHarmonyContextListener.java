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
 * @version $Id: TestHarmonyContextListener.java 4747 2009-03-04 18:25:39Z
 *          willner@cs.uni-bonn.de $
 */
package eu.ist_phosphorus.harmony.test.translator.idc.webservice;

import java.util.List;

import org.apache.axis2.AxisFault;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.DomainInformationType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EndpointType;
import eu.ist_phosphorus.harmony.common.utils.FileHelper;
import eu.ist_phosphorus.harmony.translator.idc.webservice.ContextListener;

/**
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 * @version $Id: TestHarmonyContextListener.java 3996 2008-08-07 09:09:20Z
 *          willner@cs.uni-bonn.de $
 */
public class TestHarmonyContextListener extends ContextListener {

    public TestHarmonyContextListener() throws AxisFault {
        super();
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

    /**
     * Test method for endpoint information.
     */
    @Test
    public final void testGetEndpoints() {
        final List<EndpointType> endpoints = this.getEndpoints();
        Assert
                .assertTrue("Should contain some endpoints", !endpoints
                        .isEmpty());
    }

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
     * Test method for the logger.
     */
    @Test
    public final void testGetLogger() {
        final Logger logger = this.getLogger();
        Assert.assertTrue("Should contain a logger", logger != null);
    }

    public final void testStartup() {
        this.shutdown();
        Assert.assertTrue(this.startup());
    }
}
