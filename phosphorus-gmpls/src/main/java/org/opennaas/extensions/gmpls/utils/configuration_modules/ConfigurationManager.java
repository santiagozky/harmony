package org.opennaas.extensions.gmpls.utils.configuration_modules;

import org.apache.log4j.Logger;

import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.PathType;
import org.opennaas.extensions.gmpls.utils.database.orm.DeviceInformation;
import org.opennaas.extensions.gmpls.utils.database.orm.LspInformation;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;

import org.opennaas.core.utils.PhLogger;

/**
 * @author Stephan Wagner(stephan.wagner@iais.fraunhofer.de), Daniel Beer
 *         (daniel.beer@iais.fraunhofer.de)
 */
public class ConfigurationManager implements IConfigurationModule {
	private static Logger logger = PhLogger.getLogger();
	/** */
	private String moduleName = null;

	public ConfigurationManager(final String moduleName) {
		// TODO dynamics
		this.moduleName = moduleName;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.opennaas.extensions.gmpls.utils.configuration_modules.IConfigurationModule
	 * #createPath (org.opennaas.extensions.idb.serviceinterface.databinding.
	 * jaxb.PathType,
	 * org.opennaas.extensions.gmpls.utils.database.orm.DeviceInformation,
	 * org.opennaas.extensions.gmpls.utils.database.orm.DeviceInformation)
	 */
	public final int createPath(final PathType path,
			final DeviceInformation sourceDev,
			final DeviceInformation destinationDev)
			throws UnexpectedFaultException {

		// DynamicMethods dynMeth = DynamicMethods.getInstance();
		// Method createPath =
		// dynMeth.getMethod(sourceDevice.getModule(),
		// IConfigurationModule.createPath);
		// pathId.setPathIdentifier(
		// ((Integer)
		// createPath.invoke(dynMeth.getInstance(sourceDevice.getModule()),
		// createPathType.getPath())).intValue());
		try {
			if ("AlcatelSpc".equalsIgnoreCase(this.moduleName)) {
				return new AlcatelSpc().createPath(path, sourceDev,
						destinationDev);
			} else if ("AlcatelSc".equalsIgnoreCase(this.moduleName)) {
				return new AlcatelSc().createPath(path, sourceDev,
						destinationDev);
			} else if ("Dummy".equalsIgnoreCase(this.moduleName)) {
				return new DummyModule().createPath(path, sourceDev,
						destinationDev);
			} else {
				logger.fatal("wrong module");
				throw new UnexpectedFaultException("wrong module");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new UnexpectedFaultException(ex);
		}
	}

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

		// find out status
		// TODO: implement dynamics
		// try {
		//
		// DynamicMethods dynMeth = DynamicMethods.getInstance();
		// Method getStatus =
		// dynMeth.getMethod(lspInformation.getSourceDevice()
		// .getModule(), "getStatus");
		//
		// if (false == ((Boolean) getStatus.invoke(dynMeth
		// .getInstance(lspInformation.getSourceDevice().getModule()),
		// lspInformation)).booleanValue()) {
		// throw new PathNotFoundFaultException();
		// }
		//
		// } catch (IllegalAccessException e) {
		// throw new UnexpectedFaultException(e);
		// } catch (IllegalArgumentException e) {
		// throw new UnexpectedFaultException(e);
		// } catch (InvocationTargetException e) {
		// throw new UnexpectedFaultException(e);
		// }

		try {
			if ("AlcatelSpc".equalsIgnoreCase(this.moduleName)) {
				return new AlcatelSpc().getStatus(lspInformation);
			} else if ("AlcatelSc".equalsIgnoreCase(this.moduleName)) {
				return new AlcatelSc().getStatus(lspInformation);
			} else if ("Dummy".equalsIgnoreCase(this.moduleName)) {
				return new DummyModule().getStatus(lspInformation);
			} else {
				logger.fatal("wrong module");
				throw new UnexpectedFaultException("wrong module");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new UnexpectedFaultException(ex);
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
			if ("AlcatelSpc".equalsIgnoreCase(this.moduleName)) {
				return new AlcatelSpc().terminatePath(lspInformation);
			} else if ("AlcatelSc".equalsIgnoreCase(this.moduleName)) {
				return new AlcatelSc().terminatePath(lspInformation);
			} else if ("Dummy".equalsIgnoreCase(this.moduleName)) {
				return new DummyModule().terminatePath(lspInformation);
			} else {
				logger.fatal("wrong module");
				throw new UnexpectedFaultException("wrong module");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new UnexpectedFaultException(ex);
		}
	}

}
