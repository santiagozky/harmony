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


package org.opennaas.ui.topology.ws;

import java.net.ConnectException;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.muse.ws.addressing.soap.SoapFault;
import org.w3c.dom.Element;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AddDomain;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AddDomainResponse;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AddDomainResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AddDomainType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AddEndpoint;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AddEndpointResponse;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AddEndpointType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AddLink;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AddLinkResponse;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AddLinkType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DeleteDomain;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DeleteDomainResponse;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DeleteDomainType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DeleteEndpoint;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DeleteEndpointResponse;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DeleteEndpointType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DeleteLink;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DeleteLinkResponse;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DeleteLinkResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DeleteLinkType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DomainInformationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EditDomain;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EditDomainResponse;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EditDomainType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EditEndpoint;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EditEndpointResponse;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EditEndpointType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EditLink;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EditLinkResponse;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EditLinkType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetDomains;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetDomainsResponse;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetDomainsResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetDomainsType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetEndpoints;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetEndpointsResponse;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetEndpointsResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetEndpointsType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetLinks;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetLinksResponse;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetLinksResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetLinksType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.Link;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ObjectFactory;
import org.opennaas.extensions.idb.serviceinterface.databinding.utils.JaxbSerializer;
import org.opennaas.extensions.idb.serviceinterface.topology.SimpleTopologyClient;
import org.opennaas.core.utils.Config;

/**
 * This class is used to put in contact the visual part of the client * with the
 * topology module of the NSP. It contains the WS Proxy and the methods to
 * get/delete/create/modify the domains, endpoints and links. It interacts with
 * the topology interface of the NSP.
 * 
 * @author Angel Sanchez (angel.sanchez@i2cat.net)
 */

public class WSProxy {

    /**
     * Proxy to invoke the WS operations.
     */
    private static SimpleTopologyClient proxy;

    /**
     * The logger.
     */
    private static final Logger logger = Logger.getLogger(WSProxy.class);

    /**
     * Serializer for JAXB objects.
     */
    private static JaxbSerializer jaxbSerializer;

    static {
        try {
            WSProxy.jaxbSerializer = JaxbSerializer.getInstance();
        } catch (final RuntimeException re) {
            re.printStackTrace();
        }
    }

    /**
     * Sends the domain creation message.
     * 
     * @param dom
     * 
     * @return Successful result or not
     * @throws ConnectException
     */
    public static boolean addDomain(final DomainInformationType dom)
            throws ConnectException {

        final ObjectFactory of = new ObjectFactory();

        final AddDomainType adddomtype = of.createAddDomainType();
        final AddDomain adddom = of.createAddDomain();
        adddomtype.setDomain(dom);
        adddom.setAddDomain(adddomtype);

        /* Response messages */
        AddDomainResponse domResp = of.createAddDomainResponse();
        AddDomainResponseType domRespType = of.createAddDomainResponseType();

        boolean res = true;

        WSProxy.logger.info("Adding Domain...");

        try {
            final Element e = WSProxy.getProxy().addDomain(
                    WSProxy.jaxbSerializer.objectToElement(adddom));
            domResp = (AddDomainResponse) WSProxy.jaxbSerializer
                    .elementToObject(e);
            domRespType = domResp.getAddDomainResponse();
            res = domRespType.isSuccess();

            WSProxy.logger.info("Domain added!");

        } catch (final SoapFault sf) {
            res = false;
            WSProxy.logger
                    .error("Couldn't add domain. A SoapFault occurred... ");
            sf.printStackTrace();
            throw new ConnectException(sf.getMessage());
        }

        return res;
    }

