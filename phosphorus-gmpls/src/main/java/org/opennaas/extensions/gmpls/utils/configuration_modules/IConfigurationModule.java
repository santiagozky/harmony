package org.opennaas.extensions.gmpls.utils.configuration_modules;

import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.PathType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.exceptions.CreatePathFaultException;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.exceptions.DestinationTNAFaultException;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.exceptions.PathNotFoundFaultException;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.exceptions.SourceTNAFaultException;
import org.opennaas.extensions.gmpls.utils.database.orm.DeviceInformation;
import org.opennaas.extensions.gmpls.utils.database.orm.LspInformation;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;

/**
 * @author Stephan Wagner(stephan.wagner@iais.fraunhofer.de)
 */
public interface IConfigurationModule {

	/** */
	String getStatus = "getStatus";
	/** */
	String terminatePath = "terminatePath";
	/** */
	String createPath = "createPath";

	/**
	 * 
	 * @param lspInformation
	 * @return
	 * @throws PathNotFoundFaultException
	 * @throws UnexpectedFaultException
	 */
	boolean getStatus(LspInformation lspInformation)
			throws PathNotFoundFaultException, UnexpectedFaultException;

	/**
	 * 
	 * @param lspInformation
	 * @return
	 * @throws PathNotFoundFaultException
	 * @throws UnexpectedFaultException
	 */
	boolean terminatePath(LspInformation lspInformation)
			throws PathNotFoundFaultException, UnexpectedFaultException;

	/**
	 * 
	 * @param path
	 * @param sourceDev
	 * @param destinationDev
	 * @return
	 * @throws SourceTNAFaultException
	 * @throws DestinationTNAFaultException
	 * @throws UnexpectedFaultException
	 * @throws CreatePathFaultException
	 */
	int createPath(PathType path, DeviceInformation sourceDev,
			DeviceInformation destinationDev) throws SourceTNAFaultException,
			DestinationTNAFaultException, UnexpectedFaultException,
			CreatePathFaultException;
}