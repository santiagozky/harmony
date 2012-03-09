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

package eu.ist_phosphorus.harmony.test.testbed;

import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.muse.ws.addressing.soap.SoapFault;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.w3c.dom.Element;

import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.AvailabilityCodeType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CancelReservationResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CancelReservationType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.ConnectionAvailabilityType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CreateReservationResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.DomainStatusType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetReservation;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetReservationResponse;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetReservationResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetReservationType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetStatusResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.IsAvailableResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.ServiceConstraintType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.StatusType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetStatusResponseType.ServiceStatus;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.utils.JaxbSerializer;
import eu.ist_phosphorus.harmony.common.utils.Config;
import eu.ist_phosphorus.harmony.common.utils.Helpers;
import eu.ist_phosphorus.harmony.common.utils.Tuple;

/**
 * JUnit test suite to test all the links in the testbed.
 * 
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 * @version $Id:TestReservations.java 2311 2008-02-23 19:01:02Z
 *          willner@cs.uni-bonn.de $
 */
public abstract class AbstractTestbedTest extends AbstractReservationTest {
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
    private IsAvailableResponseType availability;
    private String availabilityCode;

    public AbstractTestbedTest(final String epr) {
        super(epr);
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

    protected String createMalleableReservation(final String src,
            final String dst, final XMLGregorianCalendar startTime,
            final XMLGregorianCalendar deadline, final int dataAmount)
            throws SoapFault {
        return this.processCreateReservationResponse(this
                .createMalleableTestReservation(src, dst, startTime, deadline,
                        dataAmount));
    }

    protected String createMultiConnectionReservation(final int duration,
            final Tuple<String, String>... ep) throws SoapFault {

        XMLGregorianCalendar startTime;
        try {
            startTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(
                    new GregorianCalendar());
        } catch (final DatatypeConfigurationException e) {
            throw new UnexpectedFaultException(
                    "A serious configuration error was detected ...", e);
        }

        return this.processCreateReservationResponse(this.reservationClient
                .createReservation(Config.getInt("test", "default.bandwidth")
                        .intValue(), Config.getInt("test", "default.delay")
                        .intValue(), duration, startTime, ep));
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

    private String processCreateReservationResponse(
            final CreateReservationResponseType reservationResponse) {
        Assert.assertTrue("Reservation ID is empty", reservationResponse
                .getReservationID().length() > 0);
        final String reservationId = reservationResponse.getReservationID();
        this.reservationIds.add(reservationId);
        System.out.println("    reservationId:" + reservationId);
        return reservationId;
    }

    protected void runFixedSecuredTest(final String description,
            final String src, final String dst) {
        final String samlAssertion = ""; // TODO: we need a test assertion here

        try {
            final CreateReservationResponseType myResult = this.reservationClient
                    .createReservation(src, dst, this.bandwidth, this.delay,
                            this.duration, samlAssertion);
            this.runFixedTest(description, src, dst, myResult);
        } catch (final SoapFault exception) {
            this.exception = exception.getMessage().replace("\n", " ");
            Assert.fail("An exception occured: " + this.exception);
        }
    }

    protected void runFixedTest(final String description, final String src,
            final String dst) {
        try {
            final CreateReservationResponseType myResult = this.reservationClient
                    .createReservation(src, dst, this.bandwidth, this.delay,
                            this.duration);
            this.runFixedTest(description, src, dst, myResult);
        } catch (final SoapFault exception) {
            this.exception = exception.getMessage().replace("\n", " ");
            Assert.fail("An exception occured: " + this.exception);
        }
    }

    /**
     * Tests a connection.
     * 
     * @param src
     *            Source endpoint.
     * @param dst
     *            Target endpoint.
     */
    private void runFixedTest(final String description, final String src,
            final String dst, final CreateReservationResponseType myResult) {
        System.out.print("Testing " + description + " (fixed): " + src + " to "
                + dst + "...");

        try {
            /* create reservation ------------------------------------------- */
            this.result = myResult;
            System.out.print("LRI: " + this.result.getReservationID() + "...");
            Assert.assertTrue("Reservation should return an LRI.", this.result
                    .isSetReservationID());
            long start = System.currentTimeMillis();
            long end = System.currentTimeMillis();
            Thread.sleep(this.signalBuffer);
            /* -------------------------------------------------------------- */

            /* wait till it is active --------------------------------------- */
            boolean isActive = false;
            boolean isCancelled = false;
            this.status = this.reservationClient.getStatus(this.result
                    .getReservationID());
            this.statusList = this.status.getServiceStatus();
            this.currentStatus = this.statusList.iterator().next().getStatus();
            System.out.print("status: " + this.currentStatus + "...");

            for (int i = 4; i <= 9; i++) {
                isActive = this.isStatus(this.statusList, StatusType.ACTIVE);
                isCancelled = this.isStatus(this.statusList,
                        StatusType.CANCELLED_BY_SYSTEM);
                if (isCancelled) {
                    System.out.print("(now: " + this.currentStatus + " (after "
                            + (end - start) / 1000 + "s)...");
                    break;
                }
                if (!isActive) {
                    Thread.sleep(this.signalBuffer * i);
                } else {
                    end = System.currentTimeMillis();
                    System.out.print("now: " + this.currentStatus + " (after "
                            + (end - start) / 1000 + "s)...");
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

            /* test availability -------------------------------------------- */
            final XMLGregorianCalendar now = Helpers.generateXMLCalendar();
            this.availability = this.checkAvailability(src, dst, now,
                    this.duration);
            Assert.assertTrue("Should have a result", this.availability
                    .isSetDetailedResult());
            Assert.assertTrue("Should have exactly one connection",
                    this.availability.getDetailedResult().size() == 1);

            for (final ConnectionAvailabilityType ca : this.availability
                    .getDetailedResult()) {
                this.availabilityCode = ca.getAvailability().name();
                Assert.assertEquals("Should be not available",
                        AvailabilityCodeType.ENDPOINT_NOT_AVAILABLE.toString(),
                        this.availabilityCode);
            }
            System.out.print("(endpoints blocked)...");

            if (this.availability.isSetAlternativeStartTimeOffset()) {
                final long altStart = this.availability
                        .getAlternativeStartTimeOffset();
                System.out.print("offset: " + altStart + "...");
                Assert.assertTrue(
                        "Should contain start time offset greater than 0",
                        altStart > 0);
                Assert.assertTrue(
                        "Should contain start time offset no greater "
                                + "than the previous duration +  1 minute ("
                                + (this.duration + 60) + "s)",
                        altStart <= this.duration + 60);
            } else {
                System.out.print("offset: not set...");
            }

            /* -------------------------------------------------------------- */

            /* cancel the reservation --------------------------------------- */
            this.reservationClient.cancelReservation(this.result
                    .getReservationID());

            isCancelled = false;
            start = System.currentTimeMillis();
            for (int i = 1; !isCancelled; i++) {
                Thread.sleep(this.signalBuffer * i);
                this.status = this.reservationClient.getStatus(this.result
                        .getReservationID());
                this.statusList = this.status.getServiceStatus();
                isCancelled = this.isStatus(this.statusList,
                        StatusType.CANCELLED_BY_USER)
                        || this.isStatus(this.statusList,
                                StatusType.CANCELLED_BY_SYSTEM);
                System.out.print(".");
            }
            end = System.currentTimeMillis();
            System.out.println("finally: "
                    + this.statusList.iterator().next().getStatus()
                    + " (after " + (end - start) / 1000 + "s).");
            Assert.assertTrue("Services should be cancelled.", this.isStatus(
                    this.statusList, StatusType.CANCELLED_BY_USER));
            this.success = true;
            /* -------------------------------------------------------------- */
        } catch (final SoapFault exception) {
            this.exception = exception.getMessage().replace("\n", " ");
            Assert.fail("An exception occured: " + this.exception);
        } catch (final InterruptedException exception) {
            this.exception = exception.getMessage().replace("\n", " ");
            Assert.fail("An exception occured: " + this.exception);
        }
    }

    /**
     * Tests a connection.
     * 
     * @param description
     * @param src
     * @param dst
     * @param bandwidth
     */
    protected void runFixedTest(final String description, final String src,
            final String dst, final int bandwidth) {
        this.bandwidth = bandwidth;
        this.runFixedTest(description, src, dst);
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

    protected void testMalleableReservation(final String src, final String dst,
            final XMLGregorianCalendar startTime,
            final XMLGregorianCalendar deadline, final int dataAmount)
            throws Exception {

        System.out.println("Running testMalleableReservation from " + src
                + " to " + dst + " ...");

        /* create reservation ----------------------------------------------- */
        System.out.println("  [1/5] create reservation ...");
        this.reservationId = this.createMalleableReservation(src, dst,
                startTime, deadline, dataAmount);
        /* ------------------------------------------------------------------ */

        /* ------------------------------------------------------------------ */
        System.out.println("  sleeping " + this.rsvSignalBuffer + "ms ...");
        Thread.sleep(this.rsvSignalBuffer);
        System.out.println("  [2/5] get reservation ...");
        final GetReservationType getResvReq = new GetReservationType();

        getResvReq.setReservationID(this.reservationId);
        final GetReservation envelope = new GetReservation();
        envelope.setGetReservation(getResvReq);
        final Element reqElement = JaxbSerializer.getInstance()
                .objectToElement(envelope);
        final Element resElement = this.reservationClient
                .getReservation(reqElement);
        final GetReservationResponse response = (GetReservationResponse) JaxbSerializer
                .getInstance().elementToObject(resElement);
        final GetReservationResponseType getResvResponse = response
                .getGetReservationResponse();
        Assert.assertTrue(
                "GetReservationsResponse should contain exactly 1 service",
                getResvResponse.getService().size() == 1);
        final ServiceConstraintType service = getResvResponse.getService().get(
                0);
        System.out.println("    start time:"
                + service.getFixedReservationConstraints().getStartTime()
                        .toString());
        System.out.println("    duration:"
                + service.getFixedReservationConstraints().getDuration());
        /* ------------------------------------------------------------------ */

        /* ------------------------------------------------------------------ */
        System.out.println("  [3/5] get status ...");
        Thread.sleep(this.rsvSignalBuffer);
        final GetStatusResponseType status = this.checkConnectionStatus(
                this.reservationId, StatusType.PENDING);
        final Set<String> domains = new HashSet<String>();
        for (final ServiceStatus s : status.getServiceStatus()) {
            for (final DomainStatusType ds : s.getDomainStatus()) {
                domains.add(ds.getDomain());
            }
        }
        System.out.print("involved domains:");
        for (final String domain : domains) {
            System.out.print(" " + domain);
        }
        System.out.println();
        /* ------------------------------------------------------------------ */

        /* ------------------------------------------------------------------ */
        System.out.println("  [4/5] cancel reservation ...");
        this.cancelReservation(this.reservationId);
        /* ------------------------------------------------------------------ */

        /* ------------------------------------------------------------------ */
        System.out.println("  [5/5] get status ...");
        Thread.sleep(this.signalBuffer);
        this.checkConnectionStatus(this.reservationId,
                StatusType.CANCELLED_BY_USER);
        /* ------------------------------------------------------------------ */

        this.success = true;
    }

    protected void testMultiConnectionReservation(final int duration,
            final Tuple<String, String>... ep) throws Exception {

        System.out.println("Running multiple connection test ...");

        /* create reservation ----------------------------------------------- */
        System.out.println("  create reservation ...");
        this.reservationId = this
                .createMultiConnectionReservation(duration, ep);
        /* ------------------------------------------------------------------ */

        /* ------------------------------------------------------------------ */
        System.out.println("  get status ...");
        Thread.sleep(this.rsvSignalBuffer);
        this.checkConnectionStatus(this.reservationId, StatusType.ACTIVE);
        /* ------------------------------------------------------------------ */

        /* ------------------------------------------------------------------ */
        System.out.println("  cancel reservation ...");
        this.cancelReservation(this.reservationId);
        /* ------------------------------------------------------------------ */

        /* ------------------------------------------------------------------ */
        System.out.println("  get status ...");
        Thread.sleep(this.signalBuffer);
        this.checkConnectionStatus(this.reservationId,
                StatusType.CANCELLED_BY_USER);
        /* ------------------------------------------------------------------ */

        this.success = true;

    }

}
