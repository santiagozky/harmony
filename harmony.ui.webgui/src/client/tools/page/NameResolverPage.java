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
package client.tools.page;

import client.helper.RpcRequest;
import client.template.dialog.ErrorDialog;
import client.template.dialog.InfoDialog;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author gassen
 */
public class NameResolverPage extends Composite implements ClickListener,
        AsyncCallback {
    private final TextBox query;
    private final Button button;

    /**
     * 
     */
    public NameResolverPage() {
        this.query = new TextBox();
        this.button = new Button("Resolve");

        this.button.addClickListener(this);

        final VerticalPanel panel = new VerticalPanel();

        panel.add(new HTML("Please enter hostname"));
        panel.add(this.query);
        panel.add(this.button);

        this.initWidget(panel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.google.gwt.user.client.ui.ClickListener#onClick(com.google.gwt.user
     * .client.ui.Widget)
     */
    public void onClick(final Widget sender) {
        RpcRequest.common().resolveName(this.query.getText(), this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.google.gwt.user.client.rpc.AsyncCallback#onFailure(java.lang.Throwable
     * )
     */
    public void onFailure(final Throwable caught) {
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
        final String ip = (String) result;

        final HTML text =
                new HTML(("".equals(ip)) ? "No TNA found for "
                        + this.query.getText() : this.query.getText() + ": "
                        + ip);

        final InfoDialog dialog = new InfoDialog("TNA Lookup", text);

        dialog.show();
        dialog.center();
    }
}
