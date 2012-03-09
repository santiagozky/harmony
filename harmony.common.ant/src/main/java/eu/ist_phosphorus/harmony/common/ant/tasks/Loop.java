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

package eu.ist_phosphorus.harmony.common.ant.tasks;

import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.TaskContainer;

public class Loop extends Task implements TaskContainer {
	private final List<Task> tasks = new ArrayList<Task>();
	private String csl = null;

	public final void setCommaSeperatedList(final String token) {
		this.csl = token;
	}

	@Override
	public void execute() throws BuildException {
		if (null == this.csl) {
			throw new BuildException(
					"You need to specify a Comma speperated list",
					this.getLocation());
		}

		final String[] token = this.csl.split(",");

		for (String rawItem : token) {
			String item = rawItem.trim();

			for (Task task : this.tasks) {
				task.getProject().setProperty("val", item);

				task.init();
				task.perform();
			}
		}
	}

	public void addTask(Task task) {
		this.tasks.add(task);
	}
}
