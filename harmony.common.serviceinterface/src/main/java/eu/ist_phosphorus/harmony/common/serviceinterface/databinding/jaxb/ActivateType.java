
package eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActivateType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ActivateType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ReservationID" type="{http://ist_phosphorus.eu/nsp/webservice/reservation}ReservationIdentifierType"/>
 *         &lt;element name="GRI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ServiceID" type="{http://ist_phosphorus.eu/nsp/webservice/reservation}ServiceIdentifierType"/>
 *         &lt;element name="Token" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ActivateType", propOrder = {
    "reservationID",
    "gri",
    "serviceID",
    "token"
})
public class ActivateType
    implements Serializable, Cloneable
{
    public ActivateType clone() throws CloneNotSupportedException {
        return (ActivateType)super.clone();
    }

    @XmlElement(name = "ReservationID", required = true)
    protected String reservationID;
    @XmlElement(name = "GRI")
    protected String gri;
    @XmlElement(name = "ServiceID")
    protected int serviceID;
    @XmlElement(name = "Token")
    protected String token;

    /**
     * Gets the value of the reservationID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReservationID() {
        return reservationID;
    }

    /**
     * Sets the value of the reservationID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReservationID(String value) {
        this.reservationID = value;
    }

    public boolean isSetReservationID() {
        return (this.reservationID!= null);
    }

    /**
     * Gets the value of the gri property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGRI() {
        return gri;
    }

    /**
     * Sets the value of the gri property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGRI(String value) {
        this.gri = value;
    }

    public boolean isSetGRI() {
        return (this.gri!= null);
    }

    /**
     * Gets the value of the serviceID property.
     * 
     */
    public int getServiceID() {
        return serviceID;
    }

    /**
     * Sets the value of the serviceID property.
     * 
     */
    public void setServiceID(int value) {
        this.serviceID = value;
    }

    public boolean isSetServiceID() {
        return true;
    }

    /**
     * Gets the value of the token property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the value of the token property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setToken(String value) {
        this.token = value;
    }

    public boolean isSetToken() {
        return (this.token!= null);
    }

}
