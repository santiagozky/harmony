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

public class ServiceConstraintType implements Serializable, Cloneable {

    /**
     *
     */
    private static final long serialVersionUID = 7261496512686059776L;
    protected int serviceID;
    protected ReservationType typeOfReservation;
    protected FixedReservationConstraintType fixedReservationConstraints;
    protected DeferrableReservationConstraintType deferrableReservationConstraints;
    protected MalleableReservationConstraintType malleableReservationConstraints;
    protected boolean automaticActivation;
    protected List<client.classes.nsp.ConnectionConstraintType> connections;

    /**
     * Gets the value of the serviceID property.
     * 
     */
    public int getServiceID() {
        return this.serviceID;
    }

    /**
     * Sets the value of the serviceID property.
     * 
     */
    public void setServiceID(final int value) {
        this.serviceID = value;
    }

    public boolean isSetServiceID() {
        return true;
    }

    public ReservationType getTypeOfReservation() {
        return this.typeOfReservation;
    }

    public void setTypeOfReservation(final ReservationType value) {
        this.typeOfReservation = value;
    }

    public boolean isSetTypeOfReservation() {
        return (this.typeOfReservation != null);
    }

    public FixedReservationConstraintType getFixedReservationConstraints() {
        return this.fixedReservationConstraints;
    }

    public void setFixedReservationConstraints(
            final FixedReservationConstraintType value) {
        this.fixedReservationConstraints = value;
    }

    public boolean isSetFixedReservationConstraints() {
        return (this.fixedReservationConstraints != null);
    }

    public DeferrableReservationConstraintType getDeferrableReservationConstraints() {
        return this.deferrableReservationConstraints;
    }

    public void setDeferrableReservationConstraints(
            final DeferrableReservationConstraintType value) {
        this.deferrableReservationConstraints = value;
    }

    public boolean isSetDeferrableReservationConstraints() {
        return (this.deferrableReservationConstraints != null);
    }

    public MalleableReservationConstraintType getMalleableReservationConstraints() {
        return this.malleableReservationConstraints;
    }

    public void setMalleableReservationConstraints(
            final MalleableReservationConstraintType value) {
        this.malleableReservationConstraints = value;
    }

    public boolean isSetMalleableReservationConstraints() {
        return (this.malleableReservationConstraints != null);
    }

    public boolean isAutomaticActivation() {
        return this.automaticActivation;
    }

    public void setAutomaticActivation(final boolean value) {
        this.automaticActivation = value;
    }

    public boolean isSetAutomaticActivation() {
        return true;
    }

    public List<client.classes.nsp.ConnectionConstraintType> getConnections() {
        if (this.connections == null) {
            this.connections =
                    new ArrayList<client.classes.nsp.ConnectionConstraintType>();
        }
        return this.connections;
    }

    public boolean isSetConnections() {
        return ((this.connections != null) && (!this.connections.isEmpty()));
    }

    public void unsetConnections() {
        this.connections = null;
    }

}