    /**
     * Sends the endpoint creation message.
     * 
     * @return Operation successful or not
     * @throws ConnectException
     */
    public static boolean addEndpoint(final EndpointType epinfo)
            throws ConnectException {

        final ObjectFactory of = new ObjectFactory();

        /* Messages creation */
        final AddEndpointType addEPtype = of.createAddEndpointType();
        final AddEndpoint addEP = of.createAddEndpoint();

        addEPtype.setEndpoint(epinfo);
        addEP.setAddEndpoint(addEPtype);

        /* Response messages */
        AddEndpointResponse epResp;

        WSProxy.logger.info("Adding Endpoint...");

        try {
            final Element e = WSProxy.getProxy().addEndpoint(
                    WSProxy.jaxbSerializer.objectToElement(addEP));
            epResp = (AddEndpointResponse) WSProxy.jaxbSerializer
                    .elementToObject(e);

            WSProxy.logger.info("Endpoint added!");

        } catch (final SoapFault sf) {
            WSProxy.logger
                    .error("Couldn't add endpoint(s). A SoapFault occurred... ");
            sf.printStackTrace();
            throw new ConnectException(sf.getMessage());
        }

        return epResp.getAddEndpointResponse().isSuccess();
    }

    /**
     * Sends the link creation message.
     * 
     * @return Operation successful or not
     * @throws ConnectException
     */
    public static boolean addLink(final Link linkToAdd) throws ConnectException {

        final ObjectFactory of = new ObjectFactory();

        /* Messages creation */
        final AddLinkType addLinktype = of.createAddLinkType();
        addLinktype.setLinkData(linkToAdd);
        final AddLink addLink = of.createAddLink();
        addLinktype.setLinkData(linkToAdd);
        addLink.setAddLink(addLinktype);

        /* Response messages */
        AddLinkResponse linkResp;

        WSProxy.logger.info("Adding link...");

        try {
            final Element e = WSProxy.getProxy().addLink(
                    WSProxy.jaxbSerializer.objectToElement(addLink));
            linkResp = (AddLinkResponse) WSProxy.jaxbSerializer
                    .elementToObject(e);

            WSProxy.logger.info("Link added!");

        } catch (final SoapFault sf) {
            WSProxy.logger.error("Couldn't add link. A SoapFault occurred... ");
            sf.printStackTrace();
            throw new ConnectException(sf.getMessage());
        }

        return linkResp.getAddLinkResponse().isSuccess();
    }

    /**
     * Sends the domain deletion message.
     * 
     * @param domaux
     * 
     * @return Successful result or not
     * @throws ConnectException
     */
    public static boolean deleteDomain(final DomainInformationType domaux)
            throws ConnectException {

        final ObjectFactory of = new ObjectFactory();

        final DeleteDomainType deldomtype = of.createDeleteDomainType();
        final DeleteDomain deldom = of.createDeleteDomain();

        deldomtype.setDomainId(domaux.getDomainId());
        deldom.setDeleteDomain(deldomtype);

        /* Response messages */
        DeleteDomainResponse domResp;

        WSProxy.logger.info("Deleting Domain...");

        boolean res = true;

        try {
            final Element e = WSProxy.getProxy().deleteDomain(
                    WSProxy.jaxbSerializer.objectToElement(deldom));
            domResp = (DeleteDomainResponse) WSProxy.jaxbSerializer
                    .elementToObject(e);
            res = domResp.getDeleteDomainResponse().isSuccess();

            WSProxy.logger.info("Domain deleted!");

        } catch (final SoapFault sf) {
            res = false;
            WSProxy.logger
                    .error("Couldn't delete domain. A SoapFault occurred... ");
            sf.printStackTrace();
            throw new ConnectException(sf.getMessage());
        }

        return res;
    }

    /**
     * Sends the endpoint deletion message.
     * 
     * @return Successful result or not
     * @throws ConnectException
     */

