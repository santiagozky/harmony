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


package org.opennaas.extensions.idb.reservation;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.apache.muse.ws.addressing.soap.SoapFault;
import org.xml.sax.SAXException;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ActivateResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ActivateType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelReservationResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CreateReservationResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CreateReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationsResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationsType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatusResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.IsAvailableResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.IsAvailableType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.TimeoutFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.utils.WebserviceUtils;
import org.opennaas.core.utils.Config;
import org.opennaas.core.utils.PhLogger;
import org.opennaas.extensions.idb.Constants;
import org.opennaas.extensions.idb.database.hibernate.Domain;
import org.opennaas.extensions.idb.exception.database.DatabaseException;
import org.opennaas.extensions.idb.reservation.handler.ReservationRequestHandler;

/**
 * This class is intended to manage the requests to the NRPSs. Each operation of
 * this will be executed atomically to ensure the consistency of the
 * connections. This will be achieved by implementing each operation in a way
 * that it will receive a list of the messages to be sent and the destination
 * NRPS of the message. The request will be processed and each one of the
 * messages will be sent to the NRPSs concurrently.
 */
public final class AdapterManager implements IManager {

    /** Singleton Instance. */
    private static IManager selfInstance = null;

    /** Singelton instance. */
    private static IManager selfMalleableInstance = null;

    /** malleable reservation flag. */
    private boolean malleable;

