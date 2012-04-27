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


package org.opennaas.extensions.idb.da.argia.implementation;

import org.ietf.jgss.GSSCredential;

/**
 * Stores information about the session of the user.
 * 
 * @author Laieta
 * 
 */
public class Session {
    private static Session instance = null;

    public static Session getInstance() {
        if (Session.instance == null) {
            Session.instance = new Session();
        }

        return Session.instance;
    }

    GSSCredential credential = null;
    String organization = null;
    String host = null;

    String port = null;

    private Session() {
    }

    public GSSCredential getCredential() {
        return this.credential;
    }

    public String getHost() {
        return this.host;
    }

    public String getOrganization() {
        return this.organization;
    }

    public String getPort() {
        return this.port;
    }

    public void setCredential(final GSSCredential credential) {
        this.credential = credential;
    }

    public void setHost(final String host) {
        this.host = host;
    }

    public void setOrganization(final String organiation) {
        this.organization = organiation;
    }

    public void setPort(final String port) {
        this.port = port;
    }
}
