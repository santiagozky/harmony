/**
*  This code is part of the Harmony System implemented in Work Package 1 
*  of the Phosphorus project. This work is supported by the European 
*  Comission under the Sixth Framework Programme with contract number 
*  IST-034115.
*
*  Copyright (C) 2006-2009 Phosphorus WP1 partners. Phosphorus Consortium.
*  http://ist-phosphorus.eu/
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */


package client.classes.nsp;

import java.io.Serializable;

/**
 * <p>
 * Java class for StatusType.
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * <p>
 * 
 * <pre>
 * &lt;simpleType name=&quot;StatusType&quot;&gt;
 *   &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;&gt;
 *     &lt;enumeration value=&quot;active&quot;/&gt;
 *     &lt;enumeration value=&quot;setup_in_progress&quot;/&gt;
 *     &lt;enumeration value=&quot;teardown_in_progress&quot;/&gt;
 *     &lt;enumeration value=&quot;completed&quot;/&gt;
 *     &lt;enumeration value=&quot;pending&quot;/&gt;
 *     &lt;enumeration value=&quot;cancelled_by_user&quot;/&gt;
 *     &lt;enumeration value=&quot;cancelled_by_system&quot;/&gt;
 *     &lt;enumeration value=&quot;unknown&quot;/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 */
public class StatusType implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -1376817860761178191L;

    private String[] enums;

    private String value;

    public StatusType(final String v) {
        this.value = v;
        this.enums =
                new String[] { "active", "setup_in_progress",
                        "teardown_in_progress", "completed", "pending",
                        "cancelled_by_user", "cancelled_by_system", "unknown" };
    }

    public StatusType() {
        // neded for serialisbility
        this.enums =
                new String[] { "active", "setup_in_progress",
                        "teardown_in_progress", "completed", "pending",
                        "cancelled_by_user", "cancelled_by_system", "unknown" };
    }

    public final void setValue(final String v) {
        this.value = v;
    }

    public String value() {
        return this.value;
    }

    public String fromValue(final String v) {
        for (int x = 0; x < this.enums.length; x++) {
            if (this.enums[x].equals(v)) {
                return v;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
