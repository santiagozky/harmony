package org.opennaas.extensions.gmpls.client.views.components;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.opennaas.extensions.gmpls.client.utils.EndpointListSorter;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.EndpointInterfaceType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.EndpointType;

/**
 * This Table extends the AbstractTableModel and is used to display information
 * about endpoints.
 * 
 * @author Daniel Beer daniel.beer@iais.fraunhofer.de, Stephan Wagner
 *         stephan.wagner@iais.fraunhofer.de
 */
public class EndpointTable extends AbstractTableModel {
    /***/
    private static final long serialVersionUID = 2734120212952726750L;
    /** Names of the columns in the table. */
    private final String[] columnNames = { "TNA", "Bandwidth", "Description",
	    "Domain", "InterfaceType", "" };
    /** Classes of data that are to be displayed in the columns. */
    private final Class<?>[] columnClasses = { String.class, Integer.class,
	    String.class, String.class, String.class, String.class };
    /** List of endpoints to be displayed. */
    private List<EndpointType> contents = new ArrayList<EndpointType>();

    /**
     * Constructor that sets the content of the table.
     * 
     * @param list
     *            The content to set.
     */
    public EndpointTable(final Object list) {
	super();
	List<Object> oList = (List<Object>) list;
	if (oList.get(0).getClass() == EndpointType.class) {

	    List<EndpointType> endpointList = new ArrayList<EndpointType>(0);
	    for (Object object : oList) {
		endpointList.add((EndpointType) object);
	    }
	    EndpointListSorter.sort(endpointList);
	    this.contents = endpointList;
	    fireTableDataChanged();
	} else if (oList.get(0).getClass() == org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointType.class) {
	    List<EndpointType> endpointList = new ArrayList<EndpointType>(0);
	    for (Object object : oList) {
		org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointType harmonyEndpoint = (org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointType) object;

		EndpointType ep = new EndpointType();
		ep.setBandwidth(harmonyEndpoint.getBandwidth());
		ep.setDescription(harmonyEndpoint.getDescription());
		ep.setDomainId(harmonyEndpoint.getDomainId());
		ep.setEndpointId(harmonyEndpoint.getEndpointId());
		if (harmonyEndpoint.getInterface() == org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointInterfaceType.BORDER) {
		    ep.setInterface(EndpointInterfaceType.BORDER);
		} else {
		    ep.setInterface(EndpointInterfaceType.USER);
		}
		ep.setName(harmonyEndpoint.getName());
		endpointList.add((EndpointType) object);
	    }
	    EndpointListSorter.sort(endpointList);
	    this.contents = endpointList;
	    fireTableDataChanged();
	}
    }

    /**
     * Gets the number of columns.
     * 
     * @return The number of columns
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
     * Gets the column name.
     * 
     * @param col
     *            The column to get
     * @return The name of the column
     */
    @Override
    public final String getColumnName(final int col) {
	return this.columnNames[col];
    }

    /**
     * Gets the value at x, y.
     * 
     * @param x
     *            x-axis coordinate
     * @param y
     *            y-axis coordinate
     * @return Object at position x,y
     */
    public final Object getValueAt(final int x, final int y) {
	Object ret = null;
	if (null != this.contents.get(x)) {
	    switch (y) {
	    case 0:
		ret = this.contents.get(x).getEndpointId();
		break;
	    case 1:
		ret = this.contents.get(x).getBandwidth();
		break;
	    case 2:
		ret = this.contents.get(x).getDescription();
		break;
	    case 3:
		ret = this.contents.get(x).getDomainId();
		break;
	    case 4:
		if (null != this.contents.get(x).getInterface()) {
		    ret = this.contents.get(x).getInterface().toString();
		} else {
		    ret = "";
		}
		break;
	    case 5:
		ret = "";
		break;
	    default:
		break;
	    }
	    return ret;
	}
	return new Integer(0);
    }

    /**
     * Gets the class of the column.
     * 
     * @param c
     *            The column to get
     * @return The class of the column
     */
    @Override
    public final Class<?> getColumnClass(final int c) {
	return this.columnClasses[c];
    }

    /**
     * Specifies whether the cell is editable.
     * 
     * @param row
     *            The row to read
     * @param col
     *            The column to read
     * @return true if the cell should be editable
     */
    @Override
    @SuppressWarnings("unused")
    public final boolean isCellEditable(final int row, final int col) {
	if (this.contents.get(row).getInterface() == EndpointInterfaceType.BORDER) {
	    return false;
	}
	return (col < 4);
    }

    /**
     * Sets the contents of the list.
     * 
     * @param cont
     *            the content of the list
     */
    public final void setContents(final List<EndpointType> cont) {
	EndpointListSorter.sort(cont);
	this.contents = cont;
	fireTableDataChanged();
    }

    /**
     * Returns list of endpoints.
     * 
     * @return the endpoints in this table
     */
    public final List<EndpointType> getContents() {
	return this.contents;
    }

    @Override
    public final void setValueAt(final Object value, final int rowIndex,
	    final int columnIndex) {
	switch (columnIndex) {
	case 0:
	    this.contents.get(rowIndex).setEndpointId((String) value);
	    break;
	case 1:
	    this.contents.get(rowIndex).setBandwidth((Integer) value);
	    break;
	case 2:
	    this.contents.get(rowIndex).setDescription((String) value);
	    break;
	case 3:
	    this.contents.get(rowIndex).setDomainId((String) value);
	    break;
	case 4:
	    this.contents.get(rowIndex).setInterface(
		    EndpointInterfaceType.fromValue((String) value));
	    break;
	default:
	    break;
	}

	fireTableCellUpdated(rowIndex, columnIndex);
    }

    /**
     * Add an Enpoint as row to table.
     * 
     * @param ep
     *            the endpoint
     */
    public final void addRow(final EndpointType ep) {

	this.contents.add(ep);
	EndpointListSorter.sort(this.contents);
	fireTableDataChanged();
    }

    /**
     * Remove a row from table.
     * 
     * @param row
     *            the row to remove
     */
    public final void removeRow(final int row) {
	if (row >= 0) {
	    this.contents.remove(row);
	    fireTableRowsDeleted(row, row);
	}
    }
}
