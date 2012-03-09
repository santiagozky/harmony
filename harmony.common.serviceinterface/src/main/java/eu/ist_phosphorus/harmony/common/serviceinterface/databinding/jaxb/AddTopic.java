
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
 *         &lt;element name="addTopic" type="{http://ist_phosphorus.eu/nsp/webservice/notification}addTopicType"/>
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
    "addTopic"
})
@XmlRootElement(name = "addTopic", namespace = "http://ist_phosphorus.eu/nsp/webservice/notification")
public class AddTopic
    implements Serializable, Cloneable
{
    public AddTopic clone() throws CloneNotSupportedException {
        return (AddTopic)super.clone();
    }

    @XmlElement(namespace = "http://ist_phosphorus.eu/nsp/webservice/notification", required = true)
    protected AddTopicType addTopic;

    /**
     * Gets the value of the addTopic property.
     * 
     * @return
     *     possible object is
     *     {@link AddTopicType }
     *     
     */
    public AddTopicType getAddTopic() {
        return addTopic;
    }

    /**
     * Sets the value of the addTopic property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddTopicType }
     *     
     */
    public void setAddTopic(AddTopicType value) {
        this.addTopic = value;
    }

    public boolean isSetAddTopic() {
        return (this.addTopic!= null);
    }

}