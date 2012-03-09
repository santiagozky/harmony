
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
 *         &lt;element name="activate" type="{http://ist_phosphorus.eu/nsp/webservice/reservation}ActivateType"/>
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
    "activate"
})
@XmlRootElement(name = "activate")
public class Activate
    implements Serializable, Cloneable
{
    public Activate clone() throws CloneNotSupportedException {
        return (Activate)super.clone();
    }

    @XmlElement(required = true)
    protected ActivateType activate;

    /**
     * Gets the value of the activate property.
     * 
     * @return
     *     possible object is
     *     {@link ActivateType }
     *     
     */
    public ActivateType getActivate() {
        return activate;
    }

    /**
     * Sets the value of the activate property.
     * 
     * @param value
     *     allowed object is
     *     {@link ActivateType }
     *     
     */
    public void setActivate(ActivateType value) {
        this.activate = value;
    }

    public boolean isSetActivate() {
        return (this.activate!= null);
    }

}
