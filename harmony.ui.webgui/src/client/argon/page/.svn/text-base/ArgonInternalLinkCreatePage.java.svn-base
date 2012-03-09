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


package client.argon.page;

import client.classes.InternalLink;
import client.helper.GuiLogger;
import client.helper.RpcRequest;
import client.template.dialog.InfoDialog;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class ArgonInternalLinkCreatePage extends ArgonEndpointCreatePage {

    /**
		 * 
		 */
    public ArgonInternalLinkCreatePage() {
        super();
    }

    @Override
    public void onSuccess(final Object result) {
        this.activity.setVisible(false);
        this.button.setEnabled(true);

        final String message = "InternalLink created and inserted";

        final InfoDialog dialog =
                new InfoDialog("Create Internal Link", new HTML(message));

        dialog.show();
        dialog.center();

    }

    private void sendInternalLink(final InternalLink ep) {
        GuiLogger
                .traceLog("Sending createInternalLink: " + ep.getSourceNode()
                        + ", " + ep.getDestinationNode() + ", "
                        + ep.getIngoingInterface() + ", "
                        + ep.getOutgoingInterface() + ", " + ep.getDelay()
                        + ", " + ep.getBandwidth() + ", " + ep.getValidFrom()
                        + ", " + ep.getValidTo() + ", " + ep.isUp());
        RpcRequest.argon().addInternalLink(ep, this);
    }

    /**
     * executed, when
     */
    @Override
    public void onClick(final Widget sender) {

        // build the Router and send it to op-intf
        this.activity.setVisible(true);
        this.button.setEnabled(false);

        final InternalLink ep = new InternalLink();
        ep.setDestinationNode(this.destinationNode.getText());
        ep.setSourceNode(this.sourceNode.getText());
        ep.setIngoingInterface(this.ingoingInterface.getText());
        ep.setOutgoingInterface(this.outgoingInterface.getText());

        if (this.linkUp.getItemText(this.linkUp.getSelectedIndex()).equals(
                "true")) {
            ep.setUp(true);
        } else {
            ep.setUp(false);
        }

        // ep.setLinkUp(Boolean.parseBoolean(this.linkUp.getText()));
        ep.setValidFrom(this.dateTimeFormat.parse(this.validFrom.getText()));
        ep.setValidTo(this.dateTimeFormat.parse(this.validTo.getText()));

        ep.setBandwidth(Long.parseLong(this.bandwidth.getText()));
        ep.setDelay(Integer.parseInt(this.delay.getText()));

        this.sendInternalLink(ep);

    }

}
