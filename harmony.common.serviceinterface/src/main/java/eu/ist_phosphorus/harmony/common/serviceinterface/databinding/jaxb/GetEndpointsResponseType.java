
package eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetEndpointsResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetEndpointsResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Endpoints" type="{http://ist_phosphorus.eu/nsp}EndpointType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetEndpointsResponseType", namespace = "http://ist_phosphorus.eu/nsp/webservice/topology", propOrder = {
    "endpoints"
})
public class GetEndpointsResponseType
    implements Serializable, Cloneable
{
    public GetEndpointsResponseType clone() throws CloneNotSupportedException {
        return (GetEndpointsResponseType)super.clone();
    }

    @XmlElement(name = "Endpoints")
    protected List<EndpointType> endpoints;

    /**
     * Gets the value of the endpoints property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the endpoints property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEndpoints().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EndpointType }
     * 
     * 
     */
    public List<EndpointType> getEndpoints() {
        if (endpoints == null) {
            endpoints = new ArrayList<EndpointType>();
        }
        return this.endpoints;
    }

    public boolean isSetEndpoints() {
        return ((this.endpoints!= null)&&(!this.endpoints.isEmpty()));
    }

    public void unsetEndpoints() {
        this.endpoints = null;
    }

}
