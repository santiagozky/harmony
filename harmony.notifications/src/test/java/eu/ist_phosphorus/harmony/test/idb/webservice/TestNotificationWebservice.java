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


package org.opennaas.extensions.idb.test.webservice;

import org.apache.muse.ws.addressing.soap.SoapFault;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Element;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AddDomainType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AddTopicResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.AddTopicType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ConnectionStatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.DomainInformationType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.EndpointType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetTopicsResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.GetTopicsType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.NotificationMessageType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.PublishResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.PublishType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.RemoveTopicResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.RemoveTopicType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.ServiceStatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.StatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.SubscribeResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.SubscribeType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.UnsubscribeResponseType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.UnsubscribeType;
import org.opennaas.extensions.idb.serviceinterface.databinding.utils.WebserviceUtils;
import org.opennaas.core.utils.Config;
import org.opennaas.core.utils.Helpers;
import org.opennaas.core.utils.NotificationTopic;
import org.opennaas.extensions.idb.database.hibernate.Subscription;

/**
 * JUnit test cases for the IDB Notification webservice.
 * 
 * @author Alexander Zimmermann (zimmerm2@cs.uni-bonn.de)
 * @version $Id: TestNotificationWebservice.java 2593 2008-03-19 10:57:44Z
 *          zimmerm2@cs.uni-bonn.de $
 */
public class TestNotificationWebservice extends AbstractNotificationTest {
    // reference domain
    private static DomainInformationType domainInfoType;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        TestNotificationWebservice.domainInfoType = new DomainInformationType();
        TestNotificationWebservice.domainInfoType.setDomainId("NotificationTestDomain");
        TestNotificationWebservice.domainInfoType.setReservationEPR(Helpers.getRandomString());
        
        AddDomainType addDomainType = new AddDomainType();
        addDomainType.setDomain(TestNotificationWebservice.domainInfoType);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    protected SubscribeType subscribe = null;
    protected UnsubscribeType unsubscribe = null;
    protected AddTopicType addTopic = null;

    protected RemoveTopicType removeTopic = null;
    protected GetTopicsType getTopics = null;

    protected PublishType publish = null;
    protected NotificationTopic topic = null;

    protected NotificationMessageType message = new NotificationMessageType();

    private AddTopicResponseType addTopic(final AddTopicType req)
            throws Exception {
        final Element reqElement = WebserviceUtils.createAddTopicRequest(req);
        final Element responseElement = this.notificationClient
                .addTopic(reqElement);
        final AddTopicResponseType response = WebserviceUtils
                .createAddTopicResponse(responseElement);
        Assert.assertTrue("AddTopic was not successfull!", response != null);
        return response;
    }

    private GetTopicsResponseType getTopics(final GetTopicsType req)
            throws Exception {
        final Element reqElement = WebserviceUtils.createGetTopicsRequest(req);
        final Element responseElement = this.notificationClient
                .getTopics(reqElement);
        final GetTopicsResponseType response = WebserviceUtils
                .createGetTopicsResponse(responseElement);
        return response;
    }

    private PublishResponseType publish(final PublishType req) throws Exception {
        final Element reqElement = WebserviceUtils.createPublishRequest(req);
        final Element responseElement = this.notificationClient
                .publish(reqElement);
        final PublishResponseType response = WebserviceUtils
                .createPublishResponse(responseElement);
        Assert.assertTrue("Publish was not successfull!", response != null);
        return response;
    }

    private RemoveTopicResponseType removeTopic(final RemoveTopicType req)
            throws Exception {
        final Element reqElement = WebserviceUtils
                .createRemoveTopicRequest(req);
        final Element responseElement = this.notificationClient
                .removeTopic(reqElement);
        final RemoveTopicResponseType response = WebserviceUtils
                .createRemoveTopicResponse(responseElement);
        Assert.assertTrue("RemoveTopic was not successfull!", response != null);
        return response;
    }

    /**
     * Setup before a test.
     * 
     * @throws java.lang.Exception
     *             An exception
     */
    @Before
    public final void setUp() throws Exception {
        this.topic = new NotificationTopic(TestNotificationWebservice.domainInfoType
                .getDomainId(), 666);

        this.addTopic = new AddTopicType();
        this.addTopic.setTopic(this.topic.toString());

        this.removeTopic = new RemoveTopicType();
        this.removeTopic.setTopic(this.topic.toString());

        this.subscribe = new SubscribeType();
        this.subscribe.setConsumerReference(Config.getString("test",
                "epr.reservation"));
        this.subscribe.setTopic(this.topic.toString());

        this.unsubscribe = new UnsubscribeType();
        this.unsubscribe.setConsumerReference(Config.getString("test",
                "epr.reservation"));
        this.unsubscribe.setTopic(this.topic.toString());

        final ServiceStatusType serviceStatus = new ServiceStatusType();
        serviceStatus.setServiceID(666);
        serviceStatus.setStatus(StatusType.ACTIVE);

        final ConnectionStatusType connType = new ConnectionStatusType();
        connType.setConnectionID(666);
        connType.setStatus(StatusType.ACTIVE);
        connType.setActualBW(666);
        connType.setDirectionality(0);
        EndpointType source = new EndpointType();
        source.setEndpointId("128.0.0.1");
        connType.setSource(source);
        EndpointType target = new EndpointType();
        target.setEndpointId("128.0.0.1");
        connType.getTarget().add(target);
        serviceStatus.getConnections().add(connType);

        this.message.setServiceStatus(serviceStatus);

        this.publish = new PublishType();
        this.publish.setTopic(this.topic.toString());
        this.publish.getNotificationList().add(this.message);

        this.getTopics = new GetTopicsType();

    }

