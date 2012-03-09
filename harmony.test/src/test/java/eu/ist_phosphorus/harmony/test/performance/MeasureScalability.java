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


package eu.ist_phosphorus.harmony.test.performance;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.muse.ws.addressing.soap.SoapFault;
import org.junit.Test;
import org.w3c.dom.Element;

import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CreateReservation;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CreateReservationType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.IsAvailable;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.IsAvailableType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.exceptions.InvalidRequestFaultException;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.utils.JaxbSerializer;
import eu.ist_phosphorus.harmony.common.serviceinterface.reservation.IReservationWS;
import eu.ist_phosphorus.harmony.common.serviceinterface.reservation.SimpleReservationClient;
import eu.ist_phosphorus.harmony.common.serviceinterface.topology.SimpleTopologyClient;
import eu.ist_phosphorus.harmony.common.utils.Tuple;
import eu.ist_phosphorus.harmony.idb.webservice.TopologyWS;
import eu.ist_phosphorus.harmony.test.performance.MeasureThread.RequestType;

/**
 * Measure the scalability of the HSI infra structure.
 * 
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 * @version $Id$
 */
public class MeasureScalability {
	/** The reservation client */
	private final SimpleReservationClient reservationClient;
	private SimpleTopologyClient topologyClient;
	private Logger durationLogger;
	private Logger successLogger;
	private Logger errorLogger;
	private Properties properties;
	private int repititions;
	private int max_num_threads;
	private String src;
	private String dst;
	private int duration;
	private int bandwidth;
	private RequestType type;
	private String test_entity;
	private boolean use_ws;
	private int delay;
	private int waitingtime_in_thread;
	private String hsi_tpy_idb_epr;
	private String hsi_rsv_first_epr;
	private String hsi_rsv_mock_epr;
	private String propertyFile;
	private String hsi_rsv_first_class;
	private int start_num_threads;
	private int step_num_threads;

