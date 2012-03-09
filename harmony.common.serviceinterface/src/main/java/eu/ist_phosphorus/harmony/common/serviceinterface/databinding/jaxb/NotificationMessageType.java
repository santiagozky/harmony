
package eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NotificationMessageType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NotificationMessageType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ServiceStatus" type="{http://ist_phosphorus.eu/nsp/webservice/reservation}ServiceStatusType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NotificationMessageType", namespace = "http://ist_phosphorus.eu/nsp/webservice/notification", propOrder = {
    "serviceStatus"
})
public class NotificationMessageType
    implements Serializable, Cloneable
{
    public NotificationMessageType clone() throws CloneNotSupportedException {
        return (NotificationMessageType)super.clone();
    }

    @XmlElement(name = "ServiceStatus")
    protected ServiceStatusType serviceStatus;

    /**
     * Gets the value of the serviceStatus property.
     * 
     * @return
     *     possible object is
     *     {@link ServiceStatusType }
     *     
     */
    public ServiceStatusType getServiceStatus() {
        return serviceStatus;
    }

    /**
     * Sets the value of the serviceStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServiceStatusType }
     *     
     */
    public void setServiceStatus(ServiceStatusType value) {
        this.serviceStatus = value;
    }

    public boolean isSetServiceStatus() {
        return (this.serviceStatus!= null);
    }

}
