
package eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for publishType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="publishType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Topic" type="{http://ist_phosphorus.eu/nsp/webservice/notification}TopicIdentifierType"/>
 *         &lt;element name="NotificationList" type="{http://ist_phosphorus.eu/nsp/webservice/notification}NotificationMessageType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "publishType", namespace = "http://ist_phosphorus.eu/nsp/webservice/notification", propOrder = {
    "topic",
    "notificationList"
})
public class PublishType
    implements Serializable, Cloneable
{
    public PublishType clone() throws CloneNotSupportedException {
        return (PublishType)super.clone();
    }

    @XmlElement(name = "Topic", required = true)
    protected String topic;
    @XmlElement(name = "NotificationList")
    protected List<NotificationMessageType> notificationList;

    /**
     * Gets the value of the topic property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTopic() {
        return topic;
    }

    /**
     * Sets the value of the topic property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTopic(String value) {
        this.topic = value;
    }

    public boolean isSetTopic() {
        return (this.topic!= null);
    }

    /**
     * Gets the value of the notificationList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the notificationList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNotificationList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NotificationMessageType }
     * 
     * 
     */
    public List<NotificationMessageType> getNotificationList() {
        if (notificationList == null) {
            notificationList = new ArrayList<NotificationMessageType>();
        }
        return this.notificationList;
    }

    public boolean isSetNotificationList() {
        return ((this.notificationList!= null)&&(!this.notificationList.isEmpty()));
    }

    public void unsetNotificationList() {
        this.notificationList = null;
    }

}
