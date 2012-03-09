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
package client.template.page;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author gassen
 * 
 */
public class DefaultPage extends Composite {

    /**
     * Creates a default page with title + describing text
     */
    public DefaultPage(final String title, final String text) {
        final FlexTable table = new FlexTable();

        final HorizontalPanel hPanel = new HorizontalPanel();
        hPanel.setStyleName("gui-DefaultPage");

        hPanel.add(new Image("phosphoruslogo.png"));

        final VerticalPanel vPanel = new VerticalPanel();

        final Label titleLabel = new Label(title);
        titleLabel.setStyleName("gui-DefaultPage-Title");
        vPanel.add(titleLabel);

        final HTML ruler = new HTML("<hr />");
        ruler.setStyleName("gui-DefaultPage-Ruler");
        vPanel.add(ruler);

        final Label textLabel = new Label(text);
        textLabel.setStyleName("gui-DefaultPage-Text");
        vPanel.add(textLabel);

        hPanel.add(vPanel);

        table.setWidget(0, 0, hPanel);
        table.setWidth("100%");
        table.setHeight("100%");
        table.getCellFormatter().setAlignment(0, 0,
                HasHorizontalAlignment.ALIGN_CENTER,
                HasVerticalAlignment.ALIGN_MIDDLE);

        this.initWidget(table);
    }

}
