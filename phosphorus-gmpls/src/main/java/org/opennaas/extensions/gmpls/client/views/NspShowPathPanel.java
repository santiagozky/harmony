package org.opennaas.extensions.gmpls.client.views;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import org.apache.muse.ws.addressing.soap.SoapFault;

import org.opennaas.extensions.gmpls.client.utils.MultiLineCellRenderer;
import org.opennaas.extensions.gmpls.client.utils.NspWS;
import org.opennaas.extensions.gmpls.client.utils.TableFitter;
import org.opennaas.extensions.gmpls.client.views.components.ReservationTable;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.InvalidReservationIDFaultException;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.exceptions.PathNotFoundFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;

/**
 * This is the panel where the user is able to look at the information about all
 * the paths and delete the ones he does not need anymore.
 * 
 * @author Stephan Wagner (stephan.wagner@iais.fraunhofer.de), Daniel Beer
 *         (daniel.beer@iais.fraunhofer.de)
 */
public class NspShowPathPanel extends JPanel implements ActionListener {

	/** */
	private static final long serialVersionUID = 1L;
	/** Title of the Page. */
	private JLabel lblTitle = null;
	/** Scrollable view. */
	private JScrollPane scrollPage = null;
	/** Table holding Information about all Reservations. */
	private JTable tblInfo = null;
	/* Buttons */
	/** Cancel reservation. */
	private JButton cmdcancel = null;
	/** Refresh this view. */
	private JButton cmdRefresh = null;
	private JButton cmdStatus = null;

	/**
	 * Default constructor. Initializes all components and updates the List.
	 */
	public NspShowPathPanel() {
		super();
		this.updateTable();
		this.initialize();
	}

	/**
	 * Initializes this Panel.
	 */
	private void initialize() {
		final GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.gridx = 0;
		gridBagConstraints3.insets = new Insets(5, 25, 5, 5);
		gridBagConstraints3.weightx = 0.5D;
		gridBagConstraints3.gridy = 2;
		final GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.gridx = 0;
		gridBagConstraints4.insets = new Insets(5, 25, 5, 5);
		gridBagConstraints4.weightx = 0.5D;
		gridBagConstraints4.gridy = 2;
		final GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 2;
		gridBagConstraints2.insets = new Insets(5, 5, 5, 25);
		gridBagConstraints2.weightx = 0.5D;
		gridBagConstraints2.gridy = 2;
		final GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
		gridBagConstraints5.gridx = 1;
		gridBagConstraints5.insets = new Insets(5, 5, 5, 25);
		gridBagConstraints5.weightx = 0.5D;
		gridBagConstraints5.gridy = 2;
		final GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.fill = GridBagConstraints.BOTH;
		gridBagConstraints1.gridy = 1;
		gridBagConstraints1.weightx = 1.0;
		gridBagConstraints1.weighty = 1.0;
		gridBagConstraints1.gridwidth = 3;
		gridBagConstraints1.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints1.gridx = 0;
		final GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(2, 2, 2, 2);
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.gridy = 0;
		this.lblTitle = new JLabel();
		this.lblTitle.setText("Show / Cancel Reservations");
		this.lblTitle.setFont(new Font("Dialog", Font.BOLD, 24));
		this.setLayout(new GridBagLayout());
		this.setSize(new Dimension(620, 354));

		this.add(this.lblTitle, gridBagConstraints);
		this.add(this.getScrollInfo(), gridBagConstraints1);
		this.add(this.getCmdCancel(), gridBagConstraints2);
		this.add(this.getCmdRefresh(), gridBagConstraints3);
		this.add(this.getCmdStatus(), gridBagConstraints5);

	}

	private JButton getCmdStatus() {
		if (this.cmdStatus == null) {
			this.cmdStatus = new JButton();
			this.cmdStatus.setText("get Status");
			this.cmdStatus.addActionListener(this);
		}
		return this.cmdStatus;

	}

	/**
	 * Updates the {@link ReservationTable}. The ReservationTable loads Status
	 * information at run time, therefore it establishes a connection to the
	 * NRPS or the NSP
	 */
	public final void updateTable() {
		try {
			this.getTblInfo().setModel(
					new ReservationTable(NspWS.getReservations()));
			TableFitter.fitToContent(this.getTblInfo());

		} catch (SoapFault e) {
			e.printStackTrace();
		}
	}

