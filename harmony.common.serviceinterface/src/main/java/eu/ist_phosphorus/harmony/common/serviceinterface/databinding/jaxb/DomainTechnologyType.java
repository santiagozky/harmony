
package eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DomainTechnologyType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DomainTechnologyType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DomainSupportedAdaptation" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="DomainSupportedBandwidth" type="{http://www.w3.org/2001/XMLSchema}long" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="DomainSupportedSwitchMatrix" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DomainTechnologyType", namespace = "http://ist_phosphorus.eu/nsp", propOrder = {
    "domainSupportedAdaptation",
    "domainSupportedBandwidth",
    "domainSupportedSwitchMatrix"
})
public class DomainTechnologyType
    implements Serializable, Cloneable
{
    public DomainTechnologyType clone() throws CloneNotSupportedException {
        return (DomainTechnologyType)super.clone();
    }

    @XmlElement(name = "DomainSupportedAdaptation")
    protected List<String> domainSupportedAdaptation;
    @XmlElement(name = "DomainSupportedBandwidth", type = Long.class)
    protected List<Long> domainSupportedBandwidth;
    @XmlElement(name = "DomainSupportedSwitchMatrix", required = true)
    protected List<String> domainSupportedSwitchMatrix;

    /**
     * Gets the value of the domainSupportedAdaptation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the domainSupportedAdaptation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDomainSupportedAdaptation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getDomainSupportedAdaptation() {
        if (domainSupportedAdaptation == null) {
            domainSupportedAdaptation = new ArrayList<String>();
        }
        return this.domainSupportedAdaptation;
    }

    public boolean isSetDomainSupportedAdaptation() {
        return ((this.domainSupportedAdaptation!= null)&&(!this.domainSupportedAdaptation.isEmpty()));
    }

    public void unsetDomainSupportedAdaptation() {
        this.domainSupportedAdaptation = null;
    }

    /**
     * Gets the value of the domainSupportedBandwidth property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the domainSupportedBandwidth property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDomainSupportedBandwidth().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Long }
     * 
     * 
     */
    public List<Long> getDomainSupportedBandwidth() {
        if (domainSupportedBandwidth == null) {
            domainSupportedBandwidth = new ArrayList<Long>();
        }
        return this.domainSupportedBandwidth;
    }

    public boolean isSetDomainSupportedBandwidth() {
        return ((this.domainSupportedBandwidth!= null)&&(!this.domainSupportedBandwidth.isEmpty()));
    }

    public void unsetDomainSupportedBandwidth() {
        this.domainSupportedBandwidth = null;
    }

    /**
     * Gets the value of the domainSupportedSwitchMatrix property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the domainSupportedSwitchMatrix property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDomainSupportedSwitchMatrix().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getDomainSupportedSwitchMatrix() {
        if (domainSupportedSwitchMatrix == null) {
            domainSupportedSwitchMatrix = new ArrayList<String>();
        }
        return this.domainSupportedSwitchMatrix;
    }

    public boolean isSetDomainSupportedSwitchMatrix() {
        return ((this.domainSupportedSwitchMatrix!= null)&&(!this.domainSupportedSwitchMatrix.isEmpty()));
    }

    public void unsetDomainSupportedSwitchMatrix() {
        this.domainSupportedSwitchMatrix = null;
    }

}