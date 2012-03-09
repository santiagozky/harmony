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


package client.tools.tree;

import client.template.TreeMenu;
import client.tools.page.ConsoleSettingsPage;
import client.tools.page.NameResolverPage;

import com.google.gwt.user.client.ui.TreeItem;

/**
 * @author claus
 */
public class ToolsTreeMenu extends TreeMenu {

    /**
     * default constructor.
     */
    public ToolsTreeMenu() {
        super();

        final TreeItem treeItem = new TreeItem("TNA Lookup");
        treeItem.setUserObject(new NameResolverPage());
        this.getTree().addItem(treeItem);

        final TreeItem consoleItem = new TreeItem("Console Settings");
        consoleItem.setUserObject(new ConsoleSettingsPage());
        this.getTree().addItem(consoleItem);
    }

}
