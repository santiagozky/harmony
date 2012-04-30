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


package org.opennaas.extensions.idb.test.webservice;

import java.util.Date;
import java.util.List;

import org.apache.muse.ws.addressing.soap.SoapFault;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DomainInformationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DomainTechnologyType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointInterfaceType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetDomainsResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetEndpointsResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetLinksResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.InterdomainLinkType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.Link;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.DomainAlreadyExistsFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.DomainNotFoundFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.EndpointAlreadyExistsFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.EndpointNotFoundFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.InvalidRequestFaultException;
import org.opennaas.core.utils.Helpers;
import org.opennaas.extensions.idb.database.hibernate.Domain;
import org.opennaas.extensions.idb.topology.handler.TopologyRequestHandler;
import org.opennaas.extensions.idb.utils.TopologyHelpers;
import org.opennaas.extensions.idb.test.AbstractTest;

/**
 * JUnit test cases for the network service plane web service.
 * 
 * @author Alexander Zimmermann (zimmerm2@cs.uni-bonn.de)
 * @version $Id: TestTopologyWebservice.java 2352 2008-02-25 14:09:02Z
 *          willner@cs.uni-bonn.de $
 */
public class TestTopologyWebservice extends AbstractTopologyTest {

    /**
     * Setup before creating test suite.
     * 
     * @throws java.lang.Exception
     *             An exception.
     */
    @BeforeClass
    public static final void setUpBeforeClass() throws Exception {
        // nothing
    }

    /**
     * Tear down after test suite ran.
     * 
     * @throws java.lang.Exception
     *             An exception.
     */
    @AfterClass
    public static final void tearDownAfterClass() throws Exception {
        // ...
    }

    /** TestDomain-ID. */
    private String domainID = "TestDomain";
    /** Second test domain used for interdomain link stuff. */
    private final String destdomainID = "InterdomainLinkDest";

    /** first TestEndpoint-ID. */
    private final String endpointID1 = "192.188.0.1";
    /** second TestEndpoint-ID. */
    private final String endpointID2 = "192.188.0.2";
    private DomainInformationType domain = null;
    private DomainInformationType destdomain = null;
    private DomainInformationType domainNew = null;
    private EndpointType endpoint1 = null;
    private EndpointType endpoint2 = null;
    private EndpointType endpoint1New = null;

    private Link link = null;

    private Link linkNew = null;

