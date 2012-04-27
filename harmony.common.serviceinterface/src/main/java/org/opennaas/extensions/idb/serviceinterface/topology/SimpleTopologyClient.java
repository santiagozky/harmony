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

/**
 *
 */
package org.opennaas.extensions.idb.serviceinterface.topology;

import org.apache.muse.ws.addressing.EndpointReference;
import org.apache.muse.ws.addressing.soap.SoapFault;
import org.w3c.dom.Element;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AddDomainResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AddDomainType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AddEndpointResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AddEndpointType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AddLinkResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AddLinkType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AddOrEditDomainResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AddOrEditDomainType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DeleteDomainResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DeleteDomainType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DeleteEndpointResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DeleteEndpointType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DeleteLinkResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DeleteLinkType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DomainInformationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EditDomainResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EditDomainType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EditEndpointResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EditEndpointType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EditLinkResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EditLinkType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetDomainsResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetDomainsType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetEndpointsResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetEndpointsType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetLinksResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetLinksType;
import org.opennaas.extensions.idb.serviceinterface.databinding.utils.WebserviceUtils;

/**
 * @author willner
 */
public class SimpleTopologyClient extends CommonTopologyClient {

    /**
     * @param endpointReference
     */
    public SimpleTopologyClient(final EndpointReference endpointReference) {
        super(endpointReference);
        // TODO Auto-generated constructor stub
    }

    /**
     * 
     * @param topologyWS
     */
    public SimpleTopologyClient(final ITopologyWS topologyWS) {
        super(topologyWS);
    }

    /**
     * @param endpointReference
     */
    public SimpleTopologyClient(final String endpointReference) {
        super(endpointReference);
    }

    /**
     * generate a AddDomainRequest, call the Web Service and return a
     * AddDomainResponseType.
     * 
     * @param addDomain
     *            AddDomainType
     * @return AddDomainResponseType
     * @throws SoapFault
     */
    public AddDomainResponseType addDomain(final AddDomainType addDomain)
            throws SoapFault {

        final Element request = WebserviceUtils
                .createAddDomainRequest(addDomain);
        final Element responseElement = super.addDomain(request);
        final AddDomainResponseType result = WebserviceUtils
                .createAddDomainResponse(responseElement);
        return result;
    }

    /**
     * 
     * @param identifier
     * @param resEPR
     * @param TNAPrefix
     * @return The response
     * @throws SoapFault
     */
    public AddDomainResponseType addDomain(final String identifier,
            final String resEPR, final String TNAPrefix) throws SoapFault {
        final AddDomainType addDomain = new AddDomainType();
        final DomainInformationType dit = this.getDomainInformation(identifier,
                resEPR, TNAPrefix);
        addDomain.setDomain(dit);
        return this.addDomain(addDomain);
    }

    /**
     * generate a AddEndpointRequest, call the Web Service and return a
     * AddEndpointResponseType.
     * 
     * @param addEndpoint
     *            AddEndpointType
     * @return AddEndpointResponseType
     * @throws SoapFault
     */
    public AddEndpointResponseType addEndpoint(final AddEndpointType addEndpoint)
            throws SoapFault {
        final Element request = WebserviceUtils
                .createAddEndpointRequest(addEndpoint.getEndpoint());
        final Element responseElement = super.addEndpoint(request);
        final AddEndpointResponseType result = WebserviceUtils
                .createAddEndpointResponse(responseElement);
        return result;
    }

    /**
     * generate a AddEndpointRequest, call the Web Service and return a
     * AddEndpointResponseType.
     * 
     * @param endpoint
     *            EndpointType
     * @return AddEndpointResponseType
     * @throws SoapFault
     */
    public AddEndpointResponseType addEndpoint(final EndpointType endpoint)
            throws SoapFault {
        final AddEndpointType addEndpointType = new AddEndpointType();
        addEndpointType.setEndpoint(endpoint);
        return this.addEndpoint(addEndpointType);
    }

    /**
     * generate a AddLinkRequest, call the Web Service and return a
     * AddLinkResponseType.
     * 
     * @param addLink
     *            AddLinkType
     * @return AddLinkResponseType
     * @throws SoapFault
     */
    public AddLinkResponseType addLink(final AddLinkType addLink)
            throws SoapFault {
        final Element request = WebserviceUtils.createAddLinkRequest(addLink);
        final Element responseElement = super.addLink(request);
        final AddLinkResponseType result = WebserviceUtils
                .createAddLinkResponse(responseElement);
        return result;
    }

