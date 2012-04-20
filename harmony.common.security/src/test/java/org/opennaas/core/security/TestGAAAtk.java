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

import java.util.HashMap;

import junit.framework.Assert;

import org.aaaarch.config.ConstantsNS;
import org.aaaarch.gaaapi.ActionSet;
import org.aaaarch.gaaapi.PEP;
import org.aaaarch.gaaapi.ResourceHelper;
import org.junit.Before;
import org.junit.Test;

import org.opennaas.core.utils.Config;

/**
 * Test different GAAA-tk API calls.
 * 
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 * @version $Id$
 * 
 */
public class TestGAAAtk {

    private HashMap<String, String> subjmap;
    private HashMap<String, String> resmap;
    private HashMap<String, String> actmap;
    private String resourceIdURI;
    private String action;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.subjmap = new HashMap<String, String>();
        this.actmap = new HashMap<String, String>();

        /* setup configuration ---------------------------------------------- */
        final String resourceNamespace = Config.getString("aai",
                "resource.namespace");
        final String resourceDomain = Config
                .getString("aai", "resource.domain");
        final String resourceService = Config.getString("aai",
                "resource.service");
        final String resourceSource = Config
                .getString("aai", "resource.source");
        final String resourceTarget = Config
                .getString("aai", "resource.target");
        final String subjectId = Config.getString("test", "subject.id");
        final String subjectConfdata = Config.getString("test",
                "subject.confdata");
        final String subjectRole = Config.getString("test", "subject.role");
        final String subjectContext = Config
                .getString("test", "subject.context");

        this.resourceIdURI = resourceNamespace + resourceDomain
                + resourceService + resourceSource + resourceTarget;
        this.action = ActionSet.NSP_CREATE_PATH;
        this.resmap = ResourceHelper.parseResourceURI(this.resourceIdURI);
        this.actmap.put(ConstantsNS.ACTION_ACTION_ID, this.action);
        this.subjmap.put(ConstantsNS.SUBJECT_SUBJECT_ID, subjectId);
        this.subjmap.put(ConstantsNS.SUBJECT_CONFDATA, subjectConfdata);
        this.subjmap.put(ConstantsNS.SUBJECT_ROLE, subjectRole);
        this.subjmap.put(ConstantsNS.SUBJECT_CONTEXT, subjectContext);
        /* ------------------------------------------------------------------ */
    }

    /**
     * This is the most general PEP API. It be adopted for XACML-NRP profile and
     * using topology.
     * 
     * @param subjmap
     * @param resmap
     * @param actmap
     * @throws Exception
     */
    @Test
    public void testGeneralAPI() throws Exception {
        final boolean result = PEP.authorizeAction(this.resmap, this.actmap,
                this.subjmap);
        Assert.assertTrue("General method (MMM) should return true", result);
    }

    /**
     * A more simple API.
     * 
     * @param resourceIdURI
     * @param action
     * @param subjmap
     * @throws Exception
     */
    @Test
    public void testSimpleAPI() throws Exception {
        final boolean result = PEP.authorizeAction(this.resourceIdURI,
                this.action, this.subjmap);
        Assert.assertTrue("Simple method (SSM) should return true", result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testTnaBasedPolicy() throws Exception {
        final String resourceNamespace = Config.getString("aai",
                "resource.namespace");
        final String resourceDomain = Config
                .getString("aai", "resource.domain");
        final String resourceService = Config.getString("aai",
                "resource.service");
        final String resourceSource = Config
                .getString("aai", "resource.source");
        final String resourceTarget = Config
                .getString("aai", "resource.target");
        final String resourceSourceWrong = "source=9.9.9.9/";

        this.resourceIdURI = resourceNamespace + resourceDomain
                + resourceService + resourceSourceWrong + resourceTarget;
        this.resmap = ResourceHelper.parseResourceURI(this.resourceIdURI);

        final boolean resultWrong = PEP.authorizeAction(this.resmap,
                this.actmap, this.subjmap);
        Assert.assertFalse("General method should return false", resultWrong);

        this.resourceIdURI = resourceNamespace + resourceDomain
                + resourceService + resourceSource + resourceTarget;
        this.resmap = ResourceHelper.parseResourceURI(this.resourceIdURI);

        final boolean result = PEP.authorizeAction(this.resmap, this.actmap,
                this.subjmap);
        Assert.assertTrue("General method should return true", result);
    }
}