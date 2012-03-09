
package eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CreateReservationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CreateReservationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Service" type="{http://ist_phosphorus.eu/nsp/webservice/reservation}ServiceConstraintType" maxOccurs="unbounded"/>
 *         &lt;element name="JobID" type="{http://ist_phosphorus.eu/nsp/webservice/reservation}JobIdentifierType" minOccurs="0"/>
 *         &lt;element name="NotificationConsumerURL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SubdomainRestriction" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="GRI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Token" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SamlAssertion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Trunk" type="{http://ist_phosphorus.eu/nsp}Tuple" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CreateReservationType", propOrder = {
    "service",
    "jobID",
    "notificationConsumerURL",
    "subdomainRestriction",
    "gri",
    "token",
    "samlAssertion",
    "trunk"
})
public class CreateReservationType
    implements Serializable, Cloneable
{
    public CreateReservationType clone() throws CloneNotSupportedException {
        return (CreateReservationType)super.clone();
    }

    @XmlElement(name = "Service", required = true)
    protected List<ServiceConstraintType> service;
    @XmlElement(name = "JobID")
    protected Long jobID;
    @XmlElement(name = "NotificationConsumerURL")
    protected String notificationConsumerURL;
    @XmlElement(name = "SubdomainRestriction")
    protected Boolean subdomainRestriction;
    @XmlElement(name = "GRI")
    protected String gri;
    @XmlElement(name = "Token")
    protected String token;
    @XmlElement(name = "SamlAssertion")
    protected String samlAssertion;
    @XmlElement(name = "Trunk")
    protected List<Tuple> trunk;

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

    /**
     * Gets the value of the subdomainRestriction property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSubdomainRestriction() {
        return subdomainRestriction;
    }

    /**
     * Sets the value of the subdomainRestriction property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSubdomainRestriction(Boolean value) {
        this.subdomainRestriction = value;
    }

    public boolean isSetSubdomainRestriction() {
        return (this.subdomainRestriction!= null);
    }

    /**
     * Gets the value of the gri property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGRI() {
        return gri;
    }

    /**
     * Sets the value of the gri property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGRI(String value) {
        this.gri = value;
    }

    public boolean isSetGRI() {
        return (this.gri!= null);
    }

    /**
     * Gets the value of the token property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the value of the token property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setToken(String value) {
        this.token = value;
    }

    public boolean isSetToken() {
        return (this.token!= null);
    }

    /**
     * Gets the value of the samlAssertion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSamlAssertion() {
        return samlAssertion;
    }

    /**
     * Sets the value of the samlAssertion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSamlAssertion(String value) {
        this.samlAssertion = value;
    }

    public boolean isSetSamlAssertion() {
        return (this.samlAssertion!= null);
    }

    /**
     * Gets the value of the trunk property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the trunk property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTrunk().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Tuple }
     * 
     * 
     */
    public List<Tuple> getTrunk() {
        if (trunk == null) {
            trunk = new ArrayList<Tuple>();
        }
        return this.trunk;
    }

    public boolean isSetTrunk() {
        return ((this.trunk!= null)&&(!this.trunk.isEmpty()));
    }

    public void unsetTrunk() {
        this.trunk = null;
    }

}
