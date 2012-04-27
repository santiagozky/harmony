package org.opennaas.extensions.gmpls.utils.configuration_modules.snmp;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.adventnet.snmp.beans.SnmpTarget;
import com.adventnet.snmp.mibs.MibException;
import com.adventnet.snmp.snmp2.SnmpOID;
import com.adventnet.snmp.snmp2.SnmpVar;

import org.opennaas.extensions.gmpls.utils.configuration_modules.snmp.exceptions.SnmpException;
import org.opennaas.core.utils.PhLogger;

/**
 * @author Jochen Schon
 */

/*******************************************************************************
 * The SNMPConnection class is based on AdventNet SNMP API functionality. It
 * abstracts from a bundle of necessary SNMP configuration steps like
 * "Connection establishment", "MIB loading", "OID interpretation".
 ******************************************************************************/
public class SNMPConnection {
    private static Logger logger = PhLogger.getLogger();
    /** */
    private final SnmpTarget target = new SnmpTarget();

    /**
     * Creates a new instance of SNMPConnection using SNMP Version 2c. The
     * default constructor does not specify the host address of the SNMP agent
     * or the community String. These Variables have to be set afterwards. The
     * addmib Method is called.
     * 
     * @throws SnmpException
     */

    public SNMPConnection() throws SnmpException {

        this.target.setSnmpVersion(SnmpTarget.VERSION2C);

        try {
            addMibs();
        } catch (IOException e) {
            e.printStackTrace();
            throw new SnmpException(e);
        } catch (MibException e) {
            e.printStackTrace();
            throw new SnmpException(e);
        }
    }

    /**
     * Creates Instance of SNMPConnection with specified host address and
     * community String using SNMP version 2c. The Method also calls the addmib
     * Method.
     * 
     * @param hostAdr
     * @param community
     * @throws SnmpException
     */
    public SNMPConnection(final String hostAdr, final String community)
            throws SnmpException {
        // Use an SNMP target bean to perform SNMP operations
        this.target.setTargetHost(hostAdr);
        this.target.setSnmpVersion(SnmpTarget.VERSION2C);
        this.target.setCommunity(community);

        try {
            addMibs();
        } catch (IOException e) {
            e.printStackTrace();
            throw new SnmpException(e);
        } catch (MibException e) {
            e.printStackTrace();
            throw new SnmpException(e);
        }
    }

    /**
     * The method loads a number of mibs necessary for SNMP communication with
     * Alcatel UNI-C Proxys. This Method uses the relative path mibs/
     * 
     * @throws IOException
     * @throws MibException
     */
    private void addMibs() throws IOException, MibException {
        final String[] mibs =
                { "AGENTX-MIB", "DIFFSERV-DSCP-TC", "DIFFSERV-MIB",
                        "DISMAN-SCHEDULE-MIB", "DISMAN-SCRIPT-MIB",
                        "EtherLike-MIB", "HCNUM-TC", "HOST-RESOURCES-MIB",
                        "HOST-RESOURCES-TYPES",
                        "IANA-ADDRESS-FAMILY-NUMBERS-MIB", "IANAifType-MIB",
                        "IANA-LANGUAGE-MIB", "IANA-RTPROTO-MIB",
                        "IF-INVERTED-STACK-MIB", "IF-MIB", "INET-ADDRESS-MIB",
                        "INTEGRATED-SERVICES-MIB",
                        "INTEGRATED-SERVICES-MIB.mib", "IP-FORWARD-MIB",
                        "IP-MIB", "IPV6-ICMP-MIB", "IPV6-MIB", "IPV6-TC",
                        "IPV6-TCP-MIB", "IPV6-UDP-MIB", "MPLS-TC-STD-MIB",
                        "RFC1155-SMI", "RFC1213-MIB", "RFC-1215", "RMON-MIB",
                        "SMUX-MIB", "SNMP-COMMUNITY-MIB", "SNMP-FRAMEWORK-MIB",
                        "SNMP-MPD-MIB", "SNMP-NOTIFICATION-MIB",
                        "SNMP-PROXY-MIB", "SNMP-TARGET-MIB",
                        "SNMP-USER-BASED-SM-MIB", "SNMP-USM-AES-MIB",
                        "SNMP-USM-DH-OBJECTS-MIB", "SNMPv2-CONF", "SNMPv2-MIB",
                        "SNMPv2-SMI", "SNMPv2-TC", "SNMPv2-TM",
                        "SNMP-VIEW-BASED-ACM-MIB",
                        "TCP-MIB",
                        "TE-LINK-STD-MIB",
                        "TIMETRA-GLOBAL-MIB",
                        "TIMETRA-GLOBAL-MIB.mib",
                        "TIMETRA-GMPLS-LMP-MIB",
                        "TIMETRA-GMPLS-LMP-MIB.mib",
                        "TIMETRA-GMPLS-MIB",
                        "TIMETRA-GMPLS-MIB.mib",
                        "TIMETRA-GMPLS-RSVP-MIB.mib",
                        "TRANSPORT-ADDRESS-MIB",
                        "UCD-DEMO-MIB",
                        "UCD-DISKIO-MIB",
                        "UCD-DLMOD-MIB",
                        "UCD-IPFWACC-MIB",
                        "UCD-SNMP-MIB",
                        "UDP-MIB",

                        // mibs for Alcatel UNI 2.0 in
                        // alcateluni2mibs/

                        "alcateluni2mibs/TIMETRA-GMPLS-LMP-MIB",
                        "alcateluni2mibs/TIMETRA-GMPLS-LMP-MIB.mib",
                        "alcateluni2mibs/TIMETRA-GMPLS-MIB",
                        "alcateluni2mibs/TIMETRA-GMPLS-MIB.mib",
                        "alcateluni2mibs/TIMETRA-GMPLS-RSVP-MIB",
                        "alcateluni2mibs/TIMETRA-GMPLS-RSVP-MIB.mib", };

        final StringBuffer mibString = new StringBuffer();

        final String mibPath = "mibs/";
        for (String m : mibs) {
            mibString.append(mibPath);
            mibString.append(m);
            mibString.append(' ');
        }

        this.target.loadMibs(mibString.toString());

    }

