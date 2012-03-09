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


/**
 * 
 */
package client.helper;

import java.util.List;

import client.classes.nsp.ConnectionStatusType;
import client.classes.nsp.DomainInformationType;
import client.classes.nsp.DomainStatusType;
import client.classes.nsp.EndpointType;
import client.classes.nsp.GetStatusResponseType;
import client.classes.nsp.InterdomainLinkType;
import client.classes.nsp.GetStatusResponseType.ServiceStatus;

/**
 * @author gassen
 */
public final class PopupOutput {

    public static final String format(final DomainInformationType source) {

        String detail =
                "<b>Description:</b> " + source.getDescription() + "<hr />"
                        + "<b>Max BW:</b> " + source.getMaxBW() + "<br>"
                        + "<b>Avg. Delay:</b> " + source.getAvgDelay()
                        + "<hr />";

        detail += "<b>TNA prefixes</b><br>";

        final List prefixes = source.getTNAPrefix();

        for (int x = 0; x < prefixes.size(); x++) {
            if (x > 0) {
                detail += "<br>";
            }
            final String tp = (String) prefixes.get(x);
            detail += tp;
        }

        final List interdomainLinks = source.getInterdomainLink();

        detail += "<hr /><b>Interdomain Links:</b><br>";

        for (int x = 0; x < interdomainLinks.size(); x++) {
            final InterdomainLinkType idlt =
                    (InterdomainLinkType) interdomainLinks.get(x);

            detail +=
                    idlt.getSourceEndpoint().getEndpointId() + " -> "
                            + idlt.getDestinationDomain() + "/"
                            + idlt.getLinkID();
            if (idlt.isSetCosts()) {
                detail += " (" + idlt.getCosts() + ")";
            }
            detail += " <br>";
        }

        if (0 == interdomainLinks.size()) {
            detail += "<i>none</i><br>";
        }

        detail += "<b>Domain Relationship:</b> ";
        if (source.isSetRelationship()) {
            detail += source.getRelationship().value();
        }
        detail += "<hr />";

        detail +=
                "<b>TopologyEPR:</b> " + source.getTopologyEPR() + "<br>"
                        + "<b>ReservationEPR:</b> "
                        + source.getReservationEPR() + "<br>"
                        + "<b>NotificationEPR:</b> "
                        + source.getNotificationEPR();

        return detail;
    }

    public static final String format(final GetStatusResponseType source) {
        final List serviceStatusList = source.getServiceStatus();

        String detail = "";

        for (int x = 0; x < serviceStatusList.size(); x++) {
            final ServiceStatus serviceStatus =
                    (ServiceStatus) serviceStatusList.get(x);

            detail +=
                    "Reservation Status: <b>"
                            + serviceStatus.getStatus().value()
                            + "</b><br>&nbsp;<br>";

            final List connectionsList = serviceStatus.getConnections();

            detail += "<b>Connections:</b> <br>";

            for (int y = 0; y < connectionsList.size(); y++) {
                final ConnectionStatusType connectionStatusType =
                        (ConnectionStatusType) connectionsList.get(y);

                detail +=
                        "<i>Src:</i> "
                                + connectionStatusType.getSource()
                                        .getEndpointId() + "<br>";

                final List targetList = connectionStatusType.getTarget();

                for (int z = 0; z < targetList.size(); z++) {
                    final EndpointType endpointType =
                            (EndpointType) targetList.get(z);

                    detail +=
                            "<i>Dst (" + (z + 1) + "): </i>"
                                    + endpointType.getEndpointId() + "<br>";
                }

                detail +=
                        "<i>Status:</i> "
                                + connectionStatusType.getStatus().value()
                                + "<br>";
                detail +=
                        "<i>BW:</i> " + connectionStatusType.getActualBW()
                                + "<br>";
                detail +=
                        "<i>Directionality:</i> "
                                + connectionStatusType.getDirectionality()
                                + "<br>";
                detail +=
                        "<i>ID:</i> " + connectionStatusType.getConnectionID()
                                + "<br>";
            }

            final List domainStatusList = serviceStatus.getDomainStatus();

            detail += "<br><b>Domain States:</b> <br>";

            for (int y = 0; y < domainStatusList.size(); y++) {
                final DomainStatusType domainStatusType =
                        (DomainStatusType) domainStatusList.get(y);

                detail += "\t" + domainStatusType.getDomain() + ": ";
                detail += domainStatusType.getStatus().value() + "<br>";
            }
        }

        return detail;
    }
}
