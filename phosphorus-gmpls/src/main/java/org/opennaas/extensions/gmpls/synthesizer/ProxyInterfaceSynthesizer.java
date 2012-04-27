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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import org.apache.muse.tools.generator.synthesizer.ClassInfo;
import org.apache.muse.tools.generator.util.Capability;
import org.apache.muse.tools.generator.util.ConfigurationData;
import org.apache.muse.tools.generator.util.ConfigurationDataDescriptor;
import org.apache.muse.tools.inspector.JavaMethod;
import org.apache.muse.tools.inspector.JavaProperty;
import org.apache.muse.tools.inspector.ResourceInspector;
import org.apache.muse.util.ReflectUtils;
import org.apache.muse.util.xml.XmlUtils;
import org.apache.muse.ws.addressing.soap.SoapFault;
import org.apache.muse.ws.metadata.WsxConstants;
import org.apache.muse.ws.notification.WsnConstants;
import org.apache.muse.ws.notification.remote.NotificationConsumerClient;
import org.apache.muse.ws.notification.remote.NotificationProducerClient;
import org.apache.muse.ws.resource.lifetime.WsrlConstants;
import org.apache.muse.ws.resource.properties.WsrpConstants;
import org.apache.muse.ws.resource.remote.WsResourceClient;
import org.apache.muse.ws.wsdl.WsdlUtils;
import org.w3c.dom.Document;

public class ProxyInterfaceSynthesizer extends AbstractSynthesizer {

    private static final String TARGET_NS_ATTR = "targetNamespace";

    protected static final String PARAM_NAME = "param";

    static ConfigurationDataDescriptor[] REQUIRED_PARAMETERS = new ConfigurationDataDescriptor[] {
            ConfigurationData.CAPABILITIES_MAP_LIST_CONFIGURATION,
            ConfigurationData.GENERATE_CUSTOM_HEADERS_CONFIGURATION,
            ConfigurationData.WSDL_DOCUMENT_LIST_CONFIGURATION };

    protected final static Set<String> _ignoredCapabilitySet;

    protected final static Map _clientCapabilitiesMap;

    static {
        _ignoredCapabilitySet = new HashSet<String>();

        ProxyInterfaceSynthesizer._ignoredCapabilitySet
                .add(WsxConstants.GET_METADATA_CAPABILITY);
    }

    static {
        _clientCapabilitiesMap = new HashMap();

        final Set<String> baseClientset = new HashSet<String>();
        baseClientset.add(WsrpConstants.GET_CAPABILITY);
        baseClientset.add(WsrpConstants.QUERY_CAPABILITY);
        baseClientset.add(WsrpConstants.SET_CAPABILITY);
        baseClientset.add(WsrlConstants.IMMEDIATE_TERMINATION_URI);
        baseClientset.add(WsrlConstants.SCHEDULED_TERMINATION_URI);
        ProxyInterfaceSynthesizer._clientCapabilitiesMap.put(
                WsResourceClient.class, baseClientset);

        Set<String> set = new HashSet<String>();
        set.add(WsnConstants.PRODUCER_URI);
        set.addAll(baseClientset);
        ProxyInterfaceSynthesizer._clientCapabilitiesMap.put(
                NotificationProducerClient.class, set);

        set = new HashSet();
        set.add(WsnConstants.CONSUMER_URI);
        set.addAll(baseClientset);
        ProxyInterfaceSynthesizer._clientCapabilitiesMap.put(
                NotificationConsumerClient.class, set);
    }

    protected StringBuffer _headerCode;

    protected StringBuffer _operationsCode;

    protected StringBuffer _propertiesCode;

    String _className;

    protected Set<Class<?>> _importSet;

    private boolean _hasProperties;

    protected boolean _generateCustomHeaders;

    private Map<?, ?>[] _capabilityMaps;

    private Document[] _wsdlDocuments;

    protected Class<?> _baseClientClass;

    private Map<String,String>[] _filesMaps;

    private int _index = 0;

    protected void addImport(final Class<?> className) {
        this._importSet.add(className);
    }

    protected void addImports(final Class<?>[] classes) {
        for (final Class<?> element : classes) {
            this.addImport(element);
        }
    }

    private StringBuffer beginOperationsCode() {
        return new StringBuffer();
    }

