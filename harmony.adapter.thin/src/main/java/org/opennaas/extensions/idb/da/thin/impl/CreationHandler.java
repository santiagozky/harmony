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


package org.opennaas.extensions.idb.da.thin.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.SimpleTimeZone;

import org.apache.log4j.Logger;
import org.apache.muse.ws.addressing.soap.SoapFault;
import org.quartz.SchedulerException;

import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.CreatePathResponse;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.CreatePathResponseType;
import org.opennaas.extensions.idb.da.thin.persistence.DbManager;
import org.opennaas.extensions.idb.da.thin.persistence.exceptions.DestinationPortUnavailableException;
import org.opennaas.extensions.idb.da.thin.persistence.exceptions.PathNotFoundException;
import org.opennaas.extensions.idb.da.thin.persistence.exceptions.SourceAndDestinationPortUnavailableException;
import org.opennaas.extensions.idb.da.thin.persistence.exceptions.SourcePortUnavailableException;
import org.opennaas.extensions.idb.da.thin.persistence.orm.GmplsConnection;
import org.opennaas.extensions.idb.da.thin.scheduler.JobManager;
import org.opennaas.extensions.idb.da.thinutils.Notifications;
import org.opennaas.extensions.idb.da.thin.webservice.ContextListener;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ConnectionConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CreateReservationResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CreateReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ServiceConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.StatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.InvalidRequestFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.InvalidReservationIDFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.utils.AJaxbSerializer;
import org.opennaas.extensions.idb.serviceinterface.databinding.utils.WebserviceUtils;
import org.opennaas.core.utils.Helpers;
import org.opennaas.core.utils.PhLogger;

/**
 * @author Stephan Wagner (stephan.wagner@iais.fraunhofer.de), Daniel Beer
 *         (daniel.beer@iais.fraunhofer.de)
 */
public final class CreationHandler {

    /** Singleton instance. */
    private static CreationHandler selfInstance = null;
    private static Logger logger = PhLogger.getLogger(CreationHandler.class);

    /**
     * Instance getter.
     * 
     * @return Singleton Instance
     */
    public static CreationHandler getInstance() {
	if (selfInstance == null) {
	    selfInstance = new CreationHandler();
	}
	return selfInstance;
    }

    /**
     * sets up a gmpls connection through the gmpls web service.
     * 
     * @param gmplsConnection
     *            connection to be established
     * @return true if connection could be established
     * @throws InvalidRequestFaultException
     *             request was not valid
     * @throws SoapFault
     *             should not happen
     */
    public static int setUpConnection(final GmplsConnection gmplsConnection)
	    throws SoapFault {
	AJaxbSerializer jserGmpls = org.opennaas.extensions.gmpls.serviceinterface.databinding.utils.JaxbSerializer
		.getInstance();
	try {
	    gmplsConnection.setStatus(StatusType.SETUP_IN_PROGRESS);
	    DbManager.updateStatus(gmplsConnection,
		    "Trying to setup the Connection");
	} catch (UnexpectedFaultException e1) {
	    e1.printStackTrace();
	}
	final CreatePathResponse response = (CreatePathResponse) jserGmpls
		.elementToObject(ContextListener.getGmplsWS().createPath(
			jserGmpls.objectToElement(gmplsConnection
				.getCreatePathRequest())));
	CreatePathResponseType resp = response.getCreatePathResponse();
	gmplsConnection.setPathId(resp.getPathIdentifier().getPathIdentifier());
	gmplsConnection.setStatus(StatusType.ACTIVE);

	DbManager.updatePathId(gmplsConnection);
	DbManager.updateStatus(gmplsConnection, "PathId is "
		+ gmplsConnection.getPathId());
	Notifications.subscribe(gmplsConnection.getPathId());
	return gmplsConnection.getPathId();
    }

