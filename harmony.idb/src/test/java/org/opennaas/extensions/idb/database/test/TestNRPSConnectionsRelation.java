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

import org.opennaas.extensions.idb.database.hibernate.Connections;
import org.opennaas.extensions.idb.database.hibernate.Domain;
import org.opennaas.extensions.idb.database.hibernate.Endpoint;
import org.opennaas.extensions.idb.database.hibernate.NrpsConnections;
import org.opennaas.extensions.idb.database.hibernate.Reservation;
import org.opennaas.extensions.idb.database.hibernate.Service;
import org.opennaas.extensions.idb.exception.database.DatabaseException;
import org.opennaas.extensions.idb.utils.ReservationHelpers;

public class TestNRPSConnectionsRelation extends DatabaseTest {

    /** reference reservation. */
    private static Reservation reservation;

    /** reference domain. */
    private static Domain domain;

    /** reference endpoint 1. */
    private static Endpoint endpoint1;

    /** reference endpoint 2. */
    private static Endpoint endpoint2;

    /** reference service. */
    private static Service service;

    /** reference connection. */
    private static Connections connection;

    @BeforeClass
    public static void setUpBeforeClass() throws DatabaseException {
		// create reference domain
		TestNRPSConnectionsRelation.domain = DatabaseTest.createReferenceDomain();
		// create reference reservation
		TestNRPSConnectionsRelation.reservation = DatabaseTest
			.createReferenceReservation();
		// create reference endpoint 1
		TestNRPSConnectionsRelation.endpoint1 = DatabaseTest
			.createReferenceEndpoint(TestNRPSConnectionsRelation.domain
				.getName());
		// create reference endpoint 2
		TestNRPSConnectionsRelation.endpoint2 = DatabaseTest
			.createReferenceEndpoint(TestNRPSConnectionsRelation.domain
				.getName());
		// create reference service
		TestNRPSConnectionsRelation.service = DatabaseTest
			.createReferenceService(TestNRPSConnectionsRelation.reservation);
		// create reference connection
		TestNRPSConnectionsRelation.connection = DatabaseTest.createReferenceConnection(
						TestNRPSConnectionsRelation.service,
						TestNRPSConnectionsRelation.endpoint1,
						TestNRPSConnectionsRelation.endpoint2);
    }

    @AfterClass
    public static void tearDownAfterClass() throws DatabaseException {
		// delete reference reservation, service and connection
		TestNRPSConnectionsRelation.reservation.delete();
		Assert.assertNull("reference reservation should be deleted",
			Reservation.load(TestNRPSConnectionsRelation.reservation
				.getReservationId()));
		Assert.assertNull("reference service should be deleted", Service
			.load(TestNRPSConnectionsRelation.service.getPK_service()));
		Assert.assertNull("reference connection should be deleted", Connections
				.load(TestNRPSConnectionsRelation.connection.getPK_Connections()));
		// delete reference domain and endpoint
		TestNRPSConnectionsRelation.domain.delete();
		Assert.assertNull("reference domain should be deleted", Domain
			.load(TestNRPSConnectionsRelation.domain.getName()));
		Assert.assertNull("reference endpoint1 should be deleted", Endpoint
			.load(TestNRPSConnectionsRelation.endpoint1.getTNA()));
		Assert.assertNull("reference endpoint2 should be deleted", Endpoint
				.load(TestNRPSConnectionsRelation.endpoint1.getTNA()));
    }

    /** test connection. */
    private NrpsConnections nrpsConnection;


    @Override
    public void setUpBeforeEveryTest() {
    	// create test connection
    	try {
    		this.nrpsConnection = ReservationHelpers.getTestNrpsConnection();
    	} catch (URISyntaxException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	this.nrpsConnection.setPkNrpsConnection(0);
    	this.nrpsConnection.setSourceEndpoint(TestNRPSConnectionsRelation.endpoint1);
    	this.nrpsConnection.setDestinationEndpoint(TestNRPSConnectionsRelation.endpoint2);
    	this.nrpsConnection.setConnection(TestNRPSConnectionsRelation.connection);
    	this.nrpsConnection.setDomain(TestNRPSConnectionsRelation.domain);
    }

    @Override
    public void tearDownAfterEveryTest() {
        // TODO Auto-generated method stub

    }

    @Override
    public void testReferentialIntegrity() throws DatabaseException {
    	// save test nrpsConnection
    	this.nrpsConnection.save();

    	// load referenced objects and check for saved connection
    	final Domain referenceDomain = Domain
    		.load(TestNRPSConnectionsRelation.domain.getName());
    	Assert.assertNotNull("referenced connection should be existent",
        		referenceDomain.getNrpsConnections().contains(this.nrpsConnection));
    	final Connections referenceConnection = Connections
    		.load(TestNRPSConnectionsRelation.connection.getPK_Connections());
    	Assert.assertNotNull("referenced connection should be existent",
        		referenceConnection.getNrpsConnections().values().contains(this.nrpsConnection));
    }

    @Override
    public void testSaveLoadEditDeleteRelationTuple() throws DatabaseException {
    	// save
    	this.nrpsConnection.save();
    	Assert.assertTrue("nrpsConnection-PK-ID should be greater than 0",
    		this.nrpsConnection.getPkNrpsConnection() > 0);

    	// load
    	final NrpsConnections loadedConnection = NrpsConnections.load(this.nrpsConnection
    		.getPkNrpsConnection());
    	Assert.assertEquals("persisted and loaded nrpsConnection should be equal",
    		this.nrpsConnection, loadedConnection);

    	// edit
    	loadedConnection.setBandwidth(loadedConnection.getBandwidth() + 1);
    	loadedConnection.save();

    	// delete
    	NrpsConnections nc = TestNRPSConnectionsRelation.connection
    									.getNrpsConnection(loadedConnection.getDomain());
    	TestNRPSConnectionsRelation.connection.getNrpsConnections()
    									.remove(loadedConnection.getDomain());
    	nc.delete();
    	Assert.assertNull("test nrpsConnection should be deleted", NrpsConnections
    		.load(this.nrpsConnection.getPkNrpsConnection()));
    }

}
