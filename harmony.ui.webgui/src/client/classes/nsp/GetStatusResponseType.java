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

/**
 * <p>
 * Java class for GetStatusResponseType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;GetStatusResponseType&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;ServiceStatus&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;extension base=&quot;{http://ist_phosphorus.eu/nsp/webservice/reservation}ServiceStatusType&quot;&gt;
 *               &lt;/extension&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
public class GetStatusResponseType implements Serializable, Cloneable {
    /**
     *
     */
    private static final long serialVersionUID = -5867863643844924507L;

    protected List<client.classes.nsp.GetStatusResponseType.ServiceStatus> serviceStatus;

    /**
     * Gets the value of the serviceStatus property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the serviceStatus property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getServiceStatus().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GetStatusResponseType.ServiceStatus }
     * 
     * 
     */
    public List<client.classes.nsp.GetStatusResponseType.ServiceStatus> getServiceStatus() {
        if (this.serviceStatus == null) {
            this.serviceStatus =
                    new ArrayList<client.classes.nsp.GetStatusResponseType.ServiceStatus>();
        }
        return this.serviceStatus;
    }

    public boolean isSetServiceStatus() {
        return ((this.serviceStatus != null) && (!this.serviceStatus.isEmpty()));
    }

    public void unsetServiceStatus() {
        this.serviceStatus = null;
    }

    /**
     * <p>
     * Java class for anonymous complex type.
     * 
     * <p>
     * The following schema fragment specifies the expected content contained
     * within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;extension base=&quot;{http://ist_phosphorus.eu/nsp/webservice/reservation}ServiceStatusType&quot;&gt;
     *     &lt;/extension&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */

    public static class ServiceStatus extends ServiceStatusType implements
            Serializable, Cloneable {

        /**
         *
         */
        private static final long serialVersionUID = 6979944395502784893L;

    }

}
