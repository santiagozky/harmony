package org.opennaas.extensions.gmpls.client.views;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.muse.ws.addressing.soap.SoapFault;

import org.opennaas.extensions.gmpls.client.utils.NspWS;
import org.opennaas.extensions.gmpls.client.utils.Serializer;
import org.opennaas.extensions.gmpls.client.utils.TableFitter;
import org.opennaas.extensions.gmpls.client.views.components.EndpointTable;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.EndpointInterfaceType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.EndpointType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.exceptions.BandwidthFaultException;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.exceptions.CreatePathFaultException;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.exceptions.DestinationTNAFaultException;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.exceptions.SourceTNAFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ConnectionConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CreateReservationResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.CreateReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.FixedReservationConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ReservationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ServiceConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;

/**
 * This is the panel for the gmpls client where the user can create a path.
 * 
 * @author Stephan Wagner (stephan.wagner@iais.fraunhofer.de), Daniel Beer
 *         (daniel.beer@iais.fraunhofer.de)
 */
public class NspCreatePathPanel extends JPanel implements
		ListSelectionListener, ActionListener {

	/** */
	private static final long serialVersionUID = 1L;

	/** Map with all information for all TNA's. */
	private Map<String, EndpointType> endpointMap = null;
	/** Title for the page. */
	private JLabel lblTitle = null;
	/** Information about source TNA. */
	private JLabel lblSource = null;
	/** Scrollable view for the source table. */
	private JScrollPane scrollSource = null;
	/** Table for source TNA. */
	private JTable tblSource = null;
	/** Information about destination TNA. */
	private JLabel lblDest = null;
	/** Scrollable view for the source table. */
	private JScrollPane scrollDest = null;
	/** Table for destination TNA. */
	private JTable tblDest = null;
	/** Label for bandwidth. */
	private JLabel lblBandwidth = null;
	/** Input field for bandwidth. */
	private JTextField txtBandwidth = null;
	/** Panel for all reservation constraints. */
	private JPanel pnlAdvance = null;
	/** Label for start time. */
	private JLabel lblStart = null;
	/** Label for duration. */
	private JLabel lblDuration = null;
	/** Input field for start time. */
	private JTextField txtStart = null;
	/** Input field for duration. */
	private JTextField txtDuration = null;
	/** Button to create reservations. */
	private JButton cmdCreate = null;
	/** Button to create reservations. */
	private JButton cmdAddEndpoint = null;
	/** Date formatter to display the date in a common fashion. */
	private SimpleDateFormat sdf = null;
	/** the table model. */
	private EndpointTable endpointTableModel = null;
	/** Button to delete Endpoint. */
	private JButton cmdDeleteEndpoint = null;

	/**
	 * Creates this Panel.
	 */
	public NspCreatePathPanel() {
		super();
		this.sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		refreshMap();
		initialize();
	}

	/**
	 * refresh endpoint mapping.
	 */
	private void refreshMap() {
		this.endpointMap = new HashMap<String, EndpointType>(0);
		this.endpointMap.clear();
		for (final EndpointType listType : getEndpointTableModel()
				.getContents()) {
			this.endpointMap.put(listType.getEndpointId(), listType);
		}
	}

	/**
	 * Initializes this Panel.
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 5;
		GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
		gridBagConstraints12.gridx = 4;
		gridBagConstraints12.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints12.anchor = GridBagConstraints.SOUTHEAST;
		gridBagConstraints12.gridy = 9;
		GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
		gridBagConstraints13.gridx = 3;
		gridBagConstraints13.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints13.anchor = GridBagConstraints.SOUTHEAST;
		gridBagConstraints13.gridy = 5;
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.gridx = 0;
		gridBagConstraints11.gridwidth = 5;
		gridBagConstraints11.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints11.gridy = 8;

		gridBagConstraints11.insets = new Insets(5, 25, 5, 5);
		GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
		gridBagConstraints10.gridx = 2;
		gridBagConstraints10.gridy = 6;
		GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
		gridBagConstraints9.gridx = 1;
		gridBagConstraints9.gridy = 6;
		GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
		gridBagConstraints8.gridx = 0;
		gridBagConstraints8.gridy = 6;
		GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
		gridBagConstraints7.fill = GridBagConstraints.BOTH;
		gridBagConstraints7.gridy = 6;
		gridBagConstraints7.gridwidth = 3;
		gridBagConstraints7.anchor = GridBagConstraints.WEST;
		gridBagConstraints7.insets = new Insets(2, 5, 2, 2);
		gridBagConstraints7.gridx = 1;
		GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
		gridBagConstraints6.gridx = 0;
		gridBagConstraints6.anchor = GridBagConstraints.WEST;
		gridBagConstraints6.gridy = 6;
		gridBagConstraints6.insets = new Insets(2, 5, 2, 2);
		GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
		gridBagConstraints5.fill = GridBagConstraints.BOTH;
		gridBagConstraints5.gridy = 4;
		gridBagConstraints5.gridwidth = 5;
		gridBagConstraints5.weightx = 1.0D;
		gridBagConstraints5.weighty = 1.0D;
		gridBagConstraints5.insets = new Insets(2, 7, 2, 2);
		gridBagConstraints5.gridx = 0;
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.gridx = 0;
		gridBagConstraints4.gridy = 3;
		gridBagConstraints4.anchor = GridBagConstraints.WEST;
		gridBagConstraints4.insets = new Insets(2, 5, 2, 2);
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.fill = GridBagConstraints.BOTH;
		gridBagConstraints3.gridy = 2;
		gridBagConstraints3.gridwidth = 5;
		gridBagConstraints3.weightx = 1.0D;
		gridBagConstraints3.weighty = 1.0D;
		gridBagConstraints3.insets = new Insets(2, 7, 2, 2);
		gridBagConstraints3.gridx = 0;
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.gridy = 1;
		gridBagConstraints2.anchor = GridBagConstraints.WEST;
		gridBagConstraints2.insets = new Insets(2, 5, 2, 2);
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.gridy = 0;
		gridBagConstraints1.anchor = GridBagConstraints.WEST;
		gridBagConstraints1.insets = new Insets(2, 2, 2, 2);

		this.lblTitle = new JLabel();
		this.lblTitle.setText("Create Path");
		this.lblTitle.setFont(new Font("Dialog", Font.BOLD, 24));

		this.lblSource = new JLabel();
		this.lblSource.setText("Source Information");
		this.lblSource.setFont(new Font("Dialog", Font.BOLD, 18));

		this.lblDest = new JLabel();
		this.lblDest.setText("Destination Information");
		this.lblDest.setFont(new Font("Dialog", Font.BOLD, 18));

		this.lblBandwidth = new JLabel();
		this.lblBandwidth.setText("Bandwidth (Mbps):");

		setLayout(new GridBagLayout());

		add(getScrollSource(), gridBagConstraints3);
		add(this.lblTitle, gridBagConstraints1);
		add(this.lblSource, gridBagConstraints2);
		this.add(getScrollDest(), gridBagConstraints5);
		add(getTxtBandwidth(), gridBagConstraints7);
		add(this.lblDest, gridBagConstraints4);
		this.add(this.lblBandwidth, gridBagConstraints6);
		add(getPnlAdvance(), gridBagConstraints11);
		add(cmdCreate(), gridBagConstraints12);
		add(cmdAddEndpoint(), gridBagConstraints13);
		add(getCmdDeleteEndpoint(), gridBagConstraints);
	}

	/** Method to create a reservation. */
	public final void createReservation() {
		try {
			int bandwidth = Integer.valueOf(this.txtBandwidth.getText())
					.intValue();
			if (bandwidth <= 0) {
				throw new org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.exceptions.BandwidthFaultException(
						"Bandwidth too small");
			}
			String sourceTNA = (String) this.tblSource.getValueAt(
					this.tblSource.getSelectedRow(), 0);
			String destTNA = (String) this.tblDest.getValueAt(
					this.tblDest.getSelectedRow(), 0);

			CreateReservationType request = new CreateReservationType();
			ServiceConstraintType service = new ServiceConstraintType();
			ConnectionConstraintType connection = new ConnectionConstraintType();
			FixedReservationConstraintType frc = new FixedReservationConstraintType();

			int duration = Integer.parseInt(this.txtDuration.getText()) * 60;

			request.setJobID(Long.valueOf(System.currentTimeMillis()));

			service.setAutomaticActivation(true);
			service.setServiceID(1);
			service.setTypeOfReservation(ReservationType.FIXED);
			frc.setDuration(duration);
			GregorianCalendar cal = new GregorianCalendar();

			cal.setTime(this.sdf.parse(this.txtStart.getText()));
			XMLGregorianCalendar start = DatatypeFactory.newInstance()
					.newXMLGregorianCalendar(cal);
			frc.setStartTime(start);
			service.setFixedReservationConstraints(frc);

			connection.setConnectionID(1);
			connection.setMinBW(bandwidth);
			connection.setMaxBW(Integer.valueOf(bandwidth));
			org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointType src = new org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointType();
			org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointType dest = new org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointType();
			src.setEndpointId(sourceTNA);
			dest.setEndpointId(destTNA);
			connection.setSource(src);
			connection.getTarget().add(dest);

			service.getConnections().add(connection);
			request.getService().add(service);

			CreateReservationResponseType response = NspWS
					.createReservation(request);
			JOptionPane.showMessageDialog(
					this,
					"Reservation created! Reservation identifier is: "
							+ response.getJobID() + " - "
							+ response.getReservationID(),
					"Reservation created!", JOptionPane.INFORMATION_MESSAGE);

		} catch (final NumberFormatException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, e.getMessage(),
					"NumberFormatException", JOptionPane.ERROR_MESSAGE);

		} catch (final UnexpectedFaultException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, e.getMessage(),
					"Unexpected Fault", JOptionPane.ERROR_MESSAGE);
		} catch (final SourceTNAFaultException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, e.getMessage(),
					"SourceTNA Fault", JOptionPane.ERROR_MESSAGE);

		} catch (final DestinationTNAFaultException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, e.getMessage(),
					"DestinationTNA Fault", JOptionPane.ERROR_MESSAGE);

		} catch (final CreatePathFaultException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, e.getMessage(),
					"CreatePath Fault", JOptionPane.ERROR_MESSAGE);

		} catch (final BandwidthFaultException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, e.getMessage(),
					"Bandwidth Fault", JOptionPane.ERROR_MESSAGE);

		} catch (final SoapFault e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, e.getMessage(), "SoapFault",
					JOptionPane.ERROR_MESSAGE);
		} catch (ParseException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(),
					"ParseException", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (DatatypeConfigurationException e) {
			JOptionPane
					.showMessageDialog(this, e.getMessage(),
							"DatatypeConfigurationException",
							JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	/** Recalculates the bandwidth when the selection changes. */
	@SuppressWarnings("unused")
	public final void valueChanged(final ListSelectionEvent e) {
		this.txtBandwidth
				.setText((String) minMaxBW(this.tblSource.getSelectedRow(),
						this.tblDest.getSelectedRow()));
	}

	/**
	 * Gets the minimum bandwidth for the reservation between source and
	 * destination.
	 * 
	 * @param src
	 *            row in the source TNA table
	 * @param dest
	 *            row in the destination TNA table
	 * @return Object representing the bandwidth
	 */
	private Object minMaxBW(final int src, final int dest) {
		String ret = "1000";
		if (src >= 0) {
			if (dest >= 0) {
				int d = ((Integer) this.tblDest.getModel().getValueAt(dest, 1))
						.intValue();
				int s = ((Integer) this.tblSource.getModel().getValueAt(src, 1))
						.intValue();
				ret = Integer.toString(Math.min(s, d));
			} else {
				ret = ((Integer) this.tblSource.getModel().getValueAt(src, 1))
						.toString();
			}
		} else {
			if (dest >= 0) {
				ret = ((Integer) this.tblDest.getModel().getValueAt(dest, 1))
						.toString();
			}
		}
		return ret;
	}

	/**
	 * This method initializes scrollSource.
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getScrollSource() {
		if (this.scrollSource == null) {
			this.scrollSource = new JScrollPane();
			this.scrollSource.setViewportView(getTblSource());
		} else {
			this.scrollSource.setViewportView(getTblSource());
		}
		return this.scrollSource;
	}

	/**
	 * This method initializes tblSource.
	 * 
	 * @return javax.swing.JTable
	 */
	private JTable getTblSource() {

		this.tblSource = new JTable(getEndpointTableModel());
		this.tblSource.getSelectionModel().addListSelectionListener(this);
		this.tblSource.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		TableFitter.fitToContent(this.tblSource);

		return this.tblSource;
	}

	/**
	 * This method initializes scrollDest.
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getScrollDest() {
		if (this.scrollDest == null) {
			this.scrollDest = new JScrollPane();
			this.scrollDest.setViewportView(getTblDest());
		} else {
			this.scrollDest.setViewportView(getTblDest());
		}
		return this.scrollDest;
	}

	/**
	 * This method initializes tblDest.
	 * 
	 * @return javax.swing.JTable
	 */
	private JTable getTblDest() {

		this.tblDest = new JTable(getEndpointTableModel());
		this.tblDest.getSelectionModel().addListSelectionListener(this);
		this.tblDest.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		TableFitter.fitToContent(this.tblDest);

		return this.tblDest;
	}

	/**
	 * get the endpoint table model.
	 * 
	 * @return the model
	 */
	private EndpointTable getEndpointTableModel() {
		if (this.endpointTableModel == null) {
			try {

				this.endpointTableModel = new EndpointTable(
						NspWS.getTopologyInformation());
				this.endpointTableModel
						.addTableModelListener(new TableModelListener() {
							public void tableChanged(
									@SuppressWarnings("unused") TableModelEvent e) {
								Serializer
										.saveUserEndpointToFile(getEndpointTableModel()
												.getContents());
							}
						});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return this.endpointTableModel;
	}

	/**
	 * This method initializes txtBandwidth.
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getTxtBandwidth() {
		if (this.txtBandwidth == null) {
			this.txtBandwidth = new JTextField();
			this.txtBandwidth.setText("1000");
			this.txtBandwidth.setPreferredSize(new Dimension(100, 20));
		}
		return this.txtBandwidth;
	}

	/**
	 * This method initializes pnlAdvance.
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getPnlAdvance() {
		if (this.pnlAdvance == null) {
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.gridx = 0;
			gridBagConstraints15.anchor = GridBagConstraints.WEST;
			gridBagConstraints15.gridy = 1;
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.gridx = 0;
			gridBagConstraints14.anchor = GridBagConstraints.WEST;
			gridBagConstraints14.gridy = 0;
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.fill = GridBagConstraints.BOTH;
			gridBagConstraints13.gridx = 1;
			gridBagConstraints13.gridy = 1;
			gridBagConstraints13.anchor = GridBagConstraints.WEST;
			gridBagConstraints13.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints13.weightx = 1.0;
			gridBagConstraints13.gridwidth = 2;
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.fill = GridBagConstraints.BOTH;
			gridBagConstraints12.gridx = 1;
			gridBagConstraints12.gridy = 0;
			gridBagConstraints12.gridwidth = 2;
			gridBagConstraints12.anchor = GridBagConstraints.WEST;
			gridBagConstraints12.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints12.weightx = 1.0;
			this.lblDuration = new JLabel();
			this.lblDuration.setText("Duration (min):");
			this.lblStart = new JLabel();
			this.lblStart.setText("Start Time:");
			this.pnlAdvance = new JPanel();
			this.pnlAdvance.setLayout(new GridBagLayout());
			this.pnlAdvance.setBorder(BorderFactory.createTitledBorder(null,
					"Advance Reservation Options",
					TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, null, null));
			this.pnlAdvance.add(this.lblStart, gridBagConstraints14);
			this.pnlAdvance.add(getTxtStart(), gridBagConstraints12);
			this.pnlAdvance.add(this.lblDuration, gridBagConstraints15);
			this.pnlAdvance.add(getTxtDuration(), gridBagConstraints13);
			this.pnlAdvance.setEnabled(true);
		}
		return this.pnlAdvance;
	}

	/**
	 * This method initializes txtStart.
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getTxtStart() {
		if (this.txtStart == null) {
			this.txtStart = new JTextField();
			this.txtStart.setPreferredSize(new Dimension(80, 19));
			this.txtStart.setText(this.sdf.format(new Date()));
			this.txtStart.setEnabled(true);
		}
		return this.txtStart;
	}

	/**
	 * This method initializes txtDuration.
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getTxtDuration() {
		if (this.txtDuration == null) {
			this.txtDuration = new JTextField("2");
			this.txtDuration.setPreferredSize(new Dimension(80, 19));
			this.txtDuration.setEnabled(true);
		}
		return this.txtDuration;
	}

	/**
	 * This method initializes jButton.
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton cmdCreate() {
		if (this.cmdCreate == null) {
			this.cmdCreate = new JButton();
			this.cmdCreate.setText("Create");
			this.cmdCreate.addActionListener(this);
		}
		return this.cmdCreate;
	}

	/**
	 * This method initializes jButton.
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton cmdAddEndpoint() {
		if (this.cmdAddEndpoint == null) {
			this.cmdAddEndpoint = new JButton();
			this.cmdAddEndpoint.setText("Add Endpoint");
			this.cmdAddEndpoint.addActionListener(this);
		}
		return this.cmdAddEndpoint;
	}

	/**
     *
     */
	private void addEndpointToTable() {
		EndpointType ep = new EndpointType();
		ep.setInterface(EndpointInterfaceType.USER);
		ep.setBandwidth(Integer.valueOf(1000));
		ep.setDomainId("Not Set");
		ep.setEndpointId("Not Set");
		// NspCreatePathPanel.endpointList.add(ep);
		getEndpointTableModel().addRow(ep);
		refreshEndpoints();

		Rectangle rect = this.tblDest.getCellRect(
				this.tblDest.getRowCount() - 1,
				this.tblDest.getColumnCount() - 1, true);
		this.tblDest.scrollRectToVisible(rect);
		rect = this.tblSource.getCellRect(this.tblSource.getRowCount() - 1,
				this.tblSource.getColumnCount() - 1, true);
		this.tblSource.scrollRectToVisible(rect);

		this.tblSource.getSelectionModel().setSelectionInterval(
				this.tblSource.getRowCount() - 1,
				this.tblSource.getRowCount() - 1);
	}

	/**
     *
     */
	private void refreshEndpoints() {
		refreshMap();
		this.tblDest.setModel(getEndpointTableModel());
		this.tblSource.setModel(getEndpointTableModel());
		repaint();
	}

	/**
	 * @param ep
	 */
	public final void refreshEndpoints(final List<EndpointType> ep) {
		for (EndpointType endpointType : ep) {
			if (!this.endpointMap.containsKey(endpointType.getEndpointId())) {
				getEndpointTableModel().addRow(endpointType);
				this.endpointMap
						.put(endpointType.getEndpointId(), endpointType);
			}
		}

	}

	/**
	 * Set start time to current time.
	 */
	public final void resetStartTime() {
		this.txtStart.setText(this.sdf.format(new Date()));
	}

	/**
	 * Method to react on events. It is called every time the user acts somehow.
	 * 
	 * @param e
	 *            the event that was triggered.
	 */
	public final void actionPerformed(final ActionEvent e) {
		Object src = e.getSource();
		setCursor(new Cursor(Cursor.WAIT_CURSOR));
		if (this.cmdCreate.equals(src)) {
			createReservation();
		} else if (this.cmdAddEndpoint.equals(src)) {
			addEndpointToTable();
			System.out.println("Add endpoint"
					+ getEndpointTableModel().getContents().size());
		} else if (this.cmdDeleteEndpoint.equals(src)) {
			getEndpointTableModel().removeRow(this.tblSource.getSelectedRow());
			getEndpointTableModel().removeRow(this.tblDest.getSelectedRow());
		}
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	/**
	 * This method initializes cmdDeleteEndpoint
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getCmdDeleteEndpoint() {
		if (this.cmdDeleteEndpoint == null) {
			this.cmdDeleteEndpoint = new JButton();
			this.cmdDeleteEndpoint.setText("Delete Endpoint");
			this.cmdDeleteEndpoint.addActionListener(this);
		}
		return this.cmdDeleteEndpoint;
	}
}
