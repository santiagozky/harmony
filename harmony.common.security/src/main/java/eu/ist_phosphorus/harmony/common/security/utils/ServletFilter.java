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


package eu.ist_phosphorus.harmony.common.security.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.TimeZone;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import eu.ist_phosphorus.harmony.common.security.utils.helper.MuseHelper;
import eu.ist_phosphorus.harmony.common.security.utils.wrapper.RequestWrapper;
import eu.ist_phosphorus.harmony.common.security.utils.wrapper.ResponseWrapper;
import eu.ist_phosphorus.harmony.common.utils.PhLogger;

/**
 * 
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 * 
 */
public class ServletFilter implements Filter {

    public static Document originalRequest;

    // Backup old timezone
    private TimeZone timeZone = null;

    /** * */
    private static final Logger LOGGER = PhLogger.getSeparateLogger("aai");

    /**
     * 
     * @see javax.servlet.Filter#destroy()
     */
    public void destroy() {
        if (null != this.timeZone) {
            TimeZone.setDefault(this.timeZone);
        }
    }

    public static final Logger getLogger() {
        return ServletFilter.LOGGER;
    }

    /**
     * 
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
     *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    public void doFilter(final ServletRequest request,
            final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {

        try {
            final ResponseWrapper responseWrapper =
                    new ResponseWrapper(response);

            final RequestWrapper requestWrapper = new RequestWrapper(request);

            chain.doFilter(requestWrapper, responseWrapper);

            responseWrapper.getOutputStream().close();
            requestWrapper.getInputStream().close();
        } catch (Exception e) {
            LOGGER.error("Error while processing request in Servlet Filter: "
                    + e.getMessage(), e);

            final String responseString = MuseHelper.handleError(e);

            // Write response to output stream
            final PrintWriter errorWriter =
                    new PrintWriter(response.getOutputStream());

            LOGGER.debug("Sending SoapFault:\n" + responseString);

            errorWriter.write(responseString);
            errorWriter.flush();
            errorWriter.close();
        }
    }

    /**
     * 
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    public void init(final FilterConfig config) throws ServletException {
        this.timeZone = TimeZone.getDefault();

        TimeZone zone = TimeZone.getTimeZone("GMT");
        TimeZone.setDefault(zone);
    }
}