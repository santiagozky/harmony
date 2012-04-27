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


package org.opennaas.extensions.idb.da.thin.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;

import org.opennaas.extensions.idb.da.thin.persistence.exceptions.DestinationPortUnavailableException;
import org.opennaas.extensions.idb.da.thin.persistence.exceptions.PathNotFoundException;
import org.opennaas.extensions.idb.da.thin.persistence.exceptions.SourceAndDestinationPortUnavailableException;
import org.opennaas.extensions.idb.da.thin.persistence.exceptions.SourcePortUnavailableException;
import org.opennaas.extensions.idb.da.thin.persistence.orm.GmplsConnection;
import org.opennaas.extensions.idb.da.thin.persistence.orm.GmplsReservation;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.StatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.InvalidRequestFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.InvalidReservationIDFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.utils.WebserviceUtils;
import org.opennaas.core.utils.Helpers;
import org.opennaas.core.utils.PhLogger;

/**
 * @author Stephan Wagner(stephan.wagner@iais.fraunhofer.de), Daniel Beer
 *         (daniel.beer@iais.fraunhofer.de)
 */
public final class DbManager {
    /** */
    private static DbConnectionManager connectionManager = null;

    private static Logger logger = PhLogger.getLogger(DbManager.class);

    /**
     * Constructor.
     */
    private DbManager() {
	super();
	if (null == DbManager.connectionManager) {
	    DbManager.connectionManager = DbConnectionManager.getInstance();
	}
    }

    /**
     * add a reservation to DB.
     * 
     * @param jobId
     * @param notificationURL
     * @return new reservationId
     * @throws UnexpectedFaultException
     */
    public static long insertReservation(final long jobId,
	    final String notificationURL) throws UnexpectedFaultException {
	Connection con = DbConnectionManager.getConnection();
	PreparedStatement ps;
	try {
	    ps = con
		    .prepareStatement(
			    "INSERT INTO reservations (jobId, notificationConsumerURL) VALUES (?, ?)",
			    Statement.RETURN_GENERATED_KEYS);
	    ps.setLong(1, jobId);
	    ps.setString(2, notificationURL);
	    ps.executeUpdate();

	    ResultSet rsKey = ps.getGeneratedKeys();
	    if (rsKey.next()) {
		long reservationId = rsKey.getLong(1);
		rsKey.close();
		ps.close();
		con.close();
		return reservationId;
	    }
	    ps.close();
	    con.close();
	    throw new UnexpectedFaultException("Database error");

	} catch (SQLException e) {

	    throw new UnexpectedFaultException(e);
	}

    }

    /**
     * @param tna
     * @return
     * @throws UnexpectedFaultException
     */
    private static boolean isTnaInDb(final String tna)
	    throws UnexpectedFaultException {
	try {
	    Connection con = DbConnectionManager.getConnection();
	    PreparedStatement ps = con
		    .prepareStatement("SELECT COUNT(*) FROM endpoints WHERE ( endpointId = ? )");
	    ps.setString(1, tna);
	    ResultSet rs = ps.executeQuery();
	    int endpointCount = -1;
	    if (rs.first()) {
		endpointCount = rs.getInt(1);
	    }
	    rs.close();
	    ps.close();
	    if (endpointCount > 0) {
		return true;
	    }
	    return false;
	} catch (SQLException ex) {
	    throw new UnexpectedFaultException(ex);
	}
    }

