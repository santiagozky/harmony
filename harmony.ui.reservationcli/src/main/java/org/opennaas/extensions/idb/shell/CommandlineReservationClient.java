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


package org.opennaas.extensions.idb.shell;

import java.net.UnknownHostException;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;
import org.apache.muse.ws.addressing.soap.SoapFault;
import org.apache.xerces.impl.dv.util.Base64;
import org.xbill.DNS.TextParseException;

import org.opennaas.extensions.idb.da.dummy.webservice.ReservationWS;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelReservationResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CancelReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CreateReservationResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CreateReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DomainStatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatusResponseType;
import org.opennaas.extensions.idb.serviceinterface.reservation.SimpleReservationClient;
import org.opennaas.core.utils.Config;
import org.opennaas.core.utils.Helpers;
import org.opennaas.core.utils.PhLogger;
import org.opennaas.core.utils.TNAHelper;
import org.opennaas.core.utils.Tuple;

/**
 * 
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 * @version $Id: CommandlineReservationClient.java 2695 2008-03-31 22:52:36Z
 *          willner@cs.uni-bonn.de $
 */
public class CommandlineReservationClient {
	private int minBandwidth;
	private int maxBandwidth;
	private int delay;
	private int duration;
	private String source;
	private String target;
	private SimpleReservationClient client;
	private static Options options;
	private static CommandLineParser parser;
	private static CommandlineReservationClient cli;
	private static CommandLine line;
	private static String action;
	private static String reservationId;
	private final Logger logger;
	private String epr;
	private int dataAmount;
	private XMLGregorianCalendar startTime;
	private XMLGregorianCalendar deadline;
	private String gri = null;
	private String trunk = null;

	private static String shortLicense = '\n'
			+ "**************************************************************************"
			+ '\n'
			+ "*  Harmony Reservation Command-line Client"
			+ '\n'
			+ "*  Copyright (C) 2006-2009 Phosphorus WP1 partners. Phosphorus Consortium."
			+ '\n'
			+ "*  http://ist-phosphorus.eu/"
			+ '\n'
			+ "**************************************************************************"
			+ '\n';
	/* Variables manage the different variables */
	private String userId;
	private boolean isEncrypt = true;
	private boolean isSigned = true;
	private String certificate;
	private String password;
	private String token = null;

	/**
	 * Main function.
	 * 
	 * @param args
	 *            The parameter.
	 */
	public static void main(final String[] args) {
		cli = new CommandlineReservationClient();
		parser = new PosixParser();
		options = new Options();
		System.out.println(shortLicense);
		generateParameter();
		analyzeArguments(args);
		if ("help".equals(action)) {
			new HelpFormatter().printHelp("java "
					+ CommandlineReservationClient.class.getSimpleName(),
					options);
		} else {

			runArguments(args);
		}
	}

	private static void analyzeArguments(String[] args) {
		action = "";
		try {
			line = parser.parse(options, args);
		} catch (final ParseException exception) {
			System.err.println("Couldn't parse parameter: "
					+ exception.getMessage());
		}

		try {
			if (line.hasOption("h") || (action.length() == 0)) {
				action = "help";
			}
			if (line.hasOption("d")) {
				cli.setDelay(Integer.parseInt(line.getOptionValue("d").trim()));
			}

			if (line.hasOption("lb")) {
				cli.setMinBandwidth(Integer.parseInt(line.getOptionValue("lb")
						.trim()));
			}
			if (line.hasOption("hb")) {
				cli.setMaxBandwidth(Integer.parseInt(line.getOptionValue("hb")
						.trim()));
			}
			if (line.hasOption("s")) {
				cli.setSource(line.getOptionValue("s").trim());
			}
			if (line.hasOption("t")) {
				cli.setTarget(line.getOptionValue("t").trim());
			}
			if (line.hasOption("r")) {
				cli.setDuration(Integer.parseInt(line.getOptionValue("r")
						.trim()));
			}
			if (line.hasOption("e")) {
				cli.setEpr(line.getOptionValue("e").trim());
			}
			if (line.hasOption("a")) {
				action = line.getOptionValue("a").trim();
			}
			if (line.hasOption("i")) {
				reservationId = line.getOptionValue("i").trim();
			}
			if (line.hasOption("m")) {
				cli.setDataAmount(Integer.parseInt(line.getOptionValue("m")
						.trim()));
			}
			if (line.hasOption("st")) {
				cli.setStartTime(Integer.parseInt(line.getOptionValue("st")
						.trim()));
			}

			if (line.hasOption("dl")) {
				cli.setDeadline(Integer.parseInt(line.getOptionValue("dl")
						.trim()));
			}

			if (line.hasOption("dl")) {
				cli.setDeadline(Integer.parseInt(line.getOptionValue("dl")
						.trim()));
			}

			if (line.hasOption("gri")) {
				cli.setGri(line.getOptionValue("gri").trim());
			}

			if (line.hasOption("trunk")) {
				cli.setTrunk(line.getOptionValue("trunk").trim());
			}

			/* The security options */
			if (line.hasOption("T")) {
				final String token = new String(Base64.decode(line
						.getOptionValue("T").trim()));

				cli.setToken(token);
			}

			cli.setEncrypt(line.hasOption("encrypt"));
			cli.setSigned(line.hasOption("sign"));

			if (line.hasOption("certificate")) {
				cli.setCertificate(line.getOptionValue("certificate").trim());
			}

			if (line.hasOption("password")) {
				cli.setPassword(line.getOptionValue("password").trim());
			}

		} catch (final RuntimeException exception) {
			System.err.println("Couldn't parse parameter: "
					+ exception.getMessage());
		}
	}

