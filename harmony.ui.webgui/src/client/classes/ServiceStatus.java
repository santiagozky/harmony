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


/**
 *
 */
package client.classes;

import java.io.Serializable;
import java.util.Vector;

/**
 * Service Status Informations.
 * 
 * @author gassen
 */
public class ServiceStatus implements Serializable {
    /** Version Id. */
    private static final long serialVersionUID = 1L;

    /**
     * Domain states Vecor.
     */
    private Vector<client.classes.DomainStatus> domainStates = null;

    /** Service Id. */
    private int serviceId;

    /**
     * Default Constructor.
     */
    public ServiceStatus() {
        this.domainStates = new Vector<client.classes.DomainStatus>();
    }

    /**
     * Get Domain states Vector.
     * 
     * @return Domain states Vector
     */
    public Vector<client.classes.DomainStatus> getDomainStates() {
        return this.domainStates;
    }

    /**
     * Get Service Id.
     * 
     * @return the serviceId
     */
    public final int getServiceId() {
        return this.serviceId;
    }

    /**
     * Set Service Id.
     * 
     * @param serviceId
     *            the serviceId to set
     */
    public final void setServiceId(final int serviceId) {
        this.serviceId = serviceId;
    }
}
