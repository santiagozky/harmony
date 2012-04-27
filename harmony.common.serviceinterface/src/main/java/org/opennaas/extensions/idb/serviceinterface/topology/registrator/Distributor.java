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

import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.muse.ws.addressing.EndpointReference;
import org.apache.muse.ws.addressing.soap.SoapFault;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AddEndpointResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AddEndpointType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AddOrEditDomainResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AddOrEditDomainType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DeleteDomainResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DeleteDomainType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DeleteEndpointResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DeleteEndpointType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DomainInformationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EditEndpointResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EditEndpointType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.DomainNotFoundFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.EndpointAlreadyExistsFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.EndpointNotFoundFaultException;
import org.opennaas.extensions.idb.serviceinterface.topology.SimpleTopologyClient;

/**
 * Distributor object take care of distributing topology data to other domains.
 */

public class Distributor implements Runnable {
    /** Maximum number of domain registration attempts. */
    public static final int MAX_REGISTER_ATTEMPTS = 2;

    /** Maximum number of domain deregistration attempts. */
    public static final int MAX_DEREGISTER_ATTEMPTS = 2;

    /** Sleep time between registration/deregistration attempts (in ms). */
    public static final long SLEEP_BETWEEN_ATTEMPTS = 5000l;

    /** Sleep time between registration/deregistration attempts (in ms). */
    public static final long SLEEP_BETWEEN_SYNCS = 300000l;

    /**
     * Sleep time in ms before the next registration attempt. This variable is
     * used to implement an initial sleep time that helped fix some strange
     * problems I experienced. However after cleaning some code, it seems this
     * is not necessary anymore.
     */
    protected long sleep = 0l;
    // protected long sleep = (long)(Math.random() * SLEEP_BETWEEN_SYNCS);

    /** Where to put log messages; may be null. */
    protected Logger log = null;

    /** Domains to register. Use Vector here for sync-safety. */
    protected List<DomainInformationType> domains = new Vector<DomainInformationType>();

    /**
     * Name of the domain this thread is in charge of. If a domain with this ID
     * is given as argument in the addDomain() method, it is ignored (don't
     * register back a domain's own information).
     */
    protected final String domainName;

    /**
     * Access to the topology web service we are registering our information to.
     */
    protected final SimpleTopologyClient topologyClient;

    /** Indicates whether this thread is supposed to be active. */
    protected boolean active = false;

    protected Thread myThread = null;

    /**
     * Constructor, start a new thread.
     * 
     * @param topologyEPR
     *            EPR of the topology WS to deliver the domain information to.
     * @param initialDomain
     *            Initial domain in the domain list, may be null.
     */
    public Distributor(final String name, final Logger log,
            final String domainName, final String topologyEPR,
            final DomainInformationType initialDomain) {
        this.log = log;

        this.domainName = domainName;

        final URI uri = URI.create(topologyEPR);
        final EndpointReference epr = new EndpointReference(uri);
        this.topologyClient = new SimpleTopologyClient(epr);

        // optional, but useful
        this.topologyClient.setTrace(true);

        if (initialDomain != null) {
            this.domains.add(initialDomain);
        }

        this.active = true;
        this.myThread = new Thread(this);
        this.myThread.setName(name);
    }

    public synchronized void addDomain(final DomainInformationType domain) {
        final List<DomainInformationType> rm = new LinkedList<DomainInformationType>();
        for (final DomainInformationType dom : this.domains) {
            if (dom.getDomainId().equals(domain.getDomainId())) {
                rm.add(dom);
            }
        }
        for (final DomainInformationType dom : rm) {
            this.domains.remove(dom);
        }
        this.domains.add(domain);
    }

    protected boolean deregisterDomain(final String domainId) {
        final GenericRegistrator r1 = new GenericRegistrator(domainId,
                Distributor.SLEEP_BETWEEN_ATTEMPTS) {
            @Override
            public boolean register() throws SoapFault {
                final DeleteDomainType requestType = new DeleteDomainType();
                requestType.setDomainId((String) this.req);
                final DeleteDomainResponseType response = Distributor.this.topologyClient
                        .deleteDomain(requestType);
                return (response != null) && response.isSetSuccess()
                        && response.isSuccess();
            }
        };
        final GenericRegistrator dummy = new GenericRegistrator(null,
                Distributor.SLEEP_BETWEEN_ATTEMPTS) {
            @Override
            public boolean register() throws SoapFault {
                return true;
            }
        };
        final boolean success = r1.genericRegistrator(this.log,
                Distributor.MAX_DEREGISTER_ATTEMPTS,
                DomainNotFoundFaultException.class, dummy);
        return success;
    }

