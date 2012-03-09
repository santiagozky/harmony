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


package server.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * Class copy data from one class into one other.
 * 
 * @author gassen
 */
public abstract class Converter {
    /** Package one. */
    private final String package1;
    /** Package two. */
    private final String package2;
    /** Method cache. */
    private final MethodReflectionCache cache = new MethodReflectionCache();
    /** Logger. */
    private static Logger LOG = null;
    /** Exemptionlist */
    private final HashMap<Class<?>, Class<?>> exemptionList =
            new HashMap<Class<?>, Class<?>>();

    public static final Logger getLogger() {
        if (null == Converter.LOG) {
            Converter.LOG = GuiLogger.getInternalLogger();
        }

        return Converter.LOG;
    }

    /**
     * Default Constructor.
     * 
     * @param pgk1
     *            Package1
     * @param pkg2
     *            Package2
     */
    public Converter(final String pgk1, final String pkg2) {
        this.package1 = pgk1;
        this.package2 = pkg2;

        Converter.getLogger();
    }

    /**
     * Copy data from one hash map into another (non-generic -> generic)
     * 
     * @param dst
     *            Destination Object
     * @param methodName
     *            Method name of get Method
     * @param returnObject
     *            Object returned from get method
     * @param returnType
     *            Type of the returned object
     * @throws NoSuchMethodException
     *             Reflection error
     * @throws SecurityException
     *             Reflection error
     * @throws InvocationTargetException
     *             Reflection error
     * @throws IllegalAccessException
     *             Reflection error
     * @throws IllegalArgumentException
     *             Reflection error
     */
    @SuppressWarnings("unchecked")
    private final void convertMap(final Object dst, final String methodName,
            final Object returnObject, final Class<?> returnType)
            throws SecurityException, NoSuchMethodException,
            IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {

        final Class<?> dstClass = dst.getClass();

        final Method current = this.cache.getMethod(dstClass, methodName);

        HashMap<Object, Object> srcMap = null;

        if (HashMap.class.isInstance(returnObject)) {
            srcMap = (HashMap<Object, Object>) returnObject;
        } else {
            throw new IllegalArgumentException("Unsupported return value: "
                    + returnObject);
        }

        final Object dstObject = current.invoke(dst);

        HashMap<Object, Object> dstMap = null;

        if (HashMap.class.isInstance(dstObject)) {
            dstMap = (HashMap<Object, Object>) dstObject;
        } else {
            throw new IllegalArgumentException("Unsupported return value: "
                    + dstObject);
        }

        final Set<Object> set = dstMap.keySet();
        final Iterator<Object> iter = set.iterator();

        while (iter.hasNext()) {
            final Object key = iter.next();
            final Object value = srcMap.get(key);

            dstMap.put(this.autoConvert(key), this.autoConvert(value));
        }

    }

    /**
     * Copy data from one collection into another (non-generic -> generic)
     * 
     * @param dst
     *            Destination Object
     * @param methodName
     *            Method name of get Method
     * @param returnObject
     *            Object returned from get method
     * @param returnType
     *            Type of the returned object
     * @throws SecurityException
     *             Reflection error
     * @throws NoSuchMethodException
     *             Reflection error
     * @throws IllegalArgumentException
     *             Reflection error
     * @throws IllegalAccessException
     *             Reflection error
     * @throws InvocationTargetException
     *             Reflection error
     */
    @SuppressWarnings("unchecked")
    private final void convertCollection(final Object dst,
            final String methodName, final Object returnObject,
            final Class<?> returnType) throws SecurityException,
            NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException {

        final Class<?> dstClass = dst.getClass();

        final Method current = this.cache.getMethod(dstClass, methodName);

        Collection<Object> srcCollection = null;

        if (Collection.class.isInstance(returnObject)) {
            srcCollection = (Collection<Object>) returnObject;
        } else {
            throw new IllegalArgumentException("Unsupported return value: "
                    + returnObject);
        }

        final Object dstObject = current.invoke(dst);
        Collection<Object> dstCollection = null;

        if (Collection.class.isInstance(dstObject)) {
            dstCollection = (Collection<Object>) dstObject;
        } else {
            throw new IllegalArgumentException("Unsupported return value: "
                    + dstObject);
        }

        for (final Object obj : srcCollection) {
            final Object convertedObject = this.autoConvert(obj);

            dstCollection.add(convertedObject);
        }

    }

