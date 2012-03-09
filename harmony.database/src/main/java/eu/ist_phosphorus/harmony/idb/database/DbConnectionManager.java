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


package eu.ist_phosphorus.harmony.idb.database;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import eu.ist_phosphorus.harmony.common.utils.Config;
import eu.ist_phosphorus.harmony.common.utils.PhLogger;
import eu.ist_phosphorus.harmony.idb.exceptions.database.DatabaseException;

/**
 * Database connection manager used by DbManager.
 * 
 * @author Stephan Wagner (wagners@cs.uni-bonn.de)
 * @version 0.1
 */

public class DbConnectionManager {
	// Do not remove this. Nothing will work otherwise...
	@SuppressWarnings("unused")
	private static Logger logger = PhLogger.getLogger();

	// private static Session privSession = null;
	private static final ThreadLocal<Session> privSession = new ThreadLocal<Session>();

	/**
	 * static SessionFactory for Hibernate.
	 */
	private final SessionFactory sf;

	/**
	 * Instance for singleton.
	 */
	private static DbConnectionManager instance;

	/**
	 * Private constructor for singleton.
	 * 
	 * @throws DatabaseException
	 * @throws URISyntaxException
	 *             if getURL can't find property
	 * @throws FileNotFoundException
	 *             if config was not found
	 */
	protected DbConnectionManager() throws DatabaseException {
		// TODO: Implement C3PO(connection-pool)

		final AnnotationConfiguration config = new AnnotationConfiguration();
		try {

			config.setProperties(Config.getProperties("hibernate"));
		} catch (IOException e) {
			throw new DatabaseException("Cannot read hibernate properties:" + e);
		}
		try {
			config.configure(Config.getURL("hibernate", "hibernate.cfg.xml"));
		} catch (Exception e) {
			throw new DatabaseException("Error configuring hibernate:" + e);
		} // catch (FileNotFoundException e) {
		// throw new DatabaseException("Error configuring hibernate:" + e);
		// }
		this.sf = config.buildSessionFactory();
		this.sf.openStatelessSession();
	}

	/**
	 * getInstance of the singleton.
	 * 
	 * @return instance
	 * @throws DatabaseException
	 * @throws HibernateException
	 */
	public static DbConnectionManager getInstance() throws DatabaseException {
		synchronized (DbConnectionManager.class) {
			if (DbConnectionManager.instance == null) {
				DbConnectionManager.instance = new DbConnectionManager();
			}
		}
		return DbConnectionManager.instance;
	}

	/**
	 * Getter for the SessionFactory instance.
	 * 
	 * @return SessionFactory instance
	 * @throws DatabaseException
	 */
	public static SessionFactory getSessionFactory() throws DatabaseException {
		if (DbConnectionManager.instance == null) {
			DbConnectionManager.instance = DbConnectionManager.getInstance();
		}
		return DbConnectionManager.instance.sf;
	}

	/**
	 * Create new Session from SessionFactory.
	 * 
	 * @return Session instance
	 * @throws DatabaseException
	 */
	public static Session openSession() throws DatabaseException {
		if (DbConnectionManager.instance == null) {
			DbConnectionManager.instance = DbConnectionManager.getInstance();
		}
		try {
			return DbConnectionManager.instance.sf.openSession();
		} catch (HibernateException e) {
			throw new DatabaseException("Cannot create hibernate session: " + e);
		}
	}

	public static Session getCurrentSession() throws DatabaseException {
		Session s = DbConnectionManager.privSession.get();
		if (s == null) {
			s = DbConnectionManager.openSession();
			DbConnectionManager.privSession.set(s);
		}
		return s;
	}

	public static void closeSession() {
		Session s = DbConnectionManager.privSession.get();
		DbConnectionManager.privSession.set(null);
		if (s != null) {
			s.close();
		}
	}

	@Override
	protected void finalize() throws DatabaseException {
		Session s = DbConnectionManager.privSession.get();
		if (s != null) {
			s.close();
		}
	}
}
