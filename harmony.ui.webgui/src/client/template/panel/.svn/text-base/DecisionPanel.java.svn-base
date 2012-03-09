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

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;

public abstract class DecisionPanel extends HorizontalPanel {
    protected Button button;
    protected Image activity;
    protected AsyncCallback callback;
    protected ListPanel list;
    protected ListBox dropdown;

    public ListBox getDropdown() {
        return this.dropdown;
    }

    public void setDropdown(final ListBox dropdown) {
        this.dropdown = dropdown;
    }

    public AsyncCallback getCallback() {
        return this.callback;
    }

    public void setCallback(final AsyncCallback callback) {
        this.callback = callback;
    }

    public ListPanel getList() {
        return this.list;
    }

    public void setList(final ListPanel list) {
        this.list = list;
    }

    public Button getButton() {
        return this.button;
    }

    public void setButton(final Button button) {
        this.button = button;
    }

    public Image getActivity() {
        return this.activity;
    }

    public void setActivity(final Image activity) {
        this.activity = activity;
    }

    public void lock() {
        this.activity.setVisible(true);
        this.button.setEnabled(false);
    }

    public void release() {
        this.activity.setVisible(false);
        this.button.setEnabled(true);
    }
}
