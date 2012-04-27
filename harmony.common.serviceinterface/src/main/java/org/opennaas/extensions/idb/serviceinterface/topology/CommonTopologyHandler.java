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

package org.opennaas.extensions.idb.serviceinterface.topology;

import org.apache.muse.ws.addressing.soap.SoapFault;

import org.opennaas.extensions.idb.serviceinterface.RequestHandler;
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
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EditDomainResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EditDomainType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EditEndpointResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EditEndpointType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EditLinkResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EditLinkType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetDomainsResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetDomainsType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetEndpointsResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetEndpointsType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetLinksResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetLinksType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.DomainAlreadyExistsFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.OperationNotSupportedFaultException;

/**
 * Class to handle NSP topology-requests in a predictable manner.
 */
public class CommonTopologyHandler extends RequestHandler {
    /** Singleton Instance. */
    private static CommonTopologyHandler selfInstance;

    /**
     * Instance getter.
     * 
     * @return Singleton Instance
     */
    public static CommonTopologyHandler getInstance() {
        if (CommonTopologyHandler.selfInstance == null) {
            CommonTopologyHandler.selfInstance = new CommonTopologyHandler();
        }
        return CommonTopologyHandler.selfInstance;
    }

    /**
     * Private constructor: Singleton.
     */
    public CommonTopologyHandler() {
        super();
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
    public AddDomainResponseType addDomain(final AddDomainType request)
            throws Throwable {
        throw new OperationNotSupportedFaultException("Not implemented yet.");
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
    public AddEndpointResponseType addEndpoint(final AddEndpointType request)
            throws SoapFault, Throwable {
        throw new OperationNotSupportedFaultException("Not implemented yet.");
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
     * @throws SoapFault
     */
    public AddLinkResponseType addLink(final AddLinkType request)
            throws SoapFault, Throwable {
        throw new OperationNotSupportedFaultException("Not implemented yet.");
    }

    /**
     * 
     * @param request
     * @return The result.
     * @throws Throwable
     */
    public AddOrEditDomainResponseType addOrEditDomain(
            final AddOrEditDomainType element) throws Throwable {

        final AddOrEditDomainResponseType response = new AddOrEditDomainResponseType();
        response.setSuccess(false);

        try {
            final AddDomainType addDomainType = new AddDomainType();
            addDomainType.setDomain(element.getDomain());
            final AddDomainResponseType addResponse = this
                    .addDomain(addDomainType);
            response.setSuccess(addResponse.isSuccess());
        } catch (final DomainAlreadyExistsFaultException exception) {
            final EditDomainType editDomainType = new EditDomainType();
            editDomainType.setDomain(element.getDomain());
            final EditDomainResponseType editResponse = this
                    .editDomain(editDomainType);
            response.setSuccess(editResponse.isSuccess());
        }

        return response;
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
    public DeleteDomainResponseType deleteDomain(final DeleteDomainType request)
            throws Throwable {
        throw new OperationNotSupportedFaultException("Not implemented yet.");
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
    public DeleteEndpointResponseType deleteEndpoint(
            final DeleteEndpointType request) throws SoapFault, Throwable {
        throw new OperationNotSupportedFaultException("Not implemented yet.");
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
    public DeleteLinkResponseType deleteLink(final DeleteLinkType request)
            throws SoapFault, Throwable {
        throw new OperationNotSupportedFaultException("Not implemented yet.");
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
    public EditDomainResponseType editDomain(final EditDomainType request)
            throws SoapFault, Throwable {
        throw new OperationNotSupportedFaultException("Not implemented yet.");
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
    public EditEndpointResponseType editEndpoint(final EditEndpointType request)
            throws SoapFault, Throwable {
        throw new OperationNotSupportedFaultException("Not implemented yet.");
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
    public EditLinkResponseType editLink(final EditLinkType request)
            throws SoapFault, Throwable {
        throw new OperationNotSupportedFaultException("Not implemented yet.");
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
    public GetDomainsResponseType getDomains(final GetDomainsType request)
            throws SoapFault, Throwable {
        throw new OperationNotSupportedFaultException("Not implemented yet.");
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
    public GetEndpointsResponseType getEndpoints(final GetEndpointsType request)
            throws SoapFault, Throwable {
        throw new OperationNotSupportedFaultException("Not implemented yet.");
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
    public GetLinksResponseType getLinks(final GetLinksType request)
            throws SoapFault, Throwable {
        throw new OperationNotSupportedFaultException("Not implemented yet.");
    }
}
