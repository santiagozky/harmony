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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.EntityManager;

import org.apache.openjpa.kernel.jpql.JPQL;

import com.mysema.query.jpa.impl.JPAQuery;

import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EndpointInterfaceType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EndpointType;
import eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.exceptions.EndpointNotFoundFaultException;
import eu.ist_phosphorus.harmony.idb.database.TransactionManager;
import eu.ist_phosphorus.harmony.idb.database.TransactionManagerLoad;
import eu.ist_phosphorus.harmony.idb.exceptions.database.DatabaseException;

/**
 * @author dewaal
 */
/**
 * Java representation of of the database entity {@link Endpoint}. This object
 * does not contain any logic.
 * 
 * @author Stephan Wagner (wagners@cs.uni-bonn.de)
 * @version 0.1
 */
@Entity
@Table(name = "Endpoint")
public class Endpoint implements java.io.Serializable, Comparable<Endpoint> {

	// Fields

	/**
     *
     *
     */
	private static final long serialVersionUID = -3486326025963475471L;

	/**
	 * TNA of the endpoint.
	 */
	private String TNA;

	/**
	 * name of the endpoint.
	 */
	private String name;

	/**
	 * description of the endpoint.
	 */
	private String description;
	/**
	 * id of the Type.
	 */
	private int type;

	/**
	 * bandwidth of the endpoint.
	 */
	private int bandwidth;

	/**
	 * Connections that belong to the endpoint.
	 */
	private Map<String, Connections> connections = new HashMap<String, Connections>(
			0);

	private Map<String, Connections> endpointInConnections = new HashMap<String, Connections>(
			0);

	private Set<InterDomainLink> interDomainLinks = new HashSet<InterDomainLink>(
			0);

	private boolean disabled;
	private int priority;

	private Domain domain;

	// Constructors

	/** default constructor. */
	public Endpoint() {
		// empty
	}

	/**
	 * minimal constructor.
	 * 
	 * @param tnaParam
	 *            TNA of the endpoint
	 */
	public Endpoint(final String tnaParam, final Domain domain,
			final int typeParam) {
		super();
		setTNA(tnaParam);
		this.name = null;
		this.description = null;
		this.domain = domain;
		this.type = typeParam;
		this.bandwidth = 0;
	}

	/**
	 * full constructor.
	 * 
	 * @param TNAParam
	 *            TNA of the endpoint
	 * @param nameParam
	 *            name of the endpoint
	 * @param descriptionParam
	 *            description of the endpoint
	 * @param fkDomainIdParam
	 *            Id or corresponding Domain
	 * @param typeParam
	 *            type of endpoint
	 * @param bandwidthParam
	 *            bandwidth of the endpoint
	 */
	public Endpoint(final String TNAParam, final String nameParam,
			final String descriptionParam, final Domain domain,
			final int typeParam, final int bandwidthParam) {
		setTNA(TNAParam);
		this.name = nameParam;
		this.description = descriptionParam;
		this.domain = domain;
		this.type = typeParam;
		this.bandwidth = bandwidthParam;
	}

	/**
	 * full constructor.
	 * 
	 * @param TNAParam
	 *            TNA of the endpoint
	 * @param nameParam
	 *            name of the endpoint
	 * @param descriptionParam
	 *            description of the endpoint
	 * @param fkDomainIdParam
	 *            Id or corresponding Domain
	 * @param typeParam
	 *            type of endpoint
	 * @param bandwidthParam
	 *            bandwidth of the endpoint
	 */
	public Endpoint(final String TNAParam, final String nameParam,
			final String descriptionParam, final int typeParam,
			final int bandwidthParam,
			final HashMap<String, Connections> connectionsParam) {
		setTNA(TNAParam);
		this.name = nameParam;
		this.description = descriptionParam;
		this.type = typeParam;
		this.bandwidth = bandwidthParam;
		this.connections = connectionsParam;
	}

	public Endpoint(String tna, String name, String description, int type,
			int bandwidth, boolean disabled, int priority) {
		super();
		this.TNA = tna;
		this.name = name;
		this.description = description;
		this.type = type;
		this.bandwidth = bandwidth;
		this.disabled = disabled;
		this.priority = priority;
	}

