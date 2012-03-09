
package eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetReservationsResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetReservationsResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Reservation" type="{http://ist_phosphorus.eu/nsp/webservice/reservation}GetReservationsComplexType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetReservationsResponseType", propOrder = {
    "reservation"
})
public class GetReservationsResponseType
    implements Serializable, Cloneable
{
    public GetReservationsResponseType clone() throws CloneNotSupportedException {
        return (GetReservationsResponseType)super.clone();
    }

    @XmlElement(name = "Reservation")
    protected List<GetReservationsComplexType> reservation;

    /**
     * Gets the value of the reservation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the reservation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReservation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GetReservationsComplexType }
     * 
     * 
     */
    public List<GetReservationsComplexType> getReservation() {
        if (reservation == null) {
            reservation = new ArrayList<GetReservationsComplexType>();
        }
        return this.reservation;
    }

    public boolean isSetReservation() {
        return ((this.reservation!= null)&&(!this.reservation.isEmpty()));
    }

    public void unsetReservation() {
        this.reservation = null;
    }

}
