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

import junit.framework.Assert;

import org.junit.Test;

import eu.ist_phosphorus.harmony.adapter.dummy.handler.ReservationHandler;
import eu.ist_phosphorus.harmony.translator.idc.implementation.IDCClient;
import eu.ist_phosphorus.harmony.translator.idc.implementation.ReservationIdMapper;

public class TestReservationMapper {
    private final ReservationIdMapper mapper;

    public TestReservationMapper() {
        this.mapper = new ReservationIdMapper();
    }

    /**
     *
     */
    @Test
    public final void testHsi2Idc() {
        final String harmonyId =
                ReservationHandler.getInstance().createReservationId();

        final String idcId = this.mapper.harmonyToIdc(harmonyId);
        final String test = this.mapper.idcToHarmony(idcId);

        Assert.assertEquals(harmonyId, test);
    }

    /**
     *
     */
    @Test
    public final void testIdc2Hsi() {
        final String idcId = IDCClient.createReservationId();

        final String harmonyId = this.mapper.idcToHarmony(idcId);
        final String test = this.mapper.harmonyToIdc(harmonyId);

        Assert.assertEquals(idcId, test);
    }
}
