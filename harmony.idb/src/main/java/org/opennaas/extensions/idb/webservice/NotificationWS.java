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

package org.opennaas.extensions.idb.webservice;

import org.apache.muse.core.AbstractCapability;
import org.w3c.dom.Element;

import org.opennaas.extensions.idb.serviceinterface.notification.INotificationWS;
import org.opennaas.extensions.idb.notification.handler.NotificationRequestHandler;

/**
 * nrpsDummyTopology server.
 * 
 * MyCapability.java Thu Jun 26 10:38:49 CEST 2008 Generated by the Apache Muse
 * Code Generation Tool
 */
public class NotificationWS extends AbstractCapability implements
		INotificationWS {
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.opennaas.extensions.idb.serviceinterface.notification.INotificationWS
	 * #addTopic(org.w3c.dom.Element)
	 */
	public Element addTopic(Element request) throws Exception {
		return NotificationRequestHandler.getInstance().handle(request,
				"addTopic");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.opennaas.extensions.idb.serviceinterface.notification.INotificationWS
	 * #getTopics(org.w3c.dom.Element)
	 */
	public Element getTopics(Element request) throws Exception {
		return NotificationRequestHandler.getInstance().handle(request,
				"getTopics");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.opennaas.extensions.idb.serviceinterface.notification.INotificationWS
	 * #publish(org.w3c.dom.Element)
	 */
	public Element publish(Element request) throws Exception {
		return NotificationRequestHandler.getInstance().handle(request,
				"publish");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.opennaas.extensions.idb.serviceinterface.notification.INotificationWS
	 * #removeTopic(org.w3c.dom.Element)
	 */
	public Element removeTopic(Element request) throws Exception {
		return NotificationRequestHandler.getInstance().handle(request,
				"removeTopic");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.opennaas.extensions.idb.serviceinterface.notification.INotificationWS
	 * #subscribe(org.w3c.dom.Element)
	 */
	public Element subscribe(Element request) throws Exception {
		return NotificationRequestHandler.getInstance().handle(request,
				"subscribe");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.opennaas.extensions.idb.serviceinterface.notification.INotificationWS
	 * #unsubscribe(org.w3c.dom.Element)
	 */
	public Element unsubscribe(Element request) throws Exception {
		return NotificationRequestHandler.getInstance().handle(request,
				"unsubscribe");
	}
}