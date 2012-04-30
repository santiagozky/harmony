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
import java.util.Set;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import org.opennaas.core.utils.Helpers;
import org.opennaas.extensions.idb.database.hibernate.Connections;
import org.opennaas.extensions.idb.database.hibernate.Domain;
import org.opennaas.extensions.idb.database.hibernate.Endpoint;
import org.opennaas.extensions.idb.database.hibernate.Reservation;
import org.opennaas.extensions.idb.database.hibernate.Service;
import org.opennaas.extensions.idb.exception.database.DatabaseException;
import org.opennaas.extensions.idb.utils.ReservationHelpers;
import org.opennaas.extensions.idb.utils.TopologyHelpers;

public class TestEndpointsRelation extends DatabaseTest {

	/** reference domain. */
	private static Domain domain;

	@BeforeClass
	public static void setUpBeforeClass() throws DatabaseException {
		// create reference domain
		TestEndpointsRelation.domain = DatabaseTest.createReferenceDomain();
	}

	@AfterClass
	public static void tearDownAfterClass() throws DatabaseException {
		// delete reference domain
		TestEndpointsRelation.domain.delete();
		Assert.assertNull("reference domain should be deleted",
				Domain.load(TestEndpointsRelation.domain.getName()));
	}

	/** test endpoint. */
	private Endpoint endpoint;

	@Override
	public void setUpBeforeEveryTest() {
		// create test endpoint

		this.endpoint = new Endpoint(TopologyHelpers.getRandomTNA(),
				Helpers.getRandomString(), Helpers.getRandomString(),
				TestEndpointsRelation.domain, Helpers.getPositiveRandomInt(),
				Helpers.getPositiveRandomInt());

	}

	@Override
	public void tearDownAfterEveryTest() {
		// TODO Auto-generated method stub

	}

	@Override
	public void testReferentialIntegrity() throws DatabaseException {
		// save test endpoint
		this.endpoint.save();

		final Domain referenceDomain = Domain.load(TestEndpointsRelation.domain
				.getName());

		final Set<Endpoint> referenceEndpoints = referenceDomain.getEndpoints();
		Assert.assertNotNull("referenced endpoint should be existent",
				referenceEndpoints);
		Assert.assertTrue("referenced endpoint should be the same",
				referenceEndpoints.contains(this.endpoint));

		// delet test endpoint
		this.endpoint.delete();
	}

	@Override
	public void testSaveLoadEditDeleteRelationTuple() throws DatabaseException {
		// save
		this.endpoint.save();
		Assert.assertTrue("endpoint-TNA length should be larger than 0",
				this.endpoint.getTNA().length() > 0);

		// load
		Endpoint loadedEndpoint = Endpoint.load(this.endpoint.getTNA());
		Assert.assertEquals("persisted and loaded endpoint should be equal",
				this.endpoint, loadedEndpoint);

		// edit
		loadedEndpoint.setDescription("edited description");
		loadedEndpoint.save();

		final Endpoint editedEndpoint = Endpoint.load(loadedEndpoint.getTNA());
		// Assert.assertNotSame("edited endpoint should be different",
		// loadedEndpoint, editedEndpoint);

		// delete
		loadedEndpoint.delete();
		Assert.assertNull("test endpoint should be deleted",
				Endpoint.load(this.endpoint.getTNA()));
	}

	@Test
	public void testReferentialIntegrity2() throws DatabaseException,
			URISyntaxException {
		// save the endpoint
		this.endpoint.save();

		Endpoint auxEnd = new Endpoint(TopologyHelpers.getRandomTNA(),
				Helpers.getRandomString(), Helpers.getRandomString(),
				TestEndpointsRelation.domain, Helpers.getPositiveRandomInt(),
				Helpers.getPositiveRandomInt());

		auxEnd.save();

		// create reference reservation, service
		Reservation res = TestEndpointsRelation.createReferenceReservation();
		Service service = TestEndpointsRelation.createReferenceService(res);

		// create reference connection and put it in the endpoint object
		Connections conn = ReservationHelpers.getTestConnection();
		conn.setPK_Connections(0);
		conn.setStartpoint(this.endpoint);
		conn.getEndpoints().add(auxEnd);
		conn.setService(service);
		conn.save();

		// load endpoint new from DB and check if connection is existent
		Endpoint testEnd = Endpoint.load(this.endpoint.getTNA());
		Assert.assertNotNull("there should be connections in the endpoint",
				testEnd.getConnections());
		Assert.assertTrue("stored connection should be existent", testEnd
				.getConnections().values().contains(conn));

		// remove the connection
		conn.getService().getConnections()
				.remove(new Integer(conn.getConnectionId()));
		conn.delete();

		// load endpoint new from DB and check if connection is not existent
		// anymore
		testEnd = Endpoint.load(this.endpoint.getTNA());
		Assert.assertFalse("there should be no connection anymore", testEnd
				.getConnections().values().contains(conn));
		Assert.assertNull("the connection should be deleted",
				Connections.load(conn.getPK_Connections()));

		// delete the reference reservation
		res.delete();
	}
}
