
package org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PathIdentifierType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PathIdentifierType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PathIdentifier" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PathIdentifierType", propOrder = {
    "pathIdentifier"
})
public class PathIdentifierType {

    @XmlElement(name = "PathIdentifier")
    protected int pathIdentifier;

    /**
     * Gets the value of the pathIdentifier property.
     * 
     */
    public int getPathIdentifier() {
        return pathIdentifier;
    }

    /**
     * Sets the value of the pathIdentifier property.
     * 
     */
    public void setPathIdentifier(int value) {
        this.pathIdentifier = value;
    }

}
