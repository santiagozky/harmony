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


package org.opennaas.core.security;

import java.io.File;
import java.util.Date;
import java.util.HashMap;

import org.aaaarch.config.ConfigDomainsPhosphorus;
import org.aaaarch.gaaapi.tvs.TVS;
import org.aaaarch.gaaapi.tvs.TVSTable;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import org.opennaas.core.security.authz.HarmonyPEP;
import org.opennaas.core.security.authz.HarmonyTVS;
import org.opennaas.core.security.utils.helper.TokenHelper;
import org.opennaas.core.utils.FileHelper;

public class TestHarmonyTVS {

    private static String createReservationXML = "";
    private static String cancelReservationXML = "";
    private static long gri = 0;
    private static String userCredentials;

    @BeforeClass
    public static void setup() throws Exception {
        TestHarmonyTVS.createReservationXML =
                FileHelper.readFile("resources/data/CreateReservation.xml");
        TestHarmonyTVS.cancelReservationXML =
                FileHelper.readFile("resources/data/CancelReservation.xml");
        TestHarmonyTVS.userCredentials =
                "junit@viola.testbed.ist-phosphorus.eu";
        System.out.println("TVS: "+TVS.getTVSTableFile());
        final File tvsTable = new File(TVS.getTVSTableFile());
        if (!tvsTable.exists()) {

            final File dir = new File(tvsTable.getParent());
            dir.mkdirs();
            tvsTable.createNewFile();
            TVSTable.buildTVSTable(new HashMap());
        }
    }

    @Test
    public void test() throws Exception {

        final HarmonyPEP pep = new HarmonyPEP();

        final HarmonyTVS tvs = new HarmonyTVS();

        boolean createValid = false;
        Assert.assertTrue("The request is not valid", createValid =
                pep.pre(TestHarmonyTVS.createReservationXML,
                        TestHarmonyTVS.userCredentials));
        if (createValid) {
            // generate GRI:
            TestHarmonyTVS.gri = 83;
        }

        final int validTime = 1440; // 24 hours
        final long notBefore = System.currentTimeMillis();
        final long notAfter = notBefore + validTime * 60 * 1000;
        // save request credentials
        // tvs.addEntryTVSTable(String.valueOf(gri),
        // TokenKey.generateTokenKey(String.valueOf(gri)), validTime,
        // "10.7.12.2", "10.3.17.3");
        TVSTable.deleteEntryTVSTable(
                ConfigDomainsPhosphorus.DOMAIN_PHOSPHORUS_VIOLA, String
                        .valueOf(TestHarmonyTVS.gri));
        tvs.addEntryTVSTable(String.valueOf(TestHarmonyTVS.gri), notBefore,
                notAfter, "10.7.12.2", "10.3.17.4");

        final String token =
            HarmonyTVS.getXMLToken(String.valueOf(TestHarmonyTVS.gri),
                        validTime - 1, false, new Date(notBefore), new Date(
                                notAfter - 1));
        
        String tokenVal = TokenHelper.getTokenValue(token);

        Assert.assertTrue(tvs.validate(tokenVal,
                ConfigDomainsPhosphorus.DOMAIN_PHOSPHORUS_VIOLA, String
                        .valueOf(TestHarmonyTVS.gri)));
    }

}