	/**
	 * Anylyse the command line parameters and run the client.
	 * 
	 * @param args
	 *            The command line parameter.
	 */
	private static void runArguments(final String[] args) {
		try {

			if (action.equalsIgnoreCase("create")) {
				reservationId = cli.createReservation();
				System.out.println("Reservation ID: " + reservationId);
			} else if (action.equalsIgnoreCase("getStatus")) {
				final String status = cli.getReservationStatus(reservationId);
				System.out.println(status);
			} else if (action.equalsIgnoreCase("cancel")) {
				final boolean success = cli.cancelReservation(reservationId);
				System.out.println("Success: " + success);
			} else if (action.equalsIgnoreCase("createMalleable")) {
				reservationId = cli.createMalleableReservation();
				System.out.println("Reservation ID: " + reservationId);
			} else {
				new HelpFormatter().printHelp("java "
						+ CommandlineReservationClient.class.getSimpleName(),
						options);
			}
		} catch (final SoapFault exception) {
			System.err
					.println("Error in Webservice: " + exception.getMessage());
		}
	}

	/**
	 * Setup the parameter.
	 */
	private static void generateParameter() {
		options.addOption(String.valueOf("e"), "epr", true,
				"The EPR. Default: "
						+ Config.getString("reservationcli", "default.epr"));
		options.addOption(String.valueOf("s"), "source", true,
				"The source TNA/IP/Name. Default: "
						+ Config.getString("reservationcli", "default.source"));
		options.addOption(String.valueOf("t"), "target", true,
				"The target TNA/IP/Name. Default: "
						+ Config.getString("reservationcli", "default.target"));
		options.addOption(String.valueOf("lb"), "minBandwidth", true,
				"The minimal bandwidth. Default: "
						+ Config.getString("reservationcli",
								"default.minBandwidth") + " mb.");
		options.addOption(String.valueOf("hb"), "maxBandwidth", true,
				"The maximal bandwidth. Default: "
						+ Config.getString("reservationcli",
								"default.maxBandwidth") + " mb.");
		options.addOption(String.valueOf("d"), "delay", true,
				"The delay. Default: "
						+ Config.getString("reservationcli", "default.delay")
						+ " ms.");
		options
				.addOption(String.valueOf("r"), "duration", true,
						"The duration. Default: "
								+ Config.getString("reservationcli",
										"default.duration") + " seconds.");
		options.addOption(String.valueOf("a"), "action", true,
				"The action: (create|getstatus|cancel|createMalleable).");
		options.addOption(String.valueOf("i"), "reservationId", true,
				"The reservation ID.");
		options.addOption(String.valueOf("h"), "help", false, "This help.");
		options.addOption(String.valueOf("m"), "dataAmount", true,
				"The dataAmount. (relevant for malleable) Default: "
						+ Config.getString("reservationcli",
								"default.dataAmount") + " MB");
		options.addOption(String.valueOf("st"), "startTime", true,
				"The startTime. (relevant for malleable) Default-offset: "
						+ Config.getString("reservationcli",
								"default.startTimeOffset") + " min");
		options.addOption(String.valueOf("dl"), "deadline", true,
				"The deadline. (relevant for malleable) Default-offset: "
						+ Config.getString("reservationcli",
								"default.deadlineOffset") + " min");

		options.addOption(String.valueOf("gri"), "gri", true,
				"Global Reservation ID");

		options.addOption(String.valueOf("trunk"), "trunk", true,
				"Any additional String value, seperated by Semicolon");

		/* security variables */
		options.addOption(String.valueOf("encrypt"), "encrypt", false,
				"encrypt");
		options.addOption(String.valueOf("certificate"), "certificate", true,
				"certificate");
		options.addOption(String.valueOf("sign"), "sign", false, "sign");
		options.addOption(String.valueOf("password"), "password", true,
				"password");

		options.addOption("T", "token", true, "Token");
		/* ----------------------------------------------------------------- */
	}

