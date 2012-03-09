
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
 *         &lt;element name="editEndpoint" type="{http://ist_phosphorus.eu/nsp/webservice/topology}EditEndpointType"/>
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
    "editEndpoint"
})
@XmlRootElement(name = "editEndpoint", namespace = "http://ist_phosphorus.eu/nsp/webservice/topology")
public class EditEndpoint
    implements Serializable, Cloneable
{
    public EditEndpoint clone() throws CloneNotSupportedException {
        return (EditEndpoint)super.clone();
    }

    @XmlElement(namespace = "http://ist_phosphorus.eu/nsp/webservice/topology", required = true)
    protected EditEndpointType editEndpoint;

    /**
     * Gets the value of the editEndpoint property.
     * 
     * @return
     *     possible object is
     *     {@link EditEndpointType }
     *     
     */
    public EditEndpointType getEditEndpoint() {
        return editEndpoint;
    }

    /**
     * Sets the value of the editEndpoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link EditEndpointType }
     *     
     */
    public void setEditEndpoint(EditEndpointType value) {
        this.editEndpoint = value;
    }

    public boolean isSetEditEndpoint() {
        return (this.editEndpoint!= null);
    }

}
