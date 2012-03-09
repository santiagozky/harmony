
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
 *         &lt;element name="createReservation" type="{http://ist_phosphorus.eu/nsp/webservice/reservation}CreateReservationType"/>
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
    "createReservation"
})
@XmlRootElement(name = "createReservation")
public class CreateReservation
    implements Serializable, Cloneable
{
    public CreateReservation clone() throws CloneNotSupportedException {
        return (CreateReservation)super.clone();
    }

    @XmlElement(required = true)
    protected CreateReservationType createReservation;

    /**
     * Gets the value of the createReservation property.
     * 
     * @return
     *     possible object is
     *     {@link CreateReservationType }
     *     
     */
    public CreateReservationType getCreateReservation() {
        return createReservation;
    }

    /**
     * Sets the value of the createReservation property.
     * 
     * @param value
     *     allowed object is
     *     {@link CreateReservationType }
     *     
     */
    public void setCreateReservation(CreateReservationType value) {
        this.createReservation = value;
    }

    public boolean isSetCreateReservation() {
        return (this.createReservation!= null);
    }

}
