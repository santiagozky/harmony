/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License. Note: THIS FILE HAS
 * BEEN CHANGED BY THE IST-PHOSPHORUS PROJECT
 */

package org.opennaas.extensions.gmpls.synthesizer;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.muse.core.proxy.ProxyHandler;
import org.apache.muse.core.proxy.ReflectionProxyHandler;
import org.apache.muse.tools.generator.synthesizer.ClassInfo;
import org.apache.muse.tools.generator.util.ConfigurationData;
import org.apache.muse.tools.generator.util.ConfigurationDataDescriptor;
import org.apache.muse.tools.inspector.JavaMethod;
import org.apache.muse.tools.inspector.JavaProperty;
import org.apache.muse.tools.inspector.ResourceInspector;
import org.apache.muse.util.ReflectUtils;
import org.apache.muse.util.xml.XsdUtils;
import org.w3c.dom.Document;

public class ProxyClassSynthesizer extends ProxyInterfaceSynthesizer {
    @SuppressWarnings("hiding")
    static ConfigurationDataDescriptor[] REQUIRED_PARAMETERS = new ConfigurationDataDescriptor[] {
            ConfigurationData.CAPABILITIES_MAP_LIST_CONFIGURATION,
            ConfigurationData.GENERATE_CUSTOM_HEADERS_CONFIGURATION,
            ConfigurationData.WSDL_DOCUMENT_LIST_CONFIGURATION };

    StringBuffer _operationNamesCode;
    StringBuffer _returnTypesCode;
    StringBuffer _actionsCode;

    StringBuffer _requestNamesCode;
    StringBuffer _responseNamesCode;
    StringBuffer _requestParamNamesCode;

    StringBuffer _propertiesOperationsCode;
    private String _interfaceName;

    private boolean _hasMethods = false;
    private boolean _hasProperties = false;

    private StringBuffer beginActionsCode() {
        final StringBuffer code = new StringBuffer();

        this.indent(code);
        code.append("private static final String[] _ACTIONS = {");
        this.newLine(code);
        this.indent(2, code);
        return code;
    }

    private StringBuffer beginOperationNamesCode() {
        final StringBuffer code = new StringBuffer();

        this.indent(code);
        code.append("private static final String[] _METHOD_NAMES = {");
        this.newLine(code);
        this.indent(2, code);
        return code;
    }

    private StringBuffer beginOperationsCode() {
        return new StringBuffer();
    }

    private StringBuffer beginRequestNamesCode() {
        final StringBuffer code = new StringBuffer();

        this.indent(code);
        code.append("private static final QName[] _REQUEST_NAMES = {");
        this.newLine(code);
        this.indent(2, code);
        this._importSet.add(QName.class);
        return code;
    }

    private StringBuffer beginRequestParamNamesCode() {
        final StringBuffer code = new StringBuffer();

        this.indent(code);
        code.append("private static final QName[][] _REQUEST_PARAM_NAMES = {");
        this.newLine(code);
        this._importSet.add(QName.class);
        return code;
    }

    private StringBuffer beginResponseNamesCode() {
        final StringBuffer code = new StringBuffer();

        this.indent(code);
        code.append("private static final QName[] _RESPONSE_NAMES = {");
        this.newLine(code);
        this.indent(2, code);
        this._importSet.add(QName.class);
        return code;
    }

    private StringBuffer beginReturnTypesCode() {
        final StringBuffer code = new StringBuffer();

        this.indent(code);
        code.append("private static final Class[] _RETURN_TYPES = {");
        this.newLine(code);
        this.indent(2, code);
        return code;
    }

    private void endDeclarationCode(final StringBuffer code) {
        final int length = code.length();
        code.delete(length - 2, length);

        this.newLine(code);
        this.indent(code);
        this.generateCloseBlock(code);
        code.append(";");
        this.newLine(code);
    }

