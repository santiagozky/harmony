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


package client.helper;

import client.template.panel.ListPanel;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class RefreshButtonListener implements ClickListener {

    private Button button = null;

    private Image activity = null;

    private ListPanel list = null;

    private AsyncCallback callback = null;

    private ListBox listBox = null;

    public void onClick(final Widget sender) {
        this.button.setEnabled(false);
        this.activity.setVisible(true);

        int value = 1;

        if (null != this.listBox) {
            final int index = this.listBox.getSelectedIndex();

            value = Integer.parseInt(this.listBox.getValue(index));
        }

        this.list.refresh(value, this.callback);
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

    public ListPanel getList() {
        return this.list;
    }

    public void setList(final ListPanel list) {
        this.list = list;
    }

    public AsyncCallback getCallback() {
        return this.callback;
    }

    public void setCallback(final AsyncCallback callback) {
        this.callback = callback;
    }

    public ListBox getListBox() {
        return this.listBox;
    }

    public void setListBox(final ListBox listBox) {
        this.listBox = listBox;
    }

}
