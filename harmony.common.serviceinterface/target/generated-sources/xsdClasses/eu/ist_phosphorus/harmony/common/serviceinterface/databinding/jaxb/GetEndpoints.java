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
 *         &lt;element name="getEndpoints" type="{http://ist_phosphorus.eu/nsp/webservice/topology}GetEndpointsType"/>
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
    "getEndpoints"
})
@XmlRootElement(name = "getEndpoints", namespace = "http://ist_phosphorus.eu/nsp/webservice/topology")
public class GetEndpoints
    implements Serializable
{

    @XmlElement(namespace = "http://ist_phosphorus.eu/nsp/webservice/topology", required = true)
    protected GetEndpointsType getEndpoints;

    /**
     * Gets the value of the getEndpoints property.
     * 
     * @return
     *     possible object is
     *     {@link GetEndpointsType }
     *     
     */
    public GetEndpointsType getGetEndpoints() {
        return getEndpoints;
    }

    /**
     * Sets the value of the getEndpoints property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetEndpointsType }
     *     
     */
    public void setGetEndpoints(GetEndpointsType value) {
        this.getEndpoints = value;
    }

    public boolean isSetGetEndpoints() {
        return (this.getEndpoints!= null);
    }

}
