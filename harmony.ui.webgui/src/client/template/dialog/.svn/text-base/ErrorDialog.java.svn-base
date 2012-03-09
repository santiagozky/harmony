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

import client.helper.GuiException;
import client.helper.GuiLogger;
import client.helper.StringUtils;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ErrorDialog extends DialogBox {

    private final VerticalPanel vPanel = new VerticalPanel();

    public ErrorDialog(final String title, final Throwable caught) {

        String message;

        if ((null == caught.getMessage()) || ("" == caught.getMessage())) {
            message = "Unknown reason (" + caught.toString() + ")";
        } else {
            message = caught.getMessage();
        }

        GuiLogger.errorLog(message);
        
        message = StringUtils.escape(message);

        String stackTrace = null;
        try {
            final GuiException ex = (GuiException) caught;

            stackTrace = ex.getStackTraceString();
        } catch (final Exception e) {
            stackTrace = GuiException.exception2string(caught);
        }

        this.setText(title);
        this.vPanel.add(new HTML(message + "<br>&nbsp;<br>"));

        if ((null != stackTrace) && ("" != stackTrace)) {
            final DisclosurePanel details = new DisclosurePanel("Details");

            final HTML stackTraceWidget = new HTML(stackTrace);
            stackTraceWidget.setStyleName("gui-ErrorPanel");

            details.add(stackTraceWidget);
            this.vPanel.add(details);
        }

        this.vPanel.setStyleName("gwt-DialogBoxContent");

        this.vPanel.add(new Button("Close", new ClickListener() {
            public void onClick(final Widget sender) {
                ErrorDialog.this.hide();
            }
        }));

        this.setWidget(this.vPanel);
    }

    @Override
    public boolean onKeyDownPreview(final char key, final int modifiers) {
        switch (key) {
        case KeyboardListener.KEY_ENTER:
        case KeyboardListener.KEY_ESCAPE:
            this.hide();
            break;
        }

        return true;
    }
}