    /**
     * Copy default java classes (non-generic -> generic)
     * 
     * @param dst
     *            Destination Object
     * @param methodName
     *            Method name of get Method
     * @param returnObject
     *            Object returned from get method
     * @param returnType
     *            Type of the returned object
     * @throws SecurityException
     *             Reflection error
     * @throws NoSuchMethodException
     *             Reflection error
     * @throws IllegalArgumentException
     *             Reflection error
     * @throws IllegalAccessException
     *             Reflection error
     * @throws InvocationTargetException
     *             Reflection error
     */
    private final void convertDefault(final Object dst,
            final String methodName, final Object returnObject,
            final Class<?> returnType) throws SecurityException,
            NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException {

        String targetMethod = null;

        if (methodName.startsWith("get")) {
            // Everything else then boolean
            targetMethod = "set" + methodName.substring(3);
        } else if (methodName.startsWith("isSet")) {
            // isSet has no setter method... do not convert
            return;
        } else if (methodName.startsWith("is")) {
            // Boolean
            targetMethod = "set" + methodName.substring(2);
        } else {
            // Whatever... no point of interest
            return;
        }

        final Class<?> dstClass = dst.getClass();

        final Method current =
                this.cache.getMethod(dstClass, targetMethod, returnType);

        current.invoke(dst, returnObject);
    }

    /**
     * Convert custom Objects.
     * 
     * @param dst
     *            Destination Object
     * @param methodName
     *            Method name of get Method
     * @param returnObject
     *            Object returned from get method
     * @param returnType
     *            Type of the returned object
     * @throws IllegalArgumentException
     *             Reflection error
     * @throws IllegalAccessException
     *             Reflection error
     * @throws InvocationTargetException
     *             Reflection error
     * @throws SecurityException
     *             Reflection error
     * @throws NoSuchMethodException
     *             Reflection error
     */
    private final void convertCustom(final Object dst, final String methodName,
            final Object returnObject, final Class<?> returnType)
            throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException, SecurityException, NoSuchMethodException {

        String targetMethod = null;

        if (methodName.startsWith("get")) {
            targetMethod = "set" + methodName.substring(3);
        } else {
            return;
        }

        final Class<?> dstClass = dst.getClass();

        final Object convertedObject = this.autoConvert(returnObject);

        final Method current =
                this.cache.getMethod(dstClass, targetMethod, convertedObject
                        .getClass());

        current.invoke(dst, convertedObject);
    }

