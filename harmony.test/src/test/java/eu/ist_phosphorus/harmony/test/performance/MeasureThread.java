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


package eu.ist_phosphorus.harmony.test.performance;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CreateReservationResponse;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.exceptions.InvalidRequestFaultException;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.utils.JaxbSerializer;
import eu.ist_phosphorus.harmony.common.serviceinterface.reservation.SimpleReservationClient;
import eu.ist_phosphorus.harmony.common.utils.PhLogger;

/**
 * Thread to measure a create reservation request.
 * 
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 */
class MeasureThread extends Thread {
    private long duration = 0;
    final private SimpleReservationClient reservationClient;
    final private Logger logger;
    private Exception exception;
    final private Element reqElement;
    final private int waitingTime;
    final private RequestType type;

    public enum RequestType {
        CREATE, ISAVAILABLE
    }

    /**
     * @param source
     * @param target
     * @param simpleReservationClient
     */
    public MeasureThread(
            final SimpleReservationClient simpleReservationClient,
            final Element request, final int waitingTime,
            final RequestType type) {
        this.reservationClient = simpleReservationClient;
        this.logger = PhLogger.getSeparateLogger("scalability");
        this.reqElement = request;
        this.waitingTime = waitingTime;
        this.type = type;
    }

    /**
     * @return
     */
    public long getDuration() {
        return this.duration;
    }

    /**
     * @return
     */
    public Exception getException() {
        return this.exception;
    }

    /**
     * @see java.lang.Thread#run()
     */
    @Override
    public void run() {
        this.logger.info("Sending request...");

        try {
            final long start;
            final long end;

            
            if (RequestType.CREATE.equals(this.type)) {
                final Element result;
                final String lri;
                final CreateReservationResponse response;

                start = System.currentTimeMillis();
                result =
                    this.reservationClient.createReservation(this.reqElement);
                end = System.currentTimeMillis();
                response = generateResponse(result);
                
                lri = response.getCreateReservationResponse().getReservationID();
                this.logger.info("Cancelling LRI: " + lri);
                this.reservationClient.cancelReservation(lri);                
            } else if (RequestType.ISAVAILABLE.equals(this.type)) {
                start = System.currentTimeMillis();
                this.reservationClient.isAvailable(this.reqElement);
                end = System.currentTimeMillis();
            } else {
                this.exception = new Exception("Unknow type: " + this.type);
                start = 0;
                end = 0;
            }

            this.duration = end - start;
            Thread.sleep(this.waitingTime);
        } catch (final Exception exception) {
            this.exception = exception;
        }
    }

    /**
     * @param result
     * @return
     * @throws InvalidRequestFaultException
     * @throws UnexpectedFaultException
     */
    private CreateReservationResponse generateResponse(final Element result)
            throws InvalidRequestFaultException, UnexpectedFaultException {
        final CreateReservationResponse response;
        /* -------------------------------------------------------------- */
        response =
                (CreateReservationResponse) JaxbSerializer.getInstance()
                        .elementToObject(result);
        /* -------------------------------------------------------------- */
        return response;
    }

}
