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

package eu.ist_phosphorus.harmony.test.common.serviceinterface;

import java.net.URISyntaxException;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.muse.ws.addressing.soap.SoapFault;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.exceptions.InvalidRequestFaultException;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.exceptions.OperationNotSupportedFaultException;
import eu.ist_phosphorus.harmony.common.serviceinterface.reservation.CommonReservationWS;
import eu.ist_phosphorus.harmony.common.serviceinterface.reservation.SimpleReservationClient;

/**
 * 
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 * @version $Id: TestSimpleReservationClient.java 2692 2008-03-31 19:54:59Z
 *          willner@cs.uni-bonn.de $
 */
public class TestSimpleReservationClient {

    private SimpleReservationClient client;

    /**
     * Test method for
     * {@link eu.ist_phosphorus.nrps.common.reservation.SimpleReservationClient#SimpleReservationClient(java.lang.String)}
     * .
     * 
     * @throws URISyntaxException
     */
    @Test
    @Before
    public final void testConstructor() {
        this.client = new SimpleReservationClient(new CommonReservationWS());
        Assert.assertNotNull("Client should be created.", this.client);
    }

    @Test(expected = RuntimeException.class)
    public final void testConstructorWrong() {
        this.client = new SimpleReservationClient("sadf��, fweppp");
        Assert.fail("Client should not be created.");
    }

    /**
     * Test method for
     * {@link eu.ist_phosphorus.nrps.common.reservation.SimpleReservationClient#createReservation(eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CreateReservationType)}
     * .
     * 
     * @throws SoapFault
     * @throws URISyntaxException
     */
    @Test(expected = InvalidRequestFaultException.class)
    public final void testCreateReservationWrong() throws SoapFault {
        this.client.createReservation("wrongTNAhere", "anotherWrongTNA", 800,
                800, 444);
    }

    /**
     * Test the new SAML assertions parameter. It should throw an
     * OperationNotSupportedFaultException in this test environment.
     * 
     * @throws SoapFault
     *             If any problem occurs while sending via SOAP.
     */
    @Test(expected = OperationNotSupportedFaultException.class)
    public final void testCreateSecureReservation() throws SoapFault {
        final String source = "1.2.3.4";
        final String target = "1.2.3.5";
        final int bandwidth = 1000;
        final int delay = 100;
        final int duration = 9999;
        final String samlAssertion = "";
        this.client.createReservation(source, target, bandwidth, delay,
                duration, samlAssertion);
    }

    /**
     * 
     * @throws SoapFault
     * @throws DatatypeConfigurationException
     * @throws URISyntaxException
     */
    @Test(expected = OperationNotSupportedFaultException.class)
    public final void testUnsupportedMethod() throws SoapFault {
        this.client.activate("1@hsiTemplateDomain", 1);
    }
}
