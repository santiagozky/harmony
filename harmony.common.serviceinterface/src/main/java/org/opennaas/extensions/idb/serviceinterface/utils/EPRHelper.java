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

package org.opennaas.extensions.idb.serviceinterface.utils;

import org.apache.muse.ws.addressing.EndpointReference;
import org.apache.muse.ws.addressing.WsaConstants;

import org.opennaas.core.utils.Config;
import org.opennaas.core.utils.Helpers;
import org.opennaas.core.utils.PhLogger;

/**
 * Class loader for the service interface configuration
 * 
 * @author Carlos Baez Ruiz
 * 
 */
public class EPRHelper {
    public static final String RESERVATION = "domain.reservationEPR";
    public static final String TOPOLOGY = "domain.topologyEPR";
    public static final String NOTIFICATION = "domain.notificationEPR";

    /**
     * Get the string EPR
     * 
     * @param option
     * @return
     */
    public static final String getEPR(final String option) {
        String epr = "";
        try {
            epr = Config.getString("hsi", option);
        } catch (final Exception e) {
            try {
                epr = Config.getString("hsiIDB", option);
            } catch (final Exception e2) {
                try {
                    epr = Config.getString("hsiTranslatorG2MPLS", option);
                } catch (final Exception e3) {
                    return "";
                }
            }
        }

        return epr;
    }

    /**
     * Get the string EPR source
     * 
     * @param option
     * @return
     */
    public static final EndpointReference getSource(final String option) {
        try {

            final String epr = EPRHelper.getEPR(option);
            if ((epr == null) || epr.equalsIgnoreCase("")) {
                PhLogger.getLogger().info(
                        "Warning, it was impossible to get some EPR.");
                return WsaConstants.ANONYMOUS_EPR;
            }
            return Helpers.convertStringtoEPR(epr);

        } catch (final Exception e) {
            PhLogger.getLogger().info("Could not get a EPR " + option);
            return WsaConstants.ANONYMOUS_EPR;
        }
    }

}
