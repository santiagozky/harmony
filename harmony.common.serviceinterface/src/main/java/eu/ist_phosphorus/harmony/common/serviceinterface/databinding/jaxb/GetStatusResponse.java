
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
 *         &lt;element name="getStatusResponse" type="{http://ist_phosphorus.eu/nsp/webservice/reservation}GetStatusResponseType"/>
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
    "getStatusResponse"
})
@XmlRootElement(name = "getStatusResponse")
public class GetStatusResponse
    implements Serializable, Cloneable
{
    public GetStatusResponse clone() throws CloneNotSupportedException {
        return (GetStatusResponse)super.clone();
    }

    @XmlElement(required = true)
    protected GetStatusResponseType getStatusResponse;

    /**
     * Gets the value of the getStatusResponse property.
     * 
     * @return
     *     possible object is
     *     {@link GetStatusResponseType }
     *     
     */
    public GetStatusResponseType getGetStatusResponse() {
        return getStatusResponse;
    }

    /**
     * Sets the value of the getStatusResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetStatusResponseType }
     *     
     */
    public void setGetStatusResponse(GetStatusResponseType value) {
        this.getStatusResponse = value;
    }

    public boolean isSetGetStatusResponse() {
        return (this.getStatusResponse!= null);
    }

}
