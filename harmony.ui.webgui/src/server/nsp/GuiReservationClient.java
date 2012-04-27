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


/**
 *
 */
package server.nsp;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;

import org.apache.muse.ws.addressing.EndpointReference;

import server.common.MethodReflectionCache;
import server.common.NspConverter;
import org.opennaas.extensions.idb.serviceinterface.reservation.SimpleReservationClient;

/**
 * @author gassen
 */
public class GuiReservationClient extends SimpleReservationClient {
    private final MethodReflectionCache methods = new MethodReflectionCache();

    private final NspConverter converter = new NspConverter();

    /**
     * Init methods.
     */
    private final void init() {
        this.methods.hashClassMethods(SimpleReservationClient.class);
    }

    /**
     * @param endpointReference
     */
    public GuiReservationClient(final EndpointReference endpointReference) {
        super(endpointReference);

        this.init();
    }

    /**
     * @param endpointReference
     * @throws URISyntaxException
     */
    public GuiReservationClient(final String endpointReference)
            throws URISyntaxException {
        super(endpointReference);

        this.init();
    }

    /**
     * Forward request to nsp client.
     * 
     * @param methodName
     * @param req
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    public final Object sendRequest(final String methodName, final Object req)
            throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException, SecurityException, NoSuchMethodException {

        final Object convertedReq = this.converter.convert(req);

        final Method method =
                this.methods.getMethod(this, methodName, convertedReq
                        .getClass());

        if (null == method) {
            throw new IllegalAccessException("Unknown Method: " + methodName);
        }

        if (null == convertedReq) {
            throw new IllegalArgumentException("Invalid argument: " + req);
        }
        
        TokenCache.getCache().addTokenToObj(convertedReq);

        final Object res = method.invoke(this, convertedReq);

        TokenCache.getCache().getTokenFromObj(res);
        
        final Object convertedRes = this.converter.convert(res);

        if (null == convertedRes) {
            throw new IllegalArgumentException("Converter can't convert " + res);
        }

        return convertedRes;
    }

}
