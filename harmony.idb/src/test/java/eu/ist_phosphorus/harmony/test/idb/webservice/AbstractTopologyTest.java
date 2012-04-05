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


package eu.ist_phosphorus.harmony.test.idb.webservice;

import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

import org.apache.muse.ws.addressing.soap.SoapFault;
import org.junit.After;
import org.junit.Assert;
import org.w3c.dom.Element;

import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.AddDomainResponse;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.AddDomainResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.AddDomainType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.AddEndpoint;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.AddEndpointResponse;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.AddEndpointResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.AddEndpointType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.AddLink;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.AddLinkResponse;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.AddLinkResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.AddLinkType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.DeleteDomain;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.DeleteDomainResponse;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.DeleteDomainResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.DeleteDomainType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.DeleteEndpoint;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.DeleteEndpointResponse;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.DeleteEndpointResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.DeleteEndpointType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.DeleteLink;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.DeleteLinkResponse;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.DeleteLinkResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.DeleteLinkType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.DomainInformationType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EditDomain;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EditDomainResponse;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EditDomainResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EditDomainType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EditEndpoint;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EditEndpointResponse;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EditEndpointResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EditEndpointType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EditLink;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EditLinkResponse;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EditLinkResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EditLinkType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EndpointType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetDomains;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetDomainsResponse;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetDomainsResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetDomainsType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetEndpoints;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetEndpointsResponse;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetEndpointsResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetEndpointsType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetLinks;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetLinksResponse;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetLinksResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetLinksType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.Link;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.LinkIdentifierType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.utils.JaxbSerializer;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.utils.WebserviceUtils;
import eu.ist_phosphorus.harmony.common.serviceinterface.topology.SimpleTopologyClient;
import eu.ist_phosphorus.harmony.common.utils.Config;
import eu.ist_phosphorus.harmony.idb.Constants;
import eu.ist_phosphorus.harmony.idb.database.hibernate.Domain;
import eu.ist_phosphorus.harmony.idb.exceptions.database.DatabaseException;
import eu.ist_phosphorus.harmony.idb.webservice.TopologyWS;
import eu.ist_phosphorus.harmony.test.idb.AbstractTest;

/**
 * Abstract class for all webservice based tests.
 * 
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 * @version $Id: AbstractTest.java 619 2007-08-29 19:04:26Z
 *          willner@cs.uni-bonn.de $
 */
public abstract class AbstractTopologyTest extends AbstractTest {
    private static final String topologyEpr = Config.getString(Constants.testProperties,
            "epr.topology");

    public static void deleteTestTopology(final Domain domain)
            throws DatabaseException {
        domain.delete();
    }

    public static void setupTestTopology(final Domain domain1) throws SoapFault {
        final AddDomainType addDomainType = new AddDomainType();
        addDomainType.setDomain(domain1.toJaxb());
        final Element addDomainReq = WebserviceUtils
                .createAddDomainRequest(addDomainType);
        new SimpleTopologyClient(AbstractTopologyTest.topologyEpr)
                .addDomain(addDomainReq);
        System.out.println("saved domain " + domain1);
    }

    /**
     * Topology client.
     */
    protected SimpleTopologyClient topologyClient;


    /**
     * Default constructor.
     * 
     * @throws SoapFault
     * @throws URISyntaxException
     * @throws URISyntaxException
     *             If the given EPR is not a valid URI.
     */
    protected AbstractTopologyTest() throws SoapFault {
        if (Config.isTrue(Constants.testProperties, "test.callWebservice")) {
            this.topologyClient = new SimpleTopologyClient(
                    AbstractTopologyTest.topologyEpr);
        } else {
            this.topologyClient = new SimpleTopologyClient(new TopologyWS());
        }
    }

