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
package client.reservation.panel;

import java.util.Date;

import client.classes.nsp.CreateReservationType;
import client.helper.GuiLogger;
import client.helper.RpcRequest;
import client.reservation.listener.CreateClickListener;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

/**
 * @author jangassen
 * 
 */
public class NspFixedReservationCreatePanel extends NspReservationCreatePanel {
    /**
     * 
     */
    private final TextBox start = new TextBox();
    /**
     * 
     */
    private final TextBox duration = new TextBox();

    /**
     * 
     */
    public NspFixedReservationCreatePanel(final String epr) {
        super(epr);

        final Label labelStart = new Label();
        labelStart.setText("Start: ");
        this.start.setText(this.dateTimeFormat.format(new Date()));
        this.table.setWidget(2, 0, labelStart);
        this.table.setWidget(2, 1, this.start);

        final Label labelDuration = new Label();
        labelDuration.setText("Duration: ");
        this.duration.setText(800 + "");
        this.table.setWidget(2, 2, labelDuration);
        this.table.setWidget(2, 3, this.duration);
    }

    @Override
    protected CreateClickListener getClickListener() {
        return new CreateClickListener(this);
    }

    /**
     * Is called by superclass when button is clicked. Performes a create
     * Reservation Request on the NSP with given epr.
     */
    @Override
    public void sendRequest(final CreateReservationType reservation) {
        GuiLogger.traceLog("Sending create reservation request to " + this.epr);

        if (this.isArgon()) {
            RpcRequest.argon().createReservation(reservation, this);
        } else {
            RpcRequest.nsp().createReservation(this.epr, reservation, this);
        }
    }

    /**
     * @return the start
     */
    public final TextBox getStart() {
        return this.start;
    }

    /**
     * @return the duration
     */
    public final TextBox getDuration() {
        return this.duration;
    }

    /*
     * (non-Javadoc)
     * 
     * @see client.reservation.panel.NspReservationCreatePanel#update()
     */
    @Override
    public void update() {
        this.start.setText(this.dateTimeFormat.format(new Date()));
    }

}
