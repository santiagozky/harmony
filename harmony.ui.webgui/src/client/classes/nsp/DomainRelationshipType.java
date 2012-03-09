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
 * Java class for DomainRelationshipType.
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * <p>
 * 
 * <pre>
 * &lt;simpleType name=&quot;DomainRelationshipType&quot;&gt;
 *   &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;&gt;
 *     &lt;enumeration value=&quot;superdomain&quot;/&gt;
 *     &lt;enumeration value=&quot;peer&quot;/&gt;
 *     &lt;enumeration value=&quot;subdomain&quot;/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 */
public class DomainRelationshipType implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 8772492092578249956L;

    private String[] enums;

    private String value;

    public DomainRelationshipType(final String v) {
        this.value = v;
        this.enums = new String[] { "superdomain", "peer", "subdomain" };
    }

    public DomainRelationshipType() {
        this.enums = new String[] { "superdomain", "peer", "subdomain" };
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
