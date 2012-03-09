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


package client.types;

import java.util.Vector;

import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * Generic class to represent a Table row.
 * 
 * @author gassen
 */
public class TableRow {
    /** Table fields. */
    private final Vector<TableField> fields;
    /** Dialog pupup. */
    private DialogBox dialog = null;
    /** Details. */
    private final Vector<TableRow> detailRows;
    /** Is Detail Row. */
    private boolean isDetail = false;
    /** Row Number. */
    private int row;
    /** Syle name. */
    private String syle = null;

    /**
     * Boring Constructor.
     */
    public TableRow() {
        this.fields = new Vector<TableField>();
        this.detailRows = new Vector<TableRow>();
    }

    /**
     * Get the row number.
     * 
     * @return Row Number
     */
    public int getRow() {
        return this.row;
    }

    /**
     * Set the row number.
     * 
     * @param row
     *            number
     */
    public void setRow(final int row) {
        this.row = row;
    }

    /**
     * Return fields vector.
     * 
     * @return Fields
     */
    public final Vector<TableField> getFields() {
        return this.fields;
    }

    /**
     * Add a new field.
     * 
     * @param field
     *            New Field
     */
    public final void addField(final String field) {
        this.fields.add(new TableField(field));
    }

    /**
     * Add a new field.
     * 
     * @param field
     *            New Field
     */
    public final void addField(final Widget field) {
        this.fields.add(new TableField(field));
    }

    /**
     * Add a new field.
     * 
     * @param field
     *            New Field
     */
    public final void addField(final HTML field) {
        this.fields.add(new TableField(field));
    }

    /**
     * Add a new field.
     * 
     * @param field
     *            New Field
     */
    public final void addField(final TableField field) {
        this.fields.add(field);
    }

    /**
     * Get field.
     * 
     * @param index
     * @return Field object
     */
    public final Object getField(final int index) {
        return this.fields.get(index);
    }

    /**
     * Get the info dialog.
     * 
     * @return InfoDialog
     */
    public final DialogBox getDialog() {
        return this.dialog;
    }

    /**
     * Set the info dialog.
     * 
     * @param dialog
     *            InfoDialog
     */
    public final void setDialog(final DialogBox dialog) {
        this.dialog = dialog;
    }

    /**
     * set the value of a specified field
     * 
     * @param col
     *            Column
     * @param field
     *            Field value
     */
    public final void setField(final int col, final TableField field) {
        this.fields.set(col, field);
    }

    /**
     * Check if row has details.
     * 
     * @return
     */
    public boolean hasDetails() {
        return (!this.detailRows.isEmpty());
    }

    /**
     * Get Detail Rows.
     * 
     * @return Detail Rows Vector.
     */
    public Vector<TableRow> getDetails() {
        return this.detailRows;
    }

    /**
     * Add a detail Row.
     * 
     * @param detail
     *            Detail Row.
     */
    public void addDetail(final TableRow detail) {
        detail.setDetailRow(true);

        this.detailRows.add(detail);
    }

    /**
     * Set row status.
     * 
     * @param status
     *            True if Row is a detail row.
     */
    public void setDetailRow(final boolean status) {
        this.isDetail = status;
    }

    /**
     * Get row status.
     * 
     * @return True if row is a detail row.
     */
    public boolean isDetailRow() {
        return this.isDetail;
    }

    /**
     * Get Row Style
     * 
     * @return Style name
     */
    public String getSyle() {
        return this.syle;
    }

    /**
     * Set Row Style
     * 
     * @param syle
     *            Style name
     */
    public void setSyle(final String syle) {
        this.syle = syle;
    }
}
