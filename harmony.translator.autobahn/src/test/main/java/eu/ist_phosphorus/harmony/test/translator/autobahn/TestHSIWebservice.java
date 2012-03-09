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


package eu.ist_phosphorus.harmony.test.translator.autobahn;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.axis2.AxisFault;
import org.apache.muse.ws.addressing.soap.SoapFault;
import org.junit.Assert;
import org.junit.Test;

import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CreateReservationResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetStatusResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.StatusType;
import eu.ist_phosphorus.harmony.common.utils.Config;
import eu.ist_phosphorus.harmony.test.common.serviceinterface.TestWorkflow;

/**
 * @author willner
 */
public class TestHSIWebservice extends AbstractReservationTest {

    public TestHSIWebservice() throws AxisFault {
        super();
    }

    @Test
    public final void testCreateCancelReservation() throws Exception {
        /* ------------------------------------------------------------------ */
        final String src = Config.getString("test.test.endpoint0.tna");
        final String dst = Config.getString("test.test.endpoint1.tna");
        final int bandwidth = Config.getInt("test", "test.bandwidth");
        final int duration = Config.getInt("test", "test.duration");
        final int delay = Config.getInt("test", "test.delay");
        /* ------------------------------------------------------------------ */

        /* ------------------------------------------------------------------ */
        final CreateReservationResponseType createReply =
                this.hsiRsvClient.createReservation(src, dst, bandwidth, delay,
                        duration);
        this.hsiResId = createReply.getReservationID();
        this.logger.info("Got HSI ID: " + this.hsiResId);
        Assert.assertTrue("Should return a valid ID", null != this.hsiResId);
        /* ------------------------------------------------------------------ */

        /* ------------------------------------------------------------------ */
        final GetStatusResponseType resDetails =
                this.hsiRsvClient.getStatus(this.hsiResId);
        final StatusType status =
                resDetails.getServiceStatus().iterator().next().getStatus();
        this.logger.info("Got status: " + status);
        /* ------------------------------------------------------------------ */

        /* ------------------------------------------------------------------ */
        /* ------------------------------------------------------------------ */
    }

    @Test
    public final void testWorkflow() throws SoapFault,
            DatatypeConfigurationException {
        final String src = Config.getString("test.test.endpoint0.tna");
        final String dst = Config.getString("test.test.endpoint1.tna");
        final int bandwidth = Config.getInt("test", "test.bandwidth");
        final int delay = Config.getInt("test", "test.delay");
        final int duration = Config.getInt("test", "test.duration");

        TestWorkflow.testSimpleWorkflow(this.hsiRsvClient, this.logger, src,
                dst, delay, bandwidth, duration);
    }
}
