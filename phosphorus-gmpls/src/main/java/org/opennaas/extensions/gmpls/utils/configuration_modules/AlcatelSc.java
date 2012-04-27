package org.opennaas.extensions.gmpls.utils.configuration_modules;

import org.apache.log4j.Logger;

import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.PathType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.exceptions.DestinationTNAFaultException;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.exceptions.SourceTNAFaultException;
import org.opennaas.extensions.gmpls.utils.configuration_modules.snmp.SNMPConfig;
import org.opennaas.extensions.gmpls.utils.configuration_modules.snmp.exceptions.SnmpException;
import org.opennaas.extensions.gmpls.utils.database.orm.DeviceInformation;
import org.opennaas.extensions.gmpls.utils.database.orm.LspInformation;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;
import org.opennaas.core.utils.PhLogger;

/**
 * @author Stephan Wagner(stephan.wagner@iais.fraunhofer.de), Daniel Beer
 *         (daniel.beer@iais.fraunhofer.de)
 */
public class AlcatelSc implements IConfigurationModule {
	private static Logger logger = PhLogger.getLogger(AlcatelSc.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.opennaas.extensions.gmpls.utils.configuration_modules.IConfigurationModule
	 * #
	 * getStatus(org.opennaas.extensions.gmpls.utils.database.orm.LspInformation)
	 */
	public final boolean getStatus(final LspInformation lspInformation)
			throws UnexpectedFaultException {

		try {
			SNMPConfig snmpConfig = new SNMPConfig(lspInformation
					.getSourceDevice().getIpAddress(), "private");

			return ("up".equalsIgnoreCase(snmpConfig.pathStatusAlcatel(Integer
					.toString(lspInformation.getLspIndex()))));
		} catch (SnmpException e) {
			e.printStackTrace();
			throw new UnexpectedFaultException(e);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.opennaas.extensions.gmpls.utils.configuration_modules.IConfigurationModule
	 * #
	 * terminatePath(org.opennaas.extensions.gmpls.utils.database.orm.LspInformation
	 * )
	 */
	public final boolean terminatePath(final LspInformation lspInformation)
			throws UnexpectedFaultException {
		try {
			SNMPConfig snmpConfig = new SNMPConfig(lspInformation
					.getSourceDevice().getIpAddress(), "private");

			snmpConfig.terminateLSPAlcatel(Integer.toString(lspInformation
					.getLspIndex()));
		} catch (SnmpException e) {
			e.printStackTrace();
			throw new UnexpectedFaultException(e);
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.opennaas.extensions.gmpls.utils.configuration_modules.IConfigurationModule
	 * #createPath (org.opennaas.extensions.idb.serviceinterface.databinding.
	 * jaxb.gmpls.PathType)
	 */
	public final int createPath(final PathType path,
			final DeviceInformation sourceDev,
			final DeviceInformation destinationDev)
			throws SourceTNAFaultException, DestinationTNAFaultException,
			UnexpectedFaultException {

		try {

			String sourceTNA = path.getSourceTNA();
			String destinationTNA = path.getDestinationTNA();

			String sourceIPAddr = sourceDev.getIpAddress();
			String destinationIPAddr = destinationDev.getIpAddress();

			logger.debug("DEBUG: SNMP1");

			SNMPConfig snmpConfig = new SNMPConfig(sourceIPAddr, "private");
			// check if the TNAs are occupied and exist in the
			// TunnelEntry table.
			String tunnelEntries = null;
			tunnelEntries = snmpConfig
					.walkSnmpTableAlcatel("tmnxGmplsTunnelTable");
			logger.debug("DEBUG: SNMP2");
			if (tunnelEntries != null) {
				logger.debug("DEBUG: SNMP3");
				if (tunnelEntries.indexOf(sourceTNA) >= 0) {
					throw new SourceTNAFaultException(
							"The source TNA is already occupied at the source");
				}
				if (tunnelEntries.indexOf(destinationTNA) >= 0) {
					throw new DestinationTNAFaultException(
							"The destination TNA is already occupied at the source");
				}
			}
			// check on the destination device also
			logger.debug("DEBUG: SNMP4");
			snmpConfig = new SNMPConfig(destinationIPAddr, "private");
			tunnelEntries = snmpConfig
					.walkSnmpTableAlcatel("tmnxGmplsTunnelTable");
			logger.debug("DEBUG: SNMP5");
			if (tunnelEntries != null) {
				logger.debug("DEBUG: SNMP6");
				if (tunnelEntries.indexOf(sourceTNA) >= 0) {
					throw new SourceTNAFaultException(
							"The source TNA is already occupied at the destination");
				}
				if (tunnelEntries.indexOf(destinationTNA) >= 0) {
					throw new DestinationTNAFaultException(
							"The destination TNA is already occupied at the destination");
				}
			}
			// due to Walking the resources has been released from
			// the target so new SNMPConfig
			logger.debug("DEBUG: SNMP7");
			snmpConfig = new SNMPConfig(sourceIPAddr, "private");
			logger.debug("DEBUG: SNMP8");
			return snmpConfig.lspSetupAlcatel(path.getBandwidth(),
					path.getSourceTNA(), path.getDestinationTNA());
		} catch (SnmpException e) {
			e.printStackTrace();
			throw new UnexpectedFaultException(e);
		}

	}
}
