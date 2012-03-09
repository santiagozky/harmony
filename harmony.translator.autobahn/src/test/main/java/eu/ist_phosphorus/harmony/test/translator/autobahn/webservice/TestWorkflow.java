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


package eu.ist_phosphorus.harmony.test.translator.autobahn.webservice;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.axis2.AxisFault;
import org.apache.log4j.Logger;
import org.apache.muse.ws.addressing.soap.SoapFault;
import org.junit.Test;

import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CancelReservationResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CancelReservationType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.ConnectionConstraintType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CreateReservationResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CreateReservationType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EndpointType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.FixedReservationConstraintType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetReservationsComplexType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetReservationsResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetReservationsType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetStatusResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetStatusType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.IsAvailableType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.ReservationType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.ServiceConstraintType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetStatusResponseType.ServiceStatus;
import eu.ist_phosphorus.harmony.common.serviceinterface.reservation.SimpleReservationClient;
import eu.ist_phosphorus.harmony.common.utils.Config;
import eu.ist_phosphorus.harmony.common.utils.Helpers;
import eu.ist_phosphorus.harmony.common.utils.PhLogger;
import eu.ist_phosphorus.harmony.translator.autobahn.webservice.ReservationWS;

public class TestWorkflow {

    private final SimpleReservationClient client;
    private final Logger logger;

    public TestWorkflow() throws AxisFault {
        this.client = new SimpleReservationClient(new ReservationWS());
        this.logger = PhLogger.getLogger();
    }

    private ServiceConstraintType getService() {
        final int harmonyDuration = 100;
        final int harmonyBandwidth = 22;
        final boolean automaticActivation = true;

        final EndpointType harmonySource = new EndpointType();
        harmonySource
                .setEndpointId(Config.getString("test.test.endpoint0.tna"));
        final EndpointType harmonyTarget = new EndpointType();
        harmonyTarget
                .setEndpointId(Config.getString("test.test.endpoint1.tna"));
        final XMLGregorianCalendar harmonyStartTime =
                Helpers.generateXMLCalendar();

        final ConnectionConstraintType harmonyConnection =
                new ConnectionConstraintType();
        harmonyConnection.setConnectionID(1);
        harmonyConnection.setMinBW(harmonyBandwidth);
        harmonyConnection.setMaxBW(harmonyBandwidth);
        harmonyConnection.setSource(harmonySource);
        harmonyConnection.getTarget().add(harmonyTarget);

        final FixedReservationConstraintType harmonyFixedConstraints =
                new FixedReservationConstraintType();
        harmonyFixedConstraints.setDuration(harmonyDuration);
        harmonyFixedConstraints.setStartTime(harmonyStartTime);

        final ServiceConstraintType harmonyServiceContraint =
                new ServiceConstraintType();
        harmonyServiceContraint.setAutomaticActivation(automaticActivation);
        harmonyServiceContraint.setServiceID(1);
        harmonyServiceContraint.setTypeOfReservation(ReservationType.FIXED);
        harmonyServiceContraint
                .setFixedReservationConstraints(harmonyFixedConstraints);
        harmonyServiceContraint.getConnections().add(harmonyConnection);

        return harmonyServiceContraint;
    }

    @Test
    public final void testGetReservations() throws SoapFault {
        /*
         * GetReservations
         */
        final GetReservationsType getReservations = new GetReservationsType();
        getReservations
                .setPeriodStartTime(Helpers.generateXMLCalendar(-120, 0));
        getReservations.setPeriodEndTime(Helpers.generateXMLCalendar());

        final GetReservationsResponseType response =
                this.client.getReservations(getReservations);

        for (final GetReservationsComplexType reservation : response
                .getReservation()) {
            this.logger.info(reservation.getReservationID());
        }
    }

    @Test
    public final void testLocalWorkflow() throws SoapFault {
        /*
         * IsAvailable
         */
        final IsAvailableType req = new IsAvailableType();

        req.getService().add(this.getService());

        this.client.isAvailable(req);
        this.logger.info("Service Available");

        /*
         * CreateReervation
         */
        final CreateReservationType result = new CreateReservationType();

        result.getService().add(this.getService());

        String reservationId = null;

        final CreateReservationResponseType response =
                this.client.createReservation(result);

        this.logger.info(response.getReservationID() + " Created!");

        reservationId = response.getReservationID();

        /*
         * GetStatus
         */
        final GetStatusType getStatus = new GetStatusType();

        getStatus.setReservationID(reservationId);

        final GetStatusResponseType statusResponse =
                this.client.getStatus(getStatus);

        for (final ServiceStatus status : statusResponse.getServiceStatus()) {
            this.logger.info(status.getStatus().toString());
        }

        /*
         * GetReservations
         */
        final GetReservationsType getReservations = new GetReservationsType();
        getReservations.setPeriodStartTime(Helpers.generateXMLCalendar(-60, 0));
        getReservations.setPeriodEndTime(Helpers.generateXMLCalendar(400, 0));

        final GetReservationsResponseType getResponse =
                this.client.getReservations(getReservations);

        for (final GetReservationsComplexType reservation : getResponse
                .getReservation()) {
            this.logger.info(reservation.getReservationID());
        }

        /*
         * CancelReservation
         */
        final CancelReservationType cancelType = new CancelReservationType();
        cancelType.setReservationID(reservationId);

        final CancelReservationResponseType cancelResponse =
                this.client.cancelReservation(cancelType);

        this.logger.info(cancelResponse.isSuccess());

    }

    @Test
    public final void testSimpleWorkflow() throws SoapFault,
            DatatypeConfigurationException {
        eu.ist_phosphorus.harmony.test.common.serviceinterface.TestWorkflow
                .testSimpleWorkflow(this.client, this.logger);
    }
}
