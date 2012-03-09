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
import java.util.Date;

/**
 * class that holds information about routers.
 * 
 * @author claus
 * 
 */
public class Router implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 4476943969783125121L;
    private String loopBackAddress;
    private String configurationAddress;
    private String type;

    private String popup;

    private Date validTo;

    public Router() {

    }

    public Date getValidTo() {
        return this.validTo;
    }

    public void setValidTo(final Date validTo) {
        this.validTo = validTo;
    }

    public Router(final String loopBackAddress, final String configurationIp,
            final String type, final Date validTo) {
        super();
        this.loopBackAddress = loopBackAddress;
        this.configurationAddress = configurationIp;
        this.type = type;
        this.validTo = validTo;
    }

    public Router(final String loopBackAddress, final String configurationIp,
            final String type) {
        super();
        this.loopBackAddress = loopBackAddress;
        this.configurationAddress = configurationIp;
        this.type = type;
        this.validTo =
                new Date(System.currentTimeMillis() + 10 * 365 * 24 * 60 * 60
                        * 1000);
    }

    public String getConfigurationAddress() {
        return this.configurationAddress;
    }

    public void setConfigurationAddress(final String configurationIp) {
        this.configurationAddress = configurationIp;
    }

    public String getLoopBackAddress() {
        return this.loopBackAddress;
    }

    public void setLoopBackAddress(final String loopBackAddress) {
        this.loopBackAddress = loopBackAddress;
    }

    public String getType() {
        return this.type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result =
                prime
                        * result
                        + ((this.configurationAddress == null) ? 0
                                : this.configurationAddress.hashCode());
        result =
                prime
                        * result
                        + ((this.loopBackAddress == null) ? 0
                                : this.loopBackAddress.hashCode());
        result =
                prime * result
                        + ((this.type == null) ? 0 : this.type.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        final Router other = (Router) obj;
        if (this.configurationAddress == null) {
            if (other.configurationAddress != null) {
                return false;
            }
        } else if (!this.configurationAddress
                .equals(other.configurationAddress)) {
            return false;
        }
        if (this.loopBackAddress == null) {
            if (other.loopBackAddress != null) {
                return false;
            }
        } else if (!this.loopBackAddress.equals(other.loopBackAddress)) {
            return false;
        }
        if (this.type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!this.type.equals(other.type)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getLoopBackAddress();
    }

    public String getPopup() {
        return this.popup;
    }

    public void setPopup(final String popup) {
        this.popup = popup;
    }

}