    public TestTopologyWebservice() throws SoapFault {
        super();

        try {
            final long time = (new Date()).getTime();
            final int p1 = (int) (time / 256 % 256);
            final int p2 = (int) (time % 256);

            final String prefix = "" + p1 + "." + p2;

            this.domainID = this.domainID + (time % 10000);

            this.domain = new DomainInformationType();
            this.domain.setDomainId(this.domainID);
            this.domain.setDescription("Domain to be tested");
            this.domain.setReservationEPR("TestResURI");
            this.domain.setTopologyEPR("TestTopURI");
            this.domain.getTNAPrefix().add(prefix + ".1.0/24");
            this.domain.getTNAPrefix().add(prefix + ".2.0/24");
            this.domain.getTNAPrefix().add(prefix + ".3.0/24");
            final InterdomainLinkType l = new InterdomainLinkType();
            final EndpointType be1 = new EndpointType();
            be1.setEndpointId(prefix + ".1.101");
            be1.setDomainId(this.domain.getDomainId());
            l.setSourceEndpoint(be1);
            l.setLinkID("link1");
            l.setCosts(Integer.valueOf(4));
            l.setDestinationDomain(this.destdomainID);
            this.domain.getInterdomainLink().add(l);

            this.destdomain = new DomainInformationType();
            this.destdomain.setDomainId(this.destdomainID);
            this.destdomain.setReservationEPR("junk");
            this.destdomain.setTopologyEPR("junk");
            final InterdomainLinkType ldest = new InterdomainLinkType();
            final EndpointType bedest = new EndpointType();
            bedest.setEndpointId("0.0.0.0");
            bedest.setDomainId(this.destdomain.getDomainId());
            ldest.setSourceEndpoint(bedest);
            ldest.setLinkID(l.getLinkID());
            ldest.setCosts(Integer.valueOf(3));
            ldest.setDestinationDomain(this.domainID);
            this.destdomain.getInterdomainLink().add(ldest);

            // don't clone, or we are using the same TNAPrefixList!
            this.domainNew = new DomainInformationType();
            this.domainNew.setDomainId(this.domainID);
            this.domainNew.setDescription("New description");
            this.domainNew.setReservationEPR("NewTestResURI");
            this.domainNew.setTopologyEPR("NewTestTopURI");
            this.domainNew.getTNAPrefix().add(prefix + ".1.0/24");
            this.domainNew.getTNAPrefix().add(prefix + ".2.0/24");
            this.domainNew.getTNAPrefix().add(prefix + ".3.0/24");
            this.domainNew.getTNAPrefix().add(prefix + ".4.0/24");
            this.domainNew.getTNAPrefix().add(prefix + ".5.0/24");
            final InterdomainLinkType l2 = new InterdomainLinkType();
            final EndpointType be2 = new EndpointType();
            be2.setEndpointId(prefix + ".1.201");
            be2.setDomainId(this.domainNew.getDomainId());
            l2.setSourceEndpoint(be2);
            l2.setLinkID("link1");
            l2.setCosts(Integer.valueOf(2));
            l2.setDestinationDomain(this.destdomainID);
            this.domainNew.getInterdomainLink().add(l2);

            this.endpoint1 = new EndpointType();
            this.endpoint2 = new EndpointType();

            this.endpoint1.setEndpointId(this.endpointID1);
            this.endpoint1.setName("Test-Endpoint 1");
            this.endpoint1.setDescription("Endpoint 1 to be tested");
            this.endpoint1.setDomainId(this.domainID);
            this.endpoint1.setBandwidth(Integer.valueOf(1));
            this.endpoint1.setInterface(EndpointInterfaceType.USER);

            this.endpoint1New = this.endpoint1.clone();
            this.endpoint1New.setName("Test-Endpoint 1, new name");
            this.endpoint1New.setDescription("Endpoint 1, new description");
            this.endpoint1New.setBandwidth(Integer.valueOf(33));
            this.endpoint1New.setInterface(EndpointInterfaceType.BORDER);

            this.endpoint2.setEndpointId(this.endpointID2);
            this.endpoint2.setName("Test-Endpoint 2");
            this.endpoint2.setDescription("Endpoint 2 to be tested");
            this.endpoint2.setDomainId(this.domainID);
            this.endpoint2.setBandwidth(Integer.valueOf(1));
            this.endpoint2.setInterface(EndpointInterfaceType.USER);

            this.link = new Link();
            this.link.setName("TestLink");
            this.link.setDescription("Link to be tested");
            this.link.setSourceEndpoint(this.endpointID1);
            this.link.setDestinationEndpoint(this.endpointID2);

            this.linkNew = this.link.clone();
            this.linkNew.setName("TestLinkNew");
            this.linkNew.setDescription("Link to be tested, new description");
        } catch (final CloneNotSupportedException e) {
            // I *know* they are cloneable! This never happens.
            e.printStackTrace();
        }
    }

    /**
     * Tear down before a test.
     * 
     * @throws java.lang.Exception
     *             An exception.
     */
    @After
    public void cleanUp() throws Exception {
        try {
            this.deleteDomain(this.domainID);
            this.deleteEndpoint(this.endpointID1);
            this.deleteEndpoint(this.endpointID2);
        } catch (final DomainNotFoundFaultException e) {
            // nasty ;)
        } catch (final EndpointNotFoundFaultException e) {
            // nasty ;)
        } catch (final Exception e) {
            e.printStackTrace(); // even more nasty ;)
        }
    }

    /*-------------------------------------------------------------------------
     * TESTS
     *-----------------------------------------------------------------------*/

    /**
     * Test that adds and deletes a domain.
     * 
     * @throws Exception
     *             Any Exception that can occur during test-run
     */
    @Test(timeout = AbstractTest.TIMEOUT)
    public final void testAddDeleteDomain() throws Exception {
        this.addDomain(this.domain);
        this.deleteDomain(this.domainID);
    }

    /**
     * Test that adds and deletes an endpoint.
     * 
     * @throws Exception
     *             Any Exception that can occur during test-run
     */
    @Test(timeout = AbstractTest.TIMEOUT)
    public final void testAddDeleteEndpoint() throws Exception {
        this.addDomain(this.domain);
        this.addEndpoint(this.endpoint1);
        this.deleteEndpoint(this.endpointID1);
    }

