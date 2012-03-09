
package eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NotificationFault complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NotificationFault">
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
@XmlType(name = "NotificationFault", namespace = "http://ist_phosphorus.eu/nsp/webservice/notification")
@XmlSeeAlso({
    TopicNotFoundFault.class
})
public class NotificationFault
    extends eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.BaseFaultType
    implements Serializable, Cloneable
{
    public NotificationFault clone() throws CloneNotSupportedException {
        return (NotificationFault)super.clone();
    }


}
