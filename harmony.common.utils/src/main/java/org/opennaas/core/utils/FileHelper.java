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


/**
 * 
 */
package org.opennaas.core.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Harmony System: File utilites.
 * 
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 * 
 */
public class FileHelper {

    /**
     * Checks if a file exists.
     * 
     * @param filename
     *            The filename.
     * @return True if file exists.
     */
    public static boolean fileExists(final String filename) {
        final File file = new File(filename);
        return file.exists();
    }

    /**
     * Read the content of a file into a String.
     * 
     * @param filename
     *            The Filename
     * @return The content of the file.
     * @throws IOException
     *             If the file cannot be read.
     */
    public static String readFile(final String filename) throws IOException {
        final File file = new File(filename);
        final StringBuffer contents = new StringBuffer();
        BufferedReader reader = null;

        reader = new BufferedReader(new FileReader(file));
        String text = null;

        while ((text = reader.readLine()) != null) {
            contents.append(text).append(System.getProperty("line.separator"));
        }
        return contents.toString();
    }

    /**
     * 
     * @param in
     * @return
     * @throws IOException
     */
    public static String streamToString(final InputStream in)
            throws IOException {
        String string;
        final StringBuilder outputBuilder = new StringBuilder();
        if (in != null) {
          final BufferedReader reader =
              new BufferedReader(new InputStreamReader(in));
          while (null != (string = reader.readLine())) {
            outputBuilder.append(string).append('\n');
          }
        }
        return outputBuilder.toString().trim();
    }
}
