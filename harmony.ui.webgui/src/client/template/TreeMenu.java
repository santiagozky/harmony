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


package client.template;

import client.ManagementGui;
import client.template.page.DefaultPage;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeImages;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.TreeListener;

/**
 * the treeMenu which shows the available options to choose from.
 * 
 * @author claus
 */
public class TreeMenu extends Composite {

    /**
     * Allows us to override Tree default images. If we don't override one of
     * the methods, the default will be used.
     */
    interface NspTreeImages extends TreeImages {

        @Resource("public/downArrow.png")
        AbstractImagePrototype treeOpen();

        @Resource("public/rightArrow.png")
        AbstractImagePrototype treeClosed();
    }

    /**
     * the tree.
     */
    private Tree tree;

    /**
     * the listener that displays objects associated with tree-entries.
     */
    private TreeListener treeListener;

    /**
     * Constructor. Build the Tree Menu
     */
    public TreeMenu() {
        final TreeImages images = (TreeImages) GWT.create(NspTreeImages.class);
        this.tree = new Tree(images);

        this.treeListener = new TreeListener() {
            public void onTreeItemSelected(final TreeItem item) {
                if (item.getUserObject() != null) {
                    ManagementGui.get().displayItem(item.getUserObject());
                } else {
                    ManagementGui
                            .get()
                            .displayItem(
                                    new DefaultPage(
                                            "Welcome to Phosphorus WP1 Administrator Gui",
                                            "Please select any Subitem"));
                }
            }

            public void onTreeItemStateChanged(final TreeItem item) {

            }
        };

        this.tree.addTreeListener(this.treeListener);
        this.initWidget(this.tree);
    }

    /**
     * Get the Tree.
     * 
     * @return Tree
     */
    public Tree getTree() {
        return this.tree;
    }

    /**
     * Set the Tree
     * 
     * @param tree
     *            Tree
     */
    public void setTree(final Tree tree) {
        this.tree = tree;
    }

    /**
     * Get the Tree Listener.
     * 
     * @return Tree Listener.
     */
    public TreeListener getTreeListener() {
        return this.treeListener;
    }

    /**
     * Set the Tree Listener.
     * 
     * @param treeListener
     *            Tree Listener
     */
    public void setTreeListener(final TreeListener treeListener) {
        this.treeListener = treeListener;
    }

}