    public static boolean deleteEndpoint(final EndpointType epToDel)
            throws ConnectException {

        final ObjectFactory of = new ObjectFactory();

        /* Messages creation */
        final DeleteEndpointType delEPtype = of.createDeleteEndpointType();
        final DeleteEndpoint delEP = of.createDeleteEndpoint();
        delEPtype.setEndpoint(epToDel.getEndpointId());
        delEP.setDeleteEndpoint(delEPtype);

        /* Response messages */
        DeleteEndpointResponse epResp;

        WSProxy.logger.info("Deleting Endpoint with ID["
                + epToDel.getEndpointId() + "]...");

        try {
            final Element e = WSProxy.getProxy().deleteEndpoint(
                    WSProxy.jaxbSerializer.objectToElement(delEP));

            epResp = (DeleteEndpointResponse) WSProxy.jaxbSerializer
                    .elementToObject(e);

            WSProxy.logger.info("Endpoint deleted!");

        } catch (final SoapFault sf) {
            WSProxy.logger
                    .error("Couldn't delete domain. A SoapFault occurred... ");
            sf.printStackTrace();
            throw new ConnectException(sf.getMessage());
        }

        return epResp.getDeleteEndpointResponse().isSuccess();
    }

    /**
     * Sends the link deletion message.
     * 
     * @return Successful result or not
     * @throws ConnectException
     */
    public static boolean deleteLink(@SuppressWarnings("unused") final int linkID) throws ConnectException {

        final ObjectFactory of = new ObjectFactory();

        /* Messages creation */
        final DeleteLinkType delLinktype = of.createDeleteLinkType();
        final DeleteLink delLink = of.createDeleteLink();
        // delLinktype.setLinkId(linkID);
        delLink.setDeleteLink(delLinktype);

        /* Response messages */
        DeleteLinkResponse linkResp = of.createDeleteLinkResponse();
        DeleteLinkResponseType linkRespType = of.createDeleteLinkResponseType();

        WSProxy.logger.info("Deleting Link...");

        try {
            final Element e = WSProxy.getProxy().deleteLink(
                    WSProxy.jaxbSerializer.objectToElement(delLink));
            linkResp = (DeleteLinkResponse) WSProxy.jaxbSerializer
                    .elementToObject(e);

        } catch (final SoapFault sf) {
            WSProxy.logger
                    .error("Couldn't delete link. A SoapFault occurred... ");
            sf.printStackTrace();
            throw new ConnectException(sf.getMessage());
        }

        linkRespType = linkResp.getDeleteLinkResponse();
        final boolean res = linkRespType.isSuccess();

        WSProxy.logger.info("Link deleted!");
        return res;
    }

    /**
     * Sends the edit domain message.
     * 
     * @param domaux
     * 
     * @return Successful result or not
     * @throws ConnectException
     */
    public static boolean editDomain(final DomainInformationType domaux)
            throws ConnectException {

        final ObjectFactory of = new ObjectFactory();

        final EditDomainType editdomtype = of.createEditDomainType();
        final EditDomain editdom = of.createEditDomain();

        editdomtype.setDomain(domaux);
        editdom.setEditDomain(editdomtype);

        /* Response messages */
        EditDomainResponse domResp;

        WSProxy.logger.info("Updating Domain...");

        boolean res = true;

        try {
            final Element e = WSProxy.getProxy().editDomain(
                    WSProxy.jaxbSerializer.objectToElement(editdom));
            domResp = (EditDomainResponse) WSProxy.jaxbSerializer
                    .elementToObject(e);
            res = domResp.getEditDomainResponse().isSuccess();

            WSProxy.logger.info("Domain updated!");

        } catch (final SoapFault sf) {
            res = false;
            WSProxy.logger
                    .error("Couldn't edit domain. A SoapFault occurred... ");
            sf.printStackTrace();
            throw new ConnectException(sf.getMessage());
        }

        return res;
    }

