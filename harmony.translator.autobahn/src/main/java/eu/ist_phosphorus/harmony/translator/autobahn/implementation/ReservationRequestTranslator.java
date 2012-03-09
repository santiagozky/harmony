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

import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import net.es.oscars.wsdlTypes.CreateReply;
import net.es.oscars.wsdlTypes.CreateReservationResponse;
import net.es.oscars.wsdlTypes.Layer2Info;
import net.es.oscars.wsdlTypes.PathInfo;
import net.es.oscars.wsdlTypes.ResCreateContent;
import net.es.oscars.wsdlTypes.VlanTag;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.ConnectionConstraintType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CreateReservationResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CreateReservationType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EndpointType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.FixedReservationConstraintType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.ReservationType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.ServiceConstraintType;
import eu.ist_phosphorus.harmony.translator.autobahn.exceptions.InvalidRequestException;

/**
 * The heart of this module: the Harmony-AutoBAHN-Translator.
 * 
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 */
public final class ReservationRequestTranslator extends HarmonyAutobahnTranslator {

    /**
     * Convert a request.
     * 
     * @param autobahnRequest
     *            The AutoBAHN request.
     * @return The Harmony request.
     * @throws DatatypeConfigurationException
     */
    public CreateReservationType convert(final ResCreateContent autobahnRequest)
            throws DatatypeConfigurationException {
        final CreateReservationType result = new CreateReservationType();

        /* AutoBAHN request information ------------------------------------------ */
        final ResCreateContent autobahnReservation = autobahnRequest;

        final GregorianCalendar autobahnStartTime = new GregorianCalendar();
        autobahnStartTime.setTimeInMillis(autobahnReservation.getStartTime() * 1000);
        final PathInfo autobahnPathInfo = autobahnReservation.getPathInfo();
        final Layer2Info autobahnLayer2Info = autobahnPathInfo.getLayer2Info();
        final String autobahnSource = autobahnLayer2Info.getSrcEndpoint();
        final String autobahnTarget = autobahnLayer2Info.getDestEndpoint();
        final int autobahnBW = autobahnReservation.getBandwidth();
        /* ------------------------------------------------------------------ */

        final ServiceConstraintType serviceConstraintType =
                new ServiceConstraintType();

        serviceConstraintType.setTypeOfReservation(ReservationType.FIXED);

        final FixedReservationConstraintType fixedReservationConstraints =
                new FixedReservationConstraintType();
        fixedReservationConstraints.setDuration((int) ((autobahnReservation
                .getEndTime() - autobahnReservation.getStartTime())));

        fixedReservationConstraints.setStartTime(DatatypeFactory.newInstance()
                .newXMLGregorianCalendar(autobahnStartTime));

        serviceConstraintType
                .setFixedReservationConstraints(fixedReservationConstraints);

        final ConnectionConstraintType connectionContratintType =
                new ConnectionConstraintType();
        final EndpointType target = new EndpointType();
        target.setEndpointId(this.endpointMapper.autobahnToHarmony(autobahnTarget));
        connectionContratintType.getTarget().add(target);
        final EndpointType source = new EndpointType();
        source.setEndpointId(this.endpointMapper.autobahnToHarmony(autobahnSource));
        connectionContratintType.setSource(source);
        connectionContratintType.setMaxBW(autobahnBW);

        serviceConstraintType.getConnections().add(connectionContratintType);

        result.getService().add(serviceConstraintType);

        return result;
    }

    /**
     * Convert a response.
     * 
     * @param autobahnResponse
     *            The AutoBAHN response.
     * @return The Harmony response.
     */
    public CreateReservationResponseType convert(final CreateReply autobahnResponse) {
        /* AutoBAHN values --------------------------------------------------- */
        final String id =
                this.reservationIdMapper.autobahnToHarmony(autobahnResponse
                        .getGlobalReservationId());
        final String autobahnToken = autobahnResponse.getToken();
        /* ------------------------------------------------------------------ */

        final CreateReservationResponseType result =
                new CreateReservationResponseType();
        result.setReservationID(id);
        result.setToken(autobahnToken);

        return result;
    }

    /**
     * Convert a response.
     * 
     * @param harmonyResponse
     *            The Harmony response.
     * @return The AutoBAHN response.
     */
    public CreateReservationResponse convert(
            final CreateReservationResponseType harmonyResponse) {

        /* Harmony values --------------------------------------------------- */
        final String id =
                this.reservationIdMapper.harmonyToAutobahn(harmonyResponse
                        .getReservationID());
        /* ------------------------------------------------------------------ */

        final CreateReply parameter = new CreateReply();
        parameter.setGlobalReservationId(id);

        final CreateReservationResponse result =
                new CreateReservationResponse();
        result.setCreateReservationResponse(parameter);

        return result;
    }

