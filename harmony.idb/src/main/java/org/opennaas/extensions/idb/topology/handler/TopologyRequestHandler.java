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


package org.opennaas.extensions.idb.topology.handler;

import java.util.Set;

import org.apache.log4j.Logger;

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
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DomainRelationshipType;
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
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.InterdomainLinkType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.DomainAlreadyExistsFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.DomainNotFoundFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.EndpointAlreadyExistsFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.InvalidRequestFaultException;
import org.opennaas.extensions.idb.serviceinterface.topology.CommonTopologyHandler;
import org.opennaas.extensions.idb.serviceinterface.topology.registrator.AbstractTopologyRegistrator;
import org.opennaas.core.utils.Config;
import org.opennaas.core.utils.PhLogger;
import org.opennaas.extensions.idb.Constants;
import org.opennaas.extensions.idb.database.hibernate.Domain;
import org.opennaas.extensions.idb.database.hibernate.Endpoint;
import org.opennaas.extensions.idb.database.hibernate.InterDomainLink;
import org.opennaas.extensions.idb.exception.database.DatabaseException;

/** Topology Request Handler. */
public final class TopologyRequestHandler extends CommonTopologyHandler {
    /** Singleton Instance. */
    private static TopologyRequestHandler selfInstance = null;

    /**
     * Instance getter.
     * 
     * @return Singleton Instance
     */
    public static synchronized TopologyRequestHandler getInstance() {
        if (TopologyRequestHandler.selfInstance == null) {
            TopologyRequestHandler.selfInstance = new TopologyRequestHandler();
        }
        return TopologyRequestHandler.selfInstance;
    }

    private String myDomainName = null;

    private final Logger log = PhLogger.getLogger(TopologyRequestHandler.class);

    /**
     * Private constructor: Singleton.
     * 
     * @throws DatabaseException
     */
    public TopologyRequestHandler() {
        this.myDomainName = Config.getString(Constants.hsiProperties, "domain.name");
    }

    /**
     * AddDomain Handler.
     * 
     * @param element
     *            AddDomain Request
     * @return AddDomain Response
     * @throws DatabaseException
     * @throws DomainAlreadyExistsFaultException
     */
    @Override
    public AddDomainResponseType addDomain(final AddDomainType element)
            throws DatabaseException, DomainAlreadyExistsFaultException {

        final DomainInformationType domInfo = element.getDomain();
        if (!domInfo.isSetRelationship()) {
            domInfo.setRelationship(DomainRelationshipType.SUBDOMAIN);
        }
        final String domainName = domInfo.getDomainId();
        final Domain dom = Domain.load(domainName);
        // play it safe: the get method can either return "null" or "new
        // Domain()"
        if ((dom != null) && (domainName.equals(dom.getName()))) {
            throw new DomainAlreadyExistsFaultException(domainName);
        }

        final AddDomainResponseType responseType = new AddDomainResponseType();
        responseType.setSuccess(this.addOrEditDomain(domInfo, false));

        return responseType;
    }

    /*
     * Handler
     * =========================================================================
     */

