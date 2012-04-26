
package org.opennaas.extension.gmpls.serviceinterface.databinding.jaxb;

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
 *         &lt;element name="createPath" type="{http://ist_phosphorus.eu/gmpls/webservice}CreatePathType"/>
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
    "createPath"
})
@XmlRootElement(name = "createPath")
public class CreatePath {

    @XmlElement(required = true)
    protected CreatePathType createPath;

    /**
     * Gets the value of the createPath property.
     * 
     * @return
     *     possible object is
     *     {@link CreatePathType }
     *     
     */
    public CreatePathType getCreatePath() {
        return createPath;
    }

    /**
     * Sets the value of the createPath property.
     * 
     * @param value
     *     allowed object is
     *     {@link CreatePathType }
     *     
     */
    public void setCreatePath(CreatePathType value) {
        this.createPath = value;
    }

}
