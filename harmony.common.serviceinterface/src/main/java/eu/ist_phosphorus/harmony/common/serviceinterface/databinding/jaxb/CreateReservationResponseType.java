
package eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CreateReservationResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CreateReservationResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="JobID" type="{http://ist_phosphorus.eu/nsp/webservice/reservation}JobIdentifierType" minOccurs="0"/>
 *         &lt;element name="ReservationID" type="{http://ist_phosphorus.eu/nsp/webservice/reservation}ReservationIdentifierType"/>
 *         &lt;element name="GRI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "CreateReservationResponseType", propOrder = {
    "jobID",
    "reservationID",
    "gri",
    "token"
})
public class CreateReservationResponseType
    implements Serializable, Cloneable
{
    public CreateReservationResponseType clone() throws CloneNotSupportedException {
        return (CreateReservationResponseType)super.clone();
    }

    @XmlElement(name = "JobID")
    protected Long jobID;
    @XmlElement(name = "ReservationID", required = true)
    protected String reservationID;
    @XmlElement(name = "GRI")
    protected String gri;
    @XmlElement(name = "Token")
    protected String token;

    /**
     * Gets the value of the jobID property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getJobID() {
        return jobID;
    }

    /**
     * Sets the value of the jobID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setJobID(Long value) {
        this.jobID = value;
    }

    public boolean isSetJobID() {
        return (this.jobID!= null);
    }

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
