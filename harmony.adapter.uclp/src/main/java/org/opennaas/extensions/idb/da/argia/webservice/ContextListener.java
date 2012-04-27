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


package org.opennaas.extensions.idb.da.argia.webservice;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.MissingResourceException;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.globus.myproxy.MyProxy;
import org.globus.myproxy.MyProxyException;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;

import ca.inocybe.argia.stubs.reservation.GetDomainTechnologyInformation;
import ca.inocybe.argia.stubs.reservation.GetEndpoints;
import ca.inocybe.argia.stubs.reservation.GetEndpointsResponse;
import org.opennaas.extensions.idb.da.argia.handler.ReservationHandler;
import org.opennaas.extensions.idb.da.argia.implementation.AdvReservationsWrapper;
import org.opennaas.extensions.idb.da.argia.implementation.Session;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DomainInformationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DomainTechnologyType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointInterfaceType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointTechnologyType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointType;
import org.opennaas.extensions.idb.serviceinterface.topology.SimpleTopologyClient;
import org.opennaas.extensions.idb.serviceinterface.topology.registrator.AbstractTopologyRegistrator;
import org.opennaas.core.utils.Config;

/**
 * Initializer class for the UCLP Adapter
 * 
 * @author Daniel Beer (beer@cs.uni-bonn.de), Angel Sanchez
 *         (angel.sanchez@i2cat.net), Jordi Ferrer-Riera
 *         (jordi.ferrer@i2cat.net)
 */
public class ContextListener extends AbstractTopologyRegistrator {

	private static final long serialVersionUID = -2567343177983533519L;
	public static String interdomainPropertyFile = "hsi";
	/**
	 * The logger.
	 */
	private static final Logger logger = Logger
			.getLogger(ContextListener.class);
	private SimpleTopologyClient topologyWS;

	private final Hashtable<String, EndpointType> endpoints = new Hashtable<String, EndpointType>();

	private DomainTechnologyType domainTechnology;

	private final Vector<EndpointType> epList = new Vector<EndpointType>();

	@Override
	protected List<EndpointType> getEndpoints() {

		if (!this.testMode()) {

			ContextListener.logger.info("Running in normal mode");

			ContextListener.logger
					.info("Getting endpoints from UCLP server... "
							+ Config.getString("uclp", "epr.uclp.host") + " "
							+ Config.getString("uclp", "epr.uclp.port"));

			final GetEndpoints endpointsReq = new GetEndpoints();
			GetEndpointsResponse uclpResponse = new GetEndpointsResponse();

			endpointsReq.setOrganization(Session.getInstance()
					.getOrganization());

			try {
				uclpResponse = AdvReservationsWrapper
						.getEndpoints(endpointsReq);

				ContextListener.logger
						.info("Got response from UCLP server, processing answer...");

			} catch (final RemoteException e) {
				ContextListener.logger.error("Could not get endpoints");
				e.printStackTrace();
			} catch (final Exception e) {
				ContextListener.logger
						.error("Could not get endpoints - Exception");
				e.printStackTrace();
			}

			final ca.inocybe.argia.stubs.reservation.EndpointType[] epType = uclpResponse
					.getGetEndpointsResponse().getEndpoints();

			for (final ca.inocybe.argia.stubs.reservation.EndpointType element : epType) {
				final EndpointType aux = new EndpointType();

				aux.setBandwidth(element.getBandwidth());
				aux.setDescription(element.getDescription());
				aux.setDomainId(Config.getString("hsi", "domain.name"));
				aux.setEndpointId(element.getEndpointId());
				aux.setName(element.getName());

				// Added: Endpoint technology type
				EndpointTechnologyType ett = new EndpointTechnologyType();
				if (ett.isSetEndpointSupportedAdaptation())
					ett.unsetEndpointSupportedAdaptation();

				int i = 0;
				for (String supportedTechnology : element.getTechnology()
						.getEndpointSupportedAdaptation()) {
					ett.getEndpointSupportedAdaptation().add(
							supportedTechnology);

					ContextListener.logger
							.info("Endpoint supported adaptation: "
									+ ett.getEndpointSupportedAdaptation().get(
											i));
					++i;
				}
				aux.setTechnology(ett);

				if (element.get_interface().getValue().equalsIgnoreCase(
						"border")) {
					aux.setInterface(EndpointInterfaceType.BORDER);
				} else if (element.get_interface().getValue().equalsIgnoreCase(
						"user")) {
					aux.setInterface(EndpointInterfaceType.USER);
				}

				if (this.endpoints.isEmpty()) {
					this.epList.add(aux);
					this.endpoints.put(aux.getEndpointId(), aux);
				} else {
					if (this.endpoints.containsKey(aux.getEndpointId())) {
						;
					} else {
						this.endpoints.put(aux.getEndpointId(), aux);
						this.epList.add(aux);
					}
				}
			}

			ContextListener.logger.info("Answer processed!");

		}

		return this.epList;

	}

