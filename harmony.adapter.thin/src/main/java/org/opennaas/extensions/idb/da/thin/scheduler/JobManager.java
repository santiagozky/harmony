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


/**
 *
 */
package org.opennaas.extensions.idb.da.thin.scheduler;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;

import org.opennaas.extensions.idb.da.thin.persistence.orm.GmplsConnection;
import org.opennaas.extensions.idb.da.thin.scheduler.jobs.DbMaintenanceJob;
import org.opennaas.extensions.idb.da.thin.scheduler.jobs.SetUpJob;
import org.opennaas.extensions.idb.da.thin.scheduler.jobs.TearDownJob;
import org.opennaas.core.utils.PhLogger;

/**
 * @author Stephan Wagner (stephan.wagner@iais.fraunhofer.de), Daniel Beer
 *         (daniel.beer@iais.fraunhofer.de)
 */
public final class JobManager {
    /** */
    private static JobManager instanceJobManager;
    /** */
    private static Scheduler scheduler = null;
    /** */
    private static final int SECURITY_PERIOD = 30;
    /** */
    private static Logger logger = null;

    /**
     * @throws SchedulerException
     */
    private JobManager() throws SchedulerException {
        logger = PhLogger.getLogger(this.getClass());
        SchedulerFactory schedulerFactory =
                new org.quartz.impl.StdSchedulerFactory();
        scheduler = schedulerFactory.getScheduler();
        scheduler.start();

        JobDetail jobDetail =
                new JobDetail("ThinDbMaintenanceJob", null,
                        DbMaintenanceJob.class);

        Trigger trigger = TriggerUtils.makeMinutelyTrigger(2);
        trigger.setStartTime(new Date());
        trigger.setName("ThinDbMaintenanceTrigger");

        scheduler.scheduleJob(jobDetail, trigger);

    }

    /**
     * get singleton instance.
     *
     * @return
     * @throws SchedulerException
     */
    public static JobManager getInstance() throws SchedulerException {
        if (null == instanceJobManager) {
            instanceJobManager = new JobManager();
        }
        return instanceJobManager;
    }

    /**
     * schedule PathCreation.
     *
     * @param gmplsConnection
     * @throws SchedulerException
     */
    public void schedulePathSetUp(final GmplsConnection gmplsConnection)
            throws SchedulerException {
        JobDetail jobDetail =
                new JobDetail(
                        "CreatePathJob" + gmplsConnection.getIdentifier(),
                        Scheduler.DEFAULT_GROUP, SetUpJob.class);
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("request", gmplsConnection);
        jobDetail.setJobDataMap(jobDataMap);
        Calendar cal = Calendar.getInstance();
        cal.setTime(gmplsConnection.getStartTime());
        // cal.add(Calendar.SECOND, -SECURITY_PERIOD / 2);
        logger.info("scheduling path creation at: " + cal.getTime());
        SimpleTrigger trigger =
                new SimpleTrigger("CreatePathTrigger"
                        + gmplsConnection.getIdentifier(),
                        Scheduler.DEFAULT_GROUP, cal.getTime());
        scheduler.scheduleJob(jobDetail, trigger);
    }

    /**
     * schedule PathTermination.
     *
     * @param gmplsConnection
     * @throws SchedulerException
     */
    public void schedulePathTermination(final GmplsConnection gmplsConnection)
            throws SchedulerException {
        JobDetail jobDetail =
                new JobDetail("TerminatePathJob"
                        + gmplsConnection.getIdentifier(),
                        Scheduler.DEFAULT_GROUP, TearDownJob.class);
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("request", gmplsConnection);
        jobDetail.setJobDataMap(jobDataMap);
        Calendar cal = Calendar.getInstance();
        cal.setTime(gmplsConnection.getEndTime());
        cal.add(Calendar.SECOND, -SECURITY_PERIOD);
        logger.info("scheduling path termination at: " + cal.getTime());
        SimpleTrigger trigger =
                new SimpleTrigger("TerminatePathTrigger"
                        + gmplsConnection.getIdentifier(),
                        Scheduler.DEFAULT_GROUP, cal.getTime());
        scheduler.scheduleJob(jobDetail, trigger);
    }

    /**
     * unschedule PathTermination.
     *
     * @param gmplsConnection
     * @throws SchedulerException
     */
    public void unschedulePathTermination(final GmplsConnection gmplsConnection)
            throws SchedulerException {
        scheduler.unscheduleJob("TerminatePathTrigger"
                + gmplsConnection.getIdentifier(), Scheduler.DEFAULT_GROUP);
    }

    /**
     * reschedule Path termination.
     *
     * @param gmplsConnection
     * @throws SchedulerException
     */
    public void reschedulePathTermination(final GmplsConnection gmplsConnection)
            throws SchedulerException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(gmplsConnection.getEndTime());

        logger.info("rescheduling path termination at: " + cal.getTime());

        SimpleTrigger trigger =
                (SimpleTrigger) scheduler.getTrigger("TerminatePathTrigger"
                        + gmplsConnection.getIdentifier(),
                        Scheduler.DEFAULT_GROUP);
        if (trigger != null) {
            trigger.setStartTime(cal.getTime());

            scheduler.rescheduleJob("TerminatePathTrigger"
                    + gmplsConnection.getIdentifier(), Scheduler.DEFAULT_GROUP,
                    trigger);
        }
    }

    /**
     * unschedule PathCreation.
     *
     * @param gmplsConnection
     * @throws SchedulerException
     */
    public void unschedulePathSetUp(final GmplsConnection gmplsConnection)
            throws SchedulerException {
        scheduler.unscheduleJob("CreatePathTrigger"
                + gmplsConnection.getIdentifier(), Scheduler.DEFAULT_GROUP);
    }

    public void shutdown() throws SchedulerException {
        scheduler.shutdown(true);
    }
}
