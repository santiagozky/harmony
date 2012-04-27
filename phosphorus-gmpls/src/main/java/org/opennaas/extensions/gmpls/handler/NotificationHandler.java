package org.opennaas.extensions.gmpls.handler;

import org.opennaas.extensions.idb.serviceinterface.notification.CommonNotificationHandler;

/**
 * Class to handle NSP topology-requests in a predictable manner.
 */
public final class NotificationHandler extends CommonNotificationHandler {
    /** Singleton Instance. */
    private static NotificationHandler selfInstance = null;

    /**
     * Instance getter.
     * 
     * @return Singleton Instance
     */
    public synchronized static NotificationHandler getInstance() {
	if (NotificationHandler.selfInstance == null) {
	    NotificationHandler.selfInstance = new NotificationHandler();
	}
	return NotificationHandler.selfInstance;
    }

    /**
     * Private constructor: Singleton.
     */
    private NotificationHandler() {
	super();
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

    /*
     * Handler
     * =========================================================================
     */

}
