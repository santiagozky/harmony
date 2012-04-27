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
package org.opennaas.extensions.idb.da.thin.persistence.orm;

import java.sql.Timestamp;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;

import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.CreatePath;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.CreatePathType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.PathIdentifierType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.PathType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.TerminatePath;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.TerminatePathType;
import org.opennaas.extensions.idb.da.thin.persistence.DbManager;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.StatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;
import org.opennaas.core.utils.PhLogger;

/**
 * @author Daniel Beer (daniel.beer@iais.fraunhofer.de), Stephan Wagner
 *         (stephan.wagner@iais.fraunhofer.de)
 */

public class GmplsConnection {
    private static Logger logger = PhLogger.getLogger();
    /** */
    private static final int STATUS_ACTIVE = 1;
    /** */
    private static final int STATUS_CANCELLED_BY_SYSTEM = 2;
    /** */
    private static final int STATUS_CANCELLED_BY_USER = 3;
    /** */
    private static final int STATUS_COMPLETED = 4;
    /** */
    private static final int STATUS_PENDING = 5;
    /** */
    private static final int STATUS_SETUP_IN_PROGRESS = 6;
    /** */
    private static final int STATUS_TEARDOWN_IN_PROGRESS = 7;
    /** */
    private static final int STATUS_UNKNOWN = 8;
    /** */
    private long jobId;
    /** */
    private long reservationId;
    /** */
    private int serviceId;
    /** */
    private int connectionId;
    /** */
    private int pathId;
    /** */
    private String srcTNA;
    /** */
    private String destTNA;
    /** */
    private int bandwidth;
    /** */
    private Timestamp startTime;
    /** */
    private Timestamp endTime;
    /** */
    private int status = STATUS_UNKNOWN;
    /** */
    private boolean autoActivation = true;

    /**
     * @return the reservationId
     */
    public final long getReservationId() {
	return this.reservationId;
    }

    /**
     * @param reservationId
     *            the reservationId to set
     */
    public final void setReservationId(final long reservationId) {
	this.reservationId = reservationId;
    }

    /**
     * @return the jobId
     */
    public final long getJobId() {
	return this.jobId;
    }

    /**
     * @param jobId
     *            the jobId to set
     */
    public final void setJobId(final long jobId) {
	this.jobId = jobId;
    }

    /**
     * @return the connectionId
     */
    public final int getConnectionId() {
	return this.connectionId;
    }

    /**
     * @param connectionId
     *            the connectionId to set
     */
    public final void setConnectionId(final int connectionId) {
	this.connectionId = connectionId;
    }

    /**
     * @return the pathId
     */
    public final int getPathId() {
	return this.pathId;
    }

    /**
     * @param pathId
     *            the pathId to set
     */
    public final void setPathId(final int pathId) {
	this.pathId = pathId;
    }

    /**
     * @return the srcTNA
     */
    public final String getSrcTNA() {
	return this.srcTNA;
    }

    /**
     * @param srcTNA
     *            the srcTNA to set
     */
    public final void setSrcTNA(final String srcTNA) {
	this.srcTNA = srcTNA;
    }

    /**
     * @return the destTNA
     */
    public final String getDestTNA() {
	return this.destTNA;
    }

    /**
     * @param destTNA
     *            the destTNA to set
     */
    public final void setDestTNA(final String destTNA) {
	this.destTNA = destTNA;
    }

    /**
     * @return the bandwidth
     */
    public final int getBandwidth() {
	return this.bandwidth;
    }

    /**
     * @param bandwidth
     *            the bandwidth to set
     */
    public final void setBandwidth(final int bandwidth) {
	this.bandwidth = bandwidth;
    }

    /**
     * @return the startTime
     */
    public final Timestamp getStartTime() {
	return this.startTime;
    }

    /**
     * @param startTime
     *            the startTime to set
     */
    public final void setStartTime(final Timestamp startTime) {
	this.startTime = startTime;
    }

    /**
     * @return the endTime
     */
    public final Timestamp getEndTime() {
	return this.endTime;
    }

    /**
     * @param endTime
     *            the endTime to set
     */
    public final void setEndTime(final Timestamp endTime) {
	this.endTime = endTime;
    }

    /**
     * @return the serviceId
     */
    public final int getServiceId() {
	return this.serviceId;
    }

    /**
     * @param serviceId
     *            the serviceId to set
     */
    public final void setServiceId(final int serviceId) {
	this.serviceId = serviceId;
    }

    /**
     * @return the status
     */
    public final int getStatus() {
	return this.status;
    }

    /**
     * @param status
     *            the status to set
     */
    public final void setStatus(final int status) {
	this.status = status;
    }

    /**
     * @param status
     *            the status to set
     * @throws UnexpectedFaultException
     */
    public final void setStatus(final StatusType status)
	    throws UnexpectedFaultException {
	this.status = convertStatus(status);
	logger.debug(this.getIdentifier() + " status now is: " + status + " ("
		+ this.status + ")");
    }

    /**
     * return the connection identifier consisting of
     * jobId-reservationId-serviceId-connectionId.
     * 
     * @return the ConnectionIdentifier
     */
    public final String getIdentifier() {
	return this.jobId + "-" + this.reservationId + "-" + this.serviceId
		+ "-" + this.connectionId;
    }

