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


package org.opennaas.ui.topology.linkViews;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.ConnectException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DomainInformationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.Link;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ObjectFactory;
import org.opennaas.ui.topology.ws.WSProxy;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class LinkPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    /**
     * The logger.
     */
    static final Logger logger = Logger.getLogger(LinkPanel.class);
    private JLabel linkTitle = null;
    private JLabel linkDescLabel = null;
    JTextField linkDescField = null;
    JComboBox linkEndPointList1 = null;
    JComboBox linkEndPointList2 = null;
    private JLabel linkSrcEPLabel = null;
    private JLabel linkDestEPLabel = null;
    private JLabel linkIDLabel1 = null;
    JTextField nameField;
    private JLabel srcDomLabel;
    private JLabel dstDomLabel;
    JComboBox dstDomCombo;
    JComboBox srcDomCombo;
    JTextField delayField;
    JTextField costsField;
    private JLabel delayLabel;
    private JLabel nameLabel;
    JComboBox linkIDComboBox = null;
    private JButton linkButton = null;
    Hashtable<String, Link> linkList = null;
    private Hashtable<String, DomainInformationType> domList = null;
    ObjectFactory factory = null;
    Hashtable<String, EndpointType> epList1;

    Hashtable<String, EndpointType> epList2;

    /**
     * This is the default constructor.
     */
    public LinkPanel(final String action) {
        super();
        this.initialize(action);
    }

    private JLabel getDelayLabel() {
        if (this.delayLabel == null) {
            this.delayLabel = new JLabel();
            this.delayLabel.setText("Delay");
            this.delayLabel.setBounds(21, 252, 63, 28);
        }
        return this.delayLabel;
    }

    // TODO: add CostsLabel here

    private JTextField getDelayTextField() {

        if (this.delayField == null) {
            this.delayField = new JTextField();
            this.delayField.setBounds(168, 252, 112, 28);
        }
        return this.delayField;
    }

    /**
     * Gets the domains from the proxy and populates the combo boxes
     */
    private void getDomInfo() {

        this.srcDomCombo.removeAllItems();
        this.dstDomCombo.removeAllItems();

        try {
            this.domList = WSProxy.getDomains();
        } catch (final ConnectException ce) {
            LinkPanel.logger
                    .error("Error ocurred while contacting the server... "
                            + ce.getMessage());
            ce.printStackTrace();
            this.showErrorDialog(ce.getMessage(), "Error");
        }

        final Enumeration<?> e = this.domList.keys();
        String s = null;

        while (e.hasMoreElements()) {
            s = (String) e.nextElement();

            this.srcDomCombo.addItem(s);
            this.dstDomCombo.addItem(s);
        }
    }

    private JComboBox getDstDomCombo() {

        if (this.dstDomCombo == null) {
            final ComboBoxModel dstDomComboModel = new DefaultComboBoxModel();
            this.dstDomCombo = new JComboBox();
            this.dstDomCombo.setModel(dstDomComboModel);
            this.dstDomCombo.setBounds(462, 203, 147, 28);
        }
        return this.dstDomCombo;
    }

    private JLabel getDstDomLabel() {

        if (this.dstDomLabel == null) {
            this.dstDomLabel = new JLabel();
            this.dstDomLabel.setText("Dest Domain");
            this.dstDomLabel.setBounds(343, 203, 91, 28);
        }
        return this.dstDomLabel;
    }

    /**
     * This method gets the filled Hashtable of endpoints
     * (EndpointInformationType) from the WSProxy and populates the combo box
     * with the identifiers.
     */
    void getEPsInformation() {

        this.linkEndPointList1.removeAllItems();
        this.linkEndPointList2.removeAllItems();

        if (this.srcDomCombo.getItemCount() > 0) {
            try {
                this.epList1 = WSProxy.getEndpoints((String) this.srcDomCombo
                        .getSelectedItem());
            } catch (final ConnectException ce) {
                LinkPanel.logger
                        .error("Error ocurred while contacting the server... "
                                + ce.getMessage());
                ce.printStackTrace();
                this.showErrorDialog(ce.getMessage(), "Error");
            }

            final Enumeration<String> enu = this.epList1.keys();
            String s = null;
            while (enu.hasMoreElements()) {
                s = enu.nextElement();
                this.linkEndPointList1.addItem(s);
            }
        }
        if (this.dstDomCombo.getItemCount() > 0) {
            try {
                this.epList2 = WSProxy.getEndpoints((String) this.dstDomCombo
                        .getSelectedItem());
            } catch (final ConnectException ce) {
                LinkPanel.logger
                        .error("Error ocurred while contacting the server... "
                                + ce.getMessage());
                ce.printStackTrace();
                this.showErrorDialog(ce.getMessage(), "Error");
            }

            final Enumeration<String> enu = this.epList2.keys();
            String s = null;
            while (enu.hasMoreElements()) {
                s = enu.nextElement();
                this.linkEndPointList2.addItem(s);
            }
        }
    }

    /**
     * This method gets the filled Hashtable of links (Link) from the WSProxy
     * and populates the combo boxes with the identifiers of the link and the
     * endpoints.
     * 
     * TODO?: Invoke this only once and save the retrieved info
     */
    void getInformation() {

        this.linkEndPointList1.removeAllItems();
        this.linkEndPointList2.removeAllItems();
        this.srcDomCombo.removeAllItems();
        this.dstDomCombo.removeAllItems();
        this.linkList = new Hashtable<String, Link>();

        this.getDomInfo();

        for (int i = 0, count = this.srcDomCombo.getItemCount(); i < count; i++) {
            this.getLinkInfo((String) this.srcDomCombo.getItemAt(i));
        }

        this.showMessageDialog("Got Links successfully!", "Get Links Result");

        this.linkIDComboBox.addActionListener(new ActionListener() {
            Link l = null;

            public void actionPerformed(final ActionEvent arg0) {
                this.l = LinkPanel.this.linkList
                        .get(LinkPanel.this.linkIDComboBox.getSelectedItem());
                LinkPanel.this.linkDescField.setText(this.l.getDescription());
                LinkPanel.this.nameField.setText(this.l.getName());
                // delayField.setText(l.getDelay().toString());
                LinkPanel.this.linkEndPointList1.setSelectedItem(this.l
                        .getSourceEndpoint());
                LinkPanel.this.linkEndPointList2.setSelectedItem(this.l
                        .getDestinationEndpoint());

                // TODO: implement the retrieval of the domain an EP belongs to
            }
        });
    }

    /**
     * This method initializes linkButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getLinkButton() {

        if (this.linkButton == null) {
            this.linkButton = new JButton();
            this.linkButton.setBounds(392, 329, 126, 28);
        }
        return this.linkButton;
    }

    /**
     * This method initializes linkDescField
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getLinkDescField() {

        if (this.linkDescField == null) {
            this.linkDescField = new JTextField();
            this.linkDescField.setBounds(168, 112, 350, 21);
        }
        return this.linkDescField;
    }

    /**
     * This method initializes linkEndPointList1
     * 
     * @return javax.swing.JComboBox
     */
    private JComboBox getLinkEndPointList1() {

        if (this.linkEndPointList1 == null) {
            this.linkEndPointList1 = new JComboBox();
            this.linkEndPointList1.setBounds(168, 154, 154, 28);
        }
        return this.linkEndPointList1;
    }

    /**
     * This method initializes linkEndPointList2
     * 
     * @return javax.swing.JComboBox
     */
    private JComboBox getLinkEndPointList2() {
        if (this.linkEndPointList2 == null) {
            this.linkEndPointList2 = new JComboBox();
            this.linkEndPointList2.setBounds(168, 203, 154, 28);
        }
        return this.linkEndPointList2;
    }

    /**
     * This method initializes linkIDComboBox
     * 
     * @return javax.swing.JComboBox
     */
    private JComboBox getLinkIdComboBox() {

        if (this.linkIDComboBox == null) {
            this.linkIDComboBox = new JComboBox();
            this.linkIDComboBox.setBounds(182, 329, 154, 28);
        }
        return this.linkIDComboBox;
    }

    /**
     * This method gets the links related to acertain domain from the proxy and
     * populates the correspondant fields
     * 
     * @param dom
     */
    private void getLinkInfo(final String dom) {

        try {
            final Hashtable<String, Link> aux = WSProxy.getLinks(dom);

            Enumeration<?> e = aux.keys();
            String s = null;

            while (e.hasMoreElements()) {
                s = (String) e.nextElement();
                this.linkIDComboBox.addItem(s);
            }

            e = Collections.enumeration(aux.values());
            Link l = null;

            while (e.hasMoreElements()) {
                l = (Link) e.nextElement();
                this.linkEndPointList1.addItem(l.getSourceEndpoint());
                this.linkEndPointList2.addItem(l.getDestinationEndpoint());
                this.linkEndPointList2.addItem(l.getSourceEndpoint());
                this.linkEndPointList1.addItem(l.getDestinationEndpoint());
            }

            this.linkList.putAll(aux);

        } catch (final ConnectException ce) {
            LinkPanel.logger
                    .error("Error ocurred while contacting the server... "
                            + ce.getMessage());
            ce.printStackTrace();
            this.showErrorDialog(ce.getMessage(), "Error");
        }
    }

    private JTextField getNameField() {

        if (this.nameField == null) {
            this.nameField = new JTextField();
            this.nameField.setBounds(168, 70, 350, 21);
        }
        return this.nameField;
    }

    private JLabel getNameLabel() {

        if (this.nameLabel == null) {
            this.nameLabel = new JLabel();
            this.nameLabel.setText("Name");
            this.nameLabel.setBounds(21, 63, 63, 28);
        }
        return this.nameLabel;
    }

    private JComboBox getSrcDomCombo() {

        if (this.srcDomCombo == null) {
            final ComboBoxModel srcDomComboModel = new DefaultComboBoxModel();
            this.srcDomCombo = new JComboBox();
            this.srcDomCombo.setModel(srcDomComboModel);
            this.srcDomCombo.setBounds(462, 154, 147, 28);
        }
        return this.srcDomCombo;
    }

    private JLabel getSrcDomLabel() {

        if (this.srcDomLabel == null) {
            this.srcDomLabel = new JLabel();
            this.srcDomLabel.setText("Src Domain");
            this.srcDomLabel.setBounds(343, 154, 84, 28);
        }
        return this.srcDomLabel;
    }

    /**
     * This method initializes this.
     * 
     * @return void
     */
    private void initialize(final String action) {

        this.linkIDLabel1 = new JLabel();
        this.linkIDLabel1.setBounds(42, 329, 84, 21);
        this.linkIDLabel1.setText("Link Name");
        this.linkDestEPLabel = new JLabel();
        this.linkDestEPLabel.setBounds(21, 210, 140, 21);
        this.linkDestEPLabel.setText("Destination Endpoint");
        this.linkSrcEPLabel = new JLabel();
        this.linkSrcEPLabel.setBounds(21, 154, 105, 28);
        this.linkSrcEPLabel.setText("Source Endpoint");
        this.linkDescLabel = new JLabel();
        this.linkDescLabel.setBounds(21, 112, 112, 21);
        this.linkDescLabel.setText("Link Description");
        this.linkTitle = new JLabel();
        this.linkTitle.setFont(new Font("Dialog", Font.BOLD, 24));
        this.linkTitle.setBounds(new Rectangle(18, 10, 364, 32));
        this.setLayout(null);
        this.setSize(new Dimension(517, 325));
        this.setPreferredSize(new java.awt.Dimension(616, 392));
        this.add(this.linkTitle, null);
        this.add(this.linkDescLabel, null);
        this.add(this.getLinkDescField(), null);
        this.add(this.getLinkEndPointList1(), null);
        this.add(this.getLinkEndPointList2(), null);
        this.add(this.linkSrcEPLabel, null);
        this.add(this.linkDestEPLabel, null);
        this.add(this.linkIDLabel1, null);
        this.add(this.getLinkIdComboBox(), null);
        this.add(this.getLinkButton(), null);
        this.add(this.getNameLabel());
        this.add(this.getNameField());
        this.add(this.getDelayLabel());
        this.add(this.getDelayTextField());
        this.add(this.getSrcDomLabel());
        this.add(this.getDstDomLabel());
        this.add(this.getSrcDomCombo());
        this.add(this.getDstDomCombo());

        if (action.equalsIgnoreCase("add")) {
            this.initializeAdd();
        } else if (action.equalsIgnoreCase("del")) {
            this.initializeDel();
        } else if (action.equalsIgnoreCase("mod")) {
            this.initializeMod();
        } else if (action.equalsIgnoreCase("get")) {
            this.initializeGet();
        }
    }

    /**
     * Initialize the panel for the Add operation
     */
    private void initializeAdd() {

        this.getDomInfo();
        this.getEPsInformation();
        this.linkTitle.setText("Add Link");
        this.linkIDLabel1.setVisible(false);
        this.linkIDComboBox.setVisible(false);
        this.linkButton.setText("Add");
        this.srcDomLabel.setVisible(true);
        this.dstDomLabel.setVisible(true);
        this.srcDomCombo.setVisible(true);
        this.dstDomCombo.setVisible(true);
        this.linkEndPointList1.setEditable(true);
        this.linkEndPointList2.setEditable(true);

        this.linkButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent arg0) {

                final Link l = new Link();
                l.setDescription(LinkPanel.this.linkDescField.getText());
                l.setName(LinkPanel.this.nameField.getText());
                l.setSourceEndpoint((String) LinkPanel.this.linkEndPointList1
                        .getSelectedItem());
                l
                        .setDestinationEndpoint((String) LinkPanel.this.linkEndPointList2
                                .getSelectedItem());
                l.setDelay(Integer.getInteger((LinkPanel.this.delayField
                        .getText())));
                l.setCosts(Integer.getInteger((LinkPanel.this.costsField
                        .getText())));

                try {
                    final boolean result = WSProxy.addLink(l);

                    if (result) {
                        LinkPanel.this.showMessageDialog(
                                "Link added successfully!", "Add Link Result");
                    } else {
                        LinkPanel.this.showErrorDialog("Add Link failed!",
                                "Add Link Result");
                    }
                } catch (final ConnectException ce) {
                    LinkPanel.logger
                            .error("Error ocurred while contacting the server... "
                                    + ce.getMessage());
                    ce.printStackTrace();
                    LinkPanel.this.showErrorDialog(ce.getMessage(), "Error");
                }
            }
        });

        this.srcDomCombo.addActionListener(new ActionListener() {
            EndpointType e = null;

            public void actionPerformed(final ActionEvent arg0) {
                LinkPanel.this.getEPsInformation();
                if (LinkPanel.this.linkEndPointList1.getItemCount() > 0) {
                    this.e = LinkPanel.this.epList1
                            .get(LinkPanel.this.linkEndPointList1
                                    .getSelectedItem());
                    LinkPanel.this.srcDomCombo.setSelectedItem(this.e
                            .getDomainId());
                }
            }
        });

        this.dstDomCombo.addActionListener(new ActionListener() {
            EndpointType e = null;

            public void actionPerformed(final ActionEvent arg0) {
                LinkPanel.this.getEPsInformation();
                if (LinkPanel.this.linkEndPointList2.getItemCount() > 0) {
                    this.e = LinkPanel.this.epList2
                            .get(LinkPanel.this.linkEndPointList2
                                    .getSelectedItem());
                    LinkPanel.this.dstDomCombo.setSelectedItem(this.e
                            .getDomainId());
                }
            }
        });
    }

    /**
     * Initialize the panel for the Delete operation
     */
    private void initializeDel() {

        this.linkTitle.setText("Delete Link");
        this.linkButton.setText("Delete");
        this.nameField.setEditable(false);
        this.linkDescField.setEditable(false);
        this.linkEndPointList1.setEnabled(false);
        this.linkEndPointList2.setEnabled(false);
        this.srcDomLabel.setVisible(false);
        this.dstDomLabel.setVisible(false);
        this.srcDomCombo.setVisible(false);
        this.dstDomCombo.setVisible(false);
        this.delayField.setEnabled(false);

        this.getInformation();

        this.linkButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent arg0) {

                try {
                    final boolean result = WSProxy.deleteLink(Integer
                            .parseInt((String) LinkPanel.this.linkIDComboBox
                                    .getSelectedItem()));

                    if (result) {
                        LinkPanel.this.showMessageDialog(
                                "Link deleted successfully!",
                                "Delete Link Result");
                    } else {
                        LinkPanel.this.showErrorDialog("Link deletion failed!",
                                "Delete Link Result");
                    }
                } catch (final ConnectException ce) {
                    LinkPanel.logger
                            .error("Error ocurred while contacting the server... "
                                    + ce.getMessage());
                    ce.printStackTrace();
                    LinkPanel.this.showErrorDialog(ce.getMessage(), "Error");
                }
            }
        });
    }

    /**
     * Initialize the panel for the Get operation
     */
    private void initializeGet() {

        this.linkTitle.setText("Get Link");
        this.linkButton.setVisible(false);
        this.nameField.setEditable(false);
        this.linkDescField.setEditable(false);
        this.linkEndPointList1.setEnabled(false);
        this.linkEndPointList2.setEnabled(false);
        this.srcDomLabel.setVisible(false);
        this.dstDomLabel.setVisible(false);
        this.srcDomCombo.setVisible(false);
        this.dstDomCombo.setVisible(false);
        this.delayField.setEnabled(false);
        // this.costsField.setEnabled(false);

        this.getInformation();

        this.linkButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent arg0) {
                LinkPanel.logger.info("GET LINK");
                LinkPanel.this.getInformation();
            }
        });
    }

    /**
     * Initialize the panel for the Modify operation
     */
    private void initializeMod() {

        this.linkTitle.setText("Modify Link");
        this.linkButton.setText("Modify");
        this.srcDomLabel.setVisible(false);
        this.dstDomLabel.setVisible(false);
        this.srcDomCombo.setVisible(false);
        this.dstDomCombo.setVisible(false);
        this.delayField.setEnabled(false);

        this.getInformation();

        this.linkButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent arg0) {

                LinkPanel.logger.info("MODIFY LINK");
                final Link l = new Link();
                l.setDescription(LinkPanel.this.linkDescField.getText());
                l.setName(LinkPanel.this.nameField.getText());
                l.setDelay(Integer.getInteger((LinkPanel.this.delayField
                        .getText())));
                l.setCosts(Integer.getInteger((LinkPanel.this.costsField
                        .getText())));
                l.setSourceEndpoint((String) LinkPanel.this.linkEndPointList1
                        .getSelectedItem());
                l
                        .setDestinationEndpoint((String) LinkPanel.this.linkEndPointList2
                                .getSelectedItem());

                try {
                    final boolean result = WSProxy.editLink(l);

                    if (result) {
                        LinkPanel.this
                                .showMessageDialog("Edited Link successfully!",
                                        "Edit Link Result");
                    } else {
                        LinkPanel.this.showErrorDialog("Edit Link failed!",
                                "Edit Link Result");
                    }
                } catch (final ConnectException ce) {
                    LinkPanel.logger
                            .error("Error ocurred while contacting the server... "
                                    + ce.getMessage());
                    ce.printStackTrace();
                    LinkPanel.this.showErrorDialog(ce.getMessage(), "Error");
                }
            }
        });
    }

    void showErrorDialog(final String msg, final String title) {

        JOptionPane.showMessageDialog(this.getComponent(0), msg, title,
                JOptionPane.ERROR_MESSAGE);
    }

    void showMessageDialog(final String msg, final String title) {
        JOptionPane.showMessageDialog(this.getComponent(0), msg, title,
                JOptionPane.INFORMATION_MESSAGE);

    }
}
