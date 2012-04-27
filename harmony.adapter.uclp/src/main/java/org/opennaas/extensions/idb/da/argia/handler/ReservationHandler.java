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


package org.opennaas.extensions.idb.da.argia.handler;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;
import org.globus.myproxy.MyProxy;
import org.globus.myproxy.MyProxyException;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;

import ca.inocybe.argia.stubs.reservation.Activate;
import ca.inocybe.argia.stubs.reservation.ActivateResponse;
import ca.inocybe.argia.stubs.reservation.CancelJob;
import ca.inocybe.argia.stubs.reservation.CancelJobResponse;
import ca.inocybe.argia.stubs.reservation.CompleteJob;
import ca.inocybe.argia.stubs.reservation.CompleteJobResponse;
import ca.inocybe.argia.stubs.reservation.CreateReservation;
import ca.inocybe.argia.stubs.reservation.CreateReservationResponse;
import ca.inocybe.argia.stubs.reservation.EndpointNotFoundFault;
import ca.inocybe.argia.stubs.reservation.GetReservations;
import ca.inocybe.argia.stubs.reservation.GetReservationsResponse;
import ca.inocybe.argia.stubs.reservation.GetStatus;
import ca.inocybe.argia.stubs.reservation.GetStatusResponse;
import ca.inocybe.argia.stubs.reservation.IsAvailable;
import ca.inocybe.argia.stubs.reservation.IsAvailableResponse;
import ca.inocybe.argia.stubs.reservation.ServiceStatusType;
import ca.inocybe.argia.stubs.reservation.UnexpectedFault;
import org.opennaas.extensions.idb.da.argia.implementation.AdvReservationsWrapper;
import org.opennaas.extensions.idb.da.argia.implementation.Session;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ActivateResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ActivateType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AvailabilityCodeType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelJobResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelJobType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelReservationResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CompleteJobResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CompleteJobType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ConnectionAvailabilityType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ConnectionConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ConnectionStatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CreateReservationResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CreateReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DomainStatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointInterfaceType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.FixedReservationConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationsResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationsType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatusResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.IsAvailableResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.IsAvailableType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.MalleableReservationConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ObjectFactory;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ServiceConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.StatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatusResponseType.ServiceStatus;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.EndpointNotFoundFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.InvalidReservationIDFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.utils.WebserviceUtils;
import org.opennaas.extensions.idb.serviceinterface.reservation.CommonReservationHandler;
import org.opennaas.core.utils.Config;

/**
 * Class to handle NSP reservation-requests in a predictable manner.
 * 
 * @author Angel Sánchez (angel.sanchez@i2cat.net)
 * @author Jordi Ferrer-Riera (jordi.ferrer@i2cat.net)
 */
public final class ReservationHandler extends CommonReservationHandler {

	/*
	 * Instance Variables
	 * =========================================================================
	 */

	/** Singleton instance. */
	private static ReservationHandler selfInstance = null;

	/**
	 * The logger.
	 */
	private static final Logger logger = Logger
			.getLogger(ReservationHandler.class);

	/**
	 * The object factory for the muse classes
	 */
	private static final ObjectFactory objectFactory = new ObjectFactory();

	/*
	 * Singleton Stuff
	 * =========================================================================
	 */

	/**
	 * Instance getter.
	 * 
	 * @return Singleton Instance
	 */
	public static ReservationHandler getInstance() {
		if (ReservationHandler.selfInstance == null) {
			ReservationHandler.selfInstance = new ReservationHandler();
		}
		return ReservationHandler.selfInstance;
	}

