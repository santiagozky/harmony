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

package eu.ist_phosphorus.harmony.test.maintainance;

import java.io.IOException;
import java.net.URL;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.muse.ws.addressing.soap.SoapFault;

import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.DomainInformationType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetReservationsComplexType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetReservationsResponseType;
import eu.ist_phosphorus.harmony.common.serviceinterface.reservation.SimpleReservationClient;
import eu.ist_phosphorus.harmony.common.serviceinterface.topology.SimpleTopologyClient;
import eu.ist_phosphorus.harmony.common.utils.Config;

/**
 * Maintainance class to cancel all reservations within the testbed.
 *
 * @author Alexander Willner
 * @version $Id$
 */
public class CancelAllReservations {

    private static final int RSV_TIMEFRAME = 10000;

    /**
     * @param args
     * @throws SoapFault
     * @throws DatatypeConfigurationException
     */
    public static void main(final String[] args) throws SoapFault,
            DatatypeConfigurationException {

        final DomainInformationType root = new DomainInformationType();
        root.setTopologyEPR(Config.getString("test.real.epr.topology"));
        root.setReservationEPR(Config.getString("test.real.epr.reservation"));
        root.setDomainId("idb");

        CancelAllReservations.cancelRecursive(root, " ");
    }

    /**
     * Cancel all reservations recursively.
     *
     * @param domain The start domain.
     * @param indention For the layout.
     */
    private static void cancelRecursive(final DomainInformationType domain,
            final String indention) {

        /* ------------------------------------------------------------------ */
        System.out.println(indention + "* Getting all reservations from: "
                + domain.getDomainId());
        if (null == domain.getReservationEPR()) {
            System.out.println(indention + " * No reservations found.");
        } else {
            final SimpleReservationClient rsvClient =
                    new SimpleReservationClient(domain.getReservationEPR());
            GetReservationsResponseType reservations;
            try {
                final URL rsvEpr = new URL(domain.getReservationEPR());
                if (!CancelAllReservations.ping(rsvEpr.getHost())) {
                    throw new Exception(indention + "Host not reachable");
                }
                reservations = rsvClient.getReservations(RSV_TIMEFRAME);
                for (final GetReservationsComplexType reservation : reservations
                        .getReservation()) {
                    System.out.println(indention + " * Cancelling ID: "
                            + reservation.getReservationID());
                    rsvClient
                            .cancelReservation(reservation.getReservationID());
                }
            } catch (final Exception e) {
                System.out.println(indention + " * FAIL: "
                        + e.getMessage().replace('\n', ' '));
            }
        }
        /* ------------------------------------------------------------------ */

        /* ------------------------------------------------------------------ */
        System.out.println(indention + "* Getting all subdomains from: "
                + domain.getDomainId());

        if (null == domain.getTopologyEPR()) {
            System.out.println(indention + " * no topology interface found");
        } else {
            final SimpleTopologyClient tpyClient =
                    new SimpleTopologyClient(domain.getTopologyEPR());
            try {
                final URL tpyEpr = new URL(domain.getTopologyEPR());
                if (!CancelAllReservations.ping(tpyEpr.getHost())) {
                    throw new Exception(indention + " * Host not reachable");
                }

                for (final DomainInformationType subdomain : tpyClient.getDomains()
                        .getDomains()) {
                    System.out.println(indention + " * found domain: "
                            + subdomain.getDomainId() + " (in "
                            + domain.getDomainId() + ")");
                    if (subdomain.getDomainId().equals(domain.getDomainId())) {
                        System.out.println(indention
                                + " * Not calling subdomain: "
                                + subdomain.getDomainId());
                    } else {
                        System.out.println(indention
                                + " * Calling subdomain: "
                                + subdomain.getDomainId());
                        CancelAllReservations.cancelRecursive(subdomain, indention + " ");
                    }
                }
            } catch (final Exception e) {
                System.out.println(indention + " * FAIL: "
                        + e.getMessage().replace('\n', ' '));
            }
        }
        /* ------------------------------------------------------------------ */

    }

    /**
     * Check whether a host is reachable.
     *
     * @param host The host IP or name
     * @return True if ping works.
     * @throws IOException If ping executions fails.
     * @throws InterruptedException If ping was interrupted.
     */
    private static boolean ping(final String host) throws IOException,
            InterruptedException {
        boolean isPingable = false;
        ProcessBuilder pb;
        Process p;

        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            pb = new ProcessBuilder("ping", host);
        } else {
            pb = new ProcessBuilder("ping", "-c", "1", host);
        }

        p = pb.start();
        p.waitFor();
        isPingable = (p.exitValue() == 0);
        return isPingable;
    }

}
