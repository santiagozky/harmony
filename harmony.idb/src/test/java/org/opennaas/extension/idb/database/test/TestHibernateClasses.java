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


package org.opennaas.extensions.idb.database.test;

import java.net.URISyntaxException;

import junit.framework.Assert;
import junit.framework.JUnit4TestAdapter;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.opennaas.extensions.idb.database.hibernate.Connections;
import org.opennaas.extensions.idb.database.hibernate.Domain;
import org.opennaas.extensions.idb.database.hibernate.Endpoint;
import org.opennaas.extensions.idb.database.hibernate.Reservation;
import org.opennaas.extensions.idb.database.hibernate.Service;
import org.opennaas.extensions.idb.database.hibernate.TNAPrefix;
import org.opennaas.extensions.idb.database.hibernate.VIEW_InterDomainLink;
import org.opennaas.extensions.idb.utils.ReservationHelpers;
import org.opennaas.extensions.idb.utils.TopologyHelpers;

/**
 * Test cases for the hibernate classes.
 * 
 * @author Alexander Zimmermann (zimmerm2@cs.uni-bonn.de)
 * @version 0.1
 */

public class TestHibernateClasses {

    /**
     * The setUp methode generates the Test data for the Class.
     * 
     * @throws Exception
     *             should not happen.
     */
    @BeforeClass
    public static final void setUpClass() throws Exception {
        // nothing yet
    }

    /**
     * Compatibilty with JUnit 3.
     * 
     * @return A JUnit4 test adapter.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(TestHibernateClasses.class);
    }

    /**
     * Test copy of Connection Object
     * @throws URISyntaxException 
     */
    @Test
    public final void copyTestConnection() throws URISyntaxException {
        final Connections conn = ReservationHelpers.getTestConnection();
        final Connections testConn = conn.getCopy();
        Assert.assertTrue(testConn.isEqual(conn));
        testConn.setDataAmount(100);
        Assert.assertFalse(testConn.isEqual(conn));
    }

    /**
     * Test copy of Domain Object.
     * 
     * @throws URISyntaxException
     */
    @Test
    public final void copyTestDomain() throws URISyntaxException {
        final Domain domain = TopologyHelpers.getTestDomain("TestDomain");
        final Domain testDomain = domain.getCopy();
        Assert.assertTrue("check equals", testDomain.isEqual(domain));
        testDomain.setDescription("TestDesc");
        Assert.assertFalse(testDomain.isEqual(domain));
    }

    /* Copy Testing -------------------------------------------------------- */

    /**
     * Test copy of Endpoint Object.
     * @throws URISyntaxException 
     */
    @Test
    public final void copyTestEndpoint() throws URISyntaxException {
        final Endpoint endpoint = TopologyHelpers.getTestEndpoint();
        final Endpoint testEndpoint = endpoint.getCopy();
        Assert.assertTrue(testEndpoint.isEqual(endpoint));
        testEndpoint.setDescription("TestDesc");
        Assert.assertFalse(testEndpoint.isEqual(endpoint));

    }

    /**
     * Test copy of Link Object.
     * @throws URISyntaxException 
     */
    @Test
    public final void copyTestLink() throws URISyntaxException {
        final VIEW_InterDomainLink link = TopologyHelpers.getTestLink();
        final VIEW_InterDomainLink testLink = link.getCopy();
        Assert.assertTrue(testLink.isEqual(link));
        testLink.setName("TestLink");
        Assert.assertFalse(testLink.isEqual(link));
    }

    /**
     * Test copy of Reservation Object
     */
    @Test
    public final void copyTestReservation() {
        final Reservation res = ReservationHelpers.getTestReservation();
        final Reservation testRes = res.getCopy();
        Assert.assertTrue(testRes.isEqual(res));
        testRes.setConsumerUrl("blub");
        Assert.assertFalse(testRes.isEqual(res));
    }

    /**
     * Test copy of Service Object
     */
    @Test
    public final void copyTestService() {
        final Service service = ReservationHelpers.getTestService();
        final Service testService = service.getCopy();
        Assert.assertTrue(testService.isEqual(service));
        testService.setDuration(100);
        Assert.assertFalse(testService.isEqual(service));
    }

