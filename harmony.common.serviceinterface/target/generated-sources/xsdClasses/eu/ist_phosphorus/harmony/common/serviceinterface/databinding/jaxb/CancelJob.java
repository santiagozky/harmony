//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-b10 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.03.07 at 10:16:02 AM CET 
//


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
 *         &lt;element name="cancelJob" type="{http://ist_phosphorus.eu/nsp/webservice/reservation}CancelJobType"/>
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
    "cancelJob"
})
@XmlRootElement(name = "cancelJob")
public class CancelJob
    implements Serializable
{

    @XmlElement(required = true)
    protected CancelJobType cancelJob;

    /**
     * Gets the value of the cancelJob property.
     * 
     * @return
     *     possible object is
     *     {@link CancelJobType }
     *     
     */
    public CancelJobType getCancelJob() {
        return cancelJob;
    }

    /**
     * Sets the value of the cancelJob property.
     * 
     * @param value
     *     allowed object is
     *     {@link CancelJobType }
     *     
     */
    public void setCancelJob(CancelJobType value) {
        this.cancelJob = value;
    }

    public boolean isSetCancelJob() {
        return (this.cancelJob!= null);
    }

}
