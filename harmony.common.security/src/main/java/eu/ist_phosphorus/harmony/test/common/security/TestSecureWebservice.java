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


package eu.ist_phosphorus.harmony.test.common.security;

import junit.framework.Assert;

import org.apache.muse.ws.addressing.soap.SoapFault;
import org.junit.Before;
import org.junit.Test;

import eu.ist_phosphorus.harmony.adapter.dummy.webservice.ReservationWS;
import eu.ist_phosphorus.harmony.common.security.utils.helper.ConfigHelper;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CreateReservationResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.exceptions.OperationNotAllowedFaultException;
import eu.ist_phosphorus.harmony.common.serviceinterface.reservation.SimpleReservationClient;
import eu.ist_phosphorus.harmony.common.utils.Config;

/**
 * 
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 * 
 */
public class TestSecureWebservice {

    private SimpleReservationClient client;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    	if (Config.isTrue("test", "test.callWebservice")) {
            this.client =
                new SimpleReservationClient(Config.getString("test",
                "test.reservationEPR")); 
        } else {
            this.client = new SimpleReservationClient(new ReservationWS());
        }

    	
    }

    @Test
    public void testSignedEncryptedReservation() throws SoapFault {
        ConfigHelper.setSigning(true);
        ConfigHelper.setEncryption(true);

        try {
            final CreateReservationResponseType rsv =
                    this.client.createReservation("130.0.0.1", "130.0.0.2", 10,
                            1000, 678);

            Assert.assertTrue("No GRI received!", rsv.getReservationID() != "");
        } catch (OperationNotAllowedFaultException e) {
            System.out.println("\n\nUnable to complete request: "
                    + e.getMessage());

            Assert.assertTrue(e.getMessage(), false);
        }

    }

    @Test
    public void testUnsignedUnencryptedReservation() throws SoapFault {
        ConfigHelper.setSigning(false);
        ConfigHelper.setEncryption(false);

        try {
            final CreateReservationResponseType rsv =
                    this.client.createReservation("130.0.0.1", "130.0.0.2", 10,
                            1000, 678);

            Assert.assertTrue("No GRI received!", rsv.getReservationID() != "");
        } catch (OperationNotAllowedFaultException e) {
            System.out.println("\n\nUnable to complete request: "
                    + e.getMessage());

            Assert.assertTrue(e.getMessage(), false);
        }

    }

}
