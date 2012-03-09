
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
 *         &lt;element name="isAvailable" type="{http://ist_phosphorus.eu/nsp/webservice/reservation}IsAvailableType"/>
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
    "isAvailable"
})
@XmlRootElement(name = "isAvailable")
public class IsAvailable
    implements Serializable, Cloneable
{
    public IsAvailable clone() throws CloneNotSupportedException {
        return (IsAvailable)super.clone();
    }

    @XmlElement(required = true)
    protected IsAvailableType isAvailable;

    /**
     * Gets the value of the isAvailable property.
     * 
     * @return
     *     possible object is
     *     {@link IsAvailableType }
     *     
     */
    public IsAvailableType getIsAvailable() {
        return isAvailable;
    }

    /**
     * Sets the value of the isAvailable property.
     * 
     * @param value
     *     allowed object is
     *     {@link IsAvailableType }
     *     
     */
    public void setIsAvailable(IsAvailableType value) {
        this.isAvailable = value;
    }

    public boolean isSetIsAvailable() {
        return (this.isAvailable!= null);
    }

}
