
package eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 				Stores status information for a service
 * 			
 * 
 * <p>Java class for ServiceStatusType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ServiceStatusType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ServiceID" type="{http://ist_phosphorus.eu/nsp/webservice/reservation}ServiceIdentifierType"/>
 *         &lt;element name="Status" type="{http://ist_phosphorus.eu/nsp/webservice/reservation}StatusType"/>
 *         &lt;element name="DomainStatus" type="{http://ist_phosphorus.eu/nsp/webservice/reservation}DomainStatusType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Connections" type="{http://ist_phosphorus.eu/nsp/webservice/reservation}ConnectionStatusType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ServiceStatusType", propOrder = {
    "serviceID",
    "status",
    "domainStatus",
    "connections"
})
@XmlSeeAlso({
    eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.GetStatusResponseType.ServiceStatus.class
})
public class ServiceStatusType
    implements Serializable, Cloneable
{
    public ServiceStatusType clone() throws CloneNotSupportedException {
        return (ServiceStatusType)super.clone();
    }

    @XmlElement(name = "ServiceID")
    protected int serviceID;
    @XmlElement(name = "Status", required = true)
    protected StatusType status;
    @XmlElement(name = "DomainStatus")
    protected List<DomainStatusType> domainStatus;
    @XmlElement(name = "Connections", required = true)
    protected List<ConnectionStatusType> connections;

    /**
     * Gets the value of the serviceID property.
     * 
     */
    public int getServiceID() {
        return serviceID;
    }

    /**
     * Sets the value of the serviceID property.
     * 
     */
    public void setServiceID(int value) {
        this.serviceID = value;
    }

    public boolean isSetServiceID() {
        return true;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link StatusType }
     *     
     */
    public StatusType getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link StatusType }
     *     
     */
    public void setStatus(StatusType value) {
        this.status = value;
    }

    public boolean isSetStatus() {
        return (this.status!= null);
    }

    /**
     * Gets the value of the domainStatus property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the domainStatus property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDomainStatus().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DomainStatusType }
     * 
     * 
     */
    public List<DomainStatusType> getDomainStatus() {
        if (domainStatus == null) {
            domainStatus = new ArrayList<DomainStatusType>();
        }
        return this.domainStatus;
    }

    public boolean isSetDomainStatus() {
        return ((this.domainStatus!= null)&&(!this.domainStatus.isEmpty()));
    }

    public void unsetDomainStatus() {
        this.domainStatus = null;
    }

    /**
     * Gets the value of the connections property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the connections property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getConnections().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ConnectionStatusType }
     * 
     * 
     */
    public List<ConnectionStatusType> getConnections() {
        if (connections == null) {
            connections = new ArrayList<ConnectionStatusType>();
        }
        return this.connections;
    }

    public boolean isSetConnections() {
        return ((this.connections!= null)&&(!this.connections.isEmpty()));
    }

    public void unsetConnections() {
        this.connections = null;
    }

}
