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

import java.rmi.Naming;

import org.apache.log4j.Logger;

import de.unibonn.viola.argon.requestProcessor.requestHandler.operator.externalInterface.OperatorInterface;
import org.opennaas.extensions.idb.da.argon.Constants;
import org.opennaas.core.utils.Config;

public class ArgonOperatorClient {
    protected static OperatorInterface operator;
    protected static String argonOperatorInterfaceUrl;
    protected static Logger log = Logger.getLogger(ArgonOperatorClient.class);
	private static boolean shutdown = false;


    private static String getArgonOperatorInterfaceURL(){
        if(argonOperatorInterfaceUrl == null){
            argonOperatorInterfaceUrl = Config.getString(Constants.adapterProperties, "argonNRPS.operatorInterfaceAddress");
        }
        return argonOperatorInterfaceUrl;
    }

    /**
     * blocking function
     * @return
     */
    private static boolean getConnectionToArgon() {
        String argonURL = getArgonOperatorInterfaceURL();
        if(argonURL==null)
            return false;
        log.debug("try to get connection to argon on: "+argonURL);
        int tries = 10;
        while(tries-- > 0 || shutdown == true){
            try {
                operator = (OperatorInterface)Naming.lookup(argonURL);
            }catch(Throwable t){
            	log.error("Could not get connection to argon.", t);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                       log.error("Interrupted while sleeping.", e);
                }
                continue;
            }
            if(operator == null){
                    try {
                            Thread.sleep(2000);
                    } catch (InterruptedException e) {
                    	log.error("Interrupted while sleeping.", e);
                    }
                    log.debug("Waiting for connection to argon");

            }else{
                    break;
            }
        }
        if(operator==null)
            return false;
        return true;
    }

    public static OperatorInterface getOperator(){
        if(operator==null){
            getConnectionToArgon();
        }
        return operator;
    }

	public static void setShutdown(boolean shutdown) {
		ArgonOperatorClient.shutdown = shutdown;
	}
}
