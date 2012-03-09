
package eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 				Tuple of domain ID and ConnectionStatusType
 * 			
 * 
 * <p>Java class for DomainConnectionStatusType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DomainConnectionStatusType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Domain" type="{http://ist_phosphorus.eu/nsp}DomainIdentifierType"/>
 *         &lt;element name="Status" type="{http://ist_phosphorus.eu/nsp/webservice/reservation}ConnectionStatusType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DomainConnectionStatusType", propOrder = {
    "domain",
    "status"
})
public class DomainConnectionStatusType
    implements Serializable, Cloneable
{
    public DomainConnectionStatusType clone() throws CloneNotSupportedException {
        return (DomainConnectionStatusType)super.clone();
    }

    @XmlElement(name = "Domain", required = true)
    protected String domain;
    @XmlElement(name = "Status", required = true)
    protected ConnectionStatusType status;

    /**
     * Gets the value of the domain property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDomain() {
        return domain;
    }

    /**
     * Sets the value of the domain property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDomain(String value) {
        this.domain = value;
    }

    public boolean isSetDomain() {
        return (this.domain!= null);
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link ConnectionStatusType }
     *     
     */
    public ConnectionStatusType getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConnectionStatusType }
     *     
     */
    public void setStatus(ConnectionStatusType value) {
        this.status = value;
    }

    public boolean isSetStatus() {
        return (this.status!= null);
    }

}