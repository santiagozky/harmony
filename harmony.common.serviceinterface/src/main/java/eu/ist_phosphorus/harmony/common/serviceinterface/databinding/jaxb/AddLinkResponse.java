
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
 *         &lt;element name="addLinkResponse" type="{http://ist_phosphorus.eu/nsp/webservice/topology}AddLinkResponseType"/>
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
    "addLinkResponse"
})
@XmlRootElement(name = "addLinkResponse", namespace = "http://ist_phosphorus.eu/nsp/webservice/topology")
public class AddLinkResponse
    implements Serializable, Cloneable
{
    public AddLinkResponse clone() throws CloneNotSupportedException {
        return (AddLinkResponse)super.clone();
    }

    @XmlElement(namespace = "http://ist_phosphorus.eu/nsp/webservice/topology", required = true)
    protected AddLinkResponseType addLinkResponse;

    /**
     * Gets the value of the addLinkResponse property.
     * 
     * @return
     *     possible object is
     *     {@link AddLinkResponseType }
     *     
     */
    public AddLinkResponseType getAddLinkResponse() {
        return addLinkResponse;
    }

    /**
     * Sets the value of the addLinkResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddLinkResponseType }
     *     
     */
    public void setAddLinkResponse(AddLinkResponseType value) {
        this.addLinkResponse = value;
    }

    public boolean isSetAddLinkResponse() {
        return (this.addLinkResponse!= null);
    }

}
