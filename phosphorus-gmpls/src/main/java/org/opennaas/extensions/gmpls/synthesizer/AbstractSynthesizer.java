/* 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 * Note:
 * THIS FILE HAS BEEN CHANGED BY THE IST-PHOSPHORUS PROJECT
 */

package org.opennaas.extensions.gmpls.synthesizer;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.namespace.QName;

import org.apache.muse.tools.generator.synthesizer.ClassInfo;
import org.apache.muse.tools.generator.synthesizer.Synthesizer;
import org.apache.muse.tools.generator.util.Capability;
import org.apache.muse.tools.inspector.JavaMethod;
import org.apache.muse.tools.inspector.JavaProperty;
import org.apache.muse.util.ReflectUtils;

public abstract class AbstractSynthesizer implements Synthesizer {
    static final String INDENT = "    ";

    static final String REQUEST_SUFFIX = "Request";

    private static final String JAVA_CLASS_NAME = "MyCapability";

    static final String INTERFACE_PREFIX = "I";

    private int _prefixCounter = 0;

    private final HashMap<String, String> _prefixes = new HashMap<String, String>();

    protected StringBuffer beginHeaderCode(final String className) {
        final StringBuffer code = new StringBuffer();

        this.generateHeaderComment(className, code);
        this.generatePackage(className, code);

        return code;
    }

    protected void comment(final String comment, final StringBuffer code) {
        code.append("// " + comment);
    }

    protected void comment(final StringBuffer code) {
        this.comment("", code);
    }

    protected String convertType(Class<?> returnType, final ClassInfo classInfo) {
        final boolean isArray = returnType.isArray();

        returnType = isArray ? ReflectUtils.getClassFromArrayClass(returnType)
                : returnType;

        // Use fully-qualified name if conflict exists
        if (classInfo.hasConflict(returnType)) {
            return returnType.getName();
        }
        return ReflectUtils.getShortName(returnType) + (isArray ? "[]" : "");
    }

    protected String createFileName(final String interfaceName) {
        return interfaceName.replaceAll("\\.", "\\" + File.separator) + ".java";
    }

    protected void generateClassDef(final String remoteClassName,
            final boolean isInterface, final StringBuffer code) {
        this.generateClassDef(remoteClassName, null, null, isInterface, code);
    }

    protected void generateClassDef(final String remoteClassName,
            final String extendsClass, final String[] implementsClasses,
            final boolean isInterface, final StringBuffer code) {
        code.append("public ");
        code.append(isInterface ? "interface" : "class");
        code.append(" ");
        code.append(ReflectUtils.getShortName(remoteClassName));

        if (extendsClass != null) {
            code.append(" extends " + extendsClass);
        }

        if ((implementsClasses != null) && (implementsClasses.length > 0)) {
            code.append(" implements ");
            code.append(implementsClasses[0]);

            for (int i = 1; i < implementsClasses.length; i++) {
                code.append(", " + implementsClasses[i]);
            }
        }

        this.newLine(code);
        this.generateOpenBlock(code);
        this.newLine(code);
    }

    protected String generateClassName(final Capability capability) {
        return this.generateClassName(null, capability);
    }

    protected String generateClassName(final String prefix,
            final Capability capability) {
        final String implementingClass = capability.getImplementingClass();

        String packageName = null;
        String shortClassName = null;

        if (implementingClass == null) {
            packageName = ClassInfo.getPackageName(capability.getURI());
            shortClassName = AbstractSynthesizer.JAVA_CLASS_NAME;
        } else {
            packageName = ReflectUtils.getPackageName(implementingClass);
            shortClassName = ReflectUtils.getShortName(implementingClass);
        }

        if (prefix != null) {
            shortClassName = prefix + shortClassName;
        }

        return packageName + "." + shortClassName;
    }

    protected void generateCloseBlock(final StringBuffer code) {
        code.append("}");
    }

    protected void generateHeaderComment(final String className,
            final StringBuffer code) {
        final String shortName = ReflectUtils.getShortName(className);
        final String fileName = shortName + ".java";

        this.comment(code);
        this.newLine(code);

        this.comment(fileName, code);
        this.newLine(code);

        this.comment(new Date().toString(), code);
        this.newLine(code);

        this.comment("Generated by the Apache Muse Code Generation Tool", code);
        this.newLine(code);

        this.comment(code);
        this.newLine(code);
    }

