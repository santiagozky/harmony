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


package org.opennaas.extensions.idb.da.argia.implementation;

/*import java.net.MalformedURLException;
 import java.net.URL;
 import java.rmi.RemoteException;

 import javax.xml.rpc.ServiceException;

 import org.apache.axis.client.Stub;
 import org.globus.axis.util.Util;

 import ca.inocybe.argia.stubs.reservation.Activate;
 import ca.inocybe.argia.stubs.reservation.ActivateResponse;
 import ca.inocybe.argia.stubs.reservation.CancelJob;
 import ca.inocybe.argia.stubs.reservation.CancelJobResponse;
 import ca.inocybe.argia.stubs.reservation.CancelReservation;
 import ca.inocybe.argia.stubs.reservation.CancelReservationResponse;
 import ca.inocybe.argia.stubs.reservation.CompleteJob;
 import ca.inocybe.argia.stubs.reservation.CompleteJobResponse;
 import ca.inocybe.argia.stubs.reservation.CreateReservation;
 import ca.inocybe.argia.stubs.reservation.CreateReservationResponse;
 import ca.inocybe.argia.stubs.reservation.DeleteAllJobs;
 import ca.inocybe.argia.stubs.reservation.DeleteAllJobsResponse;
 import ca.inocybe.argia.stubs.reservation.DeleteAllReservations;
 import ca.inocybe.argia.stubs.reservation.DeleteAllReservationsResponse;
 import ca.inocybe.argia.stubs.reservation.DeleteJob;
 import ca.inocybe.argia.stubs.reservation.DeleteJobResponse;
 import ca.inocybe.argia.stubs.reservation.DeleteJobsRequested;
 import ca.inocybe.argia.stubs.reservation.DeleteJobsRequestedResponse;
 import ca.inocybe.argia.stubs.reservation.DeleteReservation;
 import ca.inocybe.argia.stubs.reservation.DeleteReservationResponse;
 import ca.inocybe.argia.stubs.reservation.DeleteReservationsRequested;
 import ca.inocybe.argia.stubs.reservation.DeleteReservationsRequestedResponse;
 import ca.inocybe.argia.stubs.reservation.GetEndpoints;
 import ca.inocybe.argia.stubs.reservation.GetEndpointsResponse;
 import ca.inocybe.argia.stubs.reservation.GetFeatures;
 import ca.inocybe.argia.stubs.reservation.GetFeaturesResponse;
 import ca.inocybe.argia.stubs.reservation.GetJobs;
 import ca.inocybe.argia.stubs.reservation.GetJobsResponse;
 import ca.inocybe.argia.stubs.reservation.GetReservations;
 import ca.inocybe.argia.stubs.reservation.GetReservationsResponse;
 import ca.inocybe.argia.stubs.reservation.GetStatus;
 import ca.inocybe.argia.stubs.reservation.GetStatusResponse;
 import ca.inocybe.argia.stubs.reservation.IsAvailable;
 import ca.inocybe.argia.stubs.reservation.IsAvailableResponse;
 import ca.inocybe.argia.stubs.reservation.ReservationPortType;
 import ca.inocybe.argia.stubs.reservation.service.ReservationServiceAddressingLocator;

 *//**
 * This class calls the web service.
 * 
 * @author Laia
 */
/*
public class AdvReservationsWrapper {
    static {
        Util.registerTransport();
    }

 *//**
 * This operation is invoked by the upper layers when the reservation start
 * time arrives. It only needs the AAA information, the ReservationID and
 * the ServiceID of the service that the user wants to activate to execute
 * the operation.
 * 
 * @param activationReq
 * @param host
 *            Of the service requested
 * @param port
 *            Of the service requested
 * @return
 * @throws RemoteException
 */
/*
    public static ActivateResponse activate(final Activate activationReq)
            throws RemoteException {
        ReservationPortType portType = null;

        try {
            portType = AdvReservationsWrapper.createPort();
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return portType.activate(activationReq);
    }

 *//**
 * Several independent reservations can be grouped to a single job with a
 * common job ID. While the job is not yet completed, these reservations are
 * only loosely blocked as �pre-reservations�. This method allows the user
 * to cancel all these �pre-reservations� with a single request in case the
 * job cannot be completed.
 * 
 * @param cancelJobReq
 * @param host
 *            Of the service requested
 * @param port
 *            Of the service requested
 * @return
 * @throws RemoteException
 */
/*
    public static CancelJobResponse cancelJob(final CancelJob cancelJobReq)
            throws RemoteException {
        ReservationPortType portType = null;

        try {
            portType = AdvReservationsWrapper.createPort();
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return portType.cancelJob(cancelJobReq);
    }

 *//**
 * This method allows canceling a reservation done by the user giving its
 * identifier.
 * 
 * @param cancelReservationReq
 * @param host
 *            Of the service requested
 * @param port
 *            Of the service requested
 * @return
 * @throws RemoteException
 */
