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


package client.classes;

import java.io.Serializable;

/**
 * Cancel Reservation Response.
 * 
 * @author gassen
 */
public class CancelResponse implements Serializable {
    /** Version Id. */
    private static final long serialVersionUID = 1L;
    /** Reservation Id. */
    private String id;
    /** Cancel Status. */
    private boolean status;

    /**
     * Get Reservation Id.
     * 
     * @return Reservation Id
     */
    public String getId() {
        return this.id;
    }

    /**
     * Set Reservation Id.
     * 
     * @param id
     *            Reservation Id
     */
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * Get Cancel Status.
     * 
     * @return True if success
     */
    public boolean isStatus() {
        return this.status;
    }

    /**
     * Set cancel status.
     * 
     * @param status
     *            Cancel Status
     */
    public void setStatus(final boolean status) {
        this.status = status;
    }
}
