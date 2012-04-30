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

import javax.persistence.Column;
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
@Table(name = "DomSupportedAdaption")
public class DomSupportedAdaption implements java.io.Serializable,
		Comparable<DomSupportedAdaption> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3616530788509681620L;

	/** primary key of the table in the Database. */
	private long PK_Adaption;

	/** corresponding domain. */
	private Domain domain;

	/** Technology adaption of the domain. */
	private String adaption;

	/**
     *
     */
	public DomSupportedAdaption() {
		// empty
	}

	/**
	 * minimal constructor.
	 */
	public DomSupportedAdaption(Domain domainParam) {
		super();
		this.domain = domainParam;
	}

	/**
	 * full constructor.
	 * 
	 * @param domainParam
	 *            corresponding domain
	 * @param adaptionParam
	 *            technology adaption of the domain
	 */
	public DomSupportedAdaption(Domain domainParam, String adaptionParam) {
		super();
		this.domain = domainParam;
		setAdaption(adaptionParam);
	}

	/**
	 * @return the pkAdaption
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getPK_Adaption() {
		return this.PK_Adaption;
	}

	/**
	 * @param pkAdaptionParam
	 *            the pkAdaption to set
	 */
	public void setPK_Adaption(long pkAdaptionParam) {
		this.PK_Adaption = pkAdaptionParam;
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
	 * @return technology adaption of the domain
	 */
	@Column(length = 40, nullable = false)
	public String getAdaption() {
		if (this.adaption == null) {
			this.adaption = "";
		}
		return this.adaption;
	}

	/**
	 * @param adaptionParam
	 *            technology adaption of the domain
	 */
	public void setAdaption(String adaptionParam) {
		this.adaption = adaptionParam;
	}

	/**
	 * @param domainAdaptionParam
	 *            domainAdaption to be compared
	 * @return -1 0 1
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(DomSupportedAdaption domainAdaptionParam) {
		if (this.getAdaption().length() < domainAdaptionParam.getAdaption()
				.length()) {
			return -1;
		} else if (this.getAdaption().equals(domainAdaptionParam.getAdaption())) {
			return 0;
		} else {
			return 1;
		}
	}

	/**
	 * @param domainAdaptionParam
	 *            domainAdaption to be checked
	 * @return true if equals
	 */
	public boolean isEqual(DomSupportedAdaption domainAdaptionParam) {
		if (this.hashCode() == domainAdaptionParam.hashCode()) {
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
		if (o.getClass() == DomSupportedAdaption.class) {
			return isEqual((DomSupportedAdaption) o);
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
		int result = Long.valueOf(this.getPK_Adaption()).hashCode()
				^ this.getAdaption().hashCode();
		return result;
	}

	/**
	 * @return copy of domainAdaption
	 */
	@Transient
	public DomSupportedAdaption getCopy() {
		return new DomSupportedAdaption(this.getDomain(), this.getAdaption());
	}

	public void save(EntityManager session) {
		session.persist(this);
	}

	public void save() throws DatabaseException {
		new TransactionManager(new HashSet<Object>(Arrays.asList(getDomain()))) {
			@Override
			protected void dbOperation() throws Exception {
				save(this.session);
			}
		};
	}

	public static DomSupportedAdaption load(long id) throws DatabaseException {
		return (DomSupportedAdaption) (new TransactionManagerLoad(
				DomSupportedAdaption.class, Long.valueOf(id))).getResult();
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
	public static Set<DomSupportedAdaption> loadAll(Domain dom)
			throws DatabaseException {
		return (Set<DomSupportedAdaption>) (new TransactionManager(dom) {
			@Override
			protected void dbOperation() {
				Domain d = (Domain) this.arg;
				QDomSupportedAdaption domAdaption = QDomSupportedAdaption.domSupportedAdaption;
				JPAQuery query = new JPAQuery(this.session);
				List<DomSupportedAdaption> aList = query.from(domAdaption)
						.where(domAdaption.domain.eq(d)).list(domAdaption);

				Set<DomSupportedAdaption> adaptions = new HashSet<DomSupportedAdaption>();
				for (DomSupportedAdaption adaption : aList) {
					adaptions.add(adaption);
				}
				this.result = adaptions;
			}
		}).getResult();
	}

	@Override
	public String toString() {
		return "<domainAdaption><pk>" + getPK_Adaption() + "</pk><adaption>"
				+ getAdaption() + "</adaption><domain>" + getDomain().getName()
				+ "</domain></domainAdaption>";
	}
}
