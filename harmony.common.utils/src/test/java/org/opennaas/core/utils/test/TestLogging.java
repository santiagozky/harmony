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


package org.opennaas.core.utils.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Date;

import junit.framework.Assert;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.junit.Test;

import org.opennaas.core.utils.FileHelper;
import org.opennaas.core.utils.PhLogger;

public class TestLogging {

	/** The Logger */
	private Logger logger;
	private OutputStream capturedOut;
	private PrintStream capturedPrinter;

	private static String traceFile = "log" + File.separator
			+ "harmony.trace.log";
	private static String debugFile = "log" + File.separator
			+ "harmony.debug.log";
	private static String infoFile = "log" + File.separator
			+ "harmony.info.log";
	private static String warnFile = "log" + File.separator
			+ "harmony.warn.log";
	private static String errorFile = "log" + File.separator
			+ "harmony.error.log";
	private static String fatalFile = "log" + File.separator
			+ "harmony.fatal.log";

	/**
	 * Constructor.
	 * 
	 * @throws IOException
	 */
	public TestLogging() throws IOException {
		this.logger = PhLogger.getLogger();
		this.initCaptureStream();
	}

	/**
	 * @throws IOException
	 */
	private void initCaptureStream() throws IOException {
		if (this.capturedOut != null) {
			this.capturedOut.close();
		}
		if (this.capturedPrinter != null) {
			this.capturedPrinter.close();
		}
		this.capturedOut = new ByteArrayOutputStream();
		this.capturedPrinter = new PrintStream(this.capturedOut);
	}

	private void testLevel(int level, String message, String logFile,
			String prefix) throws IOException {

		this.logger.log(Level.toLevel(level), message);
		
		Assert.assertTrue(prefix + " logfile should be created", FileHelper
				.fileExists(logFile));
		Assert.assertTrue(prefix + " message should be in the right file",
				FileHelper.readFile(logFile).contains(message));
		
		if (5000 < level) {
			String message2 = message + " should not be logged in logfile: "+logFile;
			this.logger.log(Level.toLevel(level - 5000), message2);
			Assert.assertFalse(prefix + "lower message should not be logged",
					FileHelper.readFile(logFile).contains(message2));
		}
	}

	/**
	 * Test the trace logger.
	 * 
	 * @throws IOException
	 *             If the logger file is not readable.
	 */
	@Test
	public void testTrace() throws IOException {
		final long timestamp = new Date().getTime();
		final String message = "Trace log message at: " + timestamp;
		testLevel(Level.TRACE_INT, message, traceFile, "Trace");
	}

	/**
	 * Test the debug logger.
	 * 
	 * @throws IOException
	 *             If the logger file is not readable.
	 */
	@Test
	public void testDebug() throws IOException {
		final long timestamp = new Date().getTime();
		final String message = "Debug log message at: " + timestamp;
		testLevel(Priority.DEBUG_INT, message, debugFile, "Debug");
	}

	/**
	 * Test the info logger.
	 * 
	 * @throws IOException
	 *             If the logger file is not readable.
	 */
	@Test
	public void testInfo() throws IOException {
		final long timestamp = new Date().getTime();
		final String message = "Info log message at: " + timestamp;
		testLevel(Priority.INFO_INT, message, infoFile, "Info");
	}

	/**
	 * Test the error logger.
	 * 
	 * @throws IOException
	 *             If the logger file is not readable.
	 */
	@Test
	public void testWarn() throws IOException {
		final long timestamp = new Date().getTime();
		final String message = "Warn log message at: " + timestamp;
		testLevel(Priority.WARN_INT, message, warnFile, "Warn");
	}

	/**
	 * Test the error logger.
	 * 
	 * @throws IOException
	 *             If the logger file is not readable.
	 */
	@Test
	public void testError() throws IOException {
		final long timestamp = new Date().getTime();
		final String message = "Error log message at: " + timestamp;
		testLevel(Priority.ERROR_INT, message, errorFile, "Error");
	}

	/**
	 * Test the fatal logger.
	 * 
	 * @throws IOException
	 *             If the logger file is not readable.
	 */
	@Test
	public void testFatal() throws IOException {
		final long timestamp = new Date().getTime();
		final String message = "Fatal log message at: " + timestamp;
		testLevel(Priority.FATAL_INT, message, fatalFile, "Fatal");
	}

	/**
	 * Test a special logger.
	 * 
	 * @throws IOException
	 *             If the logger file is not readable.
	 */
	@Test
	public void testSpecialPrefix() throws IOException {
		String crypticPrefixName = "prefix";
		Logger log = PhLogger.getSeparateLogger("special", crypticPrefixName);
		final long timestamp = new Date().getTime();
		final String message = "Special log message at: " + timestamp;
		System.out.println(message);
		logAll(log, message);
		Assert.assertTrue("Special logfile should be created", FileHelper
				.fileExists("log/" + crypticPrefixName + ".special.log"));
		Assert.assertTrue("Special message should be in the right file",
				FileHelper
						.readFile("log/" + crypticPrefixName + ".special.log")
						.contains(message));
	}

	/**
	 * Test a special logger.
	 * 
	 * @throws IOException
	 *             If the logger file is not readable.
	 */
	@Test
	public void testSpecial() throws IOException {
		Logger log = PhLogger.getSeparateLogger("special");
		final long timestamp = new Date().getTime();
		final String message = "Special log message at: " + timestamp;
		logAll(log, message);
		Assert.assertTrue("Special logfile should be created", FileHelper
				.fileExists("log/harmony.special.log"));
		Assert.assertTrue("Special message should be in the right file",
				FileHelper.readFile("log/harmony.special.log")
						.contains(message));
	}

	private void logAll(Logger log) {
		log.trace("logTrace");
		log.debug("logDebug");
		log.info("logInfo");
		log.warn("logWarn");
		log.error("logError");
		log.fatal("logFatal");
	}

	private void logAll(Logger log, String message) {
		log.trace(message);
		log.debug(message);
		log.info(message);
		log.warn(message);
		log.error(message);
		log.fatal(message);
	}

	@Test
	public void logAll() {
		Logger log = PhLogger.getLogger();
		logAll(log);
	}

}
