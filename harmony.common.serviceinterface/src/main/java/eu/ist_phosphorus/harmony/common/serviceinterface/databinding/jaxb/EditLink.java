
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
 *         &lt;element name="editLink" type="{http://ist_phosphorus.eu/nsp/webservice/topology}EditLinkType"/>
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
    "editLink"
})
@XmlRootElement(name = "editLink", namespace = "http://ist_phosphorus.eu/nsp/webservice/topology")
public class EditLink
    implements Serializable, Cloneable
{
    public EditLink clone() throws CloneNotSupportedException {
        return (EditLink)super.clone();
    }

    @XmlElement(namespace = "http://ist_phosphorus.eu/nsp/webservice/topology", required = true)
    protected EditLinkType editLink;

    /**
     * Gets the value of the editLink property.
     * 
     * @return
     *     possible object is
     *     {@link EditLinkType }
     *     
     */
    public EditLinkType getEditLink() {
        return editLink;
    }

    /**
     * Sets the value of the editLink property.
     * 
     * @param value
     *     allowed object is
     *     {@link EditLinkType }
     *     
     */
    public void setEditLink(EditLinkType value) {
        this.editLink = value;
    }

    public boolean isSetEditLink() {
        return (this.editLink!= null);
    }

}
