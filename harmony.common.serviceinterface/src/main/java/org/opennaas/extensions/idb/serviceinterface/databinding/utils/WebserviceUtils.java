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

package org.opennaas.extensions.idb.serviceinterface.databinding.utils;

import java.net.URI;
import java.net.URISyntaxException;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.*;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatusResponseType.ServiceStatus;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.InvalidRequestFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.InvalidReservationIDFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;

/**
 * Utility class to handle request and response elements.
 * 
 * @see TestDomainViola
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 * @version $Id: WebserviceUtils.java 619 2007-08-29 19:04:26Z
 *          willner@cs.uni-bonn.de $
 */
public final class WebserviceUtils {

    /**
     * converts a long reservation-ID to a String
     * 
     * @param resID
     *            reservationID
     * @return String reservationID
     */
    public static String convertReservationID(final long resID) {
        return String.valueOf(resID);
    }

    /**
     * converts a String reservation-ID to a long
     * 
     * @param resID
     *            reservationID
     * @return long reservationID
     */
    public static long convertReservationID(final String resID)
            throws InvalidReservationIDFaultException {
        String id = resID;
        try {
            if (resID.contains("@")) {
                final String[] part = resID.split("@");
                id = part[0];
            }
            return Long.parseLong(id);
        } catch (final NumberFormatException e) {
            throw new InvalidReservationIDFaultException(
                    "ReservationID is not parsable: '" + id + "'", e);
        }
    }

    public static Element createActivateRequest(final ActivateType activateType)
            throws InvalidRequestFaultException, UnexpectedFaultException {
        final Activate request = new Activate();
        /* generating valid Activate-Request ------------------------------- */
        request.setActivate(activateType);
        /* ----------------------------------------------------------------- */

        /* serializing the request ----------------------------------------- */
        final Element requestElement = JaxbSerializer.getInstance()
                .objectToElement(request);
        /* ----------------------------------------------------------------- */

        return requestElement;
    }

    public static ActivateResponseType createActivateResponse(
            final Element responseElement) throws InvalidRequestFaultException,
            UnexpectedFaultException {
        return ((ActivateResponse) JaxbSerializer.getInstance()
                .elementToObject(responseElement)).getActivateResponse();
    }

    /*
     * ------------------------------------------------------------------------
     * DOMAIN-UTILS
     * -----------------------------------------------------------------------
     */
    /**
     * Create an AddDomainRequest.
     * 
     * @param domain
     *            The domain informations.
     * @return The AddDomainRequest.
     * @throws InvalidRequestFaultException
     *             If the request does not match the given XSD.
     * @throws UnexpectedFaultException
     */
    public static Element createAddDomainRequest(final AddDomainType domainType)
            throws InvalidRequestFaultException, UnexpectedFaultException {
        final AddDomain request = new AddDomain();
        /* generating valid AddDomain-Request ------------------------------ */
        request.setAddDomain(domainType);
        /* ----------------------------------------------------------------- */

        /* serializing the request ----------------------------------------- */
        final Element requestElement = JaxbSerializer.getInstance()
                .objectToElement(request);
        /* ----------------------------------------------------------------- */

        return requestElement;
    }

    /**
     * Create an AddDomainResponseType.
     * 
     * @param responseElement
     *            The response element.
     * @return The AddDomainResponseType.
     * @throws InvalidRequestFaultException
     *             If the request does not match the given XSD.
     * @throws UnexpectedFaultException
     */
    public static AddDomainResponseType createAddDomainResponse(
            final Element responseElement) throws InvalidRequestFaultException,
            UnexpectedFaultException {
        return ((AddDomainResponse) JaxbSerializer.getInstance()
                .elementToObject(responseElement)).getAddDomainResponse();
    }

    /**
     * Create an AddEndpointRequest.
     * 
     * @param endpoint
     *            The request type.
     * @return The AddEndpointRequest.
     * @throws InvalidRequestFaultException
     *             If the request does not match the given XSD.
     * @throws UnexpectedFaultException
     */
    public static Element createAddEndpointRequest(final EndpointType endpoint)
            throws InvalidRequestFaultException, UnexpectedFaultException {
        final AddEndpoint request = new AddEndpoint();
        final AddEndpointType requestType = new AddEndpointType();
        /* generating valid AddEndpoint-Request ---------------------------- */
        requestType.setEndpoint(endpoint);
        request.setAddEndpoint(requestType);
        /* ----------------------------------------------------------------- */

        /* serializing the request ----------------------------------------- */
        final Element requestElement = JaxbSerializer.getInstance()
                .objectToElement(request);
        /* ----------------------------------------------------------------- */

        return requestElement;
    }

