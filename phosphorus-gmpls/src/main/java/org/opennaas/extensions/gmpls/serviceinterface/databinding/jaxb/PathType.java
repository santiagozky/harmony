
package org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PathType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PathType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SourceTNA" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DestinationTNA" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Bandwidth" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PathType", propOrder = {
    "sourceTNA",
    "destinationTNA",
    "bandwidth"
})
public class PathType {

    @XmlElement(name = "SourceTNA", required = true)
    protected String sourceTNA;
    @XmlElement(name = "DestinationTNA", required = true)
    protected String destinationTNA;
    @XmlElement(name = "Bandwidth")
    protected int bandwidth;

    /**
     * Gets the value of the sourceTNA property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourceTNA() {
        return sourceTNA;
    }

    /**
     * Sets the value of the sourceTNA property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourceTNA(String value) {
        this.sourceTNA = value;
    }

    /**
     * Gets the value of the destinationTNA property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDestinationTNA() {
        return destinationTNA;
    }

    /**
     * Sets the value of the destinationTNA property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDestinationTNA(String value) {
        this.destinationTNA = value;
    }

    /**
     * Gets the value of the bandwidth property.
     * 
     */
    public int getBandwidth() {
        return bandwidth;
    }

    /**
     * Sets the value of the bandwidth property.
     * 
     */
    public void setBandwidth(int value) {
        this.bandwidth = value;
    }

}
