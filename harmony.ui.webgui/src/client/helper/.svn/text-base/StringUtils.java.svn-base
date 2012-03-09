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

/**
 * @author gassen
 * 
 */
public class StringUtils {

    /**
     * Replace everything but a-zA-Z by html escapes
     * 
     * @param src
     *            Source String
     * @return Escaped String
     */
    public static String escape(final String src) {
        String result = "";

        final char[] chars = src.toCharArray();

        for (int x = 0; x < chars.length; x++) {
            if (((65 <= chars[x]) && (chars[x] <= 90))
                    || ((97 <= chars[x]) && (chars[x] <= 122))) {
                result += chars[x];
            } else {
                result += "&#" + (byte) (chars[x] & 0xFF) + ";";
            }

        }

        return result;
    }
}
