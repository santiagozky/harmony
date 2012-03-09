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

import client.classes.Router;
import client.helper.GuiLogger;
import client.helper.RpcRequest;
import client.template.dialog.ErrorDialog;
import client.template.dialog.InfoDialog;
import client.template.page.CreatePage;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author claus
 * 
 */
public class ArgonRouterCreatePage extends CreatePage {

    private final DockPanel dockPanel = new DockPanel();

    private final TextBox loopBackAd = new TextBox();
    private final TextBox configAd = new TextBox();
    // TODO: how to choose between the values that are allowed?
    private final TextBox type = new TextBox();
    private final Button button = new Button();
    private final Image activity = new Image("ajaxloader.gif");

    /**
	 * 
	 */
    public ArgonRouterCreatePage() {
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

        final String message = "Router created and inserted";

        final InfoDialog dialog =
                new InfoDialog("Create Router", new HTML(message));

        dialog.show();
        dialog.center();

    }

    private void sendRouter(final Router router) {
        GuiLogger.traceLog("Sending create router: "
                + router.getLoopBackAddress() + ", "
                + router.getConfigurationAddress() + ", " + router.getType()
                + ", " + router.getValidTo().toString());
        RpcRequest.argon().addRouter(router, this);
    }

    /**
     * executed, when
     */
    public void onClick(final Widget sender) {
        // TODO Auto-generated method stub

        // build the Router and send it to op-intf
        this.activity.setVisible(true);
        this.button.setEnabled(false);

        final String loopBackAd = this.loopBackAd.getText();
        final String configAd = this.configAd.getText();
        final String type = this.type.getText();

        final Router router = new Router(loopBackAd, configAd, type);

        this.sendRouter(router);

    }

    private final Widget getEditPanel() {
        final VerticalPanel outerPanel = new VerticalPanel();

        final FlexTable table = new FlexTable();
        outerPanel.setSpacing(5);

        final Label labelLoopB = new Label();
        labelLoopB.setText("LoopBack: ");
        table.setWidget(0, 0, labelLoopB);
        table.setWidget(0, 1, this.loopBackAd);
        //
        final Label labelConfig = new Label();
        labelConfig.setText("Configuration: ");
        table.setWidget(1, 0, labelConfig);
        table.setWidget(1, 1, this.configAd);
        //
        final Label labelType = new Label();
        labelType.setText("Type: ");
        table.setWidget(2, 0, labelType);
        table.setWidget(2, 1, this.type);

        this.button.addClickListener(this);
        this.button.setText("Create");
        table.setWidget(3, 0, this.button);
        table.setWidget(3, 1, this.activity);
        // table.getFlexCellFormatter().setColSpan(3, 1, 3);

        return table;
    }

}