    /**
     * @param gmplsConn
     * @return
     * @throws UnexpectedFaultException
     */
    private static boolean isCompleteConnectionAvailable(
	    final GmplsConnection gmplsConn) throws UnexpectedFaultException {
	try {
	    Connection con = DbConnectionManager.getConnection();
	    PreparedStatement ps = con
		    .prepareStatement("SELECT COUNT(*) FROM connections WHERE ( (srcTNA = ? OR destTNA = ?) OR (srcTNA = ? OR destTNA = ?) ) AND "
			    + "( "
			    + "  ("
			    + "     (startTime BETWEEN ? AND ?) OR "
			    + "     (endTime BETWEEN ? AND ?) OR"
			    + "     (startTime <= ? AND endTime >= ? )"
			    + "  )"
			    + " AND ("
			    + "(status = ?) OR"
			    + "(status = ?) OR"
			    + "(status = ?) ))");

	    ps.setString(1, gmplsConn.getSrcTNA());
	    ps.setString(2, gmplsConn.getSrcTNA());
	    ps.setString(3, gmplsConn.getDestTNA());
	    ps.setString(4, gmplsConn.getDestTNA());
	    ps.setTimestamp(5, gmplsConn.getStartTime());
	    ps.setTimestamp(6, gmplsConn.getEndTime());
	    ps.setTimestamp(7, gmplsConn.getStartTime());
	    ps.setTimestamp(8, gmplsConn.getEndTime());
	    ps.setTimestamp(9, gmplsConn.getStartTime());
	    ps.setTimestamp(10, gmplsConn.getEndTime());
	    ps.setInt(11, GmplsConnection.convertStatus(StatusType.ACTIVE));
	    ps.setInt(12, GmplsConnection.convertStatus(StatusType.PENDING));
	    ps.setInt(13, GmplsConnection
		    .convertStatus(StatusType.SETUP_IN_PROGRESS));

	    logger.debug("SQLStatement" + ps.toString());

	    ResultSet rs = ps.executeQuery();
	    int numConnections = -1;

	    if (rs.first()) {
		logger.debug("count " + rs.getInt(1));
		numConnections = rs.getInt(1);
	    }
	    rs.close();
	    ps.close();
	    con.close();
	    return (numConnections == 0);
	} catch (SQLException ex) {
	    throw new UnexpectedFaultException(ex);
	}
    }

    /**
     * @param tna
     * @param start
     * @param end
     * @return
     * @throws UnexpectedFaultException
     */
    private static boolean isConnectionAvailableAtTna(final String tna,
	    final Timestamp start, final Timestamp end)
	    throws UnexpectedFaultException {
	try {
	    Connection con = DbConnectionManager.getConnection();
	    PreparedStatement ps = con
		    .prepareStatement("SELECT COUNT(*) FROM connections WHERE (( "
			    + "(srcTNA = ? OR destTNA = ?) "
			    + "AND ( "
			    + "       (startTime BETWEEN ? AND ?) "
			    + "    OR (endTime BETWEEN ? AND ?) "
			    + "    OR (startTime <= ? AND endtime >= ?)"
			    + "    ) "
			    + ")"
			    + " AND ("
			    + "         (status = ?) OR"
			    + "         (status = ?) OR"
			    + "         (status = ?) " + "        )" + "  )");

	    ps.setString(1, tna);
	    ps.setString(2, tna);
	    ps.setTimestamp(3, start);
	    ps.setTimestamp(4, end);
	    ps.setTimestamp(5, start);
	    ps.setTimestamp(6, end);
	    ps.setTimestamp(7, start);
	    ps.setTimestamp(8, end);
	    ps.setInt(9, GmplsConnection.convertStatus(StatusType.ACTIVE));
	    ps.setInt(10, GmplsConnection.convertStatus(StatusType.PENDING));
	    ps.setInt(11, GmplsConnection
		    .convertStatus(StatusType.SETUP_IN_PROGRESS));
	    logger.debug("SQLStatement" + ps.toString());

	    ResultSet rs = ps.executeQuery();
	    int numConnections = -1;

	    if (rs.first()) {
		logger.debug("count " + rs.getInt(1));
		numConnections = rs.getInt(1);
	    }
	    rs.close();
	    ps.close();
	    con.close();
	    return (numConnections == 0);
	} catch (SQLException ex) {
	    throw new UnexpectedFaultException(ex);
	}
    }

