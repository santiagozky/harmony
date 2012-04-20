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


package org.opennaas.core.utils.test;

import java.net.UnknownHostException;

import org.junit.Assert;
import org.junit.Test;
import org.xbill.DNS.TextParseException;

import org.opennaas.core.utils.TNAHelper;

/**
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 * 
 */
public class TestTNAHelper {

    /**
     * Test method for
     * {@link org.opennaas.core.utils.TNAHelper#resolve(java.lang.String)}.
     * 
     * @throws UnknownHostException
     * @throws TextParseException
     */
	//TODO: add database to resolve addresses to pass test
  //  @Test
    public void testResolve() throws TextParseException, UnknownHostException {
        final String expected = "10.3.1.9";
        Assert.assertEquals("IP should be resolved", expected, TNAHelper
                .resolve("10.0.0.3"));
        Assert.assertEquals("FQDN should be resolved", expected, TNAHelper
                .resolve("amy.i2cat.net.viola-testbed.de"));
        Assert.assertEquals("Short name should be resolved", expected,
                TNAHelper.resolve("amy.i2cat.net"));
        Assert.assertEquals("Local name should be resolved", expected,
                TNAHelper.resolve("amy"));
        Assert.assertEquals("TNA should remain TNA", "128.0.0.1",
                TNAHelper.resolve("128.0.0.1"));
    }

    @Test
    public void testIsName() {
        Assert.assertTrue("Is a name", TNAHelper.isName("amy.i2cat"));
        Assert.assertTrue("Is not a name", !TNAHelper.isName("10.2.0.0"));
    }
}
