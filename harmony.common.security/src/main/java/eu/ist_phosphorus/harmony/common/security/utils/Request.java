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


package eu.ist_phosphorus.harmony.common.security.utils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.muse.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import eu.ist_phosphorus.harmony.common.security.utils.helper.MuseHelper;
import eu.ist_phosphorus.harmony.common.security.utils.helper.TokenHelper;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.Activate;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.AddDomain;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.AddEndpoint;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.AddLink;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.AddOrEditDomain;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.Bind;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CancelJob;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CancelReservation;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CompleteJob;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.ConnectionConstraintType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CreateReservation;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.CreateReservationType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.DeleteDomain;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.DeleteEndpoint;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.DeleteLink;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EditDomain;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EditEndpoint;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EditLink;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EndpointType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetDomains;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetEndpoints;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetLinks;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetReservation;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetReservations;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetStatus;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.IsAvailable;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.ServiceConstraintType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.exceptions.InvalidRequestFaultException;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.exceptions.OperationNotSupportedFaultException;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.utils.JaxbSerializer;
import eu.ist_phosphorus.harmony.common.utils.PhLogger;

public class Request {

    private final Logger log = PhLogger.getLogger();

    private final Object rawRequest;

    /**
     * @param request
     * @throws IOException
     * @throws SAXException
     * @throws InvalidRequestFaultException
     * @throws UnexpectedFaultException
     */
    public Request(final String request) throws IOException, SAXException,
            InvalidRequestFaultException, UnexpectedFaultException {
        this(XmlUtils.createDocument(request));

    }

    /**
     * @param request
     * @throws IOException
     * @throws SAXException
     * @throws InvalidRequestFaultException
     * @throws UnexpectedFaultException
     */
    public Request(final Document request) throws IOException, SAXException,
            InvalidRequestFaultException, UnexpectedFaultException {

        this.rawRequest =
                JaxbSerializer.getInstance().elementToObject(
                        MuseHelper.getData(request));
    }

    /**
     * @return
     */
    public final Object getRawRequest() {
        return this.rawRequest;
    }

    public String getAction() {
        // TODO: Implement getAction, action is always create-path, as long as
        // gaaa-tk cannot handle
        // several actions
        return "create-path";
    }

    public HashMap<String, ArrayList<String>> getEndoints() {
        HashMap<String, ArrayList<String>> endpoints =
                new HashMap<String, ArrayList<String>>();

        if (this.rawRequest instanceof CreateReservation) {
            endpoints =
                    this.getEndpoints(((CreateReservation) this.rawRequest)
                            .getCreateReservation().getService());
        } else if (this.rawRequest instanceof IsAvailable) {
            endpoints =
                    this.getEndpoints(((IsAvailable) this.rawRequest)
                            .getIsAvailable().getService());
        } else if (this.rawRequest instanceof Activate) {
            // TODO: return value?
        } else if (this.rawRequest instanceof CancelReservation) {
            // TODO: return value?
            // user has to submit GRI and Token...
        } else {
            endpoints = null;
        }
        return endpoints;
    }

    private HashMap<String, ArrayList<String>> getEndpoints(
            final List<ServiceConstraintType> sc) {
        final HashMap<String, ArrayList<String>> endpoints =
                new HashMap<String, ArrayList<String>>();
        for (final ServiceConstraintType s : sc) {
            for (final ConnectionConstraintType c : s.getConnections()) {
                final ArrayList<String> targets = new ArrayList<String>();
                for (final EndpointType ep : c.getTarget()) {
                    targets.add(ep.getEndpointId());
                }
                endpoints.put(c.getSource().getEndpointId(), targets);
            }
        }
        return endpoints;
    }

    public final String getGRI() throws IllegalArgumentException,
            IllegalAccessException, InvocationTargetException,
            OperationNotSupportedFaultException {
        try {
            final Object requestType = this.getRequestType();
            log.error("Request type get");
            final Method getGRI =
                    requestType.getClass().getMethod("getReservationID");
            log.error("GRI get");
            return (String) getGRI.invoke(requestType);
        } catch (final NoSuchMethodException nsme) {
            throw new OperationNotSupportedFaultException("The request: "
                    + this.rawRequest.getClass().getCanonicalName()
                    + " contains no GRI!", nsme);
        }
    }

