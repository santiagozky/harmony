
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
 *         &lt;element name="getLinksResponse" type="{http://ist_phosphorus.eu/nsp/webservice/topology}GetLinksResponseType"/>
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
    "getLinksResponse"
})
@XmlRootElement(name = "getLinksResponse", namespace = "http://ist_phosphorus.eu/nsp/webservice/topology")
public class GetLinksResponse
    implements Serializable, Cloneable
{
    public GetLinksResponse clone() throws CloneNotSupportedException {
        return (GetLinksResponse)super.clone();
    }

    @XmlElement(namespace = "http://ist_phosphorus.eu/nsp/webservice/topology", required = true)
    protected GetLinksResponseType getLinksResponse;

    /**
     * Gets the value of the getLinksResponse property.
     * 
     * @return
     *     possible object is
     *     {@link GetLinksResponseType }
     *     
     */
    public GetLinksResponseType getGetLinksResponse() {
        return getLinksResponse;
    }

    /**
     * Sets the value of the getLinksResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetLinksResponseType }
     *     
     */
    public void setGetLinksResponse(GetLinksResponseType value) {
        this.getLinksResponse = value;
    }

    public boolean isSetGetLinksResponse() {
        return (this.getLinksResponse!= null);
    }

}
