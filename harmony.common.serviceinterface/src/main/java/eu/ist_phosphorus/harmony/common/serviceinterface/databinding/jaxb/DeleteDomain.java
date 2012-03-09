
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
 *         &lt;element name="deleteDomain" type="{http://ist_phosphorus.eu/nsp/webservice/topology}DeleteDomainType"/>
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
    "deleteDomain"
})
@XmlRootElement(name = "deleteDomain", namespace = "http://ist_phosphorus.eu/nsp/webservice/topology")
public class DeleteDomain
    implements Serializable, Cloneable
{
    public DeleteDomain clone() throws CloneNotSupportedException {
        return (DeleteDomain)super.clone();
    }

    @XmlElement(namespace = "http://ist_phosphorus.eu/nsp/webservice/topology", required = true)
    protected DeleteDomainType deleteDomain;

    /**
     * Gets the value of the deleteDomain property.
     * 
     * @return
     *     possible object is
     *     {@link DeleteDomainType }
     *     
     */
    public DeleteDomainType getDeleteDomain() {
        return deleteDomain;
    }

    /**
     * Sets the value of the deleteDomain property.
     * 
     * @param value
     *     allowed object is
     *     {@link DeleteDomainType }
     *     
     */
    public void setDeleteDomain(DeleteDomainType value) {
        this.deleteDomain = value;
    }

    public boolean isSetDeleteDomain() {
        return (this.deleteDomain!= null);
    }

}
