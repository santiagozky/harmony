/**
 * Request Handler for the GMPLS-Webservice.
 */
package org.opennaas.extensions.gmpls.handler;

import org.apache.log4j.Logger;

import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.CreatePathResponseType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.CreatePathType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.GetEndpointDiscoveryResponseType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.GetEndpointDiscoveryType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.GetPathDiscoveryResponseType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.GetPathDiscoveryType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.GetPathStatusResponseType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.GetPathStatusType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.TerminatePathResponseType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.TerminatePathType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.exceptions.BandwidthFaultException;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.exceptions.CreatePathFaultException;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.exceptions.DestinationTNAFaultException;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.exceptions.PathNotFoundFaultException;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.exceptions.SourceTNAFaultException;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.utils.JaxbSerializer;
import org.opennaas.extensions.idb.serviceinterface.RequestHandler;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;
import org.opennaas.core.utils.PhLogger;

/**
 * @author Daniel Beer (daniel.beer@iais.fraunhofer.de), Stephan
 *         Wagner(stephan.wagner@iais.fraunhofer.de)
 */
public final class GmplsRequestHandler extends RequestHandler {
	/*
	 * Instance Variables
	 * =========================================================================
	 */
	private static Logger logger = PhLogger
			.getLogger(GmplsRequestHandler.class);

	/** Singleton instance. */
	private static GmplsRequestHandler selfInstance = null;

	/*
	 * Singleton Stuff
	 * =========================================================================
	 */

	/**
	 * Instance getter.
	 * 
	 * @return Singleton Instance
	 */
	public static GmplsRequestHandler getInstance() {
		if (GmplsRequestHandler.selfInstance == null) {
			GmplsRequestHandler.selfInstance = new GmplsRequestHandler();
		}
		return GmplsRequestHandler.selfInstance;
	}

	/** Private constructor: Singleton. */
	private GmplsRequestHandler() {
		this.setSerializer(JaxbSerializer.getInstance());
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
	 * CreatePath service.
	 * 
	 * @param element
	 *            the request
	 * @return the response
	 * @throws BandwidthFaultException
	 * @throws CreatePathFaultException
	 * @throws DestinationTNAFaultException
	 * @throws SourceTNAFaultException
	 * @throws UnexpectedFaultException
	 */
	public CreatePathResponseType createPath(final CreatePathType element)
			throws UnexpectedFaultException, SourceTNAFaultException,
			DestinationTNAFaultException, CreatePathFaultException,
			BandwidthFaultException {
		return GmplsCreationHandler.getInstance().createPath(element);
	}

	/**
	 * GetTopology service.
	 * 
	 * @param element
	 *            the request
	 * @return the response
	 * @throws UnexpectedFaultException
	 */
	public GetEndpointDiscoveryResponseType getEndpointDiscovery(
			final GetEndpointDiscoveryType element)
			throws UnexpectedFaultException {
		return GmplsTopologyHandler.getInstance().getEndpointDiscovery(element);
	}

	/**
	 * GetPathDiscovery service.
	 * 
	 * @param element
	 *            the request
	 * @return the response
	 * @throws UnexpectedFaultException
	 */
	public GetPathDiscoveryResponseType getPathDiscovery(
			final GetPathDiscoveryType element) throws UnexpectedFaultException {
		return GmplsTopologyHandler.getInstance().getPathDiscovery(element);
	}

	/**
	 * GetPathStatus service.
	 * 
	 * @param element
	 *            the request
	 * @return the response
	 * @throws PathNotFoundFaultException
	 * @throws eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.exceptions.PathNotFoundFaultException
	 */
	public GetPathStatusResponseType getPathStatus(
			final GetPathStatusType element) throws PathNotFoundFaultException {
		return GmplsTopologyHandler.getInstance().getPathStatus(element);
	}

	/**
	 * TerminatePath service.
	 * 
	 * @param element
	 *            the request
	 * @return the response
	 * @throws PathNotFoundFaultException
	 * @throws UnexpectedFaultException
	 */
	public TerminatePathResponseType terminatePath(
			final TerminatePathType element) throws UnexpectedFaultException,
			PathNotFoundFaultException {
		return GmplsCreationHandler.getInstance().terminatePath(element);
	}

}
