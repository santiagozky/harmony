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

package org.opennaas.extensions.idb.database;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.opennaas.extensions.idb.exception.database.DatabaseException;

/**
 * This is a specific TransactionManager used to load objects from the database.
 * The object to be loaded is specified in the constructor by giving its type
 * and its primary database key.
 * 
 * @author Christian de Waal
 * 
 */
public class TransactionManagerLoad {
	final private Class<?> type;
	final private Serializable dbKey;
	private Object result = null;

	/**
	 * Constructor.
	 * 
	 * @param type
	 *            Type of object to be loaded (hibernate class).
	 * @param dbKey
	 *            Primary database key of object.
	 * @throws DatabaseException
	 */
	public TransactionManagerLoad(Class<?> type, Serializable dbKey)
			throws DatabaseException {
		this.type = type;
		this.dbKey = dbKey;
		dbOperation();
	}

	/** Retrieve result of a database operation. */
	public Object getResult() {
		return this.result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.opennaas.extensions.idb.database.TransactionManager#dbOperation()
	 */
	private void dbOperation() throws DatabaseException {
		int i = 2;

		EntityManager session = DbConnectionManager.getCurrentSession();

		DatabaseException exception = null;
		try {
			while ((this.result == null) && (i-- > 0)) {
				this.result = session.find(this.type, this.dbKey);
			}
		} catch (final Exception ex) {
			exception = new DatabaseException(ex.getMessage(), ex);
		}

		if (exception != null) {
			DbConnectionManager.closeSession();
			throw exception;
		}
	}

}
