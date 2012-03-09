
package eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for subscribeType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="subscribeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ConsumerReference" type="{http://ist_phosphorus.eu/nsp/webservice/notification}ConsumerIdentifierType"/>
 *         &lt;element name="Topic" type="{http://ist_phosphorus.eu/nsp/webservice/notification}TopicIdentifierType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "subscribeType", namespace = "http://ist_phosphorus.eu/nsp/webservice/notification", propOrder = {
    "consumerReference",
    "topic"
})
public class SubscribeType
    implements Serializable, Cloneable
{
    public SubscribeType clone() throws CloneNotSupportedException {
        return (SubscribeType)super.clone();
    }

    @XmlElement(name = "ConsumerReference", required = true)
    protected String consumerReference;
    @XmlElement(name = "Topic", required = true)
    protected String topic;

    /**
     * Gets the value of the consumerReference property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConsumerReference() {
        return consumerReference;
    }

    /**
     * Sets the value of the consumerReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConsumerReference(String value) {
        this.consumerReference = value;
    }

    public boolean isSetConsumerReference() {
        return (this.consumerReference!= null);
    }

    /**
     * Gets the value of the topic property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTopic() {
        return topic;
    }

    /**
     * Sets the value of the topic property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTopic(String value) {
        this.topic = value;
    }

    public boolean isSetTopic() {
        return (this.topic!= null);
    }

}
