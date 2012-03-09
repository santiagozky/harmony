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


package client.template.panel;

import java.util.Vector;

import client.helper.ExecutionCallbackHandler;
import client.template.dialog.InfoDialog;
import client.types.TableField;
import client.types.TableRow;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.user.client.ui.Widget;

/**
 * Generic class to represent a list page. This is the Read-Tasks together with
 * update and delete from CRUD.
 * 
 * @author gassen
 */
public abstract class ListPanel extends Composite implements TableListener {

    /** Const for CLicklistener. */
    public static final int NONE = -1;
    /** Const for CLicklistener. */
    public static final int ALL = -2;
    /** Array containing all column numbers which should show popups. */
    private int popupCells[] = new int[] { ListPanel.ALL };
    /** Array containing all column numbers which should show detail rows. */
    private int detailCells[] = new int[] { ListPanel.ALL };

    /** the core table. */
    private FlexTable table;
    /** last inserted row. */
    private int lastRow = 1;
    /** last selected row. */
    private int selectedRow = 1;
    /**
     * last sorted col. Initial value = default sorting. -1 = no default
     * sorting.
     */
    private int lastSortedCol = -1;
    /** last sorted direction. */
    private boolean lastSortedDirection = true;

    /**
     * This vector is a representation of all rows which are not detail rows.
     */
    private final Vector<client.types.TableRow> rows =
            new Vector<client.types.TableRow>();

    /**
     * This vector identifies rows and detail rows. Rows are represented by
     * their line, detail rows by -1.
     */
    private final Vector<java.lang.Integer> index =
            new Vector<java.lang.Integer>();

    /**
     * This Vector represents a one to one mapping of displayed row to object.
     */
    private final Vector<client.types.TableRow> map =
            new Vector<client.types.TableRow>();

    /**
     * Store which col was sorte in which order.
     */
    private final Vector<java.lang.Boolean> lastSort =
            new Vector<java.lang.Boolean>();

    /** default dialog. */
    private final InfoDialog defaultDialog =
            new InfoDialog("Info", "No details available");

    /**
     * Method to be Implemented by Subclasses.
     * 
     * @param result
     */
    public abstract void onData(Object result);

    /**
     * Method to refreh list Content.
     * 
     * @param value
     *            Timeframe.
     * @param callback
     *            Callback Object for Result.
     */
    public abstract void refresh(int value, AsyncCallback<?> callback);

    /**
     * Method called if exec button is pressed.
     * 
     * @param type
     *            Type of execution
     * @param checked
     *            Checked ids
     */
    public abstract void execute(String type, Vector<String> checked,
            ExecutionCallbackHandler callback);

    /**
     * Method should return true if fields should be swapped.
     * 
     * @param col
     *            Column Column number
     * @param field1
     *            field1 TableRow object
     * @param field2
     *            field2 TableRow object
     * @return True if fields are in wrong order
     */
    protected abstract boolean fieldCompare(int col, TableField field1,
            TableField field2);

    /**
     * Select a row. Toggle Detail Row visibility if needed.
     * 
     * @param row
     *            Clicked Row
     * @param cel
     *            Clicked Cell
     */
    private void selectRow(final int row, final int cell) {
        final Integer key = this.index.get(row);

        if ((key.intValue() >= 0) && this.checkCol(cell, this.detailCells)) {
            final TableRow selected = this.rows.get(key.intValue());

            final Vector<TableRow> details = selected.getDetails();

            for (int i = 0; i < details.size(); i++) {
                final TableRow value = (TableRow) details.get(i);

                final boolean visibility =
                        this.table.getRowFormatter().isVisible(
                                value.getRow() + 1);

                this.table.getRowFormatter().setVisible(value.getRow() + 1,
                        !visibility);
            }
        } else {
            return;
        }

        this.styleRow(this.selectedRow, false);
        this.styleRow(row, true);

        this.selectedRow = row;
    }

