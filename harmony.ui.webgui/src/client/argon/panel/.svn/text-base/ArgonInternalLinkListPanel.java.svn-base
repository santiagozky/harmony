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

import client.classes.InternalLink;
import client.classes.InternalLinkList;
import client.helper.ExecutionCallbackHandler;
import client.helper.RpcRequest;
import client.helper.RudListener;
import client.template.dialog.InfoDialog;
import client.template.panel.LinkListPanel;
import client.types.TableRow;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;

public class ArgonInternalLinkListPanel extends LinkListPanel {

    public ArgonInternalLinkListPanel() {
        super();
    }

    @Override
    public final void refresh(final int value, final AsyncCallback callback) {

        RpcRequest.argon().getInternalLinks(callback);

    }

    @Override
    public final void execute(final String type, final Vector checked,
            final ExecutionCallbackHandler callback) {

        if (type.compareTo("Enable/Disable") == 0) {

            RpcRequest.argon().disableInternalLink(checked, callback);

        } else if (type.compareTo("Modify") == 0) {
            final DialogBox info =
                    new InfoDialog("Failure", type + " is not yet supported");
            info.center();
            info.show();
            callback.onFailure(new Throwable(new RuntimeException()));
        } else if (type.compareTo("Delete") == 0) {
            RpcRequest.argon().removeInternalLink(checked, callback);
        }

    }

    /**
     * @param result
     *            the list of links
     */
    @Override
    public final void onData(final Object result) {

        final InternalLinkList links = (InternalLinkList) result;
        RudListener.getInstance().getChecked().clear();

        this.reset();

        for (int i = 0; i < links.getLinks().size(); ++i) {

            final TableRow row = new TableRow();
            final InternalLink l = ((InternalLink) links.getLinks().get(i));

            // TODO: make more generic
            this.fillTable(row, l);

            // the checkbox to delete a link
            final CheckBox deleteBox = new CheckBox();
            // Router can be identified by their LoopBackAddress
            deleteBox.setName(l.getUniqueLabel());
            deleteBox.addClickListener(RudListener.getInstance());
            row.addField(deleteBox);

            if (null != l.getPopup()) {
                row.setDialog(new InfoDialog("Info", l.getPopup()));
            }

            this.addRow(row);

        }

        if (0 == links.getLinks().size()) {
            this.addDummyRow("Nothing found...");
        }

        this.flush();

        // notify listeners (i.e. button and gif from ReservationAdmin)
    }

    /**
     * fills a specififc row whith information from link.
     * 
     * @param row
     *            the row to be filled
     * @param l
     *            the link to to added
     */
    private void fillTable(final TableRow row, final InternalLink l) {
        if (l.getUniqueLabel() != null) {
            row.addField(l.getUniqueLabel());
        } else {
            row.addField("");
        }
        if (l.getSourceNode() != null) {
            row.addField(l.getSourceNode());
        } else {
            row.addField("");
        }
        if (l.getOutgoingInterface() != null) {
            row.addField(l.getOutgoingInterface());
        } else {
            row.addField("");
        }
        if (l.getDestinationNode() != null) {
            row.addField(l.getDestinationNode());
        } else {
            row.addField("");
        }
        if (l.getIngoingInterface() != null) {
            row.addField(l.getIngoingInterface());
        } else {
            row.addField("");
        }
    }

}
