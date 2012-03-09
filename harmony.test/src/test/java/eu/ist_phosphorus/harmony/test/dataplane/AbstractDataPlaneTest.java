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


package eu.ist_phosphorus.harmony.test.dataplane;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import junit.framework.Assert;

import org.apache.muse.ws.addressing.soap.SoapFault;
import org.junit.After;
import org.junit.Before;

import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CancelReservationResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CancelReservationType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CreateReservationResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetStatusResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.StatusType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetStatusResponseType.ServiceStatus;

import eu.ist_phosphorus.harmony.test.testbed.AbstractReservationTest;

/**
 * 
 * JUnit tests suite to test data plane status. Workflow: 1. Create reservation
 * 2. Check Host is Available & Reachable 3. Cancel Reservation
 * 
 * @author Jordi Ferrer Riera (jordi.ferrer@i2cat.net)
 * @version 0.1
 * 
 * This class uses the classes created by A. Willner for the real testbed
 * service plane tests.
 * 
 */
public abstract class AbstractDataPlaneTest extends AbstractReservationTest {

	/** * The status response */
	private GetStatusResponseType status;
	/** * List of statuses for a reservation */
	private List<ServiceStatus> statusList;
	/** * The create reservation response */
	private CreateReservationResponseType result;
	/** * True if the test was successful */
	private boolean success;
	/** * Store the exception message */
	private String exception;
	private StatusType currentStatus;
	private boolean pingSuccess;

	public AbstractDataPlaneTest(final String epr) {
		super(epr);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.success = false;
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		if (!this.success) {
			System.out.println("FAILED: " + this.exception);
			if (null != this.result) {
				this.reservationClient.cancelReservation(this.result
						.getReservationID());
			}
			if (null != this.reservationId) {
				this.reservationClient.cancelReservation(this.reservationId);
			}
		}
	}

	private boolean isStatus(final List<ServiceStatus> statusList,
			final StatusType expected) {
		boolean isStatus = false;

		for (final ServiceStatus status : statusList) {
			if (status.getStatus() == expected) {
				isStatus = true;
			}
		}

		return isStatus;
	}

	protected void cancelReservation(final String reservationId)
			throws SoapFault {
		final CancelReservationType cancelRequest = new CancelReservationType();
		cancelRequest.setReservationID(reservationId);
		final CancelReservationResponseType cancelResponse = this.reservationClient
				.cancelReservation(cancelRequest);
		Assert.assertTrue("Cancel should succeed", cancelResponse
				.isSetSuccess());
		this.reservationIds.remove(reservationId);
	}

	protected String createFixedReservation(final String src, final String dst,
			final XMLGregorianCalendar startTime, final int duration)
			throws Exception {
		return this.processCreateReservationResponse(this
				.createFixedTestReservation(src, dst, startTime, duration));
	}

	private String processCreateReservationResponse(
			final CreateReservationResponseType reservationResponse) {
		Assert.assertTrue("Reservation ID is empty", reservationResponse
				.getReservationID().length() > 0);
		final String reservationId = reservationResponse.getReservationID();
		this.reservationIds.add(reservationId);
		System.out.println("    reservationId:" + reservationId);
		return reservationId;
	}
	
	protected void runDataPlaneTest(final String description, final String src, final String dst,final String host, int bw) {
		
		this.bandwidth = bw;
		this.runDataPlaneTest(description, src,dst, host);
		
	}

	/**
	 * Tests a connection.
	 * 
	 * @param src
	 *            Source endpoint.
	 * @param dst
	 *            Target endpoint.
	 * @param hostDst
	 *            Target IP-host to ping
	 */
	protected void runDataPlaneTest(final String description, final String src,
			final String dst, String hostDst) {
		System.out.print("Dataplane: " + description + ": " + src + " to "
				+ dst + "(" + hostDst + ")" + "...");

		try {
			/* create reservation ------------------------------------------- */
			this.result = this.reservationClient.createReservation(src, dst,
					this.bandwidth, this.delay, this.duration);
			System.out.print("LRI: " + this.result.getReservationID() + "...");
			Assert.assertTrue("Reservation should return an LRI.", this.result
					.isSetReservationID());
			final long start = System.currentTimeMillis();
			Thread.sleep(this.signalBuffer);
			/* -------------------------------------------------------------- */

			/* wait till it is active --------------------------------------- */
			boolean isActive = false;
			this.status = this.reservationClient.getStatus(this.result
					.getReservationID());
			this.statusList = this.status.getServiceStatus();
			this.currentStatus = this.statusList.iterator().next().getStatus();
			System.out.print("status: " + this.currentStatus + "...");

			for (int i = 4; i <= 9; i++) {
				isActive = this.isStatus(this.statusList, StatusType.ACTIVE);
				if (!isActive) {
					Thread.sleep(this.signalBuffer * i);
				} else {
					final long end = System.currentTimeMillis();
					System.out.print("(waited " + (end - start) / 1000
							+ "s)...");
					break;
				}
				this.status = this.reservationClient.getStatus(this.result
						.getReservationID());
				this.statusList = this.status.getServiceStatus();
				this.currentStatus = this.statusList.iterator().next()
						.getStatus();
				System.out.print(".");
			}
			Assert.assertTrue("Services should be active.", isActive);
			Thread.sleep(this.signalBuffer * 2); // * 2 for IDC
			/* -------------------------------------------------------------- */

			// TODO Fix this prudential time wait.
			Thread.sleep(60000);
			/* test host available and reachable ---------------------------- */
			this.pingSuccess = InetAddress.getByName(hostDst).isReachable(5000);

			Assert.assertTrue("Host should be reachable.", this.pingSuccess);
			Thread.sleep(10000);
			if (this.pingSuccess)
				System.out.print("Host reachable. ");

			/* -------------------------------------------------------------- */

			/* cancel the reservation --------------------------------------- */
			this.reservationClient.cancelReservation(this.result
					.getReservationID());
			Thread.sleep(this.signalBuffer);

			this.status = this.reservationClient.getStatus(this.result
					.getReservationID());
			this.statusList = this.status.getServiceStatus();
			System.out.println("finally: "
					+ this.statusList.iterator().next().getStatus());
			Assert.assertTrue("Services should be cancelled.", this.isStatus(
					this.statusList, StatusType.CANCELLED_BY_USER)
					|| this.isStatus(this.statusList,
							StatusType.TEARDOWN_IN_PROGRESS));
			this.success = true;
			/* -------------------------------------------------------------- */
		} catch (SoapFault exception) {
			this.exception = exception.getMessage().replace("\n", " ");
			Assert.fail("An exception occured: " + this.exception);
		} catch (InterruptedException exception) {
			this.exception = exception.getMessage().replace("\n", " ");
			Assert.fail("An exception occured: " + this.exception);
		} catch (UnknownHostException exception) {
			this.exception = exception.getMessage().replace("\n", " ");
			Assert.fail("An exception occured: " + this.exception);
		} catch (IOException exception) {
			this.exception = exception.getMessage().replace("\n", " ");
			Assert.fail("An exception occurred: " + this.exception);
		}
	}

}
