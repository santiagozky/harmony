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


package client.reservation.panel;

import client.helper.RefreshButtonListener;
import client.template.panel.DecisionPanel;
import client.template.panel.ListPanel;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;

/**
 * Class representing the Refresh Panel.
 * 
 * @author gassen
 */
public class RefreshReservationPanel extends DecisionPanel {

    /**
     * Constructor. Create the whole panel at once.
     * 
     * @param callback
     *            Callback Object which gets the Refresh parameters.
     * @param list
     *            ListPanel which should be attached to the refresh Panel.
     */
    public RefreshReservationPanel(final AsyncCallback callback,
            final ListPanel list) {
        final Label labelOne = new Label();
        labelOne.setText("Period: ");

        // TODO: user those values??
        this.dropdown = new ListBox();
        this.dropdown.addItem("Now", "0");
        this.dropdown.addItem("1 Hour", "1");
        this.dropdown.addItem("3 Hours", "3");
        this.dropdown.addItem("1 Day", "24");
        this.dropdown.addItem("3 Days", "72");
        this.dropdown.addItem("1 Week", "168");

        // activity indicator
        this.button = new Button();
        this.button.setText("refresh");
        this.activity = new Image("ajaxloader.gif");
        this.activity.setVisible(false);

        // tell refresh-Button which elements to update
        final RefreshButtonListener listener = new RefreshButtonListener();
        listener.setButton(this.button);
        listener.setActivity(this.activity);
        listener.setCallback(callback);
        listener.setList(list);
        listener.setListBox(this.dropdown);
        this.button.addClickListener(listener);

        this.add(labelOne);
        this.add(this.dropdown);
        this.add(this.button);
        this.add(this.activity);
        this.setStyleName("res-Top");
        this.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
    }

}