    /**
     * Create a AddEndpointResponseType.
     * 
     * @param responseElement
     *            The response element.
     * @return The AddEndpointResponseType.
     * @throws InvalidRequestFaultException
     *             If the request does not match the given XSD.
     * @throws UnexpectedFaultException
     */
    public static AddEndpointResponseType createAddEndpointResponse(
            final Element responseElement) throws InvalidRequestFaultException,
            UnexpectedFaultException {
        return ((AddEndpointResponse) JaxbSerializer.getInstance()
                .elementToObject(responseElement)).getAddEndpointResponse();
    }

    /**
     * Create an AddLinkRequest.
     * 
     * @param link
     *            The request type.
     * @return The AddLinkRequest.
     * @throws InvalidRequestFaultException
     *             If the request does not match the given XSD.
     * @throws UnexpectedFaultException
     */
    public static Element createAddLinkRequest(final AddLinkType addLinkType)
            throws InvalidRequestFaultException, UnexpectedFaultException {
        final AddLink request = new AddLink();
        /* generating valid AddLink-Request -------------------------------- */
        request.setAddLink(addLinkType);
        /* ----------------------------------------------------------------- */

        /* serializing the request ----------------------------------------- */
        final Element requestElement = JaxbSerializer.getInstance()
                .objectToElement(request);
        /* ----------------------------------------------------------------- */

        return requestElement;
    }

    /**
     * Create a AddLinkResponseType.
     * 
     * @param responseElement
     *            The response element.
     * @return The AddLinkResponseType.
     * @throws InvalidRequestFaultException
     *             If the request does not match the given XSD.
     * @throws UnexpectedFaultException
     */
    public static AddLinkResponseType createAddLinkResponse(
            final Element responseElement) throws InvalidRequestFaultException,
            UnexpectedFaultException {
        return ((AddLinkResponse) JaxbSerializer.getInstance().elementToObject(
                responseElement)).getAddLinkResponse();
    }

    /**
     * 
     * @param addOrEditDomainType
     * @return
     * @throws InvalidRequestFaultException
     * @throws UnexpectedFaultException
     */
    public static Element createAddOrEditDomainRequest(
            final AddOrEditDomainType addOrEditDomainType)
            throws InvalidRequestFaultException, UnexpectedFaultException {
        final AddOrEditDomain request = new AddOrEditDomain();

        /* generating valid request ---------------------------------------- */
        request.setAddOrEditDomain(addOrEditDomainType);
        /* ----------------------------------------------------------------- */

        /* serializing the request ----------------------------------------- */
        final Element requestElement = JaxbSerializer.getInstance()
                .objectToElement(request);
        /* ----------------------------------------------------------------- */

        return requestElement;
    }

    /*
     * ------------------------------------------------------------------------
     * ENDPOINT-UTILS
     * -----------------------------------------------------------------------
     */

    /**
     * 
     * @param responseElement
     * @return
     * @throws UnexpectedFaultException
     * @throws InvalidRequestFaultException
     */
    public static AddOrEditDomainResponseType createAddOrEditDomainResponse(
            final Element responseElement) throws InvalidRequestFaultException,
            UnexpectedFaultException {
        return ((AddOrEditDomainResponse) JaxbSerializer.getInstance()
                .elementToObject(responseElement)).getAddOrEditDomainResponse();
    }

    /**
     * Create a AddTopicRequest.
     * 
     * @param addTopicType
     *            The request type.
     * @return The AddTopicRequest.
     * @throws InvalidRequestFaultException
     *             If the request does not match the given XSD.
     * @throws UnexpectedFaultException
     */
    public static Element createAddTopicRequest(final AddTopicType addTopicType)
            throws InvalidRequestFaultException, UnexpectedFaultException {
        final AddTopic request = new AddTopic();
        /* generating valid AddTopic-Request ---------------------- */
        request.setAddTopic(addTopicType);
        /* ----------------------------------------------------------------- */

        /* serializing the request ----------------------------------------- */
        final Element requestElement = JaxbSerializer.getInstance()
                .objectToElement(request);
        /* ----------------------------------------------------------------- */

        return requestElement;
    }

    /**
     * Create a AddTopicResponseType.
     * 
     * @param responseElement
     *            The response element.
     * @return The AddTopicResponseType.
     * @throws InvalidRequestFaultException
     *             If the request does not match the given XSD.
     * @throws UnexpectedFaultException
     */
    public static AddTopicResponseType createAddTopicResponse(
            final Element responseElement) throws InvalidRequestFaultException,
            UnexpectedFaultException {
        return ((AddTopicResponse) JaxbSerializer.getInstance()
                .elementToObject(responseElement)).getAddTopicResponse();
    }

