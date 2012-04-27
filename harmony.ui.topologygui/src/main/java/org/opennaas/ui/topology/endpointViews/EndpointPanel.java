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


package org.opennaas.ui.topology.endpointViews;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.ConnectException;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.ButtonGroup;
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
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointInterfaceType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ObjectFactory;
import org.opennaas.ui.topology.ws.WSProxy;

public class EndpointPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    /**
     * The logger.
     */
    static final Logger logger = Logger.getLogger(EndpointPanel.class);
    JLabel endpointTitle = null;
    JLabel endpointIDLabel = null;
    JLabel endpointNameLabel = null;
    JLabel endpointDescLabel = null;
    JTextField endpointIDField = null;
    JTextField endpointNameField = null;
    JTextField endpointDescField = null;
    JLabel endpointDomLabel = null;
    JLabel endpointBWLabel = null;
    JTextField endpointBWField = null;
    JLabel endpointTypeLabel = null;
    JComboBox endpointTypeList = null;
    ButtonGroup group = null;
    JButton endpointButton = null;
    JLabel endpointLabel = null;
    JComboBox endpointIDComboBox = null;
    JComboBox domainCombo;
    Hashtable<String, EndpointType> endpointList = null;

    private Hashtable<String, DomainInformationType> domainList = null;

    /**
     * This is the default constructor
     */
    public EndpointPanel(final String action) {
        super();
        this.initialize(action);
    }

    private JComboBox getDomainCombo() {
        if (this.domainCombo == null) {
            final ComboBoxModel domainComboModel = new DefaultComboBoxModel();
            this.domainCombo = new JComboBox();
            this.domainCombo.setModel(domainComboModel);
            this.domainCombo.setBounds(168, 217, 140, 21);
        }
        return this.domainCombo;
    }

    /**
     * This method gets the filled Hashtable of Domains from the WSProxy and
     * populates the combo box with the identifiers.
     */
    private void getDomInformation() {

        this.domainCombo.removeAllItems();

        try {
            this.domainList = WSProxy.getDomains();
        } catch (final ConnectException ce) {
            EndpointPanel.logger
                    .error("Error ocurred while contacting the server... "
                            + ce.getMessage());
            ce.printStackTrace();
            this.showErrorDialog(ce.getMessage(), "Error");
        }

        for (final Enumeration<String> e = this.domainList.keys(); e.hasMoreElements();) {
            this.domainCombo.addItem(e.nextElement());
        }
    }

    /**
     * This method initializes endpointButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getEndpointButton() {
        if (this.endpointButton == null) {
            this.endpointButton = new JButton();
            this.endpointButton.setBounds(new Rectangle(432, 311, 127, 32));
        }
        return this.endpointButton;
    }

    /**
     * This method initializes endpointBWField
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getEndpointBWField() {
        if (this.endpointBWField == null) {
            this.endpointBWField = new JTextField();
            this.endpointBWField.setBounds(455, 217, 112, 21);
        }
        return this.endpointBWField;
    }

    /**
     * This method initializes endpointDescField
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getEndpointDescField() {
        if (this.endpointDescField == null) {
            this.endpointDescField = new JTextField();
            this.endpointDescField.setBounds(168, 154, 420, 28);
        }
        return this.endpointDescField;
    }

    /**
     * This method initializes endpointIDComboBox
     * 
     * @return javax.swing.JComboBox
     */
    private JComboBox getEndpointIDComboBox() {
        if (this.endpointIDComboBox == null) {
            this.endpointIDComboBox = new JComboBox();
            this.endpointIDComboBox.setBounds(new Rectangle(32, 395, 135, 26));
            this.endpointIDComboBox.addActionListener(new ActionListener() {
                EndpointType e = null;

                public void actionPerformed(final ActionEvent arg0) {
                    if (EndpointPanel.this.endpointIDComboBox.getItemCount() > 0) {
                        this.e = EndpointPanel.this.endpointList
                                .get(EndpointPanel.this.endpointIDComboBox
                                        .getSelectedItem());
                        EndpointPanel.this.endpointIDField.setText(this.e
                                .getEndpointId());
                        EndpointPanel.this.endpointDescField.setText(this.e
                                .getDescription());
                        EndpointPanel.this.endpointBWField.setText(""
                                + this.e.getBandwidth());
                        EndpointPanel.this.endpointNameField.setText(this.e
                                .getName());
                        EndpointPanel.this.endpointTypeList
                                .setSelectedItem(this.e.getInterface().value());
                    }
                }
            });
        }
        return this.endpointIDComboBox;
    }

    /**
     * This method initializes endpointIDField
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getEndpointIDField() {
        if (this.endpointIDField == null) {
            this.endpointIDField = new JTextField();
            this.endpointIDField.setBounds(new Rectangle(166, 64, 114, 26));
        }
        return this.endpointIDField;
    }

    /**
     * This method initializes endpointSiteIDField
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getEndpointSiteIDField() {
        if (this.endpointNameField == null) {
            this.endpointNameField = new JTextField();
            this.endpointNameField.setBounds(new Rectangle(167, 108, 114, 25));
        }
        return this.endpointNameField;
    }

    /**
     * This method initializes endpointTypeList
     * 
     * @return javax.swing.JComboBox
     */
    private JComboBox getEndpointTypeList() {
        if (this.endpointTypeList == null) {
            this.endpointTypeList = new JComboBox();
            this.endpointTypeList.setBounds(new Rectangle(468, 66, 119, 24));
            this.endpointTypeList.addItem("Border");
            this.endpointTypeList.addItem("User");
        }
        return this.endpointTypeList;
    }

    /**
     * This method gets the filled Hashtable of endpoints
     * (EndpointInformationType) from the WSProxy and populates the combo box
     * with the identifiers.
     */
    void getEPInformation() {

        this.endpointIDComboBox.removeAllItems();
        if (this.domainCombo.getItemCount() > 0) {
            try {
                this.endpointList = WSProxy
                        .getEndpoints((String) this.domainCombo
                                .getSelectedItem());
            } catch (final ConnectException ce) {
                EndpointPanel.logger
                        .error("Error ocurred while contacting the server... "
                                + ce.getMessage());
                ce.printStackTrace();
                this.showErrorDialog(ce.getMessage(), "Error");
            }

            final Enumeration<String> enu = this.endpointList.keys();
            String s = null;
            while (enu.hasMoreElements()) {
                s = enu.nextElement();
                this.endpointIDComboBox.addItem(s);
            }
        }
    }

    /**
     * This method initializes the view
     * 
     * @return void
     */
    private void initialize(final String action) {
        this.endpointLabel = new JLabel();
        this.endpointLabel.setBounds(new Rectangle(60, 359, 80, 25));
        this.endpointLabel.setText("EndpointID");
        this.endpointTypeLabel = new JLabel();
        this.endpointTypeLabel.setBounds(new Rectangle(329, 64, 103, 25));
        this.endpointTypeLabel.setText("Interface");
        this.endpointBWLabel = new JLabel();
        this.endpointBWLabel.setBounds(364, 217, 84, 21);
        // this.endpointBWLabel.setText("Endpoint BW");
        this.endpointBWLabel.setText("BW (Mbps)");
        this.endpointDomLabel = new JLabel();
        this.endpointDomLabel.setBounds(14, 217, 133, 28);
        this.endpointDomLabel.setText("Endpoint Domain");
        this.endpointDescLabel = new JLabel();
        this.endpointDescLabel.setBounds(14, 154, 147, 28);
        this.endpointDescLabel.setText("Endpoint Description");
        this.endpointNameLabel = new JLabel();
        this.endpointNameLabel.setBounds(new Rectangle(16, 105, 120, 30));
        this.endpointNameLabel.setText("Endpoint Name");
        this.endpointIDLabel = new JLabel();
        this.endpointIDLabel.setBounds(new Rectangle(16, 62, 120, 26));
        this.endpointIDLabel.setText("EndpointID  (TNA)");
        this.endpointTitle = new JLabel();
        this.endpointTitle.setFont(new Font("Dialog", Font.BOLD, 24));
        this.endpointTitle.setBounds(new Rectangle(10, 11, 322, 32));
        this.endpointTitle.setDisplayedMnemonic(KeyEvent.VK_UNDEFINED);
        this.setLayout(null);
        this.setSize(new Dimension(597, 449));
        this.add(this.endpointTitle, null);
        this.add(this.endpointIDLabel, null);
        this.add(this.endpointNameLabel, null);
        this.add(this.endpointDescLabel, null);
        this.add(this.getEndpointIDField(), null);
        this.add(this.getEndpointSiteIDField(), null);
        this.add(this.getEndpointDescField(), null);
        this.add(this.endpointDomLabel, null);
        this.add(this.endpointBWLabel, null);
        this.add(this.getEndpointBWField(), null);
        this.add(this.endpointTypeLabel, null);
        this.add(this.getEndpointTypeList(), null);
        this.group = new ButtonGroup();
        this.add(this.getEndpointButton(), null);
        this.add(this.endpointLabel, null);
        this.add(this.getEndpointIDComboBox(), null);
        this.add(this.getDomainCombo());

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
     * Initialize the panel for the Add operation
     */
    private void initializeAdd() {

        this.endpointTitle.setText("Add Endpoint");
        this.endpointButton.setText("Add");
        this.endpointIDField.setToolTipText("TNA must be IP-formatted");
        this.endpointIDComboBox.setVisible(false);
        this.endpointLabel.setVisible(false);

        this.getDomInformation();

        this.endpointButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent arg0) {

                if ((EndpointPanel.this.endpointIDField.getText().indexOf(".") == -1)
                        || EndpointPanel.this.endpointBWField.getText().equals(
                                "")) {

                    EndpointPanel.this.showMessageDialog(
                            "Please introduce valid ID or BW",
                            "Critical parameters");
                } else {
                    EndpointPanel.logger.info("ADD ENDPOINT");

                    final ObjectFactory of = new ObjectFactory();
                    final EndpointType epaux = of.createEndpointType();

                    epaux.setEndpointId(EndpointPanel.this.endpointIDField
                            .getText());
                    epaux.setDomainId((String) EndpointPanel.this.domainCombo
                            .getSelectedItem());
                    epaux.setDescription(EndpointPanel.this.endpointDescField
                            .getText());
                    epaux.setName(EndpointPanel.this.endpointNameField
                            .getText());
                    epaux.setBandwidth(Integer
                            .valueOf(EndpointPanel.this.endpointBWField
                                    .getText()));

                    if (((String) EndpointPanel.this.endpointTypeList
                            .getSelectedItem()).equalsIgnoreCase("user")) {
                        epaux.setInterface(EndpointInterfaceType.USER);
                    } else {
                        epaux.setInterface(EndpointInterfaceType.BORDER);
                    }

                    try {
                        final boolean result = WSProxy.addEndpoint(epaux);

                        if (result) {
                            EndpointPanel.this.showMessageDialog(
                                    "Endpoint added successfully!",
                                    "Add Endpoint Result");
                            EndpointPanel.logger
                                    .info("ADD ENDPOINT RESULT: true");
                        } else {
                            EndpointPanel.this.showErrorDialog(
                                    "Add Endpoint failed!",
                                    "Add Endpoint Result");
                            EndpointPanel.logger
                                    .info("ADD ENDPOINT RESULT: false");
                        }
                    } catch (final ConnectException ce) {
                        EndpointPanel.logger
                                .error("Error ocurred while contacting the server... "
                                        + ce.getMessage());
                        ce.printStackTrace();
                        EndpointPanel.this.showErrorDialog(ce.getMessage(),
                                "Error");
                    }

                }
            }
        });
    }

    /**
     * Initialize the panel for the Del operation
     */
    private void initializeDel() {

        this.endpointTitle.setText("Delete Endpoint");
        this.endpointButton.setText("Delete");
        this.endpointIDField.setEditable(false);
        this.endpointNameField.setEditable(false);
        this.endpointDescField.setEditable(false);
        this.domainCombo.setEditable(false);
        this.endpointBWField.setEditable(false);
        this.endpointTypeList.setEnabled(false);

        this.getDomInformation();
        this.getEPInformation();

        this.endpointButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent arg0) {

                EndpointPanel.logger.info("DELETE ENDPOINT");

                final ObjectFactory of = new ObjectFactory();
                final EndpointType epaux = of.createEndpointType();

                epaux.setEndpointId(EndpointPanel.this.endpointIDField
                        .getText());
                epaux.setDomainId((String) EndpointPanel.this.domainCombo
                        .getSelectedItem());
                epaux.setDescription(EndpointPanel.this.endpointDescField
                        .getText());
                epaux.setName(EndpointPanel.this.endpointNameLabel.getText());
                epaux.setBandwidth(Integer
                        .valueOf(EndpointPanel.this.endpointBWField.getText()));

                if (((String) EndpointPanel.this.endpointTypeList
                        .getSelectedItem()).equalsIgnoreCase("user")) {
                    epaux.setInterface(EndpointInterfaceType.USER);
                } else {
                    epaux.setInterface(EndpointInterfaceType.BORDER);
                }

                try {
                    final boolean result = WSProxy.deleteEndpoint(epaux);

                    if (result) {
                        EndpointPanel.this.showMessageDialog(
                                "Endpoint deleted successfully!",
                                "Delete Endpoint Result");
                        EndpointPanel.logger
                                .info("DELETE ENDPOINT RESULT: true");
                    } else {
                        EndpointPanel.this.showErrorDialog(
                                "Delete Endpoint failed!",
                                "Delete Endpoint Result");
                        EndpointPanel.logger
                                .info("DELETE ENDPOINT RESULT: false");
                    }
                } catch (final ConnectException ce) {
                    EndpointPanel.logger
                            .error("Error ocurred while contacting the server... "
                                    + ce.getMessage());
                    ce.printStackTrace();
                    EndpointPanel.this
                            .showErrorDialog(ce.getMessage(), "Error");
                }
            }
        });

        this.domainCombo.addActionListener(new ActionListener() {
            EndpointType e = null;

            public void actionPerformed(final ActionEvent arg0) {
                EndpointPanel.this.getEPInformation();

                this.e = EndpointPanel.this.endpointList
                        .get(EndpointPanel.this.endpointIDComboBox
                                .getSelectedItem());
                EndpointPanel.this.endpointIDField.setText(this.e
                        .getEndpointId());
                EndpointPanel.this.endpointDescField.setText(this.e
                        .getDescription());
                EndpointPanel.this.domainCombo.setSelectedItem(this.e
                        .getDomainId());
                EndpointPanel.this.endpointBWField.setText(""
                        + this.e.getBandwidth());
                EndpointPanel.this.endpointNameField.setText(this.e.getName());
                EndpointPanel.this.endpointTypeList.setSelectedItem(this.e
                        .getInterface().value());
            }
        });
    }

    /**
     * Initialize the panel for the Get operation
     */
    private void initializeGet() {

        this.getDomInformation();
        this.getEPInformation();
        this.endpointTitle.setText("Get Endpoint");
        this.endpointIDField.setEditable(false);
        this.endpointNameField.setEditable(false);
        this.endpointDescField.setEditable(false);
        this.endpointBWField.setEditable(false);
        this.endpointTypeList.setEnabled(false);
        this.endpointButton.setText("GET");
        this.endpointButton.setVisible(false);

        this.endpointButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent arg0) {
                EndpointPanel.logger.info("GET ENDPOINT");
            }
        });

        this.domainCombo.addActionListener(new ActionListener() {
            EndpointType e = null;

            public void actionPerformed(final ActionEvent arg0) {
                EndpointPanel.this.getEPInformation();
                if (EndpointPanel.this.endpointIDComboBox.getItemCount() > 0) {
                    this.e = EndpointPanel.this.endpointList
                            .get(EndpointPanel.this.endpointIDComboBox
                                    .getSelectedItem());
                    EndpointPanel.this.endpointIDField.setText(this.e
                            .getEndpointId());
                    EndpointPanel.this.endpointDescField.setText(this.e
                            .getDescription());
                    EndpointPanel.this.domainCombo.setSelectedItem(this.e
                            .getDomainId());
                    EndpointPanel.this.endpointBWField.setText(""
                            + this.e.getBandwidth());
                    EndpointPanel.this.endpointNameField.setText(this.e
                            .getName());
                    EndpointPanel.this.endpointTypeList.setSelectedItem(this.e
                            .getInterface().value());
                }
            }
        });
    }

    /**
     * Initialize the panel for the Modify operation
     */
    private void initializeMod() {
        this.endpointTitle.setText("Modify Endpoint");
        this.endpointButton.setText("Modify");
        this.getDomInformation();
        this.getEPInformation();

        this.endpointButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent arg0) {
                EndpointPanel.logger.info("MODIFY ENDPOINT");

                final ObjectFactory of = new ObjectFactory();
                final EndpointType epaux = of.createEndpointType();

                epaux.setEndpointId(EndpointPanel.this.endpointIDField
                        .getText());
                epaux.setDomainId((String) EndpointPanel.this.domainCombo
                        .getSelectedItem());
                epaux.setDescription(EndpointPanel.this.endpointDescField
                        .getText());
                epaux.setName(EndpointPanel.this.endpointNameField.getText());
                epaux.setBandwidth(Integer
                        .valueOf(EndpointPanel.this.endpointBWField.getText()));

                if (((String) EndpointPanel.this.endpointTypeList
                        .getSelectedItem()).equalsIgnoreCase("user")) {
                    epaux.setInterface(EndpointInterfaceType.USER);
                } else {
                    epaux.setInterface(EndpointInterfaceType.BORDER);
                }

                try {
                    final boolean result = WSProxy.editEndpoint(epaux);

                    if (result) {
                        EndpointPanel.this.showMessageDialog(
                                "Endpoint edited successfully!",
                                "Edit Endpoint Result");
                        EndpointPanel.logger.info("EDIT ENDPOINT RESULT: true");
                    } else {
                        EndpointPanel.this
                                .showErrorDialog("Edit Endpoint failed!",
                                        "Edit Endpoint Result");
                        EndpointPanel.logger
                                .info("EDIT ENDPOINT RESULT: false");
                    }
                } catch (final ConnectException ce) {
                    EndpointPanel.logger
                            .error("Error ocurred while contacting the server... "
                                    + ce.getMessage());
                    ce.printStackTrace();
                    EndpointPanel.this
                            .showErrorDialog(ce.getMessage(), "Error");
                }
            }
        });

        this.domainCombo.addActionListener(new ActionListener() {
            EndpointType e = null;

            public void actionPerformed(final ActionEvent arg0) {
                EndpointPanel.this.getEPInformation();
                this.e = EndpointPanel.this.endpointList
                        .get(EndpointPanel.this.endpointIDComboBox
                                .getSelectedItem());
                EndpointPanel.this.endpointIDField.setText(this.e
                        .getEndpointId());
                EndpointPanel.this.endpointDescField.setText(this.e
                        .getDescription());
                EndpointPanel.this.domainCombo.setSelectedItem(this.e
                        .getDomainId());
                EndpointPanel.this.endpointBWField.setText(""
                        + this.e.getBandwidth());
                EndpointPanel.this.endpointNameField.setText(this.e.getName());
                EndpointPanel.this.endpointTypeList.setSelectedItem(this.e
                        .getInterface().value());
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
