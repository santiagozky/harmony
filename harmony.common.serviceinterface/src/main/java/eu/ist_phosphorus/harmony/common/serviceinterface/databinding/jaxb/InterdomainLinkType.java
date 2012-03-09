
package eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for InterdomainLinkType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="InterdomainLinkType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="LinkID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SourceEndpoint" type="{http://ist_phosphorus.eu/nsp}EndpointType"/>
 *         &lt;element name="DestinationDomain" type="{http://ist_phosphorus.eu/nsp}DomainIdentifierType"/>
 *         &lt;element name="costs" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InterdomainLinkType", namespace = "http://ist_phosphorus.eu/nsp", propOrder = {
    "linkID",
    "sourceEndpoint",
    "destinationDomain",
    "costs"
})
public class InterdomainLinkType
    implements Serializable, Cloneable
{
    public InterdomainLinkType clone() throws CloneNotSupportedException {
        return (InterdomainLinkType)super.clone();
    }

    @XmlElement(name = "LinkID", required = true)
    protected String linkID;
    @XmlElement(name = "SourceEndpoint", required = true)
    protected EndpointType sourceEndpoint;
    @XmlElement(name = "DestinationDomain", required = true)
    protected String destinationDomain;
    protected Integer costs;

    /**
     * Gets the value of the linkID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLinkID() {
        return linkID;
    }

    /**
     * Sets the value of the linkID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLinkID(String value) {
        this.linkID = value;
    }

    public boolean isSetLinkID() {
        return (this.linkID!= null);
    }

    /**
     * Gets the value of the sourceEndpoint property.
     * 
     * @return
     *     possible object is
     *     {@link EndpointType }
     *     
     */
    public EndpointType getSourceEndpoint() {
        return sourceEndpoint;
    }

    /**
     * Sets the value of the sourceEndpoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link EndpointType }
     *     
     */
    public void setSourceEndpoint(EndpointType value) {
        this.sourceEndpoint = value;
    }

    public boolean isSetSourceEndpoint() {
        return (this.sourceEndpoint!= null);
    }

    /**
     * Gets the value of the destinationDomain property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDestinationDomain() {
        return destinationDomain;
    }

    /**
     * Sets the value of the destinationDomain property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDestinationDomain(String value) {
        this.destinationDomain = value;
    }

    public boolean isSetDestinationDomain() {
        return (this.destinationDomain!= null);
    }

    /**
     * Gets the value of the costs property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCosts() {
        return costs;
    }

    /**
     * Sets the value of the costs property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCosts(Integer value) {
        this.costs = value;
    }

    public boolean isSetCosts() {
        return (this.costs!= null);
    }

}