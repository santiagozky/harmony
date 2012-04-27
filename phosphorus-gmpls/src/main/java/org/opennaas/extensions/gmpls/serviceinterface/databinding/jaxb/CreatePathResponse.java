
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
 *         &lt;element name="createPathResponse" type="{http://ist_phosphorus.eu/gmpls/webservice}CreatePathResponseType"/>
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
    "createPathResponse"
})
@XmlRootElement(name = "createPathResponse")
public class CreatePathResponse {

    @XmlElement(required = true)
    protected CreatePathResponseType createPathResponse;

    /**
     * Gets the value of the createPathResponse property.
     * 
     * @return
     *     possible object is
     *     {@link CreatePathResponseType }
     *     
     */
    public CreatePathResponseType getCreatePathResponse() {
        return createPathResponse;
    }

    /**
     * Sets the value of the createPathResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link CreatePathResponseType }
     *     
     */
    public void setCreatePathResponse(CreatePathResponseType value) {
        this.createPathResponse = value;
    }

}
