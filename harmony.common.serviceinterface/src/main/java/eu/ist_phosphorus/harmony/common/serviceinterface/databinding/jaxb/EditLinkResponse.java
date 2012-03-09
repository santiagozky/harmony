
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
 *         &lt;element name="editLinkResponse" type="{http://ist_phosphorus.eu/nsp/webservice/topology}EditLinkResponseType"/>
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
    "editLinkResponse"
})
@XmlRootElement(name = "editLinkResponse", namespace = "http://ist_phosphorus.eu/nsp/webservice/topology")
public class EditLinkResponse
    implements Serializable, Cloneable
{
    public EditLinkResponse clone() throws CloneNotSupportedException {
        return (EditLinkResponse)super.clone();
    }

    @XmlElement(namespace = "http://ist_phosphorus.eu/nsp/webservice/topology", required = true)
    protected EditLinkResponseType editLinkResponse;

    /**
     * Gets the value of the editLinkResponse property.
     * 
     * @return
     *     possible object is
     *     {@link EditLinkResponseType }
     *     
     */
    public EditLinkResponseType getEditLinkResponse() {
        return editLinkResponse;
    }

    /**
     * Sets the value of the editLinkResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link EditLinkResponseType }
     *     
     */
    public void setEditLinkResponse(EditLinkResponseType value) {
        this.editLinkResponse = value;
    }

    public boolean isSetEditLinkResponse() {
        return (this.editLinkResponse!= null);
    }

}