    /**
     * Add test domain.
     * 
     * @throws SoapFault
     * @throws Exception
     *             Any Exception that can occur.
     */
    protected AddDomainResponseType addDomain(final DomainInformationType dom)
            throws SoapFault {
        final AddDomainType requestType = new AddDomainType();
        requestType.setDomain(dom);
        /* serializing the request ----------------------------------------- */
        final Element requestElement = WebserviceUtils
                .createAddDomainRequest(requestType);
        /* ----------------------------------------------------------------- */

        Element responseElement;
        responseElement = this.topologyClient.addDomain(requestElement);

        final AddDomainResponseType response = ((AddDomainResponse) JaxbSerializer
                .getInstance().elementToObject(responseElement))
                .getAddDomainResponse();
        Assert.assertTrue("AddDomain success check", response.isSuccess());
        return response;
    }

    protected final AddEndpointResponseType addEndpoint(final EndpointType e)
            throws Exception {
        final AddEndpoint request = new AddEndpoint();
        final AddEndpointType requestType = new AddEndpointType();
        requestType.setEndpoint(e);
        request.setAddEndpoint(requestType);
        /* serializing the request ----------------------------------------- */
        final Element requestElement = JaxbSerializer.getInstance()
                .objectToElement(request);
        /* ----------------------------------------------------------------- */

        Element responseElement;
        responseElement = this.topologyClient.addEndpoint(requestElement);
        final AddEndpointResponseType response = ((AddEndpointResponse) JaxbSerializer
                .getInstance().elementToObject(responseElement))
                .getAddEndpointResponse();
        Assert.assertTrue("AddEndpoints result-check", response.isSuccess());
        return response;
    }

    protected final AddLinkResponseType addLink(final Link l) throws Exception {
        final AddLink request = new AddLink();
        final AddLinkType requestType = new AddLinkType();

        /* generating valid AddLink-Request -------------------------------- */

        requestType.setLinkData(l);
        request.setAddLink(requestType);
        /* ----------------------------------------------------------------- */

        /* serializing the request ----------------------------------------- */
        final Element requestElement = JaxbSerializer.getInstance()
                .objectToElement(request);
        /* ----------------------------------------------------------------- */

        Element responseElement;
        responseElement = this.topologyClient.addLink(requestElement);
        final AddLinkResponseType response = ((AddLinkResponse) JaxbSerializer
                .getInstance().elementToObject(responseElement))
                .getAddLinkResponse();
        Assert.assertTrue("AddLink result-check", response.isSuccess());
        return response;
    }

    /**
     * Delete test domain.
     * 
     * @throws Exception
     *             Any Exception that can occur during test-run
     */
    protected DeleteDomainResponseType deleteDomain(final String domID)
            throws Exception {
        final DeleteDomain request = new DeleteDomain();
        final DeleteDomainType requestType = new DeleteDomainType();

        /* generating valid DeleteEndpoints-Request ------------------------ */
        requestType.setDomainId(domID);
        request.setDeleteDomain(requestType);
        /* ----------------------------------------------------------------- */

        /* serializing the request ----------------------------------------- */
        final Element requestElement = JaxbSerializer.getInstance()
                .objectToElement(request);
        /* ----------------------------------------------------------------- */

        Element responseElement;
        responseElement = this.topologyClient.deleteDomain(requestElement);
        final DeleteDomainResponseType response = ((DeleteDomainResponse) JaxbSerializer
                .getInstance().elementToObject(responseElement))
                .getDeleteDomainResponse();
        Assert.assertTrue("DeleteDomain result-check", response.isSuccess());
        return response;
    }

    /**
     * Delete test endpoints.
     * 
     * @throws Exception
     *             Any Exception that can occur during test-run
     */
    protected final DeleteEndpointResponseType deleteEndpoint(final String eid)
            throws Exception {
        final DeleteEndpoint request = new DeleteEndpoint();
        final DeleteEndpointType requestType = new DeleteEndpointType();

        /* generating valid DeleteEndpoints-Request ------------------------ */
        requestType.setEndpoint(eid);

        request.setDeleteEndpoint(requestType);
        /* ----------------------------------------------------------------- */

        /* serializing the request ----------------------------------------- */
        final Element requestElement = JaxbSerializer.getInstance()
                .objectToElement(request);
        /* ----------------------------------------------------------------- */

        Element responseElement;
        responseElement = this.topologyClient.deleteEndpoint(requestElement);
        final DeleteEndpointResponseType response = ((DeleteEndpointResponse) JaxbSerializer
                .getInstance().elementToObject(responseElement))
                .getDeleteEndpointResponse();
        Assert.assertTrue("DeleteEndpoints result-check", response.isSuccess());
        return response;
    }

