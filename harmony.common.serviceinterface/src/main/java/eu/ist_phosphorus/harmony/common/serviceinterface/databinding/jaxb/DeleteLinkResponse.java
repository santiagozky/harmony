
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
 *         &lt;element name="deleteLinkResponse" type="{http://ist_phosphorus.eu/nsp/webservice/topology}DeleteLinkResponseType"/>
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
    "deleteLinkResponse"
})
@XmlRootElement(name = "deleteLinkResponse", namespace = "http://ist_phosphorus.eu/nsp/webservice/topology")
public class DeleteLinkResponse
    implements Serializable, Cloneable
{
    public DeleteLinkResponse clone() throws CloneNotSupportedException {
        return (DeleteLinkResponse)super.clone();
    }

    @XmlElement(namespace = "http://ist_phosphorus.eu/nsp/webservice/topology", required = true)
    protected DeleteLinkResponseType deleteLinkResponse;

    /**
     * Gets the value of the deleteLinkResponse property.
     * 
     * @return
     *     possible object is
     *     {@link DeleteLinkResponseType }
     *     
     */
    public DeleteLinkResponseType getDeleteLinkResponse() {
        return deleteLinkResponse;
    }

    /**
     * Sets the value of the deleteLinkResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link DeleteLinkResponseType }
     *     
     */
    public void setDeleteLinkResponse(DeleteLinkResponseType value) {
        this.deleteLinkResponse = value;
    }

    public boolean isSetDeleteLinkResponse() {
        return (this.deleteLinkResponse!= null);
    }

}