	@Override
	protected String getInterdomainPropertyFile() {
		return ContextListener.interdomainPropertyFile;
	}

	@Override
	protected void shutdown() {
		// Nothing at the moment
	}

	@Override
	protected boolean startup() {

		try {

			if (!this.testMode()) {

				ContextListener.logger.info("Getting AAA");

				// AAA
				final String login = Config.getString("uclp", "uclp.user");
				final String psw = Config.getString("uclp", "uclp.password");
				final String host = Config.getString("uclp", "epr.uclp.host");
				final String userHome = System.getProperty("user.home");

				final File localCertPath = new File(userHome + File.separator
						+ ".globus" + File.separator + "certificates");
				final String localCertName = host + "-CAcert.0";
				final String remoteCertPath = "http://" + host + "/" + host
						+ "-CAcert.0";
				final File fullCertPath = new File(localCertPath, localCertName);

				// First check to see if the CA Certificate has been downloaded
				if (!fullCertPath.exists()) {
					// create the directory
					localCertPath.mkdirs();
					try {
						OutputStream outs = null;
						URLConnection conn = null;
						InputStream in = null;
						try {
							final URL url = new URL(remoteCertPath);
							outs = new BufferedOutputStream(
									new FileOutputStream(fullCertPath));
							final Proxy proxy = new Proxy(Proxy.Type.HTTP,
									new InetSocketAddress(host, 80));
							conn = url.openConnection(proxy);
							in = conn.getInputStream();
							final byte[] buffer = new byte[1024];
							int numRead;
							long numWritten = 0;
							while ((numRead = in.read(buffer)) != -1) {
								outs.write(buffer, 0, numRead);
								numWritten += numRead;
							}
							in.close();
							outs.close();
						} catch (final Exception e) {
							if (in != null) {
								in.close();
							}

							// delete the local file if the download failed
							fullCertPath.delete();
							throw new IOException(e.getMessage());
						}
					} catch (final IOException e) {
						e.printStackTrace();
						// throw new InvocationTargetException(e, "Error getting
						// CA
						// from " + remoteCertPath);
					}
				}

				GSSCredential newCred = null;
				final MyProxy myProxy = new org.globus.myproxy.MyProxy(host,
						7512);

				try {

					myProxy
							.setAuthorization(new org.globus.gsi.gssapi.auth.NoAuthorization());

					// 1 year
					newCred = myProxy.get(null, login, psw, 31536000);

					String aux = newCred.getName().toString();
					StringTokenizer tokenizer = new StringTokenizer(aux, "/");
					aux = tokenizer.nextToken();
					tokenizer = new StringTokenizer(aux, "=");
					tokenizer.nextToken();
					final String organization = tokenizer.nextToken();

					// account = UserWorkspaceWSWrapper.queryUser(login, host,
					// newCred);

					final Session ses = Session.getInstance();
					ses.setCredential(newCred);
					ses.setHost(host);
					ses.setPort("8443");

					// ses.setOrganization(account.getOrganization());
					ses.setOrganization(organization);

					ContextListener.logger.info("Got AAA!");

				} catch (final MyProxyException e) {
					ContextListener.logger.error("MyProxy exception");
					e.printStackTrace();
				} catch (final GSSException e) {
					ContextListener.logger.error("GSS-API level exception");
					e.printStackTrace();
				}
			}

		} catch (final MissingResourceException mse) {
			ContextListener.logger
					.fatal("Failed loading properties from file!! " + mse);
			ContextListener.logger.fatal("MissingResourceException Cause: "
					+ mse.getCause());
			ContextListener.logger.fatal("MissingResourceException Message: "
					+ mse.getMessage());
			mse.printStackTrace();
		}

		this.topologyWS = new SimpleTopologyClient(Config.getString("hsi",
				"parent.topologyEPR"));

		return true;
	}

