
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
 *         &lt;element name="publishResponse" type="{http://ist_phosphorus.eu/nsp/webservice/notification}publishResponseType"/>
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
    "publishResponse"
})
@XmlRootElement(name = "publishResponse", namespace = "http://ist_phosphorus.eu/nsp/webservice/notification")
public class PublishResponse
    implements Serializable, Cloneable
{
    public PublishResponse clone() throws CloneNotSupportedException {
        return (PublishResponse)super.clone();
    }

    @XmlElement(namespace = "http://ist_phosphorus.eu/nsp/webservice/notification", required = true)
    protected PublishResponseType publishResponse;

    /**
     * Gets the value of the publishResponse property.
     * 
     * @return
     *     possible object is
     *     {@link PublishResponseType }
     *     
     */
    public PublishResponseType getPublishResponse() {
        return publishResponse;
    }

    /**
     * Sets the value of the publishResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link PublishResponseType }
     *     
     */
    public void setPublishResponse(PublishResponseType value) {
        this.publishResponse = value;
    }

    public boolean isSetPublishResponse() {
        return (this.publishResponse!= null);
    }

}
