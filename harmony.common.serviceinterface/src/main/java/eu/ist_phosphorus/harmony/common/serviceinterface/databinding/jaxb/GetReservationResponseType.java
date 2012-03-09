
package eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetReservationResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetReservationResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Service" type="{http://ist_phosphorus.eu/nsp/webservice/reservation}ServiceConstraintType" maxOccurs="unbounded"/>
 *         &lt;element name="JobID" type="{http://ist_phosphorus.eu/nsp/webservice/reservation}JobIdentifierType" minOccurs="0"/>
 *         &lt;element name="NotificationConsumerURL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetReservationResponseType", propOrder = {
    "service",
    "jobID",
    "notificationConsumerURL"
})
public class GetReservationResponseType
    implements Serializable, Cloneable
{
    public GetReservationResponseType clone() throws CloneNotSupportedException {
        return (GetReservationResponseType)super.clone();
    }

    @XmlElement(name = "Service", required = true)
    protected List<ServiceConstraintType> service;
    @XmlElement(name = "JobID")
    protected Long jobID;
    @XmlElement(name = "NotificationConsumerURL")
    protected String notificationConsumerURL;

    /**
     * Gets the value of the service property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the service property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getService().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ServiceConstraintType }
     * 
     * 
     */
    public List<ServiceConstraintType> getService() {
        if (service == null) {
            service = new ArrayList<ServiceConstraintType>();
        }
        return this.service;
    }

    public boolean isSetService() {
        return ((this.service!= null)&&(!this.service.isEmpty()));
    }

    public void unsetService() {
        this.service = null;
    }

    /**
     * Gets the value of the jobID property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getJobID() {
        return jobID;
    }

    /**
     * Sets the value of the jobID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setJobID(Long value) {
        this.jobID = value;
    }

    public boolean isSetJobID() {
        return (this.jobID!= null);
    }

    /**
     * Gets the value of the notificationConsumerURL property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNotificationConsumerURL() {
        return notificationConsumerURL;
    }

    /**
     * Sets the value of the notificationConsumerURL property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNotificationConsumerURL(String value) {
        this.notificationConsumerURL = value;
    }

    public boolean isSetNotificationConsumerURL() {
        return (this.notificationConsumerURL!= null);
    }

}