    /**
     * Test that adds and then deletes a link.
     * 
     * @throws Exception
     *             Any Exception that can occur during test-run
     */
    // @Test(timeout = TestTopologyWebservice.TIMEOUT)
    public final void testAddDeleteLink() throws Exception {
        this.addDomain(this.domain);
        this.addEndpoint(this.endpoint1);
        this.addEndpoint(this.endpoint2);
        this.addLink(this.link);
        this.deleteLink(this.link.getSourceEndpoint(), this.link
                .getDestinationEndpoint());
    }

    /**
     * Test that adds a duplicate domain (should result in corresponding error
     * msg).
     * 
     * @throws Exception
     * @throws Exception
     * @throws Exception
     *             Any Exception that can occur during test-run
     */
    @Test(timeout = AbstractTest.TIMEOUT)
    public final void testAddDupeDomain() throws Exception {
        this.addDomain(this.domain);
        try {
            this.addDomain(this.domain);
        } catch (final Exception e) {
            Assert.assertTrue(
                    "System should send an DomainAlreadyExistsFaultException!",
                    DomainAlreadyExistsFaultException.class.isInstance(e));
        }
        this.deleteDomain(this.domain.getDomainId());
    }

    /**
     * Test that adds a duplicate endpoint (should result in corresponding error
     * msg).
     * 
     * @throws Exception
     *             Any Exception that can occur during test-run
     */
    @Test(timeout = AbstractTest.TIMEOUT, expected = EndpointAlreadyExistsFaultException.class)
    public final void testAddDupeEndpoint() throws Exception {
        this.addDomain(this.domain);
        this.addEndpoint(this.endpoint1);
        this.addEndpoint(this.endpoint1);
    }

    /**
     * Test that adds a existing link.
     * 
     * @throws Exception
     *             Any Exception that can occur during test-run
     */
    // @Test(expected = LinkAlreadyExistsFaultException.class)
    public final void testAddDuplicateLink() throws Exception {
        this.addDomain(this.domain);
        this.addEndpoint(this.endpoint1);
        this.addEndpoint(this.endpoint2);
        this.addLink(this.link);
        this.addLink(this.link);
    }

    /**
     * Test that adds, edits, and deletes a domain.
     * 
     * @throws Exception
     *             Any Exception that can occur during test-run
     */
    @Test(timeout = AbstractTest.TIMEOUT)
    public final void testAddEditDeleteDomain() throws Exception {
        this.domain.setSequenceNumber(Integer.valueOf(1));
        this.addDomain(this.domain);

        for (final DomainInformationType domain : this.getDomains()
                .getDomains()) {
            if (domain.getDescription().equals(this.domainNew.getDescription())) {
                Assert.fail("New description should not exist.");
            }
        }

        this.domainNew.setSequenceNumber(this.domain.getSequenceNumber() +1);
        DomainTechnologyType dtt = new DomainTechnologyType();
        dtt.getDomainSupportedAdaptation().add(Helpers.getRandomString());
        dtt.getDomainSupportedSwitchMatrix().add(Helpers.getRandomString());
        dtt.getDomainSupportedBandwidth().add(Helpers.getPositiveRandomLong());
        this.domainNew.setTechnology(dtt);
        this.editDomain(this.domainNew);

        boolean found = false;
        boolean technologyOk = false;
        for (final DomainInformationType domain : this.getDomains()
                .getDomains()) {
            if (domain.getDescription().equals(this.domainNew.getDescription())) {
                found = true;
            }
            DomainTechnologyType editedDtt = domain.getTechnology();
            if (editedDtt != null) {
            	if(editedDtt.getDomainSupportedAdaptation().get(0).equals(
            			dtt.getDomainSupportedAdaptation().get(0))
            	   && editedDtt.getDomainSupportedBandwidth().get(0).equals(
            	   		dtt.getDomainSupportedBandwidth().get(0))
            	   && editedDtt.getDomainSupportedSwitchMatrix().get(0).equals(
            	   		dtt.getDomainSupportedSwitchMatrix().get(0))
            	) {
            		technologyOk = true;
            	}
            }
        }
        Assert.assertTrue("New description should be set.", found);
        Assert.assertTrue("new Technology type should be set.", technologyOk);

        this.deleteDomain(this.domain.getDomainId());
    }

