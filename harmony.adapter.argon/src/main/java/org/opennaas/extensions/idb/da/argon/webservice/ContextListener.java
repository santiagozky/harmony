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


package org.opennaas.extensions.idb.da.argon.webservice;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.unibonn.viola.argon.persist.hibernate.classes.Endlink;
import de.unibonn.viola.argon.requestProcessor.requestHandler.operator.externalInterface.OperatorInterface;
import org.opennaas.extensions.idb.da.argon.Constants;
import org.opennaas.extensions.idb.da.argon.implementation.ArgonOperatorClient;
import org.opennaas.extensions.idb.da.argon.implementation.ConvertHelper;
import org.opennaas.extensions.idb.da.argon.implementation.TNAMapper;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DomainInformationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointInterfaceType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ObjectFactory;
import org.opennaas.extensions.idb.serviceinterface.topology.registrator.AbstractTopologyRegistrator;
import org.opennaas.core.utils.Config;
import org.opennaas.core.utils.PhLogger;

/**
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 */
public class ContextListener extends AbstractTopologyRegistrator {
    public static final String propertyFile = Constants.hsiProperties;
    protected String domainName;
    protected List<Endlink> outgoingEndlinks;
    protected ObjectFactory of = new ObjectFactory();
    protected String endpointReferenceToNSPReservationAdapter;
    protected TNAMapper tnaMapper = TNAMapper.getInstance();
    protected OperatorInterface operator;

    /** Where to put log messages; may be null. */
    //FIXME: NPE here when using Logger.getLogger...
    protected Logger log = PhLogger.getLogger(ContextListener.class);

    protected boolean mockMode() {
        return Config.getString(Constants.adapterProperties, "argonNRPS.mockMode").equals("true");
    }

     /**
     * blocking function
     * @return true if operatior is not null.
     */
    public boolean getConnectionToArgon() {
        if (! this.mockMode()) {
            this.operator = ArgonOperatorClient.getOperator();
            return this.operator!=null;
        }
        return true;
    }

    public void getInformationFromArgon() {
        if (! this.mockMode()) {
            this.log.debug("getting information from argon");
            try{
                this.domainName = this.operator.getDomainName();
                this.outgoingEndlinks = this.operator.getOutgoingEndlinks();
            }catch(Throwable t){
                this.log.error("problem while getting info", t);
            }
            this.log.debug("Domain is: " + this.domainName);
            this.log.debug("outgoing Endlinks are: "+this.outgoingEndlinks);
        } else {
            this.domainName = "viola-mpls";
            this.outgoingEndlinks = new ArrayList<Endlink>(0);
        }
    }

    private EndpointType convertEndpointToEndpointType(Endlink endpoint) {
        String intf = endpoint.getOutgoingInterface();
        String tna = this.tnaMapper.addInterface(intf);
        EndpointType endpointType = this.of.createEndpointType();
        endpointType.setBandwidth(ConvertHelper.convertArgonToNspBw(endpoint.getBandwidth().longValue()));
        endpointType.setDomainId(this.domainName);
        endpointType.setName(intf);
        endpointType.setEndpointId(tna);
        //if ((tna.endsWith(".106") || tna.endsWith(".107")) &&
        //        (! tna.startsWith("10.7.132.")))
        if (! endpoint.getDestinationNode().equals("Application"))
        {
            endpointType.setInterface(EndpointInterfaceType.BORDER);
        } else {
            endpointType.setInterface(EndpointInterfaceType.USER);
        }
        endpointType.setDescription("Argon endpoint: "+endpoint.getOutgoingInterface());
        return endpointType;
    }

    @Override
    protected List<EndpointType> getEndpoints() {
        List<EndpointType> result = new ArrayList<EndpointType>(this.outgoingEndlinks.size());
        for (Endlink endpoint : this.outgoingEndlinks) {
            EndpointType ep = convertEndpointToEndpointType(endpoint);
            if (ep.getInterface() == EndpointInterfaceType.BORDER) {
                result.add(ep);
            }
        }
        return result;
    }

    @Override
    protected boolean startup() {
        this.getLogger().info("Argon Adapter started!");
        return getConnectionToArgon();
    }
    

    @Override
    protected Logger getLogger() {
        return PhLogger.getLogger();
    }

    @Override
    protected DomainInformationType getDomainInformation() {
        getInformationFromArgon();

        DomainInformationType domainInformation = super.getDomainInformation();
        domainInformation.setDomainId(this.domainName);
        return domainInformation;
    }

    @Override
    protected final void shutdown() {
    	ArgonOperatorClient.setShutdown(true);
        this.getLogger().info("Argon Adapter going down.");
    }

    @Override
    protected String getInterdomainPropertyFile() {
        return propertyFile;
    }
}
