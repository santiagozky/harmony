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

package org.opennaas.core.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.Priority;
import org.apache.log4j.WriterAppender;
import org.apache.log4j.xml.DOMConfigurator;

import org.opennaas.core.utils.logging.PropertyWrapper;
import org.opennaas.core.utils.logging.PhosphorusGPL;

/** NspLogger. */
public class PhLogger extends Logger {

	private static boolean infoGPL = false;

	private static boolean debugMode = false;
	private static String prefix = "harmony";
	private static boolean configured = false;
	private static HashMap<String, Logger> existingLoggers = new HashMap<String, Logger>();

	private static Logger log;

	/**
	 * constructor.
	 * 
	 * @param name
	 *            LoggerName
	 */
	protected PhLogger(final String name) {
		super(name);
	}

	private static void loggerDebugg(String message) {
		if (debugMode) {
			System.out.println(message);
		}
	}

	public static void enableSimpleLogger() {

		loggerDebugg("enabling simple Logger");

		Properties properties;
		try {
			properties = Config.getProperties("logging");
		} catch (IOException e) {
			loggerDebugg("could not read file, disabling simpleLogging\n" + e);
			return;
		}

		if (null == properties)
			return;

		HashMap<String, PropertyWrapper> map = new HashMap<String, PropertyWrapper>();

		List<String> keyValues = new ArrayList<String>();

		for (Enumeration tempKeyValue = properties.propertyNames(); tempKeyValue
				.hasMoreElements();) {
			keyValues.add(tempKeyValue.nextElement().toString());
		}

		for (String tempKey : keyValues) {

			String loggerName = tempKey.split("\\.")[0];

			String tempValue = properties.getProperty(tempKey);

			PropertyWrapper tempWrapper;

			if (!map.containsKey(loggerName)) {
				tempWrapper = new PropertyWrapper(tempKey);
				map.put(loggerName, tempWrapper);
			} else {
				tempWrapper = map.get(loggerName);
			}
			if ("level".equalsIgnoreCase(tempKey.split("\\.")[1])) {
				tempWrapper.setLevel(tempValue);
			} else if ("out".equalsIgnoreCase(tempKey.split("\\.")[1])) {
				tempWrapper.setOutput(tempValue);
			} else {
				System.err.println("undefined value: " + tempKey);
			}
		}
		for (String s : map.keySet()) {
			loggerDebugg("new simple-LoggerConfiguration: ");
			loggerDebugg("Loggername: " + s);
			loggerDebugg("Level for " + s + ": " + map.get(s).getLevel());
			loggerDebugg("Output for " + s + ": " + map.get(s).getOutput());
			if (!map.get(s).getOutput().equalsIgnoreCase("CONSOLE")) {
				addFileAppenderToRoot(map.get(s).getOutput(), s,
						Level.toLevel(map.get(s).getLevel()));
			} else {
				addConsoleAppenderToRoot(map.get(s).getOutput(), s,
						Level.toLevel(map.get(s).getLevel()));
			}
		}
	}

	/**
	 * create logging directory.
	 */
	private static void loadProperty() {

		debugMode = "true".equalsIgnoreCase(Config.getString("utils",
				"debug.mode"));
		loggerDebugg("[PhLogger-debug-output] debug-mode enabled, disable under utils.properties for less output");

		prefix = Config.getString("utils", "standard.prefix");
		loggerDebugg("[PhLogger-debug-output] prefix set to: " + prefix);

		URL url = PhLogger.class.getClassLoader().getResource(
				"properties" + File.separator + "log4j.xml");

		if (null == url) {
			loggerDebugg("[PhLogger-debug-output] log4j.xml not found. configurating with log4j.common.xml");
			url = PhLogger.class.getClassLoader().getResource(
					"properties" + File.separator + "log4j.common.xml");

		}

		if (null == url) {
			System.err
					.println("[PhLogger-debug-output] Configuration file not found !!!");
			System.err
					.println("[PhLogger-debug-output] Please fix this immediately !!!");
			System.err
					.println("[PhLogger-debug-output] Add log4j.xml, log4.dtd to resources/properties.");
			return;
		}

		// writes debugg output in nohup.out
		loggerDebugg("[PhLogger-debug-output] configurating with file: "
				+ url.getFile());
		DOMConfigurator.configure(url);

		if (debugMode) {
			log = Logger.getLogger("PhLogger");
			log.error("[PhLogger-debug-output] on high error-lvl to ensure to get logged, configuration-file: "
					+ url.getFile());
			log.error("[PhLogger-debug-output]  debug-mode enabled, disable under utils.properties for less output");
		}

		enableSimpleLogger();
	}

