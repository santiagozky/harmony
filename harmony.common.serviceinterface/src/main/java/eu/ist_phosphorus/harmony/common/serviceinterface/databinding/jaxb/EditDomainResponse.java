
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
 *         &lt;element name="editDomainResponse" type="{http://ist_phosphorus.eu/nsp/webservice/topology}EditDomainResponseType"/>
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
    "editDomainResponse"
})
@XmlRootElement(name = "editDomainResponse", namespace = "http://ist_phosphorus.eu/nsp/webservice/topology")
public class EditDomainResponse
    implements Serializable, Cloneable
{
    public EditDomainResponse clone() throws CloneNotSupportedException {
        return (EditDomainResponse)super.clone();
    }

    @XmlElement(namespace = "http://ist_phosphorus.eu/nsp/webservice/topology", required = true)
    protected EditDomainResponseType editDomainResponse;

    /**
     * Gets the value of the editDomainResponse property.
     * 
     * @return
     *     possible object is
     *     {@link EditDomainResponseType }
     *     
     */
    public EditDomainResponseType getEditDomainResponse() {
        return editDomainResponse;
    }

    /**
     * Sets the value of the editDomainResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link EditDomainResponseType }
     *     
     */
    public void setEditDomainResponse(EditDomainResponseType value) {
        this.editDomainResponse = value;
    }

    public boolean isSetEditDomainResponse() {
        return (this.editDomainResponse!= null);
    }

}
