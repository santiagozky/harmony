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

package org.opennaas.extensions.idb.serviceinterface.synthesizer;

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

    public ConfigurationData synthesize(final ConfigurationData data)
            throws Exception {
        final ProxyInterfaceSynthesizer proxyInterfaceSynthesizer = new ProxyInterfaceSynthesizer();
        final ProxyClassSynthesizer proxyClassSynthesizer = new ProxyClassSynthesizer();

        ConfigurationData result = proxyInterfaceSynthesizer.synthesize(data);
        result = proxyClassSynthesizer.synthesize(result);

        return result;
    }
}
