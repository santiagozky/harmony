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

import client.about.tree.RenameTreeMenu;
import client.argon.tree.ArgonTreeMenu;
import client.reservation.tree.ReservationTreeMenu;
import client.template.panel.LoggingPanel;
import client.tools.tree.ToolsTreeMenu;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This is the entry-point for the Phosphorus-Management-GUI.
 * 
 * @author claus, gassen
 */
public class ManagementGui implements EntryPoint, WindowResizeListener {
    /** Self instance. */
    private static ManagementGui instance;

    /** Instance getter. */
    public static ManagementGui get() {
        return ManagementGui.instance;
    }

    /** Logo Panel. */
    private final LogoPanel logoPanel = LogoPanel.getInstance();
    /** Content Panel. */
    private final VerticalPanel rightPanel = new VerticalPanel();
    /** Stack Panel, the index. */
    private final StackPanel stackPanel = new StackPanel();
    /** Panel containing all other panels. */
    private final DockPanel dock = new DockPanel();
    /** Tree Menus for index. */
    private final ReservationTreeMenu resTree = new ReservationTreeMenu();
    /** Tree Menus for index. */
    private final ToolsTreeMenu topologyTree = new ToolsTreeMenu();
    /** Tree Menus for index. */
    private final ArgonTreeMenu argonTree = new ArgonTreeMenu();
    /** Tree Menus for index. */
    private final RenameTreeMenu renameTree = new RenameTreeMenu();
    
    

    /**
     * Method to show item on right (content) panel.
     * 
     * @param item
     *            String
     */
    public final void displayItem(final String item) {
        this.rightPanel.clear();
        this.rightPanel.add(new HTML(item));
    }

    /**
     * Method to show item on right (content) panel.
     * 
     * @param item
     *            the widget to display in the right panel
     */
    public final void displayItem(final Object item) {
        this.rightPanel.clear();
        this.rightPanel.add((Widget) item);
    }

    /**
     * Create HTML for stack panel images.
     * 
     * @param image
     *            Image url
     * @param text
     *            Stack Title
     * @return HTML string
     */
    private final String createImageHtml(final String image, final String text) {
        final String result =
                "<table>" + "<tr>" + "<td>" + "<img src='" + image
                        + "' width='32' height='32'/>" + "</td>" + "<td>"
                        + text + "</td>" + "</tr>" + "</table>";
        return result;
    }

    /**
     * Build the whole panel.
     */
    public final void onModuleLoad() {
        ManagementGui.instance = this;

        this.logoPanel.setWidth("100%");

        this.rightPanel.addStyleName("rightPanel");
        this.rightPanel.setWidth("100%");

        this.resTree.addStyleName("treeMenu");

        this.topologyTree.addStyleName("treeMenu");

        this.argonTree.addStyleName("treeMenu");

        this.stackPanel.setWidth("200");

        this.stackPanel.add(this.resTree, this.createImageHtml("journal.png",
                "Reservation"), true);
        this.stackPanel.add(this.topologyTree, this.createImageHtml(
                "connect.png", "Tools"), true);
        this.stackPanel.add(this.argonTree, this.createImageHtml(
                "database.png", "Argon"), true);
        this.stackPanel.add(this.renameTree,"About", true);
        
        this.dock.add(this.logoPanel, DockPanel.NORTH);
        this.dock.add(LoggingPanel.getPanel(), DockPanel.SOUTH);
        this.dock.add(this.stackPanel, DockPanel.WEST);
        this.dock.add(this.rightPanel, DockPanel.CENTER);

        this.dock.setWidth("100%");

        this.dock.setSpacing(4);
        this.dock.setCellWidth(this.rightPanel, "100%");

        Window.addWindowResizeListener(this);
        Window.enableScrolling(true);
        Window.setMargin("0px");

        RootPanel.get().add(this.dock);

        DeferredCommand.addCommand(new Command() {
            public void execute() {
                ManagementGui.this.onWindowResized(Window.getClientWidth(),
                        Window.getClientHeight());
            }
        });

        this.onWindowResized(Window.getClientWidth(), Window.getClientHeight());
    }

    /**
     * Resize the panels on window resize.
     * 
     * @param width
     *            Window with
     * @param heigh
     *            Window height
     */
    public final void onWindowResized(final int width, final int height) {
        int menuHeight =
                height - this.stackPanel.getAbsoluteTop() - 12
                        - LoggingPanel.getPanel().getOffsetHeight();

        if (menuHeight < 1) {
            menuHeight = 1;
        }
        this.stackPanel.setHeight("" + menuHeight);
        this.rightPanel.setHeight("" + menuHeight);

    }

    /**
     * Get the Logo Panel.
     * 
     * @return Logo panel
     */
    public final LogoPanel getLogoPanel() {
        return this.logoPanel;
    }

    /**
     * Get the right Panel.
     * 
     * @return right panel
     */
    public final VerticalPanel getRightPanel() {
        return this.rightPanel;
    }

    /**
     * Get the Stack panel.
     * 
     * @return Stack Panel
     */
    public final StackPanel getStackPanel() {
        return this.stackPanel;
    }

    /**
     * get the Dock panel.
     * 
     * @return Dock Panel
     */
    public final DockPanel getDock() {
        return this.dock;
    }

    /**
     * get the Reservation Tree Menu.
     * 
     * @return Reservation Tree Menu
     */
    public final ReservationTreeMenu getResTree() {
        return this.resTree;
    }

    /**
     * get the Topology Tree Menu.
     * 
     * @return Topology Tree Menu
     */
    public final ToolsTreeMenu getTopologyTree() {
        return this.topologyTree;
    }

    /**
     * get the Argon Tree Menu.
     * 
     * @return Argon Tree Menu
     */
    public final ArgonTreeMenu getArgonTree() {
        return this.argonTree;
    }

}
