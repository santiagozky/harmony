package eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.exceptions;

import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.TopicNotFoundFault;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.BaseFaultType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.utils.AbstractFaultException;

/**
 * Autogenerated Fault Exception.
 *
 * Generated by scripts/generateFaultExceptions.java
 * Created on Mon Mar  5 16:22:27 2012
 */
public class TopicNotFoundFaultException
        extends NotificationFaultException {

    /**
     * Autogenerated Constructor.
     *
     * @param message Message to be thrown
     * @param cause   Originator exception
     */
    public TopicNotFoundFaultException(final String message,
            final Throwable cause) {
        super(new TopicNotFoundFault(), message, cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * Autogenerated Constructor.
     *
     * @param message Message to be thrown
     */
    public TopicNotFoundFaultException(final String message) {
        super(new TopicNotFoundFault(), message);
        // TODO Auto-generated constructor stub
    }

    /**
     * Autogenerated Constructor.
     *
     * @param cause   Originator exception
     */
    public TopicNotFoundFaultException(final Throwable cause) {
        super(new TopicNotFoundFault(), cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * Autogenerated Constructor.
     */
    public TopicNotFoundFaultException() {
        super(new TopicNotFoundFault());
        // TODO Auto-generated constructor stub
    }

    /**
     * Autogenerated Extension Constructor.
     *
     * @param fault BaseFault
     * @param message Message to be thrown
     * @param cause   Originator exception
     */
    public TopicNotFoundFaultException(final BaseFaultType fault, final String message,
            final Throwable cause) {
        super(fault, message, cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * Autogenerated Extension Constructor.
     *
     * @param fault BaseFault
     * @param message Message to be thrown
     */
    public TopicNotFoundFaultException(final BaseFaultType fault, final String message) {
        super(fault, message);
        // TODO Auto-generated constructor stub
    }

    /**
     * Autogenerated Extension Constructor.
     *
     * @param fault BaseFault
     * @param cause   Originator exception
     */
    public TopicNotFoundFaultException(final BaseFaultType fault, final Throwable cause) {
        super(fault, cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * Autogenerated Extension Constructor.
     *
     * @param fault BaseFault
     */
    public TopicNotFoundFaultException(final BaseFaultType fault) {
        super(fault);
        // TODO Auto-generated constructor stub
    }

}