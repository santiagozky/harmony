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


package eu.ist_phosphorus.harmony.idb.database.hibernate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.FetchMode;
import org.hibernate.annotations.Proxy;
import org.hibernate.criterion.Restrictions;

import eu.ist_phosphorus.harmony.idb.database.TransactionManager;
import eu.ist_phosphorus.harmony.idb.exceptions.database.DatabaseException;
import eu.ist_phosphorus.harmony.common.utils.Tuple;

/**
 * Java representation of of the database entity {@link VIEW_InterDomainLink}.
 * This object does not contain any logic.
 * 
 * @author Stephan Wagner (wagners@cs.uni-bonn.de)
 * @version 0.1
 */

@Entity
@Table(name = "VIEW_InterDomainLink")
@Proxy(lazy = false)
public class VIEW_InterDomainLink implements java.io.Serializable,
        Comparable<VIEW_InterDomainLink> {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 6973314619809374335L;

    /**
     * destination of the link.
     */
    private Endpoint destEndpoint;

    /**
     * destination of the source.
     */
    private Endpoint sourceEndpoint;

    /**
     * name of the link.
     */
    private String name;

    /**
     * description of the link.
     */
    private String description;

    /**
     * delay of the link.
     */
    private int delay;

    /**
     * abtract costs of the link.
     */
    private int costs;

    // Constructors

    /** default constructor. */
    public VIEW_InterDomainLink() {
        // empty
    }

    /**
     * minimal constructor.
     * 
     * @param linkIdParam
     *                id for the link
     * @param destEndpointParam
     *                destination of the link
     * @param sourceEndpointParam
     *                source of the link
     */
    public VIEW_InterDomainLink(final Endpoint destEndpointParam,
            final Endpoint sourceEndpointParam) {
        this(destEndpointParam, sourceEndpointParam, null, null, 0, 0);
    }

    /**
     * full constructor.
     * 
     * @param linkIdParam
     *                id for the link
     * @param destEndpointParam
     *                destination of the link
     * @param sourceEndpointParam
     *                source of the link
     * @param nameParam
     *                name of the link
     * @param descriptionParam
     *                description of the link
     * @param delayParam
     *                delay of the link
     * @param costsParam
     *                TODO
     */
    public VIEW_InterDomainLink(final Endpoint destEndpointParam,
            final Endpoint sourceEndpointParam, final String nameParam,
            final String descriptionParam, final int delayParam, int costsParam) {
        super();

        this.destEndpoint = destEndpointParam;
        this.sourceEndpoint = sourceEndpointParam;
        this.name = nameParam;
        this.description = descriptionParam;
        this.delay = delayParam;
        this.setCosts(costsParam);
    }

    // Property accessors

    /**
     * @return destination of the link
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_DestEndpointTNA")
    public final Endpoint getDestEndpoint() {
        return this.destEndpoint;
    }

    /**
     * @param destEndpointParam
     *                destination of the link
     */
    public final void setDestEndpoint(final Endpoint destEndpointParam) {
        this.destEndpoint = destEndpointParam;
    }

    /**
     * @return source of the link
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_SourceEndpointTNA")
    public final Endpoint getSourceEndpoint() {
        return this.sourceEndpoint;
    }

    /**
     * @param sourceEndpointParam
     *                source of the link
     */
    public final void setSourceEndpoint(final Endpoint sourceEndpointParam) {
        this.sourceEndpoint = sourceEndpointParam;
    }

    /**
     * @return name of the link
     */
    public final String getName() {
        return this.name;
    }

    /**
     * @param nameParam
     *                name of the link
     */
    public final void setName(final String nameParam) {

        if (nameParam == null) {
            this.name = "";
        } else {
            this.name = nameParam;
        }
    }

    /**
     * @return delay of the link
     */
    @Basic(optional = true)
    public final int getDelay() {
        return this.delay;
    }

    /**
     * @param delayParam
     *                delay of the link
     */
    public final void setDelay(final int delayParam) {
        this.delay = delayParam;
    }

    /**
     * @param costs
     *                the costs to set
     */
    public void setCosts(int costs) {
        this.costs = costs;
    }

    /**
     * @return the costs
     */
    @Basic(optional = true)
    public int getCosts() {
        return this.costs;
    }

    /**
     * @return description of the link
     */
    @Id
    public final String getDescription() {
        return this.description;
    }

    /**
     * @param descriptionParam
     *                description of the link
     */
    public final void setDescription(final String descriptionParam) {
        if (descriptionParam == null) {
            this.description = "";
        } else {
            this.description = descriptionParam;
        }
    }

    /**
     * @param link
     *                to be checked
     * @return true if equal
     */
    public final boolean isEqual(final VIEW_InterDomainLink link) {
    	if(this.hashCode() == link.hashCode()) {
    		return true;
    	}
    	return false;
    }

    /**
     * @param o
     * @return
     */
    @Override
    public final boolean equals(final Object o) {
        if ((o != null) && (o.getClass() == VIEW_InterDomainLink.class)) {
            return isEqual((VIEW_InterDomainLink) o);
        }
        return false;
    }

    @Override
    public final int hashCode() {
        return (this.getName().hashCode()
        		^ this.getDescription().hashCode()
                ^ (Integer.valueOf(getDelay())).hashCode()
                ^ (Integer.valueOf(getCosts())).hashCode());
//                ^ getSourceEndpoint().getName().hashCode()
//                ^ getDestEndpoint().getName().hashCode());
    }

    /**
     * @return copy of the link
     */
    @Transient
    public final VIEW_InterDomainLink getCopy() {
        VIEW_InterDomainLink copy =
                new VIEW_InterDomainLink(this.destEndpoint.getCopy(),
                        this.sourceEndpoint.getCopy(), this.name,
                        this.description, this.delay, this.costs);

        return copy;
    }

    /**
     * @param link
     *                link to compare to
     * @return 1 0 -1
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */

    public final int compareTo(final VIEW_InterDomainLink link) {
        return this.name.compareTo(link.name);
    }

    public eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.Link toJaxb() {
        eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.Link result =
                new eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.Link();
        result.setName(getName());
        result.setDescription(getDescription());
        result.setSourceEndpoint(getSourceEndpoint().getTNA());
        result.setDestinationEndpoint(getDestEndpoint().getTNA());
        return result;
    }

    @SuppressWarnings("unchecked")
    public static final Set<VIEW_InterDomainLink> loadAll()
            throws DatabaseException {
        return (Set<VIEW_InterDomainLink>) (new TransactionManager() {
            @Override
            protected void dbOperation() {
                Set<VIEW_InterDomainLink> result =
                        new HashSet<VIEW_InterDomainLink>();
                final List<VIEW_InterDomainLink> tmpLink =
                        this.session.createCriteria(VIEW_InterDomainLink.class)
                                .setFetchMode("Link", FetchMode.SELECT).list();
                for (VIEW_InterDomainLink l : tmpLink) {
                    result.add(l);
                }
                this.result = result;
            }
        }).getResult();
    }

    /**
     * Load link from the DB.
     */
    @SuppressWarnings("unchecked")
    public static final VIEW_InterDomainLink load(Endpoint source, Endpoint dest)
            throws DatabaseException {
        return (VIEW_InterDomainLink) (new TransactionManager(
                new Tuple<Endpoint, Endpoint>(source, dest)) {
            @Override
            protected void dbOperation() {
                Tuple<Endpoint, Endpoint> ep =
                        (Tuple<Endpoint, Endpoint>) this.arg;
                final List<VIEW_InterDomainLink> tmpLink =
                        this.session.createCriteria(VIEW_InterDomainLink.class)
                                .setFetchMode("", FetchMode.SELECT).add(
                                        Restrictions.like("sourceEndpoint", ep
                                                .getFirstElement())).add(
                                        Restrictions.like("destEndpoint", ep
                                                .getSecondElement())).list();
                if (tmpLink.size() > 0) {
                    this.result = tmpLink.get(0);
                }
            }
        }).getResult();
    }

    /**
     * Load link from the DB.
     */
    public static final VIEW_InterDomainLink load(String sourceTNA,
            String destTNA) throws DatabaseException {
        Endpoint src = Endpoint.load(sourceTNA);
        Endpoint dest = Endpoint.load(destTNA);
        return load(src, dest);
    }

}
