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

public class PerformanceLogLevel extends Level {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Value of performance log level. This value is lesser than
     * {@link org.apache.log4j.Priority#INFO_INT}
     * and higher than {@link org.apache.log4j.Level#DEBUG_INT}
     */
    public static final int PERFORMANCE_LOG_INT = DEBUG_INT - 10;

    /**
     * {@link Level} representing my log level
     */
    public static final Level PERFORMANCE_LOG = new PerformanceLogLevel(PERFORMANCE_LOG_INT,"PERFORMANCE_LOG",7);

        /**
          * Constructor
           *
          * @param arg0
          * @param arg1
          * @param arg2
           */
        protected PerformanceLogLevel(int arg0, String arg1, int arg2) {
            super(arg0, arg1, arg2);

        }

        /**
         * Checks whether sArg is "PERFORMANCE_LOG" level. If yes then returns
         * {@link PerformanceLogLevel#PERFORMANCE_LOG}, else calls
         * {@link PerformanceLogLevel#toLevel(String, Level)} passing it
         * {@link Level#DEBUG} as the defaultLevel.
          *
          * @see Level#toLevel(java.lang.String)
          * @see Level#toLevel(java.lang.String, org.apache.log4j.Level)
          *
          */
        public static Level toLevel(String sArg) {
            if (sArg != null && sArg.toUpperCase().equals("PERFORMANCE_LOG")) {
                return PERFORMANCE_LOG;
            }
            return toLevel(sArg, Level.DEBUG);
        }

        /**
         * Checks whether val is {@link PerformanceLogLevel#PERFORMANCE_LOG_INT}.
     * If yes then returns {@link PerformanceLogLevel#PERFORMANCE_LOG}, else calls
     * {@link PerformanceLogLevel#toLevel(int, Level)} passing it {@link Level#DEBUG}
     * as the defaultLevel
     *
          * @see Level#toLevel(int)
          * @see Level#toLevel(int, org.apache.log4j.Level)
          *
          */
        public static Level toLevel(int val) {
            if (val == PERFORMANCE_LOG_INT) {
                return PERFORMANCE_LOG;
            }
            return toLevel(val, Level.DEBUG);
        }

        /**
         * Checks whether val is {@link PerformanceLogLevel#PERFORMANCE_LOG_INT}.
         * If yes then returns {@link PerformanceLogLevel#PERFORMANCE_LOG},
         * else calls {@link Level#toLevel(int, org.apache.log4j.Level)}
         *
         * @see Level#toLevel(int, org.apache.log4j.Level)
         */
        public static Level toLevel(int val, Level defaultLevel) {
            if (val == PERFORMANCE_LOG_INT) {
                return PERFORMANCE_LOG;
            }
            return Level.toLevel(val,defaultLevel);
        }

        /**
         * Checks whether sArg is "PERFORMANCE_LOG" level.
     * If yes then returns {@link PerformanceLogLevel#PERFORMANCE_LOG}, else calls
     * {@link Level#toLevel(java.lang.String, org.apache.log4j.Level)}
     *
     * @see Level#toLevel(java.lang.String, org.apache.log4j.Level)
     */
    public static Level toLevel(String sArg, Level defaultLevel) {
           if(sArg != null && sArg.toUpperCase().equals("PERFORMANCE_LOG")) {
               return PERFORMANCE_LOG;
           }
           return Level.toLevel(sArg,defaultLevel);
    }
}
