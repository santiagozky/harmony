package org.opennaas.extensions.gmpls.utils.configuration_modules.gmre.exceptions;

/**
 * @author Stephan Wagner (stephan.wagner@iais.fraunhofer.de)
 */
public class GmreConnectionException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    public GmreConnectionException() {
        super();
    }

    /**
     * @param message
     */
    public GmreConnectionException(final String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public GmreConnectionException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause
     */
    public GmreConnectionException(final Throwable cause) {
        super(cause);
    }

}
