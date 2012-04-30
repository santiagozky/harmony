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

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;

import org.opennaas.extensions.idb.database.hibernate.Connections;
import org.opennaas.extensions.idb.database.hibernate.Domain;
import org.opennaas.extensions.idb.database.hibernate.Service;
import org.opennaas.extensions.idb.database.hibernate.TNAPrefix;
import org.opennaas.extensions.idb.exception.database.DatabaseException;

public class TestTNAPrefixRelation extends DatabaseTest {

	/** reference domain. */
	private static Domain domain;

	@BeforeClass
    public static void setUpBeforeClass() throws DatabaseException {
        // create reference domain
        TestTNAPrefixRelation.domain = DatabaseTest
                .createReferenceDomain();
    }

    @AfterClass
    public static void tearDownAfterClass() throws DatabaseException {
        // delete reference domain
        TestTNAPrefixRelation.domain.delete();
        Assert.assertNull("reference domain should be deleted",
                Domain.load(TestTNAPrefixRelation.domain
                        .getName()));
    }


	/** test prefix. */
	TNAPrefix prefix;

	@Override
	public void setUpBeforeEveryTest() {
		// TODO Auto-generated method stub
		this.prefix = new TNAPrefix(TestTNAPrefixRelation.domain, "127.0.0.1/16");
	}

	@Override
    public void tearDownAfterEveryTest() {
        // TODO Auto-generated method stub

    }

    @Override
    public void testReferentialIntegrity() throws DatabaseException {
    	// save test prefix
    	this.prefix.save();

    	// load domain and check for saved prefix
    	final Domain referenceDomain = Domain
    		.load(TestTNAPrefixRelation.domain.getName());
    	Assert.assertNotNull("referenced prefix should be existent",
    		referenceDomain.getPrefixes().contains(this.prefix));
    	
    	referenceDomain.getPrefixes().remove(this.prefix);
    	this.prefix.delete();
    }

    @Override
    public void testSaveLoadEditDeleteRelationTuple() throws DatabaseException {
        // save
        this.prefix.save();
        Assert.assertTrue("prefix should be greater than 0",
                this.prefix.getPrefix().length() > 0);

        // load
        final TNAPrefix loadedPre = TNAPrefix.load(this.prefix
                .getPrefix());
        Assert.assertEquals(
                "persisted and loaded prefix should be equal",
                this.prefix, loadedPre);

//        // edit
//        loadedPre.setPrefix("127.0.0.1/14");
//        loadedPre.save();
//
//        final TNAPrefix editedPre = TNAPrefix.load(loadedPre
//                .getPrefix());
//        Assert.assertNotSame("edited prefix should be different",
//                loadedPre, editedPre);

        // delete
        loadedPre.getDomain().getPrefixes().remove(loadedPre);
        loadedPre.delete();
        Assert.assertNull("test prefix should be deleted", TNAPrefix
                .load(loadedPre.getPrefix()));
    }
}
