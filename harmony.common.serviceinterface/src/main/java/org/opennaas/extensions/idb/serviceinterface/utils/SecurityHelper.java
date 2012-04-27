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

import org.apache.muse.ws.addressing.soap.SimpleSoapClient;
import org.apache.muse.ws.addressing.soap.SoapClient;

import org.opennaas.core.utils.Config;
import org.opennaas.core.utils.PhLogger;

public class SecurityHelper {

    /**
     * Create a SecureClient if possible.
     * 
     * @return
     */
    public static final SoapClient createSoapClient() {
        SoapClient client = null;

        try {
            final Class<?> scClass = Class.forName(Config.getString(
                    "databinding", "secure.client"));

            client = (SoapClient) scClass.newInstance();

            PhLogger.getLogger().info("Using SecureSoapClient");
        } catch (final ClassNotFoundException e) {
            client = new SimpleSoapClient();

            PhLogger.getLogger().info("Using default SimpleSoapClient");
        } catch (final Exception e) {
            client = new SimpleSoapClient();

            PhLogger.getLogger().error(
                    "Can not Create SecureSoapClient! "
                            + "Using SimpleSoapClient instead...", e);
        }

        return client;
    }
}
