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

package org.opennaas.extensions.idb.database.hibernate;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.mysema.query.jpa.impl.JPAQuery;

import org.opennaas.extensions.idb.database.TransactionManager;
import org.opennaas.extensions.idb.database.TransactionManagerLoad;
import org.opennaas.extensions.idb.exception.database.DatabaseException;

/**
 * @author Stephan Wagner (wagners@cs.uni-bonn.de)
 * @version 0.1
 */
@Entity
@Table(name = "DomSupportedBandwidth")
public class DomSupportedBandwidth implements java.io.Serializable,
		Comparable<DomSupportedBandwidth> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3047229020374814462L;

	/** primary key of the table in the Database. */
	private long PK_Bw;

	/** corresponding domain. */
	private Domain domain;

	/** supported bandwidth of the domain. */
	private long bw;

	/**
     *
     */
	public DomSupportedBandwidth() {
		// empty
	}

	/**
	 * minimal constructor.
	 */
	public DomSupportedBandwidth(Domain domainParam) {
		super();
		this.domain = domainParam;
	}

	/**
	 * full constructor.
	 * 
	 * @param domainParam
	 *            corresponding domain
	 * @param bwParam
	 *            supported bandwidth of the domain
	 */
	public DomSupportedBandwidth(Domain domainParam, long bwParam) {
		super();
		this.domain = domainParam;
		setBandwidth(bwParam);
	}

	/**
	 * @return the pkBw
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getPK_Bandwidth() {
		return this.PK_Bw;
	}

	/**
	 * @param pkBwParam
	 *            the pkBw to set
	 */
	public void setPK_Bandwidth(long pkBwParam) {
		this.PK_Bw = pkBwParam;
	}

	/**
	 * @return domain
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_domainName", nullable = false)
	public Domain getDomain() {
		return this.domain;
	}

	/**
	 * @param domainParam
	 *            corresponding domain
	 */
	public void setDomain(Domain domainParam) {
		this.domain = domainParam;
	}

	/**
	 * @return supported bandwidth of the domain
	 */
	public long getBandwidth() {
		return this.bw;
	}

	/**
	 * @param bwParam
	 *            supported bandwidth of the domain
	 */
	public void setBandwidth(long bwParam) {
		this.bw = bwParam;
	}

	/**
	 * @param domainBwParam
	 *            domainBw to be compared
	 * @return -1 0 1
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(DomSupportedBandwidth domainBwParam) {
		if (this.bw < domainBwParam.getBandwidth()) {
			return -1;
		} else if (this.bw == domainBwParam.getBandwidth()) {
			return 0;
		} else {
			return 1;
		}
	}

	/**
	 * @param domainBwParam
	 *            domainBw to be checked
	 * @return true if equals
	 */
	public boolean isEqual(DomSupportedBandwidth domainBwParam) {
		if (this.hashCode() == domainBwParam.hashCode()) {
			return true;
		}
		return false;
	}

	/**
	 * @param o
	 * @return
	 */
	@Override
	public boolean equals(Object o) {
		if (o.getClass() == DomSupportedBandwidth.class) {
			return isEqual((DomSupportedBandwidth) o);
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
		int result = Long.valueOf(this.getPK_Bandwidth()).hashCode()
				^ Long.valueOf(this.getBandwidth()).hashCode();
		return result;
	}

	/**
	 * @return copy of domainBw
	 */
	@Transient
	public DomSupportedBandwidth getCopy() {
		return new DomSupportedBandwidth(this.getDomain(), this.bw);
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

	public static DomSupportedBandwidth load(long id) throws DatabaseException {
		return (DomSupportedBandwidth) (new TransactionManagerLoad(
				DomSupportedBandwidth.class, Long.valueOf(id))).getResult();
	}

	public void delete() throws DatabaseException {
		new TransactionManager(new HashSet<Object>(Arrays.asList(getDomain()))) {
			@Override
			protected void dbOperation() throws Exception {
				delete(this.session);
			}
		};
	}

	public void delete(EntityManager session) {
		session.remove(this);
	}

	@SuppressWarnings("unchecked")
	public static Set<DomSupportedBandwidth> loadAll(Domain dom)
			throws DatabaseException {
		return (Set<DomSupportedBandwidth>) (new TransactionManager(dom) {
			@Override
			protected void dbOperation() {
				Domain d = (Domain) this.arg;
				QDomSupportedBandwidth domBandwidth = QDomSupportedBandwidth.domSupportedBandwidth;
				JPAQuery query = new JPAQuery(this.session);
				List<DomSupportedBandwidth> bList = query.from(domBandwidth)
						.where(domBandwidth.domain.eq(d)).list(domBandwidth);

				Set<DomSupportedBandwidth> bws = new HashSet<DomSupportedBandwidth>();
				for (DomSupportedBandwidth bw : bList) {
					bws.add(bw);
				}
				this.result = bws;
			}
		}).getResult();
	}

	@Override
	public String toString() {
		return "<domainBw><bw>" + getBandwidth() + "</bw><domain>"
				+ getDomain().getName() + "</domain></domainBw>";
	}
}
