
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
 *         &lt;element name="deleteEndpoint" type="{http://ist_phosphorus.eu/nsp/webservice/topology}DeleteEndpointType"/>
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
    "deleteEndpoint"
})
@XmlRootElement(name = "deleteEndpoint", namespace = "http://ist_phosphorus.eu/nsp/webservice/topology")
public class DeleteEndpoint
    implements Serializable, Cloneable
{
    public DeleteEndpoint clone() throws CloneNotSupportedException {
        return (DeleteEndpoint)super.clone();
    }

    @XmlElement(namespace = "http://ist_phosphorus.eu/nsp/webservice/topology", required = true)
    protected DeleteEndpointType deleteEndpoint;

    /**
     * Gets the value of the deleteEndpoint property.
     * 
     * @return
     *     possible object is
     *     {@link DeleteEndpointType }
     *     
     */
    public DeleteEndpointType getDeleteEndpoint() {
        return deleteEndpoint;
    }

    /**
     * Sets the value of the deleteEndpoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link DeleteEndpointType }
     *     
     */
    public void setDeleteEndpoint(DeleteEndpointType value) {
        this.deleteEndpoint = value;
    }

    public boolean isSetDeleteEndpoint() {
        return (this.deleteEndpoint!= null);
    }

}
