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

import org.junit.Assert;

import org.opennaas.extensions.idb.database.hibernate.Subscription;
import org.opennaas.extensions.idb.exception.database.DatabaseException;

public class TestSubscriptionRelation extends DatabaseTest {

	/** test subscription. */
	Subscription subscription;

	@Override
	public void setUpBeforeEveryTest() {
		// create test subscription
		this.subscription = new Subscription("subscriptionTopic",
				"subscriptionEPR");
	}

	@Override
	public void tearDownAfterEveryTest() {
		// TODO Auto-generated method stub

	}

	@Override
	public void testReferentialIntegrity() {
		// no referential integrity
		Assert.assertTrue(true);
	}

	@Override
	public void testSaveLoadEditDeleteRelationTuple() throws DatabaseException {
		// save
		this.subscription.save();
		Assert.assertTrue("subscription-ID should be greater than 0",
				this.subscription.getSubscriptionId() > 0);

		// load
		final Subscription loadedSub = Subscription.load(this.subscription
				.getSubscriptionId());
		Assert.assertEquals(
				"persisted and loaded subscription should be equal",
				this.subscription, loadedSub);

		// edit
		loadedSub.setSubscriptionEPR("edited subscriptionEPR");
		loadedSub.save();

		final Subscription editedSub = Subscription.load(loadedSub
				.getSubscriptionId());
		// Assert.assertNotSame("edited subscription should be different",
		// loadedSub, editedSub);

		// delete
		loadedSub.delete();
		Assert.assertNull("test subscription should be deleted",
				Subscription.load(this.subscription.getSubscriptionId()));
	}
}
