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

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.muse.ws.addressing.soap.SoapFault;
import org.quartz.SchedulerException;

import org.opennaas.extensions.idb.da.thin.persistence.DbManager;
import org.opennaas.extensions.idb.da.thin.persistence.orm.GmplsConnection;
import org.opennaas.extensions.idb.da.thin.scheduler.JobManager;
import org.opennaas.extensions.idb.da.thinutils.Notifications;
import org.opennaas.extensions.idb.da.thin.webservice.ContextListener;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelJobResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelJobType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelReservationResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.StatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.InvalidRequestFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.InvalidReservationIDFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.PathNotFoundFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.utils.AJaxbSerializer;
import org.opennaas.extensions.idb.serviceinterface.databinding.utils.WebserviceUtils;
import org.opennaas.core.utils.PhLogger;

/**
 * @author Stephan Wagner (stephan.wagner@iais.fraunhofer.de), Daniel Beer
 *         (daniel.beer@iais.fraunhofer.de)
 */
public final class CancelHandler {

	/** Singleton instance. */
	private static CancelHandler selfInstance = null;
	private static Logger logger = null;

	/**
	 * Instance getter.
	 * 
	 * @return Singleton Instance
	 */
	public static CancelHandler getInstance() {
		if (selfInstance == null) {
			selfInstance = new CancelHandler();
		}
		return selfInstance;
	}

	/**
	 * Private constructor: Singleton.
	 */
	private CancelHandler() {
		logger = PhLogger.getLogger(this.getClass());
	}

	/**
	 * @param gmplsConnection
	 * @return
	 * @throws UnexpectedFaultException
	 */
	public boolean cancelConnection(final GmplsConnection gmplsConnection)
			throws UnexpectedFaultException {
		AJaxbSerializer jser = org.opennaas.extensions.idb.serviceinterface.databinding.utils.JaxbSerializer
				.getInstance();
		if (gmplsConnection.convertStatus() == StatusType.COMPLETED
				|| gmplsConnection.convertStatus() == StatusType.CANCELLED_BY_SYSTEM
				|| gmplsConnection.convertStatus() == StatusType.CANCELLED_BY_USER) {
			return false;
		}
		try {
			gmplsConnection.setStatus(StatusType.TEARDOWN_IN_PROGRESS);
			DbManager.updateStatus(gmplsConnection,
					"Called by CancelHandler:cancelConnection");
		} catch (UnexpectedFaultException e1) {
			e1.printStackTrace();
		}
		try {
			JobManager.getInstance().unschedulePathTermination(gmplsConnection);
		} catch (SchedulerException e2) {
			e2.printStackTrace();
		}
		try {
			JobManager.getInstance().unschedulePathSetUp(gmplsConnection);
		} catch (SchedulerException e2) {
			e2.printStackTrace();
		}
		try {
			gmplsConnection.reloadFromDb();
			logger.debug("Terminate GMPLS path: " + gmplsConnection.getPathId());
			jser.elementToObject(ContextListener
					.getGmplsWS()
					.terminatePath(
							(jser.objectToElement(gmplsConnection
									.getTerminatePathRequest(org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.StatusType.CANCELLED_BY_USER)))));

			gmplsConnection.reloadFromDb();
			if ((gmplsConnection.convertStatus() == StatusType.CANCELLED_BY_SYSTEM)
					|| (gmplsConnection.convertStatus() == StatusType.CANCELLED_BY_USER)
					|| (gmplsConnection.convertStatus() == StatusType.COMPLETED)) {
				return true;
			}
			gmplsConnection.setStatus(StatusType.CANCELLED_BY_USER);
			DbManager.updateStatus(gmplsConnection, "Canceled");

		} catch (InvalidRequestFaultException e) {
			logger.error(e.getMessage(), e);

		} catch (UnexpectedFaultException e) {
			logger.error(e.getMessage(), e);
		} catch (PathNotFoundFaultException e) {
			try {
				gmplsConnection.setStatus(StatusType.CANCELLED_BY_SYSTEM);
				DbManager.updateStatus(gmplsConnection, e.getMessage());
			} catch (UnexpectedFaultException e1) {
				logger.error(e1.getMessage(), e1);
			}
			logger.error(e.getMessage(), e);
		} catch (SoapFault e) {
			try {
				gmplsConnection.setStatus(StatusType.UNKNOWN);
				DbManager.updateStatus(gmplsConnection, e.getMessage());
			} catch (UnexpectedFaultException e1) {
				logger.error(e1.getMessage(), e1);

			}
			logger.error(e.getMessage(), e);

		}
		return true;

	}

