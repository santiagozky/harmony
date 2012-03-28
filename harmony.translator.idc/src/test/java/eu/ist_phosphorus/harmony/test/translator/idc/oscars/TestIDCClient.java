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


package eu.ist_phosphorus.harmony.test.translator.idc.oscars;

import junit.framework.Assert;
import net.es.oscars.wsdlTypes.CreateReply;
import net.es.oscars.wsdlTypes.GlobalReservationId;
import net.es.oscars.wsdlTypes.InitiateTopologyPullContent;
import net.es.oscars.wsdlTypes.InitiateTopologyPullResponseContent;
import net.es.oscars.wsdlTypes.Layer2Info;
import net.es.oscars.wsdlTypes.ListReply;
import net.es.oscars.wsdlTypes.ListRequest;
import net.es.oscars.wsdlTypes.ListRequestSequence_type0;
import net.es.oscars.wsdlTypes.PathInfo;
import net.es.oscars.wsdlTypes.ResCreateContent;
import net.es.oscars.wsdlTypes.ResDetails;
import net.es.oscars.wsdlTypes.VlanTag;

import org.apache.axis2.AxisFault;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.ist_phosphorus.harmony.common.utils.Config;
import eu.ist_phosphorus.harmony.common.utils.PhLogger;
import eu.ist_phosphorus.harmony.translator.idc.implementation.EndpointMapper;
import eu.ist_phosphorus.harmony.translator.idc.implementation.IDCClient;

/**
 * @author willner
 */
public class TestIDCClient {
    private static final int IDC_SIGNAL_BUFFER = 0;
    private static final int DEFAULT_BANDWIDTH = 80;
    private static final String IDC_MSG_SUCCESS = "SUCCESS";
    private final IDCClient oscarsClient;

    private GlobalReservationId gri;
    private final Logger logger = PhLogger.getLogger("idcclient");

    /**
     * @throws AAAFaultMessage
     * @throws Exception
     */
    private void cancelTestReservation() throws Exception {
        this.logger.info("Cancel reservaton id: " + this.gri.getGri());
        final String reply = this.oscarsClient.cancelReservation(this.gri);
        this.gri = null;
        this.logger.info("Reply was: " + reply);
        Assert.assertTrue("Was: " + reply, IDCClient.STATUS_ACCEPTED
                .equals(reply)
                || IDCClient.STATUS_TEARDOWN.equals(reply)
                || IDCClient.STATUS_CANCELLED.equals(reply));
        Thread.sleep(TestIDCClient.IDC_SIGNAL_BUFFER);
    }

    @After
    public void cleanUp() {
        if (null != this.gri) {
            try {
                this.oscarsClient.cancelReservation(this.gri);
            } catch (final Exception e) {
                this.logger.info("While cleanup: " + e.getMessage());
            }
        }
    }

    /**
     * @throws AAAFaultMessage
     * @throws Exception
     */
    private void createTestReservation() throws Exception {
        final Layer2Info layer2Info = new Layer2Info();
        final EndpointMapper mapper = new EndpointMapper();
        final String src =
                mapper
                        .harmonyToIdc(Config
                                .getString("test.test.endpoint0.tna"));
        final String dst =
                mapper
                        .harmonyToIdc(Config
                                .getString("test.test.endpoint1.tna"));

        layer2Info.setSrcEndpoint(src);
        layer2Info.setDestEndpoint(dst);

        final PathInfo pathInfo = new PathInfo();
        pathInfo.setPathSetupMode("timer-automatic");

        final VlanTag srcVtag = new VlanTag();
        srcVtag.setString("3216");
        srcVtag.setTagged(true);
        layer2Info.setSrcVtag(srcVtag);

        final VlanTag destVtag = new VlanTag();
        destVtag.setString("3216");
        destVtag.setTagged(true);
        layer2Info.setDestVtag(destVtag);

        final ResCreateContent request = new ResCreateContent();
        request.setStartTime(System.currentTimeMillis() / 1000);
        request.setEndTime(System.currentTimeMillis() / 1000 + 5 * 60);
        request.setBandwidth(100);
        request.setDescription("Test reservation from translator junit test");
        pathInfo.setLayer2Info(layer2Info);
        request.setPathInfo(pathInfo);
        request.setBandwidth(TestIDCClient.DEFAULT_BANDWIDTH);

        final CreateReply response =
                this.oscarsClient.createReservation(request);

        final GlobalReservationId theGRI = new GlobalReservationId();
        theGRI.setGri(response.getGlobalReservationId());
        this.gri = theGRI;
        final String status = response.getStatus();
        final VlanTag vlan =
                response.getPathInfo().getLayer2Info().getDestVtag();

        this.logger.info("Status is: " + status);

        Assert.assertTrue("Was: " + this.gri.getGri(), this.gri.getGri()
                .startsWith("uva.nl-"));
        Assert.assertEquals(IDCClient.STATUS_ACTIVE, status);
        Assert
                .assertTrue("Was: " + vlan,
                        Integer.parseInt(vlan.toString()) > 0);
        Thread.sleep(TestIDCClient.IDC_SIGNAL_BUFFER);
    }