	/**
	 * @throws SoapFault
	 * @throws IOException
	 */
	public MeasureScalability() throws SoapFault, IOException {
		/* ----------------------------------------------------------------- */
		this.propertyFile = System.getenv("measureProperties");
		if (null == this.propertyFile)
			propertyFile = "Test.properties";
		this.properties = new Properties();
		FileInputStream stream = new FileInputStream(this.propertyFile);
		properties.load(stream);
		stream.close();
		this.use_ws = Boolean.parseBoolean(this.properties
				.getProperty("USE_WS"));
		this.repititions = Integer.parseInt(this.properties
				.getProperty("REPETITIONS"));
		this.max_num_threads = Integer.parseInt(this.properties
				.getProperty("MAX_NUM_THREADS"));
		this.start_num_threads = Integer.parseInt(this.properties
				.getProperty("START_NUM_THREADS"));
		this.step_num_threads = Integer.parseInt(this.properties
				.getProperty("STEP_NUM_THREADS"));
		this.duration = Integer.parseInt(this.properties
				.getProperty("DURATION"));
		this.bandwidth = Integer.parseInt(this.properties
				.getProperty("BANDWIDTH"));
		this.delay = Integer.parseInt(this.properties.getProperty("DELAY"));
		this.waitingtime_in_thread = Integer.parseInt(this.properties
				.getProperty("WAITINGTIME_IN_THREAD"));
		this.src = this.properties.getProperty("SRC");
		this.dst = this.properties.getProperty("DST");
		this.test_entity = this.properties.getProperty("TEST_ENTITY");
		this.hsi_rsv_first_epr = this.properties
				.getProperty("HSI_RSV_FIRST_EPR");
		this.hsi_rsv_first_class = this.properties
				.getProperty("HSI_RSV_FIRST_CLASS");
		this.hsi_tpy_idb_epr = this.properties.getProperty("HSI_TPY_IDB_EPR");
		this.hsi_rsv_mock_epr = this.properties.getProperty("HSI_RSV_MOCK_EPR");
		final String typeTemp = this.properties.getProperty("TYPE");
		if (typeTemp.equals("CREATE"))
			this.type = RequestType.CREATE;
		else if (typeTemp.equals("ISAVAILABLE"))
			this.type = RequestType.ISAVAILABLE;
		/* ----------------------------------------------------------------- */

		/* ------------------------------------------------------------------ */
		if (use_ws) {
			this.reservationClient = new SimpleReservationClient(
					hsi_rsv_first_epr);
			this.topologyClient = new SimpleTopologyClient(hsi_tpy_idb_epr);
		} else {
			this.reservationClient = new SimpleReservationClient(
					(IReservationWS) createObject(hsi_rsv_first_class));
			this.topologyClient = new SimpleTopologyClient(new TopologyWS());
		}
		try {
			this.topologyClient.addOrEditDomain("localDummy", hsi_rsv_mock_epr,
					"128.0.0.0/16");
		} catch (SoapFault e) {
			this.errorLogger.error("Could not add local dummy: "
					+ e.getMessage());
		}

		/* ------------------------------------------------------------------ */

		/* ----------------------------------------------------------------- */
		final String infoText = "\n\n" + "# Description: "
				+ this.properties.getProperty("TEST_DESCR") + "\n"
				+ "# Repetitions: " + repititions + " Threads: "
				+ max_num_threads + " Type: " + type + " EPR: "
				+ this.reservationClient.getEndpointReference().getAddress()
				+ "\n";
		final String isDirectly = (use_ws) ? "WS" : "NOWS";
		final String logFile_duration = test_entity + "." + type + "."
				+ isDirectly + "." + "durations" + "." + "data";
		final String logFile_success = test_entity + "." + type + "."
				+ isDirectly + "." + "success" + "." + "data";
		final String logFile_error = test_entity + "." + type + "."
				+ isDirectly + "." + "error" + "." + "data";

		FileAppender fileAppender;
		PatternLayout dataLayout = new PatternLayout("%m");

		new File(logFile_duration).delete();
		fileAppender = new FileAppender(dataLayout, logFile_duration);
		this.durationLogger = Logger.getLogger(logFile_duration);
		durationLogger.addAppender(fileAppender);
		durationLogger.info(infoText);
		new File(logFile_success).delete();
		fileAppender = new FileAppender(dataLayout, logFile_success);
		this.successLogger = Logger.getLogger(logFile_success);
		successLogger.addAppender(fileAppender);
		successLogger.info(infoText);
		new File(logFile_error).delete();
		fileAppender = new FileAppender(dataLayout, logFile_error);
		this.errorLogger = Logger.getLogger(logFile_error);
		errorLogger.addAppender(fileAppender);
		/* ------------------------------------------------------------------ */

		/* avoid initialization delays -------------------------------------- */
		try {
			this.reservationClient.isAvailable(src, dst, bandwidth, delay,
					duration);
		} catch (final Exception exception) {
			this.errorLogger.error("Could not initialize client: "
					+ exception.getMessage());
		}
		/* ------------------------------------------------------------------ */
	}

