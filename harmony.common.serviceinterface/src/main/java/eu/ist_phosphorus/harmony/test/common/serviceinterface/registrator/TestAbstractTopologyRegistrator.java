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
package eu.ist_phosphorus.harmony.test.common.serviceinterface.registrator;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EndpointType;
import eu.ist_phosphorus.harmony.common.serviceinterface.topology.registrator.AbstractTopologyRegistrator;
import eu.ist_phosphorus.harmony.common.utils.Config;

/**
 * @author gassen
 * 
 */
public class TestAbstractTopologyRegistrator extends
        AbstractTopologyRegistrator {

    /**
     * 
     */
    public TestAbstractTopologyRegistrator() {
        // TODO Auto-generated constructor stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * eu.ist_phosphorus.harmony.common.serviceinterface.topology.registrator
     * .AbstractTopologyRegistrator#getEndpoints()
     */
    @Override
    protected List<EndpointType> getEndpoints() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * eu.ist_phosphorus.harmony.common.serviceinterface.topology.registrator
     * .AbstractTopologyRegistrator#getInterdomainPropertyFile()
     */
    @Override
    protected String getInterdomainPropertyFile() {
        return "test";
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * eu.ist_phosphorus.harmony.common.serviceinterface.topology.registrator
     * .AbstractTopologyRegistrator#shutdown()
     */
    @Override
    protected void shutdown() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * eu.ist_phosphorus.harmony.common.serviceinterface.topology.registrator
     * .AbstractTopologyRegistrator#startup()
     */
    @Override
    protected boolean startup() {
        return true;
    }

    /**
     * Test if registrator is acive.
     */
    // @Test
    public final void testContext() {
        /*
         * this.contextInitialized(new ServletContextEvent(new ServletContext()
         * {
         * 
         * public Object getAttribute(String arg0) { // TODO Auto-generated
         * method stub return null; }
         * 
         * public Enumeration getAttributeNames() { // TODO Auto-generated
         * method stub return null; }
         * 
         * public ServletContext getContext(String arg0) { // TODO
         * Auto-generated method stub return null; }
         * 
         * public String getContextPath() { // TODO Auto-generated method stub
         * return null; }
         * 
         * public String getInitParameter(String arg0) { // TODO Auto-generated
         * method stub return null; }
         * 
         * public Enumeration getInitParameterNames() { // TODO Auto-generated
         * method stub return null; }
         * 
         * public int getMajorVersion() { // TODO Auto-generated method stub
         * return 0; }
         * 
         * public String getMimeType(String arg0) { // TODO Auto-generated
         * method stub return null; }
         * 
         * public int getMinorVersion() { // TODO Auto-generated method stub
         * return 0; }
         * 
         * public RequestDispatcher getNamedDispatcher(String arg0) { // TODO
         * Auto-generated method stub return null; }
         * 
         * public String getRealPath(String arg0) { // TODO Auto-generated
         * method stub return null; }
         * 
         * public RequestDispatcher getRequestDispatcher(String arg0) { // TODO
         * Auto-generated method stub return null; }
         * 
         * public URL getResource(String arg0) throws MalformedURLException { //
         * TODO Auto-generated method stub return null; }
         * 
         * public InputStream getResourceAsStream(String arg0) { // TODO
         * Auto-generated method stub return null; }
         * 
         * public Set getResourcePaths(String arg0) { // TODO Auto-generated
         * method stub return null; }
         * 
         * public String getServerInfo() { // TODO Auto-generated method stub
         * return null; }
         * 
         * public Servlet getServlet(String arg0) throws ServletException { //
         * TODO Auto-generated method stub return null; }
         * 
         * public String getServletContextName() { // TODO Auto-generated method
         * stub return null; }
         * 
         * public Enumeration getServletNames() { // TODO Auto-generated method
         * stub return null; }
         * 
         * public Enumeration getServlets() { // TODO Auto-generated method stub
         * return null; }
         * 
         * public void log(String arg0) { // TODO Auto-generated method stub
         * 
         * }
         * 
         * public void log(Exception arg0, String arg1) { // TODO Auto-generated
         * method stub
         * 
         * }
         * 
         * public void log(String arg0, Throwable arg1) { // TODO Auto-generated
         * method stub
         * 
         * }
         * 
         * public void removeAttribute(String arg0) { // TODO Auto-generated
         * method stub
         * 
         * }
         * 
         * public void setAttribute(String arg0, Object arg1) { // TODO
         * Auto-generated method stub
         * 
         * }
         * 
         * }));
         * 
         * Assert.assertTrue(this.active);
         * 
         * Assert.assertNotNull(this.superDomainDistributor);
         * 
         * Assert.assertTrue(this.updateDomain());
         */
    }

    /**
     * Check if all domains are available.
     */
    @Test
    public final void testInitialisation() {
        final int prefixes = Config.getInt("test", "domain.numTNAPrefixes");

        for (int x = 0; x < prefixes; x++) {
            Assert.assertEquals(this.getTNAPrefixList().get(0), Config
                    .getString("test", "domain.TNAPrefix" + x));
        }
    }

}
