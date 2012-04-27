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


package org.opennaas.extensions.idb.utils;

import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.opennaas.core.utils.Helpers;
import org.opennaas.extensions.idb.database.hibernate.Domain;
import org.opennaas.extensions.idb.database.hibernate.Endpoint;
import org.opennaas.extensions.idb.database.hibernate.InterDomainLink;
import org.opennaas.extensions.idb.database.hibernate.TNAPrefix;
import org.opennaas.extensions.idb.database.hibernate.VIEW_InterDomainLink;

/**
 * Collection Class for small helping methods for topology-purposes.
 * 
 * @author zimmerm2
 */
public final class TopologyHelpers {

    protected static Random random = null;

    /**
     * return a InterDomainLink with random values.
     * 
     * @return InterDomainLink with random values
     * @throws URISyntaxException 
     */
    public static InterDomainLink getInterDomainTestLink() throws URISyntaxException {
        final InterDomainLink link = new InterDomainLink(Helpers
                .getRandomString(), Helpers.getRandomString(), Helpers.getRandomString(),
                TopologyHelpers.getTestEndpoint(), Helpers
                .getPositiveRandomInt());
        return link;
    }

    /**
     * return a TNAPrefix with random values.
     * 
     * @return TNAPrefix with random values
     * @throws URISyntaxException 
     */
    public static TNAPrefix getRandomPrefix(Domain dom) throws URISyntaxException {
        final TNAPrefix prefix = new TNAPrefix();
        prefix.setPrefix(Endpoint.ipv4ToString(Helpers.getRandomInt()) + "/16");
        prefix.setDomain(dom);
        return prefix;
    }

    /**
     * return a random TNA.
     * 
     * @return String
     */
    public static String getRandomTNA() {
        final String tna = Endpoint.ipv4ToString(Helpers.getPositiveRandomInt());
        return tna;
    }

    /**
     * return a random TNA.
     * 
     * @return String
     */
    public static String getRandomTNA(Set<TNAPrefix> prefix) {
    	// get one prefix, not important which one
    	TNAPrefix pre = (TNAPrefix)prefix.toArray()[0];
    	// split into prefix-base and the prefix-length
    	String prefixBase = pre.getPrefix().split("/")[0];
    	int prefixLength = Integer.parseInt(pre.getPrefix().split("/")[1]);

    	// split the prefix into the four parts
    	String[] parts = prefixBase.split("\\.");
    	String normalizedPrefixBase = "";
    	// convert every part into binary representation, normalized to 8 Bits, and put
    	// all together
    	for(int i = 0; i < parts.length; i++) {
    		String binary = Integer.toBinaryString(Integer.parseInt(parts[i]));
    		while(binary.length() < 8) {
    			binary = "0" + binary;
    		}
    		normalizedPrefixBase +=binary; 
    	}
    	
    	// use a char-array to edit the binary representation
    	char[] normalizedPreChar = normalizedPrefixBase.toCharArray();
    	for(int i = 0; i < normalizedPreChar.length; i++) {
    		// only randomize Bits after the prefix
    		if(i >= prefixLength) {
        		char c = String.valueOf(Helpers.getPositiveRandomInt()%2).charAt(0);
        		normalizedPreChar[i] = c;
    		}
    	}

    	// now convert the binary representation back into a TNA-Address 
    	int aux = normalizedPreChar.length - 1;
    	int pow = 0;
    	int result = 0;
    	String resultTNA = "";
    	// loop through four TNA-parts
    	for(int i = 1; i <= 4; i++) {
    		// loop through 8 Bits per part
    		for(int j = 1; j <= 8; j++) {
        		int wert = Integer.parseInt(String.valueOf(normalizedPreChar[aux]));
        		if(wert != 0) result += Math.pow(wert*2, pow);
    			pow++;
    			aux--;
    		}
			resultTNA = "." + result + resultTNA;
    		pow = 0;
    		result = 0;
    	}
    	// cut of the leading point and return the new TNA-Address
    	return resultTNA.substring(1);
    }

