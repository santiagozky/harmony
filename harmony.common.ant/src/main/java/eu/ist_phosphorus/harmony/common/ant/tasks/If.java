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

import org.apache.tools.ant.Task;
import org.apache.tools.ant.TaskContainer;

public class If extends Task implements TaskContainer {
    private final List<Task> tasks = new ArrayList<Task>();
    private String property = null;
    private boolean negation = false;

    public final void setProperty(String prop) {
        this.property = prop;
    }

    public final void setNegation(boolean prop) {
        this.negation = prop;
    }

    public void addTask(Task arg0) {
        this.tasks.add(arg0);
    }

    public final void execute() {
        if ((!this.negation && null != this.getProject().getProperty(
                this.property))
                || (this.negation && null == this.getProject().getProperty(
                        this.property))) {
            for (Task task : this.tasks) {
                task.init();
                task.perform();
            }
        }
    }

}