    private StringBuffer beginPropertiesCode() {
        final StringBuffer code = new StringBuffer();

        this.indent(code);
        code.append("QName[] PROPERTIES = {");

        return code;
    }

    protected void endHeaderCode(final ClassInfo classInfo) {
        this.generateImports(classInfo, this._headerCode);
        this.generateClassDef(this._className, true, this._headerCode);
    }

    private void endOperationsCode() {
        this.newLine(this._operationsCode);
    }

    private void endPropertiesCode() {
        final int length = this._propertiesCode.length();
        this._propertiesCode.delete(length - 1, length);

        this.newLine(this._propertiesCode);
        this.indent(this._propertiesCode);
        this.generateCloseBlock(this._propertiesCode);
        this._propertiesCode.append(";");
        this.newLine(this._propertiesCode);
    }

    protected String generateClassName(final Document wsdlDocument) {
        final String packageName = ClassInfo.getPackageName(wsdlDocument
                .getDocumentElement().getAttribute(
                        ProxyInterfaceSynthesizer.TARGET_NS_ATTR));
        return packageName
                + "."
                + WsdlUtils.getServiceName(XmlUtils
                        .getDocumentRoot(wsdlDocument));
    }

    protected void generateCode(final Document wsdl, final Map<?,?> capabilityMap,
            final Map<String,String> files) {
        final String className = this.generateClassName(wsdl);

        this._baseClientClass = this.getBaseClientClass(capabilityMap);
        this.initializeCode(className);

        final ClassInfo classInfo = new ClassInfo();

        for (final Iterator<?> i = capabilityMap.values().iterator(); i.hasNext();) {
            final Capability capability = (Capability) i.next();

            if (this.needsGeneratedCode(this._baseClientClass, capability)) {
                classInfo.setCapability(capability);
                this.updateCode(classInfo);
            }
        }

        files.put(this.createFileName(className), this
                .generateCombinedCode(classInfo));
    }

    protected String generateCombinedCode(final ClassInfo classInfo) {
        this.endHeaderCode(classInfo);
        this.endOperationsCode();
        this.endPropertiesCode();

        final StringBuffer code = new StringBuffer();

        code.append(this._headerCode);
        code.append(this._operationsCode);

        if (this._hasProperties) {
            code.append(this._propertiesCode);
        }

        return code.append(this.generateFooterCode()).toString();
    }

    private StringBuffer generateFooterCode() {
        final StringBuffer code = new StringBuffer();

        this.newLine(code);
        this.generateCloseBlock(code);
        this.newLine(code);

        return code;
    }

    private void generateMethod(final JavaMethod method,
            final ClassInfo classInfo, final StringBuffer code) {
        this.newLine(2, code);
        this.indent(code);

        this.generateMethodQualifier(code);

        final Class<?> returnType = method.getReturnType();
        code.append(ReflectUtils.getShortName(returnType));

        code.append(' ');
        code.append(method.getJavaName());

        final QName[] paramNames = method.getParameterTypeNames();
        final Class[] paramTypes = method.getParameterTypes();
        this.generateParamList(paramNames, paramTypes, code);

        this.newLine(code);
        this.indent(2, code);
        code.append("throws SoapFault");
        this.addImport(SoapFault.class);

        this.generateMethodBody(method, classInfo, code);

        this.newLine(code);
    }

    protected void generateMethodBody(final JavaMethod method,
            final ClassInfo classInfo, final StringBuffer code) {
        code.append(';');
    }

    protected void generateMethodQualifier(final StringBuffer code) {
        // Do nothing
    }

    protected void generateParamList(final QName[] paramNames,
            final Class<?>[] paramTypes, final StringBuffer code) {
        code.append('(');

        for (int n = 0; n < paramTypes.length; ++n) {
            code.append(ReflectUtils.getShortName(paramTypes[n]));
            code.append(' ');

            if (paramNames != null) {
                code.append(ResourceInspector.getLowerCamelName(paramNames[n]
                        .getLocalPart()));
            } else {
                code.append(ProxyInterfaceSynthesizer.PARAM_NAME + n);
            }

            if (n != paramTypes.length - 1) {
                code.append(", ");
            }
        }

        if (this._generateCustomHeaders) {
            if (paramTypes.length > 0) {
                code.append(", ");
            }

            code.append("Element[] customHeaders");
        }

        code.append(")");
    }

