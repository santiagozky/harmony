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


package org.opennaas.core.utils;

import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.TriggeringEventEvaluator;

public class Log4JTriggerEvaluator implements TriggeringEventEvaluator {

    /** timestamp of the last fatal-error event. */
    private long timeLastLogEvent = 0;

    /** error-counter. */
    private int errorCounter = 0;

    /** designated time between tow fatal-error events in MilliSecs.  */
    private long timeBetweenFatals;

    /** designated max error-rate. */
    private int maxErrorRate;

    public Log4JTriggerEvaluator() {
        super();
        this.timeBetweenFatals = Config.getInt("utils", "log4j.timeBetweenFatals").intValue();
        this.maxErrorRate = Config.getInt("utils", "log4j.maxFatalMails").intValue();
    }

    /**
     * checking if an event should be logged or not
     */
    public boolean isTriggeringEvent(LoggingEvent event) {
        if (event.getLevel() == Level.FATAL) {
            // increment fatal-error counter
            this.errorCounter++;

            // first fatal-event ever
            if(this.timeLastLogEvent == 0) {
                this.timeLastLogEvent = event.timeStamp;
            }

            if( ((event.timeStamp - this.timeLastLogEvent) < this.timeBetweenFatals)
                    && this.errorCounter > this.maxErrorRate ) {
                // no mail, if too much failures in too short time-gap
                return false;
            } else if( (event.timeStamp - this.timeLastLogEvent) > this.timeBetweenFatals ) {
                // time-gap between two fatal-errors exceeded
                // -> start new measuring
                this.timeLastLogEvent = event.timeStamp;
                this.errorCounter = 1;
            }
        }
        return true;
    }
}
