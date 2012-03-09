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


package eu.ist_phosphorus.harmony.common.security.authz;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.aaaarch.config.ConfigDomainsPhosphorus;
import org.aaaarch.config.ConstantsNS;
import org.aaaarch.gaaapi.ResourceHelper;
import org.aaaarch.gaaapi.SubjectSet;
import org.aaaarch.gaaapi.tvs.TVS;
import org.aaaarch.gaaapi.tvs.TVSTable;
import org.aaaarch.gaaapi.tvs.TokenBuilder;
import org.aaaarch.gaaapi.tvs.TokenKey;
import org.aaaarch.gaaapi.tvs.XMLTokenType;
import org.apache.log4j.Logger;
import org.apache.muse.util.xml.XmlUtils;
import org.w3c.dom.Document;

import eu.ist_phosphorus.harmony.common.security.utils.helper.TokenHelper;
import eu.ist_phosphorus.harmony.common.utils.Config;
import eu.ist_phosphorus.harmony.common.utils.PhLogger;

public class HarmonyTVS {
	static Logger logger = PhLogger.getSeparateLogger("aai");
    /**
     * 
     * @param gri
     *            the gri
     * @param tokenkey
     *            the binary tokenkey
     * @param validTime
     *            the valid time in minutes
     * @param resourceSource
     * @param resourceTarget
     * @throws Exception
     *             if GAAA-tk throws Exception
     */
    @SuppressWarnings("unchecked")
    public void addEntryTVSTable(final String gri, final long notBefore,
            final long notAfter, final String resourceSource,
            final String resourceTarget) throws Exception {
    	
        final String domainId = ConfigDomainsPhosphorus.DOMAIN_PHOSPHORUS_VIOLA;
        // For now any action is create-path
        final String[] actions = { "create-path" };

        final Vector sessionCtx =
                TVS
                        .getSessionCtxVector(
                                domainId,
                                gri,
                                ResourceHelper.parseResourceURI(this
                                        .getResourceURI(resourceSource,
                                                resourceTarget, gri)),
                                this.getActMap(actions),
                                this
                                        .getSubjMap("junit@viola.testbed.ist-phosphorus.eu"));
        sessionCtx.set(0, new Date(notBefore));
        sessionCtx.set(1, new Date(notAfter));
        TVSTable.addEntryTVSTable(domainId, gri, sessionCtx);
    }  
    
    private HashMap<String, String> getActMap(final String... actions)
            throws Exception {
        final HashMap<String, String> actmap = new HashMap<String, String>();
        for (final String action : actions) {
            actmap.put(ConstantsNS.ACTION_ACTION_ID, action);
        }

        return actmap;
    }

    /**
     * 
     * @param gri
     *            the gri
     * @return Token in binary format
     * @throws Exception
     *             if GAAA-tk throws Exception
     */
    public byte[] getBinaryToken(final String gri) throws Exception {
        final byte[] tokenKey = TokenKey.generateTokenKey(gri);
        return TokenBuilder.getBinaryToken(gri, tokenKey);
    }

    private String getResourceURI(final String resourceSource,
            final String resourceTarget, final String gri) {
        final String resourceNamespace =
                Config.getString("aai", "resource.namespace");
        final String resourceDomain =
                Config.getString("aai", "resource.domain");
        final String resourceService =
                Config.getString("aai", "resource.service");
        final String resourceIdURI =
                resourceNamespace + resourceDomain + resourceService
                        + "resource-id=" + gri + "/" + "source="
                        + resourceSource + "/" + "target=" + resourceTarget
                        + "/";
        return resourceIdURI;
    }

    @SuppressWarnings("unchecked")
    private HashMap<String, String> getSubjMap(final String userCredentials)
            throws Exception {
        final String subjectId = userCredentials;
        final String subjectConfdata = "";
        // TODO: get from aaa-module?
        final String subjectRole = "admin";
        final String subjectContext = "demo041"; // use TNA based policy

        final HashMap<String, String> subjmap = SubjectSet.getSubjSetTest();
        subjmap.put(ConstantsNS.SUBJECT_SUBJECT_ID, subjectId);
        subjmap.put(ConstantsNS.SUBJECT_CONFDATA, subjectConfdata);
        subjmap.put(ConstantsNS.SUBJECT_ROLE, subjectRole);
        subjmap.put(ConstantsNS.SUBJECT_CONTEXT, subjectContext);
        return subjmap;
    }

    /**
     * 
     * @param gri
     *            the gri
     * @param validtime
     *            the valid time in minutes
     * @param simple
     *            simple or complex Token
     * @return Token in XML format
     * @throws Exception
     *             if GAAA-tk throws Exception
     */
    public static String getXMLToken(final String gri, final int validtime,
            final boolean simple, final Date notBefore, final Date notOnOrAfter)
            throws Exception {

        final String domainId = ConfigDomainsPhosphorus.DOMAIN_PHOSPHORUS_VIOLA;
        final byte[] tokenKey = TokenKey.generateTokenKey(gri);
        final String issuer = TokenBuilder.getTokenIssuer(domainId);
        final String tokenValue = TokenBuilder.getXMLTokenValue(gri, tokenKey);

        final Document tokendoc =
                XMLTokenType.generateTokenXML(gri, tokenValue, issuer,
                        notBefore, notOnOrAfter);

        return XmlUtils.toString(tokendoc);
    }

    @SuppressWarnings("unchecked")
    public boolean validate(final String token, final String domainId,
            final String gri) throws Exception {       
        
        final HashMap<String, Comparable> context = TVSTable.getGRIContextMap(domainId, gri);
        final String subjectId =
                (String) context.get(TVSTable.TVS_CTX_SUBJECT_ID);
        final String subjectConfdata = "";
        final String subjectRole =
                (String) context.get(TVSTable.TVS_CTX_SUBJECT_ROLE);
        final String subjectContext =
                (String) context.get(TVSTable.TVS_CTX_SUBJECT_CONTEXT);
        
        
        final HashMap<String, String> subjmap = SubjectSet.getSubjSetTest();
        subjmap.put(ConstantsNS.SUBJECT_SUBJECT_ID, subjectId);
        subjmap.put(ConstantsNS.SUBJECT_CONFDATA, subjectConfdata);
        subjmap.put(ConstantsNS.SUBJECT_ROLE, subjectRole);
        subjmap.put(ConstantsNS.SUBJECT_CONTEXT, subjectContext);

        final String resourceSource =
            (String) context.get(TVSTable.TVS_CTX_RESOURCE_SOURCE);
        final String resourceTarget =
            (String) context.get(TVSTable.TVS_CTX_RESOURCE_TARGET);
        
        final HashMap<String, String> resmap =
                ResourceHelper.parseResourceURI(this.getResourceURI(
                        resourceSource, resourceTarget, gri));

        final String[] actions =
                { (String) context.get(TVSTable.TVS_CTX_ACTION_ID) };
//        final String[] actions =
//        { action };

        final HashMap<String, String> actmap = this.getActMap(actions);

        final String fullToken = TokenHelper.getFullTokenByValue(token);

        return TVS.validateAuthzRequestByToken(fullToken, resmap, actmap,
                subjmap);
    }
    
    
}
