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

import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.apache.muse.ws.addressing.soap.SoapFault;
import org.xml.sax.SAXException;

import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.GetPathStatus;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.GetPathStatusResponse;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.GetPathStatusResponseType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.GetPathStatusType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.PathIdentifierType;
import org.opennaas.extensions.idb.da.thin.persistence.DbManager;
import org.opennaas.extensions.idb.da.thin.persistence.orm.GmplsConnection;
import org.opennaas.extensions.idb.da.thin.persistence.orm.GmplsReservation;
import org.opennaas.extensions.idb.da.thinutils.Notifications;
import org.opennaas.extensions.idb.da.thin.webservice.ContextListener;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ConnectionConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ConnectionStatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DomainStatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.FixedReservationConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationsComplexType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationsResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationsType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatusResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ServiceConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.StatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatusResponseType.ServiceStatus;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.InvalidReservationIDFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.PathNotFoundFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.utils.AJaxbSerializer;
import org.opennaas.extensions.idb.serviceinterface.databinding.utils.WebserviceUtils;
import org.opennaas.core.utils.Config;
import org.opennaas.core.utils.PhLogger;

/**
 * @author Stephan Wagner (stephan.wagner@iais.fraunhofer.de), Daniel Beer
 *         (daniel.beer@iais.fraunhofer.de)
 */
public final class StatusHandler {
	/** Singleton instance. */
	private static StatusHandler selfInstance = null;
	private static Logger logger = null;

	private static String domainName = Config.getString("hsi", "domain.name");
	private static String gmplsDomainName = Config.getString("adapter",
			"domain.name");

	/**
	 * Instance getter.
	 * 
	 * @return Singleton Instance
	 */
	public static StatusHandler getInstance() {
		if (selfInstance == null) {
			selfInstance = new StatusHandler();
		}
		return selfInstance;
	}

