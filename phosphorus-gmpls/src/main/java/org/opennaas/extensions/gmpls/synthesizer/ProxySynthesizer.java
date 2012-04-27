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

import org.apache.muse.tools.generator.synthesizer.Synthesizer;
import org.apache.muse.tools.generator.util.ConfigurationData;
import org.apache.muse.tools.generator.util.ConfigurationDataDescriptor;

/**
 * 
 * ProxySynthesizer is a tool for creating web service proxies, with extra
 * support for WS-Addressing and manageability interfaces. It can create a
 * remote interface and implementation class from a WSDL that describes a
 * resource type. The tool can be run from the command line or invoked directly
 * from Java code.
 * 
 * @author Dan Jemiolo (danj)
 * @author Andrew Eberbach (aeberbac)
 * 
 */
public class ProxySynthesizer implements Synthesizer {
    static ConfigurationDataDescriptor[] REQUIRED_PARAMETERS = new ConfigurationDataDescriptor[] { ConfigurationData.GENERATE_CUSTOM_HEADERS_CONFIGURATION };

    public ConfigurationDataDescriptor[] getConfigurationDataDescriptions() {
        return ProxySynthesizer.REQUIRED_PARAMETERS;
    }

    public ConfigurationData synthesize(ConfigurationData data)
            throws Exception {
        final ProxyInterfaceSynthesizer proxyInterfaceSynthesizer = new ProxyInterfaceSynthesizer();
        final ProxyClassSynthesizer proxyClassSynthesizer = new ProxyClassSynthesizer();

        data = proxyInterfaceSynthesizer.synthesize(data);
        data = proxyClassSynthesizer.synthesize(data);

        return data;
    }
}
