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


package org.opennaas.core.utils;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.Resolver;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.TXTRecord;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;

/**
 * TNA lookup via DNS for names and IPs.
 * 
 * @author Jan Gassen (gassen@cs.uni-bonn.de)
 * @author Alexander Willner (willner@cs.uni-bonn.de)
 * 
 */
public class TNAHelper {
    private final static String NAMESERVER = ".ip-lookup.viola-testbed.de";
    private final static String[] domains =
            { ".viola-testbed.de", ".crc.ca.viola-testbed.de",
                    ".i2cat.net.viola-testbed.de",
                    ".surfnet.nl.viola-testbed.de" };
    private static final HashMap<String, Tuple<String, Long>> cache =
            new HashMap<String, Tuple<String, Long>>();
    static int igsd = 0;

    private static final int TIMEOUT = 1000 * 60 * 30; // Millis * Sec * Min

    /**
     * Get a TNA from Cache considering timeout.
     *
     * @param key
     * @return
     */
    private static final String getTnaFromCache(final String key) {
        Tuple<String, Long> tuple = cache.get(key);

        if (null != tuple) {
            if (System.currentTimeMillis()
                    - tuple.getSecondElement().longValue() > TIMEOUT) {
                
                cache.remove(key);
                
                return null;
            }
        } else {
            return null;
        }
        
        return tuple.getFirstElement();
    }

    /**
     * Set TNA to cache.
     * 
     * @param key
     * @param val
     */
    private static final void addTnaToCache(final String key, final String val) {
        Tuple<String, Long> tuple =
                new Tuple<String, Long>(val, System.currentTimeMillis());

        cache.put(key, tuple);
    }

    /**
     * Ask the last TXT=TNA-record from the DNS.
     * 
     * @param input
     *            A Name or IP.
     * @return The TXT record, null otherwise.
     * @throws TextParseException
     *             In a bogus input.
     * @throws UnknownHostException
     *             If the DNS is not reachable.
     */
    @SuppressWarnings("unchecked")
    private static String getLastTNARecord(final String input)
            throws TextParseException, UnknownHostException {
        String host = input;
        String responseMessage = getTnaFromCache(input);

        // If there was something valid in cache...
        if (null != responseMessage) {
            return responseMessage;
        }

        if (!TNAHelper.isName(input)) {
            host = TNAHelper.revertIp(input) + TNAHelper.NAMESERVER;
            responseMessage = input;
        }

        final Lookup lookup = new Lookup(host, Type.ANY);
        final Resolver resolver = new SimpleResolver();

        lookup.setResolver(resolver);
        lookup.setCache(null);

        final Record[] records = lookup.run();

        if (lookup.getResult() == Lookup.SUCCESSFUL) {
            for (final Record element : records) {
                if (element instanceof TXTRecord) {
                    final TXTRecord txt = (TXTRecord) element;
                    for (final Iterator<String> j = txt.getStrings().iterator(); j
                            .hasNext();) {
                        final String message = j.next().trim();
                        if (message.startsWith("TNA=")) {
                            responseMessage = message.replace("TNA=", "");
                        }
                    }
                }
            }
        }

        addTnaToCache(input, responseMessage);
        
        return responseMessage;
    }

    /**
     * Check if the input is a Name or an IP.
     * 
     * @param input
     *            The IP or Name.
     * @return False if it's an IP, true otherwise.
     */
    public static boolean isName(final String input) {
        final Pattern p =
                Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
        final Matcher m = p.matcher(input);
        return !m.matches();
    }

    /**
     * Resolve the IP or Name to a TNA.
     * 
     * @param input
     *            The IP or Name.
     * @return The TNA or "null" if TNA was not found.
     * @throws TextParseException
     *             If a bogous input was given-
     * @throws UnknownHostException
     *             If the DNS server is not reachable.
     */
    public static String resolve(final String input) throws TextParseException,
            UnknownHostException {
        String tna = TNAHelper.getLastTNARecord(input);
        if ((tna == null) && TNAHelper.isName(input)) {
            for (final String domain : TNAHelper.domains) {
                if (tna == null) {
                    tna = TNAHelper.getLastTNARecord(input + domain);
                    continue;
                }
            }
        }
        return tna;
    }

    /**
     * Revert an IP address. E.g. 10.7.3.1 -> 1.3.7.10.
     * 
     * @param ip
     *            The IP address.
     * @return The reverted address.
     */
    private static final String revertIp(final String ip) {
        String result = "";

        final String fields[] = ip.split("\\.");

        for (int x = fields.length - 1; x >= 0; x--) {
            result += fields[x];

            if (x != 0) {
                result += ".";
            }
        }

        return result;
    }
}