    public static Element createCancelReservationRequest(
            final CancelReservationType cancelReservationType)
            throws InvalidRequestFaultException, UnexpectedFaultException {
        final CancelReservation request = new CancelReservation();
        /* generating valid CancelReservation-Request ---------------------- */
        request.setCancelReservation(cancelReservationType);
        /* ----------------------------------------------------------------- */

        /* serializing the request ----------------------------------------- */
        final Element requestElement = JaxbSerializer.getInstance()
                .objectToElement(request);
        /* ----------------------------------------------------------------- */

        return requestElement;
    }

    public static CancelReservationResponseType createCancelReservationResponse(
            final Element responseElement) throws InvalidRequestFaultException,
            UnexpectedFaultException {
        return ((CancelReservationResponse) JaxbSerializer.getInstance()
                .elementToObject(responseElement))
                .getCancelReservationResponse();
    }

    /**
     * Create a DeleteDomainRequest.
     * 
     * @param domain
     *            The domain informations.
     * @return The DeleteDomainRequest.
     * @throws InvalidRequestFaultException
     *             If the request does not match the given XSD.
     * @throws UnexpectedFaultException
     */
    public static Element createDeleteDomainRequest(final String domainID)
            throws InvalidRequestFaultException, UnexpectedFaultException {
        final DeleteDomain request = new DeleteDomain();
        final DeleteDomainType requestType = new DeleteDomainType();
        /* generating valid DeleteDomain-Request --------------------------- */
        requestType.setDomainId(domainID);
        request.setDeleteDomain(requestType);
        /* ----------------------------------------------------------------- */

        /* serializing the request ----------------------------------------- */
        final Element requestElement = JaxbSerializer.getInstance()
                .objectToElement(request);
        /* ----------------------------------------------------------------- */

        return requestElement;
    }

    /**
     * Create a DeleteDomainResponseType.
     * 
     * @param responseElement
     *            The response element.
     * @return The DeleteDomainResponseType.
     * @throws InvalidRequestFaultException
     *             If the request does not match the given XSD.
     * @throws UnexpectedFaultException
     */
    public static DeleteDomainResponseType createDeleteDomainResponse(
            final Element responseElement) throws InvalidRequestFaultException,
            UnexpectedFaultException {
        return ((DeleteDomainResponse) JaxbSerializer.getInstance()
                .elementToObject(responseElement)).getDeleteDomainResponse();
    }

    /**
     * Create an DeleteEndpointRequest.
     * 
     * @param endpoint
     *            The request type.
     * @return The DeleteEndpointRequest.
     * @throws InvalidRequestFaultException
     *             If the request does not match the given XSD.
     * @throws UnexpectedFaultException
     */
    public static Element createDeleteEndpointRequest(
            final DeleteEndpointType deleteEndpointType)
            throws InvalidRequestFaultException, UnexpectedFaultException {
        final DeleteEndpoint request = new DeleteEndpoint();
        /* generating valid DeleteEndpoint-Request ------------------------- */
        request.setDeleteEndpoint(deleteEndpointType);
        /* ----------------------------------------------------------------- */

        /* serializing the request ----------------------------------------- */
        final Element requestElement = JaxbSerializer.getInstance()
                .objectToElement(request);
        /* ----------------------------------------------------------------- */

        return requestElement;
    }

    /*
     * ------------------------------------------------------------------------
     * LINK-UTILS
     * -----------------------------------------------------------------------
     */

    /**
     * Create a DeleteEndpointResponseType.
     * 
     * @param responseElement
     *            The response element.
     * @return The DeleteEndpointResponseType.
     * @throws InvalidRequestFaultException
     *             If the request does not match the given XSD.
     * @throws UnexpectedFaultException
     */
    public static DeleteEndpointResponseType createDeleteEndpointResponse(
            final Element responseElement) throws InvalidRequestFaultException,
            UnexpectedFaultException {
        return ((DeleteEndpointResponse) JaxbSerializer.getInstance()
                .elementToObject(responseElement)).getDeleteEndpointResponse();
    }

