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
import org.opennaas.extensions.idb.da.thin.impl.StatusHandler;
import org.opennaas.extensions.idb.da.thinutils.Notifications;
import org.opennaas.extensions.idb.da.thin.webservice.ContextListener;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ServiceStatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.StatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.InvalidRequestFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.PathNotFoundFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.utils.AJaxbSerializer;
import org.opennaas.core.utils.PhLogger;

/**
 * @author Daniel Beer (daniel.beer@iais.fraunhofer.de), Stephan Wagner
 *         (stephan.wagner@iais.fraunhofer.de)
 */
public class TearDownJob implements Job {

    /*
     * (non-Javadoc)
     * 
     * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
     */
    public final void execute(final JobExecutionContext context) {
	Logger logger = PhLogger.getLogger(this.getClass());
	JobDataMap dataMap = context.getJobDetail().getJobDataMap();
	AJaxbSerializer jser = org.opennaas.extensions.gmpls.serviceinterface.databinding.utils.JaxbSerializer.getInstance();
	GmplsConnection con = (GmplsConnection) dataMap.get("request");

	try {
	    con.reloadFromDb();
	    try {
		con.setStatus(StatusType.TEARDOWN_IN_PROGRESS);
		logger.debug("Trying to automaticaly terminate the Connection");
		DbManager.updateStatus(con,
			"Trying to automaticaly terminate the Connection");
	    } catch (UnexpectedFaultException e1) {
		logger.error(e1.getMessage(), e1);
	    }
	    try {
		logger.debug("Terminate GMPLS path: " + con.getPathId());
		jser
			.elementToObject(ContextListener
				.getGmplsWS()
				.terminatePath(
					(jser
						.objectToElement(con
							.getTerminatePathRequest(org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.StatusType.COMPLETED)))));
		logger.debug("Path terminated");
		con.setStatus(StatusType.COMPLETED);
		DbManager.updateStatus(con, "Connection has been Completed");
	    } catch (InvalidRequestFaultException e) {
		con.setStatus(StatusType.UNKNOWN);
		DbManager.updateStatus(con, e.getMessage());
		logger.debug(e.getMessage(), e);
	    } catch (UnexpectedFaultException e) {
		con.setStatus(StatusType.UNKNOWN);
		DbManager.updateStatus(con, e.getMessage());
		logger.debug(e.getMessage(), e);
	    } catch (PathNotFoundFaultException e) {
		try {
		    con.setStatus(StatusType.CANCELLED_BY_SYSTEM);
		    DbManager.updateStatus(con, e.getMessage());
		} catch (UnexpectedFaultException e1) {
		    logger.debug(e1.getMessage(), e1);
		}
		logger.debug(e.getMessage(), e);
	    } catch (SoapFault e) {
		try {
		    con.setStatus(StatusType.UNKNOWN);
		    DbManager.updateStatus(con, e.getMessage());
		} catch (UnexpectedFaultException e1) {
		    logger.debug(e1.getMessage(), e1);
		}
		logger.debug(e.getMessage(), e);
	    }
	} catch (UnexpectedFaultException e2) {
	    logger.debug(e2.getMessage(), e2);
	}
	try {
	    logger.debug("TearDownJob sending notifications for Reservation: "
		    + con.getReservationId());
	    for (Integer serviceID : DbManager.getServiceIdsForReservation(con
		    .getReservationId())) {
		ServiceStatusType temp = StatusHandler.getStatusOfService(con
			.getReservationId(), serviceID.intValue(), false);
		Notifications.publish(con.getReservationId(), temp);
	    }
	} catch (UnexpectedFaultException e) {
	    e.printStackTrace();
	}

    }
}
