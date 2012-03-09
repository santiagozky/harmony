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


package client.argon.panel;

import java.util.Date;
import java.util.Vector;

import client.classes.Reservation;
import client.classes.Service;
import client.helper.ExecutionCallbackHandler;
import client.helper.RpcRequest;
import client.template.dialog.InfoDialog;
import client.template.panel.ReservationListPanel;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DialogBox;

public class ArgonReservationListPanel extends ReservationListPanel {

    private final Date now = new Date();

    public ArgonReservationListPanel() {
        super();
    }

    @Override
    public final void refresh(final int value, final AsyncCallback callback) {

        RpcRequest.argon().getReservations(callback);

    }

    @Override
    public final void execute(final String type, final Vector checked,
            final ExecutionCallbackHandler callback) {
        if (type.compareTo("Cancel") == 0) {
            RpcRequest.argon().cancelReservation(checked, callback);
        }
    }

    @Override
    public final DialogBox getPopUp(final Reservation reservation) {
        if (null != reservation.getPopup()) {
            return new InfoDialog("Info", reservation.getPopup());
        }

        return null;
    }

    @Override
    public final boolean isActive(final Service service, final Date start,
            final int duration) {
        final Date end = new Date();
        final long time = start.getTime() + duration * 1000;
        end.setTime(time);

        if (service.getStatus().compareTo("active") == 0) {
            return true;
        }

        return false;
    }

}
