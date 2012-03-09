
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
 *         &lt;element name="deleteDomainResponse" type="{http://ist_phosphorus.eu/nsp/webservice/topology}DeleteDomainResponseType"/>
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
    "deleteDomainResponse"
})
@XmlRootElement(name = "deleteDomainResponse", namespace = "http://ist_phosphorus.eu/nsp/webservice/topology")
public class DeleteDomainResponse
    implements Serializable, Cloneable
{
    public DeleteDomainResponse clone() throws CloneNotSupportedException {
        return (DeleteDomainResponse)super.clone();
    }

    @XmlElement(namespace = "http://ist_phosphorus.eu/nsp/webservice/topology", required = true)
    protected DeleteDomainResponseType deleteDomainResponse;

    /**
     * Gets the value of the deleteDomainResponse property.
     * 
     * @return
     *     possible object is
     *     {@link DeleteDomainResponseType }
     *     
     */
    public DeleteDomainResponseType getDeleteDomainResponse() {
        return deleteDomainResponse;
    }

    /**
     * Sets the value of the deleteDomainResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link DeleteDomainResponseType }
     *     
     */
    public void setDeleteDomainResponse(DeleteDomainResponseType value) {
        this.deleteDomainResponse = value;
    }

    public boolean isSetDeleteDomainResponse() {
        return (this.deleteDomainResponse!= null);
    }

}
