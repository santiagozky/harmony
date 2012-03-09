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


package client.interfaces;

import java.util.Vector;

import client.classes.Endpoint;
import client.classes.InternalLink;
import client.classes.Router;
import client.classes.nsp.CreateReservationType;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * the interface needed by GWT to use RPC
 * 
 * @author steffen
 * 
 */
public interface ArgonManagementServiceAsync {

    void getReservations(AsyncCallback callback);

    void cancelReservation(Vector ids, AsyncCallback callback);

    void createReservation(CreateReservationType request, AsyncCallback callback);

    void getEndpoints(AsyncCallback callback);

    void getOutGoingEndpoints(AsyncCallback callback);

    void addEndpoint(Endpoint newLink, AsyncCallback callback);

    void removeEndpoint(Vector ids, AsyncCallback callback);

    void disableEndpoint(Vector ids, AsyncCallback callback);

    void modifyEndpoint(Endpoint modLink, AsyncCallback callback);

    void getDomainName(AsyncCallback callback);

    void getRouters(AsyncCallback callback);

    void addRouter(Router newRouter, AsyncCallback callback);

    void removeRouter(Vector ids, AsyncCallback callback);

    void modifyRouter(Router modRouter, AsyncCallback callback);

    void getInternalLinks(AsyncCallback callback);

    void addInternalLink(InternalLink newLink, AsyncCallback callback);

    void removeInternalLink(Vector ids, AsyncCallback callback);

    void disableInternalLink(Vector ids, AsyncCallback callback);

    void modifyInternalLink(InternalLink modLink, AsyncCallback callback);

}
