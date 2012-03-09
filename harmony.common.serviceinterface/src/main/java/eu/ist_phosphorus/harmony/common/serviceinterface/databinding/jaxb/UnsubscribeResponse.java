
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
 *         &lt;element name="unsubscribeResponse" type="{http://ist_phosphorus.eu/nsp/webservice/notification}unsubscribeResponseType"/>
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
    "unsubscribeResponse"
})
@XmlRootElement(name = "unsubscribeResponse", namespace = "http://ist_phosphorus.eu/nsp/webservice/notification")
public class UnsubscribeResponse
    implements Serializable, Cloneable
{
    public UnsubscribeResponse clone() throws CloneNotSupportedException {
        return (UnsubscribeResponse)super.clone();
    }

    @XmlElement(namespace = "http://ist_phosphorus.eu/nsp/webservice/notification", required = true)
    protected UnsubscribeResponseType unsubscribeResponse;

    /**
     * Gets the value of the unsubscribeResponse property.
     * 
     * @return
     *     possible object is
     *     {@link UnsubscribeResponseType }
     *     
     */
    public UnsubscribeResponseType getUnsubscribeResponse() {
        return unsubscribeResponse;
    }

    /**
     * Sets the value of the unsubscribeResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link UnsubscribeResponseType }
     *     
     */
    public void setUnsubscribeResponse(UnsubscribeResponseType value) {
        this.unsubscribeResponse = value;
    }

    public boolean isSetUnsubscribeResponse() {
        return (this.unsubscribeResponse!= null);
    }

}
