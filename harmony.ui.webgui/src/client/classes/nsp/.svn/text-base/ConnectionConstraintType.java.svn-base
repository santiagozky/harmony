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

public class ConnectionConstraintType extends ConnectionType implements
        Serializable, Cloneable {

    /**
     *
     */
    private static final long serialVersionUID = 2232166313075753982L;
    protected int minBW;
    protected Integer maxBW;
    protected Integer maxDelay;
    protected Long dataAmount;

    public int getMinBW() {
        return this.minBW;
    }

    public void setMinBW(final int value) {
        this.minBW = value;
    }

    public boolean isSetMinBW() {
        return true;
    }

    public Integer getMaxBW() {
        return this.maxBW;
    }

    public void setMaxBW(final Integer value) {
        this.maxBW = value;
    }

    public boolean isSetMaxBW() {
        return (this.maxBW != null);
    }

    public Integer getMaxDelay() {
        return this.maxDelay;
    }

    public void setMaxDelay(final Integer value) {
        this.maxDelay = value;
    }

    public boolean isSetMaxDelay() {
        return (this.maxDelay != null);
    }

    public Long getDataAmount() {
        return this.dataAmount;
    }

    public void setDataAmount(final Long value) {
        this.dataAmount = value;
    }

    public boolean isSetDataAmount() {
        return (this.dataAmount != null);
    }

}
