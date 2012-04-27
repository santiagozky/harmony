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


package org.opennaas.extensions.idb.da.argon.implementation;

import java.util.HashMap;
import java.util.Map;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.EndpointNotFoundFaultException;

/**
 * Singleton, that stores Mapping-Information from argon-interfaces to TNA-Strings
 *
 * @author Steffen Claus
 *
 */
public class TNAMapper {

    private static TNAMapper instance = null;

    /*
     * use two maps for faster lookup
     */
    private Map<String,String> tna2Interface;
    private Map<String,String> interface2Tna;


    protected TNAMapper(){
        this.tna2Interface = new HashMap<String,String>();
        this.interface2Tna = new HashMap<String,String>();
    }

    public static TNAMapper getInstance(){
        if(instance == null){
            instance = new TNAMapper();
        }
        return instance;
    }

    /**
     * maps argon-interfaces to TNA-strings
     * @param intf the internal argon-interface
     * @return TNA generated from input interface
     *
     */
    public String addInterface(String intf){
        String tna = convertInterfaceStringtoTNAString(intf);
        String collision = this.tna2Interface.get(tna);
        if (collision == null) {
            this.tna2Interface.put(tna, intf);
            this.interface2Tna.put(intf, tna);
        } else {
            if (! collision.equals(intf)) {
                throw new RuntimeException("duplicate TNA "+tna+" for interfaces "+collision+" and "+intf);
            }
        }
        return tna;
    }

    /**
     *
     * @param tna tha TNA-String
     * @return the internal argon-interface
     * @throws EndpointNotFoundFaultException 
     */
    public String getInterfaceFromTNA(String tna) throws EndpointNotFoundFaultException{
        final String ifIdentifier = this.tna2Interface.get(tna);
        if (null == ifIdentifier) {
            throw new EndpointNotFoundFaultException("TNA '"+tna+"' not found.");
        }
        return ifIdentifier;
    }

    /**
     *
     * @param intf the internal argon-interface
     * @return the TNA-String
     */
    public String getTNAFromInterface(String intf){
        return this.interface2Tna.get(intf);
    }

    public static String convertInterfaceStringtoTNAString(String intf) {
        final String errorMsg = "cannot parse interface string \""+intf+"\"";
        String[] s = intf.split("@");
        if (s.length != 2) {
            throw new RuntimeException(errorMsg);
        }

        if (s[1].startsWith("r08")) {
            String r1 = null;
            if (s[1].equals("r08bn")) {
                r1 = "10.7.11.";
            } else if (s[1].equals("r08ju")) {
                r1 = "10.7.12.";
            } else if (s[1].equals("r08sa")) {
                r1 = "10.7.13.";
            } else if (s[1].equals("r08sa2")) {
                r1 = "10.7.132.";
            } else {
                r1 = "0.0.0.";
            }
    
            String[] t = s[0].split("/");
            if (t.length != 3) {
                throw new RuntimeException(errorMsg);
            }
            int r2 = 0;
            for (int i = 0; i < 3; i++) {
                char[] n = t[i].toCharArray();
                int value = 0;
                for (int j = 0; j < n.length; j++) {
                    if ((n[j] >= '0') && (n[j] <= '9')) {
                        value = 10 * value + (n[j] - '0');
                    }
                }
                r2 = 10 * r2 + value;
            }
            if (r2 > 255) {
                throw new RuntimeException(errorMsg);
            }
    
            return r1+r2;
        } else if (s[1].startsWith("v")) {
            int offset = 0;
            if (s[1].equals("vbn")) {
                offset = 0;
            } else if (s[1].equals("vju")) {
                offset = 32;
            } else if (s[1].equals("vsa")) {
                offset = 64;
            } else {
                throw new RuntimeException(errorMsg);
            }
            String[] t = s[0].split("/");
            if (t.length != 3) {
                throw new RuntimeException(errorMsg);
            }
            if ((! t[0].equals("0")) || (! t[1].equals("0"))) {
                throw new RuntimeException(errorMsg);
            }
            int i = Integer.parseInt(t[2]);
            return "10.7.227."+(offset+i);
        } else {
            throw new RuntimeException(errorMsg);
        }
    }
}
