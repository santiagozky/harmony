
package eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 				Information about an endpoint
 * 			
 * 
 * <p>Java class for EndpointType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EndpointType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="EndpointId" type="{http://ist_phosphorus.eu/nsp}EndpointIdentifierType"/>
 *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Interface" type="{http://ist_phosphorus.eu/nsp}EndpointInterfaceType" minOccurs="0"/>
 *         &lt;element name="DomainId" type="{http://ist_phosphorus.eu/nsp}DomainIdentifierType" minOccurs="0"/>
 *         &lt;element name="Bandwidth" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="Technology" type="{http://ist_phosphorus.eu/nsp}EndpointTechnologyType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EndpointType", namespace = "http://ist_phosphorus.eu/nsp", propOrder = {
    "endpointId",
    "name",
    "description",
    "_interface",
    "domainId",
    "bandwidth",
    "technology"
})
public class EndpointType
    implements Serializable, Cloneable
{
    public EndpointType clone() throws CloneNotSupportedException {
        return (EndpointType)super.clone();
    }

    @XmlElement(name = "EndpointId", required = true)
    protected String endpointId;
    @XmlElement(name = "Name")
    protected String name;
    @XmlElement(name = "Description")
    protected String description;
    @XmlElement(name = "Interface")
    protected EndpointInterfaceType _interface;
    @XmlElement(name = "DomainId")
    protected String domainId;
    @XmlElement(name = "Bandwidth")
    protected Integer bandwidth;
    @XmlElement(name = "Technology")
    protected EndpointTechnologyType technology;

    /**
     * Gets the value of the endpointId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEndpointId() {
        return endpointId;
    }

    /**
     * Sets the value of the endpointId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEndpointId(String value) {
        this.endpointId = value;
    }

    public boolean isSetEndpointId() {
        return (this.endpointId!= null);
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    public boolean isSetName() {
        return (this.name!= null);
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
     * Gets the value of the interface property.
     * 
     * @return
     *     possible object is
     *     {@link EndpointInterfaceType }
     *     
     */
    public EndpointInterfaceType getInterface() {
        return _interface;
    }

    /**
     * Sets the value of the interface property.
     * 
     * @param value
     *     allowed object is
     *     {@link EndpointInterfaceType }
     *     
     */
    public void setInterface(EndpointInterfaceType value) {
        this._interface = value;
    }

    public boolean isSetInterface() {
        return (this._interface!= null);
    }

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
     * Gets the value of the bandwidth property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getBandwidth() {
        return bandwidth;
    }

    /**
     * Sets the value of the bandwidth property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setBandwidth(Integer value) {
        this.bandwidth = value;
    }

    public boolean isSetBandwidth() {
        return (this.bandwidth!= null);
    }

    /**
     * Gets the value of the technology property.
     * 
     * @return
     *     possible object is
     *     {@link EndpointTechnologyType }
     *     
     */
    public EndpointTechnologyType getTechnology() {
        return technology;
    }

    /**
     * Sets the value of the technology property.
     * 
     * @param value
     *     allowed object is
     *     {@link EndpointTechnologyType }
     *     
     */
    public void setTechnology(EndpointTechnologyType value) {
        this.technology = value;
    }

    public boolean isSetTechnology() {
        return (this.technology!= null);
    }

}
