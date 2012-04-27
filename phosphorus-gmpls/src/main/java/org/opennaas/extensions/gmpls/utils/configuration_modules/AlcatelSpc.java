package org.opennaas.extensions.gmpls.utils.configuration_modules;

import org.apache.log4j.Logger;

import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.PathType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.exceptions.CreatePathFaultException;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.exceptions.DestinationTNAFaultException;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.exceptions.SourceTNAFaultException;
import org.opennaas.extensions.gmpls.utils.configuration_modules.gmre.GmreCliManager;
import org.opennaas.extensions.gmpls.utils.configuration_modules.gmre.exceptions.GmreConnectionException;
import org.opennaas.extensions.gmpls.utils.configuration_modules.gmre.exceptions.PathCreationException;
import org.opennaas.extensions.gmpls.utils.database.orm.DeviceInformation;
import org.opennaas.extensions.gmpls.utils.database.orm.LspInformation;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;
import org.opennaas.core.utils.PhLogger;

/**
 * @author Stephan Wagner(stephan.wagner@iais.fraunhofer.de), Daniel Beer
 *         (daniel.beer@iais.fraunhofer.de)
 */
public class AlcatelSpc implements IConfigurationModule {
	private static Logger logger = PhLogger.getLogger(AlcatelSpc.class);

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
			GmreCliManager cliManager = GmreCliManager.getInstance();
			return cliManager.isLspAdminUp(lspInformation.getSourceDevice()
					.getIpAddress(), lspInformation.getLspIndex());
		} catch (GmreConnectionException e) {
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
			GmreCliManager cliManager = GmreCliManager.getInstance();
			return cliManager.terminateLsp(lspInformation.getSourceDevice()
					.getIpAddress(), lspInformation.getLspIndex(),
					lspInformation.getLspDescriptor());
		} catch (GmreConnectionException e) {
			throw new UnexpectedFaultException(e);
		}
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
			throws UnexpectedFaultException,
			SourceTNAFaultException,
			org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.exceptions.DestinationTNAFaultException,
			CreatePathFaultException {
		try {

			GmreCliManager cliManager = GmreCliManager.getInstance();

			String sourceTNA = path.getSourceTNA();

			String sourceIPAdd = sourceDev.getIpAddress();
			String sourcePort = sourceDev.getDatabearer();

			String destinationIPAdd = destinationDev.getIpAddress();
			String destinationPort = destinationDev.getDatabearer();
			int bwid = path.getBandwidth() / 155 + 1; // map bandwidth to
			// number of VC4 containers

			if (cliManager.isTNAinLSP(destinationIPAdd, sourceTNA)) {

				throw new SourceTNAFaultException(
						"The source TNA is already occupied at destination: "
								+ destinationIPAdd + ".");
			}
			if (cliManager.isTNAinLSP(destinationIPAdd,
					destinationDev.getTnaAddress())) {

				throw new DestinationTNAFaultException(
						"The destination TNA is already occupied at destination: "
								+ destinationIPAdd + ".");
			}

			// check first if tna is occupied or not with show lsp TNA
			// srcTNA

			if (cliManager.isTNAinLSP(sourceIPAdd, sourceTNA)) {

				throw new SourceTNAFaultException(
						"The source TNA is already occupied at source: "
								+ sourceIPAdd + ".");
			} else if (cliManager.isTNAinLSP(sourceIPAdd,
					destinationDev.getTnaAddress())) {

				throw new DestinationTNAFaultException(
						"The destination TNA is already occupied at source: "
								+ sourceIPAdd + ".");
			} else {

				final String lspTraffic = "GBETHERNET";
				final String inLink = sourcePort;
				final String inLabel = bwid + "-0-0-0-0";
				final String destLink = destinationPort;
				final String destLabel = "1-0-0-0-0";
				final String protRestType = "unPROTECTED";

				return cliManager.configLSP(sourceIPAdd, inLink, inLabel,
						destLink, destLabel, lspTraffic, protRestType,
						sourceTNA, path.getDestinationTNA());
			}
		} catch (PathCreationException e) {
			throw new CreatePathFaultException(e);
		} catch (GmreConnectionException e) {
			throw new UnexpectedFaultException(e);
		}

	}
}
