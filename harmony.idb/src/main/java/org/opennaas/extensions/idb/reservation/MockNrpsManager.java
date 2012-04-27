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

import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.apache.muse.ws.addressing.EndpointReference;
import org.apache.muse.ws.addressing.soap.SoapFault;
import org.w3c.dom.Element;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ActivateResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ActivateType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AvailabilityCodeType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelReservationResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ConnectionAvailabilityType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ConnectionConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ConnectionStatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CreateReservationResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CreateReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DomainStatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatusResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.IsAvailableResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.IsAvailableType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ServiceConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.StatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatusResponseType.ServiceStatus;
import org.opennaas.extensions.idb.serviceinterface.databinding.utils.WebserviceUtils;
import org.opennaas.extensions.idb.serviceinterface.reservation.SimpleReservationClient;
import org.opennaas.core.utils.Config;
import org.opennaas.core.utils.Helpers;
import org.opennaas.core.utils.PerformanceLogLevel;
import org.opennaas.core.utils.PhLogger;
import org.opennaas.extensions.idb.Constants;
import org.opennaas.extensions.idb.database.hibernate.Domain;
import org.opennaas.extensions.idb.exception.database.DatabaseException;

/**
 * An NrpsManager, that passes requests through to underlying Nrps-systems, if
 * desired, or creates Mock-answers instead.
 * 
 * @author Alexander Zimmermann (zimmerm2@cs.uni-bonn.de)
 * @version $Id: MockNrpsManager.java 3236 2008-06-26 16:56:43Z
 *          gassen@cs.uni-bonn.de $
 */
public final class MockNrpsManager implements IManager {

    /** Singleton Instance. */
    private static MockNrpsManager selfInstance = null;

    /* MISC ================================================================ */
    /**
     * used Webservice.
     */
    private static String usedWS;

    /**
     * Instance getter.
     * 
     * @return Singleton Instance
     * @throws DatabaseException
     *             A DatabaseException
     */
    public static MockNrpsManager getInstance() throws DatabaseException {
        if (MockNrpsManager.selfInstance == null) {
            MockNrpsManager.selfInstance = new MockNrpsManager();
        }
        return MockNrpsManager.selfInstance;
    }

    /** Webservice Instances. */
    private final HashMap<String, SimpleReservationClient> services = new HashMap<String, SimpleReservationClient>();

    /** Logger. */
    private final Logger log = PhLogger.getLogger(MockNrpsManager.class);

    /** PerformanceLogger instance. */
    private final Logger performanceLogger = PhLogger.getLogger("Performance");

    /**
     * Private constructor: Singleton.
     * 
     * @throws DatabaseException
     *             A DatabaseException
     */
    private MockNrpsManager() throws DatabaseException {
        this.initDataFromDB();
    }

    /**
     * Activate-Request over WS.
     * 
     * @throws SoapFault
     *             Soap-Fault
     * @param activateType
     *            ActivateType
     * @return ActivateResponseType
     */
    private ActivateResponseType activateOverWS(final ActivateType activateType)
            throws SoapFault {
        final Element request = WebserviceUtils
                .createActivateRequest(activateType);
        Element response = null;

        // call WS if designated
        if (this.passTo()) {
            final SimpleReservationClient proxy = this.services
                    .get(MockNrpsManager.usedWS);
            this.log.info("Calling WS: " + proxy.getDestination().getAddress());
            this.performanceLogger.log(PerformanceLogLevel.PERFORMANCE_LOG,
                    "Calling " + proxy.getDestination().getAddress());
            response = proxy.activate(request);
            this.performanceLogger.log(PerformanceLogLevel.PERFORMANCE_LOG,
                    "Response from " + proxy.getDestination().getAddress());
            return WebserviceUtils.createActivateResponse(response);
        }
        /* else return mock-response */
        final ActivateResponseType responseType = new ActivateResponseType();
        responseType.setSuccess(true);
        return responseType;
    }

