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

import java.util.Vector;

import client.helper.ExecutionCallbackHandler;
import client.helper.RpcRequest;
import client.template.dialog.InfoDialog;
import client.template.panel.RouterListPanel;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DialogBox;

public class ArgonRouterListPanel extends RouterListPanel {

    public ArgonRouterListPanel() {
        super();
    }

    @Override
    public final void refresh(final int value, final AsyncCallback callback) {

        RpcRequest.argon().getRouters(callback);

    }

    @Override
    public final void execute(final String type, final Vector checked,
            final ExecutionCallbackHandler callback) {

        if (type.compareTo("Disable") == 0) {
            final DialogBox info =
                    new InfoDialog("Failure", type + " is not yet supported");
            info.center();
            info.show();
            callback.onFailure(new Throwable(new RuntimeException()));
        } else if (type.compareTo("Modify") == 0) {
            final DialogBox info =
                    new InfoDialog("Failure", type + " is not yet supported");
            info.center();
            info.show();
            callback.onFailure(new Throwable(new RuntimeException()));
        } else if (type.compareTo("Delete") == 0) {
            RpcRequest.argon().removeRouter(checked, callback);
        }

    }
}