    @Override
    protected void endHeaderCode(final ClassInfo classInfo) {
        this.generateImports(classInfo, this._headerCode);
        final String[] interfaces = { ReflectUtils
                .getShortName(this._interfaceName) };
        final String parentClass = ReflectUtils
                .getShortName(this._baseClientClass);
        this.generateClassDef(this._className, parentClass, interfaces, false,
                this._headerCode);
    }

    @Override
    protected String generateClassName(final Document wsdlDocument) {
        this._interfaceName = super.generateClassName(wsdlDocument);
        return this._interfaceName + "Proxy";
    }

    @Override
    public String generateCombinedCode(final ClassInfo classInfo) {
        this.endDeclarationCode(this._operationNamesCode);
        this.endDeclarationCode(this._returnTypesCode);
        this.endDeclarationCode(this._actionsCode);

        this.endDeclarationCode(this._requestNamesCode);
        this.endDeclarationCode(this._responseNamesCode);
        this.endDeclarationCode(this._requestParamNamesCode);

        final StringBuffer footerCode = this.generateFooterCode();

        classInfo.addImports(this._importSet);
        this.endHeaderCode(classInfo);

        final StringBuffer code = new StringBuffer();

        code.append(this._headerCode);

        if (this._hasProperties || this._hasMethods) {
            code.append(this._operationsCode);
            this.newLine(code);
        }

        if (this._hasMethods) {
            code.append(this._operationNamesCode);
            this.newLine(code);
            code.append(this._returnTypesCode);
            this.newLine(code);
            code.append(this._actionsCode);
            this.newLine(code);
            code.append(this._requestNamesCode);
            this.newLine(code);
            code.append(this._responseNamesCode);
            this.newLine(code);
            code.append(this._requestParamNamesCode);
        }

        code.append(footerCode);

        return code.toString();
    }

    private StringBuffer generateFooterCode() {
        final StringBuffer code = new StringBuffer();

        this.generateProxyConstructors(code);

        if (this._hasMethods) {
            this.generateGetHandler(code);

            this.newLine(code);
            this.generateStatic(code);
        }

        this.generateCloseBlock(code);

        this.newLine(code);
        return code;
    }

    private void generateGetHandler(final StringBuffer code) {
        this.indent(code);
        code.append("protected ProxyHandler getHandler(String methodName)");
        this.addImport(ProxyHandler.class);
        this.newLine(code);

        this.indent(code);
        this.generateOpenBlock(code);
        this.newLine(code);

        this.indent(2, code);
        this
                .statement(
                        "return (ProxyHandler)_HANDLERS_BY_NAME.get(methodName);",
                        code);
        this.newLine(code);

        this.indent(code);
        this.generateCloseBlock(code);
    }

    private void generateHandler(final JavaMethod method,
            final ClassInfo classInfo, final StringBuffer code) {
        this.indent(2, code);
        code.append("ProxyHandler handler = getHandler(\"");
        code.append(method.getJavaName());
        code.append("\");");
        this.newLine(code);

        classInfo.addImport(ProxyHandler.class);
    }

    private void generateInvoke(final JavaMethod method, final StringBuffer code) {
        this.indent(2, code);

        final Class<?> returnType = method.getReturnType();

        if (returnType == void.class) {
            code.append(this.getInvokeString() + ";");
        } else if (!returnType.isPrimitive()) {

            this.newLine(code);
            this.indent(2, code);
            code.append("try {");
            this.newLine(code);
            this.indent(3, code);

            code.append("return (");
            code.append(ReflectUtils.getShortName(returnType));
            code.append(")" + this.getInvokeString() + ";");

            this.newLine(code);
            this.indent(2, code);
            code.append("} catch (SoapFault e) {");
            this.newLine(code);
            this.indent(3, code);
            code
                    .append("throw "
                            + "org.opennaas.extensions.idb.serviceinterface.databinding.utils.FaultConverter");
            this.newLine(code);
            this.indent(4, code);
            code
                    .append(".getInstance().getOriginalFault(e, this.getDestination());");
            this.newLine(code);
            this.indent(2, code);
            code.append("}");

        } else {
            final String className = this.getPrimitiveClassName(returnType);
            final String convertMethodName = this
                    .getPrimitiveConvertMethodName(className);

            code.append(className);
            code.append(" result = (");
            code.append(className);
            code.append(")" + this.getInvokeString() + ";");
            this.newLine(code);

            this.indent(2, code);
            code.append("return result.");
            code.append(convertMethodName);
            code.append("();");
        }

        this.newLine(code);
    }

