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

import client.helper.GuiLogger;
import client.helper.RpcRequest;
import client.reservation.panel.NspReservationCreatePanel;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;

/**
 * @author gassen
 */
public abstract class ReservationCallbackListener implements AsyncCallback {

    private final NspReservationCreatePanel owner;

    private final String nameServer = "ip-lookup.ist-phosphorus.eu";

    private final String fqdn = "ist-phosphorus.eu";

    private final String lookUp;

    /**
     * @param ip
     * @return
     */
    private final String revertIp(final String ip) {
        String result = "";

        final String fields[] = ip.split("\\.");

        for (int x = fields.length - 1; x >= 0; x--) {
            result += fields[x];

            if (x != 0) {
                result += ".";
            }
        }

        return result;
    }

    /**
     * @param parent
     */
    public ReservationCallbackListener(final NspReservationCreatePanel parent) {
        this.owner = parent;

        String request = this.getTextBox().getText();

        if ((null != request) && !"".equals(request)) {

            if (!this.checkText(request)) {
                request = this.revertIp(request) + "." + this.nameServer;
            } else if (!request.endsWith(this.fqdn)) {
                request += "." + this.fqdn;
            }

            this.lookUp = request;

            GuiLogger.debugLog("Looking up:  " + this.lookUp);

            RpcRequest.common().resolveName(request, this);
        } else {
            this.lookUp = "";
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.google.gwt.user.client.rpc.AsyncCallback#onSuccess(java.lang.Object)
     */
    public void onSuccess(final Object result) {
        if (this.checkResult(result)) {
            this.getTextBox().setText((String) result);
        } else {
            GuiLogger.debugLog("No TNA found for  " + this.lookUp);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.google.gwt.user.client.rpc.AsyncCallback#onFailure(java.lang.Throwable
     * )
     */
    public void onFailure(final Throwable caught) {
        GuiLogger.debugLog(this.lookUp + " can't be resolved");
    }

    public abstract TextBox getTextBox();

    /**
     * @param text
     * @return
     */
    public final boolean checkText(final String text) {
        return (text.matches(".*[a-zA-Z].*"));
    }

    /**
     * @param result
     * @return
     */
    public final boolean checkResult(final Object result) {
        final String tna = (String) result;

        return ((null != tna) && !"".equals(tna));
    }

    /**
     * @return the owner
     */
    public final NspReservationCreatePanel getOwner() {
        return this.owner;
    }
}
