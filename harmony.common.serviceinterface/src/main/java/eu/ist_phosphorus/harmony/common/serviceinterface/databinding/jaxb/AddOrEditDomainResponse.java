
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
 *         &lt;element name="addOrEditDomainResponse" type="{http://ist_phosphorus.eu/nsp/webservice/topology}AddOrEditDomainResponseType"/>
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
    "addOrEditDomainResponse"
})
@XmlRootElement(name = "addOrEditDomainResponse", namespace = "http://ist_phosphorus.eu/nsp/webservice/topology")
public class AddOrEditDomainResponse
    implements Serializable, Cloneable
{
    public AddOrEditDomainResponse clone() throws CloneNotSupportedException {
        return (AddOrEditDomainResponse)super.clone();
    }

    @XmlElement(namespace = "http://ist_phosphorus.eu/nsp/webservice/topology", required = true)
    protected AddOrEditDomainResponseType addOrEditDomainResponse;

    /**
     * Gets the value of the addOrEditDomainResponse property.
     * 
     * @return
     *     possible object is
     *     {@link AddOrEditDomainResponseType }
     *     
     */
    public AddOrEditDomainResponseType getAddOrEditDomainResponse() {
        return addOrEditDomainResponse;
    }

    /**
     * Sets the value of the addOrEditDomainResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddOrEditDomainResponseType }
     *     
     */
    public void setAddOrEditDomainResponse(AddOrEditDomainResponseType value) {
        this.addOrEditDomainResponse = value;
    }

    public boolean isSetAddOrEditDomainResponse() {
        return (this.addOrEditDomainResponse!= null);
    }

}
