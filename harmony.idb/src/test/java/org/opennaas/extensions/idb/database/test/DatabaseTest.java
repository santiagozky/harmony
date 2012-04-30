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

import java.io.IOException;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointInterfaceType;
import org.opennaas.core.utils.Config;
import org.opennaas.core.utils.Helpers;
import org.opennaas.extensions.idb.Constants;
import org.opennaas.extensions.idb.database.hibernate.Connections;
import org.opennaas.extensions.idb.database.hibernate.Domain;
import org.opennaas.extensions.idb.database.hibernate.Endpoint;
import org.opennaas.extensions.idb.database.hibernate.Reservation;
import org.opennaas.extensions.idb.database.hibernate.Service;
import org.opennaas.extensions.idb.exception.database.DatabaseException;
import org.opennaas.extensions.idb.utils.TopologyHelpers;
import org.opennaas.extensions.idb.test.AbstractTest;

/**
 * abstract test-class for DB-Tests. every test for a relation should contain
 * the basic actions of DB-handling. So there is one common main-workflow, which
 * should be tested: save, load, edit and delete a relation-tuple additional
 * referential integrity tests should be performed, if there exist references.
 * Important! Every single test should be independent of the others, because
 * there is nor ordering in JUnit!
 * 
 * @author Alexander Zimmermann (zimmerm2@cs.uni-bonn.de)
 */
public abstract class DatabaseTest extends AbstractTest {
	/**
	 * Test to load database properties.
	 * 
	 * @throws IOException
	 *             If the config file cannot be found.
	 */
	@Test
	public final void testDbProperties() throws IOException {

		final EntityManagerFactory config = Persistence
				.createEntityManagerFactory("persistence",
						System.getProperties());

	}

	/**
	 * create a reference domain in DB
	 * 
	 * @throws DatabaseException
	 */
	protected static Domain createReferenceDomain() throws DatabaseException {
		// create reference domain
		final Domain referenceDomain = new Domain(Helpers.getRandomString(),
				Helpers.getRandomString(), Helpers.getRandomString(),
				Helpers.getRandomString(), Helpers.getRandomString());
		referenceDomain.save();
		Assert.assertNotNull("reference domain should be set up correct",
				Domain.load(referenceDomain.getName()));

		return referenceDomain;
	}

	/**
	 * create a reference endpoint in DB
	 * 
	 * @throws DatabaseException
	 */
	protected static Endpoint createReferenceEndpoint(final String domainName)
			throws DatabaseException {
		// create reference endpoint

		final Endpoint referenceEndpoint = new Endpoint(
				TopologyHelpers.getRandomTNA(), Helpers.getRandomString(),
				Helpers.getRandomString(), Domain.load(domainName),
				EndpointInterfaceType.USER.ordinal(),
				Helpers.getPositiveRandomInt());

		referenceEndpoint.save();
		Assert.assertNotNull("reference endpoint should be set up correct",
				Endpoint.load(referenceEndpoint.getTNA()));

		return referenceEndpoint;
	}

	/**
	 * create a reference reservation in DB
	 * 
	 * @throws DatabaseException
	 */
	protected static Reservation createReferenceReservation()
			throws DatabaseException {
		// create reference reservation
		Reservation referenceReservation = new Reservation(0,
				"TestConsumerURL", Helpers.generateXMLCalendar(10, 0)
						.toGregorianCalendar().getTime(),
				Helpers.getPositiveRandomLong());
		referenceReservation.save();
		Assert.assertTrue("reference reservation should be set up correct",
				referenceReservation.getReservationId() > 0);

		return referenceReservation;
	}

	/**
	 * create a reference service in DB
	 * 
	 * @throws DatabaseException
	 */

	protected static Service createReferenceService(final Reservation res)
			throws DatabaseException {
		// create reference service

		final Service referenceService = new Service(
				Helpers.getPositiveRandomInt(), res,
				Helpers.getPositiveRandomInt(), Helpers
						.generateXMLCalendar(10, 0).toGregorianCalendar()
						.getTime(), Helpers.generateXMLCalendar(20, 0)
						.toGregorianCalendar().getTime(),
				Helpers.getPositiveRandomInt(), true);
		referenceService.save();
		Assert.assertTrue("reference service should be set up correct",
				referenceService.getPK_service() > 0);

		return referenceService;
	}

	/**
	 * create a reference connection in DB
	 * 
	 * @throws DatabaseException
	 */

	protected static Connections createReferenceConnection(
			final Service service, final Endpoint start, final Endpoint target)
			throws DatabaseException {
		// create reference connection

		final Connections referenceConnection = new Connections(
				Helpers.getPositiveRandomInt(), service,
				Helpers.getPositiveRandomInt(), Helpers.getPositiveRandomInt(),
				Helpers.getPositiveRandomInt(), Helpers.getPositiveRandomInt(),
				Helpers.getPositiveRandomInt(), start);
		referenceConnection.getEndpoints().add(target);

		referenceConnection.save();
		Assert.assertTrue("reference connection should be set up correct",
				referenceConnection.getPK_Connections() > 0);

		return referenceConnection;
	}

	/**
	 * actions to be performed before every single test
	 */
	@Before
	public abstract void setUpBeforeEveryTest();

	/**
	 * actions to be performed after every single test
	 */
	@After
	public abstract void tearDownAfterEveryTest();

	/**
	 * test referential integrity if existent
	 */
	@Test
	public abstract void testReferentialIntegrity() throws DatabaseException;

	/**
	 * test to save, load, edit and delete a relation-tuple
	 */
	@Test
	public abstract void testSaveLoadEditDeleteRelationTuple()
			throws DatabaseException;
}
