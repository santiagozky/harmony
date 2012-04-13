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

import java.util.HashMap;

import org.aaaarch.gaaapi.ActionSet;
import org.aaaarch.gaaapi.PEP;
import org.aaaarch.gaaapi.SubjectSet;
import org.aaaarch.gaaapi.tvs.GRIgenerator;
import org.junit.Assert;
import org.junit.Test;

import eu.ist_phosphorus.harmony.common.security.authn.HarmonyWSSecurity;
import eu.ist_phosphorus.harmony.common.security.authz.HarmonyPEP;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.exceptions.OperationNotAllowedFaultException;
import eu.ist_phosphorus.harmony.common.utils.FileHelper;

/**
 * Test the Harmony Policy Enforcement Point.
 * 
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 * @version $Id$
 */
public class TestHarmonyPEP {
    /**
     * System Under Test: HarmonyPEP
     */
    private final HarmonyPEP pep;

    /**
     * Initialization of the PEP.
     */
    public TestHarmonyPEP() {
        this.pep = new HarmonyPEP();
    }

    /**
     * 
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Test
    public void oldTestPEPexample() throws Exception {
        // String subjectId = "WHO740@users.testbed.ist-phosphorus.eu";
        // String subjconfdata = "2SeDFGVHYTY83ZXxEdsweOP8Iok";
        // String roles = "researcher, admin";
        // String subjctx = "demo001";

        final HashMap<String, String> subjmap = SubjectSet.getSubjSetTest();
        final String griprefix = "nsp-domain.uob";
        final String sessionId = GRIgenerator.generateGRI(32, griprefix);
        final String resourceId =
                "http://testbed.ist-phosphorus.eu/resource-type/nsp";
        final String actions = ActionSet.NSP_CREATE_PATH;

        final boolean decision =
                PEP.authorizeAction(resourceId, actions, subjmap);
        Assert.assertTrue("Test action should be permitted", decision);

        final String azticket =
                PEP.authorizeAction(null, sessionId, resourceId, actions,
                        subjmap);
        Assert.assertTrue("Test action should get a ticket",
                azticket.length() > 0);

        // TODO: validate ticket
        // String result = this.pep.authorizeAction(azticket, sessionId,
        // resourceId, actions, subjmap);
        // System.out.println(result);
    }

    @Test
    // TODO: "PDP response: NotApplicable: Request and Policy semantics or
    // values don't match"
    public void testAuthZInValidCreateReservation() throws Exception {
        /* load an example Harmony reservation request ---------------------- */
        final String rawRequest =
                FileHelper
                        .readFile("resources/data/RawReservationRequestWrongEndpoints.xml");
        /* ------------------------------------------------------------------ */

        /* get user credentials --------------------------------------------- */
        // TODO: get user credentials here
        final String userCredentials = "junit@viola.testbed.ist-phosphorus.eu";
        /* ------------------------------------------------------------------ */

        /* get decision from GAAA-tk ---------------------------------------- */
        final boolean decision = this.pep.pre(rawRequest, userCredentials);
        Assert.assertFalse("Test action should be permitted", decision);
        /* ------------------------------------------------------------------ */
    }

    @Test
    public void testAuthZInvalidIsAvailable() throws Exception {
        final String rawRequest =
                FileHelper.readFile("resources/data/InvalidIsAvailable.xml");
        /* ------------------------------------------------------------------ */

        /* get user credentials --------------------------------------------- */
        // TODO: get user credentials here
        final String userCredentials = "junit@viola.testbed.ist-phosphorus.eu";
        /* ------------------------------------------------------------------ */

        /* get decision from GAAA-tk ---------------------------------------- */
        try {
            final boolean decision = this.pep.pre(rawRequest, userCredentials);

            Assert.assertFalse("Test action should be permitted", decision);
        } catch (OperationNotAllowedFaultException e) {
            Assert.assertTrue("Test action should be permitted", true);
        }
        /* ------------------------------------------------------------------ */
    }

    @Test
    public void testAuthZValidCancelJob() throws Exception {
        final String rawRequest =
                FileHelper.readFile("resources/data/ValidCancelRequest.xml");
        /* ------------------------------------------------------------------ */

        /* get user credentials --------------------------------------------- */
        // TODO: get user credentials here
        final String userCredentials = "junit@viola.testbed.ist-phosphorus.eu";
        /* ------------------------------------------------------------------ */

        /* get decision from GAAA-tk ---------------------------------------- */

        try {
            final boolean decision = this.pep.pre(rawRequest, userCredentials);
            Assert.assertTrue("Test action should be permitted", decision);
        } catch (OperationNotAllowedFaultException e) {
            Assert.assertTrue("Test action should be permitted", true);
        }
        /* ------------------------------------------------------------------ */
    }

    /**
     * Authorize a create reservation request.
     * 
     * @throws Exception
     *             If an exception occurs within the GAAA-tk.
     */
    @Test
    // TODO: "PDP response: NotApplicable: Request and Policy semantics or
    // values don't match"
    public void testAuthZValidCreateReservation() throws Exception {
        /* load an example Harmony reservation request ---------------------- */
        final String rawRequest =
                FileHelper.readFile("resources/data/RawReservationRequest.xml");
        /* ------------------------------------------------------------------ */

        /* get user credentials --------------------------------------------- */
        // TODO: get user credentials here
        final String userCredentials = "testuser";
        /* ------------------------------------------------------------------ */

        /* get decision from GAAA-tk ---------------------------------------- */
        final boolean decision = this.pep.pre(rawRequest, userCredentials);
        Assert.assertTrue("Test action should be permitted", decision);
        /* ------------------------------------------------------------------ */
    }

    @Test
    public void testAuthZValidIsAvailable() throws Exception {
        final String rawRequest =
                FileHelper.readFile("resources/data/ValidIsAvailable.xml");
        /* ------------------------------------------------------------------ */

        /* get user credentials --------------------------------------------- */
        // TODO: get user credentials here
        final String userCredentials = "junit@viola.testbed.ist-phosphorus.eu";
        /* ------------------------------------------------------------------ */

        /* get decision from GAAA-tk ---------------------------------------- */
        try {
            final boolean decision = this.pep.pre(rawRequest, userCredentials);
            Assert.assertTrue("Test action should be permitted", decision);
        } catch (OperationNotAllowedFaultException e) {
            Assert.assertTrue("Test action should be permitted", true);
        }
        /* ------------------------------------------------------------------ */
    }

    @Test
    public void testCompleteValidPEP() throws Exception {

        final String request =
                FileHelper.readFile("resources/data/CreateReservation.xml");
        final HarmonyWSSecurity hwss = new HarmonyWSSecurity();

        String userCredentials = "testuser";// hwss.getUser(request);
        if (userCredentials.equals("")) {
            System.out
                    .println("User could not be extracted using authn module.");
            userCredentials = "junit@viola.testbed.ist-phosphorus.eu";
        }

        Assert.assertTrue(this.pep.pre(request, userCredentials));

        // servlet: super.doPost(...)
        // get GRI, if CreateReservation...
        final String gri = "83";
        this.pep.post(request, gri);

    }
}
