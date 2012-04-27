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

package org.opennaas.extensions.idb.serviceinterface.databinding.utils;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.apache.muse.util.xml.XmlUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.InvalidRequestFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;

/**
 * Abstract class for all JaxbSerializers.
 * 
 * @author Stephan Wagner(wagners@cs.uni-bonn.de)
 * 
 */
public abstract class AJaxbSerializer {

    private static AJaxbSerializer selfInstance = null;

    /**
     * Very important bugfix. See
     * http://ws.apache.org/muse/docs/2.2.0/manual/troubleshooting/
     * default-namespaces-xerces.html
     * 
     * @param xml
     *            xml string
     * @return xml string with corrected namespace
     */
    private static String adjustNamespace(final String xml) {
        return (xml.replace("xmlns:=", "xmlns="));
    }

    /**
     * Convert an Element to XML string.
     * 
     * @param element
     *            Dom Element
     * @return XML String
     */
    public static String elementToXml(final Node element) {
        String xml = XmlUtils.toString(element);

        xml = AJaxbSerializer.adjustNamespace(xml);

        return xml;
    }

    public static synchronized AJaxbSerializer getInstance() {
        return AJaxbSerializer.selfInstance;
    }

    /**
     * Convert XML String to Element.
     * 
     * @param xml
     *            XML-String to be converted to an Element
     * @return Dom Element Element derived out of XML-String
     * @throws SAXException
     *             A SEXException
     * @throws IOException
     *             A IOException
     * @author Alexander Willner (willner@cs.uni-bonn.de)
     */
    public static Element xmlToElement(final String xml) throws SAXException,
            IOException {
        String result = xml;

        result = AJaxbSerializer.adjustNamespace(result);
        final Element el = XmlUtils.createDocument(result).getDocumentElement();
        return el;
    }

    public abstract Object elementToObject(final Node element)
            throws InvalidRequestFaultException, UnexpectedFaultException;

    public abstract Object elementToObject(final Node element,
            final boolean useValidator) throws UnexpectedFaultException,
            InvalidRequestFaultException;

    public abstract Element objectToElement(final Object obj)
            throws InvalidRequestFaultException, UnexpectedFaultException;

    public abstract Element objectToElement(final Object obj,
            final boolean useValidator) throws InvalidRequestFaultException,
            UnexpectedFaultException;

    public abstract String objectToXml(final Object obj) throws JAXBException;

    public abstract Object xmlToObject(final String xml) throws JAXBException;

}
