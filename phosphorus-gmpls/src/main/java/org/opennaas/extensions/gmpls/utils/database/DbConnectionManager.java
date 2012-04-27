package org.opennaas.extensions.gmpls.utils.database;

import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.log4j.Logger;

import org.opennaas.core.utils.Config;
import org.opennaas.core.utils.PhLogger;

/**
 * @author Stephan Wagner(stephan.wagner@iais.fraunhofer.de)
 */
public final class DbConnectionManager {

    /** */
    private static Connection con;
    /**
     * TODO What about mysql connector?
     */
    static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    // static final String DB_DRIVER = "org.gjt.mm.mysql.Driver";
    private static Logger logger = PhLogger.getLogger();

    /** */
    static final String USERNAME = Config.getString("gmpls", "database.user");
    static final String HOST = Config.getString("gmpls", "database.host");
    static final String PORT = Config.getString("gmpls", "database.port");
    static final String DBNAME = Config.getString("gmpls", "database.db");
    /** */
    static final String PASSWORD = Config.getString("gmpls", "database.pass");
    /** ? */
    static final String URL =
            "jdbc:mysql://" + HOST + ":" + PORT + "/" + DBNAME + "?user="
                    + USERNAME + "&password=" + PASSWORD + "";

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

            logger.debug("URL: " + URL);
            con = DriverManager.getConnection(URL);
        } catch (Exception e) {
            logger.debug("Could not connect" + e.getMessage());
            logger.error(e.getMessage(), e);
        }
        logger.debug("Connected");
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

            logger.error(e.getMessage(), e);
        }
    }
}
