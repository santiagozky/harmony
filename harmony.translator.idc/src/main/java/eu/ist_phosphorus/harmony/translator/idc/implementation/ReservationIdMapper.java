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


package eu.ist_phosphorus.harmony.translator.idc.implementation;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import eu.ist_phosphorus.harmony.common.utils.PhLogger;
import eu.ist_phosphorus.harmony.translator.idc.implementation.interfaces.IReservationIdMapper;

public class ReservationIdMapper implements IReservationIdMapper {

    private final Logger logger;

    /**
     * HSI -> IDC
     */
    private static final Map<String, String> HSI2IDC =
            new HashMap<String, String>();

    private static final Map<String, String> IDC2HSI =
            new HashMap<String, String>();

    public ReservationIdMapper() {
        this.logger = PhLogger.getLogger();
    }

    /*
     * (non-Javadoc)
     * @seeeu.ist_phosphorus.harmony.translator.idc.implementation.interfaces.
     * IReservationIdMapper#generateHarmonyId()
     */
    public String generateHarmonyId() {
        return eu.ist_phosphorus.harmony.adapter.dummy.handler.ReservationHandler
                .getInstance().createReservationId();
    }

    /*
     * (non-Javadoc)
     * @seeeu.ist_phosphorus.harmony.translator.idc.implementation.interfaces.
     * IReservationIdMapper#generateIdcId()
     */
    public String generateIdcId() {
        return IDCClient.createReservationId();
    }

    public String harmonyToIdc(final String harmonyId) {
        return this.harmonyToIdc(harmonyId, true);
    }

    /*
     * (non-Javadoc)
     * @seeeu.ist_phosphorus.harmony.translator.idc.implementation.interfaces.
     * IReservationIdMapper#harmonyToItc(java.lang.String)
     */
    public String harmonyToIdc(final String harmonyId,
            final boolean allowGeneration) {
        if (ReservationIdMapper.HSI2IDC.containsKey(harmonyId)) {
            return ReservationIdMapper.HSI2IDC.get(harmonyId);
        }

        if (!allowGeneration) {
            throw new RuntimeException("Unregistered Reservation ID: "
                    + harmonyId + " could not be resolved");
        }

        final String idcId = this.generateIdcId();

        ReservationIdMapper.HSI2IDC.put(harmonyId, idcId);
        ReservationIdMapper.IDC2HSI.put(idcId, harmonyId);

        this.logger.info("Mapping Reservation Id: HSI (" + harmonyId
                + ") <-> IDC (" + idcId + ")");

        return idcId;
    }

    public String idcToHarmony(final String idcId) {
        return this.idcToHarmony(idcId, true);
    }

    /*
     * (non-Javadoc)
     * @seeeu.ist_phosphorus.harmony.translator.idc.implementation.interfaces.
     * IReservationIdMapper#idcToHarmony(java.lang.String)
     */
    public String idcToHarmony(final String idcId, final boolean allowGeneration) {
        if (ReservationIdMapper.IDC2HSI.containsKey(idcId)) {
            return ReservationIdMapper.IDC2HSI.get(idcId);
        }

        if (!allowGeneration) {
            throw new RuntimeException("Unregistered Reservation ID: " + idcId
                    + " could not be resolved");
        }

        final String harmonyId = this.generateHarmonyId();

        ReservationIdMapper.HSI2IDC.put(harmonyId, idcId);
        ReservationIdMapper.IDC2HSI.put(idcId, harmonyId);

        this.logger.info("Mapping Reservation Id: IDC (" + idcId
                + ") <-> HSI (" + harmonyId + ")");

        return harmonyId;
    }

}
