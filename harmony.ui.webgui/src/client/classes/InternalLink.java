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


package client.classes;

import java.io.Serializable;
import java.util.Date;

public class InternalLink implements Serializable {

    // TODO: needed?
    private String uniqueLabel;

    /**
     * Identifies the router where the Link begins by an IP.
     */
    private String sourceNode;

    /**
     * Identifies the router where the Link ends by an IP.
     */
    private String destinationNode;

    /**
     * Identifies the beginning of a Link by an IP.
     */
    private String outgoingInterface;

    /**
     * Identifies the end of a Link by an IP.
     */
    private String ingoingInterface;

    /**
     * The maximal bandwidth, which is always provided by the Link.
     */
    private long bandwidth;

    /**
     * The date until the Link is valid. After that date, the link will be
     * removed.
     */
    private Date validTo;

    /**
     * The date where link becomes usable for reservations.
     */
    private Date validFrom;

    /**
     * If the TopologyManager is notified, that a link doesn't work, this value
     * will be set to false
     */
    private boolean up;

    /**
     * The delay of the link
     */
    private int delay;

    private String popup;

    public InternalLink() {

    }

    public InternalLink(final String sourceNode, final String destinationNode,
            final String outgoingInterface, final String ingoingInterface,
            final long bandwidth, final Date validFrom, final Date validTo,
            final boolean linkUp, final int delay) {
        this.sourceNode = sourceNode;
        this.destinationNode = destinationNode;
        this.outgoingInterface = outgoingInterface;
        this.ingoingInterface = ingoingInterface;
        this.bandwidth = bandwidth;
        this.validTo = validTo;
        this.validFrom = validFrom;
        this.up = linkUp;
        this.delay = delay;

    }

    public String getUniqueLabel() {
        return this.getIngoingInterface();
    }

    // TODO: this is weird
    public void setUniqueLabel(final String uniqueLabel) {
        this.uniqueLabel = this.getUniqueLabel();
    }

    public String getSourceNode() {
        return this.sourceNode;
    }

    public void setSourceNode(final String sourceNode) {
        this.sourceNode = sourceNode;
    }

    public String getDestinationNode() {
        return this.destinationNode;
    }

    public void setDestinationNode(final String destinationNode) {
        this.destinationNode = destinationNode;
    }

    public String getOutgoingInterface() {
        return this.outgoingInterface;
    }

    public void setOutgoingInterface(final String outgoingInterface) {
        this.outgoingInterface = outgoingInterface;
    }

    public String getIngoingInterface() {
        return this.ingoingInterface;
    }

    public void setIngoingInterface(final String ingoingInterface) {
        this.ingoingInterface = ingoingInterface;
    }

    public long getBandwidth() {
        return this.bandwidth;
    }

    public void setBandwidth(final long bandwidth) {
        this.bandwidth = bandwidth;
    }

    public Date getValidTo() {
        return this.validTo;
    }

    public void setValidTo(final Date validTo) {
        this.validTo = validTo;
    }

    public Date getValidFrom() {
        return this.validFrom;
    }

    public void setValidFrom(final Date validFrom) {
        this.validFrom = validFrom;
    }

    public boolean isUp() {
        return this.up;
    }

    public void setUp(final boolean up) {
        this.up = up;
    }

    public int getDelay() {
        return this.delay;
    }

    public void setDelay(final int delay) {
        this.delay = delay;
    }

    public String getPopup() {
        return this.popup;
    }

    public void setPopup(final String popup) {
        this.popup = popup;
    }

}