    /**
     * Instance getter.
     * 
     * @return Singleton Instance
     */
    public static IManager getInstance() {
        if (AdapterManager.selfInstance == null) {
            if (Config.getString(Constants.idbProperties, "useMockManager").equals("true")) {
                try {
                    AdapterManager.selfInstance = MockNrpsManager.getInstance();
                } catch (final DatabaseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                AdapterManager.selfInstance = new AdapterManager(false);
            }
        }
        return AdapterManager.selfInstance;
    }

    /**
     * Instance getter.
     * 
     * @return Singleton Instance
     */
    public static IManager getMalleableInstance() {
        if (AdapterManager.selfMalleableInstance == null) {
                AdapterManager.selfMalleableInstance = new AdapterManager(true);
        }
        return AdapterManager.selfMalleableInstance;
    }

    /** Logger. */
    private final Logger logger;

    /** PerformanceLogger. */
    private final Logger performanceLogger;

    /** Timeout for the NRPS responses (in milliseconds) */
    private final long nrpsTimeout;

    /**
     * List to keep track of the Domains that have created successfully the
     * reservation for rollback on errors
     */
    private final HashMap<Domain, Long> rollbackList;

    private AdapterManager(boolean malleable) {

    	this.malleable = malleable;

        this.logger = PhLogger.getLogger(this.getClass());

        try {
			this.performanceLogger = ReservationRequestHandler.getInstance().getPerformanceLogger();
		} catch (SoapFault e) {
			throw new RuntimeException("cannot retrieve performance logger instance", e);
		}

        this.rollbackList = new HashMap<Domain, Long>();

        this.nrpsTimeout = Long.parseLong(Config
                .getString(Constants.idbProperties, "nrpsTimeout"));

        this.logger.info("Threaded NRPSManager created");
    }

    /**
     * This method implements the ActivateReservation operation for a set of
     * domains and messages. It creates the controllers, executes the single
     * operation in each one and returns the responses for each one of the
     * domains.
     * 
     * @param requests
     *            Set of requests for the NRPSs
     * @return Status of the activation
     * @throws SAXException
     * @throws IOException
     * @throws JAXBException
     * @throws SoapFault
     */
    public Hashtable<Domain, ActivateResponseType> activateReservation(
            final Hashtable<Domain, ActivateType> requests) throws SoapFault {
    	final long startTime = System.currentTimeMillis();

        final Enumeration<Domain> doms = requests.keys();
        final Hashtable<Domain, ActivateResponseType> response = new Hashtable<Domain, ActivateResponseType>();

        final NRPSController[] threads = new NRPSController[requests.size()];

        final long responseTime = System.currentTimeMillis();

        int i = 0;

        while (doms.hasMoreElements()) {

            final Domain dom = doms.nextElement();

            threads[i] = new NRPSController(dom, "activateReservation",
                    requests.get(dom), this.malleable, this.performanceLogger);

            threads[i].activateReservation(requests.get(dom));

            i++;
        }

        this.waitForThreads(threads);

        this.logResponse(responseTime);

        for (final NRPSController element : threads) {

            ActivateResponseType res = (ActivateResponseType) element
                    .getResult();

            if (res != null) {
                response.put(element.getDomain(), res);
            } else {
                this.logger.error("NRPS returned a null message...");

                res = new ActivateResponseType();
                response.put(element.getDomain(), res);
            }
        }

        this.cleanupThreads(threads);

        this.logTotalDuration(startTime);

        return response;
    }

    /**
     * This method implements the ActivateReservation operation for a set of
     * domains and messages. It creates the controllers, executes the single
     * operation in each one and returns the responses for each one of the
     * domains.
     * 
     * @param requests
     * @return
     * @throws SAXException
     * @throws IOException
     * @throws JAXBException
     * @throws SoapFault
     */
    public Hashtable<Domain, CancelReservationResponseType> cancelReservation(
            final Hashtable<Domain, CancelReservationType> requests)
            throws SoapFault {
    	final long startTime = System.currentTimeMillis();

        final Enumeration<Domain> doms = requests.keys();
        final Hashtable<Domain, CancelReservationResponseType> response = new Hashtable<Domain, CancelReservationResponseType>();

        final NRPSController[] threads = new NRPSController[requests.size()];

        final long responseTime = System.currentTimeMillis();

        int i = 0;

        while (doms.hasMoreElements()) {

            final Domain dom = doms.nextElement();

            threads[i] = new NRPSController(dom, "cancelReservation", requests
                    .get(dom), this.malleable, this.performanceLogger);

            threads[i].cancelReservation(requests.get(dom));

            i++;
        }

        this.waitForThreads(threads);

        this.logResponse(responseTime);

        for (final NRPSController element : threads) {

            CancelReservationResponseType res = (CancelReservationResponseType) element
                    .getResult();

            if (res != null) {
                response.put(element.getDomain(), res);
            } else {
                this.logger.error("NRPS returned a null message...");

                res = new CancelReservationResponseType();
                response.put(element.getDomain(), res);
            }
        }

        this.cleanupThreads(threads);

        this.logTotalDuration(startTime);

        return response;
    }

    private void cleanupThreads(final NRPSController[] threads) {
        // Kill all the threads
        for (int z = 0; z < threads.length; z++) {

            threads[z].finalize();
            threads[z] = null;
        }
        // Deep clean for the threads
        System.gc();
    }

    /**
     * Singleton - Cloning _not_ supported!
     * 
     * @return Should never return anything...
     * @throws CloneNotSupportedException
     *             Singleton hates to be cloned!
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    /**
     * This method implements the CreateReservation operation for a set of
     * domains and messages. It creates the controllers, executes the single
     * operation in each one and returns the responses for each one of the
     * domains.
     * 
     * @return Hasthtable with the results for each NRPS
     */
    public Hashtable<Domain, CreateReservationResponseType> createReservation(
            final Hashtable<Domain, CreateReservationType> requests)
            throws SoapFault {
    	final long startTime = System.currentTimeMillis();

        this.logger.info("Entered NRPSManager.CreateReservation");

        final Enumeration<Domain> doms = requests.keys();
        final Hashtable<Domain, CreateReservationResponseType> response = new Hashtable<Domain, CreateReservationResponseType>();

        final NRPSController[] threads = new NRPSController[requests.size()];

        final long responseTime = System.currentTimeMillis();

        int i = 0;

        while (doms.hasMoreElements()) {

            final Domain dom = doms.nextElement();

            threads[i] = new NRPSController(dom, "createReservation", requests
                    .get(dom), this.malleable, this.performanceLogger);

            threads[i].createReservation(requests.get(dom));

            i++;
        }

        this.waitForThreads(threads);

        this.logResponse(responseTime);

        boolean rollbackNeeded = false;

        for (final NRPSController element : threads) {

            CreateReservationResponseType res = (CreateReservationResponseType) element
                    .getResult();

            if (res != null) {
                response.put(element.getDomain(), res);
                // Put in the rollback list
                this.rollbackList.put(element.getDomain(), WebserviceUtils.convertReservationID(res
                        .getReservationID()));
            } else {
                this.logger.error("NRPS returned a null message...");

                res = new CreateReservationResponseType();
                response.put(element.getDomain(), res);
            }

            // If a thread is tagged with rollback, do the operation after the
            // loop
            if (element.isRollback()) {
                rollbackNeeded = true;
            }
        }

        this.cleanupThreads(threads);

        if (rollbackNeeded) {

            this.logger.warn("One or more NRPSs couldn't "
                    + "complete the reservation... making rollback!");
            this.rollback();
        }

        this.logTotalDuration(startTime);

        return response;
    }

    /**
     * Gets the reservations for each domain included in the request
     * 
     * @param requests
     *            Requests for domains
     * @return Responses of each domain
     * @throws SoapFault
     */
    public Hashtable<Domain, GetReservationsResponseType> getReservations(
            final Hashtable<Domain, GetReservationsType> requests)
            throws SoapFault {
    	final long startTime = System.currentTimeMillis();

        final Enumeration<Domain> doms = requests.keys();
        final Hashtable<Domain, GetReservationsResponseType> response = new Hashtable<Domain, GetReservationsResponseType>();

        final NRPSController[] threads = new NRPSController[requests.size()];

        final long responseTime = System.currentTimeMillis();

        int i = 0;

        while (doms.hasMoreElements()) {

            final Domain dom = doms.nextElement();

            threads[i] = new NRPSController(dom, "getReservations", requests
                    .get(dom), this.malleable, this.performanceLogger);

            threads[i].getReservations(requests.get(dom));

            i++;
        }

        this.waitForThreads(threads);

        this.logResponse(responseTime);

        for (final NRPSController element : threads) {

            GetReservationsResponseType res = (GetReservationsResponseType) element
                    .getResult();

            if (res != null) {
                response.put(element.getDomain(), res);
            } else {
                this.logger.error("NRPS returned a null message...");

                res = new GetReservationsResponseType();
                response.put(element.getDomain(), res);
            }
        }

        this.cleanupThreads(threads);

        this.logTotalDuration(startTime);

        return response;
    }

    /**
     * @param requests
     * @return
     * @throws SAXException
     * @throws IOException
     * @throws JAXBException
     * @throws SoapFault
     */
    public Hashtable<Domain, GetStatusResponseType> getStatus(
            final Hashtable<Domain, GetStatusType> requests) throws SoapFault {

        final long startTime = System.currentTimeMillis();

        final Enumeration<Domain> doms = requests.keys();
        final Hashtable<Domain, GetStatusResponseType> response = new Hashtable<Domain, GetStatusResponseType>();

        final NRPSController[] threads = new NRPSController[requests.size()];

        final long responseTime = System.currentTimeMillis();

        int i = 0;

        while (doms.hasMoreElements()) {

            final Domain dom = doms.nextElement();

            threads[i] = new NRPSController(dom, "getStatus", requests.get(dom), this.malleable, this.performanceLogger);

            threads[i].getStatus(requests.get(dom));

            i++;
        }

        this.waitForThreads(threads);

        this.logResponse(responseTime);

        for (final NRPSController element : threads) {

            GetStatusResponseType res = (GetStatusResponseType) element
                    .getResult();

            if (res != null) {
                response.put(element.getDomain(), res);
            } else {
                this.logger.error("NRPS returned a null message...");

                res = new GetStatusResponseType();
                response.put(element.getDomain(), res);
            }
        }

        this.cleanupThreads(threads);

        this.logTotalDuration(startTime);

        return response;
    }

    public Hashtable<Domain, IsAvailableResponseType> isAvailable(
            final Hashtable<Domain, IsAvailableType> requests) throws SoapFault {

    	final long startTime = System.currentTimeMillis();
    	
    	// auxiliary hashtable for requests
    	// -> so the original requests won't be deleted
    	Hashtable<Domain, IsAvailableType> auxRequests = 
    											new Hashtable<Domain, IsAvailableType>();
    	auxRequests.putAll(requests);

        final boolean useCache = Config
                .getString(Constants.idbProperties, "useNRPSResponseCache").equals("true");

        Enumeration<Domain> doms = auxRequests.keys();
        final Hashtable<Domain, IsAvailableResponseType> response = new Hashtable<Domain, IsAvailableResponseType>();

        // Look for objects in the cache if configured to do so
        if (useCache) {
            while (doms.hasMoreElements()) {

                final Domain dom = doms.nextElement();

                final IsAvailableResponseType resp = NRPSResponseCache
                        .getInstance().lookup(auxRequests.get(dom));

                // Put the cached response in the result and remove from
                // requests.
                // If the result is null, the object was not in the cache
                if (resp != null) {
                    response.put(dom, resp);
                    auxRequests.remove(dom);
                }
            }
        }

        // Send requests to the NRPSs whose response was not cached
        // or if we don't use the cache
        if (!auxRequests.isEmpty()) {

            doms = auxRequests.keys();

            final NRPSController[] threads = new NRPSController[auxRequests.size()];

            final long responseTime = System.currentTimeMillis();

            int i = 0;

            while (doms.hasMoreElements()) {

                final Domain dom = doms.nextElement();

                threads[i] = new NRPSController(dom, "isAvailable", auxRequests
                        .get(dom), this.malleable, this.performanceLogger);

                threads[i].isAvailable(auxRequests.get(dom));

                i++;
            }

            this.waitForThreads(threads);

            this.logResponse(responseTime);

            for (final NRPSController element : threads) {

                IsAvailableResponseType res = (IsAvailableResponseType) element
                        .getResult();

                if (res != null) {
                    response.put(element.getDomain(), res);

                    // Put response in the cache
                    if (useCache) {
                        NRPSResponseCache.getInstance().insert(
                                (IsAvailableType) element.getMsg(), res);
                    }
                } else {
                    this.logger.error("NRPS returned a null message...");

                    res = new IsAvailableResponseType();
                    response.put(element.getDomain(), res);
                }
            }

            this.cleanupThreads(threads);
        }

        this.logTotalDuration(startTime);

        return response;
    }

    private void logResponse(final long startTime) {
        this.performanceLogger.info("NrpsManager_response_time "
                + (System.currentTimeMillis() - startTime) + "ms");
    }
    
    private void logTotalDuration(final long startTime) {
        this.performanceLogger.info("NrpsManager_total_time "
                + (System.currentTimeMillis() - startTime) + "ms");
    }

    /**
     * Rollback for the createReservation operation.
     * 
     */
    private void rollback() throws SoapFault {

        if (!this.rollbackList.isEmpty()) {

            final Hashtable<Domain, CancelReservationType> requests = new Hashtable<Domain, CancelReservationType>();

            Hashtable<Domain, CancelReservationResponseType> responses;

            for (final Domain dom : this.rollbackList.keySet()) {

                final CancelReservationType cancelType = new CancelReservationType();

                cancelType.setReservationID(WebserviceUtils
                        .convertReservationID(this.rollbackList.get(dom)
                                .longValue()));

                requests.put(dom, cancelType);
            }

            // Call the cancel reservation operation
            responses = this.cancelReservation(requests);

            for (final CancelReservationResponseType res : responses.values()) {

                if (!res.isSuccess()) {
                    this.logger.info("Rollback failed!");
                }
            }
        } else {
            this.logger.info("No work to rollback...");
        }

    }

    /**
     * This method waits for the finalization of the sender threads
     * 
     * @param threads
     *            Array of threads to wait for
     * @return True if all the threads ended on time
     */
    private boolean waitForThreads(final NRPSController[] threads)
            throws TimeoutFaultException, SoapFault {

        boolean alive = true;
        boolean success = true;

        int iterations = 0;

        if (threads.length < 1) {
            alive = false;
        }

        while (alive) {

            // Check if exceptions have occurred in the threads
            for (final NRPSController element : threads) {

                if (element.isSetException()) {
                    throw element.getException();
                }
            }

            alive = threads[0].isAlive();

            for (int z = 1; z < threads.length; z++) {

                alive = alive || threads[z].isAlive();
            }

            // Check if there is some thread alive
            if (alive) {
                try {

                    if (iterations % 40 == 0) {
                        this.logger.info("Found some thread alive... waiting");
                    }

                    Thread.sleep(50);
                } catch (final InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            // Check the timeout
            if ((50 * iterations) > this.nrpsTimeout) {
                alive = false;
                success = false;

                String failed = "";

                for (final NRPSController element : threads) {

                    if (element.isAlive()) {
                        failed = failed + element.getDomain().getName() + " ";
                        element.interrupt();
                    }
                }

                this.logger.error("Timeout expired (" + this.nrpsTimeout
                        + " ms)... " + "One or more NRPSs did not reply: "
                        + failed);

                throw new TimeoutFaultException(
                        "The next Domains have failed (TIMEOUT): " + failed);
            }

            iterations++;
        }

        return success;
    }
}
