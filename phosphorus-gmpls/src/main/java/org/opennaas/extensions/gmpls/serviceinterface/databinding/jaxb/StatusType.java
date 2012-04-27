
package org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for StatusType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="StatusType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="active"/>
 *     &lt;enumeration value="setup_in_progress"/>
 *     &lt;enumeration value="teardown_in_progress"/>
 *     &lt;enumeration value="completed"/>
 *     &lt;enumeration value="pending"/>
 *     &lt;enumeration value="cancelled_by_user"/>
 *     &lt;enumeration value="cancelled_by_system"/>
 *     &lt;enumeration value="unknown"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "StatusType")
@XmlEnum
public enum StatusType {

    @XmlEnumValue("active")
    ACTIVE("active"),
    @XmlEnumValue("setup_in_progress")
    SETUP_IN_PROGRESS("setup_in_progress"),
    @XmlEnumValue("teardown_in_progress")
    TEARDOWN_IN_PROGRESS("teardown_in_progress"),
    @XmlEnumValue("completed")
    COMPLETED("completed"),
    @XmlEnumValue("pending")
    PENDING("pending"),
    @XmlEnumValue("cancelled_by_user")
    CANCELLED_BY_USER("cancelled_by_user"),
    @XmlEnumValue("cancelled_by_system")
    CANCELLED_BY_SYSTEM("cancelled_by_system"),
    @XmlEnumValue("unknown")
    UNKNOWN("unknown");
    private final String value;

    StatusType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static StatusType fromValue(String v) {
        for (StatusType c: StatusType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
