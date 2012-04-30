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

import org.junit.Assert;
import org.junit.Test;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DomainInformationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DomainTechnologyType;
import org.opennaas.core.utils.Helpers;
import org.opennaas.extensions.idb.database.hibernate.Connections;
import org.opennaas.extensions.idb.database.hibernate.DomSupportedAdaption;
import org.opennaas.extensions.idb.database.hibernate.DomSupportedBandwidth;
import org.opennaas.extensions.idb.database.hibernate.DomSupportedSwitch;
import org.opennaas.extensions.idb.database.hibernate.Domain;
import org.opennaas.extensions.idb.database.hibernate.Endpoint;
import org.opennaas.extensions.idb.database.hibernate.NrpsConnections;
import org.opennaas.extensions.idb.database.hibernate.Reservation;
import org.opennaas.extensions.idb.database.hibernate.Service;
import org.opennaas.extensions.idb.database.hibernate.TNAPrefix;
import org.opennaas.extensions.idb.exception.database.DatabaseException;
import org.opennaas.extensions.idb.utils.ReservationHelpers;
import org.opennaas.extensions.idb.utils.TopologyHelpers;

public class TestDomainRelation extends DatabaseTest {

	/** test domain. */
	private Domain domain;

	@Override
	public void setUpBeforeEveryTest() {
		// create test domain
		try {
			this.domain = TopologyHelpers.getTestDomain();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void tearDownAfterEveryTest() {
		// Nothing?!
	}

	@Override
	public void testSaveLoadEditDeleteRelationTuple() throws DatabaseException {
		// save
		this.domain.save();
		Assert.assertTrue("length of domain-name should be greater than 0",
				this.domain.getName().length() > 0);

		// load
		final Domain loadedDom = Domain.load(this.domain.getName());
		Assert.assertEquals("persisted and loaded domain should be equal",
				this.domain, loadedDom);

		// edit
		loadedDom.setDescription("edited Description");
		loadedDom.save();

		// delete
		loadedDom.delete();
		Assert.assertNull("test Domain should be deleted",
				Domain.load(this.domain.getName()));
	}

	@Override
	public void testReferentialIntegrity() throws DatabaseException {
		// create reference endpoint and put it in the domain object
		Endpoint endpoint = TopologyHelpers
				.getTestEndpointForDomain(this.domain);
		this.domain.addEndpoint(endpoint);

		// save the changed domain
		this.domain.save();

		// load domain new from DB and check if endpoint is existent
		Domain testDom = Domain.load(this.domain.getName());
		Assert.assertNotNull("there should be endpoints in the domain",
				testDom.getEndpoints());
		Assert.assertTrue("stored endpoint should be existent", testDom
				.getEndpoints().contains(endpoint));

		// remove the endpoint in the domain object and save this
		// -> the endpoint should also be removed
		testDom.getEndpoints().remove(endpoint);
		testDom.save();

		// load domain new from DB and check if endpoint is not existent anymore
		testDom = Domain.load(this.domain.getName());
		Assert.assertFalse("there should be no endpoint anymore", testDom
				.getEndpoints().contains(endpoint));
		Assert.assertNull("the endpoint should be deleted",
				Endpoint.load(endpoint.getTNA()));

		// delete the test domain
		testDom.delete();
	}

	@Test
	public void testReferentialIntegrity2() throws DatabaseException,
			URISyntaxException {
		// create reference TNAPrefix and put it in the domain object
		TNAPrefix prefix = TopologyHelpers.getRandomPrefix(this.domain);
		this.domain.addPrefix(prefix);
		// save the changed domain
		this.domain.save();

		// load domain new from DB and check if TNAPrefix is existent
		Domain testDom = Domain.load(this.domain.getName());
		Assert.assertNotNull("there should be prefixes in the domain",
				testDom.getPrefixes());
		Assert.assertTrue("stored prefix should be existent", testDom
				.getPrefixes().contains(prefix));

		// remove the TNAPrefix in the domain object and save this
		// -> the TNAPrefix should also be removed
		testDom.getPrefixes().remove(prefix);
		prefix.delete();

		// load domain new from DB and check if TNAPrefix is not existent
		// anymore
		testDom = Domain.load(this.domain.getName());
		Assert.assertFalse("there should be no TNAPrefix anymore", testDom
				.getPrefixes().contains(prefix));
		Assert.assertNull("the TNAPrefix should be deleted",
				TNAPrefix.load(prefix.getPrefix()));

		// delete the test domain
		testDom.delete();
	}

	@Test
	public void testReferentialIntegrity3() throws DatabaseException,
			URISyntaxException {
		// create reference domain, reservation, service and connection
		Domain dom = TopologyHelpers.getTestDomain();
		Endpoint end1 = (Endpoint) dom.getEndpoints().toArray()[0];
		Endpoint end2 = (Endpoint) dom.getEndpoints().toArray()[1];
		dom.save();
		end1.save();
		end2.save();
		Reservation res = TestDomainRelation.createReferenceReservation();
		Service service = TestDomainRelation.createReferenceService(res);
		Connections conn = ReservationHelpers.getTestConnection();
		conn.setService(service);
		conn.setStartpoint(end1);
		conn.getEndpoints().add(end2);
		conn.save();

		// create reference NRPSConnection and put it in the domain object
		NrpsConnections nrpsConn = ReservationHelpers.getTestNrpsConnection();
		nrpsConn.setPkNrpsConnection(0);
		nrpsConn.setConnection(conn);
		nrpsConn.setSourceEndpoint((Endpoint) this.domain.getEndpoints()
				.toArray()[0]);
		nrpsConn.setDestinationEndpoint((Endpoint) this.domain.getEndpoints()
				.toArray()[1]);
		this.domain.addNrpsConnection(nrpsConn);
		// save the changed domain
		this.domain.save();

		// load domain new from DB and check if NrpsConnection is existent
		Domain testDom = Domain.load(this.domain.getName());
		Assert.assertNotNull("there should be nrpsConnections in the domain",
				testDom.getNrpsConnections());
		Assert.assertTrue("stored nrpsConnection should be existent", testDom
				.getNrpsConnections().contains(nrpsConn));

		// remove the NrpsConnection in the domain object and save this
		// -> the NrpsConnection should also be removed
		testDom.getNrpsConnections().remove(nrpsConn);
		testDom.save();

		// load domain new from DB and check if nrpsConnection is not existent
		// anymore
		testDom = Domain.load(this.domain.getName());
		Assert.assertFalse("there should be no nrpsConnection anymore", testDom
				.getNrpsConnections().contains(nrpsConn));
		Assert.assertNull("the nrpsConnection should be deleted",
				NrpsConnections.load(nrpsConn.getPkNrpsConnection()));

		// delete the test domain
		testDom.delete();
		// delete the reference reservation and domain
		res.delete();
		dom.delete();
	}

	@Test
	public void testReservationCleaning() throws DatabaseException {
		Endpoint end1 = (Endpoint) this.domain.getEndpoints().toArray()[0];
		Endpoint end2 = (Endpoint) this.domain.getEndpoints().toArray()[1];
		// save domain
		this.domain.save();
		end1.save();
		end2.save();

		Reservation res = TestDomainRelation.createReferenceReservation();
		Service service = TestDomainRelation.createReferenceService(res);
		TestDomainRelation.createReferenceConnection(service, end1, end2);

		this.domain.delete();
		Reservation nullRes = Reservation.load(res.getReservationId());
		Assert.assertNull(nullRes);
	}

	@Test
	public void testReferentialIntegrity4() throws DatabaseException {
		// create reference DomSupportedAdaption and put it in the domain object
		DomSupportedAdaption adaption = new DomSupportedAdaption();
		adaption.setPK_Adaption(0);
		adaption.setAdaption(Helpers.getRandomString());
		this.domain.addAdaption(adaption);

		// save the changed domain
		this.domain.save();

		// get the adaption from the domain, because of generated primary key
		adaption = this.domain.getSupportedAdaptions().iterator().next();

		// load domain new from DB and check if adaption is existent
		Domain testDom = Domain.load(this.domain.getName());
		Assert.assertNotNull(
				"there should be DomSupportAdaptions in the domain",
				testDom.getSupportedAdaptions());
		Assert.assertEquals("stored adaption should be existent", testDom
				.getSupportedAdaptions().iterator().next(), adaption);

		// remove the adaption in the domain object and save this
		// -> the adaption should also be removed
		DomSupportedAdaption ad = testDom.getSupportedAdaptions().iterator()
				.next();
		testDom.getSupportedAdaptions().clear();
		ad.delete();
		testDom.save();

		// load domain new from DB and check if adaption is not existent
		// anymore
		testDom = Domain.load(this.domain.getName());
		Assert.assertTrue("there should be no adaptions anymore", testDom
				.getSupportedAdaptions().isEmpty());
		Assert.assertNull("the adaption should be deleted",
				DomSupportedAdaption.load(ad.getPK_Adaption()));

		// delete the test domain
		testDom.delete();
	}

	@Test
	public void testReferentialIntegrity5() throws DatabaseException {
		// create reference DomSupportedSwitch and put it in the domain object
		DomSupportedSwitch switchd = new DomSupportedSwitch();
		switchd.setPK_Switch(0);
		switchd.setSwitch(Helpers.getRandomString());
		this.domain.addSwitch(switchd);

		// save the changed domain
		this.domain.save();

		// get the switch from the domain, because of generated primary key
		switchd = this.domain.getSupportedSwitchMatrix().iterator().next();

		// load domain new from DB and check if switch is existent
		Domain testDom = Domain.load(this.domain.getName());
		Assert.assertNotNull(
				"there should be DomSupportSwitches in the domain",
				testDom.getSupportedSwitchMatrix());
		Assert.assertEquals("stored switch should be existent", testDom
				.getSupportedSwitchMatrix().iterator().next(), switchd);

		// remove the switch in the domain object and save this
		// -> the switch should also be removed
		DomSupportedSwitch sw = testDom.getSupportedSwitchMatrix().iterator()
				.next();
		testDom.getSupportedSwitchMatrix().clear();
		sw.delete();
		testDom.save();

		// load domain new from DB and check if switch is not existent
		// anymore
		testDom = Domain.load(this.domain.getName());
		Assert.assertTrue("there should be no switches anymore", testDom
				.getSupportedSwitchMatrix().isEmpty());
		Assert.assertNull("the switch should be deleted",
				DomSupportedSwitch.load(sw.getPK_Switch()));

		// delete the test domain
		testDom.delete();
	}

	@Test
	public void testReferentialIntegrity6() throws DatabaseException {
		// create reference DomSupportedBandwidth and put it in the domain
		// object
		DomSupportedBandwidth bandwidth = new DomSupportedBandwidth();
		bandwidth.setPK_Bandwidth(0);
		bandwidth.setBandwidth(Helpers.getPositiveRandomLong());
		this.domain.addBandwidth(bandwidth);

		// save the changed domain
		this.domain.save();

		// get the bandwidth from the domain, because of generated primary key
		bandwidth = this.domain.getSupportedBandwidths().iterator().next();

		// load domain new from DB and check if bandwidth is existent
		Domain testDom = Domain.load(this.domain.getName());
		Assert.assertNotNull(
				"there should be DomSupportBandwidths in the domain",
				testDom.getSupportedBandwidths());
		Assert.assertEquals("stored bandwidth should be existent", testDom
				.getSupportedBandwidths().iterator().next(), bandwidth);

		// remove the bandwidth in the domain object and save this
		// -> the bandwidth should also be removed
		DomSupportedBandwidth bw = testDom.getSupportedBandwidths().iterator()
				.next();
		testDom.getSupportedBandwidths().clear();
		bw.delete();
		testDom.save();

		// load domain new from DB and check if bandwidth is not existent
		// anymore
		testDom = Domain.load(this.domain.getName());
		Assert.assertTrue("there should be no bandwidths anymore", testDom
				.getSupportedBandwidths().isEmpty());
		Assert.assertNull("the bwndwidth should be deleted",
				DomSupportedBandwidth.load(bw.getPK_Bandwidth()));

		// delete the test domain
		testDom.delete();
	}

	@Test
	public void testTechnologyType() throws DatabaseException,
			URISyntaxException {
		// create random domain
		Domain randomDom = TopologyHelpers.getTestDomain();
		DomainInformationType dit = randomDom.toJaxb();
		// add technology type
		DomainTechnologyType dtt = new DomainTechnologyType();
		dtt.getDomainSupportedAdaptation().add(Helpers.getRandomString());
		dtt.getDomainSupportedBandwidth().add(Helpers.getPositiveRandomLong());
		dtt.getDomainSupportedSwitchMatrix().add(Helpers.getRandomString());
		dit.setTechnology(dtt);

		Domain testDom1 = Domain.fromJaxb(dit);
		testDom1.save();

		Domain testDom2 = Domain.load(testDom1.getName());
		DomainInformationType dit2 = testDom2.toJaxb();
		Assert.assertTrue("adaption should be the same",
				dit2.getTechnology().getDomainSupportedAdaptation().get(0)
						.equals(dtt.getDomainSupportedAdaptation().get(0)));
		Assert.assertTrue("bandwidth should be the same",
				dit2.getTechnology().getDomainSupportedBandwidth().get(0)
						.equals(dtt.getDomainSupportedBandwidth().get(0)));
		Assert.assertTrue("switch should be the same",
				dit2.getTechnology().getDomainSupportedSwitchMatrix().get(0)
						.equals(dtt.getDomainSupportedSwitchMatrix().get(0)));

		// delete
		testDom1.delete();
	}
}