package org.opennaas.extensions.gmpls.client.views.components;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.opennaas.extensions.gmpls.client.utils.PathInformation;

/**
 * This Table extends the AbstractTableModel and is used to display information
 * about paths that are active on the gmplsWS.
 *
 * @author Daniel Beer daniel.beer@iais.fraunhofer.de, Stephan Wagner
 *         stephan.wagner@iais.fraunhofer.de
 */
public class GmplsPathTable extends AbstractTableModel {

    /***/
    private static final long serialVersionUID = 2064332391862743569L;
    /** Names of the columns. */
    private final String[] columnNames =
            { "LSPIndex", "SourceTNA", "DestTNA", "Bandwidth", "" };
    /** Types of the columns. */
    private final Class<?>[] columnClasses =
            { Integer.class, String.class, String.class, Integer.class,
                    String.class };
    /** Contents of the table. */
    private List<PathInformation> contents = new ArrayList<PathInformation>();

    /**
     * Constructor for the GmplsPathTable.
     *
     * @param cont
     *                content to initialize the table with.
     */
    public GmplsPathTable(final List<PathInformation> cont) {
        this.contents = cont;
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
     *                index of the column to get
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
     *                row to get
     * @param y
     *                column to get
     * @return object representing the content of the field at x, y
     */
    public final Object getValueAt(final int x, final int y) {
        Object ret = null;

        switch (y) {
            case 0:
                ret = Integer.valueOf(this.contents.get(x).getLspIndex());
                break;
            case 1:
                ret = this.contents.get(x).getSrcTNA();
                break;
            case 2:
                ret = this.contents.get(x).getDestTNA();
                break;
            case 3:
                ret = Integer.valueOf(this.contents.get(x).getBandwidth());
                break;
            case 4:
                ret = "";
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
     * Sets the contents of the list.
     *
     * @param cont
     *                the content of the list
     */
    public final void setContents(final List<PathInformation> cont) {
        this.contents = cont;
        fireTableDataChanged();
    }
}
