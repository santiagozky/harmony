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

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * Class to represent a table field.
 * 
 * @author gassen
 */
public class TableField {
    /** Type. */
    private final int type;
    /** String. */
    public static final int STRING = 0;
    /** Widget. */
    public static final int WIDGET = 1;
    /** HTML. */
    public static final int HTML = 2;

    /** ColSpan. */
    private int colSpan = 1;

    /** Data. */
    private final Object dataObject;

    /**
     * Constructor.
     * 
     * @param text
     *            Data
     */
    public TableField(final String text) {
        this.type = TableField.STRING;
        this.dataObject = text;
    }

    /**
     * Constructor.
     * 
     * @param widget
     *            Data
     */
    public TableField(final Widget widget) {
        this.type = TableField.WIDGET;
        this.dataObject = widget;
    }

    /**
     * Constructor.
     * 
     * @param html
     *            Data
     */
    public TableField(final HTML html) {
        this.type = TableField.HTML;
        this.dataObject = html;
    }

    /**
     * Get Type.
     * 
     * @return Type of data Object
     */
    public final int getType() {
        return this.type;
    }

    /**
     * Get data object.
     * 
     * @return Data object
     */
    public final Object getDisplayObject() {
        return this.dataObject;
    }

    /**
     * Get the cells colspan.
     * 
     * @return colspan
     */
    public int getColSpan() {
        return this.colSpan;
    }

    /**
     * Get the cells colspan.
     * 
     * @return colspan
     */
    public void setColSpan(final int colSpan) {
        this.colSpan = colSpan;
    }
}
