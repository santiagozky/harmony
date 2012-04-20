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

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Class to speed up method lookups.
 * <br>The main performance impact in using reflections is the method lookup, as
 * shown by a simple benchmark:<br>
 * <br>
 * 1000000 regular method calls:1566 milliseconds.<br>
 * 1000000 reflective method calls without lookup:1713 milliseconds.<br>
 * 1000000 reflective method calls with lookup:5291 milliseconds.<br>
 * <br>
 * This class uses a hashmap to hash already known methods for faster lookups.
 * <br>Using this cache in the benchmark speeds up the method lookups a lot:<br>
 * <br>
 * 1000000 reflective method calls with lookup and cache:3256 milliseconds.<br>
 * <br>
 * Pre-hashing the methods has no strong effects. It's slightly faster so you
 * may use it if it doesn't make any circumstances.
 *
 * @author gassen
 */
public class MethodReflectionCache {
    /**
     * Hashmap to store methods
     */
    private final HashMap<String, Method> cache = new HashMap<String, Method>();

    /**
     * Generate a unique hash string for every method.
     *
     * @param cls Declaring class
     * @param method Method Name
     * @param params Method Parameters
     * @return Unique identifier string.
     */
    private final String generateHashString(final Class<?> cls,
            final String method, final Class<?>... params) {
        String string = cls.getName() + ":" + method;

        if (null != params) {
            for (final Class<?> paramClass : params) {
                string += (":" + paramClass.getName());
            }
        }

        return string;
    }

    /**
     * Generate a unique hash string for every method.
     *
     * @param cls Declaring class.
     * @param method Method
     * @return Unique identifier string.
     */
    private final String generateHashString(final Class<?> cls,
            final Method method) {
        return this.generateHashString(cls, method.getName(), method
                .getParameterTypes());
    }

    /**
     * Hash a whole class at once.
     *
     * @param cls Class
     */
    public final void hashClassMethods(final Class<?> cls) {
        for (final Method method : cls.getMethods()) {
            this.cache.put(this.generateHashString(cls, method), method);
        }
    }

    /**
     * Default Constructor.
     */
    public MethodReflectionCache() {
        // Nothing to do...
    }

    /**
     * Constructor.
     *
     * @param cls Class to hash
     */
    public MethodReflectionCache(final Class<?> cls) {
        this.hashClassMethods(cls);
    }

    /**
     * Constructor.
     *
     * @param obj Object to hash
     */
    public MethodReflectionCache(final Object obj) {
        this.hashClassMethods(obj.getClass());
    }

    /**
     * Get a method.
     *
     * @param cls Declaring class
     * @param name Method name
     * @param params Parameter types
     * @return Method
     * @throws SecurityException Reflection Error
     * @throws NoSuchMethodException Reflection Error
     */
    public final Method getMethod(final Class<?> cls, final String name,
            final Class<?>... params) throws SecurityException,
            NoSuchMethodException {
        final String lookup = this.generateHashString(cls, name, params);

        Method result = this.cache.get(lookup);

        if (null == result) {
            result = cls.getMethod(name, params);

            this.cache.put(lookup, result);
        }

        return result;
    }

    /**
     * Get a method.
     *
     * @param obj Object containing method
     * @param name Method name
     * @param params Parameter types
     * @return Method
     * @throws SecurityException Reflection Error
     * @throws NoSuchMethodException Reflection Error
     */
    public final Method getMethod(final Object obj, final String name,
            final Class<?>... params) throws SecurityException,
            NoSuchMethodException {
        return this.getMethod(obj.getClass(), name, params);
    }
}
