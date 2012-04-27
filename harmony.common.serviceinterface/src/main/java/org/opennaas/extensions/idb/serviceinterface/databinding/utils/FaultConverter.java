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

import java.util.List;
import java.util.Vector;

import org.apache.muse.util.xml.XmlUtils;
import org.apache.muse.ws.addressing.EndpointReference;
import org.apache.muse.ws.addressing.soap.SoapFault;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.BaseFaultType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.BaseFaultType.Description;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;
import org.opennaas.core.utils.Config;

/**
 * @author jangassen
 */
public final class FaultConverter {

    /**
     * Class to represent the recieved data of Fault object.
     * 
     * @author jangassen
     */
    protected final class FaultData {

        /** Fault Class. */
        private Class<? extends Throwable> faultClass;
        /** Fault Stack Trace. */
        private StackTraceElement[] stackTrace;
        /** Fault Cause object. */
        private FaultData faultCause;
        /** Fault EPR */
        private EndpointReference epr = null;

        /** StackTrace xml element. */
        private Element stackTraceXml = null;
        /** Fault Class Name. */
        private String faultClassName = null;
        /** Fault Class Message. */
        private String faultMessage = null;

        /** Exception class path. */
        private final String exceptionPath = Config.getString("databinding",
                "exception.path");

        /** Recieved data. */
        private BaseFaultType data;

        /**
         * Constructor. Parse and convert all recieved data.
         * 
         * @param fault
         */
        FaultData(final BaseFaultType fault) {
            this.init(fault, null);
        }

        /**
         * Constructor. Parse and convert all received data.
         * 
         * @param fault
         */
        FaultData(final BaseFaultType fault, final EndpointReference causeEpr) {
            this.init(fault, causeEpr);
        }

        /**
         * Generate StackTrace objects from xml elements.
         */
        private final void generateStackTrace() {
            Element[] traceElements = null;

            try {
                traceElements = XmlUtils.getAllElements(this.stackTraceXml);
            } catch (final NullPointerException e) {
                return;
            }

            final Vector<StackTraceElement> elements = new Vector<StackTraceElement>();

            for (final Element traceElement : traceElements) {
                final String className = traceElement.getAttribute("className");
                final String methodName = traceElement
                        .getAttribute("methodName");
                String fileName = traceElement.getAttribute("fileName");
                final int lineNumber = Integer.valueOf(
                        traceElement.getAttribute("lineNumber")).intValue();

                if (fileName.equals("null")) {
                    fileName = null;
                }

                final StackTraceElement stackTraceElement = new StackTraceElement(
                        className, methodName, fileName, lineNumber);

                elements.add(stackTraceElement);
            }

            this.stackTrace = new StackTraceElement[elements.size()];

            for (int x = 0; x < elements.size(); x++) {
                this.stackTrace[x] = elements.get(x);
            }
        }

        /**
         * Wrapper for Constructors.
         * 
         * @param fault
         * @param causeEpr
         */
        private void init(final BaseFaultType fault,
                final EndpointReference causeEpr) {
            this.data = fault;
            this.epr = causeEpr;

            this.parseDescription(fault.getDescription());
            this.setClass();

            this.generateStackTrace();

            this.setCause();
        }

        /**
         * Parse the recieved basefault description.
         * 
         * @param descriptions
         *            Recieved Description.
         */
        private final void parseDescription(
                final List<BaseFaultType.Description> descriptions) {
            if (null == descriptions) {
                return;
            }

            for (final Description description : descriptions) {

                if ((null != description.getLang())
                        && description.getLang().equals("stack")) {

                    Document document;
                    try {
                        document = XmlUtils.createDocument(description
                                .getValue());
                        this.stackTraceXml = document.getDocumentElement();
                    } catch (final Exception e) {
                        // Do nothing
                    }

                }

                if ((null != description.getLang())
                        && description.getLang().equals("cause")) {
                    this.faultClassName = description.getValue();
                }

                if ((null != description.getLang())
                        && description.getLang().equals("message")) {
                    this.faultMessage = description.getValue();
                }

            }
        }

        /**
         * Set the fault cause.
         */
        private final void setCause() {
            if ((null != this.data.getFaultCause())
                    && (null != this.data.getFaultCause().getAny())) {
                final BaseFaultType fault = (BaseFaultType) this.data
                        .getFaultCause().getAny();

                this.faultCause = new FaultData(fault);
            }
        }

