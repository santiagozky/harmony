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
package org.opennaas.extensions.idb.test.webservice;

import org.apache.muse.ws.addressing.soap.SoapFault;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AddOrEditDomainType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ConnectionConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CreateReservationResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CreateReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DomainInformationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointInterfaceType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetDomainsResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.InterdomainLinkType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.MalleableReservationConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ServiceConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.PathNotFoundFaultException;
import org.opennaas.extensions.idb.serviceinterface.topology.SimpleTopologyClient;
import org.opennaas.core.utils.Config;
import org.opennaas.core.utils.Helpers;
import org.opennaas.extensions.idb.Constants;
import org.opennaas.extensions.idb.database.hibernate.Domain;
import org.opennaas.extensions.idb.database.hibernate.Endpoint;
import org.opennaas.extensions.idb.database.hibernate.Reservation;
import org.opennaas.extensions.idb.utils.TopologyHelpers;
import org.opennaas.extensions.idb.webservice.TopologyWS;

/**
 * Test malleable reservations
 * 
 * @author zimmerm2
 */
public class TestMalleableReservations extends AbstractReservationTest {

    // source TNA of the malleable reservation
    private static String sourceTNA1;
    private static String sourceTNA2;
    // destination TNA of the malleable reservation
    private static String destinationTNA1;
    private static String destinationTNA2;
    // data-amount to be transferred in MB 
    // (45000 MB should induce a duration of 3600 seconds on a 100 MBit path) 
    private static long dataAmount = 45000;

    // referenceDomain
    private static Domain referenceDomain;
    private static Domain referenceDomain2;
    private static Domain referenceDomain3;

    // created reservation
    private static String reservationId = "";

    // topology client
    private static SimpleTopologyClient topologyClient;

    public TestMalleableReservations() throws SoapFault {
        super();
    }

    /**
     * @throws SoapFault 
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws SoapFault {
        if (Config.isTrue(Constants.testProperties, "test.callWebservice")) {
            final String topoEpr = Config.getString(Constants.testProperties, "epr.topology");
            TestMalleableReservations.topologyClient = new SimpleTopologyClient(topoEpr);
        } else {
            TestMalleableReservations.topologyClient = new SimpleTopologyClient(new TopologyWS());
        }

        int auxSequenceNr = 10;

        /* cleanup database ------------------------------------------------- */
        GetDomainsResponseType domains = TestMalleableReservations.topologyClient.getDomains();
        for (DomainInformationType domain : domains.getDomains()) {
            try {
                TestMalleableReservations.topologyClient.deleteDomain(domain.getDomainId());
            } catch (SoapFault e) {
//                logger.info("Could not remove domain: " + e.getMessage(),
//                        e);
            }
        }
        /* ------------------------------------------------------------------ */

        // create reference domain1 in the IDB
        TestMalleableReservations.referenceDomain = new Domain(Config.getString(Constants.testProperties,
                "test.malleable.domain1.name"), Config.getString(Constants.testProperties,
                "test.malleable.domain1.epr"), Config.getString(Constants.testProperties,
                "test.malleable.domain1.epr"));
        TestMalleableReservations.referenceDomain.addPrefix(Config.getString(Constants.testProperties,
                "test.malleable.domain1.prefix"));
        Endpoint end1_1 = TopologyHelpers
                .getTestEndpointForDomain(TestMalleableReservations.referenceDomain);
        Endpoint end1_2 = TopologyHelpers
                .getTestEndpointForDomain(TestMalleableReservations.referenceDomain);
        Endpoint end1_3 = TopologyHelpers
                .getTestEndpointForDomain(TestMalleableReservations.referenceDomain);
        end1_1.setTNA(Config.getString(Constants.testProperties,
                "test.malleable.domain1.endpoint.border1"));
        end1_1.setType(EndpointInterfaceType.BORDER.ordinal());
        	end1_1.setBandwidth(600);
        TestMalleableReservations.referenceDomain.getEndpoints().add(end1_1);
        end1_2.setTNA(Config.getString(Constants.testProperties,
                "test.malleable.domain1.endpoint.border2"));
        end1_2.setType(EndpointInterfaceType.BORDER.ordinal());
        	end1_2.setBandwidth(600);
        TestMalleableReservations.referenceDomain.getEndpoints().add(end1_2);
        end1_3.setTNA(Config.getString(Constants.testProperties,
                "test.malleable.domain1.endpoint.border3"));
        end1_3.setType(EndpointInterfaceType.BORDER.ordinal());
        	end1_3.setBandwidth(600);
        TestMalleableReservations.referenceDomain.getEndpoints().add(end1_3);

