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


package eu.ist_phosphorus.harmony.test.adapter.dummy.webservice;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.log4j.Logger;
import org.apache.muse.ws.addressing.soap.SoapFault;
import org.junit.Assert;
import org.junit.Test;

import eu.ist_phosphorus.harmony.adapter.dummy.webservice.ReservationWS;
import eu.ist_phosphorus.harmony.common.serviceinterface.reservation.
    SimpleReservationClient;
import eu.ist_phosphorus.harmony.common.utils.Config;
import eu.ist_phosphorus.harmony.common.utils.PhLogger;

/**
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 */
public final class TestWorkflow {

    /** The reservation client. */
    private SimpleReservationClient client;

    /** The logger. */
    private final Logger logger;

    /**
     * Default constructor.
     */
    public TestWorkflow() {
        if (Config.isTrue("test", "test.callWebservice")) {
            final String epr = Config.getString("test", "test.reservationEPR");
            this.client = new SimpleReservationClient(epr);
        } else {
            this.client = new SimpleReservationClient(new ReservationWS());
        }
        this.logger = PhLogger.getLogger();
    }

    /**
     * This workflow shows how the adapter is used by the IDB.
     * 
     * @throws SoapFault
     */
    @Test
    public void testSimpleWorkflow() throws SoapFault,
            DatatypeConfigurationException {
        eu.ist_phosphorus.harmony.test.common.serviceinterface.TestWorkflow
                .testSimpleWorkflow(this.client, this.logger);
        Assert.assertTrue(true);
    }
}
