
package eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 				Stores connection data: Connection ID, involved
 * 				endpoints and directionality
 * 			
 * 
 * <p>Java class for ConnectionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ConnectionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ConnectionID" type="{http://ist_phosphorus.eu/nsp/webservice/reservation}ConnectionIdentifierType"/>
 *         &lt;element name="Source" type="{http://ist_phosphorus.eu/nsp}EndpointType"/>
 *         &lt;element name="Target" type="{http://ist_phosphorus.eu/nsp}EndpointType" maxOccurs="unbounded"/>
 *         &lt;element name="Directionality" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ConnectionType", propOrder = {
    "connectionID",
    "source",
    "target",
    "directionality"
})
@XmlSeeAlso({
    ConnectionConstraintType.class,
    ConnectionStatusType.class
})
public class ConnectionType
    implements Serializable, Cloneable
{
    public ConnectionType clone() throws CloneNotSupportedException {
        return (ConnectionType)super.clone();
    }

    @XmlElement(name = "ConnectionID")
    protected int connectionID;
    @XmlElement(name = "Source", required = true)
    protected EndpointType source;
    @XmlElement(name = "Target", required = true)
    protected List<EndpointType> target;
    @XmlElement(name = "Directionality")
    protected int directionality;

    /**
     * Gets the value of the connectionID property.
     * 
     */
    public int getConnectionID() {
        return connectionID;
    }

    /**
     * Sets the value of the connectionID property.
     * 
     */
    public void setConnectionID(int value) {
        this.connectionID = value;
    }

    public boolean isSetConnectionID() {
        return true;
    }

    /**
     * Gets the value of the source property.
     * 
     * @return
     *     possible object is
     *     {@link EndpointType }
     *     
     */
    public EndpointType getSource() {
        return source;
    }

    /**
     * Sets the value of the source property.
     * 
     * @param value
     *     allowed object is
     *     {@link EndpointType }
     *     
     */
    public void setSource(EndpointType value) {
        this.source = value;
    }

    public boolean isSetSource() {
        return (this.source!= null);
    }

    /**
     * Gets the value of the target property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the target property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTarget().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EndpointType }
     * 
     * 
     */
    public List<EndpointType> getTarget() {
        if (target == null) {
            target = new ArrayList<EndpointType>();
        }
        return this.target;
    }

    public boolean isSetTarget() {
        return ((this.target!= null)&&(!this.target.isEmpty()));
    }

    public void unsetTarget() {
        this.target = null;
    }

    /**
     * Gets the value of the directionality property.
     * 
     */
    public int getDirectionality() {
        return directionality;
    }

    /**
     * Sets the value of the directionality property.
     * 
     */
    public void setDirectionality(int value) {
        this.directionality = value;
    }

    public boolean isSetDirectionality() {
        return true;
    }

}
