
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
 *     &lt;extension base="{http://ist_phosphorus.eu/nsp/webservice/topology}TopologyFault">
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "EndpointAlreadyExistsFault", namespace = "http://ist_phosphorus.eu/nsp/webservice/topology")
public class EndpointAlreadyExistsFault
    extends TopologyFault
    implements Serializable, Cloneable
{
    public EndpointAlreadyExistsFault clone() throws CloneNotSupportedException {
        return (EndpointAlreadyExistsFault)super.clone();
    }


}