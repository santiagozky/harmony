
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
 *         &lt;element name="cancelReservation" type="{http://ist_phosphorus.eu/nsp/webservice/reservation}CancelReservationType"/>
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
    "cancelReservation"
})
@XmlRootElement(name = "cancelReservation")
public class CancelReservation
    implements Serializable, Cloneable
{
    public CancelReservation clone() throws CloneNotSupportedException {
        return (CancelReservation)super.clone();
    }

    @XmlElement(required = true)
    protected CancelReservationType cancelReservation;

    /**
     * Gets the value of the cancelReservation property.
     * 
     * @return
     *     possible object is
     *     {@link CancelReservationType }
     *     
     */
    public CancelReservationType getCancelReservation() {
        return cancelReservation;
    }

    /**
     * Sets the value of the cancelReservation property.
     * 
     * @param value
     *     allowed object is
     *     {@link CancelReservationType }
     *     
     */
    public void setCancelReservation(CancelReservationType value) {
        this.cancelReservation = value;
    }

    public boolean isSetCancelReservation() {
        return (this.cancelReservation!= null);
    }

}