	public Endpoint(String tna, String name, String description, int type,
			int bandwidth, boolean disabled, int priority, Domain d) {
		this(tna, name, description, type, bandwidth, disabled, priority);
		this.setDomain(d);
	}

	public final void set(Endpoint e) {
		this.setTNA(e.getTNA());
		this.setName(e.getName());
		this.setDescription(e.getDescription());
		this.setDomain(e.getDomain());
		this.setType(e.getType());
		this.setBandwidth(e.getBandwidth());
	}

	// Property accessors

	/**
	 * @return TNA of endpoint
	 */

	@Id
	public String getTNA() {
		return this.TNA;
	}

	/**
	 * @param tnaParam
	 *            TNA of endpoint
	 */
	public void setTNA(final String tnaParam) {
		this.TNA = normalizeIPv4(tnaParam);
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

	/**
	 * @return name of endpoint
	 */
	@Basic(optional = true)
	public String getName() {
		if (null == this.name) {
			this.name = "";
		}
		return this.name;
	}

	/**
	 * @param nameParam
	 *            name of endpoint
	 */
	public void setName(final String nameParam) {
		this.name = nameParam;
	}

	/**
	 * @return bandwidth of endpoint
	 */
	@Basic(optional = true)
	public int getBandwidth() {
		return this.bandwidth;
	}

	/**
	 * @param bandwidthParam
	 *            bandwidth of endpoint
	 */
	public void setBandwidth(final int bandwidthParam) {
		this.bandwidth = bandwidthParam;
	}

	/**
	 * @return type of endpoint
	 */
	public int getType() {
		return this.type;
	}

	/**
	 * @param type
	 *            type of endpoint
	 */
	public void setType(final int type) {
		this.type = type;
	}

	/**
	 * @return description of endpoint
	 */
	@Basic(optional = true)
	public String getDescription() {
		if (null == this.description) {
			this.description = "";
		}

		return this.description;
	}

	/**
	 * @param descriptionParam
	 *            description of endpoint
	 */
	public void setDescription(final String descriptionParam) {
		this.description = descriptionParam;
	}

	/**
	 * @return the domain
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fkDomainName")
	public Domain getDomain() {
		return this.domain;
	}

	/**
	 * @param domain
	 *            the domain to set
	 */
	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	/**
	 * @return the endpointInConnections
	 */
	@ManyToMany(mappedBy = "endpoints", targetEntity = Connections.class)
	public Map<String, Connections> getEndpointInConnections() {
		return this.endpointInConnections;
	}

	/**
	 * @param endpointInConnections
	 *            the endpointInConnections to set
	 */
	public void setEndpointInConnections(
			Map<String, Connections> endpointInConnections) {
		this.endpointInConnections = endpointInConnections;
	}

	@OneToMany(mappedBy = "startpoint", fetch = FetchType.LAZY, cascade = { javax.persistence.CascadeType.REMOVE })
	@MapKey(name = "connectionId")
	public Map<String, Connections> getConnections() {
		return this.connections;
	}

	/**
	 * Retrieve connection with a specific ID.
	 * 
	 * @param connectionId
	 *            Connection ID of requested connection.
	 * @return Connection with the given Connection ID, or null if not found.
	 */
	@Transient
	public Connections getConnection(final int connectionId) {
		return getConnections().get(String.valueOf(connectionId));
	}

	/**
	 * @param connections
	 *            the prefixes to set
	 */
	public void setConnections(Map<String, Connections> connections) {
		this.connections = connections;
	}

	/**
	 * Add a connection to this endpoint.
	 * 
	 * @param connection
	 *            connection to be added.
	 */
	public void addConnection(final Connections connectionParam) {
		connectionParam.setStartpoint(this);
		getConnections().put(String.valueOf(connectionParam.getConnectionId()),
				connectionParam);
	}

	@OneToMany(mappedBy = "sourceEndpoint", fetch = FetchType.LAZY, cascade = { javax.persistence.CascadeType.REMOVE })
	public Set<InterDomainLink> getInterDomainLinks() {
		return this.interDomainLinks;
	}

	/**
	 * @param interDomainLinks
	 *            to set
	 */
	public void setInterDomainLinks(Set<InterDomainLink> links) {
		this.interDomainLinks = links;
	}

	/**
	 * Add a interDomainLink to this endpoint.
	 * 
	 * @param interDomainLink
	 *            to be added.
	 */
	public void addInterDomainLink(final InterDomainLink link) {
		link.setSourceEndpoint(this);
		getInterDomainLinks().add(link);
	}

	/**
	 * @param endpoint
	 *            endpoint to be checked
	 * @return true if equal
	 */
	public final boolean isEqual(final Endpoint endpoint) {
		if (this.hashCode() == endpoint.hashCode()) {
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
		if (o.getClass() == Endpoint.class) {
			return isEqual((Endpoint) o);
		}
		return false;
	}

	@Override
	public final int hashCode() {
		// till the mappings are correct check only domain-intern attributes
		// no underlying objects
		int result = this.getTNA().hashCode()
				^ ((this.getName() == null) ? 0 : this.getName().hashCode())
				^ ((this.getDescription() == null) ? 0 : this.getDescription()
						.hashCode())
				^ new Integer(this.getBandwidth()).hashCode()
				^ new Integer(this.getType()).hashCode()
				^ new Integer(this.getPriority()).hashCode()
				^ new Boolean(this.isDisabled()).hashCode();

		// in the underlying objects, don't use the hashCode()-method, because
		// this can end in
		// dependency-circles. Instead only use the DB-primary key for the hash.
		// result ^= ((this.getDomain() == null) ? 0 :
		// this.getDomain().getName().hashCode());
		//
		// for(Connections c : this.getEndpointInConnections().values()) {
		// result ^= new Long(c.getPK_Connections()).hashCode();
		// }
		//
		// for(Connections c : this.getConnections().values()) {
		// result ^= new Long(c.getPK_Connections()).hashCode();
		// }

		return result;
	}

	/**
	 * @return copy of endpoint
	 */
	@Transient
	public Endpoint getCopy() {
		return new Endpoint(this.getTNA(), this.getName(),
				this.getDescription(), this.getType(), this.getBandwidth(),
				this.isDisabled(), this.getPriority());
	}

	/**
	 * @return copy of endpoint
	 */
	@Transient
	public Endpoint getCopy(Domain d) {
		return new Endpoint(this.getTNA(), this.getName(),
				this.getDescription(), this.getType(), this.getBandwidth(),
				this.isDisabled(), this.getPriority(), d);
	}

	/**
	 * @param endpoint
	 *            endpoint to be compared
	 * @return -1 0 1
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public final int compareTo(final Endpoint endpoint) {
		if (Endpoint.ipv4ToInt(this.getTNA()) < Endpoint.ipv4ToInt(endpoint
				.getTNA())) {
			return -1;
		} else if (Endpoint.ipv4ToInt(this.getTNA()) == Endpoint
				.ipv4ToInt(endpoint.getTNA())) {
			return 0;
		} else {
			return 1;
		}
	}

	/**
	 * Create JAXB EndpointType object with the data contained in this object.
	 * 
	 * @return JAXB EndpointType object with the data contained in this object.
	 */
	public eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EndpointType toJaxb() {
		eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EndpointType result = new eu.ist_phosphorus.harmony.common.serviceinterface.databinding.jaxb.EndpointType();
		result.setEndpointId(this.getTNA());
		result.setName(this.getName());
		result.setDescription(this.getDescription());
		if (getType() == 0) {
			result.setInterface(EndpointInterfaceType.USER);
		} else if (getType() == 1) {
			result.setInterface(EndpointInterfaceType.BORDER);
		}
		result.setDomainId(getDomain().getName());
		result.setBandwidth(Integer.valueOf(getBandwidth()));
		return result;
	}

	/**
	 * Try to load the endpoint with the given TNA from the database. If no such
	 * endpoint is stored in the database, the domain the given TNA is located
	 * in is found by prefix matching and a new user endpoint object with the
	 * given TNA is created, stored to the database. If no matching domain is
	 * found, returns null.
	 * 
	 * @throws DatabaseException
	 * @throws EndpointNotFoundFaultException
	 */
	public static final Endpoint loadOrCreateUserEndpoint(String tna)
			throws DatabaseException, EndpointNotFoundFaultException {
		return loadOrCreateEndpoint(tna, EndpointInterfaceType.USER);
	}

	public static final Endpoint loadOrCreateEndpoint(String tna,
			EndpointInterfaceType type) throws DatabaseException,
			EndpointNotFoundFaultException {
		Endpoint e = Endpoint.load(tna);
		if (e != null) {
			return e;
		}
		Domain domain = Domain.getDomainMatchingTNA(tna);
		if (domain == null) {
			throw new EndpointNotFoundFaultException("cannot locate " + tna);
		}

		// return new Endpoint(tna, domain, type.ordinal());
		e = new Endpoint(tna, domain, type.ordinal());
		e.save();
		return e;
	}

	/**
	 * Convert IPv4 address represented as String into an int value.
	 * 
	 * @param ipStr
	 *            String representation of IPv4 address.
	 * @return Integer representation of input IPv4 address.
	 */
	public static int ipv4ToInt(final String ipStr) {
		int result = 0;
		String[] s = ipStr.split("\\.");
		if (s.length != 4) {
			throw new RuntimeException("malformed IPv4 address: " + ipStr); // TODO
			// own
			// exception?
		}
		for (int i = 0; i < s.length; i++) {
			int b = Integer.parseInt(s[i]);
			if ((b < 0) || (b > 255)) {
				throw new RuntimeException("malformed IPv4 address: " + ipStr); // TODO
				// own
				// exception?
			}
			result = (result << 8) | b;
		}
		return result;
	}

	/**
	 * Convert IPv4 address represented as int into an String value.
	 * 
	 * @param ipInt
	 * @return
	 */
	public static String ipv4ToString(final int ipInt) {
		return "" + ((ipInt >> 24) & 0xff) + "." + ((ipInt >> 16) & 0xff) + "."
				+ ((ipInt >> 8) & 0xff) + "." + (ipInt & 0xff);
	}

	/**
	 * Helping method for normalizing an IPv4-String.
	 * 
	 * @param addr
	 *            IPv4-address
	 * @return String normalized IPv4-address
	 */
	public static String normalizeIPv4(final String addr) {
		return ipv4ToString(ipv4ToInt(addr));
	}

	@Override
	public final String toString() {
		return "<tna>" + getTNA() + "</tna><name>" + getName()
				+ "</name><description" + getDescription()
				+ "</description><domainName>" + getDomain().getName()
				+ "</domainName><type>" + getType() + "</type><bandwidth>"
				+ getBandwidth() + "</bandwidth>";
	}

	/**
	 * Create Endpoint object from JAXB input.
	 * 
	 * @param epJaxb
	 *            JAXB input
	 * @return Endpoint object created from JAXB input.
	 * @throws DatabaseException
	 */
	public static final Endpoint fromJaxb(EndpointType epJaxb)
			throws DatabaseException {
		Endpoint result = new Endpoint();
		result.setTNA(epJaxb.getEndpointId());

		if (epJaxb.isSetDomainId()) {
			result.setDomain(Domain.load(epJaxb.getDomainId()));
		} else {
			result.setDomain(null);
		}
		if (epJaxb.isSetName()) {
			result.setName(epJaxb.getName());
		} else {
			result.setName(null);
		}
		if (epJaxb.isSetBandwidth()) {
			result.setBandwidth(epJaxb.getBandwidth().intValue());
		} else {
			result.setBandwidth(0);
		}

		if (epJaxb.isSetInterface()) {
			result.setType(epJaxb.getInterface().ordinal());
		} else {
			result.setType(0);
		}
		if (epJaxb.isSetDescription()) {
			result.setDescription(epJaxb.getDescription());
		} else {
			result.setDescription(null);
		}
		return result;
	}

	public void mergeFromJaxb(final EndpointType jaxb) throws DatabaseException {
		this.setTNA(jaxb.getEndpointId());

		if (jaxb.isSetDomainId()) {
			this.setDomain(Domain.load(jaxb.getDomainId()));
		} else {
			this.setDomain(null);
		}
		if (jaxb.isSetName()) {
			this.setName(jaxb.getName());
		} else {
			this.setName(null);
		}
		if (jaxb.isSetBandwidth()) {
			this.setBandwidth(jaxb.getBandwidth().intValue());
		} else {
			this.setBandwidth(0);
		}

		if (jaxb.isSetInterface()) {
			this.setType(jaxb.getInterface().ordinal());
		} else {
			this.setType(0);
		}
		if (jaxb.isSetDescription()) {
			this.setDescription(jaxb.getDescription());
		} else {
			this.setDescription(null);
		}
	}

	/**
	 * Saves one {@link Endpoint} to the DB. Fails if no domainID has been
	 * specified for the {@link Endpoint}.
	 * 
	 * @param endpoint
	 *            Instance of an {@link Endpoint} to be saved
	 * @throws DatabaseException
	 *             if entity could not be saved
	 */
	public final void save() throws DatabaseException {
		HashSet<Object> refresh = new HashSet<Object>(
				Arrays.asList(getDomain()));
		refresh.addAll(getConnections().values());
		refresh.addAll(getEndpointInConnections().values());
		refresh.addAll(getInterDomainLinks());
		new TransactionManager(refresh) {
			@Override
			protected void dbOperation() throws Exception {
				save(this.session);
			}
		};
	}

	public final void save(EntityManager session) {
		session.persist(this);
	}

	/**
	 * Gets all information for a given {@link Endpoint} from the DB.
	 * 
	 * @param endpointId
	 *            ID of endpoint to be retrieved from the database.
	 * @return {@link Endpoint} for given ID
	 * @throws DatabaseException
	 * @throws HibernateException
	 */
	public static final Endpoint load(final String endpointId)
			throws DatabaseException {
		return (Endpoint) (new TransactionManagerLoad(Endpoint.class,
				endpointId)).getResult();
	}

	@SuppressWarnings("unchecked")
	public static final Set<Endpoint> loadAll() throws DatabaseException {
		return (Set<Endpoint>) (new TransactionManager() {
			@Override
			protected void dbOperation() {
				Set<Endpoint> result = new HashSet<Endpoint>();
				QEndpoint endpoint = QEndpoint.endpoint;
				JPAQuery query = new JPAQuery(this.session);
				final List<Endpoint> tmpEndpoint = query.from(endpoint).list(
						endpoint);

				for (Endpoint d : tmpEndpoint) {
					result.add(d);
				}
				this.result = result;
			}
		}).getResult();
	}

	@SuppressWarnings("unchecked")
	public final Set<VIEW_InterDomainLink> loadLinks() throws DatabaseException {
		return (Set<VIEW_InterDomainLink>) (new TransactionManager(this) {
			@Override
			protected void dbOperation() {
				final Set<VIEW_InterDomainLink> result = new HashSet<VIEW_InterDomainLink>();
				QVIEW_InterDomainLink interlink = QVIEW_InterDomainLink.vIEW_InterDomainLink;
				JPAQuery query = new JPAQuery(this.session);
				final List<VIEW_InterDomainLink> tmpSrc = query
						.from(interlink)
						.where(interlink.sourceEndpoint.eq((Endpoint) this.arg))
						.list(interlink);

				final List<VIEW_InterDomainLink> tmpDst = query.from(interlink)
						.where(interlink.destEndpoint.eq((Endpoint) this.arg))
						.list(interlink);

				for (final VIEW_InterDomainLink l : tmpSrc) {
					result.add(l);
				}
				for (final VIEW_InterDomainLink l : tmpDst) {
					result.add(l);
				}
				this.result = result;
			}
		}).getResult();
	}

	public final void delete(EntityManager session) {
		session.remove(this);
	}

	/**
	 * Deletes this {@link Endpoint} from the DB.
	 * 
	 * @throws DatabaseException
	 */
	public final void delete() throws DatabaseException {
		HashSet<Object> refresh = new HashSet<Object>(
				Arrays.asList(getDomain()));
		refresh.addAll(getConnections().values());
		refresh.addAll(getEndpointInConnections().values());
		refresh.addAll(getInterDomainLinks());
		new TransactionManager(refresh) {
			@Override
			protected void dbOperation() throws Exception {
				delete(this.session);
			}
		};
	}
}
