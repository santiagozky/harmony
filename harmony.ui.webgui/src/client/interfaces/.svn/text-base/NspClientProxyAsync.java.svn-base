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

import client.classes.nsp.CancelReservationType;
import client.classes.nsp.CreateReservationType;
import client.classes.nsp.GetEndpointsType;
import client.classes.nsp.GetLinksType;
import client.classes.nsp.GetReservationsType;
import client.classes.nsp.GetStatusType;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface NspClientProxyAsync {
    public void getReservations(String epr, int duration, AsyncCallback callback);

    public void getDomains(AsyncCallback callback);

    public void getDomains(String epr, AsyncCallback callback);

    public void getLinks(String epr, GetLinksType request,
            AsyncCallback callback);

    public void getEndpoints(String epr, GetEndpointsType request,
            AsyncCallback callback);

    public void getReservations(String epr, GetReservationsType request,
            AsyncCallback callback);

    public void getStatus(String epr, GetStatusType request,
            AsyncCallback callback);

    public void createReservation(String epr, CreateReservationType request,
            AsyncCallback callback);

    public void cancelReservation(String epr, CancelReservationType request,
            AsyncCallback callback);

    public void cancelReservation(String epr, Vector<java.lang.String> ids, AsyncCallback callback);

    public void getStatus(String epr, String id, AsyncCallback callback);
}
