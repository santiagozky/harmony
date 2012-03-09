
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
 *         &lt;element name="subscribe" type="{http://ist_phosphorus.eu/nsp/webservice/notification}subscribeType"/>
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
    "subscribe"
})
@XmlRootElement(name = "subscribe", namespace = "http://ist_phosphorus.eu/nsp/webservice/notification")
public class Subscribe
    implements Serializable, Cloneable
{
    public Subscribe clone() throws CloneNotSupportedException {
        return (Subscribe)super.clone();
    }

    @XmlElement(namespace = "http://ist_phosphorus.eu/nsp/webservice/notification", required = true)
    protected SubscribeType subscribe;

    /**
     * Gets the value of the subscribe property.
     * 
     * @return
     *     possible object is
     *     {@link SubscribeType }
     *     
     */
    public SubscribeType getSubscribe() {
        return subscribe;
    }

    /**
     * Sets the value of the subscribe property.
     * 
     * @param value
     *     allowed object is
     *     {@link SubscribeType }
     *     
     */
    public void setSubscribe(SubscribeType value) {
        this.subscribe = value;
    }

    public boolean isSetSubscribe() {
        return (this.subscribe!= null);
    }

}