    @Override
    protected void generateMethodBody(final JavaMethod method,
            final ClassInfo classInfo, final StringBuffer code) {
        this._hasMethods = true;

        this.newLine(code);
        this.indent(code);
        this.generateOpenBlock(code);
        this.newLine(code);

        this.generateParamConversion(method, code);
        this.generateHandler(method, classInfo, code);
        this.generateInvoke(method, code);

        this.indent(code);
        this.generateCloseBlock(code);

        this.updateMethodNames(method);
        this.updateReturnTypes(method);
        this.updateActions(method);

        this.updateRequestNames(method);
        this.updateResponseNames(method);
        this.updateRequestParamNamesCode(method);
    }

    @Override
    protected void generateMethodQualifier(final StringBuffer code) {
        code.append("public ");
    }

    private void generateParamConversion(final JavaMethod operation,
            final StringBuffer code) {
        final QName[] paramNames = operation.getParameterTypeNames();
        final Class<?>[] paramTypes = operation.getParameterTypes();

        this.indent(2, code);
        code.append("Object[] params = new Object[");
        code.append(paramTypes.length);
        code.append("];");
        this.newLine(code);

        if (paramTypes.length > 0) {
            this.newLine(code);
        }

        for (int n = 0; n < paramTypes.length; ++n) {
            this.indent(2, code);
            code.append("params[");
            code.append(n);
            code.append("] = ");

            final String name = ResourceInspector
                    .getLowerCamelName(paramNames[n].getLocalPart());
            code.append(this.getObjectName(name, paramTypes[n]));
            code.append(";");
            this.newLine(code);
        }

        this.newLine(code);
    }

    @Override
    protected void generatePropertyDeleteBody(final JavaProperty property,
            final ClassInfo classInfo, final int propertyIndex,
            final StringBuffer code) {
        this._hasProperties = true;

        this.newLine(code);
        this.indent(code);
        this.generateOpenBlock(code);
        this.newLine(code);

        this.indent(2, code);
        code.append("deleteResourceProperty(PROPERTIES[");
        code.append(propertyIndex);
        code.append("]);\n");

        this.indent(code);
        this.generateCloseBlock(code);
    }

    @Override
    protected void generatePropertyGetBody(final JavaProperty property,
            final ClassInfo classInfo, final int propertyIndex,
            final StringBuffer code) {
        this._hasProperties = true;

        Class<?> type = property.getJavaType();
        this.newLine(code);
        this.indent(code);
        this.generateOpenBlock(code);
        this.newLine(code);

        if (type.isArray()) {
            this.indent(2, code);
            code.append("return (");
            code.append(ReflectUtils.getShortName(type));
            code.append(")getPropertyAsObject(");
            code.append("PROPERTIES[");
            code.append(propertyIndex);
            code.append("], ");

            type = ReflectUtils.getClassFromArrayClass(type);
            code.append(ReflectUtils.getShortName(type));
            code.append(".class);");
            this.newLine(code);
        }

        else {
            this.indent(2, code);

            final Class<?> array = ReflectUtils.getArrayClassFromClass(type);
            code.append(ReflectUtils.getShortName(array));

            code.append(" results = (");
            code.append(ReflectUtils.getShortName(array));
            code.append(")getPropertyAsObject(");
            code.append("PROPERTIES[");
            code.append(propertyIndex);
            code.append("], ");
            code.append(ReflectUtils.getShortName(type));
            code.append(".class);");
            this.newLine(code);
            this.indent(2, code);
            code.append("return results.length == 0 ? ");
            code.append(this.getNullValue(type));
            code.append(" : results[0];");
            this.newLine(code);
        }

        this.indent(code);
        this.generateCloseBlock(code);
    }

