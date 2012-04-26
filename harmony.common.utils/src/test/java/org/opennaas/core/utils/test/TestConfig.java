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

import org.apache.log4j.Level;
import org.junit.Assert;
import org.junit.Test;

import org.opennaas.core.utils.Config;

public class TestConfig {
    /**
     * Simple test to test property file related issues.
     * 
     * @author Alexander Willner (willner@cs.uni-bonn.de)
     * @version $Id: TestProperties.java 3328 2008-07-01 13:53:48Z
     *          wagners@cs.uni-bonn.de $
     */
    @Test
    public final void testProperties() {
        Assert.assertEquals("Test string", Config.getString("test",
                "junit.stringValue"), "testValue");
        Assert.assertEquals("Test integer", Integer.valueOf(12345), Config
                .getInt("test", "junit.intValue"));
        Assert.assertEquals("Test long", Long.valueOf(123456), Config.getLong(
                "test", "junit.longValue"));
        Assert.assertTrue("Test boolean", Config.isTrue("test",
                "junit.boolValue"));
        Assert.assertTrue("Test boolean", Config.isTrue("test.junit.boolValue"));
        Assert.assertEquals("Test fatal level", Level.FATAL, Config.getLevel(
                "test", "junit.level.fatal"));
        Assert.assertEquals("Test info level", Level.INFO, Config.getLevel(
                "test", "junit.level.info"));
        Assert.assertEquals("Test string", Config
                .getString("test.junit.stringValue"), "testValue");
    }

    /**
     * 
     */
    @Test
    public final void testOverrideFunctionality() {
        Assert.assertTrue(Config.isTrue("test", "junit.override"));
    }

    @Test
    public final void testPropertyCache() {
        final String propertyFile = "test";
        final String key = "junit.stringValue";
        Config.resetCache();
        Assert.assertFalse("Should not be cached.", Config
                .isCached(propertyFile + key));
        Config.getString(propertyFile, key);
        Assert.assertTrue("Should be cached.", Config.isCached(propertyFile
                + key));
    }
}