    /**
     * generate a GetEndpointsRequest, call the Web Service and return a
     * GetEndpointsResponseType.
     * 
     * @param getEndpoints
     *            GetEndpointsType
     * @return GetEndpointsResponseType
     * @throws SoapFault
     */
    public AddOrEditDomainResponseType addOrEditDomain(
            final AddOrEditDomainType addOrEditDomainType) throws SoapFault {
        final Element request = WebserviceUtils
                .createAddOrEditDomainRequest(addOrEditDomainType);
        final Element responseElement = super.addOrEditDomain(request);
        final AddOrEditDomainResponseType result = WebserviceUtils
                .createAddOrEditDomainResponse(responseElement);
        return result;
    }

    /**
     * 
     * @param identifier
     * @param resEPR
     * @param TNAPrefix
     * @return The result.
     * @throws SoapFault
     */
    public AddOrEditDomainResponseType addOrEditDomain(final String identifier,
            final String resEPR, final String TNAPrefix) throws SoapFault {
        final AddOrEditDomainType addOrEditDomainType = new AddOrEditDomainType();
        addOrEditDomainType.setDomain(this.getDomainInformation(identifier,
                resEPR, TNAPrefix));
        return this.addOrEditDomain(addOrEditDomainType);
    }

    /**
     * generate a DeleteDomainRequest, call the Web Service and return a
     * DeleteDomainResponseType.
     * 
     * @param deleteDomain
     *            DeleteDomainType
     * @return DeleteDomainResponseType
     * @throws SoapFault
     */
    public DeleteDomainResponseType deleteDomain(
            final DeleteDomainType deleteDomain) throws SoapFault {
        final Element request = WebserviceUtils
                .createDeleteDomainRequest(deleteDomain.getDomainId());
        final Element responseElement = super.deleteDomain(request);
        final DeleteDomainResponseType result = WebserviceUtils
                .createDeleteDomainResponse(responseElement);
        return result;
    }

    /**
     * 
     * @param identifier
     * @return The result.
     * @throws SoapFault
     */
    public DeleteDomainResponseType deleteDomain(final String identifier)
            throws SoapFault {
        final DeleteDomainType deleteDomain = new DeleteDomainType();
        deleteDomain.setDomainId(identifier);
        return this.deleteDomain(deleteDomain);
    }

    /**
     * generate a DeleteEndpointRequest, call the Web Service and return a
     * DeleteEndpointResponseType.
     * 
     * @param deleteEndpoint
     *            DeleteEndpointType
     * @return DeleteEndpointResponseType
     * @throws SoapFault
     */
    public DeleteEndpointResponseType deleteEndpoint(
            final DeleteEndpointType deleteEndpoint) throws SoapFault {
        final Element request = WebserviceUtils
                .createDeleteEndpointRequest(deleteEndpoint);
        final Element responseElement = super.deleteEndpoint(request);
        final DeleteEndpointResponseType result = WebserviceUtils
                .createDeleteEndpointResponse(responseElement);
        return result;
    }

    /**
     * generate a DeleteLinkRequest, call the Web Service and return a
     * DeleteLinkResponseType.
     * 
     * @param deleteLink
     *            DeleteLinkType
     * @return DeleteLinkResponseType
     * @throws SoapFault
     */
    public DeleteLinkResponseType deleteLink(final DeleteLinkType deleteLink)
            throws SoapFault {
        final Element request = WebserviceUtils
                .createDeleteLinkRequest(deleteLink);
        final Element responseElement = super.deleteLink(request);
        final DeleteLinkResponseType result = WebserviceUtils
                .createDeleteLinkResponse(responseElement);
        return result;
    }

    /**
     * generate a EditDomainRequest, call the Web Service and return a
     * EditDomainResponseType.
     * 
     * @param editDomain
     *            EditDomainType
     * @return EditDomainResponseType
     * @throws SoapFault
     */
    public EditDomainResponseType editDomain(final EditDomainType editDomain)
            throws SoapFault {
        final Element request = WebserviceUtils
                .createEditDomainRequest(editDomain);
        final Element responseElement = super.editDomain(request);
        final EditDomainResponseType result = WebserviceUtils
                .createEditDomainResponse(responseElement);
        return result;
    }

