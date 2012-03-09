
package eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BindType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BindType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ReservationID" type="{http://ist_phosphorus.eu/nsp/webservice/reservation}ReservationIdentifierType"/>
 *         &lt;element name="GRI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ServiceID" type="{http://ist_phosphorus.eu/nsp/webservice/reservation}ServiceIdentifierType"/>
 *         &lt;element name="Token" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ConnectionID" type="{http://ist_phosphorus.eu/nsp/webservice/reservation}ConnectionIdentifierType"/>
 *         &lt;element name="EndpointID" type="{http://ist_phosphorus.eu/nsp}EndpointIdentifierType"/>
 *         &lt;element name="IPAdress" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BindType", propOrder = {
    "reservationID",
    "gri",
    "serviceID",
    "token",
    "connectionID",
    "endpointID",
    "ipAdress"
})
public class BindType
    implements Serializable, Cloneable
{
    public BindType clone() throws CloneNotSupportedException {
        return (BindType)super.clone();
    }

    @XmlElement(name = "ReservationID", required = true)
    protected String reservationID;
    @XmlElement(name = "GRI")
    protected String gri;
    @XmlElement(name = "ServiceID")
    protected int serviceID;
    @XmlElement(name = "Token")
    protected String token;
    @XmlElement(name = "ConnectionID")
    protected int connectionID;
    @XmlElement(name = "EndpointID", required = true)
    protected String endpointID;
    @XmlElement(name = "IPAdress", required = true)
    protected List<String> ipAdress;

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

    /**
     * Gets the value of the connectionID property.
     * 
     */
    public int getConnectionID() {
        return connectionID;
    }

    /**
     * Sets the value of the connectionID property.
     * 
     */
    public void setConnectionID(int value) {
        this.connectionID = value;
    }

    public boolean isSetConnectionID() {
        return true;
    }

    /**
     * Gets the value of the endpointID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEndpointID() {
        return endpointID;
    }

    /**
     * Sets the value of the endpointID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEndpointID(String value) {
        this.endpointID = value;
    }

    public boolean isSetEndpointID() {
        return (this.endpointID!= null);
    }

    /**
     * Gets the value of the ipAdress property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ipAdress property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIPAdress().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getIPAdress() {
        if (ipAdress == null) {
            ipAdress = new ArrayList<String>();
        }
        return this.ipAdress;
    }

    public boolean isSetIPAdress() {
        return ((this.ipAdress!= null)&&(!this.ipAdress.isEmpty()));
    }

    public void unsetIPAdress() {
        this.ipAdress = null;
    }

}