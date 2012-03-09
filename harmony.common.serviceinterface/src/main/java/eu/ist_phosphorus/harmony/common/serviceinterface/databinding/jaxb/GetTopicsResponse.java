
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
 *         &lt;element name="getTopicsResponse" type="{http://ist_phosphorus.eu/nsp/webservice/notification}getTopicsResponseType"/>
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
    "getTopicsResponse"
})
@XmlRootElement(name = "getTopicsResponse", namespace = "http://ist_phosphorus.eu/nsp/webservice/notification")
public class GetTopicsResponse
    implements Serializable, Cloneable
{
    public GetTopicsResponse clone() throws CloneNotSupportedException {
        return (GetTopicsResponse)super.clone();
    }

    @XmlElement(namespace = "http://ist_phosphorus.eu/nsp/webservice/notification", required = true)
    protected GetTopicsResponseType getTopicsResponse;

    /**
     * Gets the value of the getTopicsResponse property.
     * 
     * @return
     *     possible object is
     *     {@link GetTopicsResponseType }
     *     
     */
    public GetTopicsResponseType getGetTopicsResponse() {
        return getTopicsResponse;
    }

    /**
     * Sets the value of the getTopicsResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetTopicsResponseType }
     *     
     */
    public void setGetTopicsResponse(GetTopicsResponseType value) {
        this.getTopicsResponse = value;
    }

    public boolean isSetGetTopicsResponse() {
        return (this.getTopicsResponse!= null);
    }

}
