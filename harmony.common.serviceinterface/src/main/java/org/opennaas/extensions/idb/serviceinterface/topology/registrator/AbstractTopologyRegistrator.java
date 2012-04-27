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

package org.opennaas.extensions.idb.serviceinterface.topology.registrator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DomainInformationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DomainRelationshipType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.InterdomainLinkType;
import org.opennaas.core.utils.Config;

/**
 * This class provides base functionality for a domain to exchange topology
 * information with other domains. The most basic use case is to derive an NRPS
 * adapter that registers with an NSP Topology-WS. To achieve this, several
 * abstract methods need to be implemented. Furthermore, subclasses may want to
 * override the getDomainInformation() method. Domain data is read from a
 * property file.
 */

public abstract class AbstractTopologyRegistrator implements
        ServletContextListener {
    /*
     * static stuff
     */

    /**
     * Though there should be only one AbstractTopologyRegistrator object, this
     * class is not actually designed as a singleton. This variable holds the
     * last (and hopefully only) instance that was created.
     */
    static AbstractTopologyRegistrator instance = null;

    /**
     * Though there should be only one AbstractTopologyRegistrator object, this
     * class is not actually designed as a singleton. This method returns the
     * last (and hopefully only) instance that was created.
     * 
     * @return Last (and hopefully only) instance of this class that was
     *         created.
     */
    public static AbstractTopologyRegistrator getLatestInstance() {
        return AbstractTopologyRegistrator.instance;
    }

    /*
     * fields
     */

    /** Indicates whether this registrator is active or not. */
    protected boolean active = false;

    /** Information about the own domain. */
    protected DomainInformationType domain = null;

    protected final String myDomainName;

    /** Sequence number of the information stored in the domain field. */
    protected int mySequenceNumber = 0;

    /** List of endpoints in the own domain. */
    protected HashMap<String, EndpointType> myEndpoints = null;

    /** List of TNA prefixes linked to the own domain. */
    protected List<String> myTNAPrefixes = null;

    /**
     * Interdomain links of the own domain. The EndpointType objects contained
     * here only have their TNA field set, so additional information has to be
     * retrieved from myEndpoints to create the final interdomain link list.
     */
    protected List<InterdomainLinkType> myInterdomainLinks = new LinkedList<InterdomainLinkType>();

    /**
     * Do not update information about the own domain before date given here --
     * prevents querying underlying systems too often.
     */
    protected Calendar nextUpdate = null;

    /** List of all peer domains. */
    protected HashMap<String, DomainInformationType> peerDomains = new HashMap<String, DomainInformationType>();

    /**
     * Distributor that sends domain information to the superdomain, in case
     * such a domain exists.
     */
    protected Distributor superDomainDistributor = null;

    /** Distributors that send domain information to peer domains. */
    protected HashMap<String, Distributor> peerDomainDistributors = new HashMap<String, Distributor>();

    protected ServletContext servletContext = null;

    /*
     * abstract methods
     */

    /** Constructor. */
    public AbstractTopologyRegistrator() {
        AbstractTopologyRegistrator.instance = this;

        // get domain name

        this.myDomainName = Config.getString(this.getInterdomainPropertyFile(),
                "domain.name");

        // get TNA prefixes

        String npString = null;
        try {
            npString = Config.getString(this.getInterdomainPropertyFile(),
                    "domain.numTNAPrefixes");
        } catch (final MissingResourceException e) {
            // nothing
        }
        if (npString != null) {
            final int numTNAPrefixes = Integer.parseInt(npString);
            this.myTNAPrefixes = new ArrayList<String>(numTNAPrefixes);
            for (int prefixNum = 0; prefixNum < numTNAPrefixes; prefixNum++) {
                this.myTNAPrefixes.add(Config.getString(this
                        .getInterdomainPropertyFile(), "domain.TNAPrefix"
                        + prefixNum));
            }
        }

        // get peer information

        npString = null;
        try {
            npString = Config.getString(this.getInterdomainPropertyFile(),
                    "numPeers");
        } catch (final MissingResourceException e) {
            return;
        }
        final int numPeers = Integer.parseInt(npString);
        for (int peerNum = 0; peerNum < numPeers; peerNum++) {
            final String peer = "peer" + peerNum;
            final String domainName = Config.getString(this
                    .getInterdomainPropertyFile(), peer + ".name");
            try {
                final String topologyEPR = Config.getString(this
                        .getInterdomainPropertyFile(), peer + ".topologyEPR");
                final DomainInformationType domain = new DomainInformationType();
                domain.setTopologyEPR(topologyEPR);
                domain.setDomainId(domainName);
                this.peerDomainDistributors.put(domain.getDomainId(), this
                        .createDistributor(domain));
            } catch (final MissingResourceException e) {
                // don't care -- this entry is not meant for peering
            }
            String nlString = null;
            try {
                nlString = Config.getString(this.getInterdomainPropertyFile(),
                        peer + ".numLinks");
            } catch (final MissingResourceException e) {
                // don't care
            }
            if (nlString != null) {
                final int numLinks = Integer.parseInt(nlString);
                for (int linkNum = 0; linkNum < numLinks; linkNum++) {
                    final String link = peer + ".link" + linkNum;
                    final InterdomainLinkType i = new InterdomainLinkType();
                    final EndpointType ep = new EndpointType();
                    ep.setEndpointId(Config.getString(this
                            .getInterdomainPropertyFile(), link + ".srcEP"));
                    i.setSourceEndpoint(ep);
                    i.setDestinationDomain(domainName);
                    i.setLinkID(Config.getString(this
                            .getInterdomainPropertyFile(), link + ".name"));
                    this.myInterdomainLinks.add(i);
                }
            }
        }
    }

    /**
     * Add information about a peer domain.
     * 
     * @param peerDomain
     *            Peer domain information.
     * @param isNeighbor
     *            If true, topology information is distributed to the peer
     *            domain's Topology-WS.
     */
    public void addPeerDomain(final DomainInformationType peerDomain,
            final boolean isNeighbor) {
        this.getPeerDomains().put(peerDomain.getDomainId(), peerDomain);

        for (final Entry<String, Distributor> e : this.peerDomainDistributors
                .entrySet()) {
            final String dName = e.getKey();
            if (!dName.equals(peerDomain.getDomainId())) {
                e.getValue().addDomain(peerDomain);
            }
        }
    }

    /**
     * @return All known TNA prefixes (own ones and those of peer domains).
     */
    public List<String> allTNAPrefixes() {
        final List<String> result = new LinkedList<String>();
        if ((this.domain != null) && (this.domain.getTNAPrefix() != null)) {
            for (final String s : this.domain.getTNAPrefix()) {
                result.add(s);
            }
        }
        for (final DomainInformationType domInfo : this.getPeerDomains()
                .values()) {
            if (domInfo.getTNAPrefix() != null) {
                for (final String s : domInfo.getTNAPrefix()) {
                    result.add(s);
                }
            }
        }
        return result;
    }

    public synchronized void contextDestroyed(final ServletContextEvent arg0) {
        if (this.active) {
            this.shutdown();
            this.active = false;
            if (this.superDomainDistributor != null) {
                this.superDomainDistributor.stop();
                this.superDomainDistributor = null;
            }
            for (final Distributor dist : this.peerDomainDistributors.values()) {
                dist.stop();
            }
            this.peerDomainDistributors = new HashMap<String, Distributor>();
            this.peerDomains = new HashMap<String, DomainInformationType>();
        }
        this.servletContext = null;
    }

    public synchronized void contextInitialized(final ServletContextEvent arg0) {
        this.servletContext = arg0.getServletContext();
        this
                .servletLog("AbstractTopoplogyRegistrator: servlet context initialized.");

        for (final Distributor dist : this.peerDomainDistributors.values()) {
            dist.start();
        }

        String superDomainTopologyEPR = null;
        try {
            superDomainTopologyEPR = Config.getString(this
                    .getInterdomainPropertyFile(), "parent.topologyEPR");
            if (!superDomainTopologyEPR.startsWith("http://")) {
                superDomainTopologyEPR = null;
            }
        } catch (final MissingResourceException e) {
            // don't care
        }

        final Logger log = this.getLogger();

        if (log != null) {
            log.debug("AbstractTopologyRegistrator: Superdomain EPR = "
                    + superDomainTopologyEPR);
        }

        this.active = true;
        if (this.startup()) {

            if (log != null) {
                log.debug("AbstractTopologyRegistrator: startup succeeded");
            }

            if (superDomainTopologyEPR != null) {

                if (log != null) {
                    log
                            .debug("AbstractTopologyRegistrator: creating superDomainDistributor object");
                }

                this.superDomainDistributor = new Distributor(
                        "SuperDomainDistributor", this.getLogger(), null,
                        superDomainTopologyEPR, null) {
                    @Override
                    protected synchronized void update() {
                        AbstractTopologyRegistrator.this.updateDomain();
                        DomainInformationType domain;
                        try {
                            domain = AbstractTopologyRegistrator.this.domain
                                    .clone();
                            domain
                                    .setRelationship(DomainRelationshipType.SUBDOMAIN);
                            for (final InterdomainLinkType i : domain
                                    .getInterdomainLink()) {
                                i.getSourceEndpoint().setDomainId(
                                        domain.getDomainId());
                            }
                            this.addDomain(domain);
                        } catch (final CloneNotSupportedException e) {
                            System.err
                                    .println("This should not happen: clone support automatically added to JAXB classes!");
                            e.printStackTrace();
                        }
                    }
                };
                this.superDomainDistributor.start();
            }
        } else {
            if (log != null) {
                log.debug("AbstractTopologyRegistrator: startup failed");
            }
            this.active = false;
        }
    }

    private Distributor createDistributor(final DomainInformationType peerDomain) {
        return new Distributor("PeerDistributor@" + peerDomain.getDomainId(),
                this.getLogger(), peerDomain.getDomainId(), peerDomain
                        .getTopologyEPR(), null) {
            @Override
            protected void update() {
                AbstractTopologyRegistrator.this.updateDomain();
                DomainInformationType domain;
                try {
                    domain = AbstractTopologyRegistrator.this.domain.clone();
                    domain.setRelationship(DomainRelationshipType.PEER);
                    for (final InterdomainLinkType i : domain
                            .getInterdomainLink()) {
                        i.getSourceEndpoint().setDomainId(domain.getDomainId());
                    }
                    this.addDomain(domain);
                    // other domains are added in the addPeerDomain() method
                } catch (final CloneNotSupportedException e) {
                    System.err
                            .println("This should not happen: clone support automatically added to JAXB classes!");
                    e.printStackTrace();
                }
            }
        };
    }

    /**
     * @return Own Domain Id
     */
    public String getDomainId() {
        return this.myDomainName;
    }

    /**
     * This method compiles the information in the interdomain property file to
     * a DomainInformationType.
     * 
     * @return Information about the own domain compiled from the interdomain
     *         property file.
     */
    protected DomainInformationType getDomainInformation() {
        final DomainInformationType domInfo = new DomainInformationType();

        domInfo.setDomainId(this.myDomainName);
        domInfo.setDescription(Config.getString(this
                .getInterdomainPropertyFile(), "domain.description"));
        domInfo.setReservationEPR(Config.getString(this
                .getInterdomainPropertyFile(), "domain.reservationEPR"));
        try {
            domInfo.setTopologyEPR(Config.getString(this
                    .getInterdomainPropertyFile(), "domain.topologyEPR"));
        } catch (final MissingResourceException e) {
            // don't care
        }
        try {
            domInfo.setNotificationEPR(Config.getString(this
                    .getInterdomainPropertyFile(), "domain.notificationEPR"));
        } catch (final MissingResourceException e) {
            // don't care
        }

        if (this.myTNAPrefixes != null) {
            for (final String prefix : this.myTNAPrefixes) {
                domInfo.getTNAPrefix().add(prefix);
            }
        }

        return domInfo;
    }

    /**
     * This method returns a list of endpoints located in the own domain.
     * 
     * @return List of endpoints located in the own domain.
     */
    protected abstract List<EndpointType> getEndpoints();

    /*
     * methods
     */

    /**
     * This method returns the name of the interdomain property file used to get
     * domain information.
     * 
     * @return Name of the interdomain property file to use.
     */
    protected abstract String getInterdomainPropertyFile();

    /**
     * This method returns null, but subclasses may override it to enable
     * logging.
     * 
     * @return Logger instance.
     */
    protected Logger getLogger() {
        return null;
    }

    protected synchronized HashMap<String, DomainInformationType> getPeerDomains() {
        return this.peerDomains;
    }

    /**
     * @return
     */
    public String getPropertyFile() {
        return this.getInterdomainPropertyFile();
    }

    public ServletContext getServletContext() {
        return this.servletContext;
    }

    /**
     * @return TNA prefixes the own domain is responsible for.
     */
    public List<String> getTNAPrefixList() {
        return this.myTNAPrefixes;
    }

    public boolean hasSuperdomain() {
        return this.superDomainDistributor != null;
    }

    /**
     * Remove a peer domain.
     * 
     * @param domainName
     *            Name of the domain to remove.
     */
    public synchronized void removePeerDomain(final String domainName) {
        this.peerDomains.remove(domainName);
        this.peerDomainDistributors.remove(domainName);
    }

    protected void servletLog(final String msg) {
        if (this.servletContext != null) {
            this.servletContext.log(msg);
        }
    }

    /**
     * The shutdown method is called at shutdown and allows subclasses to clean
     * things up.
     */
    protected abstract void shutdown();

    /**
     * The startup method is called at startup and allows subclasses e.g. to
     * establish communication to their subsystems.
     */
    protected abstract boolean startup();

    /**
     * Update the information about the own domain using the
     * getDomainInformation() and getEndpoints() methods.
     */
    protected synchronized boolean updateDomain() {
        final Logger log = this.getLogger();
        final Calendar now = Calendar.getInstance();
        if ((this.nextUpdate != null) && (now.compareTo(this.nextUpdate) < 0)) {
            return false;
        }
        this.nextUpdate = now;
        this.nextUpdate.add(Calendar.MINUTE, 1);

        this.domain = this.getDomainInformation();
        this.domain.setSequenceNumber(Integer.valueOf(++this.mySequenceNumber));

        this.myEndpoints = null;
        final List<EndpointType> epList = this.getEndpoints();

        this.myEndpoints = new HashMap<String, EndpointType>();
        if (epList != null) {
            for (final EndpointType ep : epList) {
                this.myEndpoints.put(ep.getEndpointId(), ep);
            }
        }

        for (final InterdomainLinkType i : this.myInterdomainLinks) {
            final String srcTNA = i.getSourceEndpoint().getEndpointId();
            final EndpointType ep = this.myEndpoints.get(srcTNA);
            if (ep != null) {
                try {
                    final InterdomainLinkType i2 = i.clone();
                    i2.setSourceEndpoint(ep);
                    this.domain.getInterdomainLink().add(i2);
                } catch (final CloneNotSupportedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    // IMPOSSIBLE
                }
                if (log != null) {
                    log.debug("updateDomain: added interdomain link "
                            + i.getDestinationDomain() + "/" + i.getLinkID()
                            + " with source endpoint " + srcTNA);
                }
            } else {
                if (log != null) {
                    log.debug("updateDomain: omitting interdomain link "
                            + i.getDestinationDomain() + "/" + i.getLinkID()
                            + " because source endpoint " + srcTNA
                            + " is not known");
                }
            }
        }
        return true;
    }
}
