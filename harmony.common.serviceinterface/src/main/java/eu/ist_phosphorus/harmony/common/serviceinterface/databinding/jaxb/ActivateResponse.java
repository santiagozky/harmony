
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
 *         &lt;element name="activateResponse" type="{http://ist_phosphorus.eu/nsp/webservice/reservation}ActivateResponseType"/>
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
    "activateResponse"
})
@XmlRootElement(name = "activateResponse")
public class ActivateResponse
    implements Serializable, Cloneable
{
    public ActivateResponse clone() throws CloneNotSupportedException {
        return (ActivateResponse)super.clone();
    }

    @XmlElement(required = true)
    protected ActivateResponseType activateResponse;

    /**
     * Gets the value of the activateResponse property.
     * 
     * @return
     *     possible object is
     *     {@link ActivateResponseType }
     *     
     */
    public ActivateResponseType getActivateResponse() {
        return activateResponse;
    }

    /**
     * Sets the value of the activateResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link ActivateResponseType }
     *     
     */
    public void setActivateResponse(ActivateResponseType value) {
        this.activateResponse = value;
    }

    public boolean isSetActivateResponse() {
        return (this.activateResponse!= null);
    }

}
