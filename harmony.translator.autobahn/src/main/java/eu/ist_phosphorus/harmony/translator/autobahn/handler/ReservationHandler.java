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


package eu.ist_phosphorus.harmony.translator.autobahn.handler;

import java.util.LinkedList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import net.es.oscars.wsdlTypes.CancelReservation;
import net.es.oscars.wsdlTypes.CreateReply;
import net.es.oscars.wsdlTypes.GlobalReservationId;
import net.es.oscars.wsdlTypes.ListRequest;
import net.es.oscars.wsdlTypes.ResCreateContent;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.ActivateResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.ActivateType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.BindResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.BindType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CancelJobResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CancelJobType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CancelReservationResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CancelReservationType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CompleteJobResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CompleteJobType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.ConnectionConstraintType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CreateReservationResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CreateReservationType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EndpointType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetReservationsResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetReservationsType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetStatusResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetStatusType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.IsAvailableResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.IsAvailableType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.ServiceConstraintType;
import eu.ist_phosphorus.harmony.common.serviceinterface.reservation.CommonReservationHandler;
import eu.ist_phosphorus.harmony.translator.autobahn.implementation.AutoBAHNClient;
import eu.ist_phosphorus.harmony.translator.autobahn.implementation.CancelRequestTranslator;
import eu.ist_phosphorus.harmony.translator.autobahn.implementation.ManagementRequestTranslator;
import eu.ist_phosphorus.harmony.translator.autobahn.implementation.ReservationRequestTranslator;

/**
 * Class to handle reservation requests in a predictable manner.
 */
public final class ReservationHandler extends CommonReservationHandler {

    /** Singleton instance. */
    private static ReservationHandler selfInstance;

    /**
     * Instance getter.
     * 
     * @return Singleton Instance
     */
    public static ReservationHandler getInstance() {
        synchronized (ReservationHandler.class) {
            if (ReservationHandler.selfInstance == null) {
                ReservationHandler.selfInstance = new ReservationHandler();
            }
        }
        return ReservationHandler.selfInstance;
    }

    /** The AutoBAHN client */
    private final AutoBAHNClient autobahnClient = new AutoBAHNClient();
    private final CancelRequestTranslator translator =
            new CancelRequestTranslator();
    private final ReservationRequestTranslator resTranslator =
            new ReservationRequestTranslator();
    private final ManagementRequestTranslator mngTranslator =
            new ManagementRequestTranslator();

    /** Private constructor: Singleton. */
    private ReservationHandler() {
        super();
    }

    /*
     * (non-Javadoc)
     * @seeeu.ist_phosphorus.harmony.common.serviceinterface.reservation.
     * CommonReservationHandler
     * #activate(eu.ist_phosphorus.harmony.common.serviceinterface
     * .databinding.jaxb.ActivateType)
     */
    @Override
    public ActivateResponseType activate(final ActivateType request)
            throws Throwable {
        throw new OperationNotSupportedException("Not yet implemented");
    }

    /*
     * (non-Javadoc)
     * @seeeu.ist_phosphorus.harmony.common.serviceinterface.reservation.
     * CommonReservationHandler
     * #bind(eu.ist_phosphorus.harmony.common.serviceinterface
     * .databinding.jaxb.BindType)
     */
    @Override
    public BindResponseType bind(final BindType request) throws Throwable {
        throw new OperationNotSupportedException("Not yet implemented");
    }

    /*
     * (non-Javadoc)
     * @seeeu.ist_phosphorus.harmony.common.serviceinterface.reservation.
     * CommonReservationHandler
     * #cancelJob(eu.ist_phosphorus.harmony.common.serviceinterface
     * .databinding.jaxb.CancelJobType)
     */
    @Override
    public CancelJobResponseType cancelJob(final CancelJobType request)
            throws Throwable {
        throw new OperationNotSupportedException("Not yet implemented");
    }