    /**
     * Converts a Harmony CreateReservationRequest to an AutoBAHN
     * CreateReservationRequest. At the moment only fixed reservations with one
     * service and one connection and one target is supported!
     * 
     * @param harmonyRequest
     *            The Harmony request.
     * @return The AutoBAHN request.
     * @throws InvalidRequestException
     */
    public ResCreateContent convert(final CreateReservationType harmonyRequest)
            throws InvalidRequestException {
        /* Harmony request -------------------------------------------------- */
        String harmonySource = null;
        String harmonyTarget = null;
        XMLGregorianCalendar harmonyStartTime = null;
        int harmonyDuration = 0;
        int harmonyBandwidth = 0;
        boolean automaticActivation = false;

        // Only support one service
        if (harmonyRequest.getService().size() != 1) {
            throw new InvalidRequestException(
                    "Request must contain exactly one ServiceConstraintType");
        }

        final ServiceConstraintType service =
                harmonyRequest.getService().get(0);

        // Check ServiceID to be sure it's the same in GetStatus
        if (service.getServiceID() != 1) {
            throw new InvalidRequestException("ServiceID must be 1");
        }

        harmonyStartTime =
                service.getFixedReservationConstraints().getStartTime();
        harmonyDuration =
                service.getFixedReservationConstraints().getDuration();

        automaticActivation = service.isAutomaticActivation();

        final ConnectionConstraintType connection =
                service.getConnections().get(0);

        harmonySource = connection.getSource().getEndpointId();
        harmonyTarget =
                connection.getTarget().iterator().next().getEndpointId();

        harmonyBandwidth = connection.getMinBW();
        /* ------------------------------------------------------------------ */

        /* Check ------------------------------------------------------------ */
        this.checkReservationParameter(harmonySource, harmonyTarget,
                harmonyStartTime, harmonyDuration, harmonyBandwidth, service);

        /* AutoBAHN request ------------------------------------------------------ */
        final ResCreateContent autobahnRequest =
                this.createAutobahnReservationRequest(harmonySource, harmonyTarget,
                        harmonyStartTime, harmonyDuration, harmonyBandwidth,
                        automaticActivation);
        /* ------------------------------------------------------------------ */

        return autobahnRequest;
    }

    /**
     * @param harmonySource
     * @param harmonyTarget
     * @param harmonyStartTime
     * @param harmonyDuration
     * @param harmonyBandwidth
     * @param automaticActivation
     * @return
     */
    private ResCreateContent createAutobahnReservationRequest(
            final String harmonySource, final String harmonyTarget,
            final XMLGregorianCalendar harmonyStartTime,
            final int harmonyDuration, final int harmonyBandwidth,
            final boolean automaticActivation) {

        final ResCreateContent autobahnReservationContent = new ResCreateContent();
        final Layer2Info autobahnLayer2Info = new Layer2Info();
        final PathInfo autobahnPathInfo = new PathInfo();

        autobahnReservationContent.setBandwidth(harmonyBandwidth);
        autobahnReservationContent.setDescription("Reservation created from HSI: "
                + harmonySource + " - " + harmonyTarget);
        autobahnReservationContent.setStartTime(harmonyStartTime
                .toGregorianCalendar().getTimeInMillis() / 1000L);
        autobahnReservationContent.setEndTime(harmonyStartTime.toGregorianCalendar()
                .getTimeInMillis()
                / 1000L + harmonyDuration);

        final String srcUrn = this.endpointMapper.harmonyToAutobahn(harmonySource);
        final String srcVlan = this.endpointMapper.getVlan(harmonySource);
        final String dstUrn = this.endpointMapper.harmonyToAutobahn(harmonyTarget);
        final String dstVlan = this.endpointMapper.getVlan(harmonyTarget);

        if (null != srcVlan) {
            final VlanTag vlanTag = new VlanTag();
            vlanTag.setString(srcVlan);
            vlanTag.setTagged(true);
            autobahnLayer2Info.setSrcVtag(vlanTag);
        }

        if (null != dstVlan) {
            final VlanTag vlanTag = new VlanTag();
            vlanTag.setString(dstVlan);
            vlanTag.setTagged(true);
            autobahnLayer2Info.setDestVtag(vlanTag);
        }

        autobahnLayer2Info.setDestEndpoint(dstUrn);
        autobahnLayer2Info.setSrcEndpoint(srcUrn);

        autobahnPathInfo.setLayer2Info(autobahnLayer2Info);

        if (automaticActivation) {
            autobahnPathInfo.setPathSetupMode("timer-automatic");
        } else {
            autobahnPathInfo.setPathSetupMode("signal-xml");
        }

        autobahnReservationContent.setPathInfo(autobahnPathInfo);

        return autobahnReservationContent;
    }

    /**
     * @param harmonySource
     * @param harmonyTarget
     * @param harmonyStartTime
     * @param harmonyDuration
     * @param harmonyBandwidth
     * @param service
     * @throws InvalidRequestException
     */
    private void checkReservationParameter(final String harmonySource,
            final String harmonyTarget,
            final XMLGregorianCalendar harmonyStartTime,
            final int harmonyDuration, final int harmonyBandwidth,
            final ServiceConstraintType service) throws InvalidRequestException {
        if (null == harmonySource) {
            throw new InvalidRequestException("empty source");
        }
        if (null == harmonyTarget) {
            throw new InvalidRequestException("empty target");
        }
        if (null == harmonyStartTime) {
            throw new InvalidRequestException("empty starttime");
        }
        if (0 == harmonyDuration) {
            throw new InvalidRequestException("empty duration");
        }
        if (0 == harmonyBandwidth) {
            throw new InvalidRequestException("empty bandwidth");
        }
        // Check ConnectionID to be sure it's the same in GetStatus
        if (service.getServiceID() != 1) {
            throw new InvalidRequestException("ConnectionID must be 1");
        }

        // Only support one Connection
        if (service.getConnections().size() != 1) {
            throw new InvalidRequestException(
                    "Request must contain exactly one ConnectionConstraintType");
        }
    }

}
