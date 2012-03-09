
package eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IsAvailableResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IsAvailableResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DetailedResult" type="{http://ist_phosphorus.eu/nsp/webservice/reservation}ConnectionAvailabilityType" maxOccurs="unbounded"/>
 *         &lt;element name="AlternativeStartTimeOffset" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IsAvailableResponseType", propOrder = {
    "detailedResult",
    "alternativeStartTimeOffset"
})
public class IsAvailableResponseType
    implements Serializable, Cloneable
{
    public IsAvailableResponseType clone() throws CloneNotSupportedException {
        return (IsAvailableResponseType)super.clone();
    }

    @XmlElement(name = "DetailedResult", required = true)
    protected List<ConnectionAvailabilityType> detailedResult;
    @XmlElement(name = "AlternativeStartTimeOffset")
    protected Long alternativeStartTimeOffset;

    /**
     * Gets the value of the detailedResult property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the detailedResult property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDetailedResult().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ConnectionAvailabilityType }
     * 
     * 
     */
    public List<ConnectionAvailabilityType> getDetailedResult() {
        if (detailedResult == null) {
            detailedResult = new ArrayList<ConnectionAvailabilityType>();
        }
        return this.detailedResult;
    }

    public boolean isSetDetailedResult() {
        return ((this.detailedResult!= null)&&(!this.detailedResult.isEmpty()));
    }

    public void unsetDetailedResult() {
        this.detailedResult = null;
    }

    /**
     * Gets the value of the alternativeStartTimeOffset property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getAlternativeStartTimeOffset() {
        return alternativeStartTimeOffset;
    }

    /**
     * Sets the value of the alternativeStartTimeOffset property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setAlternativeStartTimeOffset(Long value) {
        this.alternativeStartTimeOffset = value;
    }

    public boolean isSetAlternativeStartTimeOffset() {
        return (this.alternativeStartTimeOffset!= null);
    }

}
