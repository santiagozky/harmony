
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
 *         &lt;element name="deleteLink" type="{http://ist_phosphorus.eu/nsp/webservice/topology}DeleteLinkType"/>
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
    "deleteLink"
})
@XmlRootElement(name = "deleteLink", namespace = "http://ist_phosphorus.eu/nsp/webservice/topology")
public class DeleteLink
    implements Serializable, Cloneable
{
    public DeleteLink clone() throws CloneNotSupportedException {
        return (DeleteLink)super.clone();
    }

    @XmlElement(namespace = "http://ist_phosphorus.eu/nsp/webservice/topology", required = true)
    protected DeleteLinkType deleteLink;

    /**
     * Gets the value of the deleteLink property.
     * 
     * @return
     *     possible object is
     *     {@link DeleteLinkType }
     *     
     */
    public DeleteLinkType getDeleteLink() {
        return deleteLink;
    }

    /**
     * Sets the value of the deleteLink property.
     * 
     * @param value
     *     allowed object is
     *     {@link DeleteLinkType }
     *     
     */
    public void setDeleteLink(DeleteLinkType value) {
        this.deleteLink = value;
    }

    public boolean isSetDeleteLink() {
        return (this.deleteLink!= null);
    }

}
