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
package client.argon.page;

import java.util.Date;

import client.classes.Endpoint;
import client.helper.GuiLogger;
import client.helper.RpcRequest;
import client.template.dialog.ErrorDialog;
import client.template.dialog.InfoDialog;
import client.template.page.CreatePage;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author claus
 * 
 */
public class ArgonEndpointCreatePage extends CreatePage {

    private final DockPanel dockPanel = new DockPanel();

    protected final TextBox sourceNode = new TextBox();
    protected final TextBox destinationNode = new TextBox();
    protected final TextBox outgoingInterface = new TextBox();
    protected final TextBox ingoingInterface = new TextBox();
    protected final TextBox bandwidth = new TextBox();

    // TODO: automatically create those values?
    protected final TextBox validTo = new TextBox();
    protected final TextBox validFrom = new TextBox();

    // private final TextBox linkUp = new TextBox();
    protected final ListBox linkUp = new ListBox();
    protected final TextBox delay = new TextBox();

    protected final Button button = new Button();
    protected final Image activity = new Image("ajaxloader.gif");

    protected final DateTimeFormat dateTimeFormat =
            DateTimeFormat.getFormat("MM/dd/yy HH:mm:ss ZZZZ");

    /**
		 * 
		 */
    public ArgonEndpointCreatePage() {
        this.dockPanel.add(this.getEditPanel(), DockPanel.SOUTH);
        this.activity.setVisible(false);
        this.initWidget(this.dockPanel);

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.google.gwt.user.client.rpc.AsyncCallback#onFailure(java.lang.Throwable
     * )
     */
    public void onFailure(final Throwable caught) {
        this.activity.setVisible(false);
        this.button.setEnabled(true);

        final ErrorDialog dialog = new ErrorDialog("Failure", caught);

        dialog.show();
        dialog.center();

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.google.gwt.user.client.rpc.AsyncCallback#onSuccess(java.lang.Object)
     */
    public void onSuccess(final Object result) {
        this.activity.setVisible(false);
        this.button.setEnabled(true);

        final String message = "Endpoint created and inserted";

        final InfoDialog dialog =
                new InfoDialog("Create Endpoint", new HTML(message));

        dialog.show();
        dialog.center();

    }

    private void sendEndpoint(final Endpoint ep) {
        GuiLogger
                .traceLog("Sending create endpoint: " + ep.getSourceNode()
                        + ", " + ep.getDestinationNode() + ", "
                        + ep.getIngoingInterface() + ", "
                        + ep.getOutgoingInterface() + ", " + ep.getDelay()
                        + ", " + ep.getBandwidth() + ", " + ep.getValidFrom()
                        + ", " + ep.getValidTo() + ", " + ep.isUp());
        RpcRequest.argon().addEndpoint(ep, this);
    }

    /**
     * executed, when
     */
    public void onClick(final Widget sender) {

        // build the Router and send it to op-intf
        this.activity.setVisible(true);
        this.button.setEnabled(false);

        final Endpoint ep = new Endpoint();

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

        this.sendEndpoint(ep);

    }

    private final Widget getEditPanel() {
        final VerticalPanel outerPanel = new VerticalPanel();

        final FlexTable table = new FlexTable();
        outerPanel.setSpacing(5);

        final Label labelSource = new Label();
        labelSource.setText("SourceNode: ");
        table.setWidget(0, 0, labelSource);
        table.setWidget(0, 1, this.sourceNode);

        final Label labelDestination = new Label();
        labelDestination.setText("DestinationNode: ");
        table.setWidget(1, 0, labelDestination);
        table.setWidget(1, 1, this.destinationNode);

        final Label labelOutgoing = new Label();
        labelOutgoing.setText("Outgoing Interface: ");
        table.setWidget(2, 0, labelOutgoing);
        table.setWidget(2, 1, this.outgoingInterface);

        final Label labelIngoing = new Label();
        labelIngoing.setText("Ingoing Interface: ");
        table.setWidget(3, 0, labelIngoing);
        table.setWidget(3, 1, this.ingoingInterface);

        final Label labelBw = new Label();
        labelBw.setText("Bandwidth: ");
        this.bandwidth.setText("0");
        table.setWidget(4, 0, labelBw);
        table.setWidget(4, 1, this.bandwidth);

        final Label labelValidFrom = new Label();
        labelValidFrom.setText("Valid From: ");
        table.setWidget(5, 0, labelValidFrom);
        this.validFrom.setText(this.dateTimeFormat.format(new Date()));
        table.setWidget(5, 1, this.validFrom);

        final Label labelValidTo = new Label();
        labelValidTo.setText("Valid To: ");
        this.validTo.setText(this.dateTimeFormat.format(new Date()));
        table.setWidget(6, 0, labelValidTo);
        table.setWidget(6, 1, this.validTo);

        final Label labelLinkUp = new Label();
        labelLinkUp.setText("Link Up: ");
        table.setWidget(7, 0, labelLinkUp);
        this.linkUp.addItem("true");
        this.linkUp.addItem("false");
        table.setWidget(7, 1, this.linkUp);

        final Label labelDelay = new Label();
        labelDelay.setText("Delay: ");
        this.delay.setText("0");
        table.setWidget(8, 0, labelDelay);
        table.setWidget(8, 1, this.delay);

        this.button.addClickListener(this);
        this.button.setText("Create");
        table.setWidget(9, 0, this.button);
        table.setWidget(9, 1, this.activity);
        // table.getFlexCellFormatter().setColSpan(3, 1, 3);

        return table;
    }

}
