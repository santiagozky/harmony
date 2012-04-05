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


package eu.ist_phosphorus.harmony.test.translator.idc.implementation;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;

import junit.framework.Assert;
import net.es.oscars.wsdlTypes.Layer2Info;
import net.es.oscars.wsdlTypes.PathInfo;
import net.es.oscars.wsdlTypes.ResCreateContent;
import net.es.oscars.wsdlTypes.VlanTag;

import org.junit.Test;

import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.ConnectionConstraintType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CreateReservationType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EndpointType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.FixedReservationConstraintType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.ReservationType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.ServiceConstraintType;
import eu.ist_phosphorus.harmony.common.utils.Helpers;
import eu.ist_phosphorus.harmony.translator.idc.exceptions.InvalidRequestException;
import eu.ist_phosphorus.harmony.translator.idc.implementation.EndpointMapper;
import eu.ist_phosphorus.harmony.translator.idc.implementation.ReservationRequestTranslator;

/**
 * Test the heart of this module: the translator.
 * 
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 */
public class TestHarmonyIdcTranslator {

    /**
     * Create a Harmony test reservation.
     * 
     * @return The Harmony test reservation.
     * @throws DatatypeConfigurationException
     *             Unkown.
     */
    public static CreateReservationType createHarmonyTestReservation()
            throws DatatypeConfigurationException {
        final CreateReservationType result = new CreateReservationType();

        final EndpointType harmonySource = new EndpointType();
        harmonySource.setEndpointId("10.9.2.1");
        final EndpointType harmonyTarget = new EndpointType();
        harmonyTarget.setEndpointId("10.9.2.3");
        final XMLGregorianCalendar harmonyStartTime =
                Helpers.generateXMLCalendar();
        final int harmonyDuration = 3600;
        final int harmonyBandwidth = 22;
        final boolean automaticActivation = true;

        final ConnectionConstraintType harmonyConnection =
                new ConnectionConstraintType();
        harmonyConnection.setConnectionID(1);
        harmonyConnection.setMinBW(harmonyBandwidth);
        harmonyConnection.setMaxBW(harmonyBandwidth);
        harmonyConnection.setSource(harmonySource);
        harmonyConnection.getTarget().add(harmonyTarget);

        final FixedReservationConstraintType harmonyFixedConstraints =
                new FixedReservationConstraintType();
        harmonyFixedConstraints.setDuration(harmonyDuration);
        harmonyFixedConstraints.setStartTime(harmonyStartTime);

        final ServiceConstraintType harmonyServiceContraint =
                new ServiceConstraintType();
        harmonyServiceContraint.setAutomaticActivation(automaticActivation);
        harmonyServiceContraint.setServiceID(1);
        harmonyServiceContraint.setTypeOfReservation(ReservationType.FIXED);
        harmonyServiceContraint
                .setFixedReservationConstraints(harmonyFixedConstraints);
        harmonyServiceContraint.getConnections().add(harmonyConnection);

        result.getService().add(harmonyServiceContraint);

        return result;
    }

    /**
     * Create a valid IDC createReservation request.
     * 
     * @return The create reservation request.
     */
    public static ResCreateContent createIdcTestReservation() {
        final Layer2Info layer2param = new Layer2Info();
        layer2param.setSrcEndpoint("urn:ogf:network:domain=uva.nl:"
                + "node=uva-node2:port=nortel:link=nortel");
        layer2param.setDestEndpoint("urn:ogf:network:domain=dcn.internet2.edu:"
                + "node=HOUS:port=S26623:link=10.100.100.5");

        final VlanTag tag = new VlanTag();
        tag.setString("3070");
        tag.setTagged(true);
        layer2param.setDestVtag(tag);

        final PathInfo pathInfo = new PathInfo();
        pathInfo.setPathSetupMode("timer-automatic");
        pathInfo.setLayer2Info(layer2param);

        final ResCreateContent parameter = new ResCreateContent();
        parameter.setBandwidth(10);
        parameter.setDescription("Test description");
        parameter.setEndTime(System.currentTimeMillis() / 1000 + 60);
        parameter.setGlobalReservationId("123@test");
        parameter.setStartTime(System.currentTimeMillis() / 1000);
        parameter.setPathInfo(pathInfo);

        return parameter;
    }

    private final EndpointMapper endpointMapper = new EndpointMapper();
    private final ReservationRequestTranslator translator =
            new ReservationRequestTranslator();

