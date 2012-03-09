
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
 *         &lt;element name="addDomain" type="{http://ist_phosphorus.eu/nsp/webservice/topology}AddDomainType"/>
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
    "addDomain"
})
@XmlRootElement(name = "addDomain", namespace = "http://ist_phosphorus.eu/nsp/webservice/topology")
public class AddDomain
    implements Serializable, Cloneable
{
    public AddDomain clone() throws CloneNotSupportedException {
        return (AddDomain)super.clone();
    }

    @XmlElement(namespace = "http://ist_phosphorus.eu/nsp/webservice/topology", required = true)
    protected AddDomainType addDomain;

    /**
     * Gets the value of the addDomain property.
     * 
     * @return
     *     possible object is
     *     {@link AddDomainType }
     *     
     */
    public AddDomainType getAddDomain() {
        return addDomain;
    }

    /**
     * Sets the value of the addDomain property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddDomainType }
     *     
     */
    public void setAddDomain(AddDomainType value) {
        this.addDomain = value;
    }

    public boolean isSetAddDomain() {
        return (this.addDomain!= null);
    }

}