/*
    public static CancelReservationResponse cancelReservation(
            final CancelReservation cancelReservationReq)
            throws RemoteException {
        ReservationPortType portType = null;

        try {
            portType = AdvReservationsWrapper.createPort();
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return portType.cancelReservation(cancelReservationReq);
    }

 *//**
 * Several independent reservations can be grouped to a single job with a
 * common job ID. While the job is not yet completed, these reservations are
 * only loosely blocked as �pre-reservations�. This method allows the user
 * to complete or finish a job; this way all the pre-reservations belonging
 * to this job can be modified to regular reservations.
 * 
 * @param completeJobReq
 * @param host
 *            Of the service requested
 * @param port
 *            Of the service requested
 * @return
 * @throws RemoteException
 */
/*
    public static CompleteJobResponse completeJob(
            final CompleteJob completeJobReq) throws RemoteException {
        ReservationPortType portType = null;

        try {
            portType = AdvReservationsWrapper.createPort();
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return portType.completeJob(completeJobReq);
    }

 *//**
 * Given the host and port of a service, this method creates the connection
 * to this service by making the URL.
 * 
 * @param host
 *            Of the service requested
 * @param port
 *            Of the service requested
 * @return
 * @throws MalformedURLException
 * @throws ServiceException
 */
/*
    private static ReservationPortType createPort()
            throws MalformedURLException, ServiceException {
        final Session ses = Session.getInstance();

        final String host = ses.getHost();

        // !!!
        final String url = "https://" + host + ":8443"
                + "/wsrf/services/argia/ReservationService";

        ReservationPortType portType = null;

        final ReservationServiceAddressingLocator service = new ReservationServiceAddressingLocator();
        portType = service.getReservationPortTypePort(new URL(url));

        AdvReservationsWrapper.setSecurity((Stub) portType);
        return portType;
    }

    
 * private static void setSecurity (Stub stub) { Session ses =
 * Session.getInstance();
 * 
 * stub._setProperty(Constants.GSI_SEC_CONV,Constants.ENCRYPTION);
 * stub._setProperty(GSIConstants.GSI_CREDENTIALS, ses.getCredential());
 * stub._setProperty(Constants.AUTHORIZATION,
 * NoAuthorization.getInstance()); }
     

 *//**
 * This method is used to actually reserve resources. A pre-reservation can
 * be used by a Middleware entity to block resources for a certain time
 * while performing several operations to reserve Grid and network
 * resources. In case of a failure, resources already blocked do not have to
 * be cancelled manually but are freed after a certain timeout.
 * 
 * It should be noted that the blocking of pre-reserved services might not
 * be definite. While blocked resources surely will not be available in
 * forthcoming requests from the same user, the system may choose to make
 * these resources available to other users, e.g. if they have a higher
 * priority level.
 * 
 * @param reservationReq
 * @param host
 *            Of the service requested
 * @param port
 *            Of the service requested
 * @return
 * @throws RemoteException
 */
/*
    public static CreateReservationResponse createReservation(
            final CreateReservation reservationReq) throws RemoteException {
        ReservationPortType portType = null;

        try {
            portType = AdvReservationsWrapper.createPort();
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return portType.createReservation(reservationReq);
    }

 *//**
 * This method allows deleting all the jobs done by the user.
 * 
 * @param cancelReservationReq
 * @param host
 *            Of the service requested
 * @param port
 *            Of the service requested
 * @return
 * @throws RemoteException
 */
/*
    public static DeleteAllJobsResponse deleteAllJobs(
            final DeleteAllJobs deleteAllJobsReq) throws RemoteException {
        ReservationPortType portType = null;

        try {
            portType = AdvReservationsWrapper.createPort();
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return portType.deleteAllJobs(deleteAllJobsReq);
    }

 *//**
 * This method allows deleting all the reservations done by the user.
 * 
 * @param cancelReservationReq
 * @param host
 *            Of the service requested
 * @param port
 *            Of the service requested
 * @return
 * @throws RemoteException
 */
/*
    public static DeleteAllReservationsResponse deleteAllReservations(
            final DeleteAllReservations deleteAllReservationsReq)
            throws RemoteException {
        ReservationPortType portType = null;

        try {
            portType = AdvReservationsWrapper.createPort();
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return portType.deleteAllReservations(deleteAllReservationsReq);
    }

 *//**
 * This method allows deleting a Job done by the user giving its identifier.
 * 
 * @param cancelJobReq
 * @param host
 *            Of the service requested
 * @param port
 *            Of the service requested
 * @return
 * @throws RemoteException
 */
