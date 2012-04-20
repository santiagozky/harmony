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


package org.opennaas.core.security;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.junit.Test;

import org.opennaas.core.security.utils.Request;
import org.opennaas.core.utils.FileHelper;
import org.opennaas.core.utils.PhLogger;

public class TestDOMRequestParsing {

    Logger log = PhLogger.getLogger();

    Request requestParser;

    @Test
    public void parseRequest() {

        String rawRequest = "";

        try {
            rawRequest = FileHelper
                    .readFile("resources/data/RawReservationRequest.xml");
        } catch (final IOException e) {
            this.log.warn("readfile error:", e);
        }

        try {
            this.requestParser = new Request(rawRequest);
        } catch (final Exception e) {
            // FIXME: printStackTrace is never the best solution
            e.printStackTrace();
        }

    }
}
