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


package org.opennaas.extensions.idb.da.argon.handler;

import java.util.ArrayList;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.naming.NamingException;
import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.apache.muse.ws.addressing.soap.SoapFault;

import de.unibonn.viola.argon.requestProcessor.requestHandler.operator.externalInterface.OperatorInterface;
import de.unibonn.viola.argon.utils.jaxb.external.IresJaxb;
import de.unibonn.viola.argon.utils.jaxb.external.classes.IRes.ActivationReplyType;
import de.unibonn.viola.argon.utils.jaxb.external.classes.IRes.ActivationRequestType;
import de.unibonn.viola.argon.utils.jaxb.external.classes.IRes.AvailabilityReplyType;
import de.unibonn.viola.argon.utils.jaxb.external.classes.IRes.AvailabilityRequestType;
import de.unibonn.viola.argon.utils.jaxb.external.classes.IRes.BindRequestType;
import de.unibonn.viola.argon.utils.jaxb.external.classes.IRes.CancelReplyType;
import de.unibonn.viola.argon.utils.jaxb.external.classes.IRes.CancelRequestType;
import de.unibonn.viola.argon.utils.jaxb.external.classes.IRes.ModificationRequestType;
import de.unibonn.viola.argon.utils.jaxb.external.classes.IRes.QueryReplyType;
import de.unibonn.viola.argon.utils.jaxb.external.classes.IRes.QueryRequestType;
import de.unibonn.viola.argon.utils.jaxb.external.classes.IRes.ReservationReplyType;
import de.unibonn.viola.argon.utils.jaxb.external.classes.IRes.ReservationRequestType;
import de.unibonn.viola.argon.utils.jms.JMSConnector;
import org.opennaas.extensions.idb.da.argon.Constants;
import org.opennaas.extensions.idb.da.argon.implementation.ArgonOperatorClient;
import org.opennaas.extensions.idb.da.argon.implementation.ConvertHelper;
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
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointInterfaceType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationsResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationsType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatusResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.IsAvailableResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.IsAvailableType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ObjectFactory;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ServiceConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.StatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatusResponseType.ServiceStatus;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.EndpointNotFoundFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;
import org.opennaas.extensions.idb.serviceinterface.reservation.CommonReservationHandler;
import org.opennaas.core.utils.Config;

/**
 * Implementation of the Reservation-Webservice for Muse.
 * 
 * @author Steffen Claus
 */
public final class ReservationHandler extends CommonReservationHandler {
    /*
     * Instance Variables
     * =========================================================================
     */

    /** Singleton instance. */
    private static ReservationHandler selfInstance = null;

    /**
     * used for JMS.
     */
    private static long lastRequestID;

    // TODO: adapt to new lib
    // test and upload the new ARGONLibs.jar
    private static JMSConnector jmsConnector = null;

    /*
     * Singleton Stuff
     * =========================================================================
     */