/*
    public static DeleteJobResponse deleteJob(final DeleteJob deleteJobReq)
            throws RemoteException {
        ReservationPortType portType = null;

        try {
            portType = AdvReservationsWrapper.createPort();
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return portType.deleteJob(deleteJobReq);
    }

 *//**
 * This method allows deleting the jobs requested done by the user giving
 * its identifiers.
 * 
 * @param cancelJobReq
 * @param host
 *            Of the service requested
 * @param port
 *            Of the service requested
 * @return
 * @throws RemoteException
 */
/*
    public static DeleteJobsRequestedResponse deleteJobsRequested(
            final DeleteJobsRequested deleteJobsRequestedReq)
            throws RemoteException {
        ReservationPortType portType = null;

        try {
            portType = AdvReservationsWrapper.createPort();
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return portType.deleteJobsRequested(deleteJobsRequestedReq);
    }

 *//**
 * This method allows deleting a reservation done by the user giving its
 * identifier.
 * 
 * @param cancelReservationReq
 * @param host
 *            Of the service requested
 * @param port
 *            Of the service requested
 * @return
 * @throws RemoteException
 */
/*
    public static DeleteReservationResponse deleteReservation(
            final DeleteReservation deleteReservationReq)
            throws RemoteException {
        ReservationPortType portType = null;

        try {
            portType = AdvReservationsWrapper.createPort();
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return portType.deleteReservation(deleteReservationReq);
    }

 *//**
 * This method allows deleting the reservations requested done by the user
 * giving its identifiers.
 * 
 * @param cancelReservationReq
 * @param host
 *            Of the service requested
 * @param port
 *            Of the service requested
 * @return
 * @throws RemoteException
 */
/*
    public static DeleteReservationsRequestedResponse deleteReservationsRequested(
            final DeleteReservationsRequested deleteReservationsRequestedReq)
            throws RemoteException {
        ReservationPortType portType = null;

        try {
            portType = AdvReservationsWrapper.createPort();
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return portType
                .deleteReservationsRequested(deleteReservationsRequestedReq);
    }

 *//**
 * This method allows retrieving the available endpoints of the network that
 * are useful for the user to create connections.
 * 
 * @param endpointsReq
 * @param host
 *            Of the service requested
 * @param port
 *            Of the service requested
 * @return
 * @throws RemoteException
 */
/*
    public static GetEndpointsResponse getEndpoints(
            final GetEndpoints endpointsReq) throws RemoteException {
        ReservationPortType portType = null;

        try {
            portType = AdvReservationsWrapper.createPort();
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return portType.getEndpoints(endpointsReq);
    }

 *//**
 * This method provides information about the supported features of the
 * system.
 * 
 * @param featuresReq
 * @param host
 *            Of the service requested
 * @param port
 *            Of the service requested
 * @return
 * @throws RemoteException
 */
/*
    public static GetFeaturesResponse getFeatures(final GetFeatures featuresReq)
            throws RemoteException {
        ReservationPortType portType = null;

        try {
            portType = AdvReservationsWrapper.createPort();
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return portType.getFeatures(featuresReq);
    }

 *//**
 * This method provides information about all the jobs done by the user.
 * 
 * @param g
 * @param host
 *            Of the service requested
 * @param port
 *            Of the service requested
 * @return
 * @throws RemoteException
 */
/*
    public static GetJobsResponse getJobs(final GetJobs g)
            throws RemoteException {
        ReservationPortType portType = null;

        try {
            portType = AdvReservationsWrapper.createPort();
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return portType.getJobs(g);
    }

 *//**
 * This method provides information about all the reservations done by the
 * user.
 * 
 * @param g
 * @param host
 *            Of the service requested
 * @param port
 *            Of the service requested
 * @return
 * @throws RemoteException
 */
/*
    public static GetReservationsResponse getReservations(
            final GetReservations g) throws RemoteException {
        ReservationPortType portType = null;

        try {
            portType = AdvReservationsWrapper.createPort();
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return portType.getReservations(g);
    }

 *//**
 * This method is used to query the status of all services contained in a
 * reservation or a specific subset of these services.
 * 
 * @param statusReq
 * @param host
 *            Of the service requested
 * @param port
 *            Of the service requested
 * @return
 * @throws RemoteException
 */
/*
    public static GetStatusResponse getStatus(final GetStatus statusReq)
            throws RemoteException {
        ReservationPortType portType = null;

        try {
            portType = AdvReservationsWrapper.createPort();
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return portType.getStatus(statusReq);
    }

 *//**
 * This method allows binding the access ports to the reserved network
 * resources later. This is due to the fact that in many cases the final
 * user port is known only a short time before the reservation has to be
 * started.
 * 
 * @param bindReq
 * @param host
 *            Of the service requested
 * @param port
 *            Of the service requested
 * @return
 * @throws RemoteException
 */
