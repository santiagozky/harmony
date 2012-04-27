package org.opennaas.extensions.gmpls.utils.configuration_modules.gmre.exceptions;

/**
 * @author Stephan Wagner (stephan.wagner@iais.fraunhofer.de)
 */
public class PathNotFoundException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    public PathNotFoundException() {
        super();
    }

    /**
     * @param message
     */
    public PathNotFoundException(final String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public PathNotFoundException(final Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public PathNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