    /**
     * Create a DeleteLinkRequest.
     * 
     * @param link
     *            The request type.
     * @return The DeleteLinkRequest.
     * @throws InvalidRequestFaultException
     *             If the request does not match the given XSD.
     * @throws UnexpectedFaultException
     */
    public static Element createDeleteLinkRequest(
            final DeleteLinkType deleteLinkType)
            throws InvalidRequestFaultException, UnexpectedFaultException {
        final DeleteLink request = new DeleteLink();

        /* generating valid DeleteLinks-Request ---------------------------- */
        request.setDeleteLink(deleteLinkType);
        /* ----------------------------------------------------------------- */

        /* serializing the request ----------------------------------------- */
        final Element requestElement = JaxbSerializer.getInstance()
                .objectToElement(request);
        /* ----------------------------------------------------------------- */

        return requestElement;
    }

    /**
     * Create a DeleteLinkResponseType.
     * 
     * @param responseElement
     *            The response element.
     * @return The DeleteLinkResponseType.
     * @throws InvalidRequestFaultException
     *             If the request does not match the given XSD.
     * @throws UnexpectedFaultException
     */
    public static DeleteLinkResponseType createDeleteLinkResponse(
            final Element responseElement) throws InvalidRequestFaultException,
            UnexpectedFaultException {
        return ((DeleteLinkResponse) JaxbSerializer.getInstance()
                .elementToObject(responseElement)).getDeleteLinkResponse();
    }

    /**
     * Create a EditDomainRequest.
     * 
     * @param domain
     *            The domain informations.
     * @return The EditDomainRequest.
     * @throws InvalidRequestFaultException
     *             If the request does not match the given XSD.
     * @throws UnexpectedFaultException
     */
    public static Element createEditDomainRequest(
            final EditDomainType editDomainType)
            throws InvalidRequestFaultException, UnexpectedFaultException {
        final EditDomain request = new EditDomain();
        /* generating valid EditDomain-Request ----------------------------- */
        request.setEditDomain(editDomainType);
        /* ----------------------------------------------------------------- */

        /* serializing the request ----------------------------------------- */
        final Element requestElement = JaxbSerializer.getInstance()
                .objectToElement(request);
        /* ----------------------------------------------------------------- */

        return requestElement;
    }

    /**
     * Create a EditDomainResponseType.
     * 
     * @param domain
     *            The domain informations.
     * @return The EditDomainResponseType.
     * @throws InvalidRequestFaultException
     *             If the request does not match the given XSD.
     * @throws UnexpectedFaultException
     */
    public static EditDomainResponseType createEditDomainResponse(
            final Element responseElement) throws InvalidRequestFaultException,
            UnexpectedFaultException {
        return ((EditDomainResponse) JaxbSerializer.getInstance()
                .elementToObject(responseElement)).getEditDomainResponse();
    }

    /**
     * Create an EditEndpointRequest.
     * 
     * @param endpoint
     *            The request type.
     * @return The EditEndpointRequest.
     * @throws InvalidRequestFaultException
     *             If the request does not match the given XSD.
     * @throws UnexpectedFaultException
     */
    public static Element createEditEndpointRequest(
            final EditEndpointType editEndpointType)
            throws InvalidRequestFaultException, UnexpectedFaultException {
        final EditEndpoint request = new EditEndpoint();
        /* generating valid EditEndpoint-Request --------------------------- */
        request.setEditEndpoint(editEndpointType);
        /* ----------------------------------------------------------------- */

        /* serializing the request ----------------------------------------- */
        final Element requestElement = JaxbSerializer.getInstance()
                .objectToElement(request);
        /* ----------------------------------------------------------------- */

        return requestElement;
    }

    /**
     * Create a EditEndpointResponseType.
     * 
     * @param responseElement
     *            The response element.
     * @return The EditEndpointResponseType.
     * @throws InvalidRequestFaultException
     *             If the request does not match the given XSD.
     * @throws UnexpectedFaultException
     */
    public static EditEndpointResponseType createEditEndpointResponse(
            final Element responseElement) throws InvalidRequestFaultException,
            UnexpectedFaultException {
        return ((EditEndpointResponse) JaxbSerializer.getInstance()
                .elementToObject(responseElement)).getEditEndpointResponse();
    }

    /**
     * Create a EditLinkRequest.
     * 
     * @param link
     *            The request type.
     * @return The EditLinkRequest.
     * @throws InvalidRequestFaultException
     *             If the request does not match the given XSD.
     * @throws UnexpectedFaultException
     */
    public static Element createEditLinkRequest(final EditLinkType editLinkType)
            throws InvalidRequestFaultException, UnexpectedFaultException {
        final EditLink request = new EditLink();

        /* generating valid EditLinks-Request ------------------------------ */
        request.setEditLink(editLinkType);
        /* ----------------------------------------------------------------- */

        /* serializing the request ----------------------------------------- */
        final Element requestElement = JaxbSerializer.getInstance()
                .objectToElement(request);
        /* ----------------------------------------------------------------- */

        return requestElement;
    }