    /**
     * Delete the test link.
     * 
     * @throws Exception
     *             Any Exception that can occur during test-run
     */
    protected final DeleteLinkResponseType deleteLink(final String src,
            final String dst) throws Exception {
        final DeleteLink request = new DeleteLink();
        final DeleteLinkType requestType = new DeleteLinkType();

        /* generating valid DeleteLink-Request ----------------------------- */
        requestType.setLinkId(new LinkIdentifierType());
        requestType.getLinkId().setSourceEndpoint(src);
        requestType.getLinkId().setDestinationEndpoint(dst);
        request.setDeleteLink(requestType);
        /* ----------------------------------------------------------------- */

        /* serializing the request ----------------------------------------- */
        final Element requestElement = JaxbSerializer.getInstance()
                .objectToElement(request);
        /* ----------------------------------------------------------------- */

        Element responseElement;
        responseElement = this.topologyClient.deleteLink(requestElement);
        final DeleteLinkResponseType response = ((DeleteLinkResponse) JaxbSerializer
                .getInstance().elementToObject(responseElement))
                .getDeleteLinkResponse();
        Assert.assertTrue("DeleteLink result-check", response.isSuccess());
        return response;
    }

    /**
     * Edit test domain.
     * 
     * @throws Exception
     *             Any Exception that can occur during test-run
     */
    protected final EditDomainResponseType editDomain(
            final DomainInformationType dom) throws Exception {
        final EditDomain request = new EditDomain();
        final EditDomainType requestType = new EditDomainType();

        /* generating valid EditDomain-Request ----------------------------- */
        requestType.setDomain(dom);
        request.setEditDomain(requestType);
        /* ----------------------------------------------------------------- */

        /* serializing the request ----------------------------------------- */
        final Element requestElement = JaxbSerializer.getInstance()
                .objectToElement(request);
        /* ----------------------------------------------------------------- */

        Element responseElement;
        responseElement = this.topologyClient.editDomain(requestElement);
        final EditDomainResponseType response = ((EditDomainResponse) JaxbSerializer
                .getInstance().elementToObject(responseElement))
                .getEditDomainResponse();
        Assert.assertTrue("EditDomain request was not successfull", response
                .isSuccess());
        return response;
    }

    /**
     * Edit test endpoint #1.
     * 
     * @throws Exception
     *             Any Exception that can occur during test-run
     */
    protected final EditEndpointResponseType editEndpoint(final EndpointType e)
            throws Exception {
        final EditEndpoint request = new EditEndpoint();
        final EditEndpointType requestType = new EditEndpointType();

        /* generating valid EditEndpoint-Request --------------------------- */

        requestType.setEndpoint(e);
        request.setEditEndpoint(requestType);
        /* ----------------------------------------------------------------- */

        /* serializing the request ----------------------------------------- */
        final Element requestElement = JaxbSerializer.getInstance()
                .objectToElement(request);
        /* ----------------------------------------------------------------- */

        Element responseElement;
        responseElement = this.topologyClient.editEndpoint(requestElement);
        final EditEndpointResponseType response = ((EditEndpointResponse) JaxbSerializer
                .getInstance().elementToObject(responseElement))
                .getEditEndpointResponse();
        Assert.assertTrue("EditEndpoints result-check", response.isSuccess());
        return response;
    }

    /**
     * Edit the test link.
     * 
     * @throws Exception
     *             Any Exception that can occur during test-run
     */
    protected final EditLinkResponseType editLink(final Link l)
            throws Exception {
        final EditLink request = new EditLink();
        final EditLinkType requestType = new EditLinkType();

        /* generating valid EditLink-Request ------------------------------- */
        requestType.setLink(l);
        request.setEditLink(requestType);
        /* ----------------------------------------------------------------- */

        /* serializing the request ----------------------------------------- */
        final Element requestElement = JaxbSerializer.getInstance()
                .objectToElement(request);
        /* ----------------------------------------------------------------- */

        Element responseElement;
        responseElement = this.topologyClient.editLink(requestElement);
        final EditLinkResponseType response = ((EditLinkResponse) JaxbSerializer
                .getInstance().elementToObject(responseElement))
                .getEditLinkResponse();
        Assert.assertTrue("EditLink result-check", response.isResult());
        return response;
    }

