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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.annotations.Proxy;
import org.hibernate.criterion.Restrictions;

import eu.ist_phosphorus.harmony.idb.database.TransactionManager;
import eu.ist_phosphorus.harmony.idb.database.TransactionManagerLoad;
import eu.ist_phosphorus.harmony.idb.exceptions.database.DatabaseException;
import eu.ist_phosphorus.harmony.common.utils.Tuple;

/**
 * Java representation of of the database entity {@link Subscription}. This object
 * does not contain any logic.
 *
 * @version 0.1
 */

@Entity
@Table(name = "Subscription")
@Proxy(lazy = false)
public class Subscription implements java.io.Serializable, Comparable<Subscription> {

    // Fields

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** subscription Id. */
    private long subscriptionId;

    /** subscription topic. */
    private String subscriptionTopic;

    /** subscriptionEPR. */
    private String subscriptionEPR;

    // Property accessors
    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    public long getSubscriptionId() {
        return this.subscriptionId;
    }

    public void setsubscriptionId(final long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getSubscriptionTopic() {
        return this.subscriptionTopic;
    }

    public void setSubscriptionTopic(final String subscriptionTopic) {
        this.subscriptionTopic = subscriptionTopic;
    }

    public String getSubscriptionEPR() {
        return this.subscriptionEPR;
    }

    public void setSubscriptionEPR(final String subscriptionEPR) {
        this.subscriptionEPR = subscriptionEPR;
    }

    // Constructors
    /** default constructor. */
    public Subscription() {
        // Nothing to do here...
    }

    /**
     * minimal Constructor.
     */
    public Subscription(final String subscriptionTopic, final String subscriptionEPR) {
        this.subscriptionId = 0;
        this.subscriptionTopic = subscriptionTopic;
        this.subscriptionEPR = subscriptionEPR;
    }

    /**
     * full Constructor.
     */
    public Subscription(final long subscriptionId, final String subscriptionTopic,
            final String subscriptionEPR) {
        this.subscriptionId = subscriptionId;
        this.subscriptionTopic = subscriptionTopic;
        this.subscriptionEPR = subscriptionEPR;
    }

    /**
     */
    @Override
    public final boolean equals(final Object o) {
        if (o.getClass() == Subscription.class) {
            return this.isEqual((Subscription) o);
        }
        return false;
    }

    /**
     * @param o
     * @return
     */
    public final boolean isEqual(final Subscription subscriptionParam) {
    	if(this.hashCode() == subscriptionParam.hashCode()) {
    		return true;
    	}
    	return false;
    }

    /**
     *
     */
    @Override
    public final int hashCode() {
        final int result = new Long(this.getSubscriptionId()).hashCode()
                		^ this.getSubscriptionTopic().hashCode()
                		^ this.getSubscriptionEPR().hashCode();
        return result;
    }

    /**
     * @return copy of Subscription
     */
    @Transient
    public final Subscription getCopy() {
        final Subscription newSubscription = new Subscription();
        newSubscription.subscriptionId = this.getSubscriptionId();
        newSubscription.subscriptionTopic = this.getSubscriptionTopic();
        newSubscription.subscriptionEPR = this.getSubscriptionEPR();

        return newSubscription;
    }

    /**
     * @param subscription
     *                Subscription to be compared to
     * @return -1 0 or 1
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public final int compareTo(final Subscription subscription) {
        if (this.subscriptionId < subscription.getSubscriptionId()) {
            return -1;
        } else if (this.subscriptionId == subscription.getSubscriptionId()) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public final String toString() {
        String result =
            "<subscription><id>" + this.getSubscriptionId() + "</id>"
            + "<topic>" + this.getSubscriptionTopic() + "</topic>"
            + "<epr>" + this.getSubscriptionEPR() + "</epr>";
        return result + "</subscription>";
    }

    /**
     */
    public static final Subscription load(final long subscriptionID) throws DatabaseException {
        return (Subscription) (new TransactionManagerLoad(Subscription.class,
                                        new Long(subscriptionID))).getResult();
    }

    public final void save(final Session session) {
        session.saveOrUpdate(this);
    }

    public final void save() throws DatabaseException {
        new TransactionManager() {
            @Override
            protected void dbOperation() {
                Subscription.this.save(this.session);
            }
        };
    }

    public final void delete() throws DatabaseException {
        new TransactionManager(this) {
            @Override
            protected void dbOperation() throws Exception {
                this.session.delete(this.arg);
            }
        };
    }

    @SuppressWarnings("unchecked")
    @Transient
    public static final List<Subscription> getSubscriptionsForTopic(String topic) throws DatabaseException {
        return (List<Subscription>) (new TransactionManager(topic) {
                @Override
                protected void dbOperation() {
                    this.result =
                            this.session.createCriteria(Subscription.class)
                                    .setFetchMode("", FetchMode.SELECT)
                                    .add(
                                            Restrictions.like(
                                                    "subscriptionTopic",
                                                    this.arg)).list();
                }
            }).getResult();
    }

    @SuppressWarnings("unchecked")
    @Transient
    public static final Subscription getSubscriptionForTopicAndEPR(String topic, String epr) throws DatabaseException {
        return (Subscription) (new TransactionManager(new Tuple<String, String>(topic, epr)) {
            @Override
            protected void dbOperation() {
                Tuple<String, String> subscribe = (Tuple<String, String>)this.arg;
                final List<Subscription> tmpSub = this.session
                            .createCriteria(Subscription.class)
                            .setFetchMode("", FetchMode.SELECT)
                            .add(Restrictions.like("subscriptionTopic",
                                            subscribe.getFirstElement()))
                            .add(Restrictions.like("subscriptionEPR",
                                            subscribe.getSecondElement()))
                           .list();
                if(tmpSub.size() > 0) {
                    this.result = tmpSub.get(0);
                }
            }
        }).getResult();
    }

    @SuppressWarnings("unchecked")
    @Transient
    public static final List<String> getAllTopics() throws DatabaseException {
        List<Subscription> tmpSubscriptions =
            (List<Subscription>) (new TransactionManager() {
            @Override
            protected void dbOperation() {
                this.result =
                        this.session.createCriteria(Subscription.class)
                                .setFetchMode("", FetchMode.SELECT).list();
            }
        }).getResult();

        List<String> result = new ArrayList<String>();
        for(Subscription sub : tmpSubscriptions) {
            if(!result.contains(sub.getSubscriptionTopic())) {
                result.add(sub.getSubscriptionTopic());
            }
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    @Transient
    public static final List<Subscription> getAllSubscriptions() throws DatabaseException {
        return (List<Subscription>) (new TransactionManager() {
            @Override
            protected void dbOperation() {
                this.result =
                    this.session.createCriteria(Subscription.class)
                                .setFetchMode("", FetchMode.SELECT).list();
            }
        }).getResult();
    }
}
