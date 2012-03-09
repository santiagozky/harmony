
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
 *         &lt;element name="getReservationResponse" type="{http://ist_phosphorus.eu/nsp/webservice/reservation}GetReservationResponseType"/>
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
    "getReservationResponse"
})
@XmlRootElement(name = "getReservationResponse")
public class GetReservationResponse
    implements Serializable, Cloneable
{
    public GetReservationResponse clone() throws CloneNotSupportedException {
        return (GetReservationResponse)super.clone();
    }

    @XmlElement(required = true)
    protected GetReservationResponseType getReservationResponse;

    /**
     * Gets the value of the getReservationResponse property.
     * 
     * @return
     *     possible object is
     *     {@link GetReservationResponseType }
     *     
     */
    public GetReservationResponseType getGetReservationResponse() {
        return getReservationResponse;
    }

    /**
     * Sets the value of the getReservationResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetReservationResponseType }
     *     
     */
    public void setGetReservationResponse(GetReservationResponseType value) {
        this.getReservationResponse = value;
    }

    public boolean isSetGetReservationResponse() {
        return (this.getReservationResponse!= null);
    }

}