    protected void generateImports(final ClassInfo classInfo,
            final StringBuffer code) {
        final Iterator<?> i = classInfo.getImports().iterator();

        while (i.hasNext()) {
            final String className = this.needsImport((Class<?>) i.next());
            if (className != null) {
                this.statement("import " + className + ";", code);
                this.newLine(code);
            }
        }

        this.newLine(code);
    }

    protected void generateOpenBlock(final StringBuffer code) {
        code.append("{");
    }

    protected void generatePackage(final String className,
            final StringBuffer code) {
        final String packageName = ReflectUtils.getPackageName(className);

        if (packageName != null) {
            this.statement("package " + packageName + ";", code);
            this.newLine(2, code);
        }

    }

    protected void generateQName(final QName qname, final StringBuffer code) {
        this.generateQName(qname.getNamespaceURI(), qname.getLocalPart(), code);
    }

    protected void generateQName(final String uri, final String name,
            final StringBuffer code) {
        code.append("new QName(\"");

        if ((uri != null) && (uri.length() > 0)) {
            code.append(uri);
            code.append("\", \"");
        }

        code.append(name);
        code.append("\", \"");

        code.append(this.getPrefix(uri));
        code.append("\")");
    }

    protected String getArrayValue(final Class<?> type) {
        if (!type.isPrimitive()) {
            return "value";
        }

        if (type == boolean.class) {
            return "Boolean.toString(value)";
        } else if ((type == float.class) || (type == double.class)) {
            return "Double.toString(value)";
        }

        return "Long.toString(value)";
    }

    protected String getMethodName(final JavaMethod method) {
        String name = method.getJavaName();
        if (name.endsWith(AbstractSynthesizer.REQUEST_SUFFIX)) {
            name = name.substring(0, name
                    .indexOf(AbstractSynthesizer.REQUEST_SUFFIX));
        }
        return name;
    }

    protected String getNullValue(final Class<?> type) {
        if (!type.isPrimitive()) {
            return "null";
        }

        if (type == boolean.class) {
            return "false";
        }

        return "0";
    }

    protected String getObjectName(final String name, final Class<?> type) {
        if (!type.isPrimitive()) {
            return name;
        } else if (type == boolean.class) {
            return "new Boolean(" + name + ')';
        } else if (type == double.class) {
            return "new Double(" + name + ')';
        } else if (type == float.class) {
            return "new Float(" + name + ')';
        } else if (type == int.class) {
            return "new Integer(" + name + ')';
        } else if (type == long.class) {
            return "new Long(" + name + ')';
        }

        return "new Short(" + name + ')';
    }

    protected String getParamName(final QName paramName, final int position) {
        String name;

        if (paramName == null) {
            name = "param" + position;
        } else {
            name = paramName.getLocalPart();
        }

        return name;
    }

    protected String getPrefix(final String uri) {
        String prefix = this._prefixes.get(uri);

        if (prefix == null) {
            prefix = "pfx" + this._prefixCounter++;
            this._prefixes.put(uri, prefix);
        }

        return prefix;
    }

    protected String getPrimitiveClassName(final Class<?> type) {
        if (type == boolean.class) {
            return "Boolean";
        } else if ((type == float.class) || (type == double.class)) {
            return "Float";
        }

        return "Integer";
    }

    protected String getPrimitiveConvertMethodName(final String type) {
        if (type.endsWith("Boolean")) {
            return "booleanValue";
        } else if (type.endsWith("Float") || type.endsWith("Double")) {
            return "floatValue";
        }

        return "intValue";
    }

    protected String getPropertyName(final JavaProperty property,
            boolean forSetter) {
        String name = property.getName().getLocalPart();
        if (!forSetter) {
            return name;
        }
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        return name;
    }

    protected void indent(final int indent, final StringBuffer code) {
        for (int i = 0; i < indent; i++) {
            code.append(AbstractSynthesizer.INDENT);
        }
    }

    protected void indent(final StringBuffer code) {
        this.indent(1, code);
    }

    protected String needsImport(Class<?> className) {
        if (className.isPrimitive()) {
            return null;
        }
        if (className.isArray()) {
            className = ReflectUtils.getClassFromArrayClass(className);
        }
        if (className.getName().startsWith("java.lang.")) {
            return null;
        }
        return className.getName();
    }

    protected void newLine(final int n, final StringBuffer code) {
        for (int i = 0; i < n; i++) {
            code.append("\n");
        }
    }

    protected void newLine(final StringBuffer code) {
        this.newLine(1, code);
    }

    protected void statement(final String statement, final StringBuffer code) {
        code.append(statement);
    }
}