    /**
     * Create a EditLinkResponseType.
     * 
     * @param responseElement
     *            The response element.
     * @return The EditLinkResponseType.
     * @throws InvalidRequestFaultException
     *             If the request does not match the given XSD.
     * @throws UnexpectedFaultException
     */
    public static EditLinkResponseType createEditLinkResponse(
            final Element responseElement) throws InvalidRequestFaultException,
            UnexpectedFaultException {
        return ((EditLinkResponse) JaxbSerializer.getInstance()
                .elementToObject(responseElement)).getEditLinkResponse();
    }

    /*
     * ------------------------------------------------------------------------
     * RESERVATION-UTILS
     * -----------------------------------------------------------------------
     */

    /**
     * Create a GetDomainsRequest.
     * 
     * @param domain
     *            The domain informations.
     * @return The GetDomainsRequest.
     * @throws InvalidRequestFaultException
     *             If the request does not match the given XSD.
     * @throws UnexpectedFaultException
     */
    public static Element createGetDomainsRequest(
            final GetDomainsType getDomainsType)
            throws InvalidRequestFaultException, UnexpectedFaultException {
        final GetDomains request = new GetDomains();
        /* generating valid GetDomains-Request ----------------------------- */
        request.setGetDomains(getDomainsType);
        /* ----------------------------------------------------------------- */

        /* serializing the request ----------------------------------------- */
        final Element requestElement = JaxbSerializer.getInstance()
                .objectToElement(request);
        /* ----------------------------------------------------------------- */

        return requestElement;
    }

    /**
     * Create a GetDomainsResponseType.
     * 
     * @param responseElement
     *            The response element.
     * @return The GetDomainResponseType.
     * @throws InvalidRequestFaultException
     *             If the request does not match the given XSD.
     * @throws UnexpectedFaultException
     * @throws SAXException
     *             Element is not Valid
     */
    public static GetDomainsResponseType createGetDomainsResponse(
            final Element responseElement) throws InvalidRequestFaultException,
            UnexpectedFaultException {
        return ((GetDomainsResponse) JaxbSerializer.getInstance()
                .elementToObject(responseElement)).getGetDomainsResponse();
    }

    /**
     * Create an GetEndpointsRequest.
     * 
     * @param endpoint
     *            The request type.
     * @return The GetEndpointsRequest.
     * @throws InvalidRequestFaultException
     *             If the request does not match the given XSD.
     * @throws UnexpectedFaultException
     */
    public static Element createGetEndpointsRequest(
            final GetEndpointsType getEndpointsType)
            throws InvalidRequestFaultException, UnexpectedFaultException {
        final GetEndpoints request = new GetEndpoints();
        /* generating valid GetEndpoints-Request --------------------------- */
        request.setGetEndpoints(getEndpointsType);
        /* ----------------------------------------------------------------- */

        /* serializing the request ----------------------------------------- */
        final Element requestElement = JaxbSerializer.getInstance()
                .objectToElement(request);
        /* ----------------------------------------------------------------- */

        return requestElement;
    }

    /**
     * Create a GetEndpointsResponseType.
     * 
     * @param responseElement
     *            The response element.
     * @return The GetEndpointsResponseType.
     * @throws InvalidRequestFaultException
     *             If the request does not match the given XSD.
     * @throws UnexpectedFaultException
     */
    public static GetEndpointsResponseType createGetEndpointsResponse(
            final Element responseElement) throws InvalidRequestFaultException,
            UnexpectedFaultException {
        return ((GetEndpointsResponse) JaxbSerializer.getInstance()
                .elementToObject(responseElement)).getGetEndpointsResponse();
    }

    /**
     * Create an GetLinksRequest.
     * 
     * @param link
     *            The request type.
     * @return The GetLinksRequest.
     * @throws InvalidRequestFaultException
     *             If the request does not match the given XSD.
     * @throws UnexpectedFaultException
     */
    public static Element createGetLinksRequest(final GetLinksType getLinksType)
            throws InvalidRequestFaultException, UnexpectedFaultException {
        final GetLinks request = new GetLinks();

        /* generating valid GetLinks-Request ------------------------------- */
        request.setGetLinks(getLinksType);
        /* ----------------------------------------------------------------- */

        /* serializing the request ----------------------------------------- */
        final Element requestElement = JaxbSerializer.getInstance()
                .objectToElement(request);
        /* ----------------------------------------------------------------- */

        return requestElement;
    }

