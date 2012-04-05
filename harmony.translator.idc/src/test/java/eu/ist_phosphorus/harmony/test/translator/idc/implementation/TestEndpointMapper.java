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

import eu.ist_phosphorus.harmony.translator.idc.implementation.EndpointMapper;

public class TestEndpointMapper {
    private final EndpointMapper mapper;

    /**
     *
     */
    public TestEndpointMapper() {
        super();

        this.mapper = new EndpointMapper();
    }

    /**
     *
     */
    @Test
    public final void testHsi2Idc() {
        final String start = "10.9.2.1";

        final String converted = this.mapper.harmonyToIdc(start);
        final String test = this.mapper.idcToHarmony(converted);

        Assert.assertEquals(start, test);
    }

    /**
     *
     */
    @Test
    public final void testIdc2Hsi() {
        final String start = "sdfs@internet2.edu";

        final String converted = this.mapper.idcToHarmony(start);
        final String test = this.mapper.harmonyToIdc(converted);

        Assert.assertEquals(start, test);
    }
}
