
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
 *         &lt;element name="getReservations" type="{http://ist_phosphorus.eu/nsp/webservice/reservation}GetReservationsType"/>
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
    "getReservations"
})
@XmlRootElement(name = "getReservations")
public class GetReservations
    implements Serializable, Cloneable
{
    public GetReservations clone() throws CloneNotSupportedException {
        return (GetReservations)super.clone();
    }

    @XmlElement(required = true)
    protected GetReservationsType getReservations;

    /**
     * Gets the value of the getReservations property.
     * 
     * @return
     *     possible object is
     *     {@link GetReservationsType }
     *     
     */
    public GetReservationsType getGetReservations() {
        return getReservations;
    }

    /**
     * Sets the value of the getReservations property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetReservationsType }
     *     
     */
    public void setGetReservations(GetReservationsType value) {
        this.getReservations = value;
    }

    public boolean isSetGetReservations() {
        return (this.getReservations!= null);
    }

}