    /**
     * ActivateReservation.
     * 
     * @throws SoapFault
     *             Soap-Fault
     * @throws DatabaseException
     *             A DatabaseException
     * @param element
     *            Hashtable < Domain, ActivateType >
     * @return Hashtable < Domain, ActivateResponseType >
     */
    public Hashtable<Domain, ActivateResponseType> activateReservation(
            final Hashtable<Domain, ActivateType> element) throws SoapFault,
            DatabaseException {
        final Date startTime = new Date();
        this.performanceLogger.log(PerformanceLogLevel.PERFORMANCE_LOG,
                "MockNrpsManager_input on: " + startTime
                        + " -> activateReservation");

        final Hashtable<Domain, ActivateResponseType> responses = new Hashtable<Domain, ActivateResponseType>();

        for (final Entry<Domain, ActivateType> entries : element.entrySet()) {
            MockNrpsManager.usedWS = entries.getKey().getName();
            // check if data is up to date
            this.checkWSInstances();
            /* pass requests to WS or create mock-response ----------------- */
            responses.put(entries.getKey(), this.activateOverWS(entries
                    .getValue()));
        }

        final Date endTime = new Date();
        this.performanceLogger.log(PerformanceLogLevel.PERFORMANCE_LOG,
                "MockNrpsManager_output on: " + endTime
                        + " -> activateReservationResponse");
        this.performanceLogger.log(PerformanceLogLevel.PERFORMANCE_LOG,
                "duration of MockNrpsManager.activateReservation" + ": "
                        + (endTime.getTime() - startTime.getTime())
                        + "MilliSecs");

        return responses;
    }

    /**
     * CancelReservation.
     * 
     * @throws SoapFault
     *             Soap-Fault
     * @throws DatabaseException
     *             A DatabaseException
     * @throws URISyntaxException
     *             A Exception occuring while generating new
     *             WS-Endpoint-Reference
     * @param element
     *            Hashtable < Domain, CancelReservationType >
     * @return Hashtable < Domain, CancelreservatioNResponseType >
     */
    public Hashtable<Domain, CancelReservationResponseType> cancelReservation(
            final Hashtable<Domain, CancelReservationType> element)
            throws SoapFault, DatabaseException {
        final Date startTime = new Date();
        this.performanceLogger.log(PerformanceLogLevel.PERFORMANCE_LOG,
                "MockNrpsManager_input on: " + startTime
                        + " -> cancelReservation");

        final Hashtable<Domain, CancelReservationResponseType> responses = new Hashtable<Domain, CancelReservationResponseType>();

        for (final Entry<Domain, CancelReservationType> entries : element
                .entrySet()) {
            MockNrpsManager.usedWS = entries.getKey().getName();
            // check if data is up to date
            this.checkWSInstances();
            /* pass requests to WS or create mock-response ----------------- */
            responses.put(entries.getKey(), this
                    .cancelReservationOverWS(entries.getValue()));
        }

        final Date endTime = new Date();
        this.performanceLogger.log(PerformanceLogLevel.PERFORMANCE_LOG,
                "MockNrpsManager_output on: " + endTime
                        + " -> cancelReservationResponse");
        this.performanceLogger.log(PerformanceLogLevel.PERFORMANCE_LOG,
                "duration of MockNrpsManager.cancelReservation" + ": "
                        + (endTime.getTime() - startTime.getTime())
                        + "MilliSecs");

        return responses;
    }

    /**
     * CancelReservation over WS.
     * 
     * @throws SoapFault
     *             Soap-Fault
     * @param cancelReservationType
     *            CancelReservationType
     * @return CancelReservationResponseType
     */
    private CancelReservationResponseType cancelReservationOverWS(
            final CancelReservationType cancelReservationType) throws SoapFault {
        final Element request = WebserviceUtils
                .createCancelReservationRequest(cancelReservationType);
        Element response = null;

        // call WS if designated
        if (this.passTo()) {
            final SimpleReservationClient proxy = this.services
                    .get(MockNrpsManager.usedWS);
            this.log.info("Calling WS: " + proxy.getDestination().getAddress());
            this.performanceLogger.log(PerformanceLogLevel.PERFORMANCE_LOG,
                    "Calling " + proxy.getDestination().getAddress());
            response = proxy.cancelReservation(request);
            this.performanceLogger.log(PerformanceLogLevel.PERFORMANCE_LOG,
                    "Response from " + proxy.getDestination().getAddress());
            return WebserviceUtils.createCancelReservationResponse(response);
        }
        /* else return mock-response */
        final CancelReservationResponseType responseType = new CancelReservationResponseType();
        responseType.setSuccess(true);
        return responseType;
    }

