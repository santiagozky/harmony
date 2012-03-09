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

import client.classes.Router;
import client.classes.RouterList;
import client.helper.FieldCompare;
import client.helper.RudListener;
import client.template.dialog.InfoDialog;
import client.types.TableField;
import client.types.TableRow;

import com.google.gwt.user.client.ui.CheckBox;

/**
 * a list that shows router-information.
 * 
 * @author claus
 */
public abstract class RouterListPanel extends ListPanel {

    /**
     * the default constructor.
     */
    public RouterListPanel() {
        this.setTitleRow(new String[] { "Loopback Address",
                "Configuration Address", "Type", "Valid to", "Choose?" });
        this.setPopupListener(0);

        this.addDummyRow("Please click refresh button to load data...");
    }

    /**
     * adds a dummy-string to the table.
     * 
     * @param content
     *            the string to be added
     */
    private void addDummyRow(final String content) {
        final TableRow row = new TableRow();

        final TableField field = new TableField(content);
        field.setColSpan(4);
        row.addField(field);

        this.addRow(row);
    }

    /**
     * @param result
     *            the data to be filled in the table
     */
    @Override
    public final void onData(final Object result) {

        // enable buttons again, this should be somewhere else TODO

        final RouterList router = (RouterList) result;
        RudListener.getInstance().getChecked().clear();

        this.reset();

        for (int i = 0; i < router.getRouter().size(); ++i) {

            final TableRow row = new TableRow();
            final Router r = ((Router) router.getRouter().get(i));

            // TODO: more generic
            this.fillTable(row, r);

            final CheckBox box = new CheckBox();
            // Router can be identified by their LoopBackAddress
            box.setName(r.getLoopBackAddress());
            box.addClickListener(RudListener.getInstance());
            row.addField(box);

            if (null != r.getPopup()) {
                row.setDialog(new InfoDialog("Info", r.getPopup()));
            }

            this.addRow(row);

        }

        if (0 == router.getRouter().size()) {
            this.addDummyRow("Nothing found...");
        }
        this.flush();

        // notify listeners (i.e. button and gif from ReservationAdmin)
    }

    /**
     * fills a specific row with information from router.
     * 
     * @param row
     *            the row to be filled
     * @param r
     *            the router to be added to the table
     */
    private void fillTable(final TableRow row, final Router r) {
        if (r.getLoopBackAddress() != null) {
            row.addField(r.getLoopBackAddress());
        } else {
            row.addField("");
        }
        if (r.getConfigurationAddress() != null) {
            row.addField(r.getConfigurationAddress());
        } else {
            row.addField("");
        }
        if (r.getType() != null) {
            row.addField(r.getType());
        } else {
            row.addField("");
        }

        if (r.getValidTo() != null) {
            row.addField(r.getValidTo().toString());
        } else {
            row.addField("");
        }
    }

    /**
     * compare two fields from table for sorting.
     * 
     * @param col
     *            the column
     * @param field1
     *            the first field
     * @param field2
     *            the second field
     * @return true or false
     */
    @Override
    protected final boolean fieldCompare(final int col,
            final TableField field1, final TableField field2) {
        switch (col) {
        case 0:
            return FieldCompare.compareString(field1, field2);
        case 1:
            return FieldCompare.compareString(field1, field2);
        case 2:
            return FieldCompare.compareDate(field1, field2);
        default:
            return false;
        }
    }

}
