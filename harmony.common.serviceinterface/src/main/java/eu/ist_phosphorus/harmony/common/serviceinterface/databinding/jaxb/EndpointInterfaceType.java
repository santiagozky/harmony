
package eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EndpointInterfaceType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="EndpointInterfaceType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="user"/>
 *     &lt;enumeration value="border"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "EndpointInterfaceType", namespace = "http://ist_phosphorus.eu/nsp")
@XmlEnum
public enum EndpointInterfaceType {

    @XmlEnumValue("user")
    USER("user"),
    @XmlEnumValue("border")
    BORDER("border");
    private final String value;

    EndpointInterfaceType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EndpointInterfaceType fromValue(String v) {
        for (EndpointInterfaceType c: EndpointInterfaceType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}