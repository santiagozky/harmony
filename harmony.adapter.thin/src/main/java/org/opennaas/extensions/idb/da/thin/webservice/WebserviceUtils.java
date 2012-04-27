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


package org.opennaas.extensions.idb.da.thin.webservice;

import java.util.ArrayList;
import java.util.List;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointInterfaceType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointType;

public class WebserviceUtils {

    public static List<EndpointType> convert(
	    List<org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.EndpointType> gmplsEndpointList) {
	List<EndpointType> harmonyEndpoints = new ArrayList<EndpointType>();
	for (org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.EndpointType gmplsEndpointType : gmplsEndpointList) {
	    harmonyEndpoints.add(convert(gmplsEndpointType));
	}

	return harmonyEndpoints;
    }

    public static EndpointType convert(
	    org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.EndpointType gmplsEndpoint) {
	EndpointType harmonyEndpoint = new EndpointType();
	harmonyEndpoint.setBandwidth(gmplsEndpoint.getBandwidth());
	harmonyEndpoint.setDescription(gmplsEndpoint.getDescription());
	harmonyEndpoint.setDomainId(gmplsEndpoint.getDomainId());
	harmonyEndpoint.setEndpointId(gmplsEndpoint.getEndpointId());
	harmonyEndpoint.setInterface(convert(gmplsEndpoint.getInterface()));
	harmonyEndpoint.setName(gmplsEndpoint.getName());
	harmonyEndpoint.setTechnology(null);
	return harmonyEndpoint;
    }

    public static EndpointInterfaceType convert(
	    org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.EndpointInterfaceType gmplsEndpointInterfaceType) {
	switch (gmplsEndpointInterfaceType) {
	case BORDER:
	    return EndpointInterfaceType.BORDER;
	case USER:
	    return EndpointInterfaceType.USER;
	default:
	    return EndpointInterfaceType.USER;

	}
    }
}
