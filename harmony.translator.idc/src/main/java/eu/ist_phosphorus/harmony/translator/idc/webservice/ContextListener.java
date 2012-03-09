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


package eu.ist_phosphorus.harmony.translator.idc.webservice;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.apache.axis2.AxisFault;
import org.apache.log4j.Logger;

import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.DomainInformationType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EndpointType;
import eu.ist_phosphorus.harmony.common.serviceinterface.topology.registrator.AbstractTopologyRegistrator;
import eu.ist_phosphorus.harmony.common.utils.Config;
import eu.ist_phosphorus.harmony.common.utils.PhLogger;
import eu.ist_phosphorus.harmony.translator.idc.implementation.SimpleIdcClient;

/**
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 */
public class ContextListener extends AbstractTopologyRegistrator {
    public static final String PROPERTY = "hsiIDC";
    private final SimpleIdcClient idcClient;

    public ContextListener() throws AxisFault {
        super();
        this.idcClient = new SimpleIdcClient();
    }

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
        return ContextListener.PROPERTY;
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

        try {
            final Properties properties = Config.getProperties("endpointMap");
            final EndpointType endpoint = new EndpointType();

            for (final Object key : properties.keySet()) {
                final String epID = (String) key;
                final String idcURN = properties.getProperty(epID, "(Unknown)");

                // final CtrlPlaneLinkContent link =
                // SimpleIdcClient.getInstance()
                // .getLinkInformation(idcURN);

                int maxBW;
                try {
                    maxBW = this.idcClient.getMaximumReservableCapacity();
                } catch (final RuntimeException e) {
                    // Handle Nullpointer + NumberFormatExceptions...
                    maxBW = Config.getInt("translatorIDC", "idc.maxBW");
                }

                endpoint.setEndpointId(epID);
                endpoint.setBandwidth(Integer.valueOf(maxBW));
                endpoint.setDomainId(Config.getString(this
                        .getInterdomainPropertyFile(), "domain.name"));
                endpoint.setDescription(idcURN);
                endpoints.add(endpoint);
            }
        } catch (final IOException e) {
            this.getLogger().error("Could not load Endpoints", e);
        }

        return endpoints;
    }

    @Override
    protected final void shutdown() {
        this.getLogger().info("IDC translator is going down.");
    }

    @Override
    protected final boolean startup() {
        this.getLogger().info("IDC translator started!");
        return true;
    }
}
