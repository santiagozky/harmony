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

package org.opennaas.extensions.idb.serviceinterface.databinding.validator;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.log4j.Logger;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.InvalidRequestFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;
import org.opennaas.core.utils.Config;
import org.opennaas.core.utils.PhLogger;

/**
 * Class to validate XML strings against a XSD scheme. <br>
 * It is initialised with a URl to the XSD-Scheme location and Optional with a
 * fail level. If no fail level is set, the fail level from the properties file
 * is used. <br>
 * It has two different ways to validate given XML-Strings. You can either get
 * true or false as result using {@link #isValid} or catch the Exception
 * {@link InvalidRequestFaultException} using {@link #validate}. <br>
 * The validation itselfs is done by Java build in XMl validator
 * {@link javax.xml.validation.Validator}. The language used by the validator is
 * specified by http://www.w3.org/2001/XMLSchema. To handle the different fail
 * level, this class uses an internal ErrorHandler.
 * 
 * @see #isValid(String)
 * @see #validate(String)
 * @see SyntaxValidator.ValidatorErrorHandler
 * 
 * @author gassen
 */
public class SyntaxValidator {
    /**
     * Error handler for Xerces Validator.
     * 
     * @author gassen
     */
    protected static class ValidatorErrorHandler implements ErrorHandler {
        /** * */
        private final int failLevel;

        /**
         * Constructor.
         * 
         * @param failLevelParam
         *            Level of failure
         */
        public ValidatorErrorHandler(final int failLevelParam) {
            this.failLevel = failLevelParam;
        }

        /**
         * Error Handler.
         * 
         * @param ex
         *            SAXParseException
         * @throws SAXException
         *             A SAXExeption
         */
        public final void error(final SAXParseException ex) throws SAXException {
            if (SyntaxValidator.ERROR >= this.failLevel) {
                throw ex;
            }
        }

        /**
         * Fatal Handler.
         * 
         * @param ex
         *            SAXParseException
         * @throws SAXException
         *             A SAXException
         */
        public final void fatalError(final SAXParseException ex)
                throws SAXException {
            if (SyntaxValidator.FATAL >= this.failLevel) {
                throw ex;
            }
        }

        /**
         * Warning Handler.
         * 
         * @param ex
         *            SAXParseException
         * @throws SAXException
         *             A SAXException
         */
        public final void warning(final SAXParseException ex)
                throws SAXException {
            if (SyntaxValidator.WARN >= this.failLevel) {
                throw ex;
            }
        }

    }

    /** * */
    public static final int WARN = 0;
    /** * */
    public static final int ERROR = 1;
    /** * */
    public static final int FATAL = 2;

    /** * */
    public static final int NONE = 3;

    /** Validator instance. */
    private Validator validator;

    private final Logger logger = PhLogger.getLogger();

    /** Generic Scheme Factory. */
    private static final SchemaFactory FACTORY = SchemaFactory
            .newInstance("http://www.w3.org/2001/XMLSchema");

    /**
     * Constructor.
     * 
     * @param schemeLocation
     *            schemeLocation-URL
     * @throws SAXException
     *             A SAXException
     */
    public SyntaxValidator(final URL schemeLocation) throws SAXException {

        this.init(schemeLocation, Integer.valueOf(
                Config.getString("databinding", "validator.fail")).intValue());
    }

    /**
     * Constructor.
     * 
     * @param schemeLocation
     *            schemeLocation-URL
     * @param failLevel
     *            level of Failure
     * @throws SAXException
     *             A SAXException
     */
    public SyntaxValidator(final URL schemeLocation, final int failLevel)
            throws SAXException {

        this.init(schemeLocation, failLevel);
    }

    /**
     * Constructor.
     * 
     * @param schemeLocation
     *            schemeLocation-URL
     * @param failLevel
     *            level of Failure
     * @throws SAXException
     *             A SAXException
     */
    private void init(final URL schemeLocation, final int failLevel)
            throws SAXException {

        final Schema schema = SyntaxValidator.FACTORY.newSchema(schemeLocation);
        this.validator = schema.newValidator();

        final ValidatorErrorHandler errorHandler = new ValidatorErrorHandler(
                failLevel);

        this.validator.setErrorHandler(errorHandler);
    }

    /**
     * Validates the xml string against a given scheme.
     * 
     * @param xml
     *            XML-String
     * @return True if xml is valid
     * @throws IOException
     *             A IOException
     */
    public final boolean isValid(final String xml)
            throws UnexpectedFaultException {

        try {
            this.validate(xml);
            return true;
        } catch (final InvalidRequestFaultException ex) {
            return false;
        }
    }

    /**
     * Validates the xml string against a given scheme.
     * 
     * @param xml
     *            XML-Stringl
     * @throws InvalidRequestFaultException
     *             Error while parsing
     * @throws UnexpectedFaultException
     * @throws IOException
     *             A IOException
     */
    public final void validate(final String xml)
            throws InvalidRequestFaultException, UnexpectedFaultException {

        final StringReader is = new StringReader(xml);
        final Source source = new StreamSource(is);

        try {
            this.validator.validate(source);
        } catch (final SAXException e) {
            this.logger.debug(e.getMessage() + ":\n" + xml);

            throw new InvalidRequestFaultException(e);
        } catch (final IOException e) {
            throw new UnexpectedFaultException("Error during validation: "
                    + e.getMessage());
        }
    }
}
