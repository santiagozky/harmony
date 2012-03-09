
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
 *         &lt;element name="completeJob" type="{http://ist_phosphorus.eu/nsp/webservice/reservation}CompleteJobType"/>
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
    "completeJob"
})
@XmlRootElement(name = "completeJob")
public class CompleteJob
    implements Serializable, Cloneable
{
    public CompleteJob clone() throws CloneNotSupportedException {
        return (CompleteJob)super.clone();
    }

    @XmlElement(required = true)
    protected CompleteJobType completeJob;

    /**
     * Gets the value of the completeJob property.
     * 
     * @return
     *     possible object is
     *     {@link CompleteJobType }
     *     
     */
    public CompleteJobType getCompleteJob() {
        return completeJob;
    }

    /**
     * Sets the value of the completeJob property.
     * 
     * @param value
     *     allowed object is
     *     {@link CompleteJobType }
     *     
     */
    public void setCompleteJob(CompleteJobType value) {
        this.completeJob = value;
    }

    public boolean isSetCompleteJob() {
        return (this.completeJob!= null);
    }

}
