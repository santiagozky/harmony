
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
 *         &lt;element name="bindResponse" type="{http://ist_phosphorus.eu/nsp/webservice/reservation}BindResponseType"/>
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
    "bindResponse"
})
@XmlRootElement(name = "bindResponse")
public class BindResponse
    implements Serializable, Cloneable
{
    public BindResponse clone() throws CloneNotSupportedException {
        return (BindResponse)super.clone();
    }

    @XmlElement(required = true)
    protected BindResponseType bindResponse;

    /**
     * Gets the value of the bindResponse property.
     * 
     * @return
     *     possible object is
     *     {@link BindResponseType }
     *     
     */
    public BindResponseType getBindResponse() {
        return bindResponse;
    }

    /**
     * Sets the value of the bindResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link BindResponseType }
     *     
     */
    public void setBindResponse(BindResponseType value) {
        this.bindResponse = value;
    }

    public boolean isSetBindResponse() {
        return (this.bindResponse!= null);
    }

}
