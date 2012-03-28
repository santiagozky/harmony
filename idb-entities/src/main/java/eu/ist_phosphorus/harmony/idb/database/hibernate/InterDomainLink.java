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

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.EntityManager;

import com.mysema.query.jpa.impl.JPAQuery;

import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EndpointInterfaceType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EndpointType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.InterdomainLinkType;
import eu.ist_phosphorus.harmony.idb.database.TransactionManager;
import eu.ist_phosphorus.harmony.idb.database.TransactionManagerLoad;
import eu.ist_phosphorus.harmony.idb.exceptions.database.DatabaseException;

/**
 * Java representation of of the database entity {@link InterDomainLink}. This
 * object does not contain any logic.
 * 
 * @author Stephan Wagner (wagners@cs.uni-bonn.de)
 * @version 0.1
 */
@Entity
@Table(name = "InterDomainLink")
public class InterDomainLink implements java.io.Serializable,
		Comparable<InterDomainLink> {

	/**
     *
     */
	private static final long serialVersionUID = 5215854051025689248L;

	/**
	 * Primary key.
	 */
	private long PK_interdomainlink;

	/**
	 * Name of the link.
	 */
	private String linkName;

	/**
	 * Abstract costs of a link.
	 */
	private int linkCosts;

	/**
	 * Source domain of the link.
	 */
	private String sourceDomain;

	/**
	 * Destination domain of the link.
	 */
	private String destinationDomain;

	/**
	 * Source endpoint of the link.
	 */
	private Endpoint sourceEndpoint;

	private boolean disabled;
	private int priority;

	// Constructors

	/** default constructor. */
	public InterDomainLink() {
		// empty
	}

	/**
	 * full constructor.
	 * 
	 * @param linkNameParam
	 *            Name of the link
	 * @param destDomainParam
	 *            Destination domain ID of the link
	 * @param sourceEndpointParam
	 *            Source TNA of the link
	 */
	public InterDomainLink(final String linkName, final String sourceDomain,
			final String destDomain, final Endpoint sourceEndpoint,
			final int costs) {
		super();
		this.linkName = linkName;
		this.sourceDomain = sourceDomain;
		this.destinationDomain = destDomain;
		this.sourceEndpoint = sourceEndpoint;
		this.linkCosts = costs;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getPk_Interdomainlink() {
		return this.PK_interdomainlink;
	}

	public void setPk_Interdomainlink(long pkInterdomainlink) {
		this.PK_interdomainlink = pkInterdomainlink;
	}

	public String getLinkName() {
		return this.linkName;
	}

	/**
	 * @return the disabled
	 */
	public boolean isDisabled() {
		return this.disabled;
	}

	/**
	 * @param disabled
	 *            the disabled to set
	 */
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	/**
	 * @return the priority
	 */
	public int getPriority() {
		return this.priority;
	}

	/**
	 * @param priority
	 *            the priority to set
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	/**
	 * @param linkCosts
	 *            the linkCosts to set
	 */
	public void setLinkCosts(int linkCosts) {
		this.linkCosts = linkCosts;
	}

	/**
	 * @return the linkCosts
	 */
	@Basic(optional = true)
	public int getLinkCosts() {
		return this.linkCosts;
	}

	public final String getSourceDomain() {
		return this.sourceDomain;
	}

	public final void setSourceDomain(final String sourceDomainParam) {
		this.sourceDomain = sourceDomainParam;
	}

	/**
	 * @return destination of the link
	 */
	public final String getDestinationDomain() {
		return this.destinationDomain;
	}

	/**
	 * @param destDomainParam
	 *            destination of the link
	 */
	public final void setDestinationDomain(final String destDomainParam) {
		this.destinationDomain = destDomainParam;
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
	 *            source of the link
	 */
	public final void setSourceEndpoint(final Endpoint sourceEndpointParam) {
		this.sourceEndpoint = sourceEndpointParam;
	}

	/**
	 * @param link
	 *            to be checked
	 * @return true if equal
	 */
	public final boolean isEqual(final InterDomainLink link) {
		if (this.hashCode() == link.hashCode()) {
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
		if ((o != null) && (o.getClass() == InterDomainLink.class)) {
			return isEqual((InterDomainLink) o);
		}
		return false;
	}

	@Override
	public final int hashCode() {
		int result = new Long(this.getPk_Interdomainlink()).hashCode()
				^ ((this.getLinkName() == null) ? 0 : this.getLinkName()
						.hashCode())
				^ ((this.getSourceDomain() == null) ? 0 : this
						.getSourceDomain().hashCode())
				^ ((this.getDestinationDomain() == null) ? 0 : this
						.getDestinationDomain().hashCode())
				^ this.getLinkCosts() ^ this.getPriority()
				^ new Boolean(this.isDisabled()).hashCode();

		// in the underlying objects, don't use the hashCode()-method, because
		// this can end in
		// dependency-circles. Instead only use the DB-primary key for the hash.
		// result ^= ((this.getSourceEndpoint() == null) ? 0 :
		// this.getSourceEndpoint()
		// .getTNA().hashCode());

		return result;
	}

	public final boolean equalsWithoutPK(InterDomainLink l) {
		return this.getLinkName().equals(l.getLinkName())
				&& this.sourceDomain.equals(l.getSourceDomain())
				&& this.destinationDomain.equals(l.getDestinationDomain())
				&& this.isDisabled() == l.isDisabled()
				&& this.getPriority() == l.getPriority()
				&& this.getLinkCosts() == l.getLinkCosts();
	}

	/**
	 * @return copy of the link
	 */
	@Transient
	public final InterDomainLink getCopy() {
		InterDomainLink copy = new InterDomainLink(this.getLinkName(),
				this.getSourceDomain(), this.getDestinationDomain(),
				this.getSourceEndpoint(), this.getLinkCosts());
		copy.setPk_Interdomainlink(getPk_Interdomainlink());
		return copy;
	}

	/**
	 * @param link
	 *            link to compare to
	 * @return 1 0 -1
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public final int compareTo(final InterDomainLink link) {
		if (this.getLinkName().length() < link.getPk_Interdomainlink()) {
			return -1;
		} else if (this.getLinkName().length() == link.getPk_Interdomainlink()) {
			return 0;
		} else {
			return 1;
		}
	}

	public InterdomainLinkType toJaxb() {
		InterdomainLinkType result = new InterdomainLinkType();
		result.setLinkID(getLinkName());
		if (getLinkCosts() > 0) {
			result.setCosts(Integer.valueOf(getLinkCosts()));
		}
		result.setSourceEndpoint(getSourceEndpoint().toJaxb());
		result.setDestinationDomain(getDestinationDomain());
		return result;
	}

	public static InterDomainLink fromJaxb(InterdomainLinkType jaxb,
			String srcDomain) throws DatabaseException {
		InterDomainLink result = new InterDomainLink();
		result.setLinkName(jaxb.getLinkID());
		if (jaxb.isSetCosts()) {
			result.setLinkCosts(jaxb.getCosts().intValue());
		}
		// Endpoint ep =
		// Endpoint.load(jaxb.getSourceEndpoint().getEndpointId());
		EndpointType epJaxb = jaxb.getSourceEndpoint();
		Endpoint ep = Endpoint.fromJaxb(epJaxb);
		if (!epJaxb.isSetDomainId()) {
			ep.setDomain(Domain.load(srcDomain));
		}
		if (!epJaxb.isSetInterface()) {
			ep.setType(EndpointInterfaceType.BORDER.ordinal());
		}
		result.setSourceEndpoint(ep);
		result.setSourceDomain(srcDomain);
		result.setDestinationDomain(jaxb.getDestinationDomain());
		return result;
	}

	/**
	 * Load link from the DB.
	 */
	public static final Set<InterDomainLink> loadForSourceDomain(String domainId)
			throws DatabaseException {
		Set<InterDomainLink> result = new HashSet<InterDomainLink>();
		for (InterDomainLink l : InterDomainLink.loadAll()) {
			if (l.getSourceDomain().equals(domainId)) {
				result.add(l);
			}
		}
		return result;
	}

	/**
	 * Load InterDomainLink from the DB.
	 * 
	 * @param InterDomainLinkId
	 * @throws DatabaseException
	 */
	public static final InterDomainLink load(long linkID)
			throws DatabaseException {
		return (InterDomainLink) (new TransactionManagerLoad(
				InterDomainLink.class, Long.valueOf(linkID))).getResult();
	}

	/**
	 * Load all InterDomainLinks
	 * 
	 * @return
	 * @throws DatabaseException
	 */
	@SuppressWarnings("unchecked")
	public static final Set<InterDomainLink> loadAll() throws DatabaseException {
		return (Set<InterDomainLink>) (new TransactionManager() {
			@Override
			protected void dbOperation() {
				Set<InterDomainLink> result = new HashSet<InterDomainLink>();
				QInterDomainLink interlink = QInterDomainLink.interDomainLink;
				JPAQuery query = new JPAQuery(this.session);

				final List<InterDomainLink> tmpLink = query.from(interlink)
						.list(interlink);

				for (InterDomainLink l : tmpLink) {
					result.add(l);
				}
				this.result = result;
			}
		}).getResult();
	}

	public final void delete() throws DatabaseException {
		new TransactionManager(new HashSet<Object>(
				Arrays.asList(sourceEndpoint))) {
			@Override
			protected void dbOperation() throws Exception {
				delete(this.session);
			}
		};
	}

	public final void delete(EntityManager session) {
		session.remove(this);
	}

	/**
	 * Saves a {@link InterDomainLink} to the DB.
	 * 
	 * @param link
	 *            {@link InterDomainLink} to be saved
	 * @throws DatabaseException
	 *             if entity could not be saved
	 */
	public final void save() throws DatabaseException {
		new TransactionManager(new HashSet<Object>(
				Arrays.asList(sourceEndpoint))) {
			@Override
			protected void dbOperation() throws Exception {
				save(this.session);
			}
		};
	}

	public final void save(EntityManager session) {
		session.persist(this);
	}

	@Override
	public String toString() {
		return "<interdomainlink><name=" + getLinkName() + "/><srcEP>"
				+ this.getSourceEndpoint().toString() + "</srcEP><srcDomain="
				+ getSourceDomain() + "/><dstDomain=" + getDestinationDomain()
				+ "/></interdomainlink>";
	}
}
