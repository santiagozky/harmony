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

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AddDomainResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AddDomainType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AddEndpointResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AddEndpointType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AddLinkResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AddLinkType;
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
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointInterfaceType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetDomainsResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetDomainsType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetEndpointsResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetEndpointsType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetLinksResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetLinksType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.Link;
import org.opennaas.extensions.idb.serviceinterface.topology.CommonTopologyHandler;

/**
 * Class to handle NSP topology-requests in a predictable manner.
 */
public final class TopologyHandler extends CommonTopologyHandler {
    /** Singleton Instance. */
    private static TopologyHandler selfInstance = null;

    /**
     * Instance getter.
     * 
     * @return Singleton Instance
     */
    public static TopologyHandler getInstance() {
        if (TopologyHandler.selfInstance == null) {
            TopologyHandler.selfInstance = new TopologyHandler();
        }
        return TopologyHandler.selfInstance;
    }

    /**
     * Private constructor: Singleton.
     */
    private TopologyHandler() {
        // nothing yet
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
     * Handler
     * =========================================================================
     */

    /**
     * AddDomain Handler.
     * <p>
     * Handler will accept AddDomain-Requests and return a AddDomain-Response
     * containing the success-parameter set to true.
     * <p>
     * 
     * @param element
     *            AddDomain Request
     * @return AddDomain Response
     */
    @Override
    public AddDomainResponseType addDomain(final AddDomainType element) {

        final AddDomainResponseType responseType = new AddDomainResponseType();
        responseType.setSuccess(true);

        return responseType;
    }

    /**
     * AddEndpoint Handler.
     * <p>
     * Handler will accept AddEndpoint-Requests and return a
     * AddEndpoint-Response containing the success-parameter set to true.
     * <p>
     * 
     * @param element
     *            AddEndpoint Request
     * @return AddEndpoint Response
     */
    @Override
    public AddEndpointResponseType addEndpoint(final AddEndpointType element) {
        final AddEndpointResponseType responseType = new AddEndpointResponseType();
        responseType.setSuccess(true);

        return responseType;
    }

    /**
     * AddLink Handler.
     * <p>
     * Handler will accept AddLink-Requests and return a AddLink-Response
     * containing a LinkID, which will be set to $(committed LinkID) + 1.
     * <p>
     * 
     * @param element
     *            AddLink Request
     * @return AddLink Response
     */
    @Override
    public AddLinkResponseType addLink(final AddLinkType element) {
        final AddLinkResponseType responseType = new AddLinkResponseType();
        responseType.setSuccess(true);

        return responseType;
    }

    /**
     * DeleteDomain Handler.
     * <p>
     * Handler will accept DeleteDomain-Requests and return a
     * DeleteDomain-Response containing the success-parameter set to true.
     * <p>
     * 
     * @param element
     *            DeleteDomain Request
     * @return DeleteDomain Response
     */
    @Override
    public DeleteDomainResponseType deleteDomain(final DeleteDomainType element) {
        final DeleteDomainResponseType responseType = new DeleteDomainResponseType();
        responseType.setSuccess(true);

        return responseType;
    }

    /**
     * DeleteEndpoints Handler.
     * <p>
     * Handler will accept DeleteEndpoint-Requests and return a
     * DeleteEndpoint-Response containing the success-parameter set to true.
     * <p>
     * 
     * @param element
     *            DeleteEndpoint Request
     * @return DeleteEndpoint Response
     */
    @Override
    public DeleteEndpointResponseType deleteEndpoint(
            final DeleteEndpointType element) {
        final DeleteEndpointResponseType responseType = new DeleteEndpointResponseType();
        responseType.setSuccess(true);

        return responseType;
    }

    /**
     * DeleteLink Handler.
     * <p>
     * Handler will accept DeleteLink-Requests and return a DeleteLink-Response
     * containing the success-parameter set to true.
     * <p>
     * 
     * @param element
     *            DeleteLink Request
     * @return DeleteLink Response
     */
    @Override
    public DeleteLinkResponseType deleteLink(final DeleteLinkType element) {
        final DeleteLinkResponseType responseType = new DeleteLinkResponseType();
        responseType.setSuccess(true);

        return responseType;
    }

    /**
     * EditDomain Handler.
     * <p>
     * Handler will accept EditDomain-Requests and return a EditDomain-Response
     * containing the success-parameter set to true.
     * <p>
     * 
     * @param element
     *            EditDomain Request
     * @return EditDomain Response
     */
    @Override
    public EditDomainResponseType editDomain(final EditDomainType element) {

        final EditDomainResponseType responseType = new EditDomainResponseType();
        responseType.setSuccess(true);

        return responseType;
    }

    /**
     * EditEndpoint Handler.
     * <p>
     * Handler will accept EditEndpoint-Requests and return a
     * EditEndpoint-Response containing the success-parameter set to true.
     * <p>
     * 
     * @param element
     *            EditEndpoint Request
     * @return EditEndpoint Response
     */
    @Override
    public EditEndpointResponseType editEndpoint(final EditEndpointType element) {
        final EditEndpointResponseType responseType = new EditEndpointResponseType();
        responseType.setSuccess(true);

        return responseType;
    }

    /**
     * EditLink Handler.
     * <p>
     * Handler will accept EditLink-Requests and return a EditLink-Response
     * containing the success-parameter set to true.
     * <p>
     * 
     * @param element
     *            EditLink Request
     * @return EditLink Response
     */
    @Override
    public EditLinkResponseType editLink(final EditLinkType element) {
        final EditLinkResponseType responseType = new EditLinkResponseType();
        responseType.setResult(true);

        return responseType;
    }

    /**
     * GetDomains Handler.
     * <p>
     * Handler will accept GetDomains-Requests and return a GetDomain-Response
     * containing a list of DomainInformationTypes with only one element built
     * as follows: <br>
     * DomainID -> DummyDomain <br>
     * Description -> DummyDomain <br>
     * ReservationEPR -> dummy.reservation.epr <br>
     * TopologyEPR -> dummy.topology.epr <br>
     * TNAPrefixList = List with one TNAPrefix: <br> -> 0.0.0.0/32 <br>
     * <p>
     * 
     * @param element
     *            GetDomains Request
     * @return GetDomains Response
     */
    @Override
    public GetDomainsResponseType getDomains(final GetDomainsType element) {
        final GetDomainsResponseType responseType = new GetDomainsResponseType();

        final DomainInformationType dom = new DomainInformationType();
        dom.setDomainId("DummyDomain");
        dom.setDescription("DummyDomain");
        dom.setReservationEPR("dummy.reservation.epr");
        dom.setTopologyEPR("dummy.topology.epr");
        dom.getTNAPrefix().add("0.0.0.0/32");
        responseType.getDomains().add(dom);

        return responseType;
    }

    /**
     * GetEndpoints Handler.
     * <p>
     * Handler will accept GetEndpoints-Requests and return a
     * GetEndpoints-Response containing a list of EndpointTypes with only one
     * element built as follows: <br>
     * EndpointID -> 0.0.0.0 <br>
     * Name -> DummyEndpoint <br>
     * Description -> DummyEndpoint <br>
     * DomainID -> committed DomainID <br>
     * Interface -> EndpointInterfaceType.BORDER <br>
     * Bandwidth -> 1 <br>
     * <p>
     * 
     * @param element
     *            GetEndpoints Request
     * @return GetEndpoints Response
     */
    @Override
    public GetEndpointsResponseType getEndpoints(final GetEndpointsType element) {
        final GetEndpointsResponseType responseType = new GetEndpointsResponseType();
        final EndpointType epInfo = new EndpointType();
        epInfo.setEndpointId("0.0.0.0");
        epInfo.setName("DummyEndpoint");
        epInfo.setDescription("DummyEndpoint");
        epInfo.setDomainId(element.getDomain());
        epInfo.setInterface(EndpointInterfaceType.BORDER);
        epInfo.setBandwidth(Integer.valueOf(1));
        responseType.getEndpoints().add(epInfo);

        return responseType;
    }

    /**
     * GetLinks Handler.
     * <p>
     * Handler will accept GetLinks-Requests and return a GetLinks-Response
     * containing a list of Links with only one element built as follows: <br>
     * LinkID -> 1 <br>
     * Name -> DummyLink <br>
     * Description -> DummyLink <br>
     * SourceEndpoint -> 0.0.0.1 <br>
     * DestinationEndpoint -> 0.0.0.2 <br>
     * <p>
     * 
     * @param element
     *            GetLinks Request
     * @return GetLinks Response
     */
    @Override
    public GetLinksResponseType getLinks(final GetLinksType element) {
        final GetLinksResponseType responseType = new GetLinksResponseType();
        final Link l = new Link();
        l.setName("DummyLink");
        l.setDescription("DummyLink");
        l.setSourceEndpoint("0.0.0.1");
        l.setDestinationEndpoint("0.0.0.2");
        responseType.getLink().add(l);

        return responseType;
    }
}