    /**
     * Private constructor: Singleton.
     */
    private CreationHandler() {
	//
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
     * Creates a new reservation in the thin Nrps.
     * 
     * @param createReservationRequest
     *            request
     * @return response for request as CreateReservationResponseType
     * @throws UnexpectedFaultException
     *             if reservation is not of type fixed
     * @throws InvalidReservationIDFaultException
     */
    public CreateReservationResponseType createReservation(
	    final CreateReservationType createReservationRequest)
	    throws UnexpectedFaultException, InvalidReservationIDFaultException {

	CreateReservationResponseType response = new CreateReservationResponseType();

	long jobId = System.currentTimeMillis();
	if (createReservationRequest.isSetJobID()
		&& createReservationRequest.getJobID().longValue() > 0) {
	    jobId = createReservationRequest.getJobID().longValue();
	}

	long reservationId = DbManager.insertReservation(jobId,
		createReservationRequest.getNotificationConsumerURL());

	boolean success = true;
	boolean partSuccess = false;

	List<GmplsConnection> connections = new ArrayList<GmplsConnection>();

	response.setJobID(Long.valueOf(jobId));
	response.setReservationID(WebserviceUtils
		.convertReservationID(reservationId));

	for (ServiceConstraintType sct : createReservationRequest.getService()) {
	    if (sct
		    .getTypeOfReservation()
		    .equals(
			    org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ReservationType.FIXED)) {

		Calendar calStart = Helpers.xmlCalendarToCalendar(sct
			.getFixedReservationConstraints().getStartTime());
		Timestamp startTime = new Timestamp(calStart.getTime()
			.getTime());

		calStart.setTimeZone(SimpleTimeZone.getDefault());
		calStart.add(Calendar.SECOND, sct
			.getFixedReservationConstraints().getDuration());
		Timestamp endTime = new Timestamp(calStart.getTimeInMillis());
		for (ConnectionConstraintType cct : sct.getConnections()) {
		    GmplsConnection con = new GmplsConnection();
		    con.setJobId(jobId);
		    con.setReservationId(reservationId);
		    con.setServiceId(sct.getServiceID());
		    con.setConnectionId(cct.getConnectionID());
		    con.setSrcTNA(cct.getSource().getEndpointId());
		    con.setDestTNA(cct.getTarget().get(0).getEndpointId());
		    con.setStartTime(startTime);
		    con.setEndTime(endTime);
		    con.setBandwidth(cct.getMinBW());
		    con.setAutoActivation(sct.isAutomaticActivation());

		    try {
			partSuccess = DbManager.insertConnection(con);
		    } catch (SourcePortUnavailableException e) {
			partSuccess = false;
			logger.error(e.getMessage(), e);
		    } catch (DestinationPortUnavailableException e) {
			partSuccess = false;
			logger.error(e.getMessage(), e);
		    } catch (SourceAndDestinationPortUnavailableException e) {
			partSuccess = false;
			logger.error(e.getMessage(), e);
		    } catch (PathNotFoundException e) {
			partSuccess = false;
			logger.error(e.getMessage(), e);
		    }

		    if (partSuccess) {
			connections.add(con);
		    }

		    success &= partSuccess;

		}
	    } else {
		DbManager.deleteWholeReservation(reservationId);
		throw new UnexpectedFaultException(
			"Only FIXED ReservationType supported");
	    }
	}

	if (!success) {
	    logger.debug("No success");
	    DbManager.deleteWholeReservation(reservationId);
	    throw new UnexpectedFaultException("No Path found.");
	}
	for (GmplsConnection gmplsConnection : connections) {
	    try {
		if (gmplsConnection.isAutoActivation()) {

		    if (gmplsConnection.getStartTime().before(
			    Helpers.xmlCalendarToDate(Helpers
				    .generateXMLCalendar(0, 0)))
			    && gmplsConnection.getEndTime().after(
				    Helpers.xmlCalendarToDate(Helpers
					    .generateXMLCalendar()))) {
			if (0 <= setUpConnection(gmplsConnection)) {
			    JobManager.getInstance().schedulePathTermination(
				    gmplsConnection);

			} else {
			    gmplsConnection.setStatus(StatusType.UNKNOWN);
			    DbManager
				    .updateStatus(gmplsConnection,
					    "Path could not be set up! PathId returned is 0");
			    throw new UnexpectedFaultException(
				    "Path could not be set up");
			}
		    } else {
			JobManager.getInstance().schedulePathSetUp(
				gmplsConnection);
			JobManager.getInstance().schedulePathTermination(
				gmplsConnection);
			gmplsConnection.setStatus(StatusType.PENDING);
			DbManager.updateStatus(gmplsConnection,
				"Connection has been scheduled");
		    }
		}
	    } catch (SchedulerException e) {
		gmplsConnection.setStatus(StatusType.UNKNOWN);
		DbManager.updateStatus(gmplsConnection, e.getMessage());
		throw new UnexpectedFaultException(e);
	    } catch (InvalidRequestFaultException e) {
		gmplsConnection.setStatus(StatusType.UNKNOWN);
		DbManager.updateStatus(gmplsConnection, e.getMessage());
		throw new UnexpectedFaultException(e);
	    } catch (SoapFault e) {
		gmplsConnection.setStatus(StatusType.UNKNOWN);
		DbManager.updateStatus(gmplsConnection, e.getMessage());
		throw new UnexpectedFaultException(e);
	    }
	}
	Notifications.addTopic(response.getReservationID());
	return response;
    }

}
