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
import java.util.Hashtable;
import java.util.List;
import java.util.MissingResourceException;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.muse.ws.addressing.soap.SoapFault;
import org.globus.myproxy.MyProxy;
import org.globus.myproxy.MyProxyException;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;

import ca.inocybe.argia.stubs.reservation.GetEndpoints;
import ca.inocybe.argia.stubs.reservation.GetEndpointsResponse;
import org.opennaas.extensions.idb.da.argia.implementation.AdvReservationsWrapper;
import org.opennaas.extensions.idb.da.argia.implementation.Session;
import org.opennaas.extensions.idb.serviceinterface.CommonServlet;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AddDomain;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AddDomainResponse;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AddDomainType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AddEndpoint;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AddEndpointType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DomainInformationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointInterfaceType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.DomainAlreadyExistsFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.EndpointAlreadyExistsFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.InvalidRequestFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.utils.JaxbSerializer;
import org.opennaas.extensions.idb.serviceinterface.topology.SimpleTopologyClient;
import org.opennaas.core.utils.Config;

/**
 * Initializer class for the UCLP Adapter
 * 
 * @author Daniel Beer (beer@cs.uni-bonn.de)
 * @author Angel Sanchez (angel.sanchez@i2cat.net)
 */
@Deprecated
public class StartupServlet extends CommonServlet {

    private static final long serialVersionUID = -2567343177983533519L;
    /**
     * The logger.
     */
    private static final Logger logger = Logger
            .getLogger(StartupServlet.class);
    private SimpleTopologyClient topologyWS;

    private final Hashtable<String, EndpointType> endpoints = new Hashtable<String, EndpointType>();

    // private AAAUserType aaa = new AAAUserType ();

    private final Vector<EndpointType> epList = new Vector<EndpointType>();

    /**
     * Constructor
     */
    public StartupServlet() {
        try {

            StartupServlet.logger.info("Getting AAA");

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
                        outs = new BufferedOutputStream(new FileOutputStream(
                                fullCertPath));
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
                    // throw new InvocationTargetException(e, "Error getting CA
                    // from " + remoteCertPath);
                }
            }

            GSSCredential newCred = null;
            final MyProxy myProxy = new org.globus.myproxy.MyProxy(host, 7512);

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

