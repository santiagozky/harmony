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


package eu.ist_phosphorus.harmony.adapter.arrm.webservice;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.DomainInformationType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EndpointType;
import eu.ist_phosphorus.harmony.common.serviceinterface.topology.registrator.AbstractTopologyRegistrator;
import eu.ist_phosphorus.harmony.common.utils.Config;
import eu.ist_phosphorus.harmony.common.utils.PhLogger;

/**
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 */
public class ContextListener extends AbstractTopologyRegistrator {
    public static final String propertyFile = "hsiArrm";

    @Override
    protected final DomainInformationType getDomainInformation() {
        final DomainInformationType dom = super.getDomainInformation();
        dom.setAvgDelay(Config.getInt(this.getInterdomainPropertyFile(),
                "domain.avgDelay"));
        dom.setMaxBW(Config.getInt(this.getInterdomainPropertyFile(),
                "domain.maxBw"));

        return dom;
    }

    @Override
    protected List<EndpointType> getEndpoints() {
        return this.pullEndpoints();
    }

    @Override
    protected String getInterdomainPropertyFile() {
        return ContextListener.propertyFile;
    }

    @Override
    protected Logger getLogger() {
        return PhLogger.getLogger();
    }

    /**
     * pull all endpoints from GMPLS-WS.
     * 
     * @return list of endpoints
     */
    private List<EndpointType> pullEndpoints() {
        final List<EndpointType> endpoints = new LinkedList<EndpointType>();

        final EndpointType endpoint0 = new EndpointType();
        endpoint0.setEndpointId(Config.getString("test",
                "test.endpoint0.tna"));
        endpoint0.setBandwidth(Config.getInt("test", "test.endpoint0.bw"));
        endpoint0.setDomainId(Config.getString(this
                .getInterdomainPropertyFile(), "domain.name"));
        endpoints.add(endpoint0);

        final EndpointType endpoint1 = new EndpointType();
        endpoint1.setEndpointId(Config.getString("test",
                "test.endpoint1.tna"));
        endpoint1.setBandwidth(Config.getInt("test", "test.endpoint1.bw"));
        endpoint1.setDomainId(Config.getString(this
                .getInterdomainPropertyFile(), "domain.name"));
        endpoints.add(endpoint1);

        return endpoints;
    }

    @Override
    protected final void shutdown() {
        this.getLogger().info("ARRM/IDC adapter is going down.");
    }

    @Override
    protected final boolean startup() {
        this.getLogger().info("ARRM/IDC adapter started!");
        return true;
    }
}