    /**
     * Check if column exists in field.
     * 
     * @param col
     *            Clicked column
     * @param field
     *            Field of set columns
     * @return true if column is found
     */
    private final boolean checkCol(final int col, final int[] field) {
        for (int x = 0; x < field.length; x++) {
            if ((col == field[x]) || (ListPanel.ALL == field[x])) {
                return true;
            } else if (ListPanel.NONE == field[x]) {
                return false;
            }
        }

        return false;
    }

    /**
     * Shows the popup.
     * 
     * @param row
     *            Table Row
     * @param col
     *            Table Column
     */
    private void showPopUp(final int row, final int col) {
        if (false == this.checkCol(col, this.popupCells)) {
            return;
        }

        final TableRow rowData = this.map.get(row);

        final DialogBox dialog =
                (rowData.getDialog() == null) ? this.defaultDialog : rowData
                        .getDialog();

        dialog.show();
        dialog.center();
    }

    /**
     * Set the rows style.
     * 
     * @param row
     *            Clicked row id
     * @param selected
     *            selected/not selected
     */
    private void styleRow(final int row, final boolean selected) {
        if (row != -1) {
            if (selected) {
                this.table.getRowFormatter().addStyleName(row + 1,
                        "res-SelectedRow");
            } else {
                this.table.getRowFormatter().removeStyleName(row + 1,
                        "res-SelectedRow");
            }
        }
    }

    /**
     * Internal method to add a row
     * 
     * @param row
     *            new row
     */
    private final void importRow(final TableRow row) {
        for (int i = 0; i < row.getFields().size(); i++) {
            final TableField field = (TableField) row.getFields().get(i);

            if (TableField.STRING == field.getType()) {
                this.table.setText(this.lastRow, i, (String) field
                        .getDisplayObject());
            } else if (TableField.WIDGET == field.getType()) {
                this.table.setWidget(this.lastRow, i, (Widget) field
                        .getDisplayObject());
            } else if (TableField.HTML == field.getType()) {
                this.table.setHTML(this.lastRow, i, ((HTML) field
                        .getDisplayObject()).getHTML());
            } else {
                throw new RuntimeException("Invalid field");
            }

            if (field.getColSpan() > 1) {
                this.table.getFlexCellFormatter().setColSpan(this.lastRow, i,
                        field.getColSpan());
            }
        }

        if (null != row.getSyle()) {
            this.table.getRowFormatter().addStyleName(this.lastRow,
                    row.getSyle());
        }

        this.map.add(row);

        this.lastRow++;
    }

    /**
     * Initialize the list.
     */
    private void init() {
        this.lastRow = 1;
        this.selectedRow = 1;

        this.table = new FlexTable();

        this.table.setCellSpacing(0);
        this.table.setCellPadding(0);
        this.table.setWidth("100%");

        this.table.addTableListener(this);

        this.initWidget(this.table);
        this.setStyleName("mail-List");
    }

    /**
     * Constructor. Create all the necessary stuff...
     */
    public ListPanel() {
        this.init();
    }

    /**
     * Define the columns to which to mouselistener shoul listen.
     * 
     * @param col
     *            column id
     */
    public final void setPopupListener(final int col) {
        this.setPopupListener(new int[] { col });
    }

    /**
     * Define the columns to which to mouselistener shoul listen.
     * 
     * @param col
     *            Field of column ids
     */
    public final void setPopupListener(final int col[]) {
        this.popupCells = col;
    }

    /**
     * Define the columns the details is shown on click.
     * 
     * @param col
     *            column id
     */
    public final void setDetailListener(final int col) {
        this.detailCells = new int[] { col };
    }

    /**
     * Define the columns the details is shown on click.
     * 
     * @param col
     *            Field of column ids
     */
    public final void setDetailListener(final int col[]) {
        this.detailCells = col;
    }

    /**
     * Set the default sort col and direction.
     * 
     * @param col
     *            Column
     * @param descending
     *            Sorting direction for this column
     */
    public void setDefaultSorting(final int col, final boolean descending) {
        this.lastSortedCol = col;
        this.lastSortedDirection = descending;
    }

    /**
     * Set table title.
     * 
     * @param fields
     *            table title fields
     */
    public final void setTitleRow(final String[] fields) {
        for (int i = 0; i < fields.length; i++) {
            this.table.setText(0, i, fields[i]);
            this.lastSort.add(new Boolean(true));
        }

        this.table.getRowFormatter().setStyleName(0, "mail-ListHeader");
    }

