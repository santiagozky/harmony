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


package client.classes.nsp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Stores status information for a service
 * 
 * 
 * <p>
 * Java class for ServiceStatusType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;ServiceStatusType&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;ServiceID&quot; type=&quot;{http://ist_phosphorus.eu/nsp/webservice/reservation}ServiceIdentifierType&quot;/&gt;
 *         &lt;element name=&quot;Status&quot; type=&quot;{http://ist_phosphorus.eu/nsp/webservice/reservation}StatusType&quot;/&gt;
 *         &lt;element name=&quot;DomainStatus&quot; type=&quot;{http://ist_phosphorus.eu/nsp/webservice/reservation}DomainStatusType&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;Connections&quot; type=&quot;{http://ist_phosphorus.eu/nsp/webservice/reservation}ConnectionStatusType&quot; maxOccurs=&quot;unbounded&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
public class ServiceStatusType implements Serializable, Cloneable {

    /**
     *
     */
    private static final long serialVersionUID = 4322148541111931165L;
    protected int serviceID;
    protected StatusType status;
    protected List<client.classes.nsp.DomainStatusType> domainStatus;
    protected List<client.classes.nsp.ConnectionStatusType> connections;

    /**
     * Gets the value of the serviceID property.
     * 
     */
    public int getServiceID() {
        return this.serviceID;
    }

    /**
     * Sets the value of the serviceID property.
     * 
     */
    public void setServiceID(final int value) {
        this.serviceID = value;
    }

    public boolean isSetServiceID() {
        return true;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return possible object is {@link StatusType }
     * 
     */
    public StatusType getStatus() {
        return this.status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *            allowed object is {@link StatusType }
     * 
     */
    public void setStatus(final StatusType value) {
        this.status = value;
    }

    public boolean isSetStatus() {
        return (this.status != null);
    }

    /**
     * Gets the value of the domainStatus property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the domainStatus property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getDomainStatus().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DomainStatusType }
     * 
     * 
     */
    public List<client.classes.nsp.DomainStatusType> getDomainStatus() {
        if (this.domainStatus == null) {
            this.domainStatus =
                    new ArrayList<client.classes.nsp.DomainStatusType>();
        }
        return this.domainStatus;
    }

    public boolean isSetDomainStatus() {
        return ((this.domainStatus != null) && (!this.domainStatus.isEmpty()));
    }

    public void unsetDomainStatus() {
        this.domainStatus = null;
    }

    /**
     * Gets the value of the connections property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the connections property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getConnections().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ConnectionStatusType }
     * 
     * 
     */
    public List<client.classes.nsp.ConnectionStatusType> getConnections() {
        if (this.connections == null) {
            this.connections =
                    new ArrayList<client.classes.nsp.ConnectionStatusType>();
        }
        return this.connections;
    }

    public boolean isSetConnections() {
        return ((this.connections != null) && (!this.connections.isEmpty()));
    }

    public void unsetConnections() {
        this.connections = null;
    }

}
