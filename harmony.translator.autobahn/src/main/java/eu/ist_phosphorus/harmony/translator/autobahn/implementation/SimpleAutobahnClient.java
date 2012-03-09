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


package eu.ist_phosphorus.harmony.translator.autobahn.implementation;

import java.rmi.RemoteException;

import javax.servlet.ServletContext;

import net.es.oscars.client.Client;
import net.es.oscars.oscars.AAAFaultMessage;
import net.es.oscars.oscars.BSSFaultMessage;
import net.es.oscars.wsdlTypes.CancelReservation;
import net.es.oscars.wsdlTypes.CancelReservationResponse;
import net.es.oscars.wsdlTypes.CreateReply;
import net.es.oscars.wsdlTypes.CreateReservation;
import net.es.oscars.wsdlTypes.CreateReservationResponse;
import net.es.oscars.wsdlTypes.GetTopologyContent;
import net.es.oscars.wsdlTypes.ListReply;
import net.es.oscars.wsdlTypes.ListReservations;
import net.es.oscars.wsdlTypes.ListReservationsResponse;
import net.es.oscars.wsdlTypes.QueryReservation;
import net.es.oscars.wsdlTypes.QueryReservationResponse;
import net.es.oscars.wsdlTypes.ResDetails;

import org.apache.axis2.AxisFault;
import org.apache.log4j.Logger;

import eu.ist_phosphorus.harmony.common.serviceinterface.topology.registrator.AbstractTopologyRegistrator;
import eu.ist_phosphorus.harmony.common.utils.Config;
import eu.ist_phosphorus.harmony.common.utils.PhLogger;
import eu.ist_phosphorus.harmony.translator.autobahn.implementation.interfaces.IOscarsClient;

/**
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 */
public class SimpleAutobahnClient extends Client implements IOscarsClient {

    /**
     *
     */
    private final Logger logger = PhLogger.getLogger();

    /**
     * @throws AxisFault
     */
    public SimpleAutobahnClient() throws AxisFault {
        super();

        String repo = Config.getString("translatorAutobahn", "autobahn.repo");
        final String epr = Config.getString("translatorAutobahn", "autobahn.epr");

        if (!repo.endsWith("/")) {
            repo += "/";
        }

        if (null != AbstractTopologyRegistrator.getLatestInstance()) {
            final ServletContext context =
                    AbstractTopologyRegistrator.getLatestInstance()
                            .getServletContext();

            if ((null != context) && (repo.charAt(0) != '/')) {
                repo = context.getRealPath("WEB-INF") + "/" + repo;
            }
        }

        super.setUp(true, epr, repo);

        if (Config.isTrue("translatorAutobahn", "autobahn.fetchTopology")) {
            try {
                this.logger.info("Fetching topology Information");

                this.getTopologyInformation();
            } catch (final Exception e) {
                this.logger.error("Could not get Topology Information", e);
            }
        }
    }

    /*
     * (non-Javadoc)
     * @seeeu.ist_phosphorus.harmony.translator.autobahn.implementation.interfaces.
     * IOscarsClient
     * #cancelReservation(net.es.oscars.wsdlTypes.CancelReservation)
     */
    public CancelReservationResponse cancelReservation(
            final CancelReservation cancelReservation120) throws Exception {
        final String param =
                this.cancelReservation(cancelReservation120
                        .getCancelReservation());

        final CancelReservationResponse response =
                new CancelReservationResponse();
        response.setCancelReservationResponse(param);

        return response;
    }

    /*
     * (non-Javadoc)
     * @seeeu.ist_phosphorus.harmony.translator.autobahn.implementation.interfaces.
     * IOscarsClient
     * #createReservation(net.es.oscars.wsdlTypes.CreateReservation)
     */
    public CreateReservationResponse createReservation(
            final CreateReservation arg0) throws Exception {
        final CreateReply param =
                super.createReservation(arg0.getCreateReservation());

        final CreateReservationResponse response =
                new CreateReservationResponse();
        response.setCreateReservationResponse(param);

        return response;
    }

    public int getMaximumReservableCapacity() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * @throws RemoteException
     * @throws AAAFaultMessage
     * @throws Exception
     */
    private void getTopologyInformation() throws Exception {
        final GetTopologyContent request = new GetTopologyContent();
        request.setTopologyType("all");

        this.getNetworkTopology(request);
    }

    /*
     * (non-Javadoc)
     * @seeeu.ist_phosphorus.harmony.translator.autobahn.implementation.interfaces.
     * IOscarsClient#listReservations(net.es.oscars.wsdlTypes.ListReservations)
     */
    public ListReservationsResponse listReservations(
            final ListReservations listReservations136) throws RemoteException,
            AAAFaultMessage, BSSFaultMessage {

        final ListReply param =
                this
                        .listReservations(listReservations136
                                .getListReservations());

        final ListReservationsResponse response =
                new ListReservationsResponse();
        response.setListReservationsResponse(param);

        return response;
    }

    /*
     * (non-Javadoc)
     * @seeeu.ist_phosphorus.harmony.translator.autobahn.implementation.interfaces.
     * IOscarsClient#queryReservation(net.es.oscars.wsdlTypes.QueryReservation)
     */
    public QueryReservationResponse queryReservation(
            final QueryReservation autobahnRequest) throws Exception {
        final ResDetails param =
                this.queryReservation(autobahnRequest.getQueryReservation());

        final QueryReservationResponse response =
                new QueryReservationResponse();
        response.setQueryReservationResponse(param);

        return response;
    }

}
