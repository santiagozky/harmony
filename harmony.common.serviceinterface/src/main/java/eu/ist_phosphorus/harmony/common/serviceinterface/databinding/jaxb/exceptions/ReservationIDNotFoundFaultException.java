package eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.exceptions;

import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.ReservationIDNotFoundFault;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.BaseFaultType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.utils.AbstractFaultException;

/**
 * Autogenerated Fault Exception.
 *
 * Generated by scripts/generateFaultExceptions.java
 * Created on Mon Mar  5 16:22:27 2012
 */
public class ReservationIDNotFoundFaultException
        extends ReservationFaultException {

    /**
     * Autogenerated Constructor.
     *
     * @param message Message to be thrown
     * @param cause   Originator exception
     */
    public ReservationIDNotFoundFaultException(final String message,
            final Throwable cause) {
        super(new ReservationIDNotFoundFault(), message, cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * Autogenerated Constructor.
     *
     * @param message Message to be thrown
     */
    public ReservationIDNotFoundFaultException(final String message) {
        super(new ReservationIDNotFoundFault(), message);
        // TODO Auto-generated constructor stub
    }

    /**
     * Autogenerated Constructor.
     *
     * @param cause   Originator exception
     */
    public ReservationIDNotFoundFaultException(final Throwable cause) {
        super(new ReservationIDNotFoundFault(), cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * Autogenerated Constructor.
     */
    public ReservationIDNotFoundFaultException() {
        super(new ReservationIDNotFoundFault());
        // TODO Auto-generated constructor stub
    }

    /**
     * Autogenerated Extension Constructor.
     *
     * @param fault BaseFault
     * @param message Message to be thrown
     * @param cause   Originator exception
     */
    public ReservationIDNotFoundFaultException(final BaseFaultType fault, final String message,
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
    public ReservationIDNotFoundFaultException(final BaseFaultType fault, final String message) {
        super(fault, message);
        // TODO Auto-generated constructor stub
    }

    /**
     * Autogenerated Extension Constructor.
     *
     * @param fault BaseFault
     * @param cause   Originator exception
     */
    public ReservationIDNotFoundFaultException(final BaseFaultType fault, final Throwable cause) {
        super(fault, cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * Autogenerated Extension Constructor.
     *
     * @param fault BaseFault
     */
    public ReservationIDNotFoundFaultException(final BaseFaultType fault) {
        super(fault);
        // TODO Auto-generated constructor stub
    }

}