    /**
     * returns CreatePath request for this connection.
     * 
     * @return the request
     */
    public final CreatePath getCreatePathRequest() {
	final CreatePathType cpt = new CreatePathType();
	final CreatePath cp = new CreatePath();
	final PathType pt = new PathType();
	pt.setBandwidth(this.bandwidth);

	pt.setDestinationTNA(this.destTNA);
	pt.setSourceTNA(this.srcTNA);
	cpt.setPath(pt);
	cp.setCreatePath(cpt);
	return cp;
    }

    /**
     * returns the TerminatePath request for this connection.
     * 
     * @return the request
     */
    public final TerminatePath getTerminatePathRequest(
	    org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.StatusType status) {
	final TerminatePathType tpt = new TerminatePathType();
	final TerminatePath tp = new TerminatePath();
	final PathIdentifierType pathId = new PathIdentifierType();
	pathId.setPathIdentifier(this.pathId);
	tpt.setPathIdentifier(pathId);
	tpt.setStatus(status);
	tp.setTerminatePath(tpt);
	return tp;
    }

    /**
     * @return
     */
    public final GregorianCalendar getStartTimeAsGregorianCalendar() {
	GregorianCalendar cal = new GregorianCalendar();
	cal.setTimeInMillis(this.getStartTime().getTime());
	return cal;
    }

    /**
     * @return
     */
    public final GregorianCalendar getEndTimeAsGregorianCalendar() {
	GregorianCalendar cal = new GregorianCalendar();
	cal.setTimeInMillis(this.getEndTime().getTime());
	return cal;
    }

    /**
     * @return
     */
    public final XMLGregorianCalendar getStartTimeAsXMLCalendar() {
	DatatypeFactory dataf;
	try {
	    dataf = DatatypeFactory.newInstance();
	} catch (DatatypeConfigurationException ex) {
	    throw new RuntimeException(ex);
	}
	dataf.newXMLGregorianCalendar(getStartTimeAsGregorianCalendar());
	return dataf.newXMLGregorianCalendar(getStartTimeAsGregorianCalendar());
    }

    /**
     * @return
     */
    public final int getDuration() {
	return Long
		.valueOf(
			(getEndTimeAsGregorianCalendar().getTimeInMillis() - getStartTimeAsGregorianCalendar()
				.getTimeInMillis()) / 1000L).intValue();
    }

    /**
     * @param status
     * @return
     * @throws UnexpectedFaultException
     */
    public static final StatusType convertStatus(final int status)
	    throws UnexpectedFaultException {

	switch (status) {
	case STATUS_ACTIVE:
	    return StatusType.ACTIVE;
	case STATUS_CANCELLED_BY_SYSTEM:
	    return StatusType.CANCELLED_BY_SYSTEM;
	case STATUS_CANCELLED_BY_USER:
	    return StatusType.CANCELLED_BY_USER;
	case STATUS_COMPLETED:
	    return StatusType.COMPLETED;
	case STATUS_PENDING:
	    return StatusType.PENDING;
	case STATUS_SETUP_IN_PROGRESS:
	    return StatusType.SETUP_IN_PROGRESS;
	case STATUS_TEARDOWN_IN_PROGRESS:
	    return StatusType.TEARDOWN_IN_PROGRESS;
	case STATUS_UNKNOWN:
	    return StatusType.UNKNOWN;
	default:
	    throw new UnexpectedFaultException("No enum for int");
	}
    }

    /**
     * @return
     * @throws UnexpectedFaultException
     */
    public final StatusType convertStatus() throws UnexpectedFaultException {
	return convertStatus(this.status);
    }

    /**
     * @param status
     * @return
     * @throws UnexpectedFaultException
     */
    public static int convertStatus(final StatusType status)
	    throws UnexpectedFaultException {
	switch (status) {
	case ACTIVE:
	    return STATUS_ACTIVE;
	case CANCELLED_BY_SYSTEM:
	    return STATUS_CANCELLED_BY_SYSTEM;
	case CANCELLED_BY_USER:
	    return STATUS_CANCELLED_BY_USER;
	case COMPLETED:
	    return STATUS_COMPLETED;
	case PENDING:
	    return STATUS_PENDING;
	case SETUP_IN_PROGRESS:
	    return STATUS_SETUP_IN_PROGRESS;
	case TEARDOWN_IN_PROGRESS:
	    return STATUS_TEARDOWN_IN_PROGRESS;
	case UNKNOWN:
	    return STATUS_UNKNOWN;
	default:
	    throw new UnexpectedFaultException("No int for enum");
	}

    }

    /**
     * @return the autoActivation
     */
    public final boolean isAutoActivation() {
	return this.autoActivation;
    }

    /**
     * @param autoActivation
     *            the autoActivation to set
     */
    public final void setAutoActivation(final boolean autoActivation) {
	this.autoActivation = autoActivation;
    }

    /**
     * @return
     */
    public final boolean isScheduled() {
	return this.status == STATUS_PENDING;
    }

    /**
     * @return
     * @throws UnexpectedFaultException
     */
    public final GmplsConnection reloadFromDb() throws UnexpectedFaultException {
	return DbManager.reloadConnection(this);
    }
}