        AddOrEditDomainType aedt = new AddOrEditDomainType();
        aedt.setDomain(TestMalleableReservations.referenceDomain.toJaxb());
        // add it to the topology-client
        TestMalleableReservations.topologyClient.addOrEditDomain(aedt);
        TestMalleableReservations.topologyClient.addEndpoint(end1_1.toJaxb());
        TestMalleableReservations.topologyClient.addEndpoint(end1_2.toJaxb());
        TestMalleableReservations.topologyClient.addEndpoint(end1_3.toJaxb());

        // create reference domain2
        TestMalleableReservations.referenceDomain2 = new Domain(Config.getString(Constants.testProperties,
                "test.malleable.domain2.name"), Config.getString(Constants.testProperties,
                "test.malleable.domain2.epr"), Config.getString(Constants.testProperties,
                "test.malleable.domain2.epr"));
        TestMalleableReservations.referenceDomain2.addPrefix(Config.getString(Constants.testProperties,
                "test.malleable.domain2.prefix"));
        Endpoint end2_1 = TopologyHelpers
                .getTestEndpointForDomain(TestMalleableReservations.referenceDomain2);
        Endpoint end2_2 = TopologyHelpers
                .getTestEndpointForDomain(TestMalleableReservations.referenceDomain2);
        Endpoint end2_3 = TopologyHelpers
                .getTestEndpointForDomain(TestMalleableReservations.referenceDomain2);
        Endpoint end2_4 = TopologyHelpers
        		.getTestEndpointForDomain(TestMalleableReservations.referenceDomain2);
        Endpoint end2_5 = TopologyHelpers
        		.getTestEndpointForDomain(TestMalleableReservations.referenceDomain2);
        Endpoint end2_6 = TopologyHelpers
        		.getTestEndpointForDomain(TestMalleableReservations.referenceDomain2);
        end2_1.setTNA(Config.getString(Constants.testProperties,
                "test.malleable.domain2.endpoint.border1"));
        end2_1.setType(EndpointInterfaceType.BORDER.ordinal());
        	end2_1.setBandwidth(600);
        TestMalleableReservations.referenceDomain2.getEndpoints().add(end2_1);
        end2_2.setTNA(Config.getString(Constants.testProperties,
                "test.malleable.domain2.endpoint.border2"));
        end2_2.setType(EndpointInterfaceType.BORDER.ordinal());
        	end2_2.setBandwidth(600);
        TestMalleableReservations.referenceDomain2.getEndpoints().add(end2_2);
        end2_3.setTNA(Config.getString(Constants.testProperties,
                "test.malleable.domain2.endpoint.border3"));
        end2_3.setType(EndpointInterfaceType.BORDER.ordinal());
        	end2_3.setBandwidth(600);
        TestMalleableReservations.referenceDomain2.getEndpoints().add(end2_3);
        end2_4.setTNA(Config.getString(Constants.testProperties,
        		"test.malleable.domain2.endpoint.border4"));
        end2_4.setType(EndpointInterfaceType.BORDER.ordinal());
        	end2_4.setBandwidth(600);
        TestMalleableReservations.referenceDomain2.getEndpoints().add(end2_4);
        end2_5.setTNA(Config.getString(Constants.testProperties,
        		"test.malleable.domain2.endpoint.border5"));
        end2_5.setType(EndpointInterfaceType.BORDER.ordinal());
        	end2_5.setBandwidth(600);
        TestMalleableReservations.referenceDomain2.getEndpoints().add(end2_5);
        end2_6.setTNA(Config.getString(Constants.testProperties,
        		"test.malleable.domain2.endpoint.border6"));
        end2_6.setType(EndpointInterfaceType.BORDER.ordinal());
        end2_6.setBandwidth(600);
        TestMalleableReservations.referenceDomain2.getEndpoints().add(end2_6);

