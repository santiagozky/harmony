
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
 *         &lt;element name="cancelReservationResponse" type="{http://ist_phosphorus.eu/nsp/webservice/reservation}CancelReservationResponseType"/>
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
    "cancelReservationResponse"
})
@XmlRootElement(name = "cancelReservationResponse")
public class CancelReservationResponse
    implements Serializable, Cloneable
{
    public CancelReservationResponse clone() throws CloneNotSupportedException {
        return (CancelReservationResponse)super.clone();
    }

    @XmlElement(required = true)
    protected CancelReservationResponseType cancelReservationResponse;

    /**
     * Gets the value of the cancelReservationResponse property.
     * 
     * @return
     *     possible object is
     *     {@link CancelReservationResponseType }
     *     
     */
    public CancelReservationResponseType getCancelReservationResponse() {
        return cancelReservationResponse;
    }

    /**
     * Sets the value of the cancelReservationResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link CancelReservationResponseType }
     *     
     */
    public void setCancelReservationResponse(CancelReservationResponseType value) {
        this.cancelReservationResponse = value;
    }

    public boolean isSetCancelReservationResponse() {
        return (this.cancelReservationResponse!= null);
    }

}