    /**
     * checks Availability.
     * 
     * @param gmplsConn
     *            Reservation
     * @return Availability
     * @throws UnexpectedFaultException
     * @throws SourcePortUnavailableException
     * @throws DestinationPortUnavailableException
     * @throws SourceAndDestinationPortUnavailableException
     * @throws PathNotFoundException
     */
    private static boolean isAvailable(final GmplsConnection gmplsConn)
	    throws UnexpectedFaultException, SourcePortUnavailableException,
	    DestinationPortUnavailableException,
	    SourceAndDestinationPortUnavailableException, PathNotFoundException {

	// Do checks!
	// Does srcTNA exist?
	if (!isTnaInDb(gmplsConn.getSrcTNA())) {
	    throw new PathNotFoundException("Source Port not in Domain");
	}
	// Does Destination TNA exist?
	if (!isTnaInDb(gmplsConn.getDestTNA())) {
	    throw new PathNotFoundException("Destination Port not in Domain");
	}
	// Is the connection possible? If so we just need one SQL Statement if
	// not we need more...
	if (isCompleteConnectionAvailable(gmplsConn)) {
	    return true;
	}
	// Connection is NOT available! But why???
	// Source in use?
	if (!isConnectionAvailableAtTna(gmplsConn.getSrcTNA(), gmplsConn
		.getStartTime(), gmplsConn.getEndTime())) {
	    // OK source is in use is Destination in use?
	    if (!isConnectionAvailableAtTna(gmplsConn.getDestTNA(), gmplsConn
		    .getStartTime(), gmplsConn.getEndTime())) {
		// Yes destination is in use
		throw new SourceAndDestinationPortUnavailableException(
			"Both ports are in use");
	    }
	    // No destination is not in use
	    throw new SourcePortUnavailableException("Source Port is in use");
	}
	// Source is not in use.. Is destination in use?
	if (!isConnectionAvailableAtTna(gmplsConn.getDestTNA(), gmplsConn
		.getStartTime(), gmplsConn.getEndTime())) {
	    // Yes destination is in use
	    throw new DestinationPortUnavailableException(
		    "Destination Port is in use");
	}
	throw new UnexpectedFaultException("No known Error");
    }