                StartupServlet.logger.info("Got AAA!");

            } catch (final MyProxyException e) {
                e.printStackTrace();
            } catch (final GSSException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            boolean reg = false;

            // Loop to retry registration if it fails
            while (!reg) {
                if (Config.getString("uclp", "registration").equalsIgnoreCase(
                        "off")) {
                    // Registration not needed
                    reg = true;
                } else {
                    this.topologyWS = new SimpleTopologyClient(Config
                            .getString("hsi", "parent.topologyEPR"));

                    reg = this.registerDomain();
                }

                // If it has been registered, continue, if not, raise error
                if (reg) {
                    this.schedulePolling();
                    StartupServlet.logger
                            .info("UCLP Adapter initialized successfully!");
                } else {
                    StartupServlet.logger
                            .error("UCLP Adapter could not be initialized! Retrying in "
                                    + Config.getString("uclp", "retry") + " ms");

                    Thread.sleep(Long.parseLong(Config.getString("uclp",
                            "retry")));
                }
            }

        } catch (final MissingResourceException mse) {
            StartupServlet.logger
                    .fatal("Failed loading properties from file!! " + mse);
            StartupServlet.logger
                    .fatal("MissingResourceException Cause: " + mse.getCause());
            StartupServlet.logger
                    .fatal("MissingResourceException Message: "
                            + mse.getMessage());
            mse.printStackTrace();
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Pull all endpoints from UCLP server and sends the ones that have not been
     * sent to the NSP yet. This is used when the Domain is registered
     * (polling=false) and in the automatic polling (polling=true)
     */
    @SuppressWarnings("unchecked")
    void pullEndpoints() {

        boolean modifList = false;

        StartupServlet.logger
                .info("Getting endpoints from UCLP server... "
                        + Config.getString("uclp", "epr.uclp.host") + " "
                        + Config.getString("uclp", "epr.uclp.port"));

        final GetEndpoints endpointsReq = new GetEndpoints();
        GetEndpointsResponse uclpResponse = new GetEndpointsResponse();

        endpointsReq.setOrganization(Session.getInstance().getOrganization());

        try {
            uclpResponse = AdvReservationsWrapper.getEndpoints(endpointsReq);

            StartupServlet.logger
                    .info("Got response from UCLP server, processing answer...");

        } catch (final RemoteException e) {
            StartupServlet.logger.error("Could not get endpoints");
            e.printStackTrace();
        } catch (final Exception e) {
            StartupServlet.logger
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

            if (element.get_interface().getValue().equalsIgnoreCase("border")) {
                aux.setInterface(EndpointInterfaceType.BORDER);
            } else if (element.get_interface().getValue().equalsIgnoreCase(
                    "user")) {
                aux.setInterface(EndpointInterfaceType.USER);
            }

            if (this.endpoints.isEmpty()) {
                modifList = true;
                this.epList.add(aux);
                this.endpoints.put(aux.getEndpointId(), aux);
            } else {
                if (this.endpoints.containsKey(aux.getEndpointId())) {
                    ;
                } else {
                    modifList = true;
                    this.endpoints.put(aux.getEndpointId(), aux);
                    this.epList.add(aux);
                }
            }
        }

        StartupServlet.logger.info("Answer processed!");

        if (!this.epList.isEmpty() && modifList) {
            this.pushEndpoints(this.epList);
        } else {
            StartupServlet.logger.info("No new endpoints to send");
        }
    }

    /**
     * Push the endpoints to the NSP-Topology-WS
     * 
     * @param endpoints
     */
    private void pushEndpoints(final List<EndpointType> endpoints) {

        int border = 0, user = 0;

        StartupServlet.logger.info("Sending endpoints to NSP...");

        for (final EndpointType endpointType : endpoints) {

            if (endpointType.getInterface().value().equalsIgnoreCase("border")) {

                border++;

                final AddEndpoint ep = new AddEndpoint();
                final AddEndpointType epType = new AddEndpointType();

                epType.setEndpoint(endpointType);

                ep.setAddEndpoint(epType);

                try {
                    this.topologyWS.addEndpoint(JaxbSerializer.getInstance()
                            .objectToElement(ep));

                    StartupServlet.logger
                            .info("Sent border endpoint to NSP: "
                                    + endpointType.getEndpointId());

                } catch (final EndpointAlreadyExistsFaultException e) {
                    // TODO Auto-generated catch block
                    StartupServlet.logger
                            .warn("Endpoint already exists... continuing");
                    e.printStackTrace();
                } catch (final InvalidRequestFaultException e) {
                    // TODO Auto-generated catch block
                    StartupServlet.logger
                            .error("Failed to send endpoint");
                    e.printStackTrace();
                } catch (final UnexpectedFaultException e) {
                    // TODO Auto-generated catch block
                    StartupServlet.logger
                            .error("Failed to send endpoint");
                    e.printStackTrace();
                } catch (final SoapFault e) {
                    // TODO Auto-generated catch block
                    StartupServlet.logger
                            .error("Failed to send endpoint");
                    e.printStackTrace();
                }
            } else {
                user++;
            }
        }

        StartupServlet.logger
                .info("Sent endpoints successfully! -> border: " + border
                        + " , (not sent) user: " + user);
    }

    /**
     * Register the Domain at NSP-Topology-WS Recursively tries to register
     */
    private boolean registerDomain() {

        StartupServlet.logger.info("Registering domain...");

        boolean res = false;

        final AddDomain addDomain = new AddDomain();
        final AddDomainType addDomainType = new AddDomainType();
        final DomainInformationType dom = new DomainInformationType();
        AddDomainResponse response = new AddDomainResponse();

        dom.setDomainId(Config.getString("hsi", "domain.name"));
        dom.setDescription(Config.getString("hsi", "domain.description"));
        dom.setReservationEPR(Config.getString("hsi", "domain.reservationEPR"));

        final String[] tnaList = Config.getString("hsi", "domain.TNAPrefix0").split(
                " ");

        for (final String element : tnaList) {
            dom.getTNAPrefix().add(element);
        }

        addDomainType.setDomain(dom);
        addDomain.setAddDomain(addDomainType);

        try {

            response = (AddDomainResponse) JaxbSerializer.getInstance()
                    .elementToObject(
                            this.topologyWS.addDomain(JaxbSerializer
                                    .getInstance().objectToElement(addDomain)));

            if (response.getAddDomainResponse().isSuccess()) {
                StartupServlet.logger
                        .info("Domain succesfully registered!");
                res = true;
            } else {
                StartupServlet.logger
                        .error("Server replied but Domain could not be registered!");
            }

        } catch (final DomainAlreadyExistsFaultException e) {
            StartupServlet.logger
                    .warn("Domain already exists! Continuing initialization...");
            res = true;
        } catch (final UnexpectedFaultException e) {
            // TODO Auto-generated catch block
            StartupServlet.logger.error("Could not register Domain!");
            e.printStackTrace();
        } catch (final InvalidRequestFaultException e) {
            // TODO Auto-generated catch block
            StartupServlet.logger.error("Could not register Domain!");
            e.printStackTrace();
        } catch (final SoapFault e) {
            // TODO Auto-generated catch block
            StartupServlet.logger.error("Could not register Domain!");
            e.printStackTrace();
        }

        return res;
    }

    /**
     * This function programs the polling of the Endpoints to the NRPS server.
     * It starts NOW and gets the polling interval from the "uclp.properties"
     * configuration file
     */
    private void schedulePolling() {

        StartupServlet.logger.info("Starting polling scheduler...");

        final TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                StartupServlet.this.pullEndpoints();
            }
        };

        final Timer timer = new Timer();

        // delay and interval are measured in milliseconds
        final long interval = Long.parseLong(Config.getString("uclp",
                "nrps.pollingInterval"));

        final long delay = Long.parseLong(Config.getString("uclp",
                "nrps.pollingDelayBeforeExecution"));

        timer.scheduleAtFixedRate(timerTask, delay, interval);

        StartupServlet.logger
                .info("Polling scheduler started successfully! (each "
                        + interval / 1000
                        + " seconds with an initial delay of " + delay / 1000);
    }

}
