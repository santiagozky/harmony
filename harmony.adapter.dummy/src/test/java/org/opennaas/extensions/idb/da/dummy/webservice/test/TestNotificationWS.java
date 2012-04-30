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


/**
 * Project: IST Phosphorus Harmony System. Module: Description:
 * 
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 * @version $Id$
 */
package org.opennaas.extensions.idb.da.dummy.webservice.test;

import org.apache.muse.ws.addressing.soap.SoapFault;
import org.junit.Test;

import org.opennaas.extensions.idb.da.dummy.webservice.NotificationWS;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AddTopicType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.OperationNotSupportedFaultException;
import org.opennaas.extensions.idb.serviceinterface.notification.SimpleNotificationClient;
import org.opennaas.core.utils.Config;

/**
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 * @version $Id$
 */
public class TestNotificationWS {

    /**
     * A simple reservation client.
     */
    private final SimpleNotificationClient client;

    /**
     * The public constructor.
     */
    public TestNotificationWS() {
        if (Config.isTrue("test", "test.callWebservice")) {
            final String epr =
                    Config.getString("test", "test.notificationEPR");
            this.client = new SimpleNotificationClient(epr);
        } else {
            this.client = new SimpleNotificationClient(new NotificationWS());
        }
    }

    /**
     * This method is not supported yet. So it should fail.
     * 
     * @throws SoapFault
     */
    @Test(expected = OperationNotSupportedFaultException.class)
    public final void testUnsopportedOperation() throws SoapFault {
        final AddTopicType addTopicType = new AddTopicType();
        addTopicType.setTopic("Test");
        this.client.addTopic(addTopicType);
    }
}