	/**
	 * Private constructor: Singleton.
	 */
	private StatusHandler() {
		logger = PhLogger.getLogger(this.getClass());
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
	 * @param lspHandle
	 * @return
	 * @throws JAXBException
	 * @throws SAXException
	 * @throws IOException
	 * @throws SoapFault
	 */
	private static GetPathStatusResponseType getPathStatus(final int lspHandle)
			throws JAXBException, SAXException, IOException, SoapFault {
		final GetPathStatusType gpst = new GetPathStatusType();
		final GetPathStatus gps = new GetPathStatus();
		final PathIdentifierType pathId = new PathIdentifierType();
		pathId.setPathIdentifier(lspHandle);
		gpst.setPathIdentifier(pathId);
		gps.setGetPathStatus(gpst);
		final AJaxbSerializer jser = org.opennaas.extensions.gmpls.serviceinterface.databinding.utils.JaxbSerializer
				.getInstance();
		final GetPathStatusResponse response = (GetPathStatusResponse) jser
				.elementToObject(ContextListener.getGmplsWS().getPathStatus(
						(jser.objectToElement(gps))));
		return response.getGetPathStatusResponse();
	}

	/**
	 * Gets the status of an reservation.
	 * 
	 * @param getStatusRequest
	 *            request
	 * @return response as GetStatusResponseType
	 * @throws UnexpectedFaultException
	 *             should not happen
	 * @throws InvalidReservationIDFaultException
	 */
	public GetStatusResponseType getStatus(final GetStatusType getStatusRequest)
			throws UnexpectedFaultException, InvalidReservationIDFaultException {
		GetStatusResponseType response = new GetStatusResponseType();

		if (getStatusRequest.getServiceID().isEmpty()) {
			logger.debug("getServiceID was empty!");
			getStatusRequest.getServiceID().addAll(
					DbManager.getServiceIdsForReservation(new Long(
							getStatusRequest.getReservationID()).longValue()));
			logger.debug("getServiceID now has "
					+ getStatusRequest.getServiceID().size() + " service id's");
		}
		for (Integer serviceId : getStatusRequest.getServiceID()) {
			response.getServiceStatus().add(
					getStatusOfService(getStatusRequest.getReservationID(),
							serviceId.intValue(), true));
		}

		return response;
	}

	/**
	 * Gets the Status of a service within a reservation.
	 * 
	 * @param reservationId
	 *            id of the reservation
	 * @param serviceId
	 *            id of the service
	 * @return Status of the service
	 * @throws UnexpectedFaultException
	 *             should not happen
	 */
	public static ServiceStatus getStatusOfService(final long reservationId,
			final int serviceId, final boolean fromGmpls)
			throws UnexpectedFaultException {
		ServiceStatus status = new ServiceStatus();
		boolean publish = false;
		status.setServiceID(serviceId);
		List<GmplsConnection> connections = DbManager
				.getAllConnectionsForService(reservationId, serviceId);
		if (connections.isEmpty()) {

			logger.debug("!!! No connections for Service !!!");
			status.setStatus(StatusType.UNKNOWN);
			status.setServiceID(serviceId);
			DomainStatusType dst = new DomainStatusType();
			dst.setDomain(gmplsDomainName);

			dst.setStatus(StatusType.UNKNOWN);
			status.getDomainStatus().add(dst);
		} else {
			GmplsConnection firstConnection = connections.get(0);
			status.setStatus(firstConnection.convertStatus());
			DomainStatusType dst = new DomainStatusType();
			dst.setDomain(gmplsDomainName);
			dst.setStatus(firstConnection.convertStatus());

			for (GmplsConnection connection : connections) {
				ConnectionStatusType conStatus = new ConnectionStatusType();
				EndpointType endpointSource = new EndpointType();
				EndpointType endpointTarget = new EndpointType();

				conStatus.setActualBW(connection.getBandwidth());
				endpointSource.setEndpointId(connection.getSrcTNA());
				conStatus.setSource(endpointSource);
				endpointTarget.setEndpointId(connection.getDestTNA());
				conStatus.getTarget().add(endpointTarget);
				conStatus.setConnectionID(connection.getConnectionId());
				conStatus.setDirectionality(1);
				if (connection.convertStatus() == StatusType.ACTIVE) {
					if (fromGmpls) {
						try {
							getPathStatus(connection.getPathId());
						} catch (PathNotFoundFaultException e) {
							logger.debug("connection wich was active on thin could not be found on gmpls-WS ");
							logger.debug("PathID was:      "
									+ connection.getPathId());
							logger.debug("connectionID was:"
									+ connection.getConnectionId());
							connection
									.setStatus(StatusType.CANCELLED_BY_SYSTEM);
							publish = true;
							DbManager.updateStatus(connection,
									"Could not be found on gmpls-WS");
						} catch (JAXBException e) {
							connection.setStatus(StatusType.UNKNOWN);
							logger.error(e.getMessage(), e);
						} catch (SAXException e) {
							connection.setStatus(StatusType.UNKNOWN);
							logger.error(e.getMessage(), e);
						} catch (IOException e) {
							connection.setStatus(StatusType.UNKNOWN);
							logger.error(e.getMessage(), e);
						} catch (SoapFault e) {
							connection.setStatus(StatusType.UNKNOWN);
							logger.error(e.getMessage(), e);
						}
					}

				}
				conStatus.setStatus(connection.convertStatus());
				status.getConnections().add(conStatus);
				if ((conStatus.getStatus().ordinal() > dst.getStatus()
						.ordinal())) {
					dst.setStatus(conStatus.getStatus());
				}
			}
			status.getDomainStatus().add(dst);
		}

		if (publish) {
			logger.info("publishing new status from getStatusOfService");
			Notifications.publish(reservationId, status);
		}
		return status;
	}

	/**
	 * Gets the Status of a service within a reservation.
	 * 
	 * @param reservationId
	 *            id of the reservation
	 * @param serviceId
	 *            id of the service
	 * @return Status of the service
	 * @throws UnexpectedFaultException
	 *             should not happen
	 * @throws InvalidReservationIDFaultException
	 */
	public static ServiceStatus getStatusOfService(final String reservationId,
			final int serviceId, final boolean fromGmpls)
			throws UnexpectedFaultException, InvalidReservationIDFaultException {
		return getStatusOfService(
				WebserviceUtils.convertReservationID(reservationId), serviceId,
				fromGmpls);
	}

	/**
	 * @param getReservationRequest
	 * @return
	 * @throws UnexpectedFaultException
	 * @throws InvalidReservationIDFaultException
	 */
	public GetReservationResponseType getReservation(
			final GetReservationType getReservationRequest)
			throws UnexpectedFaultException, InvalidReservationIDFaultException {

		GetReservationResponseType response = getReservation(
				getReservationRequest.getReservationID(),
				getReservationRequest.getServiceID());

		return response;
	}

	/**
	 * @param reservationId
	 * @param serviceId
	 * @return
	 * @throws UnexpectedFaultException
	 */
	private ServiceConstraintType getServiceConstraints(
			final long reservationId, final int serviceId)
			throws UnexpectedFaultException {
		ServiceConstraintType constraint = new ServiceConstraintType();
		FixedReservationConstraintType frct = new FixedReservationConstraintType();

		constraint.setServiceID(serviceId);
		constraint.setTypeOfReservation(ReservationType.FIXED);

		List<GmplsConnection> connections = DbManager
				.getAllConnectionsForService(reservationId, serviceId);
		if (connections.isEmpty()) {
			logger.debug("!!! No connections for Service !!!");
			constraint.setServiceID(serviceId);
		} else {
			GmplsConnection firstConnection = connections.get(0);
			constraint.setAutomaticActivation(firstConnection
					.isAutoActivation());

			frct.setStartTime(firstConnection.getStartTimeAsXMLCalendar());
			frct.setDuration(firstConnection.getDuration());

			constraint.setFixedReservationConstraints(frct);

			for (GmplsConnection connection : connections) {
				ConnectionConstraintType conConstraint = new ConnectionConstraintType();
				EndpointType endpointSource = new EndpointType();
				EndpointType endpointTarget = new EndpointType();

				conConstraint.setConnectionID(connection.getConnectionId());
				conConstraint.setDirectionality(1);
				conConstraint.setMinBW(connection.getBandwidth());

				endpointSource.setEndpointId(connection.getSrcTNA());
				conConstraint.setSource(endpointSource);
				endpointTarget.setEndpointId(connection.getDestTNA());
				conConstraint.getTarget().add(endpointTarget);
				constraint.getConnections().add(conConstraint);
			}
		}
		return constraint;
	}

	/**
	 * @param getReservationsRequest
	 * @return
	 * @throws UnexpectedFaultException
	 */
	public GetReservationsResponseType getReservations(
			final GetReservationsType getReservationsRequest)
			throws UnexpectedFaultException {
		GetReservationsResponseType response = new GetReservationsResponseType();

		List<GmplsReservation> gmplsReservations = DbManager
				.getReservationsInPeriod(
						getReservationsRequest.getPeriodStartTime(),
						getReservationsRequest.getPeriodEndTime());
		long reservationId = 0;
		for (GmplsReservation gmplsReservation : gmplsReservations) {
			GetReservationsComplexType grct = new GetReservationsComplexType();
			reservationId = gmplsReservation.getReservationId();
			grct.setReservationID(WebserviceUtils
					.convertReservationID(reservationId));
			grct.setReservation(getReservation(
					gmplsReservation.getReservationId(),
					DbManager.getServiceIdsForReservation(reservationId)));
			response.getReservation().add(grct);
		}

		return response;
	}

	/**
	 * @param reservationId
	 * @param serviceIds
	 * @return
	 * @throws UnexpectedFaultException
	 */
	private GetReservationResponseType getReservation(final long reservationId,
			final List<Integer> serviceIds) throws UnexpectedFaultException {

		GetReservationResponseType response = new GetReservationResponseType();

		GmplsReservation res = DbManager.getReservation(reservationId);

		response.setJobID(Long.valueOf(res.getJobId()));
		response.setNotificationConsumerURL(res.getNotificationConsumerURL());

		for (Integer serviceId : serviceIds) {
			response.getService().add(
					getServiceConstraints(reservationId, serviceId.intValue()));
		}

		return response;
	}

	/**
	 * @param reservationId
	 * @param serviceIds
	 * @return
	 * @throws UnexpectedFaultException
	 * @throws InvalidReservationIDFaultException
	 */
	private GetReservationResponseType getReservation(
			final String reservationId, final List<Integer> serviceIds)
			throws UnexpectedFaultException, InvalidReservationIDFaultException {
		return getReservation(
				WebserviceUtils.convertReservationID(reservationId), serviceIds);
	}
}
