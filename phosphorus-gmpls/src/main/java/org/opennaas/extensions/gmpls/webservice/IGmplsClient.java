package org.opennaas.extensions.gmpls.webservice;

import org.w3c.dom.Element;
import org.apache.muse.ws.addressing.soap.SoapFault;

/**
 * gmpls client.
 *
 * IGmplsClient.java
 * Thu Oct 09 15:04:36 CEST 2008
 * Generated by the Apache Muse Code Generation Tool
 */
 public interface IGmplsClient {


    /**
     * createPath Handler.
     *
     * @param createPath Request
     * @return createPath Response
     * @throws SoapFault In case of errors
     */
     Element createPath (Element createPath)
            throws SoapFault;


    /**
     * getPathStatus Handler.
     *
     * @param getPathStatus Request
     * @return getPathStatus Response
     * @throws SoapFault In case of errors
     */
     Element getPathStatus (Element getPathStatus)
            throws SoapFault;


    /**
     * getPathDiscovery Handler.
     *
     * @param getPathDiscovery Request
     * @return getPathDiscovery Response
     * @throws SoapFault In case of errors
     */
     Element getPathDiscovery (Element getPathDiscovery)
            throws SoapFault;


    /**
     * terminatePath Handler.
     *
     * @param terminatePath Request
     * @return terminatePath Response
     * @throws SoapFault In case of errors
     */
     Element terminatePath (Element terminatePath)
            throws SoapFault;


    /**
     * getEndpointDiscovery Handler.
     *
     * @param getEndpointDiscovery Request
     * @return getEndpointDiscovery Response
     * @throws SoapFault In case of errors
     */
     Element getEndpointDiscovery (Element getEndpointDiscovery)
            throws SoapFault;


}