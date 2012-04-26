
package org.opennaas.extension.gmpls.serviceinterface.databinding.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CreatePathResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CreatePathResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PathIdentifier" type="{http://ist_phosphorus.eu/gmpls/webservice}PathIdentifierType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CreatePathResponseType", propOrder = {
    "pathIdentifier"
})
public class CreatePathResponseType {

    @XmlElement(name = "PathIdentifier", required = true)
    protected PathIdentifierType pathIdentifier;

    /**
     * Gets the value of the pathIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link PathIdentifierType }
     *     
     */
    public PathIdentifierType getPathIdentifier() {
        return pathIdentifier;
    }

    /**
     * Sets the value of the pathIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link PathIdentifierType }
     *     
     */
    public void setPathIdentifier(PathIdentifierType value) {
        this.pathIdentifier = value;
    }

}