    /**
     * Create a GetLinksResponseType.
     * 
     * @param responseElement
     *            The response element.
     * @return The GetLinksResponseType.
     * @throws InvalidRequestFaultException
     *             If the request does not match the given XSD.
     * @throws UnexpectedFaultException
     */
    public static GetLinksResponseType createGetLinksResponse(
            final Element responseElement) throws InvalidRequestFaultException,
            UnexpectedFaultException {
        return ((GetLinksResponse) JaxbSerializer.getInstance()
                .elementToObject(responseElement)).getGetLinksResponse();
    }

    public static Element createGetStatusRequest(
            final GetStatusType getStatusType)
            throws InvalidRequestFaultException, UnexpectedFaultException {
        final GetStatus request = new GetStatus();
        /* generating valid GetStatus-Request ------------------------------ */
        request.setGetStatus(getStatusType);
        /* ----------------------------------------------------------------- */

        /* serializing the request ----------------------------------------- */
        final Element requestElement = JaxbSerializer.getInstance()
                .objectToElement(request);
        /* ----------------------------------------------------------------- */

        return requestElement;
    }

    public static GetStatusResponseType createGetStatusResponse(
            final Element responseElement) throws InvalidRequestFaultException,
            UnexpectedFaultException {
        return ((GetStatusResponse) JaxbSerializer.getInstance()
                .elementToObject(responseElement)).getGetStatusResponse();
    }

    /**
     * Create a GetTopicsRequest.
     * 
     * @param getTopicsType
     *            The request type.
     * @return The GetTopicsRequest.
     * @throws InvalidRequestFaultException
     *             If the request does not match the given XSD.
     * @throws UnexpectedFaultException
     */
    public static Element createGetTopicsRequest(
            final GetTopicsType getTopicsType)
            throws InvalidRequestFaultException, UnexpectedFaultException {
        final GetTopics request = new GetTopics();
        /* generating valid GetTopics-Request ---------------------- */
        request.setGetTopics(getTopicsType);
        /* ----------------------------------------------------------------- */

        /* serializing the request ----------------------------------------- */
        final Element requestElement = JaxbSerializer.getInstance()
                .objectToElement(request);
        /* ----------------------------------------------------------------- */

        return requestElement;
    }

    /**
     * Create a GetTopicsResponseType.
     * 
     * @param responseElement
     *            The response element.
     * @return The UnsubscribeResponseType.
     * @throws InvalidRequestFaultException
     *             If the request does not match the given XSD.
     * @throws UnexpectedFaultException
     */
    public static GetTopicsResponseType createGetTopicsResponse(
            final Element responseElement) throws InvalidRequestFaultException,
            UnexpectedFaultException {
        return ((GetTopicsResponse) JaxbSerializer.getInstance()
                .elementToObject(responseElement)).getGetTopicsResponse();
    }

    public static Element createIsAvailableRequest(
            final IsAvailableType isAvailableType)
            throws InvalidRequestFaultException, UnexpectedFaultException {
        final IsAvailable request = new IsAvailable();
        /* generating valid IsAvailable-Request ---------------------------- */
        request.setIsAvailable(isAvailableType);
        /* ----------------------------------------------------------------- */

        /* serializing the request ----------------------------------------- */
        final Element requestElement = JaxbSerializer.getInstance()
                .objectToElement(request);
        /* ----------------------------------------------------------------- */

        return requestElement;
    }

    /*
     * ------------------------------------------------------------------------
     * NOTIFICATION-UTILS
     * -----------------------------------------------------------------------
     */

    public static IsAvailableResponseType createIsAvailableResponse(
            final Element responseElement) throws InvalidRequestFaultException,
            UnexpectedFaultException {
        return ((IsAvailableResponse) JaxbSerializer.getInstance()
                .elementToObject(responseElement)).getIsAvailableResponse();
    }

    /**
     * Create a NotifyRequest.
     * 
     * @param notifyType
     *            The request type.
     * @return The NotifyRequest.
     * @throws InvalidRequestFaultException
     *             If the request does not match the given XSD.
     * @throws UnexpectedFaultException
     */
    public static Element createNotificationRequest(
            final NotificationType notifyType)
            throws InvalidRequestFaultException, UnexpectedFaultException {
        final Notification request = new Notification();
        /* generating valid GetTopics-Request ---------------------- */
        request.setNotification(notifyType);
        /* ----------------------------------------------------------------- */

        /* serializing the request ----------------------------------------- */
        final Element requestElement = JaxbSerializer.getInstance()
                .objectToElement(request);
        /* ----------------------------------------------------------------- */

        return requestElement;
    }

