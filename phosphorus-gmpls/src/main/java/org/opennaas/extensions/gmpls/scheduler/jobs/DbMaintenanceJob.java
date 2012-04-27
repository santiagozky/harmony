package org.opennaas.extensions.gmpls.scheduler.jobs;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.PathType;
import org.opennaas.extensions.gmpls.serviceinterface.databinding.jaxb.exceptions.PathNotFoundFaultException;
import org.opennaas.extensions.gmpls.utils.Notifications;
import org.opennaas.extensions.gmpls.utils.configuration_modules.ConfigurationManager;
import org.opennaas.extensions.gmpls.utils.database.DbManager;
import org.opennaas.extensions.gmpls.utils.database.orm.LspInformation;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.StatusType;
import org.opennaas.extensions.idb.serviceinterface.databinding.jaxb.exceptions.UnexpectedFaultException;
import org.opennaas.core.utils.Config;
import org.opennaas.core.utils.PhLogger;

/**
 * @author Stephan Wagner (stephan.wagner@iais.fraunhofer.de)
 */
public class DbMaintenanceJob implements Job {
	private static Logger logger = PhLogger.getLogger();

	/**
	 * Constructor.
	 */
	public DbMaintenanceJob() {
		// nothing to do here
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	public final void execute(final JobExecutionContext context) {
		try {
			logger.debug("GmplsDbMaintenance");
			final List<LspInformation> lspList = DbManager
					.getAllPathInformation();
			for (final LspInformation lspInformation : lspList) {
				logger.debug("Checking lsp: " + lspInformation.getPathId());
				final ConfigurationManager configManager = new ConfigurationManager(
						lspInformation.getSourceDevice().getModule());
				if (!configManager.getStatus(lspInformation)) {
					if ("true".equals(Config.getString("gmpls",
							"auto.reconnect"))) {
						repairConnection(lspInformation, configManager);
					} else {
						cleanConnection(lspInformation, configManager);
					}
				}
			}
			context.setResult("DbMaintenance done at " + new Date().getTime());
		} catch (final UnexpectedFaultException e) {
			context.setResult("Exception: " + e.getMessage());
			logger.error(e.getMessage(), e);
		} catch (final PathNotFoundFaultException e) {
			context.setResult("Exception: " + e.getMessage());
			logger.error(e.getMessage(), e);
		}
	}

	private void repairConnection(LspInformation lspInformation,
			ConfigurationManager configManager) {
		try {
			configManager.terminatePath(lspInformation);
		} catch (final Exception ex) {

			try {
				DbManager
						.insertLog("Could not be deleted on router-> might be already deleted.");
			} catch (UnexpectedFaultException e) {
				e.printStackTrace();
			}
			logger.warn("Could not be deleted on router-> might be already deleted.");
			logger.debug(ex.getMessage(), ex);
		}
		PathType path = new PathType();
		path.setBandwidth(lspInformation.getBandwidth());
		path.setDestinationTNA(lspInformation.getDestinationDevice()
				.getTnaAddress());
		path.setSourceTNA(lspInformation.getSourceDevice().getTnaAddress());

		try {
			configManager.createPath(path, lspInformation.getSourceDevice(),
					lspInformation.getDestinationDevice());
		} catch (UnexpectedFaultException ex) {

			try {
				DbManager.insertLog("Could not repair lsp:"
						+ lspInformation.getLspIndex());
			} catch (UnexpectedFaultException e) {
				e.printStackTrace();
			}
			logger.warn("Could not repair lsp:" + lspInformation.getLspIndex());
			logger.debug(ex.getMessage(), ex);
		}

	}

	private void cleanConnection(LspInformation lspInformation,
			ConfigurationManager configManager)
			throws UnexpectedFaultException, PathNotFoundFaultException {
		DbManager.insertLog("Cleaned lsp with pathId "
				+ lspInformation.getPathId() + " and description: "
				+ lspInformation.getLspDescriptor());
		logger.error("GmplsDbMaintenance");
		logger.error("Cleaned lsp with pathId " + lspInformation.getPathId()
				+ " and description: " + lspInformation.getLspDescriptor());
		DbManager.deletePath(lspInformation.getPathId());
		Notifications.publish(lspInformation.getPathId(),
				StatusType.CANCELLED_BY_SYSTEM);
		Notifications.removeTopic(lspInformation.getPathId());
		try {
			configManager.terminatePath(lspInformation);
		} catch (final Exception ex) {
			DbManager
					.insertLog("Could not be deleted on router-> might be already deleted.");
			logger.warn("Could not be deleted on router-> might be already deleted.");
			logger.debug(ex.getMessage(), ex);
		}

	}

}
