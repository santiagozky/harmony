/**
 *
 */
package org.opennaas.extensions.gmpls.utils.configuration_modules.snmp.exceptions;

/**
 * @author Daniel Beer (daniel.beer@iais.fraunhofer.de)
 */
public class SnmpException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    public SnmpException() {
        super();
    }

    /**
     * @param message
     * @param cause
     */
    public SnmpException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     */
    public SnmpException(final String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public SnmpException(final Throwable cause) {
        super(cause);
    }

}
