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


package client.attic;

import client.interfaces.ArgonManagementService;
import client.interfaces.ArgonManagementServiceAsync;
import client.template.dialog.InfoDialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class CreateReservation extends Composite {

    // start
    // duration
    // typeOfReservation = FIXED("fixed"),
    // "minBW",
    // "maxBW",
    // "maxDelay",
    // "dataAmount"
    // "source",
    // "target",
    // "directionality"

    private final VerticalPanel vert;

    public CreateReservation() {
        final Button button = new Button();
        this.vert = new VerticalPanel();
        final ClickListener listener = new ClickListener() {
            public void onClick(final Widget sender) {

                final ArgonManagementServiceAsync emailService =
                        (ArgonManagementServiceAsync) GWT
                                .create(ArgonManagementService.class);

                final ServiceDefTarget endpoint =
                        (ServiceDefTarget) emailService;
                final String moduleRelativeURL =
                        GWT.getModuleBaseURL() + "/gui/server/argon";
                endpoint.setServiceEntryPoint(moduleRelativeURL);

                // (3) Create an asynchronous callback to handle the result.
                //
                final AsyncCallback callback = new AsyncCallback() {
                    public void onSuccess(final Object result) {
                        final InfoDialog dialog =
                                new InfoDialog("Result", (String) result);

                        dialog.show();
                        dialog.center();
                    }

                    public void onFailure(final Throwable caught) {
                        final InfoDialog dialog =
                                new InfoDialog("Failure", new HTML(caught
                                        .toString()));

                        dialog.show();
                        dialog.center();

                    }
                };

                emailService.getDomainName(callback);
            }

        };

        button.addClickListener(listener);
        button.setText("Click me");
        this.vert.add(button);
        this.vert.setWidth("100%");
        this.initWidget(this.vert);

    }

}
