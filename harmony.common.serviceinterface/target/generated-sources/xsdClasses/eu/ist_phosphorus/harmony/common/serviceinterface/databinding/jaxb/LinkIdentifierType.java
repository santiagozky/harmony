//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-b10 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.03.07 at 10:16:02 AM CET 
//


package eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LinkIdentifierType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LinkIdentifierType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SourceEndpoint" type="{http://ist_phosphorus.eu/nsp}EndpointIdentifierType"/>
 *         &lt;element name="DestinationEndpoint" type="{http://ist_phosphorus.eu/nsp}EndpointIdentifierType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LinkIdentifierType", namespace = "http://ist_phosphorus.eu/nsp/webservice/topology", propOrder = {
    "sourceEndpoint",
    "destinationEndpoint"
})
@XmlSeeAlso({
    Link.class
})
public class LinkIdentifierType
    implements Serializable
{

    @XmlElement(name = "SourceEndpoint", required = true)
    protected String sourceEndpoint;
    @XmlElement(name = "DestinationEndpoint", required = true)
    protected String destinationEndpoint;

    /**
     * Gets the value of the sourceEndpoint property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourceEndpoint() {
        return sourceEndpoint;
    }

    /**
     * Sets the value of the sourceEndpoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourceEndpoint(String value) {
        this.sourceEndpoint = value;
    }

    public boolean isSetSourceEndpoint() {
        return (this.sourceEndpoint!= null);
    }

    /**
     * Gets the value of the destinationEndpoint property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDestinationEndpoint() {
        return destinationEndpoint;
    }

    /**
     * Sets the value of the destinationEndpoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDestinationEndpoint(String value) {
        this.destinationEndpoint = value;
    }

    public boolean isSetDestinationEndpoint() {
        return (this.destinationEndpoint!= null);
    }

}
