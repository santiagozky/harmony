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

import java.util.Date;

import client.types.TableField;

/**
 * Helper class for comparing table items.
 * 
 * @author gassen
 */
public class FieldCompare {

    /**
     * Compare two integer fields.
     * 
     * @param field1
     *            Field 1
     * @param field2
     *            Field 2
     * @return true if fields should be swapped
     */
    public static boolean compareInt(final TableField field1,
            final TableField field2) {
        return (Integer.parseInt((String) field1.getDisplayObject()) > Integer
                .parseInt((String) field2.getDisplayObject()));
    }

    /**
     * Compare two integer fields.
     * 
     * @param field1
     *            Field 1
     * @param field2
     *            Field 2
     * @return true if fields should be swapped
     */
    public static boolean compareId(final TableField field1,
            final TableField field2) {
        long id1 = 0;
        long id2 = 0;

        try {
            id1 =
                    Long.parseLong(((String) field1.getDisplayObject())
                            .split("@")[0]);
            id2 =
                    Long.parseLong(((String) field1.getDisplayObject())
                            .split("@")[0]);
        } catch (final RuntimeException e) {
            return FieldCompare.compareString(field1, field2);
        }

        return id1 > id2;
    }

    /**
     * Compare two date fields.
     * 
     * @param field1
     *            Field 1
     * @param field2
     *            Field 2
     * @return true if fields should be swapped
     */
    public static boolean compareDate(final TableField field1,
            final TableField field2) {
        long date1, date2;

        try {
            date1 = Date.parse((String) field1.getDisplayObject());
            date2 = Date.parse((String) field2.getDisplayObject());
        } catch (final Exception e) {
            return false;
        }

        return date1 > date2;
    }

    /**
     * Compare two string fields.
     * 
     * @param field1
     *            Field 1
     * @param field2
     *            Field 2
     * @return true if fields should be swapped
     */
    public static boolean compareString(final TableField field1,
            final TableField field2) {
        final String string1 = (String) field1.getDisplayObject();
        final String string2 = (String) field2.getDisplayObject();

        return (string1.compareTo(string2) > 0) ? true : false;
    }
}
