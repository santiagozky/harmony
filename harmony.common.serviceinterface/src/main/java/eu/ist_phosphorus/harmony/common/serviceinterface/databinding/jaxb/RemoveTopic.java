
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
 *         &lt;element name="removeTopic" type="{http://ist_phosphorus.eu/nsp/webservice/notification}removeTopicType"/>
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
    "removeTopic"
})
@XmlRootElement(name = "removeTopic", namespace = "http://ist_phosphorus.eu/nsp/webservice/notification")
public class RemoveTopic
    implements Serializable, Cloneable
{
    public RemoveTopic clone() throws CloneNotSupportedException {
        return (RemoveTopic)super.clone();
    }

    @XmlElement(namespace = "http://ist_phosphorus.eu/nsp/webservice/notification", required = true)
    protected RemoveTopicType removeTopic;

    /**
     * Gets the value of the removeTopic property.
     * 
     * @return
     *     possible object is
     *     {@link RemoveTopicType }
     *     
     */
    public RemoveTopicType getRemoveTopic() {
        return removeTopic;
    }

    /**
     * Sets the value of the removeTopic property.
     * 
     * @param value
     *     allowed object is
     *     {@link RemoveTopicType }
     *     
     */
    public void setRemoveTopic(RemoveTopicType value) {
        this.removeTopic = value;
    }

    public boolean isSetRemoveTopic() {
        return (this.removeTopic!= null);
    }

}
