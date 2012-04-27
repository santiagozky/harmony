package org.opennaas.extensions.gmpls.client.views;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.xml.bind.JAXBException;

import org.apache.muse.ws.addressing.soap.SoapFault;
import org.xml.sax.SAXException;

import org.opennaas.extensions.gmpls.client.utils.GmplsWS;
import org.opennaas.extensions.gmpls.client.utils.PathInformation;
import org.opennaas.extensions.gmpls.client.utils.TableFitter;
import org.opennaas.extensions.gmpls.client.views.components.GmplsPathTable;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.GetPathStatusResponseType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.PathIdentifierType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.exceptions.PathNotFoundFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;

/**
 * This is the panel where the user is able to look at the information about all
 * the paths and delete the ones he does not need anymore.
 * 
 * @author Stephan Wagner (stephan.wagner@iais.fraunhofer.de), Daniel Beer
 *         (daniel.beer@iais.fraunhofer.de)
 */
public class GmplsShowPathPanel extends JPanel implements ActionListener {

	/** */
	private static final long serialVersionUID = 1L;
	/** List of LSP handles. */
	private final List<PathInformation> lstInfo = new ArrayList<PathInformation>();
	/** Title of the page. */
	private JLabel lblTitle = null;
	/** Scrollable view for the path information table. */
	private JScrollPane scrollPathInfo = null;
	/** Path information table. */
	private JTable tblPathInfo = null;
	/** Path deletion button. */
	private JButton cmdDelete = null;
	/** Button to refresh the table content. */
	private JButton cmdRefresh = null;

	/**
	 * Default constructor.
	 * 
	 * @param list
	 *            list of LSP handles
	 */
	public GmplsShowPathPanel(final List<PathIdentifierType> list) {
		super();

		updateList(list);

		initialize();

	}

