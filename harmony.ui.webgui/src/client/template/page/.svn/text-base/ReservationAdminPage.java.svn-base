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


package client.template.page;

import client.reservation.panel.ExecuteReservationPanel;
import client.reservation.panel.RefreshReservationPanel;
import client.template.dialog.ErrorDialog;
import client.template.panel.DecisionPanel;
import client.template.panel.ListPanel;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;

/**
 * displays all reservations. opportunity to delete and create reservations
 * TODO: extract superclass
 * 
 * @author claus
 */
public class ReservationAdminPage extends Composite implements AsyncCallback {

    private DockPanel dockPanel = new DockPanel();
    private DecisionPanel top = null;
    private DecisionPanel bottom = null;
    private ListPanel table = null;

    public ReservationAdminPage(final ListPanel list) {

        this.table = list;

        this.top = new RefreshReservationPanel(this, list);
        this.bottom = new ExecuteReservationPanel(list);

        this.dockPanel.add(this.top, DockPanel.NORTH);

        this.dockPanel.add(this.table, DockPanel.CENTER);

        this.dockPanel.add(this.bottom, DockPanel.SOUTH);

        this.dockPanel.setWidth("100%");
        this.dockPanel.setSpacing(0);
        this.dockPanel.setStyleName("res-Dock");

        this.initWidget(this.dockPanel);
        this.setStyleName("mail-List");

    }

    public final DockPanel getDockPanel() {
        return this.dockPanel;
    }

    public final void setDockPanel(final DockPanel dockPanel) {
        this.dockPanel = dockPanel;
    }

    public final HorizontalPanel getTop() {
        return this.top;
    }

    public final void setTop(final DecisionPanel top) {
        this.top = top;
    }

    public final HorizontalPanel getBottom() {
        return this.bottom;
    }

    public final void setBottom(final DecisionPanel bottom) {
        this.bottom = bottom;
    }

    public final ListPanel getTable() {
        return this.table;
    }

    public final void setTable(final ListPanel table) {
        this.table = table;
    }

    public final void onFailure(final Throwable caught) {
        final ErrorDialog dialog = new ErrorDialog("Failure", caught);

        dialog.show();
        dialog.center();

        this.top.release();
        this.bottom.release();
    }

    public final void onSuccess(final Object result) {

        this.top.release();
        this.bottom.release();

        this.table.onData(result);
    }

}
