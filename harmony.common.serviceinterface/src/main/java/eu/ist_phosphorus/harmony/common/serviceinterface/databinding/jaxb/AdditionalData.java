
package eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Port itional Data
 * 
 * <p>Java class for AdditionalData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AdditionalData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Parameter" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="AssociatedPort" type="{http://ist_phosphorus.eu/nsp}EndpointType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AdditionalData", namespace = "http://ist_phosphorus.eu/nsp/webservice/topology", propOrder = {
    "parameter",
    "associatedPort"
})
public class AdditionalData
    implements Serializable, Cloneable
{
    public AdditionalData clone() throws CloneNotSupportedException {
        return (AdditionalData)super.clone();
    }

    @XmlElement(name = "Parameter", required = true)
    protected String parameter;
    @XmlElement(name = "AssociatedPort")
    protected EndpointType associatedPort;

    /**
     * Gets the value of the parameter property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParameter() {
        return parameter;
    }

    /**
     * Sets the value of the parameter property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParameter(String value) {
        this.parameter = value;
    }

    public boolean isSetParameter() {
        return (this.parameter!= null);
    }

    /**
     * Gets the value of the associatedPort property.
     * 
     * @return
     *     possible object is
     *     {@link EndpointType }
     *     
     */
    public EndpointType getAssociatedPort() {
        return associatedPort;
    }

    /**
     * Sets the value of the associatedPort property.
     * 
     * @param value
     *     allowed object is
     *     {@link EndpointType }
     *     
     */
    public void setAssociatedPort(EndpointType value) {
        this.associatedPort = value;
    }

    public boolean isSetAssociatedPort() {
        return (this.associatedPort!= null);
    }

}
