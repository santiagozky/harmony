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


package org.opennaas.extensions.idb.test.webservice;

import java.net.URISyntaxException;

import org.opennaas.extensions.idb.serviceinterface.notification.SimpleNotificationClient;
import org.opennaas.core.utils.Config;
import org.opennaas.extensions.idb.webservice.NotificationWS;

/**
 * Abstract class for all webservice based tests.
 * 
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 * @version $Id: AbstractTest.java 619 2007-08-29 19:04:26Z
 *          willner@cs.uni-bonn.de $
 */
public abstract class AbstractNotificationTest {
    /**
     * notification client.
     */
    protected SimpleNotificationClient notificationClient;

    /**
     * Default constructor.
     * 
     * @throws URISyntaxException
     * @throws URISyntaxException
     *             If the given EPR is not a valid URI.
     */
    protected AbstractNotificationTest() {
        if (Config.isTrue("test", "test.callWebservice")) {

            this.notificationClient = new SimpleNotificationClient(Config
                    .getString("test", "epr.notification"));
        } else {
            this.notificationClient = new SimpleNotificationClient(
                    new NotificationWS());
        }
    }
}