    /**
     * AddEndpoint Handler.
     * 
     * @param element
     *            AddEndpoints Request
     * @return AddEndpoints Response
     * @throws DatabaseException
     * @throws DomainNotFoundFaultException
     * @throws InvalidRequestFaultException
     * @throws EndpointAlreadyExistsFaultException
     */
    @Override
    public AddEndpointResponseType addEndpoint(final AddEndpointType element)
            throws DatabaseException, DomainNotFoundFaultException,
            InvalidRequestFaultException, EndpointAlreadyExistsFaultException {

        final AddEndpointResponseType responseType = new AddEndpointResponseType();
        responseType.setSuccess(false);

        try {

            final EndpointType epJaxb = element.getEndpoint();

            final Endpoint previousEp = Endpoint.load(epJaxb.getEndpointId());
            if (previousEp != null) {
                throw new EndpointAlreadyExistsFaultException(epJaxb
                        .getEndpointId());
            }

            final String domainName = epJaxb.getDomainId();
            if (domainName == null) {
                throw new InvalidRequestFaultException(
                        "Uidbecified domain ID in endpoint to be added");
            }
            final Domain dom = Domain.load(domainName);
            if (dom == null) {
                throw new DomainNotFoundFaultException("Cannot find domain: "
                        + domainName);
            }

            final EndpointInterfaceType itf = epJaxb.getInterface();
            if (itf == null) {
                throw new InvalidRequestFaultException(
                        "Uidbecified interface type in endpoint to be added");
            }
            final Endpoint epDb = new Endpoint(epJaxb.getEndpointId(), dom, itf
                    .ordinal());
            if (epJaxb.isSetName()) {
                epDb.setName(epJaxb.getName());
            }
            if (epJaxb.isSetDescription()) {
                epDb.setDescription(epJaxb.getDescription());
            }
            if (epJaxb.isSetBandwidth()) {
                epDb.setBandwidth(epJaxb.getBandwidth().intValue());
            }
            epDb.save();
            responseType.setSuccess(true);

        } catch (final DatabaseException e) {

            throw new DatabaseException(
                    "Database exception when adding Endpoints: "
                            + e.getMessage());

        }
        return responseType;
    }

    /**
     * AddLink Handler.
     * 
     * @param element
     *            AddLink Request
     * @return AddLink Response
     * @throws DatabaseException
     */
    @Override
    public AddLinkResponseType addLink(final AddLinkType element) {
        throw new RuntimeException("operation not supported");
    }