    /**
     * check if WS-instances are up to date.
     * 
     * @throws DatabaseException
     *             A DatabaseException
     */
    private void checkWSInstances() throws DatabaseException {
        // load actual domain data from DB
        final Domain dom = Domain.load(MockNrpsManager.usedWS);

        // if data already exists check if up to date and refresh if necessary
        if (this.services.containsKey(MockNrpsManager.usedWS)
                && !this.services.get(MockNrpsManager.usedWS)
                        .getEndpointReference().getAddress().equals(
                                dom.getReservationURI())) {
            this.services.put(MockNrpsManager.usedWS,
                    new SimpleReservationClient(new EndpointReference(dom
                            .getReservationURI())));
        } else {
            // only create wanted WS-instances
            if (this.passTo()) {
                // data does not exist, so create new
                this.services.put(MockNrpsManager.usedWS,
                        new SimpleReservationClient(new EndpointReference(dom
                                .getReservationURI())));
            }
        }
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
     * CreateReservation.
     * 
     * @throws SoapFault
     *             Soap-Fault
     * @throws DatabaseException
     *             A DatabaseException
     * @throws URISyntaxException
     *             A Exception occuring while generating new
     *             WS-Endpoint-Reference
     * @param element
     *            Hashtable < Domain, CreatReservationtype >
     * @return Hashtable < Domain, CreateReservationResponseType >
     */
    public Hashtable<Domain, CreateReservationResponseType> createReservation(
            final Hashtable<Domain, CreateReservationType> element)
            throws SoapFault, DatabaseException {
        final Date startTime = new Date();
        this.performanceLogger.log(PerformanceLogLevel.PERFORMANCE_LOG,
                "MockNrpsManager_input on: " + startTime
                        + " -> createReservation");

        // List with responses.
        final Hashtable<Domain, CreateReservationResponseType> responses = new Hashtable<Domain, CreateReservationResponseType>();
        // List with called webservices for later rollback if necessary
        final HashMap<SimpleReservationClient, Long> calledWSForRollback = new HashMap<SimpleReservationClient, Long>();

        for (final Entry<Domain, CreateReservationType> entries : element
                .entrySet()) {
            MockNrpsManager.usedWS = entries.getKey().getName();
            // check if data is up to date
            this.checkWSInstances();
            /* pass requests to WS or create mock-response ----------------- */
            try {
                responses.put(entries.getKey(), this
                        .createReservationOverWS(entries.getValue()));
            } catch (final SoapFault s) {
                // something went wrong with this CreateReservation-Call in the
                // NRPS. So execute rollback for previous calls.
                this.rollback(calledWSForRollback);
                // throw SoapFault for further handling.
                throw s;
            }

            // save data for possible rollback
            calledWSForRollback
                    .put(this.services.get(MockNrpsManager.usedWS), new Long(
                            responses.get(entries.getKey()).getReservationID()));

        }

        final Date endTime = new Date();
        this.performanceLogger.log(PerformanceLogLevel.PERFORMANCE_LOG,
                "MockNrpsManager_output on: " + endTime
                        + " -> createReservationResponse");
        this.performanceLogger.log(PerformanceLogLevel.PERFORMANCE_LOG,
                "duration of MockNrpsManager.createReservation" + ": "
                        + (endTime.getTime() - startTime.getTime())
                        + "MilliSecs");

        return responses;
    }

    /**
     * CreateReservation-Request over WS.
     * 
     * @throws SoapFault
     *             Soap-Fault
     * @param createReservationType
     *            CreateReservationType
     * @return CreateReservationResponseType
     */
    private CreateReservationResponseType createReservationOverWS(
            final CreateReservationType createReservationType) throws SoapFault {
        final Element request = WebserviceUtils
                .createReservationRequest(createReservationType);
        Element response = null;

        // call WS if designated
        if (this.passTo()) {
            final SimpleReservationClient proxy = this.services
                    .get(MockNrpsManager.usedWS);

            this.log.info("Calling WS: " + proxy.getDestination().getAddress());
            this.performanceLogger.log(PerformanceLogLevel.PERFORMANCE_LOG,
                    "Calling " + proxy.getDestination().getAddress());

            response = proxy.createReservation(request);

            this.performanceLogger.log(PerformanceLogLevel.PERFORMANCE_LOG,
                    "Response from " + proxy.getDestination().getAddress());

            return WebserviceUtils.createReservationResponse(response);
        }
        /* else return mock-response */
        final CreateReservationResponseType responseType = new CreateReservationResponseType();
        responseType
                .setReservationID(WebserviceUtils.convertReservationID(Helpers
                        .getRandomLong() & 0x7FFFFFFFFFFFFFFFL));
        return responseType;
    }

    /**
     * GetStatus.
     * 
     * @throws SoapFault
     *             Soap-Fault
     * @throws DatabaseException
     *             A DatabaseException
     * @throws URISyntaxException
     *             A Exception occuring while generating new
     *             WS-Endpoint-Reference
     * @param element
     *            Hashtable < Domain, GetStatusType >
     * @return Hashtable < Domain, GetStatusResponseType >
     */
    public Hashtable<Domain, GetStatusResponseType> getStatus(
            final Hashtable<Domain, GetStatusType> element) throws SoapFault,
            DatabaseException {
        final Date startTime = new Date();
        this.performanceLogger.log(PerformanceLogLevel.PERFORMANCE_LOG,
                "MockNrpsManager_input on: " + startTime + " -> getStatus");

        final Hashtable<Domain, GetStatusResponseType> responses = new Hashtable<Domain, GetStatusResponseType>();
        for (final Entry<Domain, GetStatusType> entries : element.entrySet()) {
            MockNrpsManager.usedWS = entries.getKey().getName();
            // check if data is up to date
            this.checkWSInstances();
            /* pass requests to WS or create mock-response ----------------- */
            responses.put(entries.getKey(), this.getStatusOverWS(entries
                    .getValue()));
        }

        final Date endTime = new Date();
        this.performanceLogger.log(PerformanceLogLevel.PERFORMANCE_LOG,
                "MockNrpsManager_output on: " + endTime
                        + " -> getStatusResponse");
        this.performanceLogger.log(PerformanceLogLevel.PERFORMANCE_LOG,
                "duration of MockNrpsManager.getStatus" + ": "
                        + (endTime.getTime() - startTime.getTime())
                        + "MilliSecs");

        return responses;
    }

    /**
     * GetStatus over WS.
     * 
     * @throws SoapFault
     *             Soap-Fault
     * @param getStatusType
     *            GetStatusType
     * @return getStatusResponseType
     */
    private GetStatusResponseType getStatusOverWS(
            final GetStatusType getStatusType) throws SoapFault {
        final Element request = WebserviceUtils
                .createGetStatusRequest(getStatusType);
        Element response = null;

        // call WS if designated
        if (this.passTo()) {
            final SimpleReservationClient proxy = this.services
                    .get(MockNrpsManager.usedWS);
            this.log.info("Calling WS: " + proxy.getDestination().getAddress());
            this.performanceLogger.log(PerformanceLogLevel.PERFORMANCE_LOG,
                    "Calling " + proxy.getDestination().getAddress());
            response = proxy.getStatus(request);
            this.performanceLogger.log(PerformanceLogLevel.PERFORMANCE_LOG,
                    "Response from " + proxy.getDestination().getAddress());
            return WebserviceUtils.createGetStatusResponse(response);
        }
        /* else return mock-response */
        final GetStatusResponseType responseType = new GetStatusResponseType();
        for (final int serviceId : getStatusType.getServiceID()) {
            final ServiceStatus status = new ServiceStatus();

            status.setServiceID(serviceId);
            status.setStatus(StatusType.PENDING);

            final DomainStatusType domainStatus = new DomainStatusType();
            domainStatus.setDomain("MockDomain");
            domainStatus.setStatus(StatusType.PENDING);

            status.getDomainStatus().add(domainStatus);

            final ConnectionStatusType connectionStatus = new ConnectionStatusType();
            connectionStatus.setConnectionID(Helpers.getRandomInt());
            connectionStatus.setDirectionality(0);
            connectionStatus.setActualBW(100);
            connectionStatus.setStatus(StatusType.PENDING);
            final EndpointType sourceEndpoint = new EndpointType();
            sourceEndpoint.setEndpointId("33.44.55.66");
            connectionStatus.setSource(sourceEndpoint);
            final EndpointType destEndpoint = new EndpointType();
            destEndpoint.setEndpointId("66.55.44.33");
            connectionStatus.getTarget().add(destEndpoint);
            // add no DomainConnectionStatusType, because no other domain is
            // underneath

            status.getConnections().add(connectionStatus);

            responseType.getServiceStatus().add(status);
        }
        return responseType;
    }

    /**
     * get data for the attached NRPS from DB.
     * 
     * @throws DatabaseException
     *             A DatabaseException
     * @throws URISyntaxException
     *             A Exception occuring while generating new
     *             WS-Endpoint-Reference
     */
    private void initDataFromDB() throws DatabaseException {
        final Set<Domain> allDomains = Domain.loadAll();
        for (final Domain d : allDomains) {
            MockNrpsManager.usedWS = d.getName();
            if (this.passTo()) {
                this.services.put(d.getName(), new SimpleReservationClient(
                        new EndpointReference(d.getReservationURI())));
            }
        }
    }

    /**
     * isAvailable.
     * 
     * @throws SoapFault
     *             Soap-Fault
     * @throws DatabaseException
     *             A DatabaseException
     * @throws URISyntaxException
     *             A Exception occuring while generating new
     *             WS-Endpoint-Reference
     * @param element
     *            Hashtable < Domain, IsAvailableType >
     * @return Hashtable < Domain, IsAvailableresponseType >
     */
    public Hashtable<Domain, IsAvailableResponseType> isAvailable(
            final Hashtable<Domain, IsAvailableType> element) throws SoapFault,
            DatabaseException {
        final Date startTime = new Date();
        this.performanceLogger.log(PerformanceLogLevel.PERFORMANCE_LOG,
                "MockNrpsManager_input on: " + startTime + " -> isAvailable");

        final Hashtable<Domain, IsAvailableResponseType> responses = new Hashtable<Domain, IsAvailableResponseType>();

        for (final Entry<Domain, IsAvailableType> entries : element.entrySet()) {
            MockNrpsManager.usedWS = entries.getKey().getName();
            // check if data is up to date
            this.checkWSInstances();
            /* pass requests to WS or create mock-response ----------------- */
            responses.put(entries.getKey(), this.isAvailableOverWS(entries
                    .getValue()));
        }

        final Date endTime = new Date();
        this.performanceLogger.log(PerformanceLogLevel.PERFORMANCE_LOG,
                "MockNrpsManager_output on: " + endTime
                        + " -> isAvailableResponse");
        this.performanceLogger.log(PerformanceLogLevel.PERFORMANCE_LOG,
                "duration of MockNrpsManager.isAvailable" + ": "
                        + (endTime.getTime() - startTime.getTime())
                        + "MilliSecs");

        return responses;
    }

    /**
     * IsAvailable-Request over WS.
     * 
     * @throws SoapFault
     *             Soap-Fault
     * @param isAvailableType
     *            IsAvailableType
     * @return IsAvailableResponseType
     */
    private IsAvailableResponseType isAvailableOverWS(
            final IsAvailableType isAvailableType) throws SoapFault {
        final Element request = WebserviceUtils
                .createIsAvailableRequest(isAvailableType);
        Element response = null;

        // call WS if designated
        if (this.passTo()) {
            final SimpleReservationClient proxy = this.services
                    .get(MockNrpsManager.usedWS);
            this.log.info("Calling WS: " + proxy.getDestination().getAddress());
            this.performanceLogger.log(PerformanceLogLevel.PERFORMANCE_LOG,
                    "Calling " + proxy.getDestination().getAddress());
            response = proxy.isAvailable(request);
            this.performanceLogger.log(PerformanceLogLevel.PERFORMANCE_LOG,
                    "Response from " + proxy.getDestination().getAddress());
            return WebserviceUtils.createIsAvailableResponse(response);
        }
        /* else return mock-response */
        final IsAvailableResponseType responseType = new IsAvailableResponseType();

        final String domainName = MockNrpsManager.usedWS;
        boolean unvailable = false;
        int bwSum = 0;

        for (final ServiceConstraintType s : isAvailableType.getService()) {
            for (final ConnectionConstraintType c : s.getConnections()) {
                final ConnectionAvailabilityType ca = new ConnectionAvailabilityType();
                responseType.getDetailedResult().add(ca);
                ca.setConnectionID(c.getConnectionID());
                ca.setServiceID(s.getServiceID());

                // basic mock functionality -- everything works

                ca.setAvailability(AvailabilityCodeType.AVAILABLE);

                // special scenario, see ...test.idb.TestScenario1
                final int transit1BW = 256;
                final int transit2BW = 768;
                final int transit3BW = 512;

                bwSum += c.getMinBW();
                if (unvailable) {
                    ca
                            .setAvailability(AvailabilityCodeType.AVAILABILITY_NOT_CHECKED);
                } else {
                    if (domainName.equals("scen1transit1")) {
                        if (bwSum > transit1BW) {
                            ca
                                    .setAvailability(AvailabilityCodeType.PATH_NOT_AVAILABLE);
                            unvailable = true;
                        }
                    } else if (domainName.equals("scen1transit2")) {
                        if (bwSum > transit2BW) {
                            ca
                                    .setAvailability(AvailabilityCodeType.PATH_NOT_AVAILABLE);
                            unvailable = true;
                        }
                    } else if (domainName.equals("scen1transit3")) {
                        if (bwSum > transit3BW) {
                            ca
                                    .setAvailability(AvailabilityCodeType.PATH_NOT_AVAILABLE);
                            unvailable = true;
                        }
                    }
                }
            }
        }
        return responseType;
    }

    /**
     * check if the request should be passed through to the according actual
     * handled NRPS. !! The values in the if-clauses have to be adapted manually
     * according !! !! to the data in the DemoWorkFlow-Classes !!
     * 
     * @return boolean
     */
    protected boolean passTo() {
        if (MockNrpsManager.usedWS.toLowerCase().contains("-mpls")) {
            // check if pass to Argon
            return Config.isTrue(Constants.idbProperties, "passToArgon");
        } else if (MockNrpsManager.usedWS.toLowerCase().contains("gmpls")) {
            // check if pass to GMPLS
            return Config.isTrue(Constants.idbProperties, "passToGMPLS");
        } else if (MockNrpsManager.usedWS.toLowerCase().contains("crc")) {
            // check if pass to CRC
            return Config.isTrue(Constants.idbProperties, "passToCRC");
        } else if (MockNrpsManager.usedWS.toLowerCase().contains("i2cat")) {
            // check if pass to I2CAT/UCLP
            return Config.isTrue(Constants.idbProperties, "passToI2CAT");
        } else if (MockNrpsManager.usedWS.toLowerCase().contains("surfnet")) {
            // check if pass to surfnet
            return Config.isTrue(Constants.idbProperties, "passToSurfnet");
        } else {
            // all other requests should be mock-responded
            return false;
        }
    }

    /**
     * rollback, if something went wrong in a NRPS-CreateReservation-Call. Send
     * a CancelReservation to all previous called NRPS.
     * 
     * @param calledWS
     *            HashMap with called NRPS and corresponding reservation-ID
     * @throws SoapFault
     *             Soap-Fault
     */
    private void rollback(final HashMap<SimpleReservationClient, Long> calledWS)
            throws SoapFault {

        for (final SimpleReservationClient proxy : calledWS.keySet()) {
            final CancelReservationType crType = new CancelReservationType();
            CancelReservationResponseType crResponseType = new CancelReservationResponseType();

            // get data from List
            crType.setReservationID(WebserviceUtils
                    .convertReservationID(calledWS.get(proxy).longValue()));
            final Element request = WebserviceUtils
                    .createCancelReservationRequest(crType);
            // call CancelReservation
            final Element response = proxy.cancelReservation(request);
            crResponseType = WebserviceUtils
                    .createCancelReservationResponse(response);
            // if CancelReservation went wrong, only log this info
            if (!crResponseType.isSuccess()) {
                this.log.info("rollback for WS: "
                        + proxy.getDestination().getAddress()
                        + " -> Reservation: " + crType.getReservationID()
                        + "failed!");
            }
        }
    }
}
