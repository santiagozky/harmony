/**
 *
 */
package org.opennaas.extensions.gmpls.handler;

import java.util.Date;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;

import org.opennaas.extensions.gmpls.scheduler.jobs.DbMaintenanceJob;
import org.opennaas.extensions.gmpls.utils.configuration_modules.gmre.GmreCliManager;
import org.opennaas.core.utils.PhLogger;

/**
 * @author Daniel Beer (daniel.beer@iais.fraunhofer.de)
 */
public class StartupServlet implements ServletContextListener {
    private static Scheduler scheduler = null;
    /** Indicates whether this servlet is deployed or not. */
    protected boolean deployed = false;
    private static Logger logger = PhLogger.getLogger();

    /*
     * (non-Javadoc)
     * 
     * @seejavax.servlet.ServletContextListener#contextDestroyed(javax.servlet.
     * ServletContextEvent)
     */
    public void contextDestroyed(
	    @SuppressWarnings("unused") final ServletContextEvent arg0) {

	try {
	    if (null != scheduler) {
		scheduler.shutdown(true);
	    }
	    GmreCliManager.getInstance().finalize();
	} catch (final Throwable e) {
	    e.printStackTrace();
	}
	this.deployed = false;
	logger.warn("GMPLS-WS going down.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.servlet.ServletContextListener#contextInitialized(javax.servlet
     * .ServletContextEvent)
     */
    public void contextInitialized(
	    @SuppressWarnings("unused") final ServletContextEvent arg0) {
	this.deployed = true;
	initScheduler();

	logger.warn("GMPLS-WS started!");

    }

    /**
     * @throws SchedulerException
     */
    private void initScheduler() {
	try {
	    logger.debug("init scheduler");
	    final SchedulerFactory schedulerFactory = new org.quartz.impl.StdSchedulerFactory();
	    scheduler = schedulerFactory.getScheduler();
	    scheduler.start();

	    final JobDetail jobDetail = new JobDetail("GmplsDbMaintenanceJob",
		    null, DbMaintenanceJob.class);

	    final Trigger trigger = TriggerUtils.makeSecondlyTrigger(20);
	    trigger.setStartTime(new Date());
	    trigger.setName("GmplsDbMaintenanceTrigger");

	    scheduler.scheduleJob(jobDetail, trigger);
	} catch (SchedulerException se) {
	    logger.error("init scheduler exception" + se);
	}
    }
}
