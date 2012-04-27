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


package org.opennaas.extensions.idb.handler.test;

import java.net.URISyntaxException;
import java.util.Hashtable;

import org.apache.muse.ws.addressing.soap.SoapFault;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AddDomainType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ConnectionConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DeleteDomainType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.FixedReservationConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.IsAvailableType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ServiceConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.DomainNotFoundFaultException;
import org.opennaas.extensions.idb.serviceinterface.topology.SimpleTopologyClient;
import org.opennaas.core.utils.Config;
import org.opennaas.core.utils.Helpers;
import org.opennaas.extensions.idb.Constants;
import org.opennaas.extensions.idb.database.hibernate.Domain;
import org.opennaas.extensions.idb.reservation.AdapterManager;
import org.opennaas.extensions.idb.reservation.IManager;
import org.opennaas.extensions.idb.utils.TopologyHelpers;
import org.opennaas.extensions.idb.webservice.TopologyWS;
import org.opennaas.extensions.idb.test.webservice.AbstractReservationTest;

/**
 * JUnit test case for the response-cahce of the AdapterManager.
 * 
 * @author Alexander Zimmermann (zimmerm2@cs.uni-bonn.de)
 * @version $Id$
 */
public class TestAdapterHandler extends AbstractReservationTest {

    /** topology client. */
    private SimpleTopologyClient topologyClient;

    /** adapterManager instance. */
    IManager adapterManager;

    /** test domain. */
    private Domain testDomain;

    /** test requests. */
    private Hashtable<Domain, IsAvailableType> requests;

    public TestAdapterHandler() throws SoapFault {
        super();
        // create topology client instance
        if (Config.isTrue(Constants.testProperties, "test.callWebservice")) {
            final String topoEpr =
                    Config.getString(Constants.testProperties, "epr.topology");
            this.topologyClient = new SimpleTopologyClient(topoEpr);
        } else {
            this.topologyClient = new SimpleTopologyClient(new TopologyWS());
        }
    }

    @Before
    public void setUpBeforeTest() throws SoapFault, URISyntaxException {
        // create adapterManager instance
        this.adapterManager = AdapterManager.getInstance();

        // set up test domain
        this.testDomain =
                new Domain(Config.getString("test", "default.domain.name"),
                        Config.getString("test", "default.domain.epr"),
                        "http://" + Helpers.getRandomString());

        testDomain.addPrefix(Config.getString("test", "default.domain.prefix"));

        AddDomainType addDomainType = new AddDomainType();
        addDomainType.setDomain(testDomain.toJaxb());

        try {
			this.topologyClient.deleteDomain(testDomain.getName());
		} catch (DomainNotFoundFaultException e) {
			;
		}
        this.topologyClient.addDomain(addDomainType);

        // set up availability-requests (all random values, not needed to be
        // valid)
        ConnectionConstraintType cct = new ConnectionConstraintType();
        cct.setConnectionID(Helpers.getPositiveRandomInt());
        cct.setDirectionality(Helpers.getPositiveRandomInt());
        cct.setMaxBW(Helpers.getPositiveRandomInt());
        cct.setMinBW(Helpers.getPositiveRandomInt());
        cct.setMaxDelay(Helpers.getPositiveRandomInt());
        cct.setSource(TopologyHelpers.getTestEndpointForDomain(testDomain)
                .toJaxb());
        cct.getTarget().add(
                TopologyHelpers.getTestEndpointForDomain(testDomain).toJaxb());

        FixedReservationConstraintType frct =
                new FixedReservationConstraintType();
        frct.setDuration(Helpers.getPositiveRandomInt());
        frct.setStartTime(Helpers.generateXMLCalendar());

        ServiceConstraintType sct = new ServiceConstraintType();
        sct.getConnections().add(cct);
        sct.setFixedReservationConstraints(frct);
        sct.setAutomaticActivation(true);
        sct.setServiceID(Helpers.getPositiveRandomInt());
        sct.setTypeOfReservation(ReservationType.FIXED);

        IsAvailableType request = new IsAvailableType();
        request.setJobID(Helpers.getPositiveRandomLong());
        request.getService().add(sct);

        // put request together
        this.requests = new Hashtable<Domain, IsAvailableType>();
        this.requests.put(this.testDomain, request);
    }

    @After
    public void tearDownAfterTest() throws SoapFault {
        // delete test domain
        DeleteDomainType deleteDomainType = new DeleteDomainType();
        deleteDomainType.setDomainId(this.testDomain.getName());
        this.topologyClient.deleteDomain(deleteDomainType);
    }

    @Test()
    public final void testResponseCache() throws Exception {
        // first call to fill response-cache
        adapterManager.isAvailable(requests);
        // second call to use the cache, BUT the requests shouldn't be deleted
        // !!
        adapterManager.isAvailable(requests);
        Assert.assertFalse("requests should be not empty!", requests.isEmpty());
    }
}
