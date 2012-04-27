package org.opennaas.extensions.gmpls.utils.configuration_modules.gmre.exceptions;

/**
 * @author Stephan Wagner (stephan.wagner@iais.fraunhofer.de)
 */
public class PathCreationException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    public PathCreationException() {
        super();
    }

    /**
     * @param message
     */
    public PathCreationException(final String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public PathCreationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause
     */
    public PathCreationException(final Throwable cause) {
        super(cause);
    }

}
