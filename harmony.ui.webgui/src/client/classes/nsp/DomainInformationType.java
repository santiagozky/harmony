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
import java.util.ArrayList;
import java.util.List;

public class DomainInformationType implements Serializable, Cloneable {

    /**
     *
     */
    private static final long serialVersionUID = -9112535579827336768L;

    protected String domainId;
    protected DomainRelationshipType relationship;
    protected Integer sequenceNumber;
    protected String description;
    protected String reservationEPR;
    protected String topologyEPR;
    protected String notificationEPR;
    protected List<java.lang.String> tnaPrefix;
    protected List<client.classes.nsp.InterdomainLinkType> interdomainLink;
    protected Integer avgDelay;
    protected Integer maxBW;

    public String getDomainId() {
        return this.domainId;
    }

    public void setDomainId(final String value) {
        this.domainId = value;
    }

    public boolean isSetDomainId() {
        return (this.domainId != null);
    }

    public DomainRelationshipType getRelationship() {
        return this.relationship;
    }

    public void setRelationship(final DomainRelationshipType value) {
        this.relationship = value;
    }

    public boolean isSetRelationship() {
        return ((this.relationship != null) && (this.relationship.value() != null));
    }

    public Integer getSequenceNumber() {
        return this.sequenceNumber;
    }

    public void setSequenceNumber(final Integer value) {
        this.sequenceNumber = value;
    }

    public boolean isSetSequenceNumber() {
        return (this.sequenceNumber != null);
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String value) {
        this.description = value;
    }

    public boolean isSetDescription() {
        return (this.description != null);
    }

    public String getReservationEPR() {
        return this.reservationEPR;
    }

    public void setReservationEPR(final String value) {
        this.reservationEPR = value;
    }

    public boolean isSetReservationEPR() {
        return (this.reservationEPR != null);
    }

    public String getTopologyEPR() {
        return this.topologyEPR;
    }

    public void setTopologyEPR(final String value) {
        this.topologyEPR = value;
    }

    public boolean isSetTopologyEPR() {
        return (this.topologyEPR != null);
    }

    public String getNotificationEPR() {
        return this.notificationEPR;
    }

    public void setNotificationEPR(final String value) {
        this.notificationEPR = value;
    }

    public boolean isSetNotificationEPR() {
        return (this.notificationEPR != null);
    }

    public List<java.lang.String> getTNAPrefix() {
        if (this.tnaPrefix == null) {
            this.tnaPrefix = new ArrayList<java.lang.String>();
        }
        return this.tnaPrefix;
    }

    public boolean isSetTNAPrefix() {
        return ((this.tnaPrefix != null) && (!this.tnaPrefix.isEmpty()));
    }

    public void unsetTNAPrefix() {
        this.tnaPrefix = null;
    }

    public List<client.classes.nsp.InterdomainLinkType> getInterdomainLink() {
        if (this.interdomainLink == null) {
            this.interdomainLink =
                    new ArrayList<client.classes.nsp.InterdomainLinkType>();
        }
        return this.interdomainLink;
    }

    public boolean isSetInterdomainLink() {
        return ((this.interdomainLink != null) && (!this.interdomainLink
                .isEmpty()));
    }

    public void unsetInterdomainLink() {
        this.interdomainLink = null;
    }

    public Integer getAvgDelay() {
        return this.avgDelay;
    }

    public void setAvgDelay(final Integer value) {
        this.avgDelay = value;
    }

    public boolean isSetAvgDelay() {
        return (this.avgDelay != null);
    }

    public Integer getMaxBW() {
        return this.maxBW;
    }

    public void setMaxBW(final Integer value) {
        this.maxBW = value;
    }

    public boolean isSetMaxBW() {
        return (this.maxBW != null);
    }

}