	/**
	 * Initializes this Panel.
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.gridx = 0;
		gridBagConstraints3.insets = new Insets(5, 25, 5, 5);
		gridBagConstraints3.weightx = 0.5D;
		gridBagConstraints3.gridy = 2;
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 1;
		gridBagConstraints2.insets = new Insets(5, 5, 5, 25);
		gridBagConstraints2.weightx = 0.5D;
		gridBagConstraints2.gridy = 2;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.fill = GridBagConstraints.BOTH;
		gridBagConstraints1.gridy = 1;
		gridBagConstraints1.weightx = 1.0;
		gridBagConstraints1.weighty = 1.0;
		gridBagConstraints1.gridwidth = 2;
		gridBagConstraints1.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints1.gridx = 0;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(2, 2, 2, 2);
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.gridy = 0;
		this.lblTitle = new JLabel();
		this.lblTitle.setText("Show / Delete Paths");
		this.lblTitle.setFont(new Font("Dialog", Font.BOLD, 24));
		this.setLayout(new GridBagLayout());
		this.setSize(new Dimension(620, 354));

		this.add(this.lblTitle, gridBagConstraints);
		this.add(getScrollInfo(), gridBagConstraints1);
		this.add(getCmdDelete(), gridBagConstraints2);
		this.add(getCmdRefresh(), gridBagConstraints3);
	}

	/**
	 * Constructor for this View.
	 * 
	 * @param list
	 *            content to initialize the table with.
	 */
	public final void updateList(final List<PathIdentifierType> list) {
		this.lstInfo.clear();
		try {
			int lspHandle;
			GetPathStatusResponseType response;
			for (int i = 0; i < list.size(); i++) {
				lspHandle = list.get(i).getPathIdentifier();
				response = GmplsWS.getPathStatus(lspHandle);

				this.lstInfo
						.add(new PathInformation(lspHandle, response.getPath()
								.getSourceTNA(), response.getPath()
								.getDestinationTNA(), response.getPath()
								.getBandwidth()));

			}
			this.getTblInfo().setModel(new GmplsPathTable(this.lstInfo));
			TableFitter.fitToContent(this.getTblInfo());
		} catch (final Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Updates the list with information from the GmplsWS.
	 * 
	 * @throws JAXBException
	 *             jaxb errors
	 * @throws SAXException
	 *             xml errors
	 * @throws IOException
	 *             communication error
	 * @throws SoapFault
	 *             muse faults
	 */
	public final void updateList() throws JAXBException, SAXException,
			IOException, SoapFault {
		this.updateList(GmplsWS.getPathInformation());
	}

	/**
	 * deletes a selected path on the gmplsWS.
	 */
	public final void deletePath() {
		try {
			for (int row : this.tblPathInfo.getSelectedRows()) {
				Integer pathId = (Integer) this.tblPathInfo.getValueAt(row, 0);
				System.out.println("Delete: " + pathId);

				GmplsWS.terminatePath(pathId.intValue());
			}
			updateList();
			this.repaint();
			JOptionPane.showMessageDialog(this, "Path(s) deleted",
					"Path(s) deleted", JOptionPane.INFORMATION_MESSAGE);
		} catch (final NumberFormatException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, e.getMessage(),
					"NumberFormatException", JOptionPane.ERROR_MESSAGE);
		} catch (final JAXBException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, e.getMessage(),
					"JAXBException", JOptionPane.ERROR_MESSAGE);
		} catch (final SAXException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, e.getMessage(), "SoapFault",
					JOptionPane.ERROR_MESSAGE);
		} catch (final IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, e.getMessage(), "IOException",
					JOptionPane.ERROR_MESSAGE);
		} catch (final UnexpectedFaultException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, e.getMessage(),
					"Unexpected Fault", JOptionPane.ERROR_MESSAGE);

		} catch (final PathNotFoundFaultException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, e.getMessage(),
					"PathNotFound Fault", JOptionPane.ERROR_MESSAGE);

		} catch (final SoapFault e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, e.getMessage(), "SoapFault",
					JOptionPane.ERROR_MESSAGE);

		}
	}

	/**
	 * This method initializes scrollInfo.
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getScrollInfo() {
		if (this.scrollPathInfo == null) {
			this.scrollPathInfo = new JScrollPane();
			this.scrollPathInfo.setViewportView(getTblInfo());
		}
		return this.scrollPathInfo;
	}

	/**
	 * This method initializes tblInfo.
	 * 
	 * @return javax.swing.JTable
	 */
	private JTable getTblInfo() {
		if (this.tblPathInfo == null) {
			this.tblPathInfo = new JTable(new GmplsPathTable(this.lstInfo));
			this.tblPathInfo
					.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		}
		return this.tblPathInfo;
	}

	/**
	 * This method initializes cmdDelete.
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getCmdDelete() {
		if (this.cmdDelete == null) {
			this.cmdDelete = new JButton();
			this.cmdDelete.setText("delete");
			this.cmdDelete.addActionListener(this);
		}
		return this.cmdDelete;
	}

	/**
	 * This method initializes cmdRefresh.
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getCmdRefresh() {
		if (this.cmdRefresh == null) {
			this.cmdRefresh = new JButton();
			this.cmdRefresh.setText("refresh");
			this.cmdRefresh.addActionListener(this);
		}
		return this.cmdRefresh;
	}

	/**
	 * Is triggered when a action is performed.
	 * 
	 * @param event
	 *            ActionEvent that triggered the this Method.
	 */
	public final void actionPerformed(final ActionEvent event) {
		Object src = event.getSource();

		if (this.cmdDelete.equals(src)) {
			setCursor(new Cursor(Cursor.WAIT_CURSOR));
			System.out.println("delete Path");
			deletePath();
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		} else if (this.cmdRefresh.equals(src)) {
			setCursor(new Cursor(Cursor.WAIT_CURSOR));
			System.out.println("refresh Path");
			try {
				GmplsShowPathPanel.this.updateList();
			} catch (final JAXBException err) {
				err.printStackTrace();
			} catch (final SAXException err) {
				err.printStackTrace();
			} catch (final IOException err) {
				err.printStackTrace();
			} catch (final SoapFault err) {
				err.printStackTrace();
			} finally {
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		}
	}

}
