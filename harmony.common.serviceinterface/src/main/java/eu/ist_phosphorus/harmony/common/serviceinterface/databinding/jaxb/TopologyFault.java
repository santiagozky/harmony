
package eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TopologyFault complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TopologyFault">
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
@XmlType(name = "TopologyFault", namespace = "http://ist_phosphorus.eu/nsp/webservice/topology")
@XmlSeeAlso({
    EndpointAlreadyExistsFault.class,
    DomainAlreadyExistsFault.class,
    DomainNotFoundFault.class,
    LinkAlreadyExistsFault.class
})
public class TopologyFault
    extends eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.BaseFaultType
    implements Serializable, Cloneable
{
    public TopologyFault clone() throws CloneNotSupportedException {
        return (TopologyFault)super.clone();
    }


}
