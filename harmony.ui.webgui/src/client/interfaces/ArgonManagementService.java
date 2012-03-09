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

import java.util.List;
import java.util.Vector;

import client.classes.Endpoint;
import client.classes.EndpointList;
import client.classes.InternalLink;
import client.classes.InternalLinkList;
import client.classes.ReservationList;
import client.classes.Router;
import client.classes.RouterList;
import client.classes.nsp.CreateReservationResponseType;
import client.classes.nsp.CreateReservationType;
import client.helper.GuiException;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * The interface for services provided by Argon has to be implemented by the
 * server.
 * 
 * TODO: insert Link handling
 * 
 * @author claus
 */
public interface ArgonManagementService extends RemoteService {

    public Vector<client.classes.CancelResponse> cancelReservation(
            Vector<java.lang.String> ids) throws GuiException;

    /**
     * @return the list of reservations
     * @throws Exception
     */
    public ReservationList getReservations() throws GuiException;

    /**
     * 
     * @param epr
     * @param request
     * @return
     * @throws GuiException
     */
    public CreateReservationResponseType createReservation(
            CreateReservationType request) throws GuiException;

    /**
     * @return the list of endpoint-links
     * @throws Exception
     */
    public EndpointList getEndpoints() throws GuiException;

    public List<client.classes.Endpoint> getOutGoingEndpoints();

    /**
     * @param newRouter
     *            the router to be added
     * @return whether the adding was successful
     */
    public void addRouter(Router newRouter) throws GuiException;

    public Vector<client.classes.RemoveResponse> removeRouter(
            Vector<java.lang.String> ids) throws GuiException;

    /**
     * 
     * @param modRouter
     *            the router to be modified
     * @return whether modification was successful
     */
    public boolean modifyRouter(Router modRouter);

    /**
     * @return the list of routers
     * @throws Exception
     */
    public RouterList getRouters() throws GuiException;

    /**
     * @param newLink
     *            the link to be added
     * @return whether the adding was successful
     */
    public void addEndpoint(Endpoint newLink) throws GuiException;

    public Vector<client.classes.RemoveResponse> removeEndpoint(
            Vector<java.lang.String> ids) throws GuiException;

    /**
     * 
     * @param modLink
     *            the link to be modified
     * @return whether modification was successful
     */
    public boolean modifyEndpoint(Endpoint modLink);

    /**
     * switch linkUp
     */
    public boolean disableEndpoint(Vector<java.lang.String> ids)
            throws GuiException;

    /**
     * 
     * @return the list of internal links
     * @throws GuiException
     */
    public InternalLinkList getInternalLinks() throws GuiException;

    /**
     * @param newLink
     *            the link to be added
     * @return whether the adding was successful
     */
    public void addInternalLink(InternalLink newLink) throws GuiException;

    public Vector<client.classes.RemoveResponse> removeInternalLink(
            Vector<java.lang.String> ids) throws GuiException;

    /**
     * 
     * @param modLink
     *            the link to be modified
     * @return whether modification was successful
     */
    public boolean modifyInternalLink(InternalLink modLink);

    /**
     * switch linkUp
     */
    public boolean disableInternalLink(Vector<java.lang.String> ids)
            throws GuiException;

    /**
     * @return the domain-name of argon
     */
    public String getDomainName();

}
