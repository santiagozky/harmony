
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
 *         &lt;element name="getEndpointsResponse" type="{http://ist_phosphorus.eu/nsp/webservice/topology}GetEndpointsResponseType"/>
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
    "getEndpointsResponse"
})
@XmlRootElement(name = "getEndpointsResponse", namespace = "http://ist_phosphorus.eu/nsp/webservice/topology")
public class GetEndpointsResponse
    implements Serializable, Cloneable
{
    public GetEndpointsResponse clone() throws CloneNotSupportedException {
        return (GetEndpointsResponse)super.clone();
    }

    @XmlElement(namespace = "http://ist_phosphorus.eu/nsp/webservice/topology", required = true)
    protected GetEndpointsResponseType getEndpointsResponse;

    /**
     * Gets the value of the getEndpointsResponse property.
     * 
     * @return
     *     possible object is
     *     {@link GetEndpointsResponseType }
     *     
     */
    public GetEndpointsResponseType getGetEndpointsResponse() {
        return getEndpointsResponse;
    }

    /**
     * Sets the value of the getEndpointsResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetEndpointsResponseType }
     *     
     */
    public void setGetEndpointsResponse(GetEndpointsResponseType value) {
        this.getEndpointsResponse = value;
    }

    public boolean isSetGetEndpointsResponse() {
        return (this.getEndpointsResponse!= null);
    }

}
