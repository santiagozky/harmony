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


package org.opennaas.ui.topology.domainViews;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.ConnectException;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DomainInformationType;
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
public class DomainPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    /**
     * The logger.
     */
    static final Logger logger = Logger.getLogger(DomainPanel.class);
     JLabel domTitle = null;
     JLabel domIDLabel = null;
     JTextField domIDField = null;
    JTextField tnaList;
     JTextField topoEPRField;
    JLabel topoEPRLabel;
    JTextField rsvEPRField;
    JLabel rsvEPRLabel;
    JButton domButton = null;
    JComboBox domComboBox = null;
    Hashtable<String, DomainInformationType> domainList = null;
    JLabel domDescLabel = null;
    JTextField domDescField = null;

    JLabel domNetAddLabel = null;

    /**
     * This is the default constructor
     */
    public DomainPanel(final String action) {
        super();
        this.initialize(action);
    }

    /**
     * This method initializes domButton
     * 
     * @return javax.swing.JButton
     */
    JButton getDomButton() {
        if (this.domButton == null) {
            this.domButton = new JButton();
            this.domButton.setBounds(new Rectangle(207, 294, 91, 24));
        }
        return this.domButton;
    }

    /**
     * This method initializes domComboBox
     * 
     * @return javax.swing.JComboBox
     */
    JComboBox getDomComboBox() {
        if (this.domComboBox == null) {
            this.domComboBox = new JComboBox();

            DomainPanel.logger.debug("1/2 getDomComboBox: beginning...");
            this.domComboBox.setBounds(406, 63, 84, 21);
            this.domComboBox.setToolTipText("Registered domains");

            this.domComboBox.addActionListener(new ActionListener() {
                DomainInformationType d = null;

                public void actionPerformed(final ActionEvent arg0) {
                    DomainPanel.logger
                            .debug("1/2 ActionListener.actionPerformed: Showing item: "
                                    + DomainPanel.this.domComboBox
                                            .getSelectedItem());

                    this.d = DomainPanel.this.domainList
                            .get(DomainPanel.this.domComboBox.getSelectedItem());

                    DomainPanel.this.domIDField.setText(this.d.getDomainId());
                    DomainPanel.this.domDescField.setText(this.d
                            .getDescription());
                    DomainPanel.this.rsvEPRField.setText(this.d
                            .getReservationEPR());
                    DomainPanel.this.topoEPRField.setText(this.d
                            .getTopologyEPR());

                    String list = "";

                    for (final String s : this.d.getTNAPrefix()) {
                        DomainPanel.logger
                                .debug("2/2 ActionListener.actionPerformed: Iterating for TNAs...");
                        list = list + s + " ";
                    }

                    DomainPanel.this.tnaList.setText(list);
                }
            });
            DomainPanel.logger
                    .debug("2/2 getDomComboBox: ActionListener started!");
        }
        return this.domComboBox;
    }

    /**
     * This method initializes domDescField.
     * 
     * @return javax.swing.JTextField.
     */
    JTextField getDomDescField() {
        if (this.domDescField == null) {
            this.domDescField = new JTextField();
            this.domDescField.setBounds(140, 126, 420, 21);
        }
        return this.domDescField;
    }

    /**
     * This method initializes domLocationField
     * 
     * @return javax.swing.JTextField
     */
    JTextField getDomLocationField() {
        if (this.domIDField == null) {
            this.domIDField = new JTextField();
            this.domIDField.setBounds(140, 84, 182, 21);
        }
        return this.domIDField;
    }

    /**
     * This method gets the filled Hashtable of Domains from the WSProxy and
     * populates the combo boxes with the identifiers of the link and the
     * endpoints.
     */
    void getInformation() {

        DomainPanel.logger.debug("Panel is getting Domains...");
        this.domComboBox.removeAllItems();
        DomainPanel.logger.debug("Domain combo-box cleaned");

        try {
            this.domainList = WSProxy.getDomains();
        } catch (final ConnectException ce) {
            DomainPanel.logger
                    .error("Error ocurred while contacting the server... "
                            + ce.getMessage());
            ce.printStackTrace();
            this.showErrorDialog(ce.getMessage(), "Error");
        }

        final int dlsize = this.domainList.size();

        int i = 1;
        for (final Enumeration<String> e = this.domainList.keys(); e.hasMoreElements();) {
            DomainPanel.logger.debug("Got domain " + i + " out of " + dlsize);
            i++;
            this.domComboBox.addItem(e.nextElement());
        }
        if (DomainPanel.logger.isDebugEnabled()) {
            this.showMessageDialog("Got Domains successfully!",
                    "Get Domains Result");
        }
    }

    JTextField getRsvEPRField() {
        if (this.rsvEPRField == null) {
            this.rsvEPRField = new JTextField();
            this.rsvEPRField.setToolTipText("Reservation WS IP address");
            this.rsvEPRField.setBounds(140, 161, 420, 21);
        }
        return this.rsvEPRField;
    }

    JLabel getRsvEPRLabel() {
        if (this.rsvEPRLabel == null) {
            this.rsvEPRLabel = new JLabel();
            this.rsvEPRLabel.setText("Reservation EPR");
            this.rsvEPRLabel.setBounds(35, 154, 112, 28);
        }
        return this.rsvEPRLabel;
    }

    JTextField getTnaList() {
        if (this.tnaList == null) {
            this.tnaList = new JTextField();
            this.tnaList.setText("Write the TNAs separated by space character");
            this.tnaList.setToolTipText("TNAs in format: ip/short_mask "
                    + "(separated by spaces!)");
            this.tnaList.setBounds(140, 238, 420, 21);
        }
        return this.tnaList;
    }

    JTextField getTopoEPRField() {
        if (this.topoEPRField == null) {
            this.topoEPRField = new JTextField();
            this.topoEPRField.setToolTipText("Topology WS IP address");
            this.topoEPRField.setBounds(140, 196, 420, 21);
        }
        return this.topoEPRField;
    }

    JLabel getTopoEPRLabel() {
        if (this.topoEPRLabel == null) {
            this.topoEPRLabel = new JLabel();
            this.topoEPRLabel.setText("Topology EPR");
            this.topoEPRLabel.setBounds(35, 189, 98, 28);
        }
        return this.topoEPRLabel;
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize(final String action) {
        this.domNetAddLabel = new JLabel();
        this.domNetAddLabel.setBounds(35, 238, 98, 21);
        this.domNetAddLabel.setText("TNA Prefix List");
        this.domDescLabel = new JLabel();
        this.domDescLabel.setBounds(35, 126, 98, 14);
        this.domDescLabel.setText("Domain Desc");
        this.domIDLabel = new JLabel();
        this.domIDLabel.setText("Domain ID");
        this.domIDLabel.setBounds(35, 84, 70, 14);
        this.domTitle = new JLabel();
        this.domTitle.setDisplayedMnemonic(KeyEvent.VK_UNDEFINED);
        this.domTitle.setBounds(new Rectangle(13, 11, 253, 27));
        this.domTitle.setFont(new Font("Dialog", Font.BOLD, 24));
        this.setLayout(null);
        this.setSize(new Dimension(620, 354));
        this.add(this.domTitle, null);
        this.add(this.domIDLabel, null);
        this.add(this.getDomLocationField(), null);
        this.add(this.getDomButton(), null);

        this.add(this.domIDLabel, null);
        this.add(this.domTitle, null);
        this.add(this.getDomComboBox(), null);
        this.add(this.domDescLabel, null);
        this.add(this.getDomDescField(), null);
        this.add(this.domNetAddLabel, null);
        this.add(this.getDomComboBox(), null);
        this.add(this.getRsvEPRLabel());
        this.add(this.getRsvEPRField());
        this.add(this.getTnaList());
        this.add(this.getTopoEPRLabel());
        this.add(this.getTopoEPRField());
        if (action.equalsIgnoreCase("add")) {
            this.initializeAdd();
        } else if (action.equalsIgnoreCase("del")) {
            this.initializeDel();
        } else if (action.equalsIgnoreCase("get")) {
            this.initializeGet();
        } else if (action.equalsIgnoreCase("mod")) {
            this.initializeMod();
        }
    }

    /**
     * AddDomain initiliser
     */
    private void initializeAdd() {
        this.domTitle.setText("Add Domain");
        this.domButton.setText("Add");
        this.domComboBox.setVisible(false);

        this.domButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent arg0) {
                DomainPanel.logger.info("ADD DOMAIN");

                final ObjectFactory of = new ObjectFactory();
                final DomainInformationType domaux = of
                        .createDomainInformationType();

                domaux.setDomainId(DomainPanel.this.domIDField.getText());
                domaux.setDescription(DomainPanel.this.domDescField.getText());
                domaux
                        .setReservationEPR(DomainPanel.this.rsvEPRField
                                .getText());
                domaux.setTopologyEPR(DomainPanel.this.topoEPRField.getText());
                final String[] list = DomainPanel.this.tnaList.getText().trim()
                        .split(" ");

                DomainPanel.logger.debug("Domain info (w/o TNAs): ID "
                        + DomainPanel.this.domIDField.getText() + " - Desc "
                        + DomainPanel.this.domDescField.getText()
                        + " - RSV-EPR "
                        + DomainPanel.this.rsvEPRField.getText()
                        + " - Topo-EPR "
                        + DomainPanel.this.topoEPRField.getText());

                for (final String element : list) {
                    domaux.getTNAPrefix().add(element);
                }

                try {
                    final boolean result = WSProxy.addDomain(domaux);

                    if (result) {
                        DomainPanel.this.showMessageDialog(
                                "Domain added successfully!",
                                "Add Domain Result");
                        DomainPanel.logger.info("RESULT ADD DOMAIN: true");
                    } else {
                        DomainPanel.this.showErrorDialog("Add Domain failed!",
                                "Add Domain Result");
                        DomainPanel.logger.error("RESULT ADD DOMAIN: false");
                    }
                } catch (final ConnectException ce) {
                    DomainPanel.logger
                            .error("Error ocurred while contacting the server... "
                                    + ce.getMessage());
                    ce.printStackTrace();
                    DomainPanel.this.showErrorDialog(ce.getMessage(), "Error");
                }
            }
        });
    }

    /**
     * DeleteDomain initialiser
     */
    private void initializeDel() {

        this.add(this.getDomComboBox(), null);
        this.domTitle.setText("Delete Domain");
        this.domButton.setText("Delete");
        this.domComboBox.setToolTipText("Select the desired domain here");

        this.domIDField.setEditable(false);
        this.domDescField.setEditable(false);
        this.rsvEPRField.setEditable(false);
        this.topoEPRField.setEditable(false);
        this.tnaList.setEditable(false);

        this.getInformation();

        this.domButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent arg0) {
                DomainPanel.logger.info("DEL DOMAIN");

                final ObjectFactory of = new ObjectFactory();
                final DomainInformationType domaux = of
                        .createDomainInformationType();

                domaux.setDomainId(DomainPanel.this.domIDField.getText());
                domaux.setDescription(DomainPanel.this.domDescField.getText());
                domaux
                        .setReservationEPR(DomainPanel.this.rsvEPRField
                                .getText());
                domaux.setTopologyEPR(DomainPanel.this.topoEPRField.getText());

                final String[] list = DomainPanel.this.tnaList.getText().trim()
                        .split(" ");

                for (final String element : list) {
                    domaux.getTNAPrefix().add(element);
                }

                try {
                    final boolean result = WSProxy.deleteDomain(domaux);

                    if (result) {
                        DomainPanel.this.showMessageDialog(
                                "Domain deleted successfully!",
                                "Del Domain Result");
                        DomainPanel.logger.info("RESULT DEL DOMAIN: true");
                    } else {
                        DomainPanel.this.showErrorDialog(
                                "Delete Domain failed!", "Del Domain Result");
                        DomainPanel.logger.error("RESULT DEL DOMAIN: false");
                    }

                    DomainPanel.this.domIDField.setText("");
                    DomainPanel.this.domDescField.setText("");
                    DomainPanel.this.rsvEPRField.setText("");
                    DomainPanel.this.topoEPRField.setText("");
                    DomainPanel.this.tnaList.setText("");

                } catch (final ConnectException ce) {
                    DomainPanel.logger
                            .error("Error ocurred while contacting the server... "
                                    + ce.getMessage());
                    ce.printStackTrace();
                    DomainPanel.this.showErrorDialog(ce.getMessage(), "Error");
                }
            }
        });
    }

    /**
     * GetDomain initialiser
     */
    private void initializeGet() {

        this.domTitle.setText("Get Domain");
        this.domButton.setVisible(false);
        this.domButton.setText("Get");
        this.domComboBox.setToolTipText("Select the desired domain here");

        this.domIDField.setEditable(false);
        this.domDescField.setEditable(false);
        this.rsvEPRField.setEditable(false);
        this.topoEPRField.setEditable(false);
        this.tnaList.setEditable(false);

        this.getInformation();

        this.domButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent arg0) {
                DomainPanel.logger.info("GET DOMAIN");
                DomainPanel.this.getInformation();
            }
        });
    }

    /**
     * ModifyDomain initialise
     * 
     * TODO Decide which parameters are editable or not
     */
    private void initializeMod() {
        this.add(this.getDomComboBox(), null);
        this.domTitle.setText("Modify Domain");
        this.domButton.setText("Modify");
        this.domComboBox.setToolTipText("Select the desired domain here");

        this.getInformation();

        this.domButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent arg0) {
                DomainPanel.logger.info("MODIFY DOMAIN");

                final ObjectFactory of = new ObjectFactory();
                final DomainInformationType domaux = of
                        .createDomainInformationType();

                domaux.setDomainId(DomainPanel.this.domIDField.getText());
                domaux.setDescription(DomainPanel.this.domDescField.getText());
                domaux
                        .setReservationEPR(DomainPanel.this.rsvEPRField
                                .getText());
                domaux.setTopologyEPR(DomainPanel.this.topoEPRField.getText());

                final String[] list = DomainPanel.this.tnaList.getText().trim()
                        .split(" ");

                for (final String element : list) {
                    domaux.getTNAPrefix().add(element);
                }

                try {
                    final boolean result = WSProxy.editDomain(domaux);

                    if (result) {
                        DomainPanel.this.showMessageDialog(
                                "Edited Domain successfully!",
                                "Edit Domain Result");
                    } else {
                        DomainPanel.this.showErrorDialog("Edit Domain failed!",
                                "Edit Domain Result");
                    }
                } catch (final ConnectException ce) {
                    DomainPanel.logger
                            .error("Error ocurred while contacting the server... "
                                    + ce.getMessage());
                    ce.printStackTrace();
                    DomainPanel.this.showErrorDialog(ce.getMessage(), "Error");
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
