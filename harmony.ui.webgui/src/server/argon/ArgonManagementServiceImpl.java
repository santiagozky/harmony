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


package server.argon;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.xml.bind.JAXBException;

import server.common.GuiLogger;
import server.common.NspConverter;
import server.common.RemoteLoggableServiceServlet;
import client.classes.CancelResponse;
import client.classes.Endpoint;
import client.classes.EndpointList;
import client.classes.InternalLink;
import client.classes.InternalLinkList;
import client.classes.RemoveResponse;
import client.classes.Reservation;
import client.classes.ReservationList;
import client.classes.Router;
import client.classes.RouterList;
import client.helper.GuiException;
import client.interfaces.ArgonManagementService;
import de.unibonn.viola.argon.requestProcessor.exceptions.TopologyManagerException;
import de.unibonn.viola.argon.requestProcessor.requestHandler.operator.externalInterface.OperatorInterface;
import de.unibonn.viola.argon.utils.jaxb.external.classes.IRes.ReservationRequestType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelReservation;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelReservationResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CreateReservationResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CreateReservationType;
import org.opennaas.extensions.idb.serviceinterface.reservation.SimpleReservationClient;
import org.opennaas.core.utils.Config;

/**
 * Implements the Service-interface and provides data via RMI-connection to
 * argon.
 * 
 * @author claus
 */
