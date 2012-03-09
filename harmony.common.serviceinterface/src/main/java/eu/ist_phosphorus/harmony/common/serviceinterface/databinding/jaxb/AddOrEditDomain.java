
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
 *         &lt;element name="addOrEditDomain" type="{http://ist_phosphorus.eu/nsp/webservice/topology}AddOrEditDomainType"/>
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
    "addOrEditDomain"
})
@XmlRootElement(name = "addOrEditDomain", namespace = "http://ist_phosphorus.eu/nsp/webservice/topology")
public class AddOrEditDomain
    implements Serializable, Cloneable
{
    public AddOrEditDomain clone() throws CloneNotSupportedException {
        return (AddOrEditDomain)super.clone();
    }

    @XmlElement(namespace = "http://ist_phosphorus.eu/nsp/webservice/topology", required = true)
    protected AddOrEditDomainType addOrEditDomain;

    /**
     * Gets the value of the addOrEditDomain property.
     * 
     * @return
     *     possible object is
     *     {@link AddOrEditDomainType }
     *     
     */
    public AddOrEditDomainType getAddOrEditDomain() {
        return addOrEditDomain;
    }

    /**
     * Sets the value of the addOrEditDomain property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddOrEditDomainType }
     *     
     */
    public void setAddOrEditDomain(AddOrEditDomainType value) {
        this.addOrEditDomain = value;
    }

    public boolean isSetAddOrEditDomain() {
        return (this.addOrEditDomain!= null);
    }

}
