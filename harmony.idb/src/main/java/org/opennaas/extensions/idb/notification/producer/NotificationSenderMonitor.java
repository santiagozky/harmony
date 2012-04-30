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


package org.opennaas.extensions.idb.notification.producer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.NotificationMessageType;
import org.opennaas.core.utils.Config;
import org.opennaas.core.utils.PerformanceLogLevel;
import org.opennaas.core.utils.PhLogger;

public class NotificationSenderMonitor extends Thread {

    /**
     * waiting thread. waits till timeout, then interrupts the
     * NotificationSenderMonitor
     */
    public class WaitingThread extends Thread {
        private final NotificationSenderMonitor monitor;
        private final int localTimeout;

        public WaitingThread(final int timeout,
                final NotificationSenderMonitor monitor) {
            this.localTimeout = timeout;
            this.monitor = monitor;
        }

        @Override
        public void run() {
            // wait for timeout
            try {
                Thread.sleep(this.localTimeout);
            } catch (final InterruptedException e) {
                // nothing here, interrupted from monitor thread
            }
            // interrupt the monitor thread
            this.monitor.interrupt();
        }
    }

    /** Lock. */
    private final Lock lock;

    /** lock-condition. */
    private final Condition pause;

    /**
     * timeout. After this time the monitor should interrupt all still running
     * sender-threads
     */
    private final int timeout;

    /**
     * number of trials to send the notification.
     */
    private int sendTrials;

    /**
     * max sending trials.
     */
    private final int maxSendTrials;

    /**
     * list of sender-threads.
     */
    private ArrayList<NotificationSender> senderList;

    /**
     * Constructor for the notification monitor.
     * 
     * @param timeout
     *            in sec
     */
    public NotificationSenderMonitor(final int timeout) {
        this.timeout = timeout * 1000;
        this.senderList = new ArrayList<NotificationSender>();

        this.lock = new ReentrantLock();
        this.pause = this.lock.newCondition();

        this.maxSendTrials = Config.getInt("idb", "maxNotificationRetries")
                .intValue();
        this.sendTrials = 1;
    }

    /**
     * deregister method for finished notificationSender.
     */
    public void deregisterSender(final NotificationSender sender) {
        this.senderList.remove(sender);
        // check if all sender-threads are finished
        if (this.senderList.isEmpty()) {
            this.lock.lock();
            // resume monitor thread
            this.pause.signal();
            this.lock.unlock();
        }
    }

    /**
     * method to create a new NotificationSender Thread out of a old one
     * 
     * @param oldSender
     *            old NotificationSender Thread
     * @return new copy of the NotificationSender Thread
     */
    private NotificationSender getSenderCopy(final NotificationSender oldSender) {
        final String epr = oldSender.getEpr();
        final String topic = oldSender.getTopic();
        final List<NotificationMessageType> messages = oldSender.getMessage();
        final boolean messageTypeFlag = oldSender.getMessageTypeFlag();

        return new NotificationSender(epr, topic, messages, messageTypeFlag,
                this);
    }

    /**
     * register method for new notificationSender.
     */
    public void registerSender(final NotificationSender sender) {
        this.senderList.add(sender);
    }

    @Override
    public void run() {
        // boolean if distribution finished
        boolean isFinished = false;

        final long startTime = System.currentTimeMillis();

        while (!isFinished) {
            // create new waiter thread
            final WaitingThread waiter = new WaitingThread(this.timeout, this);

            // start all registered threads
            for (final NotificationSender sender : this.senderList) {
                sender.start();
            }
            // start the waiting thread for timeout
            waiter.start();

            // wait till all threads are finished or timeout occurs
            this.lock.lock();
            try {
                // wait till senderList is empty
                // -> thread will continue after this command if resumed
                this.pause.await();
                // stop loop
                isFinished = true;
                // waiter thread not needed anymore -> stop it
                waiter.interrupt();
            } catch (final InterruptedException e) {
                this.lock.unlock();
                // exception from waiter thread -> timeout exceeded
                if (this.sendTrials <= this.maxSendTrials) {
                    // not enough trials -> restart the still running threads
                    final ArrayList<NotificationSender> newSenderList = new ArrayList<NotificationSender>();
                    // create new NotificationSenders and put them in the
                    // internal list
                    for (final NotificationSender sender : this.senderList) {
                        newSenderList.add(this.getSenderCopy(sender));
                        // interrupt old sender-threads
                        sender.interrupt();
                    }
                    this.senderList = newSenderList;
                    // increment the trial-counter
                    this.sendTrials++;
                } else {
                    // third send-trial timeout -> stop all still running
                    // sender-Threads
                    final Logger logger = PhLogger.getLogger(this.getClass());
                    logger.error("monitorThread timeout! "
                            + "Stopping all still running senderThreads! "
                            + "Not all notifications could be delivered!");
                    for (final NotificationSender sender : this.senderList) {
                        sender.interrupt();
                    }
                    // stop loop
                    isFinished = true;
                }
            }
            this.lock.unlock();
        }

        final long endTime = System.currentTimeMillis();

        final Logger performanceLogger = PhLogger.getLogger("Performance");
        performanceLogger.log(PerformanceLogLevel.PERFORMANCE_LOG,
                "Overall time for notification-distribution: "
                        + (endTime - startTime) + " MilliSecs");
    }
}
