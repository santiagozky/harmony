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

import client.classes.Endpoint;
import client.classes.EndpointList;
import client.helper.FieldCompare;
import client.helper.RudListener;
import client.template.dialog.InfoDialog;
import client.types.TableField;
import client.types.TableRow;

import com.google.gwt.user.client.ui.CheckBox;

/**
 * TODO: extract code that refers to the Endpont type.
 * 
 * @author claus
 */
public abstract class LinkListPanel extends ListPanel {

    /**
     * default constructor.
     */
    public LinkListPanel() {
        this.setTitleRow(new String[] { "Label", "SNode", "OIF", "DNode",
                "IIF", "Choose?" });
        this.setPopupListener(0);

        this.addDummyRow("Please click refresh button to load data...");
    }

    /**
     * @param content
     *            the dummy-string to be added
     */
    protected void addDummyRow(final String content) {
        final TableRow row = new TableRow();

        final TableField field = new TableField(content);
        field.setColSpan(5);
        row.addField(field);

        this.addRow(row);
    }

    /**
     * @param result
     *            the list of links
     */
    @Override
    public void onData(final Object result) {

        // enable buttons again, this should be somewhere else TODO

        final EndpointList links = (EndpointList) result;
        RudListener.getInstance().getChecked().clear();

        this.reset();

        for (int i = 0; i < links.getLinks().size(); ++i) {

            final TableRow row = new TableRow();
            final Endpoint l = ((Endpoint) links.getLinks().get(i));

            // TODO: make more generic
            this.fillTable(row, l);

            // the checkbox to delete a link
            final CheckBox deleteBox = new CheckBox();
            // Router can be identified by their LoopBackAddress
            deleteBox.setName(l.getUniqueLabel());
            deleteBox.addClickListener(RudListener.getInstance());
            row.addField(deleteBox);

            if (null != l.getPopup()) {
                row.setDialog(new InfoDialog("Info", l.getPopup()));
            }

            this.addRow(row);

        }

        if (0 == links.getLinks().size()) {
            this.addDummyRow("Nothing found...");
        }

        this.flush();

        // notify listeners (i.e. button and gif from ReservationAdmin)
    }

    /**
     * fills a specififc row whith information from link.
     * 
     * @param row
     *            the row to be filled
     * @param l
     *            the link to to added
     */
    private void fillTable(final TableRow row, final Endpoint l) {
        if (l.getUniqueLabel() != null) {
            row.addField(l.getUniqueLabel());
        } else {
            row.addField("");
        }
        if (l.getSourceNode() != null) {
            row.addField(l.getSourceNode());
        } else {
            row.addField("");
        }
        if (l.getOutgoingInterface() != null) {
            row.addField(l.getOutgoingInterface());
        } else {
            row.addField("");
        }
        if (l.getDestinationNode() != null) {
            row.addField(l.getDestinationNode());
        } else {
            row.addField("");
        }
        if (l.getIngoingInterface() != null) {
            row.addField(l.getIngoingInterface());
        } else {
            row.addField("");
        }
    }

    /**
     * compare two fields for sorting.
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

        return FieldCompare.compareString(field1, field2);

    }

}