	/**
	 * Measure the scalability of the Harmony infra-structure. Several requests
	 * per second are sent to an HSI an the response time is logged.
	 * 
	 * @throws InterruptedException
	 *             If the sleeping thread is interuppted.
	 * @throws IOException
	 *             If the logging file cannot be created.
	 * @throws UnexpectedFaultException
	 *             If an error occurs within the HSI.
	 * @throws InvalidRequestFaultException
	 *             If the request is invalid.
	 * @throws DatatypeConfigurationException
	 */
	@Test
	public void testDurationScalability() throws InterruptedException,
			IOException, InvalidRequestFaultException,
			UnexpectedFaultException, DatatypeConfigurationException {

		for (int iteration = 0; iteration < repititions; iteration++) {
			/*
			 * ------------------------------------------------------------------
			 */
			// This call is very expensive.
			final Element rsvRequest = this.generateRequest(src, dst);
			/*
			 * ------------------------------------------------------------------
			 */

			for (int runs = start_num_threads; runs <= max_num_threads; runs += step_num_threads) {
				long totalDuration = 0;
				long totalSuccess = 0;
				final List<MeasureThread> threads = new LinkedList<MeasureThread>();

				for (int i = 1; i <= runs; i++) {
					final MeasureThread mThread = new MeasureThread(
							this.reservationClient, rsvRequest,
							waitingtime_in_thread, type);
					mThread.setName("Thread no: " + i);
					threads.add(mThread);
				}

				for (final MeasureThread mThread : threads) {
					System.out.println("Starting: " + mThread.getName());
					mThread.start();
					Thread.sleep(1000 / runs);
				}

				for (final MeasureThread mThread : threads) {
					// mThread.wait();
					while (mThread.isAlive()) {
						Thread.sleep(100);
					}
					if (mThread.getException() == null) {
						totalSuccess++;
					} else {
						String errorDescr = "";
						for (StackTraceElement stack : mThread.getException()
								.getStackTrace()) {
							errorDescr += stack.toString();
						}
						errorLogger.error("Exception: "
								+ mThread.getException().getMessage());
						errorLogger.error(errorDescr);
					}

					totalDuration += mThread.getDuration();
				}
				successLogger.info(totalSuccess + " ");
				if (totalSuccess > 0) {
					System.out
							.println(runs + " " + totalDuration + " "
									+ totalSuccess + " " + totalDuration
									/ totalSuccess);
					durationLogger.info(totalDuration / totalSuccess + " ");
				} else {
					runs = max_num_threads + 1; // abort
					iteration = repititions + 1; // abort
					successLogger.info("\n" + totalSuccess + "\n");
				}
			}
			durationLogger.info("\n");
			successLogger.info("\n");
		}
	}

	/**
	 * Create the reservation request. Since this operation is expensive it
	 * should not be called within each measurement.
	 * 
	 * @return The reservation request element.
	 * @throws InvalidRequestFaultException
	 *             If the request is invalid.
	 * @throws UnexpectedFaultException
	 *             If something bad happend.
	 * @throws DatatypeConfigurationException
	 */
	@SuppressWarnings("unchecked")
	private Element generateRequest(final String source, final String target)
			throws InvalidRequestFaultException, UnexpectedFaultException,
			DatatypeConfigurationException {
		final Element reqElement;
		/* -------------------------------------------------------------- */
		XMLGregorianCalendar startTime;
		try {
			startTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(
					new GregorianCalendar());
		} catch (DatatypeConfigurationException e) {
			throw new RuntimeException(
					"A serious configuration error was detected ...", e);
		}
		Tuple<String, String> ep = new Tuple<String, String>(source, target);
		if (RequestType.CREATE.equals(type)) {
			final CreateReservationType request;
			final CreateReservation envelope = new CreateReservation();
			request = SimpleReservationClient.getCreateReservationRequest(
					bandwidth, delay, duration, startTime, ep);
			envelope.setCreateReservation(request);
			reqElement = JaxbSerializer.getInstance().objectToElement(envelope);
		} else if (RequestType.ISAVAILABLE.equals(type)) {
			final IsAvailableType request;
			final IsAvailable envelope = new IsAvailable();
			request = SimpleReservationClient.getIsAvailableRequest(source,
					target, bandwidth, delay, duration);
			envelope.setIsAvailable(request);
			reqElement = JaxbSerializer.getInstance().objectToElement(envelope);
		} else {
			throw new UnexpectedFaultException("Unknown type");
		}

		/* -------------------------------------------------------------- */
		return reqElement;
	}

	/**
	 * Create a new object using reflections.
	 * 
	 * @param className
	 * @return
	 */
	static Object createObject(String className) {
		Object object = null;
		try {
			Class<?> classDefinition = Class.forName(className);
			object = classDefinition.newInstance();
		} catch (InstantiationException e) {
			System.err.println(e);
		} catch (IllegalAccessException e) {
			System.err.println(e);
		} catch (ClassNotFoundException e) {
			System.err.println(e);
		}
		return object;
	}
}
