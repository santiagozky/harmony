package org.opennaas.extensions.gmpls.utils.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.EndpointInterfaceType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.EndpointType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.PathType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.exceptions.BandwidthFaultException;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.exceptions.PathNotFoundFaultException;
import org.opennaas.extensions.gmpls.utils.database.orm.DeviceInformation;
import org.opennaas.extensions.gmpls.utils.database.orm.LspInformation;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.EndpointNotFoundFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;

/**
 * @author Stephan Wagner(stephan.wagner@iais.fraunhofer.de), Daniel Beer
 *         (daniel.beer@iais.fraunhofer.de)
 */
public final class DbManager {
	/** */
	private static DbConnectionManager connectionManager = null;

	/**
	 * @param endpointId
	 *            of endpoint
	 * @param bandwidth
	 *            to reserve
	 * @return if bandwidth is available
	 * @throws UnexpectedFaultException
	 *             in case of error
	 */
	public static boolean checkBandwidthCapability(final String endpointId,
			final int bandwidth) throws BandwidthFaultException,
			UnexpectedFaultException {

		int tmpBw = 0;
		try {
			final Connection con = DbConnectionManager.getConnection();
			final PreparedStatement statement = con
					.prepareStatement("SELECT endpoint.bandwidth FROM endpoint WHERE endpoint.endpointId = ?");
			statement.setString(1, endpointId);

			ResultSet rs = statement.executeQuery();
			if (rs.isBeforeFirst()) {
				rs.first();
				tmpBw = rs.getInt("bandwidth");
			} else {
				throw new BandwidthFaultException("Not enough bandwidth at "
						+ endpointId);
			}
			statement.close();
			con.close();
		} catch (SQLException e) {
			throw new UnexpectedFaultException(e);
		}
		return (bandwidth <= tmpBw);
	}

	/**
	 * @param path
	 * @param vPathIndex
	 * @return
	 * @throws UnexpectedFaultException
	 */
	public static int createPath(final PathType path, final int vPathIndex)
			throws UnexpectedFaultException {

		int pathId = 0;

		try {
			Connection con = DbConnectionManager.getConnection();
			PreparedStatement statement = con
					.prepareStatement(
							"INSERT INTO path (vPathIndex, pathDescriptor, srcTNA, destTNA, bandwidth) VALUES ( ?, ?, ?, ?, ?)",
							Statement.RETURN_GENERATED_KEYS);

			statement.setInt(1, vPathIndex);
			statement.setString(2,
					"-" + path.getSourceTNA() + "-" + path.getDestinationTNA());
			statement.setString(3, path.getSourceTNA());
			statement.setString(4, path.getDestinationTNA());
			statement.setInt(5, path.getBandwidth());

			statement.executeUpdate();

			final ResultSet rskey = statement.getGeneratedKeys();

			if (rskey.next()) {
				pathId = rskey.getInt(1);
			} else {
				throw new SQLException(
						"SQL Error while inserting record into path table");
			}
			rskey.close();
			statement.close();

			con.close();

		} catch (final SQLException se) {
			throw new UnexpectedFaultException(se);
		}

		return pathId;
	}

	/**
	 * @param pathId
	 *            of path to delete
	 * @return success
	 * @throws PathNotFoundFaultException
	 *             in case of error
	 */
	public static boolean deletePath(final int pathId)
			throws PathNotFoundFaultException {
		int rowsUpdated = 0;
		try {
			final Connection con = DbConnectionManager.getConnection();
			final PreparedStatement statement = con
					.prepareStatement("DELETE FROM path WHERE pathId = ?");
			statement.setInt(1, pathId);
			rowsUpdated = statement.executeUpdate();

			statement.close();
			con.close();

		} catch (final SQLException se) {
			throw new PathNotFoundFaultException(se);
		}
		return (rowsUpdated > 0);
	}

	/**
	 * Retrieve all PathIds.
	 * 
	 * @return list of PathIds
	 * @throws UnexpectedFaultException
	 *             in case of error
	 */
	public static List<Integer> getAllPathIds() throws UnexpectedFaultException {
		final List<Integer> list = new ArrayList<Integer>();
		try {
			final Connection con = DbConnectionManager.getConnection();
			final Statement statement = con.createStatement();
			ResultSet rsL = null;
			rsL = statement
					.executeQuery("SELECT pathId FROM path ORDER BY pathId");
			// move all PathIds from ResultSet to list
			if (rsL.isBeforeFirst()) {
				while (rsL.next()) {
					list.add(Integer.valueOf(rsL.getInt("pathId")));
				}
			}
			rsL.close();
			statement.close();
			con.close();
		} catch (final SQLException se) {
			throw new UnexpectedFaultException(se);
		}
		return list;
	}

	/**
	 * @return
	 * @throws UnexpectedFaultException
	 */
	public static List<LspInformation> getAllPathInformation()
			throws UnexpectedFaultException {
		try {
			final Connection con = DbConnectionManager.getConnection();
			List<LspInformation> lspList = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			pstm = con
					.prepareStatement("SELECT pathId,vPathIndex, pathDescriptor, srcTNA, destTNA FROM path");
			rs = pstm.executeQuery();
			lspList = new ArrayList<LspInformation>();
			if (rs.isBeforeFirst()) {
				rs.first();

				boolean hasNext = true;
				while (hasNext) {
					LspInformation lsp = new LspInformation();
					lsp.setPathId(rs.getInt("pathId"));
					lsp.setLspIndex(rs.getInt("vPathIndex"));
					lsp.setLspDescriptor(rs.getString("pathDescriptor"));
					lsp.setSourceDevice(getDeviceInformation(rs
							.getString("srcTNA")));
					lsp.setDestinationDevice(getDeviceInformation(rs
							.getString("destTNA")));
					lspList.add(lsp);
					hasNext = rs.next();
				}
			}
			rs.close();
			pstm.close();
			con.close();
			return lspList;
		} catch (Exception ex) {
			throw new UnexpectedFaultException(ex);
		}
	}

