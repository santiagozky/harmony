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


package client.reservation.dialog;

import client.classes.nsp.GetStatusResponseType;
import client.classes.nsp.GetStatusType;
import client.helper.GuiException;
import client.helper.PopupOutput;
import client.helper.RpcRequest;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Class to retrieve and display details of Domain Service States on demand and
 * display as popup window.
 * 
 * @author gassen
 */
public class ReservationDetailDialog extends DialogBox implements AsyncCallback {

    /** Reservation Id. */
    private final String reservationId;
    /** Endpoint Reference. */
    private final String epr;

    /** Content Panel. */
    private final VerticalPanel vPanel = new VerticalPanel();

    /**
     * Constructor.
     * 
     * @param epr
     *            Endpoint Reference for Server
     * @param id
     *            Reservation Id
     */
    public ReservationDetailDialog(final String epr, final String id) {
        this.reservationId = id;
        this.epr = epr;

        this.setText("Detail " + this.reservationId);
        this.setWidget(new Image("ajaxloader.gif"));
    }

    /**
     * Set popup Content.
     * 
     * @param message
     *            HTML Content
     */
    public void setContent(final HTML message) {
        this.vPanel.clear();
        this.vPanel.add(message);
        this.vPanel.setStyleName("gwt-DialogBoxContent");
        this.vPanel.add(new Button("Close", new ClickListener() {
            public void onClick(final Widget sender) {
                ReservationDetailDialog.this.hide();
            }
        }));
        this.setWidget(this.vPanel);
    }

    /**
     * Get Status informations by using ajax and show popup.
     */
    @Override
    public void show() {
        final GetStatusType request = new GetStatusType();
        request.setReservationID(this.reservationId);

        RpcRequest.nsp().getStatus(this.epr, request, this);

        super.show();
    }

    /**
     * Error handling.
     */
    public void onFailure(final Throwable caught) {
        String stackTrace = null;
        try {
            final GuiException ex = (GuiException) caught;

            stackTrace = ex.getStackTraceString();
        } catch (final Exception e) {
            stackTrace = GuiException.exception2string(caught);
        }

        this.setContent(new HTML("Error: " + caught.getMessage() + "<br><br>"
                + stackTrace));
    }

    /**
     * Success Handing. Format result Object as human readable text and put it
     * into the Popup Window.
     */
    public void onSuccess(final Object result) {
        final GetStatusResponseType resultObject =
                (GetStatusResponseType) result;

        this.setContent(new HTML(PopupOutput.format(resultObject)));
        /*
         * Status status = (Status) result; String message = ""; for (int x = 0;
         * x < status.getServiceStates().size(); x++) { ServiceStatus
         * serviceStatus = (ServiceStatus) status.getServiceStates().get(x);
         * message += "<b><big>Service " + serviceStatus.getServiceId() +
         * ":</big></b><br>"; for (int y = 0; y <
         * serviceStatus.getDomainStates().size(); y++) { DomainStatus
         * domainState = (DomainStatus) serviceStatus.getDomainStates().get(y);
         * message += "<b>Domain: " + domainState.getDomain() + "</b><br>";
         * message += "Source: " + domainState.getSource() + "<br>"; message +=
         * "Destination: " + domainState.getDestination() + "<br>"; message +=
         * "Status: " + domainState.getStatus() + "<br>"; message +=
         * "Actual BW: " + domainState.getActualBW() + "<br>"; } }
         * setContent(new HTML(message));
         */
    }
}
