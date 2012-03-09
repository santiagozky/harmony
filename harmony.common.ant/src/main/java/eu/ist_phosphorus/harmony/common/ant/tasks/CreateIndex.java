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
package eu.ist_phosphorus.harmony.common.ant.tasks;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Delete;

import eu.ist_phosphorus.harmony.common.ant.utils.BuildConstants;
import eu.ist_phosphorus.harmony.common.ant.utils.ClasspathHelper;

/**
 * @author gassen
 */
public class CreateIndex extends Task {

    /**
     *
     */
    public CreateIndex() {
        final Delete del = new Delete();
        del.setFile(new File(this.getFileName()));
        del.setQuiet(true);
        del.execute();
    }

    /**
     * @return
     */
    private final String getFileName() {
        return BuildConstants.INDEX;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.tools.ant.Task#execute()
     */
    @Override
    public void execute() throws BuildException {
        System.out.println("Creating dependency Index from Eclipse Classpath...");

        try {
            final FileInputStream fstream = new FileInputStream(".classpath");

            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br =
                    new BufferedReader(new InputStreamReader(in));
            String strLine;

            final FileWriter outFW = new FileWriter(this.getFileName());
            final BufferedWriter out = new BufferedWriter(outFW);

            while ((strLine = br.readLine()) != null) {
                final String item = ClasspathHelper.getFileName(strLine);
                if (null != item) {
                    out.write(item + "\n");
                }
            }

            out.close();
            in.close();
        } catch (final Exception e) {
            e.printStackTrace();
            throw new BuildException(e, this.getLocation());
        }
    }

}
