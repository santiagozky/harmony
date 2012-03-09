
package eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AvailabilityCodeType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AvailabilityCodeType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="available"/>
 *     &lt;enumeration value="endpoint_not_available"/>
 *     &lt;enumeration value="path_not_available"/>
 *     &lt;enumeration value="availability_not_checked"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "AvailabilityCodeType")
@XmlEnum
public enum AvailabilityCodeType {

    @XmlEnumValue("available")
    AVAILABLE("available"),
    @XmlEnumValue("endpoint_not_available")
    ENDPOINT_NOT_AVAILABLE("endpoint_not_available"),
    @XmlEnumValue("path_not_available")
    PATH_NOT_AVAILABLE("path_not_available"),
    @XmlEnumValue("availability_not_checked")
    AVAILABILITY_NOT_CHECKED("availability_not_checked");
    private final String value;

    AvailabilityCodeType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AvailabilityCodeType fromValue(String v) {
        for (AvailabilityCodeType c: AvailabilityCodeType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
