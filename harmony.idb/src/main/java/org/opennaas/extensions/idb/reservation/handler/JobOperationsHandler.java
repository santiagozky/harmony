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


package org.opennaas.extensions.idb.reservation.handler;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelJobResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelJobType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CompleteJobResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CompleteJobType;
import org.opennaas.extensions.idb.database.hibernate.Reservation;
import org.opennaas.extensions.idb.exception.database.DatabaseException;

/**
 * . Handler for Reservation-Job-operations
 */
public final class JobOperationsHandler {
    /** Singleton instance. */
    private static JobOperationsHandler selfInstance = null;

    /**
     * Instance getter.
     * 
     * @return Singleton Instance
     */
    public static JobOperationsHandler getInstance() {
        if (JobOperationsHandler.selfInstance == null) {
            JobOperationsHandler.selfInstance = new JobOperationsHandler();
        }
        return JobOperationsHandler.selfInstance;
    }

    /**
     * Private constructor: Singleton.
     */
    private JobOperationsHandler() {
        // nothing yet
    }

    /**
     * . CancelJob Handler
     * 
     * @param element
     *            CancelJobType
     * @return CancelJobResponseType
     * @throws DatabaseException
     *             DatabaseException
     */
    public CancelJobResponseType cancelJob(final CancelJobType element)
            throws DatabaseException {
        final CancelJobResponseType response = new CancelJobResponseType();
        response.setSuccess(false);
        for (final Reservation res : Reservation.loadJob(element.getJobID())) {
            // TODO: cancel res, dont know what this is supposed to be for but
            // it was here previously:
            res.save();
        }
        response.setSuccess(true);
        return response;
    }

    /**
     * Singleton - Cloning _not_ supported!
     * 
     * @return Should never return anything...
     * @throws CloneNotSupportedException
     *             Singleton hates to be cloned!
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    /**
     * . CompleteJob handler
     * 
     * @param element
     *            CompleteJobType
     * @return CompleteJobResponseType
     * @throws DatabaseException
     *             DatabaseException
     */
    public CompleteJobResponseType completeJob(final CompleteJobType element)
            throws DatabaseException {
        final CompleteJobResponseType response = new CompleteJobResponseType();
        response.setSuccess(false);
        for (final Reservation res : Reservation.loadJob(element.getJobID())) {
            // TODO: make res permanent, dont know what this is supposed to be
            // for but it was here previously:
            res.save();
        }
        response.setSuccess(true);
        return response;
    }
}
