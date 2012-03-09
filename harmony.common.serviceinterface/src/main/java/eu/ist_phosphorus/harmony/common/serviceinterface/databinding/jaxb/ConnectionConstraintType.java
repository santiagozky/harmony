
package eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 				Type used to specify constraints for a requested
 * 				connection.
 * 			
 * 
 * <p>Java class for ConnectionConstraintType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ConnectionConstraintType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ist_phosphorus.eu/nsp/webservice/reservation}ConnectionType">
 *       &lt;sequence>
 *         &lt;element name="MinBW" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="MaxBW" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="MaxDelay" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="DataAmount" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ConnectionConstraintType", propOrder = {
    "minBW",
    "maxBW",
    "maxDelay",
    "dataAmount"
})
public class ConnectionConstraintType
    extends ConnectionType
    implements Serializable, Cloneable
{
    public ConnectionConstraintType clone() throws CloneNotSupportedException {
        return (ConnectionConstraintType)super.clone();
    }

    @XmlElement(name = "MinBW")
    protected int minBW;
    @XmlElement(name = "MaxBW")
    protected Integer maxBW;
    @XmlElement(name = "MaxDelay")
    protected Integer maxDelay;
    @XmlElement(name = "DataAmount")
    protected Long dataAmount;

    /**
     * Gets the value of the minBW property.
     * 
     */
    public int getMinBW() {
        return minBW;
    }

    /**
     * Sets the value of the minBW property.
     * 
     */
    public void setMinBW(int value) {
        this.minBW = value;
    }

    public boolean isSetMinBW() {
        return true;
    }

    /**
     * Gets the value of the maxBW property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMaxBW() {
        return maxBW;
    }

    /**
     * Sets the value of the maxBW property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMaxBW(Integer value) {
        this.maxBW = value;
    }

    public boolean isSetMaxBW() {
        return (this.maxBW!= null);
    }

    /**
     * Gets the value of the maxDelay property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMaxDelay() {
        return maxDelay;
    }

    /**
     * Sets the value of the maxDelay property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMaxDelay(Integer value) {
        this.maxDelay = value;
    }

    public boolean isSetMaxDelay() {
        return (this.maxDelay!= null);
    }

    /**
     * Gets the value of the dataAmount property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getDataAmount() {
        return dataAmount;
    }

    /**
     * Sets the value of the dataAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setDataAmount(Long value) {
        this.dataAmount = value;
    }

    public boolean isSetDataAmount() {
        return (this.dataAmount!= null);
    }

}
