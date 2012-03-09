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


package eu.ist_phosphorus.harmony.translator.idc.implementation;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.es.oscars.client.Client;
import net.es.oscars.oscars.AAAFaultMessage;
import net.es.oscars.oscars.BSSFaultMessage;
import net.es.oscars.wsdlTypes.CreateReply;
import net.es.oscars.wsdlTypes.GlobalReservationId;
import net.es.oscars.wsdlTypes.InitiateTopologyPullContent;
import net.es.oscars.wsdlTypes.InitiateTopologyPullResponseContent;
import net.es.oscars.wsdlTypes.Layer2Info;
import net.es.oscars.wsdlTypes.ListReply;
import net.es.oscars.wsdlTypes.ListRequest;
import net.es.oscars.wsdlTypes.ListRequestSequence_type0;
import net.es.oscars.wsdlTypes.ListReservationsResponse;
import net.es.oscars.wsdlTypes.PathInfo;
import net.es.oscars.wsdlTypes.ResCreateContent;
import net.es.oscars.wsdlTypes.ResDetails;
import net.es.oscars.wsdlTypes.VlanTag;

import org.apache.axis2.AxisFault;
import org.apache.log4j.Logger;

import eu.ist_phosphorus.harmony.common.utils.Config;
import eu.ist_phosphorus.harmony.common.utils.PhLogger;

/**
 * A class to simulate an IDC.
 * 
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 */
public final class IDCClient extends Client {
    public static final String STATUS_CANCELLED = "CANCELLED";
    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_INSETUP = "INSETUP";
    public static final String STATUS_ACCEPTED = "ACCEPTED";
    public static final String STATUS_TEARDOWN = "INTEARDOWN";
    public static final String STATUS_FAILED = "FAILED";
    public static final String STATUS_FINISHED = "FINISHED";
    public static final String STATUS_INCREATE = "INCREATE";

    /** Reservations */
    private final Map<String, ResDetails> reservations;
    /** The last request */
    private ResCreateContent lastRequest = new ResCreateContent();
    /** The logger */
    private final Logger logger = PhLogger.getLogger();
    /** A counter */
    private static int counter = 0;
    /** Whether to enable the mock mode or not */
    private boolean mockMode = false;
    /** Mock status */
    private String status;

    /**
     * Constructor.
     * 
     * @throws AxisFault
     */
    public IDCClient() {
        super();
        this.reservations = new HashMap<String, ResDetails>();
        this.setMockMode(Config.isTrue("translatorIDC.idc.enableMockMode"));
        this.logger.info("Using dummy IDC calls: " + this.isMockMode());

        try {
            final String url = Config.getString("translatorIDC", "idc.epr");
            final String repo = Config.getString("translatorIDC", "idc.repo");
            this.setUp(true, url, repo);
        } catch (final AxisFault e) {
            this.logger.error("Could not initialize IDC client: "
                    + e.getMessage(), e);
        }
    }

    public boolean isMockMode() {
        return this.mockMode;
    }

    public void setMockMode(final boolean setMockMode) {
        this.mockMode = setMockMode;
    }

    public static String createReservationId() {
        return Config.getString("translatorIDC", "idc.domainID")
                + (++IDCClient.counter);
    }

    @Override
    public CreateReply createReservation(final ResCreateContent resRequest)
            throws Exception {
        if (!this.isMockMode()) {
            return super.createReservation(resRequest);
        }
        this.lastRequest = resRequest;
        final CreateReply reply = new CreateReply();
        this.status = IDCClient.STATUS_ACTIVE;

        reply.setGlobalReservationId(IDCClient.createReservationId());
        reply.setPathInfo(this.lastRequest.getPathInfo());
        reply.setToken("Random Token Value Should Be Set Here");
        reply.setStatus(this.status);

        /* ------------------------------------------------------------------ */
        final ResDetails details = new ResDetails();
        details.setGlobalReservationId(reply.getGlobalReservationId());
        details.setBandwidth(resRequest.getBandwidth());
        details.setDescription(resRequest.getDescription());
        details.setEndTime(resRequest.getEndTime());
        details.setPathInfo(resRequest.getPathInfo());
        details.setStartTime(resRequest.getStartTime());
        details.setStatus(IDCClient.STATUS_ACTIVE);
        this.reservations.put(details.getGlobalReservationId(), details);
        /* ------------------------------------------------------------------ */

        return reply;
    }

    //
    // @Override
    // public GetTopologyResponseContent getNetworkTopology(
    // final GetTopologyContent request) throws Exception {
    // // TODO Auto-generated method stub
    // final GetTopologyResponseContent result =
    // new GetTopologyResponseContent();
    // result.setTopology(new CtrlPlaneTopologyContent());
    // return result;
    // }

