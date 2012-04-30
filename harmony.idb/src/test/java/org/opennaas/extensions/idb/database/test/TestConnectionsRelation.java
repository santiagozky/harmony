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
import org.opennaas.extensions.idb.database.hibernate.NrpsConnections;
import org.opennaas.extensions.idb.database.hibernate.Reservation;
import org.opennaas.extensions.idb.database.hibernate.Service;
import org.opennaas.extensions.idb.exception.database.DatabaseException;
import org.opennaas.extensions.idb.utils.ReservationHelpers;

public class TestConnectionsRelation extends DatabaseTest {

	/** reference reservation. */
	private static Reservation reservation;

	/** reference domain. */
	private static Domain domain;

	/** reference endpoint. */
	private static Endpoint endpoint;

	/** reference service. */
	private static Service service;

	@BeforeClass
	public static void setUpBeforeClass() throws DatabaseException {
		// create reference domain
		TestConnectionsRelation.domain = DatabaseTest.createReferenceDomain();
		// create reference reservation
		TestConnectionsRelation.reservation = DatabaseTest
		.createReferenceReservation();
		// create reference endpoint
		TestConnectionsRelation.endpoint = DatabaseTest
		.createReferenceEndpoint(TestConnectionsRelation.domain
				.getName());
		// create reference service
		TestConnectionsRelation.service = DatabaseTest
		.createReferenceService(TestConnectionsRelation.reservation);
	}

	@AfterClass
	public static void tearDownAfterClass() throws DatabaseException {
		// delete reference reservation and service
		TestConnectionsRelation.reservation.delete();
		Assert.assertNull("reference reservation should be deleted",
				Reservation.load(TestConnectionsRelation.reservation
						.getReservationId()));
		Assert.assertNull("reference service should be deleted", Service
				.load(TestConnectionsRelation.service.getPK_service()));
		// delete reference domain and endpoint
		TestConnectionsRelation.domain.delete();
		Assert.assertNull("reference domain should be deleted", Domain
				.load(TestConnectionsRelation.domain.getName()));
		Assert.assertNull("reference endpoint should be deleted", Endpoint
				.load(TestConnectionsRelation.endpoint.getTNA()));
	}

	/** test connection. */
	private Connections connection;

	@Override
	public void setUpBeforeEveryTest() {
		// create test connection
		try {
			this.connection = ReservationHelpers.getTestConnection();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.connection.setStartpoint(TestConnectionsRelation.endpoint);
		this.connection.setService(TestConnectionsRelation.service);
	}

	@Override
	public void tearDownAfterEveryTest() {
		// TODO Auto-generated method stub

	}

	@Override
	public void testReferentialIntegrity() throws DatabaseException {
		// save test connection
		this.connection.save();

		// load service and check for saved connection
		final Service referenceService = Service
			.load(TestConnectionsRelation.service.getPK_service());
		final Connections referenceConnection = referenceService
			.getConnection(this.connection.getConnectionId());
		Assert.assertNotNull("referenced connection should be existent",
				referenceConnection);
		Assert.assertEquals("referenced connection should be the same",
				this.connection, referenceConnection);
	}

	@Override
	public void testSaveLoadEditDeleteRelationTuple() throws DatabaseException {
		// save
		this.connection.save();
		Assert.assertTrue("connection-PK-ID should be greater than 0",
				this.connection.getPK_Connections() > 0);

		// load
		final Connections loadedConnection = Connections.load(this.connection
				.getPK_Connections());
		Assert.assertEquals("persisted and loaded connection should be equal",
				this.connection, loadedConnection);

		// edit
		loadedConnection.setConnectionId(loadedConnection.getConnectionId() + 1);
		loadedConnection.save();

		// delete
		loadedConnection.getService().getConnections().remove(
								new Integer(loadedConnection.getConnectionId()));
		loadedConnection.delete();
		Assert.assertNull("test connection should be deleted", Connections
				.load(this.connection.getPK_Connections()));
	}

	@Test
	public void testTransitivePersistence() throws DatabaseException, URISyntaxException {
		// save test connection
		this.connection.save();

		// create reference nrpsConnection and put it in the connection object
		NrpsConnections nrpsConn = ReservationHelpers.getTestNrpsConnection();
		nrpsConn.setPkNrpsConnection(0);
		nrpsConn.getDomain().save();
		nrpsConn.getSourceEndpoint().save();
		nrpsConn.getDestinationEndpoint().save();
		nrpsConn.setConnection(this.connection);
		nrpsConn.save();

		// load connection new from DB and check if nrpsConnection is existent
		Connections testConnection = Connections.load(this.connection
				.getPK_Connections());
		Assert.assertFalse("there should be nrpsConnections in the connection",
				testConnection.getNrpsConnections().isEmpty());
		Assert.assertTrue("stored nrpsConnection should be existent",
				testConnection.getNrpsConnections().containsValue(nrpsConn));

		// remove the nrpsConnection in the connection object and save this
		// -> the nrpsConnection should also be removed
		NrpsConnections nc = testConnection.getNrpsConnection(nrpsConn.getDomain());
		testConnection.getNrpsConnections().remove(nrpsConn.getDomain());
		nc.delete();

		// load connection new from DB and check if nrpsConnection is not
		// existent anymore
		Connections testConnection2 = Connections.load(this.connection
				.getPK_Connections());
		Assert.assertTrue("there should be no nrpsConnection anymore",
				testConnection2.getNrpsConnections().isEmpty());
		Assert.assertNull("the nrpsConnection should be deleted",
				NrpsConnections.load(nrpsConn.getPkNrpsConnection()));

		// delete the nrpsConnection reference domain
		nrpsConn.getDomain().delete();
	}

	@Test
	public void testTransitivePersistence2() throws DatabaseException {
		// create reference endpoint and put it in the connection object
		final Domain domain = DatabaseTest.createReferenceDomain();
		final Endpoint endpoint = DatabaseTest.createReferenceEndpoint(domain
				.getName());
		this.connection.addEndpoint(endpoint);
		// save the changed connection
		this.connection.save();

		// load connection new from DB and check if nrpsConnection is existent
		final Connections testConnection = Connections.load(this.connection
				.getPK_Connections());
		Assert.assertNotNull("there should be endpoints in the connection",
				testConnection.getEndpoints());
		Assert.assertTrue("stored endpoint should be existent", testConnection
				.getEndpoints().contains(endpoint));

		// remove the endpoint in the connection object and save this
		// -> the endpoint should be still existing, but the mapping between 
		// endpoint and connection should be removed
		testConnection.getEndpoints().remove(endpoint);
		testConnection.save();

		// check if endpoint is still existent
		Assert.assertNotNull("the endpoint should be still existent",
				Endpoint.load(endpoint.getTNA()));

		// delete the reference domain
		domain.delete();
	}
}
