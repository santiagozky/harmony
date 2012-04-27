
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
 *         &lt;element name="getEndpointDiscoveryResponse" type="{http://ist_phosphorus.eu/gmpls/webservice}getEndpointDiscoveryResponseType"/>
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
    "getEndpointDiscoveryResponse"
})
@XmlRootElement(name = "getEndpointDiscoveryResponse")
public class GetEndpointDiscoveryResponse {

    @XmlElement(required = true)
    protected GetEndpointDiscoveryResponseType getEndpointDiscoveryResponse;

    /**
     * Gets the value of the getEndpointDiscoveryResponse property.
     * 
     * @return
     *     possible object is
     *     {@link GetEndpointDiscoveryResponseType }
     *     
     */
    public GetEndpointDiscoveryResponseType getGetEndpointDiscoveryResponse() {
        return getEndpointDiscoveryResponse;
    }

    /**
     * Sets the value of the getEndpointDiscoveryResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetEndpointDiscoveryResponseType }
     *     
     */
    public void setGetEndpointDiscoveryResponse(GetEndpointDiscoveryResponseType value) {
        this.getEndpointDiscoveryResponse = value;
    }

}
