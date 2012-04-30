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
import org.junit.Test;

import org.opennaas.core.utils.Helpers;
import org.opennaas.extensions.idb.database.hibernate.DomSupportedAdaption;
import org.opennaas.extensions.idb.database.hibernate.DomSupportedBandwidth;
import org.opennaas.extensions.idb.database.hibernate.Domain;
import org.opennaas.extensions.idb.exception.database.DatabaseException;

public class TestDomSupportedBandwidthRelation extends DatabaseTest {

	/** reference domain. */
	private static Domain domain;
	
	@BeforeClass
    public static void setUpBeforeClass() throws DatabaseException {
        // create reference domain
        TestDomSupportedBandwidthRelation.domain = DatabaseTest
                .createReferenceDomain();
    }

    @AfterClass
    public static void tearDownAfterClass() throws DatabaseException {
        // delete reference domain
        TestDomSupportedBandwidthRelation.domain.delete();
        Assert.assertNull("reference domain should be deleted",
                Domain.load(TestDomSupportedBandwidthRelation.domain
                        .getName()));
    }


	/** test bandwidth. */
	DomSupportedBandwidth bw;

	@Override
	public void setUpBeforeEveryTest() {
		// TODO Auto-generated method stub
		this.bw = new DomSupportedBandwidth(TestDomSupportedBandwidthRelation.domain,
											Helpers.getPositiveRandomLong());
	}

	@Override
    public void tearDownAfterEveryTest() {
        // TODO Auto-generated method stub
    }

    @Override
    public void testReferentialIntegrity() throws DatabaseException {
    	// save test bandwidth
    	this.bw.save();

    	// load domain and check for saved bandwidth
    	final Domain referenceDomain = Domain
    		.load(TestDomSupportedBandwidthRelation.domain.getName());
    	Assert.assertNotNull("referenced bandwidth should be existent",
    		referenceDomain.getSupportedBandwidths().contains(this.bw));
    	
    	referenceDomain.getSupportedBandwidths().remove(this.bw);
    	this.bw.delete();
    }

    @Override
    public void testSaveLoadEditDeleteRelationTuple() throws DatabaseException {
        // save
        this.bw.save();
        long pk = this.bw.getPK_Bandwidth();
        Assert.assertTrue("PK_Bandwidth should be greater than 0", pk > 0);

        // load
        final DomSupportedBandwidth loadedBw = DomSupportedBandwidth.load(pk);
        Assert.assertEquals(
                "persisted and loaded bandwidth should be equal",
                this.bw, loadedBw);

        // edit
        DomSupportedBandwidth copyBw = loadedBw.getCopy();
        loadedBw.setBandwidth(654321);
        loadedBw.save();

        final DomSupportedBandwidth editedBw = DomSupportedBandwidth.load(loadedBw
                .getPK_Bandwidth());
        Assert.assertNotSame("edited bandwidth should be different",
                copyBw, editedBw);

        // delete
        loadedBw.getDomain().getSupportedBandwidths().remove(loadedBw);
        loadedBw.delete();
        Assert.assertNull("test bandwidth should be deleted", DomSupportedBandwidth
                .load(pk));
    }
    
    @Test
    public void testLoadAll() throws DatabaseException {
    	// save
    	this.bw.save();
    	DomSupportedBandwidth secondBw = this.bw.getCopy();
    	secondBw.setBandwidth(Helpers.getPositiveRandomLong());
    	secondBw.save();
    	
    	// load
    	Set<DomSupportedBandwidth> setBws = DomSupportedBandwidth
    										.loadAll(TestDomSupportedBandwidthRelation.domain);

    	Assert.assertTrue("there should be two bandwidths", setBws.size() == 2);
    	Assert.assertTrue("first bandwidth should be existent", setBws.contains(this.bw));
    	Assert.assertTrue("second bandwidth should be existent", setBws.contains(secondBw));

        // delete
        this.bw.getDomain().getSupportedAdaptions().clear();
        this.bw.delete();
        secondBw.delete();
        Assert.assertNull("first bandwidth should be deleted", DomSupportedAdaption
                .load(this.bw.getPK_Bandwidth()));
        Assert.assertNull("second bandwidth should be deleted", DomSupportedAdaption
                .load(secondBw.getPK_Bandwidth()));
    }
}