
package eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 				Type used to specify constraints for a requested
 * 				service.
 * 			
 * 
 * <p>Java class for ServiceConstraintType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ServiceConstraintType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ServiceID" type="{http://ist_phosphorus.eu/nsp/webservice/reservation}ServiceIdentifierType"/>
 *         &lt;element name="TypeOfReservation" type="{http://ist_phosphorus.eu/nsp/webservice/reservation}ReservationType"/>
 *         &lt;choice>
 *           &lt;element name="FixedReservationConstraints" type="{http://ist_phosphorus.eu/nsp/webservice/reservation}FixedReservationConstraintType"/>
 *           &lt;element name="DeferrableReservationConstraints" type="{http://ist_phosphorus.eu/nsp/webservice/reservation}DeferrableReservationConstraintType"/>
 *           &lt;element name="MalleableReservationConstraints" type="{http://ist_phosphorus.eu/nsp/webservice/reservation}MalleableReservationConstraintType"/>
 *         &lt;/choice>
 *         &lt;element name="AutomaticActivation" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="Connections" type="{http://ist_phosphorus.eu/nsp/webservice/reservation}ConnectionConstraintType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ServiceConstraintType", propOrder = {
    "serviceID",
    "typeOfReservation",
    "fixedReservationConstraints",
    "deferrableReservationConstraints",
    "malleableReservationConstraints",
    "automaticActivation",
    "connections"
})
public class ServiceConstraintType
    implements Serializable, Cloneable
{
    public ServiceConstraintType clone() throws CloneNotSupportedException {
        return (ServiceConstraintType)super.clone();
    }

    @XmlElement(name = "ServiceID")
    protected int serviceID;
    @XmlElement(name = "TypeOfReservation", required = true)
    protected ReservationType typeOfReservation;
    @XmlElement(name = "FixedReservationConstraints")
    protected FixedReservationConstraintType fixedReservationConstraints;
    @XmlElement(name = "DeferrableReservationConstraints")
    protected DeferrableReservationConstraintType deferrableReservationConstraints;
    @XmlElement(name = "MalleableReservationConstraints")
    protected MalleableReservationConstraintType malleableReservationConstraints;
    @XmlElement(name = "AutomaticActivation")
    protected boolean automaticActivation;
    @XmlElement(name = "Connections", required = true)
    protected List<ConnectionConstraintType> connections;

    /**
     * Gets the value of the serviceID property.
     * 
     */
    public int getServiceID() {
        return serviceID;
    }

    /**
     * Sets the value of the serviceID property.
     * 
     */
    public void setServiceID(int value) {
        this.serviceID = value;
    }

    public boolean isSetServiceID() {
        return true;
    }

    /**
     * Gets the value of the typeOfReservation property.
     * 
     * @return
     *     possible object is
     *     {@link ReservationType }
     *     
     */
    public ReservationType getTypeOfReservation() {
        return typeOfReservation;
    }

    /**
     * Sets the value of the typeOfReservation property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReservationType }
     *     
     */
    public void setTypeOfReservation(ReservationType value) {
        this.typeOfReservation = value;
    }

    public boolean isSetTypeOfReservation() {
        return (this.typeOfReservation!= null);
    }

    /**
     * Gets the value of the fixedReservationConstraints property.
     * 
     * @return
     *     possible object is
     *     {@link FixedReservationConstraintType }
     *     
     */
    public FixedReservationConstraintType getFixedReservationConstraints() {
        return fixedReservationConstraints;
    }

    /**
     * Sets the value of the fixedReservationConstraints property.
     * 
     * @param value
     *     allowed object is
     *     {@link FixedReservationConstraintType }
     *     
     */
    public void setFixedReservationConstraints(FixedReservationConstraintType value) {
        this.fixedReservationConstraints = value;
    }

    public boolean isSetFixedReservationConstraints() {
        return (this.fixedReservationConstraints!= null);
    }

    /**
     * Gets the value of the deferrableReservationConstraints property.
     * 
     * @return
     *     possible object is
     *     {@link DeferrableReservationConstraintType }
     *     
     */
    public DeferrableReservationConstraintType getDeferrableReservationConstraints() {
        return deferrableReservationConstraints;
    }

    /**
     * Sets the value of the deferrableReservationConstraints property.
     * 
     * @param value
     *     allowed object is
     *     {@link DeferrableReservationConstraintType }
     *     
     */
    public void setDeferrableReservationConstraints(DeferrableReservationConstraintType value) {
        this.deferrableReservationConstraints = value;
    }

    public boolean isSetDeferrableReservationConstraints() {
        return (this.deferrableReservationConstraints!= null);
    }

    /**
     * Gets the value of the malleableReservationConstraints property.
     * 
     * @return
     *     possible object is
     *     {@link MalleableReservationConstraintType }
     *     
     */
    public MalleableReservationConstraintType getMalleableReservationConstraints() {
        return malleableReservationConstraints;
    }

    /**
     * Sets the value of the malleableReservationConstraints property.
     * 
     * @param value
     *     allowed object is
     *     {@link MalleableReservationConstraintType }
     *     
     */
    public void setMalleableReservationConstraints(MalleableReservationConstraintType value) {
        this.malleableReservationConstraints = value;
    }

    public boolean isSetMalleableReservationConstraints() {
        return (this.malleableReservationConstraints!= null);
    }

    /**
     * Gets the value of the automaticActivation property.
     * 
     */
    public boolean isAutomaticActivation() {
        return automaticActivation;
    }

    /**
     * Sets the value of the automaticActivation property.
     * 
     */
    public void setAutomaticActivation(boolean value) {
        this.automaticActivation = value;
    }

    public boolean isSetAutomaticActivation() {
        return true;
    }

    /**
     * Gets the value of the connections property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the connections property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getConnections().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ConnectionConstraintType }
     * 
     * 
     */
    public List<ConnectionConstraintType> getConnections() {
        if (connections == null) {
            connections = new ArrayList<ConnectionConstraintType>();
        }
        return this.connections;
    }

    public boolean isSetConnections() {
        return ((this.connections!= null)&&(!this.connections.isEmpty()));
    }

    public void unsetConnections() {
        this.connections = null;
    }

}