    private void generatePropertyConstant(final JavaProperty property,
            final StringBuffer code) {
        this.newLine(code);
        this.indent(2, code);
        this.generateQName(property.getName(), code);
        code.append(",");
    }

    private void generatePropertyDelete(final JavaProperty property,
            final ClassInfo classInfo, final int propertyIndex,
            final StringBuffer code) {

        this.newLine(code);
        this.indent(code);

        this.generateMethodQualifier(code);

        code.append("void delete" + property.getName().getLocalPart());
        code.append("()");
        this.newLine(code);
        this.indent(2, code);
        code.append("throws SoapFault");
        this.addImport(SoapFault.class);

        this.generatePropertyDeleteBody(property, classInfo, propertyIndex,
                code);

        this.newLine(code);
    }

    protected void generatePropertyDeleteBody(final JavaProperty property,
            final ClassInfo classInfo, final int propertyIndex,
            final StringBuffer code) {
        code.append(';');
    }

    private void generatePropertyGet(final JavaProperty property,
            final ClassInfo classInfo, final int propertyIndex,
            final StringBuffer code) {
        this.newLine(code);
        this.indent(code);

        this.generateMethodQualifier(code);

        final Class<?> type = property.getJavaType();
        code.append(this.convertType(type, classInfo));

        code.append(' ');
        code.append("get" + property.getName().getLocalPart());
        code.append("()");
        this.newLine(code);
        this.indent(2, code);
        code.append("throws SoapFault");
        this.addImport(SoapFault.class);

        this.generatePropertyGetBody(property, classInfo, propertyIndex, code);

        this.newLine(code);
    }

    protected void generatePropertyGetBody(final JavaProperty property,
            final ClassInfo classInfo, final int propertyIndex,
            final StringBuffer code) {
        code.append(';');
    }

    private void generatePropertyInsert(final JavaProperty property,
            final ClassInfo classInfo, final int propertyIndex,
            final StringBuffer code) {
        this.generatePropertySet(property, classInfo, propertyIndex, "insert",
                code);
    }

    private void generatePropertySet(final JavaProperty property,
            final ClassInfo classInfo, final int propertyIndex,
            final String setType, final StringBuffer code) {
        this.newLine(code);
        this.indent(code);

        this.generateMethodQualifier(code);

        Class<?> type = property.getJavaType();
        if (type.isArray()) {
            type = ReflectUtils.getClassFromArrayClass(type);
        }

        code.append("void ");
        code.append(setType);
        code.append(property.getName().getLocalPart());
        code.append('(');
        code.append(ReflectUtils.getShortName(type));
        code.append(" value)");
        this.newLine(code);
        this.indent(2, code);
        code.append("throws SoapFault");
        this.addImport(SoapFault.class);

        this.generatePropertySetBody(property, classInfo, propertyIndex,
                setType, code);

        this.newLine(code);
    }

    protected void generatePropertySetBody(final JavaProperty property,
            final ClassInfo classInfo, final int propertyIndex,
            final String setType, final StringBuffer code) {
        code.append(';');
    }

    private void generatePropertyUpdate(final JavaProperty property,
            final ClassInfo classInfo, final int propertyIndex,
            final StringBuffer code) {
        this.generatePropertySet(property, classInfo, propertyIndex, "update",
                code);
    }

    private Class<?> getBaseClientClass(final Map capabilityMap) {
        if (capabilityMap.containsKey(WsnConstants.PRODUCER_URI)) {
            return NotificationProducerClient.class;
        } else if (capabilityMap.containsKey(WsnConstants.CONSUMER_URI)) {
            return NotificationConsumerClient.class;
        } else {
            return WsResourceClient.class;
        }
    }

    public ConfigurationDataDescriptor[] getConfigurationDataDescriptions() {
        return ProxyInterfaceSynthesizer.REQUIRED_PARAMETERS;
    }

    private int getIndex() {
        return this._index;
    }

    private void incrementIndex() {
        this._index++;
    }