        /**
         * Set the class object.
         */
        @SuppressWarnings("unchecked")
        private final void setClass() {
            if (null == this.faultClassName) {
                final String faultName = this.data.getClass().getName();
                final String className = this.exceptionPath + "."
                        + faultName.substring(faultName.lastIndexOf('.') + 1)
                        + "Exception";

                this.faultClassName = className;
            }

            try {
                this.faultClass = (Class<? extends Throwable>) Class
                        .forName(this.faultClassName);
            } catch (final ClassNotFoundException e) {
                this.faultClass = Throwable.class;
            }
        }

        /**
         * Convert the data to an Exception Object.
         * 
         * @return
         */
        public final Throwable toException() {
            Throwable exception = null;

            try {
                if (null != this.epr) {
                    this.faultMessage = FaultConverter.addEprToMessage(
                            this.epr, this.faultMessage);
                }

                exception = this.faultClass.getConstructor(String.class)
                        .newInstance(this.faultMessage);
            } catch (final Exception e) {
                exception = new UnexpectedFaultException(
                        "(Could not be backcasted to " + this.faultClassName
                                + "): " + this.faultMessage);
            }

            if (null != exception) {
                if (null != this.stackTrace) {
                    exception.setStackTrace(this.stackTrace);
                } else {
                    exception.setStackTrace(new StackTraceElement[0]);
                }

                if (null != this.faultCause) {
                    try {
                        exception.initCause(this.faultCause.toException());
                    } catch (final Exception e) {
                        System.err.println(exception.getClass().getName()
                                + " doesn't accept any cause");
                        System.err.println("Cause should have been:");

                        try {
                            this.faultCause.toException().printStackTrace();
                        } catch (final RuntimeException re) {
                            System.err.println(this.faultCause.getClass()
                                    .getName());
                        }
                    }
                }

            }

            return exception;
        }

    }

    /** Serializer. */
    private static final JaxbSerializer SERIALIZER = JaxbSerializer
            .getInstance();

    /** Singleton Instance. */
    private static FaultConverter selfInstance = null;

    /**
     * Add epr to Stacktrace Message.
     * 
     * @param epr
     * @param message
     * @return
     */
    public static String addEprToMessage(final EndpointReference epr,
            final String message) {

        final String result = ((null == message) || "".equals(message)) ? "(no message)"
                : message;

        return "Calling " + epr.getAddress() + ":\n" + result;
    }

    /**
     * Instance getter.
     * 
     * @return Singleton Instance
     */
    public static FaultConverter getInstance() {
        if (FaultConverter.selfInstance == null) {
            FaultConverter.selfInstance = new FaultConverter();
        }
        return FaultConverter.selfInstance;
    }

    /**
     * Constructor.
     */
    private FaultConverter() {
        // Nothing to do
    }

    /**
     * Singleton - Cloning _not_ supported!
     * 
     * @return Should never return anything...
     * @throws CloneNotSupportedException
     *             Singleton hates to be cloned!
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public SoapFault getOriginalFault(final SoapFault e) {
        return this.getOriginalFault(e, null);
    }

    /**
     * Convert caught exceptions back to original exception.
     * 
     * @param e
     *            Caught Exception
     * @return Original Exception
     */
    public SoapFault getOriginalFault(final SoapFault e,
            final EndpointReference epr) {
        SoapFault objResult = e;

        try {
            if (null != e.getDetail()) {
                final BaseFaultType fault = (BaseFaultType) FaultConverter.SERIALIZER
                        .elementToObject(e.getDetail(), false);

                objResult = (SoapFault) new FaultData(fault, epr).toException();
            } else if (null != epr) {
                objResult.setReason(FaultConverter.addEprToMessage(epr,
                        objResult.getReason()));
            }
        } catch (final Exception e1) {
            String message = "The incoming SoapFault \"" + e.getMessage()
                    + "\" can not be converted by FaultConverter.\n";

            if (null != epr) {
                message += "EPR: " + epr.getAddress() + "\n";
            }

            message += "Reason: " + e1.getMessage();
            e1.printStackTrace();
            System.out.println(message);
        }

        return objResult;
    }

    /**
     * Convert caught exceptions back to original exception.
     * 
     * @param e
     *            Caught Exception
     * @throws SoapFault
     *             Original Exception
     */
    public void throwOriginalFault(final SoapFault e) throws SoapFault {
        throw this.getOriginalFault(e);
    }
}
