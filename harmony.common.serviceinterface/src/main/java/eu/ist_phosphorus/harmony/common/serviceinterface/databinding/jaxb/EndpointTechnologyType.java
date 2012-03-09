
package eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EndpointTechnologyType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EndpointTechnologyType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="EndpointSupportedAdaptation" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EndpointTechnologyType", namespace = "http://ist_phosphorus.eu/nsp", propOrder = {
    "endpointSupportedAdaptation"
})
public class EndpointTechnologyType
    implements Serializable, Cloneable
{
    public EndpointTechnologyType clone() throws CloneNotSupportedException {
        return (EndpointTechnologyType)super.clone();
    }

    @XmlElement(name = "EndpointSupportedAdaptation", required = true)
    protected List<String> endpointSupportedAdaptation;

    /**
     * Gets the value of the endpointSupportedAdaptation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the endpointSupportedAdaptation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEndpointSupportedAdaptation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getEndpointSupportedAdaptation() {
        if (endpointSupportedAdaptation == null) {
            endpointSupportedAdaptation = new ArrayList<String>();
        }
        return this.endpointSupportedAdaptation;
    }

    public boolean isSetEndpointSupportedAdaptation() {
        return ((this.endpointSupportedAdaptation!= null)&&(!this.endpointSupportedAdaptation.isEmpty()));
    }

    public void unsetEndpointSupportedAdaptation() {
        this.endpointSupportedAdaptation = null;
    }

}
