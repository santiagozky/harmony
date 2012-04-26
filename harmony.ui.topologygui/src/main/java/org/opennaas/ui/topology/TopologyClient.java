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


package org.opennaas.ui.topology;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import org.opennaas.ui.topology.domainViews.DomainPanel;
import org.opennaas.ui.topology.endpointViews.EndpointPanel;
import org.opennaas.ui.topology.linkViews.LinkPanel;

public class TopologyClient {

    /**
     * @param args
     */
    public static void main(final String[] args) {
        // TODO Auto-generated method stub
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                final TopologyClient application = new TopologyClient();
                application.getJFrame().setVisible(true);
            }
        });
    }

    private JFrame jFrame = null;
    private JMenuBar jJMenuBar = null;
    private JMenu fileMenu = null;
    private JMenu helpMenu = null;
    private JMenuItem exitMenuItem = null;
    private JMenuItem aboutMenuItem = null;
    private JDialog aboutDialog = null;
    private JPanel aboutContentPane = null;
    private JLabel aboutVersionLabel = null;
    protected JSplitPane jSplitPane = null;

    JTree jTree = null;
    ImageIcon icon = new ImageIcon("resources/images/splash.gif");

    JLabel phosphoLabel = new JLabel(this.icon);

    /**
     * This method initializes aboutContentPane
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getAboutContentPane() {
        if (this.aboutContentPane == null) {
            this.aboutContentPane = new JPanel();
            this.aboutContentPane.setLayout(new BorderLayout());
            this.aboutContentPane.add(this.getAboutVersionLabel(),
                    BorderLayout.CENTER);
        }
        return this.aboutContentPane;
    }

    /**
     * This method initializes aboutDialog
     * 
     * @return javax.swing.JDialog
     */
    JDialog getAboutDialog() {
        if (this.aboutDialog == null) {
            this.aboutDialog = new JDialog(this.getJFrame(), true);
            this.aboutDialog.setTitle("About");
            this.aboutDialog.setContentPane(this.getAboutContentPane());
        }
        return this.aboutDialog;
    }

    /**
     * This method initializes jMenuItem
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getAboutMenuItem() {
        if (this.aboutMenuItem == null) {
            this.aboutMenuItem = new JMenuItem();
            this.aboutMenuItem.setText("About");
            this.aboutMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    final JDialog aboutDialog = TopologyClient.this
                            .getAboutDialog();
                    aboutDialog.pack();
                    final Point loc = TopologyClient.this.getJFrame()
                            .getLocation();
                    loc.translate(20, 20);
                    aboutDialog.setLocation(loc);
                    aboutDialog.setVisible(true);
                }
            });
        }
        return this.aboutMenuItem;
    }

    /**
     * This method initializes aboutVersionLabel
     * 
     * @return javax.swing.JLabel
     */
    private JLabel getAboutVersionLabel() {
        if (this.aboutVersionLabel == null) {
            this.aboutVersionLabel = new JLabel();
            this.aboutVersionLabel
                    .setText("   Phosphorus Topology Client v1.0   "
                            + "     Developed by i2CAT Foundation     "
                            + "        Barcelona, Catalonia, Spain    ");
            this.aboutVersionLabel
                    .setHorizontalAlignment(SwingConstants.CENTER);
            // this.aboutVersionLabel.setIcon(icon);
            // this.aboutVersionLabel.setSize(200, 200);
        }
        return this.aboutVersionLabel;
    }

    /**
     * This method initializes jMenuItem
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getExitMenuItem() {
        if (this.exitMenuItem == null) {
            this.exitMenuItem = new JMenuItem();
            this.exitMenuItem.setText("Exit");
            this.exitMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    System.exit(0);
                }
            });
        }
        return this.exitMenuItem;
    }

    /**
     * This method initializes jMenu
     * 
     * @return javax.swing.JMenu
     */
    private JMenu getFileMenu() {
        if (this.fileMenu == null) {
            this.fileMenu = new JMenu();
            this.fileMenu.setText("File");
            this.fileMenu.add(this.getExitMenuItem());
        }
        return this.fileMenu;
    }

    /**
     * This method initializes jMenu
     * 
     * @return javax.swing.JMenu
     */
    private JMenu getHelpMenu() {
        if (this.helpMenu == null) {
            this.helpMenu = new JMenu();
            this.helpMenu.setText("Help");
            this.helpMenu.add(this.getAboutMenuItem());
        }
        return this.helpMenu;
    }

    /**
     * This method initializes jFrame
     * 
     * @return javax.swing.JFrame
     */
    JFrame getJFrame() {
        if (this.jFrame == null) {
            this.jFrame = new JFrame();
            this.jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.jFrame.setJMenuBar(this.getJJMenuBar());
            this.jFrame.setSize(800, 600);
            this.jFrame.setContentPane(this.getJSplitPane());
            this.jFrame.setTitle("Phosphorus Topology Client");
            this.jFrame.setLocation(50, 50);
        }
        return this.jFrame;
    }

    /**
     * This method initializes jJMenuBar
     * 
     * @return javax.swing.JMenuBar
     */
    private JMenuBar getJJMenuBar() {
        if (this.jJMenuBar == null) {
            this.jJMenuBar = new JMenuBar();
            this.jJMenuBar.add(this.getFileMenu());
            this.jJMenuBar.add(this.getHelpMenu());
        }
        return this.jJMenuBar;
    }

    /**
     * This method initializes jSplitPane
     * 
     * @return javax.swing.JSplitPane
     */
    private JSplitPane getJSplitPane() {
        if (this.jSplitPane == null) {
            this.jSplitPane = new JSplitPane();
            this.jSplitPane.setDividerLocation(200);
            this.jSplitPane.setLeftComponent(this.getJTree());
            this.jSplitPane.setRightComponent(this.phosphoLabel);
        }
        return this.jSplitPane;
    }

    /**
     * This method initializes jTree
     * 
     * @return javax.swing.JTree
     */
    private JTree getJTree() {
        if (this.jTree == null) {

            /* Root of the tree */
            final DefaultMutableTreeNode top = new DefaultMutableTreeNode(
                    "Elements");

            /* Domain */
            final DefaultMutableTreeNode domain = new DefaultMutableTreeNode(
                    "Domains");

            final DefaultMutableTreeNode addDomain = new DefaultMutableTreeNode(
                    "Add Domain");
            domain.add(addDomain);

            final DefaultMutableTreeNode delDomain = new DefaultMutableTreeNode(
                    "Delete Domain");
            domain.add(delDomain);

            final DefaultMutableTreeNode getDomain = new DefaultMutableTreeNode(
                    "Get Domain");
            domain.add(getDomain);

            final DefaultMutableTreeNode modDomain = new DefaultMutableTreeNode(
                    "Modify Domain");
            domain.add(modDomain);

            /* Link */
            final DefaultMutableTreeNode links = new DefaultMutableTreeNode(
                    "Links");

            final DefaultMutableTreeNode addLink = new DefaultMutableTreeNode(
                    "Add Link");
            links.add(addLink);

            final DefaultMutableTreeNode delLink = new DefaultMutableTreeNode(
                    "Delete Link");
            links.add(delLink);

            final DefaultMutableTreeNode getLink = new DefaultMutableTreeNode(
                    "Get Link");
            links.add(getLink);

            final DefaultMutableTreeNode modLink = new DefaultMutableTreeNode(
                    "Modify Link");
            links.add(modLink);

            /* Endpoints */
            final DefaultMutableTreeNode endpoints = new DefaultMutableTreeNode(
                    "Endpoints");

            final DefaultMutableTreeNode addEndpoint = new DefaultMutableTreeNode(
                    "Add Endpoint");
            endpoints.add(addEndpoint);

            final DefaultMutableTreeNode delEndpoint = new DefaultMutableTreeNode(
                    "Delete Endpoint");
            endpoints.add(delEndpoint);

            final DefaultMutableTreeNode getEndpoint = new DefaultMutableTreeNode(
                    "Get Endpoint");
            endpoints.add(getEndpoint);

            final DefaultMutableTreeNode modEndpoint = new DefaultMutableTreeNode(
                    "Modify Endpoint");
            endpoints.add(modEndpoint);

            top.add(domain);
            top.add(links);
            top.add(endpoints);

            this.jTree = new JTree(top);
            this.jTree.getSelectionModel().setSelectionMode(
                    TreeSelectionModel.SINGLE_TREE_SELECTION);

            this.jTree.addTreeSelectionListener(new TreeSelectionListener() {
                public void valueChanged(final TreeSelectionEvent e) {
                    final DefaultMutableTreeNode node = (DefaultMutableTreeNode) TopologyClient.this.jTree
                            .getLastSelectedPathComponent();

                    if (node == null) {
                        return;
                    }
                    final String nodeInfo = (String) node.getUserObject();

                    if (nodeInfo.equalsIgnoreCase("Add Domain")) {
                        TopologyClient.this.jSplitPane
                                .setRightComponent(new DomainPanel("add"));
                    } else if (nodeInfo.equalsIgnoreCase("Delete Domain")) {
                        TopologyClient.this.jSplitPane
                                .setRightComponent(new DomainPanel("del"));
                    } else if (nodeInfo.equalsIgnoreCase("Get Domain")) {
                        TopologyClient.this.jSplitPane
                                .setRightComponent(new DomainPanel("get"));
                    } else if (nodeInfo.equalsIgnoreCase("Modify Domain")) {
                        TopologyClient.this.jSplitPane
                                .setRightComponent(new DomainPanel("mod"));
                    } else if (nodeInfo.equalsIgnoreCase("Add Link")) {
                        TopologyClient.this.jSplitPane
                                .setRightComponent(new LinkPanel("add"));
                    } else if (nodeInfo.equalsIgnoreCase("Delete Link")) {
                        TopologyClient.this.jSplitPane
                                .setRightComponent(new LinkPanel("del"));
                    } else if (nodeInfo.equalsIgnoreCase("Get Link")) {
                        TopologyClient.this.jSplitPane
                                .setRightComponent(new LinkPanel("get"));
                    } else if (nodeInfo.equalsIgnoreCase("Modify Link")) {
                        TopologyClient.this.jSplitPane
                                .setRightComponent(new LinkPanel("mod"));
                    } else if (nodeInfo.equalsIgnoreCase("Add Endpoint")) {
                        TopologyClient.this.jSplitPane
                                .setRightComponent(new EndpointPanel("add"));
                    } else if (nodeInfo.equalsIgnoreCase("Delete Endpoint")) {
                        TopologyClient.this.jSplitPane
                                .setRightComponent(new EndpointPanel("del"));
                    } else if (nodeInfo.equalsIgnoreCase("Get Endpoint")) {
                        TopologyClient.this.jSplitPane
                                .setRightComponent(new EndpointPanel("get"));
                    } else if (nodeInfo.equalsIgnoreCase("Modify Endpoint")) {
                        TopologyClient.this.jSplitPane
                                .setRightComponent(new EndpointPanel("mod"));
                    }
                }
            }

            );
        }
        return this.jTree;
    }

}
