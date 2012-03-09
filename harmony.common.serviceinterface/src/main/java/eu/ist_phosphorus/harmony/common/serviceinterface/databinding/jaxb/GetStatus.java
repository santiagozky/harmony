
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
 *         &lt;element name="getStatus" type="{http://ist_phosphorus.eu/nsp/webservice/reservation}GetStatusType"/>
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
    "getStatus"
})
@XmlRootElement(name = "getStatus")
public class GetStatus
    implements Serializable, Cloneable
{
    public GetStatus clone() throws CloneNotSupportedException {
        return (GetStatus)super.clone();
    }

    @XmlElement(required = true)
    protected GetStatusType getStatus;

    /**
     * Gets the value of the getStatus property.
     * 
     * @return
     *     possible object is
     *     {@link GetStatusType }
     *     
     */
    public GetStatusType getGetStatus() {
        return getStatus;
    }

    /**
     * Sets the value of the getStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetStatusType }
     *     
     */
    public void setGetStatus(GetStatusType value) {
        this.getStatus = value;
    }

    public boolean isSetGetStatus() {
        return (this.getStatus!= null);
    }

}
