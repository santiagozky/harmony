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


package eu.ist_phosphorus.harmony.translator.g2mpls.handler;

import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.AddDomainResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.AddDomainType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.AddEndpointResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.AddEndpointType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.AddLinkResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.AddLinkType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.DeleteDomainResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.DeleteDomainType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.DeleteEndpointResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.DeleteEndpointType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.DeleteLinkResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.DeleteLinkType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EditDomainResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EditDomainType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EditEndpointResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EditEndpointType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EditLinkResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EditLinkType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EndpointInterfaceType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EndpointType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetDomainsResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetDomainsType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetEndpointsResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetEndpointsType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetLinksResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetLinksType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.Link;
import eu.ist_phosphorus.harmony.common.serviceinterface.topology.CommonTopologyHandler;

/**
 * Class to handle NSP topology-requests in a predictable manner.
 */
public final class TopologyHandler extends CommonTopologyHandler {
    /** Singleton Instance. */
    private static TopologyHandler selfInstance = null;
    /** * */
    final private eu.ist_phosphorus.harmony.adapter.dummy.handler.TopologyHandler dummyHandler;

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
        super();
        this.dummyHandler = eu.ist_phosphorus.harmony.adapter.dummy.handler.TopologyHandler.getInstance();
    }

    /**
     * AddDomain Handler.
     * <p>
     * Handler will accept AddDomain-Requests and return a AddDomain-Response
     * containing the success-parameter set to true.
     * <p>
     * 
     * @param request
     *            AddDomain Request
     * @return AddDomain Response
     */
    @Override
    public AddDomainResponseType addDomain(final AddDomainType request) {
        return this.dummyHandler.addDomain(request);
    }

    /*
     * Handler
     * =========================================================================
     */

    /**
     * AddEndpoint Handler.
     * <p>
     * Handler will accept AddEndpoint-Requests and return a
     * AddEndpoint-Response containing the success-parameter set to true.
     * <p>
     * 
     * @param request
     *            AddEndpoint Request
     * @return AddEndpoint Response
     */
    @Override
    public AddEndpointResponseType addEndpoint(final AddEndpointType request) {
        return this.dummyHandler.addEndpoint(request);
    }

    /**
     * AddLink Handler.
     * <p>
     * Handler will accept AddLink-Requests and return a AddLink-Response
     * containing a LinkID, which will be set to $(committed LinkID) + 1.
     * <p>
     * 
     * @param request
     *            AddLink Request
     * @return AddLink Response
     */
    @Override
    public AddLinkResponseType addLink(final AddLinkType request) {
        return this.dummyHandler.addLink(request);
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
     * DeleteDomain Handler.
     * <p>
     * Handler will accept DeleteDomain-Requests and return a
     * DeleteDomain-Response containing the success-parameter set to true.
     * <p>
     * 
     * @param request
     *            DeleteDomain Request
     * @return DeleteDomain Response
     */
    @Override
    public DeleteDomainResponseType deleteDomain(final DeleteDomainType request) {
        return this.dummyHandler.deleteDomain(request);
    }

    /**
     * DeleteEndpoints Handler.
     * <p>
     * Handler will accept DeleteEndpoint-Requests and return a
     * DeleteEndpoint-Response containing the success-parameter set to true.
     * <p>
     * 
     * @param request
     *            DeleteEndpoint Request
     * @return DeleteEndpoint Response
     */
    @Override
    public DeleteEndpointResponseType deleteEndpoint(
            final DeleteEndpointType request) {
        return this.dummyHandler.deleteEndpoint(request);
    }

    /**
     * DeleteLink Handler.
     * <p>
     * Handler will accept DeleteLink-Requests and return a DeleteLink-Response
     * containing the success-parameter set to true.
     * <p>
     * 
     * @param request
     *            DeleteLink Request
     * @return DeleteLink Response
     */
    @Override
    public DeleteLinkResponseType deleteLink(final DeleteLinkType request) {
        return this.dummyHandler.deleteLink(request);
    }

    /**
     * EditDomain Handler.
     * <p>
     * Handler will accept EditDomain-Requests and return a EditDomain-Response
     * containing the success-parameter set to true.
     * <p>
     * 
     * @param request
     *            EditDomain Request
     * @return EditDomain Response
     */
    @Override
    public EditDomainResponseType editDomain(final EditDomainType request) {
        return this.dummyHandler.editDomain(request);
    }

    /**
     * EditEndpoint Handler.
     * <p>
     * Handler will accept EditEndpoint-Requests and return a
     * EditEndpoint-Response containing the success-parameter set to true.
     * <p>
     * 
     * @param request
     *            EditEndpoint Request
     * @return EditEndpoint Response
     */
    @Override
    public EditEndpointResponseType editEndpoint(final EditEndpointType request) {
        return this.dummyHandler.editEndpoint(request);
    }

    /**
     * EditLink Handler.
     * <p>
     * Handler will accept EditLink-Requests and return a EditLink-Response
     * containing the success-parameter set to true.
     * <p>
     * 
     * @param request
     *            EditLink Request
     * @return EditLink Response
     */
    @Override
    public EditLinkResponseType editLink(final EditLinkType request) {
        return this.dummyHandler.editLink(request);
    }

    /**
     * GetDomains Handler.
     * <p>
     * Handler will accept GetDomains-Requests and return a GetDomain-Response
     * containing a list of DomainInformationTypes with only one request built
     * as follows: <br>
     * DomainID -> DummyDomain <br>
     * Description -> DummyDomain <br>
     * ReservationEPR -> dummy.reservation.epr <br>
     * TopologyEPR -> dummy.topology.epr <br>
     * TNAPrefixList = List with one TNAPrefix: <br>
     * -> 0.0.0.0/32 <br>
     * <p>
     * 
     * @param request
     *            GetDomains Request
     * @return GetDomains Response
     */
    @Override
    public GetDomainsResponseType getDomains(final GetDomainsType request) {
        return this.dummyHandler.getDomains(request);
    }

    /**
     * GetEndpoints Handler.
     * <p>
     * Handler will accept GetEndpoints-Requests and return a
     * GetEndpoints-Response containing a list of EndpointTypes with only one
     * request built as follows: <br>
     * EndpointID -> 0.0.0.0 <br>
     * Name -> DummyEndpoint <br>
     * Description -> DummyEndpoint <br>
     * DomainID -> committed DomainID <br>
     * Interface -> EndpointInterfaceType.BORDER <br>
     * Bandwidth -> 1 <br>
     * <p>
     * 
     * @param request
     *            GetEndpoints Request
     * @return GetEndpoints Response
     */
    @Override
    public GetEndpointsResponseType getEndpoints(final GetEndpointsType request) {
        final GetEndpointsResponseType responseType = new GetEndpointsResponseType();
        final EndpointType epInfo = new EndpointType();
        epInfo.setEndpointId("10.1.2.1");
        epInfo.setName("G2MPLSEndpoint");
        epInfo.setDescription("G2MPLSEndpoint");
        epInfo.setDomainId(request.getDomain());
        epInfo.setInterface(EndpointInterfaceType.BORDER);
        epInfo.setBandwidth(Integer.valueOf(1));
        responseType.getEndpoints().add(epInfo);

        return responseType;
    }

    /**
     * GetLinks Handler.
     * <p>
     * Handler will accept GetLinks-Requests and return a GetLinks-Response
     * containing a list of Links with only one request built as follows: <br>
     * LinkID -> 1 <br>
     * Name -> DummyLink <br>
     * Description -> DummyLink <br>
     * SourceEndpoint -> 0.0.0.1 <br>
     * DestinationEndpoint -> 0.0.0.2 <br>
     * <p>
     * 
     * @param request
     *            GetLinks Request
     * @return GetLinks Response
     */
    @Override
    public GetLinksResponseType getLinks(final GetLinksType request) {
        final GetLinksResponseType responseType = new GetLinksResponseType();
        final Link l = new Link();
        l.setName("G2MPLSLink");
        l.setDescription("G2MPLSLink");
        l.setSourceEndpoint("10.1.2.1");
        l.setDestinationEndpoint("10.3.1.7");
        responseType.getLink().add(l);

        return responseType;
    }
}
