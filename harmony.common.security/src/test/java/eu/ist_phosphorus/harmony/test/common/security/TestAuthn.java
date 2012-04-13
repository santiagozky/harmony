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

package eu.ist_phosphorus.harmony.test.common.security;

import java.net.URISyntaxException;

import junit.framework.Assert;

import org.junit.Test;

import eu.ist_phosphorus.harmony.adapter.dummy.webservice.ReservationWS;
import eu.ist_phosphorus.harmony.common.security.utils.helper.ConfigHelper;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CreateReservationResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.exceptions.OperationNotAllowedFaultException;
import eu.ist_phosphorus.harmony.common.serviceinterface.reservation.IReservationWS;
import eu.ist_phosphorus.harmony.common.serviceinterface.reservation.SimpleReservationClient;
import eu.ist_phosphorus.harmony.common.utils.Config;

public class TestAuthn {

	private final SimpleReservationClient client;

	public TestAuthn() throws URISyntaxException {
		if (Config.isTrue("test", "test.callWebservice")) {
			this.client = new SimpleReservationClient(Config.getString("test",
					"test.reservationEPR"));
		} else {
			this.client = new SimpleReservationClient(new ReservationMock());
		}
	}

	private final void runTest() {
		try {
			CreateReservationResponseType response = this.client
					.createReservation(
							Config.getString("test", "test.endpoint0.tna"),
							Config.getString("test", "test.endpoint1.tna"),
							100, 800, 100);

			System.out.println("Created: " + response.getReservationID());

			Assert.assertTrue(true);
		} catch (OperationNotAllowedFaultException e) {
			System.out.println("\n\nUnable to complete request: "
					+ e.getMessage());

			e.printStackTrace();

			Assert.assertTrue(e.getMessage(), false);
		} catch (Exception e) {
			e.printStackTrace();

			Assert.assertTrue(false);
		}
	}

	@Test
	public final void testSignedCreate() throws Exception {
		System.out.println("Starting test...");

		ConfigHelper.setSigning(true);
		ConfigHelper.setEncryption(false);

		runTest();
	}

	@Test
	public final void testEncryptedCreate() throws Exception {
		System.out.println("Starting test...");

		ConfigHelper.setSigning(false);
		ConfigHelper.setEncryption(true);

		runTest();
	}

	@Test
	public final void testEncryptedSignedCreate() throws Exception {
		System.out.println("Starting test...");

		ConfigHelper.setSigning(true);
		ConfigHelper.setEncryption(true);

		runTest();
	}

}
