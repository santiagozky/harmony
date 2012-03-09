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

/**
 * 
 */
package eu.ist_phosphorus.harmony.test.common.serviceinterface;

import junit.framework.Assert;

import org.junit.Test;
import org.w3c.dom.Element;

import eu.ist_phosphorus.harmony.common.serviceinterface.RequestHandler;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetStatus;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetStatusType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.utils.JaxbSerializer;

/**
 * @author jangassen
 * 
 */
public class TestRequestHandler extends RequestHandler {

    public final GetStatusType getStatus(final GetStatusType req) {
        return req;
    }

    public final String runRequest(final String req) {
        return req;
    }

    public final String runTest(final String req) {
        return req;
    }

    // =========================================================================
    // Request Handler Methods
    // =========================================================================

    /**
     * Test Method reflection to default methods.
     * 
     * @throws Throwable
     */
    @Test
    public final void testDefaultMethodLookup() throws Throwable {
        final String result = (String) this.invokeMethod("Hallo", null);

        Assert.assertEquals(result, "Hallo");
    }

    /**
     * Test Object Handling
     * 
     * @throws Throwable
     */
    @Test
    public final void testHandling() throws Throwable {
        final GetStatus req = new GetStatus();
        final GetStatusType reqType = new GetStatusType();

        reqType.setReservationID("some id");
        req.setGetStatus(reqType);

        final Element reqElem = JaxbSerializer.getInstance().objectToElement(
                req);

        final Element resElem = this.handle("getStatus", reqElem);

        final GetStatus res = (GetStatus) JaxbSerializer.getInstance()
                .elementToObject(resElem);

        Assert.assertEquals(res.getGetStatus().getReservationID(), req
                .getGetStatus().getReservationID());
    }

    /**
     * Test method reflection to named methods.
     * 
     * @throws Throwable
     */
    @Test
    public final void testNamedMethodLookup() throws Throwable {
        final String result = (String) this.invokeMethod("Hallo", "runTest");

        Assert.assertEquals(result, "Hallo");
    }
}