/*
    
 * public static BindResponse bind(Bind bindReq) throws RemoteException {
 * ReservationPortType portType = null;
 * 
 * try { portType = createPort(); } catch (Exception e) {
 * e.printStackTrace(); }
 * 
 * return portType.bind(bindReq); }
     

 *//**
 * This method is used to check for availability of resources while planning
 * complex workflows. This request type does not lead to the blocking of
 * resources. This method allows doing advanced queries in order to avoid
 * repetitive reservation/cancel request sequences as best fit across the
 * network is determined. The answer for this query contains information
 * about the network resources� availability (earliest available starting
 * time, etc.). A user can execute this action before trying to make a
 * reservation request in order to know which the available resources are.
 * Then, depending on the answer, the user can request the reservation using
 * the available resources.
 * 
 * @param availabilityReq
 * @param host
 *            Of the service requested
 * @param port
 *            Of the service requested
 * @return
 * @throws RemoteException
 */
/*
    public static IsAvailableResponse isAvailable(
            final IsAvailable availabilityReq) throws RemoteException {
        ReservationPortType portType = null;

        try {
            portType = AdvReservationsWrapper.createPort();
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return portType.isAvailable(availabilityReq);
    }

    private static void setSecurity(final Stub stub) {
        // Session ses = Session.getInstance();
        stub._setProperty(org.globus.wsrf.security.Constants.GSI_ANONYMOUS,
                Boolean.TRUE);
        // stub._setProperty(Constants.GSI_SEC_CONV,Constants.ENCRYPTION);
        // stub._setProperty(Constants.GSI_ANONYMOUS, Boolean.TRUE);
        // stub._setProperty(Constants.AUTHORIZATION,
        // NoAuthorization.getInstance());
    }
}
 */
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Stub;
import org.globus.axis.util.Util;
import org.globus.wsrf.impl.security.authentication.Constants;

import ca.inocybe.argia.stubs.reservation.GetEndpoints;
import ca.inocybe.argia.stubs.reservation.GetEndpointsResponse;
import ca.inocybe.argia.stubs.reservation.ReservationPortType;
import ca.inocybe.argia.stubs.reservation.service.ReservationServiceAddressingLocator;
import ca.inocybe.argia.stubs.reservation.Activate;
import ca.inocybe.argia.stubs.reservation.ActivateResponse;
import ca.inocybe.argia.stubs.reservation.CancelJob;
import ca.inocybe.argia.stubs.reservation.CancelJobResponse;
import ca.inocybe.argia.stubs.reservation.CancelReservation;
import ca.inocybe.argia.stubs.reservation.CancelReservationResponse;
import ca.inocybe.argia.stubs.reservation.CompleteJob;
import ca.inocybe.argia.stubs.reservation.CompleteJobResponse;
import ca.inocybe.argia.stubs.reservation.CreateReservation;
import ca.inocybe.argia.stubs.reservation.CreateReservationResponse;
import ca.inocybe.argia.stubs.reservation.DeleteAllJobs;
import ca.inocybe.argia.stubs.reservation.DeleteAllJobsResponse;
import ca.inocybe.argia.stubs.reservation.DeleteAllReservations;
import ca.inocybe.argia.stubs.reservation.DeleteAllReservationsResponse;
import ca.inocybe.argia.stubs.reservation.DeleteJob;
import ca.inocybe.argia.stubs.reservation.DeleteJobResponse;
import ca.inocybe.argia.stubs.reservation.DeleteJobsRequested;
import ca.inocybe.argia.stubs.reservation.DeleteJobsRequestedResponse;
import ca.inocybe.argia.stubs.reservation.DeleteReservation;
import ca.inocybe.argia.stubs.reservation.DeleteReservationResponse;
import ca.inocybe.argia.stubs.reservation.DeleteReservationsRequested;
import ca.inocybe.argia.stubs.reservation.DeleteReservationsRequestedResponse;
import ca.inocybe.argia.stubs.reservation.EndpointNotFoundFault;
import ca.inocybe.argia.stubs.reservation.GetDomainTechnologyInformation;
import ca.inocybe.argia.stubs.reservation.GetDomainTechnologyInformationResponse;
import ca.inocybe.argia.stubs.reservation.GetFeatures;
import ca.inocybe.argia.stubs.reservation.GetFeaturesResponse;
import ca.inocybe.argia.stubs.reservation.GetFullReservations;
import ca.inocybe.argia.stubs.reservation.GetFullReservationsResponse;
import ca.inocybe.argia.stubs.reservation.GetJobs;
import ca.inocybe.argia.stubs.reservation.GetJobsResponse;
import ca.inocybe.argia.stubs.reservation.GetReservations;
import ca.inocybe.argia.stubs.reservation.GetReservationsResponse;
import ca.inocybe.argia.stubs.reservation.GetStatus;
import ca.inocybe.argia.stubs.reservation.GetStatusResponse;
import ca.inocybe.argia.stubs.reservation.InvalidRequestFault;
import ca.inocybe.argia.stubs.reservation.InvalidServiceIDFault;
import ca.inocybe.argia.stubs.reservation.IsAvailable;
import ca.inocybe.argia.stubs.reservation.IsAvailableResponse;
import ca.inocybe.argia.stubs.reservation.ReloadNet;
import ca.inocybe.argia.stubs.reservation.ReloadNetResponse;
import ca.inocybe.argia.stubs.reservation.UnexpectedFault;

