
package eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *     &lt;extension base="{http://docs.oasis-open.org/wsrf/bf-2}BaseFaultType">
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "OperationNotAllowedFault", namespace = "http://ist_phosphorus.eu/nsp")
public class OperationNotAllowedFault
    extends eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.BaseFaultType
    implements Serializable, Cloneable
{
    public OperationNotAllowedFault clone() throws CloneNotSupportedException {
        return (OperationNotAllowedFault)super.clone();
    }


}
