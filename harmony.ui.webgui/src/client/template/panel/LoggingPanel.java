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
package client.template.panel;

import client.ManagementGui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosureEvent;
import com.google.gwt.user.client.ui.DisclosureHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.TextArea;

/**
 * @author gassen
 */
public class LoggingPanel extends Composite implements DisclosureHandler {
    /**
     * 
     */
    private static LoggingPanel selfInstance = null;
    /**
     * 
     */
    private final TextArea textArea;

    /**
     * 
     */
    private final DisclosurePanel panel;

    /**
     * 
     */
    private final String title = "Console";

    /**
     * 
     */
    private static int unread = 0;

    /**
     * 
     */
    private static boolean isOpen = false;

    /**
     * 
     */
    private LoggingPanel() {
        this.textArea = new TextArea();
        this.textArea.setWidth("100%");
        this.textArea.setHeight("200");

        this.panel = new DisclosurePanel(this.title);
        this.panel.setStyleName("gui-ErrorPanel");
        this.panel.setWidth("100%");
        this.panel.add(this.textArea);

        this.initWidget(this.panel);
        this.setWidth("100%");
        this.setStyleName("mail-AboutText");

        this.panel.addEventHandler(this);
    }

    /**
     * @return
     */
    public static LoggingPanel getPanel() {
        if (null == LoggingPanel.selfInstance) {
            LoggingPanel.selfInstance = new LoggingPanel();
        }

        return LoggingPanel.selfInstance;
    }

    /**
     * @param message
     */
    public void logMessage(final String message) {
        if (!LoggingPanel.isOpen) {
            LoggingPanel.unread++;
        }

        final String textAdd =
                (0 == LoggingPanel.unread) ? "" : " (" + LoggingPanel.unread
                        + " New)";

        this.panel.getHeaderTextAccessor().setText(this.title + textAdd);

        this.textArea.setText(this.textArea.getText() + message + "\n");

        final String value =
                DOM.getElementProperty(this.textArea.getElement(),
                        "scrollHeight");
        DOM.setElementProperty(this.textArea.getElement(), "scrollTop", value);
    }

    /**
     * @param message
     */
    public static void log(final String message) {
        LoggingPanel.getPanel().logMessage(message);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.google.gwt.user.client.ui.DisclosureHandler#onClose(com.google.gwt
     * .user.client.ui.DisclosureEvent)
     */
    public void onClose(final DisclosureEvent event) {
        LoggingPanel.isOpen = false;

        ManagementGui.get().onWindowResized(Window.getClientWidth(),
                Window.getClientHeight());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.google.gwt.user.client.ui.DisclosureHandler#onOpen(com.google.gwt
     * .user.client.ui.DisclosureEvent)
     */
    public void onOpen(final DisclosureEvent event) {
        LoggingPanel.unread = 0;
        LoggingPanel.isOpen = true;

        this.panel.getHeaderTextAccessor().setText(this.title);

        ManagementGui.get().onWindowResized(Window.getClientWidth(),
                Window.getClientHeight());
    }
}
