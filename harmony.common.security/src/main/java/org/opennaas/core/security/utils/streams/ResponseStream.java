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
package org.opennaas.core.security.utils.streams;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;

import org.apache.log4j.Logger;
import org.apache.muse.util.xml.XmlUtils;
import org.apache.muse.ws.addressing.soap.SoapFault;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import org.opennaas.core.security.filter.OutputFilter;
import org.opennaas.extension.idb.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;
import org.opennaas.core.utils.PhLogger;

public class ResponseStream extends ServletOutputStream {
    private final OutputStream intStream;

    private ByteArrayOutputStream newOutputStream = new ByteArrayOutputStream();

    private boolean closed = false;

    private final Logger log = PhLogger.getSeparateLogger("aai");

    private final OutputFilter filter;

    /**
     * Default Constructor.
     * 
     * @param outStream
     * @throws UnexpectedFaultException
     */
    public ResponseStream(final OutputStream outStream) throws SoapFault {
        this.intStream = outStream;

        this.filter = new OutputFilter();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.io.OutputStream#close()
     */
    @Override
    public void close() throws java.io.IOException {
        if (!this.closed) {
            this.intStream.close();
            this.closed = true;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.io.OutputStream#flush()
     */
    @Override
    public void flush() throws java.io.IOException {
        if (this.newOutputStream.size() != 0) {
            if (!this.closed) {
                this.processStream(); // need to synchronize the flush!
                this.newOutputStream = new ByteArrayOutputStream();
            }
        }
    }

    /**
     * Process response stream and add token if possible.
     * 
     * @throws IOException
     */
    public void processStream() throws IOException {
        /* setup -------------------------------------------------------- */
        String str = new String(this.newOutputStream.toByteArray());
        if (str.length() == 0) {
            this.log.info("Empty message: skipping");
            return;
        }

        Document response;
        try {
            response = XmlUtils.createDocument(str);
        } catch (SAXException e1) {
            throw new IOException(e1);
        }

        try {
            response = this.filter.apply(response);
        } catch (Exception e) {
            throw new IOException(e);
        }

        this.intStream.write(XmlUtils.toString(response, false, false)
                .getBytes());
        this.intStream.flush();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.io.OutputStream#write(int)
     */
    @Override
    public void write(final int i) throws java.io.IOException {
        this.newOutputStream.write(i);
    }
}