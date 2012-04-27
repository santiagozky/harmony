
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
 *         &lt;element name="getPathDiscoveryResponse" type="{http://ist_phosphorus.eu/gmpls/webservice}GetPathDiscoveryResponseType"/>
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
    "getPathDiscoveryResponse"
})
@XmlRootElement(name = "getPathDiscoveryResponse")
public class GetPathDiscoveryResponse {

    @XmlElement(required = true)
    protected GetPathDiscoveryResponseType getPathDiscoveryResponse;

    /**
     * Gets the value of the getPathDiscoveryResponse property.
     * 
     * @return
     *     possible object is
     *     {@link GetPathDiscoveryResponseType }
     *     
     */
    public GetPathDiscoveryResponseType getGetPathDiscoveryResponse() {
        return getPathDiscoveryResponse;
    }

    /**
     * Sets the value of the getPathDiscoveryResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetPathDiscoveryResponseType }
     *     
     */
    public void setGetPathDiscoveryResponse(GetPathDiscoveryResponseType value) {
        this.getPathDiscoveryResponse = value;
    }

}
