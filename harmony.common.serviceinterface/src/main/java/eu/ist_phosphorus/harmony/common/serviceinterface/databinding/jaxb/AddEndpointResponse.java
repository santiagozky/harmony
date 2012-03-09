
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
 *         &lt;element name="addEndpointResponse" type="{http://ist_phosphorus.eu/nsp/webservice/topology}AddEndpointResponseType"/>
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
    "addEndpointResponse"
})
@XmlRootElement(name = "addEndpointResponse", namespace = "http://ist_phosphorus.eu/nsp/webservice/topology")
public class AddEndpointResponse
    implements Serializable, Cloneable
{
    public AddEndpointResponse clone() throws CloneNotSupportedException {
        return (AddEndpointResponse)super.clone();
    }

    @XmlElement(namespace = "http://ist_phosphorus.eu/nsp/webservice/topology", required = true)
    protected AddEndpointResponseType addEndpointResponse;

    /**
     * Gets the value of the addEndpointResponse property.
     * 
     * @return
     *     possible object is
     *     {@link AddEndpointResponseType }
     *     
     */
    public AddEndpointResponseType getAddEndpointResponse() {
        return addEndpointResponse;
    }

    /**
     * Sets the value of the addEndpointResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddEndpointResponseType }
     *     
     */
    public void setAddEndpointResponse(AddEndpointResponseType value) {
        this.addEndpointResponse = value;
    }

    public boolean isSetAddEndpointResponse() {
        return (this.addEndpointResponse!= null);
    }

}
