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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.InvalidRequestFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.validator.SyntaxValidator;
import org.opennaas.extensions.idb.serviceinterface.databinding.validator.TypeValidator;
import org.opennaas.core.utils.Config;
import org.opennaas.core.utils.PhLogger;

/**
 * Class to convert Element to Classes (and vice versa).
 * 
 * @author Jan Gassen (gassen@cs.uni-bonn.de)
 * @author Richard Figura (figura@cs.uni-bonn.de)
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 * @version $Id$
 */
public final class JaxbSerializer extends AJaxbSerializer {
    private static org.apache.log4j.Logger logger = PhLogger.getLogger();
    /** Singleton Instance. */
    private static JaxbSerializer selfInstance = null;

    /**
     * Singleton instance getter.
     * 
     * @return Singleton Instance
     */
    public static synchronized JaxbSerializer getInstance() {
        if (null == JaxbSerializer.selfInstance) {
            JaxbSerializer.selfInstance = new JaxbSerializer();
        }

        return JaxbSerializer.selfInstance;
    }

    /** JAXB serializer instance. */
    private final JAXBContext serializer;

    /** JAXB marshaller instance. */
    private final Marshaller marshaller;

    /** Jaxb Unmarshaller. */
    private final Unmarshaller unmarshaller;

    /** Validator instance. */
    private final SyntaxValidator syntaxValidator;

    /** Type Validator Instance. */
    private final TypeValidator typeValidator;

    /**
     * Constructor.
     */
    private JaxbSerializer() {

        this.serializer = this.createContext();

        this.marshaller = this.createMarshaller();

        this.unmarshaller = this.createUnmarshaller();

        this.syntaxValidator = this.createSyntaxValidator();

        this.typeValidator = new TypeValidator();
    }

    /**
     * Cloning _not_ supported! Should use the factory class instead
     * 
     * @return Nothing. Since cloning is _not_ supported!
     * @throws CloneNotSupportedException
     *             A CloneNotSupportedException
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    /**
     * Method to create the JaxbContext.
     * 
     * @return new JaxbContext
     */
    private final JAXBContext createContext() {
        try {
            return JAXBContext.newInstance(Config.getString("databinding",
                    "jaxb.path"));
        } catch (final JAXBException e) {
            throw new RuntimeException("Cannot create JAXB context", e);
        }
    }

    /**
     * Method to create the Marshaller.
     * 
     * @return new Marshaller
     */
    private final Marshaller createMarshaller() {
        try {
            return this.serializer.createMarshaller();
        } catch (final JAXBException e) {
            throw new RuntimeException("Cannot create Marshaller", e);
        }
    }

    /**
     * Method to create the Syntax Validator.
     * 
     * @return new SyntaxValidator
     */
    private synchronized final SyntaxValidator createSyntaxValidator() {
        try {
            return new SyntaxValidator(Config.getURL("databinding",
                    "wsdl.validator"), 0);
        } catch (final Exception e) {
            throw new RuntimeException("Cannot create Validator: "
                    + e.toString(), e);
        }
    }

    /**
     * Method to create the Unmarshaller.
     * 
     * @return new Unmarshaller
     */
    private final Unmarshaller createUnmarshaller() {
        try {
            return this.serializer.createUnmarshaller();
        } catch (final JAXBException e) {
            throw new RuntimeException("Cannot create Unmarshaller", e);
        }
    }

    /**
     * Convert Element to Object.
     * 
     * @param element
     *            Element to be converted to an Object
     * @return Object Object derived out of Element
     * @throws InvalidRequestFaultException
     * @throws UnexpectedFaultException
     */
    @Override
    public Object elementToObject(final Node element)
            throws InvalidRequestFaultException, UnexpectedFaultException {
        return this.elementToObject(element, true);
    }

