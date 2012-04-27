/**
 * Sub-Handler for creation and termination of GMPLS paths
 */
package org.opennaas.extensions.gmpls.handler;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import org.opennaas.core.utils.PhLogger;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.CreatePathResponseType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.CreatePathType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.PathIdentifierType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.TerminatePathResponseType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.TerminatePathType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.exceptions.BandwidthFaultException;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.exceptions.CreatePathFaultException;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.exceptions.DestinationTNAFaultException;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.exceptions.PathNotFoundFaultException;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.exceptions.SourceTNAFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;
import org.opennaas.extensions.gmpls.utils.Notifications;
import org.opennaas.extensions.gmpls.utils.configuration_modules.ConfigurationManager;
import org.opennaas.extensions.gmpls.utils.database.DbManager;
import org.opennaas.extensions.gmpls.utils.database.orm.DeviceInformation;
import org.opennaas.extensions.gmpls.utils.database.orm.LspInformation;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.EndpointNotFoundFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.StatusType;

/**
 * @author Daniel Beer (daniel.beer@iais.fraunhofer.de), Stephan
 *         Wagner(stephan.wagner@iais.fraunhofer.de)
 */
public final class GmplsCreationHandler {

	/** Singleton Instance. */
	private static GmplsCreationHandler selfInstance = null;

	private static Logger logger = PhLogger
			.getLogger(GmplsCreationHandler.class);

	/**
	 * Instance getter.
	 * 
	 * @return Singleton Instance
	 */
	public static GmplsCreationHandler getInstance() {
		if (GmplsCreationHandler.selfInstance == null) {
			GmplsCreationHandler.selfInstance = new GmplsCreationHandler();
		}
		return GmplsCreationHandler.selfInstance;
	}

	/** Private constructor: Singleton. */
	private GmplsCreationHandler() {
		// Nothing to do here...
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

	/*
	 * Handler
	 * =========================================================================
	 */

	/**
	 * @param createPathType
	 * @return
	 * @throws UnexpectedFaultException
	 * @throws SourceTNAFaultException
	 * @throws DestinationTNAFaultException
	 * @throws BandwidthFaultException
	 * @throws CreatePathFaultException
	 */
	public CreatePathResponseType createPath(final CreatePathType createPathType)
			throws UnexpectedFaultException, SourceTNAFaultException,
			DestinationTNAFaultException, BandwidthFaultException,
			CreatePathFaultException {
		final CreatePathResponseType response = new CreatePathResponseType();
		DeviceInformation destinationDevice;
		DeviceInformation sourceDevice;
		try {
			sourceDevice = DbManager.getDeviceInformation(createPathType
					.getPath().getSourceTNA());
		} catch (final Exception ex) {
			throw new SourceTNAFaultException(ex);
		}
		try {
			destinationDevice = DbManager.getDeviceInformation(createPathType
					.getPath().getDestinationTNA());
		} catch (final Exception ex) {
			throw new DestinationTNAFaultException("Destination TNA unknown.");
		}
		if (!DbManager.checkBandwidthCapability(sourceDevice.getTnaAddress(),
				createPathType.getPath().getBandwidth())
				|| !DbManager.checkBandwidthCapability(destinationDevice
						.getTnaAddress(), createPathType.getPath()
						.getBandwidth())) {
			// source or destination is not capable of this bandwidth
			throw new BandwidthFaultException(
					"Source or destination is not capable of this bandwidth!");
		}
		if (!sourceDevice.getModule().equalsIgnoreCase(
				destinationDevice.getModule())) {
			throw new CreatePathFaultException(
					"Modules of source and destination don't match.");
		}

		// get module
		final PathIdentifierType pathId = new PathIdentifierType();
		final ConfigurationManager configManager = new ConfigurationManager(
				sourceDevice.getModule());
		pathId.setPathIdentifier(DbManager.createPath(createPathType.getPath(),
				configManager.createPath(createPathType.getPath(),
						sourceDevice, destinationDevice)));
		response.setPathIdentifier(pathId);
		try {
			Notifications.addTopic(pathId.getPathIdentifier());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.debug("addTopic" + e.getMessage());
			e.printStackTrace();
		}
		return response;

	}

	/**
	 * Termination Service implementation.
	 * 
	 * @param terminatePathType
	 *            the request
	 * @return the response
	 * @throws UnexpectedFaultException
	 *             should not happen
	 * @throws PathNotFoundFaultException
	 *             if path could not be found
	 * @throws UnexpectedFaultException
	 */
	public TerminatePathResponseType terminatePath(
			final TerminatePathType terminatePathType)
			throws PathNotFoundFaultException, UnexpectedFaultException {
		try {
			final LspInformation lspInformation = DbManager
					.getPathInformation(terminatePathType.getPathIdentifier()
							.getPathIdentifier());

			if (null != lspInformation) {
				final ConfigurationManager configManager = new ConfigurationManager(
						lspInformation.getSourceDevice().getModule());

				final boolean success = configManager
						.terminatePath(lspInformation);

				if (success) {
					if (DbManager.deletePath(terminatePathType
							.getPathIdentifier().getPathIdentifier())) {
						Notifications.publish(terminatePathType
								.getPathIdentifier().getPathIdentifier(),
								StatusType.fromValue(terminatePathType
										.getStatus().value()));
						Notifications.removeTopic(terminatePathType
								.getPathIdentifier().getPathIdentifier());
						return new TerminatePathResponseType();
					}
				}
			} else {
				throw new PathNotFoundFaultException("Path not found.");
			}
		} catch (final SQLException e) {
			throw new PathNotFoundFaultException(e);
		} catch (final EndpointNotFoundFaultException e) {
			throw new PathNotFoundFaultException(e);
		}
		throw new UnexpectedFaultException("this should not happen.");
	}

}