	public CommandlineReservationClient() {
		this.logger = PhLogger.getLogger();
		this.logger
				.setLevel(Config.getLevel("reservationcli", "logging.level"));
		this.setMinBandwidth(Config.getInt("reservationcli",
				"default.minBandwidth").intValue());
		this.setMaxBandwidth(Config.getInt("reservationcli",
				"default.maxBandwidth").intValue());
		this.setDelay(Config.getInt("reservationcli", "default.delay")
				.intValue());
		this.setDuration(Config.getInt("reservationcli", "default.duration")
				.intValue());
		this.setEpr(Config.getString("reservationcli", "default.epr"));
		this.setSource(Config.getString("reservationcli", "default.source"));
		this.setTarget(Config.getString("reservationcli", "default.target"));
		this.setEpr(Config.getString("reservationcli", "default.epr"));
		this.setDataAmount(Config
				.getInt("reservationcli", "default.dataAmount"));
		this.setStartTime(Config.getInt("reservationcli",
				"default.startTimeOffset"));
		this.setDeadline(Config.getInt("reservationcli",
				"default.deadlineOffset"));

		/* new variables for the AAA */
		this.setUserId(Config.getString("reservationcli", "default.userid"));
		this.setCertificate(Config.getString("reservationcli",
				"default.certificate"));
		this
				.setPassword(Config.getString("reservationcli",
						"default.password"));
		this.setEncrypt(Config.isTrue("reservationcli", "default.encrypt"));
		this.setSigned(Config.isTrue("reservationcli", "default.sign"));

		if (Config.isTrue("test", "test.callWebservice")) {
			this.client = new SimpleReservationClient(this.epr);
			this.logger.debug("Call type: Webservice @ " + this.epr);
		} else {
			this.client = new SimpleReservationClient(new ReservationWS());
			this.logger.debug("Call type: Direct.");
		}
	}

	public final boolean cancelReservation(final String reservationId)
			throws SoapFault {
		CancelReservationType request = new CancelReservationType();
		request.setReservationID(reservationId);

		if (null != this.getToken()) {
			System.out.println("Using Token: \n" + this.getToken());

			request.setToken(this.getToken());
		}

		final CancelReservationResponseType success = this.client
				.cancelReservation(request);

		return success.isSuccess();
	}

	/**
	 * @param options
	 * @throws DatatypeConfigurationException
	 * @throws SoapFault
	 */
	public final String createReservation() throws SoapFault {
		this.logger.debug("Using source: " + this.source);
		this.logger.debug("Using target: " + this.target);

		XMLGregorianCalendar startTime;
		try {
			startTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(
					new GregorianCalendar());
		} catch (DatatypeConfigurationException e) {
			throw new RuntimeException(
					"A serious configuration error was detected ...", e);
		}

		final CreateReservationType request = SimpleReservationClient
				.getCreateReservationRequest(this.minBandwidth, this.delay,
						this.duration, startTime,
						new Tuple<String, String>(
								source, target));

		if (null != this.getGri()) {
			request.setGRI(this.getGri());
		}

		if (null != this.getToken()) {
			request.setToken(this.getToken());
		}

		if (null != this.getTrunk()) {
			String[] entries = this.getTrunk().split(";");
			org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.Tuple entry = new org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.Tuple();

			for (String line : entries) {
				String[] data = line.split("=");

				if (data.length == 2) {

					entry.setKey(data[0]);
					entry.setValue(data[1]);

					request.getTrunk().add(entry);
				} else {
					System.out.println("Cannot parse: " + line
							+ ". Format must equal <key>=<val>;...");
				}
			}
		}

		final CreateReservationResponseType result = this.client
				.createReservation(request);

		if (result.getToken() != null) {
			System.out.println("Your Token: \n"
					+ Base64.encode(result.getToken().replace("\n", "")
							.getBytes()));
		}
		if (result.getGRI() != null) {
			System.out.println("Your GRI: " + result.getGRI());
		}

		return result.getReservationID();
	}

