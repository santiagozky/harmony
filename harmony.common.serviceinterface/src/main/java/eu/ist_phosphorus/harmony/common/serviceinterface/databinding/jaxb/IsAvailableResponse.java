
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
 *         &lt;element name="isAvailableResponse" type="{http://ist_phosphorus.eu/nsp/webservice/reservation}IsAvailableResponseType"/>
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
    "isAvailableResponse"
})
@XmlRootElement(name = "isAvailableResponse")
public class IsAvailableResponse
    implements Serializable, Cloneable
{
    public IsAvailableResponse clone() throws CloneNotSupportedException {
        return (IsAvailableResponse)super.clone();
    }

    @XmlElement(required = true)
    protected IsAvailableResponseType isAvailableResponse;

    /**
     * Gets the value of the isAvailableResponse property.
     * 
     * @return
     *     possible object is
     *     {@link IsAvailableResponseType }
     *     
     */
    public IsAvailableResponseType getIsAvailableResponse() {
        return isAvailableResponse;
    }

    /**
     * Sets the value of the isAvailableResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link IsAvailableResponseType }
     *     
     */
    public void setIsAvailableResponse(IsAvailableResponseType value) {
        this.isAvailableResponse = value;
    }

    public boolean isSetIsAvailableResponse() {
        return (this.isAvailableResponse!= null);
    }

}