	/** Private constructor: Singleton. */
	private ReservationHandler() {

		ReservationHandler.logger.info("Getting AAA");

		// AAA
		final String login = Config.getString("uclp", "uclp.user");
		final String psw = Config.getString("uclp", "uclp.password");
		final String host = Config.getString("uclp", "epr.uclp.host");
		final String userHome = System.getProperty("user.home");

		final File localCertPath = new File(userHome + File.separator
				+ ".globus" + File.separator + "certificates");
		final String localCertName = host + "-CAcert.0";
		final String remoteCertPath = "http://" + host + "/" + host
				+ "-CAcert.0";
		final File fullCertPath = new File(localCertPath, localCertName);

		// First check to see if the CA Certificate has been downloaded
		if (!fullCertPath.exists()) {
			// create the directory
			localCertPath.mkdirs();
			try {
				OutputStream outs = null;
				URLConnection conn = null;
				InputStream in = null;
				try {
					final URL url = new URL(remoteCertPath);
					outs = new BufferedOutputStream(new FileOutputStream(
							fullCertPath));
					final Proxy proxy = new Proxy(Proxy.Type.HTTP,
							new InetSocketAddress(host, 80));
					conn = url.openConnection(proxy);
					in = conn.getInputStream();
					final byte[] buffer = new byte[1024];
					int numRead;
					long numWritten = 0;
					while ((numRead = in.read(buffer)) != -1) {
						outs.write(buffer, 0, numRead);
						numWritten += numRead;
					}
					in.close();
					outs.close();
				} catch (final Exception e) {

					if (in != null) {
						in.close();
					}

					// delete the local file if the download failed
					fullCertPath.delete();
					throw new IOException(e.getMessage());
				}
			} catch (final IOException e) {
				e.printStackTrace();
				// throw new InvocationTargetException(e, "Error getting CA from
				// " + remoteCertPath);
			}
		}

		GSSCredential newCred = null;
		final MyProxy myProxy = new org.globus.myproxy.MyProxy(host, 7512);

		try {

			myProxy
					.setAuthorization(new org.globus.gsi.gssapi.auth.NoAuthorization());

			// 1 year
			newCred = myProxy.get(null, login, psw, 31536000);
			String aux = newCred.getName().toString();
			StringTokenizer tokenizer = new StringTokenizer(aux, "/");
			aux = tokenizer.nextToken();
			tokenizer = new StringTokenizer(aux, "=");
			tokenizer.nextToken();
			final String organization = tokenizer.nextToken();

			// account = UserWorkspaceWSWrapper.queryUser(login, host, newCred);

			final Session ses = Session.getInstance();
			ses.setCredential(newCred);
			ses.setHost(host);
			ses.setPort("8443");

			// ses.setOrganization(account.getOrganization());
			ses.setOrganization(organization);

			ReservationHandler.logger.info("Got AAA!");

		} catch (final MyProxyException e) {
			e.printStackTrace();
		} catch (final GSSException e) {
			e.printStackTrace();
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

	/*
	 * TestMode
	 * =========================================================================
	 */
	/**
	 * @return True if testMode is enabled and false in other case Test mode =
	 *         No calls to Argia. Responses generated.
	 */
	protected boolean testMode() {
		return Config.isTrue("uclp", "testmode");
	}

	/*
	 * Handler
	 * =========================================================================
	 */

	/**
	 * Activation Handler.
	 * <p>
	 * Handler will redirect to UCLP Activation-Requests and return a
	 * Activation-Response from the UCLP Server.
	 * <p>
	 * 
	 * @param element
	 *            ActivationRequest
	 * @return ActivationResponse
	 * @throws InvalidReservationIDFaultException
	 */
	@Override
	public ActivateResponseType activate(final ActivateType element)
			throws InvalidReservationIDFaultException {

		// Test mode
		if (this.testMode()) {
			ReservationHandler.logger.info("Running in test mode");
			return this.runTestMode(element);
		}

		ActivateResponse uclpResp = new ActivateResponse();
		final ActivateResponseType responseType = new ActivateResponseType();

		final Activate act = new Activate();
		final ca.inocybe.argia.stubs.reservation.ActivateType acType = new ca.inocybe.argia.stubs.reservation.ActivateType();

		acType.setReservationID(WebserviceUtils.convertReservationID(element
				.getReservationID()));
		acType.setServiceID(element.getServiceID());
		act.setActivate(acType);
		act.setOrganization(Session.getInstance().getOrganization());

		try {
			uclpResp = AdvReservationsWrapper.activate(act);

			responseType.setSuccess(uclpResp.getActivateResponse().isSuccess());
		} catch (final RemoteException e) {
			e.printStackTrace();
			responseType.setSuccess(false);
		}

		return responseType;
	}

	/**
	 * CancelJob Handler.
	 * <p>
	 * Handler will accept CancelJob-Requests and return a CancelJob-Response
	 * containing the success-parameter set to true.
	 * <p>
	 * 
	 * @param element
	 *            CancelJobRequest
	 * @return CancelJobResponse
	 */
	@Override
	public CancelJobResponseType cancelJob(final CancelJobType element) {

		final CancelJob cancel = new CancelJob();
		final ca.inocybe.argia.stubs.reservation.CancelJobType cancelType = new ca.inocybe.argia.stubs.reservation.CancelJobType();

		final CancelJobResponseType responseType = new CancelJobResponseType();
		CancelJobResponse uclpResponse = new CancelJobResponse();

		cancelType.setJobID(element.getJobID());

		cancel.setCancelJob(cancelType);
		cancel.setOrganization(Session.getInstance().getOrganization());

		try {
			uclpResponse = AdvReservationsWrapper.cancelJob(cancel);

			responseType.setSuccess(uclpResponse.getCancelJobResponse()
					.isSuccess());
		} catch (final RemoteException e) {
			e.printStackTrace();
			responseType.setSuccess(false);
		}

		return responseType;
	}

	/**
	 * CancelReservation Handler.
	 * <p>
	 * Handler will accept CancelReservation-Requests and return a
	 * CancelReservation-Response containing the success-parameter set to true.
	 * <p>
	 * 
	 * @param element
	 *            CancelReservationRequest
	 * @return CancelReservationResponse
	 * @throws InvalidReservationIDFaultException
	 */
	@Override
	public CancelReservationResponseType cancelReservation(
			final CancelReservationType element)
			throws InvalidReservationIDFaultException {

		// Test mode
		if (this.testMode()) {
			ReservationHandler.logger.info("Running in test mode");
			return this.runTestMode(element);
		}

		final ca.inocybe.argia.stubs.reservation.CancelReservationType cancelType = new ca.inocybe.argia.stubs.reservation.CancelReservationType();
		final ca.inocybe.argia.stubs.reservation.CancelReservation cancel = new ca.inocybe.argia.stubs.reservation.CancelReservation();

		final CancelReservationResponseType responseType = new CancelReservationResponseType();

		ca.inocybe.argia.stubs.reservation.CancelReservationResponse uclpResponse = new ca.inocybe.argia.stubs.reservation.CancelReservationResponse();

		cancelType.setReservationID(WebserviceUtils
				.convertReservationID(element.getReservationID()));
		cancel.setCancelReservation(cancelType);

		cancel.setOrganization(Session.getInstance().getOrganization());

		try {
			uclpResponse = AdvReservationsWrapper.cancelReservation(cancel);

			responseType.setSuccess(uclpResponse.getCancelReservationResponse()
					.isSuccess());
		} catch (final RemoteException e) {
			e.printStackTrace();
			responseType.setSuccess(false);
		}

		return responseType;
	}

	/**
	 * CompleteJob Handler.
	 * <p>
	 * Handler will accept CompleteJob-Requests and will translate and send them
	 * to the UCLP server Response containing the success-parameter returned by
	 * UCLP.
	 * <p>
	 * 
	 * @param element
	 *            CompleteJobRequest
	 * @return CompleteJobResponse
	 */
	@Override
	public CompleteJobResponseType completeJob(final CompleteJobType element) {

		final CompleteJob complete = new CompleteJob();
		final ca.inocybe.argia.stubs.reservation.CompleteJobType completeType = new ca.inocybe.argia.stubs.reservation.CompleteJobType();

		final CompleteJobResponseType responseType = new CompleteJobResponseType();
		ca.inocybe.argia.stubs.reservation.CompleteJobResponse uclpResponse = new CompleteJobResponse();

		completeType.setJobID(element.getJobID());
		complete.setCompleteJob(completeType);

		complete.setOrganization(Session.getInstance().getOrganization());

		try {
			uclpResponse = AdvReservationsWrapper.completeJob(complete);

			responseType.setSuccess(uclpResponse.getCompleteJobResponse()
					.isSuccess());
		} catch (final RemoteException e) {
			e.printStackTrace();
			responseType.setSuccess(false);
		}

		return responseType;
	}

	/**
	 * Reservation Handler.
	 * <p>
	 * Handler will accept CreateReservation-Requests and return a
	 * CreateReservation-Response containing a JobID, which is equal to the
	 * committed JobID, and a ReservationID, which will be set to $(committed
	 * JobID) + 2. <br>
	 * <p>
	 * 
	 * @param element
	 *            ReservationRequest
	 * @return ReservationResponse
	 */
	@Override
	public CreateReservationResponseType createReservation(
			final CreateReservationType element)
			throws EndpointNotFoundFaultException, RemoteException, Exception {

		// Test mode
		if (this.testMode()) {
			ReservationHandler.logger.info("Running in test mode");
			return this.runTestMode(element);

		}
		CreateReservationResponse uclpResp = new CreateReservationResponse();
		final CreateReservationResponseType responseType = new CreateReservationResponseType();

		final CreateReservation create = new CreateReservation();
		final ca.inocybe.argia.stubs.reservation.CreateReservationType createType = new ca.inocybe.argia.stubs.reservation.CreateReservationType();

		if (element.isSetJobID()) {
			createType.setJobID(element.getJobID());
		}

		// Get list of ServiceConstraintType (SCT)
		final List<ServiceConstraintType> l = element.getService();

		// Create array of (UCLP's) SCT
		final ca.inocybe.argia.stubs.reservation.ServiceConstraintType[] listSCT = new ca.inocybe.argia.stubs.reservation.ServiceConstraintType[l
				.size()];

		for (int i = 0; i < l.size(); i++) {
			final ca.inocybe.argia.stubs.reservation.ServiceConstraintType uclpSCT = new ca.inocybe.argia.stubs.reservation.ServiceConstraintType();

			// Get the SCT of the NSP's list
			final ServiceConstraintType nspSCT = l.get(i);

			uclpSCT.setAutomaticActivation(nspSCT.isAutomaticActivation());
			uclpSCT.setServiceID(nspSCT.getServiceID());

			if (nspSCT.getTypeOfReservation().value().equalsIgnoreCase("fixed")) {
				uclpSCT
						.setTypeOfReservation(ca.inocybe.argia.stubs.reservation.ReservationType.fixed);

				final FixedReservationConstraintType nspConst = nspSCT
						.getFixedReservationConstraints();

				final ca.inocybe.argia.stubs.reservation.FixedReservationConstraintType uclpConst = new ca.inocybe.argia.stubs.reservation.FixedReservationConstraintType();

				uclpConst.setDuration(nspConst.getDuration());
				uclpConst.setStartTime(nspConst.getStartTime()
						.toGregorianCalendar());

				uclpSCT.setFixedReservationConstraints(uclpConst);
			} else if (nspSCT.getTypeOfReservation().value().equalsIgnoreCase(
					"malleable")) {
				uclpSCT
						.setTypeOfReservation(ca.inocybe.argia.stubs.reservation.ReservationType.malleable);

				final MalleableReservationConstraintType nspConst = nspSCT
						.getMalleableReservationConstraints();

				final ca.inocybe.argia.stubs.reservation.MalleableReservationConstraintType uclpConst = new ca.inocybe.argia.stubs.reservation.MalleableReservationConstraintType();

				uclpConst.setDeadline(nspConst.getDeadline()
						.toGregorianCalendar());
				uclpConst.setStartTime(nspConst.getStartTime()
						.toGregorianCalendar());

				uclpSCT.setMalleableReservationConstraints(uclpConst);
			}
			// END Translate Type of Reservation

			// Translate the ConnectionConstraintTypes
			final List<ConnectionConstraintType> listNspCCT = nspSCT
					.getConnections();

			final ca.inocybe.argia.stubs.reservation.ConnectionConstraintType[] arrayUclpCCT = new ca.inocybe.argia.stubs.reservation.ConnectionConstraintType[listNspCCT
					.size()];

			for (int j = 0; j < arrayUclpCCT.length; j++) {
				final ca.inocybe.argia.stubs.reservation.ConnectionConstraintType uclpCCT = new ca.inocybe.argia.stubs.reservation.ConnectionConstraintType();

				final ConnectionConstraintType nspCCT = listNspCCT.get(j);

				uclpCCT.setConnectionID(nspCCT.getConnectionID());

				uclpCCT.setDataAmount(nspCCT.getDataAmount());
				uclpCCT.setDirectionality(nspCCT.getDirectionality());
				uclpCCT.setMaxBW(nspCCT.getMaxBW());

				// HACK!!!!!!!!!!!
				uclpCCT.setMaxDelay(500);
				// HACK!!!!!!!!!!!!!

				uclpCCT.setMinBW(nspCCT.getMinBW());

				// Translate EndpointType objects
				// Source Endpoint
				final EndpointType srcNspEP = nspCCT.getSource();

				final ca.inocybe.argia.stubs.reservation.EndpointType srcUclpEP = new ca.inocybe.argia.stubs.reservation.EndpointType();

				srcUclpEP.setEndpointId(srcNspEP.getEndpointId());

				uclpCCT.setSource(srcUclpEP);
				// END Source Endpoint

				// Destination Endpoints
				final List<EndpointType> destNspEPList = nspCCT.getTarget();

				final ca.inocybe.argia.stubs.reservation.EndpointType[] destUclpEPList = new ca.inocybe.argia.stubs.reservation.EndpointType[destNspEPList
						.size()];

				for (int k = 0; k < destUclpEPList.length; k++) {
					final EndpointType nspDest = destNspEPList.get(k);

					final ca.inocybe.argia.stubs.reservation.EndpointType uclpDest = new ca.inocybe.argia.stubs.reservation.EndpointType();

					uclpDest.setEndpointId(nspDest.getEndpointId());

					destUclpEPList[k] = uclpDest;
				}
				uclpCCT.setTarget(destUclpEPList);
				// END Destination Endpoints

				arrayUclpCCT[j] = uclpCCT;
			}

			uclpSCT.setConnections(arrayUclpCCT);

			listSCT[i] = uclpSCT;
		}

		createType.setService(listSCT);

		create.setCreateReservation(createType);
		create.setOrganization(Session.getInstance().getOrganization());

		try {
			ReservationHandler.logger.info("Sending create RSV req");

			uclpResp = AdvReservationsWrapper.createReservation(create);

			ReservationHandler.logger.info("Sent create RSV req");

		} catch (final EndpointNotFoundFault f) {
			ReservationHandler.logger
					.error("Argia/UCLP Adapter: Endpoint source or target does not exist");
			throw new EndpointNotFoundFaultException("Cannot locate endpoint");

		} catch (final UnexpectedFault ef) {
			ReservationHandler.logger
					.error("Argia/UCLP Adapter: Unexpected fault while creating reservation");
			throw new UnexpectedFaultException(ef.getDescription()[0]
					.get_value());

		} catch (final RemoteException e) {
			ReservationHandler.logger
					.error("Argia/UCLP Adapter: Remote exception: "
							+ e.getMessage());
			throw new Exception(e.getLocalizedMessage());
		} catch (Exception e) {
			ReservationHandler.logger.error("Argia/UCLP Adapter: Exception: "
					+ e.getLocalizedMessage());
			throw new Exception(e.getLocalizedMessage());
		}

		if ((uclpResp.getCreateReservationResponse() != null)
				&& (uclpResp.getCreateReservationResponse().getReservationID() > 0)) {
			// Get JobID and ReservationID
			if (uclpResp.getCreateReservationResponse().getJobID() != null) {
				responseType.setJobID(uclpResp.getCreateReservationResponse()
						.getJobID());
			}

			responseType
					.setReservationID(WebserviceUtils
							.convertReservationID(uclpResp
									.getCreateReservationResponse()
									.getReservationID()));

			return responseType;

			// Should never happen
		} else {
			ReservationHandler.logger
					.error("Argia/UCLP Adapter: Error in the NRPS while creating Reservation. Reservation ID was null");

			throw new Exception(
					"Argia/UCLP Adapter: Error in the NRPS while creating Reservation. Reservation ID was null");
		}
	}

	/**
	 * Status Handler.
	 * <p>
	 * Handler will accept GetStatus-Requests and return a GetStatus-Response
	 * containing a ServiceStatus-Object.
	 * <p>
	 * 
	 * @param element
	 *            StatusRequest
	 * @return StatusResponse
	 * @throws InvalidReservationIDFaultException
	 */
	@Override
	public GetStatusResponseType getStatus(final GetStatusType element)
			throws InvalidReservationIDFaultException {

		// Test mode
		if (this.testMode()) {
			ReservationHandler.logger.info("Running in test mode");
			return this.runTestMode(element);
		}
		final ca.inocybe.argia.stubs.reservation.GetStatusType getType = new ca.inocybe.argia.stubs.reservation.GetStatusType();
		final GetStatus getStatus = new GetStatus();

		final GetStatusResponseType responseType = new GetStatusResponseType();

		GetStatusResponse uclpResp = new GetStatusResponse();

		// Fill in the GetType object
		getType.setReservationID(WebserviceUtils.convertReservationID(element
				.getReservationID()));

		final int num_services = element.getServiceID().size();
		final int[] services = new int[num_services];

		final Iterator<Integer> iter = element.getServiceID().iterator();

		for (int i = 0; i < num_services; i++) {
			services[i] = iter.next().intValue();
		}

		getType.setServiceID(services);

		getStatus.setGetStatus(getType);
		getStatus.setOrganization(Session.getInstance().getOrganization());

		try {

			ReservationHandler.logger
					.info("Sending request to UCLP (GetStatus RSVID: "
							+ getType.getReservationID() + ")...");

			uclpResp = AdvReservationsWrapper.getStatus(getStatus);

			ReservationHandler.logger
					.info("Received response from UCLP! (GetStatus)");

		} catch (final RemoteException e) {
			e.printStackTrace();
		}

		final ca.inocybe.argia.stubs.reservation.ServiceStatusType[] statusResponseType = uclpResp
				.getGetStatusResponse().getServiceStatus();

		if (statusResponseType != null) {
			for (final ServiceStatusType element2 : statusResponseType) {
				final ServiceStatus servStatus = new ServiceStatus();

				servStatus.setServiceID(element2.getServiceID());

				// Set Service Status
				if (element2.getStatus().getValue().equalsIgnoreCase(
						StatusType.ACTIVE.value())) {
					servStatus.setStatus(StatusType.ACTIVE);
				} else if (element2.getStatus().getValue().equalsIgnoreCase(
						StatusType.CANCELLED_BY_SYSTEM.value())) {
					servStatus.setStatus(StatusType.CANCELLED_BY_SYSTEM);
				} else if (element2.getStatus().getValue().equalsIgnoreCase(
						StatusType.CANCELLED_BY_USER.value())) {
					servStatus.setStatus(StatusType.CANCELLED_BY_USER);
				} else if (element2.getStatus().getValue().equalsIgnoreCase(
						StatusType.COMPLETED.value())) {
					servStatus.setStatus(StatusType.COMPLETED);
				} else if (element2.getStatus().getValue().equalsIgnoreCase(
						StatusType.PENDING.value())) {
					servStatus.setStatus(StatusType.PENDING);
				} else if (element2.getStatus().getValue().equalsIgnoreCase(
						StatusType.SETUP_IN_PROGRESS.value())) {
					servStatus.setStatus(StatusType.SETUP_IN_PROGRESS);
				} else if (element2.getStatus().getValue().equalsIgnoreCase(
						StatusType.TEARDOWN_IN_PROGRESS.value())) {
					servStatus.setStatus(StatusType.TEARDOWN_IN_PROGRESS);
				} else if (element2.getStatus().getValue().equalsIgnoreCase(
						StatusType.UNKNOWN.value())) {
					servStatus.setStatus(StatusType.UNKNOWN);
				} else {
					servStatus.setStatus(StatusType.UNKNOWN);
				}

				// Set Connection Status Type
				final ca.inocybe.argia.stubs.reservation.ConnectionStatusType[] connStatus = element2
						.getConnections();

				for (final ca.inocybe.argia.stubs.reservation.ConnectionStatusType element3 : connStatus) {
					final ConnectionStatusType cst = new ConnectionStatusType();

					cst.setActualBW(element3.getActualBW());
					cst.setConnectionID(element3.getConnectionID());
					cst.setDirectionality(element3.getDirectionality());

					if (element3.getStatus().getValue().equalsIgnoreCase(
							StatusType.ACTIVE.value())) {
						cst.setStatus(StatusType.ACTIVE);
					} else if (element3.getStatus().getValue()
							.equalsIgnoreCase(
									StatusType.CANCELLED_BY_SYSTEM.value())) {
						cst.setStatus(StatusType.CANCELLED_BY_SYSTEM);
					} else if (element3.getStatus().getValue()
							.equalsIgnoreCase(
									StatusType.CANCELLED_BY_USER.value())) {
						cst.setStatus(StatusType.CANCELLED_BY_USER);
					} else if (element3.getStatus().getValue()
							.equalsIgnoreCase(StatusType.COMPLETED.value())) {
						cst.setStatus(StatusType.COMPLETED);
					} else if (element3.getStatus().getValue()
							.equalsIgnoreCase(StatusType.PENDING.value())) {
						cst.setStatus(StatusType.PENDING);
					} else if (element3.getStatus().getValue()
							.equalsIgnoreCase(
									StatusType.SETUP_IN_PROGRESS.value())) {
						cst.setStatus(StatusType.SETUP_IN_PROGRESS);
					} else if (element3.getStatus().getValue()
							.equalsIgnoreCase(
									StatusType.TEARDOWN_IN_PROGRESS.value())) {
						cst.setStatus(StatusType.TEARDOWN_IN_PROGRESS);
					} else if (element3.getStatus().getValue()
							.equalsIgnoreCase(StatusType.UNKNOWN.value())) {
						cst.setStatus(StatusType.UNKNOWN);
					} else {
						cst.setStatus(StatusType.UNKNOWN);
					}

					// Add source EP
					EndpointType ep = new EndpointType();
					ep.setEndpointId(element3.getSource().getEndpointId());
					cst.setSource(ep);

					final ca.inocybe.argia.stubs.reservation.EndpointType[] uclpEps = element3
							.getTarget();

					// Add target EPs

					for (final ca.inocybe.argia.stubs.reservation.EndpointType element4 : uclpEps) {
						ep = new EndpointType();
						ep.setEndpointId(element4.getEndpointId());

						cst.getTarget().add(ep);
					}

					servStatus.getConnections().add(cst);
				}

				responseType.getServiceStatus().add(servStatus);
			}
		}

		return responseType;
	}

	/**
	 * IsAvailable Handler.
	 * <p>
	 * Handler will redirect to UCLP Availability-Requests and return a
	 * Availability- Response from the UCLP Server containing a
	 * ConnectionAvailability-Object.
	 * <p>
	 * 
	 * @param element
	 *            IsAvailable Request
	 * @return Response
	 */
	@Override
	public IsAvailableResponseType isAvailable(final IsAvailableType element)
			throws EndpointNotFoundFaultException, UnexpectedFaultException,
			Exception, RemoteException {

		ReservationHandler.logger
				.debug("UCLP Adapter: initiating isAvailable request processing...");

		// Test mode
		if (this.testMode()) {
			ReservationHandler.logger.info("Running in test mode");
			return this.runTestMode(element);
		}

		IsAvailableResponse uclpResp = new IsAvailableResponse();
		final IsAvailableResponseType responseType = new IsAvailableResponseType();

		final IsAvailable avail = new IsAvailable();
		final ca.inocybe.argia.stubs.reservation.IsAvailableType availType = new ca.inocybe.argia.stubs.reservation.IsAvailableType();

		availType.setJobID(element.getJobID());

		// Get list of ServiceConstraintType (SCT)
		final List<ServiceConstraintType> l = element.getService();

		// Create array of (UCLP's) SCT
		final ca.inocybe.argia.stubs.reservation.ServiceConstraintType[] listSCT = new ca.inocybe.argia.stubs.reservation.ServiceConstraintType[l
				.size()];

		ReservationHandler.logger.debug("UCLP Adapter: " + l.size()
				+ " services to translate...");

		for (int i = 0; i < l.size(); i++) {
			final ca.inocybe.argia.stubs.reservation.ServiceConstraintType uclpSCT = new ca.inocybe.argia.stubs.reservation.ServiceConstraintType();

			// Get the SCT of the NSP's list
			final ServiceConstraintType nspSCT = l.get(i);

			uclpSCT.setAutomaticActivation(nspSCT.isAutomaticActivation());
			uclpSCT.setServiceID(nspSCT.getServiceID());

			// Translate Type of Reservation
			if (nspSCT.getTypeOfReservation().value().equalsIgnoreCase("fixed")) {
				ReservationHandler.logger
						.debug("UCLP Adapter: Fixed reservation");

				uclpSCT
						.setTypeOfReservation(ca.inocybe.argia.stubs.reservation.ReservationType.fixed);

				final FixedReservationConstraintType nspConst = nspSCT
						.getFixedReservationConstraints();

				final ca.inocybe.argia.stubs.reservation.FixedReservationConstraintType uclpConst = new ca.inocybe.argia.stubs.reservation.FixedReservationConstraintType();

				uclpConst.setDuration(nspConst.getDuration());
				uclpConst.setStartTime(nspConst.getStartTime()
						.toGregorianCalendar());

				uclpSCT.setFixedReservationConstraints(uclpConst);
			} else if (nspSCT.getTypeOfReservation().value().equalsIgnoreCase(
					"malleable")) {
				uclpSCT
						.setTypeOfReservation(ca.inocybe.argia.stubs.reservation.ReservationType.malleable);

				final MalleableReservationConstraintType nspConst = nspSCT
						.getMalleableReservationConstraints();

				final ca.inocybe.argia.stubs.reservation.MalleableReservationConstraintType uclpConst = new ca.inocybe.argia.stubs.reservation.MalleableReservationConstraintType();

				uclpConst.setDeadline(nspConst.getDeadline()
						.toGregorianCalendar());
				uclpConst.setStartTime(nspConst.getStartTime()
						.toGregorianCalendar());

				uclpSCT.setMalleableReservationConstraints(uclpConst);
			}
			// END Translate Type of Reservation

			// Translate the ConnectionConstraintTypes
			final List<ConnectionConstraintType> listNspCCT = nspSCT
					.getConnections();

			final ca.inocybe.argia.stubs.reservation.ConnectionConstraintType[] arrayUclpCCT = new ca.inocybe.argia.stubs.reservation.ConnectionConstraintType[listNspCCT
					.size()];

			ReservationHandler.logger.debug("UCLP Adapter: "
					+ arrayUclpCCT.length + " connections to translate");

			for (int j = 0; j < arrayUclpCCT.length; j++) {
				final ca.inocybe.argia.stubs.reservation.ConnectionConstraintType uclpCCT = new ca.inocybe.argia.stubs.reservation.ConnectionConstraintType();

				final ConnectionConstraintType nspCCT = listNspCCT.get(j);

				uclpCCT.setConnectionID(nspCCT.getConnectionID());
				uclpCCT.setDataAmount(nspCCT.getDataAmount());
				uclpCCT.setDirectionality(nspCCT.getDirectionality());
				uclpCCT.setMaxBW(nspCCT.getMaxBW());

				// HACK!!!!!!!!!!!
				uclpCCT.setMaxDelay(500);
				// HACK!!!!!!!!!!!!!!

				uclpCCT.setMinBW(nspCCT.getMinBW());

				// Translate EndpointType objects
				// Source Endpoint
				final EndpointType srcNspEP = nspCCT.getSource();

				final ca.inocybe.argia.stubs.reservation.EndpointType srcUclpEP = new ca.inocybe.argia.stubs.reservation.EndpointType();

				srcUclpEP.setEndpointId(srcNspEP.getEndpointId());

				uclpCCT.setSource(srcUclpEP);
				// END Source Endpoint

				// Destination Endpoints
				final List<EndpointType> destNspEPList = nspCCT.getTarget();

				final ca.inocybe.argia.stubs.reservation.EndpointType[] destUclpEPList = new ca.inocybe.argia.stubs.reservation.EndpointType[destNspEPList
						.size()];

				for (int k = 0; k < destUclpEPList.length; k++) {
					final EndpointType nspDest = destNspEPList.get(k);

					final ca.inocybe.argia.stubs.reservation.EndpointType uclpDest = new ca.inocybe.argia.stubs.reservation.EndpointType();

					uclpDest.setEndpointId(nspDest.getEndpointId());

					destUclpEPList[k] = uclpDest;
				}
				uclpCCT.setTarget(destUclpEPList);
				// END Destination Endpoints

				arrayUclpCCT[j] = uclpCCT;
			}

			uclpSCT.setConnections(arrayUclpCCT);

			listSCT[i] = uclpSCT;
		}

		availType.setService(listSCT);

		avail.setIsAvailable(availType);
		avail.setOrganization(Session.getInstance().getOrganization());

		try {

			ReservationHandler.logger.info("Sending request to UCLP...");

			uclpResp = AdvReservationsWrapper.isAvailable(avail);

			ReservationHandler.logger.info("Received response from UCLP!");

		} catch (final EndpointNotFoundFault f) {
			ReservationHandler.logger
					.error("Argia/UCLP Adapter: Endpoint source or target does not exist");
			throw new EndpointNotFoundFaultException("Cannot locate endpoint");

		} catch (final UnexpectedFault ef) {
			ReservationHandler.logger
					.error("Argia/UCLP Adapter: Unexpected fault while creating reservation");
			throw new UnexpectedFaultException(ef.getDescription()[0]
					.get_value());

		} catch (final RemoteException e) {
			ReservationHandler.logger
					.error("Argia/UCLP Adapter: Remote exception: "
							+ e.getMessage());
			throw new Exception(e.getLocalizedMessage());
		} catch (Exception e) {
			ReservationHandler.logger.error("Argia/UCLP Adapter: Exception: "
					+ e.getLocalizedMessage());
			throw new Exception(e.getLocalizedMessage());
		}

		if (null != uclpResp.getIsAvailableResponse()) {
			// Get alternative Start Time Offset
			if (null != uclpResp.getIsAvailableResponse()
					.getAlternativeStartTimeOffset()) {
				responseType.setAlternativeStartTimeOffset(uclpResp
						.getIsAvailableResponse()
						.getAlternativeStartTimeOffset());
			}
		}

		// Get Detailed Results
		final ca.inocybe.argia.stubs.reservation.ConnectionAvailabilityType[] connAvail = uclpResp
				.getIsAvailableResponse().getDetailedResult();

		for (final ca.inocybe.argia.stubs.reservation.ConnectionAvailabilityType element2 : connAvail) {
			final ConnectionAvailabilityType connAvailType = new ConnectionAvailabilityType();

			// TODO: New AvailabilityCodeType enumeration
			// connAvailType.setAvailability(connAvail[i].getAvailability());

			// PATCH!
			final ca.inocybe.argia.stubs.reservation.AvailabilityCodeType av = element2
					.getAvailability();

			connAvailType.setAvailability(AvailabilityCodeType.valueOf(av
					.getValue().toUpperCase()));

			connAvailType.setMaxBW(element2.getMaxBW());

			connAvailType.setConnectionID(element2.getConnectionID());

			connAvailType.setServiceID(element2.getServiceID());

			if (element2.getBlockedEndpoints() != null) {
				for (final String blockedEP : element2.getBlockedEndpoints()) {
					connAvailType.getBlockedEndpoints().add(blockedEP);
				}
			}

			responseType.getDetailedResult().add(connAvailType);
		}

		return responseType;
	}

	/**
	 * GetReservations Handler.
	 * <p>
	 * Handler will accept GetReservations-Requests and return a
	 * GetReservations- Response containing the List of Reservations from UCLP.
	 * <p>
	 * 
	 * @param element
	 *            getReservations Request
	 * @return getReservationsResponse from UCLP
	 */
	public GetReservationsResponseType getReservations(
			final GetReservationsType element) throws RemoteException,
			UnexpectedFaultException, Exception {

		GetReservations get = new GetReservations();
		ca.inocybe.argia.stubs.reservation.GetReservationsType grt = new ca.inocybe.argia.stubs.reservation.GetReservationsType();

		grt.setPeriodStartTime(element.getPeriodStartTime()
				.toGregorianCalendar());
		grt.setPeriodEndTime(element.getPeriodEndTime().toGregorianCalendar());

		get.setGetReservations(grt);

		GetReservationsResponseType responseType = new GetReservationsResponseType();
		GetReservationsResponse uclpResponse = new GetReservationsResponse();
		get.setOrganization(Session.getInstance().getOrganization());

		// call Argia/UCLP to get the reservations in the period
		try {
			uclpResponse = AdvReservationsWrapper.getReservations(get);

		} catch (final UnexpectedFault ef) {
			ReservationHandler.logger
					.error("Argia/UCLP Adapter: Unexpected fault while getting reservations");
			throw new UnexpectedFaultException(ef.getDescription()[0]
					.get_value());

		} catch (final RemoteException e) {
			ReservationHandler.logger
					.error("Argia/UCLP Adapter: Remote exception: "
							+ e.getMessage());
			throw new Exception(e.getMessage());
		} catch (Exception e) {
			ReservationHandler.logger.error("Argia/UCLP Adapter: Exception: "
					+ e.getLocalizedMessage());
			throw new Exception(e.getMessage());
		}

		if (null != uclpResponse.getGetReservationsResponse()) {
			if (null != uclpResponse.getGetReservationsResponse()
					.getReservation()) {
				ca.inocybe.argia.stubs.reservation.GetReservationsComplexType[] reservationInfo = uclpResponse
						.getGetReservationsResponse().getReservation();

				// loop over all the reservations
				for (ca.inocybe.argia.stubs.reservation.GetReservationsComplexType rinfo : reservationInfo) {

					// Translate each reservation
					org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationsComplexType grsct = new org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationsComplexType();

					grsct.setReservationID(WebserviceUtils
							.convertReservationID(rinfo.getID()));
					grsct.setGRI(WebserviceUtils.convertReservationID(rinfo
							.getID()));

					GetReservationResponseType grrt = new GetReservationResponseType();
					ca.inocybe.argia.stubs.reservation.GetReservationResponseType uclpGrrt = new ca.inocybe.argia.stubs.reservation.GetReservationResponseType();

					uclpGrrt = rinfo.getReservation();
					grrt.setJobID(uclpGrrt.getJobID());

					ca.inocybe.argia.stubs.reservation.ServiceConstraintType[] services = uclpGrrt
							.getService();

					// loop over all the services in the reservation
					for (ca.inocybe.argia.stubs.reservation.ServiceConstraintType service : services) {

						// translate each service
						ServiceConstraintType sct = new ServiceConstraintType();
						FixedReservationConstraintType frct = new FixedReservationConstraintType();

						sct.setServiceID(service.getServiceID());

						// malleable reservations
						if (service.getTypeOfReservation().getValue()
								.equalsIgnoreCase(
										ReservationType.MALLEABLE.value())) {
							sct.setTypeOfReservation(ReservationType.MALLEABLE);

							// fixed reservations
						} else {
							sct.setTypeOfReservation(ReservationType.FIXED);
						}

						if (service.getConnections().length == 0) {
							ReservationHandler.logger
									.debug("No connections for service");
						} else {
							sct.setAutomaticActivation(service
									.isAutomaticActivation());
							XMLGregorianCalendar startXmlCalendar = null;

							try {
								startXmlCalendar = DatatypeFactory
										.newInstance()
										.newXMLGregorianCalendar(
												service
														.getFixedReservationConstraints()
														.getStartTime().get(
																Calendar.YEAR),
												service
														.getFixedReservationConstraints()
														.getStartTime().get(
																Calendar.MONTH) + 1,
												service
														.getFixedReservationConstraints()
														.getStartTime()
														.get(
																Calendar.DAY_OF_MONTH),
												service
														.getFixedReservationConstraints()
														.getStartTime().get(
																Calendar.HOUR),
												service
														.getFixedReservationConstraints()
														.getStartTime()
														.get(Calendar.MINUTE),
												service
														.getFixedReservationConstraints()
														.getStartTime()
														.get(Calendar.SECOND),
												service
														.getFixedReservationConstraints()
														.getStartTime()
														.get(
																Calendar.MILLISECOND),
												0);
							} catch (DatatypeConfigurationException e) {
								e.printStackTrace();
							}

							if (null != startXmlCalendar)
								frct.setStartTime(startXmlCalendar);
							frct.setDuration(service
									.getFixedReservationConstraints()
									.getDuration());

							sct.setFixedReservationConstraints(frct);

							// loop over all the connections in the service
							for (ca.inocybe.argia.stubs.reservation.ConnectionType connection : service
									.getConnections()) {

								// Translate all the connections in the service
								ConnectionConstraintType cct = new ConnectionConstraintType();
								cct.setConnectionID(connection
										.getConnectionID());

								EndpointType sourceEp = new EndpointType();
								sourceEp.setEndpointId(connection.getSource()
										.getEndpointId());
								sourceEp.setBandwidth(connection.getSource()
										.getBandwidth());
								sourceEp.setDescription(connection.getSource()
										.getDescription());

								cct.setSource(sourceEp);

								EndpointType targetEp = new EndpointType();

								for (ca.inocybe.argia.stubs.reservation.EndpointType target : connection
										.getTarget()) {
									targetEp.setEndpointId(target
											.getEndpointId());
									targetEp.setDescription(target
											.getDescription());
									targetEp
											.setBandwidth(target.getBandwidth());

									cct.getTarget().add(targetEp);
								}

								cct.setDirectionality(connection
										.getDirectionality());

								sct.getConnections().add(cct);
							}

						}
						grrt.getService().add(sct);
					}
					grsct.setReservation(grrt);
					responseType.getReservation().add(grsct);

				}

				return responseType;
			}
		} else {
			ReservationHandler.logger
					.info("No reservations found in the given period time");
		}
		return responseType;
	}

	/*
	 * test mode responses
	 * =====================================================================
	 */
	/**
	 * returns a testmode-response.
	 * 
	 * @param request
	 *            CreateReservationType
	 * @return CreateReservationResponseType
	 */
	protected CreateReservationResponseType runTestMode(
			final CreateReservationType request) {
		final CreateReservationResponseType testmodeResponseType = ReservationHandler.objectFactory
				.createCreateReservationResponseType();
		testmodeResponseType.setReservationID("21");
		return testmodeResponseType;
	}

	/**
	 * returns a testmode-response.
	 * 
	 * @param request
	 *            CancelReservationType
	 * @return CancelReservationResponseType
	 */
	protected CancelReservationResponseType runTestMode(
			final CancelReservationType request) {
		final CancelReservationResponseType testmodeResponseType = ReservationHandler.objectFactory
				.createCancelReservationResponseType();
		// there's only one interesting field, the result
		testmodeResponseType.setSuccess(false);

		// return response-Element
		return testmodeResponseType;
	}

	/**
	 * returns a testmode-response.
	 * 
	 * @param request
	 *            IsAvailableType
	 * @return IsAvailableResponseType
	 */
	protected IsAvailableResponseType runTestMode(final IsAvailableType request) {
		final IsAvailableResponseType testmodeResponseType = ReservationHandler.objectFactory
				.createIsAvailableResponseType();
		// mockResponseType.setAlternativeStartTimeOffset(1337L);

		// set each connection to status available
		for (final ServiceConstraintType serCons : request.getService()) {
			for (final ConnectionConstraintType conCons : serCons
					.getConnections()) {
				// DetailedResult det = new DetailedResult();
				final ConnectionAvailabilityType cAvail = ReservationHandler.objectFactory
						.createConnectionAvailabilityType();
				cAvail.setAvailability(AvailabilityCodeType.AVAILABLE);
				cAvail.setConnectionID(conCons.getConnectionID());
				cAvail.setServiceID(serCons.getServiceID());
				testmodeResponseType.getDetailedResult().add(cAvail);
			}
		}

		// return response-Element
		return testmodeResponseType;
	}

	/**
	 * returns a mock-response.
	 * 
	 * @param request
	 *            ActivateType
	 * @return ActivateResponseType
	 */
	protected ActivateResponseType runTestMode(final ActivateType request) {
		final ActivateResponseType testmodeResponseType = ReservationHandler.objectFactory
				.createActivateResponseType();
		// there's only one interesting field, the result
		testmodeResponseType.setSuccess(false);

		// return response-Element
		return testmodeResponseType;
	}

	/**
	 * returns a mock-response.
	 * 
	 * @param request
	 *            GetStatusType
	 * @return GetStatusResponseType
	 */
	protected GetStatusResponseType runTestMode(final GetStatusType request) {
		final GetStatusResponseType testmodeResponseType = ReservationHandler.objectFactory
				.createGetStatusResponseType();
		// getStatusType.getReservationID();
		for (final Integer id : request.getServiceID()) {
			final ServiceStatus serStat = new ServiceStatus();
			serStat.setServiceID(id.intValue());
			serStat.setStatus(StatusType.UNKNOWN);
			final ConnectionStatusType conStatus = ReservationHandler.objectFactory
					.createConnectionStatusType();
			conStatus.setActualBW(0);
			conStatus.setConnectionID(123);
			conStatus.setDirectionality(1);

			final EndpointType c1 = new EndpointType();
			c1.setEndpointId("127.0.0.1");
			c1.setName("TMCE1");
			c1.setDescription("TestMode Connection Endpoint 1");
			c1.setDomainId("TestDomain1");
			c1.setBandwidth(Integer.valueOf(1));
			c1.setInterface(EndpointInterfaceType.USER);
			conStatus.setSource(c1);
			final EndpointType c2 = new EndpointType();
			c2.setEndpointId("10.0.0.1");
			c2.setName("TMCE2");
			c2.setDescription("TestMode Connection Endpoint 2");
			c2.setDomainId("TestDomain2");
			c2.setBandwidth(Integer.valueOf(1));
			c2.setInterface(EndpointInterfaceType.USER);
			conStatus.getTarget().add(c2);

			conStatus.setStatus(StatusType.UNKNOWN);
			serStat.getConnections().add(conStatus);

			final DomainStatusType domStatus = ReservationHandler.objectFactory
					.createDomainStatusType();
			domStatus.setDomain("i2cat-testmode");
			domStatus.setStatus(StatusType.UNKNOWN);
			serStat.getDomainStatus().add(domStatus);

			// serStat.setStatus(4);
			testmodeResponseType.getServiceStatus().add(serStat);
		}
		return testmodeResponseType;
	}

}
