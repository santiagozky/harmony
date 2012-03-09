
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
 *         &lt;element name="addLink" type="{http://ist_phosphorus.eu/nsp/webservice/topology}AddLinkType"/>
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
    "addLink"
})
@XmlRootElement(name = "addLink", namespace = "http://ist_phosphorus.eu/nsp/webservice/topology")
public class AddLink
    implements Serializable, Cloneable
{
    public AddLink clone() throws CloneNotSupportedException {
        return (AddLink)super.clone();
    }

    @XmlElement(namespace = "http://ist_phosphorus.eu/nsp/webservice/topology", required = true)
    protected AddLinkType addLink;

    /**
     * Gets the value of the addLink property.
     * 
     * @return
     *     possible object is
     *     {@link AddLinkType }
     *     
     */
    public AddLinkType getAddLink() {
        return addLink;
    }

    /**
     * Sets the value of the addLink property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddLinkType }
     *     
     */
    public void setAddLink(AddLinkType value) {
        this.addLink = value;
    }

    public boolean isSetAddLink() {
        return (this.addLink!= null);
    }

}
