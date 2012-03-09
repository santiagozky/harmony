
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
 *         &lt;element name="getDomainsResponse" type="{http://ist_phosphorus.eu/nsp/webservice/topology}GetDomainsResponseType"/>
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
    "getDomainsResponse"
})
@XmlRootElement(name = "getDomainsResponse", namespace = "http://ist_phosphorus.eu/nsp/webservice/topology")
public class GetDomainsResponse
    implements Serializable, Cloneable
{
    public GetDomainsResponse clone() throws CloneNotSupportedException {
        return (GetDomainsResponse)super.clone();
    }

    @XmlElement(namespace = "http://ist_phosphorus.eu/nsp/webservice/topology", required = true)
    protected GetDomainsResponseType getDomainsResponse;

    /**
     * Gets the value of the getDomainsResponse property.
     * 
     * @return
     *     possible object is
     *     {@link GetDomainsResponseType }
     *     
     */
    public GetDomainsResponseType getGetDomainsResponse() {
        return getDomainsResponse;
    }

    /**
     * Sets the value of the getDomainsResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetDomainsResponseType }
     *     
     */
    public void setGetDomainsResponse(GetDomainsResponseType value) {
        this.getDomainsResponse = value;
    }

    public boolean isSetGetDomainsResponse() {
        return (this.getDomainsResponse!= null);
    }

}
