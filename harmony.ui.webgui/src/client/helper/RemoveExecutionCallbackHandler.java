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

import client.classes.RemoveResponse;
import client.template.dialog.InfoDialog;
import client.template.panel.DecisionPanel;

import com.google.gwt.user.client.ui.HTML;

public class RemoveExecutionCallbackHandler extends ExecutionCallbackHandler {

    public RemoveExecutionCallbackHandler(final DecisionPanel panel,
            final String title) {
        super(panel, title);

    }

    /**
     * Handle RPC result.
     * 
     * Display results as popup.
     * 
     * @param result
     *            Result from Server
     */
    @Override
    public void onSuccess(final Object result) {
        this.getPanel().release();

        final Vector response = (Vector) result;

        String message = "";

        if (response.size() <= 0) {
            message += "empty answer";
        }

        for (int i = 0; i < response.size(); i++) {
            final RemoveResponse detail = (RemoveResponse) response.get(i);

            message +=
                    "Id: " + detail.getId() + ": " + detail.isStatus()
                            + ",removalTime:"
                            + detail.getRemovalDate().toString() + "<br>";
        }

        final InfoDialog dialog =
                new InfoDialog(this.getPopUpTitle(), new HTML(message));

        dialog.show();
        dialog.center();
    }

}
