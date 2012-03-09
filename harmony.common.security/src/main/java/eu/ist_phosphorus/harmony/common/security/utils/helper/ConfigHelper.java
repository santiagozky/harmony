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


package eu.ist_phosphorus.harmony.common.security.utils.helper;

import java.util.MissingResourceException;

import eu.ist_phosphorus.harmony.common.utils.Config;

public class ConfigHelper {
    private static Boolean IN_REQUEST_ENCRYPT = null;

    private static Boolean OUT_REQUEST_ENCRYPT = null;

    private static Boolean IN_RESPONSE_ENCRYPT = null;

    private static Boolean OUT_RESPONSE_ENCRYPT = null;

    private static Boolean IN_REQUEST_SIGN = null;

    private static Boolean OUT_REQUEST_SIGN = null;

    private static Boolean IN_RESPONSE_SIGN = null;

    private static Boolean OUT_RESPONSE_SIGN = null;

    /**
     * @param val
     * @param alt
     * @return
     */
    private static final Boolean getVal(final String val, final String alt) {
        boolean res;

        try {
            res = Config.isTrue("aai", val);
        } catch (MissingResourceException e) {
            res = Config.isTrue("aai", alt);
        }

        return new Boolean(res);
    }

    /**
     * @return
     */
    public static final boolean inboundRequestEncryption() {
        if (null == IN_REQUEST_ENCRYPT) {
            IN_REQUEST_ENCRYPT =
                    getVal("force.in.request.encryption",
                            "force.request.encryption");
        }

        return IN_REQUEST_ENCRYPT.booleanValue();
    }
    
    /**
     * @return
     */
    public static final boolean outboundRequestEncryption() {
        if (null == OUT_REQUEST_ENCRYPT) {
            OUT_REQUEST_ENCRYPT =
                    getVal("force.out.request.encryption",
                            "force.request.encryption");
        }

        return OUT_REQUEST_ENCRYPT.booleanValue();
    }
    
    /**
     * @return
     */
    public static final boolean inboundRequestSigning() {
        if (null == IN_REQUEST_SIGN) {
            IN_REQUEST_SIGN =
                    getVal("force.in.request.signing",
                            "force.request.signing");
        }

        return IN_REQUEST_SIGN.booleanValue();
    }
    
    /**
     * @return
     */
    public static final boolean outboundRequestSigning() {
        if (null == OUT_REQUEST_SIGN) {
            OUT_REQUEST_SIGN =
                    getVal("force.out.request.signing",
                            "force.request.signing");
        }

        return OUT_REQUEST_SIGN.booleanValue();
    }
    
    
    /**
     * @return
     */
    public static final boolean inboundResponseEncryption() {
        if (null == IN_RESPONSE_ENCRYPT) {
            IN_RESPONSE_ENCRYPT =
                    getVal("force.in.response.encryption",
                            "force.response.encryption");
        }

        return IN_RESPONSE_ENCRYPT.booleanValue();
    }
    
    /**
     * @return
     */
    public static final boolean outboundResponseEncryption() {
        if (null == OUT_RESPONSE_ENCRYPT) {
            OUT_RESPONSE_ENCRYPT =
                    getVal("force.out.response.encryption",
                            "force.response.encryption");
        }

        return OUT_RESPONSE_ENCRYPT.booleanValue();
    }
    
    /**
     * @return
     */
    public static final boolean inboundResponseSigning() {
        if (null == IN_RESPONSE_SIGN) {
            IN_RESPONSE_SIGN =
                    getVal("force.in.response.signing",
                            "force.response.signing");
        }

        return IN_RESPONSE_SIGN.booleanValue();
    }
    
    /**
     * @return
     */
    public static final boolean outboundResponseSigning() {
        if (null == OUT_RESPONSE_SIGN) {
            OUT_RESPONSE_SIGN =
                    getVal("force.out.response.signing",
                            "force.response.signing");
        }

        return OUT_RESPONSE_SIGN.booleanValue();
    }
    
    /**
     * @param status
     */
    public static final void setRequestEncryption(final boolean status) {
        Boolean wrapper = new Boolean(status);
        
        IN_REQUEST_ENCRYPT = wrapper;
        OUT_REQUEST_ENCRYPT = wrapper;
    }
    
    /**
     * @param status
     */
    public static final void setRequestSigning(final boolean status) {
        Boolean wrapper = new Boolean(status);
        
        IN_REQUEST_SIGN = wrapper;
        OUT_REQUEST_SIGN = wrapper;
    }
    
    /**
     * @param status
     */
    public static final void setResponseEncryption(final boolean status) {
        Boolean wrapper = new Boolean(status);
        
        IN_RESPONSE_ENCRYPT = wrapper;
        OUT_RESPONSE_ENCRYPT = wrapper;
    }
    
    /**
     * @param status
     */
    public static final void setResponseSigning(final boolean status) {
        Boolean wrapper = new Boolean(status);
        
        IN_RESPONSE_SIGN = wrapper;
        OUT_RESPONSE_SIGN = wrapper;
    }
    
    /**
     * @param status
     */
    public static final void setSigning(final boolean status) {
        setResponseSigning(status);
        setRequestSigning(status);
    }
    
    /**
     * @param status
     */
    public static final void setEncryption(final boolean status) {
        setResponseEncryption(status);
        setRequestEncryption(status);
    }
}
