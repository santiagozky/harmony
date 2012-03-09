
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
 *         &lt;element name="getLinks" type="{http://ist_phosphorus.eu/nsp/webservice/topology}GetLinksType"/>
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
    "getLinks"
})
@XmlRootElement(name = "getLinks", namespace = "http://ist_phosphorus.eu/nsp/webservice/topology")
public class GetLinks
    implements Serializable, Cloneable
{
    public GetLinks clone() throws CloneNotSupportedException {
        return (GetLinks)super.clone();
    }

    @XmlElement(namespace = "http://ist_phosphorus.eu/nsp/webservice/topology", required = true)
    protected GetLinksType getLinks;

    /**
     * Gets the value of the getLinks property.
     * 
     * @return
     *     possible object is
     *     {@link GetLinksType }
     *     
     */
    public GetLinksType getGetLinks() {
        return getLinks;
    }

    /**
     * Sets the value of the getLinks property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetLinksType }
     *     
     */
    public void setGetLinks(GetLinksType value) {
        this.getLinks = value;
    }

    public boolean isSetGetLinks() {
        return (this.getLinks!= null);
    }

}
