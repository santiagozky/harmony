
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
 *         &lt;element name="unsubscribe" type="{http://ist_phosphorus.eu/nsp/webservice/notification}unsubscribeType"/>
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
    "unsubscribe"
})
@XmlRootElement(name = "unsubscribe", namespace = "http://ist_phosphorus.eu/nsp/webservice/notification")
public class Unsubscribe
    implements Serializable, Cloneable
{
    public Unsubscribe clone() throws CloneNotSupportedException {
        return (Unsubscribe)super.clone();
    }

    @XmlElement(namespace = "http://ist_phosphorus.eu/nsp/webservice/notification", required = true)
    protected UnsubscribeType unsubscribe;

    /**
     * Gets the value of the unsubscribe property.
     * 
     * @return
     *     possible object is
     *     {@link UnsubscribeType }
     *     
     */
    public UnsubscribeType getUnsubscribe() {
        return unsubscribe;
    }

    /**
     * Sets the value of the unsubscribe property.
     * 
     * @param value
     *     allowed object is
     *     {@link UnsubscribeType }
     *     
     */
    public void setUnsubscribe(UnsubscribeType value) {
        this.unsubscribe = value;
    }

    public boolean isSetUnsubscribe() {
        return (this.unsubscribe!= null);
    }

}