    @Override
    protected void generatePropertySetBody(final JavaProperty property,
            final ClassInfo classInfo, final int propertyIndex,
            final String setType, final StringBuffer code) {
        this._hasProperties = true;

        Class<?> type = property.getJavaType();
        if (type.isArray()) {
            type = ReflectUtils.getClassFromArrayClass(type);
        }

        this.newLine(code);
        this.indent(code);
        this.generateOpenBlock(code);
        this.newLine(code);
        this.indent(2, code);

        code.append(setType);
        code.append("ResourceProperty(PROPERTIES[");
        code.append(propertyIndex);
        code.append("], ");

        if (type.isArray()) {
            code.append("value");
        } else {
            code.append("new Object[]{ ");
            code.append(this.getArrayValue(type));
            code.append(" }");
        }

        code.append(");");
        this.newLine(code);

        this.indent(code);
        this.generateCloseBlock(code);
    }

    private void generateProxyConstructors(final StringBuffer code) {
        final String className = ReflectUtils.getShortName(this._className);

        final Constructor<?>[] ctors = this._baseClientClass.getConstructors();

        for (int n = 0; n < ctors.length; ++n) {
            this.indent(code);
            code.append("public ");
            code.append(className);

            final Class<?>[] params = ctors[n].getParameterTypes();
            this.generateParamList(null, ctors[n].getParameterTypes(), code);
            this.addImports(ctors[n].getParameterTypes());

            this.indent(code);
            this.generateOpenBlock(code);
            this.newLine(code);

            this.indent(2, code);
            code.append("super(");

            for (int i = 0; i < params.length; ++i) {
                code.append(ProxyInterfaceSynthesizer.PARAM_NAME + i);

                if (i != params.length - 1) {
                    code.append(", ");
                }
            }

            code.append(");");
            this.newLine(code);

            this.indent(code);
            this.generateCloseBlock(code);
            this.newLine(2, code);
        }
    }

    private void generateStatic(final StringBuffer code) {

        this.newLine(code);
        this.indent(code);
        this.statement(
                "private static final Map _HANDLERS_BY_NAME = new HashMap();",
                code);

        this.addImport(Map.class);
        this.addImport(HashMap.class);

        this.newLine(2, code);
        this.indent(code);
        code.append("static");

        this.newLine(code);
        this.indent(code);
        this.generateOpenBlock(code);
        this.newLine(code);

        this.indent(2, code);
        code.append("for (int n = 0; n < _METHOD_NAMES.length; ++n)");

        this.newLine(code);
        this.indent(2, code);
        this.generateOpenBlock(code);

        this.newLine(code);
        this.indent(3, code);
        code.append("ProxyHandler handler = new ReflectionProxyHandler();");
        this.addImport(ReflectionProxyHandler.class);

        this.newLine(code);
        this.indent(3, code);
        code.append("handler.setAction(_ACTIONS[n]);");

        this.newLine(code);
        this.indent(3, code);
        code.append("handler.setRequestName(_REQUEST_NAMES[n]);");

        this.newLine(code);
        this.indent(3, code);
        code
                .append("handler.setRequestParameterNames(_REQUEST_PARAM_NAMES[n]);");

        this.newLine(code);
        this.indent(3, code);
        code.append("handler.setResponseName(_RESPONSE_NAMES[n]);");

        this.newLine(code);
        this.indent(3, code);
        code.append("handler.setReturnType(_RETURN_TYPES[n]);");

        this.newLine(2, code);
        this.indent(3, code);
        code.append("_HANDLERS_BY_NAME.put(_METHOD_NAMES[n], handler);");

        this.newLine(code);
        this.indent(2, code);
        this.generateCloseBlock(code);
        this.newLine(code);

        this.indent(code);
        this.generateCloseBlock(code);
    }

