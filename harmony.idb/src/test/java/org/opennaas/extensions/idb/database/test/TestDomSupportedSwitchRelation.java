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
import org.opennaas.extensions.idb.database.hibernate.DomSupportedSwitch;
import org.opennaas.extensions.idb.database.hibernate.Domain;
import org.opennaas.extensions.idb.exception.database.DatabaseException;

public class TestDomSupportedSwitchRelation extends DatabaseTest {

	/** reference domain. */
	private static Domain domain;
	
	@BeforeClass
    public static void setUpBeforeClass() throws DatabaseException {
        // create reference domain
        TestDomSupportedSwitchRelation.domain = DatabaseTest
                .createReferenceDomain();
    }

    @AfterClass
    public static void tearDownAfterClass() throws DatabaseException {
        // delete reference domain
        TestDomSupportedSwitchRelation.domain.delete();
        Assert.assertNull("reference domain should be deleted",
                Domain.load(TestDomSupportedSwitchRelation.domain
                        .getName()));
    }


	/** test switch. */
	DomSupportedSwitch switchd;

	@Override
	public void setUpBeforeEveryTest() {
		// TODO Auto-generated method stub
		this.switchd = new DomSupportedSwitch(TestDomSupportedSwitchRelation.domain,
											  Helpers.getRandomString());
	}

	@Override
    public void tearDownAfterEveryTest() {
        // TODO Auto-generated method stub
    }

    @Override
    public void testReferentialIntegrity() throws DatabaseException {
    	// save test switch
    	this.switchd.save();

    	// load domain and check for saved switch
    	final Domain referenceDomain = Domain
    		.load(TestDomSupportedSwitchRelation.domain.getName());
    	Assert.assertNotNull("referenced switch should be existent",
    		referenceDomain.getSupportedSwitchMatrix().contains(this.switchd));
    	
    	referenceDomain.getSupportedSwitchMatrix().remove(this.switchd);
    	this.switchd.delete();
    }

    @Override
    public void testSaveLoadEditDeleteRelationTuple() throws DatabaseException {
        // save
        this.switchd.save();
        long pk = this.switchd.getPK_Switch();
        Assert.assertTrue("PK_Switch should be greater than 0", pk > 0);

        // load
        final DomSupportedSwitch loadedSw = DomSupportedSwitch.load(pk);
        Assert.assertEquals(
                "persisted and loaded switch should be equal",
                this.switchd, loadedSw);

        // edit
        DomSupportedSwitch copySw = loadedSw.getCopy();
        loadedSw.setSwitch("blubblibla");
        loadedSw.save();

        final DomSupportedSwitch editedSw = DomSupportedSwitch.load(loadedSw
                .getPK_Switch());
        Assert.assertNotSame("edited switch should be different",
                copySw, editedSw);

        // delete
        loadedSw.getDomain().getSupportedSwitchMatrix().remove(loadedSw);
        loadedSw.delete();
        Assert.assertNull("test switch should be deleted", DomSupportedSwitch
                .load(pk));
    }
    
    @Test
    public void testLoadAll() throws DatabaseException {
    	// save
    	this.switchd.save();
    	DomSupportedSwitch secondSw = this.switchd.getCopy();
    	secondSw.setSwitch(Helpers.getRandomString());
    	secondSw.save();
    	
    	// load
    	Set<DomSupportedSwitch> setSws = DomSupportedSwitch
    										.loadAll(TestDomSupportedSwitchRelation.domain);

    	Assert.assertTrue("there should be two switches", setSws.size() == 2);
    	Assert.assertTrue("first switch should be existent", setSws.contains(this.switchd));
    	Assert.assertTrue("second switch should be existent", setSws.contains(secondSw));

        // delete
        this.switchd.getDomain().getSupportedAdaptions().clear();
        this.switchd.delete();
        secondSw.delete();
        Assert.assertNull("first switch should be deleted", DomSupportedAdaption
                .load(this.switchd.getPK_Switch()));
        Assert.assertNull("second switch should be deleted", DomSupportedAdaption
                .load(secondSw.getPK_Switch()));
    }
}