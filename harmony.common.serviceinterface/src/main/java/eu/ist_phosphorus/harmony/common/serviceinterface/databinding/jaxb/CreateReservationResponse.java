
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
 *         &lt;element name="createReservationResponse" type="{http://ist_phosphorus.eu/nsp/webservice/reservation}CreateReservationResponseType"/>
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
    "createReservationResponse"
})
@XmlRootElement(name = "createReservationResponse")
public class CreateReservationResponse
    implements Serializable, Cloneable
{
    public CreateReservationResponse clone() throws CloneNotSupportedException {
        return (CreateReservationResponse)super.clone();
    }

    @XmlElement(required = true)
    protected CreateReservationResponseType createReservationResponse;

    /**
     * Gets the value of the createReservationResponse property.
     * 
     * @return
     *     possible object is
     *     {@link CreateReservationResponseType }
     *     
     */
    public CreateReservationResponseType getCreateReservationResponse() {
        return createReservationResponse;
    }

    /**
     * Sets the value of the createReservationResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link CreateReservationResponseType }
     *     
     */
    public void setCreateReservationResponse(CreateReservationResponseType value) {
        this.createReservationResponse = value;
    }

    public boolean isSetCreateReservationResponse() {
        return (this.createReservationResponse!= null);
    }

}