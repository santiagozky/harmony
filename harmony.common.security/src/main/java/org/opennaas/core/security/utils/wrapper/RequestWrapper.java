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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.opennaas.core.security.utils.streams.RequestStream;

import org.apache.muse.ws.addressing.soap.SoapFault;
import org.xml.sax.SAXException;

public class RequestWrapper extends HttpServletRequestWrapper {
    private BufferedReader reader;
    private RequestStream stream;
    String request = "";

    public RequestWrapper(final ServletRequest inReq) throws IOException,
            SoapFault, SAXException {
        super((HttpServletRequest) inReq);
        this.stream = new RequestStream(inReq.getInputStream());
        this.reader = new BufferedReader(new InputStreamReader(this.stream));
    }

    @Override
    public ServletInputStream getInputStream() throws java.io.IOException {
        return this.stream;
    }

    @Override
    public java.io.BufferedReader getReader() throws java.io.IOException {
        return this.reader;
    }

    public void resetRequest(String request) throws IOException, SoapFault,
            SAXException {
        this.stream =
                new RequestStream(new ByteArrayInputStream(request.getBytes()));
        this.reader =
                new BufferedReader(new InputStreamReader(
                        new ByteArrayInputStream(request.getBytes())));
    }

}