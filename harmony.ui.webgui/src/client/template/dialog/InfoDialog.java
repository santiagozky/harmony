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

import client.helper.GuiLogger;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class InfoDialog extends DialogBox {

    private final VerticalPanel vPanel = new VerticalPanel();
    private final String text;

    public InfoDialog(final String title, final String text) {
        this.text = title + " " + text;

        // this.setTitle(title);
        this.setText(title);
        this.vPanel.add(new HTML(text));
        this.vPanel.setStyleName("gwt-DialogBoxContent");
        this.vPanel.add(new Button("Close", new ClickListener() {
            public void onClick(final Widget sender) {
                InfoDialog.this.hide();
            }
        }));

        this.setWidget(this.vPanel);
    }

    public InfoDialog(final String title, final HTML html) {
        this.text = title + " " + html.getText();

        this.setText(title);
        this.vPanel.add(html);
        this.vPanel.setStyleName("gwt-DialogBoxContent");
        this.vPanel.add(new Button("Close", new ClickListener() {
            public void onClick(final Widget sender) {
                InfoDialog.this.hide();
            }
        }));
        this.setWidget(this.vPanel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.google.gwt.user.client.ui.PopupPanel#show()
     */
    @Override
    public final void show() {
        super.show();

        GuiLogger.traceLog(this.text);
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
        case KeyboardListener.KEY_ESCAPE:
            this.hide();
            break;
        }

        return true;
    }
}
