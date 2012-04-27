
package org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TerminatePathType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TerminatePathType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PathIdentifier" type="{http://ist_phosphorus.eu/gmpls/webservice}PathIdentifierType"/>
 *         &lt;element name="Status" type="{http://ist_phosphorus.eu/gmpls/webservice}StatusType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TerminatePathType", propOrder = {
    "pathIdentifier",
    "status"
})
public class TerminatePathType {

    @XmlElement(name = "PathIdentifier", required = true)
    protected PathIdentifierType pathIdentifier;
    @XmlElement(name = "Status", required = true)
    protected StatusType status;

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

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link StatusType }
     *     
     */
    public StatusType getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link StatusType }
     *     
     */
    public void setStatus(StatusType value) {
        this.status = value;
    }

}
