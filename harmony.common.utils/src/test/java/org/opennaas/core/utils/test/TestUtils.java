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
package org.opennaas.core.utils.test;

import java.lang.reflect.Method;

import junit.framework.Assert;

import org.junit.Test;

import org.opennaas.core.utils.Helpers;
import org.opennaas.core.utils.MethodReflectionCache;

/**
 * @author gassen
 * 
 */
public class TestUtils {
    /**
     * Test Prefix Matching.
     */
    @Test
    public final void testPrefixMatch() {
        boolean result1 = Helpers.prefixMatch("129.0.1.1", "129.0.0.1/16");

        Assert.assertTrue(result1);

        boolean result2 = Helpers.prefixMatch("129.1.1.1", "129.0.0.1/16");

        Assert.assertFalse(result2);
    }

    /**
     * Test method reflection cache.
     *
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public final void testMethodReflectionCache() throws SecurityException,
            NoSuchMethodException {
        MethodReflectionCache cache = new MethodReflectionCache();

        final String testClass = "";
        final Integer testClass2 = new Integer(1);

        Method method = cache.getMethod(testClass, "indexOf", String.class);

        Assert.assertEquals("Should be equal", String.class.getMethod(
                "indexOf", String.class), method);

        method = cache.getMethod(testClass, "indexOf", int.class);

        Assert.assertEquals("Should be equal", String.class.getMethod(
                "indexOf", int.class), method);

        method = cache.getMethod(testClass, "indexOf", int.class, int.class);

        Assert.assertEquals("Should be equal", String.class.getMethod(
                "indexOf", int.class, int.class), method);
        
        method = cache.getMethod(testClass2, "floatValue");

        Assert.assertEquals("Should be equal", Integer.class.getMethod(
                "floatValue"), method);
    }
}
