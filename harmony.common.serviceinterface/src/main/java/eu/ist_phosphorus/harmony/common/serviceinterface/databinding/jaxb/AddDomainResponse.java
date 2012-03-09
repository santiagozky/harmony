
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
 *         &lt;element name="addDomainResponse" type="{http://ist_phosphorus.eu/nsp/webservice/topology}AddDomainResponseType"/>
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
    "addDomainResponse"
})
@XmlRootElement(name = "addDomainResponse", namespace = "http://ist_phosphorus.eu/nsp/webservice/topology")
public class AddDomainResponse
    implements Serializable, Cloneable
{
    public AddDomainResponse clone() throws CloneNotSupportedException {
        return (AddDomainResponse)super.clone();
    }

    @XmlElement(namespace = "http://ist_phosphorus.eu/nsp/webservice/topology", required = true)
    protected AddDomainResponseType addDomainResponse;

    /**
     * Gets the value of the addDomainResponse property.
     * 
     * @return
     *     possible object is
     *     {@link AddDomainResponseType }
     *     
     */
    public AddDomainResponseType getAddDomainResponse() {
        return addDomainResponse;
    }

    /**
     * Sets the value of the addDomainResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddDomainResponseType }
     *     
     */
    public void setAddDomainResponse(AddDomainResponseType value) {
        this.addDomainResponse = value;
    }

    public boolean isSetAddDomainResponse() {
        return (this.addDomainResponse!= null);
    }

}
