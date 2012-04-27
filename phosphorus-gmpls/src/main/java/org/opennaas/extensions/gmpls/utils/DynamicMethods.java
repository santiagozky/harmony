package org.opennaas.extensions.gmpls.utils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;

/**
 * @author Stephan Wagner(stephan.wagner@iais.fraunhofer.de)
 */
public final class DynamicMethods {

	/**
	 * @author Stephan Wagner(stephan.wagner@iais.fraunhofer.de)
	 */
	public static class ModuleInformation {
		/** */
		private Class<?> moduleClass = null;
		/** */
		private Object instance = null;
		/** */
		private Map<String, Method> methods = null;

		/**
		 * @param moduleClass
		 * @param instance
		 * @param methods
		 */
		public ModuleInformation(final Class<?> moduleClass,
				final Object instance, final Map<String, Method> methods) {
			super();
			this.moduleClass = moduleClass;
			this.instance = instance;
			this.methods = methods;
		}

		/**
		 * @return the moduleClass
		 */
		public final Class<?> getModuleClass() {
			return this.moduleClass;
		}

		/**
		 * @param moduleClass
		 *            the moduleClass to set
		 */
		public final void setModuleClass(final Class<?> moduleClass) {
			this.moduleClass = moduleClass;
		}

		/**
		 * @return the instance
		 */
		public final Object getInstance() {
			return this.instance;
		}

		/**
		 * @param instance
		 *            the instance to set
		 */
		public final void setInstance(final Object instance) {
			this.instance = instance;
		}

		/**
		 * @return the methods
		 */
		public final Map<String, Method> getMethods() {
			return this.methods;
		}

		/**
		 * @param methods
		 *            the methods to set
		 */
		public final void setMethods(final Map<String, Method> methods) {
			this.methods = methods;
		}

	}

	/** */
	private static String[] moduleNames = {
			"org.opennaas.extensions.gmpls.utils.configuration_modules.AlcatelSc",
			"org.opennaas.extensions.gmpls.utils.configuration_modules.AlcatelSpc",
			"org.opennaas.extensions.gmpls.utils.configuration_modules.DummyModule" };

	/** Known methods map. */
	private static final Map<String, ModuleInformation> configurationModules = new HashMap<String, ModuleInformation>();

	/** */
	private static DynamicMethods instance;

	/**
	 * @throws UnexpectedFaultException
	 */
	private DynamicMethods() throws UnexpectedFaultException {
		super();

		try {
			for (String moduleName : moduleNames) {
				final Class<?> requestClass = Class.forName(moduleName);
				Object obj = requestClass.newInstance();
				Map<String, Method> configurationMethods = new HashMap<String, Method>();
				for (Method m : requestClass.getDeclaredMethods()) {
					configurationMethods.put(m.getName(), m);
				}
				configurationModules.put(moduleName, new ModuleInformation(
						requestClass, obj, configurationMethods));
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new UnexpectedFaultException(e);
		} catch (InstantiationException e) {
			e.printStackTrace();
			throw new UnexpectedFaultException(e);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new UnexpectedFaultException(e);
		}
	}

	/**
	 * @return
	 * @throws UnexpectedFaultException
	 */
	public static DynamicMethods getInstance() throws UnexpectedFaultException {
		if (null == instance) {
			instance = new DynamicMethods();
			return instance;
		}
		return instance;
	}

	/**
	 * @param moduleName
	 * @param method
	 * @return
	 */
	public Method getMethod(final String moduleName, final String method) {
		return configurationModules.get(moduleName).getMethods().get(method);
	}

	/**
	 * @param moduleName
	 * @return
	 */
	public Object getInstance(final String moduleName) {
		return configurationModules.get(moduleName).getInstance();
	}

	/**
	 * @param moduleName
	 * @return
	 */
	public Class<?> getClass(final String moduleName) {
		return configurationModules.get(moduleName).getModuleClass();
	}
}
