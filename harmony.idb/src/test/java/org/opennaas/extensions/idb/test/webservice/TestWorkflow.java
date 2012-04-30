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

package org.opennaas.extensions.idb.test.webservice;

import javax.xml.datatype.DatatypeConfigurationException;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.apache.muse.ws.addressing.soap.SoapFault;
import org.junit.Test;

import org.opennaas.extensions.idb.serviceinterface.reservation.SimpleReservationClient;
import org.opennaas.extensions.idb.serviceinterface.topology.SimpleTopologyClient;
import org.opennaas.core.utils.Config;
import org.opennaas.core.utils.PhLogger;
import org.opennaas.extensions.idb.Constants;
import org.opennaas.extensions.idb.exception.database.DatabaseException;
import org.opennaas.extensions.idb.webservice.ReservationWS;
import org.opennaas.extensions.idb.webservice.TopologyWS;

/**
 * 
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 * 
 */
public class TestWorkflow {
	/** The reservation client. */
	private SimpleReservationClient reservationClient;

	/** The topology client. */
	private SimpleTopologyClient topologyClient;

	/** The logger. */
	private final Logger logger;

	/**
	 * Default constructor.
	 */
	public TestWorkflow() {
		if (Config.isTrue(Constants.testProperties, "test.callWebservice")) {
			final String resEpr = Config.getString(Constants.testProperties,
					"epr.reservation");
			this.reservationClient = new SimpleReservationClient(resEpr);
			final String topoEpr = Config.getString(Constants.testProperties,
					"epr.topology");
			this.topologyClient = new SimpleTopologyClient(topoEpr);
		} else {
			this.reservationClient = new SimpleReservationClient(
					new ReservationWS());
			this.topologyClient = new SimpleTopologyClient(new TopologyWS());
		}
		this.logger = PhLogger.getLogger();
	}

	/**
	 * This workflow shows how the adapter is used by the IDB.
	 * 
	 * @throws SoapFault
	 * 
	 */
	@Test
	public void testSimpleReservationWorkflow() throws SoapFault,
			DatatypeConfigurationException {
		/* information about the requested path ----------------------------- */
		final int duration = 100; // seconds
		final int bandwidth = 100; // mbit/s
		final int delay = 800; // milliseconds
		final String source = "128.0.0.20";
		final String target = "128.0.0.21";
		/* ------------------------------------------------------------------ */

		/* add dummy domain ------------------------------------------------- */
		final String resEPR = Config.getString(Constants.testProperties,
				"default.domain.epr");
		final String identifier = Config.getString(Constants.testProperties,
				"default.domain.name");
		final String TNAPrefix = Config.getString(Constants.testProperties,
				"default.domain.prefix");
		this.topologyClient.addOrEditDomain(identifier, resEPR, TNAPrefix);
		/* ------------------------------------------------------------------ */

		// org.opennaas.extensions.idb.da.dummy.webservice.test.TestWorkflow
		// .testSimpleWorkflow(this.reservationClient, this.logger,
		// source, target, duration, bandwidth, delay);

		/* delete test domain ----------------------------------------------- */
		this.topologyClient.deleteDomain(identifier);
		/* ------------------------------------------------------------------ */

		Assert.assertTrue(true);

	}

}