        AddOrEditDomainType aedt2 = new AddOrEditDomainType();
        aedt2.setDomain(TestMalleableReservations.referenceDomain2.toJaxb());
        // add it to the topology-client
        TestMalleableReservations.topologyClient.addOrEditDomain(aedt2);
        TestMalleableReservations.topologyClient.addEndpoint(end2_1.toJaxb());
        TestMalleableReservations.topologyClient.addEndpoint(end2_2.toJaxb());
        TestMalleableReservations.topologyClient.addEndpoint(end2_3.toJaxb());
        TestMalleableReservations.topologyClient.addEndpoint(end2_4.toJaxb());
        TestMalleableReservations.topologyClient.addEndpoint(end2_5.toJaxb());
        TestMalleableReservations.topologyClient.addEndpoint(end2_6.toJaxb());

        // create reference domain3
        TestMalleableReservations.referenceDomain3 = new Domain(Config.getString(Constants.testProperties,
                "test.malleable.domain3.name"), Config.getString(Constants.testProperties,
                "test.malleable.domain3.epr"), Config.getString(Constants.testProperties,
                "test.malleable.domain3.epr"));
        TestMalleableReservations.referenceDomain3.addPrefix(Config.getString(Constants.testProperties,
                "test.malleable.domain3.prefix"));
        Endpoint end3_1 = TopologyHelpers
                .getTestEndpointForDomain(TestMalleableReservations.referenceDomain3);
        Endpoint end3_2 = TopologyHelpers
                .getTestEndpointForDomain(TestMalleableReservations.referenceDomain3);
        Endpoint end3_3 = TopologyHelpers
                .getTestEndpointForDomain(TestMalleableReservations.referenceDomain3);
        end3_1.setTNA(Config.getString(Constants.testProperties,
                "test.malleable.domain3.endpoint.border1"));
        end3_1.setType(EndpointInterfaceType.BORDER.ordinal());
        	end3_1.setBandwidth(600);
        TestMalleableReservations.referenceDomain3.getEndpoints().add(end3_1);
        end3_2.setTNA(Config.getString(Constants.testProperties,
                "test.malleable.domain3.endpoint.border2"));
        end3_2.setType(EndpointInterfaceType.BORDER.ordinal());
        	end3_2.setBandwidth(600);
        TestMalleableReservations.referenceDomain3.getEndpoints().add(end3_2);
        end3_3.setTNA(Config.getString(Constants.testProperties,
                "test.malleable.domain3.endpoint.border3"));
        end3_3.setType(EndpointInterfaceType.BORDER.ordinal());
        	end3_3.setBandwidth(600);
        TestMalleableReservations.referenceDomain3.getEndpoints().add(end3_3);

        AddOrEditDomainType aedt3 = new AddOrEditDomainType();
        aedt3.setDomain(TestMalleableReservations.referenceDomain3.toJaxb());
        // add it to the topology-client
        TestMalleableReservations.topologyClient.addOrEditDomain(aedt3);
        TestMalleableReservations.topologyClient.addEndpoint(end3_1.toJaxb());
        TestMalleableReservations.topologyClient.addEndpoint(end3_2.toJaxb());
        TestMalleableReservations.topologyClient.addEndpoint(end3_3.toJaxb());

        // increment sequence-number or domain-changes will not be handled
        auxSequenceNr++;

