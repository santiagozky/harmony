/**
 * Sub-Handler for Topology related stuff of GMPLS webservice.
 */
package org.opennaas.extensions.gmpls.handler;

import org.apache.log4j.Logger;

import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.GetEndpointDiscoveryResponseType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.GetEndpointDiscoveryType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.GetPathDiscoveryResponseType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.GetPathDiscoveryType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.GetPathStatusResponseType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.GetPathStatusType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.PathIdentifierType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.PathType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.exceptions.PathNotFoundFaultException;
import org.opennaas.extensions.gmpls.utils.configuration_modules.ConfigurationManager;
import org.opennaas.extensions.gmpls.utils.database.DbManager;
import org.opennaas.extensions.gmpls.utils.database.orm.LspInformation;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;
import org.opennaas.core.utils.PhLogger;

/**
 * @author Daniel Beer (daniel.beer@iais.fraunhofer.de), Stephan
 *         Wagner(stephan.wagner@iais.fraunhofer.de)
 */
public final class GmplsTopologyHandler {

	/** Singleton Instance. */
	private static GmplsTopologyHandler selfInstance = null;

	private static Logger logger = PhLogger
			.getLogger(GmplsTopologyHandler.class);

	/**
	 * Instance getter.
	 * 
	 * @return Singleton Instance
	 */
	public static GmplsTopologyHandler getInstance() {
		if (GmplsTopologyHandler.selfInstance == null) {
			GmplsTopologyHandler.selfInstance = new GmplsTopologyHandler();
		}
		return GmplsTopologyHandler.selfInstance;
	}

	/** Private constructor: Singleton. */
	private GmplsTopologyHandler() {
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
	 * Retrieve TNA information.
	 * 
	 * @param getEndpointDiscoveryType
	 *            web service parameter
	 * @return GetTNADiscoveryResponseType
	 * @throws UnexpectedFaultException
	 *             in case of error
	 */
	@SuppressWarnings("unused")
	public GetEndpointDiscoveryResponseType getEndpointDiscovery(
			final GetEndpointDiscoveryType getEndpointDiscoveryType)
			throws UnexpectedFaultException {
		final GetEndpointDiscoveryResponseType response = new GetEndpointDiscoveryResponseType();
		try {
			response.getEndpoint().addAll(DbManager.getEndpoints(null));
		} catch (final Exception ex) {
			throw new UnexpectedFaultException(ex);
		}
		return response;
	}

	/**
	 * GetPathDiscovery service implementation.
	 * 
	 * @param element
	 *            the request
	 * @return the response
	 * @throws UnexpectedFaultException
	 */
	@SuppressWarnings("unused")
	public GetPathDiscoveryResponseType getPathDiscovery(
			final GetPathDiscoveryType element) throws UnexpectedFaultException {
		final GetPathDiscoveryResponseType response = new GetPathDiscoveryResponseType();
		for (final Integer i : DbManager.getAllPathIds()) {
			final PathIdentifierType pathId = new PathIdentifierType();
			pathId.setPathIdentifier(i.intValue());
			response.getPathIdentifierList().add(pathId);
		}

		return response;

	}

	/**
	 * GetPathStatus Service implementation.
	 * 
	 * @param getPathStatusType
	 *            the request
	 * @return the response
	 * @throws PathNotFoundFaultException
	 * @throws PathNotFoundFaultException
	 */
	public GetPathStatusResponseType getPathStatus(
			final GetPathStatusType getPathStatusType)
			throws PathNotFoundFaultException {
		final GetPathStatusResponseType response = new GetPathStatusResponseType();
		final PathType path = new PathType();
		LspInformation lspInformation;
		try {
			lspInformation = DbManager.getPathInformation(getPathStatusType
					.getPathIdentifier().getPathIdentifier());
			if (!checkPathStatus(lspInformation)) {
				logger.debug("Path does not exist anymore");
				throw new PathNotFoundFaultException(
						"Path does not exist anymore");
			}
			path.setSourceTNA(lspInformation.getSourceDevice().getTnaAddress());
			path.setDestinationTNA(lspInformation.getDestinationDevice()
					.getTnaAddress());
			path.setBandwidth(lspInformation.getBandwidth());
		} catch (final Exception e) {
			throw new PathNotFoundFaultException(e);
		}
		response.setPath(path);
		return response;

	}

	public boolean checkPathStatus(LspInformation lspInformation)
			throws UnexpectedFaultException, PathNotFoundFaultException {
		final ConfigurationManager configManager = new ConfigurationManager(
				lspInformation.getSourceDevice().getModule());
		if (!configManager.getStatus(lspInformation)) {
			DbManager.insertLog("Cleaned lsp with pathId "
					+ lspInformation.getPathId() + " and description: "
					+ lspInformation.getLspDescriptor());
			logger.debug("Cleaned lsp with pathId "
					+ lspInformation.getPathId() + " and description: "
					+ lspInformation.getLspDescriptor());
			DbManager.deletePath(lspInformation.getPathId());
			try {
				configManager.terminatePath(lspInformation);
			} catch (final Exception ex) {
				DbManager
						.insertLog("Could not be deleted on router-> might be already deleted.");
				logger.debug("Could not be deleted on router-> might be already deleted.");

				logger.error(ex.getMessage(), ex);
			}
			return false;
		}
		return true;
	}

}
