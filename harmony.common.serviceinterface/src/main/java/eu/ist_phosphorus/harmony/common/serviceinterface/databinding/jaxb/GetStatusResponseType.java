
package eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetStatusResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetStatusResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ServiceStatus" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;extension base="{http://ist_phosphorus.eu/nsp/webservice/reservation}ServiceStatusType">
 *               &lt;/extension>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetStatusResponseType", propOrder = {
    "serviceStatus"
})
public class GetStatusResponseType
    implements Serializable, Cloneable
{
    public GetStatusResponseType clone() throws CloneNotSupportedException {
        return (GetStatusResponseType)super.clone();
    }

    @XmlElement(name = "ServiceStatus")
    protected List<GetStatusResponseType.ServiceStatus> serviceStatus;

    /**
     * Gets the value of the serviceStatus property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the serviceStatus property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getServiceStatus().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GetStatusResponseType.ServiceStatus }
     * 
     * 
     */
    public List<GetStatusResponseType.ServiceStatus> getServiceStatus() {
        if (serviceStatus == null) {
            serviceStatus = new ArrayList<GetStatusResponseType.ServiceStatus>();
        }
        return this.serviceStatus;
    }

    public boolean isSetServiceStatus() {
        return ((this.serviceStatus!= null)&&(!this.serviceStatus.isEmpty()));
    }

    public void unsetServiceStatus() {
        this.serviceStatus = null;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;extension base="{http://ist_phosphorus.eu/nsp/webservice/reservation}ServiceStatusType">
     *     &lt;/extension>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class ServiceStatus
        extends ServiceStatusType
        implements Serializable, Cloneable
    {


    }

}
