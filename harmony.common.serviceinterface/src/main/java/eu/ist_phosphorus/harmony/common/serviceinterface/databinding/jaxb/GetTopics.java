
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
 *         &lt;element name="getTopics" type="{http://ist_phosphorus.eu/nsp/webservice/notification}getTopicsType"/>
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
    "getTopics"
})
@XmlRootElement(name = "getTopics", namespace = "http://ist_phosphorus.eu/nsp/webservice/notification")
public class GetTopics
    implements Serializable, Cloneable
{
    public GetTopics clone() throws CloneNotSupportedException {
        return (GetTopics)super.clone();
    }

    @XmlElement(namespace = "http://ist_phosphorus.eu/nsp/webservice/notification", required = true)
    protected GetTopicsType getTopics;

    /**
     * Gets the value of the getTopics property.
     * 
     * @return
     *     possible object is
     *     {@link GetTopicsType }
     *     
     */
    public GetTopicsType getGetTopics() {
        return getTopics;
    }

    /**
     * Sets the value of the getTopics property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetTopicsType }
     *     
     */
    public void setGetTopics(GetTopicsType value) {
        this.getTopics = value;
    }

    public boolean isSetGetTopics() {
        return (this.getTopics!= null);
    }

}
