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
 * Project: IST Phosphorus Harmony System.
 * Module: 
 * Description: 
 *
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 * @version $Id: TestNotificationWS.java 4906 2009-08-20 15:46:29Z carlos.baez@i2cat.net $
 *
 */
package eu.ist_phosphorus.harmony.test.adapter.arrm.webservice;

import org.apache.muse.ws.addressing.soap.SoapFault;
import org.junit.Test;

import eu.ist_phosphorus.harmony.adapter.arrm.webservice.NotificationWS;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.AddTopicType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.exceptions.OperationNotSupportedFaultException;
import eu.ist_phosphorus.harmony.common.serviceinterface.notification.SimpleNotificationClient;
import eu.ist_phosphorus.harmony.common.utils.Config;

/**
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 * @version $Id: TestNotificationWS.java 4906 2009-08-20 15:46:29Z carlos.baez@i2cat.net $
 * 
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
            final String epr = Config.getString("test",
                    "test.notificationEPR");
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
    @Test(expected=OperationNotSupportedFaultException.class)
    public final void testUnsopportedOperation() throws SoapFault {
        AddTopicType addTopicType = new AddTopicType();
        addTopicType.setTopic("Test");
        this.client.addTopic(addTopicType);
    }
}
