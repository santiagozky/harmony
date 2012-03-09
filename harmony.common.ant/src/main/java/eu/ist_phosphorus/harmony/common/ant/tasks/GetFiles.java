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
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Copy;
import org.apache.tools.ant.taskdefs.Get;

import eu.ist_phosphorus.harmony.common.ant.utils.BuildConstants;
import eu.ist_phosphorus.harmony.common.ant.utils.ClasspathHelper;

/**
 * @author gassen
 */
public class GetFiles extends Task {

    private String proxy = "";
    private String libs = "./";
    private String cc = null;
    private final HashSet<String> cpClasses = new HashSet<String>();
    private final HashSet<String> libClasses = new HashSet<String>();

    /**
     * @param fileName
     * @throws MalformedURLException
     */
    private final void getFile(final String fileName)
            throws MalformedURLException {
        final File file = new File(this.libs + "/" + fileName);

        if (null != fileName) {
            final Get get = new Get();
            get.setUseTimestamp(true);
            get.setSrc(new URL(this.proxy + fileName));
            get.setDest(file);
            get.execute();
        }

        if (null != this.cc) {
            final Copy copy = new Copy();
            copy.setFile(file);
            copy.setTodir(new File(this.cc));
        }
    }

    /**
     * @param proxy
     */
    public final void setProxy(final String proxy) {
        if (null != proxy) {
            this.proxy = proxy;

            if (!this.proxy.endsWith("/")) {
                this.proxy += "/";
            }
        }
    }

    /**
     * @param val
     */
    public final void setCC(final String val) {
        this.cc = val;
    }

    /**
     * @param val
     */
    public final void setLibs(final String val) {
        if (null != val) {
            this.libs = val;
        }

        if (!this.libs.endsWith("/")) {
            this.libs += "/";
        }
    }

    /**
     * @throws IOException
     */
    private final void getFilesFromDeps() throws IOException {
        if (null != this.libs) {
            File dir = new File(this.libs);

            String[] children = dir.list();

            for (String child : children) {
                if (null != child && child.matches(".*\\.jar")) {

                    JarFile jarFile = new JarFile(this.libs + child);

                    JarEntry entry = jarFile.getJarEntry(BuildConstants.LOOKUP);

                    if (null != entry) {
                        final InputStream stream =
                                jarFile.getInputStream(entry);

                        final BufferedReader br =
                                new BufferedReader(
                                        new InputStreamReader(stream));
                        String strLine;

                        while ((strLine = br.readLine()) != null) {
                            if (!this.cpClasses.contains(strLine)) {
                                System.out
                                        .println("Warning: Your .classpath does not contain "
                                                + strLine
                                                + " which is needed by "
                                                + child);
                                
                                this.libClasses.add(strLine);
                            }
                        }

                        stream.close();
                    }
                }
            }
        }
    }

    /**
     * @throws MalformedURLException
     * @throws IOException
     */
    private final void getFilesFromClasspath() throws MalformedURLException,
            IOException {
        final FileInputStream fstream = new FileInputStream(".classpath");

        final DataInputStream in = new DataInputStream(fstream);
        final BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine;

        while ((strLine = br.readLine()) != null) {
            this.cpClasses.add(ClasspathHelper.getFileName(strLine));
        }

        in.close();
    }

    /**
     * @throws MalformedURLException
     */
    private final void downloadCpFiles() throws MalformedURLException {
        for (String string : this.cpClasses) {
            this.getFile(string);
        }
    }
    
    /**
     * @throws MalformedURLException
     */
    private final void downloadLibFiles() throws MalformedURLException {
        for (String string : this.libClasses) {
            this.getFile(string);
        }
    }

    /*
     * (non-Javadoc)
     * @see org.apache.tools.ant.Task#execute()
     */
    @Override
    public void execute() throws BuildException {
        try {
            System.out.println("Reading Eclipse Classpath...");
            
            // Download libs from cp first
            this.getFilesFromClasspath();
            
            this.downloadCpFiles();
            
            System.out.println("Checking jar dependencies...");
            
            // Check for new dependencies in libs
            this.getFilesFromDeps();

            this.downloadLibFiles();
        } catch (final Exception e) {
            throw new BuildException(e, this.getLocation());
        }
    }

}
