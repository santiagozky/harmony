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


package eu.ist_phosphorus.harmony.test.translator.idc;

import net.es.oscars.wsdlTypes.CreateReply;
import net.es.oscars.wsdlTypes.GlobalReservationId;
import net.es.oscars.wsdlTypes.ResDetails;

import org.apache.axis2.AxisFault;
import org.junit.Assert;
import org.junit.Test;

import eu.ist_phosphorus.harmony.common.utils.Config;

/**
 * @author willner
 */
public class TestIDCClient extends AbstractReservationTest {

    public TestIDCClient() throws AxisFault {
        super();
    }

    @Test
    public void testCreateCancelReservation() throws Exception {
        /* ------------------------------------------------------------------ */
        final String src = Config.getString("test.test.endpoint0.urn");
        final String dst = Config.getString("test.test.endpoint1.urn");
        final int bandwidth = Config.getInt("test", "test.bandwidth");
        final int duration = Config.getInt("test", "test.duration");
        /* ------------------------------------------------------------------ */

        /* ------------------------------------------------------------------ */
        final CreateReply createReply =
                this.idcRsvClient.createReservation(src, dst, bandwidth,
                        duration);
        this.idcResId = createReply.getGlobalReservationId();
        this.logger.info("Got IDC ID: " + this.idcResId);
        Assert.assertTrue("Should return a valid ID", null != this.idcResId);
        /* ------------------------------------------------------------------ */

        /* ------------------------------------------------------------------ */
        final GlobalReservationId gri = new GlobalReservationId();
        gri.setGri(this.idcResId);
        final ResDetails resDetails = this.idcRsvClient.queryReservation(gri);
        this.logger.info("Got status: " + resDetails.getStatus());
        /* ------------------------------------------------------------------ */
    }

}
