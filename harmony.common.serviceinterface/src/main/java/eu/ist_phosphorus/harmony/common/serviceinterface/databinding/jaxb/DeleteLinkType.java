
package eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DeleteLinkType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DeleteLinkType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="LinkId" type="{http://ist_phosphorus.eu/nsp/webservice/topology}LinkIdentifierType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DeleteLinkType", namespace = "http://ist_phosphorus.eu/nsp/webservice/topology", propOrder = {
    "linkId"
})
public class DeleteLinkType
    implements Serializable, Cloneable
{
    public DeleteLinkType clone() throws CloneNotSupportedException {
        return (DeleteLinkType)super.clone();
    }

    @XmlElement(name = "LinkId", required = true)
    protected LinkIdentifierType linkId;

    /**
     * Gets the value of the linkId property.
     * 
     * @return
     *     possible object is
     *     {@link LinkIdentifierType }
     *     
     */
    public LinkIdentifierType getLinkId() {
        return linkId;
    }

    /**
     * Sets the value of the linkId property.
     * 
     * @param value
     *     allowed object is
     *     {@link LinkIdentifierType }
     *     
     */
    public void setLinkId(LinkIdentifierType value) {
        this.linkId = value;
    }

    public boolean isSetLinkId() {
        return (this.linkId!= null);
    }

}