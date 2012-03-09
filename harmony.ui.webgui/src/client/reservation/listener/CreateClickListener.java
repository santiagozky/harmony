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
package client.reservation.listener;

import client.classes.nsp.ConnectionConstraintType;
import client.classes.nsp.CreateReservationType;
import client.classes.nsp.EndpointType;
import client.classes.nsp.FixedReservationConstraintType;
import client.classes.nsp.MalleableReservationConstraintType;
import client.classes.nsp.ReservationType;
import client.classes.nsp.ServiceConstraintType;
import client.helper.GuiException;
import client.helper.GuiLogger;
import client.reservation.panel.NspFixedReservationCreatePanel;
import client.reservation.panel.NspMalleableReservationCreatePanel;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author gassen
 */
public class CreateClickListener extends ReservationClickListener {

    private final String type;

    public CreateClickListener(final NspFixedReservationCreatePanel parent) {
        super(parent);

        this.type = "fixed";
    }

    public CreateClickListener(final NspMalleableReservationCreatePanel parent) {
        super(parent);

        this.type = "malleable";
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.google.gwt.user.client.ui.ClickListener#onClick(com.google.gwt.user
     * .client.ui.Widget)
     */
    public void onClick(final Widget sender) {
        GuiLogger.debugLog("Creating " + this.type
                + " reservation create request");

        this.getOwner().setActive();

        final CreateReservationType createReservationType =
                new CreateReservationType();
        final ServiceConstraintType serviceType = new ServiceConstraintType();
        createReservationType.setJobID(new Long(0));

        final ReservationType reservationType = new ReservationType();
        reservationType.setValue(this.type);
        serviceType.setTypeOfReservation(reservationType);

        final ConnectionConstraintType connectionType =
                new ConnectionConstraintType();
        try {
            connectionType.setMinBW(Integer.parseInt(this.getOwner().getMinBW()
                    .getText()));
            final Integer maxBw =
                    new Integer(this.getOwner().getMaxBW().getText());
            if (maxBw.intValue() > 0) {
                connectionType.setMaxBW(maxBw);
            }
        } catch (final NumberFormatException ex) {
            this.getOwner().onFailure(ex);
            return;
        }

        final EndpointType srcEp = new EndpointType();
        srcEp.setEndpointId(this.getOwner().getSrc().getText());
        connectionType.setSource(srcEp);
        connectionType.setConnectionID(1);

        final EndpointType dstEp = new EndpointType();
        dstEp.setEndpointId(this.getOwner().getDst().getText());
        connectionType.getTarget().add(dstEp);

        connectionType.setDirectionality(1);

        serviceType.getConnections().add(connectionType);
        serviceType.setAutomaticActivation(true);
        serviceType.setServiceID((short) 1);

        createReservationType.getService().add(serviceType);

        if ("fixed".equals(this.type)) {
            final FixedReservationConstraintType fixed =
                    new FixedReservationConstraintType();

            final NspFixedReservationCreatePanel panel =
                    (NspFixedReservationCreatePanel) this.getOwner();

            fixed.setStartTime(panel.getDateTimeFormat().parse(
                    panel.getStart().getText()));
            fixed.setDuration(Integer.parseInt(panel.getDuration().getText()));

            serviceType.setFixedReservationConstraints(fixed);
        } else if ("malleable".equals(this.type)) {
            final MalleableReservationConstraintType malleable =
                    new MalleableReservationConstraintType();

            final NspMalleableReservationCreatePanel panel =
                    (NspMalleableReservationCreatePanel) this.getOwner();

            malleable.setDeadline(panel.getDateTimeFormat().parse(
                    panel.getDeadline().getText()));
            malleable.setStartTime(panel.getDateTimeFormat().parse(
                    panel.getStart().getText()));

            try {
                connectionType.setDataAmount(new Long(panel.getAmount()
                        .getText()));
            } catch (final NumberFormatException ex) {
                this.getOwner().onFailure(ex);

                return;
            }
            serviceType.setMalleableReservationConstraints(malleable);
        } else {
            this.getOwner().onFailure(
                    new GuiException("Invalid reservation Type"));
            return;
        }

        this.getOwner().sendRequest(createReservationType);
    }
}
