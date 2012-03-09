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


package client.template.page;

import java.util.Vector;

import client.classes.nsp.DomainInformationType;
import client.helper.PopupOutput;
import client.template.dialog.InfoDialog;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * a CreateReadUpdateDelete-Panel.
 * 
 * @author claus
 */
public abstract class CrudPage extends Composite {

    private CrudTabPanel tabPanel;
    private VerticalPanel vertPanel;

    /**
     * @author gassen
     */
    public class PathItem implements ClickListener {
        private final String itemTitle;
        private String itemDetail = null;

        /**
         * @param title
         */
        public PathItem(final String title) {
            this.itemTitle = title;
        }

        /**
         * @param title
         */
        public PathItem(final DomainInformationType domain) {
            try {
                this.itemDetail = PopupOutput.format(domain);
            } catch (final RuntimeException e) {
                this.itemDetail = "Error generating content: " + e.getMessage();
            }

            this.itemTitle = domain.getDomainId();
        }

        /**
         * @param title
         * @param detail
         */
        public PathItem(final String title, final String detail) {
            this.itemTitle = title;
            this.itemDetail = detail;
        }

        /**
         * @return
         */
        public final HTML toHTML() {
            final HTML result =
                    new HTML("<img src='rightArrow.png'>&nbsp;"
                            + this.itemTitle + "&nbsp;");
            if (this.itemDetail != null) {
                result.addClickListener(this);
                result.addStyleName("gui-Clickable");
            }
            return result;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * com.google.gwt.user.client.ui.ClickListener#onClick(com.google.gwt
         * .user.client.ui.Widget)
         */
        public void onClick(final Widget sender) {
            final InfoDialog dialog =
                    new InfoDialog(this.itemTitle, this.itemDetail);
            dialog.show();
            dialog.center();
        }
    }

    /**
     * @author gassen
     */
    protected final class CrudTabPanel extends TabPanel {
        private final CrudPage owner;

        /**
         * 
         */
        public CrudTabPanel(final CrudPage creator) {
            super();
            this.owner = creator;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * com.google.gwt.user.client.ui.TabPanel#onTabSelected(com.google.gwt
         * .user.client.ui.SourcesTabEvents, int)
         */
        @Override
        public final void onTabSelected(final SourcesTabEvents sender,
                final int tabIndex) {
            super.onTabSelected(sender, tabIndex);
            
            this.owner.handleTabSelect(tabIndex);
        }
    }

    /**
     * default constructor.
     */
    public CrudPage() {
        this.tabPanel = new CrudTabPanel(this);
        this.tabPanel.setWidth("100%");
        this.vertPanel = new VerticalPanel();
        this.vertPanel.add(this.tabPanel);
        this.vertPanel.setWidth("100%");
        this.initWidget(this.vertPanel);
    }

    /**
     * @return
     */
    public TabPanel getTabPanel() {
        return this.tabPanel;
    }

    /**
     * @param tabPanel
     */
    public void setTabPanel(final CrudTabPanel tabPanel) {
        this.tabPanel = tabPanel;
    }

    /**
     * @return
     */
    public VerticalPanel getVertPanel() {
        return this.vertPanel;
    }

    /**
     * @param vertPanel
     */
    public void setVertPanel(final VerticalPanel vertPanel) {
        this.vertPanel = vertPanel;
    }

    /**
     * Override this method to handle ta selections.
     * 
     * @param tab
     */
    protected abstract void handleTabSelect(final int tab);

    /**
     * @param text
     */
    public void showDetail(final PathItem[] text) {
        final HorizontalPanel panel = new HorizontalPanel();
        panel.setStyleName("gui-Pathbar");

        final HorizontalPanel innerPanel = new HorizontalPanel();
        innerPanel.setStyleName("gui-PathbarContent");

        for (int x = 0; x < text.length; x++) {
            innerPanel.add(text[x].toHTML());
        }

        panel.add(innerPanel);

        this.vertPanel.add(panel);
    }

    /**
     * @param text
     */
    public void showDetail(final PathItem text) {
        this.showDetail(new PathItem[] { text });
    }

    /**
     * @param text
     */
    public void showDetail(final Vector<PathItem> text) {
        final PathItem[] items = new PathItem[text.size()];

        for (int x = 0; x < items.length; x++) {
            items[x] = text.get(x);
        }

        this.showDetail(items);
    }

    /**
     * @param text
     */
    public void showDetail(final String text) {
        this.showDetail(new PathItem[] { new PathItem(text) });
    }
}
