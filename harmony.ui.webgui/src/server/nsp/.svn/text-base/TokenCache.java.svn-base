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


package server.nsp;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import server.common.GuiLogger;

public class TokenCache {

    private static TokenCache selfInstance = null;

    private static final String GET_METHOD = "getToken";

    private static final String SET_METHOD = "setToken";

    private static final String GET_ID = "getReservationID";

    private static final HashMap<String, String> TOKEN_MAP =
            new HashMap<String, String>();

    public static final TokenCache getCache() {
        if (null == selfInstance) {
            selfInstance = new TokenCache();
        }

        return selfInstance;
    }

    /**
     * Get ID from given Object.
     * 
     * @param obj
     * @return
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private final String getGRIfromObj(final Object obj)
            throws SecurityException, NoSuchMethodException,
            IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        Method method = obj.getClass().getMethod(GET_ID);

        return (String) method.invoke(obj);
    }

    /**
     * Add token to given object.
     * 
     * @param obj
     */
    public final void addTokenToObj(final Object obj) {
        try {
            final String id = getGRIfromObj(obj);

            final String token = TOKEN_MAP.get(id);

            if (null == token) {
                GuiLogger.getInternalLogger().debug(
                        "No Token found for REQ " + id);
                
                return;
            }

            Method method = obj.getClass().getMethod(SET_METHOD, String.class);

            method.invoke(obj, token);

            GuiLogger.getInternalLogger().debug(
                    "Added Token " + token + " to REQ " + id);
        } catch (NoSuchMethodException e) {
            GuiLogger.getInternalLogger()
                    .debug(
                            "Token not supported for "
                                    + obj.getClass().getSimpleName());
        } catch (Exception e) {
            GuiLogger.getInternalLogger().error(
                    "Unable to add token: " + e.getMessage(), e);
        }
    }

    /**
     * Get Token from given Object.
     * 
     * @param obj
     */
    public final void getTokenFromObj(final Object obj) {
        try {
            final String id = getGRIfromObj(obj);

            Method method = obj.getClass().getMethod(GET_METHOD);

            final String token = (String) method.invoke(obj);

            TOKEN_MAP.put(id, token);

            GuiLogger.getInternalLogger().debug(
                    "Stored Token " + token + " from REQ " + id);
        } catch (NoSuchMethodException e) {
            GuiLogger.getInternalLogger().debug(
                    "Token not supported from "
                            + obj.getClass().getSimpleName());
        } catch (Exception e) {
            GuiLogger.getInternalLogger().error(
                    "Unable to get token: " + e.getMessage(), e);
        }

    }

    private TokenCache() {
        TOKEN_MAP.put("dummyID", "dummyValue");
    }
}
