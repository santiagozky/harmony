
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
 *         &lt;element name="getDomains" type="{http://ist_phosphorus.eu/nsp/webservice/topology}GetDomainsType"/>
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
    "getDomains"
})
@XmlRootElement(name = "getDomains", namespace = "http://ist_phosphorus.eu/nsp/webservice/topology")
public class GetDomains
    implements Serializable, Cloneable
{
    public GetDomains clone() throws CloneNotSupportedException {
        return (GetDomains)super.clone();
    }

    @XmlElement(namespace = "http://ist_phosphorus.eu/nsp/webservice/topology", required = true)
    protected GetDomainsType getDomains;

    /**
     * Gets the value of the getDomains property.
     * 
     * @return
     *     possible object is
     *     {@link GetDomainsType }
     *     
     */
    public GetDomainsType getGetDomains() {
        return getDomains;
    }

    /**
     * Sets the value of the getDomains property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetDomainsType }
     *     
     */
    public void setGetDomains(GetDomainsType value) {
        this.getDomains = value;
    }

    public boolean isSetGetDomains() {
        return (this.getDomains!= null);
    }

}
