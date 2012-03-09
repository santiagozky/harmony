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
import java.util.Vector;

import client.classes.Reservation;
import client.classes.Service;
import client.helper.ExecutionCallbackHandler;
import client.helper.RpcRequest;
import client.reservation.dialog.ReservationDetailDialog;
import client.template.panel.ReservationListPanel;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DialogBox;

/**
 * Nsp Reservation List Panel. This class creates the Table wich lists all
 * Reservations and treats Data events and the events fired by the different
 * buttons.
 * 
 * @author gassen
 */
public class NspReservationListPanel extends ReservationListPanel {

    /** NSP EPR */
    private final String epr;

    private final Date now = new Date();

    /**
     * Constructor.
     * 
     * @param epr
     *            NSP epr
     */
    public NspReservationListPanel(final String epr) {
        super();

        this.epr = epr;
    }

    /**
     * Handles the Refresh event, fired by Update Button.
     * 
     * @param value
     *            Timeframe
     * @param callback
     *            Callback Object for Result
     */
    @Override
    public void refresh(final int value, final AsyncCallback callback) {
        RpcRequest.nsp().getReservations(this.epr, value, callback);
    }

    /**
     * Handles the Execution Event fired by the Execution Button.
     * 
     * @param type
     *            Execution Type as String (not yet used...)
     * @param checked
     *            A Vector of all Checked ids.
     * @param callback
     *            Callback Object for Result
     */
    @Override
    public void execute(final String type, final Vector<String> checked,
            final ExecutionCallbackHandler callback) {
        RpcRequest.nsp().cancelReservation(this.epr, checked, callback);
    }

    /**
     * Creates a Popup the List Items.
     * 
     * @param reservation
     *            Reservation list Item
     */
    @Override
    public DialogBox getPopUp(final Reservation reservation) {
        return new ReservationDetailDialog(this.epr, reservation.getId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * client.template.panel.ReservationListPanel#isActive(client.classes.Service
     * , java.util.Date, int)
     */
    @Override
    public boolean isActive(final Service service, final Date start,
            final int duration) {
        final Date end = new Date();
        final long time = start.getTime() + duration * 1000;
        end.setTime(time);

        if (start.before(this.now) && end.after(this.now)) {
            return true;
        }

        return false;
    }

}
