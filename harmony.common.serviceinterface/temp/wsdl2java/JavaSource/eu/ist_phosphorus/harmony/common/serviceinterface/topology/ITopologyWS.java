package eu.ist_phosphorus.harmony.common.serviceinterface.topology;

import org.w3c.dom.Element;

/**
 * topology server.
 *
 * ITopologyWS.java
 * Fri Mar 09 16:39:27 CET 2012
 * Generated by the Apache Muse Code Generation Tool
 */
 public interface ITopologyWS {
    String PREFIX = "tns";

    String NAMESPACE_URI = "http://ist_phosphorus.eu/nsp/webservice/topology";

    /**
     * addEndpoint Handler.
     *
     * @param addEndpoint Request
     * @return addEndpoint Response
     * @throws Exception In case of errors
     */
    public Element addEndpoint (Element addEndpoint)
            throws Exception;

    /**
     * deleteDomain Handler.
     *
     * @param deleteDomain Request
     * @return deleteDomain Response
     * @throws Exception In case of errors
     */
    public Element deleteDomain (Element deleteDomain)
            throws Exception;

    /**
     * addLink Handler.
     *
     * @param addLink Request
     * @return addLink Response
     * @throws Exception In case of errors
     */
    public Element addLink (Element addLink)
            throws Exception;

    /**
     * editEndpoint Handler.
     *
     * @param editEndpoint Request
     * @return editEndpoint Response
     * @throws Exception In case of errors
     */
    public Element editEndpoint (Element editEndpoint)
            throws Exception;

    /**
     * getLinks Handler.
     *
     * @param getLinks Request
     * @return getLinks Response
     * @throws Exception In case of errors
     */
    public Element getLinks (Element getLinks)
            throws Exception;

    /**
     * editDomain Handler.
     *
     * @param editDomain Request
     * @return editDomain Response
     * @throws Exception In case of errors
     */
    public Element editDomain (Element editDomain)
            throws Exception;

    /**
     * getEndpoints Handler.
     *
     * @param getEndpoints Request
     * @return getEndpoints Response
     * @throws Exception In case of errors
     */
    public Element getEndpoints (Element getEndpoints)
            throws Exception;

    /**
     * deleteEndpoint Handler.
     *
     * @param deleteEndpoint Request
     * @return deleteEndpoint Response
     * @throws Exception In case of errors
     */
    public Element deleteEndpoint (Element deleteEndpoint)
            throws Exception;

    /**
     * addDomain Handler.
     *
     * @param addDomain Request
     * @return addDomain Response
     * @throws Exception In case of errors
     */
    public Element addDomain (Element addDomain)
            throws Exception;

    /**
     * getDomains Handler.
     *
     * @param getDomains Request
     * @return getDomains Response
     * @throws Exception In case of errors
     */
    public Element getDomains (Element getDomains)
            throws Exception;

    /**
     * deleteLink Handler.
     *
     * @param deleteLink Request
     * @return deleteLink Response
     * @throws Exception In case of errors
     */
    public Element deleteLink (Element deleteLink)
            throws Exception;

    /**
     * addOrEditDomain Handler.
     *
     * @param addOrEditDomain Request
     * @return addOrEditDomain Response
     * @throws Exception In case of errors
     */
    public Element addOrEditDomain (Element addOrEditDomain)
            throws Exception;

    /**
     * editLink Handler.
     *
     * @param editLink Request
     * @return editLink Response
     * @throws Exception In case of errors
     */
    public Element editLink (Element editLink)
            throws Exception;

}