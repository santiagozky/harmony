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

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.EntityManager;

import com.mysema.query.jpa.impl.JPAQuery;

import eu.ist_phosphorus.harmony.idb.database.TransactionManager;
import eu.ist_phosphorus.harmony.idb.database.TransactionManagerLoad;
import eu.ist_phosphorus.harmony.idb.exceptions.database.DatabaseException;

/**
 * @author Stephan Wagner (wagners@cs.uni-bonn.de)
 * @version 0.1
 */
@Entity
@Table(name = "TNAPrefix")
public class TNAPrefix implements java.io.Serializable, Comparable<TNAPrefix> {
	/**
     *
     */
	private static final long serialVersionUID = -6525163186402638895L;

	/** corresponding domain. */
	private Domain domain;

	/** Prefix of the domain. (primary key of the table in the Database) */
	private String prefix;

	/**
	 * Base address in numerical representation. This value is generated by
	 * setPrefix().
	 */
	private int baseAddr = 0;

	/**
	 * Length value in numerical representation. This value is generated by
	 * setPrefix().
	 */
	private int len = 0;

	/**
	 * Netmask corresponding to length value in numerical representation. This
	 * value is generated by setPrefix().
	 */
	private int mask = 0;

	/**
     *
     */
	public TNAPrefix() {
		// empty
	}

	/**
	 * minimal constructor.
	 */
	public TNAPrefix(final Domain domainParam) {
		super();
		this.setDomain(domainParam);
	}

	/**
	 * full constructor.
	 * 
	 * @param domainParam
	 *            corresponding domain
	 * @param prefixParam
	 *            prefix of the domain
	 */
	public TNAPrefix(final Domain domainParam, final String prefixParam) {
		super();
		this.setDomain(domainParam);
		setPrefix(prefixParam);
	}

	/**
	 * @return domain
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_domainName")
	public Domain getDomain() {
		return this.domain;
	}

	/**
	 * @param domainParam
	 *            corresponding domain
	 */
	public void setDomain(final Domain domainParam) {
		this.domain = domainParam;
	}

	/**
	 * @return prefix of the domain
	 */
	@Id
	public String getPrefix() {
		if (this.prefix == null) {
			this.prefix = "";
		}
		return this.prefix;
	}

	/**
	 * @param prefixParam
	 *            prefix of the domain
	 */
	public void setPrefix(final String prefixParam) {
		String[] tmpSplit = prefixParam.split("\\/");
		this.baseAddr = Endpoint.ipv4ToInt(tmpSplit[0]);
		this.len = Integer.parseInt(tmpSplit[1]);
		int aux = 0x80000000;
		this.mask = 0;
		for (int i = 0; i < this.len; i++) {
			this.mask |= aux;
			aux >>= 1;
		}
		this.baseAddr &= this.mask;
		this.prefix = "" + Endpoint.ipv4ToString(this.baseAddr) + "/"
				+ this.len;
	}

	/**
	 * @param domainPrefixParam
	 *            domainPrefix to be compared
	 * @return -1 0 1
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(final TNAPrefix domainPrefixParam) {
		if (this.getPrefix().length() < domainPrefixParam.getPrefix().length()) {
			return -1;
		} else if (this.getPrefix().equals(domainPrefixParam.getPrefix())) {
			return 0;
		} else {
			return 1;
		}
	}

	/**
	 * @param domainPrefixParam
	 *            domainPrefix to be checked
	 * @return true if equals
	 */
	public boolean isEqual(final TNAPrefix domainPrefixParam) {
		if (this.hashCode() == domainPrefixParam.hashCode()) {
			return true;
		}
		return false;
	}

	/**
	 * @param o
	 * @return
	 */
	@Override
	public boolean equals(final Object o) {
		if (o.getClass() == TNAPrefix.class) {
			return isEqual((TNAPrefix) o);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int result = this.getPrefix().hashCode();
		// ^ this.getDomain().getName().hashCode();
		return result;
	}

	/**
	 * @return copy of domainPrefix
	 */
	@Transient
	public TNAPrefix getCopy() {
		return new TNAPrefix(this.getDomain(), this.getPrefix());
	}

	public void save(EntityManager session) {
		session.persist(this);
	}

	public void save() throws DatabaseException {
		new TransactionManager(new HashSet<Object>(Arrays.asList(this
				.getDomain()))) {
			@Override
			protected void dbOperation() throws Exception {
				save(this.session);
			}
		};
	}

	public static TNAPrefix load(final String prefix) throws DatabaseException {
		return (TNAPrefix) (new TransactionManagerLoad(TNAPrefix.class, prefix))
				.getResult();
	}

	public void delete() throws DatabaseException {
		new TransactionManager(new HashSet<Object>(Arrays.asList(this
				.getDomain()))) {
			@Override
			protected void dbOperation() throws Exception {
				delete(this.session);
			}
		};
	}

	public void delete(EntityManager session) {
		session.remove(this);
	}

	/**
	 * Match an IPv4 address against this prefix.
	 * 
	 * @param ip
	 *            IPv4 address (String representation) to match against this
	 *            prefix.
	 * @return True if address matches the prefix, false otherwise.
	 */
	public boolean matchesPrefix(final String ip) {
		return matchesPrefix(Endpoint.ipv4ToInt(ip));
	}

	/**
	 * 
	 * Match an IPv4 address against this prefix.
	 * 
	 * @param ip
	 *            IPv4 address (integer representation) to match against this
	 *            prefix.
	 * @return True if address matches the prefix, false otherwise.
	 */
	public boolean matchesPrefix(final int ip) {
		return (this.baseAddr == (ip & this.mask));
	}

	@SuppressWarnings("unchecked")
	public static Set<TNAPrefix> loadAll() throws DatabaseException {
		return (Set<TNAPrefix>) (new TransactionManager() {
			@Override
			protected void dbOperation() {
				Set<TNAPrefix> result = new HashSet<TNAPrefix>();
				QTNAPrefix tnaPrefix = QTNAPrefix.tNAPrefix;
				JPAQuery query = new JPAQuery(this.session);
				final List<TNAPrefix> tmpPrefix = query.from(tnaPrefix).list(
						tnaPrefix);

				for (TNAPrefix d : tmpPrefix) {
					result.add(d);
				}
				this.result = result;
			}
		}).getResult();
	}

	public boolean containsPrefix(TNAPrefix p) {
		return (this.len <= p.len) && matchesPrefix(p.baseAddr);
	}

	@Override
	public String toString() {
		return "<tnaprefix><prefix>" + getPrefix() + "</prefix><domain>"
				+ getDomain().getName() + "</domain></tnaprefix>";
	}
}