    /**
     * extract the Domain-Elements from a GetDomainsResponseType.
     * 
     * @param GetDomainsResponseType
     * @return List<Domain>
     */
    protected List<String> getDomainIdsFromResponse(
            final GetDomainsResponseType responseType) {
        final List<String> result = new LinkedList<String>();
        for (final DomainInformationType domType : responseType.getDomains()) {
            result.add(domType.getDomainId());
        }
        return result;
    }

    /**
     * Test getDomains method.
     * 
     * @throws Exception
     *             Any Exception that can occur during test-run
     */
    protected final GetDomainsResponseType getDomains() throws Exception {
        final GetDomains request = new GetDomains();
        final GetDomainsType requestType = new GetDomainsType();

        /* generating valid GetDomains-Request ----------------------------- */
        request.setGetDomains(requestType);
        /* ----------------------------------------------------------------- */

        /* serializing the request ----------------------------------------- */
        final Element requestElement = JaxbSerializer.getInstance()
                .objectToElement(request);
        /* ----------------------------------------------------------------- */

        Element responseElement;
        responseElement = this.topologyClient.getDomains(requestElement);
        final GetDomainsResponseType response = ((GetDomainsResponse) JaxbSerializer
                .getInstance().elementToObject(responseElement))
                .getGetDomainsResponse();
        Assert.assertTrue("GetDomains result-check", response.getDomains().get(
                0).getDomainId() != "");
        return response;
    }

    /**
     * Get endpoints.
     * 
     * @throws Exception
     *             Any Exception that can occur during test-run
     */
    protected final GetEndpointsResponseType getEndpoints(final String domainID)
            throws Exception {
        final GetEndpoints request = new GetEndpoints();
        final GetEndpointsType requestType = new GetEndpointsType();

        /* generating valid GetEndpoints-Request --------------------------- */
        requestType.setDomain(domainID);

        request.setGetEndpoints(requestType);
        /* ----------------------------------------------------------------- */

        /* serializing the request ----------------------------------------- */
        final Element requestElement = JaxbSerializer.getInstance()
                .objectToElement(request);
        /* ----------------------------------------------------------------- */

        Element responseElement;
        responseElement = this.topologyClient.getEndpoints(requestElement);
        final GetEndpointsResponseType response = ((GetEndpointsResponse) JaxbSerializer
                .getInstance().elementToObject(responseElement))
                .getGetEndpointsResponse();
        // Assert.assertTrue("GetEndpoints check-result", response
        // .getEndpoints().get(0).getEndpointId() != "");
        return response;
    }

    /**
     * Get links.
     * 
     * @throws Exception
     *             Any Exception that can occur during test-run
     */
    protected final GetLinksResponseType getLinks(final String domainID)
            throws Exception {
        final GetLinks request = new GetLinks();
        final GetLinksType requestType = new GetLinksType();

        /* generating valid GetLinks-Request ------------------------------- */
        requestType.setDomainId(domainID);
        request.setGetLinks(requestType);
        /* ----------------------------------------------------------------- */

        /* serializing the request ----------------------------------------- */
        final Element requestElement = JaxbSerializer.getInstance()
                .objectToElement(request);
        /* ----------------------------------------------------------------- */

        Element responseElement;
        responseElement = this.topologyClient.getLinks(requestElement);
        final GetLinksResponseType response = ((GetLinksResponse) JaxbSerializer
                .getInstance().elementToObject(responseElement))
                .getGetLinksResponse();
        Assert.assertTrue("Should return at least one link", response.getLink()
                .size() > 0);
        return response;
    }

    @After
    public void zzzCheckDatabase() {
        // Fix tests first!
        // int domains =
        // this.topologyClient.getDomains(new GetDomainsType())
        // .getDomains().size();
        // Assert.assertEquals("Too many domain in database after test!",
        // this.domains, domains);
    }
}
