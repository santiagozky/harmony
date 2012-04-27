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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.muse.ws.addressing.soap.SoapFault;
import org.xml.sax.SAXException;

import org.opennaas.extensions.gmpls.client.utils.GmplsWS;
import org.opennaas.extensions.gmpls.client.utils.NspWS;
import org.opennaas.extensions.gmpls.client.utils.TableFitter;
import org.opennaas.extensions.gmpls.client.views.components.EndpointTable;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.CreatePathResponseType;
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
import org.opennaas.extensions.idb.serviceinterface.reservation.SimpleReservationClient;
import org.opennaas.core.utils.Config;

/**
 * This is the panel for the gmpls client where the user can create a path.
 * 
 * @author Stephan Wagner (stephan.wagner@iais.fraunhofer.de), Daniel Beer
 *         (daniel.beer@iais.fraunhofer.de)
 */
public class GmplsCreatePathPanel extends JPanel implements
		ListSelectionListener, ActionListener {

	/** */
	private static final long serialVersionUID = 1L;

	/** List with TNA Information. */
	private List<EndpointType> endpointList = null;
	/** Map with all information for all TNA's. */
	private Map<String, EndpointType> endpointMap = null;
	/** Title of the page. */
	private JLabel lblTitle = null;
	/** Label for the source part. */
	private JLabel lblSource = null;
	/** scrollable view for the source table. */
	private JScrollPane scrollSource = null;
	/** Table with source TNA information. */
	private JTable tblSource = null;
	/** Label for the destination part. */
	private JLabel lblDest = null;
	/** scrollable view for the destination table. */
	private JScrollPane scrollDest = null;
	/** Table with destination TNA information. */
	private JTable tblDest = null;
	/** Label for the Bandwidth field. */
	private JLabel lblBandwidth = null;
	/** Input field for the Bandwidth. */
	private JTextField txtBandwidth = null;
	/** Radio button GMPLS. */
	private JRadioButton optGMPLS = null;
	/** Radio button Thin Nrps. */
	private JRadioButton optThin = null;
	/** Radio button NSP. */
	private JRadioButton optNSP = null;
	/**
	 * Panel with Path creation Information and the possibility to change
	 * between GMPLS, Thin NRPS and NSP.
	 */
	private JPanel pnlAdvance = null;
	/** Start time. */
	private JLabel lblStart = null;
	/** Duration. */
	private JLabel lblDuration = null;
	/** Input field for the start time. */
	private JTextField txtStart = null;
	/** Input field for the duration. */
	private JTextField txtDuration = null;
	/** Create button. */
	private JButton cmdCreate = null;
	/** Date formatter to display the date in a common fashion. */
	private SimpleDateFormat sdf = null;

	/**
	 * Creates this Panel.
	 * 
	 * @param list
	 *            list with TNA addresses
	 */
	public GmplsCreatePathPanel(final List<EndpointType> list) {
		super();
		this.sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.endpointList = list;
		this.endpointMap = new HashMap<String, EndpointType>(0);
		this.endpointMap.clear();
		for (final EndpointType listType : list) {
			this.endpointMap.put(listType.getEndpointId(), listType);

		}
		this.initialize();
	}

	/**
	 * Initializes this Panel.
	 */
	private void initialize() {
		final GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
		gridBagConstraints12.gridx = 4;
		gridBagConstraints12.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints12.anchor = GridBagConstraints.SOUTHEAST;
		gridBagConstraints12.gridy = 8;
		final GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.gridx = 0;
		gridBagConstraints11.gridwidth = 3;
		gridBagConstraints11.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints11.gridy = 7;
		gridBagConstraints11.insets = new Insets(5, 25, 5, 5);
		final GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
		gridBagConstraints10.gridx = 2;
		gridBagConstraints10.gridy = 6;
		final GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
		gridBagConstraints9.gridx = 1;
		gridBagConstraints9.gridy = 6;
		final GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
		gridBagConstraints8.gridx = 0;
		gridBagConstraints8.gridy = 6;
		final GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
		gridBagConstraints7.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints7.gridy = 5;
		gridBagConstraints7.anchor = GridBagConstraints.WEST;
		gridBagConstraints7.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints7.gridx = 1;
		final GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
		gridBagConstraints6.gridx = 0;
		gridBagConstraints6.anchor = GridBagConstraints.WEST;
		gridBagConstraints6.gridy = 5;
		gridBagConstraints6.insets = new Insets(2, 5, 2, 2);
		final GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
		gridBagConstraints5.fill = GridBagConstraints.BOTH;
		gridBagConstraints5.gridy = 4;
		gridBagConstraints5.gridwidth = 5;
		gridBagConstraints5.weightx = 1.0D;
		gridBagConstraints5.weighty = 1.0D;
		gridBagConstraints5.insets = new Insets(2, 2, 2, 2);
		gridBagConstraints5.gridx = 0;
		final GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.gridx = 0;
		gridBagConstraints4.gridy = 3;
		gridBagConstraints4.anchor = GridBagConstraints.WEST;
		gridBagConstraints4.insets = new Insets(2, 5, 2, 2);
		final GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.fill = GridBagConstraints.BOTH;
		gridBagConstraints3.gridy = 2;
		gridBagConstraints3.gridwidth = 5;
		gridBagConstraints3.weightx = 1.0D;
		gridBagConstraints3.weighty = 1.0D;
		gridBagConstraints3.insets = new Insets(2, 7, 2, 2);
		gridBagConstraints3.gridx = 0;
		final GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.gridy = 1;
		gridBagConstraints2.anchor = GridBagConstraints.WEST;
		gridBagConstraints2.insets = new Insets(2, 5, 2, 2);
		final GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
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

		this.setLayout(new GridBagLayout());

		this.add(this.getScrollSource(), gridBagConstraints3);
		this.add(this.lblTitle, gridBagConstraints1);
		this.add(this.lblSource, gridBagConstraints2);
		this.add(this.getScrollDest(), gridBagConstraints5);
		this.add(this.lblDest, gridBagConstraints4);
		this.add(this.lblBandwidth, gridBagConstraints6);
		this.add(this.getTxtBandwidth(), gridBagConstraints7);
		this.add(this.getOptGMPLS(), gridBagConstraints8);
		this.add(this.getOptThin(), gridBagConstraints9);
		this.add(this.getOptNSP(), gridBagConstraints10);
		this.add(this.getPnlAdvance(), gridBagConstraints11);
		this.add(this.cmdCreate(), gridBagConstraints12);
		final ButtonGroup bGroup = new ButtonGroup();
		bGroup.add(this.getOptGMPLS());
		bGroup.add(this.getOptNSP());
		bGroup.add(this.getOptThin());
	}

	/** Method to create a Path. */
	public final void createPath() {
		try {
			final int bandwidth = Integer.valueOf(this.txtBandwidth.getText())
					.intValue();
			if (bandwidth <= 0) {
				throw new BandwidthFaultException("Bandwidth too small");
			}
			final String sourceTNA = (String) this.tblSource.getValueAt(
					this.tblSource.getSelectedRow(), 0);
			final String destTNA = (String) this.tblDest.getValueAt(
					this.tblDest.getSelectedRow(), 0);

			if (this.optGMPLS.isSelected()) {
				final CreatePathResponseType crt = GmplsWS.createPath(
						sourceTNA, destTNA, bandwidth);

				JOptionPane.showMessageDialog(this,
						"Path created! Path identifier is: "
								+ crt.getPathIdentifier().getPathIdentifier(),
						"Path created!", JOptionPane.INFORMATION_MESSAGE);
			} else {
				final CreateReservationType request = new CreateReservationType();
				final ServiceConstraintType service = new ServiceConstraintType();
				final ConnectionConstraintType connection = new ConnectionConstraintType();
				final FixedReservationConstraintType frc = new FixedReservationConstraintType();

				final int duration = Integer.parseInt(this.txtDuration
						.getText()) * 60;
				request.setJobID(new Long(System.currentTimeMillis()));

				service.setAutomaticActivation(true);
				service.setServiceID(1);
				service.setTypeOfReservation(ReservationType.FIXED);
				frc.setDuration(duration);
				final GregorianCalendar cal = new GregorianCalendar();

				cal.setTime(this.sdf.parse(this.txtStart.getText()));
				final XMLGregorianCalendar start = DatatypeFactory
						.newInstance().newXMLGregorianCalendar(cal);
				frc.setStartTime(start);
				service.setFixedReservationConstraints(frc);

				connection.setConnectionID(1);
				connection.setMinBW(bandwidth);
				connection.setMaxBW(Integer.valueOf(bandwidth));
				final org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointType src = new org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointType();
				final org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointType dest = new org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointType();
				src.setEndpointId(sourceTNA);
				dest.setEndpointId(destTNA);
				connection.setSource(src);
				connection.getTarget().add(dest);

				service.getConnections().add(connection);
				request.getService().add(service);

				CreateReservationResponseType response = null;
				SimpleReservationClient resClient = null;
				if (this.optThin.isSelected()) {
					resClient = new SimpleReservationClient(Config.getString(
							"gmplsClient", "epr.thin.reservationEPR"));
					response = resClient.createReservation(request);
				} else {
					response = NspWS.createReservation(request);
				}

				JOptionPane
						.showMessageDialog(this,
								"Reservation created! Reservation identifier is: "
										+ response.getJobID() + " - "
										+ response.getReservationID(),
								"Reservation created!",
								JOptionPane.INFORMATION_MESSAGE);

			}
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
		} catch (final ParseException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, e.getMessage(),
					"ParseException", JOptionPane.ERROR_MESSAGE);
		} catch (final DatatypeConfigurationException e) {
			e.printStackTrace();
			JOptionPane
					.showMessageDialog(this, e.getMessage(),
							"DatatypeConfigurationException",
							JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Recalculates the bandwidth when the selection changes.
	 * 
	 * @param e
	 *            Event that was triggered.
	 */
	@SuppressWarnings("unused")
	public final void valueChanged(final ListSelectionEvent e) {
		this.txtBandwidth
				.setText((String) this.minMaxBW(
						this.tblSource.getSelectedRow(),
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
				final int d = ((Integer) this.tblDest.getModel().getValueAt(
						dest, 1)).intValue();
				final int s = ((Integer) this.tblDest.getModel().getValueAt(
						src, 1)).intValue();
				ret = Integer.toString(Math.min(s, d));
			} else {
				ret = ((Integer) this.tblSource.getModel().getValueAt(src, 1))
						.toString();
			}
		} else {
			if (dest >= 0) {
				ret = ((Integer) this.tblSource.getModel().getValueAt(dest, 1))
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
			this.scrollSource.setViewportView(this.getTblSource());
		}
		return this.scrollSource;
	}

	/**
	 * This method initializes tblSource.
	 * 
	 * @return javax.swing.JTable
	 */
	private JTable getTblSource() {
		if (this.tblSource == null) {
			this.tblSource = new JTable(new EndpointTable(this.endpointList));
			this.tblSource.getSelectionModel().addListSelectionListener(this);
			this.tblSource
					.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			TableFitter.fitToContent(this.tblSource);
		}
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
			this.scrollDest.setViewportView(this.getTblDest());
		}
		return this.scrollDest;
	}

	/**
	 * This method initializes tblDest.
	 * 
	 * @return javax.swing.JTable
	 */
	private JTable getTblDest() {
		if (this.tblDest == null) {
			this.tblDest = new JTable(new EndpointTable(this.endpointList));
			this.tblDest.getSelectionModel().addListSelectionListener(this);
			this.tblDest.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			TableFitter.fitToContent(this.tblDest);
		}
		return this.tblDest;
	}

	/**
	 * This method initializes txtBandwidth.
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getTxtBandwidth() {
		if (this.txtBandwidth == null) {
			this.txtBandwidth = new JTextField();
		}
		return this.txtBandwidth;
	}

	/**
	 * This method initializes optGMPLS.
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getOptGMPLS() {
		if (this.optGMPLS == null) {
			this.optGMPLS = new JRadioButton();
			this.optGMPLS.setText("use GMPLS WS");
			this.optGMPLS.addActionListener(this);
			this.optGMPLS.setSelected(true);
		}
		return this.optGMPLS;
	}

	/**
	 * This method initializes optThin.
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getOptThin() {
		if (this.optThin == null) {
			this.optThin = new JRadioButton();
			this.optThin.setText("use ThinNRPS");
			this.optThin.addActionListener(this);
		}
		return this.optThin;
	}

	/**
	 * This method initializes optNSP.
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getOptNSP() {
		if (this.optNSP == null) {
			this.optNSP = new JRadioButton();
			this.optNSP.setText("use NSP");
			this.optNSP.addActionListener(this);
		}
		return this.optNSP;
	}

	/**
	 * This method initializes pnlAdvance.
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getPnlAdvance() {
		if (this.pnlAdvance == null) {
			final GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.gridx = 0;
			gridBagConstraints15.anchor = GridBagConstraints.WEST;
			gridBagConstraints15.gridy = 1;
			final GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.gridx = 0;
			gridBagConstraints14.anchor = GridBagConstraints.WEST;
			gridBagConstraints14.gridy = 0;
			final GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.fill = GridBagConstraints.BOTH;
			gridBagConstraints13.gridx = 1;
			gridBagConstraints13.gridy = 1;
			gridBagConstraints13.anchor = GridBagConstraints.WEST;
			gridBagConstraints13.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints13.weightx = 1.0;
			gridBagConstraints13.gridwidth = 2;
			final GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
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
			this.pnlAdvance.add(this.getTxtStart(), gridBagConstraints12);
			this.pnlAdvance.add(this.lblDuration, gridBagConstraints15);
			this.pnlAdvance.add(this.getTxtDuration(), gridBagConstraints13);
			this.pnlAdvance.setEnabled(false);
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
			this.txtStart.setEnabled(false);
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
			this.txtDuration.setEnabled(false);
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
	 * Method to react on events. It is called every time the user acts somehow.
	 * 
	 * @param e
	 *            the event that was triggered.
	 */
	public final void actionPerformed(final ActionEvent e) {
		final Object src = e.getSource();

		if (this.cmdCreate.equals(src)) {
			this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			this.createPath();
			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		} else if (this.optGMPLS.equals(src) || this.optNSP.equals(src)
				|| this.optThin.equals(src)) {
			final boolean en = !this.optGMPLS.isSelected();
			this.getPnlAdvance().setEnabled(en);
			this.getTxtDuration().setEnabled(en);
			this.getTxtStart().setEnabled(en);
		}

	}
}
