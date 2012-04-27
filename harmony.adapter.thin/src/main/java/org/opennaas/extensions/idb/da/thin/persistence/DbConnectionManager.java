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


package org.opennaas.extensions.idb.da.thin.persistence;

import java.sql.Connection;
import java.sql.DriverManager;

import org.opennaas.core.utils.Config;

/**
 * @author Stephan Wagner(stephan.wagner@iais.fraunhofer.de)
 */
public final class DbConnectionManager {

    /** */
    private static Connection con;
    /** */
    static final String DB_DRIVER = "org.gjt.mm.mysql.Driver";
    /** */
    static final String URL =
            Config.getString("adapter", "database.connection")
                    + "?autoReconnect=true";
    /** */
    static final String USERNAME =
            Config.getString("adapter", "database.user");
    /** */
    static final String PASSWORD =
            Config.getString("adapter", "database.pass");

    /** */
    private static DbConnectionManager instance = null;

    static {
        try {
            Class.forName(DB_DRIVER).newInstance();
        } catch (ClassNotFoundException cnfx) {
            cnfx.printStackTrace();
        } catch (IllegalAccessException iaex) {
            iaex.printStackTrace();
        } catch (InstantiationException iex) {
            iex.printStackTrace();
        }
    }

    /**
     * private constructor.
     */
    private DbConnectionManager() {
        // nothing to do.
    }

    /**
     * @return
     */
    public static DbConnectionManager getInstance() {
        if (null == instance) {
            instance = new DbConnectionManager();
        }
        return instance;
    }

    /**
     * Returns a connection to the database.
     *
     * @return the connection
     */
    public static Connection getConnection() {
        try {
            con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }

    /**
     * Static method that releases a connection.
     *
     * @param con
     *                the connection
     */
    public static void closeConnection(final Connection con) {
        try {
            if (con != null) {
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