    /**
     * Convert custom Objects.
     * 
     * @param dst
     *            Destination Object
     * @param methodName
     *            Method name of get Method
     * @param returnObject
     *            Object returned from get method
     * @param returnType
     *            Type of the returned object
     * @throws IllegalArgumentException
     *             Reflection error
     * @throws IllegalAccessException
     *             Reflection error
     * @throws InvocationTargetException
     *             Reflection error
     * @throws SecurityException
     *             Reflection error
     * @throws NoSuchMethodException
     *             Reflection error
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    private final void convertEnum(final Object dst, final String methodName,
            final Object returnObject, final Class<?> returnType)
            throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException, SecurityException,
            NoSuchMethodException, ClassNotFoundException,
            InstantiationException {

        String targetMethod = null;

        if (methodName.startsWith("get")) {
            targetMethod = "set" + methodName.substring(3);
        } else {
            return;
        }

        final Class<?> dstClass = dst.getClass();

        final Method method = returnType.getMethod("value");
        final String enumValue = (String) method.invoke(returnObject);

        final Object convertedObject = this.getDestinationObject(returnType);
        final Class<?> convertedClass = convertedObject.getClass();
        final Method setMethod =
                convertedClass.getMethod("setValue", String.class);
        setMethod.invoke(convertedObject, enumValue);

        final Method current =
                this.cache.getMethod(dstClass, targetMethod, convertedObject
                        .getClass());

        current.invoke(dst, convertedObject);
    }

    /**
     * Converts a exemption by using specified methods. Exemption coversion is a
     * little bit tricky. This method calls this.convertExemption(Object). The
     * method call would be reflected to the subclass first. If no special
     * method is defined, it will call its own method which will call the
     * ConverterUtils.
     * 
     * @param dst
     * @param methodName
     * @param returnObject
     * @param returnType
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    private final void convertExemption(final Object dst,
            final String methodName, final Object returnObject,
            final Class<?> returnType, final Class<?> resultType)
            throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException, SecurityException, NoSuchMethodException {

        String targetMethod = null;

        if (methodName.startsWith("get")) {
            targetMethod = "set" + methodName.substring(3);
        } else {
            return;
        }

        final Class<?> dstClass = dst.getClass();

        Object convertedObject = null;

        Method converterMethod = null;

        try {
            converterMethod =
                    this.cache.getMethod(this.getClass(), "convertExemption",
                            returnType);

            convertedObject = converterMethod.invoke(this, returnObject);
        } catch (final InvocationTargetException e) {
            throw e;
        } catch (final Exception e) {
            converterMethod =
                    this.cache.getMethod(ConverterUtils.class,
                            "convertExemption", returnType);

            convertedObject = converterMethod.invoke(null, returnObject);
        }

        if (null != convertedObject) {
            final Method current =
                    this.cache.getMethod(dstClass, targetMethod, resultType);

            current.invoke(dst, convertedObject);
        } else {
            throw new IllegalArgumentException("Returned parameter is empty");
        }
    }

    /**
     * Figure out the destination Class and create Object.
     * 
     * @param srcClass
     *            Source class which should be converted
     * @return Result Object
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws Exception
     *             If nothing is found
     */
    private final Object getDestinationObject(final Class<?> srcClass)
            throws ClassNotFoundException, InstantiationException,
            IllegalAccessException {
        String className = srcClass.getName();

        className = className.substring(className.lastIndexOf(".") + 1);

        final String packageName = srcClass.getPackage().getName();

        if (packageName.equals(this.package2)) {
            className = this.package1 + "." + className;
        } else if (packageName.equals(this.package1)) {
            className = this.package2 + "." + className;
        } else if (packageName.startsWith("java.")) {
            className = packageName + "." + className;
        } else {
            throw new ClassNotFoundException("Unsupported class: " + className);
        }

        final Class<?> result = Class.forName(className);

        return result.newInstance();
    }

