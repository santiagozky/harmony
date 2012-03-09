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


package eu.ist_phosphorus.harmony.test.testbed;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.muse.ws.addressing.soap.SoapFault;
import org.junit.Test;

import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.AddOrEditDomainType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.DomainInformationType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.DomainRelationshipType;
import eu.ist_phosphorus.harmony.common.serviceinterface.topology.SimpleTopologyClient;
import eu.ist_phosphorus.harmony.common.utils.Config;
import eu.ist_phosphorus.harmony.common.utils.Helpers;
import eu.ist_phosphorus.harmony.common.utils.Tuple;

public final class VirtualTestbedTests extends AbstractTestbedTest {

    private final SimpleDateFormat sdf =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private void printRunning() {
        System.out.println(">> at "
                + this.sdf.format(Calendar.getInstance()
                        .getTime()) + ": running "
                + (new Exception()).getStackTrace()[2].getMethodName()
                + " ...");
    }

    private static class TestConnection {
        public final Tuple<String, String> connection;
        public final List<Tuple<String, String>> blocking =
                new LinkedList<Tuple<String, String>>();

        public TestConnection(final String src, final String dst) {
            this.connection = new Tuple<String, String>(src, dst);
        }

        public void addBlocking(final String src, final String dst) {
            this.blocking.add(new Tuple<String, String>(src, dst));
        }
    }

    HashMap<String, TestConnection> testConnections =
            new HashMap<String, TestConnection>();

    private final SimpleTopologyClient topologyClient;

    public VirtualTestbedTests() {
        super(Config.getString("test", "testbed.epr.reservation"));
        this.topologyClient =
                new SimpleTopologyClient(Config.getString("test",
                        "testbed.epr.topology"));

        TestConnection c;

        c = new TestConnection("10.3.3.8", "10.3.4.8");
        c.addBlocking("10.3.3.11", "10.3.4.11");
        c.addBlocking("10.3.3.12", "10.3.4.12");
        c.addBlocking("10.3.3.13", "10.3.4.13");
        c.addBlocking("10.3.3.14", "10.3.4.14");
        c.addBlocking("10.3.3.15", "10.3.4.15");
        this.testConnections.put("i2cat", c);

        c = new TestConnection("10.7.225.7", "10.7.226.7");
        c.addBlocking("10.7.225.71", "10.7.226.71");
        c.addBlocking("10.7.225.72", "10.7.226.72");
        this.testConnections.put("viola", c);

        c = new TestConnection("10.8.3.1", "10.8.4.1");
        c.addBlocking("10.8.3.2", "10.7.227.72");
        c.addBlocking("10.3.3.15", "10.8.4.2");
        this.testConnections.put("crc", c);

        c = new TestConnection("10.3.4.8", "10.7.226.7");
        c.addBlocking("10.3.4.14", "10.7.225.71");
        c.addBlocking("10.3.4.15", "10.7.225.72");
        this.testConnections.put("i2cat-viola", c);

        c = new TestConnection("10.3.4.8", "10.8.3.1");
        c.addBlocking("10.3.3.14", "10.8.4.1");
        c.addBlocking("10.3.3.15", "10.8.3.2");
        this.testConnections.put("i2cat-crc", c);

        c = new TestConnection("10.7.226.7", "10.8.3.1");
        c.addBlocking("10.7.227.71", "10.8.4.1");
        c.addBlocking("10.7.227.72", "10.8.4.2");
        this.testConnections.put("viola-crc", c);

        c = new TestConnection("10.7.226.7", "10.8.3.1");
        c.addBlocking("10.7.226.71", "10.7.227.71");
        c.addBlocking("10.7.226.72", "10.7.227.72");
        this.testConnections.put("viola-crc-1", c);

        c = new TestConnection("10.7.225.7", "10.8.4.1");
        c.addBlocking("10.7.226.71", "10.7.227.71");
        c.addBlocking("10.7.226.72", "10.7.227.72");
        this.testConnections.put("extreme-reroute", c);
    }

    // --- private helper methods

    private void testFixed(final String name) throws Exception {
        this.printRunning();
        final TestConnection c = this.testConnections.get(name);
        final String src = c.connection.getFirstElement();
        final String dst = c.connection.getSecondElement();
        // this.testFixedReservation(src, dst, this.duration);
        this.runFixedTest(name, src, dst);
    }

    private void testMalleable(final String name) throws Exception {
        this.printRunning();
        final XMLGregorianCalendar now = Helpers.generateXMLCalendar();
        final XMLGregorianCalendar startTime =
                Helpers.rollXMLCalendar(now, 30, 0);
        final XMLGregorianCalendar deadline =
                Helpers.rollXMLCalendar(now, 90, 0);
        final TestConnection c = this.testConnections.get(name);
        this.testMalleableReservation(c.connection.getFirstElement(),
                c.connection.getSecondElement(), startTime, deadline, 1000); // 1GB
    }