        // InterdomainLink Domain1.endpoint1 -> Domain2.endpoint2
        InterdomainLinkType idlt = new InterdomainLinkType();
        idlt.setCosts(Helpers.getPositiveRandomInt());
        idlt.setDestinationDomain(TestMalleableReservations.referenceDomain2.getName());
        idlt.setLinkID(Helpers.getRandomString());
        idlt.setSourceEndpoint(end1_1.toJaxb());
        aedt.getDomain().getInterdomainLink().add(idlt);
        aedt.getDomain().setSequenceNumber(auxSequenceNr);
        TestMalleableReservations.topologyClient.addOrEditDomain(aedt);

        // InterdomainLink Domain2.endpoint2 -> Domain1.endpoint1
        InterdomainLinkType idlt2 = new InterdomainLinkType();
        idlt2.setCosts(Helpers.getPositiveRandomInt());
        idlt2.setDestinationDomain(TestMalleableReservations.referenceDomain.getName());
        idlt2.setLinkID(idlt.getLinkID());
        idlt2.setSourceEndpoint(end2_2.toJaxb());
        aedt2.getDomain().getInterdomainLink().add(idlt2);
        aedt2.getDomain().setSequenceNumber(auxSequenceNr);
        TestMalleableReservations.topologyClient.addOrEditDomain(aedt2);

        // increment sequence-number or domain-changes will not be handled
        auxSequenceNr++;

        // InterdomainLink Domain1.endpoint2 -> Domain2.endpoint1
        idlt = new InterdomainLinkType();
        idlt.setCosts(Helpers.getPositiveRandomInt());
        idlt.setDestinationDomain(TestMalleableReservations.referenceDomain2.getName());
        idlt.setLinkID(Helpers.getRandomString());
        idlt.setSourceEndpoint(end1_2.toJaxb());
        aedt.getDomain().getInterdomainLink().add(idlt);
        aedt.getDomain().setSequenceNumber(auxSequenceNr);
        TestMalleableReservations.topologyClient.addOrEditDomain(aedt);

        // InterdomainLink Domain2.endpoint1 -> Domain1.endpoint2
        idlt2 = new InterdomainLinkType();
        idlt2.setCosts(Helpers.getPositiveRandomInt());
        idlt2.setDestinationDomain(TestMalleableReservations.referenceDomain.getName());
        idlt2.setLinkID(idlt.getLinkID());
        idlt2.setSourceEndpoint(end2_1.toJaxb());
        aedt2.getDomain().getInterdomainLink().add(idlt2);
        aedt2.getDomain().setSequenceNumber(auxSequenceNr);
        TestMalleableReservations.topologyClient.addOrEditDomain(aedt2);

        // increment sequence-number or domain-changes will not be handled
        auxSequenceNr++;

        // InterdomainLink Domain1.endpoint3 -> Domain2.endpoint3
        idlt = new InterdomainLinkType();
        idlt.setCosts(Helpers.getPositiveRandomInt());
        idlt.setDestinationDomain(TestMalleableReservations.referenceDomain2.getName());
        idlt.setLinkID(Helpers.getRandomString());
        idlt.setSourceEndpoint(end1_3.toJaxb());
        aedt.getDomain().getInterdomainLink().add(idlt);
        aedt.getDomain().setSequenceNumber(auxSequenceNr);
        TestMalleableReservations.topologyClient.addOrEditDomain(aedt);

        // InterdomainLink Domain2.endpoint3 -> Domain1.endpoint3
        idlt2 = new InterdomainLinkType();
        idlt2.setCosts(Helpers.getPositiveRandomInt());
        idlt2.setDestinationDomain(TestMalleableReservations.referenceDomain.getName());
        idlt2.setLinkID(idlt.getLinkID());
        idlt2.setSourceEndpoint(end2_3.toJaxb());
        aedt2.getDomain().getInterdomainLink().add(idlt2);
        aedt2.getDomain().setSequenceNumber(auxSequenceNr);
        TestMalleableReservations.topologyClient.addOrEditDomain(aedt2);

        // increment sequence-number or domain-changes will not be handled
        auxSequenceNr++;