    /** Private constructor: Singleton. 
     * @throws SoapFault */
    private ReservationHandler() throws SoapFault {

        this.performanceLogger = getPerformanceLogger();

        while (true) {
            try {
                ReservationHandler.jmsConnector = new JMSConnector(Config
                        .getString(Constants.adapterProperties, "argonNRPS.jmsInterfaceAddress"));
            } catch (NamingException e) {
                e.printStackTrace();
                continue;
            } catch (JMSException e) {
                e.printStackTrace();
                continue;
            }

            if (ReservationHandler.jmsConnector == null) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                break;
            }

        }

    }

    /**
     * Instance getter.
     * 
     * @return Singleton Instance
     * @throws SoapFault 
     */
    public static ReservationHandler getInstance() {
        if (ReservationHandler.selfInstance == null) {
       		try {
				ReservationHandler.selfInstance = new ReservationHandler();
			} catch (SoapFault e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        return ReservationHandler.selfInstance;
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

    /** translation-factor between sec. and millisec. */
    private static final long SEC_MILLISEC_FACTOR = 1000L;
    /**
     * the time to wait for an reply from JMS. two minutes
     */
    private static final long TIMEOUT = 30L * ReservationHandler.SEC_MILLISEC_FACTOR;

    /**
     * the log4j-Logger.
     */
    private static Logger log = Logger.getLogger(ReservationHandler.class);

    /**
     * the Performance-Logger.
     */
    private Logger performanceLogger = null;

    /** The Constant ACTIVATION. */
    public static final String ACTIVATION = "Activation";

    /** The Constant MODIFICATION. */
    public static final String MODIFICATION = "Modification";

    /** The Constant QUERY. */
    public static final String QUERY = "Query";

    /** The Constant CANCEL. */
    public static final String CANCEL = "Cancel";

    /** The Constant BIND. */
    public static final String BIND = "Bind";

    /** The Constant AVAILABILITY. */
    public static final String AVAILABILITY = "Availability";

    /** The Constant RESERVATION. */
    public static final String RESERVATION = "Reservation";

    /**
     * The Object Factory for the muse-classes.
     */
    private static ObjectFactory of = new ObjectFactory();

    /**
     * Helper Class for converting from NSP to Argon and the other way round.
     */
    private static ConvertHelper converter = ConvertHelper.getInstance();

    /**
     * @return whether mock-mode is set
     */
    protected boolean mockMode() {
        return Config.getString(Constants.adapterProperties, "argonNRPS.mockMode").equals("true");
    }

    /* REQUESTS =============================================================== */

    /**
     * @param request
     *            the GetStatus - Message
     * @return the GetStatusResponse - Message
     * @throws SoapFault
     *             Any Exception occuring during request-handling
     */
    @Override
    public GetStatusResponseType getStatus(final GetStatusType request)
            throws SoapFault {
        ReservationHandler.log.info("getStatus-Message received");

        // mock-modus, for testing
        if (this.mockMode()) {
            ReservationHandler.log.info("running in mock-mode");
            return this.runMock(request);
        }

        // internal QueryRequest
        final QueryRequestType internalRequest = ReservationHandler.converter
                .createArgonRequest(request);

        // send to JMS
        final QueryReplyType reply = this.convertAndSend(internalRequest,
                QueryReplyType.class);

        return ReservationHandler.converter.createNspResponse(reply);
    }

    /**
     * @param request
     *            the CreateReservation-Message-Object
     * @return Element the CreateReservationResponse-Message-Object
     * @throws EndpointNotFoundFaultException
     * @throws UnexpectedFaultException 
     */
    @Override
    public CreateReservationResponseType createReservation(
            final CreateReservationType request)
            throws EndpointNotFoundFaultException, UnexpectedFaultException {
        ReservationHandler.log.info("createReservation-Message received");

        // mock-modus for testing
        if (this.mockMode()) {
            ReservationHandler.log.info("running in mock-mode");
            return this.runMock(request);
        }

        // internal Object which will be sent to JMS-Topic
        final ReservationRequestType internalRequest = ReservationHandler.converter
                .createArgonRequest(request);

        // converts and send internal request to JMS
        // and waits timeout-seconds for reply
        final ReservationReplyType reply = this.convertAndSend(internalRequest,
                ReservationReplyType.class);
        if (!reply.isResult()) {
            throw new UnexpectedFaultException(reply.getResultDescription());
        }

        return ReservationHandler.converter.createNspResponse(reply);
    }

    /**
     * @param request
     *            the CancelReservation-Message contains the reservationID to be
     *            cancelled
     * @return Element the CancelReservationResponse-Message contains the result
     *         and the result-description
     */
    @Override
    public CancelReservationResponseType cancelReservation(
            final CancelReservationType request) throws SoapFault {
        ReservationHandler.log.info("cancelReservation-Message received");

        if (this.mockMode()) {
            ReservationHandler.log.info("running in mock-mode");
            return this.runMock(request);
        }

        // internal Object which will be sent to JMS-Topic
        final CancelRequestType internalRequest = ReservationHandler.converter
                .createArgonRequest(request);

        // converts and send internal request to JMS
        // and waits timeout-seconds for reply
        final CancelReplyType reply = this.convertAndSend(internalRequest,
                CancelReplyType.class);
        return ReservationHandler.converter.createNspResponse(reply);
    }

    /**
     * @param request
     *            the isAvailable-Message contains a list of ServiceConstraints,
     *            how to handle the JobID?
     * @return Element the isAvailableResponse-Message contains a result(true if
     *         all services are available), a result-description and a
     *         detailedResult(serviceID, conID, result)
     */
    @Override
    public IsAvailableResponseType isAvailable(final IsAvailableType request) throws SoapFault {
        ReservationHandler.log.info("isAvailable-Message received");

        // mock-mode for testing
        if (this.mockMode()) {
            ReservationHandler.log.info("running in mock-mode");
            return this.runMock(request);
        }

        // internal Object which will be sent to JMS-Topic
        final AvailabilityRequestType internalRequest = ReservationHandler.converter
                .createArgonRequest(request);

        // converts and send internal request to JMS
        // and waits timeout-seconds for reply
        final AvailabilityReplyType reply = this.convertAndSend(
                internalRequest, AvailabilityReplyType.class);

        return ReservationHandler.converter.createNspResponse(reply);
    }

    /**
     * @param request
     *            the activate-Message contains reservationID and serviceID to
     *            be activated
     * @return Element the activeResponse-Message contains the result and a
     *         result-description
     */
    @Override
    public ActivateResponseType activate(final ActivateType request)
            throws SoapFault {
        ReservationHandler.log.info("activate-Message received");

        // mock-mode for testing
        if (this.mockMode()) {
            ReservationHandler.log.info("running in mock-mode");
            return this.runMock(request);
        }

        // internal Object which will be sent to JMS-Topic
        final ActivationRequestType internalRequest = ReservationHandler.converter
                .createArgonRequest(request);

        // send internal request to JMS-Topic
        final ActivationReplyType reply = this.convertAndSend(internalRequest,
                ActivationReplyType.class);

        return ReservationHandler.converter.createNspResponse(reply);
    }

    /**
     * @param element
     * @return The reservation response.
     */
    @Override
    public GetReservationsResponseType getReservations(
            final GetReservationsType element) throws UnexpectedFaultException {
        ReservationHandler.log.info("getReservations-Message received");

        if (this.mockMode()) {
            log
                    .error("Could not get Information from argon. Running in Mock-Mode.");
            throw new UnexpectedFaultException(
                    "Unable to get the necessary information from argon. Running in Mock-Mode.");
        }

        OperatorInterface operator = ArgonOperatorClient.getOperator();
        if (operator == null) {
            throw new UnexpectedFaultException(
                    "Could not reach argon via the operator interface.");
        }
        ArrayList<ReservationRequestType> argonRequests = null;
        try {
            // TODO: new operator function
            Long start = element.getPeriodStartTime().toGregorianCalendar().getTimeInMillis();
            Long end = element.getPeriodEndTime().toGregorianCalendar().getTimeInMillis();
            
            argonRequests = operator.getReservationsIn(start, end);
            
            log.debug("Found " + argonRequests.size()
                    + " requests in the interval!");
        } catch (Exception e) {
            log.error("Error while getting information from argon: "
                    + e.getMessage(), e);
            throw new UnexpectedFaultException(
                    "Error while getting information from argon: "
                            + e.getMessage(), e);
        }
        return ReservationHandler.converter.createNspResponse(argonRequests);
    }

    /**
     * @param element
     * @return The reservation response.
     * @throws UnexpectedFaultException 
     */
    public GetReservationResponseType getReservation(
            final GetReservationType element) throws UnexpectedFaultException {
        ReservationHandler.log.info("getReservation-Message received");

        if (this.mockMode()) {
            log.error("Could not get Information from argon.");
            throw new UnexpectedFaultException(
                    "Unable to get the necessary information from argon.");
        }

        OperatorInterface operator = ArgonOperatorClient.getOperator();
        if (operator == null) {
            throw new UnexpectedFaultException(
                    "Could not reach argon via the operator interface.");
        }
        ReservationRequestType argonRequest = null;
        try {
        	argonRequest = operator.getReservation(Long
                        .parseLong(element.getReservationID()));
            log.debug("Found request in with specified id!");
        } catch (Exception e) {
            log.error("Error while getting information from argon!", e);
            throw new UnexpectedFaultException(
                    "Error while getting information from argon!", e);
        }
        if (argonRequest == null) {
            log.error("Could not find the requested reservation!");
            throw new UnexpectedFaultException(
                    "Could not find the requested reservation!");
        }
        return ReservationHandler.converter.createNspResponse(argonRequest);
    }

    /* MOCK-RESPONSES ========================================================= */
    /**
     * returns a mock-response.
     * 
     * @param request
     *            CreateReservationType
     * @return CreateReservationResponseType
     */
    protected CreateReservationResponseType runMock(
            final CreateReservationType request) {
        final CreateReservationResponseType mockResponseType = ReservationHandler.of
                .createCreateReservationResponseType();
        // mockResponseType.setJobID(42L);
        mockResponseType.setReservationID("1337");
        // return response-Element
        return mockResponseType;
    }

    /**
     * returns a mock-response.
     * 
     * @param request
     *            CancelReservationType
     * @return CancelReservationResponseType
     */
    protected CancelReservationResponseType runMock(
            final CancelReservationType request) {
        final CancelReservationResponseType mockResponseType = ReservationHandler.of
                .createCancelReservationResponseType();
        // there's only one interesting field, the result
        mockResponseType.setSuccess(false);

        // return response-Element
        return mockResponseType;
    }

    /**
     * returns a mock-response.
     * 
     * @param request
     *            IsAvailableType
     * @return IsAvailableResponseType
     */
    protected IsAvailableResponseType runMock(final IsAvailableType request) {
        final IsAvailableResponseType mockResponseType = ReservationHandler.of
                .createIsAvailableResponseType();
        // mockResponseType.setAlternativeStartTimeOffset(1337L);

        // set each connection to status available
        for (final ServiceConstraintType serCons : request.getService()) {
            for (final ConnectionConstraintType conCons : serCons
                    .getConnections()) {
                // DetailedResult det = new DetailedResult();
                final ConnectionAvailabilityType cAvail = ReservationHandler.of
                        .createConnectionAvailabilityType();
                cAvail.setAvailability(AvailabilityCodeType.AVAILABLE);
                cAvail.setConnectionID(conCons.getConnectionID());
                cAvail.setServiceID(serCons.getServiceID());
                mockResponseType.getDetailedResult().add(cAvail);
            }
        }

        // return response-Element
        return mockResponseType;
    }

    /**
     * returns a mock-response.
     * 
     * @param request
     *            ActivateType
     * @return ActivateResponseType
     */
    protected ActivateResponseType runMock(final ActivateType request) {
        final ActivateResponseType mockResponseType = ReservationHandler.of
                .createActivateResponseType();
        // there's only one interesting field, the result
        mockResponseType.setSuccess(false);

        // return response-Element
        return mockResponseType;
    }

    /**
     * returns a mock-response.
     * 
     * @param request
     *            GetStatusType
     * @return GetStatusResponseType
     */
    protected GetStatusResponseType runMock(final GetStatusType request) {
        final GetStatusResponseType mockResponseType = ReservationHandler.of
                .createGetStatusResponseType();
        // getStatusType.getReservationID();
        for (final Integer id : request.getServiceID()) {
            final ServiceStatus serStat = new ServiceStatus();
            serStat.setServiceID(id.intValue());
            serStat.setStatus(StatusType.UNKNOWN);
            final ConnectionStatusType conStatus = ReservationHandler.of
                    .createConnectionStatusType();
            conStatus.setActualBW(0);
            conStatus.setConnectionID(444);
            conStatus.setDirectionality(1);

            final EndpointType c1 = new EndpointType();
            c1.setEndpointId("127.0.0.1");
            c1.setName("DCE1");
            c1.setDescription("Dummy-Connection Endpoint 1");
            c1.setDomainId("TestDomain1");
            c1.setBandwidth(Integer.valueOf(1));
            c1.setInterface(EndpointInterfaceType.USER);
            conStatus.setSource(c1);
            final EndpointType c2 = new EndpointType();
            c2.setEndpointId("10.0.0.1");
            c2.setName("DCE2");
            c2.setDescription("Dummy-Connection Endpoint 2");
            c2.setDomainId("TestDomain2");
            c2.setBandwidth(Integer.valueOf(1));
            c2.setInterface(EndpointInterfaceType.USER);
            conStatus.getTarget().add(c2);

            conStatus.setStatus(StatusType.UNKNOWN);
            serStat.getConnections().add(conStatus);

            final DomainStatusType domStatus = ReservationHandler.of
                    .createDomainStatusType();
            domStatus.setDomain("viola");
            domStatus.setStatus(StatusType.UNKNOWN);
            serStat.getDomainStatus().add(domStatus);

            // serStat.setStatus(4);
            mockResponseType.getServiceStatus().add(serStat);
        }
        return mockResponseType;
    }

    /**
     * create a unique ID.
     * 
     * @return long
     */
    private synchronized long getUniqueRequestId() {
        long requestID = System.currentTimeMillis();
        if (ReservationHandler.lastRequestID >= requestID) {
            requestID = ++ReservationHandler.lastRequestID;
        } else {
            ReservationHandler.lastRequestID = requestID;
        }
        return requestID;
    }

    /**
     * @param <T>
     *            the replyType-Class Parameter
     * @param handle
     *            the reservation Handle
     * @param document
     *            the xmlString to be sent
     * @param service
     *            the ServiceType
     * @param replyType
     *            the ClassType of the ReplyObject
     * @return the ReplyObject of Type T
     */
    private <T> T sendRequestMessage(final long handle, final String document,
            final String service, final Class<T> replyType) {

        // JMSConnector factory = null;
        String replyStr = null;
        MapMessage replyMessage = null;
        Session session = null;

        try {

            final MapMessage message = ReservationHandler.jmsConnector
                    .createMapMessage();
            message.setStringProperty("Service", service);
            message.setStringProperty("Mode", "Request");

            message.setString("Service", service);
            message.setString("Mode", "Request");

            /*
             * set IresId to be able to identify the answer on JMS-Topic
             */
            message.setLong("IresId", handle);

            /*
             * set the actual message
             */
            message.setString("Message", document);

            /*
             * set the correlation ID
             */
            // message.setJMSCorrelationID(""+handle);
            /*
             * send Message to JMS-Topic
             */
            // ReservationHandler.jmsConnector.sendMessage(message);
            session = ReservationHandler.jmsConnector.getReceiverSession();
            String messageSelector = new String("JMSCorrelationID = '" + handle
                    + "'");
            MessageConsumer receiver = session.createConsumer(
                    ReservationHandler.jmsConnector.getDestination(),
                    messageSelector);
            jmsConnector.sendMessage(message);
            Message msg = receiver.receive(TIMEOUT); // waiting 30 seconds

            log.debug("received reply " + msg + " now");
            replyMessage = (MapMessage) msg;

        } catch (final JMSException ex) {
            throw new RuntimeException(
                    "Could not delegate the request to Argon");
        }

        try {
            session.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }

        if (replyMessage == null) {
            throw new RuntimeException("no answer from argon within "
                    + ReservationHandler.TIMEOUT
                    / ReservationHandler.SEC_MILLISEC_FACTOR);
        }

        /*
         * get included Message
         */
        try {
            replyStr = replyMessage.getString("Message");
        } catch (final JMSException e) {
            throw new RuntimeException("Could not generate answer");
        }

        /*
         * build object out of xml-String
         */
        final IresJaxb converter = new IresJaxb(
                "de.unibonn.viola.argon.utils.jaxb.external.classes.IRes");
        T reply;
        try {
            reply = converter.unmarshall(replyStr, replyType);
        } catch (final JAXBException ex) {
            throw new RuntimeException("Could not generate answer");
        }

        return reply;

    }

    /**
     * @param <S>
     *            the type of the Reply
     * @param <T>
     *            the type of the Request
     * @param body
     *            the request to be sent
     * @param replyType
     *            the replyObject-Class
     * @return
     */
    private <S, T> S convertAndSend(final T body, final Class<S> replyType) {

        try {
            final IresJaxb converter = new IresJaxb(
                    "de.unibonn.viola.argon.utils.jaxb.external.classes.IRes");

            final long handle = this.getUniqueRequestId();

            /*
             * get xmlString of Jaxb-Object
             */
            final String xmlString = converter.marshall(body);

            ReservationHandler.log.debug("xmlString: " + xmlString);

            String service = null;

            if (body instanceof ReservationRequestType) {
                service = ReservationHandler.RESERVATION;
            } else if (body instanceof ModificationRequestType) {
                service = ReservationHandler.MODIFICATION;
            } else if (body instanceof QueryRequestType) {
                service = ReservationHandler.QUERY;
            } else if (body instanceof CancelRequestType) {
                service = ReservationHandler.CANCEL;
            } else if (body instanceof BindRequestType) {
                service = ReservationHandler.BIND;
            } else if (body instanceof AvailabilityRequestType) {
                service = ReservationHandler.AVAILABILITY;
            } else if (body instanceof ActivationRequestType) {
                service = ReservationHandler.ACTIVATION;
            }

            String requestType = body.getClass().getSimpleName();
            //Thread.currentThread().setName(requestType);
            this.performanceLogger.debug("start_" + requestType);
            long startTime = System.currentTimeMillis();

            S reply = this.sendRequestMessage(handle, xmlString, service,
                    replyType);

            long duration = System.currentTimeMillis() - startTime;
            this.performanceLogger.info("duration_" + requestType + " " + duration + "ms");
            return reply;

        } catch (final JAXBException ex) {
            throw new RuntimeException(
                    "Could not delegate the request to Argon");
        }

    }
}