    /**
     * 
     * @param identifier
     * @param resEPR
     * @param TNAPrefix
     * @return
     * @throws SoapFault
     */
    public EditDomainResponseType editDomain(final String identifier,
            final String resEPR, final String TNAPrefix) throws SoapFault {
        final EditDomainType editDomain = new EditDomainType();
        editDomain.setDomain(this.getDomainInformation(identifier, resEPR,
                TNAPrefix));
        return this.editDomain(editDomain);
    }

    /**
     * generate a EditEndpointRequest, call the Web Service and return a
     * EditEndpointResponseType.
     * 
     * @param editEndpoint
     *            EditEndpointType
     * @return EditEndpointResponseType
     * @throws SoapFault
     */
    public EditEndpointResponseType editEndpoint(
            final EditEndpointType editEndpoint) throws SoapFault {
        final Element request = WebserviceUtils
                .createEditEndpointRequest(editEndpoint);
        final Element responseElement = super.editEndpoint(request);
        final EditEndpointResponseType result = WebserviceUtils
                .createEditEndpointResponse(responseElement);
        return result;
    }

    /**
     * generate a EditLinkRequest, call the Web Service and return a
     * EditLinkResponseType.
     * 
     * @param editLink
     *            EditLinkType
     * @return EditLinkResponseType
     * @throws SoapFault
     */
    public EditLinkResponseType editLink(final EditLinkType editLink)
            throws SoapFault {
        final Element request = WebserviceUtils.createEditLinkRequest(editLink);
        final Element responseElement = super.editLink(request);
        final EditLinkResponseType result = WebserviceUtils
                .createEditLinkResponse(responseElement);
        return result;
    }

    public DomainInformationType getDomainInformation(final String identifier,
            final String resEPR, final String TNAPrefix) {
        final DomainInformationType dit = new DomainInformationType();
        dit.setDomainId(identifier);
        dit.setReservationEPR(resEPR);
        dit.getTNAPrefix().add(TNAPrefix);
        return dit;
    }

    /**
     * 
     * @return
     * @throws SoapFault
     */
    public GetDomainsResponseType getDomains() throws SoapFault {
        return this.getDomains(new GetDomainsType());
    }

    /**
     * generate a GetDomainsRequest, call the Web Service and return a
     * GetDomainsResponseType.
     * 
     * @param getDomains
     *            GetDomainsType
     * @return GetDomainsResponseType
     * @throws SoapFault
     */
    public GetDomainsResponseType getDomains(final GetDomainsType getDomains)
            throws SoapFault {
        final Element request = WebserviceUtils
                .createGetDomainsRequest(getDomains);
        final Element responseElement = super.getDomains(request);
        final GetDomainsResponseType result = WebserviceUtils
                .createGetDomainsResponse(responseElement);
        return result;
    }

    /**
     * generate a GetEndpointsRequest, call the Web Service and return a
     * GetEndpointsResponseType.
     * 
     * @param getEndpoints
     *            GetEndpointsType
     * @return GetEndpointsResponseType
     * @throws SoapFault
     */
    public GetEndpointsResponseType getEndpoints(
            final GetEndpointsType getEndpoints) throws SoapFault {
        final Element request = WebserviceUtils
                .createGetEndpointsRequest(getEndpoints);
        final Element responseElement = super.getEndpoints(request);
        final GetEndpointsResponseType result = WebserviceUtils
                .createGetEndpointsResponse(responseElement);
        return result;
    }

    /**
     * 
     * @param domainID
     * @return
     * @throws SoapFault
     */
    public GetEndpointsResponseType getEndpoints(final String domainID)
            throws SoapFault {
        final GetEndpointsType getEndpoints = new GetEndpointsType();
        getEndpoints.setDomain(domainID);
        return this.getEndpoints(getEndpoints);
    }

    /**
     * generate a GetLinksRequest, call the Web Service and return a
     * GetLinksResponseType.
     * 
     * @param getLinks
     *            GetLinksType
     * @return GetLinksResponseType
     * @throws SoapFault
     */
    public GetLinksResponseType getLinks(final GetLinksType getLinks)
            throws SoapFault {
        final Element request = WebserviceUtils.createGetLinksRequest(getLinks);
        final Element responseElement = super.getLinks(request);
        final GetLinksResponseType result = WebserviceUtils
                .createGetLinksResponse(responseElement);
        return result;
    }

    /**
     * 
     * @param domainID
     * @return
     * @throws SoapFault
     */
    public GetLinksResponseType getLinks(final String domainID)
            throws SoapFault {
        final GetLinksType getLinks = new GetLinksType();
        getLinks.setDomainId(domainID);
        return this.getLinks(getLinks);
    }
}