    /**
     * return a TestDomain with random values.
     * 
     * @return Domain with random values
     * @throws URISyntaxException 
     * @throws URISyntaxException
     */
    public static Domain getTestDomain() throws URISyntaxException {
        return getTestDomain(Helpers.getRandomString());
    }

    /**
     * return a TestDomain with random values.
     * 
     * @return Domain with random values
     * @throws URISyntaxException
     */
    public static Domain getTestDomain(final String name)
            throws URISyntaxException {
        final Domain testDomain = new Domain(name, Helpers.getRandomString(),
                Helpers.getRandomString());

        testDomain.addPrefix(TopologyHelpers.getRandomPrefix(testDomain).getPrefix());

        for (final Endpoint endpoint : TopologyHelpers
                .getTestEndpointsForDomain(testDomain)) {
            testDomain.getEndpoints().add(endpoint);
        }

        return testDomain;
    }

    /**
     * return a list of 3 domains with random values.
     * 
     * @return List of 3 Domains with random values
     * @throws URISyntaxException
     */
    public static List<Domain> getTestDomains() throws URISyntaxException {
        final LinkedList<Domain> domainList = new LinkedList<Domain>();

        domainList
                .add(TopologyHelpers.getTestDomain(Helpers.getRandomString()));
        domainList
                .add(TopologyHelpers.getTestDomain(Helpers.getRandomString()));
        domainList
                .add(TopologyHelpers.getTestDomain(Helpers.getRandomString()));

        return domainList;
    }

    /**
     * return a Endpoint with random values.
     * 
     * @return Endpoint with random values
     * @throws URISyntaxException 
     */
    public static Endpoint getTestEndpoint() throws URISyntaxException {
        final Endpoint endpoint = new Endpoint(TopologyHelpers.getRandomTNA(),
                Helpers.getRandomString(), Helpers.getRandomString(),
                TopologyHelpers.getTestDomain(),
                Helpers.getPositiveRandomInt(), Helpers.getPositiveRandomInt());

        return endpoint;
    }

    /**
     * return a list of 2 Endpoints with random values.
     * 
     * @return List of 2 Endpoints with random values
     * @throws URISyntaxException 
     */
    public static List<Endpoint> getTestEndpoints() throws URISyntaxException {
        final List<Endpoint> endpoints = new LinkedList<Endpoint>();
        endpoints.add(TopologyHelpers.getTestEndpoint());
        endpoints.add(TopologyHelpers.getTestEndpoint());
        return endpoints;
    }

    /**
     * return a list of 2 Endpoints with random values, but the fkDomainParam
     * set to the committed one.
     * 
     * @param fkDomainName
     * @return List of 2 Endpoints
     */
    public static List<Endpoint> getTestEndpointsForDomain(final Domain domain) {
        final List<Endpoint> result = TopologyHelpers.getTestEndpoints(domain);
        return result;
    }

    public static Endpoint getTestEndpointForDomain(final Domain domain) {
    	return TopologyHelpers.getTestEndpoint(domain);
    }

    private static List<Endpoint> getTestEndpoints(Domain domain) {
        final List<Endpoint> endpoints = new LinkedList<Endpoint>();
        endpoints.add(TopologyHelpers.getTestEndpoint(domain));
        endpoints.add(TopologyHelpers.getTestEndpoint(domain));
        return endpoints;        
    }

    private static Endpoint getTestEndpoint(Domain domain) {
        final Endpoint endpoint = new Endpoint(TopologyHelpers.getRandomTNA(domain.getPrefixes()),
                Helpers.getRandomString(), Helpers.getRandomString(),
                domain, Helpers.getPositiveRandomInt(), Helpers.getPositiveRandomInt());

        return endpoint;
    }

    /**
     * return a Link with random values.
     * 
     * @return Link with random values
     * @throws URISyntaxException 
     */
    public static VIEW_InterDomainLink getTestLink() throws URISyntaxException {
        final VIEW_InterDomainLink link = new VIEW_InterDomainLink(
                TopologyHelpers.getTestEndpoint(), TopologyHelpers
                        .getTestEndpoint(), Helpers.getRandomString(), Helpers
                        .getRandomString(), Helpers.getRandomInt(), Helpers
                        .getRandomInt());
        return link;
    }
}