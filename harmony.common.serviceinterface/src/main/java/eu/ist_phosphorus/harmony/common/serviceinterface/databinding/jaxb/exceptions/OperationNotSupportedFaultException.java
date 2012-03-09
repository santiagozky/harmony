package eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.exceptions;

import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.OperationNotSupportedFault;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.BaseFaultType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.utils.AbstractFaultException;

/**
 * Autogenerated Fault Exception.
 *
 * Generated by scripts/generateFaultExceptions.java
 * Created on Mon Mar  5 16:22:27 2012
 */
public class OperationNotSupportedFaultException
        extends AbstractFaultException {

    /**
     * Autogenerated Constructor.
     *
     * @param message Message to be thrown
     * @param cause   Originator exception
     */
    public OperationNotSupportedFaultException(final String message,
            final Throwable cause) {
        super(new OperationNotSupportedFault(), message, cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * Autogenerated Constructor.
     *
     * @param message Message to be thrown
     */
    public OperationNotSupportedFaultException(final String message) {
        super(new OperationNotSupportedFault(), message);
        // TODO Auto-generated constructor stub
    }

    /**
     * Autogenerated Constructor.
     *
     * @param cause   Originator exception
     */
    public OperationNotSupportedFaultException(final Throwable cause) {
        super(new OperationNotSupportedFault(), cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * Autogenerated Constructor.
     */
    public OperationNotSupportedFaultException() {
        super(new OperationNotSupportedFault());
        // TODO Auto-generated constructor stub
    }

    /**
     * Autogenerated Extension Constructor.
     *
     * @param fault BaseFault
     * @param message Message to be thrown
     * @param cause   Originator exception
     */
    public OperationNotSupportedFaultException(final BaseFaultType fault, final String message,
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
    public OperationNotSupportedFaultException(final BaseFaultType fault, final String message) {
        super(fault, message);
        // TODO Auto-generated constructor stub
    }

    /**
     * Autogenerated Extension Constructor.
     *
     * @param fault BaseFault
     * @param cause   Originator exception
     */
    public OperationNotSupportedFaultException(final BaseFaultType fault, final Throwable cause) {
        super(fault, cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * Autogenerated Extension Constructor.
     *
     * @param fault BaseFault
     */
    public OperationNotSupportedFaultException(final BaseFaultType fault) {
        super(fault);
        // TODO Auto-generated constructor stub
    }

}
