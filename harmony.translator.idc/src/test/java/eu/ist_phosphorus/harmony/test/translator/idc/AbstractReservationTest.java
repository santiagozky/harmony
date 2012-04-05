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

package eu.ist_phosphorus.harmony.test.translator.idc;

import static org.junit.Assert.assertTrue;
import net.es.oscars.wsdlTypes.GlobalReservationId;

import org.apache.axis2.AxisFault;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Test;

import eu.ist_phosphorus.harmony.common.serviceinterface.reservation.SimpleReservationClient;
import eu.ist_phosphorus.harmony.common.utils.Config;
import eu.ist_phosphorus.harmony.common.utils.PhLogger;
import eu.ist_phosphorus.harmony.translator.idc.implementation.IDCClient;
import eu.ist_phosphorus.harmony.translator.idc.webservice.ReservationWS;

public class AbstractReservationTest {
	/** The HSI reservation client */
	protected final SimpleReservationClient hsiRsvClient;
	/** The HSI reservation client */
	protected final IDCClient idcRsvClient;
	/** The logger */
	protected final Logger logger = PhLogger.getLogger("TranslatorRsvTest");

	protected String hsiResId;
	protected String idcResId;

	/**
	 * Default constructor.
	 * 
	 * @throws AxisFault
	 *             Good question.
	 */
	public AbstractReservationTest() throws AxisFault {
		/* ------------------------------------------------------------------ */
		if (Config.isTrue("test", "test.callWebservice")) {
			this.logger.info("Using HSI Webservice");
			this.hsiRsvClient = new SimpleReservationClient(Config.getString(
					"test", "test.reservationEPR"));
		} else {
			this.logger.info("Using direct HSI calls");
			this.hsiRsvClient = new SimpleReservationClient(new ReservationWS());
		}
		/* ------------------------------------------------------------------ */

		/* ------------------------------------------------------------------ */
		this.idcRsvClient = new IDCClient();
		/* ------------------------------------------------------------------ */
	}

	/**
	 * junit asks for all test classes to include at least one test or they will
	 * fail
	 */
	@Test
	public void fakeTest() {
		assertTrue(true);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() {
		try {
			if (null != this.hsiResId) {
				this.hsiRsvClient.cancelReservation(this.hsiResId);
			}
			if (null != this.idcResId) {
				final GlobalReservationId gri = new GlobalReservationId();
				gri.setGri(this.idcResId);
				this.idcRsvClient.cancelReservation(gri);
			}
		} catch (final Exception e) {
			this.logger.info("In tear down: " + e.getMessage());
		}
	}

}
