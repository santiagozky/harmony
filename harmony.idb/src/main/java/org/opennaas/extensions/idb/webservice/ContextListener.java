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


package org.opennaas.extensions.idb.webservice;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DomainInformationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DomainRelationshipType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointInterfaceType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.InterdomainLinkType;
import org.opennaas.extensions.idb.serviceinterface.topology.registrator.AbstractTopologyRegistrator;
import org.opennaas.core.utils.PhLogger;
import org.opennaas.extensions.idb.Constants;
import org.opennaas.extensions.idb.database.hibernate.Domain;
import org.opennaas.extensions.idb.database.hibernate.Endpoint;
import org.opennaas.extensions.idb.database.hibernate.InterDomainLink;
import org.opennaas.extensions.idb.database.hibernate.TNAPrefix;
import org.opennaas.extensions.idb.exception.database.DatabaseException;
import org.opennaas.extensions.idb.topology.handler.TopologyRequestHandler;

public class ContextListener extends AbstractTopologyRegistrator {
    public static final String interdomainPropertyFile = Constants.hsiProperties;
    private List<TNAPrefix> configuredPrefixes;

    public ContextListener() {
        super();
        if (this.myTNAPrefixes != null) {
        	final Domain me = Domain.fromJaxb(this.getDomainInformation());
            this.configuredPrefixes = new ArrayList<TNAPrefix>(
                    this.myTNAPrefixes.size());
            for (final String s : this.myTNAPrefixes) {
                this.configuredPrefixes
                        .add(new TNAPrefix(me, s));
            }
        } else {
            this.configuredPrefixes = null;
        }
    }
    
    @Override
    public final List<String> getTNAPrefixList() {
    	if (this.domain == null) {
    		updateDomain();
    	}
        return this.domain.getTNAPrefix();
    }

    @Override
    public final String getDomainId() {
    	if (this.domain == null) {
    		updateDomain();
    	}
        return this.domain.getDomainId();
    }

    @Override
    protected DomainInformationType getDomainInformation() {
        final Logger log = this.getLogger();

        final DomainInformationType domInfo = super.getDomainInformation();

        try {
            // add TNA prefixes of subdomain
            final Set<String> subdomainNames = new HashSet<String>(); // used
                                                                        // to
                                                                        // check
                                                                        // which
                                                                        // interdomain
                                                                        // links
                                                                        // to
                                                                        // export
            for (final Domain d : Domain.loadAll()) {
                final String relationship = d.getRelationship();
                if ((relationship != null)
                        && relationship.equals(DomainRelationshipType.SUBDOMAIN
                                .value())) {
                    subdomainNames.add(d.getName());
                    for (final TNAPrefix p : d.getPrefixes()) {
                        boolean propagate = true;
                        if (this.configuredPrefixes != null) {
                            for (final TNAPrefix p2 : this.configuredPrefixes) {
                                if (p2.containsPrefix(p)) {
                                    propagate = false;
                                    break;
                                }
                            }
                        }
                        if (propagate) {
                            domInfo.getTNAPrefix().add(p.getPrefix());
                        }
                    }
                }
            }
            // add interdomain links reported by subdomains iff the dest domain
            // is not also a subdomain of ours
            for (final InterDomainLink i : InterDomainLink.loadAll()) {
                final String srcDomain = i.getSourceDomain();
                final String dstDomain = i.getDestinationDomain();
                if ((!subdomainNames.contains(dstDomain))
                        && subdomainNames.contains(srcDomain)) {
                    if (log != null) {
                        log.debug("add interdomain link, srcDomain="
                                + srcDomain + " dstDomain=" + dstDomain);
                    }
                    final InterdomainLinkType iJaxb = i.toJaxb();
                    domInfo.getInterdomainLink().add(iJaxb);
                }
            }
        } catch (final DatabaseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return domInfo;
    }

    @Override
    protected List<EndpointType> getEndpoints() {
        final Logger log = this.getLogger();
        final Set<String> haveTNAs = new HashSet<String>();
        try {
            final List<EndpointType> result = new LinkedList<EndpointType>();
            for (final Endpoint ep : Endpoint.loadAll()) {
                result.add(ep.toJaxb());
                haveTNAs.add(ep.getTNA());
            }
            for (final InterdomainLinkType i : this.myInterdomainLinks) {
                final String iTNA = i.getSourceEndpoint().getEndpointId();
                if (log != null) {
                    log.debug("getEndpoints() check tna=" + iTNA);
                }
                if (!haveTNAs.contains(iTNA)) {
                    final Domain dom = Domain.getDomainMatchingTNA(iTNA);
                    if (log != null) {
                        log.debug("getEndpoints() check domain="
                                + ((dom == null) ? "<none>" : dom.getName()));
                    }
                    if ((dom != null)
                            && (dom.getRelationship()
                                    .equals(DomainRelationshipType.SUBDOMAIN
                                            .value()))) {
                        final EndpointType ep = new EndpointType();
                        ep.setEndpointId(iTNA);
                        ep.setDomainId(dom.getName());
                        ep.setInterface(EndpointInterfaceType.BORDER);
                        result.add(ep);
                        haveTNAs.add(iTNA);
                    }
                }
            }
            return result;
        } catch (final DatabaseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected String getInterdomainPropertyFile() {
        return ContextListener.interdomainPropertyFile;
    }

    @Override
    protected Logger getLogger() {
        return PhLogger.getLogger(ContextListener.class);
    }

    private boolean saveDomain() {
        DomainInformationType d = null;
        try {
            d = this.domain.clone();
            d.unsetTNAPrefix();
            return TopologyRequestHandler.getInstance().addOrEditDomain(d,
                    false);
        } catch (final CloneNotSupportedException e) {
            // this is impossible, clone support is automatically added to the
            // JAXB classes
            e.printStackTrace();
        } catch (final DatabaseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void shutdown() {
        // nothing
    }

    @Override
    protected boolean startup() {
        Domain me = null;
        try {
            me = Domain.load(this.myDomainName);
        } catch (final DatabaseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (me != null) {
            this.mySequenceNumber = me.getSeqNo();
        }
        return true;
    }

    @Override
    protected synchronized boolean updateDomain() {
        final boolean updated = super.updateDomain();
        if (updated) {
            this.saveDomain();
        }
        return updated;
    }
}
