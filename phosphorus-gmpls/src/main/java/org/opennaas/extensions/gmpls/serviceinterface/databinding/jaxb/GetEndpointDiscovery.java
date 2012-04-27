
package org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb;

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
 *         &lt;element name="getEndpointDiscovery" type="{http://ist_phosphorus.eu/gmpls/webservice}getEndpointDiscoveryType"/>
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
    "getEndpointDiscovery"
})
@XmlRootElement(name = "getEndpointDiscovery")
public class GetEndpointDiscovery {

    @XmlElement(required = true)
    protected GetEndpointDiscoveryType getEndpointDiscovery;

    /**
     * Gets the value of the getEndpointDiscovery property.
     * 
     * @return
     *     possible object is
     *     {@link GetEndpointDiscoveryType }
     *     
     */
    public GetEndpointDiscoveryType getGetEndpointDiscovery() {
        return getEndpointDiscovery;
    }

    /**
     * Sets the value of the getEndpointDiscovery property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetEndpointDiscoveryType }
     *     
     */
    public void setGetEndpointDiscovery(GetEndpointDiscoveryType value) {
        this.getEndpointDiscovery = value;
    }

}
