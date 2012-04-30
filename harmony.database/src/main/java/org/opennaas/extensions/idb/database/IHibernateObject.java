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

/**
 * Interface for all hibernate objects. Currently not used.
 *
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 * @version $Id: IHibernateObject.java 3575 2008-07-07 03:15:42Z willner@cs.uni-bonn.de $
 *
 */
public interface IHibernateObject {
    /**
     * Load hibernate object from the database.
     */
    void dbLoad();

    /**
     * Save hibernate object to the database.
     */
    void dbSave();

    /**
     * Delete hibernate object from the database.
     */
    void dbDelete();
}
