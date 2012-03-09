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
package eu.ist_phosphorus.harmony.common.security.utils.streams;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletInputStream;

import org.apache.muse.util.xml.XmlUtils;
import org.apache.muse.ws.addressing.soap.SoapFault;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import eu.ist_phosphorus.harmony.common.security.filter.InputFilter;
import eu.ist_phosphorus.harmony.common.security.utils.ServletFilter;

public class RequestStream extends ServletInputStream {
    /** * */
    private InputStream newInputStream = null;

    private boolean hasChanged = false;

    private final InputFilter filter;

    public final void update(final Document doc) {
        ServletFilter.originalRequest = doc;

        this.hasChanged = true;
    }

    private final InputStream getInputStream() {
        if (this.hasChanged) {
            final String str =
                    XmlUtils.toString(ServletFilter.originalRequest, false,
                            false);

            this.newInputStream = new ByteArrayInputStream(str.getBytes());
        } else if (null == this.newInputStream) {
            this.newInputStream =
                    new ByteArrayInputStream("No data".getBytes());
        }

        this.hasChanged = false;

        return this.newInputStream;
    }

    /**
     * Default Constructor. Checks for security.
     * 
     * @param inStream
     * @throws IOException
     * @throws SAXException
     */
    public RequestStream(final InputStream inStream) throws IOException,
            SoapFault, SAXException {
        Document result = XmlUtils.createDocument(inStream);

        this.update(result);

        this.filter = new InputFilter();

        result = this.filter.apply(result);

        this.update(result);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.io.InputStream#close()
     */
    @Override
    public void close() throws IOException {
        if (null != this.newInputStream) {
            this.newInputStream.close();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.io.InputStream#read()
     */
    @Override
    public int read() throws IOException {
        return this.getInputStream().read();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.io.InputStream#read(byte[])
     */
    @Override
    public int read(final byte[] b) throws IOException {
        return this.getInputStream().read(b);
    }

}