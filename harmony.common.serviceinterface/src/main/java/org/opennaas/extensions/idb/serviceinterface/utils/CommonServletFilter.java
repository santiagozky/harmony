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

package org.opennaas.extensions.idb.serviceinterface.utils;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.opennaas.core.utils.Config;
import org.opennaas.core.utils.PhLogger;

/**
 * Dummy Servlet Filter Class.
 * 
 * This class forwards every request to SecurityFilter if its available. So,
 * this filter should be invoked in every module, that the SecurityFilter will
 * be activated automatically.
 * 
 * @author gassen
 */
public class CommonServletFilter implements Filter {

    private Filter servletFiler = null;

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.Filter#destroy()
     */
    @Override
    public void destroy() {
        if (null != this.servletFiler) {
            this.servletFiler.destroy();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
     * javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(final ServletRequest arg0, final ServletResponse arg1,
            final FilterChain arg2) throws IOException, ServletException {

        if (null != this.servletFiler) {
            this.servletFiler.doFilter(arg0, arg1, arg2);
        } else {
            arg2.doFilter(arg0, arg1);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(final FilterConfig arg0) throws ServletException {
        try {
            final Class<?> filterClass = Class.forName(Config.getString(
                    "databinding", "secure.filter"));

            this.servletFiler = (Filter) filterClass.newInstance();
            this.servletFiler.init(arg0);

            PhLogger.getLogger().info("Security Filter successfully loaded");
        } catch (final ClassNotFoundException e) {
            PhLogger.getLogger().info("No Security Filter available");
        } catch (final Exception e) {
            PhLogger.getLogger().error(
                    "Could not create Servlet Filter: " + e.getMessage(), e);
        }
    }
}