    /**
     * Sends the edit endpoint message.
     * 
     * @return Successful result or not
     * @throws ConnectException
     */
    public static boolean editEndpoint(final EndpointType epToEdit)
            throws ConnectException {

        final ObjectFactory of = new ObjectFactory();

        /* Messages creation */
        final EditEndpointType editEPtype = of.createEditEndpointType();
        final EditEndpoint editEP = of.createEditEndpoint();
        editEPtype.setEndpoint(epToEdit);
        editEP.setEditEndpoint(editEPtype);

        /* Response messages */
        EditEndpointResponse epResp;

        WSProxy.logger.info("Updating Endpoint...");

        try {
            final Element e = WSProxy.getProxy().editEndpoint(
                    WSProxy.jaxbSerializer.objectToElement(editEP));
            epResp = (EditEndpointResponse) WSProxy.jaxbSerializer
                    .elementToObject(e);

            WSProxy.logger.info("Endpoint updated!");

        } catch (final SoapFault sf) {
            WSProxy.logger
                    .error("Couldn't edit endpoint. A SoapFault occurred... ");
            sf.printStackTrace();
            throw new ConnectException(sf.getMessage());
        }

        return epResp.getEditEndpointResponse().isSuccess();
    }

    /**
     * Sends the edit link message.
     * 
     * @return Successful result or not
     * @throws ConnectException
     */
    public static boolean editLink(final Link link) throws ConnectException {

        final ObjectFactory of = new ObjectFactory();

        /* Messages creation */
        final EditLinkType editLinktype = of.createEditLinkType();
        final EditLink editLink = of.createEditLink();
        editLinktype.setLink(link);
        editLink.setEditLink(editLinktype);

        /* Response messages */
        EditLinkResponse linkResp;

        WSProxy.logger.info("Updating Link...");

        try {
            final Element e = WSProxy.getProxy().editLink(
                    WSProxy.jaxbSerializer.objectToElement(editLink));
            linkResp = (EditLinkResponse) WSProxy.jaxbSerializer
                    .elementToObject(e);

            WSProxy.logger.info("Link updated!");

        } catch (final SoapFault sf) {
            WSProxy.logger
                    .error("Couldn't edit link. A SoapFault occurred... ");
            sf.printStackTrace();
            throw new ConnectException(sf.getMessage());
        }

        return linkResp.getEditLinkResponse().isResult();
    }

    /**
     * Gets all the domains stored in the NSP.
     * 
     * @return Hashtable filled with the objects for the View
     */
    public static Hashtable<String, DomainInformationType> getDomains()
            throws ConnectException {

        final Hashtable<String, DomainInformationType> domains = new Hashtable<String, DomainInformationType>();

        final ObjectFactory of = new ObjectFactory();

        final GetDomainsType getdomtype = of.createGetDomainsType();
        final GetDomains getdom = of.createGetDomains();
        getdom.setGetDomains(getdomtype);

        /* Response messages */
        GetDomainsResponse domResp = of.createGetDomainsResponse();
        GetDomainsResponseType domRespType = of.createGetDomainsResponseType();

        WSProxy.logger.debug("Proxy is getting Domains...");

        try {
            final Element e = WSProxy.getProxy().getDomains(
                    WSProxy.jaxbSerializer.objectToElement(getdom));
            domResp = (GetDomainsResponse) WSProxy.jaxbSerializer
                    .elementToObject(e);
            domRespType = domResp.getGetDomainsResponse();

            WSProxy.logger.debug("Got response from WS!");

            // res = domRespType.isResult();

        } catch (final SoapFault sf) {
            WSProxy.logger
                    .error("Couldn't get domains. A SoapFault occurred... "
                            + sf.getMessage());
            sf.printStackTrace();
            throw new ConnectException(sf.getMessage());
        }

        final List<DomainInformationType> domainsName = domRespType
                .getDomains();
        for (final DomainInformationType domInfo : domainsName) {
            domains.put(domInfo.getDomainId(), domInfo);
        }

        return domains;
    }

