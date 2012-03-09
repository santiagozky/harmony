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

import java.util.Date;
import java.util.Vector;

import client.classes.Reservation;
import client.classes.ReservationList;
import client.classes.Service;
import client.helper.FieldCompare;
import client.helper.RudListener;
import client.types.TableField;
import client.types.TableRow;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;

public abstract class ReservationListPanel extends ListPanel {
    // private final Date now = new Date();

    public ReservationListPanel() {
        this.setTitleRow(new String[] { "Id", "StartTime", "Duration (sec)",
                "Choose?" });
        this.setPopupListener(0);
        this.setDetailListener(new int[] { 1, 2 });
        this.setDefaultSorting(1, false);

        this.addDummyRow("Please click refresh button to load data...");
    }

    private final void addDummyRow(final String content) {
        final TableRow row = new TableRow();

        final TableField field = new TableField(content);
        field.setColSpan(4);
        row.addField(field);

        this.addRow(row);
    }

    public boolean isActive(final Service service, final Date start,
            final int duration) {
        return false;
    }

    public abstract DialogBox getPopUp(Reservation reservation);

    /**
     *
     */
    @Override
    public final void onData(final Object result) {

        // enable buttons again, this should be somewhere else TODO

        final ReservationList reservations = (ReservationList) result;
        RudListener.getInstance().getChecked().clear();

        this.reset();

        for (int i = 0; i < reservations.getReservations().size(); ++i) {

            final TableRow row = new TableRow();
            final Reservation res =
                    ((Reservation) reservations.getReservations().get(i));

            row.addField("" + res.getId());
            row.addField("-");
            row.addField("-");

            final CheckBox box = new CheckBox();
            box.setName("" + res.getId());
            box.addClickListener(RudListener.getInstance());
            row.addField(box);

            row.setDialog(this.getPopUp(res));

            for (int j = 0; j < res.getServices().size(); j++) {
                final Service service = (Service) res.getServices().get(j);

                // Take details from first service...
                if (0 == j) {
                    final Vector<TableField> rowData = row.getFields();
                    rowData.set(1, new TableField(service.getStartTime()
                            .toString()));
                    rowData.set(2, new TableField(service.getDuration() + ""));
                    if (this.isActive(service, service.getStartTime(), service
                            .getDuration())) {
                        row.setSyle("list-Now");
                    }
                }

                final TableRow detailRow = new TableRow();
                detailRow.addField(service.getId() + "");
                detailRow.addField(service.getStartTime().toString());
                detailRow.addField(service.getDuration() + "");
                detailRow.addField(" ");
                detailRow.setSyle("list-Detail");

                row.addDetail(detailRow);
            }

            this.addRow(row);
        }

        if (0 == reservations.getReservations().size()) {
            this.addDummyRow("Nothing found...");
        }

        this.flush();

        // notify listeners (i.e. button and gif from ReservationAdmin)
    }

    /**
     * Compare two fields for sorting.
     */
    @Override
    protected boolean fieldCompare(final int col, final TableField field1,
            final TableField field2) {
        switch (col) {
        // ID
        case 0:
            return FieldCompare.compareId(field1, field2);
            // StartTime
        case 1:
            return FieldCompare.compareDate(field1, field2);
            // Duration
        case 2:
            return FieldCompare.compareInt(field1, field2);
        default:
            return false;
        }
    }

}
