
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
 *         &lt;element name="getReservation" type="{http://ist_phosphorus.eu/nsp/webservice/reservation}GetReservationType"/>
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
    "getReservation"
})
@XmlRootElement(name = "getReservation")
public class GetReservation
    implements Serializable, Cloneable
{
    public GetReservation clone() throws CloneNotSupportedException {
        return (GetReservation)super.clone();
    }

    @XmlElement(required = true)
    protected GetReservationType getReservation;

    /**
     * Gets the value of the getReservation property.
     * 
     * @return
     *     possible object is
     *     {@link GetReservationType }
     *     
     */
    public GetReservationType getGetReservation() {
        return getReservation;
    }

    /**
     * Sets the value of the getReservation property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetReservationType }
     *     
     */
    public void setGetReservation(GetReservationType value) {
        this.getReservation = value;
    }

    public boolean isSetGetReservation() {
        return (this.getReservation!= null);
    }

}
