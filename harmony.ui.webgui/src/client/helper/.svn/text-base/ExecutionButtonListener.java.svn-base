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

import java.util.Vector;

import client.template.panel.ListPanel;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Event Listener for execution Button.
 * 
 * @author gassen
 */
public class ExecutionButtonListener implements ClickListener {
    /** List Panel. */
    private ListPanel list = null;
    /** Callback Handler. */
    private ExecutionCallbackHandler callback = null;
    /** Execution type Dropdown. */
    private ListBox listBox = null;
    /** Execution Button. */
    private Button button = null;
    /** Activity Image. */
    private Image activity = null;

    /**
     * Get the Execution Type dropdown.
     * 
     * @return Execution Type Dropdown
     */
    public ListBox getListBox() {
        return this.listBox;
    }

    /**
     * Set the Execution Type dropdown.
     * 
     * @param listBox
     *            Execution Type Dropdown
     */
    public void setListBox(final ListBox listBox) {
        this.listBox = listBox;
    }

    /**
     * Get the callback Class.
     * 
     * @return Callback Class.
     */
    public AsyncCallback getCallback() {
        return this.callback;
    }

    /**
     * Set the callback class.
     * 
     * @param callback
     *            Callback Class.
     */
    public void setCallback(final ExecutionCallbackHandler callback) {
        this.callback = callback;
    }

    /**
     * Handle onclick event. Call execute methon in list class.
     * 
     * @param sender
     *            Event sender
     */
    public void onClick(final Widget sender) {
        this.button.setEnabled(false);
        this.activity.setVisible(true);

        final Vector<java.lang.String> checked =
                RudListener.getInstance().getChecked();

        final int index = this.listBox.getSelectedIndex();

        final String type = this.listBox.getItemText(index);

        this.list.execute(type, checked, this.callback);
    }

    /**
     * Get the list Panel.
     * 
     * @return ListPanel
     */
    public ListPanel getList() {
        return this.list;
    }

    /**
     * Set the list Panel.
     * 
     * @param list
     *            ListPanel
     */
    public void setList(final ListPanel list) {
        this.list = list;
    }

    /**
     * Get Execution Button.
     * 
     * @return Execution Button
     */
    public Button getButton() {
        return this.button;
    }

    /**
     * Set Execution Button.
     * 
     * @param button
     *            Execution Button
     */
    public void setButton(final Button button) {
        this.button = button;
    }

    /**
     * Get the activity Image.
     * 
     * @return activity Image
     */
    public Image getActivity() {
        return this.activity;
    }

    /**
     * Set the activity Image.
     * 
     * @param activity
     *            Activity Image
     */
    public void setActivity(final Image activity) {
        this.activity = activity;
    }

}
