
package eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 				Stores the availability of a connection
 * 			
 * 
 * <p>Java class for ConnectionAvailabilityType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ConnectionAvailabilityType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ServiceID" type="{http://ist_phosphorus.eu/nsp/webservice/reservation}ServiceIdentifierType"/>
 *         &lt;element name="ConnectionID" type="{http://ist_phosphorus.eu/nsp/webservice/reservation}ConnectionIdentifierType"/>
 *         &lt;element name="Availability" type="{http://ist_phosphorus.eu/nsp/webservice/reservation}AvailabilityCodeType"/>
 *         &lt;element name="BlockedEndpoints" type="{http://ist_phosphorus.eu/nsp}EndpointIdentifierType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="MaxBW" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ConnectionAvailabilityType", propOrder = {
    "serviceID",
    "connectionID",
    "availability",
    "blockedEndpoints",
    "maxBW"
})
public class ConnectionAvailabilityType
    implements Serializable, Cloneable
{
    public ConnectionAvailabilityType clone() throws CloneNotSupportedException {
        return (ConnectionAvailabilityType)super.clone();
    }

    @XmlElement(name = "ServiceID")
    protected int serviceID;
    @XmlElement(name = "ConnectionID")
    protected int connectionID;
    @XmlElement(name = "Availability", required = true)
    protected AvailabilityCodeType availability;
    @XmlElement(name = "BlockedEndpoints")
    protected List<String> blockedEndpoints;
    @XmlElement(name = "MaxBW")
    protected Integer maxBW;

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
     * Gets the value of the connectionID property.
     * 
     */
    public int getConnectionID() {
        return connectionID;
    }

    /**
     * Sets the value of the connectionID property.
     * 
     */
    public void setConnectionID(int value) {
        this.connectionID = value;
    }

    public boolean isSetConnectionID() {
        return true;
    }

    /**
     * Gets the value of the availability property.
     * 
     * @return
     *     possible object is
     *     {@link AvailabilityCodeType }
     *     
     */
    public AvailabilityCodeType getAvailability() {
        return availability;
    }

    /**
     * Sets the value of the availability property.
     * 
     * @param value
     *     allowed object is
     *     {@link AvailabilityCodeType }
     *     
     */
    public void setAvailability(AvailabilityCodeType value) {
        this.availability = value;
    }

    public boolean isSetAvailability() {
        return (this.availability!= null);
    }

    /**
     * Gets the value of the blockedEndpoints property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the blockedEndpoints property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBlockedEndpoints().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getBlockedEndpoints() {
        if (blockedEndpoints == null) {
            blockedEndpoints = new ArrayList<String>();
        }
        return this.blockedEndpoints;
    }

    public boolean isSetBlockedEndpoints() {
        return ((this.blockedEndpoints!= null)&&(!this.blockedEndpoints.isEmpty()));
    }

    public void unsetBlockedEndpoints() {
        this.blockedEndpoints = null;
    }

    /**
     * Gets the value of the maxBW property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMaxBW() {
        return maxBW;
    }

    /**
     * Sets the value of the maxBW property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMaxBW(Integer value) {
        this.maxBW = value;
    }

    public boolean isSetMaxBW() {
        return (this.maxBW!= null);
    }

}