    /**
     * Get the internal used FlexTable.
     * 
     * @return Internal FlexTable.
     */
    public FlexTable getTable() {
        return this.table;
    }

    /**
     * Get a vector with all Objects shown in the rows
     * 
     * @return The internal Dat vector.
     */
    public Vector<TableRow> getRows() {
        return this.rows;
    }

    /**
     * add a table row.
     * 
     * @param row
     *            table row
     */
    public final void addRow(final TableRow row) {
        this.importRow(row);

        row.setRow(this.lastRow - 1);

        this.index.add(new Integer(this.rows.size()));
        this.rows.add(row);

        for (int x = 0; x < row.getDetails().size(); x++) {
            this.addDetailRow((TableRow) row.getDetails().get(x));
        }
    }

    /**
     * Add adetail row to the last inserted Main Row.
     * 
     * @param row
     *            Detail Row
     */
    private final void addDetailRow(final TableRow row) {
        this.importRow(row);

        row.setRow(this.lastRow - 2);

        this.table.getRowFormatter().setVisible(this.lastRow - 1, false);

        this.index.add(new Integer(-1));
    }

    /**
     * Handle onCellClicked Event.
     * 
     * @param sender
     *            Event Sender
     * @param row
     *            Clicked Row
     * @param cell
     *            Clicked Cell
     */
    public final void onCellClicked(final SourcesTableEvents sender,
            final int row, final int cell) {

        if (row > 0) {
            this.selectRow(row - 1, cell);
            this.showPopUp(row - 1, cell);
        } else {
            final Boolean last = this.lastSort.get(cell);

            final boolean next = !last.booleanValue();

            this.sortTable(cell, next);

            this.lastSortedCol = cell;
            this.lastSortedDirection = next;

            this.lastSort.set(cell, new Boolean(next));
        }

    }

    /**
     * Sort the table by specified compare function.
     * 
     * @param col
     *            Column which should be sorted
     * @param descending
     *            Sorting direction
     */
    protected void sortTable(final int col, final boolean descending) {
        // Sort at least 2 rows
        if ((this.rows.size() <= 1) || (col < 0)) {
            this.table.setVisible(true);
            return;
        }

        final TableRow[] data = new TableRow[this.rows.size()];

        // Copy data
        for (int x = 0; x < this.rows.size(); x++) {
            data[x] = this.rows.get(x);
        }

        // Sort data (Bubblesort)
        for (int y = 1; y < data.length; y++) {
            for (int x = 0; x < data.length - y; x++) {
                final TableField field1 = (TableField) data[x].getField(col);
                final TableField field2 =
                        (TableField) data[x + 1].getField(col);

                final boolean swap = this.fieldCompare(col, field1, field2);

                if ((!descending && swap) || (descending && !swap)) {

                    final TableRow temp = data[x];
                    data[x] = data[x + 1];
                    data[x + 1] = temp;
                }
            }
        }

        this.reset();

        // Rebuild table
        for (int x = 0; x < data.length; x++) {
            this.addRow(data[x]);
        }

        this.table.setVisible(true);
    }

    /**
     * Remove all rows from Table, but keep Data vector.
     */
    public void clearDisplay() {
        for (int i = this.table.getRowCount(); i >= 1; i--) {
            this.table.getRowFormatter().setVisible(i, true);
            this.table.getRowFormatter().setStyleName(1, "");
            this.table.removeRow(i);
        }
    }

    /**
     * Reset the whole list, including data vector.
     */
    public void reset() {
        this.lastRow = 1;
        this.selectedRow = 1;

        this.clearDisplay();

        this.rows.clear();
        this.index.clear();
        this.map.clear();

        if (this.lastSortedCol > -1) {
            this.table.setVisible(false);
        }
    }

    /**
     * Sort and show Table.
     */
    public void flush() {
        try {
            this.sortTable(this.lastSortedCol, this.lastSortedDirection);
        } catch (final RuntimeException e) {
            this.table.setVisible(true);
        }
    }

}