	@Override
	protected DomainInformationType getDomainInformation() {

		// Common part
		DomainInformationType domInfo = super.getDomainInformation();
		domInfo.setDomainId(Config.getString("hsi", "domain.name"));

		// Specific part: Add domain technology type.
		this.domainTechnology = new DomainTechnologyType();
		ca.inocybe.argia.stubs.reservation.GetDomainTechnologyInformationResponse uclpResponse = new ca.inocybe.argia.stubs.reservation.GetDomainTechnologyInformationResponse();
		ca.inocybe.argia.stubs.reservation.GetDomainTechnologyInformation gdti = new ca.inocybe.argia.stubs.reservation.GetDomainTechnologyInformation();

		try {
			uclpResponse = AdvReservationsWrapper
					.getDomainTechnologyInformation(gdti);

			ContextListener.logger
					.info("Got domain technology response from UCLP server, processing answer...");

		} catch (final RemoteException e) {
			ContextListener.logger
					.error("Could not get domain technology information");
			e.printStackTrace();
		} catch (final Exception e) {
			ContextListener.logger
					.error("Could not get domain technology information");
			e.printStackTrace();
		}

		if (this.domainTechnology.isSetDomainSupportedAdaptation())
			this.domainTechnology.unsetDomainSupportedAdaptation();
		if (this.domainTechnology.isSetDomainSupportedBandwidth())
			this.domainTechnology.unsetDomainSupportedBandwidth();
		if (this.domainTechnology.isSetDomainSupportedSwitchMatrix())
			this.domainTechnology.unsetDomainSupportedSwitchMatrix();

		for (String supportedAdaptation : uclpResponse
				.getGetDomainTechnologyInformationResponse()
				.getDomainTechnology().getDomainSupportedAdaptation()) {
			this.domainTechnology.getDomainSupportedAdaptation().add(
					supportedAdaptation);
		}

		for (Long supportedBandwidth : uclpResponse
				.getGetDomainTechnologyInformationResponse()
				.getDomainTechnology().getDomainSupportedBandwidth()) {
			this.domainTechnology.getDomainSupportedBandwidth().add(
					supportedBandwidth);
		}

		for (String supportedSwitchMatrix : uclpResponse
				.getGetDomainTechnologyInformationResponse()
				.getDomainTechnology().getDomainSupportedSwitchMatrix()) {
			this.domainTechnology.getDomainSupportedSwitchMatrix().add(
					supportedSwitchMatrix);
		}

		ContextListener.logger
				.info("Added domain technology information to domain information type");
		domInfo.setTechnology(this.domainTechnology);
		ContextListener.logger
				.info("Added domain technology information to domain information type");

		return domInfo;
	}

	/*
	 * TestMode
	 * =========================================================================
	 */
	/**
	 * @return True if testMode is enabled and false in other case Test mode =
	 *         No calls to Argia. Responses generated.
	 */
	protected boolean testMode() {
		return Config.isTrue("uclp", "testmode");
	}

}