    /**
     * test connection hashCode
     * @throws URISyntaxException 
     */
    @Test
    public final void hashTestConnection() throws URISyntaxException {
        final Connections connection = ReservationHelpers.getTestConnection();
        final Connections testConnection = connection.getCopy();
        Assert.assertTrue(connection.hashCode() == testConnection.hashCode());
        testConnection.setDataAmount(1);
        Assert.assertFalse(connection.hashCode() == testConnection.hashCode());
    }

    /**
     * test domain hashCode
     * 
     * @throws URISyntaxException
     */
    @Test
    public final void hashTestDomain() throws URISyntaxException {
        final Domain domain = TopologyHelpers.getTestDomain("TestDomain");
        final Domain testDomain = domain.getCopy();
        Assert.assertTrue(domain.hashCode() == testDomain.hashCode());
        testDomain.setName("AnotherTestDomain");
        Assert.assertFalse(domain.hashCode() == testDomain.hashCode());
    }

    /* Sort Testing -------------------------------------------------------- */

    /**
     * test endpoint hashCode
     * @throws URISyntaxException 
     */
    @Test
    public final void hashTestEndpoint() throws URISyntaxException {
        final Endpoint endpoint = TopologyHelpers.getTestEndpoint();
        final Endpoint testEndpoint = endpoint.getCopy();
        Assert.assertTrue(endpoint.hashCode() == testEndpoint.hashCode());
        testEndpoint.setName("AnotherTestEndpoint");
        Assert.assertFalse(endpoint.hashCode() == testEndpoint.hashCode());
    }

    /**
     * test link hashCode
     * @throws URISyntaxException 
     */
    @Test
    public final void hashTestLink() throws URISyntaxException {
        final VIEW_InterDomainLink link = TopologyHelpers.getTestLink();
        final VIEW_InterDomainLink testLink = link.getCopy();
        Assert.assertTrue(link.hashCode() == testLink.hashCode());
        testLink.setName("AnotherTestLink");
        Assert.assertFalse(link.hashCode() == testLink.hashCode());
    }

    /**
     * test reservation hashCode
     */
    @Test
    public final void hashTestReservation() {
        final Reservation reservation = ReservationHelpers.getTestReservation();
        final Reservation testReservation = reservation.getCopy();
        Assert.assertTrue(reservation.hashCode() == testReservation.hashCode());
        testReservation.setConsumerUrl("AnotherTestReservation");
        Assert
                .assertFalse(reservation.hashCode() == testReservation
                        .hashCode());
    }

    /**
     * test service hashCode
     */
    @Test
    public final void hashTestService() {
        final Service service = ReservationHelpers.getTestService();
        final Service testService = service.getCopy();
        Assert.assertTrue(service.hashCode() == testService.hashCode());
        testService.setDuration(1);
        Assert.assertFalse(service.hashCode() == testService.hashCode());
    }

    /**
     * test prefix match
     * @throws URISyntaxException 
     */
    @Test
    public final void prefixMatch() throws URISyntaxException {
        final String addrStr = "192.168.1.128";
        final int addrInt = Endpoint.ipv4ToInt(addrStr);
        Assert.assertEquals(addrInt, 0xC0A80180);

        final TNAPrefix prefix = new TNAPrefix(TopologyHelpers.getTestDomain(), addrStr + "/26");

        Assert.assertTrue(prefix.matchesPrefix(0xC0A80181));
        // same as above, String representation
        Assert.assertTrue(prefix.matchesPrefix("192.168.1.129"));
        Assert.assertFalse(prefix.matchesPrefix("192.168.1.1"));

        prefix.setPrefix(addrStr + "/24");

        Assert.assertTrue(prefix.matchesPrefix("192.168.1.1"));
    }

    /**
     * The setUp methode generates the Test data needet for each test.
     * 
     * @throws Exception
     *             should not happen.
     */
    @Before
    public final void setUp() throws Exception {
        // nothing yet
    }

    /* Hash Testing -------------------------------------------------------- */

