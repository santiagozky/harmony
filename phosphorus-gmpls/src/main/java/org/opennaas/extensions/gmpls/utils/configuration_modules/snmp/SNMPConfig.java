/*
 * SNMPConfig.java Created on 21. Dezember 2006, 16:29
 */

package org.opennaas.extensions.gmpls.utils.configuration_modules.snmp;

import java.util.Arrays;

import com.adventnet.snmp.snmp2.SnmpAPI;
import com.adventnet.snmp.snmp2.SnmpVar;

import org.opennaas.extensions.gmpls.utils.configuration_modules.snmp.exceptions.SnmpException;

/**
 * @author mshahid, Daniel Beer (daniel.beer@iais.fraunhofer.de)
 */
public class SNMPConfig {

    /** */
    private SNMPConnection connector;

    /**
     * Creates a new instance of SNMPConfig.
     * 
     * @param hostAdr
     * @param community
     * @throws SnmpException
     */
    public SNMPConfig(final String hostAdr, final String community)
            throws SnmpException {
        this.connector = new SNMPConnection(hostAdr, community);
    }

    /**
     * @param bandwidth
     * @param srcTNA
     * @param destTNA
     * @return
     * @throws SnmpException
     */
    public final int lspSetupAlcatel(final int bandwidth, final String srcTNA,
            final String destTNA) throws SnmpException {

        final String[] bandwidths =
                { "150", "250", "350", "500", "650", "800", "1000" };

        final int resourceIndex =
                Arrays.asList(bandwidths).indexOf(Integer.toString(bandwidth)) + 1;

        int freeTunnelIndex = this.getFreeTunnelIDAlcatel();
        // creating necessary oids
        String[] oids = new String[8];

        oids[0] = "tmnxGmplsTunnelRowStatus." + freeTunnelIndex;
        oids[1] = "tmnxGmplsTunnelDescr." + freeTunnelIndex;
        oids[2] = "tmnxGmplsTunnelSourceTNA." + freeTunnelIndex;
        oids[3] = "tmnxGmplsTunnelDestinationTNA." + freeTunnelIndex;
        oids[4] = "tmnxGmplsTunnelHoBandwidth." + freeTunnelIndex;
        oids[5] = "tmnxGmplsTunnelSwitchingType." + freeTunnelIndex;
        oids[6] = "tmnxGmplsTunnelLSPEncoding." + freeTunnelIndex;
        oids[7] = "tmnxGmplsTunnelResourcePointer." + freeTunnelIndex;

        // creating oid variables
        SnmpVar[] vars = new SnmpVar[8];
        try {
            vars[0] = SnmpVar.createVariable("4", SnmpAPI.INTEGER);
            vars[1] = SnmpVar.createVariable("-" + srcTNA + "-" + destTNA, SnmpAPI.STRING);
            vars[2] = SnmpVar.createVariable(srcTNA, SnmpAPI.IPADDRESS);
            vars[3] = SnmpVar.createVariable(destTNA, SnmpAPI.IPADDRESS);
            vars[4] = SnmpVar.createVariable("0", SnmpAPI.INTEGER);
            vars[5] = SnmpVar.createVariable("51", SnmpAPI.INTEGER);
            vars[6] = SnmpVar.createVariable("2", SnmpAPI.INTEGER);
            vars[7] =
                    SnmpVar.createVariable(Integer.toString(resourceIndex),
                            SnmpAPI.UNSIGNED32);

        } catch (com.adventnet.snmp.snmp2.SnmpException e) {
            e.printStackTrace();
            throw new SnmpException(e);
        }
        this.connector.uniSnmpSet(oids, vars);
        return freeTunnelIndex;
    }

    /**
     * terminate LSP is the main LSP termination Method. It shuts down a
     * specific LSP on a UNI-C Proxy device specified by the hostAdr. The Proxy
     * device have to be the head device for the LSP. The lspIndex parameter
     * identifies the LSP an is unique on one UNI-C Proxy
     * 
     * @param lspIndex
     * @return
     * @throws SnmpException
     */
    public final void terminateLSPAlcatel(final String lspIndex)
            throws SnmpException {

        String[] oids = new String[1];
        oids[0] = "tmnxGmplsTunnelRowStatus." + lspIndex;

        SnmpVar[] vars = new SnmpVar[1];
        try {
            vars[0] = SnmpVar.createVariable("6", SnmpAPI.INTEGER);
        } catch (com.adventnet.snmp.snmp2.SnmpException w) {
            w.printStackTrace();
            throw new SnmpException(w);
        }
        this.connector.uniSnmpSet(oids, vars);
    }

    /**
     * The pathStatus Method enables for checking up or down Status of LSPs.
     * 
     * @param lspIndex
     * @return
     * @throws SnmpException
     */
    public final String pathStatusAlcatel(final String lspIndex)
            throws SnmpException {
        String status =
                this.connector.uniSnmpGet("tmnxGmplsTunnelOperStatus."
                        + lspIndex);

        if ("2".equals(status)) {
            status = "down";
        }
        if ("1".equals(status)) {
            status = "up";
        }
        if ("3".equals(status)) {
            status = "degraded";
        }
        return status;
    }

    /**
     * The pathErrorInfo Method enables for checking up error info column in
     * tmnxGmplsTunnelEntry.
     * 
     * @param lspIndex
     * @return
     * @throws SnmpException
     */
    public final String pathErrorInfoAlcatel(final String lspIndex)
            throws SnmpException {
        return this.connector
                .uniSnmpGet("tmnxGmplsTunnelErrorInfo." + lspIndex);

    }

    /**
     * This method walks over the given oid and return results as one string.
     * 
     * @param oid
     * @return
     * @throws SnmpException
     */
    public final String walkSnmpTableAlcatel(final String oid)
            throws SnmpException {
        return this.connector.snmpWalk(oid);
    }

    /**
     * This method triggers an SNMP get for a free LSP index on the UNI-C device
     * specified.
     * 
     * @return
     * @throws SnmpException
     */
    private int getFreeTunnelIDAlcatel() throws SnmpException {
        return Integer.parseInt(this.connector
                .uniSnmpGet("tmnxGmplsTunnelIdNext.0"));
    }

}