/**
 * This class calls the web service.
 * 
 * @author Laia
 */
public class AdvReservationsWrapper {
	static {
		Util.registerTransport();
	}

	/**
	 * This method allows retrieving the available endpoints of the network that
	 * are useful for the user to create connections.
	 * 
	 * @param endpointsReq
	 * @param host
	 *            Of the service requested
	 * @param port
	 *            Of the service requested
	 * @return
	 * @throws RemoteException
	 *             , InvalidRequestFault, EndpointNotFoundFault,
	 *             UnexpectedFault, InvalidServiceIDFault
	 * @throws NetworkReservationException
	 * @throws RemoteException
	 */
	public static GetEndpointsResponse getEndpoints(GetEndpoints endpointsReq)
			throws RemoteException, InvalidRequestFault, EndpointNotFoundFault,
			UnexpectedFault, InvalidServiceIDFault {
		ReservationPortType portType = null;

		try {
			portType = createPort();
		} catch (Exception e) {
			e.printStackTrace();
		}

		//endpointsReq.setOrganization(Session.getInstance().getOrganization());
		return portType.getEndpoints(endpointsReq);
	}

	/**
	 * This method allows retrieving the available endpoints of the network that
	 * are useful for the user to create connections.
	 * 
	 * @param endpointsReq
	 * @param host
	 *            Of the service requested
	 * @param port
	 *            Of the service requested
	 * @return
	 * @throws RemoteException
	 */
	/*
	 * public static GetEndpointsLocationResponse
	 * getEndpointsLocation(GetEndpointsLocation endpointsReq) throws
	 * NetworkReservationException, RemoteException, InvalidRequestFault,
	 * EndpointNotFoundFault, UnexpectedFault, InvalidServiceIDFault {
	 * ReservationPortType portType = null;
	 * 
	 * try { portType = createPort(); } catch (Exception e) {
	 * e.printStackTrace(); }
	 * //endpointsReq.setOrganization(Session.getInstance().getOrganization());
	 * return portType.getEndpointsLocation(endpointsReq); }
	 */

