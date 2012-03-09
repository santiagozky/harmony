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
package eu.ist_phosphorus.harmony.test.common.serviceinterface.databinding.utils;

import junit.framework.Assert;

import org.apache.muse.ws.addressing.soap.SoapFault;
import org.junit.Test;
import org.w3c.dom.Element;

import eu.ist_phosphorus.harmony.common.serviceinterface.RequestHandler;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetStatus;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetStatusType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.exceptions.InvalidRequestFaultException;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.exceptions.OperationNotSupportedFaultException;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.utils.FaultConverter;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.utils.JaxbSerializer;

/**
 * @author gassen
 * 
 */
public class TestFaultConverter extends RequestHandler {
    private final NullPointerException testException = new NullPointerException(
            "some meaningless text...");

    private final OperationNotSupportedFaultException testException2 = new OperationNotSupportedFaultException(
            "bla bla");

    /**
     * Test the Faultconverter
     * 
     * @throws InvalidRequestFaultException
     * @throws UnexpectedFaultException
     */
    @Test
    public final void testFaultConverter() throws InvalidRequestFaultException,
            UnexpectedFaultException {
        final GetStatus obj = new GetStatus();
        final GetStatusType type = new GetStatusType();
        type.setReservationID("42@test");
        obj.setGetStatus(type);

        final Element elem = JaxbSerializer.getInstance().objectToElement(obj);

        Throwable newEx;
        StackTraceElement[] orig;
        StackTraceElement[] converted;

        /* Test Default exceptions ------------------------------------------ */
        try {
            this.handle(elem, "testMethod");
            Assert.fail("Should throw a SoapFault!");
        } catch (final SoapFault ex) {
            newEx = FaultConverter.getInstance().getOriginalFault(ex);
            Assert.assertEquals(UnexpectedFaultException.class, newEx
                    .getClass());
            Assert.assertEquals(UnexpectedFaultException.class, newEx
                    .getClass());
            Assert.assertEquals(this.testException.getMessage(), newEx
                    .getCause().getMessage());
            orig = this.testException.getStackTrace();
            converted = newEx.getCause().getStackTrace();
            Assert.assertEquals(orig.length, converted.length);

            for (int x = 0; x < orig.length; x++) {
                Assert.assertEquals(orig[x], converted[x]);
            }
        }

        /* Test Custom Exceptions ------------------------------------------- */
        try {
            this.handle(elem, "testMethod2");
            Assert.fail("Should throw a SoapFault!");
        } catch (final SoapFault ex) {
            newEx = FaultConverter.getInstance().getOriginalFault(ex);
            Assert.assertEquals(OperationNotSupportedFaultException.class,
                    newEx.getClass());

            Assert.assertEquals(this.testException2.getMessage(), newEx
                    .getMessage());

            orig = this.testException2.getStackTrace();
            converted = newEx.getStackTrace();

            Assert.assertEquals(orig.length, converted.length);
            for (int x = 0; x < orig.length; x++) {
                Assert.assertEquals(orig[x], converted[x]);
            }
        }
    }

    /**
     * Dummy method to throw a npe.
     * 
     * @param param
     */
    public final void testMethod(final GetStatusType param) {
        throw this.testException;
    }

    /**
     * Dummy method to throw a custom exception.
     * 
     * @param param
     * @throws OperationNotSupportedFaultException
     */
    public final void testMethod2(final GetStatusType param)
            throws OperationNotSupportedFaultException {
        throw this.testException2;
    }
}
