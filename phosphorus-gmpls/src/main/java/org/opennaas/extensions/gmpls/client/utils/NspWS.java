/**
 *
 */
package org.opennaas.extensions.gmpls.client.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.muse.ws.addressing.soap.SoapFault;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CreateReservationResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CreateReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DomainInformationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetDomainsResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetDomainsType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetEndpointsResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetEndpointsType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservations;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationsComplexType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationsResponse;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationsResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationsType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatusResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.utils.JaxbSerializer;
import org.opennaas.extensions.idb.serviceinterface.databinding.utils.WebserviceUtils;
import org.opennaas.extensions.idb.serviceinterface.reservation.SimpleReservationClient;
import org.opennaas.extensions.idb.serviceinterface.topology.SimpleTopologyClient;
import org.opennaas.core.utils.Config;
import org.opennaas.core.utils.Helpers;

/**
 * @author Stephan Wagner (stephan.wagner@iais.fraunhofer.de)
 */
public class NspWS {

    /** The Topology Client. */
    private static SimpleTopologyClient topologyClient;
    /** The Reservation Client. */
    private static SimpleReservationClient reservationClient;
    /** List of CreateReservation Responses. */
    private static List<CreateReservationResponseType> reservations = new ArrayList<CreateReservationResponseType>();
    /** TNA List. */
    private static List<EndpointType> endpointList = new ArrayList<EndpointType>();

    static {
	NspWS.topologyClient = new SimpleTopologyClient(Config.getString(
		"gmplsClient", "epr.nsp.topology"));
	NspWS.reservationClient = new SimpleReservationClient(Config.getString(
		"gmplsClient", "epr.nsp.reservation"));
	// NspWS.reservationClient =
	// new SimpleReservationClient(new EndpointReference(new URI(
	// Config.getString(
	// "gmpls", "epr.nrpsThinReservation"))));
    }

    /**
     * @param reservationId
     * @throws IOException
     * @throws JAXBException
     * @throws SAXException
     * @throws SoapFault
     */
    public static final synchronized void cancelReservation(
	    final long reservationId) throws SoapFault {
	final CancelReservationType c = new CancelReservationType();
	c.setReservationID(WebserviceUtils.convertReservationID(reservationId));
	NspWS.reservationClient.cancelReservation(c);
	final List<CreateReservationResponseType> reservationsToBeRemoved = new ArrayList<CreateReservationResponseType>();
	for (final CreateReservationResponseType reservation : NspWS.reservations) {
	    if (WebserviceUtils.convertReservationID(reservation
		    .getReservationID()) == reservationId) {
		reservationsToBeRemoved.add(reservation);
	    }
	}

	for (final CreateReservationResponseType createReservationResponseType : reservationsToBeRemoved) {
	    NspWS.reservations.remove(createReservationResponseType);
	}
	System.out.println("Canceled: " + reservationId);
    }

    /**
     * @param request
     * @return
     * @throws IOException
     * @throws JAXBException
     * @throws SAXException
     * @throws SoapFault
     */
    public static final synchronized CreateReservationResponseType createReservation(
	    final CreateReservationType request) throws SoapFault {

	final CreateReservationResponseType response = NspWS.reservationClient
		.createReservation(request);
	NspWS.reservations.add(response);
	return response;
    }

    /**
     * @return the reservations
     * @throws SAXException
     * @throws JAXBException
     * @throws IOException
     * @throws SoapFault
     */
    public static synchronized List<GetReservationsComplexType> getReservations()
	    throws SoapFault {
	final GetReservations envelope = new GetReservations();
	final GetReservationsType request = new GetReservationsType();

	request.setPeriodStartTime(Helpers.generateXMLCalendar());
	// reservations for the next 7 days
	request.setPeriodEndTime(Helpers.generateXMLCalendar(7 * 24 * 60, 0));

	System.out.println("Getting reservation in period: "
		+ request.getPeriodStartTime() + " - "
		+ request.getPeriodEndTime());

	envelope.setGetReservations(request);

	final Element reqElement = JaxbSerializer.getInstance()
		.objectToElement(envelope);

	final Element resElement = NspWS.reservationClient
		.getReservations(reqElement);

	final GetReservationsResponseType response = ((GetReservationsResponse) JaxbSerializer
		.getInstance().elementToObject(resElement))
		.getGetReservationsResponse();

	return response.getReservation();

    }

    /**
     * @param reservationId
     * @return
     * @throws IOException
     * @throws JAXBException
     * @throws SAXException
     * @throws SoapFault
     */
    public static final synchronized GetStatusResponseType getStatus(
	    final long reservationId) throws SoapFault {
	final GetStatusType c = new GetStatusType();
	c.setReservationID(WebserviceUtils.convertReservationID(reservationId));

	return NspWS.reservationClient.getStatus(c);
    }

    /**
     * @return List<EndpointType>
     * @throws SoapFault
     * @throws JAXBException
     * @throws SAXException
     * @throws IOException
     */
    public static final synchronized List<EndpointType> getTopologyInformation()
	    throws SoapFault {

	final GetDomainsResponseType domResponseType = NspWS.topologyClient
		.getDomains(new GetDomainsType());

	for (final DomainInformationType domain : domResponseType.getDomains()) {
	    final GetEndpointsType epRequest = new GetEndpointsType();
	    epRequest.setDomain(domain.getDomainId());

	    final GetEndpointsResponseType epResponseType = NspWS.topologyClient
		    .getEndpoints(epRequest);
	    NspWS.endpointList.addAll(epResponseType.getEndpoints());
	}
	EndpointListSorter.sortHarmonyEndpoints(NspWS.endpointList);
	return NspWS.endpointList;
    }

}
