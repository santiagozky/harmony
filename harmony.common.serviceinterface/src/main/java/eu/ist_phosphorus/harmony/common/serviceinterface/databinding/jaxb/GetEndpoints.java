
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
    implements Serializable, Cloneable
{
    public GetEndpoints clone() throws CloneNotSupportedException {
        return (GetEndpoints)super.clone();
    }

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
