
package eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CompleteJobType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CompleteJobType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="JobID" type="{http://ist_phosphorus.eu/nsp/webservice/reservation}JobIdentifierType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CompleteJobType", propOrder = {
    "jobID"
})
public class CompleteJobType
    implements Serializable, Cloneable
{
    public CompleteJobType clone() throws CloneNotSupportedException {
        return (CompleteJobType)super.clone();
    }

    @XmlElement(name = "JobID")
    protected long jobID;

    /**
     * Gets the value of the jobID property.
     * 
     */
    public long getJobID() {
        return jobID;
    }

    /**
     * Sets the value of the jobID property.
     * 
     */
    public void setJobID(long value) {
        this.jobID = value;
    }

    public boolean isSetJobID() {
        return true;
    }

}
