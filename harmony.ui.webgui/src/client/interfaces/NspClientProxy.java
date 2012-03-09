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

import client.classes.ReservationList;
import client.classes.Status;
import client.classes.nsp.CancelReservationResponseType;
import client.classes.nsp.CancelReservationType;
import client.classes.nsp.CreateReservationResponseType;
import client.classes.nsp.CreateReservationType;
import client.classes.nsp.GetDomainsResponseType;
import client.classes.nsp.GetEndpointsResponseType;
import client.classes.nsp.GetEndpointsType;
import client.classes.nsp.GetLinksResponseType;
import client.classes.nsp.GetLinksType;
import client.classes.nsp.GetReservationsResponseType;
import client.classes.nsp.GetReservationsType;
import client.classes.nsp.GetStatusResponseType;
import client.classes.nsp.GetStatusType;
import client.helper.GuiException;

import com.google.gwt.user.client.rpc.RemoteService;

public interface NspClientProxy extends RemoteService {
    public ReservationList getReservations(String epr, int duration)
            throws GuiException;

    public GetDomainsResponseType getDomains() throws GuiException;

    public GetDomainsResponseType getDomains(String epr) throws GuiException;

    public GetLinksResponseType getLinks(String epr, GetLinksType request)
            throws GuiException;

    public GetEndpointsResponseType getEndpoints(String epr,
            GetEndpointsType request) throws GuiException;

    public GetReservationsResponseType getReservations(String epr,
            GetReservationsType request) throws GuiException;

    public GetStatusResponseType getStatus(String epr, GetStatusType request)
            throws GuiException;

    public CreateReservationResponseType createReservation(String epr,
            CreateReservationType request) throws GuiException;

    public CancelReservationResponseType cancelReservation(String epr,
            CancelReservationType request) throws GuiException;

    public Vector<client.classes.CancelResponse> cancelReservation(String epr,
            Vector<java.lang.String> ids) throws Exception;

    public Status getStatus(String epr, String id) throws GuiException;
}
