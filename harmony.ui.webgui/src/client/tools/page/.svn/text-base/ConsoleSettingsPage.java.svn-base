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

import client.helper.GuiLogger;
import client.template.dialog.InfoDialog;

import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author gassen
 */
public class ConsoleSettingsPage extends Composite implements ClickListener {

    /**
     * 
     */
    private final Button button;

    /**
     * 
     */
    private final RadioButton rb0;
    /**
     * 
     */
    private final RadioButton rb1;
    /**
     * 
     */
    private final RadioButton rb2;
    /**
     * 
     */
    private final RadioButton rb3;
    /**
     * 
     */
    private final RadioButton rb4;

    /**
     * 
     */
    public ConsoleSettingsPage() {
        final VerticalPanel panel = new VerticalPanel();

        final int cookie = this.getValue();

        final Label label = new Label("Console LogLevel");
        panel.add(label);

        // Make some radio buttons, all in one group.
        this.rb0 = new RadioButton("logLevelGrp", "Trace");
        this.rb1 = new RadioButton("logLevelGrp", "Debug");
        this.rb2 = new RadioButton("logLevelGrp", "Warn");
        this.rb4 = new RadioButton("logLevelGrp", "Error");
        this.rb3 = new RadioButton("logLevelGrp", "Fatal");

        this.button = new Button("Save");
        this.button.addClickListener(this);

        if (GuiLogger.DEBUG == cookie) {
            this.rb1.setChecked(true);
        } else if (GuiLogger.WARN == cookie) {
            this.rb2.setChecked(true);
        } else if (GuiLogger.ERROR == cookie) {
            this.rb3.setChecked(true);
        } else if (GuiLogger.FATAL == cookie) {
            this.rb4.setChecked(true);
        } else {
            this.rb0.setChecked(true);
        }

        panel.add(this.rb0);
        panel.add(this.rb1);
        panel.add(this.rb2);
        panel.add(this.rb3);
        panel.add(this.rb4);
        panel.add(this.button);

        this.initWidget(panel);
    }

    /**
     * @return
     */
    private final int getValue() {
        final String value = Cookies.getCookie("LogLevel");

        if (null != value) {
            return Integer.valueOf(value).intValue();
        } else {
            return GuiLogger.TRACE;
        }
    }

    /**
     * @param level
     */
    private final void setValue(final int level) {
        Cookies.setCookie("LogLevel", level + "");

        GuiLogger.setLogLevel(level);

        final HTML text = new HTML("LogLevel successfully changed!");

        final InfoDialog dialog = new InfoDialog("Console Settings", text);

        dialog.show();
        dialog.center();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.google.gwt.user.client.ui.ClickListener#onClick(com.google.gwt.user
     * .client.ui.Widget)
     */
    public void onClick(final Widget sender) {
        if (this.rb0.isChecked()) {
            this.setValue(GuiLogger.TRACE);
        } else if (this.rb1.isChecked()) {
            this.setValue(GuiLogger.DEBUG);
        } else if (this.rb2.isChecked()) {
            this.setValue(GuiLogger.WARN);
        } else if (this.rb3.isChecked()) {
            this.setValue(GuiLogger.ERROR);
        } else if (this.rb4.isChecked()) {
            this.setValue(GuiLogger.FATAL);
        }

    }

}
