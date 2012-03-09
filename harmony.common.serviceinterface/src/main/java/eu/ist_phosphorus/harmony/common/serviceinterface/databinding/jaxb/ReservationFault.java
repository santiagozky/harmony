
package eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ReservationFault complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ReservationFault">
 *   &lt;complexContent>
 *     &lt;extension base="{http://docs.oasis-open.org/wsrf/bf-2}BaseFaultType">
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReservationFault")
@XmlSeeAlso({
    ReservationIDNotFoundFault.class,
    TimeoutFault.class,
    InvalidReservationIDFault.class,
    InvalidServiceIDFault.class,
    EndpointNotFoundFault.class
})
public class ReservationFault
    extends eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.BaseFaultType
    implements Serializable, Cloneable
{
    public ReservationFault clone() throws CloneNotSupportedException {
        return (ReservationFault)super.clone();
    }


}