    /**
     * Test method for.
     * 
     * @throws Exception
     * @throws AAAFaultMessage
     */
    @Test
    public final void testCleanUp() throws Exception {
        if (null != this.gri) {
            this.cancelTestReservation();
        }
    }

    /**
     * Test method for {@link net.es.oscars.client.Client#Client()}.
     * 
     * @throws AxisFault
     */
    @Test
    @Before
    public final void testClient() throws AxisFault {
        final String url = Config.getString("translatorIDC.idc.epr");
        final String repo = Config.getString("translatorIDC.idc.repo");
        this.oscarsClient.setUp(true, url, repo);
    }

    /**
     * Default constructor.
     */
    public TestIDCClient() {
        this.oscarsClient = new IDCClient();
    }

    /**
     * Test method for.
     * 
     * @throws Exception
     * @throws AAAFaultMessage
     */
    @Test
    public final void testCreateCancelReservation() throws Exception {
        this.createTestReservation();
        this.cancelTestReservation();
    }

    /**
     * Test method for.
     * 
     * @throws Exception
     * @throws AAAFaultMessage
     */
    @Test
    public final void testInitiateTopologyPull() throws Exception {
        final InitiateTopologyPullContent request =
                new InitiateTopologyPullContent();
        final InitiateTopologyPullResponseContent result =
                this.oscarsClient.initiateTopologyPull(request);
        Assert.assertEquals(TestIDCClient.IDC_MSG_SUCCESS, result
                .getResultMsg());
    }

    /**
     * Test method for.
     * 
     * @throws Exception
     */
    @Test
    public final void testListReservations() throws Exception {
        /* ------------------------------------------------------------------ */
        final ListRequest listReq = new ListRequest();
        final ListRequestSequence_type0 param = new ListRequestSequence_type0();
        param.setStartTime(System.currentTimeMillis() / 1000 - 400);
        param.setEndTime(System.currentTimeMillis() / 1000 + 400);
        listReq.setListRequestSequence_type0(param);
        ListReply list;
        /* ------------------------------------------------------------------ */

        /* ------------------------------------------------------------------ */
        list = this.oscarsClient.listReservations(listReq);
        final int reservationsBegin = list.getTotalResults();
        this.createTestReservation();

        list = this.oscarsClient.listReservations(listReq);
        final int reservationsNow = list.getTotalResults();
        Assert.assertTrue("Should be one reservtion more now",
                reservationsNow == (reservationsBegin + 1));
        /* ------------------------------------------------------------------ */

        this.cancelTestReservation();
    }

    /**
     * Test method for.
     * 
     * @throws Exception
     * @throws AAAFaultMessage
     */
    @Test
    public final void testQueryReservation() throws Exception {
        this.createTestReservation();
        final ResDetails result = this.oscarsClient.queryReservation(this.gri);

        Assert.assertEquals(TestIDCClient.DEFAULT_BANDWIDTH, result
                .getBandwidth());
        this.cancelTestReservation();
    }
}