	/**
	 * @param tnaAddress
	 *            of device
	 * @return device information
	 * @throws UnexpectedFaultException
	 * @throws EndpointNotFoundFaultException
	 */
	public static DeviceInformation getDeviceInformation(final String tnaAddress)
			throws EndpointNotFoundFaultException {
		DeviceInformation result = null;
		try {
			final Connection con = DbConnectionManager.getConnection();
			PreparedStatement pstm = null;
			ResultSet rs = null;
			pstm = con
					.prepareStatement("SELECT * FROM View_Device_Endpoint WHERE endpointId = ?");

			pstm.setString(1, tnaAddress);
			rs = pstm.executeQuery();
			if (rs.isBeforeFirst()) {
				result = new DeviceInformation();
				rs.first();
				result.setDeviceName(rs.getString("deviceName"));
				result.setDeviceType(rs.getString("deviceType"));
				result.setIpAddress(rs.getString("ipAddress"));
				result.setModel(rs.getString("model"));
				result.setModule(rs.getString("module"));
				result.setSoftwarerelease(rs.getString("softwareRelease"));
				result.setTnaAddress(rs.getString("endpointId"));
				result.setVendorName(rs.getString("vendorName"));
				result.setDatabearer(rs.getString("databearer"));
			}
			rs.close();
			pstm.close();

			con.close();
			if (null == result) {
				throw new EndpointNotFoundFaultException(
						"TNA not found in database.");
			}

		} catch (SQLException e) {
			throw new EndpointNotFoundFaultException(e);
		}
		return result;
	}

	/**
	 * Gets all ports from the ports Table.
	 * 
	 * @param deviceIPAddress
	 *            if null all devices are considered.
	 * @return ResultSet with the Informations
	 * @throws SQLException
	 */
	public static List<EndpointType> getEndpoints(final String deviceIPAddress)
			throws SQLException {
		final Connection con = DbConnectionManager.getConnection();
		Statement statement = null;
		ResultSet rs = null;
		List<EndpointType> tnaList = new ArrayList<EndpointType>(0);
		if ((deviceIPAddress != null) && (deviceIPAddress.length() > 0)) {
			statement = con
					.prepareStatement("SELECT endpoint.endpointId, endpoint.name, endpoint.bandwidth, endpoint.domain, endpoint.interface, endpoint.description FROM endpoint WHERE endpointId = ? ORDER BY endpointId");
			((PreparedStatement) statement).setString(1, deviceIPAddress);

		} else {
			statement = con
					.prepareStatement("SELECT endpoint.endpointId, endpoint.name, endpoint.bandwidth, endpoint.domain, endpoint.interface, endpoint.description FROM endpoint ORDER BY endpointId");

		}
		rs = ((PreparedStatement) statement).executeQuery();
		if (rs.isBeforeFirst()) {
			while (rs.next()) {
				final EndpointType tna = new EndpointType();
				// TODO: clean up
				tna.setDescription(rs.getString("description"));
				tna.setName(rs.getString("name"));
				tna.setEndpointId(rs.getString("endpointId"));
				tna.setDomainId(rs.getString("domain"));
				tna.setBandwidth(Integer.valueOf(rs.getInt("bandwidth")));
				tna.setInterface(EndpointInterfaceType.fromValue((rs
						.getString("interface").toLowerCase())));
				tnaList.add(tna);
			}
			rs.close();
			statement.close();
		}
		return tnaList;
	}

	/**
	 * @param pathId
	 *            of path
	 * @return Path Information
	 * @throws SQLException
	 * @throws EndpointNotFoundFaultException
	 */
	public static LspInformation getPathInformation(final int pathId)
			throws SQLException, EndpointNotFoundFaultException {
		LspInformation result = null;
		final Connection con = DbConnectionManager.getConnection();
		PreparedStatement pstm = null;
		ResultSet rs = null;
		pstm = con
				.prepareStatement("SELECT vPathIndex, pathDescriptor, srcTNA, destTNA, bandwidth FROM path WHERE pathId = ?");
		pstm.setInt(1, pathId);
		rs = pstm.executeQuery();

		if (rs.isBeforeFirst()) {
			rs.first();
			result = new LspInformation();
			result.setPathId(pathId);
			result.setLspIndex(rs.getInt("vPathIndex"));
			result.setLspDescriptor(rs.getString("pathDescriptor"));
			result.setSourceDevice(getDeviceInformation(rs.getString("srcTNA")));
			result.setDestinationDevice(getDeviceInformation(rs
					.getString("destTNA")));
			result.setBandwidth(rs.getInt("bandwidth"));
		}
		rs.close();
		pstm.close();
		con.close();
		return result;
	}

	/**
	 * @param path
	 * @param vPathIndex
	 * @return
	 * @throws UnexpectedFaultException
	 */
	public static int insertLog(final String message)
			throws UnexpectedFaultException {
		try {
			Connection con = DbConnectionManager.getConnection();
			PreparedStatement statement = con
					.prepareStatement("INSERT INTO DbMaintenanceJobLog (message) VALUES ( ? )");
			statement.setString(1, message);
			statement.executeUpdate();
			statement.close();
			con.close();
		} catch (final SQLException se) {
			throw new UnexpectedFaultException(se);
		}
		return 1;
	}

	/**
	 * Constructor.
	 */
	private DbManager() {
		super();
		if (null == DbManager.connectionManager) {
			DbManager.connectionManager = DbConnectionManager.getInstance();
		}
	}
}
