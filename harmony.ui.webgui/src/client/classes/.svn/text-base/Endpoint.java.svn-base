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

public class Endpoint implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 8290486340136384602L;

    public static final String EXTERNAL = "external";

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
     * The maximal bandwith, which is always provided by the Link
     */
    private long bandwidth;

    /**
     * The date until the Link is valid, after that date, the link will be
     * removed
     */
    private Date validTo;

    /**
     * defines the point in time, where this link kann be used for reservations
     */
    private Date validFrom;

    /**
     * If the TopologyManager is notified, that a link doesn`t work, this value
     * will bes set to false
     */
    private boolean up;

    /**
     * The delay of the link
     */
    private int delay;

    /**
     * is true, if the sourceInterface of the link is an endpoint: The
     * destinationInterface does not lie within the controlled Domain
     */
    // private boolean isEndpoint;
    private String popup;

    public Endpoint() {

    }

    public Endpoint(final String sourceNode, final String destinationNode,
            final String outgoingInterface, final String ingoingInterface,
            final long bandwidth, final Date validFrom, final Date validTo,
            final boolean linkUp, final int delay) {
        super();
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

    public String getOutgoingInterface() {
        return this.outgoingInterface;
    }

    public void setOutgoingInterface(final String interfaceA) {
        this.outgoingInterface = interfaceA;

    }

    public String getIngoingInterface() {
        return this.ingoingInterface;
    }

    public void setIngoingInterface(final String interfaceB) {
        this.ingoingInterface = interfaceB;

    }

    public boolean isUp() {
        return this.up;
    }

    public void setUp(final boolean up) {
        this.up = up;
    }

    public String getSourceNode() {
        return this.sourceNode;
    }

    public void setSourceNode(final String nodeA) {
        this.sourceNode = nodeA;
    }

    public String getDestinationNode() {
        return this.destinationNode;
    }

    public void setDestinationNode(final String nodeB) {
        this.destinationNode = nodeB;
    }

    public Date getValidFrom() {
        return this.validFrom;
    }

    public void setValidFrom(final Date validFrom) {
        this.validFrom = validFrom;
    }

    public Date getValidTo() {
        return this.validTo;
    }

    public void setValidTo(final Date validTo) {
        this.validTo = validTo;
    }

    public long getBandwidth() {
        return this.bandwidth;
    }

    public void setBandwidth(final long bandwith) {
        this.bandwidth = bandwith;
    }

    public int getDelay() {
        return this.delay;
    }

    public void setDelay(final int delay) {
        this.delay = delay;
    }

    /**
     * For an endpoint, the uniqueLabel consists of outgoingInterface +
     * ingoingInterface, since one of them is not unique.
     */
    public String getUniqueLabel() {
        return this.getOutgoingInterface() + " to "
                + this.getIngoingInterface();
    }

    public void setUniqueLabel(final String uniqueLabel) {
        this.uniqueLabel = this.getUniqueLabel();
    }

    /**
     * if the outgoingInterface is known, the uniqueLabel of the endpoint is
     * known.
     * 
     * @param outgoingInterface
     * @return the uniqueLabel
     */
    public static String getLabelFromOutgoingInterface(
            final String outgoingInterface) {
        return outgoingInterface + " to " + Endpoint.EXTERNAL;
    }

    /**
     * if ingoingInterface is known, the uniqueLabel of the endpoint is known.
     * 
     * @param ingoingInterface
     * @return the uniqueLabel
     */
    public static String getLabelFromIngoingInterface(
            final String ingoingInterface) {
        return Endpoint.EXTERNAL + " to " + ingoingInterface;
    }

    @Override
    public String toString() {
        return new String("sourceInterface = uniqueLabel: "
                + this.getUniqueLabel() + ", destinationInterface: "
                + this.getIngoingInterface() + ", sourceNode: "
                + this.getSourceNode() + ", destinationNode: "
                + this.getDestinationNode());
    }

    public Endpoint cloneMe() {
        final Endpoint ret = new Endpoint();
        ret.setBandwidth(this.getBandwidth());
        ret.setDelay(this.getDelay());
        ret.setIngoingInterface(this.getIngoingInterface());
        ret.setDestinationNode(this.getDestinationNode());
        // ret.setIsEndpoint(this.getIsEndpoint());
        ret.setUp(this.isUp());
        ret.setOutgoingInterface(this.getOutgoingInterface());
        ret.setSourceNode(this.getSourceNode());
        ret.setValidFrom(this.getValidFrom());
        ret.setValidTo(this.getValidTo());
        return ret;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result =
                prime * result
                        + (int) (this.bandwidth ^ (this.bandwidth >>> 32));
        result = prime * result + this.delay;
        result =
                prime
                        * result
                        + ((this.destinationNode == null) ? 0
                                : this.destinationNode.hashCode());
        result =
                prime
                        * result
                        + ((this.ingoingInterface == null) ? 0
                                : this.ingoingInterface.hashCode());
        result = prime * result + (this.up ? 1231 : 1237);
        result =
                prime
                        * result
                        + ((this.outgoingInterface == null) ? 0
                                : this.outgoingInterface.hashCode());
        result =
                prime
                        * result
                        + ((this.sourceNode == null) ? 0 : this.sourceNode
                                .hashCode());
        result =
                prime
                        * result
                        + ((this.validFrom == null) ? 0 : this.validFrom
                                .hashCode());
        result =
                prime
                        * result
                        + ((this.validTo == null) ? 0 : this.validTo.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        final Endpoint other = (Endpoint) obj;
        if (this.bandwidth != other.bandwidth) {
            return false;
        }
        if (this.delay != other.delay) {
            return false;
        }
        if (this.destinationNode == null) {
            if (other.destinationNode != null) {
                return false;
            }
        } else if (!this.destinationNode.equals(other.destinationNode)) {
            return false;
        }
        if (this.ingoingInterface == null) {
            if (other.ingoingInterface != null) {
                return false;
            }
        } else if (!this.ingoingInterface.equals(other.ingoingInterface)) {
            return false;
        }
        if (this.up != other.up) {
            return false;
        }
        if (this.outgoingInterface == null) {
            if (other.outgoingInterface != null) {
                return false;
            }
        } else if (!this.outgoingInterface.equals(other.outgoingInterface)) {
            return false;
        }
        if (this.sourceNode == null) {
            if (other.sourceNode != null) {
                return false;
            }
        } else if (!this.sourceNode.equals(other.sourceNode)) {
            return false;
        }
        if (this.validFrom == null) {
            if (other.validFrom != null) {
                return false;
            }
        } else if (!this.validFrom.equals(other.validFrom)) {
            return false;
        }
        if (this.validTo == null) {
            if (other.validTo != null) {
                return false;
            }
        } else if (!this.validTo.equals(other.validTo)) {
            return false;
        }
        return true;
    }

    public String getPopup() {
        return this.popup;
    }

    public void setPopup(final String popup) {
        this.popup = popup;
    }

}
