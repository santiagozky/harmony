
package eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AddEndpointType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AddEndpointType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Endpoint" type="{http://ist_phosphorus.eu/nsp}EndpointType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AddEndpointType", namespace = "http://ist_phosphorus.eu/nsp/webservice/topology", propOrder = {
    "endpoint"
})
public class AddEndpointType
    implements Serializable, Cloneable
{
    public AddEndpointType clone() throws CloneNotSupportedException {
        return (AddEndpointType)super.clone();
    }

    @XmlElement(name = "Endpoint", required = true)
    protected EndpointType endpoint;

    /**
     * Gets the value of the endpoint property.
     * 
     * @return
     *     possible object is
     *     {@link EndpointType }
     *     
     */
    public EndpointType getEndpoint() {
        return endpoint;
    }

    /**
     * Sets the value of the endpoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link EndpointType }
     *     
     */
    public void setEndpoint(EndpointType value) {
        this.endpoint = value;
    }

    public boolean isSetEndpoint() {
        return (this.endpoint!= null);
    }

}