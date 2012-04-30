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
import org.junit.Test;

import org.opennaas.extensions.idb.database.hibernate.Reservation;
import org.opennaas.extensions.idb.database.hibernate.Service;
import org.opennaas.extensions.idb.exception.database.DatabaseException;
import org.opennaas.extensions.idb.utils.ReservationHelpers;

/**
 * Test reservation relation
 * 
 * @author Alexander Zimmermann (zimmerm2@cs.uni-bonn.de)
 */
public class TestReservationRelation extends DatabaseTest {

    /** test reservation. */
    private Reservation reservation;

    @Override
    public void setUpBeforeEveryTest() {
        // create test reservation
        this.reservation = ReservationHelpers.getTestReservation();
        this.reservation.setReservationId(0);
    }

    @Override
    public void tearDownAfterEveryTest() {
        // nothing here
    }

    @Override
    public void testReferentialIntegrity() {
        // no referential integrity
        Assert.assertTrue(true);
    }

    @Override
    public void testSaveLoadEditDeleteRelationTuple() throws DatabaseException {
        // save
        this.reservation.save();
        Assert.assertTrue("Reservation-ID should be greater than 0",
                this.reservation.getReservationId() > 0);

        // load
        final Reservation loadedRes = Reservation.load(this.reservation
                .getReservationId());
        Assert.assertEquals(
                "persisted and loaded Reservations should be equal",
                this.reservation, loadedRes);

        // edit
        loadedRes.setConsumerUrl("edited consumerURL");
        loadedRes.save();

        final Reservation editedRes = Reservation.load(loadedRes
                .getReservationId());
//        Assert.assertNotSame("edited Reservation should be different",
//                this.reservation, editedRes);

        // delete
        loadedRes.delete();
        Assert.assertNull("test reservation should be deleted", Reservation
                .load(this.reservation.getReservationId()));
    }

    @Test
    public void testTransitivePersistence() throws DatabaseException {
        // create reference service and put it in the reservation object
        Service service = ReservationHelpers.getTestService();
        service.setPK_service(0);
        this.reservation.addService(service);
        // save the changed reservation
        this.reservation.save();

        // get the service from the reservation, because of generated primary
        // key
        service = this.reservation.getService(service.getServiceId());

        // load reservation new from DB and check if service is existent
        Reservation testRes = Reservation.load(this.reservation
                .getReservationId());
        Assert.assertNotNull("there should be services in the reservation",
                testRes.getServices());
        Assert.assertTrue("stored service should be existent", testRes
                .getServices().containsValue(service));

        // remove the service in the reservation object and save this
        // -> the service should also be removed
        testRes.getServices().remove(new Integer(service.getServiceId()));
        service.delete();
        testRes.save();

        // load domain new from DB and check if endpoint is not existent anymore
        testRes = Reservation.load(this.reservation.getReservationId());
        Assert.assertTrue("there should be no services anymore", testRes
                .getServices().isEmpty());
        Assert.assertNull("the service should be deleted", Service.load(service
                .getPK_service()));

        // delete the test reservation
        testRes.delete();
    }
}