    public long getNotAfter() {
        long notAfter = 0;
        if (this.isCreateReservationRequest()) {
            final CreateReservationType crt =
                    (CreateReservationType) this.getRequestType();
            for (final ServiceConstraintType service : crt.getService()) {
                if (service.getMalleableReservationConstraints() != null) {
                    notAfter =
                            Math.max(notAfter, service
                                    .getMalleableReservationConstraints()
                                    .getDeadline().toGregorianCalendar()
                                    .getTimeInMillis());
                }
                if (service.getFixedReservationConstraints() != null) {
                    notAfter =
                            Math.max(notAfter, service
                                    .getFixedReservationConstraints()
                                    .getStartTime().toGregorianCalendar()
                                    .getTimeInMillis()
                                    + service.getFixedReservationConstraints()
                                            .getDuration() * 1000);
                }
            }
        }
        return notAfter;
    }

    public long getNotBefore() {
        // TODO: This should be set according to the action, e.g. the cancel
        // should be possible from now on, but the
        // Activation should not
        final long notBefore = System.currentTimeMillis();

        // if (isCreateReservationRequest()) {
        // CreateReservationType crt = (CreateReservationType) getRequestType();
        // for (ServiceConstraintType service : crt.getService()) {
        // if (service.getMalleableReservationConstraints()!=null &&
        // service.getMalleableReservationConstraints().isSetStartTime()) {
        // notBefore =
        // service.getMalleableReservationConstraints().getStartTime(
        // ).toGregorianCalendar().getTimeInMillis();
        // }
        // if (service.getFixedReservationConstraints()!=null) {
        // notBefore = service.getFixedReservationConstraints().getStartTime().
        // toGregorianCalendar().getTimeInMillis();
        // }
        // }
        // }
        return notBefore;
    }

    private Object getRequestType() {
        final String name = this.rawRequest.getClass().getSimpleName();
        try {
            final Method getCorrespondingType =
                    this.rawRequest.getClass().getMethod("get" + name);
            return getCorrespondingType.invoke(this.rawRequest);
        } catch (final NoSuchMethodException e) {
            this.log.error("The requestType of request: "
                    + this.rawRequest.getClass().getCanonicalName()
                    + "could not be resolved!", e);
        } catch (final IllegalArgumentException e) {
            this.log.error("The passed arguments are wrong!", e);
        } catch (final IllegalAccessException e) {
            this.log.error("Access denied!", e);
        } catch (final InvocationTargetException e) {
            this.log.error("Invoked method threw an exception: ", e);
        }
        return null;
    }

    /**
     * @return Token, or null
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public String getToken() throws IllegalArgumentException,
            IllegalAccessException, InvocationTargetException {
        return TokenHelper.getToken(this.getRequestType());
    }

    public boolean isCancelRequest() {
        return (this.rawRequest instanceof CancelReservation);
    }

    public boolean isCreateReservationRequest() {
        return (this.rawRequest instanceof CreateReservation);
    }

    public boolean isGetReservationsRequest() {
        return (this.rawRequest instanceof GetReservations);
    }
    
    public boolean isAddOrEditDomainRequest() {
        return (this.rawRequest instanceof AddOrEditDomain);
    }


    public boolean isReservationRequest() {
        return (this.rawRequest instanceof CreateReservation)
                || (this.rawRequest instanceof IsAvailable)
                || (this.rawRequest instanceof CancelReservation)
                || (this.rawRequest instanceof Activate)
                || (this.rawRequest instanceof Bind)
                || (this.rawRequest instanceof GetReservations)
                || (this.rawRequest instanceof GetReservation)
                || (this.rawRequest instanceof GetStatus)
                || (this.rawRequest instanceof CompleteJob)
                || (this.rawRequest instanceof CancelJob);
    }

    public boolean isTopologyRequest() {
        return (this.rawRequest instanceof AddOrEditDomain)
                || (this.rawRequest instanceof AddDomain)
                || (this.rawRequest instanceof EditDomain)
                || (this.rawRequest instanceof DeleteDomain)
                || (this.rawRequest instanceof GetDomains)
                || (this.rawRequest instanceof AddEndpoint)
                || (this.rawRequest instanceof DeleteEndpoint)
                || (this.rawRequest instanceof EditEndpoint)
                || (this.rawRequest instanceof GetEndpoints)
                || (this.rawRequest instanceof AddLink)
                || (this.rawRequest instanceof DeleteLink)
                || (this.rawRequest instanceof EditLink)
                || (this.rawRequest instanceof GetLinks);
    }
    
    
    public static boolean isCreateReservationResponse(Document dom) {
        return (dom.getElementsByTagName("muse-op:createReservationResponse")!=null 
  		&& dom.getElementsByTagName("muse-op:createReservationResponse").getLength()>0);
      	  
      }

    
    
    public static boolean isCancelReservation(Document dom) {
        return (dom.getElementsByTagName("pfx0:cancelReservation")!=null 
  		&& dom.getElementsByTagName("pfx0:cancelReservation").getLength()>0);
      	  
      }

}