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


package client.helper;

import java.util.Vector;

import client.classes.CancelResponse;
import client.template.dialog.ErrorDialog;
import client.template.dialog.InfoDialog;
import client.template.panel.DecisionPanel;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;

/**
 * Callback handler for execution Button.
 * 
 * @author gassen
 */
public class ExecutionCallbackHandler implements AsyncCallback {
    /** Popup Title. */
    private String popUpTitle = "Execution";
    /** Panel containing butoon and dropdown. */
    private DecisionPanel panel = null;

    /**
     * Default Constructor.
     */
    public ExecutionCallbackHandler() {
        super();
    }

    /**
     * Constructor.
     * 
     * @param panel
     *            Execution Panel
     * @param title
     *            Popup Title
     */
    public ExecutionCallbackHandler(final DecisionPanel panel,
            final String title) {
        super();

        this.panel = panel;
        this.popUpTitle = title;
    }

    /**
     * Handle errors during RPC request.
     * 
     * @param caught
     *            Caught Exception
     */
    public void onFailure(final Throwable caught) {
        this.panel.release();

        final ErrorDialog dialog = new ErrorDialog("Failure", caught);

        dialog.show();
        dialog.center();
    }

    /**
     * Handle RPC result.
     * 
     * Display results as popup.
     * 
     * @param result
     *            Result from Server
     */
    public void onSuccess(final Object result) {
        this.panel.release();

        final Vector response = (Vector) result;

        String message = "";

        for (int i = 0; i < response.size(); i++) {
            final CancelResponse detail = (CancelResponse) response.get(i);

            message +=
                    "Id: " + detail.getId() + ": " + detail.isStatus() + "<br>";
        }

        final InfoDialog dialog =
                new InfoDialog(this.popUpTitle, new HTML(message));

        dialog.show();
        dialog.center();
    }

    public String getPopUpTitle() {
        return this.popUpTitle;
    }

    public void setPopUpTitle(final String popUpTitle) {
        this.popUpTitle = popUpTitle;
    }

    public DecisionPanel getPanel() {
        return this.panel;
    }

    public void setPanel(final DecisionPanel panel) {
        this.panel = panel;
    }

}
