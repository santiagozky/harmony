
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
 *         &lt;element name="editDomain" type="{http://ist_phosphorus.eu/nsp/webservice/topology}EditDomainType"/>
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
    "editDomain"
})
@XmlRootElement(name = "editDomain", namespace = "http://ist_phosphorus.eu/nsp/webservice/topology")
public class EditDomain
    implements Serializable, Cloneable
{
    public EditDomain clone() throws CloneNotSupportedException {
        return (EditDomain)super.clone();
    }

    @XmlElement(namespace = "http://ist_phosphorus.eu/nsp/webservice/topology", required = true)
    protected EditDomainType editDomain;

    /**
     * Gets the value of the editDomain property.
     * 
     * @return
     *     possible object is
     *     {@link EditDomainType }
     *     
     */
    public EditDomainType getEditDomain() {
        return editDomain;
    }

    /**
     * Sets the value of the editDomain property.
     * 
     * @param value
     *     allowed object is
     *     {@link EditDomainType }
     *     
     */
    public void setEditDomain(EditDomainType value) {
        this.editDomain = value;
    }

    public boolean isSetEditDomain() {
        return (this.editDomain!= null);
    }

}
