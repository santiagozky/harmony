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


package client.template.dialog;

import client.LogoPanel;
import client.helper.RpcRequest;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class LoginDialog extends DialogBox implements AsyncCallback {
    private final TextBox username;
    private final PasswordTextBox password;

    public LoginDialog(final String title) {
        this.setText(title);

        final FlexTable panel = new FlexTable();
        panel.setStyleName("gwt-DialogBoxContent");

        this.username = new TextBox();
        this.password = new PasswordTextBox();

        panel.setWidget(0, 0, new Label("Username "));
        panel.setWidget(0, 1, this.username);

        panel.setWidget(1, 0, new Label("Password "));
        panel.setWidget(1, 1, this.password);

        panel.setWidget(2, 0, new Button("Close", new ClickListener() {
            public void onClick(final Widget sender) {
                LoginDialog.this.hide();
            }
        }));

        panel.setWidget(2, 1, new Button("Sign In", new ClickListener() {
            public void onClick(final Widget sender) {
                LoginDialog.this.logIn();
            }
        }));

        this.setWidget(panel);
    }

    protected final void logIn() {
        RpcRequest.common().logIn(this.username.getText(),
                this.password.getText(), this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.google.gwt.user.client.ui.PopupPanel#show()
     */
    @Override
    public final void show() {
        super.show();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.google.gwt.user.client.ui.PopupPanel#onKeyDownPreview(char, int)
     */
    @Override
    public boolean onKeyDownPreview(final char key, final int modifiers) {
        switch (key) {
        case KeyboardListener.KEY_ENTER:
            this.logIn();
            break;
        case KeyboardListener.KEY_ESCAPE:
            this.hide();
            break;
        }

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.google.gwt.user.client.rpc.AsyncCallback#onFailure(java.lang.Throwable
     * )
     */
    public void onFailure(final Throwable caught) {
        this.hide();

        final ErrorDialog dialog = new ErrorDialog("Error", caught);
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
        final Boolean res = (Boolean) result;

        if (false == res.booleanValue()) {
            final LoginDialog dialog =
                    new LoginDialog("Wrong Username/Password... Try Again");
            dialog.show();
            dialog.center();
        } else {
            LogoPanel.getInstance().setSigendIn(this.username.getText());
        }

        this.hide();
    }
}
