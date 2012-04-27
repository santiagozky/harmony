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


/**
 *
 */
package org.opennaas.extensions.idb.da.thin.scheduler.jobs;

import org.apache.log4j.Logger;
import org.apache.muse.ws.addressing.soap.SoapFault;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import org.opennaas.extensions.idb.da.thin.persistence.DbManager;
import org.opennaas.extensions.idb.da.thin.persistence.orm.GmplsConnection;
import org.opennaas.extensions.idb.da.thin.impl.CreationHandler;
import org.opennaas.extensions.idb.da.thin.impl.StatusHandler;
import org.opennaas.extensions.idb.da.thinutils.Notifications;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.StatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.InvalidRequestFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;
import org.opennaas.core.utils.PhLogger;

/**
 * @author Daniel Beer (daniel.beer@iais.fraunhofer.de), Stephan Wagner
 *         (stephan.wagner@iais.fraunhofer.de)
 */
public class SetUpJob implements Job {

    /*
     * (non-Javadoc)
     *
     * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
     */
    public final void execute(final JobExecutionContext context) {
        Logger logger = PhLogger.getLogger(this.getClass());
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        logger.debug("Setting up GMPLS path");
        GmplsConnection con = (GmplsConnection) dataMap.get("request");
        try {
            CreationHandler.setUpConnection(con);
        } catch (InvalidRequestFaultException e) {
            logger.debug(e.getMessage(), e);
        } catch (SoapFault e) {

            try {
                con.setStatus(StatusType.CANCELLED_BY_SYSTEM);
                DbManager.updateStatus(con,
                        "scheduler was not able to setup path");
            } catch (UnexpectedFaultException e1) {

                logger.debug(e1.getMessage(), e1);
            }
            logger.debug(e.getMessage(), e);
        }

        try {
            logger.debug("SetUpJob sending notifications for Reservation: "
                    + con.getReservationId());
            for (Integer serviceID : DbManager.getServiceIdsForReservation(con
                    .getReservationId())) {
                Notifications.publish(con.getReservationId(), StatusHandler
                        .getStatusOfService(con.getReservationId(), serviceID
                                .intValue(), false));
            }
        } catch (UnexpectedFaultException e) {
            e.printStackTrace();
        }
    }
}
