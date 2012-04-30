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
import org.opennaas.extensions.idb.database.hibernate.Domain;
import org.opennaas.extensions.idb.exception.database.DatabaseException;

public class TestDomSupportedAdaptionRelation extends DatabaseTest {

	/** reference domain. */
	private static Domain domain;

	@BeforeClass
	public static void setUpBeforeClass() throws DatabaseException {
		// create reference domain
		TestDomSupportedAdaptionRelation.domain = DatabaseTest
				.createReferenceDomain();
	}

	@AfterClass
	public static void tearDownAfterClass() throws DatabaseException {
		// delete reference domain
		TestDomSupportedAdaptionRelation.domain.delete();
		Assert.assertNull("reference domain should be deleted",
				Domain.load(TestDomSupportedAdaptionRelation.domain.getName()));
	}

	/** test adaption. */
	DomSupportedAdaption adaption;

	@Override
	public void setUpBeforeEveryTest() {
		// TODO Auto-generated method stub
		this.adaption = new DomSupportedAdaption(
				TestDomSupportedAdaptionRelation.domain,
				Helpers.getRandomString());
	}

	@Override
	public void tearDownAfterEveryTest() {
		// TODO Auto-generated method stub
	}

	@Override
	public void testReferentialIntegrity() throws DatabaseException {
		// save test adaption
		this.adaption.save();

		// load domain and check for saved adaption
		final Domain referenceDomain = Domain
				.load(TestDomSupportedAdaptionRelation.domain.getName());
		Assert.assertNotNull("referenced adaption should be existent",
				referenceDomain.getSupportedAdaptions().contains(this.adaption));

		referenceDomain.getSupportedAdaptions().remove(this.adaption);
		this.adaption.delete();
	}

	@Override
	public void testSaveLoadEditDeleteRelationTuple() throws DatabaseException {
		// save
		this.adaption.save();
		long pk = this.adaption.getPK_Adaption();
		Assert.assertTrue("PK_Adaption should be greater than 0", pk > 0);

		// load
		final DomSupportedAdaption loadedAd = DomSupportedAdaption.load(pk);
		Assert.assertEquals("persisted and loaded adaption should be equal",
				this.adaption, loadedAd);

		// edit
		DomSupportedAdaption copyAd = loadedAd.getCopy();
		loadedAd.setAdaption("blubblibla");
		loadedAd.save();

		final DomSupportedAdaption editedAd = DomSupportedAdaption
				.load(loadedAd.getPK_Adaption());
		Assert.assertNotSame("edited adaption should be different", copyAd,
				editedAd);

		// delete
		loadedAd.getDomain().getSupportedAdaptions().remove(loadedAd);
		loadedAd.delete();
		Assert.assertNull("test adaption should be deleted",
				DomSupportedAdaption.load(pk));
	}

	@Test
	public void testLoadAll() throws DatabaseException {
		// save
		this.adaption.save();
		DomSupportedAdaption secondAd = this.adaption.getCopy();
		secondAd.setAdaption(Helpers.getRandomString());
		secondAd.save();

		// load
		Set<DomSupportedAdaption> setAds = DomSupportedAdaption
				.loadAll(TestDomSupportedAdaptionRelation.domain);

		Assert.assertTrue("there should be two adaptions", setAds.size() == 2);
		Assert.assertTrue("first adaption should be existent",
				setAds.contains(this.adaption));
		Assert.assertTrue("second adaption should be existent",
				setAds.contains(secondAd));

		// delete
		this.adaption.getDomain().getSupportedAdaptions().clear();
		this.adaption.delete();
		// deleting this.adaption removes secondAd because of cascade, I
		// think....
		secondAd.delete();
		Assert.assertNull("first adaption should be deleted",
				DomSupportedAdaption.load(this.adaption.getPK_Adaption()));
		Assert.assertNull("second adaption should be deleted",
				DomSupportedAdaption.load(secondAd.getPK_Adaption()));
	}
}