    /**
     * insert connection into DB.
     * 
     * @param gmplsConn
     *            the Connection
     * @return
     * @throws UnexpectedFaultException
     * @throws PathNotFoundException
     * @throws SourceAndDestinationPortUnavailableException
     * @throws DestinationPortUnavailableException
     * @throws SourcePortUnavailableException
     */
    public static boolean insertConnection(final GmplsConnection gmplsConn)
	    throws UnexpectedFaultException, SourcePortUnavailableException,
	    DestinationPortUnavailableException,
	    SourceAndDestinationPortUnavailableException, PathNotFoundException {
	int count = 0;
	if (isAvailable(gmplsConn)) {
	    try {
		Connection con = DbConnectionManager.getConnection();
		PreparedStatement ps = con
			.prepareStatement("INSERT INTO connections (reservationId, serviceId, connectionId, pathId, srcTNA, destTNA, bandwidth, startTime, endTime, status, autoActivation) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		ps.setLong(1, gmplsConn.getReservationId());
		ps.setInt(2, gmplsConn.getServiceId());
		ps.setInt(3, gmplsConn.getConnectionId());
		ps.setInt(4, gmplsConn.getPathId());
		ps.setString(5, gmplsConn.getSrcTNA());
		ps.setString(6, gmplsConn.getDestTNA());
		ps.setInt(7, gmplsConn.getBandwidth());
		ps.setTimestamp(8, gmplsConn.getStartTime());
		ps.setTimestamp(9, gmplsConn.getEndTime());
		ps.setInt(10, gmplsConn.getStatus());
		ps.setBoolean(11, gmplsConn.isAutoActivation());
		count = ps.executeUpdate();
		ps.close();
		con.close();
	    } catch (SQLException e) {
		e.printStackTrace();
		throw new UnexpectedFaultException(e);
	    }
	}
	return (count > 0);

    }

    /**
     * return all Connections for Job.
     * 
     * @param jobId
     * @return list of Connections
     * @throws UnexpectedFaultException
     */
    public static List<GmplsConnection> getAllConnections()
	    throws UnexpectedFaultException {
	List<GmplsConnection> connections = new ArrayList<GmplsConnection>();

	try {
	    Connection con = DbConnectionManager.getConnection();
	    PreparedStatement ps = con
		    .prepareStatement("SELECT jobId, reservationId, serviceId, connectionId, pathId, srcTNA, destTNA, bandwidth, startTime, endTime, status, autoActivation FROM FullReservationView  ORDER BY jobId, reservationId, serviceId, connectionId");

	    ResultSet rs = ps.executeQuery();

	    while (rs.next()) {
		GmplsConnection res = new GmplsConnection();

		res.setJobId(rs.getLong("jobId"));
		res.setReservationId(rs.getLong("reservationId"));
		res.setServiceId(rs.getInt("serviceId"));
		res.setConnectionId(rs.getInt("connectionId"));
		res.setPathId(rs.getInt("pathId"));
		res.setSrcTNA(rs.getString("srcTNA"));
		res.setDestTNA(rs.getString("destTNA"));
		res.setBandwidth(rs.getInt("bandwidth"));
		res.setStartTime(rs.getTimestamp("startTime"));
		res.setEndTime(rs.getTimestamp("endTime"));
		res.setStatus(rs.getInt("status"));
		res.setAutoActivation(rs.getBoolean("autoActivation"));

		connections.add(res);
	    }
	    rs.close();
	    ps.close();
	    con.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	    throw new UnexpectedFaultException(e);
	}

	return connections;
    }

    /**
     * return all Connections for Job.
     * 
     * @param jobId
     * @return list of Connections
     * @throws UnexpectedFaultException
     */
    public static List<Long> getAllReservationsForJob(final long jobId)
	    throws UnexpectedFaultException {
	List<Long> reservationList = new ArrayList<Long>();

	try {
	    Connection con = DbConnectionManager.getConnection();
	    PreparedStatement ps = con
		    .prepareStatement("SELECT reservationId FROM reservations WHERE ( jobId = ? ) ORDER BY reservationId");
	    ps.setLong(1, jobId);
	    ResultSet rs = ps.executeQuery();
	    while (rs.next()) {

		reservationList.add(Long.valueOf(rs.getLong("reservationId")));
	    }
	    rs.close();
	    ps.close();
	    con.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	    throw new UnexpectedFaultException(e);
	}

	return reservationList;
    }

    /**
     * return all Connections for reservation.
     * 
     * @param reservationId
     * @return list of connections
     * @throws InvalidRequestFaultException
     */
    public static List<GmplsConnection> getAllConnectionsForReservation(
	    final long reservationId) {
	List<GmplsConnection> connections = new ArrayList<GmplsConnection>();

	try {
	    Connection con = DbConnectionManager.getConnection();
	    PreparedStatement ps = con
		    .prepareStatement("SELECT jobId, serviceId, connectionId, pathId, srcTNA, destTNA, bandwidth, startTime, endTime, status, autoActivation FROM FullReservationView WHERE ( reservationId = ? )");

	    ps.setLong(1, reservationId);

	    ResultSet rs = ps.executeQuery();

	    while (rs.next()) {
		GmplsConnection res = new GmplsConnection();

		res.setJobId(rs.getLong("jobId"));
		res.setReservationId(reservationId);
		res.setServiceId(rs.getInt("serviceId"));
		res.setConnectionId(rs.getInt("connectionId"));
		res.setPathId(rs.getInt("pathId"));
		res.setSrcTNA(rs.getString("srcTNA"));
		res.setDestTNA(rs.getString("destTNA"));
		res.setBandwidth(rs.getInt("bandwidth"));
		res.setStartTime(rs.getTimestamp("startTime"));
		res.setEndTime(rs.getTimestamp("endTime"));
		res.setStatus(rs.getInt("status"));
		res.setAutoActivation(rs.getBoolean("autoActivation"));

		connections.add(res);
	    }
	    rs.close();
	    ps.close();
	    con.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	}

	return connections;
    }

    /**
     * return all Connections for reservation.
     * 
     * @param reservationId
     * @return list of connections
     * @throws InvalidReservationIDFaultException
     * @throws InvalidRequestFaultException
     */
    public static List<GmplsConnection> getAllConnectionsForReservation(
	    final String reservationId)
	    throws InvalidReservationIDFaultException {
	return getAllConnectionsForReservation(WebserviceUtils
		.convertReservationID(reservationId));
    }

    /**
     * return all connections for service.
     * 
     * @param reservationId
     * @param serviceId
     * @return list of connections
     * @throws UnexpectedFaultException
     */
    public static List<GmplsConnection> getAllConnectionsForService(
	    final long reservationId, final int serviceId)
	    throws UnexpectedFaultException {
	List<GmplsConnection> connections = new ArrayList<GmplsConnection>();

	try {
	    Connection con = DbConnectionManager.getConnection();
	    PreparedStatement ps = con
		    .prepareStatement("SELECT jobId, connectionId, pathId, srcTNA, destTNA, bandwidth, startTime, endTime, status, autoActivation FROM FullReservationView WHERE ( reservationId = ? ) AND (serviceId = ?)");

	    ps.setLong(1, reservationId);
	    ps.setInt(2, serviceId);

	    ResultSet rs = ps.executeQuery();

	    while (rs.next()) {
		GmplsConnection res = new GmplsConnection();

		res.setJobId(rs.getLong("jobId"));
		res.setReservationId(reservationId);
		res.setServiceId(serviceId);
		res.setConnectionId(rs.getInt("connectionId"));
		res.setPathId(rs.getInt("pathId"));
		res.setSrcTNA(rs.getString("srcTNA"));
		res.setDestTNA(rs.getString("destTNA"));
		res.setBandwidth(rs.getInt("bandwidth"));
		res.setStartTime(rs.getTimestamp("startTime"));
		res.setEndTime(rs.getTimestamp("endTime"));
		res.setStatus(rs.getInt("status"));
		res.setAutoActivation(rs.getBoolean("autoActivation"));

		connections.add(res);
	    }
	    rs.close();
	    ps.close();
	    con.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	    throw new UnexpectedFaultException(e);
	}

	return connections;
    }

    /**
     * return all connections for service.
     * 
     * @param reservationId
     * @param serviceId
     * @return list of connections
     * @throws UnexpectedFaultException
     * @throws InvalidReservationIDFaultException
     */
    public static List<GmplsConnection> getAllConnectionsForService(
	    final String reservationId, final int serviceId)
	    throws UnexpectedFaultException, InvalidReservationIDFaultException {
	return getAllConnectionsForService(WebserviceUtils
		.convertReservationID(reservationId), serviceId);
    }

    /**
     * return all connections for service.
     * 
     * @param reservationId
     * @param serviceId
     * @return list of connections
     * @throws UnexpectedFaultException
     */
    public static GmplsConnection reloadConnection(
	    final GmplsConnection gmplsConnection)
	    throws UnexpectedFaultException {
	try {
	    Connection con = DbConnectionManager.getConnection();
	    PreparedStatement ps = con
		    .prepareStatement("SELECT jobId, connectionId, pathId, srcTNA, destTNA, bandwidth, startTime, endTime, status, autoActivation FROM FullReservationView WHERE ( reservationId = ? ) AND (serviceId = ?) AND (connectionId = ?)");

	    ps.setLong(1, gmplsConnection.getReservationId());
	    ps.setInt(2, gmplsConnection.getServiceId());
	    ps.setInt(3, gmplsConnection.getConnectionId());
	    ResultSet rs = ps.executeQuery();
	    if (rs.first()) {

		gmplsConnection.setConnectionId(rs.getInt("connectionId"));
		gmplsConnection.setPathId(rs.getInt("pathId"));
		gmplsConnection.setSrcTNA(rs.getString("srcTNA"));
		gmplsConnection.setDestTNA(rs.getString("destTNA"));
		gmplsConnection.setBandwidth(rs.getInt("bandwidth"));
		gmplsConnection.setStartTime(rs.getTimestamp("startTime"));
		gmplsConnection.setEndTime(rs.getTimestamp("endTime"));
		gmplsConnection.setStatus(rs.getInt("status"));
		gmplsConnection.setAutoActivation(rs
			.getBoolean("autoActivation"));
	    }
	    rs.close();
	    ps.close();
	    con.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	    throw new UnexpectedFaultException(e);
	}

	return gmplsConnection;
    }

    /**
     * return all connections for service.
     * 
     * @param reservationId
     * @param serviceId
     * @return list of connections
     * @throws UnexpectedFaultException
     */
    public static GmplsConnection reloadConnectionByPathId(final int pathId)
	    throws UnexpectedFaultException {
	GmplsConnection gmplsConnection = new GmplsConnection();
	try {

	    Connection con = DbConnectionManager.getConnection();
	    PreparedStatement ps = con
		    .prepareStatement("SELECT jobId, reservationId, serviceId, connectionId, pathId, srcTNA, destTNA, bandwidth, startTime, endTime, status, autoActivation FROM FullReservationView WHERE ( pathId = ? )");

	    ps.setInt(1, pathId);

	    ResultSet rs = ps.executeQuery();
	    if (rs.first()) {
		gmplsConnection.setJobId(rs.getLong("jobId"));
		gmplsConnection.setReservationId(rs.getLong("reservationId"));
		gmplsConnection.setServiceId(rs.getInt("serviceId"));
		gmplsConnection.setConnectionId(rs.getInt("connectionId"));
		gmplsConnection.setPathId(rs.getInt("pathId"));
		gmplsConnection.setSrcTNA(rs.getString("srcTNA"));
		gmplsConnection.setDestTNA(rs.getString("destTNA"));
		gmplsConnection.setBandwidth(rs.getInt("bandwidth"));
		gmplsConnection.setStartTime(rs.getTimestamp("startTime"));
		gmplsConnection.setEndTime(rs.getTimestamp("endTime"));
		gmplsConnection.setStatus(rs.getInt("status"));
		gmplsConnection.setAutoActivation(rs
			.getBoolean("autoActivation"));
	    }
	    rs.close();
	    ps.close();
	    con.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	    throw new UnexpectedFaultException(e);
	}

	return gmplsConnection;
    }

    /**
     * save updated status to db.
     * 
     * @param res
     * @throws UnexpectedFaultException
     */
    public static void updateStatus(final GmplsConnection res,
	    final String statusMessage) throws UnexpectedFaultException {
	updateStatus(res.getReservationId(), res.getServiceId(), res
		.getConnectionId(), GmplsConnection.convertStatus(res
		.getStatus()), statusMessage);
    }

    /**
     * save updated status to db.
     * 
     * @param res
     * @throws UnexpectedFaultException
     */
    public static void updateStatus(final long resId, int serviceID,
	    int connectionId, StatusType status, final String statusMessage)
	    throws UnexpectedFaultException {
	try {
	    Connection con = DbConnectionManager.getConnection();
	    PreparedStatement ps = con
		    .prepareStatement("UPDATE connections SET status = ?, statusMessage = ? WHERE ( (reservationId = ?) AND (serviceId = ?) AND (connectionId = ?) )");
	    ps.setInt(1, GmplsConnection.convertStatus(status));
	    ps.setString(2, status.toString() + ": " + statusMessage);
	    ps.setLong(3, resId);
	    ps.setInt(4, serviceID);
	    ps.setInt(5, connectionId);
	    ps.executeUpdate();
	    ps.close();
	    con.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	    throw new UnexpectedFaultException(e);
	}
    }

    /**
     * save returned pathId to db.
     * 
     * @param res
     * @throws UnexpectedFaultException
     */
    public static void updatePathId(final GmplsConnection res)
	    throws UnexpectedFaultException {
	try {
	    Connection con = DbConnectionManager.getConnection();
	    PreparedStatement ps = con
		    .prepareStatement("UPDATE connections SET pathId = ?, status = ? WHERE ( (reservationId = ?) AND (serviceId = ?) AND (connectionId = ?) )");

	    ps.setInt(1, res.getPathId());
	    ps.setInt(2, res.getStatus());
	    ps.setLong(3, res.getReservationId());
	    ps.setInt(4, res.getServiceId());
	    ps.setInt(5, res.getConnectionId());

	    ps.executeUpdate();
	    ps.close();
	    con.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	    throw new UnexpectedFaultException(e);
	}
    }

    /**
     * remove connection from DB.
     * 
     * @param res
     * @return success
     * @throws UnexpectedFaultException
     */
    public static boolean deleteConnection(final GmplsConnection res)
	    throws UnexpectedFaultException {
	int count = 0;
	try {
	    Connection con = DbConnectionManager.getConnection();
	    PreparedStatement ps = con
		    .prepareStatement("DELETE FROM connections WHERE ( (reservationId = ?) AND (serviceId = ?) AND (connectionId = ?) )");

	    ps.setLong(1, res.getReservationId());
	    ps.setInt(2, res.getServiceId());
	    ps.setInt(3, res.getConnectionId());

	    count = ps.executeUpdate();
	    ps.close();
	    con.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	    throw new UnexpectedFaultException(e);
	}

	return (count > 0);
    }

    /**
     * remove Job from DB.
     * 
     * @param jobId
     * @return success
     * @throws UnexpectedFaultException
     */
    public static boolean deleteJob(final long jobId)
	    throws UnexpectedFaultException {
	int count = 0;
	try {
	    Connection con = DbConnectionManager.getConnection();
	    PreparedStatement ps = con
		    .prepareStatement("DELETE FROM reservations WHERE (jobId = ?)");

	    ps.setLong(1, jobId);
	    count = ps.executeUpdate();
	    ps.close();
	    con.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	    throw new UnexpectedFaultException(e);
	}

	return (count > 0);
    }

    /**
     * insert Endpoint into DB.
     * 
     * @param endpoint
     * @return
     */
    public static boolean insertEndpoint(
	    final org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.EndpointType endpoint) {
	int count = 0;
	try {
	    Connection con = DbConnectionManager.getConnection();
	    PreparedStatement ps = con
		    .prepareStatement("INSERT INTO endpoints (endpointId, name, description, interface, domain, bandwidth) VALUES (?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE name=?, description=?, interface=?, domain=?, bandwidth=?");

	    ps.setString(1, endpoint.getEndpointId());
	    ps.setString(2, endpoint.getName());
	    ps.setString(3, endpoint.getDescription());
	    ps.setString(4, endpoint.getInterface().toString());
	    ps.setString(5, endpoint.getDomainId());
	    ps.setInt(6, endpoint.getBandwidth().intValue());
	    ps.setString(7, endpoint.getName());
	    ps.setString(8, endpoint.getDescription());
	    ps.setString(9, endpoint.getInterface().toString());
	    ps.setString(10, endpoint.getDomainId());
	    ps.setInt(11, endpoint.getBandwidth().intValue());

	    count = ps.executeUpdate();
	    ps.close();
	    con.close();
	} catch (SQLException e) {
	    logger.error(e.getMessage(), e);
	}

	return (count > 0);
    }

    /**
     * @return
     * @throws UnexpectedFaultException
     */
    public static String getDomainId() throws UnexpectedFaultException {
	String domain = "viola-gmpls";

	try {
	    Connection con = DbConnectionManager.getConnection();
	    PreparedStatement ps = con
		    .prepareStatement("SELECT DISTINCT domain FROM endpoints;");

	    ResultSet rs = ps.executeQuery();
	    if (rs.first()) {
		domain = rs.getString("domain");
	    }

	    ps.close();
	    con.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	    throw new UnexpectedFaultException(e);
	}

	return domain;
    }

    /**
     * remove reservation and all it's connections from DB.
     * 
     * @param reservationId
     * @return success
     * @throws UnexpectedFaultException
     */
    public static boolean deleteWholeReservation(final long reservationId)
	    throws UnexpectedFaultException {
	int count = 0;
	try {
	    Connection con = DbConnectionManager.getConnection();
	    PreparedStatement ps = con
		    .prepareStatement("DELETE FROM connections WHERE (reservationId = ?)");

	    ps.setLong(1, reservationId);

	    count = ps.executeUpdate();
	    ps.close();

	    ps = con
		    .prepareStatement("DELETE FROM reservations WHERE (reservationId = ?)");

	    ps.setLong(1, reservationId);
	    count += ps.executeUpdate();
	    ps.close();
	    con.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	    throw new UnexpectedFaultException(e);
	}

	return (count > 0);
    }

    /**
     * @param reservationId
     * @return
     * @throws UnexpectedFaultException
     */
    public static GmplsReservation getReservation(final long reservationId)
	    throws UnexpectedFaultException {
	GmplsReservation res = null;
	try {
	    Connection con = DbConnectionManager.getConnection();
	    PreparedStatement ps = con
		    .prepareStatement("SELECT jobId, notificationConsumerURL FROM reservations WHERE reservationId = ?;");

	    ps.setLong(1, reservationId);

	    ResultSet rs = ps.executeQuery();
	    if (rs.first()) {
		res = new GmplsReservation(reservationId, rs.getLong("jobId"),
			rs.getString("notificationConsumerURL"));
	    }

	    ps.close();
	    con.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	    throw new UnexpectedFaultException(e);
	}
	return res;
    }

    /**
     * @param periodStartTime
     * @param periodEndTime
     * @return
     * @throws UnexpectedFaultException
     */
    public static List<GmplsReservation> getReservationsInPeriod(
	    XMLGregorianCalendar periodStartTime,
	    XMLGregorianCalendar periodEndTime) throws UnexpectedFaultException {

	List<GmplsReservation> reservations = new ArrayList<GmplsReservation>();
	try {
	    Connection con = DbConnectionManager.getConnection();
	    PreparedStatement ps = con
		    .prepareStatement("SELECT DISTINCT FullReservationView.jobId, FullReservationView.reservationId, reservations.notificationConsumerURL FROM (FullReservationView JOIN reservations ON reservations.reservationId = FullReservationView.reservationId) WHERE "
			    + "( "
			    + "  ("
			    + "     (startTime BETWEEN ? AND ?) OR "
			    + "     (endTime BETWEEN ? AND ?) OR"
			    + "     (startTime <= ? AND endTime >= ? )"
			    + "  ))");

	    Calendar calStart = Helpers.xmlCalendarToCalendar(periodStartTime);
	    Timestamp startTime = new Timestamp(calStart.getTime().getTime());

	    Calendar calEnd = Helpers.xmlCalendarToCalendar(periodEndTime);
	    Timestamp endTime = new Timestamp(calEnd.getTime().getTime());

	    ps.setTimestamp(1, startTime);
	    ps.setTimestamp(2, endTime);
	    ps.setTimestamp(3, startTime);
	    ps.setTimestamp(4, endTime);
	    ps.setTimestamp(5, startTime);
	    ps.setTimestamp(6, endTime);

	    logger.debug("SQLStatement" + ps.toString());

	    ResultSet rs = ps.executeQuery();

	    while (rs.next()) {
		reservations.add(new GmplsReservation(rs
			.getLong("reservationId"), rs.getLong("jobId"), rs
			.getString("notificationConsumerURL")));
	    }

	    rs.close();
	    ps.close();
	    con.close();
	} catch (SQLException ex) {
	    throw new UnexpectedFaultException(ex);
	}
	return reservations;
    }

    /**
     * @param reservationId
     * @return
     * @throws UnexpectedFaultException
     */
    public static List<Integer> getServiceIdsForReservation(long reservationId)
	    throws UnexpectedFaultException {
	List<Integer> serviceIds = new ArrayList<Integer>();
	try {
	    Connection con = DbConnectionManager.getConnection();
	    PreparedStatement ps = con
		    .prepareStatement("SELECT DISTINCT serviceId FROM connections WHERE reservationId = ?;");
	    ps.setLong(1, reservationId);
	    logger.debug("SQLStatement" + ps.toString());

	    ResultSet rs = ps.executeQuery();

	    while (rs.next()) {
		serviceIds.add(Integer.valueOf(rs.getInt("serviceId")));
	    }

	} catch (SQLException e) {
	    e.printStackTrace();
	    throw new UnexpectedFaultException(e);

	}

	return serviceIds;
    }
}