    private SubscribeResponseType subscribe(final SubscribeType req)
            throws Exception {
        final Element reqElement = WebserviceUtils.createSubscribeRequest(req);
        final Element responseElement = this.notificationClient
                .subscribe(reqElement);
        final SubscribeResponseType response = WebserviceUtils
                .createSubscribeResponse(responseElement);
        Assert
                .assertTrue("Subscribe was not successfull!", response
                        .isResult());
        return response;
    }


    @Test(timeout = 5 * 60 * 1000)
    public final void testNotificationWorkflow() throws Exception {
        System.out.print("test AddTopic ...");
        final AddTopicResponseType addTopicResponse = this
                .addTopic(this.addTopic);
        Assert.assertTrue("addTopic-Response should be true", addTopicResponse
                .isResult());
        System.out.println(" passed!");

        System.out.print("test GetTopics ...");
        final GetTopicsResponseType GetTopicsResponse = this
                .getTopics(this.getTopics);
        Assert.assertTrue(
                "getTopics-Response should contain the previous topic",
                GetTopicsResponse.getTopics().contains(this.topic.toString()));
        System.out.println(" passed!");

        System.out.print("test Subscribe ...");
        final SubscribeResponseType SubscribeResponse = this
                .subscribe(this.subscribe);
        Assert.assertTrue("subscribe-Response should be true",
                SubscribeResponse.isResult());
        System.out.println(" passed!");

        System.out.print("test Notify ...");
        final PublishResponseType NotifyResponse = this.publish(this.publish);
        Assert.assertTrue("notify-Response should be true", NotifyResponse
                .isResult());
        System.out.println(" passed!");

        System.out.print("test Unsubscribe ...");
        final UnsubscribeResponseType UnsubscribeResponse = this
                .unsubscribe(this.unsubscribe);
        Assert.assertTrue("unsubscribe-Response should be true",
                UnsubscribeResponse.isResult());
        System.out.println(" passed!");

        System.out.print("test RemoveTopic ...");
        final RemoveTopicResponseType RemoveTopicResponse = this
                .removeTopic(this.removeTopic);
        Assert.assertTrue("removeTopic-Response should be true",
                RemoveTopicResponse.isResult());
        System.out.println(" passed!");

        System.out.print("test GetTopics is empty ...");
        final GetTopicsResponseType GetTopicsResponse2 = this
                .getTopics(this.getTopics);
        Assert
                .assertTrue(
                        "getTopics-Response should not contain the previous topic anymore",
                        !GetTopicsResponse2.getTopics().contains(this.topic));
        System.out.println(" passed!");

        System.out.print("test FalseSubscription ...");
        try {
            this.subscribe(this.subscribe);
        } catch (final SoapFault f) {
            Assert.assertTrue(f.getMessage()
                    .contains(this.subscribe.getTopic()));
            System.out.println(" passed!");
        }

        System.out.print("test FalsePublish ...");
        try {
            this.publish(this.publish);
        } catch (final SoapFault f) {
            Assert.assertTrue(f.getMessage()
                    .contains(this.subscribe.getTopic()));
            System.out.println(" passed!");
        }

        System.out.print("test FalseUnsubscription ...");
        try {
            this.unsubscribe(this.unsubscribe);
        } catch (final SoapFault f) {
            Assert.assertTrue(f.getMessage()
                    .contains(this.subscribe.getTopic()));
            System.out.println(" passed!");
        }
    }

    @Test(timeout = 5 * 60 * 1000)
    public final void testSubscriptionInDB() throws Exception {
        final AddTopicResponseType addTopicResponse = this
                .addTopic(this.addTopic);
        Assert.assertTrue("addTopic-response should be true", addTopicResponse
                .isResult());

        final SubscribeResponseType SubscribeResponse = this
                .subscribe(this.subscribe);
        Assert.assertTrue("subscribe-Response should be true",
                SubscribeResponse.isResult());
        Thread.sleep(2000);

        // only check DB-subscription if persisting is activated
        if (Config.isTrue("idb", "persistingSubscriptions")) {
            final Subscription sub = Subscription
                    .getSubscriptionForTopicAndEPR(this.topic.toString(),
                            this.subscribe.getConsumerReference());
            Assert.assertNotNull("there should be a subscription in DB", sub);
        }

        final UnsubscribeResponseType unsubscribeResponse = this
                .unsubscribe(this.unsubscribe);
        Assert.assertTrue("unsubscribe-Response should be true",
                unsubscribeResponse.isResult());

        // wait one second, because thread on webservice-side is not fast enough
        Thread.sleep(2000);

        // only check DB-subscription if persisting is activated
        if (Config.isTrue("idb", "persistingSubscriptions")) {
            final Subscription sub = Subscription
                    .getSubscriptionForTopicAndEPR(this.topic.toString(),
                            this.subscribe.getConsumerReference());
            Assert
                    .assertNull(
                            "there should not be a subscription for this topic and epr in DB anymore",
                            sub);
        }

        final RemoveTopicResponseType removeTopicResponse = this
                .removeTopic(this.removeTopic);
        Assert.assertTrue("removeTopic-Response should be true",
                removeTopicResponse.isResult());
    }

    private UnsubscribeResponseType unsubscribe(final UnsubscribeType req)
            throws Exception {
        final Element reqElement = WebserviceUtils
                .createUnsubscribeRequest(req);
        final Element responseElement = this.notificationClient
                .unsubscribe(reqElement);
        final UnsubscribeResponseType response = WebserviceUtils
                .createUnsubscribeResponse(responseElement);
        Assert.assertTrue("Unsubscribe was not successfull!", response
                .isResult());
        return response;
    }
}
