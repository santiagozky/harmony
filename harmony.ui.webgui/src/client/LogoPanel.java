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


/*
 * Copyright 2007 Google Inc. Licensed under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package client;

import client.helper.RpcRequest;
import client.template.dialog.ErrorDialog;
import client.template.dialog.LoginDialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ImageBundle;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * the panel showing the logo and the login-link.
 * 
 * @author claus
 */
public class LogoPanel extends Composite implements AsyncCallback {

    private final Label label = new Label();
    private final Label aai = new Label();
    private final Label link = new Label();

    /** Image Class for Logo Panel. */
    private static final Images images = (Images) GWT.create(Images.class);

    private static LogoPanel instance = null;

    public static final LogoPanel getInstance() {
        if (LogoPanel.instance == null) {
            LogoPanel.instance = new LogoPanel(LogoPanel.images);
        }

        return LogoPanel.instance;
    }

    private final ClickListener SIGN_IN_LISTENER = new ClickListener() {
        public void onClick(final Widget sender) {
            final LoginDialog dialog = new LoginDialog("Sign In");
            dialog.show();
            dialog.center();
        }
    };

    private final ClickListener SIGN_OUT_LISTENER = new ClickListener() {
        public void onClick(final Widget sender) {
            RpcRequest.common().logOut(LogoPanel.this);
        }
    };

    /**
     * class that holds the logo.
     * 
     * @author claus
     */
    public interface Images extends ImageBundle {
        @Resource("public/phosphoruslogo2.png")
        AbstractImagePrototype phosphoruslogo2();
    }

    public final void setSigendOut() {
        this.link.setText("sign in");
        this.label.setText("Not logged in");

        this.link.removeClickListener(this.SIGN_OUT_LISTENER);
        this.link.addClickListener(this.SIGN_IN_LISTENER);
    }

    public final void setSigendIn(final String username) {
        this.link.setText("sign out");
        this.label.setText("Logged in as " + username);

        this.link.removeClickListener(this.SIGN_IN_LISTENER);
        this.link.addClickListener(this.SIGN_OUT_LISTENER);
    }

    /**
     * default constructor.
     * 
     * @param images
     */
    private LogoPanel(final Images images) {
        final VerticalPanel outer = new VerticalPanel();

        outer.setWidth("100%");
        outer.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        final Image logo = images.phosphoruslogo2().createImage();

        RpcRequest.common().isLoggedIn(new AsyncCallback() {
            public void onFailure(final Throwable caught) {
                LogoPanel.this.setSigendOut();
            }

            public void onSuccess(final Object result) {
                LogoPanel.this.setSigendIn((String) result);
            }
        });
        
        RpcRequest.common().isSecure(new AsyncCallback() {
            public void onFailure(final Throwable caught) {
                LogoPanel.this.aai.setStyleName("gui-TopPanel-Label");
                LogoPanel.this.aai.setText("Unable to determine Security-Status");
            }

            public void onSuccess(final Object result) {
                boolean res = ((Boolean) result).booleanValue();
                
                if (res) {
                    LogoPanel.this.aai.setStyleName("gui-TopPanel-Sec");
                    LogoPanel.this.aai.setText("Security Toolkit available!");
                } else {
                    LogoPanel.this.aai.setStyleName("gui-TopPanel-UnSec");
                    LogoPanel.this.aai.setText("Unsecured");
                }
            }
        });

        outer.add(logo);

        this.label.setStyleName("gui-TopPanel-Label");
        this.link.setStyleName("gui-TopPanel-Link");

        final HorizontalPanel panel = new HorizontalPanel();
        panel.setWidth("100%");
        panel.setStyleName("gui-TopPanel-Status");
        panel.add(this.label);
        panel.add(this.aai);
        panel.add(this.link);

        outer.add(panel);
        panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

        this.initWidget(outer);
        this.setStyleName("gui-TopPanel");
    }

    public void onFailure(final Throwable caught) {
        final ErrorDialog dialog = new ErrorDialog("Error", caught);
        dialog.show();
        dialog.center();
    }

    public void onSuccess(final Object result) {
        this.setSigendOut();
    }

}
