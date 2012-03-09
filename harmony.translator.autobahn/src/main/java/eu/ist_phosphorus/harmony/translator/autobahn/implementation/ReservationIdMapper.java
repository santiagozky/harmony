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


package eu.ist_phosphorus.harmony.translator.autobahn.implementation;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import eu.ist_phosphorus.harmony.common.utils.PhLogger;
import eu.ist_phosphorus.harmony.translator.autobahn.implementation.interfaces.IReservationIdMapper;

public class ReservationIdMapper implements IReservationIdMapper {

    private final Logger logger;

    /**
     * HSI -> AutoBAHN
     */
    private static final Map<String, String> HSI2AutoBAHN =
            new HashMap<String, String>();

    private static final Map<String, String> AutoBAHN2HSI =
            new HashMap<String, String>();

    public ReservationIdMapper() {
        this.logger = PhLogger.getLogger();
    }

    /*
     * (non-Javadoc)
     * @seeeu.ist_phosphorus.harmony.translator.autobahn.implementation.interfaces.
     * IReservationIdMapper#generateHarmonyId()
     */
    public String generateHarmonyId() {
        return eu.ist_phosphorus.harmony.adapter.dummy.handler.ReservationHandler
                .getInstance().createReservationId();
    }

    /*
     * (non-Javadoc)
     * @seeeu.ist_phosphorus.harmony.translator.autobahn.implementation.interfaces.
     * IReservationIdMapper#generateAutobahnId()
     */
    public String generateAutobahnId() {
        return AutoBAHNClient.createReservationId();
    }

    public String harmonyToAutobahn(final String harmonyId) {
        return this.harmonyToAutobahn(harmonyId, true);
    }

    /*
     * (non-Javadoc)
     * @seeeu.ist_phosphorus.harmony.translator.autobahn.implementation.interfaces.
     * IReservationIdMapper#harmonyToItc(java.lang.String)
     */
    public String harmonyToAutobahn(final String harmonyId,
            final boolean allowGeneration) {
        if (ReservationIdMapper.HSI2AutoBAHN.containsKey(harmonyId)) {
            return ReservationIdMapper.HSI2AutoBAHN.get(harmonyId);
        }

        if (!allowGeneration) {
            throw new RuntimeException("Unregistered Reservation ID: "
                    + harmonyId + " could not be resolved");
        }

        final String autobahnId = this.generateAutobahnId();

        ReservationIdMapper.HSI2AutoBAHN.put(harmonyId, autobahnId);
        ReservationIdMapper.AutoBAHN2HSI.put(autobahnId, harmonyId);

        this.logger.info("Mapping Reservation Id: HSI (" + harmonyId
                + ") <-> AutoBAHN (" + autobahnId + ")");

        return autobahnId;
    }

    public String autobahnToHarmony(final String autobahnId) {
        return this.autobahnToHarmony(autobahnId, true);
    }

    /*
     * (non-Javadoc)
     * @seeeu.ist_phosphorus.harmony.translator.autobahn.implementation.interfaces.
     * IReservationIdMapper#autobahnToHarmony(java.lang.String)
     */
    public String autobahnToHarmony(final String autobahnId, final boolean allowGeneration) {
        if (ReservationIdMapper.AutoBAHN2HSI.containsKey(autobahnId)) {
            return ReservationIdMapper.AutoBAHN2HSI.get(autobahnId);
        }

        if (!allowGeneration) {
            throw new RuntimeException("Unregistered Reservation ID: " + autobahnId
                    + " could not be resolved");
        }

        final String harmonyId = this.generateHarmonyId();

        ReservationIdMapper.HSI2AutoBAHN.put(harmonyId, autobahnId);
        ReservationIdMapper.AutoBAHN2HSI.put(autobahnId, harmonyId);

        this.logger.info("Mapping Reservation Id: AutoBAHN (" + autobahnId
                + ") <-> HSI (" + harmonyId + ")");

        return harmonyId;
    }

}