    /**
     * Compare two requests.
     * 
     * @param idcRequest
     *            The IDC request.
     * @param harmonyRequest
     *            The Harmony request.
     */
    private void compare(final ResCreateContent idcRequest,
            final CreateReservationType harmonyRequest) {
        /* ------------------------------------------------------------------ */
        final ServiceConstraintType harmonyService =
                harmonyRequest.getService().iterator().next();
        final ConnectionConstraintType harmonyConnection =
                harmonyService.getConnections().iterator().next();
        /* ------------------------------------------------------------------ */

        /* ------------------------------------------------------------------ */
        final long actualStartTime =
                harmonyService.getFixedReservationConstraints().getStartTime()
                        .toGregorianCalendar().getTimeInMillis() / 1000;
        final long expectedStartTime = idcRequest.getStartTime();
        Assert.assertEquals("Starttime should equal", expectedStartTime,
                actualStartTime);

        final int actualBandwidth = harmonyConnection.getMaxBW();
        final int expectedBandwidth = idcRequest.getBandwidth();
        Assert.assertEquals("Bandwidth should equal", expectedBandwidth,
                actualBandwidth);

        final String expectedSource =
                harmonyConnection.getSource().getEndpointId();
        final String actualSource =
                idcRequest.getPathInfo().getLayer2Info().getSrcEndpoint();

        Assert.assertEquals("Source endpoints should equal (by now)",
                expectedSource, this.endpointMapper.idcToHarmony(actualSource));

        final String expectedTarget =
                harmonyConnection.getTarget().iterator().next().getEndpointId();
        final String actualTarget =
                idcRequest.getPathInfo().getLayer2Info().getDestEndpoint();

        Assert.assertEquals("Target endpoints should equal (by now)",
                expectedTarget, this.endpointMapper.idcToHarmony(actualTarget));
        // TODO More assertions
        /* ------------------------------------------------------------------ */
    }

    /**
     * Test method for.
     * 
     * @throws DatatypeConfigurationException
     * @throws InvalidRequestException
     */
    @Test
    public final void testConvertCreateReservationToHarmony()
            throws DatatypeConfigurationException, InvalidRequestException {
        /* Create the IDC request ------------------------------------------- */
        final ResCreateContent idcRequest =
                TestHarmonyIdcTranslator.createIdcTestReservation();
        /* ------------------------------------------------------------------ */

        /* Get Harmony request ---------------------------------------------- */
        final CreateReservationType harmonyRequest =
                this.translator.convert(idcRequest);
        /* ------------------------------------------------------------------ */

        /* Test the result -------------------------------------------------- */
        this.compare(idcRequest, harmonyRequest);
        /* ------------------------------------------------------------------ */
    }

    /**
     * Test method for .
     * 
     * @throws DatatypeConfigurationException
     * @throws InvalidRequestException
     */
    @Test
    public final void testConvertCreateReservationToIdc()
            throws DatatypeConfigurationException, InvalidRequestException {
        /* Get Harmony request ---------------------------------------------- */
        final CreateReservationType harmonyRequest =
                TestHarmonyIdcTranslator.createHarmonyTestReservation();
        /* ------------------------------------------------------------------ */

        /* Create the IDC request ------------------------------------------- */
        final ResCreateContent idcRequest =
                this.translator.convert(harmonyRequest);
        /* ------------------------------------------------------------------ */

        /* Test the result -------------------------------------------------- */
        this.compare(idcRequest, harmonyRequest);
        /* ------------------------------------------------------------------ */
    }

    /**
     * Test method for .
     * 
     * @throws DatatypeConfigurationException
     * @throws InvalidRequestException
     */
    @Test(expected = InvalidRequestException.class)
    public final void testConvertCreateReservationToIdcWithFailure()
            throws DatatypeConfigurationException, InvalidRequestException {
        /* Get Harmony request ---------------------------------------------- */
        final CreateReservationType harmonyRequest =
                TestHarmonyIdcTranslator.createHarmonyTestReservation();
        harmonyRequest.getService().iterator().next()
                .getFixedReservationConstraints().setDuration(0);
        /* ------------------------------------------------------------------ */

        /* Create the IDC request ------------------------------------------- */
        this.translator.convert(harmonyRequest);
        Assert.fail("Should throow an exception.");
        /* ------------------------------------------------------------------ */
    }

}