    /**
     * uniSnmpSet does an SNMP set for a number of Variables. It takes an array
     * of oids (mib based) and an array of SNMP Variables as an input and
     * reuturns the Sting "success" or "failure"
     * 
     * @param oids
     * @param vars
     * @return
     * @throws SnmpException
     */
    public final void uniSnmpSet(final String[] oids, final SnmpVar[] vars)
            throws SnmpException {

        if (oids.length == vars.length) {
            try {
                this.target.setObjectIDList(oids); // set OID list on
                                                    // SnmpTarget
                this.target.getObjectIDList();

                this.target.setTimeout(60);
                this.target.snmpSetVariables(vars);
            } catch (com.adventnet.snmp.beans.DataException d) {
                d.printStackTrace();
                throw new SnmpException(d);
            }
        } else {
            throw new SnmpException("Error: All variable Bindings needed");
        }
    }

    /**
     * uniSnmpGet takes an oid as an argument and returns the variable value as
     * a String.
     * 
     * @param oidObject
     * @return
     * @throws SnmpException
     */
    public final String uniSnmpGet(final String oidObject) throws SnmpException {
        this.target.setObjectID(oidObject);
        String result = this.target.snmpGet();
        if (result == null) {
            throw new SnmpException("Error: " + this.target.getErrorString());
        }
        return result;
    }

    /**
     * Takes an oid as an argument and return results as a string.
     * 
     * @param oidObject
     * @param whichmib
     * @return
     * @throws SnmpException
     */
    public final String snmpWalk(final String oidObject) throws SnmpException {
        // Set the OID on the SnmpTarget instance.
        final StringBuffer s = new StringBuffer(64);
        this.target.setObjectID(oidObject);
        int maxtry = 0;
        final SnmpOID[] oidList = this.target.getSnmpOIDList();
        if (oidList == null) {
            this.target.releaseResources();
            throw new SnmpException(
                    "Error: Invalid OID has been specified / Check if the OID is present in the MIB loaded if any");
        }
        final SnmpOID rootoid = oidList[0];
        logger.debug("oidList: " + oidList.length);
        logger.debug("rootoid: " + rootoid);
        while (maxtry++ < 1000) { // limit the max getnexts to 1000
            final String[] results = this.target.snmpGetNextList();
            if (results == null) {
                logger.debug("no result!");
                break;
            }
            if (!SnmpTarget.isInSubTree(rootoid, this.target.getSnmpOID())) {
                break; // check first column
            }
            for (int i = 0; i < results.length; i++) { // print the values
                final String[] pieces = this.target.getObjectID(i).split("\\.");
                s.append(pieces[pieces.length - 2]);
                s.append('.');
                s.append(pieces[pieces.length - 1]);
                s.append('=');
                s.append(results[i]);
                s.append("::");
            }
        }

        this.target.releaseResources();
        return s.toString();
    }
}
