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

public class CreateReservationType implements Serializable, Cloneable {

    protected List<client.classes.nsp.ServiceConstraintType> service;
    protected Long jobID;
    protected String notificationConsumerURL;

    public List<client.classes.nsp.ServiceConstraintType> getService() {
        if (this.service == null) {
            this.service =
                    new ArrayList<client.classes.nsp.ServiceConstraintType>();
        }
        return this.service;
    }

    public boolean isSetService() {
        return ((this.service != null) && (!this.service.isEmpty()));
    }

    public void unsetService() {
        this.service = null;
    }

    public Long getJobID() {
        return this.jobID;
    }

    public void setJobID(final Long value) {
        this.jobID = value;
    }

    public boolean isSetJobID() {
        return (this.jobID != null);
    }

    public String getNotificationConsumerURL() {
        return this.notificationConsumerURL;
    }

    public void setNotificationConsumerURL(final String value) {
        this.notificationConsumerURL = value;
    }

    public boolean isSetNotificationConsumerURL() {
        return (this.notificationConsumerURL != null);
    }

}
