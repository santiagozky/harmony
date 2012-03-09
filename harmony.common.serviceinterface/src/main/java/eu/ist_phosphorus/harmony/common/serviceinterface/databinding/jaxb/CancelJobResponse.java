
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
 *         &lt;element name="cancelJobResponse" type="{http://ist_phosphorus.eu/nsp/webservice/reservation}CancelJobResponseType"/>
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
    "cancelJobResponse"
})
@XmlRootElement(name = "cancelJobResponse")
public class CancelJobResponse
    implements Serializable, Cloneable
{
    public CancelJobResponse clone() throws CloneNotSupportedException {
        return (CancelJobResponse)super.clone();
    }

    @XmlElement(required = true)
    protected CancelJobResponseType cancelJobResponse;

    /**
     * Gets the value of the cancelJobResponse property.
     * 
     * @return
     *     possible object is
     *     {@link CancelJobResponseType }
     *     
     */
    public CancelJobResponseType getCancelJobResponse() {
        return cancelJobResponse;
    }

    /**
     * Sets the value of the cancelJobResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link CancelJobResponseType }
     *     
     */
    public void setCancelJobResponse(CancelJobResponseType value) {
        this.cancelJobResponse = value;
    }

    public boolean isSetCancelJobResponse() {
        return (this.cancelJobResponse!= null);
    }

}
