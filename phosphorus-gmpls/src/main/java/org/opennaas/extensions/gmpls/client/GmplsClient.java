package org.opennaas.extensions.gmpls.client;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

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
import javax.xml.bind.JAXBException;

import org.apache.muse.ws.addressing.soap.SoapFault;
import org.xml.sax.SAXException;

import org.opennaas.extensions.gmpls.client.utils.GmplsWS;
import org.opennaas.extensions.gmpls.client.utils.Serializer;
import org.opennaas.extensions.gmpls.client.views.GmplsCreatePathPanel;
import org.opennaas.extensions.gmpls.client.views.GmplsShowPathPanel;
import org.opennaas.extensions.gmpls.client.views.NspCreatePathPanel;
import org.opennaas.extensions.gmpls.client.views.NspShowPathPanel;

/**
 * GUI for the gmpls webservice and the NSP.
 *
 * @author Stephan Wagner (stephan.wagner@iais.fraunhofer.de)
 */
public class GmplsClient implements ActionListener, TreeSelectionListener {

    /** Reference to the proxy. */
    /** Navigation Top. */
    private DefaultMutableTreeNode top;
    /** Navigation --Gmpls. */
    private DefaultMutableTreeNode gmpls;
    /** Navigation ----Create Path. */
    private DefaultMutableTreeNode createPathGmpls;
    /** Navigation ----Show Path. */
    private DefaultMutableTreeNode showPathGmpls;
    /** Navigation --NSP. */
    private DefaultMutableTreeNode nspNavigation;
    /** Navigation ----Create Path. */
    private DefaultMutableTreeNode createPathNSP;
    /** Navigation ----Show Path. */
    private DefaultMutableTreeNode showPathNSP;

    /* GUI elements */
    /** frame. */
    private JFrame jFrame = null;
    /** menu bar. */
    private JMenuBar jJMenuBar = null;
    /** file menu. */
    private JMenu mnuFile = null;
    /** help menu. */
    private JMenu mnuHelp = null;
    /** exit menu item. */
    private JMenuItem mnuFileExit = null;
    /** exit menu item. */
    private JMenuItem mnuFileLoadUserEP = null;
    /** about menu item. */
    private JMenuItem mnuHelpAbout = null;
    /** about dialog. */
    private JDialog aboutDialog = null;
    /** about panel. */
    private JPanel aboutContentPane = null;
    /** version label. */
    private JLabel aboutVersionLabel = null;
    /** jSplitpane used to split the main view. */
    private JSplitPane mainPane = null;
    /** tree to navigate in the client. */
    private JTree treeNavigation = null;
    /** The Phosphorus icon. */
    private static final ImageIcon ICON =
            new ImageIcon("resources/images/splash.gif");
    /** Label with the Phosphorus icon. */
    private final JLabel lblPhosphorus = new JLabel(ICON);

    /** The NSP Create Path Panel. */
    private NspCreatePathPanel nspCreatePathPanel = null;

