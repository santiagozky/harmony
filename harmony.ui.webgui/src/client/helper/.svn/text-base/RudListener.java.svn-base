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

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

/**
 * singleton listener for a ReadUpdateDelete List of Reservations etc. It stores
 * all checked Rows; used for deleting and updating afterwards.
 * 
 * @author claus
 */
public class RudListener implements ClickListener {

    /**
     * the vector with currently checked rows.
     */
    private static Vector<java.lang.String> checked =
            new Vector<java.lang.String>();

    private static RudListener instance = new RudListener();

    protected RudListener() {
    }

    public static RudListener getInstance() {
        return RudListener.instance;
    }

    public Vector<java.lang.String> getChecked() {
        return RudListener.checked;
    }

    /**
     * action that is executed after clicking on a button that the RudListener
     * is attached to.
     * 
     * @param sender
     *            the widget that triggered the onClick
     */
    public final void onClick(final Widget sender) {
        final CheckBox box = (CheckBox) sender;
        final String name = box.getName();
        if (box.isChecked()) {
            if (!RudListener.checked.contains(name)) {
                RudListener.checked.add(name);
            }
        } else {
            RudListener.checked.remove(name);
        }
    }

}