public class ArgonManagementServiceImpl extends RemoteLoggableServiceServlet
        implements ArgonManagementService {
    private final NspConverter converter = new NspConverter();

    /**
	 * 
	 */
    private static final long serialVersionUID = -4215808141033179899L;
    /**
     * the log4j-Logger.
     */
    private static GuiLogger log;// = new Logger();

    // Logger.getLogger(ArgonManagementServiceImpl.class);
    /**
     * the domain to reach the RMI-Interface by argon.
     */
    private final String url =
            Config.getString("management", "argon.operatorInterfaceAddress");

    /**
     * the Client to cancel Reservations with.
     */
    private SimpleReservationClient reservationClient = null;

    /**
     * constructor.
     * 
     * @throws URISyntaxException
     */
    public ArgonManagementServiceImpl() throws URISyntaxException {
        this.reservationClient =
                new SimpleReservationClient(Config.getString("management",
                        "argon.epr.reservation"));
        ArgonManagementServiceImpl.log = new GuiLogger(this);
    }

    /**
     * @return the list with Argon-Routers
     * @throws RemoteException
     * @throws MalformedURLException
     * @throws NotBoundException
     */
    private ArrayList<de.unibonn.viola.argon.persist.hibernate.classes.Router> getRouterviaRMI()
            throws RemoteException, MalformedURLException, NotBoundException {
        final OperatorInterface operator =
                (OperatorInterface) Naming.lookup(this.url);
        return operator.getRouters();
    }

    /**
     * @return the list with EndpointLinks from argon
     * @throws RemoteException
     * @throws MalformedURLException
     * @throws NotBoundException
     */
    private ArrayList<de.unibonn.viola.argon.persist.hibernate.classes.Endlink> getEndpointLinksviaRMI()
            throws RemoteException, MalformedURLException, NotBoundException {
        final OperatorInterface operator =
                (OperatorInterface) Naming.lookup(this.url);
        return operator.getEndlinks();
    }

    private ArrayList<de.unibonn.viola.argon.persist.hibernate.classes.InternalLink> getLinksviaRMI()
            throws RemoteException, MalformedURLException, NotBoundException {
        final OperatorInterface operator =
                (OperatorInterface) Naming.lookup(this.url);
        return operator.getLinks();
    }

    /**
     * @return the list with currently stored reservations
     * @throws RemoteException
     * @throws JAXBException
     * @throws MalformedURLException
     * @throws NotBoundException
     */
    private ArrayList<de.unibonn.viola.argon.utils.jaxb.external.classes.IRes.ReservationRequestType> getReservationsviaRMI()
            throws RemoteException, JAXBException, MalformedURLException,
            NotBoundException {
        final OperatorInterface operator =
                (OperatorInterface) Naming.lookup(this.url);
        return operator.getReservations();

    }

    /**
     * This is taken from the NspClientProxyImpl
     */
    public client.classes.nsp.CreateReservationResponseType createReservation(
            final client.classes.nsp.CreateReservationType reservation)
            throws GuiException {
        ArgonManagementServiceImpl.log.debug("createReservation received");

        ArgonManagementServiceImpl.log.debug("before sending: " + reservation);

        client.classes.nsp.CreateReservationResponseType responseType;

        try {
            CreateReservationResponseType harmonyResponse =
                    this.reservationClient
                            .createReservation((CreateReservationType) this.converter
                                    .convert(reservation));

            responseType =
                    (client.classes.nsp.CreateReservationResponseType) this.converter
                            .convert(harmonyResponse);
        } catch (final Exception e) {
            throw new GuiException(e);
        }

        return responseType;
    }

    /**
     * uses argon RMI-interface to get all stored reservations
     * 
     * @throws NotBoundException
     * @throws JAXBException
     * @throws MalformedURLException
     * @throws RemoteException
     */
    public final ReservationList getReservations() throws GuiException {

        ArgonManagementServiceImpl.log.debug("getReservation called");
        final ReservationList result = new ReservationList();
        ArrayList<de.unibonn.viola.argon.utils.jaxb.external.classes.IRes.ReservationRequestType> res =
                null;

        try {
            res = this.getReservationsviaRMI();
        } catch (final MalformedURLException e) {
            e.printStackTrace();
            throw new GuiException(e);
        } catch (final JAXBException e) {
            e.printStackTrace();
            throw new GuiException(e);

        } catch (final NotBoundException e) {
            e.printStackTrace();
            throw new GuiException(e);

        } catch (final RemoteException e) {
            e.printStackTrace();
            throw new GuiException(e);

        }

        if (res != null) {
            for (final ReservationRequestType r : res) {
                final Reservation commonReservation =
                        ArgonGWTMapper.argon2gwt(r);
                result.getReservations().add(commonReservation);
            }
        }
        return result;
    }

    /**
     * uses ReservationClient to cancel a list of reservations
     * 
     * @param ids
     *            the reservations to be cancelled
     * @return the results of all cancel-actions
     */
    public final Vector<CancelResponse> cancelReservation(
            final Vector<String> ids) throws GuiException {
        final Vector<CancelResponse> result = new Vector<CancelResponse>();

        for (int i = 0; i < ids.size(); i++) {

            final String id = (String) ids.get(i);

            final CancelReservationType requestType =
                    new CancelReservationType();
            requestType.setReservationID(id);

            final CancelReservation request = new CancelReservation();
            request.setCancelReservation(requestType);

            final CancelResponse response = new CancelResponse();
            response.setId(id);

            try {
                final CancelReservationResponseType responseType =
                        this.reservationClient.cancelReservation(requestType);

                if (responseType != null) {
                    response.setStatus(responseType.isSuccess());
                } else {
                    response.setStatus(false);
                }

            } catch (final Exception e) {
                throw new GuiException(e);
            }

            result.add(response);
        }

        return result;

    }

    /**
     * @return the LinkList class, which contains all endpoint-links from argon
     */
    public final EndpointList getEndpoints() throws GuiException {
        ArgonManagementServiceImpl.log.debug("getLinks called");
        final EndpointList eps = new EndpointList();

        ArrayList<de.unibonn.viola.argon.persist.hibernate.classes.Endlink> res =
                null;
        try {
            res = this.getEndpointLinksviaRMI();
        } catch (final MalformedURLException e) {
            e.printStackTrace();
            throw new GuiException(e);

        } catch (final NotBoundException e) {
            e.printStackTrace();
            throw new GuiException(e);

        } catch (final RemoteException e) {

            e.printStackTrace();
            throw new GuiException(e);

        }

        if (res != null) {
            for (final de.unibonn.viola.argon.persist.hibernate.classes.Endlink l : res) {
                eps.getLinks().add(ArgonGWTMapper.argon2GWT(l));
            }
        }

        return eps;
    }

    /**
     * @return the RouterList-class which contains all routers from argon
     */
    public final RouterList getRouters() throws GuiException {
        ArgonManagementServiceImpl.log.debug("getRouter called");
        final RouterList router = new RouterList();

        ArrayList<de.unibonn.viola.argon.persist.hibernate.classes.Router> res =
                null;
        try {
            res = this.getRouterviaRMI();
        } catch (final MalformedURLException e) {
            e.printStackTrace();
            throw new GuiException(e);

        } catch (final NotBoundException e) {
            e.printStackTrace();
            throw new GuiException(e);

        } catch (final RemoteException e) {

            e.printStackTrace();
            throw new GuiException(e);

        }

        if (res != null) {
            for (final de.unibonn.viola.argon.persist.hibernate.classes.Router r : res) {
                router.getRouter().add(ArgonGWTMapper.argon2GWT(r));
            }
        }
        return router;
    }

    public final void addEndpoint(final Endpoint newEp) throws GuiException {

        OperatorInterface operator;
        try {
            operator = (OperatorInterface) Naming.lookup(this.url);

            final de.unibonn.viola.argon.persist.hibernate.classes.Endlink argonEp =
                    ArgonGWTMapper.gwt2Argon(newEp);
            operator.addEndlink(argonEp);

        } catch (final MalformedURLException e) {
            e.printStackTrace();
            throw new GuiException(e);

        } catch (final RemoteException e) {
            e.printStackTrace();
            throw new GuiException(e);

        } catch (final NotBoundException e) {
            e.printStackTrace();
            throw new GuiException(e);

        } catch (final TopologyManagerException e) {
            e.printStackTrace();
            throw new GuiException(e);

        }

    }

    public final void addRouter(final Router newRouter) throws GuiException {

        try {
            final OperatorInterface operator =
                    (OperatorInterface) Naming.lookup(this.url);
            final de.unibonn.viola.argon.persist.hibernate.classes.Router argonRouter =
                    ArgonGWTMapper.gwt2Argon(newRouter);
            operator.addRouter(argonRouter);

        } catch (final MalformedURLException e) {
            e.printStackTrace();
            throw new GuiException(e);
        } catch (final NotBoundException e) {
            e.printStackTrace();
            throw new GuiException(e);
        } catch (final TopologyManagerException e) {
            e.printStackTrace();
            throw new GuiException(e);
        } catch (final RemoteException e) {
            e.printStackTrace();
            throw new GuiException(e);
        }
    }

    public final String getDomainName() {

        return new String("viola-mpls");
    }

    public final List getOutGoingEndpoints() {

        return null;
    }

    public final Vector<RemoveResponse> removeEndpoint(final Vector<String> ids)
            throws GuiException {

        // return the remove-time to the Gui-User
        final Vector<RemoveResponse> result = new Vector<RemoveResponse>();
        long timestamp = -1;
        final Date removalDate = new Date();

        try {
            final OperatorInterface operator =
                    (OperatorInterface) Naming.lookup(this.url);
            String id;

            for (int i = 0; i < ids.size(); i++) {
                id = (String) ids.get(i);
                final RemoveResponse response = new RemoveResponse();
                response.setId(id);
                // response.setRemovalDate(removalDate);

                try {
                    timestamp = operator.removeEndlink(id, removalDate);
                    if (timestamp == -1) {
                        // first removalDate succeeded
                        response.setStatus(true);
                        response.setRemovalDate(removalDate);
                    } else {
                        // try again ONCE with returned timestamp
                        final Date newRemovalDate = new Date(timestamp);
                        timestamp = operator.removeEndlink(id, newRemovalDate);
                        if (timestamp == -1) {
                            response.setStatus(true);
                            response.setRemovalDate(newRemovalDate);
                        } else {
                            response.setStatus(false);
                        }
                    }

                } catch (final TopologyManagerException e) {
                    response.setStatus(false);
                }
                ArgonManagementServiceImpl.log.debug("removalTime(long): "
                        + timestamp);
                ArgonManagementServiceImpl.log.debug("removalTime(date): "
                        + new Date(timestamp));
                result.add(response);
            }

        } catch (final MalformedURLException e) {
            e.printStackTrace();
            throw new GuiException(e);

        } catch (final NotBoundException e) {
            e.printStackTrace();
            throw new GuiException(e);

        } catch (final RemoteException e) {
            e.printStackTrace();
            throw new GuiException(e);

        }

        return result;
    }

    public final Vector<RemoveResponse> removeRouter(final Vector<String> ids)
            throws GuiException {

        final Vector<RemoveResponse> result = new Vector<RemoveResponse>();
        final Date removalDate = new Date();
        long timestamp = -1;

        try {
            final OperatorInterface operator =
                    (OperatorInterface) Naming.lookup(this.url);
            String id;
            for (int i = 0; i < ids.size(); i++) {
                id = (String) ids.get(i);
                final RemoveResponse response = new RemoveResponse();
                response.setId(id);

                try {
                    timestamp = operator.removeRouter(id, removalDate);
                    if (timestamp == -1) {
                        response.setStatus(true);
                        response.setRemovalDate(removalDate);
                    } else {
                        final Date newRemovalDate = new Date(timestamp);
                        timestamp = operator.removeRouter(id, newRemovalDate);
                        if (timestamp == -1) {
                            response.setStatus(true);
                            response.setRemovalDate(newRemovalDate);
                        } else {
                            response.setStatus(false);
                        }
                    }
                } catch (final TopologyManagerException e) {
                    response.setStatus(false);
                }
                result.add(response);
            }

        } catch (final MalformedURLException e) {

            e.printStackTrace();
            throw new GuiException(e);

        } catch (final NotBoundException e) {
            e.printStackTrace();
            throw new GuiException(e);

        } catch (final RemoteException e) {
            e.printStackTrace();
            throw new GuiException(e);

        }

        return result;
    }

    // TODO
    public final boolean modifyEndpoint(final Endpoint modLink) {
        return false;
    }

    // TODO
    public final boolean modifyRouter(final Router modRouter) {
        return false;
    }

    public boolean disableEndpoint(final Vector<String> ids)
            throws GuiException {

        ArgonManagementServiceImpl.log.debug("disableEndpoint called");

        OperatorInterface operator;
        try {
            operator = (OperatorInterface) Naming.lookup(this.url);

            // first query for all links
            final EndpointList linkList = this.getEndpoints();
            String id = null;
            Endpoint link = null;

            // then match the given ids to get real Link-Object
            for (int i = 0; i < ids.size(); i++) {
                id = (String) ids.get(i);
                ArgonManagementServiceImpl.log.debug("disableLink-id: " + id);
                for (int j = 0; j < linkList.getLinks().size(); j++) {
                    link = (Endpoint) linkList.getLinks().get(j);
                    if (id.compareTo(link.getUniqueLabel()) == 0) {
                        ArgonManagementServiceImpl.log.debug("link found: "
                                + link.getUniqueLabel());
                        link.setUp(!link.isUp());
                        operator.modifyEndlink(ArgonGWTMapper.gwt2Argon(link));
                    }
                }
            }

        } catch (final MalformedURLException e) {
            e.printStackTrace();
            throw new GuiException(e);
        } catch (final RemoteException e) {
            e.printStackTrace();
            throw new GuiException(e);
        } catch (final NotBoundException e) {
            e.printStackTrace();
            throw new GuiException(e);
        } catch (final TopologyManagerException e) {
            e.printStackTrace();
            throw new GuiException(e);
        }
        // TODO: more detailed result type?
        return true;
    }

    public InternalLinkList getInternalLinks() throws GuiException {
        final InternalLinkList links = new InternalLinkList();

        ArrayList<de.unibonn.viola.argon.persist.hibernate.classes.InternalLink> res =
                null;
        try {
            res = this.getLinksviaRMI();
        } catch (final MalformedURLException e) {
            e.printStackTrace();
            throw new GuiException(e);

        } catch (final NotBoundException e) {
            e.printStackTrace();
            throw new GuiException(e);

        } catch (final RemoteException e) {

            e.printStackTrace();
            throw new GuiException(e);

        }

        if (res != null) {
            for (final de.unibonn.viola.argon.persist.hibernate.classes.InternalLink r : res) {
                links.getLinks().add(ArgonGWTMapper.argon2GWT(r));
            }
        }
        return links;

    }

    public void addInternalLink(final InternalLink newLink) throws GuiException {

        OperatorInterface operator;
        try {
            operator = (OperatorInterface) Naming.lookup(this.url);

            final de.unibonn.viola.argon.persist.hibernate.classes.InternalLink argonLink =
                    ArgonGWTMapper.gwt2Argon(newLink);
            operator.addLink(argonLink);

        } catch (final MalformedURLException e) {
            e.printStackTrace();
            throw new GuiException(e);

        } catch (final RemoteException e) {
            e.printStackTrace();
            throw new GuiException(e);

        } catch (final NotBoundException e) {
            e.printStackTrace();
            throw new GuiException(e);

        } catch (final TopologyManagerException e) {
            e.printStackTrace();
            throw new GuiException(e);

        }

    }

    public boolean disableInternalLink(final Vector<String> ids)
            throws GuiException {

        OperatorInterface operator;
        try {
            operator = (OperatorInterface) Naming.lookup(this.url);

            // first query for all links
            final InternalLinkList linkList = this.getInternalLinks();

            // TODO

            String id = null;
            InternalLink link = null;

            // then match the given ids to get real Link-Object
            for (int i = 0; i < ids.size(); i++) {
                id = (String) ids.get(i);
                ArgonManagementServiceImpl.log.debug("disableLink-id: " + id);
                for (int j = 0; j < linkList.getLinks().size(); j++) {
                    link = (InternalLink) linkList.getLinks().get(j);
                    if (id.compareTo(link.getUniqueLabel()) == 0) {
                        ArgonManagementServiceImpl.log.debug("link found: "
                                + link.getUniqueLabel());
                        link.setUp(!link.isUp());
                        operator.modifyLink(ArgonGWTMapper.gwt2Argon(link));

                    }
                }
            }

        } catch (final MalformedURLException e) {
            e.printStackTrace();
            throw new GuiException(e);
        } catch (final RemoteException e) {
            e.printStackTrace();
            throw new GuiException(e);
        } catch (final NotBoundException e) {
            e.printStackTrace();
            throw new GuiException(e);
        } catch (final TopologyManagerException e) {
            e.printStackTrace();
            throw new GuiException(e);
        }
        // TODO: more detailed result type?
        return true;
    }

    public boolean modifyInternalLink(final InternalLink modLink) {
        // TODO Auto-generated method stub
        return false;
    }

    public Vector<RemoveResponse> removeInternalLink(final Vector<String> ids)
            throws GuiException {
        // return the remove-time to the Gui-User
        final Vector<RemoveResponse> result = new Vector<RemoveResponse>();
        long timestamp = -1;
        final Date removalDate = new Date();

        try {
            final OperatorInterface operator =
                    (OperatorInterface) Naming.lookup(this.url);
            String id;

            for (int i = 0; i < ids.size(); i++) {
                id = (String) ids.get(i);
                final RemoveResponse response = new RemoveResponse();
                response.setId(id);
                // response.setRemovalDate(removalDate);

                try {
                    timestamp = operator.removeLink(id, removalDate);
                    if (timestamp == -1) {
                        // first removalDate succeeded
                        response.setStatus(true);
                        response.setRemovalDate(removalDate);
                    } else {
                        // try again ONCE with returned timestamp
                        final Date newRemovalDate = new Date(timestamp);
                        timestamp = operator.removeLink(id, newRemovalDate);
                        if (timestamp == -1) {
                            response.setStatus(true);
                            response.setRemovalDate(newRemovalDate);
                        } else {
                            response.setStatus(false);
                        }
                    }

                } catch (final TopologyManagerException e) {
                    response.setStatus(false);
                }
                result.add(response);
            }

        } catch (final MalformedURLException e) {
            e.printStackTrace();
            throw new GuiException(e);

        } catch (final NotBoundException e) {
            e.printStackTrace();
            throw new GuiException(e);

        } catch (final RemoteException e) {
            e.printStackTrace();
            throw new GuiException(e);

        }

        return result;
    }

}
