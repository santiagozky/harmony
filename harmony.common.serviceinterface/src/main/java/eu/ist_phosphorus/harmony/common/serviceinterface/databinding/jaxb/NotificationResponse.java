
package eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="notificationResponse" type="{http://ist_phosphorus.eu/nsp/webservice/notification}notificationResponseType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "notificationResponse"
})
@XmlRootElement(name = "notificationResponse")
public class NotificationResponse
    implements Serializable, Cloneable
{
    public NotificationResponse clone() throws CloneNotSupportedException {
        return (NotificationResponse)super.clone();
    }

    @XmlElement(required = true)
    protected NotificationResponseType notificationResponse;

    /**
     * Gets the value of the notificationResponse property.
     * 
     * @return
     *     possible object is
     *     {@link NotificationResponseType }
     *     
     */
    public NotificationResponseType getNotificationResponse() {
        return notificationResponse;
    }

    /**
     * Sets the value of the notificationResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link NotificationResponseType }
     *     
     */
    public void setNotificationResponse(NotificationResponseType value) {
        this.notificationResponse = value;
    }

    public boolean isSetNotificationResponse() {
        return (this.notificationResponse!= null);
    }

}