	/**
	 * Updates the {@link ReservationTable}. The ReservationTable loads Status
	 * information at run time, therefore it establishes a connection to the
	 * NRPS or the NSP
	 * 
	 * @throws InvalidReservationIDFaultException
	 */
	public final void updateTable(long reservationId)
			throws InvalidReservationIDFaultException {
		((ReservationTable) this.getTblInfo().getModel())
				.updateStatus(reservationId);
		TableFitter.fitToContent(this.getTblInfo());

	}

	/**
	 * Cancels all marked reservations. For each reservation that is marked in
	 * the table this method will call the cancelReservation method in
	 * {@link NspWS}.
	 * 
	 * @throws InvalidReservationIDFaultException
	 */
	public final synchronized void getStatusForReservations()
			throws InvalidReservationIDFaultException {
		try {
			for (final int row : this.tblInfo.getSelectedRows()) {
				final Long reservationId = (Long) this.tblInfo.getValueAt(row,
						0);
				System.out.println("get Status for: " + reservationId);
				updateTable(reservationId.longValue());
			}
			this.repaint();
		} catch (final NumberFormatException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, e.getMessage(),
					"NumberFormatException", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Cancels all marked reservations. For each reservation that is marked in
	 * the table this method will call the cancelReservation method in
	 * {@link NspWS}.
	 */
	public final synchronized void cancelReservation() {
		try {
			for (final int row : this.tblInfo.getSelectedRows()) {
				final Long reservationId = (Long) this.tblInfo.getValueAt(row,
						0);
				System.out.println("Canceling: " + reservationId);

				NspWS.cancelReservation(reservationId.longValue());
				updateTable(reservationId.longValue());
			}

			this.repaint();
			JOptionPane.showMessageDialog(this, "Canceled",
					"Canceled Reservation", JOptionPane.INFORMATION_MESSAGE);
		} catch (final NumberFormatException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, e.getMessage(),
					"NumberFormatException", JOptionPane.ERROR_MESSAGE);

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
	 * Initializes the scrollable Page.
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getScrollInfo() {
		if (this.scrollPage == null) {
			this.scrollPage = new JScrollPane();
			this.scrollPage.setViewportView(this.getTblInfo());
		}
		return this.scrollPage;
	}

	/**
	 * Initializes the Table containing all reservation information. The Table
	 * is of type {@link ReservationTable}.
	 * 
	 * @return javax.swing.JTable
	 */
	private JTable getTblInfo() {
		if (this.tblInfo == null) {
			try {
				this.tblInfo = new JTable(new ReservationTable(
						NspWS.getReservations()));
				this.tblInfo
						.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				this.tblInfo.setDefaultRenderer(String.class,
						new MultiLineCellRenderer());
				this.tblInfo.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
				// this.tblInfo.setRowHeight(this.tblInfo.getRowHeight() * 5);

			} catch (SoapFault e) {
				e.printStackTrace();
			}
		}
		return this.tblInfo;
	}

	/**
	 * Initializes the cancel Button.
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getCmdCancel() {
		if (this.cmdcancel == null) {
			this.cmdcancel = new JButton();
			this.cmdcancel.setText("cancel");
			this.cmdcancel.addActionListener(this);
		}
		return this.cmdcancel;
	}

	/**
	 * Initializes the refresh button.
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
	 * @throws InvalidReservationIDFaultException
	 */
	public final void actionPerformed(final ActionEvent event) {
		final Object src = event.getSource();
		if (this.cmdcancel.equals(src)) {
			this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			System.out.println("cancel");
			this.cancelReservation();
			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		} else if (this.cmdRefresh.equals(src)) {
			this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			System.out.println("refresh Path");
			NspShowPathPanel.this.updateTable();
			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		} else if (this.cmdStatus.equals(src)) {
			this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			System.out.println("get Status");
			try {
				this.getStatusForReservations();
			} catch (InvalidReservationIDFaultException e) {
				System.out.println("error parsing reservationID");
				e.printStackTrace();
			}
			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}
}
