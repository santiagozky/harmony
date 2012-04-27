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

package org.opennaas.extensions.idb.serviceinterface;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.log4j.Logger;
import org.apache.muse.core.platform.mini.MiniServlet;

import org.opennaas.extensions.idb.serviceinterface.topology.registrator.AbstractTopologyRegistrator;
import org.opennaas.core.utils.PhLogger;

public class CommonServlet extends MiniServlet {
    /**
     * Private class to log the input.
     * 
     * @author Alexander Willner (willner@cs.uni-bonn.de)
     * 
     */
    private class InputLogger extends ServletInputStream {

        private final ServletInputStream inputStream;
        private final ByteArrayOutputStream input;

        public InputLogger(final ServletInputStream inputStream) {
            super();
            this.inputStream = inputStream;
            this.input = new ByteArrayOutputStream();
        }

        @Override
        public synchronized void close() throws IOException {
            CommonServlet.this.requestLogger
                    .debug("Incoming request [content]:\n"
                            + this.input.toString().replace('\0', ' ').trim());
            this.input.flush();
            this.inputStream.close();
            super.close();
        }

        @Override
        public int read() throws IOException {
            return this.inputStream.read();
        }

        @Override
        public synchronized int read(final byte[] b, final int off,
                final int len) throws IOException {
            this.input.write(b);
            return this.inputStream.read(b, off, len);
        }
    }

    /**
     * Private class to log the output.
     * 
     * @author Alexander Willner (willner@cs.uni-bonn.de)
     * 
     */
    private class OutputLogger extends ServletOutputStream {

        private final ServletOutputStream outputStream;
        private final ByteArrayOutputStream output = new ByteArrayOutputStream();

        public OutputLogger(final ServletOutputStream outputStream) {
            super();
            this.outputStream = outputStream;
        }

        @Override
        public void close() throws IOException {
            CommonServlet.this.requestLogger
                    .debug("Outgoing request [content]:\n"
                            + this.output.toString().trim());
            this.output.flush();
            this.outputStream.close();
        }

        @Override
        public synchronized void flush() throws IOException {
            CommonServlet.this.requestLogger
                    .debug("Outgoing request [content]:\n"
                            + this.output.toString().trim());
            this.output.flush();
            this.outputStream.flush();
        }

        @Override
        public void write(final int b) throws IOException {
            this.output.write(b);
            this.outputStream.write(b);
        }
    }

    /**
     * Private class to log the input.
     * 
     * @author Alexander Willner (willner@cs.uni-bonn.de)
     * 
     */
    private class RequestWrapper extends HttpServletRequestWrapper {

        InputLogger logIn;

        public RequestWrapper(final HttpServletRequest request) {
            super(request);
        }

        @Override
        public InputLogger getInputStream() throws IOException {
            this.logIn = new InputLogger(super.getInputStream());
            return this.logIn;
        }
    }

    /**
     * Private class to log the output.
     * 
     * @author Alexander Willner (willner@cs.uni-bonn.de)
     * 
     */
    private class ResponseWrapper extends HttpServletResponseWrapper {

        OutputLogger logOut;

        public ResponseWrapper(final HttpServletResponse response) {
            super(response);
        }

        @Override
        public OutputLogger getOutputStream() throws IOException {
            this.logOut = new OutputLogger(super.getOutputStream());
            return this.logOut;
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            return new PrintWriter(this.getOutputStream());
        }
    }

    /** serial ID */
    private static final long serialVersionUID = 1L;

    /** PerformanceLogger instance. */
    final transient Logger performanceLogger;

    /** RequestLogger instance. */
    final transient Logger requestLogger;

    /**
     * 
     */
    public CommonServlet() {
        this.performanceLogger = PhLogger.getSeparateLogger("performance");
        final AbstractTopologyRegistrator registrator = AbstractTopologyRegistrator
                .getLatestInstance();
        final String domainId;
        if (null == registrator) {
            domainId = "unknown";
        } else {
            domainId = registrator.getDomainId();
        }
        this.requestLogger = PhLogger.getSeparateLogger("requests." + domainId);
    }

    @Override
    public void doPost(final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {
        /* request logging -------------------------------------------------- */
        this.requestLogger.debug("Incoming request [from]: "
                + request.getRemoteAddr());
        this.requestLogger.debug("Incoming request [length]: "
                + request.getContentLength());
        /* ------------------------------------------------------------------ */

        /* performance logging ---------------------------------------------- */
        /*
         * this.performanceLogger.log(PerformanceLogLevel.PERFORMANCE_LOG,
         * "start_servlet"); final String name =
         * Thread.currentThread().getName(); final long startTime =
         * System.currentTimeMillis();
         */
        /* ------------------------------------------------------------------ */

        super
                .doPost(new RequestWrapper(request), new ResponseWrapper(
                        response));
        /* performance logging ---------------------------------------------- */
        /*
         * final long endTime = System.currentTimeMillis();
         * Thread.currentThread().setName(name); final long duration = endTime -
         * startTime;
         * this.performanceLogger.log(PerformanceLogLevel.PERFORMANCE_LOG,
         * "duration_servlet " + duration + "ms");
         */
        /* ------------------------------------------------------------------ */
    }
}