    /**
     * Constructor.
     */
    public GmplsClient() {
        try {
            this.nspCreatePathPanel = new NspCreatePathPanel();
            this.nspCreatePathPanel.refreshEndpoints(Serializer
                    .loadUserEndpointFromFile());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes the main split pane to be able to place components side by
     * side.
     *
     * @return javax.swing.JSplitPane
     */
    private JSplitPane getJSplitPane() {
        if (this.mainPane == null) {
            this.mainPane = new JSplitPane();
            this.mainPane.setDividerLocation(200);
            this.mainPane.setLeftComponent(this.getJTree());
            this.mainPane.setRightComponent(this.lblPhosphorus);
        }
        return this.mainPane;
    }

    /**
     * @param args
     *                arguments given to the main method
     */
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                final GmplsClient application = new GmplsClient();
                application.getJFrame().setVisible(
                        true);
            }
        });
    }

    /**
     * Initializes jFrame.
     *
     * @return javax.swing.JFrame
     */
    final JFrame getJFrame() {
        if (this.jFrame == null) {
            this.jFrame = new JFrame();
            this.jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.jFrame.setJMenuBar(this.getJJMenuBar());
            this.jFrame.setSize(
                    800, 600);
            this.jFrame.setContentPane(this.getJSplitPane());
            this.jFrame.setTitle("Phosphorus GMPLS Client");
        }
        return this.jFrame;
    }

    /**
     * Initializes jJMenuBar.
     *
     * @return javax.swing.JMenuBar
     */
    private JMenuBar getJJMenuBar() {
        if (this.jJMenuBar == null) {
            this.jJMenuBar = new JMenuBar();
            this.jJMenuBar.add(this.getMnuFile());
            this.jJMenuBar.add(this.getMnuHelp());
        }
        return this.jJMenuBar;
    }

    /**
     * Initializes jMenu.
     *
     * @return javax.swing.JMenu
     */
    private JMenu getMnuFile() {
        if (this.mnuFile == null) {
            this.mnuFile = new JMenu();
            this.mnuFile.setText("File");
            this.mnuFile.add(this.getMnuFileLoadUserEP());
            this.mnuFile.add(this.getMnuFileExit());
        }
        return this.mnuFile;
    }

    /**
     * Initializes the help menu.
     *
     * @return javax.swing.JMenu
     */
    private JMenu getMnuHelp() {
        if (this.mnuHelp == null) {
            this.mnuHelp = new JMenu();
            this.mnuHelp.setText("Help");
            this.mnuHelp.add(this.getMnuHelpAbout());
        }
        return this.mnuHelp;
    }

    /**
     * Initializes the menu item to leaf the menu.
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getMnuFileExit() {
        if (this.mnuFileExit == null) {
            this.mnuFileExit = new JMenuItem();
            this.mnuFileExit.setText("Exit");
            this.mnuFileExit.addActionListener(this);
        }
        return this.mnuFileExit;
    }

    /**
     * Initializes the menu item to leaf the menu.
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getMnuFileLoadUserEP() {
        if (this.mnuFileLoadUserEP == null) {
            this.mnuFileLoadUserEP = new JMenuItem("Load User Endpoints");
            this.mnuFileLoadUserEP.addActionListener(this);
        }
        return this.mnuFileLoadUserEP;
    }

    /**
     * Initializes the "about" menu item.
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getMnuHelpAbout() {
        if (this.mnuHelpAbout == null) {
            this.mnuHelpAbout = new JMenuItem();
            this.mnuHelpAbout.setText("About");
            this.mnuHelpAbout.addActionListener(this);
        }
        return this.mnuHelpAbout;
    }

    /**
     * Initializes the "about" Dialog.
     *
     * @return javax.swing.JDialog
     */
    final JDialog getAboutDialog() {
        if (this.aboutDialog == null) {
            this.aboutDialog = new JDialog(this.getJFrame(), true);
            this.aboutDialog.setTitle("About");
            this.aboutDialog.setContentPane(this.getAboutContentPanel());
        }
        return this.aboutDialog;
    }

    /**
     * Initializes the panel for informations about this software.
     *
     * @return javax.swing.JPanel
     */
    private JPanel getAboutContentPanel() {
        if (this.aboutContentPane == null) {
            this.aboutContentPane = new JPanel();
            this.aboutContentPane.setLayout(new BorderLayout());
            this.aboutContentPane.add(
                    this.getAboutVersionLabel(), BorderLayout.CENTER);
        }
        return this.aboutContentPane;
    }

    /**
     * Initializes the label with informations about the version of this
     * Software.
     *
     * @return javax.swing.JLabel
     */
    private JLabel getAboutVersionLabel() {
        if (this.aboutVersionLabel == null) {
            this.aboutVersionLabel = new JLabel();
            this.aboutVersionLabel
                    .setText("   Phosphorus GMPLS Client v1.0   ");
            this.aboutVersionLabel
                    .setHorizontalAlignment(SwingConstants.CENTER);
        }
        return this.aboutVersionLabel;
    }

    /**
     * Initializes a JTree for navigation in the client.
     *
     * @return javax.swing.JTree
     */
    private JTree getJTree() {
        if (this.treeNavigation == null) {

            /* Root of the tree */
            this.top = new DefaultMutableTreeNode("Elements");

            /* GMPLS */
            this.gmpls = new DefaultMutableTreeNode("GMPLS");

            this.createPathGmpls = new DefaultMutableTreeNode("create Path");
            this.gmpls.add(this.createPathGmpls);
            this.showPathGmpls =
                    new DefaultMutableTreeNode("show/delete Paths");
            this.gmpls.add(this.showPathGmpls);

            this.top.add(this.gmpls);

            /* THIN */
            this.nspNavigation = new DefaultMutableTreeNode("NSP");
            this.createPathNSP = new DefaultMutableTreeNode("create Path");
            this.nspNavigation.add(this.createPathNSP);
            this.showPathNSP = new DefaultMutableTreeNode("show/delete Paths");
            this.nspNavigation.add(this.showPathNSP);
            this.top.add(this.nspNavigation);

            this.treeNavigation = new JTree(this.top);
            this.treeNavigation.getSelectionModel().setSelectionMode(
                    TreeSelectionModel.SINGLE_TREE_SELECTION);

            this.treeNavigation.addTreeSelectionListener(this);
        }
        return this.treeNavigation;
    }

    /**
     * Is invoked when a action is performed.
     *
     * @param e
     *                action that is performed by the user
     */
    public final void actionPerformed(final ActionEvent e) {
        Object src = e.getSource();
        if (this.mnuFileExit.equals(src)) {
            System.exit(0);
        } else if (this.mnuFileLoadUserEP.equals(src)) {
            this.nspCreatePathPanel.refreshEndpoints(Serializer
                    .loadUserEndpointFromFile());
        } else if (this.mnuHelpAbout.equals(src)) {
            final JDialog newAboutDialog = getAboutDialog();
            newAboutDialog.pack();
            final Point loc = getJFrame().getLocation();
            loc.translate(
                    20, 20);
            newAboutDialog.setLocation(loc);
            newAboutDialog.setVisible(true);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
     */
    public final void valueChanged(@SuppressWarnings("unused")
    final TreeSelectionEvent e) {
        final DefaultMutableTreeNode node =
                (DefaultMutableTreeNode) this.treeNavigation
                        .getLastSelectedPathComponent();

        if (node == null) {
            return;
        }
        if (node.equals(this.createPathGmpls)) {
            try {
                this.mainPane.setRightComponent(new GmplsCreatePathPanel(
                        GmplsWS.getTopologyInformation()));
            } catch (final SoapFault e1) {
                e1.printStackTrace();
            } catch (final JAXBException e1) {
                e1.printStackTrace();
            } catch (final SAXException e1) {
                e1.printStackTrace();
            } catch (final IOException e1) {
                e1.printStackTrace();
            }
        } else if (node.equals(this.showPathGmpls)) {
            try {
                this.mainPane.setRightComponent(new GmplsShowPathPanel(GmplsWS
                        .getPathInformation()));
            } catch (final JAXBException e1) {
                e1.printStackTrace();
            } catch (final SAXException e1) {
                e1.printStackTrace();
            } catch (final IOException e1) {
                e1.printStackTrace();
            } catch (final SoapFault e1) {
                e1.printStackTrace();
            }
        } else if (node.equals(this.createPathNSP)) {
            this.nspCreatePathPanel.resetStartTime();
            this.mainPane.setRightComponent(this.nspCreatePathPanel);
        } else if (node.equals(this.showPathNSP)) {
            this.mainPane.setRightComponent(new NspShowPathPanel());
        }

    }
}
