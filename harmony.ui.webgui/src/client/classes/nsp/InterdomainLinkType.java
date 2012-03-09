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
 * Java class for InterdomainLinkType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;InterdomainLinkType&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;LinkID&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *         &lt;element name=&quot;SourceEndpoint&quot; type=&quot;{http://ist_phosphorus.eu/nsp}EndpointType&quot;/&gt;
 *         &lt;element name=&quot;DestinationDomain&quot; type=&quot;{http://ist_phosphorus.eu/nsp}DomainIdentifierType&quot;/&gt;
 *         &lt;element name=&quot;costs&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}int&quot; minOccurs=&quot;0&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
public class InterdomainLinkType implements Serializable, Cloneable {

    /**
     *
     */
    private static final long serialVersionUID = -937816653348811211L;
    protected String linkID;
    protected EndpointType sourceEndpoint;
    protected String destinationDomain;
    protected Integer costs;

    /**
     * Gets the value of the linkID property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getLinkID() {
        return this.linkID;
    }

    /**
     * Sets the value of the linkID property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setLinkID(final String value) {
        this.linkID = value;
    }

    public boolean isSetLinkID() {
        return (this.linkID != null);
    }

    /**
     * Gets the value of the sourceEndpoint property.
     * 
     * @return possible object is {@link EndpointType }
     * 
     */
    public EndpointType getSourceEndpoint() {
        return this.sourceEndpoint;
    }

    /**
     * Sets the value of the sourceEndpoint property.
     * 
     * @param value
     *            allowed object is {@link EndpointType }
     * 
     */
    public void setSourceEndpoint(final EndpointType value) {
        this.sourceEndpoint = value;
    }

    public boolean isSetSourceEndpoint() {
        return (this.sourceEndpoint != null);
    }

    /**
     * Gets the value of the destinationDomain property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getDestinationDomain() {
        return this.destinationDomain;
    }

    /**
     * Sets the value of the destinationDomain property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setDestinationDomain(final String value) {
        this.destinationDomain = value;
    }

    public boolean isSetDestinationDomain() {
        return (this.destinationDomain != null);
    }

    /**
     * Gets the value of the costs property.
     * 
     * @return possible object is {@link Integer }
     * 
     */
    public Integer getCosts() {
        return this.costs;
    }

    /**
     * Sets the value of the costs property.
     * 
     * @param value
     *            allowed object is {@link Integer }
     * 
     */
    public void setCosts(final Integer value) {
        this.costs = value;
    }

    public boolean isSetCosts() {
        return (this.costs != null);
    }

}
