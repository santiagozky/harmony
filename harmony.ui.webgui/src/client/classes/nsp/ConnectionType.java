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

public class ConnectionType implements Serializable, Cloneable {

    private static final long serialVersionUID = -560905127670225960L;
    protected int connectionID;
    protected EndpointType source;
    protected List<client.classes.nsp.EndpointType> target;
    protected int directionality;

    public int getConnectionID() {
        return this.connectionID;
    }

    public void setConnectionID(final int value) {
        this.connectionID = value;
    }

    public boolean isSetConnectionID() {
        return true;
    }

    public EndpointType getSource() {
        return this.source;
    }

    public void setSource(final EndpointType value) {
        this.source = value;
    }

    public boolean isSetSource() {
        return (this.source != null);
    }

    public List<client.classes.nsp.EndpointType> getTarget() {
        if (this.target == null) {
            this.target = new ArrayList<client.classes.nsp.EndpointType>();
        }
        return this.target;
    }

    public boolean isSetTarget() {
        return ((this.target != null) && (!this.target.isEmpty()));
    }

    public void unsetTarget() {
        this.target = null;
    }

    public int getDirectionality() {
        return this.directionality;
    }

    public void setDirectionality(final int value) {
        this.directionality = value;
    }

    public boolean isSetDirectionality() {
        return true;
    }

}
