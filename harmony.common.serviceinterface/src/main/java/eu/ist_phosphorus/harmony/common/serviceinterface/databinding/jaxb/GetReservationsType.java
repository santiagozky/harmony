
package eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * 
 * 				Get reservations ending or starting in a given
 * 				time-period
 * 			
 * 
 * <p>Java class for GetReservationsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetReservationsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PeriodStartTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="PeriodEndTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetReservationsType", propOrder = {
    "periodStartTime",
    "periodEndTime"
})
public class GetReservationsType
    implements Serializable, Cloneable
{
    public GetReservationsType clone() throws CloneNotSupportedException {
        return (GetReservationsType)super.clone();
    }

    @XmlElement(name = "PeriodStartTime", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar periodStartTime;
    @XmlElement(name = "PeriodEndTime", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar periodEndTime;

    /**
     * Gets the value of the periodStartTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getPeriodStartTime() {
        return periodStartTime;
    }

    /**
     * Sets the value of the periodStartTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setPeriodStartTime(XMLGregorianCalendar value) {
        this.periodStartTime = value;
    }

    public boolean isSetPeriodStartTime() {
        return (this.periodStartTime!= null);
    }

    /**
     * Gets the value of the periodEndTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getPeriodEndTime() {
        return periodEndTime;
    }

    /**
     * Sets the value of the periodEndTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setPeriodEndTime(XMLGregorianCalendar value) {
        this.periodEndTime = value;
    }

    public boolean isSetPeriodEndTime() {
        return (this.periodEndTime!= null);
    }

}
