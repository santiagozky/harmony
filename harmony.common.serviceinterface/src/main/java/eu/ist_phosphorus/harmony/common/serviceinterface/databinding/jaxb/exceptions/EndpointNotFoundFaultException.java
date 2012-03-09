package eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.exceptions;

import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EndpointNotFoundFault;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.BaseFaultType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.utils.AbstractFaultException;

/**
 * Autogenerated Fault Exception.
 *
 * Generated by scripts/generateFaultExceptions.java
 * Created on Mon Mar  5 16:22:27 2012
 */
public class EndpointNotFoundFaultException
        extends ReservationFaultException {

    /**
     * Autogenerated Constructor.
     *
     * @param message Message to be thrown
     * @param cause   Originator exception
     */
    public EndpointNotFoundFaultException(final String message,
            final Throwable cause) {
        super(new EndpointNotFoundFault(), message, cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * Autogenerated Constructor.
     *
     * @param message Message to be thrown
     */
    public EndpointNotFoundFaultException(final String message) {
        super(new EndpointNotFoundFault(), message);
        // TODO Auto-generated constructor stub
    }

    /**
     * Autogenerated Constructor.
     *
     * @param cause   Originator exception
     */
    public EndpointNotFoundFaultException(final Throwable cause) {
        super(new EndpointNotFoundFault(), cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * Autogenerated Constructor.
     */
    public EndpointNotFoundFaultException() {
        super(new EndpointNotFoundFault());
        // TODO Auto-generated constructor stub
    }

    /**
     * Autogenerated Extension Constructor.
     *
     * @param fault BaseFault
     * @param message Message to be thrown
     * @param cause   Originator exception
     */
    public EndpointNotFoundFaultException(final BaseFaultType fault, final String message,
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
    public EndpointNotFoundFaultException(final BaseFaultType fault, final String message) {
        super(fault, message);
        // TODO Auto-generated constructor stub
    }

    /**
     * Autogenerated Extension Constructor.
     *
     * @param fault BaseFault
     * @param cause   Originator exception
     */
    public EndpointNotFoundFaultException(final BaseFaultType fault, final Throwable cause) {
        super(fault, cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * Autogenerated Extension Constructor.
     *
     * @param fault BaseFault
     */
    public EndpointNotFoundFaultException(final BaseFaultType fault) {
        super(fault);
        // TODO Auto-generated constructor stub
    }

}