	/**
	 * Reloads the Network
	 * 
	 * @param
	 * @param host
	 *            Of the service requested
	 * @param port
	 *            Of the service requested
	 * @return
	 * @throws RemoteException
	 */
	public static ReloadNetResponse reloadNet(ReloadNet rnReq)
			throws RemoteException, InvalidRequestFault, EndpointNotFoundFault,
			UnexpectedFault, InvalidServiceIDFault {
		ReservationPortType portType = null;

		try {
			portType = createPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// rnReq.setOrganization(Session.getInstance().getOrganization());
		return portType.reloadNet(rnReq);
	}

	/**
	 * Gets the domain technology information
	 * 
	 * @param g
	 * @return
	 * @throws RemoteException
	 * @throws InvalidRequestFault
	 * @throws UnexpectedFault
	 */
	public static GetDomainTechnologyInformationResponse getDomainTechnologyInformation(
			GetDomainTechnologyInformation technologyRequest) throws RemoteException,
			InvalidRequestFault, UnexpectedFault {
		ReservationPortType portType = null;

		try {
			portType = createPort();
		} catch (Exception e) {
			e.printStackTrace();
		}

		technologyRequest.setOrganization(Session.getInstance().getOrganization());
		return portType.getDomainTechnologyInformation(technologyRequest);
	}

	/**
	 * This method provides information about the supported features of the
	 * system.
	 * 
	 * @param featuresReq
	 * @param host
	 *            Of the service requested
	 * @param port
	 *            Of the service requested
	 * @return
	 * @throws RemoteException
	 */
	public static GetFeaturesResponse getFeatures(GetFeatures featuresReq)
			throws RemoteException, InvalidRequestFault, EndpointNotFoundFault,
			UnexpectedFault, InvalidServiceIDFault {
		ReservationPortType portType = null;

		try {
			portType = createPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// featuresReq.setOrganization(Session.getInstance().getOrganization());
		return portType.getFeatures(featuresReq);
	}

	/**
	 * This method provides information about all the reservations done by the
	 * user.
	 * 
	 * @param g
	 * @param host
	 *            Of the service requested
	 * @param port
	 *            Of the service requested
	 * @return
	 * @throws RemoteException
	 */
	public static GetReservationsResponse getReservations(GetReservations g)
			throws RemoteException, InvalidRequestFault, EndpointNotFoundFault,
			UnexpectedFault, InvalidServiceIDFault {
		ReservationPortType portType = null;

		try {
			portType = createPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// g.setOrganization(Session.getInstance().getOrganization());
		return portType.getReservations(g);
	}

	/**
	 * This method provides information about all the reservations done by the
	 * user.
	 * 
	 * @param g
	 * @param host
	 *            Of the service requested
	 * @param port
	 *            Of the service requested
	 * @return
	 * @throws RemoteException
	 */
	public static GetFullReservationsResponse getFullReservations(
			GetFullReservations g) throws RemoteException, InvalidRequestFault,
			EndpointNotFoundFault, UnexpectedFault, InvalidServiceIDFault {
		ReservationPortType portType = null;

		try {
			portType = createPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// g.setOrganization(Session.getInstance().getOrganization());
		return portType.getFullReservations(g);
	}

	/**
	 * This method provides information about all the jobs done by the user.
	 * 
	 * @param g
	 * @param host
	 *            Of the service requested
	 * @param port
	 *            Of the service requested
	 * @return
	 * @throws RemoteException
	 */
	public static GetJobsResponse getJobs(GetJobs g) throws RemoteException,
			InvalidRequestFault, EndpointNotFoundFault, UnexpectedFault,
			InvalidServiceIDFault {
		ReservationPortType portType = null;

		try {
			portType = createPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// g.setOrganization(Session.getInstance().getOrganization());
		return portType.getJobs(g);
	}

	/**
	 * This method is used to check for availability of resources while planning
	 * complex workflows. This request type does not lead to the blocking of
	 * resources. This method allows doing advanced queries in order to avoid
	 * repetitive reservation/cancel request sequences as best fit across the
	 * network is determined. The answer for this query contains information
	 * about the network resources availability (earliest available starting
	 * time, etc.). A user can execute this action before trying to make a
	 * reservation request in order to know which the available resources are.
	 * Then, depending on the answer, the user can request the reservation using
	 * the available resources.
	 * 
	 * @param availabilityReq
	 * @param host
	 *            Of the service requested
	 * @param port
	 *            Of the service requested
	 * @return
	 * @throws RemoteException
	 */
	public static IsAvailableResponse isAvailable(IsAvailable availabilityReq)
			throws RemoteException, InvalidRequestFault, EndpointNotFoundFault,
			UnexpectedFault, InvalidServiceIDFault {
		ReservationPortType portType = null;

		try {
			portType = createPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//availabilityReq.setOrganization(Session.getInstance().getOrganization(
		// ));
		return portType.isAvailable(availabilityReq);
	}

	/**
	 * This method is used to actually reserve resources. A pre-reservation can
	 * be used by a Middleware entity to block resources for a certain time
	 * while performing several operations to reserve Grid and network
	 * resources. In case of a failure, resources already blocked do not have to
	 * be cancelled manually but are freed after a certain timeout.
	 * 
	 * It should be noted that the blocking of pre-reserved services might not
	 * be definite. While blocked resources surely will not be available in
	 * forthcoming requests from the same user, the system may choose to make
	 * these resources available to other users, e.g. if they have a higher
	 * priority level.
	 * 
	 * @param reservationReq
	 * @param host
	 *            Of the service requested
	 * @param port
	 *            Of the service requested
	 * @return
	 * @throws RemoteException
	 */
	public static CreateReservationResponse createReservation(
			CreateReservation reservationReq) throws RemoteException,
			InvalidRequestFault, EndpointNotFoundFault, UnexpectedFault,
			InvalidServiceIDFault {
		ReservationPortType portType = null;

		try {
			portType = createPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//reservationReq.setOrganization(Session.getInstance().getOrganization()
		// );
		return portType.createReservation(reservationReq);
	}

	/**
	 * This method is used to query the status of all services contained in a
	 * reservation or a specific subset of these services.
	 * 
	 * @param statusReq
	 * @param host
	 *            Of the service requested
	 * @param port
	 *            Of the service requested
	 * @return
	 * @throws RemoteException
	 */
	public static GetStatusResponse getStatus(GetStatus statusReq)
			throws RemoteException, InvalidRequestFault, EndpointNotFoundFault,
			UnexpectedFault, InvalidServiceIDFault {
		ReservationPortType portType = null;

		try {
			portType = createPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// statusReq.setOrganization(Session.getInstance().getOrganization());
		return portType.getStatus(statusReq);
	}

	/**
	 * This method allows canceling a reservation done by the user giving its
	 * identifier.
	 * 
	 * @param cancelReservationReq
	 * @param host
	 *            Of the service requested
	 * @param port
	 *            Of the service requested
	 * @return
	 * @throws RemoteException
	 */
	public static CancelReservationResponse cancelReservation(
			CancelReservation cancelReservationReq) throws RemoteException,
			InvalidRequestFault, EndpointNotFoundFault, UnexpectedFault,
			InvalidServiceIDFault {
		ReservationPortType portType = null;

		try {
			portType = createPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// cancelReservationReq.setOrganization(Session.getInstance().
		// getOrganization());
		return portType.cancelReservation(cancelReservationReq);
	}

	/**
	 * This method allows deleting a reservation done by the user giving its
	 * identifier.
	 * 
	 * @param cancelReservationReq
	 * @param host
	 *            Of the service requested
	 * @param port
	 *            Of the service requested
	 * @return
	 * @throws RemoteException
	 */
	public static DeleteReservationResponse deleteReservation(
			DeleteReservation deleteReservationReq) throws RemoteException,
			InvalidRequestFault, EndpointNotFoundFault, UnexpectedFault,
			InvalidServiceIDFault {
		ReservationPortType portType = null;

		try {
			portType = createPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// deleteReservationReq.setOrganization(Session.getInstance().
		// getOrganization());
		return portType.deleteReservation(deleteReservationReq);
	}

	/**
	 * This method allows deleting the reservations requested done by the user
	 * giving its identifiers.
	 * 
	 * @param cancelReservationReq
	 * @param host
	 *            Of the service requested
	 * @param port
	 *            Of the service requested
	 * @return
	 * @throws RemoteException
	 */
	public static DeleteReservationsRequestedResponse deleteReservationsRequested(
			DeleteReservationsRequested deleteReservationsRequestedReq)
			throws RemoteException, InvalidRequestFault, EndpointNotFoundFault,
			UnexpectedFault, InvalidServiceIDFault {
		ReservationPortType portType = null;

		try {
			portType = createPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// deleteReservationsRequestedReq.setOrganization(Session.getInstance().
		// getOrganization());
		return portType
				.deleteReservationsRequested(deleteReservationsRequestedReq);
	}

	/**
	 * This method allows deleting all the reservations done by the user.
	 * 
	 * @param cancelReservationReq
	 * @param host
	 *            Of the service requested
	 * @param port
	 *            Of the service requested
	 * @return
	 * @throws RemoteException
	 */
	public static DeleteAllReservationsResponse deleteAllReservations(
			DeleteAllReservations deleteAllReservationsReq)
			throws RemoteException, InvalidRequestFault, EndpointNotFoundFault,
			UnexpectedFault, InvalidServiceIDFault {
		ReservationPortType portType = null;

		try {
			portType = createPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// deleteAllReservationsReq.setOrganization(Session.getInstance().
		// getOrganization());
		return portType.deleteAllReservations(deleteAllReservationsReq);
	}

	/**
	 * Several independent reservations can be grouped to a single job with a
	 * common job ID. While the job is not yet completed, these reservations are
	 * only loosely blocked as pre-reservations. This method allows the user
	 * to complete or finish a job; this way all the pre-reservations belonging
	 * to this job can be modified to regular reservations.
	 * 
	 * @param completeJobReq
	 * @param host
	 *            Of the service requested
	 * @param port
	 *            Of the service requested
	 * @return
	 * @throws RemoteException
	 */
	public static CompleteJobResponse completeJob(CompleteJob completeJobReq)
			throws RemoteException, InvalidRequestFault, EndpointNotFoundFault,
			UnexpectedFault, InvalidServiceIDFault {
		ReservationPortType portType = null;

		try {
			portType = createPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//completeJobReq.setOrganization(Session.getInstance().getOrganization()
		// );
		return portType.completeJob(completeJobReq);
	}

	/**
	 * Several independent reservations can be grouped to a single job with a
	 * common job ID. While the job is not yet completed, these reservations are
	 * only loosely blocked as pre-reservations. This method allows the user
	 * to cancel all these pre-reservations with a single request in case the
	 * job cannot be completed.
	 * 
	 * @param cancelJobReq
	 * @param host
	 *            Of the service requested
	 * @param port
	 *            Of the service requested
	 * @return
	 * @throws RemoteException
	 */
	public static CancelJobResponse cancelJob(CancelJob cancelJobReq)
			throws RemoteException, InvalidRequestFault, EndpointNotFoundFault,
			UnexpectedFault, InvalidServiceIDFault {
		ReservationPortType portType = null;

		try {
			portType = createPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//cancelJobReq.setOrganization(Session.getInstance().getOrganization());
		return portType.cancelJob(cancelJobReq);
	}

	/**
	 * This method allows deleting a Job done by the user giving its identifier.
	 * 
	 * @param cancelJobReq
	 * @param host
	 *            Of the service requested
	 * @param port
	 *            Of the service requested
	 * @return
	 * @throws RemoteException
	 */
	public static DeleteJobResponse deleteJob(DeleteJob deleteJobReq)
			throws RemoteException, InvalidRequestFault, EndpointNotFoundFault,
			UnexpectedFault, InvalidServiceIDFault {
		ReservationPortType portType = null;

		try {
			portType = createPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//deleteJobReq.setOrganization(Session.getInstance().getOrganization());
		return portType.deleteJob(deleteJobReq);
	}

	/**
	 * This method allows deleting the jobs requested done by the user giving
	 * its identifiers.
	 * 
	 * @param cancelJobReq
	 * @param host
	 *            Of the service requested
	 * @param port
	 *            Of the service requested
	 * @return
	 * @throws RemoteException
	 */
	public static DeleteJobsRequestedResponse deleteJobsRequested(
			DeleteJobsRequested deleteJobsRequestedReq) throws RemoteException,
			InvalidRequestFault, EndpointNotFoundFault, UnexpectedFault,
			InvalidServiceIDFault {
		ReservationPortType portType = null;

		try {
			portType = createPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// deleteJobsRequestedReq.setOrganization(Session.getInstance().
		// getOrganization());
		return portType.deleteJobsRequested(deleteJobsRequestedReq);
	}

	/**
	 * This method allows deleting all the jobs done by the user.
	 * 
	 * @param cancelReservationReq
	 * @param host
	 *            Of the service requested
	 * @param port
	 *            Of the service requested
	 * @return
	 * @throws RemoteException
	 */
	public static DeleteAllJobsResponse deleteAllJobs(
			DeleteAllJobs deleteAllJobsReq) throws RemoteException,
			InvalidRequestFault, EndpointNotFoundFault, UnexpectedFault,
			InvalidServiceIDFault {
		ReservationPortType portType = null;

		try {
			portType = createPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//deleteAllJobsReq.setOrganization(Session.getInstance().getOrganization
		// ());
		return portType.deleteAllJobs(deleteAllJobsReq);
	}

	/**
	 * This operation is invoked by the upper layers when the reservation start
	 * time arrives. It only needs the AAA information, the ReservationID and
	 * the ServiceID of the service that the user wants to activate to execute
	 * the operation.
	 * 
	 * @param activationReq
	 * @param host
	 *            Of the service requested
	 * @param port
	 *            Of the service requested
	 * @return
	 * @throws RemoteException
	 */
	public static ActivateResponse activate(Activate activationReq)
			throws RemoteException, InvalidRequestFault, EndpointNotFoundFault,
			UnexpectedFault, InvalidServiceIDFault {
		ReservationPortType portType = null;

		try {
			portType = createPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//activationReq.setOrganization(Session.getInstance().getOrganization())
		// ;
		return portType.activate(activationReq);
	}

	/**
	 * This method allows binding the access ports to the reserved network
	 * resources later. This is due to the fact that in many cases the final
	 * user port is known only a short time before the reservation has to be
	 * started.
	 * 
	 * @param bindReq
	 * @param host
	 *            Of the service requested
	 * @param port
	 *            Of the service requested
	 * @return
	 * @throws RemoteException
	 */
	/*
	 * public static BindResponse bind(Bind bindReq) throws RemoteException {
	 * ReservationPortType portType = null;
	 * 
	 * try { portType = createPort(); } catch (Exception e) {
	 * e.printStackTrace(); }
	 * 
	 * return portType.bind(bindReq); }
	 */

	/**
	 * Given the host and port of a service, this method creates the connection
	 * to this service by making the URL.
	 * 
	 * @param host
	 *            Of the service requested
	 * @param port
	 *            Of the service requested
	 * @return
	 * @throws MalformedURLException
	 * @throws ServiceException
	 */
	private static ReservationPortType createPort()
			throws MalformedURLException, ServiceException {
		Session ses = Session.getInstance();

		String host = ses.getHost();

		// !!!
		String url = "https://" + host + ":8443"
				+ "/wsrf/services/argia/ReservationService";

		ReservationPortType portType = null;

		ReservationServiceAddressingLocator service = new ReservationServiceAddressingLocator();
		portType = service.getReservationPortTypePort(new URL(url));

		setSecurity((Stub) portType);
		return portType;
	}

	private static void setSecurity(Stub stub) {
		stub._setProperty(Constants.GSI_ANONYMOUS, Boolean.TRUE);
	}
}