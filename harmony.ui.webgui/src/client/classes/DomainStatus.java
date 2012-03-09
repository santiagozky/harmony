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

/**
 * Class representing Domain Status informations.
 * 
 * @author gassen
 */
public class DomainStatus implements Serializable {
    /** Version Id. */
    private static final long serialVersionUID = 1L;
    /** Domain Id. */
    private String domain;
    /** Source Endpoint Id. */
    private String source;
    /** Destination Endpoint Id. */
    private String destination;
    /** Domain Status. */
    private String status;
    /** Actual Bandwith. */
    private int actualBW;

    /**
     * Get actual BW.
     * 
     * @return the actualBW
     */
    public final int getActualBW() {
        return this.actualBW;
    }

    /**
     * Get Destinaton Endpoint Id.
     * 
     * @return the destination
     */
    public final String getDestination() {
        return this.destination;
    }

    /**
     * Get Domain Id.
     * 
     * @return the domain
     */
    public final String getDomain() {
        return this.domain;
    }

    /**
     * Get Source Endpoint Id.
     * 
     * @return the source
     */
    public final String getSource() {
        return this.source;
    }

    /**
     * Get Domain Status.
     * 
     * @return the status
     */
    public final String getStatus() {
        return this.status;
    }

    /**
     * Set actual BW.
     * 
     * @param actualBW
     *            the actualBW to set
     */
    public final void setActualBW(final int actualBW) {
        this.actualBW = actualBW;
    }

    /**
     * Set Destinaton Endpoint Id.
     * 
     * @param destination
     *            the destination to set
     */
    public final void setDestination(final String destination) {
        this.destination = destination;
    }

    /**
     * Set Domain Id.
     * 
     * @param domain
     *            the domain to set
     */
    public final void setDomain(final String domain) {
        this.domain = domain;
    }

    /**
     * Set Source Endpoint Id.
     * 
     * @param source
     *            the source to set
     */
    public final void setSource(final String source) {
        this.source = source;
    }

    /**
     * Set Domain Status.
     * 
     * @param status
     *            the status to set
     */
    public final void setStatus(final String status) {
        this.status = status;
    }

}