    /**
     * Create a NotifyResponseType.
     * 
     * @param responseElement
     *            The response element.
     * @return The NotifyResponseType.
     * @throws InvalidRequestFaultException
     *             If the request does not match the given XSD.
     * @throws UnexpectedFaultException
     */
    public static NotificationResponseType createNotificationResponse(
            final Element responseElement) throws InvalidRequestFaultException,
            UnexpectedFaultException {
        return ((NotificationResponse) JaxbSerializer.getInstance()
                .elementToObject(responseElement)).getNotificationResponse();
    }

    /**
     * Create a PublishRequest.
     * 
     * @param publishType
     *            The request type.
     * @return The PublishRequest.
     * @throws InvalidRequestFaultException
     *             If the request does not match the given XSD.
     * @throws UnexpectedFaultException
     */
    public static Element createPublishRequest(final PublishType publishType)
            throws InvalidRequestFaultException, UnexpectedFaultException {
        final Publish request = new Publish();
        /* generating valid Publish-Request ---------------------- */
        request.setPublish(publishType);
        /* ----------------------------------------------------------------- */

        /* serializing the request ----------------------------------------- */
        final Element requestElement = JaxbSerializer.getInstance()
                .objectToElement(request);
        /* ----------------------------------------------------------------- */

        return requestElement;
    }

    /**
     * Create a PublishResponseType.
     * 
     * @param responseElement
     *            The response element.
     * @return The PublishResponseType.
     * @throws InvalidRequestFaultException
     *             If the request does not match the given XSD.
     * @throws UnexpectedFaultException
     */
    public static PublishResponseType createPublishResponse(
            final Element responseElement) throws InvalidRequestFaultException,
            UnexpectedFaultException {
        return ((PublishResponse) JaxbSerializer.getInstance().elementToObject(
                responseElement)).getPublishResponse();
    }

    /**
     * Create a RemoveTopicRequest.
     * 
     * @param removeTopicType
     *            The request type.
     * @return The RemoveTopicRequest.
     * @throws InvalidRequestFaultException
     *             If the request does not match the given XSD.
     * @throws UnexpectedFaultException
     */
    public static Element createRemoveTopicRequest(
            final RemoveTopicType removeTopicType)
            throws InvalidRequestFaultException, UnexpectedFaultException {
        final RemoveTopic request = new RemoveTopic();
        /* generating valid RemoveTopic-Request ---------------------- */
        request.setRemoveTopic(removeTopicType);
        /* ----------------------------------------------------------------- */

        /* serializing the request ----------------------------------------- */
        final Element requestElement = JaxbSerializer.getInstance()
                .objectToElement(request);
        /* ----------------------------------------------------------------- */

        return requestElement;
    }

    /**
     * Create a RemoveTopicResponseType.
     * 
     * @param responseElement
     *            The response element.
     * @return The RemoveTopicResponseType.
     * @throws InvalidRequestFaultException
     *             If the request does not match the given XSD.
     * @throws UnexpectedFaultException
     */
    public static RemoveTopicResponseType createRemoveTopicResponse(
            final Element responseElement) throws InvalidRequestFaultException,
            UnexpectedFaultException {
        return ((RemoveTopicResponse) JaxbSerializer.getInstance()
                .elementToObject(responseElement)).getRemoveTopicResponse();
    }

    /**
     * Create a ReservationRequest.
     * 
     * @param createReservationType
     *            The request type.
     * @return The ReservationRequest.
     * @throws InvalidRequestFaultException
     *             If the request does not match the given XSD.
     * @throws UnexpectedFaultException
     */
    public static Element createReservationRequest(
            final CreateReservationType createReservationType)
            throws InvalidRequestFaultException, UnexpectedFaultException {
        final CreateReservation request = new CreateReservation();
        /* generating valid CreateReservation-Request ---------------------- */
        request.setCreateReservation(createReservationType);
        /* ----------------------------------------------------------------- */

        /* serializing the request ----------------------------------------- */
        final Element requestElement = JaxbSerializer.getInstance()
                .objectToElement(request);
        /* ----------------------------------------------------------------- */

        return requestElement;
    }

    /**
     * Create a CreateReservationResponseType.
     * 
     * @param responseElement
     *            The response element.
     * @return The CreateReservationResponseType.
     * @throws InvalidRequestFaultException
     *             If the request does not match the given XSD.
     * @throws UnexpectedFaultException
     */
    public static CreateReservationResponseType createReservationResponse(
            final Element responseElement) throws InvalidRequestFaultException,
            UnexpectedFaultException {
        return ((CreateReservationResponse) JaxbSerializer.getInstance()
                .elementToObject(responseElement))
                .getCreateReservationResponse();
    }

