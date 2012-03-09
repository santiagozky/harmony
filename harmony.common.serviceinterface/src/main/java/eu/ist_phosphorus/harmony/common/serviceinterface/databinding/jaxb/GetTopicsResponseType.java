
package eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getTopicsResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getTopicsResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Topics" type="{http://ist_phosphorus.eu/nsp/webservice/notification}TopicIdentifierType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getTopicsResponseType", namespace = "http://ist_phosphorus.eu/nsp/webservice/notification", propOrder = {
    "topics"
})
public class GetTopicsResponseType
    implements Serializable, Cloneable
{
    public GetTopicsResponseType clone() throws CloneNotSupportedException {
        return (GetTopicsResponseType)super.clone();
    }

    @XmlElement(name = "Topics")
    protected List<String> topics;

    /**
     * Gets the value of the topics property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the topics property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTopics().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getTopics() {
        if (topics == null) {
            topics = new ArrayList<String>();
        }
        return this.topics;
    }

    public boolean isSetTopics() {
        return ((this.topics!= null)&&(!this.topics.isEmpty()));
    }

    public void unsetTopics() {
        this.topics = null;
    }

}
