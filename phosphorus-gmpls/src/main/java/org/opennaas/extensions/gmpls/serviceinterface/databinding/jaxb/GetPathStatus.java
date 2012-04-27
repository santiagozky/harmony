
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
 *         &lt;element name="getPathStatus" type="{http://ist_phosphorus.eu/gmpls/webservice}GetPathStatusType"/>
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
    "getPathStatus"
})
@XmlRootElement(name = "getPathStatus")
public class GetPathStatus {

    @XmlElement(required = true)
    protected GetPathStatusType getPathStatus;

    /**
     * Gets the value of the getPathStatus property.
     * 
     * @return
     *     possible object is
     *     {@link GetPathStatusType }
     *     
     */
    public GetPathStatusType getGetPathStatus() {
        return getPathStatus;
    }

    /**
     * Sets the value of the getPathStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetPathStatusType }
     *     
     */
    public void setGetPathStatus(GetPathStatusType value) {
        this.getPathStatus = value;
    }

}
