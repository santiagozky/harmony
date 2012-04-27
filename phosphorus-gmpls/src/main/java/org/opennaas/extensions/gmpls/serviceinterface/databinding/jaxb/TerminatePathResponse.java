
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
 *         &lt;element name="terminatePathResponse" type="{http://ist_phosphorus.eu/gmpls/webservice}TerminatePathResponseType"/>
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
    "terminatePathResponse"
})
@XmlRootElement(name = "terminatePathResponse")
public class TerminatePathResponse {

    @XmlElement(required = true)
    protected TerminatePathResponseType terminatePathResponse;

    /**
     * Gets the value of the terminatePathResponse property.
     * 
     * @return
     *     possible object is
     *     {@link TerminatePathResponseType }
     *     
     */
    public TerminatePathResponseType getTerminatePathResponse() {
        return terminatePathResponse;
    }

    /**
     * Sets the value of the terminatePathResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link TerminatePathResponseType }
     *     
     */
    public void setTerminatePathResponse(TerminatePathResponseType value) {
        this.terminatePathResponse = value;
    }

}
