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


package org.opennaas.extensions.idb.da.thin.impl;

import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.SchedulerException;

import org.opennaas.extensions.idb.da.thin.persistence.DbManager;
import org.opennaas.extensions.idb.da.thin.persistence.orm.GmplsConnection;
import org.opennaas.extensions.idb.da.thin.scheduler.JobManager;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ActivateResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ActivateType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.StatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.InvalidReservationIDFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;
import org.opennaas.core.utils.Helpers;
import org.opennaas.core.utils.PhLogger;

/**
 * @author Stephan Wagner (stephan.wagner@iais.fraunhofer.de), Daniel Beer
 *         (daniel.beer@iais.fraunhofer.de)
 */
public final class ActivationHandler {
    /** Singleton instance. */
    private static ActivationHandler selfInstance = null;

    private static Logger logger = null;

    /**
     * Private constructor: Singleton.
     */
    private ActivationHandler() {
        // nothing to do here
        logger = PhLogger.getLogger(this.getClass());
    }

    /**
     * Instance getter.
     *
     * @return Singleton Instance
     */
    public static ActivationHandler getInstance() {
        if (ActivationHandler.selfInstance == null) {
            ActivationHandler.selfInstance = new ActivationHandler();
        }
        return ActivationHandler.selfInstance;
    }

    /**
     * Singleton - Cloning _not_ supported!
     *
     * @return Should never return anything...
     * @throws CloneNotSupportedException
     *                 Singleton hates to be cloned!
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    /**
     * @param activateRequest
     *                request for activation
     * @return response as ActivateResponseType
     * @throws UnexpectedFaultException
     *                 should not happen
     * @throws InvalidReservationIDFaultException
     */
    public ActivateResponseType activate(final ActivateType activateRequest)
            throws UnexpectedFaultException, InvalidReservationIDFaultException {

        boolean success = false;
        final ActivateResponseType response = new ActivateResponseType();

        List<GmplsConnection> connections = null;
        final Timestamp now =
                new Timestamp(Helpers.generateXMLCalendar()
                        .toGregorianCalendar().getTimeInMillis());

        if (activateRequest.getServiceID() > 0) {
            connections =
                    DbManager
                            .getAllConnectionsForService(activateRequest
                                    .getReservationID(), activateRequest
                                    .getServiceID());
        } else {
            connections =
                    DbManager.getAllConnectionsForReservation(activateRequest
                            .getReservationID());
        }

        for (final GmplsConnection gmplsConnection : connections) {
            if (!gmplsConnection.isAutoActivation()
                    && !gmplsConnection.isScheduled()) {
                // reservation is not autoActivation and not yet scheduled
                if (gmplsConnection.getEndTime().after(now)) {
                    try {
                        JobManager.getInstance().schedulePathSetUp(
                                gmplsConnection);
                        JobManager.getInstance().schedulePathTermination(
                                gmplsConnection);
                        gmplsConnection.setStatus(StatusType.PENDING);
                        DbManager.updateStatus(gmplsConnection,
                                "Has been activated");
                        success = true;
                    } catch (final SchedulerException e) {
                        throw new UnexpectedFaultException(e);
                    }
                }
            }
        }
        response.setSuccess(success);

        return response;
    }
}
