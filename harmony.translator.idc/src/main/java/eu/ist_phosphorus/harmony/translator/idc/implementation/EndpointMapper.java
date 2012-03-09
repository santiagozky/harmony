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


package eu.ist_phosphorus.harmony.translator.idc.implementation;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import eu.ist_phosphorus.harmony.common.utils.Config;
import eu.ist_phosphorus.harmony.common.utils.PhLogger;
import eu.ist_phosphorus.harmony.translator.idc.implementation.interfaces.IEndpointMapper;

public class EndpointMapper implements IEndpointMapper {

    private final Map<String, String> idc2hsi;
    private final Map<String, String> hsi2idc;
    private final Map<String, String> vlans;

    private final Logger logger;

    /**
     *
     */
    public EndpointMapper() {
        super();
        this.hsi2idc = new HashMap<String, String>();
        this.idc2hsi = new HashMap<String, String>();
        this.vlans = new HashMap<String, String>();

        this.logger = PhLogger.getLogger("EndpointMapper");

        try {
            final Properties prop = Config.getProperties("endpointMap");

            for (final Entry<Object, Object> foo : prop.entrySet()) {
                final String key = (String) foo.getKey();
                final String value = ((String) foo.getValue()).trim();

                final int sub = key.indexOf(".vlan");
                if (sub > 0) {
                    final String hsiKey = key.substring(0, sub);
                    final String idcKey = this.hsi2idc.get(hsiKey);
                    this.vlans.put(hsiKey, value);
                    this.vlans.put(idcKey, value);
                } else {
                    this.hsi2idc.put(key, value);
                    this.idc2hsi.put(value, key);
                    final String[] temp = value.split(" ");
                    this.hsi2idc.put(key, temp[0]);
                    this.idc2hsi.put(temp[0], key);
                }
            }
        } catch (final IOException e) {
            this.logger.error("Properties not found!");
        }
    }

    /*
     * (non-Javadoc)
     * @seeeu.ist_phosphorus.harmony.translator.idc.implementation.interfaces.
     * IEndpointMapper#harmonyToIdc(java.lang.String)
     */
    public String harmonyToIdc(final String id) {
        return this.harmonyToIdc(id, false);
    }

    /*
     * (non-Javadoc)
     * @seeeu.ist_phosphorus.harmony.translator.idc.implementation.interfaces.
     * IEndpointMapper#harmonyToIdc(java.lang.String)
     */
    public String harmonyToIdc(final String id, final boolean force) {
        this.logger.info("Getting: " + id);
        final String result = this.hsi2idc.get(id);

        if (null != result) {
            this.logger.info("Convertig Endpoint: HSI (" + id + ") -> IDC ("
                    + result + ")");

            return result;
        } else if (force) {
            throw new RuntimeException("Can not map id" + id);
        }

        return id;
    }

    /*
     * (non-Javadoc)
     * @seeeu.ist_phosphorus.harmony.translator.idc.implementation.interfaces.
     * IEndpointMapper#idcToHarmony(java.lang.String)
     */
    public String idcToHarmony(final String id) {
        return this.idcToHarmony(id, false);
    }

    /*
     * (non-Javadoc)
     * @seeeu.ist_phosphorus.harmony.translator.idc.implementation.interfaces.
     * IEndpointMapper#idcToHarmony(java.lang.String)
     */
    public String idcToHarmony(final String id, final boolean force) {
        this.logger.info("Getting: " + id);
        final String result = this.idc2hsi.get(id);

        if (null != result) {
            this.logger.info("Convertig Endpoint: IDC (" + id + ") -> HSI ("
                    + result + ")");

            return result;
        } else if (force) {
            throw new RuntimeException("Can not map id" + id);
        }

        return id;
    }

    public String getVlan(final String harmonySource) {
        return this.vlans.get(harmonySource);
    }
}
