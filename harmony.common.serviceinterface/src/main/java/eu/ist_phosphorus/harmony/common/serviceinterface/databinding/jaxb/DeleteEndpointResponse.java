
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
 *         &lt;element name="deleteEndpointResponse" type="{http://ist_phosphorus.eu/nsp/webservice/topology}DeleteEndpointResponseType"/>
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
    "deleteEndpointResponse"
})
@XmlRootElement(name = "deleteEndpointResponse", namespace = "http://ist_phosphorus.eu/nsp/webservice/topology")
public class DeleteEndpointResponse
    implements Serializable, Cloneable
{
    public DeleteEndpointResponse clone() throws CloneNotSupportedException {
        return (DeleteEndpointResponse)super.clone();
    }

    @XmlElement(namespace = "http://ist_phosphorus.eu/nsp/webservice/topology", required = true)
    protected DeleteEndpointResponseType deleteEndpointResponse;

    /**
     * Gets the value of the deleteEndpointResponse property.
     * 
     * @return
     *     possible object is
     *     {@link DeleteEndpointResponseType }
     *     
     */
    public DeleteEndpointResponseType getDeleteEndpointResponse() {
        return deleteEndpointResponse;
    }

    /**
     * Sets the value of the deleteEndpointResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link DeleteEndpointResponseType }
     *     
     */
    public void setDeleteEndpointResponse(DeleteEndpointResponseType value) {
        this.deleteEndpointResponse = value;
    }

    public boolean isSetDeleteEndpointResponse() {
        return (this.deleteEndpointResponse!= null);
    }

}
