
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
 *         &lt;element name="terminatePath" type="{http://ist_phosphorus.eu/gmpls/webservice}TerminatePathType"/>
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
    "terminatePath"
})
@XmlRootElement(name = "terminatePath")
public class TerminatePath {

    @XmlElement(required = true)
    protected TerminatePathType terminatePath;

    /**
     * Gets the value of the terminatePath property.
     * 
     * @return
     *     possible object is
     *     {@link TerminatePathType }
     *     
     */
    public TerminatePathType getTerminatePath() {
        return terminatePath;
    }

    /**
     * Sets the value of the terminatePath property.
     * 
     * @param value
     *     allowed object is
     *     {@link TerminatePathType }
     *     
     */
    public void setTerminatePath(TerminatePathType value) {
        this.terminatePath = value;
    }

}
