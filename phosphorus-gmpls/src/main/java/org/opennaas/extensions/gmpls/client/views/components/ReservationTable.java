package org.opennaas.extensions.gmpls.client.views.components;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import org.apache.muse.ws.addressing.soap.SoapFault;

import org.opennaas.extensions.gmpls.client.utils.NspWS;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ConnectionConstraintType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ConnectionStatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DomainConnectionStatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetReservationsComplexType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetStatusResponseType.ServiceStatus;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.InvalidReservationIDFaultException;
import org.opennaas.extensions.idb.serviceinterface.databinding.utils.WebserviceUtils;

/**
 * This Table extends the AbstractTableModel and is used to display all
 * information about current Reservations.
 * 
 * @author Daniel Beer daniel.beer@iais.fraunhofer.de, Stephan Wagner
 *         stephan.wagner@iais.fraunhofer.de
 */
public class ReservationTable extends AbstractTableModel {

    /**
     *
     */
    private static final long serialVersionUID = 2064332391862743569L;
    /** Names of the columns. */
    private final String[] columnNames = { "ReservationId", "JobId", "Status" };
    /** Types of the columns. */
    private final Class<?>[] columnClasses = { Long.class, Long.class,
	    String.class };
    /** Contents of the table. */
    private List<GetReservationsComplexType> contents = new ArrayList<GetReservationsComplexType>();
    private final Map<Long, String> statusMap = new HashMap<Long, String>();

    /**
     * Constructor for the ReservationTable.
     * 
     * @param list
     *            content to initialize the table with.
     * @throws InvalidReservationIDFaultException
     */
    public ReservationTable(final List<GetReservationsComplexType> list)
	    throws InvalidReservationIDFaultException {
	super();
	this.contents = list;
	updateStatus(0);
    }

    public void updateStatus(long reservationId)
	    throws InvalidReservationIDFaultException {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	for (GetReservationsComplexType res : this.contents) {
	    String ret = "";

	    int duration = res.getReservation().getService().get(0)
		    .getFixedReservationConstraints().getDuration();
	    GregorianCalendar starttime = res.getReservation().getService()
		    .get(0).getFixedReservationConstraints().getStartTime()
		    .toGregorianCalendar();
	    ret = ret + "Starttime: " + sdf.format(starttime.getTime()) + "\n";

	    starttime.add(Calendar.SECOND, duration);
	    ret = ret + "Endtime: " + sdf.format(starttime.getTime()) + "\n";
	    ret = ret + "Duration (min): " + (duration / 60) + "\n";
	    for (ConnectionConstraintType conn : res.getReservation()
		    .getService().get(0).getConnections()) {
		ret = ret + "Source: " + conn.getSource().getEndpointId()
			+ "Destination: "
			+ conn.getTarget().get(0).getEndpointId() + "\n";
	    }

	    List<ServiceStatus> list = new ArrayList<ServiceStatus>();
	    if (WebserviceUtils.convertReservationID(res.getReservationID()) == reservationId) {
		try {
		    list = NspWS.getStatus(
			    WebserviceUtils.convertReservationID(res
				    .getReservationID())).getServiceStatus();

		} catch (final SoapFault e) {
		    e.printStackTrace();
		}
		for (final ServiceStatus serviceStatus : list) {

		    for (ConnectionStatusType conn : serviceStatus
			    .getConnections()) {

			for (final DomainConnectionStatusType domainConnectionStatusType : conn
				.getDomainStatus()) {

			    ret = ret + "Domain: "
				    + domainConnectionStatusType.getDomain()
				    + " TNA: ";

			    ret = ret
				    + domainConnectionStatusType.getStatus()
					    .getSource().getEndpointId()
				    + " - "
				    + domainConnectionStatusType.getStatus()
					    .getTarget().get(0).getEndpointId();

			    ret = ret
				    + " Status:"
				    + domainConnectionStatusType.getStatus()
					    .getStatus().name() + "\n";
			}
		    }
		}
	    }
	    this.statusMap.put(Long.valueOf(res.getReservationID()), ret);

	}

    }

    /**
     * Gets the number of columns in the table.
     * 
     * @return number of columns
     */
    public final int getColumnCount() {
	return this.columnNames.length;
    }

    /**
     * Gets the number of rows.
     * 
     * @return number of rows
     */
    public final int getRowCount() {
	return this.contents.size();
    }

    /**
     * Gets the name of the column at a specified position.
     * 
     * @param col
     *            index of the column to get
     * @return name of the column
     */
    @Override
    public final String getColumnName(final int col) {
	return this.columnNames[col];
    }

    /**
     * Gets the value of one field in the table. If the StatusColumn is
     * specified it will get the information from a remote source.
     * 
     * @param x
     *            row to get
     * @param y
     *            column to get
     * @return object representing the content of the field at x, y
     */
    public final Object getValueAt(final int x, final int y) {
	Object ret = null;

	switch (y) {
	case 0: // reservationId
	    ret = Long.valueOf(this.contents.get(x).getReservationID());
	    break;
	case 1: // jobId
	    ret = this.contents.get(x).getReservation().getJobID();
	    break;
	case 2: // status
	    ret = this.statusMap.get(Long.valueOf(this.contents.get(x)
		    .getReservationID()));
	    break;
	default:
	    break;
	}

	return ret;
    }

    /** Gets the Class of a specified column. */
    @Override
    public final Class<?> getColumnClass(final int c) {
	return this.columnClasses[c];
    }

    /** Sets the cell at x, y editable. */
    @Override
    @SuppressWarnings("unused")
    public final boolean isCellEditable(final int row, final int col) {
	return false;
    }

    /**
     * Sets the content of the cell.
     * 
     * @throws InvalidReservationIDFaultException
     */
    public final void setContents(final List<GetReservationsComplexType> cont)
	    throws InvalidReservationIDFaultException {
	this.contents = cont;
	updateStatus(0);
	fireTableDataChanged();
    }
}
