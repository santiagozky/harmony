
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
 *         &lt;element name="addEndpoint" type="{http://ist_phosphorus.eu/nsp/webservice/topology}AddEndpointType"/>
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
    "addEndpoint"
})
@XmlRootElement(name = "addEndpoint", namespace = "http://ist_phosphorus.eu/nsp/webservice/topology")
public class AddEndpoint
    implements Serializable, Cloneable
{
    public AddEndpoint clone() throws CloneNotSupportedException {
        return (AddEndpoint)super.clone();
    }

    @XmlElement(namespace = "http://ist_phosphorus.eu/nsp/webservice/topology", required = true)
    protected AddEndpointType addEndpoint;

    /**
     * Gets the value of the addEndpoint property.
     * 
     * @return
     *     possible object is
     *     {@link AddEndpointType }
     *     
     */
    public AddEndpointType getAddEndpoint() {
        return addEndpoint;
    }

    /**
     * Sets the value of the addEndpoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddEndpointType }
     *     
     */
    public void setAddEndpoint(AddEndpointType value) {
        this.addEndpoint = value;
    }

    public boolean isSetAddEndpoint() {
        return (this.addEndpoint!= null);
    }

}
