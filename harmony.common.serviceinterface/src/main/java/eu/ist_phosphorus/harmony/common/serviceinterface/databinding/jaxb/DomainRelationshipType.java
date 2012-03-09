
package eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DomainRelationshipType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DomainRelationshipType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="superdomain"/>
 *     &lt;enumeration value="peer"/>
 *     &lt;enumeration value="subdomain"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DomainRelationshipType", namespace = "http://ist_phosphorus.eu/nsp")
@XmlEnum
public enum DomainRelationshipType {

    @XmlEnumValue("superdomain")
    SUPERDOMAIN("superdomain"),
    @XmlEnumValue("peer")
    PEER("peer"),
    @XmlEnumValue("subdomain")
    SUBDOMAIN("subdomain");
    private final String value;

    DomainRelationshipType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DomainRelationshipType fromValue(String v) {
        for (DomainRelationshipType c: DomainRelationshipType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