	public final String createMalleableReservation() throws SoapFault {
		this.logger.debug("Using source: " + this.source);
		this.logger.debug("Using target: " + this.target);
		this.logger.debug("Using dataAmount: " + this.dataAmount);
		this.logger.debug("Using minBw: " + this.minBandwidth);
		this.logger.debug("Using maxBw: " + this.maxBandwidth);
		this.logger.debug("Using startTime: " + this.startTime);
		this.logger.debug("Using deadline: " + this.deadline);
		final CreateReservationResponseType result = this.client
				.createMalleableReservation(this.source, this.target,
						this.dataAmount, this.startTime, this.deadline,
						this.minBandwidth, this.maxBandwidth, this.delay);
		return result.getReservationID();
	}

	public final String getReservationStatus(final String reservationId)
			throws SoapFault {
		final GetStatusResponseType status = this.client
				.getStatus(reservationId);

		final StringBuffer result = new StringBuffer(50);
		for (final GetStatusResponseType.ServiceStatus service : status
				.getServiceStatus()) {
			result.append("Status: " + service.getStatus().toString() + "\n"
					+ "Details: \n");
			for (final DomainStatusType domainStatus : service
					.getDomainStatus()) {
				result.append(" * Domain: " + domainStatus.getDomain() + " - "
						+ domainStatus.getStatus().toString() + "\n");
			}
		}

		return result.toString();
	}

	/**
	 * @param minBandwidth
	 *            the minimal bandwidth to set
	 */
	public final void setMinBandwidth(final int minBandwidth) {
		this.minBandwidth = minBandwidth;
	}

	/**
	 * @param maxBandwidth
	 *            the maximal bandwidth to set
	 */
	public final void setMaxBandwidth(final int maxBandwidth) {
		this.maxBandwidth = maxBandwidth;
	}

	/**
	 * @param delay
	 *            the delay to set
	 */
	public final void setDelay(final int delay) {
		this.delay = delay;
	}

	/**
	 * @param duration
	 *            the duration to set
	 */
	public final void setDuration(final int duration) {
		this.duration = duration;
	}

	/**
	 * @param epr
	 *            the epr to set
	 */
	public final void setEpr(final String epr) {
		this.epr = epr;
		// create new SimpleReservationClient
		if (Config.isTrue("test", "test.callWebservice")) {
			this.client = new SimpleReservationClient(this.epr);
			this.logger.debug("Call type: Webservice @ " + this.epr);
		}
	}

	/**
	 * @param dataAmount
	 *            the dataAmount to set
	 */
	public final void setDataAmount(final int dataAmount) {
		this.dataAmount = dataAmount;
	}

	/**
	 * @param offset
	 *            the offset of now till the startTime to set
	 */
	public final void setStartTime(final int offset) {
		this.startTime = Helpers.generateXMLCalendar(offset, 0);
	}

	/**
	 * @param offset
	 *            the offset of now till the deadline to set
	 */
	public final void setDeadline(final int offset) {
		this.deadline = Helpers.generateXMLCalendar(offset, 0);
	}

	/**
	 * @param source
	 *            the source to set
	 */
	public final void setSource(final String source) {
		try {
			this.source = TNAHelper.resolve(source);
		} catch (final TextParseException e) {
			this.source = source;
			System.err.println("Warning: Invalid input - " + e.getMessage());
		} catch (final UnknownHostException e) {
			this.source = source;
			System.err.println("Warning: Lookup not successfull - "
					+ e.getMessage());
		}
	}

	/**
	 * @param target
	 *            the target to set
	 */
	public final void setTarget(final String target) {
		try {
			this.target = TNAHelper.resolve(target);
		} catch (final TextParseException e) {
			this.target = target;
			System.err.println("Warning: Invalid input - " + e.getMessage());
		} catch (final UnknownHostException e) {
			this.target = target;
			System.err.println("Warning: Lookup not successfull - "
					+ e.getMessage());
		}
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public boolean isEncrypt() {
		return isEncrypt;
	}

	public void setEncrypt(boolean isEncrypt) {
		this.isEncrypt = isEncrypt;
	}

	public boolean isSigned() {
		return isSigned;
	}

	public void setSigned(boolean isSigned) {
		this.isSigned = isSigned;
	}

	public String getCertificate() {
		return certificate;
	}

	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public final String getToken() {
		return this.token;
	}

	public final void setToken(String token) {
		this.token = token;
	}

	/**
	 * @return the gri
	 */
	public final String getGri() {
		return gri;
	}

	/**
	 * @param gri
	 *            the gri to set
	 */
	public final void setGri(String gri) {
		this.gri = gri;
	}

	/**
	 * @return the trunk
	 */
	public final String getTrunk() {
		return trunk;
	}

	/**
	 * @param trunk
	 *            the trunk to set
	 */
	public final void setTrunk(String trunk) {
		this.trunk = trunk;
	}
}
