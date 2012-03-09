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


package client.helper;

import client.interfaces.ArgonManagementService;
import client.interfaces.ArgonManagementServiceAsync;
import client.interfaces.CommonService;
import client.interfaces.CommonServiceAsync;
import client.interfaces.NspClientProxy;
import client.interfaces.NspClientProxyAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * Helper Class to create RPC requests.
 * 
 * @author gassen
 */
public class RpcRequest {
    /** Argon Gui Server URL. */
    public static final String ARGON_URL = "/gui/server/argon";
    /** NSP Gui Server URL. */
    public static final String NSP_URL = "/gui/server/nsp";
    /** Common Gui Server URL. */
    public static final String COMMON_URL = "/gui/server/common";

    /** RPC Class. */
    private static ArgonManagementServiceAsync argonService = null;
    /** RPC Class. */
    private static NspClientProxyAsync nspService = null;
    /** RPC Class. */
    private static CommonServiceAsync commonService = null;

    /**
     * Create Argon RPC request class.
     */
    private static final void createArgon() {
        System.out.println("before");
        RpcRequest.argonService =
                (ArgonManagementServiceAsync) GWT
                        .create(ArgonManagementService.class);

        final ServiceDefTarget argonEndpoint =
                (ServiceDefTarget) RpcRequest.argonService;
        final String argonRelativeURL =
                GWT.getModuleBaseURL() + RpcRequest.ARGON_URL;
        argonEndpoint.setServiceEntryPoint(argonRelativeURL);
    }

    /**
     * Create NSP RPC request class.
     */
    private static final void createNsp() {
        RpcRequest.nspService =
                (NspClientProxyAsync) GWT.create(NspClientProxy.class);

        final ServiceDefTarget nspEndpoint =
                (ServiceDefTarget) RpcRequest.nspService;
        final String nspRelativeURL =
                GWT.getModuleBaseURL() + RpcRequest.NSP_URL;
        nspEndpoint.setServiceEntryPoint(nspRelativeURL);
    }

    /**
     * Create NSP RPC request class.
     */
    private static final void createCommon() {
        RpcRequest.commonService =
                (CommonServiceAsync) GWT.create(CommonService.class);

        final ServiceDefTarget commonEndpoint =
                (ServiceDefTarget) RpcRequest.commonService;
        final String commonRelativeURL =
                GWT.getModuleBaseURL() + RpcRequest.COMMON_URL;
        commonEndpoint.setServiceEntryPoint(commonRelativeURL);
    }

    /**
     * Get Argon RPC Request Class.
     * 
     * @return Argon RPC Request Class
     */
    public static final ArgonManagementServiceAsync argon() {
        if (null == RpcRequest.argonService) {
            RpcRequest.createArgon();
        }

        return RpcRequest.argonService;
    }

    /**
     * Get NSP RPC Request Class.
     * 
     * @return NSP RPC Request Class
     */
    public static final NspClientProxyAsync nsp() {
        if (null == RpcRequest.nspService) {
            RpcRequest.createNsp();
        }

        return RpcRequest.nspService;
    }

    /**
     * Get Common RPC Request Class.
     * 
     * @return Common RPC Request Class
     */
    public static final CommonServiceAsync common() {
        if (null == RpcRequest.commonService) {
            RpcRequest.createCommon();
        }

        return RpcRequest.commonService;
    }
}