	/**
	 * 
	 * @return phosphorus logging-instance
	 */
	public static Logger getLogger() {
		if (!configured) {
			configured = true;
			loadProperty();
		}
		Logger logger = LogManager.getLogger("org.opennaas.extensions.idb");

		/* If for print logs in configuration files */
		// if (!infoGPL) {
		// logger.info(PhosphorusGPL.getGplLicense());
		// infoGPL=true;
		// }

		return logger;
	}

	/**
	 * @param name
	 *            Logger by name
	 * @return Logger
	 */
	public static Logger getLogger(final String name) {
		if (!configured) {
			configured = true;
			loadProperty();
		}
		Logger logger = LogManager.getLogger(name);

		/* If for print logs in configuration files */
		// if (!infoGPL) {
		// logger.info(PhosphorusGPL.getGplLicense());
		// infoGPL=true;
		// }

		return logger;
	}

	/**
	 * @param clazz
	 *            Logger by class
	 * @return Logger
	 */
	public static Logger getLogger(final Class<?> clazz) {
		if (!configured) {
			configured = true;
			loadProperty();
		}
		Logger logger = LogManager.getLogger(clazz.getName());

		/* If for print logs in configuration files */
		// if (!infoGPL) {
		// logger.info(PhosphorusGPL.getGplLicense());
		// infoGPL=true;
		// }

		return logger;
	}

	/**
	 * @return RootLogger
	 */
	public static Logger getRootLogger() {
		if (!configured) {
			configured = true;
			loadProperty();
		}
		return LogManager.getRootLogger();
	}

	private static WriterAppender addAppenderToRoot(WriterAppender appender,
			PatternLayout pl, String LoggerName, Priority threshold,
			Logger logger) {
		appender.setLayout(pl);
		appender.setName(LoggerName);
		appender.setThreshold(threshold);
		logger.addAppender(appender);
		return appender;
	}

	private static void addConsoleAppenderToRoot(String FileName,
			String Loggername, Priority threshold) {
		Logger log = Logger.getLogger(Loggername);
		if (null != log.getAppender(Loggername)) {
			return;
		}

		PatternLayout layout = new PatternLayout();
		layout.setConversionPattern("%-5p - [%t] - %m%n");

		ConsoleAppender ca = (ConsoleAppender) addAppenderToRoot(
				new ConsoleAppender(), layout, Loggername, threshold,
				Logger.getRootLogger());
		ca.activateOptions();

		return;
	}

	private static void addFileAppenderToRoot(String FileName,
			String Loggername, Priority threshold) {
		addFileAppenderToLogger(FileName, Loggername, threshold,
				Logger.getRootLogger());
	}

	private static void addFileAppenderToLogger(String FileName,
			String Loggername, Priority threshold, Logger logger) {
		Logger log = Logger.getLogger(Loggername);
		if (null != log.getAppender(Loggername)) {
			return;
		}

		PatternLayout layout = new PatternLayout();
		layout.setConversionPattern("%-5p %d{ISO8601} [%t] - %m%n");

		FileAppender fa = (FileAppender) addAppenderToRoot(new FileAppender(),
				layout, Loggername, threshold, logger);

		fa.setFile("log" + File.separator + FileName);
		fa.activateOptions();
		fa.setAppend(true);

		return;
	}

	public static Logger getSeparateLogger(String name, Priority threshold,
			String fileNamePrefix) {

		final String id = fileNamePrefix + name;
		Logger log = PhLogger.existingLoggers.get(id);

		if (null == log) {
			log = Logger.getLogger(id);
			PhLogger.existingLoggers.put(id, log);
		} else {
			return log;
		}

		if (null != log.getAppender(id)) {
			return log;
		}

		log.setAdditivity(false);

		FileAppender fa = new FileAppender();

		PatternLayout bla = new PatternLayout();
		bla.setConversionPattern("%-5p %d{ISO8601} [%t] - %m%n");

		fa.setAppend(true);

		fa.setFile("log" + File.separator + fileNamePrefix + "." + name
				+ ".log");
		fa.setLayout(bla);
		fa.setName(name);
		fa.setThreshold(threshold);
		fa.activateOptions();

		log.addAppender(fa);
		log.getEffectiveLevel();
		log.setLevel((Level) threshold);

		return log;
	}

	/**
	 * default logger debugg
	 * 
	 * @param name
	 * @return
	 */
	public static Logger getSeparateLogger(String name) {

		return getSeparateLogger(name, Level.ALL, prefix);
	}

	public static Logger getSeparateLogger(String name, String fileName) {
		return getSeparateLogger(name, Level.ALL, fileName);
	}

	public static Logger getSeparateLogger(String name, Priority threshold) {
		return getSeparateLogger(name, threshold, prefix);
	}

}