    /**
     * Test sort of Connection Object
     * @throws URISyntaxException 
     */
    @Test
    public final void sortTestConnection() throws URISyntaxException {
        final Connections conn = ReservationHelpers.getTestConnection();
        final Connections testConn = conn.getCopy();
        conn.setConnectionId(1);
        testConn.setConnectionId(2);
        Assert.assertTrue("greater than", 1 == testConn.compareTo(conn));
        testConn.setConnectionId(0);
        Assert.assertTrue("less than", -1 == testConn.compareTo(conn));
        testConn.setConnectionId(1);
        Assert.assertTrue("equals than", 0 == testConn.compareTo(conn));
    }

    /**
     * Test sort of Domain Object.
     * 
     * @throws URISyntaxException
     */
    @Test
    public final void sortTestDomain() throws URISyntaxException {
        final Domain testDomain1 = TopologyHelpers.getTestDomain("TestDomain1");
        final Domain testDomain2 = TopologyHelpers.getTestDomain("TestDomain2");
        testDomain1.setName("DomainTest12");
        Assert.assertTrue("greater than", 1 == testDomain1
                .compareTo(testDomain2));
        testDomain1.setName("DomainT");
        Assert
                .assertTrue("less than", -1 == testDomain1
                        .compareTo(testDomain2));
        testDomain1.setName("DomainTest1");
        Assert.assertTrue("equals than", 0 == testDomain1
                .compareTo(testDomain2));
    }

    /**
     * Test sort of Endpoint Object.
     * @throws URISyntaxException 
     */
    @Test
    public final void sortTestEndpoint() throws URISyntaxException {
        final Endpoint testEndpoint1 = TopologyHelpers.getTestEndpoint();
        final Endpoint testEndpoint2 = testEndpoint1.getCopy();
        testEndpoint2.setTNA("10.0.0.1");
        testEndpoint1.setTNA("11.0.0.1");
        Assert.assertTrue("greater than", 1 == testEndpoint1
                .compareTo(testEndpoint2));
        testEndpoint1.setTNA("9.0.0.1");
        Assert.assertTrue("less than", -1 == testEndpoint1
                .compareTo(testEndpoint2));
        testEndpoint1.setTNA("10.0.0.1");
        Assert.assertTrue("equals than", 0 == testEndpoint1
                .compareTo(testEndpoint2));
    }

    /**
     * Test sort of Link Object.
     * @throws URISyntaxException 
     */
    @Test
    public final void sortTestLink() throws URISyntaxException {
        final VIEW_InterDomainLink link = TopologyHelpers.getTestLink();
        final VIEW_InterDomainLink testLink = link.getCopy();

        link.setName("b");
        testLink.setName("c");
        Assert.assertTrue("greater than", 1 == testLink.compareTo(link));
        testLink.setName("a");
        Assert.assertTrue("less than", -1 == testLink.compareTo(link));
        testLink.setName("b");
        Assert.assertTrue("equals than", 0 == testLink.compareTo(link));
    }

    /**
     * Test sort of Reservation Object
     */
    @Test
    public final void sortTestReservation() {
        final Reservation res = ReservationHelpers.getTestReservation();
        final Reservation testRes = res.getCopy();
        res.setReservationId(1);
        testRes.setReservationId(2);
        Assert.assertTrue("greater than", 1 == testRes.compareTo(res));
        testRes.setReservationId(0);
        Assert.assertTrue("less than", -1 == testRes.compareTo(res));
        testRes.setReservationId(1);
        Assert.assertTrue("equals than", 0 == testRes.compareTo(res));
    }

    /**
     * Test sort of Service Object
     */
    @Test
    public final void sortTestService() {
        final Service service = ReservationHelpers.getTestService();
        final Service testService = service.getCopy();
        service.setPK_service(1);
        testService.setPK_service(2);
        Assert.assertTrue("greater than", 1 == testService.compareTo(service));
        testService.setPK_service(0);
        Assert.assertTrue("less than", -1 == testService.compareTo(service));
        testService.setPK_service(1);
        Assert.assertTrue("equals than", 0 == testService.compareTo(service));
    }

    /* Misc Testing -------------------------------------------------------- */

    /**
     * No tearDown for Domain needed because of the delete Test.
     * 
     * @throws Exception
     *             should not happen.
     */
    @After
    public final void tearDown() throws Exception {
        // nothing yet
    }
}
