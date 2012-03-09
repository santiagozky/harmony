
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
 *         &lt;element name="addTopicResponse" type="{http://ist_phosphorus.eu/nsp/webservice/notification}addTopicResponseType"/>
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
    "addTopicResponse"
})
@XmlRootElement(name = "addTopicResponse", namespace = "http://ist_phosphorus.eu/nsp/webservice/notification")
public class AddTopicResponse
    implements Serializable, Cloneable
{
    public AddTopicResponse clone() throws CloneNotSupportedException {
        return (AddTopicResponse)super.clone();
    }

    @XmlElement(namespace = "http://ist_phosphorus.eu/nsp/webservice/notification", required = true)
    protected AddTopicResponseType addTopicResponse;

    /**
     * Gets the value of the addTopicResponse property.
     * 
     * @return
     *     possible object is
     *     {@link AddTopicResponseType }
     *     
     */
    public AddTopicResponseType getAddTopicResponse() {
        return addTopicResponse;
    }

    /**
     * Sets the value of the addTopicResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddTopicResponseType }
     *     
     */
    public void setAddTopicResponse(AddTopicResponseType value) {
        this.addTopicResponse = value;
    }

    public boolean isSetAddTopicResponse() {
        return (this.addTopicResponse!= null);
    }

}