    /**
     * Call the appropriate method to copy the individual return values.
     * 
     * @param dst
     *            Destination Object
     * @param methodName
     *            Method name of get Method
     * @param returnObject
     *            Object returned from get method
     * @param returnType
     *            Type of the returned object
     * @throws SecurityException
     *             Reflection error
     * @throws IllegalArgumentException
     *             Reflection error
     * @throws NoSuchMethodException
     *             Reflection error
     * @throws IllegalAccessException
     *             Reflection error
     * @throws InvocationTargetException
     *             Reflection error
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    private final void splitConversion(final Object dst,
            final String methodName, final Object returnObject,
            final Class<?> returnType) throws SecurityException,
            IllegalArgumentException, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException,
            ClassNotFoundException, InstantiationException {

        if (this.exemptionList.containsKey(returnType)) {
            // Convert Exemption
            this.convertExemption(dst, methodName, returnObject, returnType,
                    this.exemptionList.get(returnType));
        } else if (returnType.isEnum()) {
            this.convertEnum(dst, methodName, returnObject, returnType);
        } else if (returnType.isPrimitive()) {
            this.convertDefault(dst, methodName, returnObject, returnType);
        } else if (Collection.class.isInstance(returnObject)) {
            // Convert Collections
            this.convertCollection(dst, methodName, returnObject, returnType);
        } else if (AbstractMap.class.isInstance(returnObject)) {
            // Convert Maps
            this.convertMap(dst, methodName, returnObject, returnType);
        } else if (returnType.getPackage().getName().startsWith("java.")) {
            // Convert default classes
            this.convertDefault(dst, methodName, returnObject, returnType);
        } else if (returnType.getPackage().getName().equals(this.package1)
                || returnType.getPackage().getName().equals(this.package2)) {
            // Convert custom classes
            this.convertCustom(dst, methodName, returnObject, returnType);
        } else {
            // This class is not designed to do something else
        }
    }

    /**
     * Copy the data using get set methods.
     * 
     * @param src
     *            Source Object
     * @param srcClass
     *            Source Class
     * @param dst
     *            Destination Object
     */
    private final void copyData(final Object src, final Class<?> srcClass,
            final Object dst) {

        for (final Method method : srcClass.getMethods()) {
            // Don't touch inherited methods from Object class
            if (Object.class.equals(method.getDeclaringClass())) {
                continue;
            }

            // Get return type
            final Class<?> returnType = method.getReturnType();

            // Skip if method doesn't return anything
            if ((null == returnType) || returnType.equals(java.lang.Void.TYPE)) {
                continue;
            }

            // Try the conversion
            try {
                // Get return Object form src object
                final Object returnObject = method.invoke(src);

                if (null == returnObject) {
                    continue;
                }

                final String methodName = method.getName();

                this.splitConversion(dst, methodName, returnObject, returnType);

            } catch (final Exception e) {
                this
                        .printInfo("Field can't be converted: "
                                + e.getMessage(), e);

                continue;
            }

        }
    }

    /**
     * Try to concert objects automatically using reflections.
     * 
     * @param src
     *            Source Object
     * @param dst
     *            Source Object
     * @return Target Object
     */
    public final Object autoConvert(final Object src) {
        if (null == src) {
            return null;
        }

        final Class<?> srcClass = src.getClass();

        Object dst = null;

        try {

            dst = this.getDestinationObject(srcClass);

        } catch (final Exception e) {
            this.printInfo("Could not instantiate target class for: "
                    + e.getMessage(), e);

            return null;
        }

        if (srcClass.equals(dst.getClass())) {
            // Nothing to do if classes are equal
            dst = src;
        } else {
            // Copy data
            this.copyData(src, srcClass, dst);
        }

        return dst;
    }

    /**
     * Handle debug/status outputs. TODO: do something useful i.e. write to
     * logfile
     * 
     * @param message
     *            Output message
     * @param error
     *            true if message is an error message
     */
    protected final void printInfo(final String message, final boolean error) {
        if (error) {
            Converter.LOG.error(message);
        } else {
            Converter.LOG.warn(message);
        }
    }

    /**
     * Handle debug/status outputs. TODO: do something useful i.e. write to
     * logfile
     * 
     * @param message
     *            Output message
     * @param error
     *            true if message is an error message
     */
    protected final void printInfo(final String message, final Throwable error) {
        if (NoSuchMethodException.class.isInstance(error)) {
            Converter.LOG.error(message + " (No Such Field)");
        } else {
            Converter.LOG.error(message, error);
        }
    }

    /**
     * Default convert method.
     * 
     * @param src
     *            Source Object
     * @return Converted Object
     */
    public Object convert(final Object src) {
        this.printInfo("No method found to convert " + src.getClass().getName()
                + ": Using default converter", false);

        return this.autoConvert(src);
    }

    /**
     * Default convert method.
     * 
     * @param src
     *            Source Object
     * @return Converted Object
     */
    public Object convertExemption(final Object src) {
        this.printInfo("Forwarding " + src + " to ConverterUtils", false);

        return ConverterUtils.convertExemption(src);
    }

    /**
     * @param src
     * @param dst
     */
    public void addExemption(final Class<?> src, final Class<?> dst) {
        this.exemptionList.put(src, dst);
    }
}
