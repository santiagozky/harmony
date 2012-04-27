/**
 *
 */
package org.opennaas.extensions.gmpls.utils.configuration_modules.gmre;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import org.opennaas.extensions.gmpls.utils.configuration_modules.gmre.exceptions.GmreConnectionException;
import org.opennaas.extensions.gmpls.utils.configuration_modules.gmre.exceptions.PathCreationException;
import org.opennaas.core.utils.PhLogger;

/**
 * @author Stephan Wagner (stephan.wagner@iais.fraunhofer.de)
 */
public final class GmreCliManager {
    private static Logger logger = PhLogger.getLogger();
    /** */
    private static int GMRE_CLIENT_PORT = 30000;
    /** */
    private static GmreCliManager instance = null;
    /** */
    private static Map<String, GmreCli> gmreCliMap =
            new HashMap<String, GmreCli>();

    /**
     * getInstance of the singleton.
     * 
     * @return instance
     */
    public static GmreCliManager getInstance() {
        if (GmreCliManager.instance == null) {
            GmreCliManager.instance = new GmreCliManager();
        }
        return GmreCliManager.instance;
    }

    /**
     *
     */
    private GmreCliManager() {
        // nothing to do
    }

    /**
     * @param server
     * @param inLink
     * @param inLabel
     * @param destLink
     * @param destLabel
     * @param lspTraffic
     * @param protRestType
     * @param srcTNA
     * @param destTNA
     * @param pathIDE
     * @return
     * @throws GmreConnectionException
     * @throws PathCreationException
     */
    public synchronized int configLSP(final String server, final String inLink,
            final String inLabel, final String destLink,
            final String destLabel, final String lspTraffic,
            final String protRestType, final String srcTNA, final String destTNA)
            throws GmreConnectionException, PathCreationException {
        try {
            final GmreCli cli = this.getGmreCliForIp(server);
            return cli.configLSP(inLink, inLabel, destLink, destLabel,
                    lspTraffic, protRestType, srcTNA, destTNA);
        } catch (GmreConnectionException e) {
            try {
                renewGmreCliForIp(server);
                return configLSP(server, inLink, inLabel, destLink, destLabel,
                        lspTraffic, protRestType, srcTNA, destTNA);
            } catch (GmreConnectionException e2) {
                logger.fatal("Could not reestablish connection to gmreCli on: "
                        + server, e2);
                throw new GmreConnectionException(
                        "Could not reestablish connection", e2);
            }
        }
    }

    /**
     *
     */
    @Override
    public void finalize() throws Throwable {
        super.finalize();
        for (final GmreCli cli : GmreCliManager.gmreCliMap.values()) {
            cli.endSession();
        }
    }

    /**
     * @param server
     * @return
     * @throws GmreConnectionException
     */
    private synchronized GmreCli getGmreCliForIp(final String server)
            throws GmreConnectionException {

        if (null == GmreCliManager.gmreCliMap.get(server)) {
            GmreCliManager.gmreCliMap.put(server, new GmreCli(server, "gmre",
                    "gmre", GmreCliManager.GMRE_CLIENT_PORT));
        }
        return GmreCliManager.gmreCliMap.get(server);
    }

    /**
     * @param server
     * @return
     * @throws GmreConnectionException
     */
    private synchronized GmreCli renewGmreCliForIp(final String server)
            throws GmreConnectionException {

        GmreCliManager.gmreCliMap.put(server, new GmreCli(server, "gmre",
                "gmre", GmreCliManager.GMRE_CLIENT_PORT));

        return GmreCliManager.gmreCliMap.get(server);
    }

    /**
     * @param server
     * @param lspIndex
     * @return
     * @throws GmreConnectionException
     */
    public synchronized boolean isLspAdminUp(final String server,
            final int lspIndex) throws GmreConnectionException {
        try {
            final GmreCli cli = this.getGmreCliForIp(server);
            return cli.isLspAdminUp(lspIndex);
        } catch (GmreConnectionException e) {
            try {
                renewGmreCliForIp(server);
                return isLspAdminUp(server, lspIndex);
            } catch (GmreConnectionException e2) {
                logger.fatal("Could not reestablish connection to gmreCli on: "
                        + server, e2);
                throw new GmreConnectionException(
                        "Could not reestablish connection", e2);
            }
        }
    }

    /**
     * @param server
     * @param TNA
     * @return
     * @throws GmreConnectionException
     */
    public synchronized boolean isTNAinLSP(final String server, final String TNA)
            throws GmreConnectionException {
        try {
            final GmreCli cli = this.getGmreCliForIp(server);
            return cli.isTNAinLSP(TNA);
        } catch (GmreConnectionException e) {
            try {
                renewGmreCliForIp(server);
                return isTNAinLSP(server, TNA);
            } catch (GmreConnectionException e2) {
                logger.fatal("Could not reestablish connection to gmreCli on: "
                        + server, e2);
                throw new GmreConnectionException(
                        "Could not reestablish connection", e2);
            }
        }
    }

    /**
     * @param server
     * @param lspIndex
     * @param description
     * @return
     * @throws GmreConnectionException
     */
    public synchronized boolean terminateLsp(final String server,
            final int lspIndex, final String description)
            throws GmreConnectionException {

        try {
            final GmreCli cli = this.getGmreCliForIp(server);
            return cli.terminateLsp(lspIndex, description);
        } catch (GmreConnectionException e) {
            try {
                renewGmreCliForIp(server);
                return terminateLsp(server, lspIndex, description);
            } catch (GmreConnectionException e2) {
                logger.fatal("Could not reestablish connection to gmreCli on: "
                        + server, e2);
                throw new GmreConnectionException(
                        "Could not reestablish connection", e2);
            }
        }

    }

}