    protected void initializeCode(final String className) {
        this._className = className;
        this._headerCode = this.beginHeaderCode(this._className);
        this._operationsCode = this.beginOperationsCode();
        this._propertiesCode = this.beginPropertiesCode();
        this._importSet = new HashSet<Class<?>>();
        this._hasProperties = false;
        this.resetIndex();
    }

    protected void loadParameters(final ConfigurationData data) {
        this._generateCustomHeaders = ((Boolean) data
                .getParameter(ConfigurationData.GENERATE_CUSTOM_HEADERS))
                .booleanValue();
        this._capabilityMaps = (Map[]) data
                .getParameter(ConfigurationData.CAPABILITIES_MAP_LIST);
        this._wsdlDocuments = (Document[]) data
                .getParameter(ConfigurationData.WSDL_DOCUMENT_LIST);
        this._filesMaps = (Map[]) data
                .getParameter(ConfigurationData.FILES_MAP_LIST);

        if (this._filesMaps == null) {
            this._filesMaps = new HashMap[this._capabilityMaps.length];
            data
                    .addParameter(ConfigurationData.FILES_MAP_LIST,
                            this._filesMaps);
        }
    }

    private boolean needsGeneratedCode(final Class<?> clientClass,
            final Capability capability) {
        boolean needsGeneratedCode = true;
        final String uri = capability.getURI();
        Class<?> clazz = null;

        if (NotificationProducerClient.class.isAssignableFrom(clientClass)) {
            clazz = NotificationProducerClient.class;
        } else if (NotificationConsumerClient.class
                .isAssignableFrom(clientClass)) {
            clazz = NotificationConsumerClient.class;
        } else { // if(WsResourceClient.class.isAssignableFrom(clientClass))
                    // {
            clazz = WsResourceClient.class;
        }

        final Set<?> set = (Set) ProxyInterfaceSynthesizer._clientCapabilitiesMap
                .get(clazz);
        if (set.contains(uri)
                || ProxyInterfaceSynthesizer._ignoredCapabilitySet
                        .contains(uri)) {
            needsGeneratedCode = false;
        }

        return needsGeneratedCode;
    }

    protected void resetIndex() {
        this._index = 0;
    }

    public ConfigurationData synthesize(final ConfigurationData data)
            throws Exception {
        ConfigurationData.checkConfiguration(this, data);
        this.loadParameters(data);

        for (int i = 0; i < this._capabilityMaps.length; i++) {
            if (this._filesMaps[i] == null) {
                this._filesMaps[i] = new HashMap();
            }

            this.generateCode(this._wsdlDocuments[i], this._capabilityMaps[i],
                    this._filesMaps[i]);
        }

        return data;
    }

    protected void updateCode(final ClassInfo classInfo) {
        final Capability capability = classInfo.getCapability();

        this.updateMethods(classInfo, this._operationsCode);

        if (!capability.getProperties().isEmpty()) {
            this._hasProperties = true;
            this.updateProperties(classInfo, this._propertiesCode);
        }

        classInfo.addImports(this._importSet);
    }

    protected void updateMethods(final ClassInfo classInfo,
            final StringBuffer code) {
        Iterator<?> i = classInfo.getCapability().getOperations().iterator();

        while (i.hasNext()) {
            final JavaMethod method = (JavaMethod) i.next();
            this.generateMethod(method, classInfo, code);
        }

        i = classInfo.getCapability().getProperties().iterator();

        while (i.hasNext()) {
            final JavaProperty property = (JavaProperty) i.next();

            this
                    .generatePropertyGet(property, classInfo, this.getIndex(),
                            code);
            if (property.isAppendable()) {
                this.generatePropertyInsert(property, classInfo, this
                        .getIndex(), code);
            }

            if (property.isMutable()) {
                this.generatePropertyUpdate(property, classInfo, this
                        .getIndex(), code);
                this.generatePropertyDelete(property, classInfo, this
                        .getIndex(), code);
            }

            this.incrementIndex();
        }
    }

    private void updateProperties(final ClassInfo classInfo,
            final StringBuffer code) {
        final Iterator<?> i = classInfo.getCapability().getProperties().iterator();

        if (i.hasNext()) {
            this.addImport(QName.class);
        }

        while (i.hasNext()) {
            this.generatePropertyConstant((JavaProperty) i.next(), code);
        }
    }
}
