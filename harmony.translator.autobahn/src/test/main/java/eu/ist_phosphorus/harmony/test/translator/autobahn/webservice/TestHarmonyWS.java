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
 * Project: IST Phosphorus Harmony System. Module: Description:
 * 
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 * @version $Id: TestHarmonyWS.java 4747 2009-03-04 18:25:39Z
 *          willner@cs.uni-bonn.de $
 */
package eu.ist_phosphorus.harmony.test.translator.autobahn.webservice;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.axis2.AxisFault;
import org.apache.muse.ws.addressing.soap.SoapFault;
import org.junit.Assert;
import org.junit.Test;

import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.AddTopicType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CreateReservationResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CreateReservationType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetDomainsResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.exceptions.OperationNotSupportedFaultException;
import eu.ist_phosphorus.harmony.common.serviceinterface.notification.SimpleNotificationClient;
import eu.ist_phosphorus.harmony.common.serviceinterface.reservation.SimpleReservationClient;
import eu.ist_phosphorus.harmony.common.serviceinterface.topology.SimpleTopologyClient;
import eu.ist_phosphorus.harmony.common.utils.Config;
import eu.ist_phosphorus.harmony.test.translator.autobahn.implementation.TestHarmonyAutobahnTranslator;
import eu.ist_phosphorus.harmony.translator.autobahn.webservice.NotificationWS;
import eu.ist_phosphorus.harmony.translator.autobahn.webservice.ReservationWS;
import eu.ist_phosphorus.harmony.translator.autobahn.webservice.TopologyWS;

/**
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 * @version $Id: TestHarmonyWS.java 3996 2008-08-07 09:09:20Z
 *          willner@cs.uni-bonn.de $
 */
public class TestHarmonyWS {

    /** A simple Harmony reservationClient. */
    private final SimpleReservationClient reservationClient;
    /** A simple Harmony topologyClient. */
    private final SimpleTopologyClient topologyClient;
    /** A simple Harmony notificationClient. */
    private final SimpleNotificationClient notificationClient;

    /**
     * The public constructor.
     * 
     * @throws AxisFault
     */
    public TestHarmonyWS() throws AxisFault {
        if (Config.isTrue("test", "test.callWebservice")) {
            String epr;
            epr = Config.getString("test", "test.reservationEPR");
            this.reservationClient = new SimpleReservationClient(epr);
            epr = Config.getString("test", "test.topologyEPR");
            this.topologyClient = new SimpleTopologyClient(epr);
            epr = Config.getString("test", "test.notificationEPR");
            this.notificationClient = new SimpleNotificationClient(epr);
        } else {
            this.reservationClient =
                    new SimpleReservationClient(new ReservationWS());
            this.topologyClient = new SimpleTopologyClient(new TopologyWS());
            this.notificationClient =
                    new SimpleNotificationClient(new NotificationWS());
        }
    }

    /**
     * Test the Harmony getDomains request.
     * 
     * @throws DatatypeConfigurationException
     */
    @Test(expected = OperationNotSupportedFaultException.class)
    public final void testAddTopic() throws SoapFault {
        final AddTopicType request = new AddTopicType();
        request.setTopic("Test Topic");
        this.notificationClient.addTopic(request);
        Assert.fail("Shoudl throw an exception.");
    }

    /**
     * Test the Harmony createReservaton request.
     * 
     * @throws DatatypeConfigurationException
     */
    @Test
    public final void testCreateReservation() throws SoapFault,
            DatatypeConfigurationException {
        final CreateReservationType reservationParameter =
                TestHarmonyAutobahnTranslator.createHarmonyTestReservation();
        final CreateReservationResponseType result =
                this.reservationClient.createReservation(reservationParameter);
        System.out.println(result.getReservationID());
        Assert.assertTrue("Should contain a GRI.", result.getReservationID()
                .contains("@"));
        this.reservationClient.cancelReservation(result.getReservationID());
    }

    /**
     * Test the Harmony getDomains request.
     * 
     * @throws DatatypeConfigurationException
     */
    @Test
    public final void testGetDomains() throws SoapFault {
        final GetDomainsResponseType result = this.topologyClient.getDomains();
        Assert.assertEquals("Should not return a domain.", 0, result
                .getDomains().size());
    }

    /**
     * Test the Harmony createReservaton request.
     * 
     * @throws DatatypeConfigurationException
     */
    @Test
    public final void testGetReservations() throws SoapFault,
            DatatypeConfigurationException {

        this.reservationClient.getReservations(10000);
    }

}