    private void testMalleableWithBlocking(final String name,
            final int blockingDuration) throws Exception {
        final XMLGregorianCalendar startTime = Helpers.generateXMLCalendar();
        final TestConnection c = this.testConnections.get(name);
        int i = 1;
        for (final Tuple<String, String> b : c.blocking) {
            System.out.println("creating blocking reservation " + i++ + "/"
                    + c.blocking.size() + " from " + b.getFirstElement()
                    + " to " + b.getSecondElement() + " ...");
            this.createFixedReservation(b.getFirstElement(), b
                    .getSecondElement(), startTime, blockingDuration);
        }
        this.testMalleableReservation(c.connection.getFirstElement(),
                c.connection.getSecondElement(), Helpers.rollXMLCalendar(
                        startTime, 30, 0), Helpers.rollXMLCalendar(startTime,
                        90, 0), 1000);
    }

    private void testDeferMalleable(final String name) throws Exception {
        this.printRunning();
        this.testMalleableWithBlocking(name, 3600);
    }

    private void testRerouteMalleable(final String name) throws Exception {
        this.printRunning();
        this.testMalleableWithBlocking(name, 7200);
    }

    // --- inject dummy domain information

    public void addTestDomain() throws SoapFault {
        this.printRunning();
        final DomainInformationType domInfo = new DomainInformationType();
        domInfo.setDomainId("DummyTestDomain");
        domInfo
                .setDescription("Dummy domain used for testing domain information dissemination");
        domInfo
                .setReservationEPR("http://127.0.0.1/harmony-idb/services/Reservation");
        domInfo
                .setTopologyEPR("http://127.0.0.1/harmony-idb/services/Topology");
        domInfo.setRelationship(DomainRelationshipType.PEER);
        domInfo.setSequenceNumber(Integer.valueOf(1));

        final AddOrEditDomainType r = new AddOrEditDomainType();
        r.setDomain(domInfo);

        this.topologyClient.addOrEditDomain(r);
    }

    // --- fixed reservation tests

    @Test
    public void testFixedI2Cat() throws Exception {
        this.testFixed("i2cat");
    }

    @Test
    public void testFixedViola() throws Exception {
        this.testFixed("viola");
    }

    @Test
    public void testFixedCRC() throws Exception {
        this.testFixed("crc");
    }

    @Test
    public void testFixedI2CatViola() throws Exception {
        this.testFixed("i2cat-viola");
    }

    @Test
    public void testFixedI2CatCRC() throws Exception {
        this.testFixed("i2cat-crc");
    }

    @Test
    public void testFixedViolaCRC() throws Exception {
        this.testFixed("viola-crc");
    }

    // --- malleable reservation tests

    @Test
    public void testMalleableViola() throws Exception {
        this.testMalleable("viola");
    }

    @Test
    public void testMalleableI2Cat() throws Exception {
        this.testMalleable("i2cat");
    }

    @Test
    public void testMalleableCRC() throws Exception {
        this.testMalleable("crc");
    }

    @Test
    public void testMalleableI2CatViola() throws Exception {
        this.testMalleable("i2cat-viola");
    }

    @Test
    public void testMalleableI2CatCRC() throws Exception {
        this.testMalleable("i2cat-crc");
    }

    @Test
    public void testMalleableViolaCRC() throws Exception {
        this.testMalleable("viola-crc");
    }

    // --- malleable reservation tests: malleable reservation starts after a
    // fixed reservation

    @Test
    public void testDeferMalleableI2Cat() throws Exception {
        this.testDeferMalleable("i2cat");
    }

    @Test
    public void testDeferMalleableViola() throws Exception {
        this.testDeferMalleable("viola");
    }

    @Test
    public void testDeferMalleableCRC() throws Exception {
        this.testDeferMalleable("crc");
    }

    @Test
    public void testDeferMalleableI2CatViola() throws Exception {
        this.testDeferMalleable("i2cat-viola");
    }

    @Test
    public void testDeferMalleableI2CatCRC() throws Exception {
        this.testDeferMalleable("i2cat-crc");
    }

    @Test
    public void testDeferMalleableViolaCRC() throws Exception {
        this.testDeferMalleable("viola-crc-1");
    }

    // --- malleable reservation tests: malleable reservation takes longer route
    // due to blocking fixed reservation

    @Test
    public void testRerouteMalleableI2Cat() throws Exception {
        this.testRerouteMalleable("i2cat");
    }

    @Test
    public void testRerouteMalleableViola() throws Exception {
        this.testRerouteMalleable("viola");
    }

    @Test
    public void testRerouteMalleableCRC() throws Exception {
        this.testRerouteMalleable("crc");
    }

    @Test
    public void testRerouteMalleableI2CatViola() throws Exception {
        this.testRerouteMalleable("i2cat-viola");
    }

    @Test
    public void testRerouteMalleableI2CatCRC() throws Exception {
        this.testRerouteMalleable("i2cat-crc");
    }

    @Test
    public void testRerouteMalleableViolaCRC() throws Exception {
        this.testRerouteMalleable("viola-crc");
    }

    @Test
    public void testExtremeReroute() throws Exception {
        this.testRerouteMalleable("extreme-reroute");
    }
}