    @Override
    public InitiateTopologyPullResponseContent initiateTopologyPull(
            final InitiateTopologyPullContent request) throws Exception {
        if (!this.isMockMode()) {
            return super.initiateTopologyPull(request);
        }
        final InitiateTopologyPullResponseContent result =
                new InitiateTopologyPullResponseContent();
        result.setResultMsg("SUCCESS");
        return result;
    }

    @Override
    public ListReply listReservations(final ListRequest listReq)
            throws BSSFaultMessage, RemoteException, AAAFaultMessage {
        if (!this.isMockMode()) {
            return super.listReservations(listReq);
        }

        final ListReply listReply = new ListReply();
        final ArrayList<ResDetails> results = new ArrayList<ResDetails>();
        final Iterator<String> iterator = this.reservations.keySet().iterator();
        final ListRequestSequence_type0 sequence =
                listReq.getListRequestSequence_type0();
        final long start;
        final long end;

        if (null != sequence) {
            start = sequence.getStartTime();
            end = sequence.getEndTime();
        } else {
            start = Long.MIN_VALUE;
            end = Long.MAX_VALUE;
        }

        while (iterator.hasNext()) {
            final String key = iterator.next();
            final ResDetails details = this.reservations.get(key);

            if ((details.getStartTime() >= start)
                    && (details.getEndTime() <= end)) {
                results.add(details);
            } else {
                this.logger.info("Skipping: "
                        + details.getGlobalReservationId() + " with start: "
                        + details.getStartTime() + " and stop: "
                        + details.getEndTime() + " - query from " + start
                        + " to " + end);
            }
        }

        final int count = results.size();
        final ResDetails[] arrayDetails = new ResDetails[count];

        results.toArray(arrayDetails);

        listReply.setResDetails(arrayDetails);
        listReply.setTotalResults(results.size());

        final ListReservationsResponse response =
                new ListReservationsResponse();
        response.setListReservationsResponse(listReply);

        return listReply;
    }

    @Override
    public ResDetails queryReservation(final GlobalReservationId gri)
            throws Exception {
        this.logger.info("idc.queryReservation: " + gri.getGri());

        if (!this.isMockMode()) {
            return super.queryReservation(gri);
        }
        final ResDetails result = new ResDetails();
        result.setBandwidth(this.lastRequest.getBandwidth());
        result.setDescription(this.lastRequest.getDescription());
        result.setEndTime(this.lastRequest.getEndTime());
        result.setStartTime(this.lastRequest.getStartTime());
        result.setPathInfo(this.lastRequest.getPathInfo());
        result.setStatus(this.status);
        return result;
    }

    @Override
    public String cancelReservation(final GlobalReservationId gri)
            throws Exception {
        this.logger.info("idc.cancelReservation: " + gri.getGri());

        if (!this.isMockMode()) {
            return super.cancelReservation(gri);
        }

        /* ------------------------------------------------------------------ */
        final ResDetails details = this.reservations.get(gri.getGri());
        // details.setStatus(STATUS_CANCELLED);
        // this.reservations.put(details.getGlobalReservationId(), details);
        this.reservations.remove(details.getGlobalReservationId());
        /* ------------------------------------------------------------------ */

        this.status = IDCClient.STATUS_CANCELLED;
        return this.status;
    }

    public CreateReply createReservation(final String src, final String dst,
            final int bandwidth, final int duration) throws Exception {
        final ResCreateContent resRequest =
                IDCClient.getIdcTestReservationReq(src, dst, bandwidth,
                        duration);
        return this.createReservation(resRequest);
    }

    public static ResCreateContent getIdcTestReservationReq(
            final String srcUrn, final String dstUrn, final int bandwidth,
            final int duration) {
        final ResCreateContent createRequest = new ResCreateContent();
        final String desc = Config.getString("test", "test.description");
        final String srcVlanS = Config.getString("test", "test.endpoint0.vlan");
        final String dstVlanS = Config.getString("test", "test.endpoint2.vlan");
        final String pathType = Config.getString("test", "test.pathtype");

        final long startTime = System.currentTimeMillis() / 1000L;
        final long endTime = startTime + duration;
        final Layer2Info layer2info = new Layer2Info();
        final PathInfo pathInfo = new PathInfo();
        final VlanTag srcVlan = new VlanTag();
        final VlanTag dstVlan = new VlanTag();

        srcVlan.setString(srcVlanS);
        srcVlan.setTagged(true);
        dstVlan.setString(dstVlanS);
        dstVlan.setTagged(true);

        layer2info.setSrcEndpoint(srcUrn);
        layer2info.setSrcVtag(srcVlan);
        layer2info.setDestEndpoint(dstUrn);
        layer2info.setDestVtag(dstVlan);

        pathInfo.setLayer2Info(layer2info);
        pathInfo.setPathSetupMode(pathType);

        createRequest.setBandwidth(bandwidth);
        createRequest.setDescription(desc);
        createRequest.setEndTime(endTime);
        createRequest.setStartTime(startTime);
        createRequest.setPathInfo(pathInfo);

        return createRequest;
    }

}
