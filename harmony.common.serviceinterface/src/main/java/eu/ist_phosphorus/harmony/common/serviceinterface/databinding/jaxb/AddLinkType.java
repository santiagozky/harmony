
package eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AddLinkType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AddLinkType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="LinkData" type="{http://ist_phosphorus.eu/nsp/webservice/topology}Link"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AddLinkType", namespace = "http://ist_phosphorus.eu/nsp/webservice/topology", propOrder = {
    "linkData"
})
public class AddLinkType
    implements Serializable, Cloneable
{
    public AddLinkType clone() throws CloneNotSupportedException {
        return (AddLinkType)super.clone();
    }

    @XmlElement(name = "LinkData", required = true)
    protected Link linkData;

    /**
     * Gets the value of the linkData property.
     * 
     * @return
     *     possible object is
     *     {@link Link }
     *     
     */
    public Link getLinkData() {
        return linkData;
    }

    /**
     * Sets the value of the linkData property.
     * 
     * @param value
     *     allowed object is
     *     {@link Link }
     *     
     */
    public void setLinkData(Link value) {
        this.linkData = value;
    }

    public boolean isSetLinkData() {
        return (this.linkData!= null);
    }

}