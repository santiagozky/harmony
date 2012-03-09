
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
 * 				Type used to return status information for a
 * 				connection.
 * 			
 * 
 * <p>Java class for ConnectionStatusType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ConnectionStatusType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ist_phosphorus.eu/nsp/webservice/reservation}ConnectionType">
 *       &lt;sequence>
 *         &lt;element name="Status" type="{http://ist_phosphorus.eu/nsp/webservice/reservation}StatusType"/>
 *         &lt;element name="DomainStatus" type="{http://ist_phosphorus.eu/nsp/webservice/reservation}DomainConnectionStatusType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ActualBW" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ConnectionStatusType", propOrder = {
    "status",
    "domainStatus",
    "actualBW"
})
public class ConnectionStatusType
    extends ConnectionType
    implements Serializable, Cloneable
{
    public ConnectionStatusType clone() throws CloneNotSupportedException {
        return (ConnectionStatusType)super.clone();
    }

    @XmlElement(name = "Status", required = true)
    protected StatusType status;
    @XmlElement(name = "DomainStatus")
    protected List<DomainConnectionStatusType> domainStatus;
    @XmlElement(name = "ActualBW")
    protected int actualBW;

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
     * {@link DomainConnectionStatusType }
     * 
     * 
     */
    public List<DomainConnectionStatusType> getDomainStatus() {
        if (domainStatus == null) {
            domainStatus = new ArrayList<DomainConnectionStatusType>();
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
     * Gets the value of the actualBW property.
     * 
     */
    public int getActualBW() {
        return actualBW;
    }

    /**
     * Sets the value of the actualBW property.
     * 
     */
    public void setActualBW(int value) {
        this.actualBW = value;
    }

    public boolean isSetActualBW() {
        return true;
    }

}