    /**
     * Create a SubscribeRequest.
     * 
     * @param subscribeType
     *            The request type.
     * @return The SubscribeRequest.
     * @throws InvalidRequestFaultException
     *             If the request does not match the given XSD.
     * @throws UnexpectedFaultException
     */
    public static Element createSubscribeRequest(
            final SubscribeType subscribeType)
            throws InvalidRequestFaultException, UnexpectedFaultException {
        final Subscribe request = new Subscribe();
        /* generating valid Subscribe-Request ---------------------- */
        request.setSubscribe(subscribeType);
        /* ----------------------------------------------------------------- */

        /* serializing the request ----------------------------------------- */
        final Element requestElement = JaxbSerializer.getInstance()
                .objectToElement(request);
        /* ----------------------------------------------------------------- */

        return requestElement;
    }

    /**
     * Create a SubscribeResponseType.
     * 
     * @param responseElement
     *            The response element.
     * @return The SubscribeResponseType.
     * @throws InvalidRequestFaultException
     *             If the request does not match the given XSD.
     * @throws UnexpectedFaultException
     */
    public static SubscribeResponseType createSubscribeResponse(
            final Element responseElement) throws InvalidRequestFaultException,
            UnexpectedFaultException {
        return ((SubscribeResponse) JaxbSerializer.getInstance()
                .elementToObject(responseElement)).getSubscribeResponse();
    }

    /**
     * Create a UnsubscribeRequest.
     * 
     * @param unsubscribeType
     *            The request type.
     * @return The UnsubscribeRequest.
     * @throws InvalidRequestFaultException
     *             If the request does not match the given XSD.
     * @throws UnexpectedFaultException
     */
    public static Element createUnsubscribeRequest(
            final UnsubscribeType unsubscribeType)
            throws InvalidRequestFaultException, UnexpectedFaultException {
        final Unsubscribe request = new Unsubscribe();
        /* generating valid Unsubscribe-Request ---------------------- */
        request.setUnsubscribe(unsubscribeType);
        /* ----------------------------------------------------------------- */

        /* serializing the request ----------------------------------------- */
        final Element requestElement = JaxbSerializer.getInstance()
                .objectToElement(request);
        /* ----------------------------------------------------------------- */

        return requestElement;
    }

    /**
     * Create a UnsubscribeResponseType.
     * 
     * @param responseElement
     *            The response element.
     * @return The UnsubscribeResponseType.
     * @throws InvalidRequestFaultException
     *             If the request does not match the given XSD.
     * @throws UnexpectedFaultException
     */
    public static UnsubscribeResponseType createUnsubscribeResponse(
            final Element responseElement) throws InvalidRequestFaultException,
            UnexpectedFaultException {
        return ((UnsubscribeResponse) JaxbSerializer.getInstance()
                .elementToObject(responseElement)).getUnsubscribeResponse();
    }

    public static String getDebugMsg(final GetStatusResponseType response) {
        String debug = "<status>";
        for (final ServiceStatus s : response.getServiceStatus()) {
            debug += "<service id=" + s.getServiceID() + ">";
            debug += "<status code=" + s.getStatus().ordinal() + " />";
            for (final DomainStatusType ds : s.getDomainStatus()) {
                debug += "<domainstatus domain=" + ds.getDomain() + " code="
                        + ds.getStatus().ordinal() + " />";
            }
            for (final ConnectionStatusType c : s.getConnections()) {
                debug += "<connection id=" + c.getConnectionID() + ">";
                debug += "<status code=" + c.getStatus().ordinal() + " />";
                for (final DomainConnectionStatusType ds : c.getDomainStatus()) {
                    debug += "<domainstatus domain=" + ds.getDomain()
                            + " code=" + ds.getStatus().getStatus().ordinal()
                            + " />";
                }
                debug += "</connection>";
            }
            debug += "</service>";
        }
        debug += "</status>";
        return debug;
    }

    public static URI getEmptyURI() {
        try {
            return new URI("");
        } catch (final URISyntaxException exception) {
            throw new RuntimeException("Internal Error: 0x234bef79");
        }
    }

    public static boolean isFinalStatus(final StatusType st) {
        if ((st != StatusType.CANCELLED_BY_SYSTEM)
                && (st != StatusType.CANCELLED_BY_USER)
                && (st != StatusType.COMPLETED)) {
            return false;
        }
        return true;
    }

    /**
     * Utility classes should not have a public constructor.
     */
    private WebserviceUtils() {
        throw new InstantiationError();
    }

}
