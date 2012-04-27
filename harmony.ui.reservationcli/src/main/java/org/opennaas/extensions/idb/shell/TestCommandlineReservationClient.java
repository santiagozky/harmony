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


package org.opennaas.extensions.idb.shell;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.muse.ws.addressing.soap.SoapFault;
import org.junit.Assert;
import org.junit.Test;

import org.opennaas.extensions.idb.shell.CommandlineReservationClient;

/**
 * 
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 * @version $Id: TestCommandlineReservationClient.java 2695 2008-03-31 22:52:36Z
 *          willner@cs.uni-bonn.de $
 */
public class TestCommandlineReservationClient {
    private final PrintStream originalOut = System.out;
    private OutputStream capturedOut;
    private PrintStream capturedPrinter;

    public TestCommandlineReservationClient() throws IOException {
        this.initCaptureStream();
    }

    /**
     * @throws IOException
     */
    private void initCaptureStream() throws IOException {
        if (this.capturedOut != null) {
            this.capturedOut.close();
        }
        if (this.capturedPrinter != null) {
            this.capturedPrinter.close();
        }
        this.capturedOut = new ByteArrayOutputStream();
        this.capturedPrinter = new PrintStream(this.capturedOut);
    }

    /**
     * @param reservationID
     * @param result
     * @return
     */
    private String parseCreateReservationResult(final String result) {
        final Pattern pattern = Pattern.compile(".*:\\s+([0-9]+@.+)");
        final Matcher matcher = pattern.matcher(result);
        final String reservationID;
        if (matcher.find()) {
            reservationID = matcher.group(1);
        } else {
            throw new IllegalArgumentException(
                    "Could not parse output. It was: " + result);
        }
        return reservationID;
    }

    @Test
    public final void testCreateGetCancelReservation() throws IOException {
        final String[] createArgs = { "-a create" };
        testReservationWorkflow(createArgs);
        //TODO: test output
    }

    @Test
    public final void testCreateGetCancelMalleableReservation() throws IOException {
        final String[] createArgs = { "-a createMalleable" };
        testReservationWorkflow(createArgs);
        //TODO: test output
    }

    /**
     * @param createArgs
     * @throws IOException
     */
    private void testReservationWorkflow(final String[] createArgs)
            throws IOException {
        String reservationID = "0@idb";

        System.setOut(this.capturedPrinter);
        CommandlineReservationClient.main(createArgs);
        System.setOut(this.originalOut);
        final String result = this.capturedOut.toString();

        Assert.assertTrue("Should show reservation ID, but was: " + result, result
                .contains("Reservation ID: "));

        reservationID = this.parseCreateReservationResult(result);

        final String[] getArgs = { "-a getstatus", "-i " + reservationID };
        this.initCaptureStream();
        System.setOut(this.capturedPrinter);
        CommandlineReservationClient.main(getArgs);
        System.setOut(this.originalOut);
        System.out.print(this.capturedOut.toString());
        Assert.assertTrue("Should return a value", this.capturedOut.toString()
                .contains("Status: "));

        final String[] cancelArgs = { "-a cancel", "-i " + reservationID };
        this.initCaptureStream();
        System.setOut(this.capturedPrinter);
        CommandlineReservationClient.main(cancelArgs);
        System.setOut(this.originalOut);
        System.out.print(this.capturedOut.toString());
        Assert.assertTrue("Should return success", this.capturedOut.toString()
                .contains("Success: true"));
    }

    /**
     * Test method for
     * {@link eu.ist_phosphorus.nrps.common.reservation.CommandlineReservationClient#main(java.lang.String[])}
     * .
     * 
     * @throws DatatypeConfigurationException
     * @throws SoapFault
     */
    @Test
    public final void testParamsAll() {
        final String[] args = { "-h", "-i 23@idb", "-s 1", "-t 2", "-r 222",
                "-a test2", "-e test3", "-d 200", "-m 50", "-st 20" };

        System.setOut(this.capturedPrinter);
        CommandlineReservationClient.main(args);
        System.setOut(this.originalOut);
        Assert.assertTrue("Should show help message", this.capturedOut
                .toString().contains("usage:"));
    }

    /**
     * Test method for
     * {@link eu.ist_phosphorus.nrps.common.reservation.CommandlineReservationClient#main(java.lang.String[])}
     * .
     * 
     * @throws IOException
     */
    @Test
    public final void testCreateGetCancelReservationWithSecurity()
            throws IOException {
        final String[] createArgs = { "-a create", "-encrypt", "-sign",
                "--certificate=/etc/certs.db", "--password=mypassword" };
        String reservationID = "0@idb";

        System.setOut(this.capturedPrinter);
        CommandlineReservationClient.main(createArgs);
        System.setOut(this.originalOut);
        final String result = this.capturedOut.toString();

        Assert.assertTrue("Should show reservation ID but was: " + result, result
                .contains("Reservation ID: "));

        reservationID = this.parseCreateReservationResult(result);

        final String[] getArgs = { "-a getstatus", "-i " + reservationID,
                "-encrypt", "-sign", "--certificate=/etc/certs.db",
                "--password=mypassword" };
        this.initCaptureStream();
        System.setOut(this.capturedPrinter);
        CommandlineReservationClient.main(getArgs);
        System.setOut(this.originalOut);
        System.out.print(this.capturedOut.toString());
        Assert.assertTrue("Should return a value", this.capturedOut.toString()
                .contains("Status: "));

        final String[] cancelArgs = { "-a cancel", "-i " + reservationID,
                "-encrypt", "-sign", "--certificate=/etc/certs.db",
                "--password=mypassword" };
        this.initCaptureStream();
        System.setOut(this.capturedPrinter);
        CommandlineReservationClient.main(cancelArgs);
        System.setOut(this.originalOut);
        System.out.print(this.capturedOut.toString());
        Assert.assertTrue("Should return success", this.capturedOut.toString()
                .contains("Success: true"));
    }
    
    

    /**
     * 
     * @throws IOException
     */
    @Test
    public final void testParamsWrong() throws IOException {
        final String[] args = { "-d Shouldn'tBeAStringHere" };

        this.initCaptureStream();
        System.setErr(this.capturedPrinter);
        CommandlineReservationClient.main(args);
        System.setErr(this.originalOut);
        System.out.println(this.capturedOut);
        Assert.assertTrue("Couldn't find required error message.",
                this.capturedOut.toString().equals(
                        "Couldn't parse parameter: For input string: \"Shouldn'tBeAStringHere\""
                                + System.getProperty("line.separator")));
    }
}
