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


package client.classes.nsp;

import java.io.Serializable;

public class EndpointType implements Serializable, Cloneable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    protected String endpointId;
    protected String name;
    protected String description;
    protected EndpointInterfaceType _interface;
    protected String domainId;
    protected Integer bandwidth;

    public String getEndpointId() {
        return this.endpointId;
    }

    public void setEndpointId(final String value) {
        this.endpointId = value;
    }

    public boolean isSetEndpointId() {
        return (this.endpointId != null);
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String value) {
        this.name = value;
    }

    public boolean isSetName() {
        return (this.name != null);
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String value) {
        this.description = value;
    }

    public boolean isSetDescription() {
        return (this.description != null);
    }

    public EndpointInterfaceType getInterface() {
        return this._interface;
    }

    public void setInterface(final EndpointInterfaceType value) {
        this._interface = value;
    }

    public boolean isSetInterface() {
        return (this._interface != null);
    }

    public String getDomainId() {
        return this.domainId;
    }

    public void setDomainId(final String value) {
        this.domainId = value;
    }

    public boolean isSetDomainId() {
        return (this.domainId != null);
    }

    public Integer getBandwidth() {
        return this.bandwidth;
    }

    public void setBandwidth(final Integer value) {
        this.bandwidth = value;
    }

    public boolean isSetBandwidth() {
        return (this.bandwidth != null);
    }

}