        // InterdomainLink Domain2.endpoint4-> Domain3.endpoint1
        idlt = new InterdomainLinkType();
        idlt.setCosts(Helpers.getPositiveRandomInt());
        idlt.setDestinationDomain(TestMalleableReservations.referenceDomain3.getName());
        idlt.setLinkID(Helpers.getRandomString());
        idlt.setSourceEndpoint(end2_4.toJaxb());
        aedt2.getDomain().getInterdomainLink().add(idlt);
        aedt2.getDomain().setSequenceNumber(auxSequenceNr);
        TestMalleableReservations.topologyClient.addOrEditDomain(aedt2);

        // InterdomainLink Domain3.endpoint1 -> Domain2.endpoint4
        idlt2 = new InterdomainLinkType();
        idlt2.setCosts(Helpers.getPositiveRandomInt());
        idlt2.setDestinationDomain(TestMalleableReservations.referenceDomain2.getName());
        idlt2.setLinkID(idlt.getLinkID());
        idlt2.setSourceEndpoint(end3_1.toJaxb());
        aedt3.getDomain().getInterdomainLink().add(idlt2);
        aedt3.getDomain().setSequenceNumber(auxSequenceNr);
        TestMalleableReservations.topologyClient.addOrEditDomain(aedt3);

        // increment sequence-number or domain-changes will not be handled
        auxSequenceNr++;

        // InterdomainLink Domain2.endpoint5-> Domain3.endpoint2
        idlt = new InterdomainLinkType();
        idlt.setCosts(Helpers.getPositiveRandomInt());
        idlt.setDestinationDomain(TestMalleableReservations.referenceDomain3.getName());
        idlt.setLinkID(Helpers.getRandomString());
        idlt.setSourceEndpoint(end2_5.toJaxb());
        aedt2.getDomain().getInterdomainLink().add(idlt);
        aedt2.getDomain().setSequenceNumber(auxSequenceNr);
        TestMalleableReservations.topologyClient.addOrEditDomain(aedt2);

        // InterdomainLink Domain3.endpoint2 -> Domain2.endpoint5
        idlt2 = new InterdomainLinkType();
        idlt2.setCosts(Helpers.getPositiveRandomInt());
        idlt2.setDestinationDomain(TestMalleableReservations.referenceDomain2.getName());
        idlt2.setLinkID(idlt.getLinkID());
        idlt2.setSourceEndpoint(end3_2.toJaxb());
        aedt3.getDomain().getInterdomainLink().add(idlt2);
        aedt3.getDomain().setSequenceNumber(auxSequenceNr);
        TestMalleableReservations.topologyClient.addOrEditDomain(aedt3);

        // increment sequence-number or domain-changes will not be handled
        auxSequenceNr++;

        // InterdomainLink Domain2.endpoint6-> Domain3.endpoint3
        idlt = new InterdomainLinkType();
        idlt.setCosts(Helpers.getPositiveRandomInt());
        idlt.setDestinationDomain(TestMalleableReservations.referenceDomain3.getName());
        idlt.setLinkID(Helpers.getRandomString());
        idlt.setSourceEndpoint(end2_6.toJaxb());
        aedt2.getDomain().getInterdomainLink().add(idlt);
        aedt2.getDomain().setSequenceNumber(auxSequenceNr);
        TestMalleableReservations.topologyClient.addOrEditDomain(aedt2);

        // InterdomainLink Domain3.endpoint3 -> Domain2.endpoint6
        idlt2 = new InterdomainLinkType();
        idlt2.setCosts(Helpers.getPositiveRandomInt());
        idlt2.setDestinationDomain(TestMalleableReservations.referenceDomain2.getName());
        idlt2.setLinkID(idlt.getLinkID());
        idlt2.setSourceEndpoint(end3_3.toJaxb());
        aedt3.getDomain().getInterdomainLink().add(idlt2);
        aedt3.getDomain().setSequenceNumber(auxSequenceNr);
        TestMalleableReservations.topologyClient.addOrEditDomain(aedt3);

