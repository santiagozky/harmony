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

package org.opennaas.extensions.idb.da.argia.implementation;

import java.net.MalformedURLException;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Stub;
import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;
import org.globus.axis.gsi.GSIConstants;
import org.globus.axis.util.Util;
import org.globus.wsrf.impl.security.authorization.NoAuthorization;
import org.globus.wsrf.security.Constants;
import org.ietf.jgss.GSSCredential;

import ca.inocybe.core.stubs.userworkspace.UserWorkspaceFactoryPortType;
import ca.inocybe.core.stubs.userworkspace.UserWorkspacePortType;
import ca.inocybe.core.stubs.userworkspace.UserWorkspaceType;
import ca.inocybe.core.stubs.userworkspace.service.UserWorkspaceFactoryServiceAddressingLocator;
import ca.inocybe.core.stubs.userworkspace.service.UserWorkspaceServiceAddressingLocator;

/**
 * Utility class to wrap calls to the User Management Web Service
 * 
 * @author Scott Campbell
 * 
 */
public class UserWorkspaceWSWrapper {
	static {
		Util.registerTransport();
	}

	private static UserWorkspaceFactoryPortType getFactoryPort(
			final GSSCredential credential, final String host)
			throws MalformedURLException, ServiceException,
			MalformedURIException {
		final String url = "https://" + host
				+ ":8443/wsrf/services/core/UserWorkspaceFactory";
		final UserWorkspaceFactoryServiceAddressingLocator factoryLocator = new UserWorkspaceFactoryServiceAddressingLocator();
		final EndpointReferenceType factoryEPR = new EndpointReferenceType();
		factoryEPR.setAddress(new Address(url));
		final UserWorkspaceFactoryPortType workspaceFactory = factoryLocator
				.getUserWorkspaceFactoryPortTypePort(factoryEPR);
		((Stub) workspaceFactory)._setProperty(Constants.GSI_SEC_CONV,
				org.globus.gsi.GSIConstants.ENCRYPTION);
		((Stub) workspaceFactory)._setProperty(GSIConstants.GSI_CREDENTIALS,
				credential);
		((Stub) workspaceFactory)._setProperty(Constants.AUTHORIZATION,
				NoAuthorization.getInstance());

		return workspaceFactory;
	}

	private static UserWorkspacePortType getInstancePort(
			final EndpointReferenceType epr) throws MalformedURLException,
			ServiceException, MalformedURIException {
		final UserWorkspaceServiceAddressingLocator instanceLocator = new UserWorkspaceServiceAddressingLocator();
		final UserWorkspacePortType workspace = instanceLocator
				.getUserWorkspacePortTypePort(epr);
		((Stub) workspace)._setProperty(Constants.GSI_SEC_CONV,
				org.globus.gsi.GSIConstants.SIGNATURE);
		((Stub) workspace)._setProperty(Constants.GSI_ANONYMOUS, Boolean.TRUE);
		((Stub) workspace)._setProperty(Constants.AUTHORIZATION,
				NoAuthorization.getInstance());
		return workspace;
	}

	public static UserWorkspaceType queryUser(final String userID,
			final String host, final GSSCredential credential)
			throws RemoteException, ServiceException {
		UserWorkspaceFactoryPortType factory = null;

		try {
			factory = UserWorkspaceWSWrapper.getFactoryPort(credential, host);
		} catch (final Exception e) {
			e.printStackTrace();
		}

		final EndpointReferenceType epr = factory.findUserWorkspace(userID);
		System.out.println("Factory EPR: " + epr.toString());

		UserWorkspacePortType portType = null;
		try {
			portType = UserWorkspaceWSWrapper.getInstancePort(epr);
		} catch (final Exception e) {
			System.out.println("Error getting port type");
			e.printStackTrace();
		}

		final UserWorkspaceType response = portType.queryWorkspaceInfo("nouse");

		return response;
	}

}

/*******************************************************************************
 * Copyright (c) 2005,2007 i2CAT Foundation Communications Research Centre
 * Canada, Inocybe Technologies inc.
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Eduard Grasa (i2CAT), Scott Campbell (CRC), and Albert Forns
 * (i2CAT) - initial API and implementation
 ******************************************************************************/
/*
 *//**
     * 
     */
/**
 * Utility class to wrap calls to the User Management Web Service
 * 
 * @author Scott Campbell
 * 
 */
/*
 * public class UserWorkspaceWSWrapper { static { Util.registerTransport(); }
 * 
 * 
 * public static UserWorkspaceType queryUser(String userID, String host,
 * GSSCredential credential) throws RemoteException, ServiceException {
 * UserWorkspaceFactoryPortType factory = null;
 * 
 * try { factory = getFactoryPort(credential, host); } catch (Exception e) {
 * e.printStackTrace(); }
 * 
 * System.out.println("VI A PILLAR EL USER WSPACE: " + userID);
 * EndpointReferenceType epr = factory.findUserWorkspace(userID);
 * System.out.println("Factory EPR: " + epr.toString());
 * 
 * UserWorkspacePortType portType = null; try { portType = getInstancePort(epr);
 * } catch (Exception e) { System.out.println("Error getting port type");
 * e.printStackTrace(); }
 * 
 * UserWorkspaceType response = portType.queryWorkspaceInfo("nouse");
 * 
 * return response; }
 * 
 * 
 * private static UserWorkspaceFactoryPortType getFactoryPort(GSSCredential
 * credential, String host) throws MalformedURLException, ServiceException,
 * MalformedURIException { String url =
 * "https://"+host+":8443/wsrf/services/core/UserWorkspaceFactory";
 * UserWorkspaceFactoryServiceAddressingLocator factoryLocator = new
 * UserWorkspaceFactoryServiceAddressingLocator(); EndpointReferenceType
 * factoryEPR = new EndpointReferenceType(); factoryEPR.setAddress(new
 * Address(url)); UserWorkspaceFactoryPortType workspaceFactory = factoryLocator
 * .getUserWorkspaceFactoryPortTypePort(factoryEPR); ((Stub)
 * workspaceFactory)._setProperty(Constants.GSI_SEC_CONV, Constants.ENCRYPTION);
 * ((Stub) workspaceFactory)._setProperty(GSIConstants.GSI_CREDENTIALS,
 * credential); ((Stub) workspaceFactory)._setProperty(Constants.AUTHORIZATION,
 * NoAuthorization .getInstance());
 * 
 * return workspaceFactory; }
 * 
 * private static UserWorkspacePortType getInstancePort(EndpointReferenceType
 * epr) throws MalformedURLException, ServiceException, MalformedURIException {
 * UserWorkspaceServiceAddressingLocator instanceLocator = new
 * UserWorkspaceServiceAddressingLocator(); UserWorkspacePortType workspace =
 * instanceLocator.getUserWorkspacePortTypePort(epr); ((Stub)
 * workspace)._setProperty(Constants.GSI_SEC_CONV, Constants.SIGNATURE); ((Stub)
 * workspace)._setProperty(Constants.GSI_ANONYMOUS, Boolean.TRUE); ((Stub)
 * workspace)._setProperty(Constants.AUTHORIZATION,
 * NoAuthorization.getInstance()); return workspace; } }
 */
