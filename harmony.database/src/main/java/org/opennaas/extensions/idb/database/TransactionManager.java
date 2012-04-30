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

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.opennaas.extensions.idb.exception.database.DatabaseException;

/**
 * This class takes care of accessing the database in a nice and tidy way. It
 * opens a session, opens a transaction in this session, executes a database
 * operation, commits the transaction, and closes the session. If anything goes
 * wrong, the transaction is rolled back and a DatabaseException is thrown.
 * 
 * The actual database operation is not part of this class, but is an abstract
 * method dbOperation() that has to be defined in a child class.
 * 
 * @author Christian de Waal
 * 
 */
public abstract class TransactionManager {
	/**
	 * This EntityManager object can be used from within the dbOperation()
	 * method to access the database.
	 */
	protected EntityManager session = null;
	/**
	 * Any object that can be handed to the TransactionManager through the
	 * appropriate constructor.
	 */
	protected Object arg = null;
	/**
	 * Any kind of result of the database operation that can be retrieved using
	 * the getResult() method.
	 */
	protected Object result = null;

	public Set<Object> refreshObjects;

	/** Default constructor. */
	public TransactionManager() throws DatabaseException {
		start();
	}

	/** Constructor with some additional argument. */
	public TransactionManager(Object arg) throws DatabaseException {
		this.arg = arg;
		start();
	}

	/** Constructor with some additional argument. */
	public TransactionManager(HashSet<Object> arg) throws DatabaseException {
		this.refreshObjects = arg;
		start();
	}

	/** Constructor with some additional argument. */
	public TransactionManager(Object arg1, HashSet<Object> arg2)
			throws DatabaseException {
		this.arg = arg1;
		this.refreshObjects = arg2;
		start();
	}

	/** Retrieve result of a database operation. */
	public Object getResult() {
		return this.result;
	}

	/**
	 * Execute the database operation, take care of cleaning up in case of
	 * errors.
	 */
	private void start() throws DatabaseException {
		this.session = DbConnectionManager.getCurrentSession();

		final EntityTransaction trans = this.session.getTransaction();
		trans.begin();
		DatabaseException exception = null;
		try {
			this.dbOperation();
			this.session.flush();
			trans.commit();
			// this.session.close();
		} catch (final Exception ex) {
			exception = new DatabaseException(ex.getMessage(), ex);
		}

		if (exception != null) {
			trans.rollback();
			DbConnectionManager.closeSession();
			throw exception;
		}

		// if transaction was successfull, refres dependent object in session
		refreshObjects(this.session);
	}

	private void refreshObjects(EntityManager sess) {
		if (refreshObjects != null) {
			for (Object o : refreshObjects) {
				sess.refresh(o);
			}
		}
	}

	/** The actual database operation to be defined in a child class. */
	protected abstract void dbOperation() throws Exception;
}
