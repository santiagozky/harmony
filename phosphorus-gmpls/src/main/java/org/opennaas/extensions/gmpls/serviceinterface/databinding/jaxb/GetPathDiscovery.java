
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
 *         &lt;element name="getPathDiscovery" type="{http://ist_phosphorus.eu/gmpls/webservice}GetPathDiscoveryType"/>
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
    "getPathDiscovery"
})
@XmlRootElement(name = "getPathDiscovery")
public class GetPathDiscovery {

    @XmlElement(required = true)
    protected GetPathDiscoveryType getPathDiscovery;

    /**
     * Gets the value of the getPathDiscovery property.
     * 
     * @return
     *     possible object is
     *     {@link GetPathDiscoveryType }
     *     
     */
    public GetPathDiscoveryType getGetPathDiscovery() {
        return getPathDiscovery;
    }

    /**
     * Sets the value of the getPathDiscovery property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetPathDiscoveryType }
     *     
     */
    public void setGetPathDiscovery(GetPathDiscoveryType value) {
        this.getPathDiscovery = value;
    }

}
