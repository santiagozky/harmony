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
package client.argon.page;

import client.reservation.panel.NspFixedReservationCreatePanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ImageBundle;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author gassen
 * 
 */
public class ArgonReservationCreatePage extends Composite {

    /**
     * 
     */
    private DockPanel dockPanel = new DockPanel();

    /**
     * 
     */
    private static final DemoImage demoImage =
            (DemoImage) GWT.create(DemoImage.class);

    /**
     * @author gassen
     * 
     */
    public interface DemoImage extends ImageBundle {

        @Resource("public/testbed.jpg")
        AbstractImagePrototype demoImage();
    }

    /**
     * @return
     */
    private final Widget getImagePanel() {
        final Image logo =
                ArgonReservationCreatePage.demoImage.demoImage().createImage();

        final VerticalPanel demoPanel = new VerticalPanel();
        demoPanel.add(logo);

        return demoPanel;
    }

    /**
     * @return
     */
    private final Widget getEditPanel() {
        final VerticalPanel outerPanel = new VerticalPanel();

        NspFixedReservationCreatePanel panel =
                new NspFixedReservationCreatePanel(null);
        
        panel.setArgon(true);

        outerPanel.add(panel);

        return outerPanel;
    }

    /**
     * 
     */
    public ArgonReservationCreatePage() {
        this.dockPanel.add(this.getImagePanel(), DockPanel.NORTH);
        this.dockPanel.add(this.getEditPanel(), DockPanel.SOUTH);

        this.initWidget(this.dockPanel);
        this.setStyleName("mail-List");
    }

    /**
     * @return the dockPanel
     */
    public final DockPanel getDockPanel() {
        return this.dockPanel;
    }

    /**
     * @param dockPanel
     *            the dockPanel to set
     */
    public final void setDockPanel(final DockPanel dockPanel) {
        this.dockPanel = dockPanel;
    }

    /**
     * @return the demoImage
     */
    public static final DemoImage getDemoImage() {
        return ArgonReservationCreatePage.demoImage;
    }
}
