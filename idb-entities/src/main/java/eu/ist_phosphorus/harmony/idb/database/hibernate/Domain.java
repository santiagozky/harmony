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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.openjpa.persistence.jdbc.DataStoreIdColumn;

import com.mysema.query.jpa.impl.JPAQuery;

import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.DomainInformationType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.DomainRelationshipType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.DomainTechnologyType;
import eu.ist_phosphorus.harmony.common.utils.Helpers;
import eu.ist_phosphorus.harmony.idb.database.TransactionManager;
import eu.ist_phosphorus.harmony.idb.database.TransactionManagerLoad;
import eu.ist_phosphorus.harmony.idb.exceptions.database.DatabaseException;

/**
 * Java representation of of the database entity {@link Domain}. This object
 * does not contain any logic.
 * 
 * @author Stephan Wagner (wagners@cs.uni-bonn.de)
 * @version 0.1
 */
@Entity
@Table(name = "Domain")
// @Proxy(lazy = false)
public class Domain implements java.io.Serializable, Comparable<Domain> {

	// Fields

	/** */
	private static final long serialVersionUID = 8895516446253440666L;

	/** Name of the domain (primary key of the table in the Database). */
	private String name;

	private int seqNo;

	/** Description of the domain. */
	private String description;

	/** URI of the reservationEPR. */
	private String reservationEPR;

	private Date registered = new Date();

	private URI reservationURI = null;

	/** URI of the topologyEPR. */
	private String topologyEPR;
	/** URI of the notificationEPR. */
	private String notificationEPR;

	private String relationship;
	private int avgDelay;
	private int maxBW;

	private boolean disabled;
	private int priority;

	private Set<DomSupportedAdaption> supportedAdaptions = new HashSet<DomSupportedAdaption>(
			0);
	private Set<DomSupportedSwitch> supportedSwitchMatrix = new HashSet<DomSupportedSwitch>(
			0);
	private Set<DomSupportedBandwidth> supportedBandwidths = new HashSet<DomSupportedBandwidth>(
			0);

	/** Set of prefixes */
	private Set<TNAPrefix> prefixes = new HashSet<TNAPrefix>(0);

	/** Set of endpoints */
	private Set<Endpoint> endpoints = new HashSet<Endpoint>(0);

	/** Set of nrpsConnections */
	private Set<NrpsConnections> nrpsConnections = new HashSet<NrpsConnections>(
			0);

	public Date getRegistered() {
		return this.registered;
	}

	public void setRegistered(final Date registered) {
		this.registered = registered;
	}

	@Basic(optional = true)
	public int getSeqNo() {
		return this.seqNo;
	}

	public void setSeqNo(final int seqNo) {
		this.seqNo = seqNo;
	}

	@Basic(optional = true)
	public String getRelationship() {
		return this.relationship;
	}

	public void setRelationship(final String relationship) {
		this.relationship = relationship;
	}

	@Basic(optional = true)
	public int getAvgDelay() {
		return this.avgDelay;
	}

	public void setAvgDelay(final int avgDelay) {
		this.avgDelay = avgDelay;
	}

	@Basic(optional = true)
	public int getMaxBW() {
		return this.maxBW;
	}

	public void setMaxBW(final int maxBW) {
		this.maxBW = maxBW;
	}

