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

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import org.opennaas.extensions.idb.database.hibernate.Connections;
import org.opennaas.extensions.idb.database.hibernate.Domain;
import org.opennaas.extensions.idb.database.hibernate.Endpoint;
import org.opennaas.extensions.idb.database.hibernate.Reservation;
import org.opennaas.extensions.idb.database.hibernate.Service;
import org.opennaas.extensions.idb.exception.database.DatabaseException;
import org.opennaas.extensions.idb.utils.ReservationHelpers;
import org.opennaas.extensions.idb.utils.TopologyHelpers;

public class TestServiceRelation extends DatabaseTest {

    /** reference reservation. */
    private static Reservation reservation;

    @BeforeClass
    public static void setUpBeforeClass() throws DatabaseException {
        // create reference reservation
        TestServiceRelation.reservation = DatabaseTest
                .createReferenceReservation();
    }

    @AfterClass
    public static void tearDownAfterClass() throws DatabaseException {
        // delete reference reservation
        TestServiceRelation.reservation.delete();
        Assert.assertNull("reference reservation should be deleted",
                Reservation.load(TestServiceRelation.reservation
                        .getReservationId()));
    }

    /** test service. */
    private Service service;

    @Override
    public void setUpBeforeEveryTest() {
        // create test service
        this.service = ReservationHelpers.getTestService();
        this.service.setReservation(TestServiceRelation.reservation);
    }

    @Override
    public void tearDownAfterEveryTest() {
        // nothing here
    }

    @Override
    public void testReferentialIntegrity() throws DatabaseException {
        // save test service
//    	TestServiceRelation.reservation.addService(this.service);
        this.service.save();

        // load reservation and check for saved service
        final Reservation referenceReservation = Reservation
                .load(TestServiceRelation.reservation.getReservationId());
        final Service referenceService = referenceReservation
                .getService(this.service.getServiceId());
        Assert.assertNotNull("referenced service should be existent",
                referenceService);
        Assert.assertEquals("referenced service should be the same",
                this.service, referenceService);

        // delete test service
        this.service.getReservation().getServices().remove(
        							new Integer(this.service.getServiceId()));
        this.service.delete();
    }

    @Override
    public void testSaveLoadEditDeleteRelationTuple() throws DatabaseException {
        // save
        this.service.save();
        Assert.assertTrue("service-PK-ID should be greater than 0",
                this.service.getPK_service() > 0);

        // load
        final Service loadedService = Service
                .load(this.service.getPK_service());
        Assert.assertEquals("persisted and loaded service should be equal",
                this.service, loadedService);

        // edit
        loadedService.setServiceId(loadedService.getServiceId() + 1);
        loadedService.save();

        final Service editedService = Service.load(loadedService
                .getPK_service());
//        Assert.assertNotSame("edited service should be different",
//                loadedService, editedService);

        // delete
        loadedService.getReservation().getServices().remove(
        						new Integer(loadedService.getServiceId()));
        loadedService.delete();
        Assert.assertNull("test service should be deleted", Service
                .load(this.service.getPK_service()));
    }

    @Test
    public void testTransitivePersistence() throws DatabaseException, URISyntaxException {
        // create reference domain and endpoint
        final Domain dom = TopologyHelpers.getTestDomain();
        final Endpoint start = TopologyHelpers.getTestEndpoint();
        start.setDomain(dom);
        dom.save();
        start.save();

        // create reference connection and put it in the service object
        Connections connection = ReservationHelpers.getTestConnection();
        connection.setPK_Connections(0);
        connection.setStartpoint(start);
        this.service.addConnection(connection);

        // save the changed service
        this.service.save();

        // get the connection from the service, because of generated primary key
        connection = this.service.getConnection(connection.getConnectionId());

        // load service new from DB and check if connection is existent
        Service testService = Service.load(this.service.getPK_service());
        Assert.assertNotNull("there should be connections in the service",
                testService.getConnections());
        Assert.assertTrue("stored connection should be existent", testService
                .getConnections().containsValue(connection));

        // remove the connection in the service object and save this
        // -> the connection should also be removed
        Connections con = testService.getConnection(connection.getConnectionId());
        testService.getConnections().remove(
                new Integer(connection.getConnectionId()));
        con.delete();
        testService.save();

        // load service new from DB and check if connection is not existent
        // anymore
        testService = Service.load(this.service.getPK_service());
        Assert.assertTrue("there should be no connections anymore", testService
                .getConnections().isEmpty());
        Assert.assertNull("the connection should be deleted", Connections
                .load(connection.getPK_Connections()));

        // delete the reference domain
        dom.delete();
        // delete the test service
        testService.getReservation().getServices().remove(
        							new Integer(testService.getServiceId()));
        testService.delete();
    }
}
