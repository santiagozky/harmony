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


package org.opennaas.extensions.idb.handler.test;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.opennaas.extensions.idb.reservation.handler.ReservationRequestHandler;

/**
 * JUnit test cases for the request handler.
 * 
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 * @version $Id$
 */
public class TestRequestHandler {
    /**
     * General timeout for a test.
     */
    private static final int TIMEOUT = 10000;

    /**
     * Setup before creating test suite.
     * 
     * @throws java.lang.Exception
     *             An exception.
     */
    @BeforeClass
    public static final void setUpBeforeClass() throws Exception {
        // nothing yet
    }

    /**
     * Tear down after test suite ran.
     * 
     * @throws java.lang.Exception
     *             An exception.
     */
    @AfterClass
    public static final void tearDownAfterClass() throws Exception {
        // Insert methods for tear down if neccessary
    }

    /**
     * Setup before a test.
     * 
     * @throws java.lang.Exception
     *             An exception
     */
    @Before
    public final void setUp() throws Exception {
        // nothing yet
    }

    /**
     * Tear down before a test.
     * 
     * @throws java.lang.Exception
     *             An exception.
     */
    @After
    public void tearDown() throws Exception {
        // Insert methods for tear down if neccessary
    }

    /**
     * Test singleton.
     * 
     * @author Alexander Willner (willner@cs.uni-bonn.de)
     */
    @Test(timeout = TestRequestHandler.TIMEOUT)
    public final void testSingleton() {
        final ReservationRequestHandler myObj1 = ReservationRequestHandler
                .getInstance();
        final ReservationRequestHandler myObj2 = ReservationRequestHandler
                .getInstance();
        Assert.assertSame(myObj1, myObj2);
    }
}