    /**
     * Gets the endpoints of the domain stored in the NSP.
     * 
     * @param Domain
     *            from which the endpoints will be retrieved
     * 
     * @return Hashtable filled with the objects for the View
     * @throws ConnectException
     */
    public static Hashtable<String, EndpointType> getEndpoints(
            final String domainName) throws ConnectException {

        final Hashtable<String, EndpointType> endpoints = new Hashtable<String, EndpointType>(
                1, 0.5F);
        final ObjectFactory of = new ObjectFactory();

        /* Messages creation */
        final GetEndpointsType getEPtype = of.createGetEndpointsType();
        final GetEndpoints getEP = of.createGetEndpoints();
        getEPtype.setDomain(domainName);
        getEP.setGetEndpoints(getEPtype);

        /* Response messages */
        GetEndpointsResponse epResp = of.createGetEndpointsResponse();
        GetEndpointsResponseType epRespType = of
                .createGetEndpointsResponseType();

        WSProxy.logger.info("Getting Endpoints...");

        try {
            final Element e = WSProxy.getProxy().getEndpoints(
                    WSProxy.jaxbSerializer.objectToElement(getEP));
            epResp = (GetEndpointsResponse) WSProxy.jaxbSerializer
                    .elementToObject(e);
        } catch (final SoapFault sf) {
            WSProxy.logger
                    .error("Couldn't get endpoints. A SoapFault occurred... ");
            sf.printStackTrace();
            throw new ConnectException(sf.getMessage());
        }

        epRespType = epResp.getGetEndpointsResponse();

        // final boolean res = epRespType.isResult();

        final List<EndpointType> eplist = epRespType.getEndpoints();
        for (final EndpointType epinfo : eplist) {
            endpoints.put(epinfo.getEndpointId(), epinfo);
        }

        WSProxy.logger.info("Got Endpoints!");

        return endpoints;
    }

    /**
     * Gets all the links stored in the NSP.
     * 
     * @return Hashtable filled with the objects for the View
     * @throws ConnectException
     */
    public static Hashtable<String, Link> getLinks(final String domain)
            throws ConnectException {

        final Hashtable<String, Link> links = new Hashtable<String, Link>(1,
                0.5F);
        final ObjectFactory of = new ObjectFactory();

        /* Messages creation */
        final GetLinksType getLinktype = of.createGetLinksType();
        final GetLinks getLink = of.createGetLinks();

        getLinktype.setDomainId(domain);
        getLink.setGetLinks(getLinktype);

        /* Response messages */
        GetLinksResponse linkResp = of.createGetLinksResponse();
        GetLinksResponseType linkRespType = of.createGetLinksResponseType();

        WSProxy.logger.info("Getting Links...");

        try {
            WSProxy.logger.debug("Sending getLinks request...");

            final Element e = WSProxy.getProxy().getLinks(
                    WSProxy.jaxbSerializer.objectToElement(getLink));
            linkResp = (GetLinksResponse) WSProxy.jaxbSerializer
                    .elementToObject(e);

        } catch (final SoapFault sf) {
            WSProxy.logger
                    .error("Couldn't get links. A SoapFault occurred... ");
            sf.printStackTrace();
            throw new ConnectException(sf.getMessage());
        }

        linkRespType = linkResp.getGetLinksResponse();

        // final boolean res = linkRespType.isResult();

        final List<Link> eplist = linkRespType.getLink();
        for (final Link linkinfo : eplist) {
            links.put(linkinfo.getName(), linkinfo);
        }

        WSProxy.logger.info("Got Links!");
        return links;
    }

    /**
     * This method provides the proxy required to invoke the WS operations.
     * 
     * @return The proxy implementation
     */
    private static SimpleTopologyClient getProxy() {
        if (WSProxy.proxy == null) {
            WSProxy.logger.debug("Getting proxy...");
            WSProxy.proxy = new SimpleTopologyClient(Config.getString(
                    "topologygui", "epr.topology"));

            WSProxy.logger.debug("Got proxy!");
            WSProxy.logger.trace("No exception after getting proxy");
        }
        WSProxy.logger.trace("Returning proxy");
        return WSProxy.proxy;
    }
}
