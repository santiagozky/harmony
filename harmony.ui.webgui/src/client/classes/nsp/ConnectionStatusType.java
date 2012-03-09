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
import java.util.ArrayList;
import java.util.List;

public class ConnectionStatusType extends ConnectionType implements
        Serializable, Cloneable {

    private static final long serialVersionUID = -7329855967958629850L;
    protected StatusType status;

    protected List<client.classes.nsp.DomainConnectionStatusType> domainStatus;
    protected int actualBW;

    public StatusType getStatus() {
        return this.status;
    }

    public void setStatus(final StatusType value) {
        this.status = value;
    }

    public boolean isSetStatus() {
        return (this.status != null);
    }

    public List<client.classes.nsp.DomainConnectionStatusType> getDomainStatus() {
        if (this.domainStatus == null) {
            this.domainStatus =
                    new ArrayList<client.classes.nsp.DomainConnectionStatusType>();
        }
        return this.domainStatus;
    }

    public boolean isSetDomainStatus() {
        return ((this.domainStatus != null) && (!this.domainStatus.isEmpty()));
    }

    public void unsetDomainStatus() {
        this.domainStatus = null;
    }

    public int getActualBW() {
        return this.actualBW;
    }

    public void setActualBW(final int value) {
        this.actualBW = value;
    }

    public boolean isSetActualBW() {
        return true;
    }

}