        // get two random endpoints in the domains for malleable reservation
        TestMalleableReservations.sourceTNA1 = Config.getString(Constants.testProperties,
                "test.malleable.domain1.endpoint.user1");
        TestMalleableReservations.destinationTNA1 = Config.getString(Constants.testProperties,
                "test.malleable.domain3.endpoint.user1");
        TestMalleableReservations.sourceTNA2 = Config.getString(Constants.testProperties,
        		"test.malleable.domain1.endpoint.user2");
        TestMalleableReservations.destinationTNA2 = Config.getString(Constants.testProperties,
        		"test.malleable.domain3.endpoint.user2");
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        // delete reservation if necessary
        if (!TestMalleableReservations.reservationId.equals("")) {
            Reservation res = Reservation.load(TestMalleableReservations.reservationId);
            if(res != null) res.delete();
        }
        // delete reference domain from topology client
        if (TestMalleableReservations.referenceDomain != null)
            TestMalleableReservations.topologyClient.deleteDomain(TestMalleableReservations.referenceDomain.getName());
        if (TestMalleableReservations.referenceDomain2 != null)
            TestMalleableReservations.topologyClient.deleteDomain(TestMalleableReservations.referenceDomain2.getName());
        if (TestMalleableReservations.referenceDomain3 != null)
            TestMalleableReservations.topologyClient.deleteDomain(TestMalleableReservations.referenceDomain3.getName());
    }

    /**
     * Test method for creating a malleable reservation.
     * 
     * @throws Exception
     */
    @Test
    public final void testMalleable() throws Exception {
        CreateReservationResponseType responseType = this.reservationClient
                .createMalleableReservation(TestMalleableReservations.sourceTNA1,
                        					TestMalleableReservations.destinationTNA1,
                        					TestMalleableReservations.dataAmount,
                        					Helpers.generateXMLCalendar(60, 0),
                        					Helpers.generateXMLCalendar(180, 0),
                        					100, 500, 50);

        // check response
        Assert.assertTrue("reservation-ID should be set", responseType
                .isSetReservationID());
        TestMalleableReservations.reservationId = responseType.getReservationID();
    }
    
    /**
     * Test method for creating a wrong malleable reservation (timeslot too short)
     * 
     * @throws Exception
     */
    @Test(expected = PathNotFoundFaultException.class)
    public final void testWrongMalleable() throws Exception {
    	this.reservationClient.createMalleableReservation(
    								TestMalleableReservations.sourceTNA1,
                					TestMalleableReservations.destinationTNA1,
                					TestMalleableReservations.dataAmount,
                					Helpers.generateXMLCalendar(60, 0),
                					Helpers.generateXMLCalendar(61, 0),
                					100, 500, 50);
    }

    //@Test
    public final void testMultipleService() throws Exception {
        final CreateReservationType resReq = new CreateReservationType();

        // service 1
        final ServiceConstraintType service1 = new ServiceConstraintType();
        final MalleableReservationConstraintType constraints1 =
                new MalleableReservationConstraintType();
        final ConnectionConstraintType connection1 =
                new ConnectionConstraintType();

        constraints1.setStartTime(Helpers.generateXMLCalendar(60, 0));
        constraints1.setDeadline(Helpers.generateXMLCalendar(120, 0));

        final EndpointType src1 = new EndpointType();
        src1.setEndpointId(TestMalleableReservations.sourceTNA1);
        final EndpointType dst1 = new EndpointType();
        dst1.setEndpointId(TestMalleableReservations.destinationTNA1);

        connection1.setDataAmount(TestMalleableReservations.dataAmount);
        connection1.setDirectionality(1);
        connection1.setMinBW(100);
        connection1.setMaxBW(500);
        connection1.setMaxDelay(50);
        connection1.setSource(src1);
        connection1.getTarget().add(dst1);
        connection1.setConnectionID(1); // Fix for ARGIA system

        service1.setTypeOfReservation(ReservationType.MALLEABLE);
        service1.setMalleableReservationConstraints(constraints1);
        service1.setAutomaticActivation(true);
        service1.setServiceID(1); // Fix for ARGIA system

        service1.getConnections().add(connection1);

        // service 2
        final ServiceConstraintType service2 = new ServiceConstraintType();
        final MalleableReservationConstraintType constraints2 =
            new MalleableReservationConstraintType();
        final ConnectionConstraintType connection2 =
            new ConnectionConstraintType();

        constraints2.setStartTime(Helpers.generateXMLCalendar(121, 0));
        constraints2.setDeadline(Helpers.generateXMLCalendar(180, 0));

        service2.setTypeOfReservation(ReservationType.MALLEABLE);
        service2.setMalleableReservationConstraints(constraints2);
        service2.setAutomaticActivation(true);
        service2.setServiceID(2); // Fix for ARGIA system

        final EndpointType src2 = new EndpointType();
        src2.setEndpointId(TestMalleableReservations.sourceTNA2);
        final EndpointType dst2 = new EndpointType();
        dst2.setEndpointId(TestMalleableReservations.destinationTNA2);

        connection2.setDataAmount(TestMalleableReservations.dataAmount);
        connection2.setDirectionality(1);
        connection2.setMinBW(100);
        connection2.setMaxBW(500);
        connection2.setMaxDelay(50);
        connection2.setSource(src2);
        connection2.getTarget().add(dst2);
        connection2.setConnectionID(1); // Fix for ARGIA system

        service2.getConnections().add(connection2);

        resReq.setJobID(Helpers.getPositiveRandomLong());
        resReq.getService().add(service1);
        resReq.getService().add(service2);

    	this.reservationClient.createMalleableReservation(resReq);
    }

    //@Test // not testable with dummy-adapter, because of too few configurations
    // but manually this test can be run for checking with real adapters
    public final void testMultipleConnections() throws Exception {
        final CreateReservationType resReq = new CreateReservationType();
        final ServiceConstraintType service = new ServiceConstraintType();
        final MalleableReservationConstraintType constraints =
            new MalleableReservationConstraintType();

        constraints.setStartTime(Helpers.generateXMLCalendar(60, 0));
        constraints.setDeadline(Helpers.generateXMLCalendar(180, 0));

        service.setTypeOfReservation(ReservationType.MALLEABLE);
        service.setMalleableReservationConstraints(constraints);
        service.setAutomaticActivation(true);
        service.setServiceID(1); // Fix for ARGIA system

        // connection 1
        final ConnectionConstraintType connection1 =
                new ConnectionConstraintType();

        final EndpointType src1 = new EndpointType();
        src1.setEndpointId(TestMalleableReservations.sourceTNA1);
        final EndpointType dst1 = new EndpointType();
        dst1.setEndpointId(TestMalleableReservations.destinationTNA1);

        connection1.setDataAmount(TestMalleableReservations.dataAmount);
        connection1.setDirectionality(1);
        connection1.setMinBW(100);
        connection1.setMaxBW(500);
        connection1.setMaxDelay(50);
        connection1.setSource(src1);
        connection1.getTarget().add(dst1);
        connection1.setConnectionID(1); // Fix for ARGIA system

        service.getConnections().add(connection1);

        // connection 2
        final ConnectionConstraintType connection2 =
            new ConnectionConstraintType();

        final EndpointType src2 = new EndpointType();
        src2.setEndpointId(TestMalleableReservations.sourceTNA2);
        final EndpointType dst2 = new EndpointType();
        dst2.setEndpointId(TestMalleableReservations.destinationTNA2);

        connection2.setDataAmount(TestMalleableReservations.dataAmount);
        connection2.setDirectionality(1);
        connection2.setMinBW(100);
        connection2.setMaxBW(500);
        connection2.setMaxDelay(50);
        connection2.setSource(src2);
        connection2.getTarget().add(dst2);
        connection2.setConnectionID(2); // Fix for ARGIA system

        service.getConnections().add(connection2);

        resReq.setJobID(Helpers.getPositiveRandomLong());
        resReq.getService().add(service);

    	this.reservationClient.createMalleableReservation(resReq);
    }
}
