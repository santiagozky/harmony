package org.opennaas.extensions.gmpls.webservice;

import org.w3c.dom.Element;

/**
 * reservation server.
 *
 * INotificationWS.java
 * Thu Oct 09 15:04:33 CEST 2008
 * Generated by the Apache Muse Code Generation Tool
 */
 public interface INotificationWS {
    String PREFIX = "tns";

    String NAMESPACE_URI = "http://ist_phosphorus.eu/nsp/webservice/notification";

    /**
     * removeTopic Handler.
     *
     * @param removeTopic Request
     * @return removeTopic Response
     * @throws Exception In case of errors
     */
    public Element removeTopic (Element removeTopic)
            throws Exception;

    /**
     * unsubscribe Handler.
     *
     * @param unsubscribe Request
     * @return unsubscribe Response
     * @throws Exception In case of errors
     */
    public Element unsubscribe (Element unsubscribe)
            throws Exception;

    /**
     * subscribe Handler.
     *
     * @param subscribe Request
     * @return subscribe Response
     * @throws Exception In case of errors
     */
    public Element subscribe (Element subscribe)
            throws Exception;

    /**
     * publish Handler.
     *
     * @param publish Request
     * @return publish Response
     * @throws Exception In case of errors
     */
    public Element publish (Element publish)
            throws Exception;

    /**
     * getTopics Handler.
     *
     * @param getTopics Request
     * @return getTopics Response
     * @throws Exception In case of errors
     */
    public Element getTopics (Element getTopics)
            throws Exception;

    /**
     * addTopic Handler.
     *
     * @param addTopic Request
     * @return addTopic Response
     * @throws Exception In case of errors
     */
    public Element addTopic (Element addTopic)
            throws Exception;

}