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
package org.opennaas.core.security.utils.wrapper;

import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.muse.ws.addressing.soap.SoapFault;

import org.opennaas.core.security.utils.streams.ResponseStream;

public class ResponseWrapper extends HttpServletResponseWrapper {
    private final PrintWriter writer;
    private final ResponseStream stream;

    public ResponseWrapper(final ServletResponse response)
            throws java.io.IOException, SoapFault {
        super((HttpServletResponse) response);
        this.stream = new ResponseStream(response.getOutputStream());
        this.writer = new PrintWriter(this.stream);
    }

    @Override
    public ServletOutputStream getOutputStream() throws java.io.IOException {
        return this.stream;
    }

    @Override
    public PrintWriter getWriter() throws java.io.IOException {
        return this.writer;
    }
}