	/**
	 * Cancels a job and removes the scheduled jobs.
	 * 
	 * @param cancelJobRequest
	 *            request
	 * @return response as CancelJobResponseType
	 * @throws UnexpectedFaultException
	 *             should not happen
	 */
	public CancelJobResponseType cancelJob(final CancelJobType cancelJobRequest)
			throws UnexpectedFaultException {

		CancelJobResponseType response = new CancelJobResponseType();
		boolean success = true;
		List<Long> reservations = DbManager
				.getAllReservationsForJob(cancelJobRequest.getJobID());
		for (Long reservationId : reservations) {
			success &= cancelReservation(reservationId.longValue()).isSuccess();
		}
		response.setSuccess(success);
		return response;
	}

	/**
	 * CancelReservation Handler.
	 * <p>
	 * Handler will accept CancelReservation-Requests and return a
	 * CancelReservation-Response containing the success-parameter.
	 * <p>
	 * 
	 * @param cancelReservationRequest
	 *            CancelReservationRequest
	 * @return CancelReservationResponse
	 * @throws UnexpectedFaultException
	 *             should not happen
	 * @throws InvalidReservationIDFaultException
	 */
	public CancelReservationResponseType cancelReservation(
			final CancelReservationType cancelReservationRequest)
			throws UnexpectedFaultException, InvalidReservationIDFaultException {
		return cancelReservation(cancelReservationRequest.getReservationID());
	}

	/**
	 * CancelReservation Handler.
	 * <p>
	 * Handler will accept CancelReservation-Requests and return a
	 * CancelReservation-Response containing the success-parameter.
	 * <p>
	 * 
	 * @param reservationId
	 *            id of the reservation to be canceled
	 * @return CancelReservationResponse
	 * @throws UnexpectedFaultException
	 *             should not happen
	 * @throws InvalidReservationIDFaultException
	 */
	private CancelReservationResponseType cancelReservation(
			final String reservationId) throws UnexpectedFaultException,
			InvalidReservationIDFaultException {
		return cancelReservation(WebserviceUtils
				.convertReservationID(reservationId));
	}

	/**
	 * CancelReservation Handler.
	 * <p>
	 * Handler will accept CancelReservation-Requests and return a
	 * CancelReservation-Response containing the success-parameter.
	 * <p>
	 * 
	 * @param reservationId
	 *            id of the reservation to be canceled
	 * @return CancelReservationResponse
	 * @throws UnexpectedFaultException
	 *             should not happen
	 */
	private CancelReservationResponseType cancelReservation(
			final long reservationId) throws UnexpectedFaultException {
		boolean success = true;
		CancelReservationResponseType responseType = new CancelReservationResponseType();

		for (GmplsConnection gmplsConnection : DbManager
				.getAllConnectionsForReservation(reservationId)) {
			success &= cancelConnection(gmplsConnection);
		}
		responseType.setSuccess(success);

		for (Integer serviceID : DbManager
				.getServiceIdsForReservation(reservationId)) {
			Notifications.publish(
					reservationId,
					StatusHandler.getStatusOfService(reservationId,
							serviceID.intValue(), false));
		}
		return responseType;
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
}
