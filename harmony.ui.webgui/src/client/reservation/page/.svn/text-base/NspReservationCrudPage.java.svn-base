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


package client.reservation.page;

import java.util.Vector;

import client.classes.nsp.DomainInformationType;
import client.reservation.panel.NspReservationListPanel;
import client.template.page.CrudPage;
import client.template.page.ReservationAdminPage;

/**
 * Class to represent the NSP Reservation Tab-Panel Page.
 * 
 * @author gassen
 */
public class NspReservationCrudPage extends CrudPage {

    private final NspReservationCreatePage createPanel;

    /**
     * Constructor.
     * 
     * @param epr
     *            NSP Endpoint EPR
     */
    public NspReservationCrudPage(final DomainInformationType domain,
            final Vector<DomainInformationType> parents) {
        String epr = null;

        final Vector<PathItem> pathItems = new Vector<PathItem>();

        final PathItem root = new PathItem("Reservation");

        pathItems.add(root);

        if (null != domain) {
            epr = domain.getReservationEPR();

            if (parents != null) {
                for (int x = 0; x < parents.size(); x++) {
                    pathItems.add(new PathItem((DomainInformationType) parents
                            .get(x)));
                }
            }

            final PathItem pathEpr = new PathItem(epr);
            pathItems.add(pathEpr);
        }

        final ReservationAdminPage list =
                new ReservationAdminPage(new NspReservationListPanel(epr));

        this.createPanel = new NspReservationCreatePage(epr);

        this.getTabPanel().add(list, "Admin");
        this.getTabPanel().add(this.createPanel, "Create");

        this.getTabPanel().selectTab(0);

        this.showDetail(pathItems);
    }

    @Override
    protected void handleTabSelect(int tab) { 
        this.createPanel.touch();
    }
}
