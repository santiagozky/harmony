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
 * 
 * Tuple of domain ID and ConnectionStatusType
 * 
 * 
 * <p>
 * Java class for DomainConnectionStatusType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;DomainConnectionStatusType&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;Domain&quot; type=&quot;{http://ist_phosphorus.eu/nsp}DomainIdentifierType&quot;/&gt;
 *         &lt;element name=&quot;Status&quot; type=&quot;{http://ist_phosphorus.eu/nsp/webservice/reservation}ConnectionStatusType&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
public class DomainConnectionStatusType implements Serializable, Cloneable {

    /**
     *
     */
    private static final long serialVersionUID = 5811549981088703179L;
    protected String domain;
    protected ConnectionStatusType status;

    /**
     * Gets the value of the domain property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getDomain() {
        return this.domain;
    }

    /**
     * Sets the value of the domain property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setDomain(final String value) {
        this.domain = value;
    }

    public boolean isSetDomain() {
        return (this.domain != null);
    }

    /**
     * Gets the value of the status property.
     * 
     * @return possible object is {@link ConnectionStatusType }
     * 
     */
    public ConnectionStatusType getStatus() {
        return this.status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *            allowed object is {@link ConnectionStatusType }
     * 
     */
    public void setStatus(final ConnectionStatusType value) {
        this.status = value;
    }

    public boolean isSetStatus() {
        return (this.status != null);
    }

}
