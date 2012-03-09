
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
 *         &lt;element name="removeTopicResponse" type="{http://ist_phosphorus.eu/nsp/webservice/notification}removeTopicResponseType"/>
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
    "removeTopicResponse"
})
@XmlRootElement(name = "removeTopicResponse", namespace = "http://ist_phosphorus.eu/nsp/webservice/notification")
public class RemoveTopicResponse
    implements Serializable, Cloneable
{
    public RemoveTopicResponse clone() throws CloneNotSupportedException {
        return (RemoveTopicResponse)super.clone();
    }

    @XmlElement(namespace = "http://ist_phosphorus.eu/nsp/webservice/notification", required = true)
    protected RemoveTopicResponseType removeTopicResponse;

    /**
     * Gets the value of the removeTopicResponse property.
     * 
     * @return
     *     possible object is
     *     {@link RemoveTopicResponseType }
     *     
     */
    public RemoveTopicResponseType getRemoveTopicResponse() {
        return removeTopicResponse;
    }

    /**
     * Sets the value of the removeTopicResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link RemoveTopicResponseType }
     *     
     */
    public void setRemoveTopicResponse(RemoveTopicResponseType value) {
        this.removeTopicResponse = value;
    }

    public boolean isSetRemoveTopicResponse() {
        return (this.removeTopicResponse!= null);
    }

}