    protected boolean deregisterEndpoint(final EndpointType ep) {
        final GenericRegistrator r1 = new GenericRegistrator(
                ep.getEndpointId(), Distributor.SLEEP_BETWEEN_ATTEMPTS) {
            @Override
            public boolean register() throws SoapFault {
                final DeleteEndpointType requestType = new DeleteEndpointType();
                requestType.setEndpoint((String) this.req);
                final DeleteEndpointResponseType response = Distributor.this.topologyClient
                        .deleteEndpoint(requestType);
                return (response != null) && response.isSetSuccess()
                        && response.isSuccess();
            }
        };
        final GenericRegistrator dummy = new GenericRegistrator(null,
                Distributor.SLEEP_BETWEEN_ATTEMPTS) {
            @Override
            public boolean register() throws SoapFault {
                return true;
            }
        };
        return r1.genericRegistrator(this.log,
                Distributor.MAX_DEREGISTER_ATTEMPTS,
                EndpointNotFoundFaultException.class, dummy);
    }

    protected boolean registerDomain(final DomainInformationType domain) {
        final GenericRegistrator r1 = new GenericRegistrator(domain,
                Distributor.SLEEP_BETWEEN_ATTEMPTS) {
            @Override
            public boolean register() throws SoapFault {
                final AddOrEditDomainType addOrEditDomainType = new AddOrEditDomainType();
                addOrEditDomainType.setDomain((DomainInformationType) this.req);
                final AddOrEditDomainResponseType response = Distributor.this.topologyClient
                        .addOrEditDomain(addOrEditDomainType);

                return (response != null) && response.isSetSuccess()
                        && response.isSuccess();
            }
        };
        boolean result = false;
        try {
            result = r1.register();
        } catch (final SoapFault e) {
            this.log.error("Could not register domain: " + e.getMessage(), e);
        }
        return result;
    }

    protected boolean registerEndpoint(final EndpointType ep) {
        final GenericRegistrator r1 = new GenericRegistrator(ep,
                Distributor.SLEEP_BETWEEN_ATTEMPTS) {
            @Override
            public boolean register() throws SoapFault {
                final AddEndpointType requestType = new AddEndpointType();
                requestType.setEndpoint((EndpointType) this.req);
                final AddEndpointResponseType response = Distributor.this.topologyClient
                        .addEndpoint(requestType);
                return (response != null) && response.isSetSuccess()
                        && response.isSuccess();
            }
        };
        final GenericRegistrator r2 = new GenericRegistrator(ep,
                Distributor.SLEEP_BETWEEN_ATTEMPTS) {
            @Override
            public boolean register() throws SoapFault {
                final EditEndpointType requestType = new EditEndpointType();
                requestType.setEndpoint((EndpointType) this.req);
                final EditEndpointResponseType response = Distributor.this.topologyClient
                        .editEndpoint(requestType);
                return (response != null) && response.isSetSuccess()
                        && response.isSuccess();
            }
        };
        return r1.genericRegistrator(this.log,
                Distributor.MAX_REGISTER_ATTEMPTS,
                EndpointAlreadyExistsFaultException.class, r2);
    }

    public void run() {
        if (this.log != null) {
            this.log.info("initialized");
        }
        while (this.active) {
            try {
                Thread.sleep(this.sleep);
            } catch (final InterruptedException e) {
                // nothing .. active has been set to false
            }
            this.sleep = Distributor.SLEEP_BETWEEN_SYNCS;
            if (this.active) {
                this.update();
                while (this.domains.size() > 0) {
                    if (!this.active) {
                        break;
                    }
                    final DomainInformationType domain = this.domains.remove(0);
                    this.registerDomain(domain);
                }
            }
        }
        if (this.log != null) {
            this.log.info("terminated");
        }
    }

    public void start() {
        this.myThread.start();
    }

    public void stop() {
        this.active = false;
        this.myThread.interrupt();
    }

    /**
     * The update method is called immediately before domain registration takes
     * place. It does not do anything in the Distributor class itself, but may
     * be overridden by subclasses to update the information in the domain list.
     */
    protected void update() {
        // nothing
    }
}