    @Override
    public ConfigurationDataDescriptor[] getConfigurationDataDescriptions() {
        return ProxyClassSynthesizer.REQUIRED_PARAMETERS;
    }

    private String getInvokeString() {
        if (this._generateCustomHeaders) {
            return "invoke(handler, params, customHeaders)";
        }

        return "invoke(handler, params)";
    }

    @Override
    protected void initializeCode(final String className) {
        this._importSet = new HashSet();
        this.addImport(this._baseClientClass);

        this._className = className;
        this._headerCode = this.beginHeaderCode(this._className);
        this._operationsCode = this.beginOperationsCode();

        this._operationNamesCode = this.beginOperationNamesCode();
        this._returnTypesCode = this.beginReturnTypesCode();
        this._actionsCode = this.beginActionsCode();

        this._requestNamesCode = this.beginRequestNamesCode();
        this._responseNamesCode = this.beginResponseNamesCode();
        this._requestParamNamesCode = this.beginRequestParamNamesCode();

        this.resetIndex();
    }

    private void updateActions(final JavaMethod method) {
        this._actionsCode.append('"');
        this._actionsCode.append(method.getActionURI());
        this._actionsCode.append('"');
        this._actionsCode.append(", ");
    }

    @Override
    protected void updateCode(final ClassInfo classInfo) {
        this.updateMethods(classInfo, this._operationsCode);
        classInfo.addImports(this._importSet);
    }

    private void updateMethodNames(final JavaMethod method) {
        this._operationNamesCode.append('"');
        this._operationNamesCode.append(method.getJavaName());
        this._operationNamesCode.append('"');
        this._operationNamesCode.append(", ");
    }

    private void updateRequestNames(final JavaMethod method) {
        final QName returnName = method.getName();
        if ((returnName != null) && !returnName.equals(XsdUtils.ANY_TYPE_QNAME)) {
            this.generateQName(returnName, this._requestNamesCode);
        } else {
            this._requestNamesCode.append("null");
        }

        this._requestNamesCode.append(", ");
    }

    private void updateRequestParamNamesCode(final JavaMethod method) {
        final QName[] names = method.getParameterTypeNames();

        this.indent(2, this._requestParamNamesCode);
        this.generateOpenBlock(this._requestParamNamesCode);
        this.newLine(this._requestParamNamesCode);

        for (final QName element : names) {
            this.indent(3, this._requestParamNamesCode);
            this.generateQName(element, this._requestParamNamesCode);
            this._requestParamNamesCode.append(",");
            this.newLine(this._requestParamNamesCode);
        }

        if (names.length > 0) {
            final int length = this._requestParamNamesCode.length();
            this._requestParamNamesCode.delete(length - 2, length);
        }

        this.newLine(this._requestParamNamesCode);
        this.indent(2, this._requestParamNamesCode);
        this.generateCloseBlock(this._requestParamNamesCode);
        this._requestParamNamesCode.append(",");
        this.newLine(this._requestParamNamesCode);
    }

    private void updateResponseNames(final JavaMethod method) {
        final QName returnName = method.getReturnName();
        if ((returnName != null) && !returnName.equals(XsdUtils.ANY_TYPE_QNAME)) {
            this.generateQName(returnName, this._responseNamesCode);
        } else {
            this._responseNamesCode.append("null");
        }

        this._responseNamesCode.append(", ");
    }

    private void updateReturnTypes(final JavaMethod method) {
        Class type = method.getReturnType();

        if (type.isArray()) {
            type = ReflectUtils.getClassFromArrayClass(type);
        }

        this._returnTypesCode.append(ReflectUtils.getShortName(type));
        this._returnTypesCode.append(".class");
        this._returnTypesCode.append(", ");
    }
}
