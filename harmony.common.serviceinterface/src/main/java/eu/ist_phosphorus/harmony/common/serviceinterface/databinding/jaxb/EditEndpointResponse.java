
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
 *         &lt;element name="editEndpointResponse" type="{http://ist_phosphorus.eu/nsp/webservice/topology}EditEndpointResponseType"/>
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
    "editEndpointResponse"
})
@XmlRootElement(name = "editEndpointResponse", namespace = "http://ist_phosphorus.eu/nsp/webservice/topology")
public class EditEndpointResponse
    implements Serializable, Cloneable
{
    public EditEndpointResponse clone() throws CloneNotSupportedException {
        return (EditEndpointResponse)super.clone();
    }

    @XmlElement(namespace = "http://ist_phosphorus.eu/nsp/webservice/topology", required = true)
    protected EditEndpointResponseType editEndpointResponse;

    /**
     * Gets the value of the editEndpointResponse property.
     * 
     * @return
     *     possible object is
     *     {@link EditEndpointResponseType }
     *     
     */
    public EditEndpointResponseType getEditEndpointResponse() {
        return editEndpointResponse;
    }

    /**
     * Sets the value of the editEndpointResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link EditEndpointResponseType }
     *     
     */
    public void setEditEndpointResponse(EditEndpointResponseType value) {
        this.editEndpointResponse = value;
    }

    public boolean isSetEditEndpointResponse() {
        return (this.editEndpointResponse!= null);
    }

}
