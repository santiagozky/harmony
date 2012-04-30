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
@Table(name = "DomSupportedSwitch")
public class DomSupportedSwitch implements java.io.Serializable,
		Comparable<DomSupportedSwitch> {
	/**
	 * 
	 */
	private static long serialVersionUID = -3439390262831916473L;

	/** primary key of the table in the Database. */
	private long PK_Switch;

	/** corresponding domain. */
	private Domain domain;

	/** Technology switch of the domain. */
	private String switchd;

	/**
     *
     */
	public DomSupportedSwitch() {
		// empty
	}

	/**
	 * minimal constructor.
	 */
	public DomSupportedSwitch(Domain domainParam) {
		super();
		this.setDomain(domainParam);
	}

	/**
	 * full constructor.
	 * 
	 * @param domainParam
	 *            corresponding domain
	 * @param switchParam
	 *            technology switch of the domain
	 */
	public DomSupportedSwitch(Domain domainParam, String switchParam) {
		super();
		this.setDomain(domainParam);
		setSwitch(switchParam);
	}

	/**
	 * @return the pkSwitch
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getPK_Switch() {
		return this.PK_Switch;
	}

	/**
	 * @param pkSwitchParam
	 *            the pkSwitch to set
	 */
	public void setPK_Switch(long pkSwitchParam) {
		this.PK_Switch = pkSwitchParam;
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
	 * @return technology switch of the domain
	 */
	@Column(nullable = false, length = 40)
	public String getSwitch() {
		if (this.switchd == null) {
			this.switchd = "";
		}
		return this.switchd;
	}

	/**
	 * @param switchParam
	 *            technology switch of the domain
	 */
	public void setSwitch(String switchParam) {
		this.switchd = switchParam;
	}

	/**
	 * @param domainSwitchParam
	 *            domainSwitch to be compared
	 * @return -1 0 1
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(DomSupportedSwitch domainSwitchParam) {
		if (this.getSwitch().length() < domainSwitchParam.getSwitch().length()) {
			return -1;
		} else if (this.getSwitch().equals(domainSwitchParam.getSwitch())) {
			return 0;
		} else {
			return 1;
		}
	}

	/**
	 * @param domainSwitchParam
	 *            domainSwitch to be checked
	 * @return true if equals
	 */
	public boolean isEqual(DomSupportedSwitch domainSwitchParam) {
		if (this.hashCode() == domainSwitchParam.hashCode()) {
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
		if (o.getClass() == DomSupportedSwitch.class) {
			return isEqual((DomSupportedSwitch) o);
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
		int result = Long.valueOf(this.getPK_Switch()).hashCode()
				^ this.getSwitch().hashCode();
		return result;
	}

	/**
	 * @return copy of domainSwitch
	 */
	@Transient
	public DomSupportedSwitch getCopy() {
		return new DomSupportedSwitch(this.getDomain(), this.getSwitch());
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

	public static DomSupportedSwitch load(long id) throws DatabaseException {
		return (DomSupportedSwitch) (new TransactionManagerLoad(
				DomSupportedSwitch.class, Long.valueOf(id))).getResult();
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

	@SuppressWarnings("unchecked")
	public static Set<DomSupportedSwitch> loadAll(Domain dom)
			throws DatabaseException {
		return (Set<DomSupportedSwitch>) (new TransactionManager(dom) {
			@Override
			protected void dbOperation() {
				Domain d = (Domain) this.arg;
				QDomSupportedSwitch domSwitch = QDomSupportedSwitch.domSupportedSwitch;
				JPAQuery query = new JPAQuery(this.session);
				List<DomSupportedSwitch> sList = query.from(domSwitch)
						.where(domSwitch.domain.eq(d)).list(domSwitch);

				Set<DomSupportedSwitch> switches = new HashSet<DomSupportedSwitch>();
				for (DomSupportedSwitch switchd : sList) {
					switches.add(switchd);
				}
				this.result = switches;
			}
		}).getResult();
	}

	@Override
	public String toString() {
		return "<domainSwitch><switch>" + getSwitch() + "</switch><domain>"
				+ getDomain().getName() + "</domain></domainSwitch>";
	}
}