    /**
     * Test that adds, edits, and deletes an endpoint.
     * 
     * @throws Exception
     *             Any Exception that can occur during test-run
     */
    @Test(timeout = AbstractTest.TIMEOUT)
    public final void testAddEditDeleteEndpoint() throws Exception {
        this.addDomain(this.domain);
        this.addEndpoint(this.endpoint1);
        this.editEndpoint(this.endpoint1New);
        this.deleteEndpoint(this.endpointID1);
    }

    /**
     * Test that adds, edits, and then deletes a link.
     * 
     * @throws Exception
     *             Any Exception that can occur during test-run
     */
    // @Test(timeout = TestTopologyWebservice.TIMEOUT)
    public final void testAddEditDeleteLink() throws Exception {
        this.addDomain(this.domain);
        this.addEndpoint(this.endpoint1);
        this.addEndpoint(this.endpoint2);
        this.addLink(this.link);
        this.editLink(this.linkNew);
        this.deleteLink(this.link.getSourceEndpoint(), this.link
                .getDestinationEndpoint());
    }

    /**
     * Test that adds, edits, gets, and deletes a domain.
     * 
     * @throws Exception
     *             Any Exception that can occur during test-run
     */
    @Test(timeout = AbstractTest.TIMEOUT)
    public final void testAddEditGetDeleteDomain() throws Exception {
        this.domain.setSequenceNumber(1);
        this.addDomain(this.domain);
        this.domainNew.setSequenceNumber(this.domain.getSequenceNumber() + 1);
        this.editDomain(this.domainNew);
        final GetDomainsResponseType response = this.getDomains();
        boolean check = false;
        for (final DomainInformationType dom : response.getDomains()) {
            if (dom.getDomainId().equals(this.domainID)) {
                Assert.assertFalse(check);
                Assert.assertTrue(dom.getDescription().equals(
                        this.domainNew.getDescription()));
                Assert.assertTrue(dom.getTNAPrefix().size() == this.domainNew
                        .getTNAPrefix().size());
                for (final String prefix : this.domainNew.getTNAPrefix()) {
                    Assert.assertTrue(dom.getTNAPrefix().contains(prefix));
                }
                check = true;
            }
        }
        Assert.assertTrue(check);
        this.deleteDomain(this.domain.getDomainId());
    }

    /**
     * Test that adds, edits, gets, and deletes an endpoint.
     * 
     * @throws Exception
     *             Any Exception that can occur during test-run
     */
    @Test(timeout = AbstractTest.TIMEOUT)
    public final void testAddEditGetDeleteEndpoint() throws Exception {
        this.addDomain(this.domain);
        this.addEndpoint(this.endpoint1);
        this.editEndpoint(this.endpoint1New);
        final GetEndpointsResponseType response = this
                .getEndpoints(this.domainID);
        boolean check = false;
        for (final EndpointType ep : response.getEndpoints()) {
            if (ep.getEndpointId().equals(this.endpointID1)) {
                Assert.assertFalse(check);
                Assert.assertTrue(ep.getDescription().equals(
                        this.endpoint1New.getDescription()));
                check = true;
            }
        }
        Assert.assertTrue("Should be true", check);
        this.deleteEndpoint(this.endpointID1);
    }

    @Test(timeout = AbstractTest.TIMEOUT, expected = InvalidRequestFaultException.class)
    public final void testAddEndpointWithoutDomain() throws Exception {
        this.addDomain(this.domain);
        final EndpointType e = new EndpointType();
        e.setEndpointId(this.endpointID1);
        e.setInterface(EndpointInterfaceType.USER);
        this.addEndpoint(e);
    }

    @Test(timeout = AbstractTest.TIMEOUT, expected = InvalidRequestFaultException.class)
    public final void testAddEndpointWithoutInterfaceType() throws Exception {
        this.addDomain(this.domain);
        final EndpointType e = new EndpointType();
        e.setEndpointId(this.endpointID1);
        e.setDomainId(this.domain.getDomainId());
        this.addEndpoint(e);
    }

