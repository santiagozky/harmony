
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
 *         &lt;element name="publish" type="{http://ist_phosphorus.eu/nsp/webservice/notification}publishType"/>
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
    "publish"
})
@XmlRootElement(name = "publish", namespace = "http://ist_phosphorus.eu/nsp/webservice/notification")
public class Publish
    implements Serializable, Cloneable
{
    public Publish clone() throws CloneNotSupportedException {
        return (Publish)super.clone();
    }

    @XmlElement(namespace = "http://ist_phosphorus.eu/nsp/webservice/notification", required = true)
    protected PublishType publish;

    /**
     * Gets the value of the publish property.
     * 
     * @return
     *     possible object is
     *     {@link PublishType }
     *     
     */
    public PublishType getPublish() {
        return publish;
    }

    /**
     * Sets the value of the publish property.
     * 
     * @param value
     *     allowed object is
     *     {@link PublishType }
     *     
     */
    public void setPublish(PublishType value) {
        this.publish = value;
    }

    public boolean isSetPublish() {
        return (this.publish!= null);
    }

}