    public boolean addOrEditDomain(final DomainInformationType domInfo,
            final boolean checkSeq) throws DatabaseException {
        final String domainName = domInfo.getDomainId();
        Domain dom = Domain.load(domainName);

        
        if(dom == null) {
        	// the domain is not yet saved in DB, so there is nothing to check
        	dom = Domain.fromJaxb(domInfo);
        } else {
	        if (checkSeq
	                && this.rejectDomainInformation(dom, Domain.fromJaxb(domInfo))) {
	            return false;
	        }
        	dom.mergeFromJaxb(domInfo);
        }

        boolean isNeighbor = false;

        // failure resilience
        for (final InterdomainLinkType l : domInfo.getInterdomainLink()) {
            final EndpointType srcEP = l.getSourceEndpoint();
            if (!srcEP.isSetDomainId()) {
                this.log.warn("advertised endpoint " + srcEP.getEndpointId()
                        + " does not have its domain ID field set");
                srcEP.setDomainId(domainName);
            }
            if ((!srcEP.isSetInterface())
                    || (srcEP.getInterface() != EndpointInterfaceType.BORDER)) {
                this.log.warn("advertised endpoint " + srcEP.getEndpointId()
                        + " is not declared as border endpoint");
                srcEP.setInterface(EndpointInterfaceType.BORDER);
            }
        }

        try {
        	dom.save();

            final Set<InterDomainLink> oldLinks = InterDomainLink
                    .loadForSourceDomain(domainName);

            for (final InterdomainLinkType link : domInfo.getInterdomainLink()) {
                final int linkCosts = link.isSetCosts() ? link.getCosts()
                        .intValue() : 0;
                if (link.getDestinationDomain().equals(this.myDomainName)) {
                    isNeighbor = true;
                }
                final InterDomainLink newLink = InterDomainLink.fromJaxb(link,
                        dom.getName());

                // search if new link is already in DB (exact match without PK)
                InterDomainLink oldLink = null;
                for (final InterDomainLink t : oldLinks) {
                	if(t.equalsWithoutPK(newLink)) {
                		oldLink = t;
                	}
                }

                if(oldLink != null) {
                	// new link is already in DB and no attributes have changed
                	// so nothing to do
                	oldLinks.remove(oldLink);
                } else {
                	// new link is not in DB or attributes have changed 

                	// at first delete links in the old list, they are now obsolete
                    for (final InterDomainLink l : oldLinks) {
                        this.log.debug("deleting obsolete link ["
                                + l.toString()
                                + "] from interdomain link table");
                        l.delete();
                    }

                    // save new link in DB
                    this.log.debug("updating [src="
                            + newLink.getSourceEndpoint().getTNA() + " dst="
                            + newLink.getDestinationDomain()
                            + "] in interdomain link table");
		            // save source endpoint if necessary
		            Endpoint src = Endpoint.load(newLink.getSourceEndpoint().getTNA());
		            if(src == null) {
		            	System.out.println("*** inserting endpoint");
		            	src = newLink.getSourceEndpoint();
		            	src.save();
		            }
		            newLink.setSourceEndpoint(src);
		            // save interDomainLink
		            newLink.save();
                }
            }
        } catch (final DatabaseException e) {
            throw new DatabaseException(
                    "Database exception when editing a domain", e);
        }
        if (domInfo.isSetRelationship()
                && domInfo.getRelationship()
                        .equals(DomainRelationshipType.PEER)) {
            final AbstractTopologyRegistrator t = AbstractTopologyRegistrator
                    .getLatestInstance();
            if (t != null) {
                t.addPeerDomain(domInfo, isNeighbor);
            }
        }
        return true;
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
     * 
     * @param element
     *            DeleteDomain Request
     * @return DeleteDomain Response
     * @throws DatabaseException
     * @throws DomainNotFoundFaultException
     */
    @Override
    public DeleteDomainResponseType deleteDomain(final DeleteDomainType element)
            throws DatabaseException, DomainNotFoundFaultException {

        final DeleteDomainResponseType responseType = new DeleteDomainResponseType();
        responseType.setSuccess(false);
        final Domain dom = Domain.load(element.getDomainId());
        if (dom == null) {
            throw new DomainNotFoundFaultException("Cannot find domain: "
                    + element.getDomainId());
        }
        dom.delete();
        responseType.setSuccess(true);
        return responseType;
    }

    /**
     * DeleteEndpoints Handler.
     * 
     * @param element
     *            DeleteEndpoints Request
     * @return DeleteEndpoints Response
     * @throws DatabaseException
     */
    @Override
    public DeleteEndpointResponseType deleteEndpoint(
            final DeleteEndpointType element) throws DatabaseException {

        final String id = element.getEndpoint();

        final DeleteEndpointResponseType responseType = new DeleteEndpointResponseType();

        final Endpoint ep = Endpoint.load(id);
        if (ep != null) {
            ep.delete();
        }

        responseType.setSuccess(true);

        return responseType;
    }

    /**
     * DeleteLink Handler.
     * 
     * @param element
     *            DeleteLink Request
     * @return DeleteLink Response
     * @throws DatabaseException
     */
    @Override
    public DeleteLinkResponseType deleteLink(final DeleteLinkType element) {
        throw new RuntimeException("operation not supported");
    }

    /**
     * EditDomain Handler.
     * 
     * @param element
     *            EditDomain Request
     * @return EditDomain Response
     * @throws DatabaseException
     * @throws DomainNotFoundFaultException
     */
    @Override
    public EditDomainResponseType editDomain(final EditDomainType element)
            throws DatabaseException {

        final DomainInformationType domInfo = element.getDomain();
        if (!domInfo.isSetRelationship()) {
            domInfo.setRelationship(DomainRelationshipType.SUBDOMAIN);
        }

        final EditDomainResponseType responseType = new EditDomainResponseType();
        responseType.setSuccess(this.addOrEditDomain(domInfo, true));
        return responseType;
    }

    /**
     * EditEndpoints Handler.
     * 
     * @param element
     *            EditEndpoints Request
     * @return EditEndpoints Response
     * @throws DatabaseException
     */
    @Override
    public EditEndpointResponseType editEndpoint(final EditEndpointType element)
            throws DatabaseException {

        final EndpointType epType = element.getEndpoint();

        final EditEndpointResponseType responseType = new EditEndpointResponseType();

        Endpoint ep = Endpoint.load(epType.getEndpointId());

//        ep = Endpoint.fromJaxb(epType);
        ep.mergeFromJaxb(epType);

        try {
            ep.save();

            responseType.setSuccess(true);
        } catch (final DatabaseException e) {

            responseType.setSuccess(false);
            throw new DatabaseException(
                    "Database exception when adding Endpoints: "
                            + e.getMessage());
        }

        return responseType;
    }

    /**
     * EditLink Handler.
     * 
     * @param element
     *            EditLink Request
     * @return EditLink Response
     * @throws DatabaseException
     */
    @Override
    public EditLinkResponseType editLink(final EditLinkType element) {
        throw new RuntimeException("operation not supported");
    }

    /**
     * GetDomains Handler.
     * 
     * @param element
     *            GetDomains Request
     * @return GetDomains Response
     * @throws DatabaseException
     *             If there was a problem accessing the database.
     */
    @Override
    public GetDomainsResponseType getDomains(final GetDomainsType element)
            throws DatabaseException {

        final GetDomainsResponseType responseType = new GetDomainsResponseType();
        for (final Domain aux : Domain.loadAll()) {
            final DomainInformationType dom = aux.toJaxb();
            final Set<InterDomainLink> ll = InterDomainLink
                    .loadForSourceDomain(dom.getDomainId());
            for (final InterDomainLink l : ll) {
                dom.getInterdomainLink().add(l.toJaxb());
            }
            responseType.getDomains().add(dom);
        }
        return responseType;
    }

    /**
     * GetEndpoints Handler.
     * 
     * @param element
     *            GetEndpoints Request
     * @return GetEndpoints Response
     * @throws DatabaseException
     */
    @Override
    public GetEndpointsResponseType getEndpoints(final GetEndpointsType element)
            throws DatabaseException {

        final GetEndpointsResponseType responseType = new GetEndpointsResponseType();
        final String domainName = element.getDomain();
        final Domain domainList = Domain.load(domainName);
        if (domainList == null) {
            throw new DatabaseException("Could not load Domain: " + domainName);
        }
        for (final Endpoint aux : domainList.getEndpoints()) {
            if (EndpointInterfaceType.values()[aux.getType()] == EndpointInterfaceType.BORDER) {
                final EndpointType epInfo = new EndpointType();
                epInfo.setEndpointId(aux.getTNA());
                epInfo.setInterface(EndpointInterfaceType.values()[aux
                        .getType()]);
                epInfo.setBandwidth(Integer.valueOf(aux.getBandwidth()));
                epInfo.setDescription(aux.getDescription());
                epInfo.setName(aux.getName());
                epInfo.setDomainId(domainName);
                responseType.getEndpoints().add(epInfo);
            }
        }
        return responseType;
    }

    /*
     * Helper methods
     * =========================================================================
     */

    /**
     * GetLinks Handler.
     * 
     * @param element
     *            GetLinks Request
     * @return GetLinks Response
     * @throws DatabaseException
     */
    @Override
    public GetLinksResponseType getLinks(final GetLinksType element) {
        throw new RuntimeException("operation not supported");
    }

    private boolean rejectDomainInformation(final Domain domSaved,
            final Domain dom) {
        int oSeq = 0;
        long oReg = 0l;
        if (domSaved != null) {
            oSeq = domSaved.getSeqNo();
            oReg = domSaved.getRegistered().getTime();
        }
        return ((dom.getSeqNo() <= oSeq) && (dom.getRegistered().getTime()
                - oReg < 900000l)); // 15
        // minutes
    }
}