    /**
     * Test that adds, gets, and then deletes a link.
     * 
     * @throws Exception
     *             Any Exception that can occur during test-run
     */
    // @Test(expected = EndpointNotFoundFaultException.class)
    public final void testAddFalseLink() throws Exception {
        final Link falseLink = new Link();
        falseLink.setName("falseTestLink");
        falseLink.setDescription("false Link to be tested");
        falseLink.setSourceEndpoint(TopologyHelpers.getRandomTNA());
        falseLink.setDestinationEndpoint(TopologyHelpers.getRandomTNA());
        this.addLink(falseLink);
    }

    /**
     * Test that adds, gets, and deletes a domain.
     * 
     * @throws Exception
     *             Any Exception that can occur during test-run
     */
    @Test(timeout = AbstractTest.TIMEOUT)
    public final void testAddGetDeleteDomain() throws Exception {
        this.addDomain(this.domain);
        this.getDomains();
    }

    /**
     * Test adds, gets and deletes 4 domains. Therefore it has to be checked if
     * the returned domains equals the added domains.
     */
    @Test(timeout = AbstractTest.TIMEOUT)
    public final void testAddGetDeleteDomains() throws Exception {
        // create TestDomains
        final List<Domain> testDomains = TopologyHelpers.getTestDomains();
        // save all Domains in DB over webservice
        for (final Domain dom : testDomains) {
            this.addDomain(dom.toJaxb());
        }
        // get all Domains from DB over webservice
        final GetDomainsResponseType responseType = this.getDomains();
        // check if created domains are existent in result
        final List<String> resultDomainIds = this
                .getDomainIdsFromResponse(responseType);
        for (final Domain dom : testDomains) {
            Assert.assertTrue(resultDomainIds.contains(dom.getName()));
        }
        // delete created Domains from DB over webservice
        for (final Domain dom : testDomains) {
            this.deleteDomain(dom.getName());
        }
    }

    /**
     * Test that adds, gets, and deletes an endpoint.
     * 
     * @throws Exception
     *             Any Exception that can occur during test-run
     */
    @Test(timeout = AbstractTest.TIMEOUT)
    public final void testAddGetDeleteEndpoint() throws Exception {
        this.addDomain(this.domain);
        this.addEndpoint(this.endpoint1);
        this.getEndpoints(this.domainID);
        this.deleteEndpoint(this.endpointID1);
    }

    /**
     * Test that adds, gets, and then deletes a link.
     * 
     * @throws Exception
     *             Any Exception that can occur during test-run
     */
    // @Test(timeout = TestTopologyWebservice.TIMEOUT)
    public final void testAddGetDeleteLink() throws Exception {
        this.addDomain(this.domain);
        this.addEndpoint(this.endpoint1);
        this.addEndpoint(this.endpoint2);
        this.addLink(this.link);
        this.getLinks(this.domainID);
        this.deleteLink(this.link.getSourceEndpoint(), this.link
                .getDestinationEndpoint());
    }

//    @Test(timeout = AbstractTest.TIMEOUT)
    public final void testAutoInterdomainLink() throws Exception {
        this.addDomain(this.domain);
        this.addDomain(this.destdomain);
        final GetLinksResponseType response = this.getLinks(this.domainID);
        for (final Link link : response.getLink()) {
            System.out.println(link.getSourceEndpoint() + "-"
                    + link.getDestinationEndpoint());
        }
        this.deleteDomain(this.destdomainID);
    }

    /**
     * Test that tries to edit a domain not known to the IDB, therefore a
     * DomainNotFoundFaultException should be thrown.
     * 
     * @throws Exception
     *             Any Exception that can occur during test-run
     */
    // not practicable, a unknown domain will be added automatically by the IDB
    // @Test(timeout = TestTopologyWebservice.TIMEOUT, expected =
    // DomainNotFoundFaultException.class)
    public final void testEditUnknownDomain() throws Exception {
        this.editDomain(this.domainNew);
    }

    /*-------------------------------------------------------------------------
     * COMMON TOPOLOGY TESTS
     *-----------------------------------------------------------------------*/

    /**
     * Test singleton cloning method.
     */
    @Test(timeout = AbstractTest.TIMEOUT, expected = CloneNotSupportedException.class)
    public final void testSingletonCloning() throws Exception {
        TopologyRequestHandler.getInstance().clone();
    }
}
