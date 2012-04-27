
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
 *         &lt;element name="getPathStatusResponse" type="{http://ist_phosphorus.eu/gmpls/webservice}GetPathStatusResponseType"/>
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
    "getPathStatusResponse"
})
@XmlRootElement(name = "getPathStatusResponse")
public class GetPathStatusResponse {

    @XmlElement(required = true)
    protected GetPathStatusResponseType getPathStatusResponse;

    /**
     * Gets the value of the getPathStatusResponse property.
     * 
     * @return
     *     possible object is
     *     {@link GetPathStatusResponseType }
     *     
     */
    public GetPathStatusResponseType getGetPathStatusResponse() {
        return getPathStatusResponse;
    }

    /**
     * Sets the value of the getPathStatusResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetPathStatusResponseType }
     *     
     */
    public void setGetPathStatusResponse(GetPathStatusResponseType value) {
        this.getPathStatusResponse = value;
    }

}
