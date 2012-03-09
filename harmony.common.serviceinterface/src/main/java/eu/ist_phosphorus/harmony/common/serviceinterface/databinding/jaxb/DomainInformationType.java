
package eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DomainInformationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DomainInformationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DomainId" type="{http://ist_phosphorus.eu/nsp}DomainIdentifierType"/>
 *         &lt;element name="Relationship" type="{http://ist_phosphorus.eu/nsp}DomainRelationshipType" minOccurs="0"/>
 *         &lt;element name="SequenceNumber" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ReservationEPR" type="{http://www.w3.org/2001/XMLSchema}anyURI"/>
 *         &lt;element name="TopologyEPR" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *         &lt;element name="NotificationEPR" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *         &lt;element name="TNAPrefix" type="{http://ist_phosphorus.eu/nsp}TNAPrefixType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="InterdomainLink" type="{http://ist_phosphorus.eu/nsp}InterdomainLinkType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="avgDelay" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="maxBW" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="Feature" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Technology" type="{http://ist_phosphorus.eu/nsp}DomainTechnologyType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DomainInformationType", namespace = "http://ist_phosphorus.eu/nsp", propOrder = {
    "domainId",
    "relationship",
    "sequenceNumber",
    "description",
    "reservationEPR",
    "topologyEPR",
    "notificationEPR",
    "tnaPrefix",
    "interdomainLink",
    "avgDelay",
    "maxBW",
    "feature",
    "technology"
})
public class DomainInformationType
    implements Serializable, Cloneable
{
    public DomainInformationType clone() throws CloneNotSupportedException {
        return (DomainInformationType)super.clone();
    }

    @XmlElement(name = "DomainId", required = true)
    protected String domainId;
    @XmlElement(name = "Relationship")
    protected DomainRelationshipType relationship;
    @XmlElement(name = "SequenceNumber")
    protected Integer sequenceNumber;
    @XmlElement(name = "Description")
    protected String description;
    @XmlElement(name = "ReservationEPR", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String reservationEPR;
    @XmlElement(name = "TopologyEPR")
    @XmlSchemaType(name = "anyURI")
    protected String topologyEPR;
    @XmlElement(name = "NotificationEPR")
    @XmlSchemaType(name = "anyURI")
    protected String notificationEPR;
    @XmlElement(name = "TNAPrefix")
    protected List<String> tnaPrefix;
    @XmlElement(name = "InterdomainLink")
    protected List<InterdomainLinkType> interdomainLink;
    protected Integer avgDelay;
    protected Integer maxBW;
    @XmlElement(name = "Feature")
    protected List<String> feature;
    @XmlElement(name = "Technology")
    protected DomainTechnologyType technology;

    /**
     * Gets the value of the domainId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDomainId() {
        return domainId;
    }

    /**
     * Sets the value of the domainId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDomainId(String value) {
        this.domainId = value;
    }

    public boolean isSetDomainId() {
        return (this.domainId!= null);
    }

    /**
     * Gets the value of the relationship property.
     * 
     * @return
     *     possible object is
     *     {@link DomainRelationshipType }
     *     
     */
    public DomainRelationshipType getRelationship() {
        return relationship;
    }

    /**
     * Sets the value of the relationship property.
     * 
     * @param value
     *     allowed object is
     *     {@link DomainRelationshipType }
     *     
     */
    public void setRelationship(DomainRelationshipType value) {
        this.relationship = value;
    }

    public boolean isSetRelationship() {
        return (this.relationship!= null);
    }

    /**
     * Gets the value of the sequenceNumber property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * Sets the value of the sequenceNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSequenceNumber(Integer value) {
        this.sequenceNumber = value;
    }

    public boolean isSetSequenceNumber() {
        return (this.sequenceNumber!= null);
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    public boolean isSetDescription() {
        return (this.description!= null);
    }

    /**
     * Gets the value of the reservationEPR property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReservationEPR() {
        return reservationEPR;
    }

    /**
     * Sets the value of the reservationEPR property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReservationEPR(String value) {
        this.reservationEPR = value;
    }

    public boolean isSetReservationEPR() {
        return (this.reservationEPR!= null);
    }

    /**
     * Gets the value of the topologyEPR property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTopologyEPR() {
        return topologyEPR;
    }

    /**
     * Sets the value of the topologyEPR property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTopologyEPR(String value) {
        this.topologyEPR = value;
    }

    public boolean isSetTopologyEPR() {
        return (this.topologyEPR!= null);
    }

    /**
     * Gets the value of the notificationEPR property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNotificationEPR() {
        return notificationEPR;
    }

    /**
     * Sets the value of the notificationEPR property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNotificationEPR(String value) {
        this.notificationEPR = value;
    }

    public boolean isSetNotificationEPR() {
        return (this.notificationEPR!= null);
    }

    /**
     * Gets the value of the tnaPrefix property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the tnaPrefix property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTNAPrefix().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getTNAPrefix() {
        if (tnaPrefix == null) {
            tnaPrefix = new ArrayList<String>();
        }
        return this.tnaPrefix;
    }

    public boolean isSetTNAPrefix() {
        return ((this.tnaPrefix!= null)&&(!this.tnaPrefix.isEmpty()));
    }

    public void unsetTNAPrefix() {
        this.tnaPrefix = null;
    }

    /**
     * Gets the value of the interdomainLink property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the interdomainLink property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInterdomainLink().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InterdomainLinkType }
     * 
     * 
     */
    public List<InterdomainLinkType> getInterdomainLink() {
        if (interdomainLink == null) {
            interdomainLink = new ArrayList<InterdomainLinkType>();
        }
        return this.interdomainLink;
    }

    public boolean isSetInterdomainLink() {
        return ((this.interdomainLink!= null)&&(!this.interdomainLink.isEmpty()));
    }

    public void unsetInterdomainLink() {
        this.interdomainLink = null;
    }

    /**
     * Gets the value of the avgDelay property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAvgDelay() {
        return avgDelay;
    }

    /**
     * Sets the value of the avgDelay property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAvgDelay(Integer value) {
        this.avgDelay = value;
    }

    public boolean isSetAvgDelay() {
        return (this.avgDelay!= null);
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

    /**
     * Gets the value of the feature property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the feature property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFeature().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getFeature() {
        if (feature == null) {
            feature = new ArrayList<String>();
        }
        return this.feature;
    }

    public boolean isSetFeature() {
        return ((this.feature!= null)&&(!this.feature.isEmpty()));
    }

    public void unsetFeature() {
        this.feature = null;
    }

    /**
     * Gets the value of the technology property.
     * 
     * @return
     *     possible object is
     *     {@link DomainTechnologyType }
     *     
     */
    public DomainTechnologyType getTechnology() {
        return technology;
    }

    /**
     * Sets the value of the technology property.
     * 
     * @param value
     *     allowed object is
     *     {@link DomainTechnologyType }
     *     
     */
    public void setTechnology(DomainTechnologyType value) {
        this.technology = value;
    }

    public boolean isSetTechnology() {
        return (this.technology!= null);
    }

}