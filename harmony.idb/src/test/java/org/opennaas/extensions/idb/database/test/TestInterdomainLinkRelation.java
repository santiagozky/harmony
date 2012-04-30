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


package org.opennaas.extensions.idb.database.test;

import java.util.Set;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;

import org.opennaas.core.utils.Helpers;
import org.opennaas.extensions.idb.database.hibernate.Domain;
import org.opennaas.extensions.idb.database.hibernate.Endpoint;
import org.opennaas.extensions.idb.database.hibernate.InterDomainLink;
import org.opennaas.extensions.idb.exception.database.DatabaseException;

public class TestInterdomainLinkRelation extends DatabaseTest {

    /** reference sourceDomain. */
    private static Domain sourceDomain;

    /** reference destDomain. */
    private static Domain destDomain;

    /** reference endpoint. */
    private static Endpoint endpoint;

    @BeforeClass
    public static void setUpBeforeClass() throws DatabaseException {
        // create reference sourceDomain
        TestInterdomainLinkRelation.sourceDomain = DatabaseTest
                .createReferenceDomain();
        // create reference destDomain
        TestInterdomainLinkRelation.destDomain = DatabaseTest
                .createReferenceDomain();
        // create reference sourceEndpoint
        TestInterdomainLinkRelation.endpoint = DatabaseTest
                .createReferenceEndpoint(TestInterdomainLinkRelation.sourceDomain
                        .getName());
    }

    @AfterClass
    public static void tearDownAfterClass() throws DatabaseException {
        // delete reference sourceDomain
        TestInterdomainLinkRelation.sourceDomain.delete();
        Assert.assertNull("reference sourceDomain should be deleted", Domain
                .load(TestInterdomainLinkRelation.sourceDomain.getName()));
        // delete reference destDomain
        TestInterdomainLinkRelation.destDomain.delete();
        Assert.assertNull("reference destDomain should be deleted", Domain
                .load(TestInterdomainLinkRelation.destDomain.getName()));
    }

    /** test interdomainLink. */
    @SuppressWarnings("unused")
    private InterDomainLink interdomainLink;

    @Override
    public void setUpBeforeEveryTest() {
        // create test interdomainLink
        this.interdomainLink = new InterDomainLink(Helpers.getRandomString(),
                TestInterdomainLinkRelation.sourceDomain.getName(),
                TestInterdomainLinkRelation.destDomain.getName(),
                TestInterdomainLinkRelation.endpoint, Helpers
                        .getPositiveRandomInt());
        this.interdomainLink.setPk_Interdomainlink(0);
    }

    @Override
    public void tearDownAfterEveryTest() {
        // nothing
    }

    @Override
    public void testReferentialIntegrity() throws DatabaseException {
//    	// save test interDomainLink
//    	this.interdomainLink.save();
//
//    	// load endpoint and check for saved interDomainLink
//    	final Endpoint referenceEndpoint = Endpoint
//    		.load(TestInterdomainLinkRelation.endpoint.getTNA());
//    	final Set<InterDomainLink> referenceLinks = referenceEndpoint
//    		.getInterDomainLinks();
//    	Assert.assertTrue("referenced interDomainLink should be the same",
//    		referenceLinks.contains(this.interdomainLink));
    }

    @Override
    public void testSaveLoadEditDeleteRelationTuple() throws DatabaseException {
    	System.out.println("***" + this.interdomainLink.toString());
        // save
        this.interdomainLink.save();
        Assert.assertTrue("interDomainLink-Id should be larger than 0",
                this.interdomainLink.getPk_Interdomainlink() > 0);

//        // load
//        final InterDomainLink loadedLink = InterDomainLink.load(this.interdomainLink
//        														.getPk_Interdomainlink());
//        Assert.assertEquals("persisted and loaded InterDomainLink should be equal",
//                this.interdomainLink, loadedLink);
//
//        // edit
//        loadedLink.setLinkName("edited name");
//        loadedLink.save();
//
//        final InterDomainLink editedLink = InterDomainLink.load(
//        											loadedLink.getPk_Interdomainlink());
////        Assert.assertNotSame("edited InterDomainLink should be different",
////                loadedLink, editedLink);
//
//        // delete
//        loadedLink.delete();
//        Assert.assertNull("test InterDomainLink should be deleted", InterDomainLink
//                .load(this.interdomainLink.getPk_Interdomainlink()));
    }
}