    /**
     * Convert Element to Object.
     * 
     * @param element
     *            Element to be converted to an Object
     * @param useValidator
     *            Use validator true/false
     * @return Object Object derived out of Element
     * @throws InvalidRequestFaultException
     * @throws UnexpectedFaultException
     */
    @Override
    public synchronized Object elementToObject(final Node element,
            final boolean useValidator) throws UnexpectedFaultException,
            InvalidRequestFaultException {

        if (useValidator) {
            this.typeValidator.validate(element);
        }

        final String xml = AJaxbSerializer.elementToXml(element);

        if (useValidator) {
            this.syntaxValidator.validate(xml);
        }

        Object obj;
        try {
            obj = this.xmlToObject(xml);
        } catch (final JAXBException e) {
            JaxbSerializer.logger.debug(e.getMessage(), e);
            final UnexpectedFaultException e2 = new UnexpectedFaultException(
                    "Error during element conversion: " + e.getMessage());
            e2.setStackTrace(e.getStackTrace());
            throw e2;
        }
        return obj;
    }

    /**
     * Convert Object to Element.
     * 
     * @param obj
     *            Object to be converted to an Element
     * @return DOM Element Element derived out of Object
     * @throws InvalidRequestFaultException
     * @throws UnexpectedFaultException
     */
    @Override
    public Element objectToElement(final Object obj)
            throws InvalidRequestFaultException, UnexpectedFaultException {
        return this.objectToElement(obj, true);
    }

    /**
     * Convert Object to Element.
     * 
     * @param obj
     *            Object to be converted to an Element
     * @param useValidator
     *            Use validator true/false
     * @return DOM Element Element derived out of Object
     * @throws InvalidRequestFaultException
     * @throws UnexpectedFaultException
     */
    @Override
    public synchronized Element objectToElement(final Object obj,
            final boolean useValidator) throws InvalidRequestFaultException,
            UnexpectedFaultException {

        String xml;
        try {
            xml = this.objectToXml(obj);
        } catch (final JAXBException e) {
            JaxbSerializer.logger.debug(e.getMessage(), e);
            final UnexpectedFaultException e2 = new UnexpectedFaultException(
                    "Error during object conversion: " + e.getMessage());
            e2.setStackTrace(e.getStackTrace());
            throw e2;
        }

        if (useValidator) {
            this.syntaxValidator.validate(xml);
        }

        Element element;
        try {
            element = AJaxbSerializer.xmlToElement(xml);
        } catch (final IOException e) {
            JaxbSerializer.logger.debug(e.getMessage(), e);
            final UnexpectedFaultException e2 = new UnexpectedFaultException(
                    "Error during object conversion: " + e.getMessage());
            e2.setStackTrace(e.getStackTrace());
            throw e2;
        } catch (final SAXException e) {
            JaxbSerializer.logger.debug(e.getMessage(), e);
            final UnexpectedFaultException e2 = new UnexpectedFaultException(
                    "Error during object conversion: " + e.getMessage());
            e2.setStackTrace(e.getStackTrace());
            throw e2;
        }

        if (useValidator) {
            this.typeValidator.validate(element);
        }

        return (element);
    }

    /**
     * Convert Object to XML.
     * 
     * @param obj
     *            Object to be converted to a XML-String
     * @return XML String XML-String derived out of Object
     * @throws JAXBException
     *             A JAXBException
     */
    @Override
    @SuppressWarnings("unchecked")
    public String objectToXml(final Object obj) throws JAXBException {

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final StreamResult result = new StreamResult(outputStream);

        try {
            if (obj.getClass().isAnnotationPresent(XmlRootElement.class)) {
                this.marshaller.marshal(obj, result);
            } else {
                final Class<?> objClass = obj.getClass();

                this.marshaller.marshal(new JAXBElement(new QName(objClass
                        .getName()), objClass, obj), result);
            }
        } catch (final NullPointerException npe) {
            throw new JAXBException(npe.getMessage()
                    + "\n\nMay be caused by an invalid XMLGregorianCalendar.\n"
                    + "Please make shure to use the generateXMLCalendar"
                    + " method from Helpers to create new Calendars to"
                    + " avoid illegal calendar states", npe);
        }

        return (outputStream.toString());
    }

    /**
     * Convert XML to Object.
     * 
     * @param xml
     *            XML-String to be converted to an Object
     * @return Object Object derived out of XML-String
     * @throws JAXBException
     *             A JAXBException
     */
    @Override
    public Object xmlToObject(final String xml) throws JAXBException {
        final StringReader is = new StringReader(xml);

        return this.unmarshaller.unmarshal(is);
    }
}
