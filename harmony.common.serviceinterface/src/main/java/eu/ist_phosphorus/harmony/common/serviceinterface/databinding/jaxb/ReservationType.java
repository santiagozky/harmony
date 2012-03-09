
package eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ReservationType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ReservationType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="fixed"/>
 *     &lt;enumeration value="deferrable"/>
 *     &lt;enumeration value="malleable"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ReservationType")
@XmlEnum
public enum ReservationType {

    @XmlEnumValue("fixed")
    FIXED("fixed"),
    @XmlEnumValue("deferrable")
    DEFERRABLE("deferrable"),
    @XmlEnumValue("malleable")
    MALLEABLE("malleable");
    private final String value;

    ReservationType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ReservationType fromValue(String v) {
        for (ReservationType c: ReservationType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
