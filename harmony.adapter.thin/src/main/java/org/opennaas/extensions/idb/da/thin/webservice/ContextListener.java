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


/**
 *
 */
package org.opennaas.extensions.idb.da.thin.webservice;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.muse.ws.addressing.EndpointReference;
import org.apache.muse.ws.addressing.soap.SoapFault;
import org.quartz.SchedulerException;

import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.GetEndpointDiscovery;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.GetEndpointDiscoveryResponse;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.GetEndpointDiscoveryType;
import org.opennaas.extensions.gmpls.webservice.GmplsClient;
import org.opennaas.extensions.idb.da.thin.persistence.DbManager;
import org.opennaas.extensions.idb.da.thin.persistence.orm.GmplsConnection;
import org.opennaas.extensions.idb.da.thin.impl.CancelHandler;
import org.opennaas.extensions.idb.da.thin.scheduler.JobManager;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.StatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.InvalidRequestFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.utils.AJaxbSerializer;
import org.opennaas.extensions.idb.serviceinterface.topology.registrator.AbstractTopologyRegistrator;
import org.opennaas.core.utils.Config;
import org.opennaas.core.utils.Helpers;
import org.opennaas.core.utils.PhLogger;

/**
 * @author Daniel Beer (daniel.beer@iais.fraunhofer.de)
 */
public class ContextListener extends AbstractTopologyRegistrator {
    public static final String interdomainPropertyFile = "hsi";
    /**
     *
     */
    private static final long serialVersionUID = -1799623170913833504L;
    /** */
    private static GmplsClient gmplsWS;

    private static Logger logger = null;

    /**
     * pull all endpoints from GMPLS-WS.
     * 
     * @return list of endpoints
     */
    private List<org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.EndpointType> pullEndpoints() {
	List<org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.EndpointType> endpoints = null;

	final GetEndpointDiscoveryType gtt = new GetEndpointDiscoveryType();
	final GetEndpointDiscovery gt = new GetEndpointDiscovery();
	gt.setGetEndpointDiscovery(gtt);

	AJaxbSerializer jser = org.opennaas.extensions.gmpls.serviceinterface.databinding.utils.JaxbSerializer
		.getInstance();

	try {
	    GetEndpointDiscoveryResponse gmplsResponse = (GetEndpointDiscoveryResponse) jser
		    .elementToObject(gmplsWS.getEndpointDiscovery((jser
			    .objectToElement(gt))));
	    endpoints = gmplsResponse.getGetEndpointDiscoveryResponse()
		    .getEndpoint();

	    for (org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.EndpointType endpointType : endpoints) {
		DbManager.insertEndpoint(endpointType);
	    }

	} catch (InvalidRequestFaultException e) {
	    e.printStackTrace();
	} catch (UnexpectedFaultException e) {
	    e.printStackTrace();
	} catch (SoapFault e) {
	    e.printStackTrace();
	}
	return endpoints;
    }

    /*
     * @Override protected final DomainInformationType getDomainInformation() {
     * DomainInformationType dom = super.getDomainInformation(); try {
     * dom.setDomainId(DbManager.getDomainId()); } catch
     * (UnexpectedFaultException e) { e.printStackTrace(); } return dom; }
     */

    @Override
    protected final void shutdown() {
	try {
	    JobManager.getInstance().shutdown();
	} catch (SchedulerException e) {
	    e.printStackTrace();
	}
	logger.debug("ThinNrps going down.");
    }

    @Override
    protected final boolean startup() {
	logger = PhLogger.getLogger(this.getClass());
	try {
	    gmplsWS = new GmplsClient(new EndpointReference(new URI(Config
		    .getString("adapter", "epr.gmpls"))));
	} catch (URISyntaxException e) {
	    e.printStackTrace();
	    return false;
	}
	rescheduleReservationsFromDB();

	logger.debug("ThinNrps started!");
	return true;
    }

    /**
     * @return
     */
    public static GmplsClient getGmplsWS() {
	if (gmplsWS == null) {
	    try {
		gmplsWS = new GmplsClient(new EndpointReference(new URI(Config
			.getString("adapter", "epr.gmpls"))));
	    } catch (URISyntaxException e) {
		e.printStackTrace();
	    }

	}
	return gmplsWS;
    }

    /**
     *
     */
    private void rescheduleReservationsFromDB() {
	try {
	    Timestamp now = new Timestamp(Helpers.generateXMLCalendar()
		    .toGregorianCalendar().getTimeInMillis());
	    for (GmplsConnection gmplsConnection : DbManager
		    .getAllConnections()) {
		// reschedule only formerly scheduled reservations
		if (gmplsConnection.isScheduled()) {
		    if (gmplsConnection.getEndTime().after(now)) {
			// only create path if time left
			if (gmplsConnection.getStartTime().before(now)) {
			    gmplsConnection.setStartTime(now);
			}
			JobManager.getInstance().schedulePathSetUp(
				gmplsConnection);
		    }
		    JobManager.getInstance().schedulePathTermination(
			    gmplsConnection);
		} else if (gmplsConnection.convertStatus().equals(
			StatusType.ACTIVE)) {
		    if (gmplsConnection.getEndTime().after(now)) {
			JobManager.getInstance().schedulePathTermination(
				gmplsConnection);
		    } else {
			CancelHandler.getInstance().cancelConnection(
				gmplsConnection);
		    }
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    @Override
    protected List<EndpointType> getEndpoints() {
	return WebserviceUtils.convert(pullEndpoints());
    }

    @Override
    protected String getInterdomainPropertyFile() {
	return interdomainPropertyFile;
    }
}