    /**
     *
     */
    @Override
    public CancelReservationResponseType cancelReservation(
            final CancelReservationType harmonyRequest) throws Throwable {
        /* send the request to the AutoBAHN -------------------------------------- */
        final CancelReservation autobahnRequest =
                this.translator.convert(harmonyRequest);
        CancelReservationResponseType harmonyResponse = null;
        final GlobalReservationId gri = new GlobalReservationId();
        gri.setGri(autobahnRequest.getCancelReservation().getGri());

        harmonyResponse = new CancelReservationResponseType();
        harmonyResponse.setSuccess(true);
        this.autobahnClient.cancelReservation(gri);
        /* ------------------------------------------------------------------ */

        return harmonyResponse;
    }

    /*
     * (non-Javadoc)
     * @seeeu.ist_phosphorus.harmony.common.serviceinterface.reservation.
     * CommonReservationHandler
     * #completeJob(eu.ist_phosphorus.harmony.common.serviceinterface
     * .databinding.jaxb.CompleteJobType)
     */
    @Override
    public CompleteJobResponseType completeJob(final CompleteJobType request)
            throws Throwable {
        throw new OperationNotSupportedException("Not yet implemented");
    }

    /**
     *
     */
    @Override
    public CreateReservationResponseType createReservation(
            final CreateReservationType harmonyRequest) throws Throwable {

        /* send the request to the AutoBAHN -------------------------------------- */
        final ResCreateContent autobahnRequest =
                this.resTranslator.convert(harmonyRequest);

        final CreateReply reply = this.autobahnClient.createReservation(autobahnRequest);

        final CreateReservationResponseType harmonyResponse =
                this.resTranslator.convert(reply);
        /* ------------------------------------------------------------------ */

        /* ------------------------------------------------------------------ */
        final List<String> endpoints = new LinkedList<String>();

        for (final ServiceConstraintType services : harmonyRequest.getService()) {
            for (final ConnectionConstraintType connection : services
                    .getConnections()) {
                endpoints.add(connection.getSource().getEndpointId());
                for (final EndpointType target : connection.getTarget()) {
                    endpoints.add(target.getEndpointId());
                }
            }
        }
        /* ------------------------------------------------------------------ */

        return harmonyResponse;
    }

    /*
     * (non-Javadoc)
     * @seeeu.ist_phosphorus.harmony.common.serviceinterface.reservation.
     * CommonReservationHandler
     * #getReservations(eu.ist_phosphorus.harmony.common.
     * serviceinterface.databinding.jaxb.GetReservationsType)
     */
    @Override
    public GetReservationsResponseType getReservations(
            final GetReservationsType request) throws Throwable {

        final ListRequest autobahnRequest = this.mngTranslator.convert(request);
        final GetReservationsResponseType harmonyResponse =
                this.mngTranslator.convert(this.autobahnClient
                        .listReservations(autobahnRequest));

        return harmonyResponse;
    }

    /*
     * (non-Javadoc)
     * @seeeu.ist_phosphorus.harmony.common.serviceinterface.reservation.
     * CommonReservationHandler
     * #getStatus(eu.ist_phosphorus.harmony.common.serviceinterface
     * .databinding.jaxb.GetStatusType)
     */
    @Override
    public GetStatusResponseType getStatus(final GetStatusType request)
            throws Throwable {

        final GlobalReservationId autobahnRequest =
                this.mngTranslator.convert(request);
        final GetStatusResponseType harmonyResponse =
                this.mngTranslator.convert(this.autobahnClient
                        .queryReservation(autobahnRequest));

        return harmonyResponse;
    }

    /*
     * (non-Javadoc)
     * @seeeu.ist_phosphorus.harmony.common.serviceinterface.reservation.
     * CommonReservationHandler
     * #isAvailable(eu.ist_phosphorus.harmony.common.serviceinterface
     * .databinding.jaxb.IsAvailableType)
     */
    @Override
    public IsAvailableResponseType isAvailable(final IsAvailableType request)
            throws Throwable {

        final ListRequest autobahnRequest = this.mngTranslator.convert(request);
        final IsAvailableResponseType harmonyResponse =
                this.mngTranslator.simulateIsAvailable(this.autobahnClient
                        .listReservations(autobahnRequest), request);
        return harmonyResponse;
    }

}