	/**
	 * @return the disabled
	 */
	public int getDisabled() {
		if (this.disabled) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * @param disabled
	 *            the disabled to set
	 */
	public void setDisabled(int disabled) {
		if (disabled == 0) {
			this.disabled = false;
		} else {
			this.disabled = true;
		}
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

	/**
	 * @return the supported technology adaption
	 */
	@OneToMany(mappedBy = "domain", fetch = FetchType.LAZY, cascade = { javax.persistence.CascadeType.ALL })
	@MapKey
	public Set<DomSupportedAdaption> getSupportedAdaptions() {
		return this.supportedAdaptions;
	}

	/**
	 * the technology adaption to set
	 * 
	 * @param adaption
	 */
	public void setSupportedAdaptions(final Set<DomSupportedAdaption> adaption) {
		this.supportedAdaptions = adaption;
	}

	public final boolean addAdaption(final DomSupportedAdaption adaption) {
		adaption.setDomain(this);
		return this.getSupportedAdaptions().add(adaption);
	}

	public final boolean addAdaption(final String adaption) {
		return this.getSupportedAdaptions().add(
				new DomSupportedAdaption(this, adaption));
	}

	/**
	 * @return the supported technology switch matrix
	 */
	@OneToMany(mappedBy = "domain", fetch = FetchType.LAZY, cascade = { javax.persistence.CascadeType.ALL })
	@MapKey
	public Set<DomSupportedSwitch> getSupportedSwitchMatrix() {
		return this.supportedSwitchMatrix;
	}

	/**
	 * the technology switch matrix to set
	 * 
	 * @param switchMatrix
	 */
	public void setSupportedSwitchMatrix(
			final Set<DomSupportedSwitch> switchMatrix) {
		this.supportedSwitchMatrix = switchMatrix;
	}

	public final boolean addSwitch(final DomSupportedSwitch switchParam) {
		switchParam.setDomain(this);
		return this.getSupportedSwitchMatrix().add(switchParam);
	}

	public final boolean addSwitch(final String switchParam) {
		return this.getSupportedSwitchMatrix().add(
				new DomSupportedSwitch(this, switchParam));
	}

	/**
	 * @return the supported bandwidth
	 */
	@OneToMany(mappedBy = "domain", fetch = FetchType.LAZY, cascade = { javax.persistence.CascadeType.ALL })
	@MapKey
	public Set<DomSupportedBandwidth> getSupportedBandwidths() {
		return this.supportedBandwidths;
	}

	/**
	 * the supported bandwidth to set
	 * 
	 * @param bandwidth
	 */
	public void setSupportedBandwidths(final Set<DomSupportedBandwidth> bw) {
		this.supportedBandwidths = bw;
	}

	public final boolean addBandwidth(final DomSupportedBandwidth bw) {
		bw.setDomain(this);
		return this.getSupportedBandwidths().add(bw);
	}

	public final boolean addBandwidth(final long bw) {
		return this.getSupportedBandwidths().add(
				new DomSupportedBandwidth(this, bw));
	}

	// Constructors
	/** default constructor. */
	private Domain() {
		// Nothing to do here...
	}

	/**
	 * minimal Constructor.
	 * 
	 * @param nameParam
	 *            Id of the Domain in the database
	 * @param reservationEPRParam
	 *            URI of the reservationEPR
	 * @param topologyEPRParam
	 *            URI of the topologyEPR
	 * @throws URISyntaxException
	 */
	public Domain(final String nameParam, final String reservationEPRParam,
			final String topologyEPRParam) {
		this.name = nameParam;
		this.description = null;
		this.setReservationEPR(reservationEPRParam);
		this.topologyEPR = topologyEPRParam;
	}

	/**
	 * full constructor.
	 * 
	 * @param nameParam
	 *            name of the Domain
	 * @param descriptionParam
	 *            description of the Domain
	 * @param reservationEPRParam
	 *            URI of the reservationEPR
	 * @param topologyEPRParam
	 *            URI of the topologyEPR
	 * @throws URISyntaxException
	 */
	public Domain(final String nameParam, final String descriptionParam,
			final String reservationEPRParam, final String topologyEPRParam,
			final String notificationEPRParam) {
		this.name = nameParam;
		this.description = descriptionParam;
		this.setReservationEPR(reservationEPRParam);
		this.topologyEPR = topologyEPRParam;
		this.notificationEPR = notificationEPRParam;
	}

	// Property accessors
	/**
	 * @return name
	 */
	@Id
	@Column(name = "name")
	public final String getName() {
		return this.name;
	}

	/**
	 * @param nameParam
	 *            name of the Domain
	 */
	public final void setName(final String nameParam) {
		this.name = nameParam;
	}

	/**
	 * @return description of the Domain
	 */
	@Basic(optional = true)
	public final String getDescription() {
		if (null == this.description) {
			this.description = "";
		}
		return this.description;
	}

	/**
	 * @param descriptionParam
	 *            description of the domain
	 */
	public final void setDescription(final String descriptionParam) {
		this.description = descriptionParam;
	}

	/**
	 * @return the reservationEPR URI
	 */
	public final String getReservationEPR() {
		if (null == this.reservationEPR) {
			this.reservationEPR = "";
		}
		return this.reservationEPR;
	}

	/**
	 * @param reservationEPRParam
	 *            URI of the reservationEPR
	 * @throws URISyntaxException
	 */
	public final void setReservationEPR(final String reservationEPRParam) {
		try {
			this.reservationURI = new URI(reservationEPRParam);
		} catch (final URISyntaxException e) {
			e.printStackTrace();
		}
		this.reservationEPR = reservationEPRParam;
	}

	@Transient
	public final URI getReservationURI() {
		return this.reservationURI;
	}

	/**
	 * Christian: This method now may return null. I might have to fight about
	 * this with Alex, but if this function return an empty string instead of
	 * null, then the empty string is also written to the database; and I am not
	 * sure if an empty string is a valid URI (which is what this field should
	 * be)!
	 * 
	 * @return the topologyEPR URI
	 */
	@Basic(optional = true)
	public final String getTopologyEPR() {
		return this.topologyEPR;
	}

	/**
	 * @param topologyEPRParam
	 *            URI of the topologyEPR
	 */
	public final void setTopologyEPR(final String topologyEPRParam) {
		this.topologyEPR = topologyEPRParam;
	}

	/**
	 * @return the notificationEPR
	 */
	@Basic(optional = true)
	public String getNotificationEPR() {
		return this.notificationEPR;
	}

	/**
	 * @param notificationEPR
	 *            the notificationEPR to set
	 */
	public void setNotificationEPR(final String notificationEPR) {
		this.notificationEPR = notificationEPR;
	}

	/**
	 * @return the prefixes
	 */
	@OneToMany(mappedBy = "domain", fetch = FetchType.LAZY, cascade = { javax.persistence.CascadeType.ALL })
	@MapKey
	public final Set<TNAPrefix> getPrefixes() {
		return this.prefixes;
	}

	/**
	 * @param prefixes
	 *            the prefixes to set
	 */
	public final void setPrefixes(final Set<TNAPrefix> prefixes) {
		this.prefixes = prefixes;
		// TODO: maybe set the domainName here
	}

	public final boolean addPrefix(final TNAPrefix prefix) {
		prefix.setDomain(this);
		return this.getPrefixes().add(prefix);
	}

	public final boolean addPrefix(final String prefix) {
		return this.getPrefixes().add(new TNAPrefix(this, prefix));
	}

	/**
	 * @return the endpoints
	 */
	@OneToMany(mappedBy = "domain", fetch = FetchType.LAZY, cascade = { javax.persistence.CascadeType.REMOVE })
	@MapKey
	public Set<Endpoint> getEndpoints() {
		return this.endpoints;
	}

	/**
	 * @param endpoints
	 *            the endpoints to set
	 */
	public void setEndpoints(final Set<Endpoint> endpoints) {
		this.endpoints = endpoints;
	}

	public final boolean addEndpoint(final Endpoint end) {
		end.setDomain(this);
		return this.getEndpoints().add(end);
	}

	/**
	 * @return the nrpsConnections
	 */
	@OneToMany(mappedBy = "domain", fetch = FetchType.LAZY, cascade = { javax.persistence.CascadeType.REMOVE })
	@MapKey
	public Set<NrpsConnections> getNrpsConnections() {
		return this.nrpsConnections;
	}

	/**
	 * @param nrpsConnections
	 *            the nrpsConnections to set
	 */
	public void setNrpsConnections(final Set<NrpsConnections> nrpsConnections) {
		this.nrpsConnections = nrpsConnections;
	}

	public final boolean addNrpsConnection(final NrpsConnections conn) {
		conn.setDomain(this);
		return this.getNrpsConnections().add(conn);
	}

	/**
	 * @param domainParam
	 *            domain to be checked
	 * @return true if equals
	 */
	@Override
	public final boolean equals(final Object o) {
		if (o.getClass().equals(Domain.class)) {
			return this.isEqual((Domain) o);
		}
		return false;
	}

	/**
	 * @param o
	 * @return
	 */
	public final boolean isEqual(final Domain domainParam) {
		if (this.hashCode() == domainParam.hashCode()) {
			return true;
		}
		return false;
	}

	/**
     *
     */
	@Override
	public final int hashCode() {
		int result = this.getName().hashCode()
				^ new Long(Helpers.trimDateToSeconds(this.getRegistered()))
						.hashCode()
				^ ((this.getReservationEPR() == null) ? 0 : this
						.getReservationEPR().hashCode())
				^ ((this.getTopologyEPR() == null) ? 0 : this.getTopologyEPR()
						.hashCode())
				^ ((this.getNotificationEPR() == null) ? 0 : this
						.getNotificationEPR().hashCode())
				^ ((this.getDescription() == null) ? 0 : this.getDescription()
						.hashCode())
				^ ((this.getRelationship() == null) ? 0 : this
						.getRelationship().hashCode()) ^ this.getSeqNo()
				^ this.getAvgDelay() ^ this.getMaxBW() ^ this.getPriority()
				^ this.getDisabled();

		// in the underlying objects, don't use the hashCode()-method, because
		// this can end in
		// dependency-circles. Instead only use the DB-primary key for the hash.
		// for (NrpsConnections c : this.getNrpsConnections()) {
		// result ^= new Long(c.getPkNrpsConnection()).hashCode();
		// }
		//
		// for(Endpoint e : this.getEndpoints()) {
		// result ^= e.getTNA().hashCode();
		// }
		//
		// for(TNAPrefix p : this.getPrefixes()) {
		// result ^= p.getPrefix().hashCode();
		// }

		return result;
	}

	/**
	 * @return copy of Domain
	 */
	@Transient
	public final Domain getCopy() {
		final Domain newDomain = new Domain();
		newDomain.setName(getName());
		newDomain.setSeqNo(getSeqNo());
		newDomain.setDescription(getDescription());
		newDomain.setRegistered(getRegistered());
		newDomain.setReservationEPR(getReservationEPR());
		newDomain.reservationURI = getReservationURI();
		newDomain.setTopologyEPR(getTopologyEPR());
		newDomain.setNotificationEPR(getNotificationEPR());
		newDomain.setRelationship(getRelationship());
		newDomain.setAvgDelay(getAvgDelay());
		newDomain.setMaxBW(getMaxBW());
		for (final Endpoint e : getEndpoints()) {
			newDomain.getEndpoints().add(e.getCopy(this));
		}
		for (final TNAPrefix p : this.getPrefixes()) {
			newDomain.getPrefixes().add(p.getCopy());
		}
		for (final NrpsConnections s : this.getNrpsConnections()) {
			newDomain.getNrpsConnections().add(s.getCopy());
		}
		for (final DomSupportedAdaption a : this.getSupportedAdaptions()) {
			newDomain.getSupportedAdaptions().add(a);
		}
		for (final DomSupportedSwitch s : this.getSupportedSwitchMatrix()) {
			newDomain.getSupportedSwitchMatrix().add(s);
		}
		for (final DomSupportedBandwidth b : this.getSupportedBandwidths()) {
			newDomain.getSupportedBandwidths().add(b);
		}
		return newDomain;
	}

	public static Domain fromJaxb(final DomainInformationType jaxb) {
		final Domain result = new Domain(jaxb.getDomainId(),
				jaxb.getDescription(), jaxb.getReservationEPR(),
				jaxb.getTopologyEPR(), jaxb.getNotificationEPR());
		if (jaxb.isSetSequenceNumber()) {
			result.setSeqNo(jaxb.getSequenceNumber().intValue());
		}
		if (jaxb.isSetRelationship()) {
			result.setRelationship(jaxb.getRelationship().value());
		}
		if (jaxb.isSetAvgDelay()) {
			result.setAvgDelay(jaxb.getAvgDelay().intValue());
		}
		if (jaxb.isSetMaxBW()) {
			result.setMaxBW(jaxb.getMaxBW().intValue());
		}
		for (final String prefix : jaxb.getTNAPrefix()) {
			result.addPrefix(prefix);
		}
		if (jaxb.isSetTechnology()) {
			DomainTechnologyType dtt = jaxb.getTechnology();
			for (String s : dtt.getDomainSupportedAdaptation()) {
				result.addAdaption(s);
			}
			for (String s : dtt.getDomainSupportedSwitchMatrix()) {
				result.addSwitch(s);
			}
			for (long b : dtt.getDomainSupportedBandwidth()) {
				result.addBandwidth(b);
			}
		}
		return result;
	}

	public void mergeFromJaxb(final DomainInformationType jaxb) {
		this.setName(jaxb.getDomainId());
		if (jaxb.getDescription() != null)
			this.setDescription(jaxb.getDescription());
		this.setReservationEPR(jaxb.getReservationEPR());
		if (jaxb.getTopologyEPR() != null)
			this.setTopologyEPR(jaxb.getTopologyEPR());
		if (jaxb.getNotificationEPR() != null)
			this.setNotificationEPR(jaxb.getNotificationEPR());
		if (jaxb.getSequenceNumber() != null)
			this.setSeqNo(jaxb.getSequenceNumber());
		if (jaxb.getRelationship() != null)
			this.setRelationship(jaxb.getRelationship().value());
		if (jaxb.getAvgDelay() != null)
			this.setAvgDelay(jaxb.getAvgDelay());
		if (jaxb.getMaxBW() != null)
			this.setMaxBW(jaxb.getMaxBW());
		for (String prefix : jaxb.getTNAPrefix()) {
			this.addPrefix(prefix);
		}
		if (jaxb.getTechnology() != null) {
			DomainTechnologyType dtt = jaxb.getTechnology();
			for (String s : dtt.getDomainSupportedAdaptation()) {
				this.addAdaption(s);
			}
			for (String s : dtt.getDomainSupportedSwitchMatrix()) {
				this.addSwitch(s);
			}
			for (long b : dtt.getDomainSupportedBandwidth()) {
				this.addBandwidth(b);
			}
		}
	}

	public DomainInformationType toJaxb() {
		final DomainInformationType result = new DomainInformationType();
		result.setDomainId(this.getName());
		if (this.getSeqNo() > 0) {
			result.setSequenceNumber(new Integer(this.getSeqNo()));
		}
		result.setDescription(this.getDescription());
		result.setReservationEPR(this.getReservationEPR());
		result.setTopologyEPR(this.getTopologyEPR());
		result.setNotificationEPR(this.getNotificationEPR());
		if (this.getRelationship() != null) {
			result.setRelationship(DomainRelationshipType.fromValue(this
					.getRelationship()));
		}
		if (this.getAvgDelay() > 0) {
			result.setAvgDelay(new Integer(this.getAvgDelay()));
		}
		if (this.getMaxBW() > 0) {
			result.setMaxBW(new Integer(this.getMaxBW()));
		}
		for (final TNAPrefix p : this.getPrefixes()) {
			result.getTNAPrefix().add(p.getPrefix());
		}

		Set<DomSupportedAdaption> adaptions = this.getSupportedAdaptions();
		Set<DomSupportedSwitch> matrix = this.getSupportedSwitchMatrix();
		Set<DomSupportedBandwidth> bws = this.getSupportedBandwidths();
		if (!adaptions.isEmpty() || !matrix.isEmpty() || !bws.isEmpty()) {
			DomainTechnologyType dtt = new DomainTechnologyType();
			dtt.getDomainSupportedAdaptation();
			for (final DomSupportedAdaption a : this.getSupportedAdaptions()) {
				dtt.getDomainSupportedAdaptation().add(a.getAdaption());
			}
			dtt.getDomainSupportedSwitchMatrix();
			for (final DomSupportedSwitch s : this.getSupportedSwitchMatrix()) {
				dtt.getDomainSupportedSwitchMatrix().add(s.getSwitch());
			}
			dtt.getDomainSupportedBandwidth();
			for (final DomSupportedBandwidth b : this.getSupportedBandwidths()) {
				dtt.getDomainSupportedBandwidth().add(b.getBandwidth());
			}
			result.setTechnology(dtt);
		}

		return result;
	}

	/**
	 * @param domain
	 *            Domain to be compared to
	 * @return -1 0 or 1
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public final int compareTo(final Domain domain) {
		if (this.getName().length() < domain.getName().length()) {
			return -1;
		} else if (this.getName().length() == domain.getName().length()) {
			return 0;
		} else {
			return 1;
		}
	}

	@Override
	public final String toString() {
		String result = "<domain><name>" + this.getName() + "</name>"
				+ "<seqno>" + this.getSeqNo() + "</seqno>" + "<description>"
				+ this.getDescription() + "</description>" + "<registered>"
				+ this.getRegistered() + "</registered>" + "<reservationEPR>"
				+ this.getReservationEPR() + "</reservationEPR>"
				+ "<topologyEPR>" + this.getTopologyEPR() + "</topologyEPR>"
				+ "<notificationEPR>" + this.getNotificationEPR()
				+ "</notificationEPR>" + "<relationship>"
				+ this.getRelationship() + "</relationship>" + "<avgDelay>"
				+ this.getAvgDelay() + "</avgDelay>" + "<maxBW>"
				+ this.getMaxBW() + "</maxBW>";
		for (final DomSupportedAdaption a : this.getSupportedAdaptions()) {
			result += a.toString();
		}
		for (final DomSupportedSwitch s : this.getSupportedSwitchMatrix()) {
			result += s.toString();
		}
		for (final DomSupportedBandwidth b : this.getSupportedBandwidths()) {
			result += b.toString();
		}
		for (final TNAPrefix p : this.getPrefixes()) {
			result += p.toString();
		}
		for (final Endpoint e : this.getEndpoints()) {
			result += e.toString();
		}
		for (final NrpsConnections con : this.getNrpsConnections()) {
			result += con.toString();
		}
		return result + "</domain>";
	}

	/**
	 * Load domain from the DB. This function does not load the endpoints and
	 * TNA prefixes the belong to the domain - there are additional methods for
	 * doing this!
	 * 
	 * @param domainName
	 *            ID of domain to be retrieved from the database.
	 * @return {@link Domain} for given ID, or null if domain was not found.
	 * @throws DatabaseException
	 */
	public static final Domain load(final String domainName)
			throws DatabaseException {
		return (Domain) (new TransactionManagerLoad(Domain.class, domainName))
				.getResult();
	}

	/**
	 * Locate a TNA address, i.e. match the given address against prefixes of
	 * domains in the database.
	 * 
	 * @param tna
	 *            TNA of endpoint to locate.
	 * @return Domain where the endpoint is located.
	 * @throws DatabaseException
	 * @throws HibernateException
	 */
	@Transient
	@SuppressWarnings("unchecked")
	public static final Domain getDomainMatchingTNA(final String tna)
			throws DatabaseException {
		return (Domain) (new TransactionManager(tna) {
			@Override
			protected void dbOperation() {

				QTNAPrefix qprefix = QTNAPrefix.tNAPrefix;
				JPAQuery query = new JPAQuery(session);
				final List<TNAPrefix> tmpPrefix = query.from(qprefix).list(
						qprefix);

				TNAPrefix match = null;
				for (final TNAPrefix prefix : tmpPrefix) {
					if (prefix.matchesPrefix((String) this.arg)) {
						match = prefix; // TODO longest prefix match
					}
				}
				if (match != null) {
					this.result = this.session.find(Domain.class, match
							.getDomain().getName());
				}
			}
		}).getResult();
	}

	@SuppressWarnings("unchecked")
	public static final Set<Domain> loadAll() throws DatabaseException {
		return (Set<Domain>) (new TransactionManager() {
			@Override
			protected void dbOperation() {
				final Set<Domain> result = new HashSet<Domain>();
				QDomain domain = QDomain.domain;
				JPAQuery query = new JPAQuery(session);
				final List<Domain> tmpDomain = query.from(domain).list(domain);

				for (final Domain d : tmpDomain) {
					result.add(d);
				}
				this.result = result;
			}
		}).getResult();
	}

	@SuppressWarnings("unchecked")
	public final Set<VIEW_InterDomainLink> loadAllLinks()
			throws DatabaseException {
		return (Set<VIEW_InterDomainLink>) (new TransactionManager() {
			@Override
			protected void dbOperation() {
				final Set<VIEW_InterDomainLink> result = new HashSet<VIEW_InterDomainLink>();
				for (final Endpoint e : Domain.this.getEndpoints()) {

					QVIEW_InterDomainLink interlink = QVIEW_InterDomainLink.vIEW_InterDomainLink;
					JPAQuery query = new JPAQuery(session);
					final List<VIEW_InterDomainLink> tmpSrc = query
							.from(interlink)
							.where(interlink.sourceEndpoint.eq(e))
							.list(interlink);
					interlink = QVIEW_InterDomainLink.vIEW_InterDomainLink;
					query = new JPAQuery(session);
					final List<VIEW_InterDomainLink> tmpDst = query
							.from(interlink)
							.where(interlink.destEndpoint.eq(e))
							.list(interlink);

					for (final VIEW_InterDomainLink l : tmpSrc) {
						result.add(l);
					}
					for (final VIEW_InterDomainLink l : tmpDst) {
						result.add(l);
					}
				}
				this.result = result;
			}
		}).getResult();
	}

	public final void save(final EntityManager session) {
		session.persist(this);
	}

	public final void save() throws DatabaseException {
		new TransactionManager() {
			@Override
			protected void dbOperation() {
				save(this.session);
			}
		};
	}

	@Transient
	@SuppressWarnings("unchecked")
	public final Set<Reservation> getAllReservations() throws DatabaseException {
		return (Set<Reservation>) (new TransactionManager(this) {
			@Override
			protected void dbOperation() throws Exception {
				final Domain me = (Domain) this.arg;

				QNrpsConnections nrpsConnection = QNrpsConnections.nrpsConnections;
				JPAQuery query = new JPAQuery(this.session);
				final List<NrpsConnections> cList = query.from(nrpsConnection)
						.where(nrpsConnection.domain.eq(me))
						.list(nrpsConnection);

				final Set<Connections> connections = new HashSet<Connections>();
				for (final NrpsConnections nConn : cList) {
					connections.add(nConn.getConnection());
				}

				// get services for every connection
				final Set<Service> services = new HashSet<Service>();
				for (final Connections c : connections) {
					services.add(c.getService());
				}
				// get reservations for every Service
				final Set<Reservation> reservations = new HashSet<Reservation>();
				for (final Service s : services) {
					reservations.add(s.getReservation());
				}

				this.result = reservations;
			}
		}).getResult();
	}

	public final void delete(final EntityManager session) {

		session.remove(this);
	}

	public final void delete() throws DatabaseException {
		/*
		 * If a domain is deleted, it is useful to delete all topology and
		 * reservation components related with this domain. Endpoints, Links and
		 * Connections are automatically deleted by foreign-key-relation in the
		 * DB. Since the relations between reservations and services and a
		 * domain are not represented in the DB-schema we have to do this
		 * manually. Therefore...
		 */

		List<VIEW_DomainReservationMapping> mappings = VIEW_DomainReservationMapping
				.getMappingsForDomain(this.getName());
		for (VIEW_DomainReservationMapping mapping : mappings) {
			Reservation.load(mapping.getReservationId()).delete();
		}

		new TransactionManager(this) {
			@Override
			protected void dbOperation() throws Exception {
				// delete the domain itself
				delete(this.session);
			}
		};
	}
}
